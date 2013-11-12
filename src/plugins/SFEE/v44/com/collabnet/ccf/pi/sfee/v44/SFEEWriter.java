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

package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
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
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.collabnet.ccf.pi.sfee.v44.meta.ArtifactMetaData;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

/**
 * This component is responsible for writing TF tracker items encoded in the
 * generic XML artifact format back to the TF tracker
 * 
 * @author jnicolai
 * 
 */
public class SFEEWriter extends AbstractWriter<Connection> implements IDataProcessor {

    /**
     * log4j logger instance
     */
    private static final Log      log                          = LogFactory
                                                                       .getLog(SFEEWriter.class);

    /**
     * TF tracker handler instance
     */
    private SFEETrackerHandler    trackerHandler;

    /**
     * Comment used when updating TF tracker items
     */
    private String                updateComment;

    private SFEEAttachmentHandler attachmentHandler;

    // private ConnectionManager<Connection> connectionManager = null;

    private String                serverUrl;

    private boolean               translateTechnicalReleaseIds = false;

    /**
     * Password that is used to login into the TF/CSFE instance in combination
     * with the username
     */
    private String                password;

    /**
     * Username that is used to login into the TF/CSFE instance
     */
    private String                username;

    /**
     * Another user name that is used to login into the TF/CSFE instance This
     * user has to differ from the ordinary user used to log in in order to
     * force initial resyncs with the source system once a new artifact has been
     * created.
     */
    private String                resyncUserName;

    /**
     * Password that belongs to the resync user. This user has to differ from
     * the ordinary user used to log in in order to force initial resyncs with
     * the source system once a new artifact has been created.
     */
    private String                resyncPassword;

