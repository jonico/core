package com.collabnet.ccf.pi.qc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.collabnet.ccf.pi.qc.QCEntityService;

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
		//Populate the sourceArtifactId into each of the GenericArtifacts from the QCEntityService
		//This operation is done in a separate component called QCEntityService. So, the input flowing into this file is already populated
				
		connectProperly();
		
		List<GenericArtifactField> allFields = genericArtifact.getAllGenericArtifactFields();
		GenericArtifact.ArtifactActionValue artifactAction = genericArtifact.getArtifactAction();
		String stringBugId = getFieldValueFromGenericArtifact(genericArtifact, "BG_BUG_ID");
		
		
		String sourceArtifactId = genericArtifact.getSourceArtifactId();
		String sourceSystemId  = genericArtifact.getSourceSystemId();
		String sourceRepositoryId = genericArtifact.getSourceRepositoryId();
		String targetArtifactId = genericArtifact.getTargetArtifactId();
		String targetSystemId = genericArtifact.getTargetSystemId();
		String targetRepositoryId = genericArtifact.getTargetRepositoryId();
		
		int bugId = Integer.parseInt(stringBugId);
		//Boolean doesBugIdExistsInQC = false;
		Boolean doesBugIdExistsInQC = checkForBugIdInQC(bugId);
		
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
					Boolean status = updateTable(sourceSystemId, sourceRepositoryId, targetSystemId, targetRepositoryId, sourceArtifactId, targetArtifactIdAfterCreation);
					genericArtifact.setTargetArtifactId(targetArtifactIdAfterCreation);
					// send this artifact to RCDU (Read COnnector Database Updater) indicating a success in creating the artifact
					}
					catch(Exception e) {
						log.error("Exception occured while creating defect in QC:"+e);
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
						
						String targetArtifactIdFromTable = QCEntityService.getTargetArtifactIdFromTable(sourceArtifactId, sourceSystemId, sourceRepositoryId, targetSystemId, targetRepositoryId);
						IQCDefect updatedArtifact = defectHandler.updateDefect(getQcc(), targetArtifactIdFromTable, allFields);
						log.info("Update Operation SUCCESSFULL!!!!! and the targetArtifactIdFromTable="+targetArtifactIdFromTable);
						genericArtifact.setTargetArtifactId(targetArtifactIdFromTable);
						//send this artifact to RCDU (Read COnnector Database Updater) indicating a success in updating the artifact
					}
					catch(Exception e) {
						log.error("Exception occured while updating defect in QC:"+genericArtifact.toString(),e);
						//send this artifact to HOSPITAL	
					}
					}
					else {
						// Now, the targetArtifactId is null. It must be a CREATE operation. But since the ACTION is update, send it to HOSPITAL.
						//send this artifact to HOSPITAL
						break;
					}
				}
				break;
			}
			
			case DELETE:
				
				
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
	
	public GenericArtifact getArtifactFromDocument(Document genericArtifactDocument) {
		
		GenericArtifact genericArtifact = new GenericArtifact();
		
		try {
			genericArtifact = GenericArtifactHelper.createGenericArtifactJavaObject(genericArtifactDocument);
		}
		catch(GenericArtifactParsingException e) {
			log.error("Exception occured while parsing the Document into a GenericArtifact:" + e);
		}
		
		return genericArtifact;
	}
	
	public void connectProperly() {
		
		connect();
		//qcc.initConnectionEx(getServerUrl());
		qcc.connectProjectEx(getDomain(), getProjectName(), getUserName(), getPassword());
		
	}
	public static String getFieldValueFromGenericArtifact(GenericArtifact individualGenericArtifact, String fieldName) {
		
		String fieldValue = (String) individualGenericArtifact.getAllGenericArtifactFieldsWithSameFieldName(fieldName).get(0).getFieldValue();
		
		return fieldValue;
	}
	
	public boolean updateTable(String sourceSystemId, String sourceRepositoryId, String targetSystemId, String targetRepositoryId, String sourceArtifactId, String targetArtifactIdAfterCreation) {
		
		Boolean status = false;
		
		String sql = "UPDATE QC_ENTITY_CHECK SET TARGET_ARTIFACT_ID='"+targetArtifactIdAfterCreation+"' WHERE SOURCE_SYSTEM_ID='"+sourceSystemId+"'";
		sql+=" AND SOURCE_REPOSITORY_ID='"+sourceRepositoryId+"' AND SOURCE_ARTIFACT_ID='"+sourceArtifactId+"'";
		sql+=" AND TARGET_SYSTEM_ID='"+targetSystemId+"' AND TARGET_REPOSITORY_ID='"+targetRepositoryId+"'";
		log.info("QCWriter UPDATE SQL Query:"+ sql); 
		ResultSet rs = null;
		try {
			rs = executeSql(sql);
		}
		catch(Exception e) {
			log.error("Exception while executing the UPDATE Query in QCWriter:"+e);
		}
		if(rs==null) return false;
		else return true;
		
		
	}
	
	
	public static ResultSet executeSql(String sql) throws ClassNotFoundException, SQLException {
	  	Connection conn;                                                
        Class.forName("org.hsqldb.jdbcDriver");
        conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
        Statement st = null;
        ResultSet rs = null;
        st = conn.createStatement();         // statement objects can be reused with
        rs = st.executeQuery(sql);    // run the query
        return rs;
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
