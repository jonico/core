package td2jira.td.api.mock;

import td2jira.td.api.IConnection;
import td2jira.td.api.IFactory;

public class Connection implements IConnection
{
    public Connection(String server, String domain, String project, String user, String pass) {
    }

	public void connect(String domain, String project) {
	}

	public void connectProjectEx(String domain, String project, String user, String pass) {
	}

	public boolean connected() {
		return true;
	}

	public void disconnect() {
	}

	public void disconnectProject() {
	}

	public IFactory getBugFactory() {
		return new Factory();
	}

	public void initConnectionEx(String serverName) {
	}

	public void login(String user, String pass) {
	}

	public void logout() {
	}

    public boolean isLoggedIn() {
		return true;
	}
	
	public void releaseConnection() {
	}

	public void safeRelease() {
	}
}