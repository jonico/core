package com.collabnet.ccf.pi.qc.v90.api;

import com.collabnet.ccf.pi.qc.v90.api.dcom.Connection;

public class ConnectionFactory {
	private static IConnection instance ;
	@SuppressWarnings("unchecked")
	public static IConnection getInstance(String server, String domain, String project, String user, String pass) {
		if( instance == null || !instance.isLoggedIn() ) {
			String clz = "com.collabnet.ccf.pi.qc.v90.api.dcom.Connection";
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
