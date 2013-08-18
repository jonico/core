package com.collabnet.ccf.rcq;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.dom4j.Document;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;

public class RCQReader extends AbstractReader<RCQConnection> {

	private String userName;
	private String password;
	private String serverUrl = "RCQSERVERNOTUSED";
	private boolean ignoreConnectorUserUpdates;
	private RCQAttachmentHandler attachee = new RCQAttachmentHandler();

	public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId) {
		// no dependencies
		return new ArrayList<GenericArtifact>();
	}

	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			GenericArtifact artifactData) {
		String artifactId = artifactData.getSourceArtifactId();
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		

		//log.debug("Getting attachments for artifact " + artifactId );
		
		RCQConnection connection = null;
		
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, serverUrl ,
					getUserName() + RCQConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the ClearQuest system (maxConnections). ";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the ClearQuest system.";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
	
		
		List<String> artifactIds = new ArrayList<String>();
		artifactIds.add(artifactId);
		
		List<GenericArtifact> attachments = null;
		attachments = attachee.listAttachments(connection, 
				lastModifiedDate, 
				getUserName(), 
				artifactIds, 
				this.getMaxAttachmentSizePerArtifact() , 
				this.isShipAttachmentsWithArtifact() , 
				artifactData);
		for ( GenericArtifact attachment : attachments ) {
			populateSrcAndDestForAttachment(syncInfo, attachment);
		}

		
		disconnect(connection);
		return attachments;
	}

	@Override
	public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		String lastSynchronizedArtifactId = this.getLastSourceArtifactId(syncInfo);
		
		RCQConnection connection = null;
		
		try {
			connection = connect(sourceSystemId , sourceSystemKind , 
					sourceRepositoryId , sourceRepositoryKind, serverUrl ,
					getUserName() + RCQConnectionFactory.PARAM_DELIMITER
					+ getPassword());
		} catch (ConnectionException e) {
			logme(syncInfo);
			log.error("Cannot connect to clearquest! SeverUrl=" + serverUrl + ". Dumping syncInfo...");
		} catch (MaxConnectionsReachedException e) {
			log.error("maximum connections opened. Wait and try again");
		}
		
		GenericArtifact ga = new GenericArtifact();
		
		try {
			populateSrcAndDest(syncInfo, ga);
			ga.setSourceArtifactId(artifactId);
			ga.setArtifactMode(ArtifactModeValue.COMPLETE);
			ga.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);
			rcqHandler.updateGAWithRecordData(connection, artifactId, isIgnoreConnectorUserUpdates(), this.getLastModifiedDate(syncInfo) , userName , ga);
		} catch (Exception e) {
			String cause = "During the artifact retrieval process from ClearQuest, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		try {
			log.trace("Retrieved Generic Artifact:\n" + GenericArtifactHelper.createGenericArtifactXMLDocument(ga).asXML() + "\n");
		} catch (GenericArtifactParsingException e) {
			log.trace("ERROR: could not get XML structure for GenericArtifact");
		}
		return	ga;
		
	}

	private void populateSrcAndDest(Document syncInfo, GenericArtifact ga) {
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String conflictResolutionPriority = this
				.getConflictResolutionPriority(syncInfo);
		
		String sourceSystemTimezone = this.getSourceSystemTimezone(syncInfo);
		String targetSystemTimezone = this.getTargetSystemTimezone(syncInfo);

		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
		String targetRepositoryKind = this.getTargetRepositoryKind(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetSystemKind = this.getTargetSystemKind(syncInfo);

		ga.setSourceRepositoryId(sourceRepositoryId);
		ga.setSourceRepositoryKind(sourceRepositoryKind);
		ga.setSourceSystemId(sourceSystemId);
		ga.setSourceSystemKind(sourceSystemKind);
		ga.setConflictResolutionPriority(conflictResolutionPriority);
		ga.setSourceSystemTimezone(sourceSystemTimezone);

		ga.setTargetRepositoryId(targetRepositoryId);
		ga.setTargetRepositoryKind(targetRepositoryKind);
		ga.setTargetSystemId(targetSystemId);
		ga.setTargetSystemKind(targetSystemKind);
		ga.setTargetSystemTimezone(targetSystemTimezone);
	}


	private void logme(Document syncInfo ) {
		
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		String lastSynchedArtifactId = this.getLastSourceArtifactId(syncInfo);

		// replaced deprecated toGMTString()
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
		
		log.debug("----> member vars");
		log.debug("ignoreConnectorUserUpdates : " + this.isIgnoreConnectorUserUpdates());
		log.debug("password                   : " + this.password);
		log.debug("serverUrl                  : " + this.serverUrl);
		log.debug("userName                   : " + this.userName);
		log.debug("----> syncInfo");
		log.debug("sourceSystemId          : " + sourceSystemId);
		log.debug("sourceSystemKind        : " + sourceSystemKind);
		log.debug("sourceRepositoryId      : " + sourceRepositoryId);
		log.debug("sourceRepositoryKind    : " + sourceRepositoryKind);
		log.debug("lastSynchronizedVersion : " + lastSynchronizedVersion);
		log.debug("targetSystemId          : " + targetSystemId );
		log.debug("targetRepositoryId      : " + targetRepositoryId);
		log.debug("lastModifiedDate        : " + sdf.format(lastModifiedDate) );
		log.debug("lastSynchedArtifactId   : " + lastSynchedArtifactId);
	}
	
	
	@Override
	public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		String lastSynchedArtifactId = this.getLastSourceArtifactId(syncInfo);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		
		log.trace("=============================>>>>>>>>>>>>>>> GETTING CHANGED ARTIFACTS SINCE " + formatter.format(lastModifiedDate));
		
		RCQConnection connection = null;
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, serverUrl ,
					getUserName() + RCQConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the ClearQuest system (maxConnections). ";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the ClearQuest system.";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}

		ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();
		try {
			// results returned in byRef object artifactStates
			rcqHandler.getChangedRecords(
					connection, 
					lastModifiedDate, 
					lastSynchronizedVersion, 
					lastSynchedArtifactId, 
					artifactStates,
					isIgnoreConnectorUserUpdates());

		} catch (Exception e) {
			String cause = "During the artifact retrieval process from ClearQuest, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		
		return artifactStates;
		
		
	}
	
	public void disconnect(RCQConnection connection) {
		getConnectionManager().releaseConnection(connection);
	}

	/**
	 * Retrieves whether updated and created artifacts from the connector user
	 * should be ignored This is the default behavior to avoid infinite update
	 * loops. However, in artifact export scenarios, where all artifacts should
	 * be extracted, this property should is set to false
	 * 
	 * @return the ignoreConnectorUserUpdates whether to ignore artifacts that
	 *         have been created or lastly modified by the connector user
	 */
	public boolean isIgnoreConnectorUserUpdates() {
		return ignoreConnectorUserUpdates;
	}
	
	


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerUrl() {
		return serverUrl;
	}

