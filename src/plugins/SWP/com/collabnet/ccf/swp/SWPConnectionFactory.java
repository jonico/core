package com.collabnet.ccf.swp;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class SWPConnectionFactory implements ConnectionFactory<Connection> {

	public void closeConnection(Connection connection)
			throws ConnectionException {
		// TODO Auto-generated method stub

	}

	public Connection createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo,
			ConnectionManager<Connection> connectionManager)
			throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAlive(Connection connection) {
		// TODO Auto-generated method stub
		return false;
	}

}
