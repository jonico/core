package com.collabnet.ccf.rcq;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class RCQConnectionFactory implements ConnectionFactory<RCQConnection> {

	public static final String PARAM_DELIMITER = ":";
	public static final String DOMAIM_DELIMETER = "\\\\";

	/**
	 * Closes SWP connection
	 */
	public void closeConnection(RCQConnection connection)
			throws ConnectionException {
		// FIXME Implement
//		connection.
	}

	/**
	 * Create an SWP connection object
	 */
	public RCQConnection createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo,
			ConnectionManager<RCQConnection> connectionManager)
			throws ConnectionException {
		try {
						
			return new RCQConnection(repositoryId, repositoryKind, connectionInfo,
					credentialInfo, connectionManager);
		} catch (Exception e) {
			throw new ConnectionException("Could not connect to SWP", e);
		}
	}

	/**
	 * Call method to check whether SWP connection still works
	 */
	public boolean isAlive(RCQConnection connection) {
		// FIXME isAlive check.
		return true;
	}

}