//	public void setServerUrl(String serverUrl) {
//		this.serverUrl = serverUrl;
//	}
//	
	public RCQConnection connect(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException,
			ConnectionException {
		RCQConnection connection = null;
		connection = getConnectionManager()
				.getConnectionToUpdateOrExtractArtifact(systemId, systemKind,
						repositoryId, repositoryKind, connectionInfo,
						credentialInfo);
		if (log.isTraceEnabled()) {
			Runtime rtInfo = Runtime.getRuntime();
			int mb = 1024^2;
			long total = rtInfo.totalMemory() / mb;
			long free  = rtInfo.freeMemory() / mb;
			long used  = total - free;
			// log.trace( String.format("Memory(mb) Used: %d ; Free: %d ; Total: %d", used , free, total));
		}
		return connection;
	}
	
	private RCQHandler rcqHandler = null;

	private static final Log log = LogFactory.getLog(RCQReader.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);

		if (StringUtils.isEmpty(getUserName())) {
			exceptions.add(new ValidationException("userName-property not set",
					this));
		}
		if (getPassword() == null) {
			exceptions.add(new ValidationException("password-property not set",
					this));
		}
		try {
			rcqHandler = new RCQHandler();
		} catch (Exception e) {
			log.error("Could not initialize RCQHandler");
			exceptions.add(new ValidationException(
					"Could not initialize RCQHandler", this));
		}
		log.info("Started RCQ Reader " + RCQPluginVersion.current());
	}
	private void populateSrcAndDestForAttachment(Document syncInfo,
			GenericArtifact ga) {
		
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

	public void setIgnoreConnectorUserUpdates(boolean ignoreConnectorUserUpdates) {
		this.ignoreConnectorUserUpdates = ignoreConnectorUserUpdates;
	}
}
