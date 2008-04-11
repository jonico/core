package com.collabnet.ccf.pi.sfee;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactAttachment;
import com.vasoftware.sf.soap44.webservices.filestorage.ISimpleFileStorageAppSoap;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ITrackerAppSoap;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The tracker handler class provides support for listing and/or edit trackers
 * and artifacts.
 */
public class SFEEAttachmentHandler {
	/** Tracker Soap API handle */
	private ITrackerAppSoap mTrackerApp;
	
	private ISimpleFileStorageAppSoap fileStorageApp = null;

	private static final Log log = LogFactory.getLog(SFEETrackerHandler.class);

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
	}
	/**
	 * Updates an artifact.
	 * 
	 * @throws RemoteException
	 *             when an error is encountered in creating the artifact.
	 * @return Newly created artifact
	 */
	public ArtifactSoapDO attachFileToArtifact(String sessionId, ArtifactSoapDO artifact,
						String comment, String fileName, String mimeType, byte[] data)
			throws RemoteException {
		
		String fileDescriptor = fileStorageApp.startFileUpload(sessionId);
		System.out.println("File descriptor "+fileDescriptor);
		fileStorageApp.write(sessionId, fileDescriptor, data);
		fileStorageApp.endFileUpload(sessionId, fileDescriptor);
			
		ArtifactSoapDO soapDo = mTrackerApp.getArtifactData(sessionId, "artf1054");
		mTrackerApp.setArtifactData(sessionId, soapDo, comment,
					fileName, mimeType, fileDescriptor);
		return artifact;
	}
	public byte[] getAttachmentData(String sessionId, String fileId, long size) throws RemoteException{
		return fileStorageApp.read(sessionId, fileId, 0, (int)size);
	}
	public void handleAttachment(String sessionId, GenericArtifactAttachment att, ArtifactSoapDO artifact) throws RemoteException {
		GenericArtifactAttachment.AttachmentContentTypeValue contentType =
			att.getAttachmentContentType();
		String attachDescription = att.getAttachmentDescription();
		String attachmentId = att.getAttachmentId();
		String attachmentName = att.getAttachmentName();
		long size = att.getAttachmentSize();
		String attachmentURL = att.getAttachmentSourceUrl();
		String attachmentType = att.getAttachmentType();
		GenericArtifactAttachment.AttachmentValueTypeValue valueType = att.getAttachmentValueType();
		byte[] data = att.getRawAttachmentData();
		GenericArtifactAttachment.AttachmentActionValue attAction = att.getAttachmentAction();
		if(attAction == GenericArtifactAttachment.AttachmentActionValue.CREATE){
			if(contentType == GenericArtifactAttachment.AttachmentContentTypeValue.DATA){
				this.attachFileToArtifact(sessionId, artifact, attachDescription,
						attachmentName, att.getMimeType(), data);
			}
			else if(contentType == GenericArtifactAttachment.AttachmentContentTypeValue.LINK){
				this.attachFileToArtifact(sessionId, artifact, attachDescription,
						attachmentName+"link.txt", GenericArtifactAttachment.TEXT_PLAIN, attachmentURL.getBytes());
			}
			else if(contentType == GenericArtifactAttachment.AttachmentContentTypeValue.EMPTY){
				//TODO What should I do now?
			}
			else if(contentType == GenericArtifactAttachment.AttachmentContentTypeValue.UNKNOWN){
				//TODO What should I do now?
			}
		}
		else if(attAction == GenericArtifactAttachment.AttachmentActionValue.DELETE){
			//TODO not implemented
		}
		else if(attAction == GenericArtifactAttachment.AttachmentActionValue.RENAME){
			//TODO not implemented
		}
		else if(attAction == GenericArtifactAttachment.AttachmentActionValue.UNKNOWN){
			//TODO What should be done if attachment action value is unknown
			System.out.println("What shout I do now?");
		}
		
	}
	public TreeMap<Date, GenericArtifact> listAttachments(String sessionId,
			Date lastModifiedDate, String username,
			List<ArtifactSoapDO> artifactRows, ISourceForgeSoap sourceForgeSoap) throws RemoteException {
		TreeMap<Date, GenericArtifact> attachmentGAs = new TreeMap<Date, GenericArtifact>();
		for(ArtifactSoapDO artifact:artifactRows){
			String artifactId = artifact.getId();
			AttachmentSoapList attachmentsList = sourceForgeSoap.listAttachments(sessionId, artifactId);
			AttachmentSoapRow[] attachmentRows = attachmentsList.getDataRows();
			for(AttachmentSoapRow row:attachmentRows){
				Date createdDate = row.getDateCreated();
				if(createdDate.after(lastModifiedDate)){
					GenericArtifact ga = new GenericArtifact();
					GenericArtifactAttachment gaAttachment = new GenericArtifactAttachment();
					gaAttachment.setAttachmentAction(GenericArtifactAttachment.AttachmentActionValue.CREATE);
					gaAttachment.setAttachmentContentType(GenericArtifactAttachment.AttachmentContentTypeValue.DATA);
					gaAttachment.setAttachmentDescription(row.getFileName());
					gaAttachment.setAttachmentId(row.getAttachmentId());
					gaAttachment.setAttachmentName(row.getFileName());
					gaAttachment.setAttachmentSize(Long.parseLong(row.getFileSize()));
					//gaAttachment.setAttachmentType();
					gaAttachment.setAttachmentValueHasChanged(true);
					gaAttachment.setAttachmentValueType(GenericArtifactAttachment.AttachmentValueTypeValue.BASE64STRING);
					gaAttachment.setMimeType(row.getMimetype());
					byte[] attachmentData = this.getAttachmentData(sessionId, row.getAttachmentId(),
							Long.parseLong(row.getFileSize()));
					gaAttachment.setRawAttachmentData(attachmentData);
					attachmentGAs.put(createdDate, ga);
				}
			}
		}
		return attachmentGAs;
	}

}
