package com.collabnet.ccf.pi.sfee;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;

public abstract class SFEEConnectHelper extends LifecycleComponent {
	
	/** SourceForge Soap interface handle */
    protected ISourceForgeSoap mSfSoap;

    /** Login user name */
    private String mLoginUserName;

     /** Session id */
    private String mSessionId;

	private String serverUrl;

	private String password;

	private String username;
	
	private boolean keepAlive=true;

	private static final Log log = LogFactory.getLog(SFEEConnectHelper.class);
	/**
	 * Reads the id stripping any new line characters at the end.
	 * @param bf Input buffer.
	 * @param size Buffer size.
	 * @return Object id.
	 */
	public static String readId(byte[] bf, int size) {
		int i = 0;
		while (i < size && Character.isLetterOrDigit((char) bf[i])) {
	    	i++;
		}
		return new String(bf, 0, i);
	}
	
	public SFEEConnectHelper(String id) {
	    super(id);
	}
	private boolean firstConnect=true;

	/**
	 * Connects to SFEE server and logs into it
	 * @throws IOException 
	 */
	public void connect() throws IOException {
		// reconnect again
		log.debug("Connect was called");
		// TODO what will happen if connection breaks?
		if (firstConnect || !keepAlive) {
			mSfSoap = (ISourceForgeSoap) ClientSoapStubFactory.getSoapStub(ISourceForgeSoap.class, getServerUrl());
		
			login(getUsername(), getPassword());
			firstConnect=false;
		}
	}

	public void disconnect() {
		log.debug("Disconnect was called");
		if (keepAlive)
			return;
		try {
			logoff();
		} catch (RemoteException e) {
			// TODO Declare exception so that it can be processed by OA exception handler
			log.error("SFEE logoff failed", e);
		}
	}


	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Logins the user and save the session id.
	 * @param username User name.
	 * @param password Password
	 * @throws RemoteException when an error is encountered during login.
	 */
	public void login(String username, String password) throws RemoteException {
	try {
	    mLoginUserName = username;
	    mSessionId = mSfSoap.login(username, password);
	} catch (RemoteException e) {
	    mLoginUserName = null;
	    mSessionId = null;
	    throw e;
	}
	}

	/**
	 * Logs off the user.
	 * @throws RemoteException when an error is encountered during logoff.
	 */
	public void logoff() throws RemoteException {
	if (mSessionId != null) {
	    try {
	    	mSfSoap.logoff(mLoginUserName, mSessionId);
	    } finally {
	    	mLoginUserName = null;
	    	mSessionId = null;
	    }
	}
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private String getUsername() {
		return username;
	}

	public SFEEConnectHelper() {
		super();
	}

	/**
	 * Returns the user's session id.
	 * @return User's session id.
	 */
	public String getSessionId() {
		return mSessionId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void validate(List exceptions) {
		// check whether all necessary properties are set
		if (getServerUrl() == null) {
			log.error("serverUrl-property no set");
			exceptions.add(new ValidationException("serverUrl-property not set",this));
		}
		
		if (getUsername() == null) {
			log.error("username-property no set");
			exceptions.add(new ValidationException("username-property not set",this));
		}
		if (getPassword()==null) {
			log.error("password-property no set");
			exceptions.add(new ValidationException("password-property not set",this));
		}
	}

	public void setKeepAlive(String keepAlive) {
		if (keepAlive.equals("false"))
				this.keepAlive=false;
	}

	public String isKeepAlive() {
		return keepAlive?"true":"false";
	}
	
	@Override
	public void stop() {
		firstConnect=true;
		try {
			logoff();
		} catch (RemoteException e) {
			// TODO Declare exception so that it can be processed by OA exception handler
			log.error("SFEE logoff failed", e);
		}
		super.stop();
	}
}