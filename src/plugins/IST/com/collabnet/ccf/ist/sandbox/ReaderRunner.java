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

import com.collabnet.ccf.ist.ISTMetaCache;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteCustomList;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocumentVersion;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteFilter;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.DateRange;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExport;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportConnectionAuthenticateServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportConnectionConnectToProjectServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportCustomPropertyRetrieveCustomListByIdServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportCustomPropertyRetrieveCustomListsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportUserRetrieveByUserNameServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ImportExport;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ObjectFactory;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomList;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomListValue;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocumentVersion;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteFilter;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteSort;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteUser;

public class ReaderRunner {

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
        return RunnerHelper.getMultiListValues(prop);
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
        return RunnerHelper.getSingleListValue(prop);
    }

    public static String getValue(RemoteArtifactCustomProperty prop) {
        return RunnerHelper.getValue(prop);
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

        meta = new ISTMetaCache(soap);

        /**
         * MAIN LOOP
         */

        while (true) {
            WriterRunner.createIncidents(10);
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
            String value = RunnerHelper.getValue(prop);
            System.out.printf(
                    "| | |--%-25s = %s\n",
                    name,
                    value);
        }

    }

    static void printIncInfo(RemoteIncident inc) {

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

    private static ISTMetaCache meta                  = null;

    private static final String WEB_SERVICE_SUFFIX    = "/Services/v4_0/ImportExport.svc";                                //$NON-NLS-1$

    private static final String WEB_SERVICE_NAMESPACE = "{http://www.inflectra.com/SpiraTest/Services/v4.0/}ImportExport"; //$NON-NLS-1$

    static ObjectFactory        objectFactory         = new ObjectFactory();

    static IImportExport        soap                  = null;

    static DateFormat           df                    = new SimpleDateFormat(
                                                              "yyyy-MM-dd HH:mm:ss");

    private static DateFormat   tf                    = new SimpleDateFormat(
                                                              "mm:ss");

}
