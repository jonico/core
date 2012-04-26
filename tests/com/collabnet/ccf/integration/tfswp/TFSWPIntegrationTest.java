/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import com.danube.scrumworks.api2.client.ScrumWorksException;

/**
 * This is the base class for all TeamForge/ScrumWorks Pro
 * Integration tests. It will provide the basic functionality to login
 * into TF/SWP, create and retrieve artifacts.
 * It can be configured using a file called tfswp.properties
 * @author jnicolai
 *
 */
public class TFSWPIntegrationTest {
	public static final String PROPERTY_FILE = "tfswp.properties";
	
	private static final String CCF_MAX_WAIT_TIME = "CCFMaxWaitTime";
	
	private static final String CCF_RETRY_INTERVAL = "CCFRetryInterval";

	private TeamForgeTester teamForgeTester; 
	
	private int ccfMaxWaitTime;

	private int ccfRetryInterval;

	private SWPTester swpTester; 

	/**
	 * Logs into TF and SWP
	 * The needed properties are taken from tfswp.properties
	 * Creates the TeamForge and ScrumWorks tester. 
	 * Clears backlog items and tasks within TeamForge and ScrumWorks.  
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Properties prop = new Properties();
		prop.load(new FileInputStream(PROPERTY_FILE));
		
		setCcfMaxWaitTime(Integer.parseInt(prop.getProperty(CCF_MAX_WAIT_TIME)));
		setCcfRetryInterval(Integer.parseInt(prop.getProperty(CCF_RETRY_INTERVAL)));
		
		swpTester = new SWPTester(); 
		
		teamForgeTester = new TeamForgeTester(); 
		
		teamForgeTester.deleteAllTasksInTF();
		swpTester.deleteAllTasksInSWP();
		teamForgeTester.deleteAllPBIsInTF();
		swpTester.deleteAllPBIsInSWP();
	}

	/**
	 * Delete all artifacts within the TeamForge and ScrumWorks project/product, and cleans up the session.
	 *  
	 * @throws ScrumWorksException if there is an error from ScrumWorks 
	 * @throws RemoteException 
	 */
	@After
	public void tearDown() throws ScrumWorksException, RemoteException  {
		teamForgeTester.deleteAllTasksInTF();
		swpTester.deleteAllTasksInSWP();
		teamForgeTester.deleteAllPBIsInTF();
		swpTester.deleteAllPBIsInSWP();
		
		teamForgeTester.logOff(); 
	}
	
	public void setCcfMaxWaitTime(int ccfMaxWaitTime) {
		this.ccfMaxWaitTime = ccfMaxWaitTime;
	}

	public int getCcfMaxWaitTime() {
		return ccfMaxWaitTime;
	}

	public void setCcfRetryInterval(int ccfRetryInterval) {
		this.ccfRetryInterval = ccfRetryInterval;
	}

	public int getCcfRetryInterval() {
		return ccfRetryInterval;
	}

	public TeamForgeTester getTeamForgeTester() {
		return teamForgeTester;
	}
	
	public SWPTester getSWPTester() {
		return swpTester;
	}
	
}
