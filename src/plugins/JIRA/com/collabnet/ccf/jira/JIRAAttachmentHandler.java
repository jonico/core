package com.collabnet.ccf.jira;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atlassian.jira.rest.client.IssueRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.Issue;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.utils.DateUtil;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.files.Attachment;
import com.microsoft.tfs.core.clients.workitem.files.AttachmentCollection;

public class JIRAAttachmentHandler {

	private static final Log log = LogFactory
			.getLog(JIRAAttachmentHandler.class);

	private static final Log logConflictResolutor = LogFactory
			.getLog("com.collabnet.ccf.core.conflict.resolution");

	static final NullProgressMonitor pm = new NullProgressMonitor();
	
	private ConnectionManager<JIRAConnection> connectionManager = null;

	public JIRAAttachmentHandler(String serverUrl,
			ConnectionManager<JIRAConnection> connectionManager) {
		this.connectionManager = connectionManager;
	}

	public List<GenericArtifact> listAttachments(JIRAConnection connection,
			Date lastModifiedDate, String username, List<String> artifactIds,
			long maxAttachmentSizePerArtifact,
			boolean shouldShipAttachmentsWithArtifact,
			GenericArtifact artifactData) throws RemoteException {

		List<GenericArtifact> attachmentGAs = new ArrayList<GenericArtifact>();

		for (String artifactId : artifactIds) {

			WorkItem wi = connection.getTpc().getWorkItemClient()
					.getWorkItemByID(Integer.valueOf(artifactId));
			AttachmentCollection attachmentsList = wi.getAttachments();

			Iterator<Attachment> it = attachmentsList.iterator();

			while (it.hasNext()) {

				Attachment attachment = it.next();

				String fileName = attachment.getFileName();
				Long attachmentSize = attachment.getFileSize();

				if (attachmentSize > maxAttachmentSizePerArtifact) {
					log.warn("attachment size is more than the configured maxAttachmentSizePerArtifact "
							+ attachmentSize);
					continue;
				}

				if (fileName.startsWith(username + "_")) {
					log.warn("file attached by ccf user, ignoring it");
					continue;
				}

				Date createdDate = attachment.getAttachmentAddedDate();
				if (createdDate.after(lastModifiedDate)) {

					GenericArtifact ga = new GenericArtifact();

					ga.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
					ga.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
					ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
					ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
					ga.setDepParentSourceArtifactId(artifactId);
					ga.setSourceArtifactId(String.valueOf(attachment
							.getFileID()));

					if (artifactData != null) {
						ga.setSourceArtifactVersion(artifactData
								.getSourceArtifactVersion());
						ga.setSourceArtifactLastModifiedDate(artifactData
								.getSourceArtifactLastModifiedDate());
					} else {
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
					// gaAttachment.setAttachmentAction(GenericArtifactAttachment
					// .AttachmentActionValue.CREATE);

					GenericArtifactField nameField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_NAME,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					nameField.setFieldValue(attachment.getFileName());
					nameField
							.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					nameField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					// gaAttachment.setAttachmentDescription(row.getFileName());
					GenericArtifactField sizeField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_SIZE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					sizeField.setFieldValue(attachment.getFileSize());
					sizeField
							.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					sizeField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					// gaAttachment.setAttachmentType();

					GenericArtifactField mimeTypeField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_MIME_TYPE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);

					File file = new File(attachment.getURL().toString());

					mimeTypeField.setFieldValue(new MimetypesFileTypeMap()
							.getContentType(file));

					mimeTypeField
							.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					mimeTypeField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

					byte[] attachmentData = null;

					attachmentData = this.getAttachmentData(connection,
							attachment, shouldShipAttachmentsWithArtifact, ga);

					if (shouldShipAttachmentsWithArtifact) {
						if (attachmentData != null) {
							ga.setRawAttachmentData(attachmentData);
						}
					}
					attachmentGAs.add(ga);

				} else {
					log.warn("The attachment creation date is before the sync date, ingnoring it");
				}

			}

		}
		return attachmentGAs;
	}

