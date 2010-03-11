package com.collabnet.ccf.swp;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ServerException;
import com.danube.scrumworks.api.client.types.TaskWSO;
/**
 * SWP Writer component
 * @author jnicolai
 *
 */
public class SWPWriter extends AbstractWriter<Connection> implements
		IDataProcessor {

	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(SWPWriter.class);
	
	private String userName;
	private String password;
	private String serverUrl;
	private String resyncUserName;
	private String resyncPassword;

	@Override
	public Document createArtifact(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document[] createAttachment(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document createDependency(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document deleteArtifact(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document[] deleteAttachment(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document deleteDependency(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document updateArtifact(Document gaDocument) {
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(gaDocument);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			XPathUtils.addAttribute(gaDocument.getRootElement(),
					GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		
		final Connection connection = getConnection(ga);
		
		final String targetRepositoryId = ga.getTargetRepositoryId();
		try {
			ScrumWorksEndpoint endpoint = connection.getEndpoint();
			if (targetRepositoryId.endsWith("-Tasks")) {
				GenericArtifactField taskTitle = ga.getAllGenericArtifactFieldsWithSameFieldName("title").get(0);
				GenericArtifactField taskDescription = ga.getAllGenericArtifactFieldsWithSameFieldName("description").get(0);
				Long taskId = Long.valueOf(ga.getTargetArtifactId());
				TaskWSO task = endpoint.getTaskById(taskId);
				
				task.setTitle(taskTitle.getFieldValue().toString());
				task.setDescription(taskDescription.getFieldValue().toString());
				Long pbiId = task.getBacklogItemId();
				BacklogItemWSO pbi = endpoint.getBacklogItem(pbiId);
				String targetPbiKey = ga.getDepParentTargetArtifactId();
				if (!targetPbiKey.equals(pbi.getKey())) {
					BacklogItemWSO targetPbi = endpoint.getBacklogItemByKey(targetPbiKey);
					endpoint.moveTaskIntoBacklogItem(task, targetPbi);
					task.setBacklogItemId(targetPbi.getBacklogItemId());
				}
				endpoint.updateTask(task);
			} else {
				GenericArtifactField pbiTitle = ga.getAllGenericArtifactFieldsWithSameFieldName("title").get(0);
				GenericArtifactField pbiDescription = ga.getAllGenericArtifactFieldsWithSameFieldName("description").get(0);
				String pbiKey = ga.getTargetArtifactId();
				BacklogItemWSO pbi = endpoint.getBacklogItemByKey(pbiKey);
				
				pbi.setTitle(pbiTitle.getFieldValue().toString());
				pbi.setDescription(pbiDescription.getFieldValue().toString());
				endpoint.updateBacklogItem(pbi);
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
		
//		return returnDocument(ga);
		return gaDocument;
	}
	
	public Document returnDocument(GenericArtifact ga) {
		Document document = null;
		try {
			document = GenericArtifactHelper
					.createGenericArtifactXMLDocument(ga);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		return document;
	}
	
	private Connection getConnection(final GenericArtifact artifact) {
		String targetSystemId = artifact.getTargetSystemId();
		String targetSystemKind = artifact.getTargetSystemKind();
		String targetRepositoryId = artifact.getTargetRepositoryId();
		String targetRepositoryKind = artifact.getTargetRepositoryKind();
		
		Connection connection;
		try {
			connection = connect(targetSystemId, targetSystemKind,
					targetRepositoryId, targetRepositoryKind, serverUrl,
					userName + SWPConnectionFactory.PARAM_DELIMITER
							+ password);
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
		return connection;
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
		return getConnectionManager()
				.getConnectionToUpdateOrExtractArtifact(systemId, systemKind,
						repositoryId, repositoryKind, connectionInfo,
						credentialInfo);
	}
	
	@Override
	public Document updateAttachment(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document updateDependency(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	public void setResyncUserName(String resyncUserName) {
		this.resyncUserName = resyncUserName;
	}
	
	public void setResyncPassword(String resyncPassword) {
		this.resyncPassword = resyncPassword;
	}

}
