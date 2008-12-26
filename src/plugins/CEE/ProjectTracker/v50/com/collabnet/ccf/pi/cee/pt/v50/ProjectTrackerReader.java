package com.collabnet.ccf.pi.cee.pt.v50;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.utils.CollectionUtils;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.core.ws.exception.WSException;
import com.collabnet.tracker.common.ClientArtifact;
import com.collabnet.tracker.common.ClientArtifactAttachment;
import com.collabnet.tracker.common.ClientArtifactComment;
import com.collabnet.tracker.common.ClientArtifactListXMLHelper;
import com.collabnet.tracker.core.TrackerWebServicesClient;
import com.collabnet.tracker.core.model.TrackerArtifactType;
import com.collabnet.tracker.core.model.TrackerAttribute;
import com.collabnet.tracker.ws.ArtifactHistoryList;
import com.collabnet.tracker.ws.ArtifactType;
import com.collabnet.tracker.ws.ArtifactTypeMetadata;
import com.collabnet.tracker.ws.Attribute;
import com.collabnet.tracker.ws.History;
import com.collabnet.tracker.ws.HistoryActivity;
import com.collabnet.tracker.ws.HistoryTransaction;
import com.collabnet.tracker.ws.Option;

public class ProjectTrackerReader extends AbstractReader<TrackerWebServicesClient> implements IDataProcessor {
	private static final Log log = LogFactory.getLog(ProjectTrackerReader.class);
	private String serverUrl = null;

	private String password = null;

	private String username = null;

	private String connectorUserDisplayName = null;
	private HashMap<String, String> movedArtifacts = new HashMap<String, String>();

	public static final String ATTACHMENT_TAG_NAME = "attachment";
	public static final String ATTACHMENT_ADDED_HISTORY_ACTIVITY_TYPE = "FileAddedDesc";
	public static final String ATTACHMENT_DELETED_HISTORY_ACTIVITY_TYPE = "FileDeletedDesc";
	public static final String URL_ADDED_HISTORY_ACTIVITY_TYPE = "UrlDescAdded";
	public static final String URL_DELETED_HISTORY_ACTIVITY_TYPE = "UrlDescDeleted";
	public static final String TRACKER_NAMESPACE = "urn:ws.tracker.collabnet.com";
	public static final String CREATED_ON_FIELD = "createdOn";
	public static final String MODIFIED_ON_FIELD = "modifiedOn";
	public static final String REASON_FIELD = "reason";
	public static final String ID_FIELD = "id";
	public static final String CREATED_BY_FIELD = "createdBy";
	public static final String MODIFIED_BY_FIELD = "modifiedBy";
	public static final String COMMENT_FIELD = "comment";
	public static final String CREATED_ON_FIELD_NAME = "{"+TRACKER_NAMESPACE+"}"+CREATED_ON_FIELD;
	public static final String MODIFIED_ON_FIELD_NAME = "{"+TRACKER_NAMESPACE+"}"+MODIFIED_ON_FIELD;
	public static final String REASON_FIELD_NAME = "{"+TRACKER_NAMESPACE+"}"+REASON_FIELD;
	public static final String ID_FIELD_NAME = "{"+TRACKER_NAMESPACE+"}"+ID_FIELD;
	public static final String CREATED_BY_FIELD_NAME = "{"+TRACKER_NAMESPACE+"}"+CREATED_BY_FIELD;
	public static final String MODIFIED_BY_FIELD_NAME = "{"+TRACKER_NAMESPACE+"}"+MODIFIED_BY_FIELD;
	public static final String COMMENT_FIELD_NAME = "{"+TRACKER_NAMESPACE+"}"+COMMENT_FIELD;
	private MetaDataHelper metadataHelper = MetaDataHelper.getInstance();
	private HashMap<String, String> attahcmentIDNameMap = null;
	// TODO Use ThreadLocal object to store these variables
	private static ThreadLocal<ArtifactHistoryList> artifactHistoryList = new ThreadLocal<ArtifactHistoryList>();
	//private static ThreadLocal<HashMap<String, ClientArtifact>> artifactListTl = new ThreadLocal<HashMap<String, ClientArtifact>>();
	ProjectTrackerHelper ptHelper = ProjectTrackerHelper.getInstance();
	private static HashMap<String, GenericArtifactField.FieldValueTypeValue> ptGATypesMap =
		new HashMap<String, GenericArtifactField.FieldValueTypeValue>();
	static {
		ptGATypesMap.put("SHORT_TEXT", GenericArtifactField.FieldValueTypeValue.STRING);
		ptGATypesMap.put("_SHORT_TEXT", GenericArtifactField.FieldValueTypeValue.STRING);
		ptGATypesMap.put("NUMBER", GenericArtifactField.FieldValueTypeValue.INTEGER);
		ptGATypesMap.put("MULTI_SELECT", GenericArtifactField.FieldValueTypeValue.STRING);
		ptGATypesMap.put("DATE", GenericArtifactField.FieldValueTypeValue.DATE);
		ptGATypesMap.put("STATE", GenericArtifactField.FieldValueTypeValue.STRING);
		ptGATypesMap.put("EMAIL", GenericArtifactField.FieldValueTypeValue.STRING);
		ptGATypesMap.put("LONG_TEXT", GenericArtifactField.FieldValueTypeValue.STRING);
		ptGATypesMap.put("SINGLE_SELECT", GenericArtifactField.FieldValueTypeValue.STRING);
		ptGATypesMap.put("USER", GenericArtifactField.FieldValueTypeValue.USER);
	}

