package com.collabnet.ccf.tfs;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class TFSConnectionFactory implements ConnectionFactory<TFSConnection> {

	public static final String PARAM_DELIMITER = ":";
	public static final String DOMAIM_DELIMETER = "\\\\";

	/**
	 * Closes SWP connection
	 */
	public void closeConnection(TFSConnection connection)
			throws ConnectionException {
		// FIXME Implement
	}

	/**
	 * Create an SWP connection object
	 */
	public TFSConnection createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo,
			ConnectionManager<TFSConnection> connectionManager)
			throws ConnectionException {
		try {
						
			return new TFSConnection(repositoryId, repositoryKind, connectionInfo,
					credentialInfo, connectionManager);
		} catch (Exception e) {
			throw new ConnectionException("Could not connect to SWP", e);
		}
	}

	/**
	 * Call method to check whether SWP connection still works
	 */
	public boolean isAlive(TFSConnection connection) {
		// FIXME
		return true;
	}

}
