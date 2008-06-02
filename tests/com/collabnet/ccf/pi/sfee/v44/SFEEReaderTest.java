package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.Date;

import junit.framework.TestCase;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
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
	private String systemKind;
	private String credentialInfo;
	private String repositoryKind;
	private String systemId;
	private String connectionInfo;
	private String repositoryId;
	public void setUp() throws Exception {
		super.setUp();
		systemKind = "kind";
		credentialInfo = username + SFEEConnectionFactory.PARAM_DELIMITER + password;
		repositoryKind = "repokind";
		systemId = "sysid";
		connectionInfo = serverUrl;
		repositoryId = "tracker1004";
		sfeeReader = new SFEEReader("SFEEReader");
		sfeeReader.setUsername(username);
		sfeeReader.setPassword(password);
		sfeeReader.setServerUrl(serverUrl);
		ConnectionManager<Connection> connectionManager = new ConnectionManager<Connection>();
		SFEEConnectionFactory connectionFactory = new SFEEConnectionFactory();
		connectionManager.setConnectionFactory(connectionFactory);
		sfeeReader.setConnectionManager(connectionManager);
		sfeeReader.setSleepInterval(1000L);
		sfeeReader.validate(null);
	}

	@SuppressWarnings("deprecation")
	public void testReadTrackerItems(){
		String projectTracker = "tracker1004";
		repositoryId = projectTracker;
		//"Mon Nov 05 00:00:00 GMT+05:30 2007"
		Date lastModifiedDate = new Date(2007-1900,10,05,0,0,0);
		//Date lastModifiedDate = new Date(2008-1900,4,7,9,10,0);
		boolean firstTimeImport = false;
		Connection connection = sfeeReader.connect(systemId, systemKind, repositoryId, repositoryKind, connectionInfo, credentialInfo);
		sfeeReader.readTrackerItems(projectTracker, lastModifiedDate, firstTimeImport, null,connection);
		sfeeReader.disconnect(connection);
	}
	
	public void te2stCommentsList(){
		Connection connection = sfeeReader.connect(systemId, systemKind, repositoryId, repositoryKind, connectionInfo, credentialInfo);
		ISourceForgeSoap soapApp = connection.getSfSoap();
		try {
			System.out.println(soapApp.getApiVersion());
			CommentSoapList list = soapApp.getCommentList(connection.getSessionId(), "artf1054");
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
		} finally {
			sfeeReader.disconnect(connection);
		}
	}
	
	public void te2stWriteAttachment(){
		Connection connection = sfeeReader.connect(systemId, systemKind, repositoryId, repositoryKind, connectionInfo, credentialInfo);
		ISimpleFileStorageAppSoap fileStorageApp = (ISimpleFileStorageAppSoap) ClientSoapStubFactory.getSoapStub(
				ISimpleFileStorageAppSoap.class, serverUrl);
		ITrackerAppSoap mTrackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
				ITrackerAppSoap.class, serverUrl);
		byte[] b = "Hello this is my simple attachment".getBytes();
		try {
			String fileDescriptor = fileStorageApp.startFileUpload(connection.getSessionId());
			System.out.println("File descriptor "+fileDescriptor);
			fileStorageApp.write(connection.getSessionId(), fileDescriptor, b);
			fileStorageApp.endFileUpload(connection.getSessionId(), fileDescriptor);
			
			ArtifactSoapDO soapDo = mTrackerApp.getArtifactData(connection.getSessionId(), "artf1179");
			mTrackerApp.setArtifactData(connection.getSessionId(), soapDo, "Adding file via Java",
						"myFile.txt", "text/plain", fileDescriptor);
		} catch (RemoteException e) {
			e.printStackTrace();
			fail();
		} finally{
			sfeeReader.disconnect(connection);
		}
	}
	
	public void te2stAttachmentsList(){
		Connection connection = sfeeReader.connect(systemId, systemKind, repositoryId, repositoryKind, connectionInfo, credentialInfo);
		ISourceForgeSoap soapApp = connection.getSfSoap();
		try {
			
			AttachmentSoapList list = soapApp.listAttachments(connection.getSessionId(), "artf1179");
			AttachmentSoapRow[] comments = list.getDataRows();
			for(AttachmentSoapRow row:comments){
				System.out.println(row.getAttachmentId());
				System.out.println(row.getRawFileId());
				System.out.println(row.getStoredFileId());
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			sfeeReader.disconnect(connection);
		}
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
