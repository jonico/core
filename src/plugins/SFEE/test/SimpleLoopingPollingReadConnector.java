package test;

import java.util.Date;

import org.openadaptor.core.connector.LoopingPollingReadConnector;

public class SimpleLoopingPollingReadConnector extends
		LoopingPollingReadConnector {
	
	@Override
	/**
	 * Calculates reconnect time from Time.NOW on,
	 * so calling the ReadConnectors too fast is avoided
	 */
	protected void calculateReconnectTime() {
		reconnectTime = new Date(new Date().getTime()+ getPollIntervalMs());
	}
}
