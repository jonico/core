package com.collabnet.ccf.rqp;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.collabnet.ccf.rqp.enums.RQPType;
import com.microsoft.tfs.core.exceptions.TECoreException;
import com.rational.reqpro.rpx._Requirement;
import com.rational.reqpro.rpx.enumRequirementFlags;
import com.rational.reqpro.rpx.enumRequirementLookups;
import com.rational.reqpro.rpx.enumRequirementsWeights;

public class RQPWriter extends AbstractWriter<RQPConnection> {

	private static final Log log = LogFactory.getLog(RQPWriter.class);

	private String userName;
	private String password;
	private String serverUrl;
	private boolean preserveSemanticallyUnchangedHTMLFieldValues;
	private RQPAttachmentHandler attachmentHandler = null;
	private RQPHandler rqpHandler;

	@Override
	public Document updateDependency(Document gaDocument) {
		return null;
	}

	@Override
	public Document updateAttachment(Document gaDocument) {
		log.warn("updateAttachment is not implemented...!");
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
			XPathUtils.addAttribute(gaDocument.getRootElement(), GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}

		// find out what to update
		String targetRepositoryId = ga.getTargetRepositoryId();
		RQPType rqpType = RQPMetaData.retrieveRQPTypeFromRepositoryId(targetRepositoryId);

		if (!RQPType.contains(rqpType)) {
			String cause = "Invalid repository format: " + targetRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		String packageName = RQPMetaData.extractPackageNameFromRepositoryId(targetRepositoryId);
		packageName = packageName.replaceAll("@",
				Matcher.quoteReplacement(RQPConnectionFactory.CONNECTION_INFO_DELIMITER));
		String projectName = RQPMetaData.extractProjectNameFromRepositoryId(targetRepositoryId);

		RQPConnection connection = connect(ga);

		try {

			if (RQPType.USER_STORY.equals(rqpType)) {

				String rqpItemType = RQPMetaData.extractRQPItemTypeFromRepositoryId(targetRepositoryId);

				_Requirement result = updateRequirement(ga, packageName, projectName, rqpItemType, connection);
				if (result != null) {
					log.info("Updated work item with data from " + ga.getSourceArtifactId());
				}
			} else {
				String cause = "Unsupported repository format: " + targetRepositoryId;
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} catch (Exception e) {
			String cause = "During the artifact update process in TFS, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return returnDocument(ga);
	}

	private _Requirement updateRequirement(GenericArtifact ga, String packageName, String projectName,
			String rqpItemType, RQPConnection connection) {
		return rqpHandler.updateRequirement(ga, packageName, projectName, rqpItemType, connection, this.getUserName());
	}

	@Override
	public Document createArtifact(Document gaDocument) {
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(gaDocument);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			XPathUtils.addAttribute(gaDocument.getRootElement(), GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}

		// find out what to create
		String targetRepositoryId = ga.getTargetRepositoryId();
		RQPType rqpType = RQPMetaData.retrieveRQPTypeFromRepositoryId(targetRepositoryId);

		if (!RQPType.contains(rqpType)) {
			String cause = "Invalid repository format: " + targetRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		String packageName = RQPMetaData.extractPackageNameFromRepositoryId(targetRepositoryId);
		packageName = packageName.replaceAll("@",
				Matcher.quoteReplacement(RQPConnectionFactory.CONNECTION_INFO_DELIMITER));
		String projectName = RQPMetaData.extractProjectNameFromRepositoryId(targetRepositoryId);

		RQPConnection connection = connect(ga);

		try {
			if (RQPType.USER_STORY.equals(rqpType)) {

				String rqpItemType = RQPMetaData.extractRQPItemTypeFromRepositoryId(targetRepositoryId);

				_Requirement result = createRequirement(ga, packageName, projectName, rqpItemType, connection, rqpType);
				if (result != null) {
					log.info("Created new requirement item with data from " + ga.getSourceArtifactId());
				}
			} else {
				String cause = "Unsupported repository format: " + targetRepositoryId;
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} catch (Exception e) {
			String cause = "During the artifact update process in RQP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return returnDocument(ga);
	}

	private _Requirement createRequirement(GenericArtifact ga, String packageName, String projectName,
			String rqpItemType, RQPConnection connection, RQPType rqpType) {
		return rqpHandler.createRequirement(ga, packageName, projectName, rqpItemType, connection, this.getUserName(),
				rqpType);
	}

	public void disconnect(RQPConnection connection) {
		getConnectionManager().releaseConnection(connection);
		connection.disconnect();
	}

	public Document returnDocument(GenericArtifact ga) {
		Document document = null;
		try {
			document = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		return document;
	}

	@Override
	public Document[] createAttachment(Document gaDocument) {
		GenericArtifact ga = null;
		try {
			ga = GenericArtifactHelper.createGenericArtifactJavaObject(gaDocument);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}

		String targetRepositoryId = ga.getTargetRepositoryId();

		RQPType rqpType = RQPMetaData.retrieveRQPTypeFromRepositoryId(targetRepositoryId);

		if (!RQPType.contains(rqpType)) {
			String cause = "Invalid repository format: " + targetRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		RQPConnection connection = connect(ga);

		String targetParentArtifactId = ga.getDepParentTargetArtifactId();

		GenericArtifact parentArtifact = null;
		try {

			attachmentHandler.handleAttachment(connection, ga, targetParentArtifactId, this.getUserName());

			_Requirement req = connection.getProjectConnection().GetRequirement(targetParentArtifactId,
					enumRequirementLookups.eReqLookup_Key, enumRequirementsWeights.eReqWeight_Heavy,
					enumRequirementFlags.eReqFlag_Refresh);

			parentArtifact = new GenericArtifact();
			// make sure that we do not update the synchronization status record
			// for replayed attachments
			parentArtifact.setTransactionId(ga.getTransactionId());
			parentArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			parentArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
			parentArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
			parentArtifact.setConflictResolutionPriority(ga.getConflictResolutionPriority());
			parentArtifact.setSourceArtifactId(ga.getDepParentSourceArtifactId());
			parentArtifact.setSourceArtifactLastModifiedDate(ga.getSourceArtifactLastModifiedDate());
			parentArtifact.setSourceArtifactVersion(ga.getSourceArtifactVersion());
			parentArtifact.setSourceRepositoryId(ga.getSourceRepositoryId());
			parentArtifact.setSourceSystemId(ga.getSourceSystemId());
			parentArtifact.setSourceSystemKind(ga.getSourceSystemKind());
			parentArtifact.setSourceRepositoryKind(ga.getSourceRepositoryKind());
			parentArtifact.setSourceSystemTimezone(ga.getSourceSystemTimezone());

			parentArtifact.setTargetArtifactId(targetParentArtifactId);

			parentArtifact.setTargetArtifactLastModifiedDate(DateUtil.format((new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss")).parse(req.getVersionDateTime())));
			parentArtifact.setTargetArtifactVersion(String.valueOf(RQPMetaData.processRQPVersionNumber(req.getVersionNumber())));

			parentArtifact.setTargetRepositoryId(ga.getTargetRepositoryId());
			parentArtifact.setTargetRepositoryKind(ga.getTargetRepositoryKind());
			parentArtifact.setTargetSystemId(ga.getTargetSystemId());
			parentArtifact.setTargetSystemKind(ga.getTargetSystemKind());
			parentArtifact.setTargetSystemTimezone(ga.getTargetSystemTimezone());

		} catch (ParseException e) {
			String cause = "Problem occured while parsing the revision date attachments in RQP";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			throw new CCFRuntimeException(cause, e);
		} catch (RemoteException e) {
			String cause = "Problem occured while creating attachments in RQP";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			throw new CCFRuntimeException(cause, e);
		} catch (IOException e) {
			String cause = "Problem occured while retrieving the RQP requirement in RQP";
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}

		Document parentArtifactDoc = returnDocument(parentArtifact);
		Document attachmentDocument = this.returnDocument(ga);
		Document[] retDocs = new Document[] { attachmentDocument, parentArtifactDoc };

		return retDocs;

	}

	@Override
	public Document createDependency(Document gaDocument) {
		log.warn("createDependency is not implemented...!");
		return null;
	}

	@Override
	public Document deleteArtifact(Document gaDocument) {
		log.warn("deleteArtifact is not implemented...!");
		return null;
	}

	@Override
	public Document[] deleteAttachment(Document gaDocument) {
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
	public boolean handleException(Throwable cause, ConnectionManager<RQPConnection> connectionManager, Document ga) {
		if (cause == null)
			return false;
		if ((cause instanceof java.net.SocketException || cause instanceof java.net.UnknownHostException)
				&& connectionManager.isEnableRetryAfterNetworkTimeout()) {
			return true;
		} else if (cause instanceof ConnectionException && connectionManager.isEnableRetryAfterNetworkTimeout()) {
			return true;
		} else if (cause instanceof RemoteException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager, ga);
		} else if (cause instanceof CCFRuntimeException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager, ga);
		} else if (cause instanceof TECoreException) {
			if (cause.getMessage().contains("Unknown host")) {
				return true;
			}
		} else if (cause instanceof RuntimeException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager, ga);
		} else if (cause instanceof SQLException) {
			if (cause.getMessage().contains("Unexpected token UNIQUE, requires COLLATION in statement")) {
				return true;
			}
		}
		return false;
	}

	public RQPConnection connect(GenericArtifact ga) {
		String systemId = ga.getTargetSystemId();
		String systemKind = ga.getTargetSystemKind();
		String repositoryId = ga.getTargetRepositoryId();
		String repositoryKind = ga.getTargetRepositoryKind();

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
			String cause = "Could not create connection to the RQP system. Max connections reached for " + serverUrl;
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_MAX_CONNECTIONS_REACHED_FOR_POOL);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the RQP system " + serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		return connection;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void validate(List exceptions) {
		super.validate(exceptions);
		RQPValidator validator = new RQPValidator();
		validator.validate(this, exceptions);
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

	public boolean isPreserveSemanticallyUnchangedHTMLFieldValues() {
		return preserveSemanticallyUnchangedHTMLFieldValues;
	}

	public void setPreserveSemanticallyUnchangedHTMLFieldValues(boolean preserveSemanticallyUnchangedHTMLFieldValues) {
		this.preserveSemanticallyUnchangedHTMLFieldValues = preserveSemanticallyUnchangedHTMLFieldValues;
	}
}
