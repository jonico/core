package com.collabnet.ccf.ist;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.Component;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;

public class ISTReader extends AbstractReader<ISTConnection> {

    private static final Log  log                        = LogFactory
            .getLog(ISTReader.class);

    /**
     * Properties set via injection
     *
     * @username
     * @password
     * @serverUrl
     * @projectId
     * @ignoreConnectorUserUpdates
     */

    private String            username                   = null;
    private String            password                   = null;
    private String            serverUrl                  = null;
    private String            projectId                  = null;
    private boolean           ignoreConnectorUserUpdates = true;

    private ISTHandler        handler                    = null;

    private DateFormat        df                         = GenericArtifactHelper.df;

    private JDBCReadConnector identityMapping            = null;

    private ISTConnection connect(String systemId, String systemKind,
            String repositoryId, String repositoryKind) {

        String credentialInfo = getUsername()
                + ISTConnectionFactory.PARAM_DELIMITER + getPassword();
        String connectionInfo = getServerUrl()
                + ISTConnectionFactory.CONNECTION_INFO_DELIMITER + repositoryId;

        ISTConnection connection = null;
        try {
            connection = getConnectionManager()
                    .getConnectionToUpdateOrExtractArtifact(
                            systemId,
                            systemKind,
                            repositoryId,
                            repositoryKind,
                            connectionInfo,
                            credentialInfo);
        } catch (MaxConnectionsReachedException e) {
            String cause = "The maximum allowed connection configuration for a Connection Pool is reached."
                    + serverUrl;
            log.error(
                    cause,
                    e);
            throw new CCFRuntimeException(cause, e);
        } catch (ConnectionException e) {
            String cause = "Could not create connection to SpiraTest at "
                    + serverUrl;
            log.error(
                    cause,
                    e);
            throw new CCFRuntimeException(cause, e);
        }

        // load handler - this also loads all current Custom Property List Values
        this.handler = new ISTHandler(connection);

        log.debug("     succesfully connected");

        return connection;
    }

