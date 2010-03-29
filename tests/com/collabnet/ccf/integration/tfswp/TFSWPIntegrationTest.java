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

	private static final String SWP_PRODUCT = "SWPProduct";

	private static final String SWP_SERVER_URL = "SWPServerUrl";

	private static final String SWP_PASSWORD = "SWPPassword";

	private static final String SWP_USER_NAME = "SWPUserName";

	private static final String TF_TASK_TRACKER = "TFTaskTracker";

	private static final String TFPBI_TRACKER = "TFPBITracker";

	private static final String TF_PROJECT = "TFProject";

	private static final String TF_SERVER_URL = "TFServerUrl";

	private static final String TF_PASSWORD = "TFPassword";

	private static final String TF_USER_NAME = "TFUserName";

	// SWP connection
	private com.collabnet.ccf.swp.Connection swpConnection;
	
	// TF connection
	private Connection tfConnection;
	
	public static final String PROPERTY_FILE = "tfswp.properties";

	private String tfUserName;

	private String tfPassword;

	private String tfServerUrl;

	private String tfProject;

	private String tfPBITracker;

	private String tfTaskTracker;

	private String swpUserName;

	private String swpPassword;

	private String swpServerUrl;

	private String swpProduct; 

	/**
	 * Logs into TF and SWP
	 * The needed properties are taken from tfswp.properties
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Properties prop = new Properties();
		prop.load(new FileInputStream(PROPERTY_FILE));
		setTfUserName(prop.getProperty(TF_USER_NAME));
		setTfPassword(prop.getProperty(TF_PASSWORD));
		setTfServerUrl(prop.getProperty(TF_SERVER_URL));
		setTfProject(prop.getProperty(TF_PROJECT));
		setTfPBITracker(prop.getProperty(TFPBI_TRACKER));
		setTfTaskTracker(prop.getProperty(TF_TASK_TRACKER));
		
		setSwpUserName(prop.getProperty(SWP_USER_NAME));
		setSwpPassword(prop.getProperty(SWP_PASSWORD));
		setSwpServerUrl(prop.getProperty(SWP_SERVER_URL));
		setSwpProduct(prop.getProperty(SWP_PRODUCT));
		
		// we pass the current system millis to work around a caching problem
		tfConnection = Connection.getConnection(getTfServerUrl(), getTfUserName(), getTfPassword(), null, Long.toString(System.currentTimeMillis()), null, false);
		swpConnection = new com.collabnet.ccf.swp.Connection(getSwpServerUrl(), getSwpUserName(), getSwpPassword()); 
	}

	/**
	 * Do the session clean up
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (tfConnection.supports50()) {
			tfConnection.getTeamForgeClient().logoff50(tfConnection.getUserId());
		} else {
			tfConnection.getTeamForgeClient().logoff44(tfConnection.getUserId());
		}
	}
	
	/**
	 * Returns SWP endpoint used for tests
	 * @return
	 */
	public ScrumWorksEndpoint getSWPEndpoint() {
		return swpConnection.getEndpoint();
	}
	
	/**
	 * Returns TF connection object
	 * @return
	 */
	public Connection getTFConnection() {
		return tfConnection;
	}

	public void setTfUserName(String tfUserName) {
		this.tfUserName = tfUserName;
	}

	public String getTfUserName() {
		return tfUserName;
	}

	public void setTfPassword(String tfPassword) {
		this.tfPassword = tfPassword;
	}

	public String getTfPassword() {
		return tfPassword;
	}

	public void setTfServerUrl(String tfServerUrl) {
		this.tfServerUrl = tfServerUrl;
	}

	public String getTfServerUrl() {
		return tfServerUrl;
	}

	public void setTfProject(String tfProject) {
		this.tfProject = tfProject;
	}

	public String getTfProject() {
		return tfProject;
	}

	public void setTfPBITracker(String tfPBITracker) {
		this.tfPBITracker = tfPBITracker;
	}

	public String getTfPBITracker() {
		return tfPBITracker;
	}

	public void setTfTaskTracker(String tfTaskTracker) {
		this.tfTaskTracker = tfTaskTracker;
	}

	public String getTfTaskTracker() {
		return tfTaskTracker;
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
	 * Delete all PBIs in the TF project
	 * @throws RemoteException 
	 */
	public void deleteAllPBIsInTF() throws RemoteException {
		ArtifactRow[] tfRows = tfConnection.getTrackerClient().getArtifactList(getTfPBITracker(), null).getDataRows();
		for (ArtifactRow artifactRow : tfRows) {
			tfConnection.getTrackerClient().deleteArtifact(artifactRow.getId());
		}
	}
	
	/**
	 * Delete all PBIs in the TF project
	 * @throws RemoteException 
	 */
	public void deleteAllTasksInTF() throws RemoteException {
		ArtifactRow[] tfRows = tfConnection.getTrackerClient().getArtifactList(getTfTaskTracker(), null).getDataRows();
		for (ArtifactRow artifactRow : tfRows) {
			tfConnection.getTrackerClient().deleteArtifact(artifactRow.getId());
		}
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
	
	/**
	 * Delete all artifacts within the TF and PBI project/product
	 * @throws RemoteException 
	 */
	public void cleanUpArtifacts() throws RemoteException {
		deleteAllTasksInTF();
		deleteAllTasksInSWP();
		deleteAllPBIsInTF();
		deleteAllPBIsInSWP();
	}
	
}
