package com.collabnet.ccf.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is called by ServiceWrapper whenever
 * the application should terminate.
 *
 * @author jnicolai
 *
 */
public class ShutDownCCF extends Thread {
	
	private int exitCode;
	private static final Log log = LogFactory.getLog(ShutDownCCF.class);
	private static boolean alreadyExited=false;
	public static void main(String[] args) {
		// set appropriate variable to flush all buffers in the reader components
		log.info("CCF has been informed to shutdown ...");
		AbstractReader.setShutDownConnector(true);
	}
	
	public static void exitCCF(int exitCode) {
		synchronized(log) {
			if (alreadyExited) {
				log.debug("CCF shutdown already in progress ...");
			}
			else {
				alreadyExited=true;
				new ShutDownCCF(exitCode).start();
			}
		}
	}
	
	public ShutDownCCF(int exitCode) {
		setDaemon(true);
		this.exitCode=exitCode;
	}	
		
	public void run() {
			log.info("Calling System.exit with exit code "+exitCode);
			System.exit(exitCode);
	}
}
