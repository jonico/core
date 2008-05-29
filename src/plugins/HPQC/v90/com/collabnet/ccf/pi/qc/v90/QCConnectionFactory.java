package com.collabnet.ccf.pi.qc.v90;

import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;

/**
 * @author madhusuthanan
 *
 */
public class QCConnectionFactory implements ConnectionFactory<IConnection> {
	
	public static final String PARAM_DELIMITER = "-";

	public void closeConnection(IConnection connection) {
		connection.logout();
		connection.disconnect();
		connection.safeRelease();
		connection = null;
	}

	/** 
	 * Connection Factory implementation for the QC adaptor.
	 * 1. connectionInfo contains the server URL.
	 * 2. credentialInfo contains the user name and password delimited by ':'
	 * 
	 *  The Repository ID contains the QC domain name and QC project name
	 *  delimited by a ':'
	 */
	public IConnection createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) {
		String domain = null;
		String project = null;
		if(repositoryId != null){
			String[] splitRepoId = repositoryId.split(PARAM_DELIMITER);
			if(splitRepoId != null){
				if(splitRepoId.length == 2){
					domain = splitRepoId[0];
					project = splitRepoId[1];
				}
				else {
					throw new IllegalArgumentException("Repository Id "+repositoryId+" is invalid.");
				}
			}
		}
		else {
			throw new IllegalArgumentException("Repository Id cannot be null");
		}
		
		String username = null;
		String password = null;
		if(credentialInfo != null){
			String[] splitCredentials = credentialInfo.split(PARAM_DELIMITER);
			if(splitCredentials != null){
				if(splitCredentials.length == 1){
					username = splitCredentials[0];
					password = "";
				}
				else if(splitCredentials.length == 2){
					username = splitCredentials[0];
					password = splitCredentials[1];
				}
				else {
					throw new IllegalArgumentException("Credentials info is not valid.");
				}
			}
		}
		
		IConnection connection = com.collabnet.ccf.pi.qc.v90.api.ConnectionFactory.getInstance(connectionInfo,
				domain, project, username, password);
		return connection;
	}

	public boolean isAlive(IConnection connection) {
		return connection.isLoggedIn();
	}

}
