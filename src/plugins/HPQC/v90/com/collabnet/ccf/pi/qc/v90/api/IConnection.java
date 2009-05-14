/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.pi.qc.v90.api;


public interface IConnection extends ILifeCycle {
	void login(String user, String pass);
	void logout();
	boolean isLoggedIn();
	void connect(String domain, String project);
	void initConnectionEx(String serverName);
	void connectProjectEx(String domain, String project, String user,String pass);
	void disconnectProject();
	void releaseConnection();
	boolean connected();
	IFactory getBugFactory();
    public ICommand getCommand();
    public IHistory getHistory();
	void disconnect();
}
