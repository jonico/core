package com.collabnet.ccf.pi.sfee.v44;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.activation.DataHandler;

import org.apache.commons.codec.binary.Base64;

import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.utils.DateUtil;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.IFileStorageAppSoap;
import com.vasoftware.sf.soap44.webservices.filestorage.ISimpleFileStorageAppSoap;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ITrackerAppSoap;


/**
 * The tracker handler class provides support for listing and/or edit trackers
 * and artifacts.
 */
public class SFEEAttachmentHandler {
	/** Tracker Soap API handle */
	private ITrackerAppSoap mTrackerApp;
	
	private ISimpleFileStorageAppSoap fileStorageApp = null;
	
	private IFileStorageAppSoap fileStorageSoapApp = null;
	
	//private static final Log log = LogFactory.getLog(SFEETrackerHandler.class);

	/**
	 * Class constructor.
	 * 
	 * @param serverUrl
	 *            Soap server URL.
	 */
	public SFEEAttachmentHandler(String serverUrl) {
		mTrackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
				ITrackerAppSoap.class, serverUrl);
		fileStorageApp = (ISimpleFileStorageAppSoap) ClientSoapStubFactory.getSoapStub(
				ISimpleFileStorageAppSoap.class, serverUrl);
		fileStorageSoapApp = (IFileStorageAppSoap) ClientSoapStubFactory.getSoapStub(
				IFileStorageAppSoap.class, serverUrl);
	}
	/**
	 * Updates an artifact.
	 * 
	 * @throws RemoteException
	 *             when an error is encountered in creating the artifact.
	 * @return Newly created artifact
	 */
	public void attachFileToArtifact(String sessionId, String artifactId,
						String comment, String fileName, String mimeType, byte[] data)
			throws RemoteException {
		
		String fileDescriptor = fileStorageApp.startFileUpload(sessionId);
		System.out.println("File descriptor "+fileDescriptor);
		fileStorageApp.write(sessionId, fileDescriptor, data);
		fileStorageApp.endFileUpload(sessionId, fileDescriptor);
			
		ArtifactSoapDO soapDo = mTrackerApp.getArtifactData(sessionId, artifactId);
		mTrackerApp.setArtifactData(sessionId, soapDo, comment,
					fileName, mimeType, fileDescriptor);
	}
	public byte[] getAttachmentData(String sessionId, String fileId, long size, String folderId) throws RemoteException{
		DataHandler dataHandler = fileStorageSoapApp.downloadFileDirect(sessionId, folderId, fileId);
		byte[] data = null;
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(dataHandler.getInputStream());
			//TODO not a safe cast here...
			data = new byte[(int)size];
			int readLength = is.read(data);
			if(readLength == size){
				//Good that we read all the data
			}
			else {
				//TODO something wrong
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// Let me digest this
					e.printStackTrace();
				}
			}
		}
		return data;
	}
	public void handleAttachment(String sessionId, GenericArtifact att, String artifactId, String userName) throws RemoteException {
		String contentType = SFEEWriter.getStringGAField(AttachmentMetaData.ATTACHMENT_TYPE, att);
		String attachDescription = SFEEWriter.getStringGAField(AttachmentMetaData.ATTACHMENT_DESCRIPTION, att);
		System.out.println();
		String attachmentMimeType = SFEEWriter.getStringGAField(AttachmentMetaData.ATTACHMENT_MIME_TYPE, att);
		String attachmentName = SFEEWriter.getStringGAField(AttachmentMetaData.ATTACHMENT_NAME, att);
		attachmentName = userName + "_" + attachmentName;
		String attachmentURL = SFEEWriter.getStringGAField(AttachmentMetaData.ATTACHMENT_SOURCE_URL, att);
		byte[] data = Base64.decodeBase64(att.getArtifactValue().getBytes());
		GenericArtifact.ArtifactActionValue attAction = att.getArtifactAction();
		if(attAction == GenericArtifact.ArtifactActionValue.CREATE){
			if(AttachmentMetaData.AttachmentType.valueOf(contentType) ==
				AttachmentMetaData.AttachmentType.DATA){
				this.attachFileToArtifact(sessionId, artifactId, attachDescription,
						attachmentName, attachmentMimeType, data);
			}
			else if(AttachmentMetaData.AttachmentType.valueOf(contentType) ==
				AttachmentMetaData.AttachmentType.LINK){
				this.attachFileToArtifact(sessionId, artifactId, attachDescription,
						attachmentName+"link.txt", AttachmentMetaData.TEXT_PLAIN, attachmentURL.getBytes());
			}
			else if(AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentMetaData.AttachmentType.EMPTY){
				//TODO What should I do now?
			}
//			else if(contentType == AttachmentMetaData.AttachmentType.UNKNOWN){
//				//TODO What should I do now?
//			}
		}
		else if(attAction == GenericArtifact.ArtifactActionValue.DELETE){
			//TODO not implemented
		}
//		else if(attAction == GenericArtifact.ArtifactActionValue.RENAME){
//			//TODO not implemented
//		}
		else if(attAction == GenericArtifact.ArtifactActionValue.UNKNOWN){
			//TODO What should be done if attachment action value is unknown
			System.out.println("What shout I do now?");
		}
		
	}
	public List<GenericArtifact> listAttachments(String sessionId,
			Date lastModifiedDate, String username,
			Set<ArtifactSoapDO> artifactRows, ISourceForgeSoap sourceForgeSoap) throws RemoteException {
		List<GenericArtifact> gaList = null;
		List<String> artifactIds = new ArrayList<String>();
		if(artifactRows != null && (!artifactIds.isEmpty())){
			for(ArtifactSoapDO artifact:artifactRows){
				String artifactId = artifact.getId();
				artifactIds.add(artifactId);
			}
			gaList = this.listAttachments(sessionId, lastModifiedDate, username, artifactIds, sourceForgeSoap);
		}
		return gaList;
	}
	public List<GenericArtifact> listAttachments(String sessionId,
			Date lastModifiedDate, String username,
			List<String> artifactIds, ISourceForgeSoap sourceForgeSoap) throws RemoteException {
		List<GenericArtifact> attachmentGAs = new ArrayList<GenericArtifact>();
		for(String artifactId:artifactIds){
			//String folderId = artifact.getFolderId();
			AttachmentSoapList attachmentsList = sourceForgeSoap.listAttachments(sessionId, artifactId);
			AttachmentSoapRow[] attachmentRows = attachmentsList.getDataRows();
			for(AttachmentSoapRow row:attachmentRows){
				String fileName = row.getFileName();
				if(fileName.startsWith(username+"_")){
					continue;
				}
				Date createdDate = row.getDateCreated();
				if(createdDate.after(lastModifiedDate)){
					GenericArtifact ga = new GenericArtifact();
					ga.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
					ga.setArtifactLastModifiedDate(DateUtil.format(createdDate));
					ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
					ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
					ga.setSourceArtifactId(artifactId);
					GenericArtifactField contentTypeField = 
						ga.addNewField(AttachmentMetaData.ATTACHMENT_TYPE, AttachmentMetaData.ATTACHMENT_TYPE,
								GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					contentTypeField.setFieldValue(AttachmentMetaData.AttachmentType.DATA);
					contentTypeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					contentTypeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					GenericArtifactField sourceURLField = 
						ga.addNewField(AttachmentMetaData.ATTACHMENT_SOURCE_URL, AttachmentMetaData.ATTACHMENT_SOURCE_URL,
								GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					sourceURLField.setFieldValue(AttachmentMetaData.AttachmentType.LINK);
					sourceURLField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					sourceURLField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					//gaAttachment.setAttachmentAction(GenericArtifactAttachment.AttachmentActionValue.CREATE);
					
					GenericArtifactField nameField = ga.addNewField(AttachmentMetaData.ATTACHMENT_NAME, AttachmentMetaData.ATTACHMENT_NAME,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					nameField.setFieldValue(row.getFileName());
					nameField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					nameField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					//gaAttachment.setAttachmentDescription(row.getFileName());
					GenericArtifactField idField = ga.addNewField(AttachmentMetaData.ATTACHMENT_ID, AttachmentMetaData.ATTACHMENT_ID,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					idField.setFieldValue(row.getAttachmentId());
					idField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					idField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					GenericArtifactField sizeField = ga.addNewField(AttachmentMetaData.ATTACHMENT_SIZE, AttachmentMetaData.ATTACHMENT_SIZE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					sizeField.setFieldValue(Long.parseLong(row.getFileSize()));
					sizeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					sizeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					//gaAttachment.setAttachmentType();

					GenericArtifactField mimeTypeField = ga.addNewField(AttachmentMetaData.ATTACHMENT_MIME_TYPE, AttachmentMetaData.ATTACHMENT_MIME_TYPE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					mimeTypeField.setFieldValue(row.getMimetype());
					mimeTypeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					mimeTypeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					System.out.println(row.getAttachmentId()+" "+
							row.getRawFileId()+" "+
							row.getStoredFileId());
					byte[] attachmentData = null;
					 attachmentData = this.getAttachmentData(sessionId, row.getRawFileId(),
							Long.parseLong(row.getFileSize()), artifactId);
					ga.setArtifactValue(new String(Base64.encodeBase64(attachmentData)));
					attachmentGAs.add(ga);
				}
			}
		}
		return attachmentGAs;
	}

}
