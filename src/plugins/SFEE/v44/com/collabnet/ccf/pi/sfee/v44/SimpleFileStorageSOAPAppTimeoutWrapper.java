package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.ISimpleFileStorageAppSoap;

public class SimpleFileStorageSOAPAppTimeoutWrapper extends TimeoutWrapper
		implements ISimpleFileStorageAppSoap {

	private ISimpleFileStorageAppSoap fileStorageApp;
	private ConnectionManager<Connection> connectionManager;

	public SimpleFileStorageSOAPAppTimeoutWrapper(String serverUrl, ConnectionManager<Connection> connectionManager) {
		fileStorageApp = (ISimpleFileStorageAppSoap) ClientSoapStubFactory
				.getSoapStub(ISimpleFileStorageAppSoap.class, serverUrl);
		this.connectionManager=connectionManager;
	}

	public void endFileUpload(String arg0, String arg1) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				fileStorageApp.endFileUpload(arg0, arg1);
				retryCall = false;
			} catch (RemoteException e) {
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
			} catch (RemoteException e) {
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
			} catch (RemoteException e) {
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
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

}
