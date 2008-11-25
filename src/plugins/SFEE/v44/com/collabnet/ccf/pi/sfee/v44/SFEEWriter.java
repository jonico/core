package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.collabnet.ccf.pi.sfee.v44.meta.ArtifactMetaData;
import com.vasoftware.sf.soap44.fault.InvalidSessionFault;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

/**
 * This component is responsible for writing SFEE tracker items encoded in the
 * generic XML artifact format back to the SFEE tracker
 *
 * @author jnicolai
 *
 */
public class SFEEWriter extends AbstractWriter<Connection> implements
		IDataProcessor {

	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(SFEEWriter.class);

	/**
	 * SFEE tracker handler instance
	 */
	private SFEETrackerHandler trackerHandler;

	/**
	 * Comment used when updating SFEE tracker items
	 */
	private String updateComment;

	private SFEEAttachmentHandler attachmentHandler;

	// private ConnectionManager<Connection> connectionManager = null;

	private String serverUrl;

	/**
	 * Password that is used to login into the SFEE/CSFE instance in combination
	 * with the username
	 */
	private String password;

	/**
	 * Username that is used to login into the SFEE/CSFE instance
	 */
	private String username;

	/**
	 * Another user name that is used to login into the SFEE/CSFE instance This
	 * user has to differ from the ordinary user used to log in in order to
	 * force initial resyncs with the source system once a new artifact has been
	 * created.
	 */
	private String resyncUserName;

	/**
	 * Password that belongs to the resync user. This user has to differ from
	 * the ordinary user used to log in in order to force initial resyncs with
	 * the source system once a new artifact has been created.
	 */
	private String resyncPassword;

	public SFEEWriter(String id) {
		super(id);
	}

	public SFEEWriter() {
	}

	private void initializeArtifact(GenericArtifact ga) {
		GenericArtifact.ArtifactActionValue artifactAction = ga
				.getArtifactAction();
		// GenericArtifact.ArtifactTypeValue artifactType =
		// ga.getArtifactType();
		// String sourceArtifactId = ga.getSourceArtifactId();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetArtifactId = ga.getTargetArtifactId();
		String tracker = targetRepositoryId;

		if (artifactAction == GenericArtifact.ArtifactActionValue.UPDATE) {
			if (SFEEGAHelper.containsSingleMandatoryField(ga,
					ArtifactMetaData.SFEEFields.id.getFieldName())) {
				SFEEGAHelper.updateSingleMandatoryField(ga,
						ArtifactMetaData.SFEEFields.id.getFieldName(),
						targetArtifactId);
				ga.setTargetArtifactId(targetArtifactId);
			} else {
				SFEEGAHelper.addField(ga, ArtifactMetaData.SFEEFields.id
						.getFieldName(), targetArtifactId,
						GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
						GenericArtifactField.FieldValueTypeValue.STRING);
			}
		}
		if (SFEEGAHelper.containsSingleMandatoryField(ga,
				ArtifactMetaData.SFEEFields.folderId.getFieldName())) {
			SFEEGAHelper.updateSingleMandatoryField(ga,
					ArtifactMetaData.SFEEFields.folderId.getFieldName(),
					tracker);
		} else {
			SFEEGAHelper.addField(ga, ArtifactMetaData.SFEEFields.folderId
					.getFieldName(), tracker,
					GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
					GenericArtifactField.FieldValueTypeValue.STRING);
		}
	}

	@Override
	public boolean handleException(Throwable cause,
			ConnectionManager<Connection> connectionManager) {
		if (cause == null)
			return false;
		if ((cause instanceof java.net.SocketException || cause instanceof java.net.UnknownHostException)
				&& connectionManager.isEnableRetryAfterNetworkTimeout()) {
			return true;
		} else if (cause instanceof ConnectionException
				&& connectionManager.isEnableRetryAfterNetworkTimeout()) {
			return true;
		} else if (cause instanceof InvalidSessionFault
				&& connectionManager.isEnableReloginAfterSessionTimeout()) {
			return true;
		} else if (cause instanceof RemoteException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		} else if (cause instanceof CCFRuntimeException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		}
		return false;
	}

	public Document returnDocument(GenericArtifact ga) {
		Document document = null;
		try {
			document = GenericArtifactHelper
					.createGenericArtifactXMLDocument(ga);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		return document;
	}

	public Document createArtifact(Document data) {
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(data);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			XPathUtils.addAttribute(data.getRootElement(),
					GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		this.initializeArtifact(ga);
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetArtifactId = ga.getTargetArtifactId();
		String tracker = targetRepositoryId;
		Connection connection = connect(ga);
		ArtifactSoapDO result = null;
		try {
			result = this.createArtifact(ga, tracker, connection);
			// update Id field after creating the artifact
			targetArtifactId = result.getId();
			SFEEGAHelper.addField(ga, ArtifactMetaData.SFEEFields.id
					.getFieldName(), targetArtifactId,
					GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
					GenericArtifactField.FieldValueTypeValue.STRING);
		} catch (NumberFormatException e) {
			log.error("Wrong data format of attribute for artifact "
					+ data.asXML(), e);
			return null;
		} finally {
			disconnect(connection);
		}
		if (result != null) {
			this.populateTargetArtifactAttributes(ga, result);
		}
		return this.returnDocument(ga);
	}

	@Override
	public Document updateArtifact(Document data) {
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(data);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			XPathUtils.addAttribute(data.getRootElement(),
					GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		this.initializeArtifact(ga);
		String targetRepositoryId = ga.getTargetRepositoryId();
		String tracker = targetRepositoryId;
		Connection connection = connect(ga);
		ArtifactSoapDO result = null;
		try {
			// update and do conflict resolution
			result = this.updateArtifact(ga, tracker,connection);
		} catch (NumberFormatException e) {
			String cause = "Number format exception while trying to extract the field data.";
			log.error(cause, e);
			XPathUtils.addAttribute(data.getRootElement(),
					GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}

		// otherwise a conflict has happened and the generic artifact has been already prepared
		if (result != null) {
			this.populateTargetArtifactAttributes(ga, result);
		}
		return this.returnDocument(ga);
	}

	public Document[] createAttachment(Document data) {
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(data);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		this.initializeArtifact(ga);
		Connection connection = connect(ga);
		String targetParentArtifactId = ga.getDepParentTargetArtifactId();
		GenericArtifact parentArtifact = null;
		try {
			attachmentHandler.handleAttachment(connection.getSessionId(), ga,
					targetParentArtifactId, this.getUsername(), connection
							.getSfSoap());
			ArtifactSoapDO artifact = trackerHandler.getTrackerItem(connection.getSessionId(), targetParentArtifactId);
			parentArtifact = new GenericArtifact();
			parentArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			parentArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
			parentArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
			parentArtifact.setConflictResolutionPriority(ga.getConflictResolutionPriority());
			parentArtifact.setSourceArtifactId(ga.getDepParentSourceArtifactId());
			parentArtifact.setSourceArtifactLastModifiedDate(ga.getSourceArtifactLastModifiedDate());
			parentArtifact.setSourceArtifactVersion(ga.getSourceArtifactVersion());
			parentArtifact.setSourceRepositoryId(ga.getSourceRepositoryId());
			parentArtifact.setSourceSystemId(ga.getSourceSystemId());
			parentArtifact.setSourceSystemKind(ga.getSourceSystemKind());
			parentArtifact.setSourceRepositoryKind(ga.getSourceRepositoryKind());
			parentArtifact.setSourceSystemTimezone(ga.getSourceSystemTimezone());

			parentArtifact.setTargetArtifactId(targetParentArtifactId);
			parentArtifact.setTargetArtifactLastModifiedDate(DateUtil.format(artifact.getLastModifiedDate()));
			parentArtifact.setTargetArtifactVersion(Integer.toString(artifact.getVersion()));
			parentArtifact.setTargetRepositoryId(ga.getTargetRepositoryId());
			parentArtifact.setTargetRepositoryKind(ga.getTargetRepositoryKind());
			parentArtifact.setTargetSystemId(ga.getTargetSystemId());
			parentArtifact.setTargetSystemKind(ga.getTargetSystemKind());
			parentArtifact.setTargetSystemTimezone(ga.getTargetSystemTimezone());

		} catch (RemoteException e) {
			String cause = "Problem occured while creating attachments in SFEE";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		Document parentArtifactDoc = returnDocument(parentArtifact);
		Document attachmentDocument = this.returnDocument(ga);
		Document[] retDocs = new Document[]{attachmentDocument, parentArtifactDoc};
		return retDocs;
	}

	/**
	 * Creates the artifact represented by the GenericArtifact object on the
	 * target SFEE system
	 *
	 * @param ga -
	 *            the GenericArtifact object
	 * @param tracker -
	 *            The target SFEE tracker ID
	 * @param connection -
	 *            The Connection object for the target SFEE system
	 * @return - the newly created artifact's ArtifactSoapDO object
	 */
	private ArtifactSoapDO createArtifact(GenericArtifact ga, String tracker,
			Connection connection) {
		ArrayList<String> flexFieldNames = new ArrayList<String>();
		ArrayList<String> flexFieldTypes = new ArrayList<String>();
		ArrayList<Object> flexFieldValues = new ArrayList<Object>();

		List<GenericArtifactField> gaFields = ga
				.getAllGenericArtifactFieldsWithSameFieldType(GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
		String targetSystemTimezone = ga.getTargetSystemTimezone();
		if (gaFields != null) {
			for (GenericArtifactField gaField : gaFields) {
				String fieldName = gaField.getFieldName();
				String trackerFieldValueType = ArtifactMetaData
						.getSFEEFieldValueTypeForGAFieldType(gaField
								.getFieldValueType());
				flexFieldNames.add(fieldName);
				flexFieldTypes.add(trackerFieldValueType);
				Object value = null;
				FieldValueTypeValue fieldType = gaField.getFieldValueType();
				if(trackerFieldValueType.equals(TrackerFieldSoapDO.FIELD_TYPE_DATE)){
					if(fieldType == FieldValueTypeValue.DATE){
						GregorianCalendar gc = (GregorianCalendar) gaField.getFieldValue();
						Date dateValue = gc.getTime();
						if(DateUtil.isAbsoluteDateInTimezone(dateValue, "GMT")){
							value = DateUtil.convertGMTToTimezoneAbsoluteDate(dateValue, targetSystemTimezone);
						}
						else {
							value = dateValue;
						}
					}
					else if(fieldType == FieldValueTypeValue.DATETIME){
						value = gaField.getFieldValue();
					}
				}
				else {
					value = gaField.getFieldValue();
				}
				flexFieldValues.add(value);
			}
		}

		String folderId = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.folderId.getFieldName(), ga);
		String description = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.description.getFieldName(), ga);
		String category = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.category.getFieldName(), ga);
		String group = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.group.getFieldName(), ga);
		String status = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.status.getFieldName(), ga);
		String statusClass = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.statusClass.getFieldName(), ga);
		String customer = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.customer.getFieldName(), ga);
		int priority = GenericArtifactHelper.getIntMandatoryGAField(
				ArtifactMetaData.SFEEFields.priority.getFieldName(), ga);
		int estimatedHours = GenericArtifactHelper.getIntMandatoryGAField(
				ArtifactMetaData.SFEEFields.estimatedHours.getFieldName(), ga);
		int actualHours = GenericArtifactHelper.getIntMandatoryGAField(
				ArtifactMetaData.SFEEFields.actualHours.getFieldName(), ga);
		Date closeDate = GenericArtifactHelper.getDateMandatoryGAField(
				ArtifactMetaData.SFEEFields.closeDate.getFieldName(), ga);
		String assignedTo = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.assignedTo.getFieldName(), ga);
		String reportedReleaseId = GenericArtifactHelper
				.getStringMandatoryGAField(
						ArtifactMetaData.SFEEFields.reportedReleaseId
								.getFieldName(), ga);
		String resolvedReleaseId = GenericArtifactHelper
				.getStringMandatoryGAField(
						ArtifactMetaData.SFEEFields.resolvedReleaseId
								.getFieldName(), ga);
		String title = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.title.getFieldName(), ga);
		String[] comments = this.getComments(ga);
		ArtifactSoapDO result = null;
		try {
			result = trackerHandler.createArtifact(connection.getSessionId(),
					folderId, description, category, group, status,
					statusClass, customer, priority, estimatedHours,
					actualHours, closeDate, assignedTo, reportedReleaseId,
					resolvedReleaseId, flexFieldNames, flexFieldValues,
					flexFieldTypes, title, comments);
			log.info("New artifact "+result.getId()
					+" is created with the change from " + ga.getSourceArtifactId());
		} catch (RemoteException e) {
			String cause = "While trying to create an artifact within SFEE, an error occured";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			throw new CCFRuntimeException(cause, e);
		}
		return result;
	}

	private void populateTargetArtifactAttributes(GenericArtifact ga,
			ArtifactSoapDO result) {
		ga.setTargetArtifactId(result.getId());
		Date targetArtifactLastModifiedDate = result.getLastModifiedDate();
		String targetArtifactLastModifiedDateStr = DateUtil
				.format(targetArtifactLastModifiedDate);
		ga.setTargetArtifactLastModifiedDate(targetArtifactLastModifiedDateStr);
		ga.setTargetArtifactVersion(Integer.toString(result.getVersion()));
	}

	/**
	 * Creates the artifact represented by the GenericArtifact object on the
	 * target SFEE system
	 *
	 * @param ga
	 * @param tracker
	 * @param forceOverride
	 * @param connection
	 * @return - returns the updated artifact's ArtifactSoapDO object
	 */
	private ArtifactSoapDO updateArtifact(GenericArtifact ga, String tracker, Connection connection) {
		String id = ga.getTargetArtifactId();
		ArrayList<String> flexFieldNames = new ArrayList<String>();
		ArrayList<String> flexFieldTypes = new ArrayList<String>();
		ArrayList<Object> flexFieldValues = new ArrayList<Object>();
		List<GenericArtifactField> gaFields = ga
				.getAllGenericArtifactFieldsWithSameFieldType(GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
		String targetSystemTimezone = ga.getTargetSystemTimezone();
		if (gaFields != null) {
			for (GenericArtifactField gaField : gaFields) {
				String fieldName = gaField.getFieldName();
				String trackerFieldValueType = ArtifactMetaData
						.getSFEEFieldValueTypeForGAFieldType(gaField
								.getFieldValueType());
				if (trackerFieldValueType.equals("SfUser")) {
					trackerFieldValueType = TrackerFieldSoapDO.FIELD_VALUE_TYPE_USER;
				}
				flexFieldNames.add(fieldName);
				flexFieldTypes.add(trackerFieldValueType);
				Object value = null;
				FieldValueTypeValue fieldType = gaField.getFieldValueType();
				if(trackerFieldValueType.equals(TrackerFieldSoapDO.FIELD_TYPE_DATE)){
					if(fieldType == FieldValueTypeValue.DATE){
						GregorianCalendar gc = (GregorianCalendar) gaField.getFieldValue();
						Date dateValue = gc.getTime();
						if(DateUtil.isAbsoluteDateInTimezone(dateValue, "GMT")){
							value = DateUtil.convertGMTToTimezoneAbsoluteDate(dateValue, targetSystemTimezone);
						}
						else {
							value = dateValue;
						}
					}
					else if(fieldType == FieldValueTypeValue.DATETIME){
						value = gaField.getFieldValue();
					}
				}
				else {
					value = gaField.getFieldValue();
				}
				flexFieldValues.add(value);
			}
		}

		String folderId = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.folderId.getFieldName(), ga);
		String description = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.description.getFieldName(), ga);
		String category = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.category.getFieldName(), ga);
		String group = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.group.getFieldName(), ga);
		String status = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.status.getFieldName(), ga);
		String statusClass = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.statusClass.getFieldName(), ga);
		String customer = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.customer.getFieldName(), ga);
		int priority = GenericArtifactHelper.getIntMandatoryGAField(
				ArtifactMetaData.SFEEFields.priority.getFieldName(), ga);
		int estimatedHours = GenericArtifactHelper.getIntMandatoryGAField(
				ArtifactMetaData.SFEEFields.estimatedHours.getFieldName(), ga);
		int actualHours = GenericArtifactHelper.getIntMandatoryGAField(
				ArtifactMetaData.SFEEFields.actualHours.getFieldName(), ga);
		Date closeDate = GenericArtifactHelper.getDateMandatoryGAField(
				ArtifactMetaData.SFEEFields.closeDate.getFieldName(), ga);
		String assignedTo = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.assignedTo.getFieldName(), ga);
		String reportedReleaseId = GenericArtifactHelper
				.getStringMandatoryGAField(
						ArtifactMetaData.SFEEFields.reportedReleaseId
								.getFieldName(), ga);
		String resolvedReleaseId = GenericArtifactHelper
				.getStringMandatoryGAField(
						ArtifactMetaData.SFEEFields.resolvedReleaseId
								.getFieldName(), ga);
		String title = GenericArtifactHelper.getStringMandatoryGAField(
				ArtifactMetaData.SFEEFields.title.getFieldName(), ga);
		String[] comments = this.getComments(ga);
		ArtifactSoapDO result = null;
		try {
			result = trackerHandler.updateArtifact(ga, connection
					.getSessionId(), folderId, description, category, group,
					status, statusClass, customer, priority, estimatedHours,
					actualHours, closeDate, assignedTo, reportedReleaseId,
					resolvedReleaseId, flexFieldNames, flexFieldValues,
					flexFieldTypes, title, id, comments);
			if(result != null) {
				log.info("Artifact "+id+ " is updated successfully");
			}
		} catch (RemoteException e) {
			String cause = "While trying to update an artifact within SFEE, an error occured";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			throw new CCFRuntimeException(cause, e);
		}
		return result;
	}

	public Connection connect(GenericArtifact ga) {
		String targetSystemId = ga.getTargetSystemId();
		String targetSystemKind = ga.getTargetSystemKind();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetRepositoryKind = ga.getTargetRepositoryKind();
		Connection connection;
		try {
			if (ga.getArtifactType().equals(
					GenericArtifact.ArtifactTypeValue.ATTACHMENT)
					|| (!ga.getArtifactAction().equals(
							GenericArtifact.ArtifactActionValue.CREATE))
					|| getResyncUserName() == null) {
				connection = connect(targetSystemId, targetSystemKind,
						targetRepositoryId, targetRepositoryKind, serverUrl,
						getUsername() + SFEEConnectionFactory.PARAM_DELIMITER
								+ getPassword(), false);
			} else {
				connection = connect(targetSystemId, targetSystemKind,
						targetRepositoryId, targetRepositoryKind, serverUrl,
						getResyncUserName()
								+ SFEEConnectionFactory.PARAM_DELIMITER
								+ getResyncPassword(), true);
			}
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SFEE system. Max connections reached for "
					+ serverUrl;
			log.error(cause, e);
			ga
					.setErrorCode(GenericArtifact.ERROR_MAX_CONNECTIONS_REACHED_FOR_POOL);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SFEE system "
					+ serverUrl;
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
			throw new CCFRuntimeException(cause, e);
		}
		return connection;
	}

	public Connection connect(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo, boolean forceResync)
			throws MaxConnectionsReachedException, ConnectionException {
		Connection connection = null;
		ConnectionManager<Connection> connectionManager = (ConnectionManager<Connection>) getConnectionManager();
		if (forceResync) {
			connection = connectionManager.getConnectionToCreateArtifact(
					systemId, systemKind, repositoryId, repositoryKind,
					connectionInfo, credentialInfo);
		} else {
			connection = connectionManager
					.getConnectionToUpdateOrExtractArtifact(systemId,
							systemKind, repositoryId, repositoryKind,
							connectionInfo, credentialInfo);
		}
		return connection;
	}

	private void disconnect(Connection connection) {
		ConnectionManager<Connection> connectionManager = (ConnectionManager<Connection>) getConnectionManager();
		connectionManager.releaseConnection(connection);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Validate whether all mandatory properties are set correctly
	 */
	public void validate(List exceptions) {
		super.validate(exceptions);

		if (getResyncUserName() == null) {
			log
					.warn("resyncUserName-property has not been set, so that initial resyncs after artifact creation are not possible.");
		}

		if (getUpdateComment() == null) {
			log.error("updateComment-property not set");
			exceptions.add(new ValidationException(
					"updateComment-property not set", this));
		}

		if (getPassword() == null) {
			log.error("password-property not set");
			exceptions.add(new ValidationException("password-property not set",
					this));
		}

		if (getUsername() == null) {
			log.error("userName-property not set");
			exceptions.add(new ValidationException("userName-property not set",
					this));
		}

		if (getServerUrl() == null) {
			log.error("serverUrl-property not set");
			exceptions.add(new ValidationException(
					"serverUrl-property not set", this));
		}

		ConnectionManager<Connection> connectionManager = getConnectionManager();

		if (exceptions.size() == 0) {
			trackerHandler = new SFEETrackerHandler(getServerUrl(),
					connectionManager);
			attachmentHandler = new SFEEAttachmentHandler(getServerUrl(),
					connectionManager);
		}
	}

	/**
	 * Reset processor
	 */
	public void reset(Object context) {
	}

	private String[] getComments(GenericArtifact ga) {
		String[] comments = null;
		List<GenericArtifactField> gaFields = ga
				.getAllGenericArtifactFieldsWithSameFieldTypeAndFieldName(
						GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD,
						ArtifactMetaData.SFEEFields.commentText.getFieldName());
		int commentsSize = 0;
		if (gaFields != null) {
			commentsSize = gaFields.size();
		}
		if (commentsSize == 0) {
			comments = new String[] { this.getUpdateComment() };
		} else {
			comments = new String[commentsSize];
			for (int i = 0; i < commentsSize; i++) {
				GenericArtifactField field = gaFields.get(i);
				String comment = (String) field.getFieldValue();
				comments[i] = comment;
			}
		}
		return comments;
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
	 * Returns the server URL of the CSFE/SFEE system that is configured in the
	 * wiring file.
	 *
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Sets the CSFE/SFEE system's SOAP server URL.
	 *
	 * @param serverUrl -
	 *            the URL of the source SFEE system.
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	@Override
	public Document createDependency(Document gaDocument) {
		throw new CCFRuntimeException("createDependency is not implemented...!");
	}

	@Override
	public Document deleteArtifact(Document gaDocument) {
		throw new CCFRuntimeException("deleteArtifact is not implemented...!");
	}

	@Override
	public Document deleteAttachment(Document gaDocument) {
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(gaDocument);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			XPathUtils.addAttribute(gaDocument.getRootElement(),
					GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		this.initializeArtifact(ga);
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetArtifactId = ga.getTargetArtifactId();
		String artifactId = ga.getDepParentTargetArtifactId();
		String tracker = targetRepositoryId;
		Connection connection = null;
		try {
			connection = connect(ga);
			attachmentHandler.deleteAttachment(connection, targetArtifactId, artifactId, ga);
			log.info("Attachment "+targetArtifactId + "is deleted");
		} catch (RemoteException e) {
			String message = "Exception while deleting attachment "+artifactId;
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} finally {
			if(connection != null) {
				this.disconnect(connection);
			}
		}
		Document returnDocument = null;;
		try {
			returnDocument = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
		} catch (GenericArtifactParsingException e) {
			String message = "Exception while deleting attachment "
				+artifactId +". Could not parse Generic artifact";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		}
		return returnDocument;
	}

	@Override
	public Document deleteDependency(Document gaDocument) {
		throw new CCFRuntimeException("deleteDependency is not implemented...!");
	}

	@Override
	public Document updateAttachment(Document gaDocument) {
		throw new CCFRuntimeException("updateAttachment is not implemented...!");
	}

	@Override
	public Document updateDependency(Document gaDocument) {
		throw new CCFRuntimeException("updateDependency is not implemented...!");
	}

	/**
	 * Sets the optional resync username
	 *
	 * The resync user name is used to login into the SFEE/CSFE instance
	 * whenever an artifact should be created. This user has to differ from the
	 * ordinary user used to log in in order to force initial resyncs with the
	 * source system once a new artifact has been created.
	 *
	 * @param resyncUserName
	 *            the resyncUserName to set
	 */
	public void setResyncUserName(String resyncUserName) {
		this.resyncUserName = resyncUserName;
	}

	/**
	 * Gets the optional resync username The resync user name is used to login
	 * into the SFEE/CSFE instance whenever an artifact should be created. This
	 * user has to differ from the ordinary user used to log in in order to
	 * force initial resyncs with the source system once a new artifact has been
	 * created.
	 *
	 * @return the resyncUserName
	 */
	private String getResyncUserName() {
		return resyncUserName;
	}

	/**
	 * Sets the optional resync password that belongs to the resync user
	 *
	 * @param resyncPassword
	 *            the resyncPassword to set
	 */
	public void setResyncPassword(String resyncPassword) {
		this.resyncPassword = resyncPassword;
	}

	/**
	 * Gets the optional resync password that belongs to the resync user
	 *
	 * @return the resyncPassword
	 */
	private String getResyncPassword() {
		return resyncPassword;
	}

	/**
	 * Gets the mandatory password that belongs to the username
	 *
	 * @return the password
	 */
	private String getPassword() {
		return password;
	}

	/**
	 * Sets the password that belongs to the username
	 *
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the manadtory user name The user name is used to login into the
	 * SFEE/CSFE instance whenever an artifact should be updated or extracted.
	 * This user has to differ from the resync user in order to force initial
	 * resyncs with the source system once a new artifact has been created.
	 *
	 * @return the userName
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the mandatory username
	 *
	 * The user name is used to login into the SFEE/CSFE instance whenever an
	 * artifact should be updated or extracted. This user has to differ from the
	 * resync user in order to force initial resyncs with the source system once
	 * a new artifact has been created.
	 *
	 * @param resyncUserName
	 *            the resyncUserName to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
