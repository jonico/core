package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import org.apache.commons.lang.StringUtils;

import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;

/**
 * @author madhusuthanan
 * 
 */
public class SFEEConnectionFactory implements ConnectionFactory<Connection> {
	
	public static final String PARAM_DELIMITER = ":";

	public void closeConnection(Connection connection) {
		ISourceForgeSoap sfSoap = connection.getSfSoap();
		String sessionId = connection.getSessionId();
		String username = connection.getUserName();
		try {
			sfSoap.logoff(username, sessionId);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Connection Factory implementation for the SFEE adaptor. 
	 * 1. connectionInfo
	 * contains the server URL.
	 * 2. credentialInfo contains the user name and
	 * password delimited by ':'
	 * 
	 * The Repository ID contains the SFEE tracker ID
	 */
	public Connection createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) {
		if(StringUtils.isEmpty(repositoryId)){
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
		Connection connection = null;
		ISourceForgeSoap sfSoap = (ISourceForgeSoap) ClientSoapStubFactory.getSoapStub(ISourceForgeSoap.class, connectionInfo);
		String sessionId = null;
		try {
			sessionId = login(sfSoap, username, password);
			connection = new Connection(username, sfSoap, sessionId);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * Logins the user and save the session id.
	 * 
	 * @param sfSoap2
	 * @param username
	 *            User name.
	 * @param password
	 *            Password
	 * @throws RemoteException
	 *             when an error is encountered during login.
	 */
	public String login(ISourceForgeSoap sfSoap, String username, String password) throws RemoteException {
		try {
		    String sessionId = sfSoap.login(username, password);
		    return sessionId;
		} catch (RemoteException e) {
		    throw e;
		}
	}

	public boolean isAlive(Connection connection) {
		ISourceForgeSoap sfSoap = connection.getSfSoap();
		String sessionId = connection.getSessionId();
		try {
			sfSoap.keepAlive(sessionId);
		} catch (RemoteException e) {
			return false;
		}
		return true;
	}

}
