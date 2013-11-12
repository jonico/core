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

package com.collabnet.ccf.pi.qc.v90.api;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Connection;

public class ConnectionFactory {
    public static IConnection getInstance(String server, String domain,
            String project, String user, String pass)
            throws ConnectionException {
        IConnection instance = null;
        try {
            instance = new Connection(server, domain, project, user, pass);
        } catch (Exception e) {
            String cause = "Could not connect to server " + server
                    + " domain: " + domain + " project: " + project;
            throw new ConnectionException(cause, e);
        }
        return instance;
    }
}
