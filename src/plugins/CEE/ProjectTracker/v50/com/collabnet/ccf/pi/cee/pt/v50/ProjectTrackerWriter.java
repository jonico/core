package com.collabnet.ccf.pi.cee.pt.v50;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.rpc.ServiceException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.core.ws.exception.WSException;
import com.collabnet.tracker.common.ClientArtifact;
import com.collabnet.tracker.common.ClientArtifactComment;
import com.collabnet.tracker.common.ClientArtifactListXMLHelper;
import com.collabnet.tracker.core.TrackerWebServicesClient;
import com.collabnet.tracker.core.model.TrackerArtifactType;
import com.collabnet.tracker.core.model.TrackerAttribute;
import com.collabnet.tracker.ws.ArtifactHistoryList;
import com.collabnet.tracker.ws.ArtifactTypeMetadata;
import com.collabnet.tracker.ws.Attribute;
import com.collabnet.tracker.ws.History;
import com.collabnet.tracker.ws.HistoryTransaction;
import com.collabnet.tracker.ws.Option;

public class ProjectTrackerWriter extends LifecycleComponent implements IDataProcessor {
	private static final Log log = LogFactory.getLog(ProjectTrackerWriter.class);
	private static final Log logConflictResolutor = LogFactory.getLog("com.collabnet.ccf.core.conflict.resolution");
	private String serverUrl = null;
	private String username = null;
	private String password = null;
	private ConnectionManager<TrackerWebServicesClient> connectionManager = null;
	private MetaDataHelper metadataHelper = MetaDataHelper.getInstance();
	ProjectTrackerHelper ptHelper = ProjectTrackerHelper.getInstance();
	public Object[] process(Object data) {
		GenericArtifact ga = null;
		if(data instanceof Document){
			ga = this.processXMLDocument((Document)data);
		}
		Document doc = null;
		try {
			doc = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
		} catch (GenericArtifactParsingException e) {
			String message = "Exception while parsing artifact";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		}
		return new Object[]{doc};
	}
	
