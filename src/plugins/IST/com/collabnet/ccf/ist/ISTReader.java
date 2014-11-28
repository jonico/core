package com.collabnet.ccf.ist;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;

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
     */

    private String           username                   = null;
    private String           password                   = null;
    private String           serverUrl                  = null;
    private String           projectId                  = null;
    private boolean          ignoreConnectorUserUpdates = true;

    private ISTConnection connect(String systemId, String systemKind,
            String repositoryId, String repositoryKind) {

        String credentialInfo = getUsername()
                + ISTConnectionFactory.PARAM_DELIMITER + getPassword();
        String connectionInfo = getServerUrl()
                + ISTConnectionFactory.PARAM_DELIMITER + getProjectId();

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

        log.trace("     succesfully connected");

        return connection;
    }

    @Override
    public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
            GenericArtifact artifactData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
            String artifactId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
        String sourceSystemId = this.getSourceSystemId(syncInfo);
        String sourceSystemKind = this.getSourceSystemKind(syncInfo);
        String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
        String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
        this.getLastSourceVersion(syncInfo);
        this.getLastModifiedDate(syncInfo);
        this.getLastSourceArtifactId(syncInfo);
        this.getSourceSystemTimezone(syncInfo);

        ISTConnection connection = connect(sourceSystemId, sourceSystemKind,
                sourceRepositoryId, sourceRepositoryKind);

        ArrayOfRemoteIncident allIncidents = ISTHandler
                .getIncidentsSorted(connection);
        log.debug("retrieved " + allIncidents.getRemoteIncident().size()
                + " incidents");
        if (allIncidents.getRemoteIncident().size() > 0) {
            log.debug(String.format("  %3d  %-20s  %-23s", "ID", "Name",
                    "Last Updated"));
            for (RemoteIncident in : allIncidents.getRemoteIncident()) {
                log.debug(String.format("  %3d  %-20s  %=23s", in
                        .getIncidentId().getValue(), in.getName().getValue(),
                        in.getLastUpdateDate().toString()));
            }
        }

        // bulld the GA list
        ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();

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

    public boolean isIgnoreConnectorUserUpdates() {
        return ignoreConnectorUserUpdates;
    }

    public void setIgnoreConnectorUserUpdates(boolean ignoreConnectorUserUpdates) {
        this.ignoreConnectorUserUpdates = ignoreConnectorUserUpdates;
    }

}
