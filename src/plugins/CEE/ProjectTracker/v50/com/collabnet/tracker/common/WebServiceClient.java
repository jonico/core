/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

/*
 * Copyright 2006 CollabNet, Inc. All rights reserved.
 */

package com.collabnet.tracker.common;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;

/**
 * The generic client for PT over axis. This class configures axis with the
 * correct configuration
 * 
 * @author Shawn Minto
 * @author sszego
 */
public class WebServiceClient {

    protected static final int    MINIMUM_SECURITY        = 0;                                 // ws-sec-min
    protected static final int    EXTENDED_SECURITY       = 1;                                 // ws-sec-ext
    protected static final int    COMPLETE_SECURITY       = 2;                                 // ws-sec-com

    //	protected static final String DEFAULT_ENCRYPTION_USER = "cee-server";
    protected static final String DEFAULT_ENCRYPTION_USER = "Collabnet Testing's CollabNet ID";

    private String                mURL                    = null;
    private String                mUserName               = null;
    private String                mPassword               = null;
    private String                mDomainName             = null;
    private String                mProjectName            = null;
    private UserIdentifier        mUserIdentifier         = null;
    private String                mSecurityToken          = null;

    /**
     * 
     * @param service
     *            the service name (e.g., ws/SystemStatus)
     * @return the URL endpoint of the service
     */
    public URL constructServiceURL(String service) {
        URL url;
        try {
            url = new URL(getURL() + "/axis/services/" + getSecurityToken()
                    + service);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to construct URL", e);
        }
        return url;
    }

    /**
     * Returns the default namespace URI
     * 
     * @return the default namespace URI
     */
    public String getDefaultNamespace() {
        return "urn:" + getDomainName();
    }

    /**
     * @return Returns the domainName.
     */
    public String getDomainName() {
        return mDomainName;
    }

