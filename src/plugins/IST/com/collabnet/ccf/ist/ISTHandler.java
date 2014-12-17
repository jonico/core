package com.collabnet.ccf.ist;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteFilter;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ObjectFactory;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteSort;

public class ISTHandler {

    class ArtifactStateComparator implements Comparator<ArtifactState> {

        @Override
        public int compare(ArtifactState as1, ArtifactState as2) {
            return as2.getArtifactLastModifiedDate().compareTo(
                    as1.getArtifactLastModifiedDate());
        }
    }

    public static ArrayOfRemoteIncident getIncidentsSorted(
            ISTConnection connection) {
        objectFactory.createArrayOfRemoteIncident();
        ArrayOfRemoteFilter filters = objectFactory.createArrayOfRemoteFilter();
        RemoteSort sort = objectFactory.createRemoteSort();
        sort.setPropertyName(objectFactory
                .createRemoteFilterPropertyName("LastUpdateDate"));
        sort.setSortAscending(false);

        try {
            return connection.getService().incidentRetrieve(
                    filters,
                    sort,
                    0,
                    Integer.MAX_VALUE);
        } catch (IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Failed to retrieve indicents!",
                    e);
        }

        return null;

    }

    /*
     * Converts XMLGregorianCalendar to java.util.Date in Java
     */
    public static Date toDate(XMLGregorianCalendar calendar) {
        if (calendar == null) {
            return null;
        }
        return (Date) calendar.toGregorianCalendar().getTime();
    }

    /*
     * Converts java.util.Date to javax.xml.datatype.XMLGregorianCalendar
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(
                            gCalendar);
        } catch (DatatypeConfigurationException ex) {
            log.error(
                    "Failed to convert Date to XML Gregorian: "
                            + date.toString(),
                            ex);
        }
        return xmlCalendar;
    }

    private static final DateFormat    df            = GenericArtifactHelper.df;

    private ISTConnection              connection    = null;

    private static final Log           log           = LogFactory
            .getLog(ISTHandler.class);

    private static final ObjectFactory objectFactory = new ObjectFactory();

    private ISTMetaData                metaHelper    = null;

    private ISTArtifactVersionHelper   vHelper       = null;

    private ArrayOfRemoteFilter        nofilters     = objectFactory
                                                             .createArrayOfRemoteFilter();

    private RemoteSort                 dateSort      = objectFactory
                                                             .createRemoteSort();

    private RemoteSort                 noSort        = objectFactory
                                                             .createRemoteSort();

    public ISTHandler(ISTConnection conn) {
        this.connection = conn;
        this.metaHelper = new ISTMetaData(conn);
        this.vHelper = new ISTArtifactVersionHelper(this.metaHelper);
        // Query Helpers
        dateSort.setPropertyName(objectFactory
                .createRemoteFilterPropertyName("LastUpdateDate"));
        dateSort.setSortAscending(false);

    }

    private void addGAField(GenericArtifact ga, String label, Object value,
            FieldValueTypeValue fieldValueType) {
        addGAField(
                ga,
                label,
                value,
                fieldValueType,
                GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
    }

    private void addGAField(GenericArtifact ga, String label, Object value,
            FieldValueTypeValue fieldValueType, boolean isMandatory) {
        if (isMandatory) {
            addGAField(
                    ga,
                    label,
                    value,
                    fieldValueType,
                    GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
        } else {
            addGAField(
                    ga,
                    label,
                    value,
                    fieldValueType,
                    GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
        }
    }

    private void addGAField(GenericArtifact ga, String label, Object value,
            FieldValueTypeValue fieldValueType, String valueFieldType) {
        GenericArtifactField gaf = ga.addNewField(
                label,
                valueFieldType);
        gaf.setFieldValueType(fieldValueType);
        gaf.setFieldAction(FieldActionValue.REPLACE);
        gaf.setFieldValue(value);
    }

    void debugAllIncidents(ISTConnection connection) {
        if (log.isDebugEnabled() || log.isTraceEnabled()) {
            ArrayOfRemoteIncident allIncidents = ISTHandler
                    .getIncidentsSorted(connection);
            log.debug("retrieved " + allIncidents.getRemoteIncident().size()
                    + " incidents");
            if (log.isTraceEnabled()
                    && allIncidents.getRemoteIncident().size() > 0) {
                log.trace(String.format(
                        "  ID   %-23s  %-30s",
                        "Last Update",
                        "Name"));
                for (RemoteIncident in : allIncidents.getRemoteIncident()) {
                    //                log.debug(in.getIncidentId().getValue() + "  "
                    //                        + in.getLastUpdateDate().toString() + " - "
                    //                        + in.getName().getValue());
                    log.trace(String.format(
                            "  %3d  %-23s  %-30s",
                            in.getIncidentId().getValue(),
                            df.format(getRealLastUpdated(in)),
                            in.getName().getValue()));
                }
            }
        }
    }

    /**
     * Feeds all comment of ri since lastModifiedDate into ga Also returns a
     * concatenation of all comments
     *
     * @param ga
     * @param ri
     * @param lastModifiedDate
     * @return String
     */
    private String fetchComments(GenericArtifact ga, RemoteIncident ri,
            Date lastModifiedDate) {

        ArrayOfRemoteComment comments = null;
        try {
            comments = connection.getService().incidentRetrieveComments(
                    ri.getIncidentId().getValue());
        } catch (IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Failed to retrieve comments for incident #"
                            + ri.getIncidentId().getValue(),
                            e);
        }

        for (RemoteComment c : comments.getRemoteComment()) {
            Date creationDate = toDate(c.getCreationDate().getValue());
            String comment = c.getText().getValue();
            comment = comment.length() > 20 ? comment.substring(
                    0,
                    20) + "(...)" : comment;

            // TODO use a HasMap to order comments correctly by Date
            log.debug("      " + df.format(creationDate) + "  C: " + comment);
            if (creationDate.after(lastModifiedDate)) {
                addGAField(
                        ga,
                        "comments",
                        c.getText().getValue(),
                        FieldValueTypeValue.HTMLSTRING);
            }
        }

        return vHelper.generateCommentsDump(comments);

    }

    private String fetchCustoms(GenericArtifact ga, RemoteIncident ri) {
        ArrayOfRemoteArtifactCustomProperty properties = ri
                .getCustomProperties().getValue();

        for (RemoteArtifactCustomProperty prop : properties
                .getRemoteArtifactCustomProperty()) {
            String name = metaHelper.getName(prop);
            String value = metaHelper.getValue(prop);
            addGAField(
                    ga,
                    name,
                    value,
                    FieldValueTypeValue.STRING,
                    false);
        }

        return vHelper.generateCustomsDump(properties);

    }

    /**
     * fills ga with all available data from the RemoteIncident. Also:
     * setSourceArtifactId, setSourceArtifactLastModifiedDate,
     * setSourceArtifactVersion
     *
     * @param ga
     * @param ri
     */
    private void fetchMandatoryData(GenericArtifact ga, RemoteIncident ri) {

        // artifact ID
        ga.setSourceArtifactId(String.valueOf(ri.getIncidentId().getValue()));

        // last modified date
        ga.setSourceArtifactLastModifiedDate(df.format(getRealLastUpdated(ri)));

        // FIXME: is this ok?
        ga.setSourceArtifactVersion("100");

        // type ID
        int riTypeId = ri.getArtifactTypeId();
        addGAField(
                ga,
                "artifactTypeId",
                String.valueOf(riTypeId),
                FieldValueTypeValue.STRING);

        // Closed Date
        addGAField(
                ga,
                "closedDate",
                toDate(ri.getClosedDate().getValue()),
                FieldValueTypeValue.DATETIME);

        // CompletionPercent
        addGAField(
                ga,
                "completionPercent",
                String.valueOf(ri.getCompletionPercent()),
                FieldValueTypeValue.STRING);

        // ConcurrencyDate
        addGAField(
                ga,
                "concurrencyDate",
                toDate(ri.getConcurrencyDate()),
                FieldValueTypeValue.DATETIME);

        // getCreationDate
        addGAField(
                ga,
                "creationDate",
                toDate(ri.getCreationDate().getValue()),
                FieldValueTypeValue.DATETIME);

        // Description
        addGAField(
                ga,
                "description",
                ri.getDescription().getValue(),
                FieldValueTypeValue.HTMLSTRING);

        // DetectedReleaseId
        addGAField(
                ga,
                "detectedReleaseId",
                ri.getDetectedReleaseId().getValue(),
                FieldValueTypeValue.STRING);

        // DetectedReleaseVersionNumber
        addGAField(
                ga,
                "detectedReleaseVersionNumber",
                ri.getDetectedReleaseVersionNumber().getValue(),
                FieldValueTypeValue.STRING);

        // EstimatedEffort
        addGAField(
                ga,
                "estimatedEffort",
                ri.getEstimatedEffort().getValue(),
                FieldValueTypeValue.INTEGER);

        // FixedBuildId
        addGAField(
                ga,
                "fixedBuildId",
                String.valueOf(ri.getFixedBuildId().getValue()),
                FieldValueTypeValue.STRING);

        // FixedBuildName
        addGAField(
                ga,
                "fixedBuildName",
                String.valueOf(ri.getFixedBuildName().getValue()),
                FieldValueTypeValue.STRING);

        // IncidentId
        addGAField(
                ga,
                "incidentId",
                String.valueOf(ri.getIncidentId().getValue()),
                FieldValueTypeValue.STRING);

        // IncidentStatusId
        addGAField(
                ga,
                "incidentStatusId",
                String.valueOf(ri.getIncidentStatusId().getValue()),
                FieldValueTypeValue.STRING);

        // IncidentStatusName
        addGAField(
                ga,
                "incidentStatusName",
                ri.getIncidentStatusName().getValue(),
                FieldValueTypeValue.STRING);

        // IncidentStatusOpenStatus
        addGAField(
                ga,
                "incidentStatusOpenStatus",
                String.valueOf(ri.getIncidentStatusOpenStatus().getValue()),
                FieldValueTypeValue.STRING);

        // IncidentTypeId
        addGAField(
                ga,
                "incidentTypeId",
                String.valueOf(ri.getIncidentTypeId().getValue()),
                FieldValueTypeValue.STRING);

        // IncidentTypeName
        addGAField(
                ga,
                "incidentTypeName",
                ri.getIncidentTypeName().getValue(),
                FieldValueTypeValue.STRING);

        // Name
        addGAField(
                ga,
                "name",
                ri.getName().getValue(),
                FieldValueTypeValue.STRING);

        // OpenerId
        addGAField(
                ga,
                "openerId",
                String.valueOf(ri.getOpenerId().getValue()),
                FieldValueTypeValue.STRING);

        // OpenerName
        addGAField(
                ga,
                "openerName",
                ri.getOpenerName().getValue(),
                FieldValueTypeValue.STRING);

        // OwnerId
        addGAField(
                ga,
                "ownerId",
                String.valueOf(ri.getOwnerId().getValue()),
                FieldValueTypeValue.STRING);

        // PriorityId
        addGAField(
                ga,
                "priorityId",
                String.valueOf(ri.getPriorityId().getValue()),
                FieldValueTypeValue.STRING);

        // PriorityName
        addGAField(
                ga,
                "priorityName",
                ri.getPriorityName().getValue(),
                FieldValueTypeValue.STRING);

        // ProjectedEffort
        addGAField(
                ga,
                "projectedEffort",
                String.valueOf(ri.getProjectedEffort().getValue()),
                FieldValueTypeValue.STRING);

        // ProjectId
        addGAField(
                ga,
                "projectId",
                String.valueOf(ri.getProjectId().getValue()),
                FieldValueTypeValue.STRING);

        // ProjectName
        addGAField(
                ga,
                "projectName",
                ri.getProjectName().getValue(),
                FieldValueTypeValue.STRING);

        // RemainingEffort
        addGAField(
                ga,
                "remainingEffort",
                String.valueOf(ri.getRemainingEffort().getValue()),
                FieldValueTypeValue.STRING);

        // ResolvedReleaseId
        addGAField(
                ga,
                "resolvedReleaseId",
                String.valueOf(ri.getResolvedReleaseId().getValue()),
                FieldValueTypeValue.STRING);

        // ResolvedReleaseVersionNumber
        addGAField(
                ga,
                "resolvedReleaseVersionNumber",
                ri.getResolvedReleaseVersionNumber().getValue(),
                FieldValueTypeValue.STRING);

        // SeverityId
        addGAField(
                ga,
                "severityId",
                String.valueOf(ri.getSeverityId().getValue()),
                FieldValueTypeValue.STRING);

        // SeverityName
        addGAField(
                ga,
                "severityName",
                ri.getSeverityName().getValue(),
                FieldValueTypeValue.STRING);

        // StartDate
        addGAField(
                ga,
                "startDate",
                toDate(ri.getStartDate().getValue()),
                FieldValueTypeValue.DATETIME);

        // TestRunStepId
        addGAField(
                ga,
                "testRunStepId",
                String.valueOf(ri.getTestRunStepId().getValue()),
                FieldValueTypeValue.STRING);

        // VerifiedReleaseId
        addGAField(
                ga,
                "verifiedReleaseId",
                String.valueOf(ri.getVerifiedReleaseId().getValue()),
                FieldValueTypeValue.STRING);

        // VerifiedReleaseVersionNumber
        addGAField(
                ga,
                "verifiedReleaseVersionNumber",
                ri.getVerifiedReleaseVersionNumber().getValue(),
                FieldValueTypeValue.STRING);

    }

    public Date getRealLastUpdated(RemoteIncident incident) {

        ArrayOfRemoteFilter filters = objectFactory.createArrayOfRemoteFilter();
        RemoteSort sort = objectFactory.createRemoteSort();

        Date ret = toDate(incident.getLastUpdateDate());

        ArrayOfRemoteDocument documents = null;
        try {
            documents = connection.getService().documentRetrieveForArtifact(
                    incident.getArtifactTypeId(),
                    incident.getIncidentId().getValue(),
                    filters,
                    sort);
        } catch (IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Failed to retrieve documents for incident #"
                            + incident.getIncidentId().getValue(),
                            e);
        }

        for (RemoteDocument d : documents.getRemoteDocument()) {
            Date uploadDate = toDate(d.getUploadDate());
            //            log.debug("       " + df.format(uploadDate) + "  F: "
            //                    + d.getFilenameOrUrl().getValue());
            ret = uploadDate.after(ret) ? uploadDate : ret;
        }

        // get comments
        ArrayOfRemoteComment comments = null;
        try {
            comments = connection.getService().incidentRetrieveComments(
                    incident.getIncidentId().getValue());
        } catch (IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Failed to retrieve comments for incident #"
                            + incident.getIncidentId().getValue(),
                            e);
        }

        for (RemoteComment c : comments.getRemoteComment()) {
            Date creationDate = toDate(c.getCreationDate().getValue());
            //            String comment = c.getText().getValue();
            //            comment = comment.length() > 20 ? comment.substring(0, 20)
            //                    + "(...)" : comment;
            //
            //            log.debug("      " + df.format(creationDate) + "  C: " + comment);
            ret = creationDate.after(ret) ? creationDate : ret;

        }

        if (!ret.equals(toDate(incident.getLastUpdateDate()))) {
            log.trace("     patched Last Update Date for incident #"
                    + incident.getIncidentId().getValue() + ": "
                    + df.format(toDate(incident.getLastUpdateDate())) + " => "
                    + df.format(ret));
        }
        return ret;
    }

    private boolean isUntouchedSinceLastVisit(String gaVersion,
            RemoteIncident ri, String commentsDump, String documentsDump,
            String customsDump) {

        long actualVersion = ISTArtifactVersionHelper.generateHash(
                ri,
                commentsDump,
                documentsDump,
                customsDump);

        int gaHash = ISTArtifactVersionHelper.getHashPart(Long
                .valueOf(gaVersion));
        int currenHash = ISTArtifactVersionHelper.getHashPart(actualVersion);

        return gaHash == currenHash;
    }

    public void retrieveChangedIncidents(final Date lastModifiedDate,
            String lastSynchronizedVersion, String lastSynchedArtifactId,
            ArrayList<ArtifactState> artifactStates) {

        ArrayOfRemoteIncident allincidents = null;
        objectFactory.createArrayOfRemoteIncident();

        try {
            allincidents = connection.getService().incidentRetrieve(
                    this.nofilters,
                    this.dateSort,
                    0,
                    Integer.MAX_VALUE);
        } catch (IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Failed to retrieve indicents!",
                    e);
        }

        Long.valueOf(lastSynchronizedVersion);

        // fetch *all* incidents
        // currently the only way to identify all changed ones and to build the version
        for (RemoteIncident inc : allincidents.getRemoteIncident()) {
            ArtifactState xs = new ArtifactState();
            xs.setArtifactId(String.valueOf(inc.getIncidentId().getValue()));
            Date lastUpdated = toDate(inc.getLastUpdateDate());

            ArrayOfRemoteDocument documents = null;
            documents = retrieveDocuments(inc);

            String attachmentsDump = vHelper.generateAttachmentsDump(documents);
            for (RemoteDocument d : documents.getRemoteDocument()) {
                Date uploadDate = toDate(d.getUploadDate());
                lastUpdated = uploadDate.after(lastUpdated) ? uploadDate
                        : lastUpdated;
            }

            ArrayOfRemoteComment comments = null;
            try {
                comments = connection.getService().incidentRetrieveComments(
                        inc.getIncidentId().getValue());
            } catch (IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage e) {
                log.error(
                        "Failed to retrieve comments for incident #"
                                + inc.getIncidentId().getValue(),
                                e);
            }

            String commentsDump = vHelper.generateCommentsDump(comments);

            ArrayOfRemoteArtifactCustomProperty properties = null;
            properties = inc.getCustomProperties().getValue();

            if (properties.getRemoteArtifactCustomProperty().size() > 0) {
                log.trace("  ==> Custom Properties for ID #"
                        + inc.getIncidentId().getValue());
                log.trace(String.format(
                        "      %-30s  %-15s  %-15s  %-15s",
                        "Field Name",
                        "System Type",
                        "Field Type",
                        "Value"));
            }
            for (RemoteArtifactCustomProperty aprop : properties
                    .getRemoteArtifactCustomProperty()) {
                RemoteCustomProperty propDef = aprop.getDefinition().getValue();
                String fieldType = propDef.getCustomPropertyTypeName()
                        .getValue();
                String systemType = propDef.getSystemDataType().getValue();
                String fieldName = metaHelper.getName(aprop);
                String value = metaHelper.getValue(aprop);
                log.trace(String.format(
                        "      %-30s  %-15s  %-15s  %-15s",
                        fieldName,
                        systemType,
                        fieldType,
                        value));
            }

            String customsDump = vHelper.generateCustomsDump(properties);

            xs.setArtifactLastModifiedDate(lastUpdated);

            int riHash = ISTArtifactVersionHelper.generateHash(
                    inc,
                    commentsDump,
                    attachmentsDump,
                    customsDump);

            int lastVersion = ISTArtifactVersionHelper.getVersionPart(Long
                    .valueOf(lastSynchronizedVersion));

            long fullVersion = ISTArtifactVersionHelper.getFullVersion(
                    lastVersion,
                    riHash);

            fullVersion = ISTArtifactVersionHelper
                    .getIncrementedVersion(fullVersion);

            xs.setArtifactVersion(fullVersion);
            artifactStates.add(xs);
        }

        // update sort order
        Collections.sort(
                artifactStates,
                new ArtifactStateComparator());

        int totalLength = artifactStates.size();

        // remove items before Last Modified Date
        CollectionUtils.filter(
                artifactStates,
                new Predicate() {

                    @Override
                    public boolean evaluate(Object o) {
                        return ((Date) ((ArtifactState) o)
                                .getArtifactLastModifiedDate())
                                .after(lastModifiedDate);
                    }
                });

        int filterLength = artifactStates.size();

        log.debug("Filtered " + filterLength + " artifacts out of "
                + totalLength);

    }

    private ArrayOfRemoteDocument retrieveDocuments(RemoteIncident inc) {
        try {
            ArrayOfRemoteDocument documents = connection.getService()
                    .documentRetrieveForArtifact(
                            inc.getArtifactTypeId(),
                            inc.getIncidentId().getValue(),
                            nofilters,
                            noSort);
            return documents;
        } catch (IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Failed to retrieve documents for incident #"
                            + inc.getIncidentId().getValue(),
                            e);
        }
        return null;
    }

    public void retrieveIncident(int incidentId, Date lastModifiedDate,
            String username, boolean ignoreConnectorUserUpdates,
            GenericArtifact ga, String sourceRepositoryId) {

        try {
            RemoteIncident ri = connection.getService().incidentRetrieveById(
                    incidentId);
            Date riCreationDate = toDate(ri.getCreationDate().getValue());

            ri.getOpenerName().getValue();
            ri.getOwnerName().getValue();

            boolean isResync = false;
            boolean isIgnore = false;

            fetchMandatoryData(
                    ga,
                    ri);

            String commentsDump = fetchComments(
                    ga,
                    ri,
                    lastModifiedDate);

            String customsDump = fetchCustoms(
                    ga,
                    ri);

            String attachmentsDump = vHelper
                    .generateAttachmentsDump(retrieveDocuments(ri));

            // FIXME - use version fingerprint check
            if (isUntouchedSinceLastVisit(
                    ga.getSourceArtifactVersion(),
                    ri,
                    commentsDump,
                    attachmentsDump,
                    customsDump)) {
                if (riCreationDate.after(lastModifiedDate)) {
                    log.info(String
                            .format(
                                    "resync is necessary, despite the artifact #%d last being updated by the connector user",
                                    incidentId));
                    isResync = true;
                } else {
                    log.debug(String.format(
                            "artifact %d updated by CCF, ignoring it.",
                            incidentId));
                    isIgnore = true;
                }
            }

            // TODO check dependencies / parents

            if (isIgnore) {
                ga.setArtifactAction(GenericArtifact.ArtifactActionValue.IGNORE);
            } else {
                if (isResync) {
                    ga.setArtifactAction(GenericArtifact.ArtifactActionValue.RESYNC);
                }
            }

        } catch (IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage e) {
            String cause = "Could not retrieve Incident #" + incidentId
                    + " as user " + username;
            log.error(
                    cause,
                    e);
            throw new CCFRuntimeException(cause, e);
        }

    }
}
