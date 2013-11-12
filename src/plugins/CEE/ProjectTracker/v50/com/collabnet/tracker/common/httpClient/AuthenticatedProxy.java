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

/*******************************************************************************
 * Copyright (c) 2004, 2007 Mylyn project committers and others. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package com.collabnet.tracker.common.httpClient;

import java.net.Proxy;
import java.net.SocketAddress;

/**
 * Abstraction for a proxy that supports user authentication.
 * 
 * @author Rob Elves
 * @since 2.0
 */
public class AuthenticatedProxy extends Proxy {

    private String userName = "";

    private String password = "";

    public AuthenticatedProxy(Type type, SocketAddress sa, String userName,
            String password) {
        super(type, sa);
        this.userName = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

}
