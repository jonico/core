/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;
import static org.junit.Assert.*;


import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;
import com.danube.scrumworks.api.client.types.ServerException;
import com.danube.scrumworks.api.client.types.TaskWSO;

/**
 * This is the base class for all TeamForge/ScrumWorks Pro
 * Integration tests. It will provide the basic functionality to login
 * into TF/SWP, create and retrieve artifacts.
 * It can be configured using a file called tfswp.properties
 * @author jnicolai
 *
 */
public class TFSWPIntegrationTest {
	
	/** Property file name. */
	public static final String PROPERTY_FILE = "tfswp.properties";
	
	private static final String SWP_PRODUCT = "SWPProduct";

	private static final String SWP_SERVER_URL = "SWPServerUrl";

	private static final String SWP_PASSWORD = "SWPPassword";

	private static final String SWP_USER_NAME = "SWPUserName";

	private static final String CCF_MAX_WAIT_TIME = "CCFMaxWaitTime";
	
	private static final String CCF_RETRY_INTERVAL = "CCFRetryInterval";

	private TeamForgeTester teamForgeTester; 
	
	// SWP connection
	private com.collabnet.ccf.swp.Connection swpConnection;
	
	private String swpUserName;

	private String swpPassword;

	private String swpServerUrl;

	private String swpProduct;

	private int ccfMaxWaitTime;

	private int ccfRetryInterval; 

	/**
	 * Logs into TF and SWP
	 * The needed properties are taken from tfswp.properties
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Properties prop = new Properties();
		prop.load(new FileInputStream(PROPERTY_FILE));
		
		setSwpUserName(prop.getProperty(SWP_USER_NAME));
		setSwpPassword(prop.getProperty(SWP_PASSWORD));
		setSwpServerUrl(prop.getProperty(SWP_SERVER_URL));
		setSwpProduct(prop.getProperty(SWP_PRODUCT));
		
		setCcfMaxWaitTime(Integer.parseInt(prop.getProperty(CCF_MAX_WAIT_TIME)));
		setCcfRetryInterval(Integer.parseInt(prop.getProperty(CCF_RETRY_INTERVAL)));
		
		swpConnection = new com.collabnet.ccf.swp.Connection(getSwpServerUrl(), getSwpUserName(), getSwpPassword()); 
		
		teamForgeTester = new TeamForgeTester(); 
	}

	/**
	 * Delete all artifacts within the TeamForge and ScrumWorks project/product, and cleans up the session.
	 *  
	 * @throws RemoteException if the TeamForge or ScrumWorks API can not be accessed 
	 */
	@After
	public void tearDown() throws RemoteException  {
		teamForgeTester.deleteAllTasksInTF();
		deleteAllTasksInSWP();
		teamForgeTester.deleteAllPBIsInTF();
		deleteAllPBIsInSWP();
		
		teamForgeTester.logOff(); 
	}
	
	/**
	 * Returns SWP endpoint used for tests
	 * @return
	 */
	public ScrumWorksEndpoint getSWPEndpoint() {
		return swpConnection.getEndpoint();
	}
	
	public void setSwpUserName(String swpUserName) {
		this.swpUserName = swpUserName;
	}

	public String getSwpUserName() {
		return swpUserName;
	}

	public void setSwpPassword(String swpPassword) {
		this.swpPassword = swpPassword;
	}

	public String getSwpPassword() {
		return swpPassword;
	}

	public void setSwpServerUrl(String swpServerUrl) {
		this.swpServerUrl = swpServerUrl;
	}

	public String getSwpServerUrl() {
		return swpServerUrl;
	}

	public void setSwpProduct(String swpProduct) {
		this.swpProduct = swpProduct;
	}

	public String getSwpProduct() {
		return swpProduct;
	}
	
	@Test
	public void testConnectivity() throws RemoteException {
		assertEquals("Brilliant!", getSWPEndpoint().getTest());
	}
	
	/**
	 * Delete all PBIs within the SWP product
	 * @throws ServerException
	 * @throws RemoteException
	 */
	public void deleteAllPBIsInSWP() throws ServerException, RemoteException {
		ProductWSO product = getSWPEndpoint().getProductByName(getSwpProduct());
		BacklogItemWSO[] pbis = getSWPEndpoint().getActiveBacklogItems(product);
		if (pbis != null) {
			for (BacklogItemWSO backlogItemWSO : pbis) {
				getSWPEndpoint().deleteBacklogItem(backlogItemWSO);
			}
		}
	}
	
	/**
	 * Delete all tasks within the SWP product
	 * @throws RemoteException 
	 * @throws ServerException 
	 */
	public void deleteAllTasksInSWP() throws ServerException, RemoteException {
		ProductWSO product = getSWPEndpoint().getProductByName(getSwpProduct());
		TaskWSO[] tasks = getSWPEndpoint().getTasksForProduct(product);
		if (tasks != null) {
			for (TaskWSO task : tasks) {
				getSWPEndpoint().deleteTask(task);
			}
		}
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
	
}
