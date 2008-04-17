package com.collabnet.ccf.pi.sfee.v44;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.db.DBHelper;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;


/**
 * This component will find out whether an artifact coming out of a non-SFEE
 * system has to be created, updated, deleted or ignored within SFEE. It will
 * also try to find out the correct SFEE id for the artifact in question.
 * 
 * @author jnicolai
 * 
 */
public class SFEEEntityService extends SFEEConnectHelper implements
		IDataProcessor {
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(SFEEEntityService.class);

	/**
	 * Token used to indicate that the SFEE tracker item has to be created
	 */
	private String createToken;

	/**
	 * User that was used in the non-SFEE system to store SFEE's tracker items.
	 * If an artifact was lastly modified by this user and not just created, it
	 * will be ignored to prevent endless update loops.
	 */
	private String synchronizationUser;

	/**
	 * field name that is used within SFEE to store the artifact id used in the
	 * source (non-SFEE) system
	 */
	private String otherSystemInSFEETargetFieldname;

	/**
	 * SFEE tracker handler instance
	 */
	

	/**
	 * openAdaptor Method to process all input and puts out the results This
	 * method will only handle Dom4J documents encoded in the generic XML schema
	 */
	public Object[] process(Object data) {
		if (data == null) {
			throw new NullRecordException(
					"Expected Document. Null record not permitted.");
		}

		if (!(data instanceof Document)) {
			throw new RecordFormatException("Expected Document. Got ["
					+ data.getClass().getName() + "]");
		}

		return processXMLDocument((Document) data);
	}

	/**
	 * Main method to handle the mapping and filtering of non-SFEE artifacts to
	 * SFEE tracker items
	 * 
	 * @param data
	 *            input XML document in generic XML artifact format
	 * @return array of generated XML documents compliant to generic XML
	 *         artifact schema
	 */
	private Object[] processXMLDocument(Document data) {
		Document filledArtifactDocument = null;
		GenericArtifact genericArtifact = new GenericArtifact();
		try {
			genericArtifact = GenericArtifactHelper.createGenericArtifactJavaObject(data);
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
		if(sourceArtifactId.equalsIgnoreCase("Unknown")){
			return new Object[]{data};
		}
		
		String targetArtifactIdFromTable = DBHelper.getTargetArtifactIdFromTable(sourceArtifactId, sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
		
		if(targetArtifactIdFromTable!=null && !(targetArtifactIdFromTable.equals("NEW")) && !(targetArtifactIdFromTable.equals("NULL"))) {
	    	genericArtifact.setTargetArtifactId(targetArtifactIdFromTable);
	    }
		if(targetArtifactIdFromTable==null) {
			// TODO Review the logic here
//	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.UPDATE)) {
//	    		//Send this artifact to HOSPITAL
//	    	}
//	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.CREATE)) {
	    		Boolean insertStatus = DBHelper.insertRecordInTable(sourceArtifactId, sourceSystemId,
	    				sourceSystemKind, sourceRepositoryId, sourceRepositoryKind, targetSystemId,
	    				targetSystemKind, targetRepositoryId, targetRepositoryKind);
//	    	}
	    }
		
	    try {
	    	filledArtifactDocument = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
	    }
	    catch(Exception e) {
	    	log.error("Exception while converting the resultantGenericArtifact into the resultDocument in QCEntityService:"+e);
	    	throw new RuntimeException(e);
	    }
		
	    Object[] result = {filledArtifactDocument};
		return result;
	}

	/**
	 * Reset the processor
	 */
	public void reset(Object context) {
	}

	/**
	 * Set synchronization user
	 * 
	 * @param synchronizationUser
	 *            see private attribute doc
	 */
	public void setSynchronizationUser(String synchronizationUser) {
		this.synchronizationUser = synchronizationUser;
	}

	/**
	 * Get synchronization user
	 * 
	 * @return see private attribute doc
	 */
	public String getSynchronizationUser() {
		return synchronizationUser;
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Validate whether all mandatory properties are set correctly
	 */
	public void validate(List exceptions) {
		super.validate(exceptions);
		if (getSynchronizationUser() == null) {
			log.error("synchronizationUser-property no set");
			exceptions.add(new ValidationException(
					"synchronizationUser-property not set", this));
		}
		if (getOtherSystemInSFEETargetFieldname() == null) {
			log.error("otherSystemInSFEETargetFieldname-property not set");
			exceptions.add(new ValidationException(
					"otherSystemInSFEETargetFieldname not set", this));
		}

		if (getCreateToken() == null) {
			log.error("createToken-property no set");
			exceptions.add(new ValidationException(
					"createToken-property not set", this));
		}
		// Create tracker handler
	}

	/**
	 * Set otherSystemInSFEETargetFieldName
	 * 
	 * @param otherSystemInSFEETargetFieldName
	 *            see private attribute doc
	 */
	public void setOtherSystemInSFEETargetFieldname(String otherSystemInSFEETargetFieldName) {
		this.otherSystemInSFEETargetFieldname = otherSystemInSFEETargetFieldName;
	}

	/**
	 * Get otherSystemInSFEETargetFieldname
	 * 
	 * @return see private attribute doc
	 */
	public String getOtherSystemInSFEETargetFieldname() {
		return otherSystemInSFEETargetFieldname;
	}

	/**
	 * Set create token
	 * 
	 * @param createToken
	 *            see private attribute doc
	 */
	public void setCreateToken(String createToken) {
		this.createToken = createToken;
	}

	/**
	 * Get create token
	 * 
	 * @return see private attribute doc
	 */
	public String getCreateToken() {
		return createToken;
	}
}
