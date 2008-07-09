package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

/**
 * This class retrieves the changed artifact details from an SFEE
 * system repository.
 * 
 *  It uses the last read time of the sync info and fetches all the 
 *  artifact data that are changed after the last read time of the
 *  particular repository.
 *  
 * @author madhusuthanan (madhusuthanan@collab.net)
 *
 */
/**
 * @author madhusuthanan
 *
 */
public class SFEEReader extends AbstractReader {
    
	private static final Log log = LogFactory.getLog(SFEEReader.class);
	
	private boolean isDry=true;

	private Object readerContext = null;

	private SFEETrackerHandler trackerHandler = null;
	
	private SFEEAttachmentHandler attachmentHandler = null;
	
	private SFEEToGenericArtifactConverter artifactConverter = null;
	
	private ConnectionManager<Connection> connectionManager = null;
	
	private String serverUrl = null;

	private String password = null;

	private String username = null;
	
	public SFEEReader() {
	    super();
	    init();
	}

    public SFEEReader(String id) {
	    super(id);
	    init();
	}
    
    public void init(){
    	super.init();
    	artifactConverter = new SFEEToGenericArtifactConverter();
    }
    
	private Date getLastModifiedDate(Document syncInfo){
		String lastModifiedDateString = this.getLastSourceArtifactModificationDate(syncInfo);
		Date lastModifiedDate = null; 
		if (!StringUtils.isEmpty(lastModifiedDateString)) {
			log.debug("Artifacts to be fetched from "+lastModifiedDateString);
			lastModifiedDate=(Date)SFEEGAHelper.asTypedValue(lastModifiedDateString, "DateTime");
			lastModifiedDate.setTime(lastModifiedDate.getTime()+1);
		} else {
			lastModifiedDate = new Date(0);
		}
		return lastModifiedDate;
	}

	public Object getReaderContext() {
		return readerContext;
	}
	
