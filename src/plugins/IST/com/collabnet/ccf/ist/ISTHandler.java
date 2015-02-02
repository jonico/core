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
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteFilter;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ObjectFactory;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteSort;

public class ISTHandler {

    class ArtifactStateComparator implements Comparator<ArtifactState> {

        @Override
        public int compare(ArtifactState as1, ArtifactState as2) {
            return as1.getArtifactLastModifiedDate().compareTo(
                    as2.getArtifactLastModifiedDate());
        }
    }

    public static void addGAField(GenericArtifact ga, String label,
            Object value, FieldValueTypeValue fieldValueTypeValue,
            String fieldType) {
        GenericArtifactField gaf = ga.addNewField(
                label,
                fieldType);
        gaf.setFieldValueType(fieldValueTypeValue);
        gaf.setFieldAction(FieldActionValue.REPLACE);
        gaf.setFieldValue(value);
    }

    public static ArrayOfRemoteIncident getIncidentsSorted(
            ISTConnection connection) {
        of.createArrayOfRemoteIncident();
        ArrayOfRemoteFilter filters = of.createArrayOfRemoteFilter();
        RemoteSort sort = of.createRemoteSort();
        sort.setPropertyName(of
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

    /**
     * Converts XMLGregorianCalendar to java.util.Date in Java
     */
    public static Date toDate(XMLGregorianCalendar calendar) {
        if (calendar == null) {
            return null;
        }
        return (Date) calendar.toGregorianCalendar().getTime();
    }

    /**
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

    private static final DateFormat    df         = GenericArtifactHelper.df;
    private static final ObjectFactory of         = new ObjectFactory();

    private static final Log           log        = LogFactory
            .getLog(ISTHandler.class);
    private ISTConnection              connection = null;
    private ISTMetaCache               meta       = null;

    private RemoteSort                 dateSort   = of.createRemoteSort();
    static final ArrayOfRemoteFilter   UNFILTERED = of.createArrayOfRemoteFilter();

    static final RemoteSort            UNSORTED   = of.createRemoteSort();

    public ISTHandler(ISTConnection conn, ISTMetaCache cache) {
        this.connection = conn;
        this.meta = cache;

        // Configure the Incident Query to sort after Last Update
        // not really needed as we have to walk over all of them in any case....
        dateSort.setPropertyName(of
                .createRemoteFilterPropertyName("LastUpdateDate"));
        dateSort.setSortAscending(false);
    }

    public void retrieveChangedIncidents(final Date lastModifiedDate,
            ArrayList<ArtifactState> artifactStates) {

        ArrayOfRemoteIncident allincidents = null;

        try {
            // fetch *all* incidents
            // currently the only way to identify all changed ones and to build the version
            // Integer.MAX_VALUE might break the SOAP message length for larger numbers
            // TODO use pagination of approx 500 incidents.
            allincidents = connection.getService().incidentRetrieve(
                    ISTHandler.UNFILTERED,
                    this.dateSort,
                    0,
                    Integer.MAX_VALUE);
        } catch (IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Failed to retrieve indicents!",
                    e);
        }

        for (RemoteIncident ri : allincidents.getRemoteIncident()) {
            ISTIncident inc = new ISTIncident(connection.getService(),
                    this.meta);
            inc.setIncident(ri);
            ArtifactState xs = new ArtifactState();
            xs.setArtifactId(String.valueOf(inc.getId()));

            Date lastUpdated = inc.getLastUpdateDate();

            xs.setArtifactLastModifiedDate(lastUpdated);
            xs.setArtifactVersion(inc.getVersion());

            inc.printIncidentInfo();

            artifactStates.add(xs);
        }

        // update sort order, last updates last
        Collections.sort(
                artifactStates,
                new ArtifactStateComparator());

        artifactStates.size();

        // TODO do I have to remove a few seconds from last modified to avoid racing conditions?
        // check with DEV about heuristics
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

    }

    public void retrieveIncident(Date lastVisitedDate, String storedVersion,
            GenericArtifact ga) {

        // load incident

        ISTIncident ri = new ISTIncident(connection.getService(), this.meta);

        ri.retrieveIncident(Integer.valueOf(ga.getSourceArtifactId()));

        // write data from incident to ga
        ri.fetchIncident(
                ga,
                lastVisitedDate);

        boolean isResync = false;
        boolean isIgnore = false;

        long compareToVersion = ri.getVersion();
        if (storedVersion != null) {
            // update version counter to match the database
            ri.setVersionCount(ISTVersion.getCountPart(Long
                    .valueOf(storedVersion)));
        }

        ri.getVersionCount();
        ri.incrementVersionCount();

        ga.setSourceArtifactVersion(String.valueOf(ri.getVersion()));

        if (!ri.hashEquals(compareToVersion)) {
            if (ri.getCreationDate().after(
                    lastVisitedDate)) {
                log.info(String
                        .format(
                                "resync is necessary, artifact #%s was created on %s, last visit on %s",
                                ga.getSourceArtifactId(),
                                df.format(ri.getCreationDate()),
                                df.format(lastVisitedDate)));
                isResync = true;
            } else {
                log.debug(String.format(
                        "artifact %s updated by CCF, ignoring it.",
                        ga.getSourceArtifactId()));
                isIgnore = true;
            }
        }

        // currently, no dependencies/parents handled for incidents.

        if (isIgnore) {
            ga.setArtifactAction(GenericArtifact.ArtifactActionValue.IGNORE);
        } else {
            if (isResync) {
                ga.setArtifactAction(GenericArtifact.ArtifactActionValue.RESYNC);
            }
        }
    }
}
