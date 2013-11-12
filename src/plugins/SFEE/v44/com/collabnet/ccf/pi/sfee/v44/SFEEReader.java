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
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
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
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

/**
 * This class retrieves the changed artifact details from an TF system
 * repository.
 * 
 * It uses the last read time of the sync info and fetches all the artifact data
 * that are changed after the last read time of the particular repository.
 * 
 * @author madhusuthanan (madhusuthanan@collab.net)
 * 
 */
/**
 * @author madhusuthanan
 * 
 */
public class SFEEReader extends AbstractReader<Connection> {

    private static final Log               log                          = LogFactory
                                                                                .getLog(SFEEReader.class);

    private SFEETrackerHandler             trackerHandler               = null;

    private SFEEAttachmentHandler          attachmentHandler            = null;

    private SFEEToGenericArtifactConverter artifactConverter            = null;

    private String                         serverUrl                    = null;

    private String                         password                     = null;

    private String                         username                     = null;

    private boolean                        ignoreConnectorUserUpdates   = true;

    private boolean                        translateTechnicalReleaseIds = false;

    /**
     * This variable indicates whether no web services introduced in SFEE 4.4
     * SP1 HF1 should be used
     */
    private boolean                        pre44SP1HF1System            = false;

    /**
     * Another user name that is used to login into the TF/CSFE instance This
     * user has to differ from the ordinary user used to log in in order to
     * force initial resyncs with the source system once a new artifact has been
     * created.
     */
    private String                         resyncUserName;

    /**
     * Constructs and Initializes the TFReader component.
     */
    public SFEEReader() {
        super();
        init();
    }

