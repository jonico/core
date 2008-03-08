package com.collabnet.ccf.core.ga;



/**
 * 
 * @author jnicolai
 * 
 * This exception will be thrown if either the Java object or the XML
 * representation of the generic artifact does not comply to the specification
 */
public class GenericArtifactParsingException extends Exception {
	/**
	 * version for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Set details of exception
	 * @param message contains exception details
	 */
	public GenericArtifactParsingException(String message) {
		super(message);
	}
}
