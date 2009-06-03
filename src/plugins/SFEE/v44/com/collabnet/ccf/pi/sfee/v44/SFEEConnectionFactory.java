/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;

/**
 * This is an implementation of the com.collabnet.ccf.core.eis.connection.ConnectionFactory
 * that will be used by the ConnectionManager to create, close and check if the connection
 * is live and the session id is not invalid.
 * 
 * @author madhusuthanan
 * 
 */
public class SFEEConnectionFactory implements ConnectionFactory<Connection> {
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(SFEEConnectionFactory.class);
	public static final String PARAM_DELIMITER = ":";

	/**
	 * Logs out from the TF system and invalidates the session id.
	 * 
	 * @see com.collabnet.ccf.core.eis.connection.ConnectionFactory#closeConnection(java.lang.Object)
	 */
	public void closeConnection(Connection connection) throws ConnectionException {
		ISourceForgeSoap sfSoap = connection.getSfSoap();
		String sessionId = connection.getSessionId();
		String username = connection.getUserName();
		try {
			sfSoap.logoff(username, sessionId);
		} catch (RemoteException e) {
			String cause = "An error occured while trying to close the connection for "+e.getMessage();
			log.error(cause, e);
			throw new ConnectionException(cause, e);
		}
	}

	/**
	 * Connection Factory implementation for the TF adaptor. 
	 * 1. connectionInfo - contains the server URL.
	 * 2. credentialInfo  - contains the user name and
	 * password delimited by ':'
	 * 
	 * The Repository ID contains the TF tracker ID
	 */
	public Connection createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo,ConnectionManager<Connection> connectionManager) throws ConnectionException {
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
		ISourceForgeSoap sfSoap = null;
		if (connectionManager.isUseStandardTimeoutHandlingCode()) {
			sfSoap = (ISourceForgeSoap) ClientSoapStubFactory.getSoapStub(ISourceForgeSoap.class, connectionInfo);
		}
		else {
			sfSoap = new SourceForgeSOAPTimeoutWrapper(connectionInfo,connectionManager);
		}
		String sessionId = null;
		try {
			sessionId = login(sfSoap, username, password);
			connection = new Connection(username, password, sfSoap, sessionId);
			connectionManager.registerConnection(sessionId, connection);
		} catch (RemoteException e) {
			String cause = "While trying to login into TF "+ connectionInfo 
								+" an exception occured: "+e.getMessage();
			log.error(cause, e);
			throw new ConnectionException(cause, e);
		}
		return connection;
	}

	/**
	 * Logins the user and save the session id.
	 * 
	 * @param sfSoap
	 * @param username
	 *            User name.
	 * @param password
	 *            Password
	 * @throws RemoteException
	 *             when an error is encountered during login.
	 */
	private String login(ISourceForgeSoap sfSoap, String username, String password) throws RemoteException {
	    String sessionId = sfSoap.login(username, password);
	    return sessionId;
	}

	/** 
	 * Checks if the session id associated with this connection obejct is
	 * valid. 
	 * @see com.collabnet.ccf.core.eis.connection.ConnectionFactory#isAlive(java.lang.Object)
	 */
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
