package com.collabnet.ccf.integration.tfswp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;
import com.danube.scrumworks.api.client.types.ServerException;
import com.danube.scrumworks.api.client.types.TaskWSO;

/**
 * Helper methods for accessing the SWP API. 
 * 
 * @author jnicolai
 */
public class SWPTester {
	/** Property file name. */
	public static final String PROPERTY_FILE = "tfswp.properties";
	
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

}