    /**
     * @return the EngineConfiguration
     */
    public EngineConfiguration getEngineConfiguration() {
        int security = getSecurityType();
        String configString = getConfigString(security);

        EngineConfiguration config = new FileProvider(new ByteArrayInputStream(
                configString.getBytes()));

        return config;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * @return Returns the projectName.
     */
    public String getProjectName() {
        return mProjectName;
    }

    /**
     * @return Returns the uRL.
     */
    public String getURL() {
        return mURL;
    }

    /**
     * @return Returns the userIdentifier.
     */
    public UserIdentifier getUserIdentifier() {
        return mUserIdentifier;
    }

    /**
     * @return Returns the userName.
     */
    public String getUserName() {
        return mUserName;
    }

    public void init(String user, String password, String url)
            throws MalformedURLException {
        mUserName = user;
        mPassword = password;
        mURL = url;
        if (password == null || password.length() == 0)
            mSecurityToken = "ws-sec-ext";
        else
            mSecurityToken = "ws-sec-min";

        computeProjectAndDomain(mURL);
        mUserIdentifier = new UserIdentifier(mUserName, mDomainName,
                mProjectName);
        Handler.addUserPwd(mUserName, mPassword);
    }

    /**
     * @param url
     *            The uRL to set.
     */

    /**
     * Returns the security token (part of the endpoint url
     * 
     * @return one of ws-sec-min, ws-sec-ext, ws-sec-com
     */
    protected String getSecurityToken() {
        return mSecurityToken;
    }

    /**
     * Computes the domain and project name from the url
     * 
     * @param urlString
     *            a sting like, http://www.domain.com
     * @throws MalformedURLException
     *             if the urlString is not a well formed URL.
     */
    private void computeProjectAndDomain(String urlString)
            throws MalformedURLException {
        URL url = new URL(urlString);
        String host = url.getHost();
        int dotPos = host.indexOf('.');
        int endPos = host.length();
        if (host.endsWith("/")) {
            endPos--;
        }
        int colPos = host.indexOf(':');
        if (colPos != -1) {
            endPos = colPos;
        }
        mProjectName = host.substring(0, dotPos);
        if (mDomainName == null) {
            mDomainName = host.substring(dotPos + 1, endPos);
        }
    }

    private String getConfigString(int type) {
        String minConfigString = "<deployment xmlns=\"http://xml.apache.org/axis/wsdd/\" xmlns:java=\"http://xml.apache.org/axis/wsdd/providers/java\">"
                + "<transport name=\"http\" pivot=\"java:com.collabnet.tracker.common.httpClient.TrackerHttpSender\" />"
                + "<transport name=\"https\" pivot=\"java:com.collabnet.tracker.common.httpClient.TrackerHttpSender\"/>"
                + "<transport name=\"local\" pivot=\"java:org.apache.axis.transport.local.LocalSender\"/>"
                + "<globalConfiguration >"
                + "<requestFlow >"
                + "<handler type=\"java:org.apache.ws.axis.security.WSDoAllSender\" >"
                + "<parameter name=\"action\" value=\"UsernameToken\"/>"
                + "<parameter name=\"user\" value=\""
                + mUserIdentifier.toString()
                + "\"/>"
                + "<parameter name=\"passwordCallbackClass\" value=\"com.collabnet.tracker.common.Handler\"/>"
                + "<parameter name=\"passwordType\" value=\"PasswordText\"/>"
                + "</handler>"
                + "</requestFlow >"
                + "</globalConfiguration >"
                + "</deployment>";
        String extendedConfigString = "<deployment xmlns=\"http://xml.apache.org/axis/wsdd/\" "
                + "xmlns:java=\"http://xml.apache.org/axis/wsdd/providers/java\">"
                + "<transport name=\"http\" pivot=\"java:com.collabnet.tracker.common.httpClient.TrackerHttpSender\" />"
                + "<transport name=\"https\" pivot=\"java:com.collabnet.tracker.common.httpClient.TrackerHttpSender\"/>"
                + "<transport name=\"local\" pivot=\"java:org.apache.axis.transport.local.LocalSender\"/>"

                + "<globalConfiguration >"
                + "<requestFlow>"
                + "<handler type=\"java:org.apache.ws.axis.security.WSDoAllSender\" >"
                + "<parameter name=\"action\" value=\"UsernameToken Encrypt\"/>"
                + "<parameter name=\"user\" value=\""
                + mUserIdentifier
                + "\"/>"
                + "<parameter name=\"passwordCallbackClass\" "
                + "value=\"com.collabnet.tracker.common.Handler\"/>"
                + "<parameter name=\"passwordType\" value=\"PasswordText\" />"
                + "<parameter name=\"addUTElements\" value=\"Nonce Created\" />"
                + "<parameter name=\"encryptionPropFile\" value=\"server-cert.properties\" />"
                + "<parameter name=\"encryptionKeyIdentifier\" value=\"X509KeyIdentifier\" />"
                + "<!-- Use the Server's cert/key to encrypt the request -->"
                + "<parameter name=\"encryptionUser\" value=\""
                + DEFAULT_ENCRYPTION_USER
                + "\" />"
                + "<parameter name=\"encryptionParts\" "
                + "value=\"{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd}UsernameToken\" />"
                + "</handler>"
                + "</requestFlow>"
                + "</globalConfiguration >"
                + "</deployment>";

        if (type == MINIMUM_SECURITY) {
            return minConfigString;
        } else {
            return extendedConfigString;
        }
    }

    /**
     * Returns one of MINIMUM_SECURITY, EXTENDED_SECURITY or COMPLETE_SECURITY
     * depending on the URL. If security token is 'ws-sec-min', then
     * MINIMUM_SECURITY is returned; if it is 'ws-sec-ext' then
     * EXTENDED_SECURITY is returned.
     * 
     * @return one of MINIMUM_SECURITY, EXTENDED_SECURITY or COMPLETE_SECURITY
     */
    private int getSecurityType() {
        if (mSecurityToken == null) {
            return MINIMUM_SECURITY;
        } else if (mSecurityToken.equals("ws-sec-min")) {
            return MINIMUM_SECURITY;
        } else if (mSecurityToken.equals("ws-sec-ext")) {
            return EXTENDED_SECURITY;
        } else if (mSecurityToken.equals("ws-sec-com")) {
            return COMPLETE_SECURITY;
        }
        throw new IllegalArgumentException("Security token must be one of "
                + "'ws-sec-min', 'ws-sec-ext' or 'ws-sec-com'.");
    }

}
