package com.collabnet.ccf.pi.cee.pt.v50;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.tracker.core.TrackerClientManager;
import com.collabnet.tracker.core.TrackerWebServicesClient;

public class CollabNetConnectionFactory implements ConnectionFactory<TrackerWebServicesClient> {
	private static final Log log = LogFactory.getLog(CollabNetConnectionFactory.class);
	private String proxyHost = null;
	private String proxyType = null;
	private int proxyPort = -1;
	private boolean proxyUsed = false;
	public static final String PARAM_DELIMITER = ":";
	public void closeConnection(TrackerWebServicesClient connection) throws ConnectionException {
		//Nothing to do
	}

	public TrackerWebServicesClient createConnection(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo,ConnectionManager<TrackerWebServicesClient> connectionManager) throws ConnectionException {
		if(StringUtils.isEmpty(repositoryId)){
			throw new IllegalArgumentException("Repository Id cannot be null");
		}
		
		String username = null;
		String password = null;
		if(credentialInfo != null){
			String[] splitCredentials = credentialInfo.split(PARAM_DELIMITER);
			if(splitCredentials != null){
				if(splitCredentials.length == 1){
					username = splitCredentials[0];
					password = "";
				}
				else if(splitCredentials.length == 2){
					username = splitCredentials[0];
					password = splitCredentials[1];
				}
				else {
					String message = "Credentials info is not valid "+credentialInfo;
					log.error(message);
					throw new IllegalArgumentException(message);
				}
			}
		}
		String projectName = null;
		if(repositoryId != null){
			String[] splitProjectName = repositoryId.split(":");
			if(splitProjectName != null){
				if(splitProjectName.length >= 1){
					projectName = splitProjectName[0];
				}
				else {
					throw new IllegalArgumentException("Repository id is not valid."
							+" Could not extract project name from repository id");
				}
			}
		}
		String url = connectionInfo.substring(0, connectionInfo.indexOf("://")+3)+
					projectName + "." +
					connectionInfo.substring(connectionInfo.indexOf("://")+3);
		TrackerWebServicesClient twsclient = null;
		try {
			Proxy proxy = null;
			if(proxyUsed){
				if(StringUtils.isEmpty(this.proxyHost)){
					throw new IllegalArgumentException("Proxy host is not valid."
							+ this.proxyHost);
				}
				else if(this.proxyPort == -1){
					throw new IllegalArgumentException("Proxy port is not valid "
							+ this.proxyPort);
				}
				Proxy.Type type = null;
				try {
					type = Proxy.Type.valueOf(this.proxyType);
				}
				catch(Exception e){
					throw new IllegalArgumentException("Proxy type is not valid "
							+ this.proxyType +". Proxy type should either be HTTP or SOCKS");
				}
				InetSocketAddress socketAddress = new InetSocketAddress(this.proxyHost, this.proxyPort);
				
				proxy = new Proxy(type, socketAddress);
			}
			else {
				proxy = Proxy.NO_PROXY;
			}
			twsclient = TrackerClientManager.getInstance().createClient(url,
					username, password,null,null, proxy);
		} catch (MalformedURLException e) {
			String message = "Exception when trying to get the Web Services client";
			log.error(message, e);
			throw new ConnectionException(message, e);
		}
		return twsclient;
	}

	public boolean isAlive(TrackerWebServicesClient connection) {
		return true;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isProxyUsed() {
		return proxyUsed;
	}

	public void setProxyUsed(boolean proxyUsed) {
		this.proxyUsed = proxyUsed;
	}

}
