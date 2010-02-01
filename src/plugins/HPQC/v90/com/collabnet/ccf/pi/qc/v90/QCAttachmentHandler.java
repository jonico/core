/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.pi.qc.v90;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.IAttachmentFactory;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IBugFactory;
import com.collabnet.ccf.pi.qc.v90.api.IRequirement;
import com.collabnet.ccf.pi.qc.v90.api.IRequirementsFactory;
import com.collabnet.ccf.pi.qc.v90.api.IVersionControl;
import com.jacob.com.ComFailException;

/**
 * The attachment handler class provides support for listing and/or create
 * attachments.
 *
 */
public class QCAttachmentHandler {

	private static final Log log = LogFactory.getLog(QCAttachmentHandler.class);
	GenericArtifact genericArtifact;
	/**
	 * This is the maximum size of the aggregate of attachments allowed to be
	 * shipped in one cycle.
	 */
	private QCGAHelper qcGAHelper = new QCGAHelper();

	/**
	 * Create the attachment for the defect identified by the incoming bugId in
	 * QC
	 *
	 * @param qcc
	 *            The Connection object
	 * @param entityId
	 *            The ID of the defect to which the attachment need to be
	 *            created
	 * @param attachmentName
	 *            The name of the file as attachment found in the source
	 *            system's defect.
	 * @param contentTypeValue
	 *            Indicates if this is a DATA or LINK
	 * @param data
	 *            The byte[] of data
	 * @param attachmentSourceUrl
	 *            Link as attachment containing the exact link found in the
	 *            source system's defect
	 *
	 *
	 */
	public void createAttachmentForDefect(IConnection qcc, String entityId,
			String attachmentName, String contentTypeValue, File attachmentFile,
			String attachmentSourceUrl, String description){
		IBugFactory bugFactory = null;
		IBug bug = null;
		try {
			bugFactory = qcc.getBugFactory();
			bug = bugFactory.getItem(entityId);
			int type = 0;
			if (contentTypeValue.equals(AttachmentMetaData.AttachmentType.DATA
					.toString())) {
				type = 1;
				try {
					if(StringUtils.isEmpty(description)) {
						bug.createNewAttachment(attachmentFile.getAbsolutePath(), type);
					}
					else {
						bug.createNewAttachment(attachmentFile.getAbsolutePath(), description, type);
					}
				} catch (Exception e) {
					String message = "Exception while attaching the file "+
							attachmentFile.getAbsolutePath() +" to defect "+entityId;
					log.error(message, e);
					throw new CCFRuntimeException(message, e);
				}
			} else {
				type = 2;
				if(StringUtils.isEmpty(description)) {
					bug.createNewAttachment(attachmentSourceUrl, type);
				}
				else {
					bug.createNewAttachment(attachmentSourceUrl, description, type);
				}
			}
		}
		finally {
			if(bug != null){
				bug.safeRelease();
				bug = null;
			}
		}

		return;
	}
	
	
	/**
	 * Create the attachment for the requirement identified by the incoming requirement id in
	 * QC
	 *
	 * @param qcc
	 *            The Connection object
	 * @param entityId
	 *            The ID of the requirement to which the attachment need to be
	 *            created
	 * @param attachmentName
	 *            The name of the file as attachment found in the source
	 *            system's defect.
	 * @param contentTypeValue
	 *            Indicates if this is a DATA or LINK
	 * @param data
	 *            The byte[] of data
	 * @param attachmentSourceUrl
	 *            Link as attachment containing the exact link found in the
	 *            source system's defect
	 *
	 *
	 */
	public void createAttachmentForRequirement(IConnection qcc, String entityId,
			String attachmentName, String contentTypeValue, File attachmentFile,
			String attachmentSourceUrl, String description){
		IRequirementsFactory reqFactory = null;
		IRequirement req = null;
		IVersionControl versionControl = null;
		boolean versionControlSupported = false;
		try {
			reqFactory = qcc.getRequirementsFactory();
			req = reqFactory.getItem(entityId);
			versionControl = req.getVersionControlObject();
			if (versionControl != null) {
				try {
					versionControlSupported = versionControl.checkOut("CCF Checkout");
				} catch (ComFailException e) {
					// check whether we have already checked out this requirement
					if (qcc.getUsername().equals(req.getFieldAsString("RQ_VC_CHECKOUT_USER_NAME"))) {
						log.warn("Requirement "+req.getId()+" has been already checked out by connector user "+ qcc.getUsername()+ " so we still proceed ...");
					} else {
						String message = "Requirement "+req.getId()+ " has been checked out by "+req.getFieldAsString("RQ_VC_CHECKOUT_USER_NAME") +
						" on " + req.getFieldAsDate("RQ_VC_CHECKOUT_DATE") +
						" at "+ req.getFieldAsString("RQ_VC_CHECKOUT_TIME") + " with version number " + req.getFieldAsInt("RQ_VC_VERSION_NUMBER"); 
						log.error(message, e);
						throw new CCFRuntimeException(message, e);
					}
				}
			}
			int type = 0;
			if (contentTypeValue.equals(AttachmentMetaData.AttachmentType.DATA
					.toString())) {
				type = 1;
				try {
					if(StringUtils.isEmpty(description)) {
						req.createNewAttachment(attachmentFile.getAbsolutePath(), type);
					}
					else {
						req.createNewAttachment(attachmentFile.getAbsolutePath(), description, type);
					}
				} catch (Exception e) {
					String message = "Exception while attaching the file "+
							attachmentFile.getAbsolutePath() +" to requirement "+entityId;
					log.error(message, e);
					throw new CCFRuntimeException(message, e);
				}
			} else {
				type = 2;
				if(StringUtils.isEmpty(description)) {
					req.createNewAttachment(attachmentSourceUrl, type);
				}
				else {
					req.createNewAttachment(attachmentSourceUrl, description, type);
				}
			}
		}
		finally {
			if (versionControlSupported) {
				try {
					versionControl.checkIn("CCF CheckIn");
					versionControl.safeRelease();
				} catch (Exception e) {
					String message = "Failed to checkin requirement " + req.getId() + " again";
					log.error(message, e);
					throw new CCFRuntimeException(message , e);
				}
			}
			
			if(req != null){
				req.safeRelease();
				req = null;
			}
		}

		return;
	}

