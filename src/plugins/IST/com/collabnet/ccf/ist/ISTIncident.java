package com.collabnet.ccf.ist;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.CRC32;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.utils.JerichoUtils;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExport;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentCreateServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentCreateValidationFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ObjectFactory;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;

/**
 * this class wraps the SpiraTest RemoteIncident, especially the Jax conversions
 *
 * Naming tip:
 *
 * fetch* reads data from Incident into Generic Artifact
 *
 * fill* reads GenericArtifact data and puts it into the Incident
 *
 * @author volker
 *
 */
public class ISTIncident extends ISTVersion {

    private ISTMetaCache                        meta;

    private IImportExport                       soap;
    private RemoteIncident                      incident;

    private static final DateFormat             df                   = GenericArtifactHelper.df;
    private final ObjectFactory                 of                   = new ObjectFactory();

    private static final Log                    log                  = LogFactory
            .getLog(ISTIncident.class);
    private final String                        DUMPSEPARATOR        = "::";
    private final String                        DUMPNAMEVALSEPARATOR = "=";

    private final String                        EMPTYDUMPVAL         = "null";
    private ArrayOfRemoteComment                comments             = null;
    private String                              commentsDump         = null;

    private ArrayOfRemoteDocument               attachments          = null;

    private String                              attachmentsDump      = null;

    private ArrayOfRemoteArtifactCustomProperty customs              = null;

    private String                              customsDump          = null;

    private String                              mandatoryDump        = null;

    private Date                                lastUpdated          = null;

    private boolean                             hasPatchedUpdate     = false;

    /**
     * creates a new incident based on the the data in ga
     *
     * @param service
     * @param ga
     */
    public ISTIncident(IImportExport service, GenericArtifact ga) {
        this.soap = service;
        this.meta = new ISTMetaCache(this.soap, true);
        this.createIncident(ga);
    }

    /**
     * retrieves an existing incident for read access from SpiraTest
     *
     * @param service
     * @param incidentId
     */
    public ISTIncident(IImportExport service, int incidentId, boolean readOnly) {
        this.soap = service;
        this.meta = new ISTMetaCache(this.soap, !readOnly);
        this.reload(incidentId);
    }

    /**
     * instantiates ISTIncident for write access
     *
     * @param service
     * @param ri
     */
    public ISTIncident(IImportExport service, RemoteIncident ri) {
        this.soap = service;
        this.incident = ri;
        this.meta = new ISTMetaCache(this.soap, true);

    }

    /**
     * creates a new incident based on the the data contained in ga
     *
     * @param ga
     */
    private void createIncident(GenericArtifact ga) {

        if ((ga.getAllGenericArtifactFieldsWithSameFieldName(
                ISTMandatoryFieldType.Name.name()).size() == 0)
                || (ga.getAllGenericArtifactFieldsWithSameFieldName(
                        ISTMandatoryFieldType.Description.name()).size() == 0)) {
            String cause = "Name and/or Description are missing (or send multiple times), please check that your XSL sends `Name` and `Description` fields, each exactly once!";
            throw new CCFRuntimeException(cause);
        }

        this.incident = new RemoteIncident();

        this.fillMandatoryFields(ga);
        this.fillCustoms(ga);
        this.fillComments(ga);

        try {
            this.incident = this.soap.incidentCreate(this.incident);
            log.info("Created new incident #" + this.getId());

            this.reload(this.getId());

        } catch (IImportExportIncidentCreateServiceFaultMessageFaultFaultMessage e) {
            String cause = "Failed to create new incident based on "
                    + ga.getSourceArtifactId();
            throw new CCFRuntimeException(cause, e);
        } catch (IImportExportIncidentCreateValidationFaultMessageFaultFaultMessage e) {
            String cause = "Could not create new incident based on "
                    + ga.getSourceArtifactId();
            throw new CCFRuntimeException(cause, e);
        }
    }

