/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.teamforge;

import static com.collabnet.ccf.teamforge.TFArtifactMetaData.COMMENT_TEXT;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.commons.lang.StringUtils;
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
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.collabnet.ce.soap50.webservices.cemain.TrackerFieldSoapDO;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.PlanningFolderRuleViolationException;
import com.collabnet.teamforge.api.planning.PlanningFolderDO;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.ArtifactDependencyRow;
import com.collabnet.teamforge.api.tracker.TrackerDO;

/**
 * This component is responsible for writing TF tracker items encoded in the
 * generic XML artifact format back to the TF tracker
 * 
 * @author jnicolai
 * 
 */
public class TFWriter extends AbstractWriter<Connection> implements IDataProcessor {

    /**
     * log4j logger instance
     */
    private static final Log    log                                          = LogFactory
                                                                                     .getLog(TFWriter.class);

    private static final Log    logConflictResolutor                         = LogFactory
                                                                                     .getLog("com.collabnet.ccf.core.conflict.resolution");

    /**
     * TF tracker handler instance
     */
    private TFTrackerHandler    trackerHandler;

    /**
     * Comment used when updating TF tracker items
     */
    private String              updateComment;

    private TFAttachmentHandler attachmentHandler;

    // private ConnectionManager<Connection> connectionManager = null;

    private String              serverUrl;

    private boolean             translateTechnicalReleaseIds                 = false;

    private boolean             releaseIdFieldsContainFileReleasePackageName = false;

    private String              packageReleaseSeparatorString                = " > ";

    /**
     * Attachment filename is prefixed with sync username when the property
     * ignoreSyncUserNameFromAttachment is set to false and by default the value
     * is false.
     */
    private boolean             ignoreSyncUserNameFromAttachment             = false;

    /**
     * Password that is used to login into the TF/CSFE instance in combination
     * with the username
     */
    private String              password;

    /**
     * Username that is used to login into the TF/CSFE instance
     */
    private String              username;

    /**
     * Another user name that is used to login into the TF/CSFE instance This
     * user has to differ from the ordinary user used to log in in order to
     * force initial resyncs with the source system once a new artifact has been
     * created.
     */
    private String              resyncUserName;

    /**
     * Password that belongs to the resync user. This user has to differ from
     * the ordinary user used to log in in order to force initial resyncs with
     * the source system once a new artifact has been created.
     */
    private String              resyncPassword;

