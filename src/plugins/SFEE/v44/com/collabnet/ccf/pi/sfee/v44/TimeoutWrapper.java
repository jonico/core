package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class TimeoutWrapper {

	private static final Log log = LogFactory.getLog(TimeoutWrapper.class);

	/**
	 * This method determines whether the exception is timeout related and will be handled in this method or whether it should be handled by another layer
	 * @param e exception that was intercepted
	 * @param numberOfTries number this method was already called for the same method call, this will help to determine the timeout
	 * @param connectionManager this object is used to retrieve timeout related configuration parameters 
	 * @return true if exception was handled here and method call should be retried, false if exception should be passed to next layer
	 */
	protected boolean handleException(RemoteException e, int numberOfTries, ConnectionManager<Connection> connectionManager) {
		Throwable cause=e.getCause();
		if (cause instanceof java.net.SocketException || cause instanceof java.net.UnknownHostException ) {
			if (numberOfTries == 1) {
				// first try, long error message
				log.error("Network related problem occurred while calling SFEE/CSFE webservice. Try operation again",e);
			}
			else if (numberOfTries < 7) {
				// error occurred again, short error message, go to sleep
				int timeOut=(int)Math.pow(2, numberOfTries);
				log.error("Network related error occurred again ("+e.getMessage()+"), sleeping for "+timeOut+" seconds.");
				try {
					Thread.sleep(timeOut*1000);
				} catch (InterruptedException e1) {
					log.error("Interrupted sleep in timeout method: ",e1);
				}
			}
			else {
				// error occurred more than 6 times, short error message, go to sleep for two minutes
				log.error("Network related error occurred again ("+e.getMessage()+") , sleeping for two minutes.");
				try {
					Thread.sleep(120000);
				} catch (InterruptedException e1) {
					log.error("Interrupted sleep in timeout method: ",e1);
				}
			}
			return true;
		}
		else
			return false;
	}
}
