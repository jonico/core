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

package com.collabnet.ccf.teamforge;

import java.rmi.RemoteException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.teamforge.api.Connection;

/**
 * This is an implementation of the com.collabnet.ccf.core.eis.connection.ConnectionFactory
 * that will be used by the ConnectionManager to create, close and check if the connection
 * is live and the session id is not invalid.
 * 
 * @author Johannes Nicolai
 * 
 */
public class TFConnectionFactory implements ConnectionFactory<Connection> {
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(TFConnectionFactory.class);
	public static final String PARAM_DELIMITER = ":";


	/**
	 * Returns whether this repository id belongs to a tracker
	 * If not, it belongs to a planning folder
	 * @param repositoryId repositoryId
	 * @return true if repository id belongs to a tracker
	 */
	public static boolean isTrackerRepository(String repositoryId) {
		return repositoryId.startsWith("tracker");
	}
	
	/**
	 * If the repository id contains the project id this will be returned
	 * @param repositoryId
	 * @return
	 */
	public static String extractProjectFromRepositoryId(String repositoryId) {
		if(repositoryId != null){
			String[] splitRepo = repositoryId.split("-");
			if(splitRepo != null){
				if(splitRepo.length != 2){
					throw new IllegalArgumentException("Repository id is not valid.");
				}
				else {
					return splitRepo[0];
				}
			}
		}
		throw new IllegalArgumentException("Repository id is not valid.");
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
		
		try {
			String key = systemId + systemKind + repositoryId + repositoryKind + connectionInfo + credentialInfo;
			// we want to make sure that we always get a new connection here since we do connection management on our own
			connection =  Connection.getConnection(connectionInfo, username, password, null, key, Long.toString(System.currentTimeMillis()), false);
			connection.login();
		} catch (RemoteException e) {
			String cause = "While trying to login into TF "+ connectionInfo 
								+" an exception occured: "+e.getMessage();
			log.error(cause, e);
			throw new ConnectionException(cause, e);
		}
		return connection;
	}

	public void closeConnection(Connection connection)
			throws ConnectionException {
		try {
			if (connection.supports50()) {
				connection.getTeamForgeClient().logoff50(connection.getUserId());
			} else {
				connection.getTeamForgeClient().logoff44(connection.getUserId());
			}
		} catch (RemoteException e) {
			String cause = "An error occured while trying to close the connection for "+e.getMessage();
			log.error(cause, e);
			throw new ConnectionException(cause, e);
		}
		
	}

	public boolean isAlive(Connection connection) {
		try {
			if (connection.supports50()) {
				connection.getTeamForgeClient().keepAlive50();
			} else {
				connection.getTeamForgeClient().keepAlive44();
			}
		} catch (RemoteException e) {
			return false;
		}
		return true;
	}

}