	@Override
	public boolean handleException(Throwable cause, ConnectionManager<TrackerWebServicesClient> connectionManager){
		// TODO What about invalid sessions?
		if(cause == null) return false;
		if ((cause instanceof java.net.SocketException
				|| cause instanceof java.net.UnknownHostException) && connectionManager.isEnableRetryAfterNetworkTimeout()) {
			return true;
		}
		else if(cause instanceof ConnectionException && connectionManager.isEnableRetryAfterNetworkTimeout()){
			return true;
		}
		else if(cause instanceof RemoteException){
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		}
		else if(cause instanceof CCFRuntimeException){
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		}
		return false;
	}

	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			GenericArtifact artifactData) {
		String artifactId = artifactData.getSourceArtifactId();
		String artifactIdentifier = artifactId.substring(artifactId.lastIndexOf(":")+1);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		long fromTime = lastModifiedDate.getTime();
//		long toTime = System.currentTimeMillis();
		TrackerArtifactType trackerArtifactType =
			this.getTrackerArtifactTypeForArtifactId(syncInfo, artifactId);
		ArtifactType[] ata = new ArtifactType[1];
		ata[0] = new ArtifactType(trackerArtifactType.getTagName(),
				trackerArtifactType.getNamespace(), trackerArtifactType.getDisplayName());
		ArrayList<GenericArtifact> attachmentGAs = new ArrayList<GenericArtifact>();
		TrackerWebServicesClient twsclient = null;

		try {
			twsclient = this.getConnection(syncInfo);
			ArtifactHistoryList ahl = ProjectTrackerReader.artifactHistoryList.get();
			History historyList[] = null;
			if(ahl != null) historyList = ahl.getHistory();
			if(historyList != null)
			{
				for(History history:historyList){
					HistoryTransaction[] transactions =  history.getHistoryTransaction();
					int transactionsCount = transactions.length;
		 			for (int i=0; i < transactionsCount; i++)
		 			{
		 				HistoryTransaction ht = transactions[i];
		 				String modifiedBy = ht.getModifiedBy();
		 				if((!connectorUserDisplayName.equals(modifiedBy)) &&
		 						ht.getModifiedOn() > fromTime){
			 				HistoryActivity[] haa = ht.getHistoryActivity();
			 				for (HistoryActivity ha : haa)
			 				{
			 					String historyArtifactId = ha.getArtifactId();
			 					if(historyArtifactId.equals(artifactIdentifier)){
			 						if(ha.getTagName().equals(ATTACHMENT_TAG_NAME)){
			 							if(ha.getType().equals(ATTACHMENT_ADDED_HISTORY_ACTIVITY_TYPE)){
			 							String[] attachmentIds = ha.getNewValue();
			 							for(String attachmentId:attachmentIds){
			 								if(attachmentId == null || attahcmentIDNameMap == null) continue;

			 								String attachmentName = attahcmentIDNameMap.get(attachmentId);
			 								if(StringUtils.isEmpty(attachmentName)){
			 									attachmentName = "PT-Attachment" + attachmentId + ".file";
			 									log.warn("Could not determine attachment name for attachment id "+attachmentId);
			 								}

			 								GenericArtifact ga = new GenericArtifact();
			 								ga.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
		 									ga.setSourceArtifactLastModifiedDate(artifactData.getSourceArtifactLastModifiedDate());
			 								ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
			 								ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
			 								ga.setDepParentSourceArtifactId(artifactId);
			 								ga.setSourceArtifactId(attachmentId);
		 									ga.setSourceArtifactVersion(artifactData.getSourceArtifactVersion());
			 								GenericArtifactField contentTypeField =
			 									ga.addNewField(AttachmentMetaData.ATTACHMENT_TYPE,
			 											GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
			 								contentTypeField.setFieldValue(AttachmentMetaData.AttachmentType.DATA);
			 								contentTypeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			 								contentTypeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
			 								GenericArtifactField sourceURLField =
			 									ga.addNewField(AttachmentMetaData.ATTACHMENT_SOURCE_URL,
			 											GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
			 								sourceURLField.setFieldValue(AttachmentMetaData.AttachmentType.LINK);
			 								sourceURLField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			 								sourceURLField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

			 								GenericArtifactField nameField = ga.addNewField(AttachmentMetaData.ATTACHMENT_NAME,
			 										GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
			 								nameField.setFieldValue(attachmentName);
			 								nameField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			 								nameField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

			 								GenericArtifactField mimeTypeField = ga.addNewField(AttachmentMetaData.ATTACHMENT_MIME_TYPE,
			 										GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
			 								mimeTypeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			 								mimeTypeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

			 								long size = 0;
			 								DataHandler handler = null;
			 								try{
			 									handler = twsclient.getDataHandlerForAttachment(artifactIdentifier, attachmentId);
			 								} catch(WSException e){
			 									int code = e.getCode();
			 									if(code == 214){
			 										continue;
			 									}
			 									else throw e;
			 								}
			 								String contentType = handler.getContentType();
			 								mimeTypeField.setFieldValue(contentType);
			 								String axisFileName = handler.getName();
		 									File attachmentAxisFile = new File(axisFileName);
		 									size = attachmentAxisFile.length();
		 									long maxAttachmentSize = this.getMaxAttachmentSizePerArtifact();
		 									if(size > maxAttachmentSize){
		 										log.warn("Attachment "+ attachmentName +" is of size "+size + " bytes."
		 												+" This is more than the configured maximum attachment size"
		 												+" that can be shipped in an artifact");
		 										continue;
		 									}
				 							if(!this.isShipAttachmentsWithArtifact()){
				 								File tempFile = null;
				 								FileOutputStream fos = null;
				 								FileInputStream fis = null;
				 								try {
				 									if(attachmentAxisFile.exists()){
					 									byte [] bytes = new byte[1024*3];
					 									tempFile = File.createTempFile("PT_Attachment", "file");

					 									String attachmentDataFile = tempFile.getAbsolutePath();
					 									int readBytes = 0;
					 									fos = new FileOutputStream(tempFile);
					 									fis = new FileInputStream(attachmentAxisFile);
					 									while((readBytes = fis.read(bytes)) != -1){
					 										fos.write(bytes,0,readBytes);
					 									}
					 									GenericArtifactField attachmentDataFileField =
					 										ga.addNewField(AttachmentMetaData.ATTACHMENT_DATA_FILE,
					 											GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					 									attachmentDataFileField.setFieldValueType(
					 											GenericArtifactField.FieldValueTypeValue.STRING);
					 									attachmentDataFileField.setFieldValue(attachmentDataFile);
					 									bytes = null;
				 									}
				 								} catch (IOException e) {
				 									String message = "Could not write attahcment content to temp file."
				 										+" Shipping the attachment with the artifact.";
				 									log.error(message, e);
				 									throw new CCFRuntimeException(message, e);
				 								}
				 								finally {
				 									if(fis != null) {
				 										try {
				 											fis.close();
				 										}
				 										catch(IOException e) {
				 											log.warn("Could not close input stream for " + attachmentAxisFile.getAbsolutePath());
				 										}
				 									}
				 									if(fos != null) {
				 										try {
				 											fos.close();
					 									}
				 										catch(IOException e) {
				 											String filename = "";
				 											if(tempFile != null) {
				 												filename = tempFile.getAbsolutePath();
				 											}
				 											log.warn("Could not close output stream for " + filename);
				 										}
				 									}
				 								}
				 							}
				 							else {
				 								InputStream is = handler.getInputStream();
					 							byte [] bytes = new byte[(int)size];
					 							is.read(bytes);
					 							is.close();
					 							ga.setRawAttachmentData(bytes);
				 							}
				 							GenericArtifactField sizeField = ga.addNewField(AttachmentMetaData.ATTACHMENT_SIZE,
			 										GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
			 								sizeField.setFieldValue(size);
			 								sizeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			 								sizeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				 							populateSrcAndDestForAttachment(syncInfo, ga);
				 							attachmentGAs.add(ga);
			 							}
			 						}
			 						else if(ha.getType().equals(ATTACHMENT_DELETED_HISTORY_ACTIVITY_TYPE)){
		 								String[] attachmentIds = ha.getOldValue();
			 							for(String attachmentId:attachmentIds){
			 								if(attachmentId == null) continue;
			 								GenericArtifact ga = new GenericArtifact();
			 								ga.setArtifactAction(GenericArtifact.ArtifactActionValue.DELETE);
		 									ga.setSourceArtifactLastModifiedDate(artifactData.getSourceArtifactLastModifiedDate());
			 								ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
			 								ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
			 								ga.setDepParentSourceArtifactId(artifactId);
			 								ga.setSourceArtifactId(attachmentId);
		 									ga.setSourceArtifactVersion(artifactData.getSourceArtifactVersion());
			 								GenericArtifactField contentTypeField =
			 									ga.addNewField(AttachmentMetaData.ATTACHMENT_TYPE,
			 											GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
			 								contentTypeField.setFieldValue(AttachmentMetaData.AttachmentType.DATA);
			 								contentTypeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			 								contentTypeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				 							populateSrcAndDestForAttachment(syncInfo, ga);
				 							attachmentGAs.add(ga);
			 							}
			 						}
			 							else if(ha.getType().equals(URL_ADDED_HISTORY_ACTIVITY_TYPE)){
			 								String[] attachmentIds = ha.getNewValue();
				 							for(String attachmentId:attachmentIds){
				 								if(attachmentId == null) continue;

//				 								String attachmentName = attahcmentIDNameMap.get(attachmentId);

				 								GenericArtifact ga = new GenericArtifact();
				 								ga.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);

				 								ga.setSourceArtifactLastModifiedDate(artifactData.getSourceArtifactLastModifiedDate());

				 								ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
				 								ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
				 								ga.setDepParentSourceArtifactId(artifactId);
				 								ga.setSourceArtifactId(attachmentId);
				 								ga.setSourceArtifactVersion(artifactData.getSourceArtifactVersion());

				 								GenericArtifactField contentTypeField =
				 									ga.addNewField(AttachmentMetaData.ATTACHMENT_TYPE,
				 											GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				 								contentTypeField.setFieldValue(AttachmentMetaData.AttachmentType.LINK);
				 								contentTypeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				 								contentTypeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				 								GenericArtifactField sourceURLField =
				 									ga.addNewField(AttachmentMetaData.ATTACHMENT_SOURCE_URL,
				 											GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				 								sourceURLField.setFieldValue(AttachmentMetaData.AttachmentType.LINK);
				 								sourceURLField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				 								sourceURLField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

//				 								GenericArtifactField nameField = ga.addNewField(AttachmentMetaData.ATTACHMENT_NAME,
//				 										GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
//				 								nameField.setFieldValue(attachmentName);
//				 								nameField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
//				 								nameField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					 							populateSrcAndDestForAttachment(syncInfo, ga);
					 							attachmentGAs.add(ga);
				 							}
			 							}
			 							else if(ha.getType().equals(URL_DELETED_HISTORY_ACTIVITY_TYPE)){
			 								String[] attachmentIds = ha.getOldValue();
				 							for(String attachmentId:attachmentIds){
				 								if(attachmentId == null) continue;
				 								GenericArtifact ga = new GenericArtifact();
				 								ga.setArtifactAction(GenericArtifact.ArtifactActionValue.DELETE);
			 									ga.setSourceArtifactLastModifiedDate(artifactData.getSourceArtifactLastModifiedDate());

				 								ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
				 								ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
				 								ga.setDepParentSourceArtifactId(artifactId);
				 								ga.setSourceArtifactId(attachmentId);
			 									ga.setSourceArtifactVersion(artifactData.getSourceArtifactVersion());
				 								GenericArtifactField contentTypeField =
				 									ga.addNewField(AttachmentMetaData.ATTACHMENT_TYPE,
				 											GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				 								contentTypeField.setFieldValue(AttachmentMetaData.AttachmentType.DATA);
				 								contentTypeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				 								contentTypeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					 							populateSrcAndDestForAttachment(syncInfo, ga);
					 							attachmentGAs.add(ga);
				 							}
			 							}
			 					}
			 				}
		 				}
		 			}
				}
			}
			}
		} catch (Exception e) {
			String message = "Exception while getting the attachment data";
			log.error(message,e);
			throw new CCFRuntimeException(message,e);
		}
		finally {
			ProjectTrackerReader.artifactHistoryList.set(null);
			this.attahcmentIDNameMap = null;
			if(twsclient != null) {
				getConnectionManager().releaseConnection(twsclient);
				twsclient = null;
			}
		}
		return attachmentGAs;
	}

	/**
	 * Populates the source and destination attributes for this GenericArtifact
	 * object from the Sync Info database document.
	 *
	 * @param syncInfo
	 * @param ga
	 */
	private void populateSrcAndDestForAttachment(Document syncInfo, GenericArtifact ga){

		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);

		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
		String targetRepositoryKind = this.getTargetRepositoryKind(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetSystemKind = this.getTargetSystemKind(syncInfo);

		ga.setSourceRepositoryId(sourceRepositoryId);
		ga.setSourceRepositoryKind(sourceRepositoryKind);
		ga.setSourceSystemId(sourceSystemId);
		ga.setSourceSystemKind(sourceSystemKind);

		ga.setDepParentSourceRepositoryId(sourceRepositoryId);
		ga.setDepParentSourceRepositoryKind(sourceRepositoryKind);

		ga.setTargetRepositoryId(targetRepositoryId);
		ga.setTargetRepositoryKind(targetRepositoryKind);
		ga.setTargetSystemId(targetSystemId);
		ga.setTargetSystemKind(targetSystemKind);

		ga.setDepParentTargetRepositoryId(targetRepositoryId);
		ga.setDepParentTargetRepositoryKind(targetRepositoryKind);
	}

	private String getRepositoryKey(Document syncInfo){
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		return sourceSystemId + sourceSystemKind + sourceRepositoryId + sourceRepositoryKind;
	}

	@Override
	public GenericArtifact getArtifactData(Document syncInfo,
			String artifactId) {
		TrackerArtifactType trackerArtifactType =
			this.getTrackerArtifactTypeForArtifactId(syncInfo, artifactId);
		String artifactIdentifier = artifactId.substring(artifactId.lastIndexOf(":")+1);
		String artifactTypeNamespace = trackerArtifactType.getNamespace();
		String artifactTypeTagName = trackerArtifactType.getTagName();
		String sourceSystemTimezone = this.getSourceSystemTimezone(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		long fromTime = lastModifiedDate.getTime();
		//long toTime = System.currentTimeMillis();
		TrackerWebServicesClient twsclient = null;
		ClientArtifact artifact = null;
		GenericArtifact artifactData = null;
		ArtifactHistoryList ahlVersion = null;
		String lastModifiedBy = null;
		try {
			try{
				Thread.sleep(500);
			}
			catch(InterruptedException e){
				log.debug("Sleep interrupted before getting the artifact data",e);
			}
			twsclient = this.getConnection(syncInfo);
			ClientArtifactListXMLHelper listHelper = twsclient.getArtifactById(artifactIdentifier);
			List<ClientArtifact> artifacts = listHelper.getAllArtifacts();
			ptHelper.processWSErrors(listHelper);
			if(artifacts == null || artifacts.size() == 0){
				throw new CCFRuntimeException("There is no artifact with id "+artifactIdentifier);
			}
			else if(artifacts.size() == 1){
				artifact = artifacts.get(0);
				String retreivedId = artifact.getArtifactID();
				if(!retreivedId.equals(artifactIdentifier)){
					movedArtifacts.put(retreivedId, artifactIdentifier);
				}
				else if(movedArtifacts.containsKey(artifactIdentifier)){
					movedArtifacts.remove(artifactIdentifier);
					return artifactData;
				}
			}
			else if(artifacts.size() > 1){
				throw new CCFRuntimeException("More than one artifact were returned for id "+artifactIdentifier);
			}
			lastModifiedBy = artifact.getAttributeValue(TrackerWebServicesClient.DEFAULT_NAMESPACE,
					TrackerWebServicesClient.MODIFIED_BY_FIELD_NAME);
			if(lastModifiedBy.equals(username)){
				return artifactData;
			}
			String createdOnTimeMillis = artifact.getAttributeValue(TrackerWebServicesClient.DEFAULT_NAMESPACE,
					CREATED_ON_FIELD);
			long createdOnTime = Long.parseLong(createdOnTimeMillis);
			ahlVersion = twsclient.getChangeHistoryForArtifact(artifactIdentifier,
					createdOnTime, null);
			ProjectTrackerReader.artifactHistoryList.set(ahlVersion);
		} catch (Exception e) {
			String message = "Exception while getting the artifact data";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		}
		finally {
			if(twsclient != null){
				getConnectionManager().releaseConnection(twsclient);
				twsclient = null;
			}
		}
		GenericArtifact ga = new GenericArtifact();
		if(lastModifiedBy.equals(this.getResyncUserName())){
			ga.setArtifactAction(GenericArtifact.ArtifactActionValue.RESYNC);
		}
		ga.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
		ga.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
		ga.setSourceArtifactId(artifactId);
		Map<String, List<String>> attributes = artifact.getAttributes();
		for(Map.Entry<String, List<String>> entry:attributes.entrySet()){
			String attributeName = entry.getKey();
			List<String> attValues = entry.getValue();
			String ptAttributeType = null;
			TrackerAttribute trackerAttribute = trackerArtifactType.getAttribute(attributeName);
			if(trackerAttribute == null) continue;
			ptAttributeType = trackerAttribute.getAttributeType();
			String attributeDisplayName = trackerAttribute.getDisplayName();
			String attributeNamespace = trackerAttribute.getNamespace();
			String attributeTagName = trackerAttribute.getTagName();
			String attributeNamespaceDiaplayName = null;
			if(attributeName.equals(CREATED_ON_FIELD_NAME)
					|| attributeName.equals(MODIFIED_ON_FIELD_NAME)
					|| attributeName.equals(ID_FIELD_NAME)
					|| attributeName.equals(CREATED_BY_FIELD_NAME)
					|| attributeName.equals(MODIFIED_BY_FIELD_NAME)){
				attributeNamespaceDiaplayName = attributeName;
			}
			else {
				attributeNamespaceDiaplayName = "{"+attributeNamespace+"}"+attributeDisplayName;
			}
			GenericArtifactField.FieldValueTypeValue gaFieldType = null;
			gaFieldType = ptGATypesMap.get(ptAttributeType);
			boolean isAttributeRequired = false;
			if(trackerAttribute.getAttributeType().equals("USER")){
				attValues = getUserAttributesValuesFromHistory(attributeNamespace,
						attributeTagName, attributeDisplayName, ahlVersion);
			}
			if(attValues != null && attValues.size() > 0 && (!CollectionUtils.isEmptyOrNull(attValues))){
				isAttributeRequired = trackerAttribute.isRequired();
			}
			else {
				GenericArtifactField field = null;
				if(attributeName.equals(CREATED_ON_FIELD_NAME)
						|| attributeName.equals(MODIFIED_ON_FIELD_NAME)
						|| attributeName.equals(ID_FIELD_NAME)
						|| attributeName.equals(CREATED_BY_FIELD_NAME)
						|| attributeName.equals(MODIFIED_BY_FIELD_NAME)){
					field = ga.addNewField(attributeName,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				}
				else {
					field = ga.addNewField(attributeNamespaceDiaplayName,
						GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				}
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				field.setFieldValueType(gaFieldType);
				field.setFieldValueHasChanged(true);
				if(trackerAttribute.getAttributeType().equals("USER")){
					field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.USER);
				}
				else if(trackerAttribute.getAttributeType().equals("DATE")
						|| attributeName.equals(CREATED_ON_FIELD_NAME)
						|| attributeName.equals(MODIFIED_ON_FIELD_NAME)){
					field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.DATE);
				}
				continue;
			}
			for(String attValue:attValues){
				if(StringUtils.isEmpty(attValue)) continue;
				GenericArtifactField field = null;
				if(isAttributeRequired) {
					field = ga.addNewField(attributeNamespaceDiaplayName,
							GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
				}
				else {
					field = ga.addNewField(attributeNamespaceDiaplayName,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				}
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				field.setFieldValueHasChanged(true);
				field.setFieldValueType(gaFieldType);
				if(trackerAttribute.getAttributeType().equals("USER")){
					field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.USER);
				}

				if(trackerAttribute.getAttributeType().equals("MULTI_SELECT") ||
						trackerAttribute.getAttributeType().equals("SINGLE_SELECT")){
					ArtifactTypeMetadata metadata = metadataHelper.getArtifactTypeMetadata(
							this.getRepositoryKey(syncInfo), artifactTypeNamespace, artifactTypeTagName);
					attValue = this.convertOptionValue(attributeNamespace, attributeTagName,
							attValue, metadata, false);
				}
				else if(trackerAttribute.getAttributeType().equals("STATE")){
					ArtifactTypeMetadata metadata = metadataHelper.getArtifactTypeMetadata(
							this.getRepositoryKey(syncInfo), artifactTypeNamespace, artifactTypeTagName);
					attValue = this.convertOptionValue(attributeNamespace, attributeTagName,
							attValue, metadata, true);
				}
				if(attributeName.equals(CREATED_ON_FIELD_NAME)
						|| attributeName.equals(MODIFIED_ON_FIELD_NAME)){
					field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.DATETIME);
					String dateStr = attValue;
					if(!StringUtils.isEmpty(dateStr)){
						Date date = new Date(Long.parseLong(dateStr));
						field.setFieldValue(date);
					}
				}
				else if(trackerAttribute.getAttributeType().equals("DATE")){
					field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.DATE);
					String dateStr = attValue;
					Date dateValue = null;
					if(!StringUtils.isEmpty(dateStr)){
						dateValue = new Date(Long.parseLong(dateStr));
					}
					if(dateValue != null){
						if(DateUtil.isAbsoluteDateInTimezone(dateValue, sourceSystemTimezone)){
							dateValue = DateUtil.convertToGMTAbsoluteDate(dateValue, sourceSystemTimezone);
							field.setFieldValue(dateValue);
						}
						else {
							field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.DATETIME);
							field.setFieldValue(dateValue);
						}
					}
				}
				else {
					field.setFieldValue(attValue);
				}
				if(attributeName.equals(MODIFIED_ON_FIELD_NAME)){
					String modifiedOnStr = attValue;
					long modifiedOnMilliSeconds = Long.parseLong(modifiedOnStr);
					Date modifiedOnDate = new Date(modifiedOnMilliSeconds);
					String lastModificationDate = DateUtil.format(modifiedOnDate);
					ga.setSourceArtifactLastModifiedDate(lastModificationDate);
					ga.setSourceArtifactVersion(Long.toString(modifiedOnMilliSeconds));
				}
				else if(attributeName.equals(CREATED_ON_FIELD_NAME)){
					//String createdOnStr = attValue;
					//long createdOnTime = Long.parseLong(createdOnStr);
				}
			}
		}
		List<ClientArtifactComment> comments = artifact.getComments();
		for(ClientArtifactComment comment:comments){
			String commentDate = comment.getCommentDate();
			long commentTime = Long.parseLong(commentDate);
			String commentor = comment.getCommenter();
			if(commentTime > fromTime && (!commentor.equals(this.getUsername()))){
				String commentText = comment.getCommentText();
				String commenter = comment.getCommenter();
				commentText = "\nOriginal commenter: " + commenter + "\n" + commentText;
				GenericArtifactField field = null;
				field = ga.addNewField(COMMENT_FIELD_NAME,
						GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				field.setFieldValueHasChanged(true);
				field.setFieldValue(commentText);
			}
		}
		if(ahlVersion != null) {
			History[] historyList = ahlVersion.getHistory();
			if(historyList != null && historyList.length == 1){
				History history = historyList[0];
				if(history != null){
					HistoryTransaction[] transactions = history.getHistoryTransaction();
					if(transactions != null) {
						for(HistoryTransaction transaction:transactions){
							if(transaction == null) continue;
							long historyTime = transaction.getModifiedOn();
							if(historyTime > fromTime){
								String reason = transaction.getReason();
								String reasonUser = transaction.getModifiedBy();
								if(reasonUser.equals(this.getConnectorUserDisplayName())){
									continue;
								}
								if(!StringUtils.isEmpty(reason)) {
									reason = "\nReason provided by "+ reasonUser +"\n" + reason;
									GenericArtifactField reasonField = ga.addNewField(REASON_FIELD_NAME,
											GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
									reasonField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
									reasonField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
									reasonField.setFieldValueHasChanged(true);
									reasonField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
									reasonField.setFieldValue(reason);
								}
							}
						}
					}
				}
			}
		}
		List<ClientArtifactAttachment> atts = artifact.getAttachments();
		for(ClientArtifactAttachment attachment:atts){
			if(attahcmentIDNameMap == null){
				attahcmentIDNameMap = new HashMap<String, String>();
			}
			String attachmentId = attachment.getAttachmentId();
			String attachmentName = attachment.getAttachmentName();
			attahcmentIDNameMap.put(attachmentId, attachmentName);
		}
		this.populateSrcAndDest(syncInfo, ga);
		return ga;
	}

	private List<String> getUserAttributesValuesFromHistory(
			String attributeNamespace, String attributeTagName,
			String attributeDisplayName, ArtifactHistoryList ahlVersion) {
		List<String> userAttributeValues = new ArrayList<String>();
		if(ahlVersion != null) {
			History[] histories = ahlVersion.getHistory();
			if(histories != null){
				for(History history:histories){
					HistoryTransaction[] transactions = history.getHistoryTransaction();
					if(transactions != null){
						for(HistoryTransaction transaction:transactions) {
							HistoryActivity[] activities = transaction.getHistoryActivity();
							for(HistoryActivity activity:activities){
								String namespace = activity.getNamespace();
								String tagName = activity.getTagName();
								if(namespace.equals(attributeNamespace)
										&& tagName.equals(attributeTagName)) {
									String[] newValues = activity.getNewValue();
									String[] oldValues = activity.getOldValue();
									if(oldValues != null && newValues != null) {
										String oldValue = oldValues[0];
										String newValue = newValues[0];
										if(oldValue == null){
											if(!userAttributeValues.contains(newValue)){
												userAttributeValues.add(newValue);
											}
										}
										else if(newValue == null){
											if(userAttributeValues.contains(oldValue)){
												userAttributeValues.remove(oldValue);
											}
										}
										else {
											// This part will not reach
											if(!userAttributeValues.contains(newValue)){
												userAttributeValues.add(newValue);
											}
											if(userAttributeValues.contains(oldValue)){
												userAttributeValues.remove(oldValue);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return userAttributeValues;
	}

	private String convertOptionValue(String attributeNamespace, String attributeTagName,
			String attributeValue, ArtifactTypeMetadata metadata, boolean isStateField){
		String optionValue = null;
		for(Attribute att:metadata.getAttribute()){
			String namespace = att.getNamespace();
			String tagName = att.getTagName();
			if(namespace.equals(attributeNamespace) && tagName.equals(attributeTagName)){
				if(attributeValue.equals("")){
					optionValue = attributeValue;
					break;
				}
				for(Option option:att.getOptions()){
					String optionTagName = option.getTagName();
					if(optionTagName.equals(attributeValue)){
						optionValue = option.getDisplayName();
					}
				}
			}
		}
		if(isStateField && StringUtils.isEmpty(optionValue)){
			optionValue = attributeValue;
		}
		if(optionValue == null) throw new CCFRuntimeException("Option tagname for option "+attributeValue+
				"is not available in {"+attributeNamespace+"}"+attributeTagName);
		return optionValue;
	}

	@Override
	public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId) {
		// TODO To be implemented
		return new ArrayList<GenericArtifact>();
	}

	private TrackerWebServicesClient getConnection(Document syncInfo){
		String systemId = this.getSourceSystemId(syncInfo);
		String systemKind = this.getSourceSystemKind(syncInfo);
		String repositoryId = this.getSourceRepositoryId(syncInfo);
		String repositoryKind = this.getSourceRepositoryKind(syncInfo);
		String connectionInfo = this.getServerUrl();
		String credentialInfo = this.getUsername()+
							CollabNetConnectionFactory.PARAM_DELIMITER+this.getPassword();
		TrackerWebServicesClient twsclient = null;
		try {
			twsclient = this.connect(systemId, systemKind, repositoryId,
					repositoryKind, connectionInfo, credentialInfo);
		} catch (MaxConnectionsReachedException e) {
			String message = "Could not get connection for PT";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} catch (ConnectionException e) {
			String message = "Could not get connection for PT";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		}
		return twsclient;
	}

	private TrackerArtifactType getTrackerArtifactTypeForArtifactId(Document syncInfo, String artifactId){
		String repositoryKey = this.getRepositoryKey(syncInfo);
		TrackerArtifactType trackerArtifactType =
			metadataHelper.getTrackerArtifactType(repositoryKey);
		return trackerArtifactType;
	}

	@Override
	public List<String> getChangedArtifacts(Document syncInfo) {
		String repositoryKey = this.getRepositoryKey(syncInfo);
		String repositoryId = this.getSourceRepositoryId(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		long fromTimeLong = lastModifiedDate.getTime();
		fromTimeLong = fromTimeLong < 0 ? 0 : fromTimeLong;
		String fromTime = Long.toString(fromTimeLong+1000);
		String artifactTypeDisplayName = repositoryId.substring(repositoryId.lastIndexOf(":")+1);
		Set<String> artifactTypes = new HashSet<String>();


		TrackerWebServicesClient twsclient = null;
		TrackerArtifactType trackerArtifactType =
			metadataHelper.getTrackerArtifactType(repositoryKey);
		ArrayList<String> artifacts = new ArrayList<String>();
		try {
			twsclient = this.getConnection(syncInfo);
			if(trackerArtifactType == null){
				trackerArtifactType = metadataHelper.getTrackerArtifactType(repositoryKey,
						artifactTypeDisplayName, twsclient);
			}

			String namespace = trackerArtifactType.getNamespace();
			String tagname = trackerArtifactType.getTagName();
			String artifactTypeFullyQualifiedName = "{"+namespace+"}"+tagname;

			artifactTypes.add(artifactTypeFullyQualifiedName);
			String[] changedArtifacts = twsclient.getChangedArtifacts(artifactTypes, fromTime);
			for (String artifact: changedArtifacts)
			{
				artifacts.add(artifactTypeFullyQualifiedName +":"+ artifact);
			}
		} catch (WSException e1) {
			String message = "Web Service Exception while retreiving Artifact Type meta data";
			log.error(message, e1);
			throw new CCFRuntimeException(message, e1);
		} catch (RemoteException e1) {
			String message = "Remote Exception while retreiving Artifact Type meta data";
			log.error(message, e1);
			throw new CCFRuntimeException(message, e1);
		} catch (ServiceException e1) {
			String message = "Service Exception while retreiving Artifact Type meta data";
			log.error(message, e1);
			throw new CCFRuntimeException(message, e1);
		}
		catch (Exception e) {
			String message = "Exception while getting the changed artifacts";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} finally {
			if(twsclient != null){
				getConnectionManager().releaseConnection(twsclient);
			}
		}
		return artifacts;
	}

	/**
	 * Connects to the source CEE system using the connectionInfo and credentialInfo
	 * details.
	 *
	 * This method uses the ConnectionManager configured in the wiring file
	 * for the CEEReader
	 *
	 * @param systemId - The system id of the source CEE system
	 * @param systemKind - The system kind of the source CEE system
	 * @param repositoryId - The tracker id in the source CEE system
	 * @param repositoryKind - The repository kind for the tracker
	 * @param connectionInfo - The CEE server URL
	 * @param credentialInfo - User name and password concatenated with a delimiter.
	 * @return - The connection object obtained from the ConnectionManager
	 * @throws MaxConnectionsReachedException
	 * @throws ConnectionException
	 */
	public TrackerWebServicesClient connect(String systemId, String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo, String credentialInfo) throws MaxConnectionsReachedException, ConnectionException {
		//log.info("Before calling the parent connect()");
		TrackerWebServicesClient connection = null;
		connection = getConnectionManager().getConnectionToUpdateOrExtractArtifact(systemId, systemKind, repositoryId,
			repositoryKind, connectionInfo, credentialInfo);
		return connection;
	}

	/**
	 * Populates the source and destination attributes for this GenericArtifact
	 * object from the Sync Info database document.
	 *
	 * @param syncInfo
	 * @param ga
	 */
	private void populateSrcAndDest(Document syncInfo, GenericArtifact ga){
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String conflictResolutionPriority = this.getConflictResolutionPriority(syncInfo);

		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
		String targetRepositoryKind = this.getTargetRepositoryKind(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetSystemKind = this.getTargetSystemKind(syncInfo);

		ga.setSourceRepositoryId(sourceRepositoryId);
		ga.setSourceRepositoryKind(sourceRepositoryKind);
		ga.setSourceSystemId(sourceSystemId);
		ga.setSourceSystemKind(sourceSystemKind);
		ga.setConflictResolutionPriority(conflictResolutionPriority);

		ga.setTargetRepositoryId(targetRepositoryId);
		ga.setTargetRepositoryKind(targetRepositoryKind);
		ga.setTargetSystemId(targetSystemId);
		ga.setTargetSystemKind(targetSystemKind);
	}

	/**
	 * Releases the connection to the ConnectionManager.
	 *
	 * @param connection - The connection to be released to the ConnectionManager
	 */
	public void disconnect(TrackerWebServicesClient connection) {
		getConnectionManager().releaseConnection(connection);
	}

	/**
	 * Returns the server URL of the CEE system that is
	 * configured in the wiring file.
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Sets the source CEE system's SOAP server URL.
	 *
	 * @param serverUrl - the URL of the source CEE system.
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
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
	 * Gets the mandatory user name The user name is used to login into the
	 * CEE instance whenever an artifact should be updated or extracted.
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
	 * The user name is used to login into the CEE instance whenever an
	 * artifact should be updated or extracted. This user has to differ from the
	 * resync user in order to force initial resyncs with the source system once
	 * a new artifact has been created.
	 *
	 * @param usser name
	 *            the user name to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getConnectorUserDisplayName() {
		return connectorUserDisplayName;
	}

	public void setConnectorUserDisplayName(String connectorUserDisplayName) {
		this.connectorUserDisplayName = connectorUserDisplayName;
	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		super.validate(exceptions);

		if (getResyncUserName() == null) {
			log
					.warn("resyncUserName-property has not been set, so that initial resyncs after artifact creation are not possible.");
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

		if (getConnectorUserDisplayName() == null) {
			log.error("connectorUserDisplayName-property not set");
			exceptions.add(new ValidationException("connectorUserDisplayName-property not set",
					this));
		}

		if (getServerUrl() == null) {
			log.error("serverUrl-property not set");
			exceptions.add(new ValidationException(
					"serverUrl-property not set", this));
		}

	}

	/**
	 * Sets the optional resync username
	 *
	 * The resync user name is used to login into the CEE instance
	 * whenever an artifact should be created. This user has to differ from the
	 * ordinary user used to log in in order to force initial resyncs with the
	 * source system once a new artifact has been created.
	 * This property can also
	 * be set for the reader component in order to be able to differentiate
	 * between artifacts created by ordinary users and artifacts to be resynced.
	 *
	 * @param resyncUserName
	 *            the resyncUserName to set
	 */
	public void setResyncUserName(String resyncUserName) {
		this.resyncUserName = resyncUserName;
	}

	/**
	 * Gets the optional resync username The resync user name is used to login
	 * into the CEE instance whenever an artifact should be created. This
	 * user has to differ from the ordinary user used to log in in order to
	 * force initial resyncs with the source system once a new artifact has been
	 * created. This property can also
	 * be set for the reader component in order to be able to differentiate
	 * between artifacts created by ordinary users and artifacts to be resynced.
	 *
	 * @return the resyncUserName
	 */
	private String getResyncUserName() {
		return resyncUserName;
	}

	/**
	 * Another user name that is used to login into the CEE instance. This
	 * user has to differ from the ordinary user used to log in in order to
	 * force initial resyncs with the source system once a new artifact has been
	 * created.
	 */
	private String resyncUserName;

}
