package com.collabnet.ccf.core;

public class CCFRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 3190879430557600332L;
	public CCFRuntimeException(String cause){
		super(cause);
	}
	public CCFRuntimeException(String cause, Throwable exception){
		super(cause, exception);
	}
}
