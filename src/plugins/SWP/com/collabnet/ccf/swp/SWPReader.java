package com.collabnet.ccf.swp;

import java.rmi.RemoteException;
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
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.swp.SWPMetaData.SWPType;
import com.danube.scrumworks.api.client.types.ServerException;

/**
 * SWP Reader component
 * 
 * @author jnicolai
 * 
 */
public class SWPReader extends AbstractReader<Connection> {
	private String username;
	private String password;
	private String serverUrl;
	private String resyncUserName;

	private static final Log log = LogFactory.getLog(SWPReader.class);

	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			GenericArtifact artifactData) {
		// TODO return attachments
		return new ArrayList<GenericArtifact>();
	}

	@Override
	public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		
		// find out what to extract
		SWPType swpType = SWPMetaData.retrieveSWPTypeFromRepositoryId(sourceRepositoryId);
		String swpProductName = SWPMetaData.retrieveProductFromRepositoryId(sourceRepositoryId);
		if (swpType.equals(SWPMetaData.SWPType.UNKNOWN) || swpProductName == null) {
			String cause = "Invalid repository format: " + sourceRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}
		
		GenericArtifact genericArtifact = new GenericArtifact();
		Connection connection;
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, serverUrl,
					getUsername() + SWPConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SWP system. Max connections reached for "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SWP system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		
		try {
			SWPHandler swpHandler = new SWPHandler(connection);
			/**
			 * Create a new generic artifact data structure
			 */
			genericArtifact.setSourceArtifactVersion("-1");
			genericArtifact.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df.format(new Date(0)));
			genericArtifact.setArtifactMode(ArtifactModeValue.COMPLETE);
			genericArtifact.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);
			genericArtifact.setSourceArtifactId(artifactId);
			
			if (swpType.equals(SWPMetaData.SWPType.TASK)) {
				swpHandler.retrieveTask(artifactId, swpProductName, genericArtifact);
			} else if (swpType.equals(SWPMetaData.SWPType.PBI)) {
				swpHandler.retrievePBI(artifactId, swpProductName, genericArtifact);
			} else if (swpType.equals(SWPMetaData.SWPType.PRODUCT)) {
				swpHandler.retrieveProduct(artifactId, swpProductName, genericArtifact);
			} else if (swpType.equals(SWPMetaData.SWPType.RELEASE)) {
				swpHandler.retrieveProductRelease(artifactId, swpProductName, genericArtifact);
			} else {
				String cause = "Unsupported repository format: " + sourceRepositoryId;
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} catch (ServerException e) {
			String cause = "During the artifact retrieval process from SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (RemoteException e) {
			String cause = "During the artifact retrieval process from SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}

		populateSrcAndDest(syncInfo, genericArtifact);
		return genericArtifact;
	}

	/**
	 * Releases the connection to the ConnectionManager.
	 * 
	 * @param connection
	 *            - The connection to be released to the ConnectionManager
	 */
	public void disconnect(Connection connection) {
		getConnectionManager().releaseConnection(connection);
	}

	/**
	 * Populates the source and destination attributes for this GenericArtifact
	 * object from the Sync Info database document.
	 * 
	 * @param syncInfo
	 * @param ga
	 */
	private void populateSrcAndDest(Document syncInfo, GenericArtifact ga) {
		String sourceArtifactId = ga.getSourceArtifactId();
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String conflictResolutionPriority = this
				.getConflictResolutionPriority(syncInfo);

		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
		String targetRepositoryKind = this.getTargetRepositoryKind(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetSystemKind = this.getTargetSystemKind(syncInfo);
		
		ga.setSourceArtifactId(sourceArtifactId);
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

	@Override
	public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId) {
		// SWP does not support dependencies at the moment
		return new ArrayList<GenericArtifact>();
	}

	@Override
	public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		
		// find out what to extract
		SWPType swpType = SWPMetaData.retrieveSWPTypeFromRepositoryId(sourceRepositoryId);
		String swpProductName = SWPMetaData.retrieveProductFromRepositoryId(sourceRepositoryId);
		if (swpType.equals(SWPMetaData.SWPType.UNKNOWN) || swpProductName == null) {
			String cause = "Invalid repository format: " + sourceRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}
		
		Connection connection;
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, serverUrl,
					getUsername() + SWPConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SWP system. Max connections reached for "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SWP system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		SWPHandler swpHandler = new SWPHandler(connection);

		ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();
		try {
			if (swpType.equals(SWPType.TASK)) {
				swpHandler.getChangedTasks(swpProductName, artifactStates);
			} else if (swpType.equals(SWPType.PBI)) {
				swpHandler.getChangedPBIs(swpProductName, artifactStates);
			} else if (swpType.equals(SWPType.PRODUCT)) {
				swpHandler.getChangedProducts(swpProductName, artifactStates);
			} else if (swpType.equals(SWPType.RELEASE)) {
				swpHandler.getChangedProductReleases(swpProductName, artifactStates);
			} else {
				String cause = "Unsupported repository format: " + sourceRepositoryId;
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} catch (ServerException e) {
			String cause = "During the artifact retrieval process from SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (RemoteException e) {
			String cause = "During the artifact retrieval process from SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return artifactStates;
	}


	/**
	 * Sets user name for SWP connector user
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.username = userName;
	}

	/**
	 * Sets password for SWP connector user
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns user name for SWP connector user
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns password for SWP connector user
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets URL for SWP server
	 * @param serverUrl
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * Returns URL for SWP server
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Sets user name of SWP resync user
	 * @param resyncUserName
	 */
	public void setResyncUserName(String resyncUserName) {
		this.resyncUserName = resyncUserName;
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
		} else if (cause instanceof RemoteException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		} else if (cause instanceof CCFRuntimeException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		}
		return false;
	}
	
	/**
	 * Gets the user name of the SWP resync user
	 * @return
	 */
	public String getResyncUserName() {
		return resyncUserName;
	}

	/**
	 * Connects to the source SWP system using the connectionInfo and
	 * credentialInfo details.
	 * 
	 * This method uses the ConnectionManager configured in the wiring file for
	 * the SWPReader
	 * 
	 * @param systemId
	 *            - The system id of the source SWP system
	 * @param systemKind
	 *            - The system kind of the source SWP system
	 * @param repositoryId
	 *            - The tracker id in the source SWP system
	 * @param repositoryKind
	 *            - The repository kind for the tracker
	 * @param connectionInfo
	 *            - The SWP server URL
	 * @param credentialInfo
	 *            - User name and password concatenated with a delimiter.
	 * @return - The connection object obtained from the ConnectionManager
	 * @throws MaxConnectionsReachedException
	 * @throws ConnectionException
	 */
	public Connection connect(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException,
			ConnectionException {
		// log.info("Before calling the parent connect()");
		Connection connection = null;
		connection = getConnectionManager()
				.getConnectionToUpdateOrExtractArtifact(systemId, systemKind,
						repositoryId, repositoryKind, connectionInfo,
						credentialInfo);
		return connection;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);

		if (getResyncUserName() == null) {
			log
					.warn("resyncUserName-property has not been set, so that initial resyncs after artifact creation are not possible.");
		}

		if (StringUtils.isEmpty(getServerUrl())) {
			exceptions.add(new ValidationException(
					"serverUrl-property not set", this));
		}
		if (StringUtils.isEmpty(getUsername())) {
			exceptions.add(new ValidationException("username-property not set",
					this));
		}
		if (getPassword() == null) {
			exceptions.add(new ValidationException("password-property not set",
					this));
		}
	}
}
