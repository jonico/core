package com.collabnet.ccf.pi.qc.api.dcom;


import com.collabnet.ccf.pi.qc.api.ICommand;
import com.collabnet.ccf.pi.qc.api.IConnection;
import com.collabnet.ccf.pi.qc.api.IFactory;
import com.collabnet.ccf.pi.qc.api.IHistory;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Connection extends ActiveXComponent implements IConnection
{
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

    public IFactory getBugFactory()
    {
        return new Factory(getPropertyAsComponent("BugFactory"));
    }
    
    public ICommand getCommand()
    {
        return new Command(getPropertyAsComponent("Command"));
    }

    // trial by Madan - doesnt work yet
    public IHistory getHistory()
    {
    	return new History(getPropertyAsComponent("TDHistory"));
    }

    public void disconnect()
    {
    	loggedIn = false;
        Dispatch.call(this, "DisconnectProject");
        Dispatch.call(this, "ReleaseConnection");
    }
}
