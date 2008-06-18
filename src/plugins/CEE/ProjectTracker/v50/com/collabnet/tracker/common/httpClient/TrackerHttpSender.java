package com.collabnet.tracker.common.httpClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.CommonsHTTPSender;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import com.collabnet.tracker.core.TrackerClientManager;
import com.collabnet.tracker.core.TrackerWebServicesClient;

/**
 * HTTPSender used by axis so that it uses the more full functioned
 * HTTPClient instead of the basic java network functions.
 * 
 * @author Shawn Minto
 * 
 */
public class TrackerHttpSender extends CommonsHTTPSender {

	private static final long serialVersionUID = 1L;

	public static final String CONTENT_ENCODING_GZIP = "gzip";

	public static final int CONNNECT_TIMEOUT = 60000;

	public static final int SOCKET_TIMEOUT = 60000;

	private static final int HTTP_PORT = 80;

	private static final int HTTPS_PORT = 443;

	@Override
	protected HostConfiguration getHostConfiguration(HttpClient client, MessageContext context, URL url) {

		Proxy proxy = null;
		String httpUser = null;
		String httpPassword = null;
		String serverUrl = url.getProtocol() + "://" + url.getHost();
		TrackerWebServicesClient webServicesClient = TrackerClientManager.getInstance().getClient(serverUrl);

		proxy = webServicesClient.getProxy();
		httpUser = webServicesClient.getHttpUser();
		httpPassword = webServicesClient.getHttpPassword();

		setupHttpClient(client, proxy, url.toString(), httpUser, httpPassword);
		return client.getHostConfiguration();
	}

	public static void setupHttpClient(HttpClient client, Proxy proxySettings, String repositoryUrl, String user,
			String password) {

		setupHttpClientParams(client, null);

		if (proxySettings != null && !Proxy.NO_PROXY.equals(proxySettings)
		/* && !WebClientUtil.repositoryUsesHttps(repositoryUrl) */
		&& proxySettings.address() instanceof InetSocketAddress) {
			InetSocketAddress address = (InetSocketAddress) proxySettings.address();
			client.getHostConfiguration().setProxy(getDomain(address.getHostName()), address.getPort());
			if (proxySettings instanceof AuthenticatedProxy) {
				AuthenticatedProxy authProxy = (AuthenticatedProxy) proxySettings;
				Credentials credentials = getCredentials(authProxy.getUserName(), authProxy.getPassword(),
						address.getAddress());
				AuthScope proxyAuthScope = new AuthScope(address.getHostName(), address.getPort(), AuthScope.ANY_REALM);
				client.getState().setProxyCredentials(proxyAuthScope, credentials);
			}
		}

		if (user != null && password != null) {
			AuthScope authScope = new AuthScope(getDomain(repositoryUrl),
					getPort(repositoryUrl), AuthScope.ANY_REALM);
			try {
				client.getState().setCredentials(authScope, getCredentials(user, password, InetAddress.getLocalHost()));
			} catch (UnknownHostException e) {
				client.getState().setCredentials(authScope, getCredentials(user, password, null));
			}
		}

		if (isRepositoryHttps(repositoryUrl)) {
			Protocol acceptAllSsl = new Protocol("https",
					(ProtocolSocketFactory) SslProtocolSocketFactory.getInstance(),
					getPort(repositoryUrl));
			client.getHostConfiguration().setHost(getDomain(repositoryUrl),
					getPort(repositoryUrl), acceptAllSsl);
			Protocol.registerProtocol("https", acceptAllSsl);
		} else {
			client.getHostConfiguration().setHost(getDomain(repositoryUrl),
					getPort(repositoryUrl));
		}
	}
	
	private static void setupHttpClientParams(HttpClient client, String userAgent) {
		client.getParams().setBooleanParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
		client.getHttpConnectionManager().getParams().setSoTimeout(SOCKET_TIMEOUT);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNNECT_TIMEOUT);
	}

	// API-3.0 rename to getHost
	public static String getDomain(String repositoryUrl) {
		String result = repositoryUrl;
		int colonSlashSlash = repositoryUrl.indexOf("://");

		if (colonSlashSlash >= 0) {
			result = repositoryUrl.substring(colonSlashSlash + 3);
		}

		int colonPort = result.indexOf(':');
		int requestPath = result.indexOf('/');

		int substringEnd;

		// minimum positive, or string length
		if (colonPort > 0 && requestPath > 0) {
			substringEnd = Math.min(colonPort, requestPath);
		} else if (colonPort > 0) {
			substringEnd = colonPort;
		} else if (requestPath > 0) {
			substringEnd = requestPath;
		} else {
			substringEnd = result.length();
		}

		return result.substring(0, substringEnd);
	}
	

	public static Credentials getCredentials(AuthenticatedProxy authProxy, InetSocketAddress address) {
		return getCredentials(authProxy.getUserName(), authProxy.getPassword(), address.getAddress());
	}

	private static Credentials getCredentials(final String username, final String password, final InetAddress address) {
		int i = username.indexOf("\\");
		if (i > 0 && i < username.length() - 1 && address != null) {
			return new NTCredentials(username.substring(i + 1), password, address.getHostName(), username.substring(0,
					i));
		} else {
			return new UsernamePasswordCredentials(username, password);
		}
	}

	static boolean isRepositoryHttps(String repositoryUrl) {
		return repositoryUrl.matches("https.*");
	}

	public static int getPort(String repositoryUrl) {
		int colonSlashSlash = repositoryUrl.indexOf("://");
		int firstSlash = repositoryUrl.indexOf("/", colonSlashSlash + 3);
		int colonPort = repositoryUrl.indexOf(':', colonSlashSlash + 1);
		if (firstSlash == -1) {
			firstSlash = repositoryUrl.length();
		}
		if (colonPort < 0 || colonPort > firstSlash) {
			return isRepositoryHttps(repositoryUrl) ? HTTPS_PORT : HTTP_PORT;
		}

		int requestPath = repositoryUrl.indexOf('/', colonPort + 1);
		int end = requestPath < 0 ? repositoryUrl.length() : requestPath;
		String port = repositoryUrl.substring(colonPort + 1, end);
		if (port.length() == 0) {
			return isRepositoryHttps(repositoryUrl) ? HTTPS_PORT : HTTP_PORT;
		}

		return Integer.parseInt(port);
	}

}
