package com.collabnet.ccf.jira;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.exception.ValidationException;

import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.collabnet.ccf.jira.JIRAMetaData.JIRAType;

public class JIRAWriter extends AbstractWriter<JIRAConnection> {

    private static final Log         log               = LogFactory
                                                               .getLog(JIRAWriter.class);

    private String                   userName;
    private String                   password;
    private String                   serverUrl;
    private boolean                  preserveSemanticallyUnchangedHTMLFieldValues;
    private JIRAAttachmentHandler    attachmentHandler = null;

    private JIRAHandler              jiraHandler;
    static final NullProgressMonitor pm                = new NullProgressMonitor();

    public JIRAConnection connect(GenericArtifact ga) {
        String targetSystemId = ga.getTargetSystemId();
        String targetSystemKind = ga.getTargetSystemKind();
        String targetRepositoryId = ga.getTargetRepositoryId();
        String targetRepositoryKind = ga.getTargetRepositoryKind();
        JIRAConnection connection;

        try {
            connection = connect(targetSystemId, targetSystemKind,
                    targetRepositoryId, targetRepositoryKind, serverUrl,
                    getUserName() + JIRAConnectionFactory.PARAM_DELIMITER
                            + getPassword());
        } catch (MaxConnectionsReachedException e) {
            String cause = "Could not create connection to the JIRA system. Max connections reached for "
                    + serverUrl;
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_MAX_CONNECTIONS_REACHED_FOR_POOL);
            throw new CCFRuntimeException(cause, e);
        } catch (ConnectionException e) {
            String cause = "Could not create connection to the JIRA system "
                    + serverUrl;
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
            throw new CCFRuntimeException(cause, e);
        }

