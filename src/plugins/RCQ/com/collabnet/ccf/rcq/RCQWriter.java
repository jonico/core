package com.collabnet.ccf.rcq;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.hospital.CCFExceptionToOrderedMapConvertor;
import com.collabnet.ccf.core.utils.XPathUtils;

import com.rational.clearquest.cqjni.CQEntity;
import com.rational.clearquest.cqjni.CQException;

public class RCQWriter extends AbstractWriter<RCQConnection> {

	private static final Log log = LogFactory.getLog(RCQWriter.class);

	private String userName;
	private String password;
	private String serverUrl;
	private boolean preserveSemanticallyUnchangedHTMLFieldValues;

	private RCQHandler rcqHandler;
	
	@Override
	public Document updateDependency(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document updateAttachment(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
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

		RCQConnection connection = connect(ga);
		try {
			CQEntity result = rcqHandler.updateRecord(ga, connection);
			if (result != null) {
				log.info("Updated record " + result.GetDisplayName() + " with data from "
						+ ga.getSourceArtifactId());
			}
		} catch (Exception e) {
			String cause = "During the artifact update process in RCQ, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}

		return returnDocument(ga);	
	}

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

	
		RCQConnection connection;
		connection = connect(ga);
		try {
			CQEntity result = rcqHandler.createRecord(ga, connection);
			if (result != null) {
				log.info("Created record item " + result.GetDisplayName() + " with data from "
						+ ga.getSourceArtifactId());
			}
		} catch (Exception e) {
			String cause = "During the artifact creation process in RCQ, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}

		return returnDocument(ga);		
	}

	public void disconnect(RCQConnection connection) {
		getConnectionManager().releaseConnection(connection);
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

	
		RCQConnection connection = connect(ga);

		CQEntity record = null;
		String currentAction = null;
		try {
			currentAction = "retrieving entity for deletion: " + ga.getTargetArtifactId();
			record = connection.getCqSession().GetEntity(connection.getRecType(), ga.getTargetArtifactId() );
			currentAction = "deleting entity " + record.GetDisplayName();
			String deleteResult = connection.getCqSession().DeleteEntity(record, "delete");
			rcqHandler.reportActionResult(deleteResult, currentAction);
		} catch (CQException e) {
			log.error("failed at " + currentAction);
			throw new CCFRuntimeException("failed at " + currentAction);
		}
		
		return returnDocument(ga);
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
			ConnectionManager<RCQConnection> connectionManager, Document ga) {
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
		}  else if (cause instanceof RuntimeException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager, ga);
		} else if (cause instanceof SQLException) {
			if (cause.getMessage().contains("Unexpected token UNIQUE, requires COLLATION in statement")) {
				return true;
			}
		} 
		return false;
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

	public void setPreserveSemanticallyUnchangedHTMLFieldValues(
			boolean preserveSemanticallyUnchangedHTMLFieldValues) {
		this.preserveSemanticallyUnchangedHTMLFieldValues = preserveSemanticallyUnchangedHTMLFieldValues;
	}

	public RCQConnection connect(GenericArtifact ga) {
		String targetSystemId = ga.getTargetSystemId();
		String targetSystemKind = ga.getTargetSystemKind();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetRepositoryKind = ga.getTargetRepositoryKind();
		RCQConnection connection;
		
		try {
			connection = connect(targetSystemId, targetSystemKind,
					targetRepositoryId, targetRepositoryKind, serverUrl ,
					getUserName() + RCQConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the RCQ system. Max connections reached for "
					+ serverUrl;
			log.error(cause, e);
			ga
					.setErrorCode(GenericArtifact.ERROR_MAX_CONNECTIONS_REACHED_FOR_POOL);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the RCQ system "
					+ serverUrl;
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
			throw new CCFRuntimeException(cause, e);
		}
		
		
		return connection;
	}

	public RCQConnection connect(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException,
			ConnectionException {
		RCQConnection connection = null;
		connection = getConnectionManager()
				.getConnectionToUpdateOrExtractArtifact(systemId, systemKind,
						repositoryId, repositoryKind, connectionInfo,
						credentialInfo);
		return connection;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void validate(List exceptions) {
		super.validate(exceptions);

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

//		if (getServerUrl() == null) {
//			log.error("serverUrl-property not set");
//			exceptions.add(new ValidationException(
//					"serverUrl-property not set", this));
//		}

		rcqHandler = new RCQHandler();
		
	}
}
