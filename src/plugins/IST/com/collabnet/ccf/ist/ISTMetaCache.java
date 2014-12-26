package com.collabnet.ccf.ist;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncidentPriority;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncidentSeverity;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncidentStatus;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncidentType;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteRelease;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfint;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExport;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportCustomPropertyRetrieveForArtifactTypeServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrievePrioritiesServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveSeveritiesServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveStatusesServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveTypesServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportReleaseRetrieveServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomList;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomListValue;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncidentPriority;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncidentSeverity;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncidentStatus;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncidentType;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteRelease;

public class ISTMetaCache {

    private static final String buildKey(RemoteArtifactCustomProperty prop,
            int valueId) {
        return getName(prop) + KEYSEPARATOR + valueId;
    }

    private static final String buildKey(RemoteCustomProperty prop, int valueId) {
        return getName(prop) + KEYSEPARATOR + valueId;
    }

    /***
     * Creates a JAXB web service IntegerList element from a Java IntegerList
     *
     * @param value
     * @return
     */
    public static JAXBElement<ArrayOfint> CreateJAXBArrayOfInt(
            String fieldName, ArrayOfint value) {
        JAXBElement<ArrayOfint> jaxIntegerList = new JAXBElement<ArrayOfint>(
                new QName(WEB_SERVICE_NAMESPACE_DATA_OBJECTS, fieldName),
                ArrayOfint.class, value);
        if (value == null) {
            jaxIntegerList.setNil(true);
        }
        return jaxIntegerList;
    }

    /***
     * Creates a JAXB web service IntegerList element from a Java IntegerList
     *
     * @param value
     * @return
     */
    public static JAXBElement<ArrayOfint> CreateJAXBArrayOfInt(
            String fieldName, List<Integer> value) {
        // Convert List<Integer> to ArrayOfint
        ArrayOfint arrayOfint = new ArrayOfint();
        if (value != null) {
            for (Integer integer : value) {
                arrayOfint.getInt().add(
                        integer);
            }
        }
        JAXBElement<ArrayOfint> jaxIntegerList = new JAXBElement<ArrayOfint>(
                new QName(WEB_SERVICE_NAMESPACE_DATA_OBJECTS, fieldName),
                ArrayOfint.class, arrayOfint);
        if (value == null) {
            jaxIntegerList.setNil(true);
        }
        return jaxIntegerList;
    }

    /***
     * Creates a JAXB web service ArrayOfRemoteArtifactCustomProperty element
     * from a Java ArrayOfRemoteArtifactCustomProperty object
     *
     * @param value
     * @return
     */
    public static JAXBElement<ArrayOfRemoteArtifactCustomProperty> CreateJAXBArrayOfRemoteArtifactCustomProperty(
            String fieldName, ArrayOfRemoteArtifactCustomProperty value) {
        JAXBElement<ArrayOfRemoteArtifactCustomProperty> jaxArrayOfRemoteArtifactCustomProperty = new JAXBElement<ArrayOfRemoteArtifactCustomProperty>(
                new QName(WEB_SERVICE_NAMESPACE_DATA_OBJECTS, fieldName),
                ArrayOfRemoteArtifactCustomProperty.class, value);
        if (value == null) {
            jaxArrayOfRemoteArtifactCustomProperty.setNil(true);
        }
        return jaxArrayOfRemoteArtifactCustomProperty;
    }

    /***
     * Creates a JAXB web service BigDecimal element from a Java BigDecimal
     *
     * @param value
     * @return
     */
    public static JAXBElement<BigDecimal> CreateJAXBBigDecimal(
            String fieldName, BigDecimal value) {
        JAXBElement<BigDecimal> jaxBigDecimal = new JAXBElement<BigDecimal>(
                new QName(WEB_SERVICE_NAMESPACE_DATA_OBJECTS, fieldName),
                BigDecimal.class, value);
        if (value == null) {
            jaxBigDecimal.setNil(true);
        }
        return jaxBigDecimal;
    }

    /***
     * Creates a JAXB web service Boolean element from a Java Boolean
     *
     * @param value
     * @return
     */
    public static JAXBElement<Boolean> CreateJAXBBoolean(String fieldName,
            Boolean value) {
        JAXBElement<Boolean> jaxBoolean = new JAXBElement<Boolean>(new QName(
                WEB_SERVICE_NAMESPACE_DATA_OBJECTS, fieldName), Boolean.class,
                value);
        if (value == null) {
            jaxBoolean.setNil(true);
        }
        return jaxBoolean;
    }

