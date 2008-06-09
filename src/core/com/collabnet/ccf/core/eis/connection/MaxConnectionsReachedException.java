package com.collabnet.ccf.core.eis.connection;

/**
 * This exception class represents the maximum allowed connection
 * configuration for a Connection Pool is reached.
 * 
 * @author madhusuthanan (madhusuthanan@collab.net)
 *
 */
public class MaxConnectionsReachedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MaxConnectionsReachedException(String arg0) {
		super(arg0);
	}

}