    /**
     * Connects to the source TF system using the connectionInfo and
     * credentialInfo details.
     * 
     * This method uses the ConnectionManager configured in the wiring file for
     * the TFReader
     * 
     * @param systemId
     *            - The system id of the source TF system
     * @param systemKind
     *            - The system kind of the source TF system
     * @param repositoryId
     *            - The tracker id in the source TF system
     * @param repositoryKind
     *            - The repository kind for the tracker
     * @param connectionInfo
     *            - The TF server URL
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
     * Queries the artifact with the artifactId to find out if there are any
     * attachments added to the artifact after the last read time in the Sync
     * Info object. If there are attachments added to this artifact after the
     * last read time for this tracker then the attachment data is retrieved and
     * returned as a GenericArtifact object. If there are multiple attachments
     * each of them are encoded in a separate GenericArtifact object and
     * returned in the list.
     * 
     * @see com.collabnet.ccf.core.AbstractReader#getArtifactAttachments(org.dom4j.Document,
     *      java.lang.String)
     */
    @Override
    public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
            GenericArtifact artifactData) {
        String artifactId = artifactData.getSourceArtifactId();
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
        Connection connection;
        try {
            connection = connect(sourceSystemId, sourceSystemKind,
                    sourceRepositoryId, sourceRepositoryKind, serverUrl,
                    getUsername() + SFEEConnectionFactory.PARAM_DELIMITER
                            + getPassword());
        } catch (MaxConnectionsReachedException e) {
            String cause = "Could not create connection to the TF system. Max connections reached for "
                    + serverUrl;
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } catch (ConnectionException e) {
            String cause = "Could not create connection to the TF system "
                    + serverUrl;
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        }
        List<String> artifactIds = new ArrayList<String>();
        artifactIds.add(artifactId);
        List<GenericArtifact> attachments = null;
        try {
            attachments = attachmentHandler.listAttachments(
                    connection.getSessionId(), lastModifiedDate,
                    isIgnoreConnectorUserUpdates() ? getUsername() : "",
                    isIgnoreConnectorUserUpdates() ? getResyncUserName() : "",
                    artifactIds, connection.getSfSoap(),
                    this.getMaxAttachmentSizePerArtifact(),
                    this.isShipAttachmentsWithArtifact(), artifactData);
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

    /**
     * Queries the tracker for the artifact with artifactId and returns its
     * latest data encoded in an GenericArtifact object. The TFReader is capable
     * of retrieving the artifact change history. But this feature is turned off
     * as of now.
     * 
     * @see com.collabnet.ccf.core.AbstractReader#getArtifactData(org.dom4j.Document,
     *      java.lang.String)
     */
    @Override
    public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
        String sourceSystemTimezone = this.getSourceSystemTimezone(syncInfo);
        Connection connection;
        try {
            connection = connect(sourceSystemId, sourceSystemKind,
                    sourceRepositoryId, sourceRepositoryKind, serverUrl,
                    getUsername() + SFEEConnectionFactory.PARAM_DELIMITER
                            + getPassword());
        } catch (MaxConnectionsReachedException e) {
            String cause = "Could not create connection to the TF system. Max connections reached for "
                    + serverUrl;
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } catch (ConnectionException e) {
            String cause = "Could not create connection to the TF system "
                    + serverUrl;
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        }
        GenericArtifact genericArtifact = null;
        try {
            TrackerFieldSoapDO[] trackerFields = null;
            HashMap<String, List<TrackerFieldSoapDO>> fieldsMap = null;
            if (this.isIncludeFieldMetaData()) {
                trackerFields = trackerHandler.getFlexFields(
                        connection.getSessionId(), sourceRepositoryId);
                fieldsMap = SFEEAppHandler
                        .loadTrackerFieldsInHashMap(trackerFields);
            }
            ArtifactSoapDO artifact = null;
            if (isPre44SP1HF1System()) {
                artifact = trackerHandler.getTrackerItem(
                        connection.getSessionId(), artifactId);
            } else {
                artifact = trackerHandler.getTrackerItemFull(
                        connection.getSessionId(), artifactId);
            }

            SFEEAppHandler appHandler = new SFEEAppHandler(
                    connection.getSfSoap(), connection.getSessionId(),
                    this.getServerUrl());
            appHandler.addComments(artifact, lastModifiedDate,
                    isIgnoreConnectorUserUpdates() ? this.getUsername() : "",
                    isIgnoreConnectorUserUpdates() ? this.getResyncUserName()
                            : "");
            if (this.translateTechnicalReleaseIds) {
                trackerHandler.convertReleaseIds(connection.getSessionId(),
                        artifact);
            }
            genericArtifact = artifactConverter.convert(artifact, fieldsMap,
                    lastModifiedDate, this.isIncludeFieldMetaData(),
                    sourceSystemTimezone);
            String lastModifiedBy = artifact.getLastModifiedBy();
            if (lastModifiedBy.equals(this.getResyncUserName())
                    && isIgnoreConnectorUserUpdates()) {
                genericArtifact
                        .setArtifactAction(GenericArtifact.ArtifactActionValue.RESYNC);
            }
            populateSrcAndDest(syncInfo, genericArtifact);
        } catch (RemoteException e) {
            String cause = "During the artifact retrieval process from TF, an error occured";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } finally {
            this.disconnect(connection);
        }
        return genericArtifact;
    }

    /**
     * This method is supposed to return all the artifacts that are associated
     * with this artifact. But not implemented yet. Returns an empty list.
     * 
     * @see com.collabnet.ccf.core.AbstractReader#getArtifactDependencies(org.dom4j.Document,
     *      java.lang.String)
     */
    @Override
    public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
            String artifactId) {
        return new ArrayList<GenericArtifact>();
    }

    public SFEEAttachmentHandler getAttachmentHandler() {
        return attachmentHandler;
    }

    /**
     * This method queries the particular tracker in the source TF system to
     * check if there are artifacts changed/created after the last read time
     * coming in, in the Sync Info object.
     * 
     * If there are changed artifacts their ids are returned in a List.
     * 
     * @see com.collabnet.ccf.core.AbstractReader#getChangedArtifacts(org.dom4j.Document)
     */
    @Override
    public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        String lastSynchronizedArtifactId = this
                .getLastSourceArtifactId(syncInfo);
        String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
        int version = 0;
        try {
            version = Integer.parseInt(lastSynchronizedVersion);
        } catch (NumberFormatException e) {
            log.warn("Version string is not a number "
                    + lastSynchronizedVersion, e);
        }
        Connection connection;
        try {
            connection = connect(sourceSystemId, sourceSystemKind,
                    sourceRepositoryId, sourceRepositoryKind, serverUrl,
                    getUsername() + SFEEConnectionFactory.PARAM_DELIMITER
                            + getPassword());
        } catch (MaxConnectionsReachedException e) {
            String cause = "Could not create connection to the TF system. Max connections reached for "
                    + serverUrl;
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } catch (ConnectionException e) {
            String cause = "Could not create connection to the TF system "
                    + serverUrl;
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        }
        Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
        if (lastModifiedDate == null) {
            lastModifiedDate = new Date(0);
        }
        ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();
        List<ArtifactSoapDO> artifactRows = null;
        try {
            artifactRows = trackerHandler.getChangedTrackerItems(
                    connection.getSessionId(), sourceRepositoryId,
                    lastModifiedDate, lastSynchronizedArtifactId, version,
                    isIgnoreConnectorUserUpdates() ? this.getUsername() : "");
        } catch (RemoteException e) {
            String cause = "During the changed artifacts retrieval process from TF, an exception occured";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } finally {
            this.disconnect(connection);
        }
        if (artifactRows != null) {
            for (ArtifactSoapDO artifact : artifactRows) {
                String artifactId = artifact.getId();
                ArtifactState artifactState = new ArtifactState();
                artifactState.setArtifactId(artifactId);
                artifactState.setArtifactLastModifiedDate(artifact
                        .getLastModifiedDate());
                artifactState.setArtifactVersion(artifact.getVersion());
                artifactStates.add(artifactState);
            }
        }
        return artifactStates;
    }

    /**
     * Returns the server URL of the source CSFE/TF system that is configured in
     * the wiring file.
     * 
     * @return
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * Gets the mandatory user name The user name is used to login into the
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
            ConnectionManager<Connection> connectionManager) {
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
            return handleException(innerCause, connectionManager);
        } else if (cause instanceof CCFRuntimeException) {
            Throwable innerCause = cause.getCause();
            return handleException(innerCause, connectionManager);
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

    /**
     * Returns whether no web service call introduced in SFEE 4.4 SP1 HF1 should
     * be used
     * 
     * @return true if no web services introduced in SFEE 4.4 SP1 HF1 should be
     *         used
     */
    public boolean isPre44SP1HF1System() {
        return pre44SP1HF1System;
    }

    public boolean isTranslateTechnicalReleaseIds() {
        return translateTechnicalReleaseIds;
    }

    public void reset(Object context) {
    }

    public void setAttachmentHandler(SFEEAttachmentHandler attachmentHandler) {
        this.attachmentHandler = attachmentHandler;
    }

    /**
     * Sets whether updated and created artifacts from the connector user should
     * be ignored This is the default behavior to avoid infinite update loops.
     * However, in artifact export scenarios, where all artifacts should be
     * extracted, this property should be set to false
     * 
     * @param ignoreConnectorUserUpdates
     *            whether to ignore artifacts that have been created or lastly
     *            modified by the connector user
     */
    public void setIgnoreConnectorUserUpdates(boolean ignoreConnectorUserUpdates) {
        this.ignoreConnectorUserUpdates = ignoreConnectorUserUpdates;
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
     * Sets whether no web service call introduced in SFEE 4.4 SP1 HF1 should be
     * used
     * 
     * @param pre44SP1HF1System
     *            true if no web services introduced in SFEE 4.4 SP1 HF1 should
     *            be used
     */
    public void setPre44SP1HF1System(boolean pre44SP1HF1System) {
        this.pre44SP1HF1System = pre44SP1HF1System;
    }

    /**
     * Sets the optional resync username
     * 
     * The resync user name is used to login into the TF/CSFE instance whenever
     * an artifact should be created. This user has to differ from the ordinary
     * user used to log in in order to force initial resyncs with the source
     * system once a new artifact has been created. This property can also be
     * set for the reader component in order to be able to differentiate between
     * artifacts created by ordinary users and artifacts to be resynced.
     * 
     * @param resyncUserName
     *            the resyncUserName to set
     */
    public void setResyncUserName(String resyncUserName) {
        this.resyncUserName = resyncUserName;
    }

    /**
     * Sets the source CSFE/TF system's SOAP server URL.
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
     * Sets the mandatory username
     * 
     * The user name is used to login into the TF/CSFE instance whenever an
     * artifact should be updated or extracted. This user has to differ from the
     * resync user in order to force initial resyncs with the source system once
     * a new artifact has been created.
     * 
     * @param userName
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(List exceptions) {
        super.validate(exceptions);

        if (getResyncUserName() == null) {
            log.warn("resyncUserName-property has not been set, so that initial resyncs after artifact creation are not possible.");
        }

        if (StringUtils.isEmpty(getServerUrl())) {
            exceptions.add(new ValidationException(
                    "serverUrl-property not set", this));
        }
        if (StringUtils.isEmpty(getUsername())) {
            exceptions.add(new ValidationException("username-property not set",
                    this));
        }
        if (getPassword() == null) {
            exceptions.add(new ValidationException("password-property not set",
                    this));
        }
        if (exceptions.size() == 0) {
            trackerHandler = new SFEETrackerHandler(getServerUrl(),
                    getConnectionManager());
            attachmentHandler = new SFEEAttachmentHandler(getServerUrl(),
                    getConnectionManager());
        }
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
     * Gets the optional resync username The resync user name is used to login
     * into the TF/CSFE instance whenever an artifact should be created. This
     * user has to differ from the ordinary user used to log in in order to
     * force initial resyncs with the source system once a new artifact has been
     * created. This property can also be set for the reader component in order
     * to be able to differentiate between artifacts created by ordinary users
     * and artifacts to be resynced.
     * 
     * @return the resyncUserName
     */
    private String getResyncUserName() {
        return resyncUserName;
    }

    /**
     * Initializes the SFEEToGenericArtifactConverter component.
     */
    private void init() {
        artifactConverter = new SFEEToGenericArtifactConverter();
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

    /**
     * Populates the source and destination attributes for this GenericArtifact
     * object from the Sync Info database document.
     * 
     * @param syncInfo
     * @param ga
     */
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
