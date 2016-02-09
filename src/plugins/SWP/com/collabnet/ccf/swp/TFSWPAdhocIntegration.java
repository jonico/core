package com.collabnet.ccf.swp;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.tracker.TrackerClient;
import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.ScrumWorksEndpointBindingStub;
import com.danube.scrumworks.api.client.ScrumWorksServiceLocator;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;

public class TFSWPAdhocIntegration {

    /**
     * @param args
     * @throws ServiceException
     * @throws RemoteException
     */
    public static void main(String[] args) throws ServiceException,
            RemoteException {
        Connection tfcon = Connection.getConnection("https://forge.collab.net",
                "", "", null, null, null, false);
        TrackerClient trackerClient = tfcon.getTrackerClient();
        // end point
        // https://scrumworks.danube.com/scrumworks-api/scrumworks?wsdl
        ScrumWorksServiceLocator locator = new ScrumWorksServiceLocator();
        locator.setScrumWorksEndpointPortEndpointAddress("https://scrumworks.danube.com/scrumworks-api/scrumworks");
        ScrumWorksEndpoint endpoint = locator.getScrumWorksEndpointPort();
        ((ScrumWorksEndpointBindingStub) endpoint).setUsername("");
        ((ScrumWorksEndpointBindingStub) endpoint).setPassword("");
        ProductWSO product = endpoint.getProductByName("SWP-TF Integration");
        BacklogItemWSO[] pbis = endpoint.getActiveBacklogItems(product);

        for (BacklogItemWSO pbi : pbis) {
            trackerClient.createArtifact("tracker2119", pbi.getTitle(),
                    pbi.getDescription(), null, null, "Open", null, 2,
                    pbi.getEstimate() == null ? 0 : pbi.getEstimate(),
                    pbi.getEstimate() == null ? 0 : pbi.getEstimate(), false,
                    0, false, null, null, null, null, new FieldValues(), null,
                    null, null);
        }
    }

}
