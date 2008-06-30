package com.collabnet.ccf.core.eis.connection;

import com.collabnet.ccf.core.CCFRuntimeException;

public class ConnectionException extends CCFRuntimeException {
	private static final long serialVersionUID = 5388373597075150891L;
	public ConnectionException(String cause){
		super(cause);
	}
	public ConnectionException(String cause, Throwable exception){
		super(cause, exception);
	}
}
