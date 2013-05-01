package com.collabnet.ccf.rcq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.rational.clearquest.cqjni.CQException;

public class RCQConnectionFactory implements ConnectionFactory<RCQConnection> {

	public static final String PARAM_DELIMITER = "<RCQDELIM>";
	public static final String DOMAIM_DELIMETER = "\\\\";
	private static final Log log = LogFactory.getLog(RCQConnectionFactory.class);
	
	/**
	 * Closes CQ connection
	 */
	public void closeConnection(RCQConnection connection)
			throws ConnectionException {
		connection.shutdown();
	}

	/**
	 * Create a CQ connection object
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
			throw new ConnectionException("Could not connect to RCQ", e);
		}
	}

	/**
	 * Call method to check whether ClearQuest connection still works
	 */
	public boolean isAlive(RCQConnection connection) {
		try {
			return connection.getCqSession().CheckHeartbeat();
		} catch (CQException e) {
			log.error("Lost connection Session" , e);
			return false;
		}
	}

}
