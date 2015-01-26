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
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.core.utils.XPathUtils;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ObjectFactory;

public class ISTWriter extends AbstractWriter<ISTConnection> {

    private static final Log           log                      = LogFactory
            .getLog(ISTWriter.class);

    private static final Log           logConflictResolutor     = LogFactory
            .getLog(ISTWriter.class);

    private String                     serverUrl                = null;
    private String                     username                 = null;
    private String                     password                 = null;

    private ISTHandler                 handler                  = null;
    private ISTMetaCache               meta                     = null;
    private static final DateFormat    df                       = GenericArtifactHelper.df;
    private static final ObjectFactory of                       = new ObjectFactory();

    private boolean                    useExtendedHashLogging   = false;
    private boolean                    useExtendedUpdateLogging = false;
    private boolean                    useExtendedCreateLogging = false;

    public ISTWriter() {
    }

    private GenericArtifact buildGaForAttachmentParent(
            GenericArtifact attachmentGA, ISTIncident parentIncident) {

        GenericArtifact parentArtifact = new GenericArtifact();
        // make sure that we do not update the synchronization status record
        // for replayed attachments
        parentArtifact.setTransactionId(attachmentGA.getTransactionId());
        parentArtifact
                .setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
        parentArtifact
                .setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
        parentArtifact
                .setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
        parentArtifact.setConflictResolutionPriority(attachmentGA
                .getConflictResolutionPriority());
        parentArtifact.setSourceArtifactId(attachmentGA
                .getDepParentSourceArtifactId());
        parentArtifact.setSourceArtifactLastModifiedDate(attachmentGA
                .getSourceArtifactLastModifiedDate());
        parentArtifact.setSourceArtifactVersion(attachmentGA
                .getSourceArtifactVersion());
        parentArtifact.setSourceRepositoryId(attachmentGA
                .getSourceRepositoryId());
        parentArtifact.setSourceSystemId(attachmentGA.getSourceSystemId());
        parentArtifact.setSourceSystemKind(attachmentGA.getSourceSystemKind());
        parentArtifact.setSourceRepositoryKind(attachmentGA
                .getSourceRepositoryKind());
        parentArtifact.setSourceSystemTimezone(attachmentGA
                .getSourceSystemTimezone());

        parentArtifact.setTargetArtifactId(attachmentGA
                .getDepParentTargetArtifactId());
        parentArtifact.setTargetArtifactLastModifiedDate(df
                .format(parentIncident.getLastUpdateDate()));
        parentArtifact.setTargetArtifactVersion(Long.toString(parentIncident
                .getVersion()));
        parentArtifact.setTargetRepositoryId(attachmentGA
                .getTargetRepositoryId());
        parentArtifact.setTargetRepositoryKind(attachmentGA
                .getTargetRepositoryKind());
        parentArtifact.setTargetSystemId(attachmentGA.getTargetSystemId());
        parentArtifact.setTargetSystemKind(attachmentGA.getTargetSystemKind());
        parentArtifact.setTargetSystemTimezone(attachmentGA
                .getTargetSystemTimezone());

        return parentArtifact;
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
            this.meta = new ISTMetaCache(connection.getService());

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
            ISTIncident ri = new ISTIncident(connection.getService(), this.meta);
            ri.createIncident(ga);
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

    /**
     * returns array of two Document objects:
     *
     * @- [0] = attachment Document
     * @- [1] = parent Artifact Document
     *
     */
    @Override
    public Document[] createAttachment(Document doc) {

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

        ISTConnection connection = connect(ga);
        ISTIncident incident = null;
        try {

            ISTAttachmentHandler ath = new ISTAttachmentHandler();
            incident = new ISTIncident(connection.getService(), this.meta);
            incident.retrieveIncident(Integer.valueOf(ga
                    .getDepParentTargetArtifactId()));
            ath.handleAttachment(
                    connection,
                    ga,
                    incident);
        } finally {
            getConnectionManager().releaseConnection(
                    connection);
        }
        Document attachmentDoc;
        Document parentDoc;

        try {
            attachmentDoc = GenericArtifactHelper
                    .createGenericArtifactXMLDocument(ga);
        } catch (GenericArtifactParsingException e1) {
            String cause = "Problem occured while converting attachment GenericArtifact to Document";
            log.error(
                    cause,
                    e1);
            throw new CCFRuntimeException(cause, e1);
        }

        GenericArtifact parentGA = this.buildGaForAttachmentParent(
                ga,
                incident);
        try {
            parentDoc = GenericArtifactHelper
                    .createGenericArtifactXMLDocument(parentGA);
        } catch (GenericArtifactParsingException e) {
            String cause = "Problem occured while converting attachment parent GenericArtifact to Document";
            log.error(
                    cause,
                    e);
            throw new CCFRuntimeException(cause, e);
        }

        return new Document[] { attachmentDoc, parentDoc };
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

    private void disconnect(ISTConnection connection) {
        getConnectionManager().releaseConnection(
                connection);
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

    public boolean isUseExtendedCreateLogging() {
        return useExtendedCreateLogging;
    }

    public void setPassword(String password) {
        this.password = Obfuscator.deObfuscatePassword(password);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setUseExtendedCreateLogging(boolean useExtendedCreateLogging) {
        this.useExtendedCreateLogging = useExtendedCreateLogging;
    }

    public void setUseExtendedHashLogging(boolean useExtendedHashLogging) {
        this.useExtendedHashLogging = useExtendedHashLogging;
    }

    public void setUseExtendedUpdateLogging(boolean useExtendedUpdateLogging) {
        this.useExtendedUpdateLogging = useExtendedUpdateLogging;
    }

    public void setUsername(String newval) {
        this.username = newval;
    }

    @Override
    public Document updateArtifact(Document gaDocument) {

        GenericArtifact ga = null;
        try {
            ga = GenericArtifactHelper
                    .createGenericArtifactJavaObject(gaDocument);
        } catch (GenericArtifactParsingException e) {
            String cause = "Problem occured while parsing the GenericArtifact into Document";
            log.error(
                    cause,
                    e);
            XPathUtils.addAttribute(
                    gaDocument.getRootElement(),
                    GenericArtifactHelper.ERROR_CODE,
                    GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
            throw new CCFRuntimeException(cause, e);
        }

        ISTConnection connection = connect(ga);

        try {
            ISTIncident incident = new ISTIncident(connection.getService(),
                    this.meta);
            incident.retrieveIncident(Integer.valueOf(ga.getTargetArtifactId()));

            incident.updateIncident(ga);

            if (!AbstractWriter.handleConflicts(
                    incident.getVersion(),
                    ga)) {
                return null;
            }

            // increment the version count
            String knownSVersion = ga.getTargetArtifactVersion();
            if (knownSVersion != null) {
                int count = ISTVersion
                        .getCountPart(Long.valueOf(knownSVersion));
                incident.setVersionCount(count);
            }
            incident.incrementVersionCount();
            ga.setTargetArtifactVersion(String.valueOf(incident.getVersion()));
            ga.setTargetArtifactLastModifiedDate(df.format(incident
                    .getLastUpdateDate()));

            log.debug(String
                    .format(
                            "updated incident #%d, version = %d, count.hash = %s, modified = %s",
                            incident.getId(),
                            incident.getVersion(),
                            incident.getVersionInfoString(),
                            ga.getTargetArtifactLastModifiedDate()));
        } finally {
            disconnect(connection);
        }

        Document ret = null;
        try {
            ret = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
        } catch (GenericArtifactParsingException e1) {
            String cause = "Problem occured while converting GenericArtifact back to Document";
            log.error(
                    cause,
                    e1);
            throw new CCFRuntimeException(cause, e1);
        }

        return ret;
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

        // set extended logging
        ISTIncident.setUseExtendedCreateLogging(useExtendedCreateLogging);
        ISTIncident.setUseExtendedHashLogging(useExtendedHashLogging);
        ISTIncident.setUseExtendedUpdateLogging(useExtendedUpdateLogging);

        log.info("===========================================================");
        log.info("started SpiraTest Writer " + ISTVersionInfo.getVersion());

    }

    private void validateNotNull(Object toValidate, String cause,
            Component component, List<ValidationException> exceptions) {
        if (toValidate == null) {
            log.error(cause);
            exceptions.add(new ValidationException(cause, component));
        }

    }

}
