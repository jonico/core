package com.collabnet.ccf.pi.cee.pt.v50;

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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
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

public class ProjectTrackerReader extends AbstractReader {
	private static final Log log = LogFactory.getLog(ProjectTrackerReader.class);
	private String serverUrl = null;

	private String password = null;

	private String username = null;
	
	private String connectorUserDisplayName = null;
	
	public static final String ATTACHMENT_TAG_NAME = "attachment";
	public static final String ATTAACHMENT_ADDED_HISTORY_ACTIVITY_TYPE = "FileAddedDesc";
	public static final String TRACKER_NAMESPACE = "urn:ws.tracker.collabnet.com";
	public static final String CREATED_ON_FIELD = "createdOn";
	public static final String MODIFIED_ON_FIELD = "modifiedOn";
	public static final String CREATED_ON_FIELD_NAME = "{"+TRACKER_NAMESPACE+"}"+CREATED_ON_FIELD;
	public static final String MODIFIED_ON_FIELD_NAME = "{"+TRACKER_NAMESPACE+"}"+MODIFIED_ON_FIELD;
	public static final String ARTIFACT_VERSION_FIELD_NAME = "{urn:cu023.cubit.maa.collab.net/PT/IZ-PT/}version";
	private MetaDataHelper metadataHelper = MetaDataHelper.getInstance();
	private HashMap<String, String> attahcmentIDNameMap = null;
	private ConnectionManager<TrackerWebServicesClient> connectionManager = null;
	// TODO Use ThreadLocal object to store these variables
	private ThreadLocal<ArtifactHistoryList> artifactHistoryList = new ThreadLocal<ArtifactHistoryList>();
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
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			String artifactId) {
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
		int version = 0;
		try {
			twsclient = this.getConnection(syncInfo);
			ArtifactHistoryList ahl = this.artifactHistoryList.get();
			History historyList[] = null;
			if(ahl != null) historyList = ahl.getHistory();
			if(historyList != null)
			{
				for(History history:historyList){
		 			for (HistoryTransaction ht: history.getHistoryTransaction())
		 			{
		 				version++;
		 				String modifiedBy = ht.getModifiedBy();
		 				if(!connectorUserDisplayName.equals(modifiedBy) &&
		 						ht.getModifiedOn() > fromTime){
		 					Date modifiedOn = new Date(ht.getModifiedOn());
			 				HistoryActivity[] haa = ht.getHistoryActivity();
			 				for (HistoryActivity ha : haa)
			 				{
			 					String historyArtifactId = ha.getArtifactId();
			 					if(historyArtifactId.equals(artifactIdentifier)){
			 						if(ha.getTagName().equals(ATTACHMENT_TAG_NAME) &&
			 								ha.getType().equals(ATTAACHMENT_ADDED_HISTORY_ACTIVITY_TYPE)){
			 							String[] attachmentIds = ha.getNewValue();
			 							for(String attachmentId:attachmentIds){
			 								if(attachmentId == null) continue;
			 								DataHandler handler =
				 								twsclient.getDataHandlerForAttachment(artifactIdentifier, attachmentId);
			 								String attachmentName = attahcmentIDNameMap.get(attachmentId);
			 								String contentType = handler.getContentType();
			 								GenericArtifact ga = new GenericArtifact();
			 								ga.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
			 								ga.setSourceArtifactLastModifiedDate(DateUtil.format(modifiedOn));
			 								ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
			 								ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
			 								ga.setDepParentSourceArtifactId(artifactId);
			 								ga.setSourceArtifactId(attachmentId);
			 								ga.setSourceArtifactVersion(Integer.toString(version));
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
			 								mimeTypeField.setFieldValue(contentType);
			 								mimeTypeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			 								mimeTypeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				 							
			 								StringBuffer buffer = new StringBuffer();
			 								long size = 0;
				 							InputStream is = handler.getInputStream();
				 							byte [] bytes = new byte[1024*3];
				 							int readCount = -1;
				 							while((readCount = is.read(bytes)) != -1){
				 								size += readCount;
				 								if(readCount < bytes.length){
				 									byte[] bytesTmp = new byte[readCount];
				 									System.arraycopy(bytes, 0, bytesTmp, 0, readCount);
				 									bytes = bytesTmp;
				 								}
				 								buffer.append(new String(Base64.encodeBase64(bytes)));
				 							}
				 							GenericArtifactField sizeField = ga.addNewField(AttachmentMetaData.ATTACHMENT_SIZE,
			 										GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
			 								sizeField.setFieldValue(size);
			 								sizeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			 								sizeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				 							ga.setArtifactValue(buffer.toString());
				 							populateSrcAndDestForAttachment(syncInfo, ga);
				 							attachmentGAs.add(ga);
			 							}
			 						}
			 					}
			 				}
		 				}
		 			}
				}
				this.artifactHistoryList.set(null);
			}
		} catch (Exception e) {
			String message = "Exception while getting the attachment data";
			log.error(message,e);
			throw new CCFRuntimeException(message,e);
		}
		finally {
			this.attahcmentIDNameMap = null;
			connectionManager.releaseConnection(twsclient);
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
	public List<GenericArtifact> getArtifactData(Document syncInfo,
			String artifactId) {
		TrackerArtifactType trackerArtifactType =
			this.getTrackerArtifactTypeForArtifactId(syncInfo, artifactId);
		String artifactIdentifier = artifactId.substring(artifactId.lastIndexOf(":")+1);
		String artifactTypeNamespace = trackerArtifactType.getNamespace();
		String artifactTypeTagName = trackerArtifactType.getTagName();
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		long fromTime = lastModifiedDate.getTime();
		long toTime = System.currentTimeMillis();
		TrackerWebServicesClient twsclient = null;
		ClientArtifact artifact = null;
		try {
			twsclient = this.getConnection(syncInfo);
			ClientArtifactListXMLHelper listHelper = twsclient.getArtifactById(artifactIdentifier);
			List<ClientArtifact> artifacts = listHelper.getAllArtifacts();
			ptHelper.processWSErrors(listHelper);
			if(artifacts == null || artifacts.size() == 0){
				throw new CCFRuntimeException("There is no artifact with id "+artifactIdentifier);
			}
			else if(artifacts.size() == 1){
				artifact = artifacts.get(0);
			}
			else if(artifacts.size() > 1){
				throw new CCFRuntimeException("More than one artifact were returned for id "+artifactIdentifier);
			}
		} catch (Exception e) {
			String message = "Exception while getting the artifact data";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		}
		finally {
			if(twsclient != null){
				connectionManager.releaseConnection(twsclient);
				twsclient = null;
			}
		}
		String lastModifiedBy = artifact.getAttributeValue(TrackerWebServicesClient.DEFAULT_NAMESPACE,
				TrackerWebServicesClient.MODIFIED_BY_FIELD_NAME);
		if(lastModifiedBy.equals(username)){
			return new ArrayList<GenericArtifact>();
		}
		List<GenericArtifact> gaList = new ArrayList<GenericArtifact>();
		GenericArtifact ga = new GenericArtifact();
		ga.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
		ga.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
		ga.setSourceArtifactId(artifactId);
		Map<String, List<String>> attributes = artifact.getAttributes();
		for(String attributeName:attributes.keySet()){
			List<String> attValues = attributes.get(attributeName);
			String ptAttributeType = null;
			TrackerAttribute trackerAttribute = trackerArtifactType.getAttribute(attributeName);
			if(trackerAttribute == null) continue;
			String attributeDisplayName = trackerAttribute.getDisplayName();
			String attributeNamespace = trackerAttribute.getNamespace();
			String attributeTagName = trackerAttribute.getTagName();
			String attributeNamespaceDiaplayName = "{"+attributeNamespace+"}"+attributeDisplayName;
			GenericArtifactField.FieldValueTypeValue gaFieldType = null;
			boolean isAttributeRequired = false;
			if(attValues.size() > 0){
				isAttributeRequired = trackerAttribute.isRequired();
				ptAttributeType = trackerAttribute.getAttributeType();
				gaFieldType = ptGATypesMap.get(ptAttributeType);
			}
			else {
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
				if(trackerAttribute.getAttributeType().equals("MULTI_SELECT") || 
						trackerAttribute.getAttributeType().equals("SINGLE_SELECT")||
						trackerAttribute.getAttributeType().equals("STATE")){
					ArtifactTypeMetadata metadata = metadataHelper.getArtifactTypeMetadata(
							this.getRepositoryKey(syncInfo), artifactTypeNamespace, artifactTypeTagName);
					attValue = this.convertOptionValue(attributeNamespace, attributeTagName,
							attValue, metadata);
				}
				if(trackerAttribute.getAttributeType().equals("DATE")
						|| attributeName.equals(CREATED_ON_FIELD_NAME)
						|| attributeName.equals(MODIFIED_ON_FIELD_NAME)){
					String dateStr = attValue;
					if(!StringUtils.isEmpty(dateStr)){
						Date date = new Date(Long.parseLong(dateStr));
						field.setFieldValue(date);
					}
				}
				else {
					field.setFieldValue(attValue);
				}
				if(attributeName.equals(MODIFIED_ON_FIELD_NAME)){
					String modifiedOnStr = attValue;
					Date modifiedOnDate = new Date(Long.parseLong(modifiedOnStr));
					String lastModificationDate = DateUtil.format(modifiedOnDate);
					ga.setSourceArtifactLastModifiedDate(lastModificationDate);
				}
				else if(attributeName.equals(CREATED_ON_FIELD_NAME)){
					String createdOnStr = attValue;
					long createdOnTime = Long.parseLong(createdOnStr);
					try {
						twsclient = this.getConnection(syncInfo);
						ArtifactHistoryList ahlVersion = 
							twsclient.getChangeHistoryForArtifact(artifactIdentifier,
									createdOnTime, toTime);
						this.artifactHistoryList.set(ahlVersion);
						History[] historyList = ahlVersion.getHistory();
						if(historyList != null && historyList.length == 1){
							History history = historyList[0];
							int version = history.getHistoryTransaction().length;
//							history.getHistoryTransaction()[0].getReason()
							ga.setSourceArtifactVersion(Integer.toString(version));
						}
					} catch (Exception e) {
						String message = "Exception while getting the version of the artifact";
						log.error(message, e);
						throw new CCFRuntimeException(message,
								e);
					}
					finally {
						if(twsclient != null){
							connectionManager.releaseConnection(twsclient);
							twsclient = null;
						}
					}
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
				GenericArtifactField field = null;
				field = ga.addNewField("Comment",
						GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				field.setFieldValueHasChanged(true);
				field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				field.setFieldValue(commentText);
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
		gaList.add(ga);
		return gaList;
	}
	
	private String convertOptionValue(String attributeNamespace, String attributeTagName,
			String attributeValue, ArtifactTypeMetadata metadata){
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
			e.printStackTrace();
			throw new CCFRuntimeException("Could not get connection for PT",e);
		} catch (ConnectionException e) {
			e.printStackTrace();
			throw new CCFRuntimeException("Could not get connection for PT",e);
		}
		finally {
			if(twsclient != null){
				connectionManager.releaseConnection(twsclient);
			}
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
		String fromTime = Long.toString(lastModifiedDate.getTime()+1000);
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
				connectionManager.releaseConnection(twsclient);
			}
		}
		return artifacts;
	}
	
	/**
	 * Connects to the source SFEE system using the connectionInfo and credentialInfo
	 * details.
	 * 
	 * This method uses the ConnectionManager configured in the wiring file
	 * for the SFEEReader
	 *  
	 * @param systemId - The system id of the source SFEE system
	 * @param systemKind - The system kind of the source SFEE system
	 * @param repositoryId - The tracker id in the source SFEE system
	 * @param repositoryKind - The repository kind for the tracker
	 * @param connectionInfo - The SFEE server URL
	 * @param credentialInfo - User name and password concatenated with a delimiter.
	 * @return - The connection object obtained from the ConnectionManager
	 * @throws MaxConnectionsReachedException 
	 * @throws ConnectionException 
	 */
	public TrackerWebServicesClient connect(String systemId, String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo, String credentialInfo) throws MaxConnectionsReachedException, ConnectionException {
		//log.info("Before calling the parent connect()");
		TrackerWebServicesClient connection = null;
		connection = connectionManager.getConnection(systemId, systemKind, repositoryId,
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
		connectionManager.releaseConnection(connection);
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

	public ConnectionManager<TrackerWebServicesClient> getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(
			ConnectionManager<TrackerWebServicesClient> connectionManager) {
		this.connectionManager = connectionManager;
	}

	public String getConnectorUserDisplayName() {
		return connectorUserDisplayName;
	}

	public void setConnectorUserDisplayName(String connectorUserDisplayName) {
		this.connectorUserDisplayName = connectorUserDisplayName;
	}

}
