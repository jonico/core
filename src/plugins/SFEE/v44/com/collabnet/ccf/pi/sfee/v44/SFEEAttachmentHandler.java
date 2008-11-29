package com.collabnet.ccf.pi.sfee.v44;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.utils.DateUtil;
import com.vasoftware.sf.soap44.fault.InvalidSessionFault;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.filestorage.IFileStorageAppSoap;
import com.vasoftware.sf.soap44.webservices.filestorage.ISimpleFileStorageAppSoap;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ITrackerAppSoap;

/**
 * This class is responsible for retrieving attachment data for an artifact from
 * a Source SFEE repository.
 *
 * @author madhusuthanan
 */
public class SFEEAttachmentHandler {
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory
			.getLog(SFEEAttachmentHandler.class);
	private static final Log logConflictResolutor = LogFactory.getLog("com.collabnet.ccf.core.conflict.resolution");
	/** Tracker Soap API handle */
	private ITrackerAppSoap mTrackerApp;

	private ISimpleFileStorageAppSoap fileStorageApp = null;

	private IFileStorageAppSoap fileStorageSoapApp = null;

	private ConnectionManager<Connection> connectionManager = null;

	/**
	 * Class constructor.
	 *
	 * @param serverUrl
	 *            Soap server URL.
	 * @param connectionManager
	 */
	public SFEEAttachmentHandler(String serverUrl,
			ConnectionManager<Connection> connectionManager) {
		this.connectionManager = connectionManager;
		if (!connectionManager.isUseStandardTimeoutHandlingCode()) {
			mTrackerApp = new TrackerAppSoapTimeoutWrapper(serverUrl,
					connectionManager);
			fileStorageApp = new SimpleFileStorageSOAPAppTimeoutWrapper(
					serverUrl, connectionManager);
			fileStorageSoapApp = new FileStorageSOAPTimeoutWrapper(serverUrl,
					connectionManager);
		} else {
			mTrackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
					ITrackerAppSoap.class, serverUrl);
			fileStorageApp = (ISimpleFileStorageAppSoap) ClientSoapStubFactory
					.getSoapStub(ISimpleFileStorageAppSoap.class, serverUrl);
			fileStorageSoapApp = (IFileStorageAppSoap) ClientSoapStubFactory
					.getSoapStub(IFileStorageAppSoap.class, serverUrl);
		}
	}

	/**
	 * This method uploads the file and gets the new file descriptor returned
	 * from the SFEE system. It then associates the file descriptor to the
	 * artifact there by adding the attachment to the artifact.
	 *
	 * @param sessionId -
	 *            The current session id
	 * @param artifactId -
	 *            The artifact's id to which the attachment should be added.
	 * @param comment -
	 *            Comment for the attachment addition
	 * @param fileName -
	 *            Name of the file that is attached to this artifact
	 * @param mimeType -
	 *            MIME type of the file that is being attached.
	 * @param att -
	 *            the file content
	 * @param linkUrl
	 *
	 * @throws RemoteException -
	 *             if any SOAP api call fails
	 */
	public ArtifactSoapDO attachFileToArtifact(String sessionId,
			String artifactId, String comment, String fileName,
			String mimeType, GenericArtifact att, byte[] linkUrl) throws RemoteException {
		ArtifactSoapDO soapDo = null;
		String attachmentDataFileName = GenericArtifactHelper.getStringGAField(
				AttachmentMetaData.ATTACHMENT_DATA_FILE, att);
		boolean retryCall = true;
		while (retryCall) {
			retryCall = false;
			String fileDescriptor = null;
			try {
				byte[] data = null;
				if(StringUtils.isEmpty(attachmentDataFileName)){
					if(linkUrl == null) {
						data = att.getRawAttachmentData();
					}
					else {
						data = linkUrl;
					}
					fileDescriptor = fileStorageApp.startFileUpload(sessionId);
					fileStorageApp.write(sessionId, fileDescriptor, data);
					fileStorageApp.endFileUpload(sessionId, fileDescriptor);
				}
				else {
					try {
						DataSource dataSource = new FileDataSource(new File(attachmentDataFileName));
						DataHandler dataHandler = new DataHandler(dataSource);
						fileDescriptor = fileStorageSoapApp.uploadFile(sessionId, dataHandler);
					} catch (IOException e) {
						String message = "Exception while uploading the attachment "+ attachmentDataFileName;
						log.error(message, e);
						throw new CCFRuntimeException(message, e);
					}
				}

				soapDo = mTrackerApp.getArtifactData(sessionId, artifactId);
				boolean fileAttached = true;
				while (fileAttached) {
					try {
						fileAttached = false;
						mTrackerApp.setArtifactData(sessionId, soapDo, comment,
								fileName, mimeType, fileDescriptor);
					} catch (com.vasoftware.sf.soap44.fault.VersionMismatchFault e) {
						logConflictResolutor.warn("Stale attachment update, trying again ...:",
								e);
						soapDo = mTrackerApp.getArtifactData(sessionId, artifactId);
						fileAttached = true;
					}
				}
			} catch (InvalidSessionFault e) {
				if (connectionManager.isEnableReloginAfterSessionTimeout()
						&& (!connectionManager
								.isUseStandardTimeoutHandlingCode())) {
					log
							.warn(
									"While uploading an attachment, the session id became invalid, trying again",
									e);
					retryCall = true;
				} else {
					throw e;
				}
			}
		}
		// we have to increase the version after the update
		// TODO Find out whether this really works if last modified date differs
		// from actual last modified date
		soapDo.setVersion(soapDo.getVersion() + 1);
		return soapDo;
	}

	/**
	 * Retrieves the attachment data for a given file id.
	 *
	 * @param sessionId
	 * @param fileId
	 * @param size
	 * @param folderId
	 * @param shouldShipAttachmentsWithArtifact
	 * @return
	 * @throws RemoteException
	 */
	public byte[] getAttachmentData(String sessionId, String fileId, long size,
			String folderId, boolean shouldShipAttachmentsWithArtifact,
			GenericArtifact ga) throws RemoteException {
		boolean retryCall = true;
		byte[] data = null;
		while (retryCall) {
			retryCall = false;
			DataHandler dataHandler = fileStorageSoapApp.downloadFileDirect(
					sessionId, folderId, fileId);
			BufferedInputStream is = null;
			try {
				is = new BufferedInputStream(dataHandler.getInputStream());
				if(shouldShipAttachmentsWithArtifact) {
					// TODO not a safe cast here...
					data = new byte[(int) size];
					int readLength = is.read(data);
					if (readLength == size) {
						// Good that we read all the data
					} else {
						// FIXME What if the attachment's size has been changed in
						// the mean time?
						log
								.error("While reading data from the attachment stream, less data than expected was returned.");
					}
				}
				else {
					File tempFile = null;
					data = new byte[1024*3];
					tempFile = File.createTempFile("CSFE_Attachment", "file");

					String attachmentDataFile = tempFile.getAbsolutePath();
					int readBytes = 0;
					FileOutputStream fos = new FileOutputStream(tempFile);
					while((readBytes = is.read(data)) != -1){
						fos.write(data,0,readBytes);
					}
					fos.close();
					GenericArtifactField attachmentDataFileField =
						ga.addNewField(AttachmentMetaData.ATTACHMENT_DATA_FILE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					attachmentDataFileField.setFieldValueType(
							GenericArtifactField.FieldValueTypeValue.STRING);
					attachmentDataFileField.setFieldValue(attachmentDataFile);
					data = null;
				}
			} catch (IOException e) {
				if (connectionManager.isEnableRetryAfterNetworkTimeout()
						&& (!connectionManager
								.isUseStandardTimeoutHandlingCode())) {
					log
							.warn(
									"An IO-Error while reading the attachment stream occured, trying again: ",
									e);
					retryCall = true;
				} else {
					throw new RuntimeException(e);
				}

			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						log
								.error("By closing the attachment stream, an error occured: "
										+ e.getMessage());
					}
				}
			}
		}
		return data;
	}

	/**
	 * This method decodes the attachment data from the incoming GenericArtifact
	 * object and adds to the target SFEE system tracker's artifact.
	 *
	 * @param sessionId
	 * @param att
	 * @param artifactId
	 * @param userName
	 * @param sourceForgeSoap
	 * @throws RemoteException
	 */
	public void handleAttachment(String sessionId, GenericArtifact att,
			String artifactId, String userName, ISourceForgeSoap sourceForgeSoap)
			throws RemoteException {
		String contentType = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_TYPE, att);
		String attachDescription = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_DESCRIPTION, att);
		String attachmentMimeType = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_MIME_TYPE, att);
		String attachmentName = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_NAME, att);
		attachmentName = userName + "_" + attachmentName;
		String attachmentURL = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_SOURCE_URL, att);
		GenericArtifact.ArtifactActionValue attAction = att.getArtifactAction();
		ArtifactSoapDO artifact = null;
		if (attAction == GenericArtifact.ArtifactActionValue.CREATE) {
			if (AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentMetaData.AttachmentType.DATA) {
				artifact = this.attachFileToArtifact(sessionId, artifactId,
						attachDescription, attachmentName, attachmentMimeType,
						att, null);
			} else if (AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentMetaData.AttachmentType.LINK) {
				attachmentName = attachmentName + "link.txt";
				artifact = this
						.attachFileToArtifact(sessionId, artifactId,
								attachDescription, attachmentName,
								AttachmentMetaData.TEXT_PLAIN, att, attachmentURL
										.getBytes());
			} else if (AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentMetaData.AttachmentType.EMPTY) {
				// TODO What should I do now?
			}
			// else if(contentType ==
			// AttachmentMetaData.AttachmentType.UNKNOWN){
			// //TODO What should I do now?
			// }
		}
		else if (attAction == GenericArtifact.ArtifactActionValue.UNKNOWN) {
			// TODO What should be done if attachment action value is unknown
			log.error("What shout I do now?");
		}
		if (artifact != null) {
			Date attachmentLastModifiedDate = artifact.getLastModifiedDate();
			AttachmentSoapRow attachmentRow = getAttachmentMetaData(
					attachmentName, attachmentLastModifiedDate,
					sourceForgeSoap, sessionId, artifactId);
			if(attachmentRow != null) {
				att.setTargetArtifactLastModifiedDate(DateUtil.format(attachmentRow
						.getDateCreated()));
				att.setTargetArtifactVersion("1");
				att.setTargetArtifactId(attachmentRow.getAttachmentId());
			}
			else {
				att.setTargetArtifactLastModifiedDate(DateUtil.format(attachmentLastModifiedDate));
				att.setTargetArtifactVersion("1");
				att.setTargetArtifactId(GenericArtifact.VALUE_UNKNOWN);
			}
		}
	}

	public void deleteAttachment(Connection connection, String attachmentId, String artifactId,
			GenericArtifact att) throws RemoteException{
		String sessionId = connection.getSessionId();
		ISourceForgeSoap sfSoap = connection.getSfSoap();
		sfSoap.deleteAttachment(sessionId, artifactId, attachmentId);
		ArtifactSoapDO artifact = mTrackerApp.getArtifactData(sessionId, artifactId);
		if (artifact != null) {
			Date attachmentLastModifiedDate = artifact.getLastModifiedDate();
			att.setTargetArtifactLastModifiedDate(DateUtil.format(attachmentLastModifiedDate));
			att.setTargetArtifactVersion(Integer.toString(artifact.getVersion()));
		}
	}

	private AttachmentSoapRow getAttachmentMetaData(String fileName,
			Date createdDate, ISourceForgeSoap sourceForgeSoap,
			String sessionId, String artifactId) throws RemoteException {
		AttachmentSoapList attachmentsList = sourceForgeSoap.listAttachments(
				sessionId, artifactId);
		AttachmentSoapRow[] attachmentRows = attachmentsList.getDataRows();
		AttachmentSoapRow returnRow = null;
		for (int i = 0; i < attachmentRows.length; i++) {
			AttachmentSoapRow row = attachmentRows[i];
			if (row.getFileName().equals(fileName)) {
				returnRow = row;
			}
		}
		if(returnRow == null) {
			log.warn("No attachments match with the name " + fileName
					+ " created time " + createdDate);
		}
		return returnRow;
	}

