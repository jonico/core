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

package com.collabnet.ccf.core.eis.connection;

/**
 * Specifies the interface for the plugin provider
 * to define how to obtain a connection to the 
 * system that he is connecting to.
 * 
 * @author madhusuthanan (madhusuthanan@collab.net)
 *
 * @param <T> - The Class of the connection object
 * 			to the system that is connected. 
 */
public interface ConnectionFactory<T> {
	/**
	 * Create a connection to the system given a system and
	 * repository information and the connection info and
	 * credential info provided.
	 * 
	 * @param systemId
	 * @param systemKind
	 * @param repositoryId
	 * @param repositoryKind
	 * @param connectionInfo
	 * @param credentialInfo
	 * @return
	 */
	public T createConnection(String systemId,
			String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo,
			String credentialInfo,ConnectionManager<T> connectionManager) throws ConnectionException;
	
	/**
	 * Closes the connection object.
	 * @param connection - the connection object to be closed.
	 */
	public void closeConnection(T connection) throws ConnectionException;
	
	/**
	 * Indicates if the connection is live or not. A connection
	 * is live if it can be used without resulting to any access denied
	 * status returned from the server.
	 * 
	 * @param connection - The connection whose status to be checked
	 * 
	 * @return - true if the connection is live
	 * 			false if the connection is stale.
	 */
	public boolean isAlive(T connection);
}
