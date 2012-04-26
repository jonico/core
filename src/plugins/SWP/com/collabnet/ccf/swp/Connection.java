package com.collabnet.ccf.swp;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.danube.scrumworks.api2.client.ScrumWorksAPIService;

/**
 * This class will be used to cover SWP specific connection information
 * 
 * @author jnicolai
 * 
 */
public class Connection {

	private String username;
	private String password;
	private ScrumWorksAPIService endpoint;

	/**
	 * Returns the SWP endpoint object
	 * 
	 * @return SWP endpoint object
	 */
	public ScrumWorksAPIService getEndpoint() {
		return endpoint;
	}

	/**
	 * Delimiter used to split the credentials The connector user password may
	 * not contain this delimiter
	 */
	public static final String PARAM_DELIMITER = ":";

	/**
	 * Creates a new SWP connection
	 * 
	 * @param repositoryId
	 * @param repositoryKind
	 * @param connectionInfo
	 * @param credentialInfo
	 * @param connectionManager
	 * @throws ServiceException
	 * @throws MalformedURLException
	 */
	public Connection(String repositoryId, String repositoryKind,
			String connectionInfo, String credentialInfo,
			ConnectionManager<Connection> connectionManager)
			throws ServiceException, MalformedURLException {
		if (credentialInfo != null) {
			String[] splitCredentials = credentialInfo.split(PARAM_DELIMITER);
			if (splitCredentials != null) {
				if (splitCredentials.length == 1) {
					username = splitCredentials[0];
					password = "";
				} else if (splitCredentials.length == 2) {
					username = splitCredentials[0];
					password = splitCredentials[1];
				} else {
					throw new IllegalArgumentException(
							"Credentials info is not valid.");
				}
			}
		}
		
		Service service = Service.create(new URL(connectionInfo), new QName(
				"http://api2.scrumworks.danube.com/",
				"ScrumWorksAPIBeanService"));
		
		endpoint = service.getPort(ScrumWorksAPIService.class);
		((BindingProvider) endpoint).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
		((BindingProvider) endpoint).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
	}

	/**
	 * Alternative constructor for test classes
	 * 
	 * @param serverUrl
	 * @param userName
	 * @param password
	 * @throws ServiceException
	 * @throws MalformedURLException 
	 */
	public Connection(String serverUrl, String userName, String password)
			throws ServiceException, MalformedURLException {
		this.username = userName;
		this.password = password;
		
		Service service = Service.create(new URL(serverUrl), new QName(
				"http://api2.scrumworks.danube.com/",
				"ScrumWorksAPIBeanService"));
		
		endpoint = service.getPort(ScrumWorksAPIService.class);
		((BindingProvider) endpoint).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
		((BindingProvider) endpoint).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
		
	}

}
