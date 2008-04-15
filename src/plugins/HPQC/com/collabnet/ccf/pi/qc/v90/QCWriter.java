package com.collabnet.ccf.pi.qc.v90;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.collabnet.ccf.pi.qc.v90.QCEntityService;
import com.collabnet.ccf.pi.sfee.v44.SFEEDBHelper;

import com.collabnet.ccf.core.ga.GenericArtifactAttachment;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;

public class QCWriter extends QCConnectHelper implements
		IDataProcessor {

	private static final Log log = LogFactory.getLog(QCWriter.class);
	private String CreateToken;
	private QCDefectHandler defectHandler;	

    public QCWriter(String id) {
	    super(id);
	}

	private Object[] processXMLDocument(Object data) {
		
		Document genericArtifactDocument = (Document) data;
		GenericArtifact genericArtifact = getArtifactFromDocument(genericArtifactDocument);
		Document resultDoc = null;
		int bugId=0;
		Boolean doesBugIdExistsInQC = false;
		//Populate the sourceArtifactId into each of the GenericArtifacts from the QCEntityService
		//This operation is done in a separate component called QCEntityService. So, the input flowing into this file is already populated
				
		connectProperly();
		
		List<GenericArtifactField> allFields = genericArtifact.getAllGenericArtifactFields();
		List<GenericArtifactAttachment> allAttachments = genericArtifact.getAllGenericArtifactAttachments();
		if(allFields!=null)
			genericArtifact = concatValuesOfSameFieldNames(genericArtifact);
		allFields = genericArtifact.getAllGenericArtifactFields();
		GenericArtifact.ArtifactActionValue artifactAction = genericArtifact.getArtifactAction();
		String stringBugId = getFieldValueFromGenericArtifact(genericArtifact, "BG_BUG_ID");
		log.info("The bugId coming in is :"+stringBugId);
		
		String sourceArtifactId = genericArtifact.getSourceArtifactId();
		String sourceSystemId  = genericArtifact.getSourceSystemId();
		String sourceSystemKind = genericArtifact.getSourceSystemKind();
		String sourceRepositoryId = genericArtifact.getSourceRepositoryId();
		String sourceRepositoryKind = genericArtifact.getSourceRepositoryKind();
		
		String targetArtifactId = genericArtifact.getTargetArtifactId();
		String targetSystemId = genericArtifact.getTargetSystemId();
		String targetSystemKind = genericArtifact.getTargetSystemKind();
		String targetRepositoryId = genericArtifact.getTargetRepositoryId();
		String targetRepositoryKind = genericArtifact.getTargetRepositoryKind();
		
		if(stringBugId!=null && (stringBugId!=null && !stringBugId.equals("")) )
			bugId = Integer.parseInt(stringBugId);
		//Boolean doesBugIdExistsInQC = false;
		if(bugId!=0)
			doesBugIdExistsInQC = checkForBugIdInQC(bugId);
		
		
		switch (artifactAction) {
			
			case CREATE: {
				//If the bugId already exists in QC, throw an error. Otherwise create it.
				if(doesBugIdExistsInQC==true) {
					//send this artifact to HOSPITAL
					break;
				}
				else
				{
					if(targetArtifactId==null || targetArtifactId.equals("unknown")) {
					try {
					IQCDefect createdArtifact = defectHandler.createDefect(getQcc(), allFields);
					String targetArtifactIdAfterCreation = createdArtifact.getId();
					log.info("Write Operation SUCCESSFULL!!!!! and the targetArtifactIdAfterCreation="+targetArtifactIdAfterCreation);
					// Update the QC_ENTITY_CHECK HSQL DB Table
					Boolean status = SFEEDBHelper.updateTable(sourceArtifactId, sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind, targetArtifactIdAfterCreation, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
					genericArtifact.setTargetArtifactId(targetArtifactIdAfterCreation);
					// send this artifact to RCDU (Read Connector Database Updater) indicating a success in creating the artifact
					
					
					if(allAttachments!=null && (allAttachments!=null && allAttachments.size()>0)) {
						//create the attachment per genericArtifact
						defectHandler.createAttachment(qcc, targetArtifactIdAfterCreation, allAttachments);
						
					}
					
					
					}
					catch(Exception e) {
						log.error("Exception occured while creating defect in QC:"+e);
						throw new RuntimeException(e);
						//send this artifact to HOSPITAL	
					}
					}
					else {
						// Now, the targetArtifactId is not null. It must be an UPDATE operation. But since the ACTION is create, send it to HOSPITAL.
						//send this artifact to HOSPITAL
						break;
					}
				}
				break;
			}
				
			case UPDATE: {
				//If the bugId does not exists in QC, throw an error. Otherwise continue.
				if(doesBugIdExistsInQC==true) { //should be checked if doesBugIdExistsInQc==false
					//send this artifact to HOSPITAL
					break;
				}
				
				else
				{
					if(targetArtifactId!=null || !(targetArtifactId.equals("")) || targetArtifactId.equals("unknown")) {
					try {
						
						String targetArtifactIdFromTable = SFEEDBHelper.getTargetArtifactIdFromTable(sourceArtifactId, sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
						if(allFields!=null) {
							IQCDefect updatedArtifact = defectHandler.updateDefect(getQcc(), targetArtifactIdFromTable, allFields);
							log.info("Update Operation SUCCESSFULL!!!!! and the targetArtifactIdFromTable="+targetArtifactIdFromTable);
							genericArtifact.setTargetArtifactId(targetArtifactIdFromTable);
							//send this artifact to RCDU (Read COnnector Database Updater) indicating a success in updating the artifact
						}
						
						if(allAttachments!=null && (allAttachments!=null && allAttachments.size()>0)) {
							//create the attachment per genericArtifact
							defectHandler.createAttachment(qcc, targetArtifactIdFromTable, allAttachments);
							
						}
					
					
					}
					catch(Exception e) {
						log.error("Exception occured while updating defect in QC:"+genericArtifact.toString(),e);
						throw new RuntimeException(e);
						//send this artifact to HOSPITAL	
					}
					}
					else {
						// Now, the targetArtifactId is null. It must be a CREATE operation. But since the ACTION is update, send it to HOSPITAL.
						//send this artifact to HOSPITAL
						throw new RuntimeException("The targetArtifactId is null. It must be a CREATE operation. But since the ACTION is update, sending it to HOSPITAL");
					}
				}
				break;
			}
			
			case DELETE:
				
				
			case UNKNOWN: {
				break;
			}
				
				
			}
		
		try {
		resultDoc = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact); 
		}
		catch(GenericArtifactParsingException e) {
			log.error("Exception occured while parsing the GenericArtifact into a Document:" + e);
		}
		log.info("***"+resultDoc.asXML());
		Object[] result={resultDoc};
		disconnect();
		return result;
	}
	
	public static GenericArtifact getArtifactFromDocument(Document genericArtifactDocument) {
		
		GenericArtifact genericArtifact = new GenericArtifact();
		
		try {
			genericArtifact = GenericArtifactHelper.createGenericArtifactJavaObject(genericArtifactDocument);
		}
		catch(GenericArtifactParsingException e) {
			log.error("Exception occured while parsing the Document into a GenericArtifact:" + e);
		}
		
		return genericArtifact;
	}
	
	public GenericArtifact concatValuesOfSameFieldNames(GenericArtifact genericArtifact) {
		
		List<GenericArtifactField> allFields = genericArtifact.getAllGenericArtifactFields();
		List<String> allFieldNames = new ArrayList();
		for(int cnt=0; cnt < allFields.size(); cnt++) {
			
			if( !(allFieldNames.contains(allFields.get(cnt).getFieldName())) && 
					genericArtifact.getAllGenericArtifactFieldsWithSameFieldName(allFields.get(cnt).getFieldName()).size()>1) {
				List<GenericArtifactField> allSameFields = genericArtifact.getAllGenericArtifactFieldsWithSameFieldName(allFields.get(cnt).getFieldName());
				String concatinatedString = (String) allSameFields.get(0).getFieldValue();
				for(int newCnt=1; newCnt < allSameFields.size(); newCnt++) {
					concatinatedString+=";";
					concatinatedString+=(String)allSameFields.get(newCnt).getFieldValue();
				}
				genericArtifact.getAllGenericArtifactFieldsWithSameFieldName(allFields.get(cnt).getFieldName()).get(0).setFieldValue(concatinatedString);
			}
			allFieldNames.add(allFields.get(cnt).getFieldName());
			
			
		}
		
		return genericArtifact;
	}
	
	
	public void connectProperly() {
		
		connect();
		
		//qcc.initConnectionEx(getServerUrl());
		qcc.connectProjectEx(getDomain(), getProjectName(), getUserName(), getPassword());
		
	}
	public static String getFieldValueFromGenericArtifact(GenericArtifact individualGenericArtifact, String fieldName) {
		
		String fieldValue = null;
		if(individualGenericArtifact.getAllGenericArtifactFields()!=null && individualGenericArtifact.getAllGenericArtifactFieldsWithSameFieldName(fieldName)!=null)
			fieldValue = (String) individualGenericArtifact.getAllGenericArtifactFieldsWithSameFieldName(fieldName).get(0).getFieldValue();
		
		return fieldValue;
	}
	
	public boolean checkForBugIdInQC(int bugId) {
		
		QCDefect thisDefect = defectHandler.getDefectWithId(this.getQcc(), bugId);
		
		if(thisDefect!=null)
			return true;
		else
			return false;
	}
	
	@Override
	public void connect()  {
		try {
			super.connect();
		} catch (IOException e) {
			// TODO Throw an exception?
			log.error("Could not login into QC: ",e);
		}
	}
	
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		// Capture the return exception list and validate the exceptions

		defectHandler = new QCDefectHandler();
	}

	public Object[] process(Object data) {
		if (data == null) {
		      throw new NullRecordException("Expected Document. Null record not permitted.");
		    }

		    if (!(data instanceof Document)) {
		      throw new RecordFormatException("Expected Document. Got [" + data.getClass().getName() + "]");
		    }
		    
		    return processXMLDocument((Object) data);
	}

	public void reset(Object context) {
	}

	private String getToTime(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TOTIME");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getCreateToken() {
		return CreateToken;
	}

	public void setCreateToken(String createToken) {
		CreateToken = createToken;
	}

	public QCWriter() {
		super();
	}
}
