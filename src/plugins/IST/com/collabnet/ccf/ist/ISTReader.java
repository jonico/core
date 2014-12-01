package com.collabnet.ccf.ist;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
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

    private static final Log log                        = LogFactory
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

    private String           username                   = null;
    private String           password                   = null;
    private String           serverUrl                  = null;
    private String           projectId                  = null;
    private boolean          ignoreConnectorUserUpdates = true;

    private ISTHandler       istHandler                 = null;
    private DateFormat       df                         = GenericArtifactHelper.df;

    private ISTConnection connect(String systemId, String systemKind,
            String repositoryId, String repositoryKind) {

        String credentialInfo = getUsername()
                + ISTConnectionFactory.PARAM_DELIMITER + getPassword();
        String connectionInfo = getServerUrl()
                + ISTConnectionFactory.CONNECTION_INFO_DELIMITER + repositoryId;

        ISTConnection connection = null;
        try {
            connection = getConnectionManager()
                    .getConnectionToUpdateOrExtractArtifact(systemId,
                            systemKind, repositoryId, repositoryKind,
                            connectionInfo, credentialInfo);
        } catch (MaxConnectionsReachedException e) {
            String cause = "The maximum allowed connection configuration for a Connection Pool is reached."
                    + serverUrl;
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        } catch (ConnectionException e) {
            String cause = "Could not create connection to the IST system "
                    + serverUrl;
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        }

        this.istHandler = new ISTHandler(connection);

        log.debug("     succesfully connected");

        return connection;
    }

    @Override
    public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
            GenericArtifact artifactData) {
        // TODO Auto-generated method stub
        return new ArrayList<GenericArtifact>();
    }

    @Override
    public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        this.getLastSourceVersion(syncInfo);
        Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
        this.getLastSourceArtifactId(syncInfo);

        ISTConnection connection = connect(sourceSystemId, sourceSystemKind,
                sourceRepositoryId, sourceRepositoryKind);

        GenericArtifact ga = new GenericArtifact();

        try {
            populateSrcAndDest(syncInfo, ga);
            ga.setSourceArtifactId(artifactId);
            ga.setArtifactMode(ArtifactModeValue.COMPLETE);
            ga.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);

            istHandler.retrieveIncident(Integer.valueOf(artifactId),
                    lastModifiedDate, sourceRepositoryKind,
                    ignoreConnectorUserUpdates, ga, sourceRepositoryId);

        } catch (Exception e) {
            String cause = "An error occurred during incident retrieval";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        }

        // release connection to pool
        getConnectionManager().releaseConnection(connection);

        try {
            log.trace("Rendered Source Artifact XML for "
                    + ga.getSourceArtifactId()
                    + ":\n"
                    + GenericArtifactHelper
                            .createGenericArtifactXMLDocument(ga).asXML());
        } catch (GenericArtifactParsingException e) {
            log.warn("Tried to convert GA to XML but failed!");
        }

        return ga;
    }

    @Override
    public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
            String artifactId) {
        // TODO Auto-generated method stub
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
        String lastSynchedArtifactId = this.getLastSourceArtifactId(syncInfo);
        this.getSourceSystemTimezone(syncInfo);

        ISTConnection connection = connect(sourceSystemId, sourceSystemKind,
                sourceRepositoryId, sourceRepositoryKind);

        // istHandler.debugAllIncidents(connection);

        // testing cutoff date
        try {
            lastModifiedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .parse("2014-11-28 6:26:50");
        } catch (ParseException e) {
            String cause = "Failed to set test date";
            log.error(cause, e);
            throw new CCFRuntimeException(cause, e);
        }

        ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();

        istHandler.retrieveChangedIncidents(lastModifiedDate,
                lastSynchronizedVersion, lastSynchedArtifactId, artifactStates);

        log.debug("found " + artifactStates.size()
                + " artifact/s changed since " + df.format(lastModifiedDate));

        if (artifactStates.size() > 0) {
            log.debug(String.format("  ID   %-23s  %-30s", "Last Update",
                    "Version"));
            for (ArtifactState as : artifactStates) {
                log.debug(String.format("  %-3s  %-23s  %-30d",
                        as.getArtifactId(),
                        df.format(as.getArtifactLastModifiedDate()),
                        as.getArtifactVersion()));
            }
        }

        // release connection to pool
        getConnectionManager().releaseConnection(connection);

        // for now, do not ship stuff over.
        return artifactStates;
    }

    @Override
    public List<ArtifactState> getChangedArtifactsToForceSync(
            Set<String> artifactsToForce, Document SyncInfo) {
        // TODO Auto-generated method stub
        return null;
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

    public void setIgnoreConnectorUserUpdates(boolean ignoreConnectorUserUpdates) {
        this.ignoreConnectorUserUpdates = ignoreConnectorUserUpdates;
    }

    public void setIstHandler(ISTHandler istHandler) {
        this.istHandler = istHandler;
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
        validateNotNull(this.getPassword(), "SpiraTest password not set", this,
                exceptions);
        validateNotNull(this.getUsername(), "SpiraTest username not set", this,
                exceptions);
        validateNotNull(this.getServerUrl(), "SpiraTest Server URL not set",
                this, exceptions);

        log.info("===========================================================");
        log.info("started SpiraTest Reader " + ISTVersion.getVersion());

    }

    private void validateNotNull(Object toValidate, String cause,
            Component component, List<ValidationException> exceptions) {
        if (toValidate == null) {
            log.error(cause);
            exceptions.add(new ValidationException(cause, component));
        }

    }

}
