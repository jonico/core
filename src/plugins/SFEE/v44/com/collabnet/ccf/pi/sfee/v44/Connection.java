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
import java.util.Date;

import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;

/**
 * This class represents the required objects needed for a SOAP API
 * connection. This is just a holder class for the SOAP API connection
 * objects.
 * The objects that are held by this class are
 * 		1) The ISourceForgeSoap SOAP app object
 * 		2) The session id created after login method is called with the 
 * 			appropriate username and password
 * 		3) The username used to login into SOAP server
 * 			- This is the Connector user's username
 * 
 * @author madhusuthanan (madhusuthanan@collab.net)
 *
 */
public class Connection {
	private ISourceForgeSoap mSfSoap;
	private String mSessionId;
	private String userName;
	private String password;
	
	@Override
	public int hashCode() {
		return mSessionId.hashCode();
	}
	
	@Override
	public boolean equals(Object comparator) {
		if (! (comparator instanceof Connection)) {
			return false;
		} else {
			Connection otherConnection = (Connection) comparator;
			return otherConnection.mSessionId.equals(mSessionId);
		}
	}
	
	/**
	 * Constructs a holder object for the 
	 * 		1) ISourceForgeSoap object
	 * 		2) The session id and
	 * 		3) the username
	 * 
	 * @param username - the connector username
	 * @param password - the connector password 
	 * @param sfSoap - The ISourceForgeSoap object that is connected to the SOAP
	 * 					server
	 * @param sessionId - The login session id
	 */
	public Connection(String username, String password, ISourceForgeSoap sfSoap,
			String sessionId) {
		this.userName = username;
		this.mSfSoap = sfSoap;
		this.mSessionId = sessionId;
		this.password=password;
	}
	
	/**
	 * Returns the ISourceForgeSoap object that is connected to the SOAP server
	 * 
	 * @return - The ISourceForgeSoap object
	 */
	public ISourceForgeSoap getSfSoap() {
		return mSfSoap;
	}
	
	/**
	 * Returns the login session id for the SOAP API calls
	 * 
	 * @return
	 */
	public String getSessionId() {
		return mSessionId;
	}
	
	/**
	 * Sets the login session id for the SOAP API calls
	 * 
	 */
	private void setSessionId(String sessionId) {
		mSessionId=sessionId;
	}
	
	/**
	 * Returns the password for the SOAP API calls
	 * 
	 * @return
	 */
	private String getPassword() {
		return password;
	}
	
	/**
	 * Returns the username that is used to login into the TFTF SOAP API server
	 * 
	 * @return - The user name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Relogins the user and changes the session id.
	 * @return New session id
	 * @throws RemoteException
	 *             when an error is encountered during login.
	 */
	public String relogin() throws RemoteException {
	    setSessionId(getSfSoap().login(getUserName(), getPassword()));
	    return getSessionId();
	}
}
