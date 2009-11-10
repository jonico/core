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

package com.collabnet.ccf.pi.qc.v90.api.dcom;


import com.collabnet.ccf.pi.qc.v90.api.ICommand;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IBugFactory;
import com.collabnet.ccf.pi.qc.v90.api.IHistory;
import com.collabnet.ccf.pi.qc.v90.api.IRequirementsFactory;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Connection extends ActiveXComponent implements IConnection
{
	private IBugFactory bugFactory = null;
	private IRequirementsFactory requirementsFactory = null;
	private ICommand command = null;
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public Connection(String server, String domain, String project, String user, String pass) {
        super("TDApiOle80.TDConnection");
        initConnectionEx(server);
        login(user, pass);
        connect(domain, project);
    }

    boolean loggedIn = false;
    public void login(String user, String pass)
    {
        Dispatch.call(this, "Login", user, pass);
        loggedIn = true;
    }

    public void logout()
    {
        Dispatch.call(this, "Logout");
        loggedIn = false;
    }

    public boolean isLoggedIn() {
		return loggedIn;
	}

    public void connect(String domain, String project)
    {
        Dispatch.call(this, "Connect", domain, project);
    }

    public void initConnectionEx(String serverName)
    {
        Dispatch.call(this, "InitConnectionEx", serverName);
    }

    public void connectProjectEx(String domain, String project, String user, String pass)
    {
        login(user, pass);
        connect(domain, project);
    }

    public void disconnectProject()
    {
    	loggedIn = false;
        Dispatch.call(this, "DisconnectProject");
    }

    public void releaseConnection()
    {
    	loggedIn = false;
        Dispatch.call(this, "ReleaseConnection");
    }

    public boolean connected()
    {
        return getPropertyAsBoolean("Connected");
    }

    public IBugFactory getBugFactory()
    {
    	if(bugFactory == null){
    		bugFactory = new BugFactory(getPropertyAsComponent("BugFactory"));
    	}
        return bugFactory;
    }
    
    public IRequirementsFactory getRequirementsFactory()
    {
    	if(requirementsFactory == null){
    		requirementsFactory = new RequirementsFactory(getPropertyAsComponent("ReqFactory"));
    	}
        return requirementsFactory;
    }

    public ICommand getCommand()
    {
    	if(command == null) {
    		command = new Command(getPropertyAsComponent("Command"));
    	}
        return command;
    }

    // trial by Madan - doesnt work yet
    public IHistory getHistory()
    {
    	return new History(getPropertyAsComponent("TDHistory"));
    }

    public void disconnect()
    {
    	loggedIn = false;
    	if(bugFactory != null) {
	    	bugFactory.safeRelease();
	    	bugFactory = null;
    	}
    	if(requirementsFactory != null) {
	    	requirementsFactory.safeRelease();
	    	requirementsFactory = null;
    	}
    	if(command != null) {
	    	command.safeRelease();
	    	command = null;
    	}
        Dispatch.call(this, "DisconnectProject");
        Dispatch.call(this, "ReleaseConnection");
    }
}
