/**
 * 
 */
package com.collabnet.ccf.samples;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;


/**
 * @author jnicolai
 *
 */
public class SampleConnectionFactory implements ConnectionFactory<String> {

	public void closeConnection(String connection) throws ConnectionException {
		// TODO Auto-generated method stub
		
	}

	public String createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo, ConnectionManager<String> connectionManager)
			throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAlive(String connection) {
		// TODO Auto-generated method stub
		return false;
	}

}
