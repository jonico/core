package com.collabnet.ccf.tfs;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class TFSConnectionFactory implements ConnectionFactory<TFSConnection> {

	public static final String PARAM_DELIMITER = ":";
	/**
	 * Closes TFS connection
	 */
	public void closeConnection(TFSConnection connection)
			throws ConnectionException {
	}

	/**
	 * Create an TFS connection object
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
			throw new ConnectionException("Could not connect to TFS", e);
		}
	}

	/**
	 * Call method to check whether TFS connection still works
	 */
	public boolean isAlive(TFSConnection connection) {
		try {
			
			String wiqlQuery = "Select ID from WorkItems where (State = 'Active')";
	        connection.getTpc().getWorkItemClient().query(wiqlQuery);
			return true;
		} catch (Exception e){
			return false;
		}
	}

}
