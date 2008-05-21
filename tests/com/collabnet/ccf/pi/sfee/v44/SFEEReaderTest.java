package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.Date;

import junit.framework.TestCase;

import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.ISimpleFileStorageAppSoap;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ITrackerAppSoap;

public class SFEEReaderTest extends TestCase {
	SFEEReader sfeeReader = null;
	String username="mseethar";
    String password="password";
    String serverUrl="http://cu074.cubit.maa.collab.net:8080";
    String keepAlive = "true";
	public void setUp() throws Exception {
		super.setUp();
		sfeeReader = new SFEEReader();
		sfeeReader.setUsername(username);
		sfeeReader.setPassword(password);
		sfeeReader.setServerUrl(serverUrl);
		sfeeReader.setKeepAlive(keepAlive);
		sfeeReader.connect();
		sfeeReader.validate(null);
	}

	@SuppressWarnings("deprecation")
	public void testReadTrackerItems(){
		String projectTracker = "tracker1004";
		//"Mon Nov 05 00:00:00 GMT+05:30 2007"
		Date lastModifiedDate = new Date(2007-1900,10,05,0,0,0);
		//Date lastModifiedDate = new Date(2008-1900,4,7,9,10,0);
		boolean firstTimeImport = false;
		sfeeReader.readTrackerItems(projectTracker, lastModifiedDate, firstTimeImport, null);
	}
	
	public void te2stCommentsList(){
		ISourceForgeSoap soapApp = sfeeReader.getMSfSoap();
		try {
			System.out.println(soapApp.getApiVersion());
			CommentSoapList list = soapApp.getCommentList(sfeeReader.getSessionId(), "artf1054");
			CommentSoapRow[] comments = list.getDataRows();
			for(CommentSoapRow row:comments){
				System.out.println(row.getCreatedBy());
				System.out.println(row.getCreatedByFullname());
				System.out.println(row.getDescription());
				System.out.println(row.getId());
				System.out.println(row.getTransactionId());
				System.out.println();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void te2stWriteAttachment(){
		
		ISimpleFileStorageAppSoap fileStorageApp = (ISimpleFileStorageAppSoap) ClientSoapStubFactory.getSoapStub(
				ISimpleFileStorageAppSoap.class, serverUrl);
		ITrackerAppSoap mTrackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
				ITrackerAppSoap.class, serverUrl);
		byte[] b = "Hello this is my simple attachment".getBytes();
		try {
			String fileDescriptor = fileStorageApp.startFileUpload(sfeeReader.getSessionId());
			System.out.println("File descriptor "+fileDescriptor);
			fileStorageApp.write(sfeeReader.getSessionId(), fileDescriptor, b);
			fileStorageApp.endFileUpload(sfeeReader.getSessionId(), fileDescriptor);
			
			ArtifactSoapDO soapDo = mTrackerApp.getArtifactData(sfeeReader.getSessionId(), "artf1179");
			mTrackerApp.setArtifactData(sfeeReader.getSessionId(), soapDo, "Adding file via Java",
						"myFile.txt", "text/plain", fileDescriptor);
		} catch (RemoteException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void te2stAttachmentsList(){
		ISourceForgeSoap soapApp = sfeeReader.getMSfSoap();
		try {
			
			AttachmentSoapList list = soapApp.listAttachments(sfeeReader.getSessionId(), "artf1179");
			AttachmentSoapRow[] comments = list.getDataRows();
			for(AttachmentSoapRow row:comments){
				System.out.println(row.getAttachmentId());
				System.out.println(row.getRawFileId());
				System.out.println(row.getStoredFileId());
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void tearDown() throws Exception {
		sfeeReader.disconnect();
		super.tearDown();
	}

}
