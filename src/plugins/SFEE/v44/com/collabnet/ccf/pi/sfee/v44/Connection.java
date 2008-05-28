package com.collabnet.ccf.pi.sfee.v44;

import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;

public class Connection {
	private ISourceForgeSoap mSfSoap;
	private String mSessionId;
	private String userName;
	public Connection(String username, ISourceForgeSoap sfSoap,
			String sessionId) {
		this.userName = username;
		this.mSfSoap = sfSoap;
		this.mSessionId = sessionId;
	}
	public ISourceForgeSoap getSfSoap() {
		return mSfSoap;
	}
	public String getSessionId() {
		return mSessionId;
	}
	public String getUserName() {
		return userName;
	}
}
