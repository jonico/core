package com.collabnet.ccf.rqp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class RQPConnectionFactory implements ConnectionFactory<RQPConnection> {

	public static final String PARAM_DELIMITER = ":";
	private static final Log log = LogFactory
			.getLog(RQPConnectionFactory.class);
	public static final String CONNECTION_INFO_DELIMITER = "\\";
	public static final String PROJECT_EXTENSION = ".rqs";

	/**
	 * Create an RQP connection object
	 */
	public RQPConnection createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo,
			ConnectionManager<RQPConnection> connectionManager)
			throws ConnectionException {
		try {
			try {
				File repositoryFile = new File(connectionInfo);
				if (!repositoryFile.exists()) {
					throw new IOException("The repository path "
							+ connectionInfo + " does not exists.");
				}
				return new RQPConnection(credentialInfo);
			} catch (Exception e) {
				throw new ConnectionException("Could not connect to RQP", e);
			}
		} catch (Exception e) {
			throw new ConnectionException("Could not connect to RQP", e);
		}
	}

	/**
	 * Release the connection.
	 * 
	 * @param connection
	 */
	public static void releaseConnection(RQPConnection connection) {
		try {
			if (connection.isAlive()) {
				connection.disconnect();
			}
		} catch (Exception e) {
			log.warn(
					"Could not close RQP connection. So releasing the Connection COM dispatch",
					e);
		}
	}

	/**
	 * Close RQP connection
	 */
	public void closeConnection(RQPConnection connection) {
		releaseConnection(connection);
		connection = null;
	}

	/**
	 * Call method to check whether RQP connection still works
	 */
	public boolean isAlive(RQPConnection connection) {
		return connection.isAlive();
	}

}
