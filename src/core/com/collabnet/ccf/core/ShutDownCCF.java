package com.collabnet.ccf.core;

/**
 * This class is called by ServiceWrapper whenever
 * the application should terminate.
 *
 * @author jnicolai
 *
 */
public class ShutDownCCF {
	public static void main(String[] args) {
		// set appropriate variable to flush all buffers in the reader components
		if (!AbstractReader.isRestartConnector()) {
			AbstractReader.setShutDownConnector(true);
		}
	}
}
