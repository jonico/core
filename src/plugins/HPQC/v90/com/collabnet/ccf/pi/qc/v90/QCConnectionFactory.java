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

package com.collabnet.ccf.pi.qc.v90;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;

/**
 * @author madhusuthanan
 *
 */
public class QCConnectionFactory implements ConnectionFactory<IConnection> {
	public static final String PARAM_DELIMITER = "-";
	private static final Log log = LogFactory.getLog(QCConnectionFactory.class);
	public void closeConnection(IConnection connection) {
		try {
			connection.logout();
			connection.disconnect();
		}
		catch (Exception e){
			log.warn("Could not close QC connection. So releasing the Connection COM dispatch", e);
		}
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
	 * @throws ConnectionException
	 */
	public IConnection createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo,ConnectionManager<IConnection> connectionManager) throws ConnectionException {
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
