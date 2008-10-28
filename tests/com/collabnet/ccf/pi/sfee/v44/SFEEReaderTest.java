package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.Date;

import junit.framework.TestCase;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.vasoftware.sf.soap44.types.SoapFilter;
import com.vasoftware.sf.soap44.types.SoapSortKey;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.ISimpleFileStorageAppSoap;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactDetailSoapRow;
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

	public void dummyTestCommentsList() throws MaxConnectionsReachedException, ConnectionException{
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
	
	public void dummyTestWriteAttachment() throws MaxConnectionsReachedException, ConnectionException{
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
	
	public void dummyTestAttachmentsList() throws MaxConnectionsReachedException, ConnectionException{
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
	
	public void testArtfactRetreival() throws RemoteException, MaxConnectionsReachedException, ConnectionException{
		repositoryId = "tracker1215";
		Connection connection = sfeeReader.connect(systemId, systemKind, repositoryId, repositoryKind, connectionInfo, credentialInfo);
		//ISourceForgeSoap soapApp = connection.getSfSoap();
		//Date lastModifiedDate = new Date(2007-1900,5,16,16,45,0);
		Date lastModifiedDate = new Date(0);
		ITrackerAppSoap trackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
				ITrackerAppSoap.class, serverUrl);
		String[] selectedColumns = { ArtifactSoapDO.COLUMN_ID,
				ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE,
				ArtifactSoapDO.COLUMN_VERSION };
		SoapSortKey[] sortKeys = { new SoapSortKey(
				ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE, true) };
		SoapFilter[] filter = { new SoapFilter("modifiedAfter",
				SoapFilter.DATE_FORMAT.format(lastModifiedDate)) };
		ArtifactDetailSoapRow[] rows = trackerApp.getArtifactDetailList(
				connection.getSessionId(), repositoryId, selectedColumns, filter, sortKeys, 0, -1,
				false, false).getDataRows();
		for(ArtifactDetailSoapRow row:rows){
			Date lmDate = row.getLastModifiedDate();
			lmDate = new Date(lmDate.getTime()+1);
			String[] selectedColumns1 = { ArtifactSoapDO.COLUMN_ID,
					ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE,
					ArtifactSoapDO.COLUMN_VERSION };
			SoapSortKey[] sortKeys1 = { new SoapSortKey(
					ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE, true) };
			SoapFilter[] filter1 = { new SoapFilter("modifiedAfter",
					SoapFilter.DATE_FORMAT.format(lmDate)) };
			ArtifactDetailSoapRow[] rows1 = trackerApp.getArtifactDetailList(
					connection.getSessionId(), repositoryId, selectedColumns1, filter1, sortKeys1, 0, -1,
					false, false).getDataRows();
			assertEquals(rows.length-1, rows1.length);
		}
		if(rows != null){
			System.out.println("There were " + rows.length +" artifacts changed");
		}
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