    @Override
    public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
            GenericArtifact artifactData) {

        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
        this.getArtifactLastModifiedDate(syncInfo);
        ISTConnection connection = null;
        List<GenericArtifact> attachments = null;

        try {

            connection = connect(
                    sourceSystemId,
                    sourceSystemKind,
                    sourceRepositoryId,
                    sourceRepositoryKind);

            log.debug("Loading attachments for incident "
                    + artifactData.getSourceArtifactId() + " since "
                    + df.format(lastModifiedDate));

            ISTAttachmentHandler attHandler = new ISTAttachmentHandler();
            attachments = attHandler.getAttachmentsForIncident(
                    connection,
                    lastModifiedDate,
                    artifactData,
                    true);
            // last arg was `this.isShipAttachmentsWithArtifact()`, default = false. why/how/when should this be set?

            for (GenericArtifact ga : attachments) {
                populateSrcAndDestForAttachment(
                        syncInfo,
                        ga);

                log.debug(String.format(
                        "  attachment %s.%s, last updated on %s",
                        ga.getDepParentSourceArtifactId(),
                        ga.getSourceArtifactId(),
                        ga.getSourceArtifactLastModifiedDate()));
                try {
                    log.trace("Attachment XML\n"
                            + GenericArtifactHelper
                            .createGenericArtifactXMLDocument(
                                    ga).asXML());
                } catch (GenericArtifactParsingException e) {
                    log.warn("Could not render attachment XML: "
                            + e.getMessage());
                }
            }
        } finally {
            // release connection to pool
            getConnectionManager().releaseConnection(
                    connection);

        }
        return attachments;
    }

    @Override
    public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        this.getLastSourceVersion(syncInfo);

        String repositoryMappingId = this.getRepositoryMappingId(syncInfo);
        String storedVersion = this.lookupSourceArtifactVersion(
                artifactId,
                repositoryMappingId);

        if (storedVersion == null)
            log.debug(String.format(
                    "No previous version found for rmID-artfID %s-%s",
                    repositoryMappingId,
                    artifactId));
        else
            log.debug(String.format(
                    "Found stored version for incident #%s: %s: ",
                    artifactId,
                    ISTIncident.getVersionString(storedVersion)));

        Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
        ISTConnection connection = null;

        GenericArtifact ga = new GenericArtifact();

        try {
            connection = connect(
                    sourceSystemId,
                    sourceSystemKind,
                    sourceRepositoryId,
                    sourceRepositoryKind);

            populateSrcAndDest(
                    syncInfo,
                    ga);
            ga.setSourceArtifactId(artifactId);
            ga.setArtifactMode(ArtifactModeValue.COMPLETE);
            ga.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);

            handler.retrieveIncident(
                    lastModifiedDate,
                    storedVersion,
                    ga);

        } catch (Exception e) {
            String cause = "An error occurred during incident retrieval";
            log.error(
                    cause,
                    e);
            throw new CCFRuntimeException(cause, e);
        } finally {

            // release connection to pool
            getConnectionManager().releaseConnection(
                    connection);
        }

        try {
            log.trace("Rendered Source Artifact XML for "
                    + ga.getSourceArtifactId() + ":\n"
                    + GenericArtifactHelper.createGenericArtifactXMLDocument(
                            ga).asXML());
        } catch (GenericArtifactParsingException e) {
            log.warn("Tried to convert GA to XML but failed!");
        }

        return ga;
    }

    @Override
    public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
            String artifactId) {
        // dependencies, a.k.a. hierarchies are not implemented in SpiraTest
        // http://www.inflectra.com/Support/Ticket/17234.aspx
        return new ArrayList<GenericArtifact>();
    }

    @Override
    public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
        Date lastModifiedDate = this.getLastModifiedDate(syncInfo);

        ISTConnection connection = null;
        ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();

        //        try {
        //            lastModifiedDate = df.parse("Fr, 1 Jan 1999 00:00:00.000 +0700");
        //        } catch (ParseException e) {
        //            log.warn("could not reset date");
        //        }

        //FIXME multi list values get lost (again)

        log.debug("retrieving incidents changed since "
                + df.format(lastModifiedDate) + " and version "
                + lastSynchronizedVersion);

        boolean skipit = false;
        if (skipit)
            return artifactStates;

        try {
            connection = connect(
                    sourceSystemId,
                    sourceSystemKind,
                    sourceRepositoryId,
                    sourceRepositoryKind);

            handler.retrieveChangedIncidents(
                    lastModifiedDate,
                    artifactStates);

            if (artifactStates.size() > 0 && log.isTraceEnabled()) {
                log.trace("====================");
                log.trace("Incident Update List");
                log.trace("====================");
                log.trace(String.format(
                        "  %-4s  %-40s  %-15s  %-15s %-15s %-15s",
                        "ID",
                        "Last Update",
                        "Full Version",
                        "DB Version",
                        "Version.Hash",
                        "DB Version.Hash"));
                String repoMappingId = getRepositoryMappingId(syncInfo);
                for (ArtifactState as : artifactStates) {
                    long fullVersion = as.getArtifactVersion();
                    String tableVersion = this.lookupSourceArtifactVersion(
                            as.getArtifactId(),
                            repoMappingId);
                    log.trace(String.format(
                            "  %-4s  %-40s  %-15d  %-15s %-15s %-15s",
                            as.getArtifactId(),
                            df.format(as.getArtifactLastModifiedDate()),
                            fullVersion,
                            tableVersion,
                            ISTIncident.getVersionString(String
                                    .valueOf(fullVersion)),
                                    ISTIncident.getVersionString(String
                                            .valueOf(tableVersion))));

                }
            } else {

                log.debug("found no artifacts changed since "
                        + df.format(lastModifiedDate)
                        + "; last sync'd Version: " + lastSynchronizedVersion);
            }
        } finally {
            // release connection to pool
            getConnectionManager().releaseConnection(
                    connection);
        }
        // for now, do not ship stuff over.
        return artifactStates;
    }

    @Override
    public List<ArtifactState> getChangedArtifactsToForceSync(
            Set<String> artifactsToForce, Document SyncInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    public JDBCReadConnector getIdentityMapping() {
        return identityMapping;
    }

    public String getPassword() {
        return password;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getUsername() {
        return username;
    }

    public boolean isIgnoreConnectorUserUpdates() {
        return ignoreConnectorUserUpdates;
    }

    /**
     * fetches a the stored source artifact version from identity mappings.
     *
     * @param sourceArtifactId
     * @param artifactType
     * @param repositoryMappingId
     * @return
     */
    private String lookupSourceArtifactVersion(String sourceArtifactId,
            String repositoryMappingId) {
        String result = null;
        IOrderedMap inputParameters = new OrderedHashMap();

        inputParameters.add(repositoryMappingId);
        inputParameters.add(sourceArtifactId);
        inputParameters.add("plainArtifact");
        identityMapping.connect();
        Object[] resultSet = identityMapping.next(
                inputParameters,
                1000);
        //identityMappingDatabaseReader.disconnect();

        if (resultSet == null || resultSet.length == 0) {
            result = null;
        } else if (resultSet.length == 1) {
            if (resultSet[0] instanceof OrderedHashMap) {
                OrderedHashMap results = (OrderedHashMap) resultSet[0];

                // Source Artifact Version is the last of the four entries
                if (results.size() == 4) {
                    // the column is TARGET_ARTIFACT_VERSION as DB colmns are aloways named with CTF as SOURCE
                    return String.valueOf(results.get(2));
                } else {
                    String cause = "Seems as if the SQL statement for identityMappingDatabase reader does not return values.";
                    log.error(cause);
                    throw new CCFRuntimeException(cause);
                }
            } else {
                String cause = "SQL query on identity mapping table did not return data in correct format!";
                log.error(cause);
                throw new CCFRuntimeException(cause);
            }
        } else {
            String cause = "There is more than one mapping for the combination RepoMappingID = "
                    + repositoryMappingId
                    + "; Source Artifact ID = "
                    + sourceArtifactId + " in the identity mapping table.";
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }
        return result;
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

    /**
     * Populates the source and destination attributes for this GenericArtifact
     * Attachment object from the Sync Info database document.
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

    public void setIdentityMapping(JDBCReadConnector identityMapping) {
        this.identityMapping = identityMapping;
    }

    public void setIgnoreConnectorUserUpdates(boolean ignoreConnectorUserUpdates) {
        this.ignoreConnectorUserUpdates = ignoreConnectorUserUpdates;
    }

    public void setIstHandler(ISTHandler istHandler) {
        this.handler = istHandler;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void validate(List exceptions) {
        super.validate(exceptions);
        validateNotNull(
                this.getPassword(),
                "SpiraTest password not set",
                this,
                exceptions);
        validateNotNull(
                this.getUsername(),
                "SpiraTest username not set",
                this,
                exceptions);
        validateNotNull(
                this.getServerUrl(),
                "SpiraTest Server URL not set",
                this,
                exceptions);

        log.info("===========================================================");
        log.info("started SpiraTest Reader " + ISTVersionInfo.getVersion());

    }

    private void validateNotNull(Object toValidate, String cause,
            Component component, List<ValidationException> exceptions) {
        if (toValidate == null) {
            log.error(cause);
            exceptions.add(new ValidationException(cause, component));
        }

    }

}
