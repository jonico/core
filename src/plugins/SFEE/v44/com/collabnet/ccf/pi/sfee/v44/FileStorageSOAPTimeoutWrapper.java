package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import javax.activation.DataHandler;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.IFileStorageAppSoap;

public class FileStorageSOAPTimeoutWrapper extends TimeoutWrapper implements
		IFileStorageAppSoap {

	private IFileStorageAppSoap fileStorageSoapApp;
	private ConnectionManager<Connection> connectionManager;

	public FileStorageSOAPTimeoutWrapper(String serverUrl, ConnectionManager<Connection> connectionManager) {
		fileStorageSoapApp = (IFileStorageAppSoap) ClientSoapStubFactory
				.getSoapStub(IFileStorageAppSoap.class, serverUrl);
		this.connectionManager=connectionManager;
	}

	public DataHandler downloadFile(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return fileStorageSoapApp.downloadFile(arg0, arg1);
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
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

}
