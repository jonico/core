package com.collabnet.ccf.pi.qc.api;


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
