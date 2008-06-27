package com.collabnet.ccf.core.eis.connection;

import com.collabnet.ccf.core.CCFRuntimeException;

public class ConnectionException extends CCFRuntimeException {
	public ConnectionException(String cause){
		super(cause);
	}
	public ConnectionException(String cause, Throwable exception){
		super(cause, exception);
	}
}
