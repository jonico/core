package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

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
		} catch (GenericArtifactParsingException e2) {
			throw new RuntimeException(e2);
		}
		//String trackerId = ga.getTargetRepositoryId();
		//String sourceRepositoryId = ga.getSourceRepositoryId();
		String sourceArtifactId = ga.getSourceArtifactId();
//		String sourceSystemId = ga.getSourceSystemId();
//		String sourceSystemKind = ga.getSourceSystemKind();
//		String targetSystemId = ga.getTargetSystemId();
//		String targetSystemKind = ga.getTargetSystemKind();
//		String sourceRepositoryKind = ga.getSourceRepositoryKind();
//		String targetRepositoryKind = ga.getTargetRepositoryKind();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetArtifactId = ga.getTargetArtifactId();
		String tracker = targetRepositoryId;
		if(sourceArtifactId.equalsIgnoreCase("Unknown")){
			return new Object[]{data};
		}
		

		if(!SFEEGAHelper.containsSingleField(ga, ArtifactMetaData.SFEEFields.id.getFieldName())){
			SFEEGAHelper.addField(ga, ArtifactMetaData.SFEEFields.id.getFieldName(), getCreateToken(), "String");
		}
		if(!StringUtils.isEmpty(targetArtifactId)){
			SFEEGAHelper.updateSingleField(ga, 
					ArtifactMetaData.SFEEFields.id.getFieldName(), targetArtifactId);
			ga.setTargetArtifactId(targetArtifactId);
		}
		if(SFEEGAHelper.containsSingleField(ga, ArtifactMetaData.SFEEFields.folderId.getFieldName())){
			SFEEGAHelper.updateSingleField(ga, ArtifactMetaData.SFEEFields.folderId.getFieldName(), tracker);
		}
		else {
			SFEEGAHelper.addField(ga, ArtifactMetaData.SFEEFields.folderId.getFieldName(), tracker, "String");
		}

		
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
		String id = (String) SFEEGAHelper.getSingleValue(ga, ArtifactMetaData.SFEEFields.id.getFieldName());
		ArtifactSoapDO result = null;
		if(ga.getArtifactType() != GenericArtifact.ArtifactTypeValue.ATTACHMENT){
			if ((StringUtils.isEmpty(id)) || SFEEGAHelper.getSingleValue(ga, 
					ArtifactMetaData.SFEEFields.id.getFieldName()).equals("NEW")) {
				// find out whether we should delete something, that is not even
				// present here
				if (deleteArtifact.booleanValue()) {
					log
							.warn("Cannot delete an artifact that is not even mirrored (yet): "
									+ data.asXML());
					return null;
				}
				Connection connection = connect(ga);
				// TODO apply a better type conversion concept here
				try {
	
					result = this.createArtifact(ga, tracker, connection);
	
					// update Id field after creating the artifact
					targetArtifactId = result.getId();
					SFEEGAHelper.updateSingleField(ga, ArtifactMetaData.SFEEFields.id.getFieldName(), targetArtifactId);
					ga.setTargetArtifactId(targetArtifactId);
				} catch (NumberFormatException e) {
					log.error("Wrong data format of attribute for artifact "
							+ data.asXML(), e);
					return null;
				} finally {
					disconnect(connection);
				}
			} else {
				Connection connection = connect(ga);
				try {
					if (deleteArtifact.booleanValue()) {
	//					trackerHandler.removeArtifact(getSessionId(),
	//							soapDoObj.getId());
					} else {
						// update token or do conflict resolution
						// TODO apply a better type conversion concept here
						result = this.updateArtifact(ga, tracker, forceOverride, connection);
						if (result == null) {
							// conflict resolution has decided in favor of the
							// target copy
							disconnect(connection);
							return new Object[0];
						}
					}
				} catch (NumberFormatException e) {
					log.error("Wrong data format of attribute for artifact "
							+ data.asXML(), e);
					return null;
				} finally {
					disconnect(connection);
				}
			}
		}
		else {
			Connection connection = connect(ga);
			try {
				attachmentHandler.handleAttachment(connection.getSessionId(), ga,
						targetArtifactId, this.getUsername());
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				disconnect(connection);
			}
		}
		Document document = null;
		try {
			document = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
		} catch (GenericArtifactParsingException e) {
			throw new RuntimeException(e);
		}
		Object[] resultDocs = { document };
		return resultDocs;
	}
	
	private ArtifactSoapDO createArtifact(GenericArtifact ga, String tracker, Connection connection){
		TrackerFieldSoapDO[] flexFields = null;
		try {
			flexFields = trackerHandler.getFlexFields(connection.getSessionId(), tracker);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<String> flexFieldNames = new ArrayList<String>();
		ArrayList<String> flexFieldTypes = new ArrayList<String>();
		ArrayList<Object> flexFieldValues = new ArrayList<Object>();
		if(flexFields != null) {
			for(TrackerFieldSoapDO flexField: flexFields){
				String fieldName = flexField.getName();
				List<GenericArtifactField> gaFields = ga.getAllGenericArtifactFieldsWithSameFieldName(fieldName);
				if(gaFields != null){
					for(GenericArtifactField gaField:gaFields){
						flexFieldNames.add(fieldName);
						flexFieldTypes.add(flexField.getFieldType());
						Object value = gaField.getFieldValue();
						flexFieldValues.add(value);
					}
				}
				else {
					// Do nothing....
				}
			}
		}
		String folderId = getStringGAField(ArtifactMetaData.SFEEFields.folderId, ga);
		String description = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.description, ga);
		String category = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.category, ga);
		String group = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.group, ga);
		String status = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.status, ga);
		String statusClass = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.statusClass, ga);
		String customer = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.customer, ga);
		int priority = SFEEWriter.getIntGAField(ArtifactMetaData.SFEEFields.category, ga);
		int estimatedHours = SFEEWriter.getIntGAField(ArtifactMetaData.SFEEFields.estimatedHours, ga);
		int actualHours = SFEEWriter.getIntGAField(ArtifactMetaData.SFEEFields.actualHours, ga);
		Date closeDate = SFEEWriter.getDateGAField(ArtifactMetaData.SFEEFields.closeDate, ga);
		String assignedTo = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.assignedTo, ga);
		String reportedReleaseId = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.reportedReleaseId, ga);
		String resolvedReleaseId = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.resolvedReleaseId, ga);
		String title = SFEEWriter.getStringGAField(ArtifactMetaData.SFEEFields.title, ga);
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
					// TODO - Don't know what these mean
					comments,
					getLastSynchronizedWithOtherSystemSFEETargetFieldname());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private ArtifactSoapDO updateArtifact(GenericArtifact ga, String tracker, boolean forceOverride, Connection connection){
		TrackerFieldSoapDO[] flexFields = null;
		try {
			flexFields = trackerHandler.getFlexFields(connection.getSessionId(), tracker);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<String> flexFieldNames = new ArrayList<String>();
		ArrayList<String> flexFieldTypes = new ArrayList<String>();
		ArrayList<Object> flexFieldValues = new ArrayList<Object>();
		if(flexFields != null){
			for(TrackerFieldSoapDO flexField: flexFields){
				String fieldName = flexField.getName();
				List<GenericArtifactField> gaFields = ga.getAllGenericArtifactFieldsWithSameFieldName(fieldName);
				if(gaFields != null){
					for(GenericArtifactField gaField:gaFields){
						flexFieldNames.add(fieldName);
						flexFieldTypes.add(flexField.getFieldType());
						Object value = gaField.getFieldValue();
						flexFieldValues.add(value);
					}
				}
			}
		}
		String id = getStringGAField(ArtifactMetaData.SFEEFields.id, ga);
		String folderId = getStringGAField(ArtifactMetaData.SFEEFields.folderId, ga);
		String description = this.getStringGAField(ArtifactMetaData.SFEEFields.description, ga);
		String category = this.getStringGAField(ArtifactMetaData.SFEEFields.category, ga);
		String group = this.getStringGAField(ArtifactMetaData.SFEEFields.group, ga);
		String status = this.getStringGAField(ArtifactMetaData.SFEEFields.status, ga);
		String statusClass = this.getStringGAField(ArtifactMetaData.SFEEFields.statusClass, ga);
		String customer = this.getStringGAField(ArtifactMetaData.SFEEFields.customer, ga);
		int priority = this.getIntGAField(ArtifactMetaData.SFEEFields.priority, ga);
		int estimatedHours = this.getIntGAField(ArtifactMetaData.SFEEFields.estimatedHours, ga);
		int actualHours = this.getIntGAField(ArtifactMetaData.SFEEFields.actualHours, ga);
		Date closeDate = this.getDateGAField(ArtifactMetaData.SFEEFields.closeDate, ga);
		String assignedTo = this.getStringGAField(ArtifactMetaData.SFEEFields.assignedTo, ga);
		String reportedReleaseId = this.getStringGAField(ArtifactMetaData.SFEEFields.reportedReleaseId, ga);
		String resolvedReleaseId = this.getStringGAField(ArtifactMetaData.SFEEFields.resolvedReleaseId, ga);
		String title = this.getStringGAField(ArtifactMetaData.SFEEFields.title, ga);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public Connection connect(GenericArtifact ga){
		String targetSystemId = ga.getTargetSystemId();
		String targetSystemKind = ga.getTargetSystemKind();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetRepositoryKind = ga.getTargetRepositoryKind();
		Connection connection = connect(targetSystemId, targetSystemKind, targetRepositoryId,
				targetRepositoryKind, serverUrl, 
				username+SFEEConnectionFactory.PARAM_DELIMITER+password);
		return connection;
	}

	public Connection connect(String systemId, String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo, String credentialInfo) {
		log.info("Before calling the parent connect()");
		//super.connect();
		Connection connection = null;
		try {
			connection = connectionManager.getConnection(systemId, systemKind, repositoryId,
					repositoryKind, connectionInfo, credentialInfo);
		} catch (MaxConnectionsReachedException e) {
			e.printStackTrace();
		}
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
	
	public static GenericArtifactField getGAField(String name, GenericArtifact ga){
		List<GenericArtifactField> gaFields = ga.getAllGenericArtifactFieldsWithSameFieldName(name);
		if(gaFields == null || gaFields.size() == 0){
			return null;
		}
		else if(gaFields.size() == 1){
			GenericArtifactField field = gaFields.get(0);
			return field;
		}
		else {
			throw new RuntimeException("This should never be the case");
		}
	}
	
	public static String getStringGAField(ArtifactMetaData.SFEEFields field, GenericArtifact ga){
		String fieldValue = SFEEWriter.getStringGAField(field.getFieldName(), ga);
		return fieldValue;
	}
	
	public static String getStringGAField(String fieldName, GenericArtifact ga){
		String fieldValue = null;
		GenericArtifactField gaField = SFEEWriter.getGAField(fieldName, ga);
		if(gaField != null){
			fieldValue = (String) gaField.getFieldValue();
		}
		return fieldValue;
	}
	
	public static int getIntGAField(ArtifactMetaData.SFEEFields field, GenericArtifact ga){
		int fieldValue = 0;
		GenericArtifactField gaField = SFEEWriter.getGAField(field.getFieldName(), ga);
		if(gaField != null){
			Object fieldValueObj = gaField.getFieldValue();
			if(fieldValueObj instanceof String){
				String fieldValueString = (String) fieldValueObj;
				fieldValue = Integer.parseInt(fieldValueString);
			}
			else if(fieldValueObj instanceof Integer){
				fieldValue = ((Integer) fieldValueObj).intValue();
			}
		}
		return fieldValue;
	}
	
	public static Date getDateGAField(ArtifactMetaData.SFEEFields field, GenericArtifact ga){
		Date fieldValue = null;
		GenericArtifactField gaField = SFEEWriter.getGAField(field.getFieldName(), ga);
		if(gaField != null){
			Object fieldValueObj = gaField.getFieldValue();
			if(fieldValueObj instanceof String){
				String fieldValueString = (String) fieldValueObj;
				fieldValue = DateUtil.parse(fieldValueString);
			}
			else if(fieldValueObj instanceof Date){
				fieldValue = (Date) fieldValueObj;
			}
		}
		return fieldValue;
	}
	
	private String[] getComments(GenericArtifact ga){
		String[] comments = null;
		List<GenericArtifactField> gaFields = 
			ga.getAllGenericArtifactFieldsWithSameFieldName(ArtifactMetaData.SFEEFields.commentText.getFieldName());
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
