/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

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