    protected int determineHash() {
        String fullString = "";
        //        String hashInfo = "empty";
        int retHash = 0;
        long theDigest = 0;

        for (ISTMandatoryFieldType fieldMeta : ISTMandatoryFieldType
                .getIdentifyingMandatoryFields()) {
            Object value = this.getMandatoryFieldValue(fieldMeta);
            fullString += fieldMeta.name() + DUMPSEPARATOR;
            if (value != null) {
                switch (fieldMeta.istFieldValueType()) {
                    case Date:
                        fullString += df.format((Date) value);
                        break;
                    default:
                        fullString += String.valueOf(value);
                }
            }
        }

        // add other identifying information
        fullString += this.getCommentsDump();
        fullString += this.getAttachmentsDump();
        fullString += this.getCustomsDump();

        try {
            CRC32 crc = new CRC32();
            byte[] bytesOfMessage = fullString.getBytes("UTF-8");
            crc.update(bytesOfMessage);
            theDigest = crc.getValue();
            retHash = (int) (Long.valueOf(theDigest) % Math.pow(
                    2,
                    20));

            //            hashInfo = Long.toHexString(theDigest) + "  "
            //                    + Math.abs(Long.valueOf(theDigest).hashCode()) + "  "
            //                    + theVersion;
        } catch (UnsupportedEncodingException e) {
            String cause = "Failed to generate version hash for incident #"
                    + this.incident.getIncidentId().getValue();
            throw new CCFRuntimeException(cause, e);
        }

        return retHash;
    }

