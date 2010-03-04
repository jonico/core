package com.collabnet.ccf.swp;

import javax.xml.rpc.ServiceException;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class SWPConnectionFactory implements ConnectionFactory<Connection> {
	
	public static final String PARAM_DELIMITER = ":";

	public void closeConnection(Connection connection)
			throws ConnectionException {
		// TODO Auto-generated method stub

	}

	public Connection createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo,
			ConnectionManager<Connection> connectionManager)
			throws ConnectionException {
		// TODO implement me
		
		try {
			return new Connection(repositoryId, repositoryKind, connectionInfo, credentialInfo, connectionManager);
		} catch (ServiceException e) {
			throw new ConnectionException("Could not connect to SWP" , e);
		}
	}

	public boolean isAlive(Connection connection) {
		// TODO Auto-generated method stub
		return true;
	}

}
