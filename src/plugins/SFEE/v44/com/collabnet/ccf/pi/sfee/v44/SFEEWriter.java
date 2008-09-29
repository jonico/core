package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.sfee.v44.meta.ArtifactMetaData;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

/**
 * This component is responsible for writing SFEE tracker items encoded in the
 * generic XML artifact format back to the SFEE tracker
 * 
 * @author jnicolai
 * 
 */
public class SFEEWriter extends LifecycleComponent implements
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
	
	private ConnectionManager<Connection> connectionManager = null;
	
	private String serverUrl;

	private String password;

	private String username;

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
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(data);
	    } catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
	    GenericArtifact.ArtifactActionValue artifactAction = ga.getArtifactAction();
	    GenericArtifact.ArtifactTypeValue artifactType = ga.getArtifactType();
		String sourceArtifactId = ga.getSourceArtifactId();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetArtifactId = ga.getTargetArtifactId();
		String tracker = targetRepositoryId;
		if(sourceArtifactId.equalsIgnoreCase("Unknown")){
			return new Object[]{data};
		}
		
		if(artifactAction == GenericArtifact.ArtifactActionValue.UPDATE){
			if(SFEEGAHelper.containsSingleMandatoryField(ga, ArtifactMetaData.SFEEFields.id.getFieldName())){
				SFEEGAHelper.updateSingleMandatoryField(ga, 
						ArtifactMetaData.SFEEFields.id.getFieldName(), targetArtifactId);
				ga.setTargetArtifactId(targetArtifactId);
			}
			else {
				SFEEGAHelper.addField(ga, ArtifactMetaData.SFEEFields.id.getFieldName(), targetArtifactId,
						GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
						GenericArtifactField.FieldValueTypeValue.STRING);
			}
		}
		if(SFEEGAHelper.containsSingleMandatoryField(ga, ArtifactMetaData.SFEEFields.folderId.getFieldName())){
			SFEEGAHelper.updateSingleMandatoryField(ga, ArtifactMetaData.SFEEFields.folderId.getFieldName(), tracker);
		}
		else {
			SFEEGAHelper.addField(ga, ArtifactMetaData.SFEEFields.folderId.getFieldName(), tracker,
					GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
					GenericArtifactField.FieldValueTypeValue.STRING);
		}


		// this field is probably added from a read processor or read connector
		// component
		Boolean forceOverride = true;
		

		// check whether we should create or update the artifact
		// TODO This has to be done on the artifactAction, not on the id value
		ArtifactSoapDO result = null;
		if(artifactType == GenericArtifact.ArtifactTypeValue.PLAINARTIFACT){
			if (artifactAction == GenericArtifact.ArtifactActionValue.CREATE) {
				Connection connection = connect(ga);
				try {
					result = this.createArtifact(ga, tracker, connection);
					// update Id field after creating the artifact
					targetArtifactId = result.getId();
					SFEEGAHelper.addField(ga, ArtifactMetaData.SFEEFields.id.getFieldName(), targetArtifactId,
							GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
							GenericArtifactField.FieldValueTypeValue.STRING);
					// this is already done in the populate target artifact method
					//ga.setTargetArtifactId(targetArtifactId);
				} catch (NumberFormatException e) {
					log.error("Wrong data format of attribute for artifact "
							+ data.asXML(), e);
					return null;
				} finally {
					disconnect(connection);
				}
			}
			else if(artifactAction == GenericArtifact.ArtifactActionValue.UPDATE) {
				Connection connection = connect(ga);
				try {
					// update token or do conflict resolution
					result = this.updateArtifact(ga, tracker, forceOverride, connection);
					if (result == null) {
						// conflict resolution has decided in favor of the
						// target copy
						return new Object[]{ga};
					}
				} catch (NumberFormatException e) {
					log.error("Wrong data format of attribute for artifact "
							+ data.asXML(), e);
					return null;
				} finally {
					disconnect(connection);
				}
			}
			else if(artifactAction == GenericArtifact.ArtifactActionValue.DELETE){
				// INFO Delete is not yet implemented
//				trackerHandler.removeArtifact(getSessionId(),
//							soapDoObj.getId());
			}
			if(result != null){
				this.populateTargetArtifactAttributes(ga, result);
			}
		}
		else if(artifactType == GenericArtifact.ArtifactTypeValue.ATTACHMENT){
			Connection connection = connect(ga);
			String targetParentArtifactId = ga.getDepParentTargetArtifactId();
			try {
				attachmentHandler.handleAttachment(connection.getSessionId(), ga,
						targetParentArtifactId, this.getUsername(),connection.getSfSoap());
			} catch (RemoteException e) {
				String cause = "Problem occured while creating attachments in SFEE";
				log.error(cause, e);
				ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
				throw new CCFRuntimeException(cause, e);
			} finally {
				disconnect(connection);
			}
		}
		Document document = null;
		try {
			document = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		Object[] resultDocs = { document };
		return resultDocs;
	}
	
	/**
	 * Creates the artifact represented by the GenericArtifact object
	 * on the target SFEE system
	 * 
	 * @param ga - the GenericArtifact object
	 * @param tracker - The target SFEE tracker ID
	 * @param connection - The Connection object for the target SFEE system
	 * @return - the newly created artifact's ArtifactSoapDO object
	 */
	private ArtifactSoapDO createArtifact(GenericArtifact ga, String tracker, Connection connection){
		ArrayList<String> flexFieldNames = new ArrayList<String>();
		ArrayList<String> flexFieldTypes = new ArrayList<String>();
		ArrayList<Object> flexFieldValues = new ArrayList<Object>();
		
		List<GenericArtifactField> gaFields = ga.getAllGenericArtifactFieldsWithSameFieldType(GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
		if(gaFields != null){
			for(GenericArtifactField gaField:gaFields){	
				String fieldName = gaField.getFieldName();
				String trackerFieldValueType = ArtifactMetaData.getSFEEFieldValueTypeForGAFieldType(gaField.getFieldValueType());
				flexFieldNames.add(fieldName);
				flexFieldTypes.add(trackerFieldValueType);
				Object value = gaField.getFieldValue();
				flexFieldValues.add(value);
			}
		}
		
		String folderId = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.folderId.getFieldName(), ga);
		String description = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.description.getFieldName(), ga);
		String category = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.category.getFieldName(), ga);
		String group = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.group.getFieldName(), ga);
		String status = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.status.getFieldName(), ga);
		String statusClass = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.statusClass.getFieldName(), ga);
		String customer = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.customer.getFieldName(), ga);
		int priority = GenericArtifactHelper.getIntMandatoryGAField(ArtifactMetaData.SFEEFields.priority.getFieldName(), ga);
		int estimatedHours = GenericArtifactHelper.getIntMandatoryGAField(ArtifactMetaData.SFEEFields.estimatedHours.getFieldName(), ga);
		int actualHours = GenericArtifactHelper.getIntMandatoryGAField(ArtifactMetaData.SFEEFields.actualHours.getFieldName(), ga);
		Date closeDate = GenericArtifactHelper.getDateMandatoryGAField(ArtifactMetaData.SFEEFields.closeDate.getFieldName(), ga);
		String assignedTo = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.assignedTo.getFieldName(), ga);
		String reportedReleaseId = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.reportedReleaseId.getFieldName(), ga);
		String resolvedReleaseId = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.resolvedReleaseId.getFieldName(), ga);
		String title = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.title.getFieldName(), ga);
		String[] comments = this.getComments(ga);
		ArtifactSoapDO result = null;
		try {
			result = trackerHandler
			.createArtifact(
					connection.getSessionId(),
					folderId,
					description,
					category,
					group,
					status,
					statusClass,
					customer,
					priority,
					estimatedHours,
					actualHours,
					closeDate,
					assignedTo,
					reportedReleaseId,
					resolvedReleaseId,
					flexFieldNames,
					flexFieldValues,
					flexFieldTypes,
					title,
					comments);
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
		String targetArtifactLastModifiedDateStr = DateUtil.format(targetArtifactLastModifiedDate);
		ga.setTargetArtifactLastModifiedDate(targetArtifactLastModifiedDateStr);
		ga.setTargetArtifactVersion(Integer.toString(result.getVersion()));
	}

	/**
	 * Creates the artifact represented by the GenericArtifact object
	 * on the target SFEE system
	 * 
	 * @param ga
	 * @param tracker
	 * @param forceOverride
	 * @param connection
	 * @return - returns the updated artifact's ArtifactSoapDO object
	 */
	private ArtifactSoapDO updateArtifact(GenericArtifact ga, String tracker, boolean forceOverride, Connection connection){
		//String id = SFEEWriter.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.id, ga);
		String id = ga.getTargetArtifactId();
		String conflictResolutionPriority = ga.getConflictResolutionPriority();
		ArtifactSoapDO currentTargetArtifact = null;
		try {
			currentTargetArtifact = 
				trackerHandler.getTrackerItem(connection.getSessionId(), id);
		} catch (RemoteException e) {
			String cause = "While trying to get the current target artifact (before updating), an error occured";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
			throw new CCFRuntimeException(cause, e);
		}
		int currentAritfactVersion = currentTargetArtifact.getVersion();
		String artifactVersionFromTable = ga.getTargetArtifactVersion();
		int artifactVersionIntFromTable = -1;
		if (!artifactVersionFromTable.equals(GenericArtifact.VALUE_UNKNOWN)) {
			artifactVersionIntFromTable= Integer.parseInt(artifactVersionFromTable);
		}
		if(artifactVersionIntFromTable < currentAritfactVersion){
			//We have a conflict here. The target artifact is modified
			log.warn("The target artifact is modified. Indicates a conflict...!!!");
			if(conflictResolutionPriority.equals(
					GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_IGNORE)){
				log.warn("Conflict Resolution Priority favours ignoring the shipment: "
						+conflictResolutionPriority);
				log.warn("Ignoring the update for artifact "+id);
				return currentTargetArtifact;
			}
			else if(conflictResolutionPriority.equals(
					GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_OVERRIDE)){
				log.warn("Conflict Resolution Priority favours overriding the changes: "
						+conflictResolutionPriority);
				log.warn("Overriding the data for artifact "+id);
			}
		}
		ArrayList<String> flexFieldNames = new ArrayList<String>();
		ArrayList<String> flexFieldTypes = new ArrayList<String>();
		ArrayList<Object> flexFieldValues = new ArrayList<Object>();
		List<GenericArtifactField> gaFields = ga.getAllGenericArtifactFieldsWithSameFieldType(GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
		if(gaFields != null){
			for(GenericArtifactField gaField:gaFields){
				String fieldName = gaField.getFieldName();
				String trackerFieldValueType = 
					ArtifactMetaData.getSFEEFieldValueTypeForGAFieldType(gaField.getFieldValueType());
				if(trackerFieldValueType.equals("SfUser")){
					trackerFieldValueType = TrackerFieldSoapDO.FIELD_VALUE_TYPE_USER;
				}
				flexFieldNames.add(fieldName);
				flexFieldTypes.add(trackerFieldValueType);
				Object value = gaField.getFieldValue();
				flexFieldValues.add(value);
			}
		}
		
		String folderId = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.folderId.getFieldName(), ga);
		String description = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.description.getFieldName(), ga);
		String category = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.category.getFieldName(), ga);
		String group = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.group.getFieldName(), ga);
		String status = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.status.getFieldName(), ga);
		String statusClass = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.statusClass.getFieldName(), ga);
		String customer = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.customer.getFieldName(), ga);
		int priority = GenericArtifactHelper.getIntMandatoryGAField(ArtifactMetaData.SFEEFields.priority.getFieldName(), ga);
		int estimatedHours = GenericArtifactHelper.getIntMandatoryGAField(ArtifactMetaData.SFEEFields.estimatedHours.getFieldName(), ga);
		int actualHours = GenericArtifactHelper.getIntMandatoryGAField(ArtifactMetaData.SFEEFields.actualHours.getFieldName(), ga);
		Date closeDate = GenericArtifactHelper.getDateMandatoryGAField(ArtifactMetaData.SFEEFields.closeDate.getFieldName(), ga);
		String assignedTo = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.assignedTo.getFieldName(), ga);
		String reportedReleaseId = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.reportedReleaseId.getFieldName(), ga);
		String resolvedReleaseId = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.resolvedReleaseId.getFieldName(), ga);
		String title = GenericArtifactHelper.getStringMandatoryGAField(ArtifactMetaData.SFEEFields.title.getFieldName(), ga);
		String[] comments = this.getComments(ga);
		ArtifactSoapDO result = null;
		try {
			result = trackerHandler
			.updateArtifact(
					connection.getSessionId(),
					folderId,
					description,
					category,
					group,
					status,
					statusClass,
					customer,
					priority,
					estimatedHours,
					actualHours,
					closeDate,
					assignedTo,
					reportedReleaseId,
					resolvedReleaseId,
					flexFieldNames,
					flexFieldValues,
					flexFieldTypes,
					title,
					id,
					comments,
					forceOverride);
		} catch (RemoteException e) {
			String cause = "While trying to update an artifact within SFEE, an error occured";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			throw new CCFRuntimeException(cause, e);
		}
		return result;
	}
	
	public Connection connect(GenericArtifact ga){
		String targetSystemId = ga.getTargetSystemId();
		String targetSystemKind = ga.getTargetSystemKind();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetRepositoryKind = ga.getTargetRepositoryKind();
		Connection connection;
		try {
			connection = connect(targetSystemId, targetSystemKind, targetRepositoryId,
					targetRepositoryKind, serverUrl, 
					username+SFEEConnectionFactory.PARAM_DELIMITER+password);
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SFEE system. Max connections reached for "+
								serverUrl;
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_MAX_CONNECTIONS_REACHED_FOR_POOL);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SFEE system "+
								serverUrl;
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
			throw new CCFRuntimeException(cause, e);
		}
		return connection;
	}

	public Connection connect(String systemId, String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo, String credentialInfo) throws MaxConnectionsReachedException, ConnectionException {
		//log.info("Before calling the parent connect()");
		//super.connect();
		Connection connection = null;
		connection = connectionManager.getConnection(systemId, systemKind, repositoryId,
				repositoryKind, connectionInfo, credentialInfo);
		return connection;
	}
	
	private void disconnect(Connection connection) {
		// TODO Auto-generated method stub
		connectionManager.releaseConnection(connection);
	}
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Validate whether all mandatory properties are set correctly
	 */
	public void validate(List exceptions) {
		super.validate(exceptions);

		if (getUpdateComment() == null) {
			log.error("updateComment-property no set");
			exceptions.add(new ValidationException(
					"updateComment-property not set", this));
		}

		trackerHandler = new SFEETrackerHandler(getServerUrl(),connectionManager);
		attachmentHandler = new SFEEAttachmentHandler(getServerUrl(), connectionManager);
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
	
	private String[] getComments(GenericArtifact ga){
		String[] comments = null;
		List<GenericArtifactField> gaFields = 
			ga.getAllGenericArtifactFieldsWithSameFieldTypeAndFieldName(
					GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD,
					ArtifactMetaData.SFEEFields.commentText.getFieldName());
		int commentsSize = 0;
		if(gaFields != null){
			commentsSize = gaFields.size();
		}
		if(commentsSize == 0){
			comments = new String[]{this.getUpdateComment()};
		}
		else {
			comments = new String[commentsSize];
			for(int i=0; i < commentsSize; i++){
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

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ConnectionManager<Connection> getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(ConnectionManager<Connection> connectionManager) {
		this.connectionManager = connectionManager;
	}
}
