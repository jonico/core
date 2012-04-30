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

import org.apache.axis.AxisFault;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.ISimpleFileStorageAppSoap;

public class SimpleFileStorageSOAPAppTimeoutWrapper extends TimeoutWrapper
		implements ISimpleFileStorageAppSoap {

	private ISimpleFileStorageAppSoap fileStorageApp;
	private ConnectionManager<Connection> connectionManager;

	public SimpleFileStorageSOAPAppTimeoutWrapper(String serverUrl,
			ConnectionManager<Connection> connectionManager) {
		fileStorageApp = (ISimpleFileStorageAppSoap) ClientSoapStubFactory
				.getSoapStub(ISimpleFileStorageAppSoap.class, serverUrl);
		this.connectionManager = connectionManager;
	}

	public void endFileUpload(String arg0, String arg1) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				fileStorageApp.endFileUpload(arg0, arg1);
				retryCall = false;
			}
			// we do not catch invalid session exceptions here since the file id
			// would be invalid
			catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public long getSize(String arg0, String arg1) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return fileStorageApp.getSize(arg0, arg1);
			}
			// we do not catch invalid session exceptions here since the file id
			// would be invalid
			catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public byte[] read(String arg0, String arg1, int arg2, int arg3)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return fileStorageApp.read(arg0, arg1, arg2, arg3);
			}
			// we do not catch invalid session exceptions here since the file id
			// would be invalid
			catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public String startFileUpload(String arg0) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return fileStorageApp.startFileUpload(arg0);
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

	public void write(String arg0, String arg1, byte[] arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				fileStorageApp.write(arg0, arg1, arg2);
				retryCall = false;
			}
			// we do not catch invalid session exceptions here since the file id
			// would be invalid
			catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

}
