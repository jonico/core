package com.collabnet.ccf.swp;

import javax.xml.rpc.ServiceException;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.ScrumWorksEndpointBindingStub;
import com.danube.scrumworks.api.client.ScrumWorksServiceLocator;

/**
 * This class will be used to cover SWP specific connection information
 * 
 * @author jnicolai
 * 
 */
public class Connection {

	private String username;
	private String password;
	private ScrumWorksEndpoint	endpoint;
	
	/**
	 * Returns the SWP endpoint object
	 * @return SWP endpoint object
	 */
	public ScrumWorksEndpoint getEndpoint() {
		return endpoint;
	}
	
	/**
	 * Delimiter used to split the credentials
	 * The connector user password may not contain this delimiter
	 */
	public static final String PARAM_DELIMITER = ":";

	/**
	 * Creates a new SWP connection
	 * @param repositoryId
	 * @param repositoryKind
	 * @param connectionInfo
	 * @param credentialInfo
	 * @param connectionManager
	 * @throws ServiceException
	 */
	public Connection(String repositoryId, String repositoryKind,
			String connectionInfo, String credentialInfo,
			ConnectionManager<Connection> connectionManager) throws ServiceException {
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
		ScrumWorksServiceLocator locator = new ScrumWorksServiceLocator();
		locator
				.setScrumWorksEndpointPortEndpointAddress(connectionInfo);
		endpoint = locator.getScrumWorksEndpointPort();
		((ScrumWorksEndpointBindingStub) endpoint).setUsername(username);
		((ScrumWorksEndpointBindingStub) endpoint).setPassword(password);
	}
	
	/**
	 * Alternative constructor for test classes 
	 * @param serverUrl
	 * @param userName
	 * @param password
	 * @throws ServiceException 
	 */
	public Connection (String serverUrl, String userName, String password) throws ServiceException {
		this.username = userName;
		this.password = password;
		ScrumWorksServiceLocator locator = new ScrumWorksServiceLocator();
		locator
				.setScrumWorksEndpointPortEndpointAddress(serverUrl);
		endpoint = locator.getScrumWorksEndpointPort();
		((ScrumWorksEndpointBindingStub) endpoint).setUsername(username);
		((ScrumWorksEndpointBindingStub) endpoint).setPassword(password);
	}

}