//	/**
//	 * This method retrieves all the attachments for all the artifacts present
//	 * in the artifactRows Set and encoded them into GenericArtifact attachment
//	 * format.
//	 *
//	 * @param sessionId
//	 * @param lastModifiedDate
//	 * @param username
//	 * @param artifactRows
//	 * @param sourceForgeSoap
//	 * @return
//	 * @throws RemoteException
//	 */
//	public List<GenericArtifact> listAttachments(String sessionId,
//			Date lastModifiedDate, String username,
//			Set<ArtifactSoapDO> artifactRows, ISourceForgeSoap sourceForgeSoap,
//			long maxAttachmentSizePerArtifact) throws RemoteException {
//		List<GenericArtifact> gaList = null;
//		List<String> artifactIds = new ArrayList<String>();
//		if (artifactRows != null && (!artifactIds.isEmpty())) {
//			for (ArtifactSoapDO artifact : artifactRows) {
//				String artifactId = artifact.getId();
//				artifactIds.add(artifactId);
//			}
//			gaList = this.listAttachments(sessionId, lastModifiedDate,
//					username, artifactIds, sourceForgeSoap,
//					maxAttachmentSizePerArtifact);
//		}
//		return gaList;
//	}

	/**
	 * This method retrieves all the attachments for all the artifacts present
	 * in the artifactRows Set and encoded them into GenericArtifact attachment
	 * format.
	 *
	 * @param sessionId
	 * @param lastModifiedDate
	 * @param username
	 * @param artifactRows
	 * @param sourceForgeSoap
	 * @param maxAttachmentSizePerArtifact
	 * @param artifactData
	 * @return
	 * @throws RemoteException
	 */
	public List<GenericArtifact> listAttachments(String sessionId,
			Date lastModifiedDate, String username, List<String> artifactIds,
			ISourceForgeSoap sourceForgeSoap, long maxAttachmentSizePerArtifact,
			boolean shouldShipAttachmentsWithArtifact, GenericArtifact artifactData)
			throws RemoteException {
		List<GenericArtifact> attachmentGAs = new ArrayList<GenericArtifact>();
		for (String artifactId : artifactIds) {
			// String folderId = artifact.getFolderId();
			AttachmentSoapList attachmentsList = sourceForgeSoap
					.listAttachments(sessionId, artifactId);
			AttachmentSoapRow[] attachmentRows = attachmentsList.getDataRows();
			for (AttachmentSoapRow row : attachmentRows) {
				String fileName = row.getFileName();
				String attachmentSizeStr = row.getFileSize();
				//String attachmentId = row.getStoredFileId();
				long attachmentSize = Long.parseLong(attachmentSizeStr);
				if (fileName.startsWith(username + "_")) {
					continue;
				}
				if (attachmentSize > maxAttachmentSizePerArtifact) {
					log
							.warn("attachment size is more than the configured maxAttachmentSizePerArtifact "
									+ attachmentSizeStr);
					continue;
				}
				Date createdDate = row.getDateCreated();
				if (createdDate.after(lastModifiedDate)) {
					GenericArtifact ga = new GenericArtifact();
					ga
							.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
					ga
							.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);

					ga
							.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
					ga
							.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
					ga.setDepParentSourceArtifactId(artifactId);
					ga.setSourceArtifactId(row.getAttachmentId());
					if(artifactData != null) {
						ga.setSourceArtifactVersion(artifactData.getSourceArtifactVersion());
						ga.setSourceArtifactLastModifiedDate(
								artifactData.getSourceArtifactLastModifiedDate());
					}
					else {
						ga.setSourceArtifactVersion("1");
						ga.setSourceArtifactLastModifiedDate(DateUtil
								.format(createdDate));
					}
					GenericArtifactField contentTypeField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_TYPE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					contentTypeField
							.setFieldValue(AttachmentMetaData.AttachmentType.DATA);
					contentTypeField
							.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					contentTypeField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					GenericArtifactField sourceURLField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_SOURCE_URL,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					sourceURLField
							.setFieldValue(AttachmentMetaData.AttachmentType.LINK);
					sourceURLField
							.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					sourceURLField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					// gaAttachment.setAttachmentAction(GenericArtifactAttachment.AttachmentActionValue.CREATE);

					GenericArtifactField nameField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_NAME,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					nameField.setFieldValue(row.getFileName());
					nameField
							.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					nameField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					// gaAttachment.setAttachmentDescription(row.getFileName());
					GenericArtifactField sizeField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_SIZE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					sizeField.setFieldValue(Long.parseLong(row.getFileSize()));
					sizeField
							.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					sizeField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					// gaAttachment.setAttachmentType();

					GenericArtifactField mimeTypeField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_MIME_TYPE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					mimeTypeField.setFieldValue(row.getMimetype());
					mimeTypeField
							.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					mimeTypeField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

					byte[] attachmentData = null;
					attachmentData = this.getAttachmentData(sessionId, row
							.getRawFileId(), Long.parseLong(row.getFileSize()),
							artifactId, shouldShipAttachmentsWithArtifact, ga);
					if(shouldShipAttachmentsWithArtifact) {
						if(attachmentData != null) {
							ga.setRawAttachmentData(attachmentData);
						}
					}
					attachmentGAs.add(ga);
				}
			}
		}
		return attachmentGAs;
	}
}
