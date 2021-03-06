package com.collabnet.ccf.tfs;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.tfs.TFSMetaData.TFSType;
import com.microsoft.tfs.core.exceptions.TECoreException;

public class TFSReader extends AbstractReader<TFSConnection> {

    private String               userName;
    private String               password;
    private String               serverUrl;
    private boolean              ignoreConnectorUserUpdates = true;
    private TFSAttachmentHandler attachmentHandler          = null;

    private TFSHandler           tfsHandler                 = null;

    private static final Log     log                        = LogFactory
                                                                    .getLog(TFSReader.class);

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

    public void disconnect(TFSConnection connection) {
        getConnectionManager().releaseConnection(connection);
    }

    @Override
    public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
            GenericArtifact artifactData) {

        String artifactId = artifactData.getSourceArtifactId();
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        Date lastModifiedDate = this.getLastModifiedDate(syncInfo);

        TFSConnection connection;
        String collectionName = TFSMetaData
                .extractCollectionNameFromRepositoryId(sourceRepositoryId);
        try {
            connection = connect(sourceSystemId, sourceSystemKind,
                    sourceRepositoryId, sourceRepositoryKind, serverUrl + "/"
                            + collectionName, getUserName()
                            + TFSConnectionFactory.PARAM_DELIMITER
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

        List<String> artifactIds = new ArrayList<String>();
        artifactIds.add(artifactId);
        List<GenericArtifact> attachments = null;
        try {

            attachments = attachmentHandler.listAttachments(connection,
                    lastModifiedDate,
                    isIgnoreConnectorUserUpdates() ? getUserName() : "",
                    artifactIds, this.getMaxAttachmentSizePerArtifact(), this
                            .isShipAttachmentsWithArtifact(), artifactData);
            for (GenericArtifact attachment : attachments) {
                populateSrcAndDestForAttachment(syncInfo, attachment);
            }

        } catch (RemoteException e) {
            String cause = "During the attachment retrieval process from TF, an error occured";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } finally {
            this.disconnect(connection);
        }
        return attachments;
    }

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
        TFSType tfsType = TFSMetaData
                .retrieveTFSTypeFromRepositoryId(sourceRepositoryId);

        if (tfsType.equals(TFSMetaData.TFSType.UNKNOWN)) {
            String cause = "Invalid repository format: " + sourceRepositoryId;
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }

        TFSConnection connection;
        String collectionName = TFSMetaData
                .extractCollectionNameFromRepositoryId(sourceRepositoryId);
        try {
            connection = connect(sourceSystemId, sourceSystemKind,
                    sourceRepositoryId, sourceRepositoryKind, serverUrl + "/"
                            + collectionName, getUserName()
                            + TFSConnectionFactory.PARAM_DELIMITER
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

        GenericArtifact genericArtifact = new GenericArtifact();
        try {
            /**
             * Create a new generic artifact data structure
             */
            populateSrcAndDest(syncInfo, genericArtifact);
            genericArtifact.setSourceArtifactId(artifactId);
            genericArtifact.setArtifactMode(ArtifactModeValue.COMPLETE);
            genericArtifact.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);

            if (tfsType.equals(TFSType.WORKITEM)) {
                String projectName = TFSMetaData
                        .extractProjectNameFromRepositoryId(sourceRepositoryId);
                String workItemType = TFSMetaData
                        .extractWorkItemTypeFromRepositoryId(sourceRepositoryId);
                tfsHandler.getWorkItem(connection, collectionName, projectName,
                        workItemType, lastModifiedDate,
                        lastSynchronizedVersion, lastSynchedArtifactId,
                        artifactId, getUserName(),
                        isIgnoreConnectorUserUpdates(), genericArtifact,
                        this.getSourceRepositoryId(syncInfo));
            }

        } catch (Exception e) {
            String cause = "During the artifact retrieval process from TFS, an error occured";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } finally {
            disconnect(connection);
        }
        return genericArtifact;
    }

    public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
            String artifactId) {

        return new ArrayList<GenericArtifact>();
    }

    public TFSAttachmentHandler getAttachmentHandler() {
        return attachmentHandler;
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
        TFSType tfsType = TFSMetaData
                .retrieveTFSTypeFromRepositoryId(sourceRepositoryId);

        if (tfsType.equals(TFSMetaData.TFSType.UNKNOWN)) {
            String cause = "Invalid repository format: " + sourceRepositoryId;
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }

        TFSConnection connection;
        String collectionName = TFSMetaData
                .extractCollectionNameFromRepositoryId(sourceRepositoryId);
        try {
            connection = connect(sourceSystemId, sourceSystemKind,
                    sourceRepositoryId, sourceRepositoryKind, serverUrl + "/"
                            + collectionName, getUserName()
                            + TFSConnectionFactory.PARAM_DELIMITER
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
                String projectName = TFSMetaData
                        .extractProjectNameFromRepositoryId(sourceRepositoryId);
                String workItemType = TFSMetaData
                        .extractWorkItemTypeFromRepositoryId(sourceRepositoryId);
                tfsHandler.getChangedWorkItems(connection, collectionName,
                        projectName, workItemType, lastModifiedDate,
                        lastSynchronizedVersion, lastSynchedArtifactId,
                        artifactStates, getUserName(),
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

    public List<ArtifactState> getChangedArtifactsToForceSync(
            Set<String> artifactsToForce, Document SyncInfo) {
        //Currently we don't support changed artifacts to force sync 
        return new ArrayList<ArtifactState>();
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
            ConnectionManager<TFSConnection> connectionManager) {
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
        } else if (cause instanceof TECoreException) {
            if (cause.getMessage().contains("Unknown host")) {
                return true;
            }
        } else if (cause instanceof RuntimeException) {
            Throwable innerCause = cause.getCause();
            return handleException(innerCause, connectionManager);
        } else if (cause instanceof SQLException) {
            if (cause.getMessage().contains(
                    "Unexpected token UNIQUE, requires COLLATION in statement")) {
                return true;
            }
        }
        return false;
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

    public void setAttachmentHandler(TFSAttachmentHandler attachmentHandler) {
        this.attachmentHandler = attachmentHandler;
    }

    public void setIgnoreConnectorUserUpdates(boolean ignoreConnectorUserUpdates) {
        this.ignoreConnectorUserUpdates = ignoreConnectorUserUpdates;
    }

    public void setPassword(String password) {
        this.password = Obfuscator.deObfuscatePassword(password);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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
        try {
            attachmentHandler = new TFSAttachmentHandler(serverUrl,
                    getConnectionManager());
        } catch (Exception e) {
            log.error("Could not initialize TFSAttachmentHandler");
            exceptions.add(new ValidationException(
                    "Could not initialize TFSAttachmentHandler", this));
        }

    }

    private void populateSrcAndDest(Document syncInfo, GenericArtifact ga) {
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String conflictResolutionPriority = this
                .getConflictResolutionPriority(syncInfo);

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

    private void populateSrcAndDestForAttachment(Document syncInfo,
            GenericArtifact ga) {

        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);

        String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
        String targetRepositoryKind = this.getTargetRepositoryKind(syncInfo);
        String targetSystemId = this.getTargetSystemId(syncInfo);
        String targetSystemKind = this.getTargetSystemKind(syncInfo);

        ga.setSourceRepositoryId(sourceRepositoryId);
        ga.setSourceRepositoryKind(sourceRepositoryKind);
        ga.setSourceSystemId(sourceSystemId);
        ga.setSourceSystemKind(sourceSystemKind);

        ga.setDepParentSourceRepositoryId(sourceRepositoryId);
        ga.setDepParentSourceRepositoryKind(sourceRepositoryKind);

        ga.setTargetRepositoryId(targetRepositoryId);
        ga.setTargetRepositoryKind(targetRepositoryKind);
        ga.setTargetSystemId(targetSystemId);
        ga.setTargetSystemKind(targetSystemKind);

        ga.setDepParentTargetRepositoryId(targetRepositoryId);
        ga.setDepParentTargetRepositoryKind(targetRepositoryKind);

    }

}