    public SFEEWriter() {
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
                        getUsername() + SFEEConnectionFactory.PARAM_DELIMITER
                                + getPassword(), false);
            } else {
                connection = connect(targetSystemId, targetSystemKind,
                        targetRepositoryId, targetRepositoryKind, serverUrl,
                        getResyncUserName()
                                + SFEEConnectionFactory.PARAM_DELIMITER
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
        this.initializeArtifact(ga);
        String targetRepositoryId = ga.getTargetRepositoryId();
        String targetArtifactId = ga.getTargetArtifactId();
        String tracker = targetRepositoryId;
        Connection connection = connect(ga);
        ArtifactSoapDO result = null;
        try {
            result = this.createArtifact(ga, tracker, connection);
            // update Id field after creating the artifact
            targetArtifactId = result.getId();
            SFEEGAHelper.addField(ga,
                    ArtifactMetaData.SFEEFields.id.getFieldName(),
                    targetArtifactId,
                    GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
                    GenericArtifactField.FieldValueTypeValue.STRING);
        } catch (NumberFormatException e) {
            log.error(
                    "Wrong data format of attribute for artifact "
                            + data.asXML(), e);
            return null;
        } finally {
            disconnect(connection);
        }
        if (result != null) {
            this.populateTargetArtifactAttributes(ga, result);
        }
        return this.returnDocument(ga);
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
            attachmentHandler.handleAttachment(connection.getSessionId(), ga,
                    targetParentArtifactId, this.getUsername(),
                    connection.getSfSoap());
            ArtifactSoapDO artifact = trackerHandler.getTrackerItem(
                    connection.getSessionId(), targetParentArtifactId);
            parentArtifact = new GenericArtifact();
            // make sure that we do not update the synchronization status record for replayed attachments
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
        //throw new CCFRuntimeException("createDependency is not implemented...!");
        log.warn("createDependency is not implemented...!");
        return null;
    }

    @Override
    public Document deleteArtifact(Document gaDocument) {
        //throw new CCFRuntimeException("deleteArtifact is not implemented...!");
        log.warn("deleteArtifact is not implemented...!");
        return null;
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
            attachmentHandler.deleteAttachment(connection, targetArtifactId,
                    artifactId, ga);
            log.info("Attachment " + targetArtifactId + "is deleted");
            ArtifactSoapDO artifact = trackerHandler.getTrackerItem(
                    connection.getSessionId(), artifactId);
            parentArtifact = new GenericArtifact();
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
        //throw new CCFRuntimeException("deleteDependency is not implemented...!");
        log.warn("deleteDependency is not implemented...!");
        return null;
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
        this.initializeArtifact(ga);
        String targetRepositoryId = ga.getTargetRepositoryId();
        String tracker = targetRepositoryId;
        Connection connection = connect(ga);
        ArtifactSoapDO result = null;
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
        } finally {
            disconnect(connection);
        }

        // otherwise a conflict has happened and the generic artifact has been
        // already prepared
        if (result != null) {
            this.populateTargetArtifactAttributes(ga, result);
        }
        return this.returnDocument(ga);
    }

    @Override
    public Document updateAttachment(Document gaDocument) {
        //throw new CCFRuntimeException("updateAttachment is not implemented...!");
        log.warn("updateAttachment is not implemented...!");
        return null;
    }

    @Override
    public Document updateDependency(Document gaDocument) {
        //throw new CCFRuntimeException("updateDependency is not implemented...!");
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

        if (getResyncUserName() == null) {
            log.warn("resyncUserName-property has not been set, so that initial resyncs after artifact creation are not possible.");
        }

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
            trackerHandler = new SFEETrackerHandler(getServerUrl(),
                    connectionManager);
            attachmentHandler = new SFEEAttachmentHandler(getServerUrl(),
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
    private ArtifactSoapDO createArtifact(GenericArtifact ga, String tracker,
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
                String trackerFieldValueType = ArtifactMetaData
                        .getSFEEFieldValueTypeForGAFieldType(gaField
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
                ArtifactMetaData.SFEEFields.folderId.getFieldName(), ga);
        String description = GenericArtifactHelper.getStringMandatoryGAField(
                ArtifactMetaData.SFEEFields.description.getFieldName(), ga);
        String category = GenericArtifactHelper.getStringMandatoryGAField(
                ArtifactMetaData.SFEEFields.category.getFieldName(), ga);
        String group = GenericArtifactHelper.getStringMandatoryGAField(
                ArtifactMetaData.SFEEFields.group.getFieldName(), ga);
        String status = GenericArtifactHelper.getStringMandatoryGAField(
                ArtifactMetaData.SFEEFields.status.getFieldName(), ga);
        String statusClass = GenericArtifactHelper.getStringMandatoryGAField(
                ArtifactMetaData.SFEEFields.statusClass.getFieldName(), ga);
        String customer = GenericArtifactHelper.getStringMandatoryGAField(
                ArtifactMetaData.SFEEFields.customer.getFieldName(), ga);
        int priority = GenericArtifactHelper.getIntMandatoryGAField(
                ArtifactMetaData.SFEEFields.priority.getFieldName(), ga);
        int estimatedHours = GenericArtifactHelper.getIntMandatoryGAField(
                ArtifactMetaData.SFEEFields.estimatedHours.getFieldName(), ga);
        int actualHours = GenericArtifactHelper.getIntMandatoryGAField(
                ArtifactMetaData.SFEEFields.actualHours.getFieldName(), ga);
        Date closeDate = GenericArtifactHelper.getDateMandatoryGAField(
                ArtifactMetaData.SFEEFields.closeDate.getFieldName(), ga);
        String assignedTo = GenericArtifactHelper.getStringMandatoryGAField(
                ArtifactMetaData.SFEEFields.assignedTo.getFieldName(), ga);
        String reportedReleaseId = GenericArtifactHelper
                .getStringMandatoryGAField(
                        ArtifactMetaData.SFEEFields.reportedReleaseId
                                .getFieldName(), ga);
        String resolvedReleaseId = GenericArtifactHelper
                .getStringMandatoryGAField(
                        ArtifactMetaData.SFEEFields.resolvedReleaseId
                                .getFieldName(), ga);
        String title = GenericArtifactHelper.getStringMandatoryGAField(
                ArtifactMetaData.SFEEFields.title.getFieldName(), ga);
        String[] comments = this.getComments(ga);
        ArtifactSoapDO result = null;
        try {
            if (this.translateTechnicalReleaseIds) {
                reportedReleaseId = trackerHandler.convertReleaseId(
                        connection.getSessionId(), reportedReleaseId, folderId);
                resolvedReleaseId = trackerHandler.convertReleaseId(
                        connection.getSessionId(), resolvedReleaseId, folderId);
            }
            result = trackerHandler.createArtifact(connection.getSessionId(),
                    folderId, description, category, group, status,
                    statusClass, customer, priority, estimatedHours,
                    actualHours, closeDate, assignedTo, reportedReleaseId,
                    resolvedReleaseId, flexFieldNames, flexFieldValues,
                    flexFieldTypes, title, comments);
            log.info("New artifact " + result.getId()
                    + " is created with the change from "
                    + ga.getSourceArtifactId());
        } catch (RemoteException e) {
            String cause = "While trying to create an artifact within TF, an error occured";
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        }
        return result;
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
                        ArtifactMetaData.SFEEFields.commentText.getFieldName());
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
            if (SFEEGAHelper.containsSingleMandatoryField(ga,
                    ArtifactMetaData.SFEEFields.id.getFieldName())) {
                SFEEGAHelper.updateSingleMandatoryField(ga,
                        ArtifactMetaData.SFEEFields.id.getFieldName(),
                        targetArtifactId);
                ga.setTargetArtifactId(targetArtifactId);
            } else {
                SFEEGAHelper.addField(ga,
                        ArtifactMetaData.SFEEFields.id.getFieldName(),
                        targetArtifactId,
                        GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
                        GenericArtifactField.FieldValueTypeValue.STRING);
            }
        }
        if (SFEEGAHelper.containsSingleMandatoryField(ga,
                ArtifactMetaData.SFEEFields.folderId.getFieldName())) {
            SFEEGAHelper.updateSingleMandatoryField(ga,
                    ArtifactMetaData.SFEEFields.folderId.getFieldName(),
                    tracker);
        } else {
            SFEEGAHelper.addField(ga,
                    ArtifactMetaData.SFEEFields.folderId.getFieldName(),
                    tracker,
                    GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
                    GenericArtifactField.FieldValueTypeValue.STRING);
        }
    }

    private void populateTargetArtifactAttributes(GenericArtifact ga,
            ArtifactSoapDO result) {
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
    private ArtifactSoapDO updateArtifact(GenericArtifact ga, String tracker,
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
                if (gaField.getFieldValueHasChanged()) {
                    overriddenFlexFields.add(fieldName);
                } else {
                    continue;
                }
                String trackerFieldValueType = ArtifactMetaData
                        .getSFEEFieldValueTypeForGAFieldType(gaField
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
                        ArtifactMetaData.SFEEFields.folderId.getFieldName(), ga);
        GenericArtifactField description = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.description.getFieldName(),
                        ga);
        GenericArtifactField category = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.category.getFieldName(), ga);
        GenericArtifactField group = GenericArtifactHelper.getMandatoryGAField(
                ArtifactMetaData.SFEEFields.group.getFieldName(), ga);
        GenericArtifactField status = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.status.getFieldName(), ga);
        GenericArtifactField statusClass = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.statusClass.getFieldName(),
                        ga);
        GenericArtifactField customer = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.customer.getFieldName(), ga);
        GenericArtifactField priority = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.priority.getFieldName(), ga);
        GenericArtifactField estimatedHours = GenericArtifactHelper
                .getMandatoryGAField(ArtifactMetaData.SFEEFields.estimatedHours
                        .getFieldName(), ga);
        GenericArtifactField actualHours = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.actualHours.getFieldName(),
                        ga);
        GenericArtifactField closeDate = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.closeDate.getFieldName(),
                        ga);
        GenericArtifactField assignedTo = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.assignedTo.getFieldName(),
                        ga);
        GenericArtifactField reportedReleaseId = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.reportedReleaseId
                                .getFieldName(), ga);
        GenericArtifactField resolvedReleaseId = GenericArtifactHelper
                .getMandatoryGAField(
                        ArtifactMetaData.SFEEFields.resolvedReleaseId
                                .getFieldName(), ga);
        GenericArtifactField title = GenericArtifactHelper.getMandatoryGAField(
                ArtifactMetaData.SFEEFields.title.getFieldName(), ga);

        String[] comments = this.getComments(ga);
        ArtifactSoapDO result = null;
        try {
            result = trackerHandler.updateArtifact(ga,
                    connection.getSessionId(), folderId, description, category,
                    group, status, statusClass, customer, priority,
                    estimatedHours, actualHours, closeDate, assignedTo,
                    reportedReleaseId, resolvedReleaseId, flexFieldNames,
                    flexFieldValues, flexFieldTypes, overriddenFlexFields,
                    title, id, comments, translateTechnicalReleaseIds);
            if (result != null) {
                log.info("Artifact " + id + " is updated successfully");
            }
        } catch (RemoteException e) {
            String cause = "While trying to update an artifact within TF, an error occured";
            log.error(cause, e);
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        }
        return result;
    }

}
