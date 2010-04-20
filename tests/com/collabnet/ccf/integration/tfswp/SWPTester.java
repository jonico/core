package com.collabnet.ccf.integration.tfswp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;
import com.danube.scrumworks.api.client.types.ReleaseWSO;
import com.danube.scrumworks.api.client.types.ServerException;
import com.danube.scrumworks.api.client.types.TaskWSO;
import com.danube.scrumworks.api.client.types.ThemeWSO;

/**
 * Helper methods for accessing the SWP API. 
 * 
 * @author jnicolai
 */
public class SWPTester {
	/** Property file name. */
	public static final String PROPERTY_FILE = "tfswp.properties";
	
	private static final String CCF_MAX_WAIT_TIME = "CCFMaxWaitTime";
	
	private static final String CCF_RETRY_INTERVAL = "CCFRetryInterval";

	private static final String SWP_PRODUCT = "SWPProduct";

	private static final String SWP_SERVER_URL = "SWPServerUrl";

	private static final String SWP_PASSWORD = "SWPPassword";

	private static final String SWP_USER_NAME = "SWPUserName";
	
	// SWP connection
	private com.collabnet.ccf.swp.Connection swpConnection;
	
	private String swpUserName;

	private String swpPassword;

	private String swpServerUrl;

	private String swpProduct;
	
	private int ccfMaxWaitTime;

	private int ccfRetryInterval;

	
	/**
	 * Constructor. 
	 * 
	 * @throws IOException if the property file can not be accessed
	 * @throws FileNotFoundException if the property file can not be found 
	 * @throws ServiceException 
	 */
	public SWPTester() throws FileNotFoundException, IOException, ServiceException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(PROPERTY_FILE));
		
		setSwpUserName(prop.getProperty(SWP_USER_NAME));
		setSwpPassword(prop.getProperty(SWP_PASSWORD));
		setSwpServerUrl(prop.getProperty(SWP_SERVER_URL));
		setSwpProduct(prop.getProperty(SWP_PRODUCT));
		
		ccfMaxWaitTime = Integer.parseInt(prop.getProperty(CCF_MAX_WAIT_TIME)); 
		ccfRetryInterval = Integer.parseInt(prop.getProperty(CCF_RETRY_INTERVAL)); 
		
		swpConnection = new com.collabnet.ccf.swp.Connection(getSwpServerUrl(), getSwpUserName(), getSwpPassword()); 
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
	 * Returns the backlog items after the backlog items appear in a ScrumWorks Pro product. 
	 * Assumes that there are no backlog items before beginning to wait.
	 * 
	 * @param product the product
	 * @param numberOfPBIs number of PBIs to wait for
	 * @return the backlog items in the product
	 * @throws RemoteException if the ScrumWorks API can not be accessed
	 * @throws ServerException if an error occurs in ScrumWorks
	 * @throws InterruptedException if the thread can not sleep
	 * 
	 */
	public BacklogItemWSO[] waitForBacklogItemToAppear(final ProductWSO product, int numberOfPBIs) throws ServerException, RemoteException, InterruptedException {
		BacklogItemWSO[] pbis = null;
		for (int i = 0; i < ccfMaxWaitTime; i += ccfRetryInterval) {
			pbis = getSWPEndpoint().getActiveBacklogItems(
					product);
			if (pbis == null || pbis.length < numberOfPBIs) {
				Thread.sleep(ccfRetryInterval);
			} else {
				return pbis; 
			}
		}
		throw new RemoteException("Backlog items were found within the given time: " + ccfMaxWaitTime); 
	}
	
	/**
	 * Returns the backlog items after the backlog items appear in a ScrumWorks Pro product. 
	 * Assumes that there are no backlog items before beginning to wait. 
	 * 
	 * @param numberOfPBIs number of PBIs to wait for
	 * @return the backlog items in the product
	 * @throws RemoteException if the ScrumWorks API can not be accessed
	 * @throws ServerException if an error occurs in ScrumWorks 
	 * @throws InterruptedException if the thread can not sleep
	 */
	public BacklogItemWSO[] waitForBacklogItemsToAppear(int numberOfPBIs) throws ServerException, RemoteException, InterruptedException {
		ProductWSO product = getProduct(); 
		return waitForBacklogItemToAppear(product, numberOfPBIs); 
	}

	/**
	 * Returns the product object for the given product name. 
	 * 
	 * @throws RemoteException if the ScrumWorks API can not be accessed 
	 * @throws ServerException if an error occurs in ScrumWorks 
	 */
	public ProductWSO getProduct() throws ServerException, RemoteException {
		return getSWPEndpoint().getProductByName(getSwpProduct());
	}
	
	/**
	 * Returns a list of the theme names. 
	 * 
	 * @param themes the theme web service objects
	 * @return the theme names
	 */
	public List<String> getThemeNames(final ThemeWSO[] themes) {
		final List<String> themeNames = new ArrayList<String>(); 
		for (int i = 0; i < themes.length; i++) {
			themeNames.add(themes[i].getName()); 
		}
		return themeNames; 
	}

	/**
	 * Returns the name of the release for the given backlog item in the ScrumWorks integration product. 
	 * @param pbi the backlog item
	 * 
	 * @return the name of the release if found, otherwise null
	 * @throws RemoteException if ScrumWorks can not be accessed
	 * @throws ServerException if there is an error from ScrumWorks
	 */
	public String getReleaseForBacklogItem(Long releaseId) throws ServerException, RemoteException {
		ReleaseWSO[] releases = getSWPEndpoint().getReleases(getProduct()); 
		
		for (int i = 0; i < releases.length; i++) {
			if (releases[i].getId().equals(releaseId)) {
				return releases[i].getTitle(); 
			}
		}
		return null;
	}
	
}
