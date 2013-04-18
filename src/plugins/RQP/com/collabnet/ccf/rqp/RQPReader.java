package com.collabnet.ccf.rqp;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.rqp.enums.RQPType;

public class RQPReader extends AbstractReader<RQPConnection> {

	private String userName;
	private String password;
	private String serverUrl;
	private boolean ignoreConnectorUserUpdates = true;
	private RQPAttachmentHandler attachmentHandler = null;
	private RQPHandler rqpHandler = null;
	private static final Log log = LogFactory.getLog(RQPReader.class);

	public List<GenericArtifact> getArtifactDependencies(Document syncInfo, String artifactId) {
		return new ArrayList<GenericArtifact>();
	}

	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo, GenericArtifact artifactData) {
		return new ArrayList<GenericArtifact>();
	}

	/**
	 * Create a new generic artifact data structure
	 */
	@Override
	public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		String lastSynchedArtifactId = this.getLastSourceArtifactId(syncInfo);

		// find out what to extract
		RQPType rqpType = RQPMetaData.retrieveRQPTypeFromRepositoryId(sourceRepositoryId);

		if (!RQPType.contains(rqpType)) {
			String cause = "Invalid repository format: " + sourceRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		String packageName = RQPMetaData.extractPackageNameFromRepositoryId(sourceRepositoryId);
		packageName = packageName.replaceAll("@",
				Matcher.quoteReplacement(RQPConnectionFactory.CONNECTION_INFO_DELIMITER));
		RQPConnection connection = connect(sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind);

		GenericArtifact genericArtifact = new GenericArtifact();

		try {
			populateSrcAndDest(syncInfo, genericArtifact);
			genericArtifact.setSourceArtifactId(artifactId);
			genericArtifact.setArtifactMode(ArtifactModeValue.COMPLETE);
			genericArtifact.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);

			if (RQPType.contains(rqpType)) {

				String projectName = RQPMetaData.extractProjectNameFromRepositoryId(sourceRepositoryId);
				String rqpItemType = RQPMetaData.extractRQPItemTypeFromRepositoryId(sourceRepositoryId);
				
				if(RQPType.isRequirement(rqpType)){
					rqpHandler.getRequirement(connection, packageName, projectName, rqpItemType, lastModifiedDate,
						lastSynchronizedVersion, lastSynchedArtifactId, artifactId, getUserName(),
						isIgnoreConnectorUserUpdates(), genericArtifact, this.getSourceRepositoryId(syncInfo));
				}else{
					rqpHandler.getPackage(connection, artifactId, genericArtifact, this.getSourceRepositoryId(syncInfo));
				}

			}

		} catch (Exception e) {
			String cause = "During the artifact retrieval process from RQP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return genericArtifact;
	}

	private void populateSrcAndDest(Document syncInfo, GenericArtifact ga) {
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String conflictResolutionPriority = this.getConflictResolutionPriority(syncInfo);

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

	@Override
	public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		String lastSynchedArtifactId = this.getLastSourceArtifactId(syncInfo);

		// find out what to extract
		RQPType rqpType = RQPMetaData.retrieveRQPTypeFromRepositoryId(sourceRepositoryId);

		if (!RQPType.contains(rqpType)) {
			String cause = "Invalid repository format: " + sourceRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		RQPConnection connection = connect(sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind);
		String packageName = RQPMetaData.extractPackageNameFromRepositoryId(sourceRepositoryId);

		ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();
		try {
			if (RQPType.contains(rqpType)) {
				if(RQPType.isRequirement(rqpType)){
					String rqpItemType = RQPMetaData.extractRQPItemTypeFromRepositoryId(sourceRepositoryId);
	
					rqpHandler.getChangedRequirements(connection, rqpItemType, lastModifiedDate, lastSynchronizedVersion,
							lastSynchedArtifactId, artifactStates, getUserName(), isIgnoreConnectorUserUpdates(),
							packageName);
				}else{
					rqpHandler.getChangedPackages(connection, lastModifiedDate, lastSynchronizedVersion,
						lastSynchedArtifactId, artifactStates, getUserName(), isIgnoreConnectorUserUpdates());
				}
			}

		} catch (Exception e) {
			String cause = "During the artifact retrieval process from RQP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return artifactStates;
	}

	public void disconnect(RQPConnection connection) {
		RQPConnectionFactory.releaseConnection(connection);
		getConnectionManager().releaseConnection(connection);
	}

	/**
	 * Connect to RQP.
	 * 
	 * @param systemId
	 *            identifier of the system.
	 * @param systemKind
	 *            kind or prefix of the system.
	 * @param repositoryId
	 *            full path to the project RQP file.
	 * @param repositoryKind
	 * @return
	 */
	public RQPConnection connect(String systemId, String systemKind, String repositoryId, String repositoryKind) {
		String projectName = RQPMetaData.extractProjectNameFromRepositoryId(repositoryId);
		projectName = projectName.replaceAll("@",
				Matcher.quoteReplacement(RQPConnectionFactory.CONNECTION_INFO_DELIMITER));
		String credentialInfo = getUserName() + RQPConnectionFactory.PARAM_DELIMITER + getPassword();
		String connectionInfo = serverUrl + RQPConnectionFactory.CONNECTION_INFO_DELIMITER + projectName
				+ RQPConnectionFactory.PROJECT_EXTENSION;

		RQPConnection connection = null;
		try {
			connection = getConnectionManager().getConnectionToUpdateOrExtractArtifact(systemId, systemKind,
					repositoryId, repositoryKind, connectionInfo, credentialInfo);
			connection.openProject(connectionInfo, this);
		} catch (MaxConnectionsReachedException e) {
			String cause = "The maximum allowed connection configuration for a Connection Pool is reached." + serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the RQP system " + serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		return connection;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		RQPValidator validator = new RQPValidator();
		validator.validate(this, exceptions);
	}

	@Override
	public boolean handleException(Throwable cause, ConnectionManager<RQPConnection> connectionManager) {

		if (cause == null)
			return false;
		if ((cause instanceof java.net.SocketException || cause instanceof java.net.UnknownHostException)
				&& connectionManager.isEnableRetryAfterNetworkTimeout()) {
			return true;
		} else if (cause instanceof ConnectionException && connectionManager.isEnableRetryAfterNetworkTimeout()) {
			return true;
		} else if (cause instanceof RemoteException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		} else if (cause instanceof CCFRuntimeException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		} else if (cause instanceof RuntimeException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		} else if (cause instanceof SQLException) {
			if (cause.getMessage().contains("Unexpected token UNIQUE, requires COLLATION in statement")) {
				return true;
			}
		}
		return false;
	}

	public void setIgnoreConnectorUserUpdates(boolean ignoreConnectorUserUpdates) {
		this.ignoreConnectorUserUpdates = ignoreConnectorUserUpdates;
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

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public RQPAttachmentHandler getAttachmentHandler() {
		return attachmentHandler;
	}

	public void setAttachmentHandler(RQPAttachmentHandler attachmentHandler) {
		this.attachmentHandler = attachmentHandler;
	}

	public void setRQPHandler(RQPHandler rqpHandler) {
		this.rqpHandler = rqpHandler;
	}

}