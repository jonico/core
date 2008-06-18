package com.collabnet.tracker.core;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;


/**
 * Class to manage the PT clients and their repository data.  This data is stored
 * between executions of eclipse so that it does not need ot be downloaded every time.
 * 
 * @author Shawn Minto
 * 
 */
public class TrackerClientManager {

	private Map<String, TrackerWebServicesClient> clientByUrl = new HashMap<String, TrackerWebServicesClient>();

	private static TrackerClientManager manager;

	private TrackerClientManager() {
	}

	public synchronized static TrackerClientManager getInstance() {
		if (manager == null)
			manager = new TrackerClientManager();
		return manager;
	}

	public synchronized TrackerWebServicesClient getClient(String serverUrl) {
		return clientByUrl.get(serverUrl);
	}


	public TrackerWebServicesClient createClient(String serverUrl, String newUserId, String newPassword,
			String httpAuthUser, String httpAuthPass, Proxy proxy) throws MalformedURLException {
		TrackerWebServicesClient client = new TrackerWebServicesClient(serverUrl, newUserId, newPassword, proxy,
				httpAuthUser, httpAuthPass);
		clientByUrl.put(serverUrl, client);
		return client;
	}

}
