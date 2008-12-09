package com.collabnet.ccf.core.test.plugins;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.core.connector.LoopingPollingReadConnector;

public class SimpleLoopingPollingReadConnector extends
		LoopingPollingReadConnector {
	private static final int RESTART_EXIT_CODE = 42;

	private static final Log log = LogFactory.getLog(SimpleLoopingPollingReadConnector.class);
	
	/**
	 * This field contains the date when the CCF was started
	 */
	private Date startedDate=new Date();
	
	/**
	 * This property denotes after how many seconds the CCF will restart automatically
	 */
	private int autoRestartPeriod=-1;
	
	@Override
	/**
	 * Calculates reconnect time from Time.NOW on,
	 * so calling the ReadConnectors too fast is avoided
	 */
	protected void calculateReconnectTime() {
		reconnectTime = new Date(new Date().getTime()+ getPollIntervalMs());
	}
	
	@Override
	public Object[] next(long time) {
		if (getAutoRestartPeriod() > 0) {
			if (new Date().getTime() - startedDate.getTime() > getAutoRestartPeriod()) {
				log.info("Trigger auto restart with the help of service wrapper ...");
				System.exit(RESTART_EXIT_CODE);
			}
		}
		return super.next(time);
	}

	/**
	 * If you set this property, the CCF will exit (with exit code 42)
	 * after the number of seconds you have specified.
	 * If CCF is wrapped by service wrapper, it will be restarted automatically.
	 * This setting can be used to release resources from time to time.
	 * If you do not set this property or set it to a negative value, the CCF will never exit.
	 * @param autoRestartPeriod the autoRestartPeriod to set
	 */
	public void setAutoRestartPeriod(int autoRestartPeriod) {
		this.autoRestartPeriod = autoRestartPeriod * 1000;
	}

	/**
	 * Returns the number of seconds, after the CCF will exit with exit code 42 and will be restarted by the
	 * ServiceWrapper. If the return value is negative, it will never exit/restarted.
	 * @return the autoRestartPeriod
	 */
	public int getAutoRestartPeriod() {
		return autoRestartPeriod;
	}
	
}
