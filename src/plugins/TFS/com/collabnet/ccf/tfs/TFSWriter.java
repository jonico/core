package com.collabnet.ccf.tfs;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
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
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.collabnet.ccf.tfs.TFSMetaData.TFSType;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.exceptions.TECoreException;

public class TFSWriter extends AbstractWriter<TFSConnection> {

    private static final Log     log               = LogFactory
                                                           .getLog(TFSWriter.class);

    private String               userName;
    private String               password;
    private String               serverUrl;
    private boolean              preserveSemanticallyUnchangedHTMLFieldValues;
    private TFSAttachmentHandler attachmentHandler = null;

    private TFSHandler           tfsHandler;

    public TFSConnection connect(GenericArtifact ga, String collectionName) {
        String targetSystemId = ga.getTargetSystemId();
        String targetSystemKind = ga.getTargetSystemKind();
        String targetRepositoryId = ga.getTargetRepositoryId();
        String targetRepositoryKind = ga.getTargetRepositoryKind();
        TFSConnection connection;

        try {
            connection = connect(targetSystemId, targetSystemKind,
                    targetRepositoryId, targetRepositoryKind, serverUrl + "/"
                            + collectionName, getUserName()
                            + TFSConnectionFactory.PARAM_DELIMITER
                            + getPassword());
        } catch (MaxConnectionsReachedException e) {
            String cause = "Could not create connection to the TFS system. Max connections reached for "
                    + serverUrl;
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_MAX_CONNECTIONS_REACHED_FOR_POOL);
            throw new CCFRuntimeException(cause, e);
        } catch (ConnectionException e) {
            String cause = "Could not create connection to the TFS system "
                    + serverUrl;
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
            throw new CCFRuntimeException(cause, e);
        }

        return connection;
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
        TFSType tfsType = TFSMetaData
                .retrieveTFSTypeFromRepositoryId(targetRepositoryId);

        if (tfsType.equals(TFSMetaData.TFSType.UNKNOWN)) {
            String cause = "Invalid repository format: " + targetRepositoryId;
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }

        TFSConnection connection;
        String collectionName = TFSMetaData
                .extractCollectionNameFromRepositoryId(targetRepositoryId);

        connection = connect(ga, collectionName);
        try {

            if (tfsType.equals(TFSType.WORKITEM)) {

                String projectName = TFSMetaData
                        .extractProjectNameFromRepositoryId(targetRepositoryId);
                String workItemType = TFSMetaData
                        .extractWorkItemTypeFromRepositoryId(targetRepositoryId);

                WorkItem result = createWorkItem(ga, collectionName,
                        projectName, workItemType, connection);
                if (result != null) {
                    log.info("Created work item " + result.getID()
                            + " with data from " + ga.getSourceArtifactId());
                }
            } else {
                String cause = "Unsupported repository format: "
                        + targetRepositoryId;
                log.error(cause);
                throw new CCFRuntimeException(cause);
            }
        } catch (Exception e) {
            String cause = "During the artifact create process in TFS, an error occured";
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

        TFSType tfsType = TFSMetaData
                .retrieveTFSTypeFromRepositoryId(targetRepositoryId);

        if (tfsType.equals(TFSMetaData.TFSType.UNKNOWN)) {
            String cause = "Invalid repository format: " + targetRepositoryId;
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }

        TFSConnection connection;
        String collectionName = TFSMetaData
                .extractCollectionNameFromRepositoryId(targetRepositoryId);

        connection = connect(ga, collectionName);
        String targetParentArtifactId = ga.getDepParentTargetArtifactId();

        GenericArtifact parentArtifact = null;
        try {
            attachmentHandler.handleAttachment(connection, ga,
                    targetParentArtifactId,
                    TFSMetaData.extractUserNameFromFullUserName(this
                            .getUserName()));

            WorkItem wi = connection.getTpc().getWorkItemClient()
                    .getWorkItemByID(Integer.valueOf(targetParentArtifactId));

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
                    .format((Date) wi.getFields()
                            .getField(CoreFieldReferenceNames.CHANGED_DATE)
                            .getValue()));
            parentArtifact.setTargetArtifactVersion(wi.getFields()
                    .getField(CoreFieldReferenceNames.REVISION).getValue()
                    .toString());
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

    public void disconnect(TFSConnection connection) {
        getConnectionManager().releaseConnection(connection);
    }

    public TFSAttachmentHandler getAttachmentHandler() {
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
            ConnectionManager<TFSConnection> connectionManager, Document ga) {
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
        } else if (cause instanceof TECoreException) {
            if (cause.getMessage().contains("Unknown host")) {
                return true;
            }
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

    public void setAttachmentHandler(TFSAttachmentHandler attachmentHandler) {
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
        TFSType tfsType = TFSMetaData
                .retrieveTFSTypeFromRepositoryId(targetRepositoryId);

        if (tfsType.equals(TFSMetaData.TFSType.UNKNOWN)) {
            String cause = "Invalid repository format: " + targetRepositoryId;
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }

        TFSConnection connection;
        String collectionName = TFSMetaData
                .extractCollectionNameFromRepositoryId(targetRepositoryId);

        connection = connect(ga, collectionName);
        try {

            if (tfsType.equals(TFSType.WORKITEM)) {

                String projectName = TFSMetaData
                        .extractProjectNameFromRepositoryId(targetRepositoryId);
                String workItemType = TFSMetaData
                        .extractWorkItemTypeFromRepositoryId(targetRepositoryId);

                WorkItem result = updateWorkItem(ga, collectionName,
                        projectName, workItemType, connection);
                if (result != null) {
                    log.info("Updated work item " + result.getID()
                            + " with data from " + ga.getSourceArtifactId());
                }
            } else {
                String cause = "Unsupported repository format: "
                        + targetRepositoryId;
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
            tfsHandler = new TFSHandler();
        } catch (Exception e) {
            log.error("Could not initialize TFSHandler");
            exceptions.add(new ValidationException(
                    "Could not initialize TFSHandler", this));
        }

        try {
            attachmentHandler = new TFSAttachmentHandler(serverUrl,
                    getConnectionManager());
        } catch (Exception e) {
            log.error("Could not initialize TFSAttachmentHandler");
            exceptions.add(new ValidationException(
                    "Could not initialize TFSAttachmentHandler", this));
        }

    }

    private WorkItem createWorkItem(GenericArtifact ga, String collectionName,
            String projectName, String workItemType, TFSConnection connection) {

        return tfsHandler.createWorkItem(ga, collectionName, projectName,
                workItemType, connection);
    }

    private WorkItem updateWorkItem(GenericArtifact ga, String collectionName,
            String projectName, String workItemType, TFSConnection connection) {

        return tfsHandler.updateWorkItem(ga, collectionName, projectName,
                workItemType, connection);
    }

}
