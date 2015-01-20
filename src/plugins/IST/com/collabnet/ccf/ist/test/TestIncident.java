package com.collabnet.ccf.ist.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.ist.ISTIncident;
import com.collabnet.ccf.ist.ISTMetaCache;
import com.collabnet.ccf.ist.ISTVersionInfo;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExport;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportConnectionAuthenticateServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportConnectionConnectToProjectServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ImportExport;

public class TestIncident {

    @BeforeClass
    public static void setupOnce() {
        // connect to SpiraTest
        try {
            serviceUrl = new URL(SRVURL + WEB_SERVICE_SUFFIX);
        } catch (MalformedURLException e) {
            log.error("Malformed URL: " + SRVURL + WEB_SERVICE_SUFFIX);
        }
        log.debug("Connecting to Spira Test: " + serviceUrl + " as " + USER
                + " to project " + PROJID);

        ImportExport service = new ImportExport(serviceUrl,
                QName.valueOf(WEB_SERVICE_NAMESPACE));
        soap = service.getBasicHttpBindingIImportExport();

        //Make sure that session is maintained
        Map<String, Object> requestContext = ((BindingProvider) soap)
                .getRequestContext();
        requestContext.put(
                BindingProvider.SESSION_MAINTAIN_PROPERTY,
                true);
        log.debug("Connecting to IST project #" + PROJID + " as " + USER);

        try {
            if (!soap.connectionAuthenticate(
                    USER,
                    PASS)) {
                log.error("Authentication failed, please verify credentials");
            }
            if (!soap.connectionConnectToProject(PROJID)) {
                log.error("Project Connection failed");
            }

        } catch (IImportExportConnectionAuthenticateServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Could not authenticate user " + USER
                    + ", please verify credentials",
                    e);
        } catch (IImportExportConnectionConnectToProjectServiceFaultMessageFaultFaultMessage e) {
            log.error(
                    "Could not open project ID (" + PROJID + ")",
                    e);
        }

        log.info("CONNECTED (v" + ISTVersionInfo.getVersion() + ") "
                + serviceUrl.getHost() + " as " + USER + " to project #"
                + PROJID);
    }

    private final File             dir                   = new File(
            "/Users/volker/Documents/20_coding/01_projects/ccf-spira/git/core/src/plugins/IST/com/collabnet/ccf/ist/test");
    private static final Log       log                   = LogFactory
            .getLog(TestIncident.class);

    private static final SAXReader xr                    = new SAXReader();

    private final File[]           xmlfiles              = dir.listFiles(new FilenameFilter() {
        public boolean accept(
                File dir,
                String name) {
            return name
                    .toLowerCase()
                    .endsWith(
                            ".xml")
                            && name
                            .toLowerCase()
                            .startsWith(
                                    "ga");
        }
    });
    private static final String    WEB_SERVICE_SUFFIX    = "/SpiraTest/Services/v4_0/ImportExport.svc";                                                                         //$NON-NLS-1$
    private static final String    WEB_SERVICE_NAMESPACE = "{http://www.inflectra.com/SpiraTest/Services/v4.0/}ImportExport";                                                   //$NON-NLS-1$
    private static final String    PASS                  = "#ge63Fc1";
    private static final String    SRVURL                = "https://test.ebaotech.com";
    private static final String    USER                  = "ccf.connector";

    private static final int       PROJID                = 21;
    private static IImportExport   soap                  = null;

    private static URL             serviceUrl            = null;

    @Test
    public final void testCreateIncidents() throws DocumentException,
    GenericArtifactParsingException {
        log.info("========== testCreateIncident()");
        for (File xmlfile : this.xmlfiles) {
            log.info("---------------------------------------------------------------------------");
            log.info("                                " + xmlfile.getName());
            log.info("---------------------------------------------------------------------------");
            Document doc = xr.read(xmlfile);
            ISTIncident i = new ISTIncident(this.soap, new ISTMetaCache(
                    this.soap));
            i.createIncident(GenericArtifactHelper
                    .createGenericArtifactJavaObject(doc));
            assertTrue(
                    "created new incident",
                    i.getId() > 0);

            long versionCreated = i.getVersion();

            log.info(String.format(
                    "created incident: %d with version %d [%d - %d]",
                    i.getId(),
                    i.getVersion(),
                    i.getVersionCount(),
                    i.getVersionHash()));

            log.info("---------------------------------------------------------------------------");
            log.info("---------------------------------------------------------------------------");

            ISTIncident checker = new ISTIncident(this.soap, new ISTMetaCache(
                    this.soap));
            checker.retrieveIncident(i.getId());
            log.info(String.format(
                    "loaded incident:  %d with version %d [%d - %d]",
                    checker.getId(),
                    checker.getVersion(),
                    checker.getVersionCount(),
                    checker.getVersionHash()));

            Assert.assertEquals(
                    versionCreated,
                    checker.getVersion());

        }

    }

}