	public GenericArtifact processXMLDocument(Document document){
		System.out.println();
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(document);
	    } catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
	    GenericArtifact.ArtifactActionValue artifactAction = ga.getArtifactAction();
	    GenericArtifact.ArtifactTypeValue artifactType = ga.getArtifactType();
		if(artifactType == ArtifactTypeValue.PLAINARTIFACT){
			if(artifactAction == GenericArtifact.ArtifactActionValue.CREATE){
				ga = this.createProjectTrackerArtifact(ga);
			}
			else if(artifactAction == GenericArtifact.ArtifactActionValue.UPDATE){
				ga = this.updateProjectTrackerArtifact(ga);
			}
			else if(artifactAction == GenericArtifact.ArtifactActionValue.DELETE){
				// INFO Delete not supported as of now
			}
			else if(artifactAction == GenericArtifact.ArtifactActionValue.IGNORE){
				// INFO - Ignoring the artifact and returning as it is
			}
		}
		else if(artifactType == ArtifactTypeValue.ATTACHMENT){
			if(artifactAction == GenericArtifact.ArtifactActionValue.CREATE){
				ga = this.createProjectTrackerAttachment(ga);
			}
			else if(artifactAction == GenericArtifact.ArtifactActionValue.UPDATE){
				// INFO Nothing to do
			}
			else if(artifactAction == GenericArtifact.ArtifactActionValue.DELETE){
				// INFO Delete not supported as of now
			}
			else if(artifactAction == GenericArtifact.ArtifactActionValue.IGNORE){
				// INFO - Ignoring the artifact and returning as it is
			}
		}
		return ga;
	}
	
	private GenericArtifact createProjectTrackerAttachment(GenericArtifact ga) {
		String targetArtifactId = ga.getDepParentTargetArtifactId();
		String artifactId = ptHelper.getArtifactIdFromFullyQualifiedArtifactId(targetArtifactId);
		TrackerWebServicesClient twsclient = this.getConnection(ga);
		byte[] data = Base64.decodeBase64(ga.getArtifactValue().getBytes());
		String attachmentMimeType = GenericArtifactHelper.getStringGAField(AttachmentMetaData.ATTACHMENT_MIME_TYPE, ga);
		String attachmentName = GenericArtifactHelper.getStringGAField(AttachmentMetaData.ATTACHMENT_NAME, ga);
		javax.mail.util.ByteArrayDataSource dataSource = new javax.mail.util.ByteArrayDataSource(data,attachmentMimeType);
		dataSource.setName(attachmentName);
		try {
			twsclient.postAttachment(artifactId, "Attachment added by Connector", dataSource);
		} catch (WSException e) {
			String message = "Exception while creating PT artifact";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} catch (RemoteException e) {
			String message = "Exception while creating PT artifact";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} catch (ServiceException e) {
			String message = "Exception while creating PT artifact";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		}
		finally {
			connectionManager.releaseConnection(twsclient);
		}
		return ga;
	}

	private GenericArtifact updateProjectTrackerArtifact(GenericArtifact ga) {
		String targetArtifactId = ga.getTargetArtifactId();
		String targetArtifactTypeNameSpace =
			ptHelper.getArtifactTypeNamespaceFromFullyQualifiedArtifactId(targetArtifactId);
		String targetArtifactTypeTagName =
			ptHelper.getArtifactTypeTagNameFromFullyQualifiedArtifactId(targetArtifactId);
		TrackerWebServicesClient twsclient = this.getConnection(ga);
		
		try {
			int version = this.getArtifactVerstion(targetArtifactId, new Date(0).getTime(),
					new Date().getTime(), twsclient);
			int lastSyncVersion = -1;
			String lastSyncVersionStr = ga.getTargetArtifactVersion();
			try{
				lastSyncVersion = Integer.parseInt(lastSyncVersionStr);
			}
			catch(NumberFormatException e){
				String message = "Last successful synchronization version of artifact "
					+ targetArtifactId + " is not a number "+lastSyncVersionStr;
				log.error(message, e);
				throw new CCFRuntimeException(message, e);
			}
			if(lastSyncVersion < version){
				String conflictResolutionPolicy = ga.getConflictResolutionPriority();
				if(conflictResolutionPolicy.equals(GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_IGNORE)){
					logConflictResolutor.warn("Conflict detected for PT artifact "+targetArtifactId
							+". Changes are ignored.");
					ga.setArtifactAction(GenericArtifact.ArtifactActionValue.IGNORE);
					return ga;
				}
				else {
					logConflictResolutor.info("Conflict detected for PT artifact "+targetArtifactId
							+". Changes are overridden.");
				}
			}
			List<ClientArtifact> cla = null;
			cla = new ArrayList<ClientArtifact>();
			ClientArtifact ca = this.getClientArtifactFromGenericArtifact(ga, twsclient,
					targetArtifactTypeNameSpace, targetArtifactTypeTagName);
			cla.add(ca);
			ClientArtifactListXMLHelper artifactHelper = twsclient.updateArtifactList(cla);
			ptHelper.processWSErrors(artifactHelper);
			List<ClientArtifact> artifacts = artifactHelper.getAllArtifacts();
			if(artifacts.size() == 1){
				ClientArtifact artifact = artifacts.get(0);
				ga.setTargetArtifactId("{"+targetArtifactTypeNameSpace+"}"
						+targetArtifactTypeTagName+":"+artifact.getArtifactID());
				String modifiedOn = artifact.getAttributeValue(
						ProjectTrackerReader.TRACKER_NAMESPACE, ProjectTrackerReader.MODIFIED_ON_FIELD);
				Date modifiedOnDate = new Date(Long.parseLong(modifiedOn));
				ga.setTargetArtifactLastModifiedDate(DateUtil.format(modifiedOnDate));
				String createdOn = artifact.getAttributeValue(
						ProjectTrackerReader.TRACKER_NAMESPACE, ProjectTrackerReader.CREATED_ON_FIELD);
				ga.setTargetArtifactVersion(Integer.toString(version+1));
			}
		} catch (WSException e) {
			String message = "WSException while updating artifact " + targetArtifactId;
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} catch (RemoteException e) {
			String message = "RemoteException while updating artifact " + targetArtifactId;
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} catch (ServiceException e) {
			String message = "ServiceException while updating artifact " + targetArtifactId;
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} catch (Exception e) {
			String message = "Exception while updating artifact " + targetArtifactId;
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		}
		finally {
			connectionManager.releaseConnection(twsclient);
		}
		return ga;
	}
	
	private int getArtifactVerstion(String artifactId, long createdOnTime, long modifiedOnTime,
			TrackerWebServicesClient twsclient) throws Exception{
		ArtifactHistoryList ahlVersion = 
			twsclient.getChangeHistoryForArtifact(artifactId,
					createdOnTime, modifiedOnTime);
		History[] historyList = ahlVersion.getHistory();
		int version = 0;
		if(historyList != null && historyList.length == 1){
			History history = historyList[0];
			HistoryTransaction[] transactionList = history.getHistoryTransaction();
			if(transactionList != null){
				version = transactionList.length;
			}
		}
		return version;
	}
	
	private GenericArtifact createProjectTrackerArtifact(GenericArtifact ga) {
		String targetRepositoryId = ga.getTargetRepositoryId();
		String repositoryKey = this.getRepositoryKey(ga);
		String artifactTypeDisplayName = targetRepositoryId.substring(targetRepositoryId.lastIndexOf(":")+1);
		
		TrackerWebServicesClient twsclient = null;
		
		try {
			twsclient = this.getConnection(ga);
			TrackerArtifactType trackerArtifactType =
				metadataHelper.getTrackerArtifactType(repositoryKey);
			if(trackerArtifactType == null){
				trackerArtifactType = metadataHelper.getTrackerArtifactType(repositoryKey,
						artifactTypeDisplayName, twsclient);
			}
			String targetArtifactTypeNamespace = trackerArtifactType.getNamespace();
			String targetArtifactTypeTagName = trackerArtifactType.getTagName();
			List<ClientArtifact> cla = null;
			cla = new ArrayList<ClientArtifact>();
			ClientArtifact ca = this.getClientArtifactFromGenericArtifact(ga, twsclient,
					targetArtifactTypeNamespace, targetArtifactTypeTagName);
			cla.add(ca);
			ClientArtifactListXMLHelper artifactHelper = twsclient.createArtifactList(cla);
			ptHelper.processWSErrors(artifactHelper);
			List<ClientArtifact> artifacts = artifactHelper.getAllArtifacts();
			if(artifacts.size() == 1) {
				ClientArtifact artifact = artifacts.get(0);
				ga.setTargetArtifactId("{"+targetArtifactTypeNamespace+"}"
						+targetArtifactTypeTagName+":"+artifact.getArtifactID());
				String createdOn = artifact.getAttributeValue(
						ProjectTrackerReader.TRACKER_NAMESPACE, ProjectTrackerReader.CREATED_ON_FIELD);
				Date createdOnDate = new Date(Long.parseLong(createdOn));
				ga.setTargetArtifactLastModifiedDate(DateUtil.format(createdOnDate));
				ga.setTargetArtifactVersion(Integer.toString(0));
			}
			else {
				String message = "Artifact creation failed...!!!";
				log.error(message);
				throw new CCFRuntimeException(message);
			}
		} catch (WSException e) {
			String message = "WSException while creating artifact";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} catch (RemoteException e) {
			String message = "RemoteException while creating artifact";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} catch (ServiceException e) {
			String message = "ServiceException while creating artifact";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		} catch (Exception e) {
			String message = "Exception while creating artifact";
			log.error(message, e);
			throw new CCFRuntimeException(message, e);
		}
		finally {
			connectionManager.releaseConnection(twsclient);
		}
		return ga;
	}
	
	private ClientArtifact getClientArtifactFromGenericArtifact(GenericArtifact ga,
			TrackerWebServicesClient twsclient, String targetArtifactTypeNameSpace,
			String targetArtifactTypeTagName){
		String targetArtifactId = ga.getTargetArtifactId();
		String artifactId = null;
		if(!targetArtifactId.equals(GenericArtifact.VALUE_UNKNOWN)){
			artifactId = ptHelper.getArtifactIdFromFullyQualifiedArtifactId(targetArtifactId);
		}
		String repositoryKey = this.getRepositoryKey(ga);
		TrackerArtifactType trackerArtifactType = metadataHelper.getTrackerArtifactType(repositoryKey,
				targetArtifactTypeNameSpace, targetArtifactTypeTagName, twsclient);
		ArtifactTypeMetadata metadata = metadataHelper.getArtifactTypeMetadata(repositoryKey,
				targetArtifactTypeNameSpace, targetArtifactTypeTagName);

		ClientArtifact ca = new ClientArtifact();
		ca.setTagName(targetArtifactTypeTagName);
		ca.setNamespace(targetArtifactTypeNameSpace);
		if(artifactId != null) {
			ca.addAttributeValue(TrackerWebServicesClient.DEFAULT_NAMESPACE, "id", artifactId);
		}
		for(GenericArtifactField field:ga.getAllGenericArtifactFields()){
			String fieldName = field.getFieldName();
			String fieldDisplayName = null;
			String namespace = ptHelper.getNamespace(fieldName);
			if(namespace == null){
				fieldDisplayName = fieldName;
				namespace = targetArtifactTypeNameSpace;
			}
			else {
				fieldDisplayName = ptHelper.getEntityName(fieldName);
			}
			if(fieldName.equals("Comment")){
				this.addComment(field,ca);
			}
			else{
				System.out.println();
				String fullyQualifiedFieldTagName = this.getFullyQualifiedFieldTagName(fieldDisplayName,
						namespace, trackerArtifactType);
				TrackerAttribute trackerAttribute = trackerArtifactType.getAttribute(fullyQualifiedFieldTagName);
				this.addAttribute(field, ca, trackerAttribute, fullyQualifiedFieldTagName, metadata);
			}
		}
		return ca;
	}
	
	private String getFullyQualifiedFieldTagName(String fieldDisplayName, String artifactTypeTagName,
								TrackerArtifactType artifactType){
		Map<String, TrackerAttribute> attributesMap =  artifactType.getAttributes();
		String fullyQualifiedFieldTagName = null;
		for(Entry<String, TrackerAttribute> entry: attributesMap.entrySet()){
			String attributeFullyQualifiedName = entry.getKey();
			TrackerAttribute attribute = entry.getValue();
			String displayName = attribute.getDisplayName();
			String namespace = attribute.getNamespace();
			if(displayName.equals(fieldDisplayName)){
				if(artifactTypeTagName.equals(namespace)){
					return attributeFullyQualifiedName;
				}
				else {
					if(fullyQualifiedFieldTagName == null){
						fullyQualifiedFieldTagName = attributeFullyQualifiedName;
					}
					else {
						String message = "There are two fields with the same name "
							+fieldDisplayName+" in "+artifactTypeTagName+" "+artifactType.getDisplayName();
						log.error(message);
						throw new CCFRuntimeException(message);
					}
				}
			}
		}
		if(fullyQualifiedFieldTagName == null){
			String message = "There is no field with the name "
				+fieldDisplayName+" in "+artifactTypeTagName+" "+artifactType.getDisplayName();
			log.error(message);
			throw new CCFRuntimeException(message);
		}
		return fullyQualifiedFieldTagName;
	}

	private void addComment(GenericArtifactField field, ClientArtifact ca) {
		ClientArtifactComment comment =
			new ClientArtifactComment(null, null,field.getFieldValue().toString(),null);
		ca.addComment(comment);
	}

	private void addAttribute(GenericArtifactField field, ClientArtifact ca,
			TrackerAttribute trackerAttribute, String fullyQualifiedFieldTagName,
			ArtifactTypeMetadata metadata) {
		String attributeNamespace =
			ptHelper.getArtifactTypeNamespaceFromFullyQualifiedArtifactType(fullyQualifiedFieldTagName);
		String attributeTagName =
			ptHelper.getArtifactTypeTagNameFromFullyQualifiedArtifactType(fullyQualifiedFieldTagName);
		Object fieldValue = field.getFieldValue();
		if(fieldValue == null) return;
		FieldValueTypeValue fieldType = field.getFieldValueType();
		String attributeValue = null;
		if(fieldType == FieldValueTypeValue.STRING){
			attributeValue = fieldValue.toString();
		}
		else if(fieldType == FieldValueTypeValue.INTEGER){
			attributeValue = fieldValue.toString();
		}
		else if(fieldType == FieldValueTypeValue.DOUBLE){
			attributeValue = fieldValue.toString();
		}
		else if(fieldType == FieldValueTypeValue.DATE){
			GregorianCalendar gc = (GregorianCalendar) fieldValue;
			attributeValue = Long.toString(gc.getTime().getTime());
		}
		else if(fieldType == FieldValueTypeValue.DATETIME){
			Date date = (Date) fieldValue;
			attributeValue = Long.toString(date.getTime());
		}
		else if(fieldType == FieldValueTypeValue.BOOLEAN){
			Boolean value = (Boolean) fieldValue;
			attributeValue = value.toString();
		}
		else if(fieldType == FieldValueTypeValue.BASE64STRING){
			attributeValue = fieldValue.toString();
		}
		else if(fieldType == FieldValueTypeValue.HTMLSTRING){
			attributeValue = fieldValue.toString();
		}
		else if(fieldType == FieldValueTypeValue.USER){
			attributeValue = fieldValue.toString();
		}
		if(trackerAttribute.getAttributeType().equals("MULTI_SELECT")){
			ca.addAttributeValue(attributeNamespace, attributeTagName, null);
			attributeValue = this.convertOptionValue(attributeNamespace, attributeTagName,
					attributeValue, metadata);
			
		}
		else if(trackerAttribute.getAttributeType().equals("SINGLE_SELECT")){
			ca.addAttributeValue(attributeNamespace, attributeTagName, null);
			attributeValue = this.convertOptionValue(attributeNamespace, attributeTagName,
					attributeValue, metadata);
		}
		ca.addAttributeValue(attributeNamespace, attributeTagName, attributeValue);
	}
	
	private String convertOptionValue(String attributeNamespace, String attributeTagName,
			String attributeValue, ArtifactTypeMetadata metadata){
		String optionValue = null;
		for(Attribute att:metadata.getAttribute()){
			String namespace = att.getNamespace();
			String tagName = att.getTagName();
			if(namespace.equals(attributeNamespace) && tagName.equals(attributeTagName)){
				for(Option option:att.getOptions()){
					String optionDisplayName = option.getDisplayName();
					if(optionDisplayName.equals(attributeValue)){
						optionValue = option.getTagName();
					}
				}
			}
		}
		if(optionValue == null) throw new CCFRuntimeException("Option tagname for option "+attributeValue+
				"is not available in {"+attributeNamespace+"}"+attributeTagName);
		return optionValue;
	}

	private TrackerWebServicesClient getConnection(GenericArtifact ga){
		String systemId = ga.getTargetSystemId();
		String systemKind = ga.getTargetSystemKind();
		String repositoryId = ga.getTargetRepositoryId();
		String repositoryKind = ga.getTargetRepositoryKind();
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
		log.info("Getting connection to PT");
		TrackerWebServicesClient connection = null;
		connection = connectionManager.getConnection(systemId, systemKind, repositoryId,
			repositoryKind, connectionInfo, credentialInfo);
		return connection;
	}
	
	private String getRepositoryKey(GenericArtifact ga){
		String targetSystemId = ga.getTargetSystemId();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetSystemKind = ga.getTargetSystemKind();
		String targetRepositoryKind = ga.getTargetRepositoryKind();
		return targetSystemId + targetSystemKind + targetRepositoryId + targetRepositoryKind;
	}

	public void reset(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ConnectionManager<TrackerWebServicesClient> getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(
			ConnectionManager<TrackerWebServicesClient> connectionManager) {
		this.connectionManager = connectionManager;
	}

}
