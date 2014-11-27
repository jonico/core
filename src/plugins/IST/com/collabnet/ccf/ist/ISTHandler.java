package com.collabnet.ccf.ist;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ArtifactState;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteFilter;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ObjectFactory;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteSort;

public class ISTHandler {

    public static ArrayOfRemoteIncident getIncidentsSorted(
            ISTConnection connection) {
        objectFactory.createArrayOfRemoteIncident();
        ArrayOfRemoteFilter filters = objectFactory.createArrayOfRemoteFilter();
        RemoteSort sort = objectFactory.createRemoteSort();
        sort.setPropertyName(objectFactory
                .createRemoteFilterPropertyName("LastUpdateDate"));
        sort.setSortAscending(false);

        try {
            return connection.getService().incidentRetrieve(filters, sort, 0,
                    10000);
        } catch (IImportExportIncidentRetrieveServiceFaultMessageFaultFaultMessage e) {
            log.error("Failed to retrieve indicents!", e);
        }

        return null;

    }

    private ISTConnection              connection    = null;

    private static final Log           log           = LogFactory
                                                             .getLog(ISTHandler.class);

    private static final ObjectFactory objectFactory = new ObjectFactory();

    public ISTHandler(ISTConnection c) {
        connection = c;
    }

    private Date getRealLastUpdated(RemoteIncident incident) {

        ISTMetaData.toDate(incident.getLastUpdateDate());
        ArrayOfRemoteFilter filters = objectFactory.createArrayOfRemoteFilter();
        RemoteSort sort = objectFactory.createRemoteSort();

        // FIXME check attachments

        ArrayOfRemoteDocument documents = null;
        try {
            documents = connection.getService().documentRetrieveForArtifact(
                    incident.getArtifactTypeId(),
                    incident.getIncidentId().getValue(), filters, sort);
        } catch (IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage e) {
            log.error("Failed to retrieve documents for incident #"
                    + incident.getIncidentId().getValue(), e);
        }

        log.debug("  Found " + documents.getRemoteDocument().size()
                + " attachments");
        for (RemoteDocument d : documents.getRemoteDocument()) {
            log.debug(String.format("    %-15s  %-23s", d.getFilenameOrUrl()
                    .getValue(), d.getUploadDate().toString()));
        }

        // get comments
        ArrayOfRemoteComment comments = null;
        try {
            comments = connection.getService().incidentRetrieveComments(
                    incident.getIncidentId().getValue());
        } catch (IImportExportIncidentRetrieveCommentsServiceFaultMessageFaultFaultMessage e) {
            log.error("Failed to retrieve comments for incident #"
                    + incident.getIncidentId().getValue(), e);
        }

        log.debug("  Found " + comments.getRemoteComment().size() + " comments");
        for (RemoteComment c : comments.getRemoteComment()) {
            log.debug(String
                    .format("    %5s(...)  %-23s", c.getText().getValue()
                            .substring(0, 5), c.getCreationDate().getValue()));
        }
        return new Date();
    }

    public void retrieveAllIncidents(ISTConnection connection,
            Date lastModifedDate, ArrayList<ArtifactState> artifactStates) {

        ArrayOfRemoteIncident incidents = getIncidentsSorted(connection);

        for (RemoteIncident i : incidents.getRemoteIncident()) {
            ArtifactState ast = new ArtifactState();

            ast.setArtifactId(String.valueOf(i.getIncidentId().getValue()));
            //ast.setArtifactLastModifiedDate(artifactLastModifiedDate);
        }

    }
}