	public boolean isDry() {
		return isDry;
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
	public Connection connect(String systemId, String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo, String credentialInfo) throws MaxConnectionsReachedException, ConnectionException {
		//log.info("Before calling the parent connect()");
		Connection connection = null;
		connection = connectionManager.getConnection(systemId, systemKind, repositoryId,
			repositoryKind, connectionInfo, credentialInfo);
		isDry=false;
		return connection;
	}
	
	/**
	 * Releases the connection to the ConnectionManager.
	 * 
	 * @param connection - The connection to be released to the ConnectionManager
	 */
	public void disconnect(Connection connection) {
		connectionManager.releaseConnection(connection);
	}
	
	/**
	 * Populates the source and destination attributes for this GenericArtifact
	 * object from the Sync Info database document.
	 * 
	 * @param syncInfo
	 * @param ga
	 */
	private void populateSrcAndDest(Document syncInfo, GenericArtifact ga){
		String sourceArtifactId = ga.getSourceArtifactId();
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		
		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
		String targetRepositoryKind = this.getTargetRepositoryKind(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetSystemKind = this.getTargetSystemKind(syncInfo);
		
		if(StringUtils.isEmpty(sourceArtifactId)){
			List<GenericArtifactField> fields = ga.getAllGenericArtifactFieldsWithSameFieldName("Id");
			for(GenericArtifactField field:fields){
				sourceArtifactId = field.getFieldValue().toString();
			}
		}
		ga.setSourceArtifactId(sourceArtifactId);
		ga.setSourceRepositoryId(sourceRepositoryId);
		ga.setSourceRepositoryKind(sourceRepositoryKind);
		ga.setSourceSystemId(sourceSystemId);
		ga.setSourceSystemKind(sourceSystemKind);
		
		ga.setTargetRepositoryId(targetRepositoryId);
		ga.setTargetRepositoryKind(targetRepositoryKind);
		ga.setTargetSystemId(targetSystemId);
		ga.setTargetSystemKind(targetSystemKind);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		boolean exceptionsPresent = false;
		if(StringUtils.isEmpty(serverUrl)){
			exceptions.add(new ValidationException("serverUrl-property not set",this));
			exceptionsPresent = true;
		}
		if(StringUtils.isEmpty(username)){
			exceptions.add(new ValidationException("username-property not set",this));
			exceptionsPresent = true;
		}
		if(password == null){
			exceptions.add(new ValidationException("password-property not set",this));
			exceptionsPresent = true;
		}
		if(!exceptionsPresent){
			trackerHandler = new SFEETrackerHandler(getServerUrl());
			attachmentHandler = new SFEEAttachmentHandler(getServerUrl());
		}
	}

	public void reset(Object context) {
	}

	public SFEEAttachmentHandler getAttachmentHandler() {
		return attachmentHandler;
	}

	public void setAttachmentHandler(SFEEAttachmentHandler attachmentHandler) {
		this.attachmentHandler = attachmentHandler;
	}

	/**
	 * Queries the artifact with the artifactId to find out if there are any
	 * attachments added to the artifact after the last read time in the 
	 * Sync Info object.
	 * If there are attachments added to this artifact after the last
	 * read time for this tracker then the attachment data is retrieved 
	 * and returned as a GenericArtifact object.
	 * If there are multiple attachments each of them are encoded in a
	 * separate GenericArtifact object and returned in the list.
	 * 
	 * @see com.collabnet.ccf.core.AbstractReader#getArtifactAttachments(org.dom4j.Document, java.lang.String)
	 */
	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			String artifactId) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		Connection connection;
		try {
			connection = connect(sourceSystemId, sourceSystemKind, sourceRepositoryId,
					sourceRepositoryKind, serverUrl, 
					username+SFEEConnectionFactory.PARAM_DELIMITER+password);
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SFEE system. Max connections reached for "+
									serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SFEE system "+
									serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		List<String> artifactIds = new ArrayList<String>();
		artifactIds.add(artifactId);
		List<GenericArtifact> attachments = null;
		try {
			attachments = attachmentHandler.listAttachments(connection.getSessionId(),
					lastModifiedDate,getUsername(),artifactIds, connection.getSfSoap(),
					this.getMaxAttachmentSizePerArtifact());
			for(GenericArtifact attachment:attachments){
				populateSrcAndDestForAttachment(syncInfo, attachment);
			}
		} catch (RemoteException e) {
			String cause = "During the attachment retrieval process from SFEE, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			this.disconnect(connection);
		}
		return attachments;
	}

	/**
	 * Queries the tracker for the artifact with artifactId and returns its latest
	 * data encoded in an GenericArtifact object.
	 * The SFEEReader is capable of retrieving the artifact change history. But this
	 * feature is turned off as of now.
	 * 
	 * @see com.collabnet.ccf.core.AbstractReader#getArtifactData(org.dom4j.Document, java.lang.String)
	 */
	@Override
	public List<GenericArtifact> getArtifactData(Document syncInfo,
			String artifactId) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		Connection connection;
		try {
			connection = connect(sourceSystemId, sourceSystemKind, sourceRepositoryId,
					sourceRepositoryKind, serverUrl, 
					username+SFEEConnectionFactory.PARAM_DELIMITER+password);
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SFEE system. Max connections reached for "+
								serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SFEE system "+
								serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		ArrayList<GenericArtifact> gaList = new ArrayList<GenericArtifact>();
		try {
			TrackerFieldSoapDO[] trackerFields = null;
			HashMap<String, List<TrackerFieldSoapDO>> fieldsMap = null;
			if(this.isIncludeFieldMetaData()){
				trackerFields = trackerHandler.getFlexFields(connection.getSessionId(), sourceRepositoryId);
				fieldsMap =	SFEEAppHandler.loadTrackerFieldsInHashMap(trackerFields);
			}
			ArtifactSoapDO artifact = trackerHandler.getTrackerItem(connection.getSessionId(), artifactId);
			SFEEAppHandler appHandler = new SFEEAppHandler(connection.getSfSoap(), connection.getSessionId());
			appHandler.addComments(artifact,
					lastModifiedDate,this.getUsername());
			GenericArtifact genericArtifact = artifactConverter.convert(artifact,
					fieldsMap, lastModifiedDate, this.isIncludeFieldMetaData());
			populateSrcAndDest(syncInfo, genericArtifact);
			gaList.add(genericArtifact);
		} catch (RemoteException e) {
			String cause = "During the artifact retrieval process from SFEE, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			this.disconnect(connection);
		}
		return gaList;
	}

	/**
	 * This method is supposed to return all the artifacts that are associated
	 * with this artifact. But not implemented yet.
	 * Returns an empty list.
	 * @see com.collabnet.ccf.core.AbstractReader#getArtifactDependencies(org.dom4j.Document, java.lang.String)
	 */
	@Override
	public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId) {
		return new ArrayList<GenericArtifact>();
	}