    /***
     * Creates a JAXB web service integer element from a Java integer
     *
     * @param value
     * @return
     */
    public static JAXBElement<Integer> CreateJAXBInteger(String fieldName,
            Integer value) {
        JAXBElement<Integer> jaxInteger = new JAXBElement<Integer>(new QName(
                WEB_SERVICE_NAMESPACE_DATA_OBJECTS, fieldName), Integer.class,
                value);
        if (value == null) {
            jaxInteger.setNil(true);
        }
        return jaxInteger;
    }

    /***
     * Creates a JAXB web service string element from a Java string
     *
     * @param value
     * @return
     */
    public static JAXBElement<String> CreateJAXBString(String fieldName,
            String value) {
        JAXBElement<String> jaxString = new JAXBElement<String>(new QName(
                WEB_SERVICE_NAMESPACE_DATA_OBJECTS, fieldName), String.class,
                value);
        if (value == null) {
            jaxString.setNil(true);
        }
        return jaxString;
    }

    /***
     * Creates a JAXB web service XMLGregorianCalendar element from a Java
     * XMLGregorianCalendar object
     *
     * @param value
     * @return
     */
    public static JAXBElement<XMLGregorianCalendar> CreateJAXBXMLGregorianCalendar(
            String fieldName, XMLGregorianCalendar value) {
        JAXBElement<XMLGregorianCalendar> jaxXMLGregorianCalendar = new JAXBElement<XMLGregorianCalendar>(
                new QName(WEB_SERVICE_NAMESPACE_DATA_OBJECTS, fieldName),
                XMLGregorianCalendar.class, value);
        if (value == null) {
            jaxXMLGregorianCalendar.setNil(true);
        }
        return jaxXMLGregorianCalendar;
    }

    public static final int getListItemId(String key) {
        return Integer.valueOf(key.split(KEYSEPARATOR)[1]);
    }

    public static final String getName(RemoteArtifactCustomProperty prop) {
        return prop.getDefinition().getValue().getName().getValue();
    }

    public static final String getName(RemoteCustomProperty prop) {
        return prop.getCustomPropertyFieldName().getValue();
    }

    public static JAXBElement<XMLGregorianCalendar> toJaxCalendar(
            String fieldName, Object value) {
        return ISTMetaCache.CreateJAXBXMLGregorianCalendar(
                fieldName,
                ISTHandler.toXMLGregorianCalendar((Date) value));
    }

    public static JAXBElement<Integer> toJXInt(String fieldName, Object value) {
        return ISTMetaCache.CreateJAXBInteger(
                fieldName,
                Integer.valueOf(String.valueOf(value)));
    }

    public static JAXBElement<String> toJXString(String fieldName, Object value) {
        return ISTMetaCache.CreateJAXBString(
                fieldName,
                (String) value);
    }

    public static final String      WEB_SERVICE_NAMESPACE_DATA_OBJECTS = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects"; //$NON-NLS-1$

    private static final DateFormat df                                 = GenericArtifactHelper.df;
    private static final Log        log                                = LogFactory
                                                                               .getLog(ISTReader.class);

    private static final String     KEYSEPARATOR                       = "::";

    private Map<String, String>     customlistValues                   = null;

    private Map<Integer, String>    priorityValues                     = new HashMap<Integer, String>();

    private Map<Integer, String>    statusValues                       = new HashMap<Integer, String>();

    private Map<Integer, String>    severityValues                     = new HashMap<Integer, String>();

    private Map<Integer, String>    incidentTypeValues                 = new HashMap<Integer, String>();

    private Map<Integer, String>    resolutionValues                   = new HashMap<Integer, String>();

    private Map<Integer, String>    releaseValues                      = new HashMap<Integer, String>();

    private IImportExport           soap                               = null;