    /**
     * read all comments from Incident that were created after last modified
     * date and add them to GA
     *
     * @param ga
     */
    private void fetchComments(GenericArtifact ga, Date lastModifiedDate) {
        int added = 0;
        for (RemoteComment c : this.retrieveAllComments().getRemoteComment()) {
            Date creationDate = ISTHandler.toDate(c.getCreationDate()
                    .getValue());
            // OPTIMIZE use a HasMap to order comments instead of trusting the API ?
            added++;
            if (creationDate.after(lastModifiedDate)) {
                ISTHandler.addGAField(
                        ga,
                        "comments",
                        c.getText().getValue(),
                        FieldValueTypeValue.HTMLSTRING,
                        GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
            }
        }
        log.trace(String.format(
                "  fetched %d comments created since %s",
                added,
                df.format(lastModifiedDate)));
    }

    /**
     * adds the Custom Property and its value, type, etc to the GenericArtifact
     *
     * @param ga
     * @param prop
     */
    private void fetchCustomProperty(GenericArtifact ga,
            RemoteArtifactCustomProperty prop) {

        RemoteCustomProperty propDef = prop.getDefinition().getValue();

        ISTCustomFieldType type = ISTCustomFieldType.valueOf(propDef
                .getCustomPropertyTypeName().getValue());

        String label = prop.getDefinition().getValue().getName().getValue();

        List<Object> values = new ArrayList<Object>();
        FieldValueTypeValue fieldValueTypeValue = FieldValueTypeValue.STRING;

        switch (type) {
            case Text:
                fieldValueTypeValue = FieldValueTypeValue.HTMLSTRING;
                if (prop.getStringValue().getValue() != null)
                    values.add(prop.getStringValue().getValue());
                break;
            case Date:
                fieldValueTypeValue = FieldValueTypeValue.DATETIME;
                if (prop.getDateTimeValue().getValue() != null)
                    values.add(ISTHandler.toDate(prop.getDateTimeValue()
                            .getValue()));
                break;
            case List:
                values.add(this.meta.getSingleListValue(prop));
                break;
            case MultiList:
                ArrayList<String> selected = this.meta.getMultiListValues(prop);
                for (String s : selected)
                    values.add(s);
                break;
            case Integer:
                fieldValueTypeValue = FieldValueTypeValue.STRING;
                values.add(prop.getIntegerValue().getValue());
                break;
        }

        // add new fields to ga
        for (Object value : values) {
            // if value is null, the null
            ISTHandler.addGAField(
                    ga,
                    label,
                    value,
                    fieldValueTypeValue,
                    GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
        }
    }

    /**
     * read all custom fields from RemoteIncident and write to GA
     *
     * @param ga
     */
    private void fetchCustoms(GenericArtifact ga) {
        for (RemoteArtifactCustomProperty prop : this.retrieveAllCustoms()
                .getRemoteArtifactCustomProperty()) {
            this.fetchCustomProperty(
                    ga,
                    prop);
        }
    }

    /**
     * fetch from incident, put data into ga
     *
     * @param ga
     */
    public void fetchIncident(GenericArtifact ga, Date lastModifiedDate) {

        ga.setSourceArtifactId(String.valueOf(this.getId()));
        ga.setSourceArtifactLastModifiedDate(df.format(this.getLastUpdateDate()));

        this.fetchMandatoryFields(ga);
        this.fetchCustoms(ga);
        this.fetchComments(
                ga,
                lastModifiedDate);
    }

    /**
     * fetches data from Incident and put it into ga replaces
     * ISTHandler.fillMandatoryData(GenericArtifact, RemoteIncident)
     *
     * @param ga
     */
    private void fetchMandatoryFields(GenericArtifact ga) {
        for (ISTMandatoryFieldType fieldMeta : ISTMandatoryFieldType.values()) {
            ISTHandler.addGAField(
                    ga,
                    fieldMeta.name(),
                    getMandatoryFieldValue(fieldMeta),
                    fieldMeta.genericFielValueType(),
                    GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
        }
    }

    /**
     * read comments from GA and update the Incident
     *
     * @param ga
     */
    private void fillComments(GenericArtifact ga) {

    }

    /**
     * read custom properties from GA and set the values on the Incident
     *
     * @param ga
     */
    private void fillCustoms(GenericArtifact ga) {

        for (RemoteArtifactCustomProperty prop : this.customs
                .getRemoteArtifactCustomProperty()) {
            // get custom field value from ga

            String fieldName = prop.getDefinition().getValue().getName()
                    .getValue();

            List<GenericArtifactField> fields = ga
                    .getAllGenericArtifactFieldsWithSameFieldName(fieldName);

            GenericArtifactField gaField = null;
            if (fields != null) {
                gaField = fields.get(0);
                if (gaField != null) {
                    switch (gaField.getFieldValueType()) {
                        case DATETIME:
                        case HTMLSTRING:
                        case USER: // currenty not implemented as target, does into texts
                        case STRING: // Need to determine the target type, Strings can also be sent to lists and booleans!
                        case INTEGER:
                        case BOOLEAN:
                            this.setCustomPropertyValue(
                                    prop,
                                    String.valueOf(gaField.getFieldValue()));
                            break;
                        case DATE:
                            log.warn("Ignoring unsupported Date field value for field "
                                    + this.getId() + "." + fieldName);
                            break;
                        case BASE64STRING:
                            log.warn("Ignoring unsupported Base64 field value for field "
                                    + this.getId() + "." + fieldName);
                            break;
                        case DOUBLE:
                            log.warn("Ignoring unsupported Double field value for field "
                                    + this.getId() + "." + fieldName);
                            break;
                    }
                }

            } else {
                log.trace("Custom field `" + fieldName
                        + "` is not in received GenericArtifact");
            }
        }

        this.incident.setCustomProperties(ISTMetaCache
                .CreateJAXBArrayOfRemoteArtifactCustomProperty(
                        "CustomProperties",
                        this.customs));
    }

    /**
     * read mandatory fields from GA and write them to the Incident
     *
     * @param ga
     */
    private void fillMandatoryFields(GenericArtifact ga) {
        for (ISTMandatoryFieldType fieldMeta : ISTMandatoryFieldType.values()) {

            // get mandatory field value from ga
            List<GenericArtifactField> fields = ga
                    .getAllGenericArtifactFieldsWithSameFieldName(fieldMeta
                            .name());

            // there are currently no mandatory fields with multiple values
            GenericArtifactField gaField = null;
            if (fields != null) {
                gaField = fields.get(0);

                if (fields.size() > 1) {
                    log.warn("Unxepected number of mandatory values for field `"
                            + fieldMeta.name()
                            + "` found. Chose first item in list. Please validate your XSL.");
                    int index = 0;
                    for (GenericArtifactField gaf : fields) {
                        log.warn(String.format(
                                "index #%2d = %s",
                                ++index,
                                gaf.getFieldValue().toString()));
                    }
                }

                if (gaField != null) {
                    this.setMandatoryFieldValue(
                            fieldMeta,
                            gaField.getFieldValue());

                }
            } else {
                log.trace("Mandatory field `" + fieldMeta.name()
                        + "` is not in received GenericArtifact");
            }

        }
    }

    private String getAttachmentsDump() {
        if (this.attachments == null)
            this.attachments = this.retrieveAllDocuments();

        if (this.attachmentsDump == null) {
            String ret = "";
            for (RemoteDocument doc : this.attachments.getRemoteDocument()) {
                ret += doc.getFilenameOrUrl() + "::" + doc.getArtifactTypeId()
                        + DUMPSEPARATOR
                        + df.format(ISTHandler.toDate(doc.getEditedDate()));
            }
            this.attachmentsDump = ret;
        }
        return this.attachmentsDump;
    }

    private String getCommentsDump() {

        if (this.comments == null)
            this.comments = this.retrieveAllComments();

        if (this.commentsDump == null) {
            String ret = "";
            for (RemoteComment rc : comments.getRemoteComment()) {
                ret += JerichoUtils.htmlToText(rc.getText().getValue())
                        + DUMPSEPARATOR
                        + df.format(ISTHandler.toDate(rc.getCreationDate()
                                .getValue()));
            }
            this.commentsDump = ret;
        }
        return this.commentsDump;
    }

    public Date getCreationDate() {
        return ISTHandler.toDate(this.incident.getCreationDate().getValue());
    }

    private String getCustomsDump() {

        if (this.customs == null) {
            this.customs = this.retrieveAllCustoms();
            String ret = "";
            for (RemoteArtifactCustomProperty prop : this.customs
                    .getRemoteArtifactCustomProperty()) {
                ret += ISTMetaCache.getName(prop) + DUMPNAMEVALSEPARATOR;
                ret += this.getCustomValueAsString(prop) == null ? EMPTYDUMPVAL
                        : this.getCustomValueAsString(prop);
                ret += DUMPSEPARATOR;
            }

            this.customsDump = ret;
        }
        return this.customsDump;
    }

    private String getCustomValueAsString(RemoteArtifactCustomProperty prop) {

        RemoteCustomProperty propDef = prop.getDefinition().getValue();

        ISTCustomFieldType type = ISTCustomFieldType.valueOf(propDef
                .getCustomPropertyTypeName().getValue());
        switch (type) {
            case Text:
                return JerichoUtils
                        .htmlToText(prop.getStringValue().getValue());
            case Date:
                if (prop.getDateTimeValue().getValue() != null)
                    return df.format(ISTHandler.toDate(prop.getDateTimeValue()
                            .getValue()));
                else
                    return null;
            case List:
                return this.meta.getSingleListValue(prop);
            case MultiList:
                ArrayList<String> selected = this.meta.getMultiListValues(prop);
                return "[" + StringUtils.join(
                        selected.toArray(),
                        ",") + "]";
            case Integer:
                return String.valueOf(prop.getIntegerValue().getValue());
            default:
                return null;
        }
    }

    public int getId() {
        return this.incident.getIncidentId().getValue();
    }

    /**
     * determines the last update date by evaluating comments and attachments.
     * Also caches the collections
     *
     * @return
     */
    public Date getLastUpdateDate() {
        if (this.lastUpdated == null) {
            Date lastUpdated = ISTHandler.toDate(this.incident
                    .getLastUpdateDate());
            Date myUpdated = lastUpdated;

            for (RemoteDocument doc : this.retrieveAllDocuments()
                    .getRemoteDocument()) {
                Date uploadDate = ISTHandler.toDate(doc.getEditedDate());
                lastUpdated = uploadDate.after(lastUpdated) ? uploadDate
                        : lastUpdated;
            }

            for (RemoteComment com : this.retrieveAllComments()
                    .getRemoteComment()) {
                Date createdDate = ISTHandler.toDate(com.getCreationDate()
                        .getValue());
                lastUpdated = createdDate.after(lastUpdated) ? createdDate
                        : lastUpdated;
            }

            this.hasPatchedUpdate = (lastUpdated.after(myUpdated));
            this.lastUpdated = lastUpdated;
        }
        return this.lastUpdated;
    }

    /**
     * return basic values as objects, list and multilists are converted to
     * strings
     *
     * @param fieldMeta
     * @return
     */
    @SuppressWarnings("unchecked")
    private Object getMandatoryFieldValue(ISTMandatoryFieldType fieldMeta) {
        String methodName = "unknown";
        String returnType = "unknown";
        Method callee;
        Object value = null;
        try {
            methodName = "get" + fieldMeta.name();
            callee = this.incident.getClass().getMethod(
                    methodName);
            if (fieldMeta.usesJAX()) {
                switch (fieldMeta.istFieldValueType()) {
                    case Date:
                        value = ISTHandler
                        .toDate(((JAXBElement<XMLGregorianCalendar>) callee
                                .invoke(this.incident)).getValue());
                        break;
                    case Integer:
                        value = ((JAXBElement<Integer>) callee
                                .invoke(this.incident)).getValue();
                        break;

                        // the mandatory list fields return a string value for the item name
                    case Text:
                    case List:
                        value = ((JAXBElement<String>) callee
                                .invoke(this.incident)).getValue();
                        break;
                    case Boolean:
                    case MultiList:
                        // TODO
                        break;
                }
            } else {
                // no JAX wrapper; direct method call returns the data already
                switch (fieldMeta.istFieldValueType()) {
                    case Date:
                        value = ISTHandler.toDate((XMLGregorianCalendar) callee
                                .invoke(this.incident));
                        break;
                    case Integer:
                        value = Integer.valueOf(String.valueOf(callee
                                .invoke(this.incident)));
                        break;
                    case Text:
                        value = String.valueOf(callee.invoke(this.incident));
                        break;
                    case List:
                    case Boolean:
                    case MultiList:
                        break;
                }
            }
        } catch (NoSuchMethodException e) {
            String cause = "API Method not found: RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        } catch (IllegalAccessException e) {
            String cause = "Not allowed to access " + returnType
                    + " RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        } catch (InvocationTargetException e) {
            String cause = "Failed to execute " + returnType
                    + " RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        }

        return value;
    }

    /**
     * dumps the incident to the log, also writes TRACE messages
     */
    public void printIncidentInfo(boolean overview) {

        String dateInfo = df.format(this.getLastUpdateDate());
        dateInfo += this.hasPatchedUpdate ? " (*)" : "";
        log.trace("\n====================================================================================="
                + "\n-------------------------------------------------------------------------------------"
                + "\n-------------------------------------------------------------------------------------"
                + "\n=====================================================================================");
        log.debug(String.format(
                "Incident %d `%s` was last updated on %s",
                this.getId(),
                this.getMandatoryFieldValue(ISTMandatoryFieldType.Name),
                dateInfo));
        log.debug("   version: " + this.getVersionString());

        if (!overview) {
            this.traceMandatories();
            this.traceCustoms();
            this.traceAttachments();
        }
    }

    public void reload(int incidentId) {
        try {
            RemoteIncident iload = this.soap.incidentRetrieveById(incidentId);
            log.trace("refreshed incident #" + incidentId);
            this.incident = iload;
        } catch (IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage e) {
            String cause = "Failed to load incident #" + incidentId;
            throw new CCFRuntimeException(cause, e);
        }
    }

    private ArrayOfRemoteComment retrieveAllComments() {
        ArrayOfRemoteComment comments = new ArrayOfRemoteComment();
        try {
            comments = this.soap.incidentRetrieveComments(this.getId());
        } catch (IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Failed to retrieve comments for incident #" + this.getId(),
                    e);
        }

        return comments;
    }

    private ArrayOfRemoteArtifactCustomProperty retrieveAllCustoms() {
        if (this.customs == null) {
            this.customs = this.incident.getCustomProperties().getValue();
        }

        return this.customs;
    }

    /**
     * loads all documents for the incident
     *
     * OPTIMIZE we could use a project wide list of documents - fetched once
     * when connecting - instead
     *
     * @return
     */
    private ArrayOfRemoteDocument retrieveAllDocuments() {
        if (this.attachments == null) {
            this.attachments = new ArrayOfRemoteDocument();
            try {
                ArrayOfRemoteDocument documents = soap
                        .documentRetrieveForArtifact(
                                this.incident.getArtifactTypeId(),
                                this.incident.getIncidentId().getValue(),
                                ISTHandler.UNFILTERED,
                                ISTHandler.UNSORTED);
                this.attachments = documents;
            } catch (IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage e) {
                log.error(
                        "Failed to retrieve documents for incident #"
                                + this.incident.getIncidentId().getValue(),
                                e);
            }
        }
        return this.attachments;
    }

    /**
     * sets the property value according to the data type of the property.
     *
     * @param prop
     * @param value
     */
    private void setCustomPropertyValue(RemoteArtifactCustomProperty prop,
            String value) {

        RemoteCustomProperty propDef = prop.getDefinition().getValue();
        ISTCustomFieldType type = ISTCustomFieldType.valueOf(propDef
                .getCustomPropertyTypeName().getValue());

        switch (type) {
            case Boolean:
                prop.setBooleanValue(ISTMetaCache.CreateJAXBBoolean(
                        "BooleanValue",
                        Boolean.valueOf(value)));
                break;
            case Date:
                try {
                    prop.setDateTimeValue(ISTMetaCache
                            .CreateJAXBXMLGregorianCalendar(
                                    "DateTimeValue",
                                    ISTHandler.toXMLGregorianCalendar(df
                                            .parse(value))));
                } catch (ParseException e) {
                    throw new CCFRuntimeException(
                            "Could not transform date string " + value, e);
                }
                break;
            case Integer:
                prop.setIntegerValue(ISTMetaCache.CreateJAXBInteger(
                        "IntegerValue",
                        Integer.valueOf(value)));
            case List:
                log.warn("Writing to to Single Lists is currently not supported");
                break;
            case MultiList:
                log.warn("Writing to to Multi Lists is currently not supported");
                break;
            case Text:
                prop.setStringValue(ISTMetaCache.CreateJAXBString(
                        "StringValue",
                        value));
                break;

        }
    }

    /**
     * return basic values as objects, list and multilists are converted to
     * strings
     *
     * @param fieldMeta
     * @return
     */
    @SuppressWarnings("unchecked")
    private Object setMandatoryFieldValue(ISTMandatoryFieldType fieldMeta,
            Object value) {
        String methodName = "unknown";
        String returnType = "unknown";
        Method callee;
        try {
            methodName = "set" + fieldMeta.name();
            callee = this.incident.getClass().getMethod(
                    methodName,
                    JAXBElement.class);
            if (fieldMeta.usesJAX()) {
                switch (fieldMeta.istFieldValueType()) {
                    case Date:
                        callee.invoke(
                                this.incident,
                                ISTMetaCache.toJaxCalendar(
                                        fieldMeta.name(),
                                        value));
                        break;
                    case Integer:
                        callee.invoke(
                                this.incident,
                                ISTMetaCache.toJXInt(
                                        fieldMeta.name(),
                                        value));
                        break;
                    case Text:
                        callee.invoke(
                                this.incident,
                                ISTMetaCache.toJXString(
                                        fieldMeta.name(),
                                        value));
                        break;
                    case Boolean:
                        callee.invoke(
                                this.incident,
                                ISTMetaCache.CreateJAXBBoolean(
                                        fieldMeta.name(),
                                        Boolean.valueOf(value.toString())));
                        break;
                    case List:
                        log.warn("Setting JAX List values is currently not implemented");
                        log.trace(String.format(
                                "%s %s: generic %s  ; reaOnly: %s",
                                fieldMeta.istFieldValueType().name(),
                                fieldMeta.name(),
                                fieldMeta.genericFielValueType().name(),
                                fieldMeta.isReadOnly()));
                        break;
                    case MultiList:
                        log.warn("Unexpected JAX Mandatory MultiSelect Field!");
                        log.warn(String.format(
                                "%s %s: generic %s  ; reaOnly: %s",
                                fieldMeta.istFieldValueType().name(),
                                fieldMeta.name(),
                                fieldMeta.genericFielValueType().name(),
                                fieldMeta.isReadOnly()));

                        break;
                }
            } else {
                // no JAX data, direct method call returns the data already
                switch (fieldMeta.istFieldValueType()) {
                    case Date:
                        callee.invoke(
                                this.incident,
                                ISTHandler.toXMLGregorianCalendar(df
                                        .parse(value.toString())));
                        break;
                    case Integer:
                        callee.invoke(
                                this.incident,
                                Integer.valueOf(String.valueOf(value)));
                        break;
                    case Text:
                        callee.invoke(
                                this.incident,
                                String.valueOf(value));
                        break;
                    case Boolean:
                        callee.invoke(
                                this.incident,
                                Boolean.valueOf(value.toString()));
                        break;
                    case List:
                        log.warn("Setting List values is currently not implemented");
                        log.trace(String.format(
                                "%s %s: generic %s  ; reaOnly: %s",
                                fieldMeta.istFieldValueType().name(),
                                fieldMeta.name(),
                                fieldMeta.genericFielValueType().name(),
                                fieldMeta.isReadOnly()));
                        break;
                    case MultiList:
                        log.warn("Unexpected Mandatory MultiSelect Field!");
                        log.warn(String.format(
                                "%s %s: generic %s  ; reaOnly: %s",
                                fieldMeta.istFieldValueType().name(),
                                fieldMeta.name(),
                                fieldMeta.genericFielValueType().name(),
                                fieldMeta.isReadOnly()));

                        break;
                }
            }
        } catch (NoSuchMethodException e) {
            String cause = "API Method not found: RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        } catch (IllegalAccessException e) {
            String cause = "Not allowed to access " + returnType
                    + " RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        } catch (InvocationTargetException e) {
            String cause = "Failed to execute " + returnType
                    + " RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return value;
    }

    private void traceAttachments() {
        int docCount = this.retrieveAllDocuments().getRemoteDocument().size();

        if (docCount > 0) {
            log.trace("============ " + docCount + " Attachments ============");
            log.trace(String.format(
                    "%-30s %10s %30s %s",
                    "FileName",
                    "File Type",
                    "Last Edited",
                    "Size"));
            log.trace(String.format(
                    "%-30s %10s %30s, %s",
                    "--------",
                    "---------",
                    "-----------",
                    "----"));

            for (RemoteDocument doc : this.retrieveAllDocuments()
                    .getRemoteDocument()) {
                log.trace(String.format(
                        "%-30s %10s %30s, %d bytes",
                        doc.getFilenameOrUrl().getValue(),
                        doc.getAttachmentTypeName().getValue(),
                        df.format(ISTHandler.toDate(doc.getEditedDate())),
                        doc.getSize()));
            }
        } else {
            log.trace("============ No Attachments ============");
        }
    }

    private void traceCustoms() {
        if (log.isTraceEnabled()) {
            int customsCount = this.retrieveAllCustoms()
                    .getRemoteArtifactCustomProperty().size();
            if (customsCount > 0) {
                log.trace("============ " + customsCount
                        + " Custom Properties ============");
                log.trace(String.format(
                        "%-30s  %-15s  %-15s  %-15s",
                        "Field Name",
                        "System Type",
                        "Field Type",
                        "Value"));
                log.trace(String.format(
                        "%-30s  %-15s  %-15s  %-15s",
                        "----------",
                        "-----------",
                        "----------",
                        "-----"));
                for (RemoteArtifactCustomProperty aprop : this.customs
                        .getRemoteArtifactCustomProperty()) {
                    RemoteCustomProperty propDef = aprop.getDefinition()
                            .getValue();
                    String fieldType = propDef.getCustomPropertyTypeName()
                            .getValue();
                    String systemType = propDef.getSystemDataType().getValue();
                    if (systemType.length() > 15)
                        systemType = systemType.substring(
                                0,
                                14);
                    String fieldName = this.meta.getName(aprop);
                    String value = this.getCustomValueAsString(aprop);
                    log.trace(String.format(
                            "%-30s  %-15s  %-15s  %-15s",
                            fieldName,
                            systemType,
                            fieldType,
                            value));
                }
            } else {
                log.trace("============ No Custom Properties ============");
            }
        }
    }

    private void traceMandatories() {
        log.trace("============ " + ISTMandatoryFieldType.values().length
                + " Mandatory Fields ============");
        log.trace(String.format(
                "%-30s   %s",
                "Field Name",
                "Value"));
        log.trace(String.format(
                "%-30s   %s",
                "----------",
                "-----"));
        for (ISTMandatoryFieldType fieldMeta : ISTMandatoryFieldType.values()) {
            log.trace(String.format(
                    "%-30s = %s",
                    fieldMeta.name(),
                    this.getMandatoryFieldValue(fieldMeta)));
        }
    }

    /**
     * used by Writer (also via CreateNewIncident) to update RemoteIncident with
     * GA data
     *
     * @param ga
     */
    public void updateIncident(GenericArtifact ga) {
        this.fetchCustoms(ga);
        this.fillMandatoryFields(ga);
    }

    /**
     * determines by hash comparison if the last change was done by CCF or not.
     *
     * if the hash parts are equal, the last update was via ISTWriter
     *
     * UNLESS the vesion part is INITIALVERSION, then the incident was never
     * updated/created via ISTWriter
     *
     * @param previousVesion
     * @return
     */
    public boolean wasModifedExternally(String previousVesion) {

        // initial shipment when incident was created in SpiraTest
        if (INITIALVERSION.equals(String
                .valueOf(getVersionPart(previousVesion))))
            return true;

        int oreviousHash = getHashPart(Long.parseLong(previousVesion));
        int currentHash = getHashPart(this.determineHash());

        return oreviousHash != currentHash;
    }
}
