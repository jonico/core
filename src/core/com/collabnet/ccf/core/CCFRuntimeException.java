package com.collabnet.ccf.core;

/**
 * This exception is intended for the CCF components to throw
 * when there is an application exception and it should be
 * propogated to the hospital.
 * 
 * @author Madhusuthanan Seetharam (madhusuthanan@collab.net)
 *
 */
public class CCFRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 3190879430557600332L;
	/**
	 * Constructs a CCFRuntimeException with the cause
	 * provided.
	 * 
	 * @param cause - A description of the exceptional condition
	 */
	public CCFRuntimeException(String cause){
		super(cause);
	}

	/**
	 * Constructs a CCFRuntimeException with the cause providedand
	 * root cause exception.
	 * 
	 * @param cause - A description of the exceptional condition
	 * @param exception - The root cause exception that should be 
	 * 						sent to the hospital
	 */
	public CCFRuntimeException(String cause, Throwable exception){
		super(cause, exception);
	}
}
