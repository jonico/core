package com.collabnet.ccf.ist;

import java.text.DateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.Component;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ObjectFactory;

public class ISTWriter extends AbstractWriter<ISTConnection> {

    private static final Log           log                  = LogFactory
                                                                    .getLog(ISTWriter.class);

    private static final Log           logConflictResolutor = LogFactory
                                                                    .getLog("com.collabnet.ccf.core.conflict.resolution");

    private String                     serverUrl            = null;
    private String                     username             = null;
    private String                     password             = null;

    private ISTHandler                 handler              = null;
    private static final DateFormat    df                   = GenericArtifactHelper.df;
    private static final ObjectFactory of                   = new ObjectFactory();

    public ISTWriter() {
    }

    private ISTConnection connect(GenericArtifact ga) {
        String targetSystemId = ga.getTargetSystemId();
        String targetSystemKind = ga.getTargetSystemKind();
        String targetRepositoryId = ga.getTargetRepositoryId();
        String targetRepositoryKind = ga.getTargetRepositoryKind();
        ISTConnection connection = null;

        String credentialInfo = getUsername()
                + ISTConnectionFactory.PARAM_DELIMITER + getPassword();
        String connectionInfo = getServerUrl()
                + ISTConnectionFactory.CONNECTION_INFO_DELIMITER
                + targetRepositoryId;

        try {
            if (ga.getArtifactAction().equals(
                    GenericArtifact.ArtifactActionValue.CREATE)) {
                connection = getConnectionManager()
                        .getConnectionToCreateArtifact(
                                targetSystemId,
                                targetSystemKind,
                                targetRepositoryId,
                                targetRepositoryKind,
                                connectionInfo,
                                credentialInfo);
            } else {
                connection = getConnectionManager()
                        .getConnectionToUpdateOrExtractArtifact(
                                targetSystemId,
                                targetSystemKind,
                                targetRepositoryId,
                                targetRepositoryKind,
                                connectionInfo,
                                credentialInfo);
            }
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

        return connection;

    }

    @Override
    public Document createArtifact(Document doc) {
        GenericArtifact ga = new GenericArtifact();

        try {
            ga = GenericArtifactHelper.createGenericArtifactJavaObject(doc);
        } catch (GenericArtifactParsingException e) {
            String cause = "Problem occured while parsing the GenericArtifact into Document";
            log.error(
                    cause,
                    e);
            XPathUtils.addAttribute(
                    doc.getRootElement(),
                    GenericArtifactHelper.ERROR_CODE,
                    GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
            throw new CCFRuntimeException(cause, e);
        }

        ISTConnection connection = null;
        try {
            connection = connect(ga);
            ISTIncident ri = new ISTIncident(connection.getService(), ga);

            // fill in missing Target values
            ga.setTargetArtifactId(String.valueOf(ri.getId()));
            ga.setTargetArtifactLastModifiedDate(df.format(ri
                    .getLastUpdateDate()));
            ga.setTargetArtifactVersion(String.valueOf(ri.getVersion()));

            log.trace("Generic Artifact XML for incident #" + ri.getId()
                    + ":\n"
                    + GenericArtifactHelper.createGenericArtifactXMLDocument(
                            ga).asXML());
        } catch (GenericArtifactParsingException e) {
            String cause = "Could not convert generic artifact to XML for logging.";
            log.warn(
                    cause,
                    e);
        } finally {
            // release connection to pool
            getConnectionManager().releaseConnection(
                    connection);
        }
        return this.GAtoDocument(ga);
    }

    @Override
    public Document[] createAttachment(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document createDependency(Document gaDocument) {
        // not implemented
        return null;
    }

    @Override
    public Document deleteArtifact(Document gaDocument) {
        // not implemented
        return null;
    }

    @Override
    public Document[] deleteAttachment(Document gaDocument) {
        // not implemented
        return null;
    }

    @Override
    public Document deleteDependency(Document gaDocument) {
        // not implemented
        return null;
    }

    private Document GAtoDocument(GenericArtifact ga) {
        Document document = null;
        try {
            document = GenericArtifactHelper
                    .createGenericArtifactXMLDocument(ga);
        } catch (GenericArtifactParsingException e) {
            String cause = "Problem occured while parsing the GenericArtifact into Document";
            log.error(
                    cause,
                    e);
            ga.setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
            throw new CCFRuntimeException(cause, e);
        }
        return document;
    }

    public String getPassword() {
        return password;
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

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setUsername(String newval) {
        this.username = newval;
    }

    @Override
    public Document updateArtifact(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document updateAttachment(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document updateDependency(Document gaDocument) {
        // not implemented
        return null;
    }

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
