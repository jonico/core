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

package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import javax.activation.DataHandler;

import org.apache.axis.AxisFault;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.IFileStorageAppSoap;

public class FileStorageSOAPTimeoutWrapper extends TimeoutWrapper implements
		IFileStorageAppSoap {

	private IFileStorageAppSoap fileStorageSoapApp;
	private ConnectionManager<Connection> connectionManager;

	public FileStorageSOAPTimeoutWrapper(String serverUrl,
			ConnectionManager<Connection> connectionManager) {
		fileStorageSoapApp = (IFileStorageAppSoap) ClientSoapStubFactory
				.getSoapStub(IFileStorageAppSoap.class, serverUrl);
		this.connectionManager = connectionManager;
	}

	public DataHandler downloadFile(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return fileStorageSoapApp.downloadFile(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public DataHandler downloadFileDirect(String arg0, String arg1, String arg2)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return fileStorageSoapApp.downloadFileDirect(arg0, arg1, arg2);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public String uploadFile(String arg0, DataHandler arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return fileStorageSoapApp.uploadFile(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

}