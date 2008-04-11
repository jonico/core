package com.collabnet.ccf.pi.sfee;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
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
import com.collabnet.ccf.core.ga.GenericArtifactAttachment;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.pi.sfee.IGAToArtifactConverter;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

/**
 * This component is responsible for writing SFEE tracker items encoded in the
 * generic XML artifact format back to the SFEE tracker
 * 
 * @author jnicolai
 * 
 */
public class SFEEWriter extends SFEEConnectHelper implements
		IDataProcessor {
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(SFEEWriter.class);

	/**
	 * Token used to indicate that the tracker item has to be created
	 */
	private String createToken;

	/**
	 * SFEE tracker handler instance
	 */
	private SFEETrackerHandler trackerHandler;

	/**
	 * Comment used when updating SFEE tracker items
	 */
	private String updateComment;

	/**
	 * name of the flex field where the version number of the last update, we
	 * did ourself, is stored
	 */
	private String lastSynchronizedWithOtherSystemSFEETargetFieldname;

	/**
	 * name of flex field in this tracker used to store the non-SFEE-version
	 * (version in the other system) of this tracker item (this is the typically
	 * the fallbackVersion for the other system)
	 */
	private String otherSystemVersionInSFEETargetFieldname;
	
	private IGAToArtifactConverter converter;
	

	private SFEEAttachmentHandler attachmentHandler;

	/**
	 * Main method to handle the creation, updating and deletion of SFEE tracker
	 * items
	 * 
	 * @param data
	 *            input XML document in generic XML artifact format
	 * @return array of generated XML documents compliant to generic XML
	 *         artifact schema
	 */
	private Object[] processXMLDocument(Document data) {
		System.out.println("asdfw  s");
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(data);
		} catch (GenericArtifactParsingException e2) {
			throw new RuntimeException(e2);
		}
		String trackerId = ga.getTargetRepositoryId();
		String sourceRepositoryId = ga.getSourceRepositoryId();
		String sourceArtifactId = ga.getSourceArtifactId();
		String sourceSystemId = ga.getSourceSystemId();
		String sourceSystemKind = ga.getSourceSystemKind();
		String targetSystemId = ga.getTargetSystemId();
		String targetSystemKind = ga.getTargetSystemKind();
		String sourceRepositoryKind = ga.getSourceRepositoryKind();
		String targetRepositoryKind = ga.getTargetRepositoryKind();
		String targetRepositoryId = ga.getTargetRepositoryId();
		if(sourceArtifactId.equalsIgnoreCase("Unknown")){
			return new Object[]{data};
		}
		String targetArtifactIdFromDB = SFEEDBHelper.getTargetArtifactIdFromTable(sourceArtifactId,
				sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind,
				targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
		if(!SFEEGAHelper.containsSingleField(ga, "Id")){
			SFEEGAHelper.addField(ga, "Id", getCreateToken(), "String");
		}
		if(!StringUtils.isEmpty(targetArtifactIdFromDB)){
			SFEEGAHelper.updateSingleField(ga, "Id", targetArtifactIdFromDB);
			ga.setTargetArtifactId(targetArtifactIdFromDB);
		}
		if(SFEEGAHelper.containsSingleField(ga, "FolderId")){
			SFEEGAHelper.updateSingleField(ga, "FolderId", trackerId);
		}
		else {
			SFEEGAHelper.addField(ga, "FolderId", trackerId, "String");
		}
		TrackerFieldSoapDO[] flexFields = null;
		try {
			connect();
			flexFields = trackerHandler.getFlexFields(getSessionId(), trackerId);
			disconnect();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArtifactSoapDO soapDoObj = (ArtifactSoapDO) converter.convert(ga, flexFields);
		
		Boolean duplicateArtifact = false;
//			(Boolean) SFEEXMLHelper
//				.asTypedValue(SFEEXMLHelper.getSingleValue(data,
//						"isDuplicate", false), "Boolean");
		if (duplicateArtifact)
			// do not suppress it anymore, but pass it unchanged
			// return new Object[0];
			return new Object[] { data };

		// this field is probably added from a read processor or read connector
		// component
		Boolean deleteArtifact = false;
//			(Boolean) SFEEXMLHelper
//				.asTypedValue(SFEEXMLHelper.getSingleValue(data,
//						"deleteFlag", false), "Boolean");
		// this field is probably added by a dedicated conflict resolution
		// policy processor or a generic processor
		// TODO Let user specify this field?
		Boolean forceOverride = false;
//			(Boolean) SFEEXMLHelper
//				.asTypedValue(SFEEXMLHelper.getSingleValue(data,
//						"forceOverride", false), "Boolean");
		// fill flexFieldNames and flexFieldValue arrays
		

		// check whether we should create or update the artifact
		String id = (String) SFEEGAHelper.getSingleValue(ga, "Id");
		ArtifactSoapDO result = null;
		if ((StringUtils.isEmpty(id)) || SFEEGAHelper.getSingleValue(ga, "Id").equals(
				"NEW")) {
			// find out whether we should delete something, that is not even
			// present here
			if (deleteArtifact.booleanValue()) {
				log
						.warn("Cannot delete an artifact that is not even mirrored (yet): "
								+ data.asXML());
				return null;
			}
			connect();
			// TODO apply a better type conversion concept here
			try {

				result = trackerHandler
						.createArtifact(
								getSessionId(),
								soapDoObj.getFolderId(),
								soapDoObj.getDescription(),
								soapDoObj.getCategory(),
								soapDoObj.getGroup(),
								soapDoObj.getStatus(),
								soapDoObj.getStatusClass(),
								soapDoObj.getCustomer(),
								soapDoObj.getPriority(),
								soapDoObj.getEstimatedHours(),
								soapDoObj.getActualHours(),
								soapDoObj.getCloseDate(),
								soapDoObj.getAssignedTo(),
								soapDoObj.getReportedReleaseId(),
								soapDoObj.getResolvedReleaseId(),
								Arrays.asList(soapDoObj.getFlexFields().getNames()),
								Arrays.asList(soapDoObj.getFlexFields().getValues()),
								Arrays.asList(soapDoObj.getFlexFields().getTypes()),
								soapDoObj.getTitle(),
								// TODO - Don't know what these mean
								getUpdateComment(),
								getLastSynchronizedWithOtherSystemSFEETargetFieldname());

				// update Id field after creating the artifact
				String targetArtifactId = result.getId();
				SFEEGAHelper.updateSingleField(ga, "Id", targetArtifactId);
				ga.setTargetArtifactId(targetArtifactId);
				SFEEDBHelper.updateTable(sourceArtifactId, sourceSystemId, sourceSystemKind,
						sourceRepositoryId, sourceRepositoryKind, targetArtifactId, 
						targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);

			} catch (NumberFormatException e) {
				log.error("Wrong data format of attribute for artifact "
						+ data.asXML(), e);
				disconnect();
				return null;
			} catch (RemoteException e) {
				log.error(
						"Remote exception while trying to create new artifact: "
								+ data.asXML(), e);
				disconnect();
				return null;
			}
		} else {
			connect();
			try {
				if (deleteArtifact.booleanValue()) {
					trackerHandler.removeArtifact(getSessionId(),
							soapDoObj.getId());
				} else {
					// update token or do conflict resolution
					// TODO apply a better type conversion concept here
					result = trackerHandler
							.updateArtifact(
									getSessionId(),
									soapDoObj.getFolderId(),
									soapDoObj.getDescription(),
									soapDoObj.getCategory(),
									soapDoObj.getGroup(),
									soapDoObj.getStatus(),
									soapDoObj.getStatusClass(),
									soapDoObj.getCustomer(),
									soapDoObj.getPriority(),
									soapDoObj.getEstimatedHours(),
									soapDoObj.getActualHours(),
									soapDoObj.getCloseDate(),
									soapDoObj.getAssignedTo(),
									soapDoObj.getReportedReleaseId(),
									soapDoObj.getResolvedReleaseId(),
									Arrays.asList(soapDoObj.getFlexFields().getNames()),
									Arrays.asList(soapDoObj.getFlexFields().getValues()),
									Arrays.asList(soapDoObj.getFlexFields().getTypes()),
									soapDoObj.getTitle(),
									soapDoObj.getId(),
									getUpdateComment(),
									forceOverride);

					if (result == null) {
						// conflict resolution has decided in favor of the
						// target copy
						disconnect();
						return new Object[0];
					}
				}
			} catch (NumberFormatException e) {
				log.error("Wrong data format of attribute for artifact "
						+ data.asXML(), e);
				disconnect();
				return null;
			} catch (RemoteException e) {
				// TODO Find a better way how to deal with stale updates
				// (calling conflict resolution and trying it again?)
				log.error(
						"Remote exception while trying to update or delete artifact: "
								+ data.asXML(), e);
				disconnect();
				return null;
			}
		}
		if(result != null){
			List<GenericArtifactAttachment> attachList = ga.getAllGenericArtifactAttachments();
			for(GenericArtifactAttachment att:attachList){
				try {
					attachmentHandler.handleAttachment(this.getSessionId(), att, result);
				} catch (RemoteException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		Document document = null;
		try {
			document = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
		} catch (GenericArtifactParsingException e) {
			throw new RuntimeException(e);
		}
		Object[] resultDocs = { document };
		disconnect();
		return resultDocs;
	}

	@Override
	/**
	 * Connect to SFEE and handle exceptions
	 */
	public void connect() {
		try {
			super.connect();
		} catch (IOException e) {
			// TODO Throw an exception?
			log.error("Could not login into SFEE: ", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Validate whether all mandatory properties are set correctly
	 */
	public void validate(List exceptions) {
		super.validate(exceptions);

		if (getCreateToken() == null) {
			log.error("createToken-property no set");
			exceptions.add(new ValidationException(
					"createToken-property not set", this));
		}

		if (getUpdateComment() == null) {
			log.error("updateComment-property no set");
			exceptions.add(new ValidationException(
					"updateComment-property not set", this));
		}

		if (getOtherSystemVersionInSFEETargetFieldname() == null) {
			log
					.error("otherSystemVersionInSFEETargetFieldname-property no set");
			exceptions.add(new ValidationException(
					"otherSystemVersionInSFEETargetFieldname-property no set",
					this));
		}

		trackerHandler = new SFEETrackerHandler(getServerUrl());
		attachmentHandler = new SFEEAttachmentHandler(getServerUrl());
	}

	/**
	 * Set the create token
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
	 * Reset processor
	 */
	public void reset(Object context) {
	}

	/**
	 * Set the update comment
	 * 
	 * @param updateComment
	 *            see private attribute doc
	 */
	public void setUpdateComment(String updateComment) {
		this.updateComment = updateComment;
	}

	/**
	 * Get the update comment
	 * 
	 * @return see private attribute doc
	 */
	public String getUpdateComment() {
		return updateComment;
	}

	/**
	 * Set otherSystemVersionInSFEETargetFieldname
	 * 
	 * @param otherSystemVersionInSFEETargetFieldname
	 *            see private attribute doc
	 */
	public void setOtherSystemVersionInSFEETargetFieldname(
			String otherSystemVersionInSFEETargetFieldname) {
		this.otherSystemVersionInSFEETargetFieldname = otherSystemVersionInSFEETargetFieldname;
	}

	/**
	 * Get otherSystemVersionInSFEETargetFieldname
	 * 
	 * @return see private attribute doc
	 */
	public String getOtherSystemVersionInSFEETargetFieldname() {
		return otherSystemVersionInSFEETargetFieldname;
	}

	/**
	 * Set lastSynchronizedWithOtherSystemSFEETargetFieldname
	 * 
	 * @param lastSynchronizedWithOtherSystemSFEETargetFieldname
	 *            see private attribute doc
	 */
	public void setLastSynchronizedWithOtherSystemSFEETargetFieldname(
			String lastSynchronizedWithOtherSystemSFEETargetFieldname) {
		this.lastSynchronizedWithOtherSystemSFEETargetFieldname = lastSynchronizedWithOtherSystemSFEETargetFieldname;
	}

	/**
	 * Get getLastSynchronizedWithOtherSystemSFEETargetFieldname
	 * 
	 * @return see private attribute doc
	 */
	public String getLastSynchronizedWithOtherSystemSFEETargetFieldname() {
		return lastSynchronizedWithOtherSystemSFEETargetFieldname;
	}

	public IGAToArtifactConverter getConverter() {
		return converter;
	}

	public void setConverter(IGAToArtifactConverter converter) {
		this.converter = converter;
	}
}
