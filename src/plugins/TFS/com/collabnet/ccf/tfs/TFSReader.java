package com.collabnet.ccf.tfs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.tfs.TFSMetaData.TFSType;

public class TFSReader extends AbstractReader<TFSConnection> {

	private String userName;
	private String password;
	private String serverUrl;
	private boolean ignoreConnectorUserUpdates;

	public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId) {

		return new ArrayList<GenericArtifact>();
	}

	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			GenericArtifact artifactData) {

		return new ArrayList<GenericArtifact>();
	}

	@Override
	public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);

		return null;
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

		// System.out.println("sourceSystemId: " + sourceSystemId);
		// System.out.println("sourceSystemKind: " + sourceSystemKind);
		// System.out.println("sourceRepositoryId: " + sourceRepositoryId);
		// System.out.println("sourceRepositoryKind: " + sourceRepositoryKind);
		// System.out.println("lastSynchronizedVersion: " +
		// lastSynchronizedVersion);
		// System.out.println("targetSystemId: " + targetSystemId);
		// System.out.println("targetRepositoryId: " + targetRepositoryId);

		// find out what to extract
		TFSType tfsType = TFSMetaData
				.retrieveTFSTypeFromRepositoryId(sourceRepositoryId);

		if (tfsType.equals(TFSMetaData.TFSType.UNKNOWN)) {
			String cause = "Invalid repository format: " + sourceRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		TFSConnection connection;
		String collectionName = TFSMetaData.extractCollectionNameFromRepositoryId(sourceRepositoryId);
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, serverUrl + "/" + collectionName,
					getUserName() + TFSConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the TFS system. Max connections reached for "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the TFS system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}

		ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();
		try {
			if (tfsType.equals(TFSType.WORKITEM)) {
				String projectName = TFSMetaData.extractProjectNameFromRepositoryId(sourceRepositoryId);
				String workItemType = TFSMetaData.extractWorkItemTypeFromRepositoryId(sourceRepositoryId);
				tfsHandler.getChangedWorkItems(connection,
						collectionName, projectName, workItemType, lastModifiedDate, lastSynchronizedVersion, lastSynchedArtifactId, artifactStates, getUserName(),
						isIgnoreConnectorUserUpdates());
			}

		} catch (Exception e) {
			String cause = "During the artifact retrieval process from TFS, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return artifactStates;
	}
	
	public void disconnect(TFSConnection connection) {
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

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	public TFSConnection connect(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException,
			ConnectionException {
		// log.info("Before calling the parent connect()");
		TFSConnection connection = null;
		connection = getConnectionManager()
				.getConnectionToUpdateOrExtractArtifact(systemId, systemKind,
						repositoryId, repositoryKind, connectionInfo,
						credentialInfo);
		return connection;
	}
	
	private TFSHandler tfsHandler = null;

	private static final Log log = LogFactory.getLog(TFSReader.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);


		if (StringUtils.isEmpty(getServerUrl())) {
			exceptions.add(new ValidationException(
					"serverUrl-property not set", this));
		}
		if (StringUtils.isEmpty(getUserName())) {
			exceptions.add(new ValidationException("userName-property not set",
					this));
		}
		if (getPassword() == null) {
			exceptions.add(new ValidationException("password-property not set",
					this));
		}
		try {
			tfsHandler = new TFSHandler();
		} catch (Exception e) {
			log.error("Could not initialize TFSHandler");
			exceptions.add(new ValidationException(
					"Could not initialize TFSHandler", this));
		}
	}

	public void setIgnoreConnectorUserUpdates(boolean ignoreConnectorUserUpdates) {
		this.ignoreConnectorUserUpdates = ignoreConnectorUserUpdates;
	}
}
