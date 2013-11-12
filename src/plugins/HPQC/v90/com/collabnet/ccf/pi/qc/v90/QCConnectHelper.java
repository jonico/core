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

package com.collabnet.ccf.pi.qc.v90;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.pi.qc.v90.api.ConnectionFactory;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;

public abstract class QCConnectHelper extends LifecycleComponent {

    // The qc connection
    protected IConnection    qcc;

    private String           serverUrl;

    private String           domain;

    private String           projectName;

    private String           userName;

    private String           password;

    private boolean          keepAlive    = true;

    private boolean          firstConnect = true;

    private static final Log log          = LogFactory
                                                  .getLog(QCConnectHelper.class);

    public QCConnectHelper() {
        super();
    }

    public QCConnectHelper(String id) {
        super(id);
    }

    /**
     * Class constructor.
     * 
     * @param qcUrl
     *            URL of the qc instance to connect to. Of the form:
     *            "http://127.0.0.1:8080/qcbin"
     * @param domain
     *            Domain to connect to.
     * @param project
     *            Project to connect to.
     * @param username
     *            Username to use for logging in.
     * @param password
     *            Password for @param username
     * 
     */
    public QCConnectHelper(String serverUrl, String domain, String project,
            String userName, String password) {
        super();
        setServerUrl(serverUrl);
        setDomain(domain);
        setProjectName(project);
        setUserName(userName);
        setPassword(password);
    }

    /**
     * Connects to the QC server and logs into it
     * 
     * @throws IOException
     */
    public void connect() throws IOException {
        // reconnect again
        log.debug("QCConnectHelper.connect was called");
        // TODO what will happen if connection breaks?
        if (firstConnect || !keepAlive) {
            //validate(exceptions); and check exceptions
            try {
                qcc = ConnectionFactory.getInstance(getServerUrl(),
                        getDomain(), getProjectName(), getUserName(),
                        getPassword());
            } catch (ConnectionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            firstConnect = false;
        }
    }

    public void disconnect() {
        log.debug("QCConnectHelper.disconnect was called");
        if (keepAlive)
            return;
        goDisconnect();
    }

    public String getDomain() {
        return domain;
    }

    public String getPassword() {
        return password;
    }

    public String getProjectName() {
        return projectName;
    }

    public IConnection getQcc() {
        return qcc;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String isKeepAlive() {
        return keepAlive ? "true" : "false";
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setKeepAlive(String keepAlive) {
        if (keepAlive.equals("false"))
            this.keepAlive = false;
    }

    public void setPassword(String password) {
        this.password = Obfuscator.deObfuscatePassword(password);
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setQcc(IConnection qcc) {
        this.qcc = qcc;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void stop() {
        firstConnect = true;
        goDisconnect();
        super.stop();
    }

    @SuppressWarnings("unchecked")
    public void validate(List exceptions) {
        // check whether all necessary properties are set
        if (getServerUrl() == null) {
            log.error("serverUrl-property not set");
            exceptions.add(new ValidationException(
                    "serverUrl-property not set", this));
        }
        if (getDomain() == null) {
            log.error("domain-property not set");
            exceptions.add(new ValidationException("domain-property not set",
                    this));
        }
        if (getProjectName() == null) {
            log.error("project-property not set");
            exceptions.add(new ValidationException("project-property not set",
                    this));
        }
        if (getUserName() == null) {
            log.error("userName-property not set");
            exceptions.add(new ValidationException("userName-property not set",
                    this));
        }
        if (getPassword() == null) {
            log.error("password-property no set");
            exceptions.add(new ValidationException("password-property not set",
                    this));
        }
    }

    private void goDisconnect() {
        try {
            qcc.logout();
            qcc.disconnect();
            qcc.safeRelease();
            qcc = null;
        } catch (Exception e) {
            // TODO Declare exception so that it can be processed by OA exception handler
            log.error("QC disconnect failed", e);
        }
    }

}