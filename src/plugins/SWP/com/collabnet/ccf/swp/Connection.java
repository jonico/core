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
	
	ScrumWorksEndpoint getEndpoint() {
		return endpoint;
	}
	
	public static final String PARAM_DELIMITER = ":";

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

}