	/**
	 * This method queries the particular tracker in the source SFEE system
	 * to check if there are artifacts changed/created after the last read time
	 * coming in, in the Sync Info object.
	 * 
	 * If there are changed artifacts their ids are returned in a List.
	 * 
	 * @see com.collabnet.ccf.core.AbstractReader#getChangedArtifacts(org.dom4j.Document)
	 */
	@Override
	public List<String> getChangedArtifacts(Document syncInfo) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String lastSynchronizedArtifactId = this.getLastSourceArtifactId(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
		int version = 0;
		try{
			version = Integer.parseInt(lastSynchronizedVersion);
		}catch(NumberFormatException e){
			log.warn("Version string is not a number "+lastSynchronizedVersion, e);
		}
		Connection connection;
		try {
			connection = connect(sourceSystemId, sourceSystemKind, sourceRepositoryId,
					sourceRepositoryKind, serverUrl, 
					username+SFEEConnectionFactory.PARAM_DELIMITER+password);
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SFEE system. Max connections reached for "+
								serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SFEE system "+
							serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		if(lastModifiedDate == null){
			lastModifiedDate = new Date(0);
		}
		ArrayList<String> artifactIds = new ArrayList<String>();
		List<ArtifactSoapDO> artifactRows = null;
		try {
			artifactRows = trackerHandler.getChangedTrackerItems(connection.getSessionId(),
					sourceRepositoryId,lastModifiedDate,lastSynchronizedArtifactId,version);
		} catch (RemoteException e) {
			String cause = "During the changed artifacts retrieval process from SFEE, an exception occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			this.disconnect(connection);
		}
		if(artifactRows != null){
			for(ArtifactSoapDO artifact:artifactRows){
				if(!artifact.getLastModifiedBy().equals(this.username)){
					String artifactId = artifact.getId();
					artifactIds.add(artifactId);
				}
			}
		}
		return artifactIds;
	}

	/**
	 * Returns the server URL of the source SFEE system that is
	 * configured in the wiring file.
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Sets the source SFEE system's SOAP server URL.
	 * 
	 * @param serverUrl - the URL of the source SFEE system.
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * Returns the password configured for this source SFEE system
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the user that is used to connect to the source SFEE system.
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gives the username configured to connect to the source SFEE system.
	 * 
	 * @return - The user name configured for this source SFEE system
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username used to connect to the source SFEE system.
	 * 
	 * @param username - the username to connect to the source SFEE system.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gives the ConnectionManager object from which the SFEEReader
	 * gets the connections from.
	 * 
	 * @return - the ConnectionManager
	 */
	public ConnectionManager<Connection> getConnectionManager() {
		return connectionManager;
	}

	/**
	 * Sets the ConnectionManager object that is used to retrieve connections
	 * from by the SFEEReader.
	 * 
	 * @param connectionManager
	 */
	public void setConnectionManager(ConnectionManager<Connection> connectionManager) {
		this.connectionManager = connectionManager;
	}
}
