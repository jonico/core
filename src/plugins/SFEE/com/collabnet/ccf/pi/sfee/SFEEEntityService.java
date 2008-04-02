package com.collabnet.ccf.pi.sfee;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

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
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactDetailSoapRow;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

import org.apache.commons.lang.StringUtils;

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
	private SFEETrackerHandler trackerHandler;
	
	private SFEEDBHelper dbHelper = null;

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
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(data);
		} catch (GenericArtifactParsingException e1) {
			throw new RuntimeException(e1);
		}
		String sourceArtifactId = ga.getSourceArtifactId();
		String sourceRepositoryId = ga.getSourceRepositoryId();
		String targetRepositoryId = ga.getTargetRepositoryId();
		
		String targetArtifactID = dbHelper.getTargetArtifactID(sourceArtifactId, sourceRepositoryId, targetRepositoryId);
		
		boolean artifactAlreadyCreated = false;
		if(StringUtils.isEmpty(targetArtifactID)){
			artifactAlreadyCreated = false;
		}
		else {
			artifactAlreadyCreated = true;
		}
		Boolean duplicateArtifact = false;
//			(Boolean) SFEEXMLHelper
//				.asTypedValue(SFEEXMLHelper.getSingleValue(data,
//						"isDuplicate", false), "Boolean");
		if (duplicateArtifact)
			// do not suppress it anymore, but pass it unchanged
			// return new Object[0];
			return new Object[] { data };

		

		// check whether there is already an entity in the target system
		// TODO Changed false to true
		//String id = SFEEXMLHelper.getSingleValue(data, "Id", true);
		if(!artifactAlreadyCreated) {
			// first check all required fields
			/*
			 * if (!SFEEXMLHelper.containsKey(data,"flexFields")) { // TODO
			 * Throw an exception? log.error("required flexFields-element not
			 * present in artifact: "+data); return null; }
			 */
			if (!SFEEGAHelper.containsSingleField(ga, "FolderId")) {
				// TODO Throw an exception?
				log.error("required folderId-element not present in artifact");
				return null;
			}
			// NOTE We will not be checking other system's field here
//			if (!SFEEXMLHelper.containsSingleField(data,
//					getOtherSystemInSFEETargetFieldname(), true)) {
//				// TODO Throw an exception?
//				log.error("required " + getOtherSystemInSFEETargetFieldname()
//						+ "-element not present in artifact: " + data.asXML());
//				return null;
//			}

			// now we can query the target system for the existence of the
			// artifact
		}
		else {
			try {
				connect();
				ArtifactSoapDO soapDO = trackerHandler.getTrackerItem(getSessionId(), targetArtifactID);
				if (soapDO == null)
					artifactAlreadyCreated = false;
				// artifact already created, insert or update Id element
				else {
					artifactAlreadyCreated = true;
					System.out.println();
					if (SFEEGAHelper.containsSingleField(ga,"Id"))
						SFEEGAHelper.updateSingleField(ga, "Id", targetArtifactID);
					else
						SFEEGAHelper.addField(ga, "Id", targetArtifactID, "String");
				}
				disconnect();
			} catch (RemoteException e) {
				// TODO Declare exception so that it can be processed by OA
				// exception handler
				log
						.error(
								"During the connection process to SFEE, an error occured",
								e);
				disconnect();
				return null;
			} catch (IOException ex) {
				// TODO postpone exception handling to OA framework
				log
						.error(
								"During the connection process to SFEE, an error occured",
								ex);
				disconnect();
				return null;
			}
		}
		if (artifactAlreadyCreated) {
			// this object is already created, now find out, whether we have to
			// do an update
			// check whether last update was done by synchronization user
			if (!SFEEGAHelper.containsSingleField(ga, "LastModifiedBy")) {
				// TODO Throw an exception?
				log
						.error("required lastModifiedBy-element not present in artifact: "
								+ data.asXML());
				return null;
			}
			// Only accept the change if the artifact was created or the change
			// did not come from the synchronization middleware itself
			// TODO Due to the update after a delete, this may miss some
			// artifacts that were just created
			if (SFEEGAHelper.getSingleValue(ga, "LastModifiedBy").equals(getSynchronizationUser())
					&& (!SFEEGAHelper.getSingleValue(ga,
							"LastModifiedDate").equals(
									SFEEGAHelper.getSingleValue(ga,
									"CreatedDate")))) {
				// we do not have to write this artifact because all changes are
				// already presented in target system
				log
						.info("Do not change object, since the change came from this system: "
								+ SFEEGAHelper.getSingleValue(ga,"Id"));
				// do not suppress it anymore, but pass it unchanged
				// return new Object[0];
				SFEEGAHelper.updateSingleField(ga, "isDuplicate", "true");
				return new Object[] { data };
			}
			// else object should be updated
			else {
				log.info("Updating or deleting object with "
						+ getOtherSystemInSFEETargetFieldname()
						+ " "
						+ SFEEGAHelper.getSingleValue(ga, "Id")
						+ " last modified by "
						+ SFEEGAHelper.getSingleValue(ga,"LastModifiedBy"));
				return new Object[] { data };
			}
		}
		// artifact has to be created
		if (SFEEGAHelper.containsSingleField(ga, "Id"))
			SFEEGAHelper.updateSingleField(ga,"Id", getCreateToken());
		else
			SFEEGAHelper.addField(ga, "Id",
					getCreateToken(), "String");
		log
				.info("Create new object or trying to delete object that is not (yet) mirrored");
		
		return new Object[] { data };
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
		trackerHandler = new SFEETrackerHandler(getServerUrl());
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

	public SFEEDBHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(SFEEDBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
}
