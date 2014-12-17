package com.collabnet.ccf.ist;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.utils.JerichoUtils;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteCustomList;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfint;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportCustomPropertyRetrieveCustomListByIdServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportCustomPropertyRetrieveCustomListsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomList;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomListValue;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomProperty;

public class ISTMetaData {

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

    public static final String      WEB_SERVICE_NAMESPACE_DATA_OBJECTS = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects"; //$NON-NLS-1$

    private static final DateFormat df                                 = GenericArtifactHelper.df;

    private static final Log        log                                = LogFactory
                                                                               .getLog(ISTReader.class);

    private Map<Integer, String>    customlistValues                   = new HashMap<Integer, String>();

    public ISTMetaData(ISTConnection conn) {
        // cache all known custom list values
        ArrayOfRemoteCustomList allCustomListSkeletons;
        try {
            allCustomListSkeletons = conn.getService()
                    .customPropertyRetrieveCustomLists();
            for (RemoteCustomList cl : allCustomListSkeletons
                    .getRemoteCustomList()) {
                cl.getName().getValue();
                cl.getCustomPropertyListId().getValue();
                // fetch each list to get all available list values
                RemoteCustomList cl2 = conn.getService()
                        .customPropertyRetrieveCustomListById(
                                cl.getCustomPropertyListId().getValue());
                for (RemoteCustomListValue listval : cl2.getValues().getValue()
                        .getRemoteCustomListValue()) {
                    this.customlistValues.put(
                            listval.getCustomPropertyValueId().getValue(),
                            listval.getName().getValue());
                }
            }
        } catch (IImportExportCustomPropertyRetrieveCustomListsServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Could not retrieve Custom Properties",
                    e);
        } catch (IImportExportCustomPropertyRetrieveCustomListByIdServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Could not retrieve Custom List values for list",
                    e);
        }

        log.debug(String.format(
                "Cached %d Custom Property Values",
                this.customlistValues.size()));

    }

    public String getCustomListItemValue(int customPropertyListValueId) {
        return this.customlistValues.get(customPropertyListValueId);
    }

    public ArrayList<String> getMultiListValues(
            RemoteArtifactCustomProperty prop) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayOfint mvalues = prop.getIntegerListValue().getValue();
        if (mvalues.getInt().size() > 0) {
            for (int listItemValueId : mvalues.getInt()) {
                list.add(this.getCustomListItemValue(listItemValueId));
            }
        }
        return list;
    }

    public final String getName(RemoteArtifactCustomProperty prop) {
        return prop.getDefinition().getValue().getName().getValue();
    }

    public final String getSingleListValue(RemoteArtifactCustomProperty prop) {
        if (!prop.getIntegerValue().isNil()) {
            int listValueId = prop.getIntegerValue().getValue();
            return this.getCustomListItemValue(listValueId);
        }
        return "null";
    }

    public String getValue(RemoteArtifactCustomProperty prop) {

        RemoteCustomProperty propDef = prop.getDefinition().getValue();

        ISTFieldTypes type = ISTFieldTypes.valueOf(propDef
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
                    return "";
            case List:
                return getSingleListValue(prop);
            case MultiList:
                ArrayList<String> selected = getMultiListValues(prop);
                return "[" + StringUtils.join(
                        selected.toArray(),
                        ",") + "]";
            case Integer:
                return String.valueOf(prop.getIntegerValue().getValue());
            default:
                return null;
        }
    }
}