	/**
	 * Obtains the artifactAction based on the date at which that defect was
	 * created and the lastReadTime synchronization parameter.
	 *
	 * @param entityId
	 *            The defectId for which the search has to be made in QC
	 * @param actionId
	 *            The transactionId at which it needs to be determined if the
	 *            defect is a create or update.
	 * @param qcc
	 *            The Connection object
	 * @param latestDefectArtifact
	 *            The GenericArtifact into which the artifactAction is populated
	 *            after it is determined.
	 * @param lastReadTime
	 *            This is synchronization parameter used to compare with the
	 *            defect created date and find out the artifactAction.
	 * @return GenericArtifact Updated artifact
	 */
//	public GenericArtifact getArtifactdAction(
//			GenericArtifact latestDefectArtifact, IConnection qcc,
//			String actionId, int entityId, String lastReadTime) {
//
//		Date lastReadDate = DateUtil.parse(lastReadTime);
////		Date createdOn = qcGAHelper.getDefectCreatedDate(qcc, entityId);
////		if (createdOn != null && createdOn.after(lastReadDate))
////			latestDefectArtifact
////					.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
////		else
////			latestDefectArtifact
////					.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
//
//		String bgVts = qcGAHelper.findBgVtsFromQC(qcc, Integer.parseInt(actionId),
//				entityId);
//		Date newBgVts = DateUtil.parseQCDate(bgVts);
//		String lastModifiedDate = DateUtil.format(newBgVts);
//		latestDefectArtifact
//				.setSourceArtifactLastModifiedDate(lastModifiedDate);
//		log.debug("The newBgVts=" + newBgVts + ", and lastReadDate="
//				+ lastReadDate);
//		return latestDefectArtifact;
//	}

