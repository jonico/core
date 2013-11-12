/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is called by ServiceWrapper whenever the application should
 * terminate.
 * 
 * @author jnicolai
 * 
 */
public class ShutDownCCF extends Thread {
    private Long             forcedShutdownDelay = null;
    private int              exitCode;
    private static final Log log                 = LogFactory
                                                         .getLog(ShutDownCCF.class);
    private static boolean   alreadyExited       = false;

    public ShutDownCCF(int exitCode) {
        setDaemon(true);
        this.exitCode = exitCode;
    }

    public ShutDownCCF(int exitCode, Long forcedShutDownDelay) {
        setDaemon(true);
        this.exitCode = exitCode;
        this.forcedShutdownDelay = forcedShutDownDelay;
    }

    public void run() {
        if (forcedShutdownDelay == null) {
            log.info("Calling System.exit with exit code " + exitCode);
            System.exit(exitCode);
        } else {
            try {
                Thread.sleep(forcedShutdownDelay);
            } catch (InterruptedException e) {
                // if our sleep got interrupted, we do not have to forcefully terminate the VM
                log.debug("Forcefully shutting down the VM seems not to be necessary, quitting ...");
                return;
            }
            log.warn("Shutdown hooks needed more than " + forcedShutdownDelay
                    + " milliseconds, forcefully shutting down VM ...");
            Runtime.getRuntime().halt(exitCode);
        }
    }

    public static void exitCCF(int exitCode) {
        synchronized (log) {
            if (alreadyExited) {
                log.debug("CCF shutdown already in progress ...");
            } else {
                alreadyExited = true;
                // After 10 seconds, we will kill the VM even if shutdown hooks are still in process
                new ShutDownCCF(exitCode, new Long(10000)).start();
                new ShutDownCCF(exitCode).start();
            }
        }
    }

    public static void main(String[] args) {
        // set appropriate variable to flush all buffers in the reader components
        log.info("CCF has been informed to shutdown ...");
        AbstractReader.setShutDownConnector(true);
    }
}