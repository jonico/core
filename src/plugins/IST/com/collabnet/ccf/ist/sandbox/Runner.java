package com.collabnet.ccf.ist.sandbox;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.collabnet.ccf.ist.ISTCustomFieldType;
import com.collabnet.ccf.ist.ISTMetaCache;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteCustomList;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocumentVersion;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteFilter;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfint;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.DateRange;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExport;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportConnectionAuthenticateServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportConnectionConnectToProjectServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportCustomPropertyRetrieveCustomListByIdServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportCustomPropertyRetrieveCustomListsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentAddCommentsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentCreateServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentCreateValidationFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportUserRetrieveByUserNameServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ImportExport;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ObjectFactory;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomList;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomListValue;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocumentVersion;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteFilter;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteSort;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteUser;

public class Runner {

    private static void addRandomComments(LoremIpsum li, RemoteIncident i) {
        // add comments every now and then
        if (li.getTruth()) {
            ArrayOfRemoteComment comments = objectFactory
                    .createArrayOfRemoteComment();
            for (int t = 0; t < 5; t++) {
                if (li.getTruth()) {
                    RemoteComment comment = objectFactory.createRemoteComment();
                    // RemoteComment comment = new RemoteComment();
                    comment.setArtifactId(i.getIncidentId().getValue());
                    comment.setText(ISTMetaCache.CreateJAXBString(
                            "Text",
                            li.getLine()));
                    comment.setCreationDate(ISTMetaCache
                            .CreateJAXBXMLGregorianCalendar(
                                    "CreationDate",
                                    toXMLGregorianCalendar(new Date())));

                    if (comments.getRemoteComment().add(
                            comment))
                        System.out.println("    added comment to #"
                                + comment.getArtifactId());
                    else
                        System.out.println("    failed to add comment to #"
                                + i.getIncidentId().getValue());
                }
            }

            System.out.println("==== list of comments intended for incident #"
                    + i.getIncidentId().getValue());

            for (RemoteComment rc : comments.getRemoteComment()) {
                System.out
                        .printf(
                                "  on Incident #%d, created on %s, %d characters: %s\n",
                                rc.getArtifactId(),
                                df.format(toDate(rc.getCreationDate()
                                        .getValue())),
                                rc.getText().getValue().length(),
                                rc.getText().getValue());
            }
            try {
                soap.incidentAddComments(comments);
            } catch (IImportExportIncidentAddCommentsServiceFaultMessageFaultFaultMessage e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private static void createIncidents(int howmany) {
        for (int i = 1; i <= howmany; i++) {
            LoremIpsum li = new LoremIpsum(15);

            RemoteIncident inc = createRandomIncident(li);
            printIncInfo(inc);
        }

    }

    /*
     * creates a random incident
     */
    private static RemoteIncident createRandomIncident(LoremIpsum li) {

        RemoteIncident ri = objectFactory.createRemoteIncident();

        //        ri.setIncidentTypeId(objectFactory
        //                .createRemoteIncidentIncidentTypeId(113));
        //
        //        ri.setIncidentStatusId(objectFactory
        //                .createRemoteIncidentIncidentStatusId(113));
        //
        ri.setName(objectFactory.createRemoteIncidentName(li.getShortLine()));
        //
        ri.setDescription(objectFactory.createRemoteIncidentDescription(li
                .getLoremIpsum()));
        //
        //        Date when = li.getDate();
        //
        //        ri.setCreationDate(objectFactory
        //                .createRemoteIncidentCreationDate(toXMLGregorianCalendar(when)));

        // Incident Type
        //        ri.setIncidentTypeName(ISTMetaData.toJXString(
        //                "IncidentTypeName",
        //                li.getTruth() ? "Bug" : "Change Request"));

        // Priority
        ri.setPriorityName(ISTMetaCache.toJXString(
                "PriorityName",
                "3 - Medium"));

        //            int prioId = meta.getPriorityKeyForValue("3 - Medium");
        //            int typeId = meta.getIncidentTypeKeyForValue(ri
        //                    .getIncidentTypeName().getValue());
        //
        //            ri.setPriorityId(ISTMetaData.toJXInt(
        //                    "PriorityId",
        //                    prioId));
        //            ri.setIncidentTypeId(ISTMetaData.toJXInt(
        //                    "IncidentTypeId",
        //                    typeId));

        //            System.out.printf(
        //                    "Attempt: prio `%s` (%d)  and type `%s` (%d)\n",
        //                    ri.getPriorityName().getValue(),
        //                    ri.getPriorityId().getValue(),
        //                    ri.getIncidentTypeName().getValue(),
        //                    ri.getIncidentTypeId().getValue());

        //        System.out.printf(
        //                "Attempt: prio `%s` and type `%s`\n",
        //                ri.getPriorityName().getValue(),
        //                ri.getIncidentTypeName().getValue());

        try {
            RemoteIncident i = soap.incidentCreate(ri);

            System.out.printf(
                    "   ===> created #%d with prio `%s` and type `%s`\n",
                    i.getIncidentId().getValue(),
                    i.getPriorityName().getValue(),
                    i.getIncidentTypeName().getValue());

            //            System.out.println("Attempting update on incident");

            // Incident Type
            //            i.setIncidentTypeId(ISTMetaData.toJXInt(
            //                    "IncidentTypeId",
            //                    typeId));
            //            i.setIncidentTypeName(ISTMetaData.toJXString(
            //                    "IncidentTypeName",
            //                    li.getTruth() ? "Bug" : "Change Request"));
            //
            //            // Priority
            //            i.setPriorityId(ISTMetaData.toJXInt(
            //                    "PriorityId",
            //                    prioId));
            //            i.setPriorityName(ISTMetaData.toJXString(
            //                    "PriorityName",
            //                    "3 - Medium"));
            //
            //
            //            soap.incidentUpdate(i);

            System.out.println("reloading incident #"
                    + i.getIncidentId().getValue());

            RemoteIncident fetch = soap.incidentRetrieveById(i.getIncidentId()
                    .getValue());
            System.out.printf(
                    "   ===> loaded #%d with prio `%s` and type `%s`\n",
                    fetch.getIncidentId().getValue(),
                    fetch.getPriorityName().getValue(),
                    fetch.getIncidentTypeName().getValue());

            return fetch;

        } catch (IImportExportIncidentCreateServiceFaultMessageFaultFaultMessage e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IImportExportIncidentCreateValidationFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //        } catch (IImportExportIncidentUpdateServiceFaultMessageFaultFaultMessage e) {
            //            // TODO Auto-generated catch block
            //            e.printStackTrace();
            //        } catch (IImportExportIncidentUpdateValidationFaultMessageFaultFaultMessage e) {
            //            // TODO Auto-generated catch block
            //            e.printStackTrace();
        } catch (IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayOfRemoteIncident getIncidents() {
        ArrayOfRemoteIncident incidents = objectFactory
                .createArrayOfRemoteIncident();

        long start = System.currentTimeMillis();
        System.out.print("\n\n######\n######\nretrieving incidents...");
        RemoteFilter f1 = objectFactory.createRemoteFilter();
        objectFactory.createMultiValueFilter();

        ArrayOfRemoteFilter filters = objectFactory.createArrayOfRemoteFilter();
        filters.getRemoteFilter().add(
                f1);

        RemoteSort sort = objectFactory.createRemoteSort();
        sort.setPropertyName(objectFactory
                .createRemoteFilterPropertyName("LastUpdateDate"));
        sort.setSortAscending(false);

        try {
            incidents = soap.incidentRetrieve(
                    filters,
                    sort,
                    0,
                    1000);
        } catch (IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        long end = System.currentTimeMillis();

        System.out.printf(
                " retrieving %d incidents took %s\n",
                incidents.getRemoteIncident().size(),
                tf.format(new Date(end - start)));

        return incidents;

    }

    private static ArrayOfRemoteIncident getIncidentsSince(int year, int month,
            int day, int hours, int minutes, int seconds) {
        ArrayOfRemoteIncident incidents = objectFactory
                .createArrayOfRemoteIncident();
        XMLGregorianCalendar cutoffdate = null;
        try {
            cutoffdate = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar(year, month, day, hours, minutes,
                            seconds));
            DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar());
        } catch (DatatypeConfigurationException e) {
            // TODO Auto-generated catch block
            log("wrong date!!");
        }

        RemoteFilter f1 = objectFactory.createRemoteFilter();
        objectFactory.createMultiValueFilter();

        DateRange dr = objectFactory.createDateRange();
        dr.setConsiderTimes(true);
        dr.setEndDate(objectFactory
                .createRemoteTestCaseExecutionDate(cutoffdate));
        // dr.setEndDate(objectFactory.createRemoteTestCaseExecutionDate(now));
        f1.setPropertyName(objectFactory
                .createRemoteIncidentDescription("LastUpdateDate"));
        f1.setDateRangeValue(objectFactory.createRemoteFilterDateRangeValue(dr));

        ArrayOfRemoteFilter filters = objectFactory.createArrayOfRemoteFilter();
        filters.getRemoteFilter().add(
                f1);

        RemoteSort sort = objectFactory.createRemoteSort();
        sort.setPropertyName(objectFactory
                .createRemoteFilterPropertyName("LastUpdateDate"));
        sort.setSortAscending(false);

        try {
            incidents = soap.incidentRetrieve(
                    filters,
                    sort,
                    0,
                    1000);
        } catch (IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return incidents;

    }

    public static ArrayList<String> getMultiListValues(
            RemoteArtifactCustomProperty prop) {
        if (!prop.getIntegerListValue().isNil()) {
            ArrayOfint mvalues = prop.getIntegerListValue().getValue();
            ArrayList<String> list = new ArrayList<String>();
            RemoteCustomProperty propDef = prop.getDefinition().getValue();
            RemoteCustomList multi = propDef.getCustomList().getValue();

            // TODO add CustomListValues Cache
            for (int i : mvalues.getInt()) {
                list.add(multi.getValues().getValue()
                        .getRemoteCustomListValue().get(
                                i).getName().getValue());
            }
            return list;
        }
        return null;
    }

    private static Date getRealLastUpdated(RemoteIncident incident) {

        toDate(incident.getLastUpdateDate());
        ArrayOfRemoteFilter filters = objectFactory.createArrayOfRemoteFilter();
        RemoteSort sort = objectFactory.createRemoteSort();

        // FIXME check attachments

        ArrayOfRemoteDocument documents = null;
        try {
            documents = soap.documentRetrieveForArtifact(
                    incident.getArtifactTypeId(),
                    incident.getIncidentId().getValue(),
                    filters,
                    sort);
        } catch (IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage e) {
            log(
                    "Failed to retrieve documents for incident #"
                            + incident.getIncidentId().getValue(),
                    e);
        }

        for (RemoteDocument d : documents.getRemoteDocument()) {
            log(String.format(
                    "      %-31s  %-23s",
                    d.getFilenameOrUrl().getValue(),
                    d.getUploadDate().toString()));
        }

        // get comments
        ArrayOfRemoteComment comments = null;
        try {
            comments = soap.incidentRetrieveComments(incident.getIncidentId()
                    .getValue());
        } catch (IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage e) {
            log(
                    "Failed to retrieve comments for incident #"
                            + incident.getIncidentId().getValue(),
                    e);
        }

        for (RemoteComment c : comments.getRemoteComment()) {
            log(String.format(
                    "      %-31s  %-23s",
                    c.getText().getValue().trim().substring(
                            0,
                            5) + "(...)",
                    c.getCreationDate().getValue()));
        }

        // log("    Found " + comments.getRemoteComment().size() +
        // " comments and " + documents.getRemoteDocument().size() +
        // " attachments");

        return null;

    }

    public static final String getSingleListValue(
            RemoteArtifactCustomProperty prop) {
        RemoteCustomProperty propDef = prop.getDefinition().getValue();

        if (!prop.getIntegerValue().isNil()
                && !propDef.getCustomPropertyId().isNil()) {
            int listIndex = prop.getIntegerValue().getValue();
            RemoteCustomList listDef = null;
            try {
                listDef = soap.customPropertyRetrieveCustomListById(propDef
                        .getCustomPropertyId().getValue());
            } catch (IImportExportCustomPropertyRetrieveCustomListByIdServiceFaultMessageFaultFaultMessage e) {
                return "(custom list def not found)";
                // e.printStackTrace();
            }
            return listDef.getValues().getValue().getRemoteCustomListValue()
                    .get(
                            listIndex).getName().getValue();
        }
        return "";
    }

    public static String getValue(RemoteArtifactCustomProperty prop) {

        RemoteCustomProperty propDef = prop.getDefinition().getValue();

        ISTCustomFieldType type = ISTCustomFieldType.valueOf(propDef
                .getCustomPropertyTypeName().getValue());
        switch (type) {
            case Text:
                if (prop.getStringValue().isNil()) {
                    return "(not set)";
                } else {
                    String val = prop.getStringValue().getValue().trim();
                    if (val.contains(System.getProperty("line.separator"))) {
                        String first = val.split("\n")[0];
                        return first.trim() + "(...)";
                    } else {
                        return val.trim();
                    }
                }
            case Date:
                if (prop.getDateTimeValue().isNil())
                    return "(not set)";
                else
                    return df
                            .format(toDate(prop.getDateTimeValue().getValue()));
            case List:
                return getSingleListValue(prop);
            case MultiList:
                String multi = "";
                ArrayList<String> selected = getMultiListValues(prop);
                for (String s : selected) {
                    multi += s + ",";
                }
                return multi;
            case Integer:
                return String.valueOf(prop.getIntegerValue().getValue());
            default:
                return null;
        }
    }

    private static void log(String message) {
        System.out.println(message);

    }

    private static void log(String message, Exception e) {
        System.out.println(message);
        e.printStackTrace();
    }

    public static void main(String[] args) throws MalformedURLException {
        // TODO Auto-generated method stub

        log("start");
        long start = System.currentTimeMillis();

        String baseUrl = "https://test.ebaotech.com/SpiraTest";
        URL serviceUrl = new URL(baseUrl + WEB_SERVICE_SUFFIX);

        System.out.printf("initiating service " + baseUrl + WEB_SERVICE_SUFFIX
                + "...");
        ImportExport service = new ImportExport(serviceUrl,
                QName.valueOf(WEB_SERVICE_NAMESPACE));
        System.out.printf("...");
        soap = service.getBasicHttpBindingIImportExport();
        System.out.printf("done\n");

        // Make sure that session is maintained
        System.out.printf("Configuring context...");
        Map<String, Object> requestContext = ((BindingProvider) soap)
                .getRequestContext();
        requestContext.put(
                BindingProvider.SESSION_MAINTAIN_PROPERTY,
                true);
        System.out.printf("done\n");

        System.out.printf("authenticating...");
        try {
            soap.connectionAuthenticate(
                    "ccf.connector",
                    "#ge63Fc1");
            System.out.printf("done\n");
        } catch (IImportExportConnectionAuthenticateServiceFaultMessageFaultFaultMessage e) {
            System.out.printf("FAILED\n");
            e.printStackTrace();
            System.exit(20);
        }
        int projectid = 21;
        System.out.printf("opening project #" + projectid + "...");
        try {
            soap.connectionConnectToProject(projectid);
            System.out.printf("done\n");
        } catch (IImportExportConnectionConnectToProjectServiceFaultMessageFaultFaultMessage e) {
            System.out.printf("FAILED\n");
            e.printStackTrace();
            System.exit(21);
        }

        RemoteUser currentUser = null;
        System.out.printf("getting current user data from server...");
        try {
            currentUser = soap.userRetrieveByUserName("ccf.connector");
            System.out.println("done. User ID is "
                    + currentUser.getUserId().getValue());
        } catch (IImportExportUserRetrieveByUserNameServiceFaultMessageFaultFaultMessage e) {
            System.out.printf("FAILED\n");
            e.printStackTrace();
            System.exit(21);
        }

        long end = System.currentTimeMillis();
        System.out.println("Connection setup took "
                + tf.format(new Date(end - start)));

        meta = new ISTMetaCache(soap, true);

        /**
         * MAIN LOOP
         */

        while (true) {
            createIncidents(10);
            // processIndicents();
        }

    }

    private static void printAllCustomLists() {
        System.out.print("Global Custom Properties: \n");

        try {
            ArrayOfRemoteCustomList allCustomLists = soap
                    .customPropertyRetrieveCustomLists();
            if (allCustomLists.getRemoteCustomList().size() > 0) {
                System.out.println("Known Custom Lists:");

                for (RemoteCustomList cl : allCustomLists.getRemoteCustomList()) {
                    // fetch full list info from server
                    RemoteCustomList cl2 = soap
                            .customPropertyRetrieveCustomListById(cl
                                    .getCustomPropertyListId().getValue());

                    System.out.printf(
                            "  == %s (%d) with %d values \n",
                            cl.getName().getValue(),
                            cl.getCustomPropertyListId().getValue(),
                            cl2.getValues().getValue()
                                    .getRemoteCustomListValue().size());
                    for (RemoteCustomListValue lv : cl2.getValues().getValue()
                            .getRemoteCustomListValue()) {
                        System.out.printf(
                                "     %s (%d)\n",
                                lv.getName().getValue(),
                                lv.getCustomPropertyValueId().getValue());
                    }
                }
            } else {
                System.out.println("No Custom Lists found");
            }
        } catch (IImportExportCustomPropertyRetrieveCustomListsServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        } catch (IImportExportCustomPropertyRetrieveCustomListByIdServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printAttachments(RemoteIncident inc) {
        try {
            ArrayOfRemoteDocument documents = soap.documentRetrieveForArtifact(
                    inc.getArtifactTypeId(),
                    inc.getIncidentId().getValue(),
                    new ArrayOfRemoteFilter(),
                    new RemoteSort());

            System.out.printf(
                    "|--%d attachments\n",
                    documents.getRemoteDocument().size());

            for (RemoteDocument doc : documents.getRemoteDocument()) {
                if (doc.getVersions().isNil()) {
                    System.out.printf(
                            "| |--%-20s - %4d - %3d versions  @%s - %s\n",
                            doc.getFilenameOrUrl().getValue(),
                            doc.getAttachmentId().getValue(),
                            0,
                            doc.getCurrentVersion().getValue(),
                            doc.getUploadDate());
                } else {
                    // FIXME - even if an attachment has multiple versions, this code didn't get executed.
                    System.out.printf(
                            "| |--%-20s - %4d - %3d versions  @%s - %s\n",
                            doc.getFilenameOrUrl(),
                            doc.getAttachmentId().getValue(),
                            doc.getVersions().getValue()
                                    .getRemoteDocumentVersion().size(),
                            doc.getCurrentVersion().getValue(),
                            doc.getUploadDate());
                    ArrayOfRemoteDocumentVersion docVersions = doc
                            .getVersions().getValue();
                    for (RemoteDocumentVersion dv : docVersions
                            .getRemoteDocumentVersion()) {
                        dv.getFilenameOrUrl().getValue();
                        System.out.printf(
                                "| | |--v %s\n",
                                dv.getVersionNumber().getValue(),
                                dv.getFilenameOrUrl());
                    }
                }
            }

        } catch (IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void printCustomProperties(RemoteIncident inc) {
        ArrayOfRemoteArtifactCustomProperty properties = inc
                .getCustomProperties().getValue();
        System.out.printf(
                "| | %d Custom Properties\n",
                properties.getRemoteArtifactCustomProperty().size());
        for (RemoteArtifactCustomProperty prop : properties
                .getRemoteArtifactCustomProperty()) {
            String name = prop.getDefinition().getValue().getName().getValue();
            String value = getValue(prop);
            System.out.printf(
                    "| | |--%-25s = %s\n",
                    name,
                    value);
        }

    }

    private static void printIncInfo(RemoteIncident inc) {

        System.out.printf(
                "\n====\n%4d  %-30s\n",
                inc.getIncidentId().getValue(),
                inc.getName().getValue());
        System.out.printf(
                "|--created on:  %s\n",
                df.format(toDate(inc.getCreationDate().getValue())));
        System.out.printf(
                "|--last update: %s\n",
                df.format(toDate(inc.getLastUpdateDate())));

    }

    private static void processIndicents() {

        ArrayOfRemoteIncident incidents = getIncidents();

        int count = 1;

        int amount = 5;

        for (RemoteIncident inc : incidents.getRemoteIncident()) {

            if (count > amount)
                break;
            else
                count++;

            printIncInfo(inc);

            // printCustomProperties(inc);

            printAttachments(inc);

            //            addRandomComments(
            //                    new LoremIpsum(10),
            //                    inc);

        }
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
            log(
                    "Failed to convert Date to XML Gregorian: "
                            + date.toString(),
                    ex);
        }
        return xmlCalendar;
    }

    private static ISTMetaCache  meta                  = null;

    private static final String  WEB_SERVICE_SUFFIX    = "/Services/v4_0/ImportExport.svc";                                //$NON-NLS-1$

    private static final String  WEB_SERVICE_NAMESPACE = "{http://www.inflectra.com/SpiraTest/Services/v4.0/}ImportExport"; //$NON-NLS-1$

    private static ObjectFactory objectFactory         = new ObjectFactory();

    private static IImportExport soap                  = null;

    private static DateFormat    df                    = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static DateFormat    tf                    = new SimpleDateFormat(
            "mm:ss");

}
