package com.collabnet.ccf.ist;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExport;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportConnectionAuthenticateServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportConnectionConnectToProjectServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportConnectionDisconnectServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportUserRetrieveByUserNameServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ImportExport;

public class ISTConnection {

    private static final String WEB_SERVICE_SUFFIX    = "/SpiraTest/Services/v4_0/ImportExport.svc";                      //$NON-NLS-1$
    private static final String WEB_SERVICE_NAMESPACE = "{http://www.inflectra.com/SpiraTest/Services/v4.0/}ImportExport"; //$NON-NLS-1$

    private static final Log    log                   = LogFactory
            .getLog(ISTConnection.class);
    private IImportExport       soap                  = null;

    private String              username              = null;
    private String              password              = null;
    private String              baseUrl               = null;
    private int                 projectId             = 0;
    private URL                 serviceUrl            = null;

    public ISTConnection(String credentialInfo, String connectionInfo) {

        if (credentialInfo != null) {
            String[] splitCredentials = credentialInfo
                    .split(ISTConnectionFactory.PARAM_DELIMITER);
            if (splitCredentials != null) {
                if (splitCredentials.length == 1) {
                    username = splitCredentials[0];
                    password = "";
                } else if (splitCredentials.length == 2) {
                    username = splitCredentials[0];
                    password = splitCredentials[1];
                } else {
                    throw new IllegalArgumentException(
                            "Credentials info is not valid.");
                }
            }
        } else {
            throw new IllegalArgumentException("No Credentials provided");
        }

        // connection info: <baseurl>:<projectID>

        if (connectionInfo != null) {
            String[] splitConnection = connectionInfo
                    .split(ISTConnectionFactory.CONNECTION_INFO_DELIMITER);
            if (splitConnection != null && splitConnection.length == 2) {
                baseUrl = splitConnection[0];
                projectId = Integer.valueOf(splitConnection[1]);
            } else {
                throw new IllegalArgumentException("Wrong connection info");
            }
        }

        try {
            serviceUrl = new URL(baseUrl + WEB_SERVICE_SUFFIX);
        } catch (MalformedURLException e) {
            log.error("Malformed URL: " + connectionInfo + WEB_SERVICE_SUFFIX);
        }
        log.debug("Connecting to Spira Test: " + serviceUrl + " as " + username
                + " to project " + projectId);

        // connect SOAP session
        connect();
    }

    public void close() {

        if (this.isAlive()) {
            try {
                soap.connectionDisconnect();
            } catch (IImportExportConnectionDisconnectServiceFaultMessageFaultFaultMessage e) {
                log.error("Could not disconnect!", e);
            }

        }
    }

    private void connect() {
        ImportExport service = new ImportExport(serviceUrl,
                QName.valueOf(WEB_SERVICE_NAMESPACE));
        soap = service.getBasicHttpBindingIImportExport();

        //Make sure that session is maintained
        Map<String, Object> requestContext = ((BindingProvider) soap)
                .getRequestContext();
        requestContext.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
        log.debug("Connecting to IST project #" + projectId + "  as "
                + username);
        try {
            if (!soap.connectionAuthenticate(username, password)) {
                log.error("Authentication failed, please verify credentials");
            }
            if (!soap.connectionConnectToProject(projectId)) {
                log.error("Project Connection failed");
            }
        } catch (IImportExportConnectionAuthenticateServiceFaultMessageFaultFaultMessage e) {
            log.error("Could not authenticate user " + username
                    + ", please verify credentials", e);
        } catch (IImportExportConnectionConnectToProjectServiceFaultMessageFaultFaultMessage e) {
            log.error("Could not open project ID (" + projectId + ")", e);
        }

        log.info("Connection (v" + ISTVersion.getVersion() + ") to "
                + serviceUrl.getHost() + " as " + username + " to project #"
                + projectId);
    }

    public String getIncidentHístory(String spec, int projectId, int incidentId) {

        String ret = null;

        return ret;
    }

    public IImportExport getService() {
        return soap;
    }

    public boolean isAlive() {
        if (username != null) {
            try {
                soap.userRetrieveByUserName(username);
                return true;
            } catch (IImportExportUserRetrieveByUserNameServiceFaultMessageFaultFaultMessage e) {
                log.debug("failed to get user info for " + username, e);
                return false;
            }
        }
        return false;
    }
}
