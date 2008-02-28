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

    public String[] getFieldNames()
    {
    	// Fix this later
    	IFactory bf = getBugFactory();
    	//List<IField> fieldList = bf.getFields();
    	// Temp fix
		String[] bugFields = new String[]{
				"BG_ACTUAL_FIX_TIME",
				"BG_BUG_VER_STAMP",
				"BG_BUG_ID",
				"BG_CLOSING_DATE",
				"BG_CLOSING_VERSION",
				"BG_CYCLE_ID",
				"BG_CYCLE_REFERENCE",
				"BG_DESCRIPTION",
				"BG_DETECTED_BY",
				"BG_DETECTION_DATE",
				"BG_DETECTION_VERSION",
				"BG_DEV_COMMENTS",
				"BG_ESTIMATED_FIX_TIME",
				"BG_EXTENDED_REFERENCE",
				"BG_HAS_CHANGE",
				"BG_PLANNED_CLOSING_VER",
				"BG_PRIORITY",
				"BG_PROJECT",
				"BG_REPRODUCIBLE",
				"BG_REQUEST_ID",
				"BG_REQUEST_NOTE",
				"BG_REQUEST_SERVER",
				"BG_REQUEST_TYPE",
				"BG_RESPONSIBLE",
				"BG_RUN_REFERENCE",
				"BG_SEVERITY",
				"BG_STATUS",
				"BG_STEP_REFERENCE",
				"BG_SUBJECT",
				"BG_SUMMARY",
				"BG_TEST_REFERENCE",
				"BG_TO_MAIL",
				"BG_USER_01",
				"BG_USER_02",
				"BG_USER_03",
				"BG_USER_04",
				"BG_USER_05",
				"BG_USER_06",
				"BG_USER_07",
				"BG_USER_08",
				"BG_USER_09",
				"BG_VTS" };
    
		return bugFields;
    }

    public String[] getFieldTypes() {
    	// Fix this later
    	// Temp fix
		String[] bugFieldTypes = new String[]{
				"Date",			// "BG_ACTUAL_FIX_TIME",
				"Number",		// "BG_BUG_VER_STAMP",
				"Number",		// "BG_BUG_ID",
				"Date",			// "BG_CLOSING_DATE",
				"Number",		// "BG_CLOSING_VERSION",
				"Number",		// BG_CYCLE_ID",
				"Number",		// BG_CYCLE_REFERENCE",
				"Memo",			// BG_DESCRIPTION",
				"User",			// BG_DETECTED_BY",
				"Date",			// BG_DETECTION_DATE",
				"Number",		// BG_DETECTION_VERSION",
				"Memo",			// BG_DEV_COMMENTS",
				"Date",			// BG_ESTIMATED_FIX_TIME",
				"Number",		// BG_EXTENDED_REFERENCE",
				"Boolean",		// BG_HAS_CHANGE",
				"List",			// BG_PLANNED_CLOSING_VER",
				"List",			// BG_PRIORITY",
				"String",		// BG_PROJECT",
				"Boolean",		// BG_REPRODUCIBLE",
				"Number",		// BG_REQUEST_ID",
				"String",		// BG_REQUEST_NOTE",
				"String",		// BG_REQUEST_SERVER",
				"List",			// BG_REQUEST_TYPE",
				"User",			// BG_RESPONSIBLE",
				"String",		// BG_RUN_REFERENCE",
				"List",			// BG_SEVERITY",
				"List",			// BG_STATUS",
				"Number",		// BG_STEP_REFERENCE",
				"String",		// BG_SUBJECT",
				"String",		// BG_SUMMARY",
				"Number",		// BG_TEST_REFERENCE",
				"String",		// BG_TO_MAIL",
				"String",		// BG_USER_01",
				"String",		// BG_USER_02",
				"String",		// BG_USER_03",
				"String",		// BG_USER_04",
				"String",		// "BG_USER_05",
				"String",		// "BG_USER_06",
				"String",		// "BG_USER_07",
				"String",		// "BG_USER_08",
				"String",		// "BG_USER_09",
				"Date"		// BG_VTS"
			};
    
		return bugFieldTypes;
    }

    public void disconnect()
    {
    	loggedIn = false;
        Dispatch.call(this, "DisconnectProject");
        Dispatch.call(this, "ReleaseConnection");
    }
}
