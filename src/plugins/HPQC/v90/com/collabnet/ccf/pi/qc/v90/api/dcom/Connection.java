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

// VZK
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.pi.qc.v90.api.IBugFactory;
import com.collabnet.ccf.pi.qc.v90.api.ICommand;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IHistory;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.collabnet.ccf.pi.qc.v90.api.IRequirementsFactory;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Connection extends ActiveXComponent implements IConnection
{
	// VZK 
	private static final Log log = LogFactory.getLog(Connection.class);
	
	
	private IBugFactory bugFactory = null;
	private IRequirementsFactory requirementsFactory = null;
	private ICommand command = null;
	private String userName = null;
	private String majorVersion;
	private String minorVersion;
	
	// VZK
	private OraConnection oc = null;
	
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public Connection(String server, String domain, String project, String user, String pass) {
        super("TDApiOle80.TDConnection");
        initConnectionEx(server);
        login(user, pass);
        populateVersions();
        connect(domain, project);
        oc = new OraConnection(domain, project);
    }

	private void populateVersions() {
		Variant majorRef = new Variant("defaultMajor", true);
		Variant minorRef = new Variant("defaultMinor", true);
		try {
			Dispatch.callSub(this, "GetTDVersion", majorRef, minorRef);
			majorVersion = majorRef.getStringRef();
			minorVersion = minorRef.getStringRef();
		} finally {
			majorRef.safeRelease();
			minorRef.safeRelease();
		}
	}

	
    boolean loggedIn = false;
    public void login(String user, String pass)
    {
        Dispatch.call(this, "Login", user, pass);
        loggedIn = true;
        userName = user;
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

	public String getUsername() {
		if (!loggedIn) {
			return null;
		} else {
			return userName;
		}
	}

	private Boolean likeStatementStandardsCompliant = null;
	/**
	 * Determines whether SQL LIKE-statements are handled according to the SQL standard.
	 * 
	 * MS SQL (and MySQL) interprets square brackets in SQL like statements much like a shell.
	 * Standard-compliant DBs (including Oracle and PostgreSQL) just treat them like regular
	 * characters.
	 * 
	 * We rely on the table AUDIT_LOG being present and not empty,
	 * because oracle would need "from dual" and mssql would need a query without a
	 * from clause otherwise.
	 */
	@Override
	public boolean isLikeStatementStandardsCompliant() {
		if(likeStatementStandardsCompliant != null) {
			return likeStatementStandardsCompliant.booleanValue();
		}
		IRecordSet rs = null;
		try {
			rs = executeSQL("select distinct 1 from audit_log where 'a' like '[abc]'");
			likeStatementStandardsCompliant =  (rs != null && rs.getRecordCount() == 0);
			return likeStatementStandardsCompliant.booleanValue();
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
	}

	//	if (log.isDebugEnabled()) {
	//		log.debug("Going to execute SQL statement " + sql);
	//	}
		public IRecordSet executeSQL(String sql) {
			ICommand command = null;
			try {
				// VZK
				log.debug("SQLExec: " + sql);
//				command = getCommand();
//				command.setCommandText(sql);
//				IRecordSet rs = command.execute();
				IRecordSet rs = oc.executeSql(sql);
				log.debug("SQL returned " + rs.getRecordCount() + " records");
				return rs;
			} finally {
				command = null;
			}
		}

		/**
		 * This functions modifies a string that still contains special characters
		 * in a format that it can be put into an SQL LIKE query
		 * This method is necessary because we cannot use prepared statements for the
		 * HP QC COM API.
		 *
		 * Security: The method must escape dangerous characters to prevent SQL injection attacks.
		 * Unfortunately, we do not know the underlying data base so that we can only
		 * guess all dangerous characters.
		 *
		 * @param unsanitizedString String that still contains special character
		 * that may conflict with our SQL statement
		 * @param escapeCharacter character that should be used to escape dangerous characters
		 * @return sanitized string ready to use within an SQL LIKE statement
		 */
		public String sanitizeStringForSQLLikeQuery(String unsanitizedString, String escapeCharacter) {
			// TODO Find a more performant way for string substitution
			unsanitizedString=unsanitizedString.replace(escapeCharacter,escapeCharacter+escapeCharacter);
			unsanitizedString=unsanitizedString.replace("'","''");
			String[] escapeStrings = isLikeStatementStandardsCompliant() ?
				new String[]{"%", "_"} :
				new String[]{"%", "_", "[", "]"};
			for (String s: escapeStrings) {
				unsanitizedString = unsanitizedString.replace(s, escapeCharacter + s);
			}
			return unsanitizedString;
		}

		public String getMajorVersion() {
			return majorVersion;
		}

		public String getMinorVersion() {
			return minorVersion;
		}

}
