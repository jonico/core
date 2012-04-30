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

package com.collabnet.ccf.core.test.plugins;

import java.util.Date;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.openadaptor.core.connector.LoopingPollingReadConnector;

public class SimpleLoopingPollingReadConnector extends
		LoopingPollingReadConnector {
	//private static final Log log = LogFactory.getLog(SimpleLoopingPollingReadConnector.class);
	@Override
	/**
	 * Calculates reconnect time from Time.NOW on,
	 * so calling the ReadConnectors too fast is avoided
	 */
	protected void calculateReconnectTime() {
		reconnectTime = new Date(new Date().getTime()+ getPollIntervalMs());
	}
	
	@Override
	public Object[] next(long timeout) {
		Object [] result = super.next(timeout);
		//if (result == null || result.length == 0) {
			// log.warn("No synchronization record matches sql statement at this point ... ");
			// ShutDownCCF.exitCCF(0);
		//}
		return result;
	}
	
	/*@Override
	public void disconnect() {
		log.info("My disconnect called");
	}*/
	
}
