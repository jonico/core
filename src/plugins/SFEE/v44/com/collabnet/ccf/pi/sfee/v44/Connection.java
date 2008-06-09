package com.collabnet.ccf.pi.sfee.v44;

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
	
	/**
	 * Constructs a holder object for the 
	 * 		1) ISourceForgeSoap object
	 * 		2) The session id and
	 * 		3) the username
	 * 
	 * @param username - the connector username
	 * @param sfSoap - The ISourceForgeSoap object that is connected to the SOAP
	 * 					server
	 * @param sessionId - The login session id
	 */
	public Connection(String username, ISourceForgeSoap sfSoap,
			String sessionId) {
		this.userName = username;
		this.mSfSoap = sfSoap;
		this.mSessionId = sessionId;
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
	 * Returns the username that is used to login into the SFEE SOAP APi server
	 * 
	 * @return - The user name
	 */
	public String getUserName() {
		return userName;
	}
}
