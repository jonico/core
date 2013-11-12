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

package com.collabnet.tracker.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

/**
 * A simple CallbackHandler to be used by clients. Typical use:
 * <ol>
 * <li>Call Handler.addUserPwd(username,password);</li>
 * <li>configure Axis engine to use Handler as the callback handler. See
 * {@link WebServiceClient#getEngineConfiguration()} for example of
 * configuration. The important piece of the configuration is specifying the
 * same username as the username specified in the first step; another important
 * piece is specifying Handler as the callback handler class.</li>
 * <li>... now call the webservice ...</li>
 * </ol>
 * 
 * @author Shawn Minto
 * @author moleary
 * @author sszego
 * 
 */
public class Handler implements CallbackHandler {

    private static Map<String, String> mUsernamePwd = new HashMap<String, String>();

    /**
     * Constructor initializes the password for the guest user.
     * 
     */
    public Handler() {
    }

    /**
     * (non-Javadoc)
     * 
     * @see CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    public void handle(Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
                // set the password given a username
                String identifier = pc.getIdentifer();
                UserIdentifier userIdentifier = new UserIdentifier(identifier);
                String userName = userIdentifier.getUserName();
                String password = mUsernamePwd.get(userName);
                pc.setPassword(password);
            } else {
                throw new UnsupportedCallbackException(callbacks[i],
                        "Client: Unrecognized Callback");
            }
        }
    }

    /**
     * Adds a username/password pair to this handler
     * 
     * @param userName
     *            the username
     * @param password
     *            the password
     */
    public static void addUserPwd(String userName, String password) {
        mUsernamePwd.put(userName, password);
    }

}