    public ISTMetaCache(IImportExport theSoap, boolean cacheMandatories) {

        // all custom lists
        this.soap = theSoap;
        try {

            // get other list values in order to write to SpiraTest
            if (cacheMandatories) {
                // mandatory lists
                ArrayOfRemoteIncidentPriority priorityList;
                ArrayOfRemoteIncidentSeverity severityList;
                ArrayOfRemoteIncidentStatus statusList;
                ArrayOfRemoteIncidentType incidentTypeList;
                ArrayOfRemoteRelease releaseList;

                priorityList = soap.incidentRetrievePriorities();
                severityList = soap.incidentRetrieveSeverities();
                statusList = soap.incidentRetrieveStatuses();
                incidentTypeList = soap.incidentRetrieveTypes();
                releaseList = soap.releaseRetrieve(false);

                for (RemoteIncidentPriority prio : priorityList
                        .getRemoteIncidentPriority()) {
                    this.priorityValues.put(
                            prio.getPriorityId().getValue(),
                            prio.getName().getValue());
                }

                for (RemoteIncidentSeverity sev : severityList
                        .getRemoteIncidentSeverity()) {
                    this.severityValues.put(
                            sev.getSeverityId().getValue(),
                            sev.getName().getValue());
                }

                for (RemoteIncidentStatus sta : statusList
                        .getRemoteIncidentStatus()) {
                    this.statusValues.put(
                            sta.getIncidentStatusId().getValue(),
                            sta.getName().getValue());
                }

                for (RemoteIncidentType typ : incidentTypeList
                        .getRemoteIncidentType()) {
                    this.incidentTypeValues.put(
                            typ.getIncidentTypeId().getValue(),
                            typ.getName().getValue());
                }

                for (RemoteRelease rel : releaseList.getRemoteRelease()) {
                    this.releaseValues.put(
                            rel.getReleaseId().getValue(),
                            rel.getName().getValue());
                }

                log.debug(String
                        .format(
                                "Cached %d Priorties, %d Severities, %d status values, %d incident types, %d releases",
                                this.priorityValues.size(),
                                this.severityValues.size(),
                                this.statusValues.size(),
                                this.incidentTypeValues.size(),
                                this.releaseValues.size()));

                // MAYBE get list values globally, or via incident itself?

            }
        } catch (IImportExportIncidentRetrieveTypesServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IImportExportIncidentRetrieveSeveritiesServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IImportExportIncidentRetrievePrioritiesServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IImportExportIncidentRetrieveStatusesServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IImportExportReleaseRetrieveServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private int findKeyForValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public String getCustomListItemValueById(RemoteArtifactCustomProperty prop,
            int customPropertyListValueId) {

        if (this.customlistValues == null)
            this.loadCustomListsForReadAccess(prop);

        return this.customlistValues.get(String
                .valueOf(customPropertyListValueId));
    }

    public int getIncidentTypeKeyForValue(Object value) {
        //FIXME this is currently broken. customListItemValues is not reverse id safe.
        return findKeyForValue(
                incidentTypeValues,
                (String) value);
    }

    public ISTCustomFieldType getISTFieldValueTypeFromGaFieldValueType(
            FieldValueTypeValue gaFieldValueType) {
        switch (gaFieldValueType) {
            case BOOLEAN:
                return ISTCustomFieldType.Integer;
            case DATE:
            case DATETIME:
                return ISTCustomFieldType.Date;
            case INTEGER:
                return ISTCustomFieldType.Integer;
            case STRING:
            case HTMLSTRING:
            case USER:
                return ISTCustomFieldType.Text;
            default:
                return ISTCustomFieldType.Text;
        }
    }

    public ArrayList<String> getMultiListValues(
            RemoteArtifactCustomProperty prop) {

        ArrayList<String> list = new ArrayList<String>();
        ArrayOfint mvalues = prop.getIntegerListValue().getValue();
        if (mvalues != null && mvalues.getInt().size() > 0) {
            for (int listItemValueId : mvalues.getInt()) {

                list.add(this.getCustomListItemValueById(
                        prop,
                        listItemValueId));
            }
        }
        return list;
    }

    public int getPriorityKeyForValue(Object value) {
        return findKeyForValue(
                priorityValues,
                (String) value);
    }

    public int getReleaseKeyForValue(Object value) {
        return findKeyForValue(
                this.releaseValues,
                (String) value);
    }

    public String getSingleListValue(RemoteArtifactCustomProperty prop) {

        if (prop.getIntegerValue().getValue() != null) {
            int listValueId = prop.getIntegerValue().getValue();
            return this.getCustomListItemValueById(
                    prop,
                    listValueId);
        }
        return null;
    }

    public int getStatusKeyForValue(Object value) {
        return findKeyForValue(
                this.statusValues,
                (String) value);
    }

    private void loadCustomListsForReadAccess(RemoteArtifactCustomProperty prop) {
        // get artifactType ID
        int artfTypeId = prop.getDefinition().getValue().getArtifactTypeId();

        this.customlistValues = new HashMap<String, String>();
        try {
            ArrayOfRemoteCustomProperty customProps = soap
                    .customPropertyRetrieveForArtifactType(
                            artfTypeId,
                            false);
            for (RemoteCustomProperty cProp : customProps
                    .getRemoteCustomProperty()) {
                if (cProp.getCustomList().getValue() != null) {
                    RemoteCustomList rcl = cProp.getCustomList().getValue();

                    for (RemoteCustomListValue listItem : rcl.getValues()
                            .getValue().getRemoteCustomListValue()) {
                        this.customlistValues
                        .put(
                                String.valueOf(listItem
                                        .getCustomPropertyValueId()
                                        .getValue()),
                                        listItem.getName().getValue());
                    }
                }
            }

            log.debug(String.format(
                    "Cached %d Custom Property Values",
                    this.customlistValues.size()));

        } catch (IImportExportCustomPropertyRetrieveForArtifactTypeServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