        return connection;
    }

    public JIRAConnection connect(String systemId, String systemKind,
            String repositoryId, String repositoryKind, String connectionInfo,
            String credentialInfo) throws MaxConnectionsReachedException,
            ConnectionException {
        // log.info("Before calling the parent connect()");
        JIRAConnection connection = null;
        connection = getConnectionManager()
                .getConnectionToUpdateOrExtractArtifact(systemId, systemKind,
                        repositoryId, repositoryKind, connectionInfo,
                        credentialInfo);
        return connection;
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

        // find out what to create
        String targetRepositoryId = ga.getTargetRepositoryId();
        JIRAType jiraType = JIRAMetaData
                .retrieveJIRATypeFromRepositoryId(targetRepositoryId);

        if (jiraType.equals(JIRAMetaData.JIRAType.UNKNOWN)) {
            String cause = "Invalid repository format: " + targetRepositoryId;
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }

        JIRAConnection connection;
        connection = connect(ga);
        try {

            if (jiraType.equals(JIRAType.ISSUE)) {

                String projectKey = JIRAMetaData
                        .extractProjectKeyFromRepositoryId(targetRepositoryId);
                String issueType = JIRAMetaData
                        .extractIssueTypeFromRepositoryId(targetRepositoryId);

                BasicIssue result = createIssue(ga, projectKey, issueType,
                        connection);
                if (result != null) {
                    log.info("Created issue " + result.getId()
                            + " with data from " + ga.getSourceArtifactId());
                }
            } else {
                String cause = "Unsupported repository format: "
                        + targetRepositoryId;
                log.error(cause);
                throw new CCFRuntimeException(cause);
            }
        } catch (Exception e) {
            String cause = "During the artifact create process in JIRA, an error occured";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } finally {
            disconnect(connection);
        }

        return returnDocument(ga);
    }

    @Override
    public Document[] createAttachment(Document gaDocument) {

        GenericArtifact ga = null;
        try {
            ga = GenericArtifactHelper
                    .createGenericArtifactJavaObject(gaDocument);
        } catch (GenericArtifactParsingException e) {
            String cause = "Problem occured while parsing the GenericArtifact into Document";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        }

        String targetRepositoryId = ga.getTargetRepositoryId();

        JIRAType jiraType = JIRAMetaData
                .retrieveJIRATypeFromRepositoryId(targetRepositoryId);

        if (jiraType.equals(JIRAMetaData.JIRAType.UNKNOWN)) {
            String cause = "Invalid repository format: " + targetRepositoryId;
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }

        JIRAConnection connection;
        connection = connect(ga);
        String targetParentArtifactId = ga.getDepParentTargetArtifactId();

        GenericArtifact parentArtifact = null;
        try {
            Issue issue = attachmentHandler.handleAttachment(connection, ga,
                    targetParentArtifactId, this.getUserName());

            if (issue == null) {
                issue = connection.getJiraRestClient().getIssueClient()
                        .getIssue(targetParentArtifactId, pm);
            }
            parentArtifact = new GenericArtifact();
            // make sure that we do not update the synchronization status record
            // for replayed attachments
            parentArtifact.setTransactionId(ga.getTransactionId());
            parentArtifact
                    .setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
            parentArtifact
                    .setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
            parentArtifact
                    .setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
            parentArtifact.setConflictResolutionPriority(ga
                    .getConflictResolutionPriority());
            parentArtifact.setSourceArtifactId(ga
                    .getDepParentSourceArtifactId());
            parentArtifact.setSourceArtifactLastModifiedDate(ga
                    .getSourceArtifactLastModifiedDate());
            parentArtifact.setSourceArtifactVersion(ga
                    .getSourceArtifactVersion());
            parentArtifact.setSourceRepositoryId(ga.getSourceRepositoryId());
            parentArtifact.setSourceSystemId(ga.getSourceSystemId());
            parentArtifact.setSourceSystemKind(ga.getSourceSystemKind());
            parentArtifact
                    .setSourceRepositoryKind(ga.getSourceRepositoryKind());
            parentArtifact
                    .setSourceSystemTimezone(ga.getSourceSystemTimezone());

            parentArtifact.setTargetArtifactId(targetParentArtifactId);
            parentArtifact.setTargetArtifactLastModifiedDate(DateUtil
                    .format(issue.getUpdateDate().toDate()));
            parentArtifact.setTargetArtifactVersion(String.valueOf(issue
                    .getUpdateDate().getMillis()));
            parentArtifact.setTargetRepositoryId(ga.getTargetRepositoryId());
            parentArtifact
                    .setTargetRepositoryKind(ga.getTargetRepositoryKind());
            parentArtifact.setTargetSystemId(ga.getTargetSystemId());
            parentArtifact.setTargetSystemKind(ga.getTargetSystemKind());
            parentArtifact
                    .setTargetSystemTimezone(ga.getTargetSystemTimezone());

        } catch (RemoteException e) {
            String cause = "Problem occured while creating attachments in TF";
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        } finally {
            disconnect(connection);
        }

        Document parentArtifactDoc = returnDocument(parentArtifact);
        Document attachmentDocument = this.returnDocument(ga);
        Document[] retDocs = new Document[] { attachmentDocument,
                parentArtifactDoc };
        return retDocs;
    }

    @Override
    public Document createDependency(Document gaDocument) {

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

    public void disconnect(JIRAConnection connection) {
        getConnectionManager().releaseConnection(connection);
    }

    public JIRAAttachmentHandler getAttachmentHandler() {
        return attachmentHandler;
    }

    public String getPassword() {
        return password;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean handleException(Throwable cause,
            ConnectionManager<JIRAConnection> connectionManager, Document ga) {
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
        } else if (cause instanceof RuntimeException) {
            Throwable innerCause = cause.getCause();
            return handleException(innerCause, connectionManager, ga);
        } else if (cause instanceof SQLException) {
            if (cause.getMessage().contains(
                    "Unexpected token UNIQUE, requires COLLATION in statement")) {
                return true;
            }
        }
        return false;
    }

    public boolean isPreserveSemanticallyUnchangedHTMLFieldValues() {
        return preserveSemanticallyUnchangedHTMLFieldValues;
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

    public void setAttachmentHandler(JIRAAttachmentHandler attachmentHandler) {
        this.attachmentHandler = attachmentHandler;
    }

    public void setPassword(String password) {
        this.password = Obfuscator.deObfuscatePassword(password);
    }

    public void setPreserveSemanticallyUnchangedHTMLFieldValues(
            boolean preserveSemanticallyUnchangedHTMLFieldValues) {
        this.preserveSemanticallyUnchangedHTMLFieldValues = preserveSemanticallyUnchangedHTMLFieldValues;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        JIRAType tfsType = JIRAMetaData
                .retrieveJIRATypeFromRepositoryId(targetRepositoryId);

        if (tfsType.equals(JIRAMetaData.JIRAType.UNKNOWN)) {
            String cause = "Invalid repository format: " + targetRepositoryId;
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }

        JIRAConnection connection;
        connection = connect(ga);
        try {

            if (tfsType.equals(JIRAType.ISSUE)) {

                String projectName = JIRAMetaData
                        .extractProjectKeyFromRepositoryId(targetRepositoryId);
                String workItemType = JIRAMetaData
                        .extractIssueTypeFromRepositoryId(targetRepositoryId);

                Issue result = updateIssue(ga, projectName, workItemType,
                        connection);
                if (result != null) {
                    log.info("Updated work item " + result.getKey()
                            + " with data from " + ga.getSourceArtifactId());
                }
            } else {
                String cause = "Unsupported repository format: "
                        + targetRepositoryId;
                log.error(cause);
                throw new CCFRuntimeException(cause);
            }
        } catch (Exception e) {
            String cause = "During the artifact update process in JIRA, an error occured";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } finally {
            disconnect(connection);
        }
        return returnDocument(ga);
    }

    @Override
    public Document updateAttachment(Document gaDocument) {
        log.warn("updateAttachment is not implemented...!");
        return null;
    }

    @Override
    public Document updateDependency(Document gaDocument) {
        return null;
    }

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

        if (getServerUrl() == null) {
            log.error("serverUrl-property not set");
            exceptions.add(new ValidationException(
                    "serverUrl-property not set", this));
        }

        try {
            jiraHandler = new JIRAHandler();
        } catch (Exception e) {
            log.error("Could not initialize TFSHandler");
            exceptions.add(new ValidationException(
                    "Could not initialize TFSHandler", this));
        }

        try {
            attachmentHandler = new JIRAAttachmentHandler(serverUrl,
                    getConnectionManager());
        } catch (Exception e) {
            log.error("Could not initialize TFSAttachmentHandler");
            exceptions.add(new ValidationException(
                    "Could not initialize TFSAttachmentHandler", this));
        }

    }

    private BasicIssue createIssue(GenericArtifact ga, String projectKey,
            String issueType, JIRAConnection connection) {

        return jiraHandler.createIssue(ga, projectKey, issueType, connection);
    }

    private Issue updateIssue(GenericArtifact ga, String projectKey,
            String issueType, JIRAConnection connection) {

        return jiraHandler.updateIssue(ga, projectKey, issueType, connection);
    }

}