	public byte[] getAttachmentData(JIRAConnection connection,
			Attachment attachment, boolean shouldShipAttachmentsWithArtifact,
			GenericArtifact ga) throws RemoteException {
		boolean retryCall = true;
		byte[] data = null;
		while (retryCall) {
			retryCall = false;

			int size = new Long(attachment.getFileSize()).intValue();

			File file = new File(String.valueOf(attachment.getFileID()));
			attachment.downloadTo(file);

			BufferedInputStream is = null;

			try {

				is = new BufferedInputStream(new FileInputStream(file));

				if (shouldShipAttachmentsWithArtifact) {

					data = new byte[(int) size];
					int readLength = is.read(data);
					if (readLength == size) {
						// Good that we read all the data
					} else {

						log.error("While reading data from the attachment stream, less data than expected was returned.");
					}
				} else {
					File tempFile = null;
					data = new byte[1024 * 3];
					tempFile = File.createTempFile("CSFE_Attachment", "file");

					String attachmentDataFile = tempFile.getAbsolutePath();
					int readBytes = 0;
					FileOutputStream fos = new FileOutputStream(tempFile);
					while ((readBytes = is.read(data)) != -1) {
						fos.write(data, 0, readBytes);
					}
					fos.close();
					GenericArtifactField attachmentDataFileField = ga
							.addNewField(
									AttachmentMetaData.ATTACHMENT_DATA_FILE,
									GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					attachmentDataFileField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					attachmentDataFileField.setFieldValue(attachmentDataFile);
					attachmentDataFileField
							.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				}
			} catch (IOException e) {
				if (connectionManager.isEnableRetryAfterNetworkTimeout()
						&& (!connectionManager
								.isUseStandardTimeoutHandlingCode())) {
					log.warn(
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
						log.error("By closing the attachment stream, an error occured: "
								+ e.getMessage());
					}
				}
			}
		}
		return data;
	}

	public Issue handleAttachment(JIRAConnection connection, GenericArtifact ga,
			String targetParentArtifactId, String userName)
			throws RemoteException {

		
		log.info("An attachment will be created for artifact id "
				+ targetParentArtifactId);
		String contentType = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_TYPE, ga);
		String attachDescription = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_DESCRIPTION, ga);
		String attachmentMimeType = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_MIME_TYPE, ga);
		String attachmentName = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_NAME, ga);
		//attachmentName = userName + "_" + attachmentName;

		GenericArtifact.ArtifactActionValue attAction = ga.getArtifactAction();

		Issue issue = null;

		if (attAction == GenericArtifact.ArtifactActionValue.CREATE) {

			if (AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentMetaData.AttachmentType.DATA) {
				issue = this.attachFileToArtifact(connection,
						targetParentArtifactId, attachDescription,
						attachmentName, attachmentMimeType, ga, null);

			} else if (AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentMetaData.AttachmentType.EMPTY) {
				log.error("Attachment type is unknown, doing nothing");
			}

		} else if (attAction == GenericArtifact.ArtifactActionValue.UNKNOWN) {

			log.error("Attachment action value is unknown");
		}

		if (issue != null) {

			Date attachmentLastModifiedDate = issue.getUpdateDate().toDate();

			com.atlassian.jira.rest.client.domain.Attachment attachment = getLastAttachmentAddByConnectorUser(connection, issue);
			
			if (attachment != null) {
				ga.setTargetArtifactLastModifiedDate(GenericArtifactHelper.df
						.format(attachment.getCreationDate().toDate()));
				ga.setTargetArtifactVersion(String.valueOf(attachment.getCreationDate().getMillis()));
				URI uri=attachment.getSelf();
				ga.setTargetArtifactId(String.valueOf(StringUtils.substringAfterLast(uri.getPath(), "/")));
			} else {
				ga.setTargetArtifactLastModifiedDate(DateUtil
						.format(attachmentLastModifiedDate));
				ga.setTargetArtifactVersion("1");
				ga.setTargetArtifactId(GenericArtifact.VALUE_UNKNOWN);
			}
		}
		return issue;

	}
	
	/**
	 * @param connection
	 * @param issue
	 */
	private com.atlassian.jira.rest.client.domain.Attachment getLastAttachmentAddByConnectorUser(JIRAConnection connection, Issue issue) {
		com.atlassian.jira.rest.client.domain.Attachment attachment = null;
		Iterator<com.atlassian.jira.rest.client.domain.Attachment> it = issue.getAttachments().iterator();
		while (it.hasNext()) {
			com.atlassian.jira.rest.client.domain.Attachment currentAttachment = it.next();
			if (connection.getUsername().equalsIgnoreCase(
					currentAttachment.getAuthor().getName())) {
					attachment=currentAttachment;
				continue;
			}
		}

		return attachment;
	}

	private Issue attachFileToArtifact(JIRAConnection connection,
			String targetParentArtifactId, String attachDescription,
			String attachmentName, String textPlain, GenericArtifact ga,
			byte[] bytes) {

		IssueRestClient issueClient=connection.getJiraRestClient().getIssueClient();
		Issue issue = issueClient.getIssue(targetParentArtifactId, pm);
				

		String attachmentDataFileName = GenericArtifactHelper.getStringGAField(
				AttachmentMetaData.ATTACHMENT_DATA_FILE, ga);

		File file = new File(attachmentDataFileName);
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			
		} catch (FileNotFoundException e) {
			log.error("When attaching a the file to the issue, file not found");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("When attaching a the file to the issue, i/o exception");
			e.printStackTrace();
		}

		issueClient.addAttachment(pm, issue.getAttachmentsUri(),in , attachmentName);
		issue=issueClient.getIssue(targetParentArtifactId, pm);
		
		log.info("file has been attached to the issue "
				+ targetParentArtifactId);
		
		return issue;

	}

/*	private Attachment getAttachmentMetaData(JIRAConnection connection,
			String attachmentName, Date attachmentLastModifiedDate,
			String targetParentArtifactId) {

		Attachment returnedAttachent = null;
		AttachmentCollection attachments = connection.getJiraRestClient().get
		
		Iterator<Attachment> it = attachments.iterator();
		List<Attachment> attachmentList = new ArrayList<Attachment>();

		while (it.hasNext()) {
			Attachment attachment = it.next();
			if (attachment.getFileName().equals(attachmentName)) {
				attachmentList.add(attachment);
			}
		}

		if (attachmentList.size() == 0) {
			log.warn("No attachments match with the name " + attachmentName);
		} else {
			if (attachmentList.size() == 1) {
				returnedAttachent = attachmentList.get(0);
			} else {
				for (int i = attachmentList.size() - 1; i > -1; --i) {

					Attachment attachment = attachmentList.get(0);

					if (attachment.getFileName().equals(attachmentName)) {
						returnedAttachent = attachment;
					}
				}

			}

		}

		return returnedAttachent;
	}*/

}
