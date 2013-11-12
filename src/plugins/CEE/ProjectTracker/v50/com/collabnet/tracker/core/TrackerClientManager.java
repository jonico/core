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

package com.collabnet.tracker.core;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage the PT clients and their repository data. This data is stored
 * between executions of eclipse so that it does not need ot be downloaded every
 * time.
 * 
 * @author Shawn Minto
 * 
 */
public class TrackerClientManager {

    private Map<String, TrackerWebServicesClient> clientByUrl = new HashMap<String, TrackerWebServicesClient>();

    private static TrackerClientManager           manager;

    private TrackerClientManager() {
    }

    public TrackerWebServicesClient createClient(String serverUrl,
            String newUserId, String newPassword, String httpAuthUser,
            String httpAuthPass, Proxy proxy) throws MalformedURLException {
        TrackerWebServicesClient client = new TrackerWebServicesClient(
                serverUrl, newUserId, newPassword, proxy, httpAuthUser,
                httpAuthPass);
        clientByUrl.put(serverUrl, client);
        return client;
    }

    public synchronized TrackerWebServicesClient getClient(String serverUrl) {
        return clientByUrl.get(serverUrl);
    }

    public synchronized static TrackerClientManager getInstance() {
        if (manager == null)
            manager = new TrackerClientManager();
        return manager;
    }

}
