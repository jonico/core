package com.collabnet.ccf.swp;

import java.rmi.RemoteException;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.collabnet.ccf.swp.SWPMetaData.PBIFields;
import com.collabnet.ccf.swp.SWPMetaData.SWPType;
import com.collabnet.ccf.swp.SWPMetaData.TaskFields;
import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.ScrumWorksException;
import com.danube.scrumworks.api2.client.Task;

/**
 * SWP Writer component
 * 
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

	private SWPHandler swpHandler;

	@Override
	public Document createArtifact(Document gaDocument) {
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper
					.createGenericArtifactJavaObject(gaDocument);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			XPathUtils.addAttribute(gaDocument.getRootElement(),
					GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}

		// find out what to create
		String targetRepositoryId = ga.getTargetRepositoryId();
		SWPType swpType = SWPMetaData
				.retrieveSWPTypeFromRepositoryId(targetRepositoryId);
		String swpProductName = SWPMetaData
				.retrieveProductFromRepositoryId(targetRepositoryId);
		if (swpType.equals(SWPMetaData.SWPType.UNKNOWN)
				|| swpProductName == null) {
			String cause = "Invalid repository format: " + targetRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		Connection connection = connect(ga);
		try {
			if (swpType.equals(SWPType.TASK)) {
				Task result = createTask(ga, swpProductName, connection);
				if (result != null) {
					log.info("Created task " + result.getId() + " of PBI "
							+ result.getBacklogItemId() + " with data from "
							+ ga.getSourceArtifactId());
				}
			} else if (swpType.equals(SWPType.PBI)) {
				BacklogItem result = createPBI(ga, swpProductName, connection);
				if (result != null) {
					log.info("Created PBI " + result.getKey()
							+ " with data from " + ga.getSourceArtifactId());
				}
			} else {
				String cause = "Unsupported repository format: "
						+ targetRepositoryId;
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} catch (Exception e) {
			String cause = "During the artifact update process in SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}

		return returnDocument(ga);
	}

	/**
	 * Creates a new SWP PBI
	 * 
	 * @param ga
	 * @param swpProductName 
	 * @param swpHandler
	 * @return newly created PBI
	 * @throws RemoteException
	 * @throws ScrumWorksException
	 */
	private BacklogItem createPBI(GenericArtifact ga, String swpProductName,
			Connection connection) throws RemoteException, ScrumWorksException {
		// TODO should we allow to set the active property?
		GenericArtifactField active = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.active.getFieldName(), ga);
		GenericArtifactField benefit = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.benefit.getFieldName(), ga);
		GenericArtifactField completedDate = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.completedDate.getFieldName(), ga);
		GenericArtifactField description = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.description.getFieldName(), ga);
		GenericArtifactField estimate = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.estimate.getFieldName(), ga);
		GenericArtifactField penalty = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.penalty.getFieldName(), ga);
		GenericArtifactField title = GenericArtifactHelper.getMandatoryGAField(
				PBIFields.title.getFieldName(), ga);
		List<GenericArtifactField> themes = ga
				.getAllGenericArtifactFieldsWithSameFieldName(PBIFields.theme
						.getFieldName());
		List<GenericArtifactField> comments = ga
				.getAllGenericArtifactFieldsWithSameFieldName(PBIFields.comment
						.getFieldName());

		return swpHandler.createPBI(connection.getEndpoint(), active, benefit,
				completedDate, description, estimate, penalty, title, themes,
				comments, swpProductName,
				getResyncUserName() == null ? getUserName()
						: getResyncUserName(), ga);
	}

	/**
	 * Creates a new SWP Task
	 * 
	 * @param ga
	 * @param swpProductName
	 * @param swpHandler
	 * @return newly created task
	 * @throws RemoteException
	 * @throws ScrumWorksException
	 */
	private Task createTask(GenericArtifact ga, String swpProductName,
			Connection connection) throws RemoteException, ScrumWorksException {
		GenericArtifactField description = GenericArtifactHelper
				.getMandatoryGAField(TaskFields.description.getFieldName(), ga);
		GenericArtifactField estimatedHours = GenericArtifactHelper
				.getMandatoryGAField(TaskFields.estimatedHours.getFieldName(),
						ga);
		GenericArtifactField originalEstimate = GenericArtifactHelper
				.getMandatoryGAField(
						TaskFields.originalEstimate.getFieldName(), ga);
		GenericArtifactField pointPerson = GenericArtifactHelper
				.getMandatoryGAField(TaskFields.pointPerson.getFieldName(), ga);
		GenericArtifactField status = GenericArtifactHelper
				.getMandatoryGAField(TaskFields.status.getFieldName(), ga);
		// GenericArtifactField taskBoardStatusRank =
		// GenericArtifactHelper.getMandatoryGAField(TaskFields.taskBoardStatusRank.getFieldName(),
		// ga);
		GenericArtifactField title = GenericArtifactHelper.getMandatoryGAField(
				TaskFields.title.getFieldName(), ga);

		List<GenericArtifactField> comments = ga
				.getAllGenericArtifactFieldsWithSameFieldName(TaskFields.comment
						.getFieldName());

		return swpHandler.createTask(connection.getEndpoint(), description,
				estimatedHours, originalEstimate, pointPerson, status, title,
				comments, swpProductName,
				getResyncUserName() == null ? getUserName()
						: getResyncUserName(), ga);
	}

	@Override
	public Document[] createAttachment(Document gaDocument) {
		// TODO Implement me
		log.warn("createAttachment is not implemented...!");
		return null;
	}

	@Override
	public Document createDependency(Document gaDocument) {
		// SWP does not support dependencies
		log.warn("createDependency is not implemented...!");
		return null;
	}

	@Override
	public Document deleteArtifact(Document gaDocument) {
		// TODO Implement me?
		log.warn("deleteArtifact is not implemented...!");
		return null;
	}

	@Override
	public Document[] deleteAttachment(Document gaDocument) {
		// TODO Implement me?
		log.warn("deleteArtifact is not implemented...!");
		return null;
	}

	@Override
	public Document deleteDependency(Document gaDocument) {
		// SWP does not support dependencies
		log.warn("deleteDependency is not implemented...!");
		return null;
	}

	@Override
	public boolean handleException(Throwable cause,
			ConnectionManager<Connection> connectionManager, Document ga) {
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
			return handleException(innerCause, connectionManager, ga);
		} else if (cause instanceof CCFRuntimeException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager, ga);
		}
		return false;
	}

	@Override
	public Document updateArtifact(Document gaDocument) {
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper
					.createGenericArtifactJavaObject(gaDocument);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			XPathUtils.addAttribute(gaDocument.getRootElement(),
					GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}

		// find out what to update
		String targetRepositoryId = ga.getTargetRepositoryId();
		SWPType swpType = SWPMetaData
				.retrieveSWPTypeFromRepositoryId(targetRepositoryId);
		String swpProductName = SWPMetaData
				.retrieveProductFromRepositoryId(targetRepositoryId);
		if (swpType.equals(SWPMetaData.SWPType.UNKNOWN)
				|| swpProductName == null) {
			String cause = "Invalid repository format: " + targetRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		Connection connection = connect(ga);
		try {
			if (swpType.equals(SWPType.TASK)) {
				Task result = updateTask(ga, swpProductName, connection);
				if (result != null) {
					log.info("Updated task " + result.getId() + " of PBI "
							+ result.getBacklogItemId() + " with data from "
							+ ga.getSourceArtifactId());
				}
			} else if (swpType.equals(SWPType.PBI)) {
				BacklogItem result = updatePBI(ga, swpProductName, connection);
				if (result != null) {
					log.info("Updated PBI " + result.getKey()
							+ " with data from " + ga.getSourceArtifactId());
				}
			} else {
				String cause = "Unsupported repository format: "
						+ targetRepositoryId;
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} catch (Exception e) {
			String cause = "During the artifact update process in SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}

		// once, SWP supports global revision numbers,
		// we have to update last modified date and revision number as well
		return returnDocument(ga);
	}

	/**
	 * Updates an SWP PBI using the properties in the passed generic artifact
	 * format
	 * 
	 * @param ga
	 * @param swpProductName
	 * @param connection
	 * @return
	 * @throws RemoteException
	 * @throws NumberFormatException
	 * @throws ScrumWorksException
	 */
	private BacklogItem updatePBI(GenericArtifact ga, String swpProductName,
			Connection connection) throws NumberFormatException,
			RemoteException, ScrumWorksException {
		// TODO should we allow to set the active property?
		GenericArtifactField active = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.active.getFieldName(), ga);
		GenericArtifactField benefit = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.benefit.getFieldName(), ga);
		GenericArtifactField completedDate = GenericArtifactHelper
		// TODO time zone conversion necessary?
				.getMandatoryGAField(PBIFields.completedDate.getFieldName(), ga);
		GenericArtifactField description = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.description.getFieldName(), ga);
		GenericArtifactField estimate = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.estimate.getFieldName(), ga);
		GenericArtifactField penalty = GenericArtifactHelper
				.getMandatoryGAField(PBIFields.penalty.getFieldName(), ga);
		GenericArtifactField title = GenericArtifactHelper.getMandatoryGAField(
				PBIFields.title.getFieldName(), ga);

		List<GenericArtifactField> themes = ga
				.getAllGenericArtifactFieldsWithSameFieldName(PBIFields.theme
						.getFieldName());

		List<GenericArtifactField> comments = ga
				.getAllGenericArtifactFieldsWithSameFieldName(PBIFields.comment
						.getFieldName());

		return swpHandler.updatePBI(connection.getEndpoint(), active, benefit,
				completedDate, description, estimate, penalty, title, themes,
				comments, swpProductName, getUserName(), ga);
	}

	/**
	 * Updates SWP task using properties stored in passed generic artifact
	 * 
	 * @param ga
	 * @param swpProductName
	 * @param connection
	 * @return updated task object
	 * @throws RemoteException
	 * @throws NumberFormatException
	 * @throws ScrumWorksException
	 */
	private Task updateTask(GenericArtifact ga, String swpProductName,
			Connection connection) throws NumberFormatException,
			RemoteException, ScrumWorksException {
		GenericArtifactField description = GenericArtifactHelper
				.getMandatoryGAField(TaskFields.description.getFieldName(), ga);
		GenericArtifactField estimatedHours = GenericArtifactHelper
				.getMandatoryGAField(TaskFields.estimatedHours.getFieldName(),
						ga);
		GenericArtifactField originalEstimate = GenericArtifactHelper
				.getMandatoryGAField(
						TaskFields.originalEstimate.getFieldName(), ga);
		GenericArtifactField pointPerson = GenericArtifactHelper
				.getMandatoryGAField(TaskFields.pointPerson.getFieldName(), ga);
		GenericArtifactField status = GenericArtifactHelper
				.getMandatoryGAField(TaskFields.status.getFieldName(), ga);
		// GenericArtifactField taskBoardStatusRank =
		// GenericArtifactHelper.getMandatoryGAField(TaskFields.taskBoardStatusRank.getFieldName(),
		// ga);
		GenericArtifactField title = GenericArtifactHelper.getMandatoryGAField(
				TaskFields.title.getFieldName(), ga);

		List<GenericArtifactField> comments = ga
				.getAllGenericArtifactFieldsWithSameFieldName(TaskFields.comment
						.getFieldName());

		return swpHandler.updateTask(connection.getEndpoint(), description,
				estimatedHours, originalEstimate, pointPerson, status, title,
				comments, getUserName(), ga);
	}

	/**
	 * Transforms the Java representation of the generic artifact back to the
	 * XML DOM format
	 * 
	 * @param ga
	 * @return
	 */
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

	/**
	 * Connect to SWP
	 * 
	 * @param ga
	 *            generic artifact
	 * @return
	 */
	public Connection connect(GenericArtifact ga) {
		String targetSystemId = ga.getTargetSystemId();
		String targetSystemKind = ga.getTargetSystemKind();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetRepositoryKind = ga.getTargetRepositoryKind();
		Connection connection;
		try {
			if ((!ga.getArtifactAction().equals(
					GenericArtifact.ArtifactActionValue.CREATE))
					|| getResyncUserName() == null) {
				connection = connect(targetSystemId, targetSystemKind,
						targetRepositoryId, targetRepositoryKind, serverUrl,
						getUserName() + SWPConnectionFactory.PARAM_DELIMITER
								+ getPassword(), false);
			} else {
				connection = connect(targetSystemId, targetSystemKind,
						targetRepositoryId, targetRepositoryKind, serverUrl,
						getResyncUserName()
								+ SWPConnectionFactory.PARAM_DELIMITER
								+ getResyncPassword(), true);
			}
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SWP system. Max connections reached for "
					+ serverUrl;
			log.error(cause, e);
			ga
					.setErrorCode(GenericArtifact.ERROR_MAX_CONNECTIONS_REACHED_FOR_POOL);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SWP system "
					+ serverUrl;
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
			throw new CCFRuntimeException(cause, e);
		}
		return connection;
	}

	/**
	 * Connects to SWP
	 * 
	 * @param systemId
	 * @param systemKind
	 * @param repositoryId
	 * @param repositoryKind
	 * @param connectionInfo
	 * @param credentialInfo
	 * @param forceResync
	 *            flag to determine whether to use ordinary connector user r
	 *            resync user for the new connection
	 * @return
	 * @throws MaxConnectionsReachedException
	 * @throws ConnectionException
	 */
	private Connection connect(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo, boolean forceResync)
			throws MaxConnectionsReachedException, ConnectionException {
		Connection connection = null;
		ConnectionManager<Connection> connectionManager = (ConnectionManager<Connection>) getConnectionManager();
		if (forceResync) {
			connection = connectionManager.getConnectionToCreateArtifact(
					systemId, systemKind, repositoryId, repositoryKind,
					connectionInfo, credentialInfo);
		} else {
			connection = connectionManager
					.getConnectionToUpdateOrExtractArtifact(systemId,
							systemKind, repositoryId, repositoryKind,
							connectionInfo, credentialInfo);
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

	@Override
	public Document updateAttachment(Document gaDocument) {
		// TODO Implement this method
		log.warn("updateAttachment is not implemented...!");
		return null;
	}

	@Override
	public Document updateDependency(Document gaDocument) {
		// SWP does not support dependencies
		log.warn("updateDependency is not implemented...!");
		return null;
	}

	/**
	 * Sets password for SWP connector user
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = Obfuscator.deObfuscatePassword(password);
	}

	/**
	 * Returns user name for SWP connector user
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets user name for SWP connector user
	 * 
	 * @return
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Returns password for SWP connector user
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets URL for SWP server
	 * 
	 * @param serverUrl
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * Returns URL for SWP server
	 * 
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Sets the password of the SWP resync user
	 * 
	 * @param resyncPassword
	 */
	public void setResyncPassword(String resyncPassword) {
		this.resyncPassword = Obfuscator.deObfuscatePassword(resyncPassword);
	}

	/**
	 * Gets the password of the SWP resync user
	 * 
	 * @return
	 */
	public String getResyncPassword() {
		return resyncPassword;
	}

	/**
	 * Sets user name of SWP resync user
	 * 
	 * @param resyncUserName
	 */
	public void setResyncUserName(String resyncUserName) {
		this.resyncUserName = resyncUserName;
	}

	/**
	 * Gets the user name of the SWP resync user
	 * 
	 * @return
	 */
	public String getResyncUserName() {
		return resyncUserName;
	}

	@SuppressWarnings("unchecked")
	@Override
	/*
	 * Validate whether all mandatory properties are set correctly
	 */
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

		if (getUserName() == null) {
			log.error("userName-property not set");
			exceptions.add(new ValidationException("userName-property not set",
					this));
		}

		if (getServerUrl() == null) {
			log.error("serverUrl-property not set");
			exceptions.add(new ValidationException(
					"serverUrl-property not set", this));
		}

		try {
			swpHandler = new SWPHandler();
		} catch (DatatypeConfigurationException e) {
			log.error("Could not initialize SWPHandler");
			exceptions.add(new ValidationException(
					"Could not initialize SWPHandler", this));
		}
	}

}
