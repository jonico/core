package td2jira.td.api;

import td2jira.Config;
import td2jira.td.api.dcom.Connection;

public class ConnectionFactory {
	private static IConnection instance ;
	public static IConnection getInstance(String server, String domain, String project, String user, String pass) {
		if( instance == null || !instance.isLoggedIn() ) {
			String clz = Config.TD_IMPLEMENTATION;
			if( clz == null || clz.trim().length() == 0 ) clz = Connection.class.getCanonicalName();
			try {
				Class[] types = new Class[]{String.class,String.class,String.class,String.class,String.class};
				Object[] params = new Object[]{server,domain,project,user,pass};
				instance = (IConnection) Class.forName(clz).getConstructor(types).newInstance(params);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return instance;
	}
}
