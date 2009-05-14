/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.tracker.common.httpClient;

import javax.net.ssl.X509TrustManager;

/**
 * TrustAll class implements X509TrustManager to access all https servers with signed and unsigned certificates.
 * 
 * @author Mik Kersten
 * @author Eugene Kuleshov
 * @since 2.0
 */
public class TrustAllTrustManager implements X509TrustManager {

	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		// don't need to do any checks
	}

	public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		// don't need to do any checks
	}
}
