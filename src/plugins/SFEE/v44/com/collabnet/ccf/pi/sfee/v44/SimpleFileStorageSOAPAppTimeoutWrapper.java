package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.ISimpleFileStorageAppSoap;


public class SimpleFileStorageSOAPAppTimeoutWrapper implements
		ISimpleFileStorageAppSoap {

	private ISimpleFileStorageAppSoap fileStorageApp;

	public SimpleFileStorageSOAPAppTimeoutWrapper(String serverUrl) {
		fileStorageApp = (ISimpleFileStorageAppSoap) ClientSoapStubFactory.getSoapStub(ISimpleFileStorageAppSoap.class, serverUrl);	
	}
	
	public void endFileUpload(String arg0, String arg1) throws RemoteException {
		fileStorageApp.endFileUpload(arg0, arg1);
	}

	public long getSize(String arg0, String arg1) throws RemoteException {
		return fileStorageApp.getSize(arg0, arg1);
	}

	public byte[] read(String arg0, String arg1, int arg2, int arg3)
			throws RemoteException {
		return fileStorageApp.read(arg0, arg1, arg2, arg3);
	}

	public String startFileUpload(String arg0) throws RemoteException {
		return fileStorageApp.startFileUpload(arg0);
	}

	public void write(String arg0, String arg1, byte[] arg2)
			throws RemoteException {
		fileStorageApp.write(arg0, arg1, arg2);
	}

}
