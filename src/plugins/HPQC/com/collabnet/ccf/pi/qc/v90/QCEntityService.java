package com.collabnet.ccf.pi.qc.v90;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.pi.sfee.v44.SFEEDBHelper;




public class QCEntityService extends QCConnectHelper implements
		IDataProcessor {
	
	public QCEntityService() {
		super();
	}

	private static final Log log = LogFactory.getLog(QCEntityService.class);
	/*private String CreateToken;
	private String synchronizationUser;
	private String otherSystemInQCTargetFieldname;
	*/
	private QCDefectHandler defectHandler;

	public Object[] process(Object data) {
		if (data == null) {
		      throw new NullRecordException("Expected Document. Null record not permitted.");
		    }

		    if (!(data instanceof Document)) {
		      throw new RecordFormatException("Expected Document. Got [" + data.getClass().getName() + "]");
		    }

		    return processXMLDocument((Document) data);
	}

	private Object[] processXMLDocument(Document data) {
		
		System.out.println("Inside QCEntityService, the incoming document is ::" + data.asXML());
		System.out.println("***************************************************");
		Document filledArtifactDocument = null;
		GenericArtifact genericArtifact = new GenericArtifact();
		try {
			genericArtifact = GenericArtifactHelper.createGenericArtifactJavaObject(data);
			//genericArtifact = fillInRequiredFields(genericArtifact);
		}
		catch(Exception e) {
			System.out.println("GenericArtifact Parsing exception" + e);
		}
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
		
		String targetArtifactIdFromTable = SFEEDBHelper.getTargetArtifactIdFromTable(sourceArtifactId, sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
		
		if(targetArtifactIdFromTable!=null && !(targetArtifactIdFromTable.equals("NEW")) && !(targetArtifactIdFromTable.equals("NULL"))) {
	    	genericArtifact.setTargetArtifactId(targetArtifactIdFromTable);
	    }
		if(targetArtifactIdFromTable==null) {
	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.UPDATE)) {
	    		//Send this artifact to HOSPITAL
	    	}
	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.CREATE)) {
	    		//Insert a new record in the QC_ENTITY_CHECK Hsql table with the targetArtifactId value as "NEW". 
	    		//This should be updated by the QCWriter after creating a defect.
	    		Boolean insertStatus = SFEEDBHelper.insertRecordInTable(sourceArtifactId, sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
	    		
	    	}
	    }
		
	    try {
	    	filledArtifactDocument = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
	    }
	    catch(Exception e) {
	    	log.error("Exception while converting the resultantGenericArtifact into the resultDocument in QCEntityService:"+e);
	    	throw new RuntimeException(e);
	    }
		
	    System.out.println("Inside QCEntityService, after filing in the targetArtifactId::" + filledArtifactDocument.asXML());
	    
	    Object[] result = {filledArtifactDocument};
		return result;
	}
	
	
	public void reset(Object context) {
	}

	/*public void setSynchronizationUser(String synchronizationUser) {
		this.synchronizationUser = synchronizationUser;
	}

	public String getSynchronizationUser() {
		return synchronizationUser;
	}
	*/
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		/*
		if (getSynchronizationUser()==null) {
			log.error("synchronizationUser-property no set");
			exceptions.add(new ValidationException("synchronizationUser-property not set",this));
		}
		if (getOtherSystemInQCTargetFieldname()==null) {
			log.error("otherSystemInQCTargetFieldname-property not set");
			exceptions.add(new ValidationException("otherSystemInQCTargetFieldname not set",this));
		}
		
		if (getCreateToken()==null) {
			log.error("createToken-property no set");
			exceptions.add(new ValidationException("createToken-property not set",this));
		}
		*/
		// Create tracker handler
		defectHandler = new QCDefectHandler();
	}

	/*public void setOtherSystemInQCTargetFieldname(String sFEEmappingID) {
		otherSystemInQCTargetFieldname = sFEEmappingID;
	}

	public String getOtherSystemInQCTargetFieldname() {
		return otherSystemInQCTargetFieldname;
	}

	public void setCreateToken(String createToken) {
		CreateToken = createToken;
	}

	public String getCreateToken() {
		return CreateToken;
	}
	*/

}
