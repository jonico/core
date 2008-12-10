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
}
