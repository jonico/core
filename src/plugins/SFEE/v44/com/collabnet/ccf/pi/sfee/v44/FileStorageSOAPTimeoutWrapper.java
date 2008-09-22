package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import javax.activation.DataHandler;

import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.IFileStorageAppSoap;

public class FileStorageSOAPTimeoutWrapper implements IFileStorageAppSoap {

	private IFileStorageAppSoap fileStorageSoapApp;

	public FileStorageSOAPTimeoutWrapper(String serverUrl) {
		fileStorageSoapApp = (IFileStorageAppSoap) ClientSoapStubFactory.getSoapStub(
			IFileStorageAppSoap.class, serverUrl);
			
	}
	
	public DataHandler downloadFile(String arg0, String arg1)
			throws RemoteException {
		return fileStorageSoapApp.downloadFile(arg0, arg1);
	}

	public DataHandler downloadFileDirect(String arg0, String arg1, String arg2)
			throws RemoteException {
		return fileStorageSoapApp.downloadFileDirect(arg0, arg1, arg2);
	}

	public String uploadFile(String arg0, DataHandler arg1)
			throws RemoteException {
		return fileStorageSoapApp.uploadFile(arg0, arg1);
	}

}
