package com.collabnet.ccf.swp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;
import com.danube.scrumworks.api.client.types.ServerException;
import com.danube.scrumworks.api.client.types.TaskWSO;

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

	private static final Log log = LogFactory.getLog(SWPReader.class);

	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			GenericArtifact artifactData) {
		// TODO Auto-generated method stub
		return new ArrayList<GenericArtifact>();
	}

	@Override
	public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
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
		GenericArtifact genericArtifact = new GenericArtifact();
		genericArtifact.setSourceArtifactVersion("-1");
		genericArtifact.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df.format(new Date(0)));
		genericArtifact.setArtifactMode(ArtifactModeValue.COMPLETE);
		genericArtifact.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);
		genericArtifact.setSourceArtifactId(artifactId);

		try {
			ScrumWorksEndpoint endpoint = connection.getEndpoint();
			if (sourceRepositoryId.endsWith("-Tasks")) {
				final String productName = getProductNameFromTaskString(sourceRepositoryId);
				final TaskWSO task = endpoint.getTaskById(Long.valueOf(artifactId));
				final BacklogItemWSO backlogItem = endpoint.getBacklogItem(task.getBacklogItemId());
				
				GenericArtifactField descriptionField = genericArtifact
					.addNewField("description", "mandatoryField");
				descriptionField.setFieldValueType(FieldValueTypeValue.STRING);
				descriptionField.setFieldAction(FieldActionValue.REPLACE);
				descriptionField.setFieldValue(task.getDescription());
				GenericArtifactField titleField = genericArtifact.addNewField(
						"title", "mandatoryField");
				titleField.setFieldValueType(FieldValueTypeValue.STRING);
				titleField.setFieldValue(task.getTitle());
				titleField.setFieldAction(FieldActionValue.REPLACE);
				GenericArtifactField estimateField = genericArtifact.addNewField(
						"estimate", "mandatoryField");
				estimateField.setFieldValueType(FieldValueTypeValue.INTEGER);
				estimateField.setFieldValue(task.getEstimatedHours());
				estimateField.setFieldAction(FieldActionValue.REPLACE);
				
				genericArtifact.setDepParentSourceArtifactId(backlogItem.getKey());
				genericArtifact.setDepParentSourceRepositoryId(productName);
			} else {
				BacklogItemWSO pbi = endpoint.getBacklogItemByKey(
						artifactId);
				GenericArtifactField descriptionField = genericArtifact
						.addNewField("description", "mandatoryField");
				descriptionField.setFieldValueType(FieldValueTypeValue.STRING);
				descriptionField.setFieldAction(FieldActionValue.REPLACE);
				descriptionField.setFieldValue(pbi.getDescription());
				GenericArtifactField titleField = genericArtifact.addNewField(
						"title", "mandatoryField");
				titleField.setFieldValueType(FieldValueTypeValue.STRING);
				titleField.setFieldValue(pbi.getTitle());
				titleField.setFieldAction(FieldActionValue.REPLACE);
			}
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			String cause = "During the artifact retrieval process from SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
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

		if (StringUtils.isEmpty(sourceArtifactId)) {
			List<GenericArtifactField> fields = ga
					.getAllGenericArtifactFieldsWithSameFieldName("Id");
			for (GenericArtifactField field : fields) {
				sourceArtifactId = field.getFieldValue().toString();
			}
		}
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
		// TODO Auto-generated method stub
		return new ArrayList<GenericArtifact>();
	}

	@Override
	public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
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

		ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();
		try {
			ScrumWorksEndpoint endpoint = connection.getEndpoint();
			if (sourceRepositoryId.endsWith("-Tasks")) {
				String productName = getProductNameFromTaskString(sourceRepositoryId);
				ProductWSO product = endpoint.getProductByName(productName);
				BacklogItemWSO[] pbis = endpoint.getActiveBacklogItems(product);
				for (BacklogItemWSO pbi : pbis) {
					TaskWSO[] tasks = endpoint.getTasks(pbi);
					if (tasks != null) {
						for (TaskWSO task : tasks) {
							ArtifactState artifactState = new ArtifactState();
							artifactState.setArtifactId(task.getId().toString());
							artifactState.setArtifactLastModifiedDate(new Date(0));
							artifactState.setArtifactVersion(-1);
							artifactStates.add(artifactState);
						}
					}
				}
			} else {
				ProductWSO product = endpoint.getProductByName(sourceRepositoryId);
				BacklogItemWSO[] pbis = endpoint.getActiveBacklogItems(product);
				for (BacklogItemWSO pbi : pbis) {
					ArtifactState artifactState = new ArtifactState();
					artifactState.setArtifactId(pbi.getKey());
					artifactState.setArtifactLastModifiedDate(new Date(0));
					artifactState.setArtifactVersion(-1);
					artifactStates.add(artifactState);
				}
			}
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			String cause = "During the artifact retrieval process from SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			String cause = "During the artifact retrieval process from SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return artifactStates;
	}

	private String getProductNameFromTaskString(String sourceRepositoryId) {
		String productName = sourceRepositoryId.substring(0, sourceRepositoryId.length() - "-Tasks".length());
		return productName;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	public void setPassword(String password) {
		// TODO implement me
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setServerUrl(String serverUrl) {
		// TODO implement me
		this.serverUrl = serverUrl;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setResyncUserName(String resyncUserName) {
		// TODO implement me
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
}
