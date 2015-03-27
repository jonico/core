package com.collabnet.ccf.ist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfint;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExport;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportCustomPropertyRetrieveForArtifactTypeServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportDocumentAddFileServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentAddCommentsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentCreateServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentCreateValidationFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentUpdateServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentUpdateValidationFaultMessageFaultFaultMessage;
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

    public static boolean isUseExtendedCreateLogging() {
        return useExtendedCreateLogging;
    }

    public static boolean isUseExtendedHashLogging() {
        return useExtendedHashLogging;
    }

    public static boolean isUseExtendedUpdateLogging() {
        return useExtendedUpdateLogging;
    }

    public static void setUseExtendedCreateLogging(
            boolean useExtendedCreateLogging) {
        ISTIncident.useExtendedCreateLogging = useExtendedCreateLogging;
    }

    public static void setUseExtendedHashLogging(boolean useExtendedHashLogging) {
        ISTIncident.useExtendedHashLogging = useExtendedHashLogging;
    }

    public static void setUseExtendedUpdateLogging(
            boolean useExtendedUpdateLogging) {
        ISTIncident.useExtendedUpdateLogging = useExtendedUpdateLogging;
    }

    private ISTMetaCache                        meta;
    private IImportExport                       soap;
    private RemoteIncident                      incident;

    private static final DateFormat             df                       = GenericArtifactHelper.df;
    private final ObjectFactory                 of                       = new ObjectFactory();
    private static final JerichoUtils           ju                       = new JerichoUtils();
    private static final Log                    log                      = LogFactory
            .getLog(ISTIncident.class);

    private final String                        DUMPSEPARATOR            = "::";

    private final String                        DUMPNAMEVALSEPARATOR     = "=";

    private final String                        EMPTYDUMPVAL             = "null";

    private ArrayOfRemoteComment                comments                 = null;

    private String                              commentsDump             = null;

    private ArrayOfRemoteDocument               attachments              = null;

    private String                              attachmentsDump          = null;

    private ArrayOfRemoteArtifactCustomProperty customs                  = null;
    private String                              customsDump              = null;
    private String                              mandatoryDump            = null;

    private Date                                lastUpdated              = null;
    private boolean                             hasPatchedUpdate         = false;

    private final int                           INCIDENTARTIFACTTYPEID   = 3;

    private static boolean                      useExtendedHashLogging   = false;
    private static boolean                      useExtendedCreateLogging = false;
    private static boolean                      useExtendedUpdateLogging = false;

    /**
     * instantiates ISTIncident
     *
     * @param service
     * @param ri
     */
    public ISTIncident(IImportExport service, ISTMetaCache cache) {
        this.soap = service;
        this.meta = cache;
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    public RemoteDocument attachDocument(byte[] binaryData, String name,
            String description) {

        RemoteDocument attachment = new RemoteDocument();
        RemoteDocument addedDocument = null;
        attachment.setArtifactId(ISTMetaCache.CreateJAXBInteger(
                "ArtifactId",
                this.getId()));
        attachment.setArtifactTypeId(ISTMetaCache.CreateJAXBInteger(
                "ArtifactTypeId",
                this.incident.getArtifactTypeId()));
        attachment.setFilenameOrUrl(ISTMetaCache.CreateJAXBString(
                "FilenameOrUrl",
                name));
        attachment.setDescription(ISTMetaCache.CreateJAXBString(
                "Description",
                description));
        attachment.setUploadDate(ISTHandler.toXMLGregorianCalendar(new Date()));

        try {
            addedDocument = this.soap.documentAddFile(
                    attachment,
                    binaryData);
        } catch (IImportExportDocumentAddFileServiceFaultMessageFaultFaultMessage e) {
            String cause = "Could not create new attachment `" + name
                    + "` on incident #" + this.getId();

            throw new CCFRuntimeException(cause, e);
        }

        return addedDocument;
    }

    /**
     * clear all cached information
     *
     * excluded
     *
     * @id
     * @meta
     * @soap
     * @all static and final members
     */
    protected void clearCache() {
        this.customs = null;
        this.customsDump = null;
        this.attachments = null;
        this.attachmentsDump = null;
        this.comments = null;
        this.commentsDump = null;

        this.mandatoryDump = null;
        this.lastUpdated = null;

        // clear version
        super.clearCache();
    }

    /**
     * creates a new incident based on the the data contained in ga
     *
     * @param ga
     */
    public void createIncident(GenericArtifact ga) {

        if ((ga.getAllGenericArtifactFieldsWithSameFieldName(
                ISTMandatoryFieldType.Name.name()).size() == 0)
                || (ga.getAllGenericArtifactFieldsWithSameFieldName(
                        ISTMandatoryFieldType.Description.name()).size() == 0)) {
            String cause = "Inconclusive data. Please check that your XSL sends `Name` and `Description` fields to SpiraTest, each exactly once!";
            throw new CCFRuntimeException(cause);
        }

        this.incident = new RemoteIncident();

        this.fillMandatoryFields(ga);
        this.fillCustoms(ga);

        try {
            this.incident = this.soap.incidentCreate(this.incident);
            this.uploadComments(ga);
            this.reload(this.getId());
            log.info("Created new incident #" + this.getId());
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

    protected void determineHash() {

        int hash = this.calculateHash(getMandatoriesDump() + getCommentsDump()
                + getAttachmentsDump() + getCustomsDump());

        this.initializeHash(hash);

    }

    /**
     * read all comments from Incident that were created after last modified
     * date and add them to GA
     *
     * @param ga
     */
    private void fetchComments(GenericArtifact ga, Date lastModifiedDate) {
        int fetched = 0;
        int retrieved = 0;
        for (RemoteComment c : this.getAllComments().getRemoteComment()) {
            Date creationDate = ISTHandler.toDate(c.getCreationDate()
                    .getValue());

            // use a HasMap to order comments instead of trusting the API ?
            retrieved++;
            if (creationDate.after(lastModifiedDate)
                    && c.getUserId().getValue() != ISTConnection
                    .getConnectorUserId()) {
                fetched++;
                ISTHandler.addGAField(
                        ga,
                        "comments",
                        c.getText().getValue(),
                        FieldValueTypeValue.HTMLSTRING,
                        GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
            }
        }
        log.trace(String.format(
                "  fetched %d comments created since %s, skipped %d",
                fetched,
                df.format(lastModifiedDate),
                retrieved - fetched));
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
                String value = prop.getStringValue().getValue();
                if (value != null) {
                    int len1 = value.length();
                    value = value.trim();
                    int trimmed = len1 - value.length();
                    if (trimmed != 0) {
                        String original = prop.getStringValue().getValue();
                        log.trace("trimmed field `" + label + "` by " + trimmed
                                + " chars");
                        log.trace(String
                                .format(
                                        "Strings:\nIncoming with %d chars:\n---%s---\nTrimmed with %d chars:\n---%s---",
                                        len1,
                                        original,
                                        len1 - trimmed,
                                        (String) value));
                    }
                }
                values.add(value);
                break;
            case Date:
                SimpleDateFormat dateParser = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                dateParser.setTimeZone(TimeZone.getTimeZone("GMT"));
                fieldValueTypeValue = FieldValueTypeValue.DATETIME;
                if (prop.getDateTimeValue().getValue() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.getTimeZone().getID();

                    XMLGregorianCalendar greg = prop.getDateTimeValue()
                            .getValue();

                    String gmt = String.format(
                            "%04d-%02d-%02d %02d:%02d:%02d",
                            greg.getYear(),
                            greg.getMonth(),
                            greg.getDay(),
                            greg.getHour(),
                            greg.getMinute(),
                            greg.getSecond());
                    Date d = null;
                    try {
                        d = dateParser.parse(gmt);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    values.add(d);

                }
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
            case User:
                fieldValueTypeValue = FieldValueTypeValue.USER;
                values.add(this.meta.getUserNameById(prop.getIntegerValue()
                        .getValue()));
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
        for (RemoteArtifactCustomProperty prop : this.getAllCustoms()
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
     * read custom properties from GA and set the values on the Incident
     *
     * @param ga
     */
    private void fillCustoms(GenericArtifact ga) {

        for (RemoteArtifactCustomProperty prop : this.getAllCustoms()
                .getRemoteArtifactCustomProperty()) {

            String fieldName = prop.getDefinition().getValue().getName()
                    .getValue();

            List<GenericArtifactField> fields = ga
                    .getAllGenericArtifactFieldsWithSameFieldName(fieldName);

            if (fields != null) {
                for (GenericArtifactField gaField : fields) {
                    if (gaField != null) {
                        switch (gaField.getFieldValueType()) {
                            case HTMLSTRING:
                            case USER: // currenty not implemented as target, goes into texts
                            case STRING: // Strings can also be sent to lists and booleans
                            case INTEGER:
                            case BOOLEAN:
                                this.setCustomPropertyValue(
                                        prop,
                                        String.valueOf(gaField.getFieldValue()));
                                break;
                            case DATETIME:
                            case DATE:
                                this.setCustomPropertyValue(
                                        prop,
                                        df.format(gaField.getFieldValue()));
                                break;
                            case BASE64STRING:
                                log.warn("Ignoring unsupported Base64 field value for field "
                                        + fieldName);
                                break;
                            case DOUBLE:
                                log.warn("Ignoring unsupported Double field value for field "
                                        + fieldName);
                                break;
                        }
                    }
                }

            } else {
                log.trace("Custom field `" + fieldName + "` is not in GA data");
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

            GenericArtifactField gaField = null;
            if (fields != null) {
                // there are currently no mandatory multi-select fields
                gaField = fields.get(0);

                if (fields.size() > 1) {
                    log.warn("More than one value for field `"
                            + fieldMeta.name()
                            + "` found. First value found was chosen. Please validate your XSL.");
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
                if (useExtendedUpdateLogging)
                    log.trace("updated field `"
                            + fieldMeta.name()
                            + "` = `"
                            + String.valueOf(this
                                    .getMandatoryFieldValue(fieldMeta)) + "`");

            } else {
                if (useExtendedUpdateLogging)
                    log.trace("Mandatory field `" + fieldMeta.name()
                            + "` is not in present GenericArtifact");
            }
        }
    }

    /**
     * reads all comments, returns cached object in subsquent calls.
     *
     * @return
     */
    private ArrayOfRemoteComment getAllComments() {
        if (this.comments == null) {
            try {
                this.comments = this.soap
                        .incidentRetrieveComments(this.getId());
            } catch (IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage e) {
                log.error(
                        "Failed to retrieve comments for incident #"
                                + this.getId(),
                                e);
            }
        }
        return this.comments;
    }

    /**
     * load all custom properties, either from incident or from project meta
     * definitions. Returns cached object on subsequent calls.
     *
     * @return
     */
    private ArrayOfRemoteArtifactCustomProperty getAllCustoms() {
        if (this.customs == null) {
            if (this.incident.getCustomProperties() != null) {
                this.customs = this.incident.getCustomProperties().getValue();
            } else {
                // new incident, create a new object
                this.customs = of.createArrayOfRemoteArtifactCustomProperty();
                try {
                    // get project generic properties
                    ArrayOfRemoteCustomProperty typeProperties = this.soap
                            .customPropertyRetrieveForArtifactType(
                                    INCIDENTARTIFACTTYPEID,
                                    false);

                    for (RemoteCustomProperty typeProp : typeProperties
                            .getRemoteCustomProperty()) {
                        // create new Artifact Property, set its definition
                        RemoteArtifactCustomProperty aProp = of
                                .createRemoteArtifactCustomProperty();

                        aProp.setDefinition(ISTMetaCache
                                .CreateJAXBRemoteCustomProperty(
                                        "Definition",
                                        typeProp));
                        // attach new property to list of customProperties
                        this.customs.getRemoteArtifactCustomProperty().add(
                                aProp);
                    }

                    // attach customs to incident
                    this.incident.setCustomProperties(ISTMetaCache
                            .CreateJAXBArrayOfRemoteArtifactCustomProperty(
                                    "CustomProperties",
                                    this.customs));

                } catch (IImportExportCustomPropertyRetrieveForArtifactTypeServiceFaultMessageFaultFaultMessage e) {
                    log.error(
                            "Failed to fetch Custom Properties for Artifact Type "
                                    + this.incident.getArtifactTypeId(),
                                    e);

                }

            }
        }

        return this.customs;
    }

    /**
     * loads all documents for the incident. Returns cached object on subsequent
     * calls.
     *
     * ? we could use a project wide list of documents cached once when
     * connecting
     *
     * @return
     */
    private ArrayOfRemoteDocument getAllDocuments() {
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

    private String getAttachmentsDump() {

        if (this.attachmentsDump == null) {
            if (this.getAllDocuments().getRemoteDocument().size() > 0) {
                for (RemoteDocument doc : this.attachments.getRemoteDocument()) {
                    String ret = "";
                    ret += doc.getFilenameOrUrl().getValue() + DUMPSEPARATOR
                            + doc.getArtifactTypeId().getValue()
                            + DUMPSEPARATOR
                            + df.format(ISTHandler.toDate(doc.getEditedDate()));
                    if (useExtendedHashLogging)
                        log.trace(String.format(
                                "   AT %-15d %s",
                                this.calculateHash(ret),
                                ret));
                    this.attachmentsDump += ret;
                }
            } else {
                this.attachmentsDump = EMPTYDUMPVAL;
            }
        }
        return this.attachmentsDump;
    }

    private String getCommentsDump() {

        if (this.commentsDump == null) {
            if (this.getAllComments().getRemoteComment().size() > 0) {
                for (RemoteComment rc : comments.getRemoteComment()) {
                    String ret = "";
                    ret += JerichoUtils.htmlToText(rc.getText().getValue())
                            + DUMPSEPARATOR
                            + df.format(ISTHandler.toDate(rc.getCreationDate()
                                    .getValue())) + DUMPSEPARATOR
                                    + rc.getUserName().getValue() + "["
                                    + rc.getUserId().getValue() + "]";
                    if (useExtendedHashLogging)
                        log.trace(String.format(
                                "   CO %-15d %s",
                                this.calculateHash(ret),
                                ret));
                    this.commentsDump += ret;
                }
            } else {
                this.commentsDump = EMPTYDUMPVAL;
            }
        }
        return this.commentsDump;
    }

    public Date getCreationDate() {
        return ISTHandler.toDate(this.incident.getCreationDate().getValue());
    }

    private String getCustomsDump() {

        if (this.customsDump == null) {
            if (this.getAllCustoms().getRemoteArtifactCustomProperty().size() > 0) {
                for (RemoteArtifactCustomProperty prop : this.getAllCustoms()
                        .getRemoteArtifactCustomProperty()) {
                    String ret = "";
                    ret += ISTMetaCache.getName(prop) + DUMPNAMEVALSEPARATOR;
                    ret += this.getCustomValueAsString(prop) == null ? EMPTYDUMPVAL
                            : this.getCustomValueAsString(prop);
                    ret += DUMPSEPARATOR;
                    if (useExtendedHashLogging) {
                        log.trace(String.format(
                                "   CP %-15d %s",
                                this.calculateHash(ret),
                                ret));
                    }
                    this.customsDump += ret;
                }
            } else {
                this.customsDump = EMPTYDUMPVAL;
            }
        }
        return this.customsDump;
    }

    @SuppressWarnings("incomplete-switch")
    private String getCustomValueAsString(RemoteArtifactCustomProperty prop) {

        RemoteCustomProperty propDef = prop.getDefinition().getValue();

        ISTCustomFieldType type = ISTCustomFieldType.valueOf(propDef
                .getCustomPropertyTypeName().getValue());
        switch (type) {
            case Text:
                if (prop.getStringValue() != null)
                    return JerichoUtils.htmlToText(prop.getStringValue()
                            .getValue());
                break;
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
        }
        return null;
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

            for (RemoteDocument doc : this.getAllDocuments()
                    .getRemoteDocument()) {
                Date uploadDate = ISTHandler.toDate(doc.getEditedDate());
                lastUpdated = uploadDate.after(lastUpdated) ? uploadDate
                        : lastUpdated;
            }

            for (RemoteComment com : this.getAllComments().getRemoteComment()) {
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

    private String getMandatoriesDump() {
        if (this.mandatoryDump == null) {
            for (ISTMandatoryFieldType fieldMeta : ISTMandatoryFieldType
                    .getIdentifyingMandatoryFields()) {
                String toAdd = "";
                Object value = this.getMandatoryFieldValue(fieldMeta);
                toAdd += fieldMeta.name() + DUMPSEPARATOR;
                if (value != null) {
                    switch (fieldMeta.istFieldValueType()) {
                        case Date:
                            toAdd += df.format((Date) value);
                            break;
                        default:
                            toAdd += String.valueOf(value);
                    }
                } else {
                    toAdd += EMPTYDUMPVAL;
                }
                if (useExtendedHashLogging)
                    log.trace(String.format(
                            "   MF %-15d %s",
                            this.calculateHash(toAdd),
                            toAdd));
                this.mandatoryDump += toAdd;
            }
        }
        return this.mandatoryDump;
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
                    case User:
                        String userIdFieldName = fieldMeta.name().replace(
                                "Name",
                                "Id");
                        Method uidCallee = this.incident.getClass().getMethod(
                                "get" + userIdFieldName);
                        Integer uid = ((JAXBElement<Integer>) uidCallee
                                .invoke(this.incident)).getValue();
                        value = this.meta.getUserNameById(uid);
                        break;
                    case Text:
                    case List:
                        // the mandatory list fields also returns a string value for the item name
                        value = ((JAXBElement<String>) callee
                                .invoke(this.incident)).getValue();
                        if (log.isTraceEnabled()
                                && fieldMeta.istFieldValueType() == ISTCustomFieldType.Text
                                && value != null) {
                            int len1 = ((String) value).length();
                            value = ((String) value).trim();
                            int trimmed = len1 - ((String) value).length();
                            if (trimmed != 0 && log.isTraceEnabled()) {
                                String original = (String) ((JAXBElement<String>) callee
                                        .invoke(this.incident)).getValue();
                                log.trace("trimmed field `" + fieldMeta.name()
                                        + "` by " + trimmed + " chars");
                                log.trace(String
                                        .format(
                                                "Strings:\nIncoming with %d chars:\n---%s---\nTrimmed with %d chars:\n---%s---",
                                                len1,
                                                original,
                                                len1 - trimmed,
                                                (String) value));
                            }
                        }
                        break;
                    case Boolean:
                        value = ((JAXBElement<Boolean>) callee
                                .invoke(this.incident)).getValue();
                        break;
                    case MultiList:
                        log.warn("Ignoring unexpected mandatory field type "
                                + fieldMeta.istFieldValueType().name()
                                + " for field " + fieldMeta.name());
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
                        if (value != null) {
                            int len1 = ((String) value).length();
                            value = ((String) value).trim();
                            int trimmed = ((String) value).length() - len1;
                            if (trimmed != 0) {
                                String original = String.valueOf(callee
                                        .invoke(this.incident));
                                log.trace("trimmed field `" + fieldMeta.name()
                                        + "` by " + trimmed + " chars");
                                log.trace(String
                                        .format(
                                                "Strings:\nIncoming with %d chars:\n---%s---\n\nTrimmed with %d chars:\n---%s---",
                                                len1,
                                                original,
                                                len1 - trimmed,
                                                (String) value));
                            }
                        }
                        break;
                    case Boolean:
                        value = String.valueOf(callee.invoke(this.incident));
                        break;
                    case List:
                    case MultiList:
                    case User:
                        log.warn("Ignoring unexpected mandatory field type "
                                + fieldMeta.istFieldValueType().name()
                                + " for plain field " + fieldMeta.name());
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
    public void printIncidentInfo() {

        String dateInfo = df.format(this.getLastUpdateDate());
        dateInfo += this.hasPatchedUpdate ? " (*)" : "";
        log.trace(String.format(
                "Incident %d `%s` was last updated on %s with hash %d",
                this.getId(),
                this.getMandatoryFieldValue(ISTMandatoryFieldType.Name),
                dateInfo,
                this.getVersionHash()));

        if (useExtendedCreateLogging || useExtendedUpdateLogging) {
            this.traceMandatories();
            this.traceCustoms();
            this.traceAttachments();
        }
    }

    public void reload(int incidentId) {
        try {
            RemoteIncident iload = this.soap.incidentRetrieveById(incidentId);
            this.clearCache();
            this.incident = iload;
            log.trace("retrieved incident #" + incidentId);
        } catch (IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage e) {
            String cause = "Failed to load incident #" + incidentId;
            throw new CCFRuntimeException(cause, e);
        }
    }

    public void retrieveIncident(int incidentId) {
        this.reload(incidentId);
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

        // without the propertyNumber, the data doesn't get saved
        prop.setPropertyNumber(propDef.getPropertyNumber());

        ISTCustomFieldType type = ISTCustomFieldType.valueOf(propDef
                .getCustomPropertyTypeName().getValue());

        switch (type) {
            case Boolean:
                prop.setBooleanValue(ISTMetaCache.CreateJAXBBoolean(
                        "BooleanValue",
                        Boolean.valueOf(value)));
                log.debug(propDef.getName().getValue() + ": set to " + value);
                break;
            case Date:
                try {
                    prop.setDateTimeValue(ISTMetaCache
                            .CreateJAXBXMLGregorianCalendar(
                                    "DateTimeValue",
                                    ISTHandler.toXMLGregorianCalendar(df
                                            .parse(value))));
                    log.debug(propDef.getName().getValue() + ": set to "
                            + df.format(df.parse(value)));

                } catch (ParseException e) {
                    throw new CCFRuntimeException(
                            "Could not transform date string " + value, e);
                }
                break;
            case Integer:
                prop.setIntegerValue(ISTMetaCache.CreateJAXBInteger(
                        "IntegerValue",
                        Integer.valueOf(value)));
                log.debug(propDef.getName().getValue() + ": set to " + value);
                break;
            case User:
                Integer uid = this.meta.getUserIdByName(value);
                if (uid == null) {
                    throw new CCFRuntimeException("User not found: " + value);
                } else {
                    prop.setIntegerValue(ISTMetaCache.CreateJAXBInteger(
                            "IntegerValue",
                            uid));
                }
                break;
            case List:
                int itemId = this.meta.getCustomListItemIdForValue(
                        prop,
                        value);
                if (itemId != -1) {
                    prop.setIntegerValue(ISTMetaCache.CreateJAXBInteger(
                            "IntegerValue",
                            itemId));
                    log.debug(propDef.getName().getValue() + ": set to "
                            + value + " (" + itemId + ")");
                } else {
                    String cause = String
                            .format(
                                    "Value `%s` does not exist in field `%s`, please edit your transformation or add the value to SpiraTest",
                                    value,
                                    propDef.getName().getValue());
                    throw new CCFRuntimeException(cause);

                }
                break;
            case MultiList:
                int multiId = this.meta.getCustomListItemIdForValue(
                        prop,
                        value);
                // we're setting one value (of one or more fields of same name in GA data)
                if (multiId != -1) {
                    if (prop.getIntegerValue() == null
                            || prop.getIntegerListValue().getValue() == null) {
                        // first time visit - create and append Array object
                        ArrayOfint intValues = of.createArrayOfint();
                        prop.setIntegerListValue(ISTMetaCache
                                .CreateJAXBArrayOfint(
                                        "IntegerListValue",
                                        intValues));
                    }
                    // add value to list of int
                    prop.getIntegerListValue().getValue().getInt().add(
                            multiId);

                    log.debug(propDef.getName().getValue() + ": added value "
                            + value + " (" + multiId + ")");
                } else if (value != null && !"null".equals(value)) {
                    String cause = String
                            .format(
                                    "Value `%s` does not exist in field `%s`, please edit your transformation or add the value to SpiraTest",
                                    value,
                                    propDef.getName().getValue());
                    // we'll keep on going
                    log.warn(cause);
                }
                break;
            case Text:
                String myVal = (String) value;
                // assuming multi-line content to be sent to rich text fields
                if (myVal.contains(System.getProperty("line.separator"))) {
                    myVal = ju.convertTextToHtml(myVal);
                }
                prop.setStringValue(ISTMetaCache.CreateJAXBString(
                        "StringValue",
                        myVal.trim()));
                if (useExtendedUpdateLogging) {
                    String valInfo = value.length() > 10 ? value.substring(
                            0,
                            9) + " (...)" : value;
                    log.trace(propDef.getName().getValue() + ": set to "
                            + valInfo);
                }
                break;

        }
    }

    public void setIncident(RemoteIncident ri) {
        this.incident = ri;
    }

    /**
     * return basic values as objects, list and multilists are converted to
     * strings
     *
     * @param fieldMeta
     * @return
     */
    private void setMandatoryFieldValue(ISTMandatoryFieldType fieldMeta,
            Object value) {

        String methodName = "unknown";
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
                        String myVal = (String) value;
                        if (fieldMeta.genericFielValueType() == FieldValueTypeValue.HTMLSTRING)
                            myVal = ju.convertTextToHtml(myVal);

                        callee.invoke(
                                this.incident,
                                ISTMetaCache.toJXString(
                                        fieldMeta.name(),
                                        myVal));
                        break;
                    case Boolean:
                        callee.invoke(
                                this.incident,
                                ISTMetaCache.CreateJAXBBoolean(
                                        fieldMeta.name(),
                                        Boolean.valueOf(value.toString())));
                        break;
                    case List:
                        // each mandatory list field comes in xyzId and xyzName pairs
                        // we'll need to get the id based on the name value, then
                        // set the ID sibling
                        String idFieldName = null;
                        int idFieldValue = this.meta.getMandatoryIdForValue(
                                fieldMeta,
                                String.valueOf(value));
                        // no fancy code saving here, plain and brutal
                        if (fieldMeta.name().endsWith(
                                "Name"))
                            idFieldName = fieldMeta.name().replace(
                                    "Name",
                                    "Id");
                        if (fieldMeta.name().endsWith(
                                "VersionNumber"))
                            idFieldName = fieldMeta.name().replace(
                                    "VersionNumber",
                                    "Id");

                        // call the nethod to set the id
                        String idMethodName = "set" + idFieldName;
                        Method idCallee = this.incident.getClass().getMethod(
                                idMethodName,
                                JAXBElement.class);

                        if (idFieldValue != -1) {
                            idCallee.invoke(
                                    this.incident,
                                    ISTMetaCache.CreateJAXBInteger(
                                            idFieldName,
                                            idFieldValue));
                            if (useExtendedUpdateLogging)
                                log.trace(String.format(
                                        "%s set to %d (%s)",
                                        idFieldName,
                                        idFieldValue,
                                        value));
                        } else {
                            String cause = String
                                    .format(
                                            "Value `%s` does not exist in field `%s`, please edit your transformation or add the value to SpiraTest",
                                            value,
                                            fieldMeta.name());
                            log.error(cause);
                            throw new CCFRuntimeException(cause);
                        }
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
                    case User:

                        // call the Id method instead of the Name method
                        String userIdFieldName = fieldMeta.name().replace(
                                "Name",
                                "Id");
                        String userIdMethodName = "set" + userIdFieldName;
                        Method userIdCallee = this.incident.getClass()
                                .getMethod(
                                        userIdMethodName,
                                        JAXBElement.class);

                        // set user id
                        Integer uid = this.meta.getUserIdByName((String) value);
                        if (uid != null) {
                            userIdCallee.invoke(
                                    this.incident,
                                    ISTMetaCache.CreateJAXBInteger(
                                            userIdFieldName,
                                            uid));
                        } else {
                            String cause = "User `" + (String) value
                                    + "` was not found in SpiraTest.";
                            throw new CCFRuntimeException(cause);
                        }
                        break;
                    default:
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
                        if (useExtendedUpdateLogging)
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
                    case User:
                        // n/a
                        break;
                }
            }
        } catch (NoSuchMethodException e) {
            String cause = "API Method not found: RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        } catch (IllegalAccessException e) {
            String cause = "Not allowed to access RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        } catch (InvocationTargetException e) {
            String cause = "Failed to execute RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        } catch (IllegalArgumentException e) {
            String cause = "Unexpected call error RemoteIncident." + methodName;
            throw new CCFRuntimeException(cause, e);
        } catch (ParseException e) {
            String cause = "Failed to parse date value "
                    + String.valueOf(value) + " for mandatory field "
                    + fieldMeta.name();
            throw new CCFRuntimeException(cause, e);
        }

    }

    private void traceAttachments() {
        int docCount = this.getAllDocuments().getRemoteDocument().size();

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

            for (RemoteDocument doc : this.getAllDocuments()
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
        int customsCount = this.getAllCustoms()
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
            for (RemoteArtifactCustomProperty aprop : this.getAllCustoms()
                    .getRemoteArtifactCustomProperty()) {
                RemoteCustomProperty propDef = aprop.getDefinition().getValue();
                String fieldType = propDef.getCustomPropertyTypeName()
                        .getValue();
                String systemType = propDef.getSystemDataType().getValue();
                if (systemType.length() > 15)
                    systemType = systemType.substring(
                            0,
                            14);
                String fieldName = ISTMetaCache.getName(aprop);
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

    public void traceHashIntel() {
        String header = "Hashes for incident #" + this.getId();
        log.trace(StringUtils.repeat(
                "=",
                header.length()));
        log.trace(header);
        log.trace(StringUtils.repeat(
                "-",
                header.length()));

        log.trace(String.format(
                "%-15s %-15s %-15s %-15s ",
                "Mandatory",
                "Comments",
                "Attachments",
                "Customs"));
        log.trace(String.format(
                "%-15d %-15d %-15d %-15d ",
                this.calculateHash(this.getMandatoriesDump()),
                this.calculateHash(this.getCommentsDump()),
                this.calculateHash(this.getAttachmentsDump()),
                this.calculateHash(this.getCustomsDump())));
        log.trace("CNT.HSH = " + this.getVersionInfoString());
        log.trace("VERSION = " + this.getVersion());
        log.trace(StringUtils.repeat(
                "-",
                header.length()));

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
     * used by Writer to update RemoteIncident with GA data
     *
     * @param ga
     */
    public void updateIncident(GenericArtifact ga) {
        this.fillMandatoryFields(ga);
        this.fillCustoms(ga);
        // documents won't be handled here, they are shipped as their own artifacts

        try {
            this.soap.incidentUpdate(this.incident);
            this.uploadComments(ga);
            this.reload(this.getId());
            log.info("Updated incident #" + this.getId());
        } catch (IImportExportIncidentUpdateServiceFaultMessageFaultFaultMessage e) {
            String cause = "Could not update incident #" + this.getId();
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        } catch (IImportExportIncidentUpdateValidationFaultMessageFaultFaultMessage e) {
            String cause = "Validation failed for incident #" + this.getId()
                    + " udpate";
            ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
            throw new CCFRuntimeException(cause, e);
        }

    }

    /**
     * read comments from GA and update the Incident
     *
     * the incident has to have an id (a.k.a. has to exist in and be read from
     * IST)
     *
     * @param ga
     */
    private void uploadComments(GenericArtifact ga) {

        List<GenericArtifactField> newComments = ga
                .getAllGenericArtifactFieldsWithSameFieldName("Comments");

        ArrayOfRemoteComment rCommentArray = new ArrayOfRemoteComment();

        if (newComments != null) {
            for (GenericArtifactField comment : newComments) {
                if (comment != null) {
                    RemoteComment rc = new RemoteComment();
                    rc.setArtifactId(this.getId());
                    String cText = String.valueOf(comment.getFieldValue());
                    rc.setText(ISTMetaCache.CreateJAXBString(
                            "Text",
                            ju.convertTextToHtml(cText)));

                    if (!rCommentArray.getRemoteComment().add(
                            rc)) {
                        String cause = "Could not add a comment to incident #"
                                + this.getId();
                        throw new CCFRuntimeException(cause);
                    }

                }
            }

            if (rCommentArray.getRemoteComment().size() > 0) {
                try {
                    this.soap.incidentAddComments(rCommentArray);
                } catch (IImportExportIncidentAddCommentsServiceFaultMessageFaultFaultMessage e) {
                    String cause = "Could not add "
                            + rCommentArray.getRemoteComment().size()
                            + " comment/s to incident #" + this.getId();
                    throw new CCFRuntimeException(cause, e);
                }
            }
        }

    }

}