    public TFWriter() {
    }

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
                        getUsername() + TFConnectionFactory.PARAM_DELIMITER
                                + getPassword(), false);
            } else {
                connection = connect(targetSystemId, targetSystemKind,
                        targetRepositoryId, targetRepositoryKind, serverUrl,
                        getResyncUserName()
                                + TFConnectionFactory.PARAM_DELIMITER
                                + getResyncPassword(), true);
            }
        } catch (MaxConnectionsReachedException e) {
            String cause = "Could not create connection to the TF system. Max connections reached for "
                    + serverUrl;
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_MAX_CONNECTIONS_REACHED_FOR_POOL);
            throw new CCFRuntimeException(cause, e);
        } catch (ConnectionException e) {
            String cause = "Could not create connection to the TF system "
                    + serverUrl;
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
            throw new CCFRuntimeException(cause, e);
        }
        return connection;
    }

    public Connection connect(String systemId, String systemKind,
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

    public Document createArtifact(Document data) {
        GenericArtifact ga = null;
        try {
            ga = GenericArtifactHelper.createGenericArtifactJavaObject(data);
        } catch (GenericArtifactParsingException e) {
            String cause = "Problem occured while parsing the GenericArtifact into Document";
            log.error(cause, e);
            XPathUtils.addAttribute(data.getRootElement(),
                    GenericArtifactHelper.ERROR_CODE,
                    GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
            throw new CCFRuntimeException(cause, e);
        }

        String targetRepositoryId = ga.getTargetRepositoryId();
        String targetArtifactId = ga.getTargetArtifactId();
        String tracker = targetRepositoryId;
        Connection connection = connect(ga);
        try {
            if (TFConnectionFactory.isTrackerRepository(targetRepositoryId)) {
                ArtifactDO result = null;
                try {
                    this.initializeArtifact(ga);
                    result = this.createArtifact(ga, tracker, connection);
                    // update Id field after creating the artifact
                    targetArtifactId = result.getId();
                    TFGAHelper
                            .addField(
                                    ga,
                                    TFArtifactMetaData.TFFields.id
                                            .getFieldName(),
                                    targetArtifactId,
                                    GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
                                    GenericArtifactField.FieldValueTypeValue.STRING);
                    this.populateTargetArtifactAttributes(ga, result);
                    return this.returnDocument(ga);
                } catch (NumberFormatException e) {
                    log.error("Wrong data format of attribute for artifact "
                            + data.asXML(), e);
                    return null;
                }
            } else if (TFConnectionFactory
                    .isPlanningFolderRepository(targetRepositoryId)) {
                // we write planning folders, so first do a check whether we
                // support this feature
                if (!connection.supports53()) {
                    // we do not support planning folders
                    throw new CCFRuntimeException(
                            "Planning Folders are not supported since TeamForge target system does not provide them.");
                }
                PlanningFolderDO result = null;
                String project = TFConnectionFactory
                        .extractProjectFromRepositoryId(targetRepositoryId);
                result = createPlanningFolder(ga, project, connection);
                targetArtifactId = result.getId();
                TFGAHelper.addField(ga,
                        TFArtifactMetaData.TFFields.id.getFieldName(),
                        targetArtifactId,
                        GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
                        GenericArtifactField.FieldValueTypeValue.STRING);
                populateTargetArtifactAttributesFromPlanningFolder(ga, result);
                return returnDocument(ga);
            } else if (TFConnectionFactory
                    .isTrackerMetaDataRepository(targetRepositoryId)) {
                TrackerDO result = null;
                String trackerId = TFConnectionFactory
                        .extractTrackerFromMetaDataRepositoryId(targetRepositoryId);
                result = updateTrackerMetaData(ga, trackerId, connection);
                populateTargetArtifactAttributesFromTracker(ga, result);
                return returnDocument(ga);
            } else {
                throw new CCFRuntimeException("Unknown repository id format: "
                        + targetRepositoryId);
            }
        } finally {
            disconnect(connection);
        }

    }

    public Document[] createAttachment(Document data) {
        GenericArtifact ga = null;
        try {
            ga = GenericArtifactHelper.createGenericArtifactJavaObject(data);
        } catch (GenericArtifactParsingException e) {
            String cause = "Problem occured while parsing the GenericArtifact into Document";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        }
        this.initializeArtifact(ga);
        Connection connection = connect(ga);
        String targetParentArtifactId = ga.getDepParentTargetArtifactId();
        GenericArtifact parentArtifact = null;
        try {
            try {
                attachmentHandler.handleAttachment(connection, ga,
                        targetParentArtifactId, this.getUsername(),
                        this.isIgnoreSyncUserNameFromAttachment());
            } catch (PlanningFolderRuleViolationException e) {
                // should not happen since we never modify the planning folder
                // here
                throw new CCFRuntimeException(
                        "While trying to attach an attachment, a planning folder rule violation occured",
                        e);
            }
            ArtifactDO artifact = trackerHandler.getTrackerItem(connection,
                    targetParentArtifactId);
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
                    .format(artifact.getLastModifiedDate()));
            parentArtifact.setTargetArtifactVersion(Integer.toString(artifact
                    .getVersion()));
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
        // throw new
        // CCFRuntimeException("createDependency is not implemented...!");
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
        // now we have to figure out whether we have to delete a planning folder
        // or a tracker item
        String targetRepositoryId = ga.getTargetRepositoryId();
        String targetArtifactId = ga.getTargetArtifactId();
        Connection connection = null;
        GenericArtifact deletedArtifact = new GenericArtifact();
        // make sure that we do not update the synchronization status record for
        // replayed attachments
        deletedArtifact.setTransactionId(ga.getTransactionId());
        deletedArtifact
                .setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
        deletedArtifact
                .setArtifactAction(GenericArtifact.ArtifactActionValue.DELETE);
        deletedArtifact
                .setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
        deletedArtifact.setConflictResolutionPriority(ga
                .getConflictResolutionPriority());
        deletedArtifact.setSourceArtifactId(ga.getSourceArtifactId());
        deletedArtifact.setSourceArtifactLastModifiedDate(ga
                .getSourceArtifactLastModifiedDate());
        deletedArtifact.setSourceArtifactVersion(ga.getSourceArtifactVersion());
        deletedArtifact.setSourceRepositoryId(ga.getSourceRepositoryId());
        deletedArtifact.setSourceSystemId(ga.getSourceSystemId());
        deletedArtifact.setSourceSystemKind(ga.getSourceSystemKind());
        deletedArtifact.setSourceRepositoryKind(ga.getSourceRepositoryKind());
        deletedArtifact.setSourceSystemTimezone(ga.getSourceSystemTimezone());

        deletedArtifact.setTargetArtifactId(targetArtifactId);
        deletedArtifact.setTargetRepositoryId(ga.getTargetRepositoryId());
        deletedArtifact.setTargetRepositoryKind(ga.getTargetRepositoryKind());
        deletedArtifact.setTargetSystemId(ga.getTargetSystemId());
        deletedArtifact.setTargetSystemKind(ga.getTargetSystemKind());
        deletedArtifact.setTargetSystemTimezone(ga.getTargetSystemTimezone());

        try {
            connection = connect(ga);
            // now we have to figure out whether we have to delete a planning
            // folder or a tracker item
            if (TFConnectionFactory.isTrackerRepository(targetRepositoryId)) {
                ArtifactDO artifact = null;
                try {
                    artifact = trackerHandler.getTrackerItem(connection,
                            targetArtifactId);
                } catch (AxisFault e) {
                    javax.xml.namespace.QName faultCode = e.getFaultCode();
                    if (faultCode.getLocalPart().equals("NoSuchObjectFault")) {
                        log.warn("Artifact " + targetArtifactId
                                + " does not exist any more!");
                    } else {
                        throw e;
                    }
                }
                if (artifact != null) {
                    deletedArtifact.setTargetArtifactLastModifiedDate(DateUtil
                            .format(artifact.getLastModifiedDate()));
                    deletedArtifact.setTargetArtifactVersion(Integer
                            .toString(artifact.getVersion()));
                    try {
                        trackerHandler.removeArtifact(connection,
                                targetArtifactId);
                    } catch (AxisFault e) {
                        javax.xml.namespace.QName faultCode = e.getFaultCode();
                        if (faultCode.getLocalPart()
                                .equals("NoSuchObjectFault")) {
                            log.warn("Artifact " + targetArtifactId
                                    + " does not exist any more!");
                        } else {
                            throw e;
                        }
                    }
                    log.info("Artifact " + targetArtifactId
                            + " is deleted successfully.");
                }
            } else if (TFConnectionFactory
                    .isPlanningFolderRepository(targetRepositoryId)) {
                PlanningFolderDO artifact = null;
                try {
                    artifact = connection.getPlanningClient()
                            .getPlanningFolderData(targetArtifactId);
                } catch (AxisFault e) {
                    javax.xml.namespace.QName faultCode = e.getFaultCode();
                    if (faultCode.getLocalPart().equals("NoSuchObjectFault")) {
                        log.warn("Artifact " + targetArtifactId
                                + " does not exist any more!");
                    } else {
                        throw e;
                    }
                }
                if (artifact != null) {
                    deletedArtifact.setTargetArtifactLastModifiedDate(DateUtil
                            .format(artifact.getLastModifiedDate()));
                    deletedArtifact.setTargetArtifactVersion(Integer
                            .toString(artifact.getVersion()));
                    try {
                        connection.getPlanningClient().deletePlanningFolder(
                                targetArtifactId);
                    } catch (AxisFault e) {
                        javax.xml.namespace.QName faultCode = e.getFaultCode();
                        if (faultCode.getLocalPart()
                                .equals("NoSuchObjectFault")) {
                            log.warn("Artifact " + targetArtifactId
                                    + " does not exist any more!");
                        } else {
                            throw e;
                        }
                    }
                    log.info("Artifact " + targetArtifactId
                            + " is deleted successfully.");
                }
            } else {
                log.warn("deleteArtifact is not implemented for repository "
                        + targetRepositoryId);
            }
        } catch (RemoteException e) {
            String message = "Exception while deleting artifact "
                    + targetArtifactId;
            log.error(message, e);
            throw new CCFRuntimeException(message, e);
        } finally {
            if (connection != null) {
                this.disconnect(connection);
            }
        }
        Document returnDocument = null;
        try {
            returnDocument = GenericArtifactHelper
                    .createGenericArtifactXMLDocument(deletedArtifact);
        } catch (GenericArtifactParsingException e) {
            String message = "Exception while deleting artifact "
                    + targetArtifactId + ". Could not parse Generic artifact";
            log.error(message, e);
            throw new CCFRuntimeException(message, e);
        }
        return returnDocument;
    }

    @Override
    public Document[] deleteAttachment(Document gaDocument) {
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
        this.initializeArtifact(ga);
        // String targetRepositoryId = ga.getTargetRepositoryId();
        String targetArtifactId = ga.getTargetArtifactId();
        String artifactId = ga.getDepParentTargetArtifactId();
        // String tracker = targetRepositoryId;
        Connection connection = null;
        GenericArtifact parentArtifact = null;
        try {
            connection = connect(ga);
            try {
                attachmentHandler.deleteAttachment(connection,
                        targetArtifactId, artifactId, ga);
            } catch (AxisFault e) {
                javax.xml.namespace.QName faultCode = e.getFaultCode();
                if (faultCode.getLocalPart().equals("NoSuchObjectFault")) {
                    log.warn("Attachment " + targetArtifactId
                            + " does not exist any more!");
                    return null;
                } else {
                    throw e;
                }
            }
            log.info("Attachment " + targetArtifactId
                    + " is deleted successfully.");
            ArtifactDO artifact = null;
            try {
                artifact = trackerHandler
                        .getTrackerItem(connection, artifactId);
            } catch (AxisFault e) {
                javax.xml.namespace.QName faultCode = e.getFaultCode();
                if (faultCode.getLocalPart().equals("NoSuchObjectFault")) {
                    log.warn("Artifact " + artifactId
                            + " does not exist any more!");
                    return null;
                } else {
                    throw e;
                }
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

            parentArtifact.setTargetArtifactId(artifactId);
            parentArtifact.setTargetArtifactLastModifiedDate(DateUtil
                    .format(artifact.getLastModifiedDate()));
            parentArtifact.setTargetArtifactVersion(Integer.toString(artifact
                    .getVersion()));
            parentArtifact.setTargetRepositoryId(ga.getTargetRepositoryId());
            parentArtifact
                    .setTargetRepositoryKind(ga.getTargetRepositoryKind());
            parentArtifact.setTargetSystemId(ga.getTargetSystemId());
            parentArtifact.setTargetSystemKind(ga.getTargetSystemKind());
            parentArtifact
                    .setTargetSystemTimezone(ga.getTargetSystemTimezone());
        } catch (RemoteException e) {

            String message = "Exception while deleting attachment "
                    + artifactId;
            log.error(message, e);
            throw new CCFRuntimeException(message, e);
        } finally {
            if (connection != null) {
                this.disconnect(connection);
            }
        }
        Document returnDocument = null;
        Document returnParentDocument = null;
        try {
            returnDocument = GenericArtifactHelper
                    .createGenericArtifactXMLDocument(ga);
            returnParentDocument = GenericArtifactHelper
                    .createGenericArtifactXMLDocument(parentArtifact);
        } catch (GenericArtifactParsingException e) {
            String message = "Exception while deleting attachment "
                    + artifactId + ". Could not parse Generic artifact";
            log.error(message, e);
            throw new CCFRuntimeException(message, e);
        }
        return new Document[] { returnDocument, returnParentDocument };
    }

    @Override
    public Document deleteDependency(Document gaDocument) {
        // throw new
        // CCFRuntimeException("deleteDependency is not implemented...!");
        log.warn("deleteDependency is not implemented...!");
        return null;
    }

    /**
     * Returns the separator used to compute the human readable name of releases
     * The default value is " > "
     * 
     * @return
     */
    public String getPackageReleaseSeparatorString() {
        return packageReleaseSeparatorString;
    }

    /**
     * Returns the server URL of the CSFE/TF system that is configured in the
     * wiring file.
     * 
     * @return
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * Get the update comment
     * 
     * @return see private attribute doc
     */
    public String getUpdateComment() {
        return updateComment;
    }

    /**
     * Gets the manadtory user name The user name is used to login into the
     * TF/CSFE instance whenever an artifact should be updated or extracted.
     * This user has to differ from the resync user in order to force initial
     * resyncs with the source system once a new artifact has been created.
     * 
     * @return the userName
     */
    public String getUsername() {
        return username;
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
        } else if (cause instanceof AxisFault) {
            QName faultCode = ((AxisFault) cause).getFaultCode();
            if (faultCode.getLocalPart().equals("InvalidSessionFault")
                    && connectionManager.isEnableReloginAfterSessionTimeout()) {
                return true;
            }
        } else if (cause instanceof RemoteException) {
            Throwable innerCause = cause.getCause();
            return handleException(innerCause, connectionManager, ga);
        } else if (cause instanceof CCFRuntimeException) {
            Throwable innerCause = cause.getCause();
            return handleException(innerCause, connectionManager, ga);
        }
        return false;
    }

    public boolean isIgnoreSyncUserNameFromAttachment() {
        return ignoreSyncUserNameFromAttachment;
    }

    /**
     * This property (false by default), determines whether the human readable
     * release name that should be translated into a technical release id is
     * prepended by the file release package title. Only used if
     * translateTechnicalReleaseIds property is set to true
     * 
     */
    public boolean isReleaseIdFieldsContainFileReleasePackageName() {
        return releaseIdFieldsContainFileReleasePackageName;
    }

    public boolean isTranslateTechnicalReleaseIds() {
        return translateTechnicalReleaseIds;
    }

    /**
     * Reset processor
     */
    public void reset(Object context) {
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

    public void setIgnoreSyncUserNameFromAttachment(
            boolean ignoreSyncUserNameFromAttachment) {
        this.ignoreSyncUserNameFromAttachment = ignoreSyncUserNameFromAttachment;
    }

    /**
     * Determines the separator used to compute the human readable name of
     * releases The default value is " > "
     * 
     * @param planningFolderSeparatorString
     */
    public void setPackageReleaseSeparatorString(
            String packageReleaseSeparatorString) {
        this.packageReleaseSeparatorString = packageReleaseSeparatorString;
    }

    /**
     * Sets the password that belongs to the username
     * 
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = Obfuscator.deObfuscatePassword(password);
    }

    /**
     * This property (false by default), determines whether the human readable
     * release name that should be translated into a technical release id is
     * prepended by the file release package title. Only used if
     * translateTechnicalReleaseIds property is set to true
     * 
     * @param releaseIdFieldsContainFileReleasePackageName
     */
    public void setReleaseIdFieldsContainFileReleasePackageName(
            boolean releaseIdFieldsContainFileReleasePackageName) {
        this.releaseIdFieldsContainFileReleasePackageName = releaseIdFieldsContainFileReleasePackageName;
    }

    /**
     * Sets the optional resync password that belongs to the resync user
     * 
     * @param resyncPassword
     *            the resyncPassword to set
     */
    public void setResyncPassword(String resyncPassword) {
        this.resyncPassword = Obfuscator.deObfuscatePassword(resyncPassword);
    }

    /**
     * Sets the optional resync username
     * 
     * The resync user name is used to login into the TF/CSFE instance whenever
     * an artifact should be created. This user has to differ from the ordinary
     * user used to log in in order to force initial resyncs with the source
     * system once a new artifact has been created.
     * 
     * @param resyncUserName
     *            the resyncUserName to set
     */
    public void setResyncUserName(String resyncUserName) {
        this.resyncUserName = resyncUserName;
    }

    /**
     * Sets the CSFE/TF system's SOAP server URL.
     * 
     * @param serverUrl
     *            - the URL of the source TF system.
     */
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setTranslateTechnicalReleaseIds(
            boolean translateTechnicalReleaseIds) {
        this.translateTechnicalReleaseIds = translateTechnicalReleaseIds;
    }

    /**
     * Set the update comment
     * 
     * @param updateComment
     *            see private attribute doc
     */
    public void setUpdateComment(String updateComment) {
        this.updateComment = updateComment;
    }

    /**
     * Sets the mandatory username
     * 
     * The user name is used to login into the TF/CSFE instance whenever an
     * artifact should be updated or extracted. This user has to differ from the
     * resync user in order to force initial resyncs with the source system once
     * a new artifact has been created.
     * 
     * @param resyncUserName
     *            the resyncUserName to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Document updateArtifact(Document data) {
        GenericArtifact ga = null;
        try {
            ga = GenericArtifactHelper.createGenericArtifactJavaObject(data);
        } catch (GenericArtifactParsingException e) {
            String cause = "Problem occured while parsing the GenericArtifact into Document";
            log.error(cause, e);
            XPathUtils.addAttribute(data.getRootElement(),
                    GenericArtifactHelper.ERROR_CODE,
                    GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
            throw new CCFRuntimeException(cause, e);
        }
        Connection connection = connect(ga);
        try {
            String targetRepositoryId = ga.getTargetRepositoryId();
            if (TFConnectionFactory.isTrackerRepository(targetRepositoryId)) {
                this.initializeArtifact(ga);

                String tracker = targetRepositoryId;

                ArtifactDO result = null;
                try {
                    // update and do conflict resolution
                    result = this.updateArtifact(ga, tracker, connection);
                } catch (NumberFormatException e) {
                    String cause = "Number format exception while trying to extract the field data.";
                    log.error(cause, e);
                    XPathUtils.addAttribute(data.getRootElement(),
                            GenericArtifactHelper.ERROR_CODE,
                            GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
                    throw new CCFRuntimeException(cause, e);
                }
                // otherwise a conflict has happened and the generic artifact
                // has been
                // already prepared
                if (result != null) {
                    this.populateTargetArtifactAttributes(ga, result);
                }
                return this.returnDocument(ga);
            } else if (TFConnectionFactory
                    .isPlanningFolderRepository(targetRepositoryId)) {
                // we write planning folders, so first do a check whether we
                // support this feature
                if (!connection.supports53()) {
                    // we do not support planning folders
                    throw new CCFRuntimeException(
                            "Planning Folders are not supported since TeamForge target system does not provide them.");
                }

                PlanningFolderDO result = null;
                try {
                    // update and do conflict resolution
                    String project = TFConnectionFactory
                            .extractProjectFromRepositoryId(targetRepositoryId);
                    result = updatePlanningFolder(ga, project, connection);
                } catch (NumberFormatException e) {
                    String cause = "Number format exception while trying to extract the field data.";
                    log.error(cause, e);
                    XPathUtils.addAttribute(data.getRootElement(),
                            GenericArtifactHelper.ERROR_CODE,
                            GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
                    throw new CCFRuntimeException(cause, e);
                } catch (RemoteException e) {
                    String cause = "While trying to update a planning folder within TF, an error occured";
                    log.error(cause, e);
                    ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
                    throw new CCFRuntimeException(cause, e);
                } catch (PlanningFolderRuleViolationException e) {
                    String cause = "While trying to move a planning folder within TF, a planning folder rule violation occured";
                    log.error(cause, e);
                    ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
                    throw new CCFRuntimeException(cause, e);
                }
                // otherwise a conflict has happened and the generic artifact
                // has been
                // already prepared
                if (result != null) {
                    populateTargetArtifactAttributesFromPlanningFolder(ga,
                            result);
                }
                return returnDocument(ga);
            } else if (TFConnectionFactory
                    .isTrackerMetaDataRepository(targetRepositoryId)) {
                TrackerDO result = null;
                String trackerId = TFConnectionFactory
                        .extractTrackerFromMetaDataRepositoryId(targetRepositoryId);
                result = updateTrackerMetaData(ga, trackerId, connection);
                populateTargetArtifactAttributesFromTracker(ga, result);
                return returnDocument(ga);
            } else {
                throw new CCFRuntimeException("Unknown repository id format: "
                        + targetRepositoryId);
            }
        } finally {
            disconnect(connection);
        }
    }

    @Override
    public Document updateAttachment(Document gaDocument) {
        // throw new
        // CCFRuntimeException("updateAttachment is not implemented...!");
        log.warn("updateAttachment is not implemented...!");
        return null;
    }

    @Override
    public Document updateDependency(Document gaDocument) {
        // throw new
        // CCFRuntimeException("updateDependency is not implemented...!");
        log.warn("updateDependency is not implemented...!");
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    /*
     * Validate whether all mandatory properties are set correctly
     */
    public void validate(List exceptions) {
        super.validate(exceptions);

        /*
         * if (getResyncUserName() == null) { log .warn(
         * "resyncUserName-property has not been set, so that initial resyncs after artifact creation are not possible."
         * ); }
         */

        if (getUpdateComment() == null) {
            log.error("updateComment-property not set");
            exceptions.add(new ValidationException(
                    "updateComment-property not set", this));
        }

        if (getPassword() == null) {
            log.error("password-property not set");
            exceptions.add(new ValidationException("password-property not set",
                    this));
        }

        if (getUsername() == null) {
            log.error("userName-property not set");
            exceptions.add(new ValidationException("userName-property not set",
                    this));
        }

        if (getServerUrl() == null) {
            log.error("serverUrl-property not set");
            exceptions.add(new ValidationException(
                    "serverUrl-property not set", this));
        }

        ConnectionManager<Connection> connectionManager = getConnectionManager();

        if (exceptions.size() == 0) {
            trackerHandler = new TFTrackerHandler(getServerUrl(),
                    connectionManager);
            attachmentHandler = new TFAttachmentHandler(getServerUrl(),
                    connectionManager);
        }
    }

    /**
     * Creates the artifact represented by the GenericArtifact object on the
     * target TF system
     * 
     * @param ga
     *            - the GenericArtifact object
     * @param tracker
     *            - The target TF tracker ID
     * @param connection
     *            - The Connection object for the target TF system
     * @return - the newly created artifact's ArtifactSoapDO object
     */
    private ArtifactDO createArtifact(GenericArtifact ga, String tracker,
            Connection connection) {
        ArrayList<String> flexFieldNames = new ArrayList<String>();
        ArrayList<String> flexFieldTypes = new ArrayList<String>();
        ArrayList<Object> flexFieldValues = new ArrayList<Object>();

        List<GenericArtifactField> gaFields = ga
                .getAllGenericArtifactFieldsWithSameFieldType(GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
        String targetSystemTimezone = ga.getTargetSystemTimezone();
        if (gaFields != null) {
            for (GenericArtifactField gaField : gaFields) {
                String fieldName = gaField.getFieldName();
                if (COMMENT_TEXT.equals(fieldName)) {
                    continue;
                }
                String trackerFieldValueType = TFArtifactMetaData
                        .getTFFieldValueTypeForGAFieldType(gaField
                                .getFieldValueType());
                flexFieldNames.add(fieldName);
                flexFieldTypes.add(trackerFieldValueType);
                Object value = null;
                FieldValueTypeValue fieldType = gaField.getFieldValueType();
                if (trackerFieldValueType
                        .equals(TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE)) {
                    if (fieldType == FieldValueTypeValue.DATE) {
                        GregorianCalendar gc = (GregorianCalendar) gaField
                                .getFieldValue();
                        if (gc != null) {
                            Date dateValue = gc.getTime();
                            if (DateUtil.isAbsoluteDateInTimezone(dateValue,
                                    "GMT")) {
                                value = DateUtil
                                        .convertGMTToTimezoneAbsoluteDate(
                                                dateValue, targetSystemTimezone);
                            } else {
                                value = dateValue;
                            }
                        }
                    } else if (fieldType == FieldValueTypeValue.DATETIME) {
                        value = gaField.getFieldValue();
                    }
                } else {
                    value = gaField.getFieldValue();
                }
                flexFieldValues.add(value);
            }
        }

        String folderId = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.folderId.getFieldName(), ga);
        String description = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.description.getFieldName(), ga);
        String category = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.category.getFieldName(), ga);
        String group = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.group.getFieldName(), ga);
        String status = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.status.getFieldName(), ga);
        String statusClass = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.statusClass.getFieldName(), ga);
        String customer = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.customer.getFieldName(), ga);
        int priority = GenericArtifactHelper.getIntMandatoryGAField(
                TFArtifactMetaData.TFFields.priority.getFieldName(), ga);

        int estimatedEffort = 0;
        int actualEffort = 0;
        int remainingEffort = 0;
        String planningFolder = null;
        Boolean autosumming = false;

        estimatedEffort = GenericArtifactHelper.getIntMandatoryGAField(
                TFArtifactMetaData.TFFields.estimatedHours.getFieldName(), ga);
        actualEffort = GenericArtifactHelper.getIntMandatoryGAField(
                TFArtifactMetaData.TFFields.actualHours.getFieldName(), ga);
        if (connection.supports53()) {
            remainingEffort = GenericArtifactHelper.getIntMandatoryGAField(
                    TFArtifactMetaData.TFFields.remainingEffort.getFieldName(),
                    ga);

            planningFolder = GenericArtifactHelper.getStringMandatoryGAField(
                    TFArtifactMetaData.TFFields.planningFolder.getFieldName(),
                    ga);

            autosumming = GenericArtifactHelper.getBooleanMandatoryGAField(
                    TFArtifactMetaData.TFFields.autosumming.getFieldName(), ga);
        }

        int points = 0;
        if (connection.supports54()) {
            points = GenericArtifactHelper.getIntMandatoryGAField(
                    TFArtifactMetaData.TFFields.points.getFieldName(), ga);

        }

        Date closeDate = GenericArtifactHelper.getDateMandatoryGAField(
                TFArtifactMetaData.TFFields.closeDate.getFieldName(), ga);
        String assignedTo = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.assignedTo.getFieldName(), ga);
        String reportedReleaseId = GenericArtifactHelper
                .getStringMandatoryGAField(
                        TFArtifactMetaData.TFFields.reportedReleaseId
                                .getFieldName(), ga);
        String resolvedReleaseId = GenericArtifactHelper
                .getStringMandatoryGAField(
                        TFArtifactMetaData.TFFields.resolvedReleaseId
                                .getFieldName(), ga);
        String title = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.title.getFieldName(), ga);
        String[] comments = this.getComments(ga);
        ArtifactDO result = null;
        try {
            if (this.translateTechnicalReleaseIds) {
                reportedReleaseId = TFTrackerHandler
                        .convertReleaseId(
                                connection,
                                reportedReleaseId,
                                folderId,
                                isReleaseIdFieldsContainFileReleasePackageName() ? getPackageReleaseSeparatorString()
                                        : null);
                resolvedReleaseId = TFTrackerHandler
                        .convertReleaseId(
                                connection,
                                resolvedReleaseId,
                                folderId,
                                isReleaseIdFieldsContainFileReleasePackageName() ? getPackageReleaseSeparatorString()
                                        : null);
            }

            // now we have to deal with the parent dependencies
            String parentId = ga.getDepParentTargetArtifactId();
            boolean associateWithParent = false;
            if (parentId != null
                    && !parentId.equals(GenericArtifact.VALUE_UNKNOWN)
                    && !parentId.equals(GenericArtifact.VALUE_NONE)) {
                // parent is a planning folder
                if (parentId.startsWith("plan")) {
                    planningFolder = parentId;
                } else {
                    associateWithParent = true;
                    if (planningFolder == null) {
                        ArtifactDO parentArtifact = connection
                                .getTrackerClient().getArtifactData(parentId);
                        planningFolder = parentArtifact.getPlanningFolderId();
                    }

                }
            }

            result = trackerHandler.createArtifact(connection, folderId,
                    description, category, group, status, statusClass,
                    customer, priority, estimatedEffort, actualEffort,
                    closeDate, assignedTo, reportedReleaseId,
                    resolvedReleaseId, flexFieldNames, flexFieldValues,
                    flexFieldTypes, title, comments, remainingEffort,
                    autosumming, planningFolder, points);

            // now create parent dependency
            if (associateWithParent) {
                trackerHandler.createArtifactDependency(connection, parentId,
                        result.getId(), "CCF created parent-child dependency");
            }

            log.info("New artifact " + result.getId()
                    + " is created with the change from "
                    + ga.getSourceArtifactId());
        } catch (RemoteException e) {
            String cause = "While trying to create an artifact within TF, an error occured";
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        } catch (PlanningFolderRuleViolationException e) {
            String cause = "While trying to create an artifact within TF, a planning folder rule violation occured: "
                    + e.getMessage();
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        }
        return result;
    }

    private PlanningFolderDO createPlanningFolder(GenericArtifact ga,
            String project, Connection connection) {

        String targetSystemTimezone = ga.getTargetSystemTimezone();
        String parentId = project;
        String targetParentArtifactId = ga.getDepParentTargetArtifactId();

        if (targetParentArtifactId != null
                && !targetParentArtifactId.equals(GenericArtifact.VALUE_NONE)
                && !targetParentArtifactId
                        .equals(GenericArtifact.VALUE_UNKNOWN)) {
            parentId = targetParentArtifactId;
        }

        String title = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.title.getFieldName(), ga);

        String description = GenericArtifactHelper.getStringMandatoryGAField(
                TFArtifactMetaData.TFFields.description.getFieldName(), ga);

        Date startDate = null;
        Date endDate = null;

        GenericArtifactField startDateField = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.startDate.getFieldName(),
                        ga);

        if (startDateField != null) {
            GregorianCalendar gc = (GregorianCalendar) startDateField
                    .getFieldValue();
            if (gc != null) {
                Date dateValue = gc.getTime();
                if (DateUtil.isAbsoluteDateInTimezone(dateValue, "GMT")) {
                    startDate = DateUtil.convertGMTToTimezoneAbsoluteDate(
                            dateValue, targetSystemTimezone);
                } else {
                    startDate = dateValue;
                }
            }
        }

        GenericArtifactField endDateField = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.endDate.getFieldName(), ga);

        if (endDateField != null) {
            GregorianCalendar gc = (GregorianCalendar) endDateField
                    .getFieldValue();
            if (gc != null) {
                Date dateValue = gc.getTime();
                if (DateUtil.isAbsoluteDateInTimezone(dateValue, "GMT")) {
                    endDate = DateUtil.convertGMTToTimezoneAbsoluteDate(
                            dateValue, targetSystemTimezone);
                } else {
                    endDate = dateValue;
                }
            }
        }

        int capacity = 0;
        String status = null;
        String releaseId = null;
        if (connection.supports54()) {
            capacity = GenericArtifactHelper.getIntMandatoryGAField(
                    TFArtifactMetaData.TFFields.capacity.getFieldName(), ga);
            status = GenericArtifactHelper.getStringMandatoryGAField(
                    TFArtifactMetaData.TFFields.status.getFieldName(), ga);
            releaseId = GenericArtifactHelper.getStringMandatoryGAField(
                    TFArtifactMetaData.TFFields.releaseId.getFieldName(), ga);
        }

        //To support display effort in field added in TF 7.0
        String trackerUnitId = null;
        if (connection.supports62()) {
            trackerUnitId = GenericArtifactHelper.getStringMandatoryGAField(
                    TFArtifactMetaData.TFFields.trackerUnitId.getFieldName(),
                    ga);

        }

        //For points capacity field added in TF 7.1
        int pointsCapacity = 0;
        if (connection.supports63()) {
            pointsCapacity = GenericArtifactHelper.getIntMandatoryGAField(
                    TFArtifactMetaData.TFFields.pointsCapacity.getFieldName(),
                    ga);
        }

        PlanningFolderDO planningFolder = null;

        try {
            if (translateTechnicalReleaseIds) {
                releaseId = TFTrackerHandler
                        .convertReleaseIdForProject(
                                connection,
                                releaseId,
                                project,
                                isReleaseIdFieldsContainFileReleasePackageName() ? getPackageReleaseSeparatorString()
                                        : null);
            }
            String trackerUnitIdValue = !StringUtils.isEmpty(trackerUnitId) ? trackerUnitId
                    : "Hours";
            trackerUnitId = TFTrackerHandler.getTrackerUnitId(connection,
                    trackerUnitIdValue, project);
            planningFolder = connection.getPlanningClient()
                    .createPlanningFolder(parentId, title, description,
                            startDate, endDate, status, capacity,
                            pointsCapacity, releaseId, trackerUnitId);
        } catch (RemoteException e) {
            String cause = "Could not create planning folder: "
                    + e.getMessage();
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        }
        log.info("Created planning folder " + planningFolder.getId()
                + " in project " + project + " with parent " + parentId
                + " other system id: " + ga.getSourceArtifactId()
                + " in repository: " + ga.getSourceRepositoryId());

        return planningFolder;
    }

    private void disconnect(Connection connection) {
        ConnectionManager<Connection> connectionManager = (ConnectionManager<Connection>) getConnectionManager();
        connectionManager.releaseConnection(connection);
    }

    private String[] getComments(GenericArtifact ga) {
        String[] comments = null;
        List<GenericArtifactField> gaFields = ga
                .getAllGenericArtifactFieldsWithSameFieldTypeAndFieldName(
                        GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD,
                        TFArtifactMetaData.TFFields.commentText.getFieldName());
        int commentsSize = 0;
        if (gaFields != null) {
            commentsSize = gaFields.size();
        }
        if (commentsSize > 0) {
            comments = new String[commentsSize];
            for (int i = 0; i < commentsSize; i++) {
                GenericArtifactField field = gaFields.get(i);
                String comment = (String) field.getFieldValue();
                comments[i] = comment;
            }
        }
        if (comments == null) {
            comments = new String[0];
        }
        return comments;
    }

    /**
     * Gets the mandatory password that belongs to the username
     * 
     * @return the password
     */
    private String getPassword() {
        return password;
    }

    /**
     * Gets the optional resync password that belongs to the resync user
     * 
     * @return the resyncPassword
     */
    private String getResyncPassword() {
        return resyncPassword;
    }

    /**
     * Gets the optional resync username The resync user name is used to login
     * into the TF/CSFE instance whenever an artifact should be created. This
     * user has to differ from the ordinary user used to log in in order to
     * force initial resyncs with the source system once a new artifact has been
     * created.
     * 
     * @return the resyncUserName
     */
    private String getResyncUserName() {
        return resyncUserName;
    }

    private void initializeArtifact(GenericArtifact ga) {
        GenericArtifact.ArtifactActionValue artifactAction = ga
                .getArtifactAction();
        // GenericArtifact.ArtifactTypeValue artifactType =
        // ga.getArtifactType();
        // String sourceArtifactId = ga.getSourceArtifactId();
        String targetRepositoryId = ga.getTargetRepositoryId();
        String targetArtifactId = ga.getTargetArtifactId();
        String tracker = targetRepositoryId;

        if (artifactAction == GenericArtifact.ArtifactActionValue.UPDATE) {
            if (TFGAHelper.containsSingleMandatoryField(ga,
                    TFArtifactMetaData.TFFields.id.getFieldName())) {
                TFGAHelper.updateSingleMandatoryField(ga,
                        TFArtifactMetaData.TFFields.id.getFieldName(),
                        targetArtifactId);
                ga.setTargetArtifactId(targetArtifactId);
            } else {
                TFGAHelper.addField(ga,
                        TFArtifactMetaData.TFFields.id.getFieldName(),
                        targetArtifactId,
                        GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
                        GenericArtifactField.FieldValueTypeValue.STRING);
            }
        }
        if (TFGAHelper.containsSingleMandatoryField(ga,
                TFArtifactMetaData.TFFields.folderId.getFieldName())) {
            TFGAHelper.updateSingleMandatoryField(ga,
                    TFArtifactMetaData.TFFields.folderId.getFieldName(),
                    tracker);
        } else {
            TFGAHelper.addField(ga,
                    TFArtifactMetaData.TFFields.folderId.getFieldName(),
                    tracker,
                    GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
                    GenericArtifactField.FieldValueTypeValue.STRING);
        }
    }

    private void populateTargetArtifactAttributes(GenericArtifact ga,
            ArtifactDO result) {
        ga.setTargetArtifactId(result.getId());
        Date targetArtifactLastModifiedDate = result.getLastModifiedDate();
        String targetArtifactLastModifiedDateStr = DateUtil
                .format(targetArtifactLastModifiedDate);
        ga.setTargetArtifactLastModifiedDate(targetArtifactLastModifiedDateStr);
        ga.setTargetArtifactVersion(Integer.toString(result.getVersion()));
    }

    private void populateTargetArtifactAttributesFromPlanningFolder(
            GenericArtifact ga, PlanningFolderDO result) {
        ga.setTargetArtifactId(result.getId());
        Date targetArtifactLastModifiedDate = result.getLastModifiedDate();
        String targetArtifactLastModifiedDateStr = DateUtil
                .format(targetArtifactLastModifiedDate);
        ga.setTargetArtifactLastModifiedDate(targetArtifactLastModifiedDateStr);
        ga.setTargetArtifactVersion(Integer.toString(result.getVersion()));
    }

    private void populateTargetArtifactAttributesFromTracker(
            GenericArtifact ga, TrackerDO result) {
        ga.setTargetArtifactId(result.getId());
        Date targetArtifactLastModifiedDate = result.getLastModifiedDate();
        String targetArtifactLastModifiedDateStr = DateUtil
                .format(targetArtifactLastModifiedDate);
        ga.setTargetArtifactLastModifiedDate(targetArtifactLastModifiedDateStr);
        ga.setTargetArtifactVersion(Integer.toString(result.getVersion()));
    }

    /**
     * Creates the artifact represented by the GenericArtifact object on the
     * target TF system
     * 
     * @param ga
     * @param tracker
     * @param forceOverride
     * @param connection
     * @return - returns the updated artifact's ArtifactSoapDO object
     */
    private ArtifactDO updateArtifact(GenericArtifact ga, String tracker,
            Connection connection) {
        String id = ga.getTargetArtifactId();
        ArrayList<String> flexFieldNames = new ArrayList<String>();
        ArrayList<String> flexFieldTypes = new ArrayList<String>();
        ArrayList<Object> flexFieldValues = new ArrayList<Object>();
        Set<String> overriddenFlexFields = new HashSet<String>();
        List<GenericArtifactField> gaFields = ga
                .getAllGenericArtifactFieldsWithSameFieldType(GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
        String targetSystemTimezone = ga.getTargetSystemTimezone();
        if (gaFields != null) {
            for (GenericArtifactField gaField : gaFields) {
                String fieldName = gaField.getFieldName();
                if (COMMENT_TEXT.equals(fieldName)) {
                    continue;
                }
                if (gaField.getFieldValueHasChanged()) {
                    overriddenFlexFields.add(fieldName);
                } else {
                    continue;
                }
                String trackerFieldValueType = TFArtifactMetaData
                        .getTFFieldValueTypeForGAFieldType(gaField
                                .getFieldValueType());
                flexFieldNames.add(fieldName);
                flexFieldTypes.add(trackerFieldValueType);
                Object value = null;
                FieldValueTypeValue fieldType = gaField.getFieldValueType();
                if (trackerFieldValueType
                        .equals(TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE)) {
                    if (fieldType == FieldValueTypeValue.DATE) {
                        GregorianCalendar gc = (GregorianCalendar) gaField
                                .getFieldValue();
                        if (gc != null) {
                            Date dateValue = gc.getTime();
                            if (DateUtil.isAbsoluteDateInTimezone(dateValue,
                                    "GMT")) {
                                value = DateUtil
                                        .convertGMTToTimezoneAbsoluteDate(
                                                dateValue, targetSystemTimezone);
                            } else {
                                value = dateValue;
                            }
                        }
                    } else if (fieldType == FieldValueTypeValue.DATETIME) {
                        value = gaField.getFieldValue();
                    }
                } else {
                    value = gaField.getFieldValue();
                }
                flexFieldValues.add(value);
            }
        }

        GenericArtifactField folderId = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.folderId.getFieldName(), ga);
        GenericArtifactField description = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.description.getFieldName(),
                        ga);
        GenericArtifactField category = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.category.getFieldName(), ga);
        GenericArtifactField group = GenericArtifactHelper.getMandatoryGAField(
                TFArtifactMetaData.TFFields.group.getFieldName(), ga);
        GenericArtifactField status = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.status.getFieldName(), ga);
        GenericArtifactField statusClass = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.statusClass.getFieldName(),
                        ga);
        GenericArtifactField customer = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.customer.getFieldName(), ga);
        GenericArtifactField priority = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.priority.getFieldName(), ga);

        GenericArtifactField estimatedEffort = null;
        GenericArtifactField actualEffort = null;
        GenericArtifactField remainingEffort = null;
        GenericArtifactField planningFolder = null;
        GenericArtifactField autosumming = null;

        estimatedEffort = GenericArtifactHelper.getMandatoryGAField(
                TFArtifactMetaData.TFFields.estimatedHours.getFieldName(), ga);
        actualEffort = GenericArtifactHelper.getMandatoryGAField(
                TFArtifactMetaData.TFFields.actualHours.getFieldName(), ga);

        if (connection.supports53()) {
            remainingEffort = GenericArtifactHelper.getMandatoryGAField(
                    TFArtifactMetaData.TFFields.remainingEffort.getFieldName(),
                    ga);
            planningFolder = GenericArtifactHelper.getMandatoryGAField(
                    TFArtifactMetaData.TFFields.planningFolder.getFieldName(),
                    ga);
            autosumming = GenericArtifactHelper.getMandatoryGAField(
                    TFArtifactMetaData.TFFields.autosumming.getFieldName(), ga);
        }

        GenericArtifactField storyPoints = null;
        if (connection.supports54()) {
            storyPoints = GenericArtifactHelper.getMandatoryGAField(
                    TFArtifactMetaData.TFFields.points.getFieldName(), ga);
        }

        GenericArtifactField closeDate = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.closeDate.getFieldName(),
                        ga);
        GenericArtifactField assignedTo = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.assignedTo.getFieldName(),
                        ga);
        GenericArtifactField reportedReleaseId = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.reportedReleaseId
                                .getFieldName(), ga);
        GenericArtifactField resolvedReleaseId = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.resolvedReleaseId
                                .getFieldName(), ga);
        GenericArtifactField title = GenericArtifactHelper.getMandatoryGAField(
                TFArtifactMetaData.TFFields.title.getFieldName(), ga);

        String[] comments = this.getComments(ga);
        ArtifactDO result = null;
        try {
            // now we have to deal with the parent dependencies
            String newParentId = ga.getDepParentTargetArtifactId();
            boolean associateWithParent = false;
            boolean deleteOldParentAssociation = false;
            String currentParentId = null;

            if (newParentId != null
                    && !newParentId.equals(GenericArtifact.VALUE_UNKNOWN)) {
                // now find out current parent id
                ArtifactDependencyRow[] parents = trackerHandler
                        .getArtifactParentDependencies(connection, id);
                if (parents.length != 0) {
                    // only take first entry of this record
                    ArtifactDependencyRow parent = parents[0];
                    currentParentId = parent.getOriginId();
                }

                if (newParentId.equals(GenericArtifact.VALUE_NONE)) {
                    // we have to set the planning folder to null
                    planningFolder = TFToGenericArtifactConverter
                            .createGenericArtifactField(
                                    TFArtifactMetaData.TFFields.planningFolder,
                                    null, ga, null, false);

                    // we have to deassociate the old parent
                    if (currentParentId != null) {
                        deleteOldParentAssociation = true;
                    }
                }

                else if (newParentId.startsWith("plan")) {
                    // we have to set the planning folder to the new parent
                    planningFolder = TFToGenericArtifactConverter
                            .createGenericArtifactField(
                                    TFArtifactMetaData.TFFields.planningFolder,
                                    newParentId, ga, null, false);

                    // we have to deassociate the old parent
                    if (currentParentId != null) {
                        deleteOldParentAssociation = true;
                    }
                }

                else if (!newParentId.equals(currentParentId)) {
                    // new parent is a tracker item
                    // we have to deassociate the old parent
                    if (currentParentId != null) {
                        deleteOldParentAssociation = true;
                    }
                    associateWithParent = true;
                    // we have to change the planning folder if there was no
                    // special value requested for this
                    if (connection.supports53()
                            && (planningFolder == null || !planningFolder
                                    .getFieldValueHasChanged())) {
                        ArtifactDO parentArtifact = trackerHandler
                                .getTrackerItem(connection, newParentId);
                        planningFolder = TFToGenericArtifactConverter
                                .createGenericArtifactField(
                                        TFArtifactMetaData.TFFields.planningFolder,
                                        parentArtifact.getPlanningFolderId(),
                                        ga, null, false);
                    }
                }
            }

            result = trackerHandler
                    .updateArtifact(
                            ga,
                            connection,
                            folderId,
                            description,
                            category,
                            group,
                            status,
                            statusClass,
                            customer,
                            priority,
                            estimatedEffort,
                            actualEffort,
                            closeDate,
                            assignedTo,
                            reportedReleaseId,
                            resolvedReleaseId,
                            flexFieldNames,
                            flexFieldValues,
                            flexFieldTypes,
                            overriddenFlexFields,
                            title,
                            id,
                            comments,
                            translateTechnicalReleaseIds,
                            remainingEffort,
                            autosumming,
                            storyPoints,
                            planningFolder,
                            deleteOldParentAssociation,
                            currentParentId,
                            associateWithParent,
                            newParentId,
                            isReleaseIdFieldsContainFileReleasePackageName() ? getPackageReleaseSeparatorString()
                                    : null);

            if (result != null) {
                log.info("Artifact " + id + " is updated successfully");
            }
        } catch (RemoteException e) {
            String cause = "While trying to update an artifact within TF, an error occured";
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        } catch (PlanningFolderRuleViolationException e) {
            String cause = "While trying to update an artifact within TF, a planning folder rule violation occured: "
                    + e.getMessage();
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        }
        return result;
    }

    private PlanningFolderDO updatePlanningFolder(GenericArtifact ga,
            String project, Connection connection) throws RemoteException,
            PlanningFolderRuleViolationException {

        String id = ga.getTargetArtifactId();

        String targetSystemTimezone = ga.getTargetSystemTimezone();

        GenericArtifactField description = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.description.getFieldName(),
                        ga);

        GenericArtifactField title = GenericArtifactHelper.getMandatoryGAField(
                TFArtifactMetaData.TFFields.title.getFieldName(), ga);

        GenericArtifactField statusField = null;
        GenericArtifactField releaseIdField = null;
        GenericArtifactField capacityField = null;
        GenericArtifactField pointsCapacityField = null;
        GenericArtifactField trackerUnitIdField = null;

        if (connection.supports54()) {
            statusField = GenericArtifactHelper.getMandatoryGAField(
                    TFArtifactMetaData.TFFields.status.getFieldName(), ga);
            releaseIdField = GenericArtifactHelper.getMandatoryGAField(
                    TFArtifactMetaData.TFFields.releaseId.getFieldName(), ga);
            capacityField = GenericArtifactHelper.getMandatoryGAField(
                    TFArtifactMetaData.TFFields.capacity.getFieldName(), ga);
        }

        if (connection.supports62()) {
            trackerUnitIdField = GenericArtifactHelper.getMandatoryGAField(
                    TFArtifactMetaData.TFFields.trackerUnitId.getFieldName(),
                    ga);
        }

        if (connection.supports63()) {
            pointsCapacityField = GenericArtifactHelper.getMandatoryGAField(
                    TFArtifactMetaData.TFFields.pointsCapacity.getFieldName(),
                    ga);
        }

        GenericArtifactField startDateField = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.startDate.getFieldName(),
                        ga);

        if (startDateField != null && startDateField.getFieldValueHasChanged()) {
            GregorianCalendar gc = (GregorianCalendar) startDateField
                    .getFieldValue();
            if (gc != null) {
                Date dateValue = gc.getTime();
                if (DateUtil.isAbsoluteDateInTimezone(dateValue, "GMT")) {
                    startDateField.setFieldValue(DateUtil
                            .convertGMTToTimezoneAbsoluteDate(dateValue,
                                    targetSystemTimezone));
                } else {
                    startDateField.setFieldValue(dateValue);
                }
            }
        }

        GenericArtifactField endDateField = GenericArtifactHelper
                .getMandatoryGAField(
                        TFArtifactMetaData.TFFields.endDate.getFieldName(), ga);

        if (endDateField != null && endDateField.getFieldValueHasChanged()) {
            GregorianCalendar gc = (GregorianCalendar) endDateField
                    .getFieldValue();
            if (gc != null) {
                Date dateValue = gc.getTime();
                if (DateUtil.isAbsoluteDateInTimezone(dateValue, "GMT")) {
                    endDateField.setFieldValue(DateUtil
                            .convertGMTToTimezoneAbsoluteDate(dateValue,
                                    targetSystemTimezone));
                } else {
                    endDateField.setFieldValue(dateValue);
                }
            }
        }

        boolean planningFolderNotUpdated = true;
        PlanningFolderDO planningFolder = null;
        while (planningFolderNotUpdated) {
            try {
                planningFolderNotUpdated = false;
                planningFolder = connection.getPlanningClient()
                        .getPlanningFolderData(id);

                // do conflict resolution
                if (!AbstractWriter.handleConflicts(
                        planningFolder.getVersion(), ga)) {
                    return null;
                }

                if (title != null && title.getFieldValueHasChanged()) {
                    planningFolder.setTitle((String) title.getFieldValue());
                }
                if (description != null
                        && description.getFieldValueHasChanged()) {
                    planningFolder.setDescription((String) description
                            .getFieldValue());
                }
                if (startDateField != null
                        && startDateField.getFieldValueHasChanged()) {
                    planningFolder.setStartDate((Date) startDateField
                            .getFieldValue());
                }
                if (endDateField != null
                        && endDateField.getFieldValueHasChanged()) {
                    planningFolder.setEndDate((Date) endDateField
                            .getFieldValue());
                }

                if (statusField != null
                        && statusField.getFieldValueHasChanged()) {
                    planningFolder.setStatus((String) statusField
                            .getFieldValue());
                }

                if (releaseIdField != null
                        && releaseIdField.getFieldValueHasChanged()) {
                    String releaseId = (String) releaseIdField.getFieldValue();
                    if (translateTechnicalReleaseIds) {
                        releaseId = TFTrackerHandler
                                .convertReleaseIdForProject(
                                        connection,
                                        releaseId,
                                        project,
                                        isReleaseIdFieldsContainFileReleasePackageName() ? getPackageReleaseSeparatorString()
                                                : null);
                    }

                    planningFolder.setReleaseId(releaseId);
                }

                if (capacityField != null
                        && capacityField.getFieldValueHasChanged()) {
                    Object fieldValueObj = capacityField.getFieldValue();
                    int fieldValue = 0;
                    if (fieldValueObj instanceof String) {
                        String fieldValueString = (String) fieldValueObj;
                        try {
                            fieldValue = Integer.parseInt(fieldValueString);
                        } catch (NumberFormatException e) {
                            throw new CCFRuntimeException(
                                    "Could not parse value of mandatory field capacity: "
                                            + e.getMessage(), e);
                        }
                    } else if (fieldValueObj instanceof Integer) {
                        fieldValue = ((Integer) fieldValueObj).intValue();
                    }
                    planningFolder.setCapacity(fieldValue);

                }

                if (pointsCapacityField != null
                        && pointsCapacityField.getFieldValueHasChanged()) {
                    Object fieldValueObj = pointsCapacityField.getFieldValue();
                    int fieldValue = 0;
                    if (fieldValueObj instanceof String) {
                        String fieldValueString = (String) fieldValueObj;
                        try {
                            fieldValue = Integer.parseInt(fieldValueString);
                        } catch (NumberFormatException e) {
                            throw new CCFRuntimeException(
                                    "Could not parse value of mandatory field points capacity: "
                                            + e.getMessage(), e);
                        }
                    } else if (fieldValueObj instanceof Integer) {
                        fieldValue = ((Integer) fieldValueObj).intValue();
                    }
                    planningFolder.setPointsCapacity(fieldValue);

                    if (trackerUnitIdField != null
                            && trackerUnitIdField.getFieldValueHasChanged()) {
                        String trackerUnitId = (String) trackerUnitIdField
                                .getFieldValue();
                        String trackerUnitIdValue = !StringUtils
                                .isEmpty(trackerUnitId) ? trackerUnitId
                                : "Hours";
                        trackerUnitId = TFTrackerHandler.getTrackerUnitId(
                                connection, trackerUnitIdValue, project);
                        planningFolder.setTrackerUnitId(trackerUnitId);
                    }

                }
                connection.getPlanningClient().setPlanningFolderData(
                        planningFolder);
            } catch (AxisFault e) {
                javax.xml.namespace.QName faultCode = e.getFaultCode();
                if (!faultCode.getLocalPart().equals("VersionMismatchFault")) {
                    throw e;
                }
                logConflictResolutor.warn(
                        "Stale update for TF planning folder " + id
                                + " in project " + project
                                + ". Trying again ...", e);
                planningFolderNotUpdated = true;
            }
        }

        planningFolder = connection.getPlanningClient().getPlanningFolderData(
                id);

        // now we have to cope with moving planning folders around
        String parentArtifactId = ga.getDepParentTargetArtifactId();
        // first of all, if parent is unknown or null, we do not change anything
        if (parentArtifactId != null
                && !parentArtifactId.equals(GenericArtifact.VALUE_UNKNOWN)) {
            // check for the special case this is a top level PF
            if (parentArtifactId.equals(GenericArtifact.VALUE_NONE)) {
                // check whether this is already a top level planning folder
                if (!planningFolder.getParentFolderId().startsWith(
                        "PlanningApp")) {
                    // move to top
                    connection.getPlanningClient().movePlanningFolder(id,
                            project);
                    planningFolder = connection.getPlanningClient()
                            .getPlanningFolderData(id);
                }
            } else {
                // check whether correct parent is already assigned
                if (!parentArtifactId
                        .equals(planningFolder.getParentFolderId())) {
                    connection.getPlanningClient().movePlanningFolder(id,
                            parentArtifactId);
                    planningFolder = connection.getPlanningClient()
                            .getPlanningFolderData(id);
                }
            }
        }
        log.info("Planning folder updated. TF Id: " + planningFolder.getId()
                + " in project " + project + " other system id: "
                + ga.getSourceArtifactId() + " in repository: "
                + ga.getSourceRepositoryId());
        return planningFolder;
    }

    private TrackerDO updateTrackerMetaData(GenericArtifact ga,
            String trackerId, Connection connection) {
        Map<String, SortedSet<String>> fieldsToBeChanged = new HashMap<String, SortedSet<String>>();
        // retrieve all fields that should be changed
        List<GenericArtifactField> fields = ga.getAllGenericArtifactFields();
        for (GenericArtifactField genericArtifactField : fields) {
            String fieldName = genericArtifactField.getFieldName();
            Object fieldValue = genericArtifactField.getFieldValue();
            SortedSet<String> values = fieldsToBeChanged.get(fieldName);
            if (values == null) {
                values = new TreeSet<String>();
                fieldsToBeChanged.put(fieldName, values);
            }
            if (fieldValue != null) {
                values.add(fieldValue.toString());
            }
        }
        TrackerDO result = null;
        try {
            result = trackerHandler.updateTrackerMetaData(ga, trackerId,
                    fieldsToBeChanged, connection);
        } catch (RemoteException e) {
            String cause = "Could not update meta data of tracker " + trackerId
                    + e.getMessage();
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_META_DATA_WRITE);
            throw new CCFRuntimeException(cause, e);
        }
        if (result != null) {
            log.info("Successfully updated meta data of tracker "
                    + result.getId());
        }
        return result;
    }

}
