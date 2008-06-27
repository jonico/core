package com.collabnet.ccf.core;

public class CCFRuntimeException extends RuntimeException {
	public CCFRuntimeException(String cause){
		super(cause);
	}
	public CCFRuntimeException(String cause, Throwable exception){
		super(cause, exception);
	}
}