	/**
	 * Used by the QCReader's getArtifactAttachments which is an implementation
	 * of the AbstractReader method. This method gets the attachment data as
	 * c(s) for a given artifact id after a particular transaction.
	 *
	 * @param modifiedAttachmentArtifacts
	 *            This list of GenericArtifacts is used to store the obtained
	 *            attachments as GenericArtifacts and returned
	 * @param qcc
	 * @param connectorUser
	 * @param transactionId
	 * @param lastReadTime
	 * @param sourceArtifactId
	 * @param sourceRepositoryId
	 * @param sourceRepositoryKind
	 * @param sourceSystemId
	 * @param sourceSystemKind
	 * @param targetRepositoryId
	 * @param targetRepositoryKind
	 * @param targetSystemId
	 * @param targetSystemKind
	 * @param shouldShipAttachmentsWithArtifact
	 * @param isDefectRepository 
	 * @return
	 * @throws RemoteException
	 */
	@SuppressWarnings("unchecked")
	public List<GenericArtifact> getLatestChangedAttachments(
			List<GenericArtifact> modifiedAttachmentArtifacts, IConnection qcc,
			String connectorUser, String resyncUser, String transactionId, String lastReadTime,
			String sourceArtifactId, String sourceRepositoryId,
			String sourceRepositoryKind, String sourceSystemId,
			String sourceSystemKind, String targetRepositoryId,
			String targetRepositoryKind, String targetSystemId,
			String targetSystemKind, long maxAttachmentSizePerArtifact,
			boolean shouldShipAttachmentsWithArtifact, boolean isDefectRepository)
			throws Exception {

		String artifactId = sourceArtifactId;
		List<Object> transactionIdAndAttachOperation = null;
		if (isDefectRepository) {
			transactionIdAndAttachOperation = qcGAHelper.getTxnIdAndAuDescriptionForDefect(
					artifactId, transactionId, qcc, connectorUser, resyncUser == null?"":resyncUser);
		} else {
			transactionIdAndAttachOperation = qcGAHelper.getTxnIdAndAuDescriptionForRequirement(
					artifactId, transactionId, qcc, connectorUser, resyncUser == null?"":resyncUser);
		}
		if (transactionIdAndAttachOperation == null)
			return modifiedAttachmentArtifacts;
		String thisTransactionId = (String) transactionIdAndAttachOperation
				.get(0);
		Map<String, Map<String, String>> attachmentNames = (Map<String, Map<String, String>>) transactionIdAndAttachOperation
				.get(1);
		Map<String, Map<String, String>> deletedAttachmentNames = (Map<String, Map<String, String>>) transactionIdAndAttachOperation
				.get(2);
//		log.debug("In getLatestChangedDefects, txnId=" + thisTransactionId
//				+ " and attachmentNames=" + attachmentNames);

		if (attachmentNames != null) {
			for (Entry<String, Map<String, String>> entry:attachmentNames.entrySet()) {
				String attachmentName = entry.getKey();
				GenericArtifact latestAttachmentArtifact = getGenericArtifactObjectOfAttachment(
						qcc, artifactId, attachmentName,
						maxAttachmentSizePerArtifact, false, null,
						shouldShipAttachmentsWithArtifact, isDefectRepository);
				if (latestAttachmentArtifact == null)
					continue;
				Map<String, String> versionDetails = entry.getValue();
				//String attTransactionId = versionDetails.get("AU_ACTION_ID");
				String attDate = versionDetails.get("AU_TIME");
				Date attLastModifiedDate = DateUtil.parseQCDate(attDate);
				latestAttachmentArtifact.setSourceArtifactLastModifiedDate(
						DateUtil.format(attLastModifiedDate));
//				latestAttachmentArtifact = getArtifactAction(
//						latestAttachmentArtifact, qcc, thisTransactionId,
//						entityId, lastReadTime);
				latestAttachmentArtifact
						.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
				latestAttachmentArtifact
						.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
				latestAttachmentArtifact.setErrorCode("ok");
				latestAttachmentArtifact
						.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
				latestAttachmentArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);

				latestAttachmentArtifact = assignValues(
						latestAttachmentArtifact, sourceArtifactId,
						sourceRepositoryId, sourceRepositoryKind,
						sourceSystemId, sourceSystemKind, targetRepositoryId,
						targetRepositoryKind, targetSystemId, targetSystemKind,
						thisTransactionId);

				modifiedAttachmentArtifacts.add(latestAttachmentArtifact);
			}
		}
		if (deletedAttachmentNames != null) {
			for (Entry<String, Map<String, String>> entry:deletedAttachmentNames.entrySet()) {
				String deleteAttachmentName = entry.getKey();
				Map<String, String> versionDetails = entry.getValue();
				String attTransactionId = versionDetails.get("AU_ACTION_ID");


				GenericArtifact latestAttachmentArtifact = getGenericArtifactObjectOfAttachment(
						qcc, artifactId, deleteAttachmentName,
						maxAttachmentSizePerArtifact, true, attTransactionId, false, isDefectRepository);
				if (latestAttachmentArtifact == null)
					continue;
//				latestAttachmentArtifact = getArtifactAction(
//						latestAttachmentArtifact, qcc, thisTransactionId,
//						entityId, lastReadTime);
				String attDate = versionDetails.get("AU_TIME");
				Date attLastModifiedDate = DateUtil.parseQCDate(attDate);
				latestAttachmentArtifact.setSourceArtifactLastModifiedDate(
						DateUtil.format(attLastModifiedDate));
				latestAttachmentArtifact
						.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
				latestAttachmentArtifact
						.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
				latestAttachmentArtifact.setErrorCode("ok");
				latestAttachmentArtifact
						.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
				latestAttachmentArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.DELETE);
				latestAttachmentArtifact = assignValues(
						latestAttachmentArtifact, sourceArtifactId,
						sourceRepositoryId, sourceRepositoryKind,
						sourceSystemId, sourceSystemKind, targetRepositoryId,
						targetRepositoryKind, targetSystemId, targetSystemKind,
						thisTransactionId);

				modifiedAttachmentArtifacts.add(latestAttachmentArtifact);
			}
		}
		// }
		return modifiedAttachmentArtifacts;
	}
	/**
	 * Assigns values of the incoming parameters to the incoming genericArtifact
	 * and returns the updated one.
	 *
	 * @param latestDefectArtifact
	 *            The GenericArtifact to which the following values need to be
	 *            assigned.
	 * @param sourceArtifactId
	 * @param sourceRepositoryId
	 * @param sourceRepositoryKind
	 * @param sourceSystemId
	 * @param sourceSystemKind
	 * @param targetRepositoryId
	 * @param targetRepositoryKind
	 * @param targetSystemId
	 * @param targetSystemKind
	 * @param thisTransactionId
	 * @return Assigned GenericArtifact
	 */
	public GenericArtifact assignValues(GenericArtifact latestDefectArtifact,
			String sourceArtifactId, String sourceRepositoryId,
			String sourceRepositoryKind, String sourceSystemId,
			String sourceSystemKind, String targetRepositoryId,
			String targetRepositoryKind, String targetSystemId,
			String targetSystemKind, String thisTransactionId) {

		latestDefectArtifact.setDepParentSourceArtifactId(sourceArtifactId);
		latestDefectArtifact.setDepParentSourceRepositoryId(sourceRepositoryId);
		latestDefectArtifact.setDepParentSourceRepositoryKind(sourceRepositoryKind);
		latestDefectArtifact.setDepParentTargetRepositoryId(targetRepositoryId);
		latestDefectArtifact.setDepParentTargetRepositoryKind(targetRepositoryKind);

		latestDefectArtifact.setSourceRepositoryId(sourceRepositoryId);
		latestDefectArtifact.setSourceRepositoryKind(sourceRepositoryKind);
		latestDefectArtifact.setSourceSystemId(sourceSystemId);
		latestDefectArtifact.setSourceSystemKind(sourceSystemKind);

		latestDefectArtifact.setTargetRepositoryId(targetRepositoryId);
		latestDefectArtifact.setTargetRepositoryKind(targetRepositoryKind);
		latestDefectArtifact.setTargetSystemId(targetSystemId);
		latestDefectArtifact.setTargetSystemKind(targetSystemKind);
		latestDefectArtifact.setSourceArtifactVersion(thisTransactionId);

		return latestDefectArtifact;
	}

	/**
	 * Constructs the GenericArtifact Java object for the attachment after
	 * getting the schema from getSchemaAttachment method It also populates all
	 * the values into the attachment artifact.
	 *
	 * @param qcc
	 * @param entityId
	 * @param attachmentName
	 * @param deletedAttachments
	 * @param deleteTransactionId
	 * @param shouldShipAttachmentsWithArtifact
	 * @param isDefectRepository 
	 * @return GenericArtifact Containing all the field values.
	 */
	public GenericArtifact getGenericArtifactObjectOfAttachment(
			IConnection qcc, String entityId, String attachmentName,
			long maxAttachmentSizePerArtifact, boolean deletedAttachments, String deleteTransactionId,
			boolean shouldShipAttachmentsWithArtifact, boolean isDefectRepository) {
		long attachmentSize = 0;
		String thisMimeType = null;
		if (attachmentName != null) {
			genericArtifact = getSchemaAttachment(qcc, entityId, attachmentName, deletedAttachments, deleteTransactionId);
		}
		if (genericArtifact == null)
			return null;
		if(!deletedAttachments) {


			MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap();
			if (attachmentName != null)
				thisMimeType = mimeType.getContentType(attachmentName);
			byte data[] = null;
			File qcAttachmentFile = null;
			String contentType = (String) genericArtifact
					.getAllGenericArtifactFieldsWithSameFieldName(AttachmentMetaData.ATTACHMENT_TYPE)
					.get(0).getFieldValue().toString();
			List<GenericArtifactField> allFields = genericArtifact
					.getAllGenericArtifactFields();
			int noOfFields = allFields.size();
			for (int cnt = 0; cnt < noOfFields; cnt++) {
				GenericArtifactField thisField = allFields.get(cnt);
				thisField
						.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				// thisField.setFieldDisplayName(thisField.getFieldName());
				thisField.setFieldValueHasChanged(true);
				if (thisField.getFieldName().equals(
						AttachmentMetaData.ATTACHMENT_TYPE)) {
					if (contentType.equals("DATA")) {
						thisField
								.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
						if(!deletedAttachments) {
							if (isDefectRepository) {
								IBugFactory bugFactory = null;
								IBug bug = null;
								try {
									bugFactory = qcc.getBugFactory();
									bug = bugFactory.getItem(entityId);
									qcAttachmentFile = bug.retrieveAttachmentData(attachmentName);
								} catch (Exception e) {
									String message = "An Exception occured in QCAttachmentHandler.getGenericArtifactObjectOfAttachment"
										+ " while trying to do retrieveAttachmentData on Filename "+attachmentName;
									log
											.error(message, e);
									throw new CCFRuntimeException(message, e);
								}
								finally {
									bugFactory = null;
									if(bug != null){
										bug.safeRelease();
									}
								}
							} else {
								IRequirementsFactory reqFactory = null;
								IRequirement req = null;
								try {
									reqFactory = qcc.getRequirementsFactory();
									req = reqFactory.getItem(entityId);
									qcAttachmentFile = req.retrieveAttachmentData(attachmentName);
								} catch (Exception e) {
									String message = "An Exception occured in QCAttachmentHandler.getGenericArtifactObjectOfAttachment"
										+ " while trying to do retrieveAttachmentData on Filename "+attachmentName;
									log
											.error(message, e);
									throw new CCFRuntimeException(message, e);
								}
								finally {
									reqFactory = null;
									if(req != null){
										req.safeRelease();
									}
								}
							}
							if(qcAttachmentFile == null) {
								return null;
							}
							attachmentSize = qcAttachmentFile.length();

							if (attachmentSize > maxAttachmentSizePerArtifact) {
								log.warn("The attachment "+ attachmentName
												+ " is bigger than our maxAttachmentSizePerArtifact, so cannot ship.");
								return null;
							}
							else{
								if(!shouldShipAttachmentsWithArtifact){
									File tempFile = null;
									try {
										if(!qcAttachmentFile.exists()){
											log.warn("Downloaded attachment is not valid");
											return null;
										}
										tempFile = File.createTempFile("QC_Attachment", "file");
										boolean renamingSuccessful = qcAttachmentFile.renameTo(tempFile);
										if(!renamingSuccessful){
											tempFile = qcAttachmentFile;
										}
										String attachmentDataFileName = tempFile.getAbsolutePath();
										GenericArtifactField attachmentDataFileField =
											genericArtifact.addNewField(AttachmentMetaData.ATTACHMENT_DATA_FILE,
												GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
										attachmentDataFileField.setFieldValueType(
												GenericArtifactField.FieldValueTypeValue.STRING);
										attachmentDataFileField.setFieldAction(FieldActionValue.REPLACE);
										attachmentDataFileField.setFieldValue(attachmentDataFileName);
										log.debug("Shipping the reference of the attachment "+attachmentDataFileName);
										if(tempFile.length() == 0){
											log.warn("The file "+attachmentDataFileName+" does not contain any data.");
										}
										data = null;
									} catch (IOException e) {
										String message = "Could not write attachment content to temp file."
											+" Shipping the attachment with the artifact.";
										log.error(message, e);
										throw new CCFRuntimeException(message, e);
									}
								}
								else {
									if(!qcAttachmentFile.exists()){
										log.warn("Downloaded attachment is not valid");
										return null;
									}
									ByteArrayOutputStream baOS = new ByteArrayOutputStream();
									FileInputStream fis = null;
									try {
										fis = new FileInputStream(qcAttachmentFile);
										int readCount = 0;
										byte[] tmpData = new byte[1024*3];
										if((readCount = fis.read(tmpData)) != -1){
											baOS.write(tmpData, 0, readCount);
										}
										data = baOS.toByteArray();
										fis.close();
										baOS.close();
									} catch (FileNotFoundException e) {
										String message = "Could not read attachment content."
											+" File not found "+qcAttachmentFile.getAbsolutePath();
										log.error(message, e);
										throw new CCFRuntimeException(message, e);
									} catch (IOException e) {
										String message = "Could not read attachment content."
											+" IOException while reading "+qcAttachmentFile.getAbsolutePath();
										log.error(message, e);
										throw new CCFRuntimeException(message, e);
									}
								}
							}
						}
					}
				}
				if (thisField.getFieldName().equals(
						AttachmentMetaData.getAttachmentName())) {
					if (contentType.equals("DATA")) {
						String prefix = (isDefectRepository ? "BUG_":"REQ_")+entityId+"_";
						String tmpAttachmentName =
							attachmentName.substring(attachmentName.indexOf(prefix)+prefix.length());
						thisField.setFieldValue(tmpAttachmentName);
					}
					else
						thisField.setFieldValue("Unknown");
					thisField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				}
				else if (thisField.getFieldName().equals(
						AttachmentMetaData.getAttachmentSize())) {
					thisField.setFieldValue(attachmentSize);
					thisField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.INTEGER);
				}
				else if (thisField.getFieldName().equals(
						AttachmentMetaData.getAttachmentSourceUrl())) {
					if (contentType.equals("DATA"))
						thisField.setFieldValue("Unknown");
					else
						thisField.setFieldValue(attachmentName);
					thisField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				}
				else if (thisField.getFieldName().equals(
						AttachmentMetaData.getAttachmentMimeType())) {
					thisField.setFieldValue(thisMimeType);
					thisField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				}
				else if (thisField.getFieldName().equals(
						AttachmentMetaData.getAttachmentValueIsNull())) {
					if (data != null)
						thisField
								.setFieldValue(AttachmentMetaData.AttachmentValueIsNull.FALSE);
					else
						thisField
								.setFieldValue(AttachmentMetaData.AttachmentValueIsNull.TRUE);
					thisField
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				}
			}
			if (data != null) {
				genericArtifact.setRawAttachmentData(data);
			}
		}
		return genericArtifact;
	}

	/*
	 * public boolean checkForAttachmentSize( List<GenericArtifactAttachment>
	 * allAttachments) {
	 *
	 * for (int cnt = 0; cnt < allAttachments.size(); cnt++) { long
	 * thisAttachmentSize = allAttachments.get(cnt) .getAttachmentSize();
	 * cumulativeAttachmentSize += thisAttachmentSize; } if
	 * (cumulativeAttachmentSize < maxAttachmentSizePerCycle) return true; else
	 * return false; }
	 */

	/**
	 * Creates the schema for the attachment artifact based on the fields that
	 * need to be added from the attachmentMetaData. It also initializes the
	 * attachmentId, attachmentType and attachmentDescription
	 *
	 * @param qcc
	 * @param entityId
	 * @param attachmentName
	 * @param deletedAttachments
	 * @param deleteTransactionId
	 * @return
	 */
	public GenericArtifact getSchemaAttachment(IConnection qcc,
			String entityId, String attachmentName, boolean deletedAttachments, String deleteTransactionId) {

		GenericArtifact genericArtifact = new GenericArtifact();
		String[] attachmentMetaData = AttachmentMetaData
				.getAttachmentMetaData();
		if (attachmentName != null) {
			List<String> attachmentIdAndType = null;
			if(!deletedAttachments){
				attachmentIdAndType = qcGAHelper.getFromTable(qcc, entityId,
						attachmentName);
				if (attachmentIdAndType != null) {
					String attachmentId = attachmentIdAndType.get(0); // CR_REF_ID
					String attachmentType = attachmentIdAndType.get(1); // CR_REF_TYPE
					String attachmentDescription = attachmentIdAndType.get(2); // CR_DESCRIPTION

					genericArtifact.setSourceArtifactId(attachmentId);

					for (int cnt = 0; cnt < attachmentMetaData.length; cnt++) {

						GenericArtifactField field;
						field = genericArtifact.addNewField(
								attachmentMetaData[cnt],
								GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);

						field
								.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
						if (attachmentMetaData[cnt].equals(AttachmentMetaData
								.getAttachmentType())) {
							if (attachmentType.equals("File"))
								field
										.setFieldValue(AttachmentMetaData.AttachmentType.DATA);
							else
								field
										.setFieldValue(AttachmentMetaData.AttachmentType.LINK);
							field
									.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
						}
						if (attachmentMetaData[cnt].equals(AttachmentMetaData
								.getAttachmentDescription())) {
							field.setFieldValue(attachmentDescription);
							field
									.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
						}
					}
				} else {
					return null;
				}
			}
			else {
				String deletedAttachmentId = qcGAHelper.getDeletedAttachmentId(qcc, entityId,
						attachmentName, deleteTransactionId);
				genericArtifact.setSourceArtifactId(deletedAttachmentId);
				GenericArtifactField field = genericArtifact.addNewField(
						AttachmentMetaData.ATTACHMENT_NAME,
						GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
			}

		}
		return genericArtifact;

	}

	public void deleteAttachmentForDefect(IConnection qcc, String bugId, String attachmentId) {
		// TODO Validate if the attachment exists before deleting
		// The QC API does not indicate whether it deleted the attachment or not.
		IBugFactory bugFactory = null;
		IBug bug = null;
		IAttachmentFactory attachmentFactory = null;
		try {
			bugFactory = qcc.getBugFactory();
			bug = bugFactory.getItem(bugId);
			attachmentFactory = bug.getAttachmentFactory();
			attachmentFactory.removeItem(attachmentId);
		}
		finally {		
			if (attachmentFactory != null) {
				attachmentFactory.safeRelease();
				attachmentFactory = null;
			}
			
			if(bug != null){
				bug.safeRelease();
				bug = null;
			}
		}
	}
	
	public void deleteAttachmentForRequirement(IConnection qcc, String defectId, String attachmentId) {
		// TODO Validate if the attachment exists before deleting
		// The QC API does not indicate whether it deleted the attachment or not.
		IRequirementsFactory reqFactory = null;
		IRequirement req = null;
		IAttachmentFactory attachmentFactory = null;
		IVersionControl versionControl = null;
		boolean versionControlSupported = false;
		try {
			reqFactory = qcc.getRequirementsFactory();
			req = reqFactory.getItem(defectId);
			
			versionControl = req.getVersionControlObject();
			if (versionControl != null) {
				try {
					versionControlSupported = versionControl.checkOut("CCF Checkout");
				} catch (ComFailException e) {
					// check whether we have already checked out this requirement
					if (qcc.getUsername().equals(req.getFieldAsString("RQ_VC_CHECKOUT_USER_NAME"))) {
						log.warn("Requirement "+req.getId()+" has been already checked out by connector user "+ qcc.getUsername()+ " so we still proceed ...");
					} else {
						String message = "Requirement "+req.getId()+ " has been checked out by "+req.getFieldAsString("RQ_VC_CHECKOUT_USER_NAME") +
						" on " + req.getFieldAsDate("RQ_VC_CHECKOUT_DATE") +
						" at "+ req.getFieldAsString("RQ_VC_CHECKOUT_TIME") + " with version number " + req.getFieldAsInt("RQ_VC_VERSION_NUMBER"); 
						log.error(message, e);
						throw new CCFRuntimeException(message, e);
					}
				}
			}
			attachmentFactory = req.getAttachmentFactory();
			attachmentFactory.removeItem(attachmentId);
		}
		finally {
			if (versionControlSupported) {
				try {
					versionControl.checkIn("CCF CheckIn");
					versionControl.safeRelease();
				} catch (Exception e) {
					String message = "Failed to checkin requirement " + req.getId() + " again";
					log.error(message, e);
					throw new CCFRuntimeException(message , e);
				}
			}
			
			if (attachmentFactory != null) {
				attachmentFactory.safeRelease();
				attachmentFactory = null;
			}
			if(req != null){
				req.safeRelease();
				req = null;
			}
		}
	}
}
