/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.teamforge;

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

import org.apache.axis.AxisFault;
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
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.PlanningFolderRuleViolationException;
import com.collabnet.teamforge.api.main.AttachmentList;
import com.collabnet.teamforge.api.main.AttachmentRow;
import com.collabnet.teamforge.api.tracker.ArtifactDO;

/**
 * This class is responsible for retrieving attachment data for an artifact from
 * a Source TF repository.
 * 
 * @author Johannes Nicolai
 */
public class TFAttachmentHandler {
    /**
     * log4j logger instance
     */
    private static final Log              log                  = LogFactory
                                                                       .getLog(TFAttachmentHandler.class);
    private static final Log              logConflictResolutor = LogFactory
                                                                       .getLog("com.collabnet.ccf.core.conflict.resolution");

    private ConnectionManager<Connection> connectionManager    = null;

    /**
     * Class constructor.
     * 
     * @param serverUrl
     *            Soap server URL.
     * @param connectionManager
     */
    public TFAttachmentHandler(String serverUrl,
            ConnectionManager<Connection> connectionManager) {
        this.connectionManager = connectionManager;
        // TODO Implement custom retry mechanism again?
    }

    /**
     * This method uploads the file and gets the new file descriptor returned
     * from the TF system. It then associates the file descriptor to the
     * artifact there by adding the attachment to the artifact.
     * 
     * @param sessionId
     *            - The current session id
     * @param artifactId
     *            - The artifact's id to which the attachment should be added.
     * @param comment
     *            - Comment for the attachment addition
     * @param fileName
     *            - Name of the file that is attached to this artifact
     * @param mimeType
     *            - MIME type of the file that is being attached.
     * @param att
     *            - the file content
     * @param linkUrl
     * 
     * @throws RemoteException
     *             - if any SOAP api call fails
     * @throws PlanningFolderRuleViolationException
     */
    public ArtifactDO attachFileToArtifact(Connection connection,
            String artifactId, String comment, String fileName,
            String mimeType, GenericArtifact att, byte[] linkUrl)
            throws RemoteException, PlanningFolderRuleViolationException {
        ArtifactDO soapDo = null;
        String attachmentDataFileName = GenericArtifactHelper.getStringGAField(
                AttachmentMetaData.ATTACHMENT_DATA_FILE, att);
        boolean retryCall = true;
        while (retryCall) {
            retryCall = false;
            String fileDescriptor = null;
            try {
                byte[] data = null;
                if (StringUtils.isEmpty(attachmentDataFileName)) {
                    if (linkUrl == null) {
                        data = att.getRawAttachmentData();
                    } else {
                        data = linkUrl;
                    }
                    fileDescriptor = connection.getFileStorageClient()
                            .startFileUpload();
                    connection.getFileStorageClient().write(fileDescriptor,
                            data);
                    connection.getFileStorageClient().endFileUpload(
                            fileDescriptor);
                } else {
                    try {
                        DataSource dataSource = new FileDataSource(new File(
                                attachmentDataFileName));
                        DataHandler dataHandler = new DataHandler(dataSource);
                        fileDescriptor = connection.getFileStorageClient()
                                .uploadFile(dataHandler);
                    } catch (IOException e) {
                        String message = "Exception while uploading the attachment "
                                + attachmentDataFileName;
                        log.error(message, e);
                        throw new CCFRuntimeException(message, e);
                    }
                }

                soapDo = connection.getTrackerClient().getArtifactData(
                        artifactId);
                boolean fileAttached = true;
                while (fileAttached) {
                    try {
                        fileAttached = false;
                        connection.getTrackerClient().setArtifactData(soapDo,
                                comment, fileName, mimeType, fileDescriptor);
                    } catch (AxisFault e) {
                        javax.xml.namespace.QName faultCode = e.getFaultCode();
                        if (!faultCode.getLocalPart().equals(
                                "VersionMismatchFault")) {
                            throw e;
                        }
                        logConflictResolutor
                                .warn("Stale attachment update, trying again ...:",
                                        e);
                        soapDo = connection.getTrackerClient().getArtifactData(
                                artifactId);
                        fileAttached = true;
                    }
                }
            } catch (AxisFault e) {
                javax.xml.namespace.QName faultCode = e.getFaultCode();
                if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
                    throw e;
                }
                if (connectionManager.isEnableReloginAfterSessionTimeout()
                        && (!connectionManager
                                .isUseStandardTimeoutHandlingCode())) {
                    log.warn(
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

    public void deleteAttachment(Connection connection, String attachmentId,
            String artifactId, GenericArtifact att) throws RemoteException {
        connection.getTeamForgeClient().deleteAttachment(artifactId,
                attachmentId);
        ArtifactDO artifact = connection.getTrackerClient().getArtifactData(
                artifactId);
        if (artifact != null) {
            Date attachmentLastModifiedDate = artifact.getLastModifiedDate();
            att.setTargetArtifactLastModifiedDate(DateUtil
                    .format(attachmentLastModifiedDate));
            att.setTargetArtifactVersion(Integer.toString(artifact.getVersion()));
        }
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
    public byte[] getAttachmentData(Connection connection, String fileId,
            long size, String folderId,
            boolean shouldShipAttachmentsWithArtifact, GenericArtifact ga)
            throws RemoteException {
        boolean retryCall = true;
        byte[] data = null;
        while (retryCall) {
            retryCall = false;
            DataHandler dataHandler = connection.getFileStorageClient()
                    .downloadFileDirect(folderId, fileId);
            BufferedInputStream is = null;
            try {
                is = new BufferedInputStream(dataHandler.getInputStream());
                if (shouldShipAttachmentsWithArtifact) {
                    // TODO not a safe cast here...
                    data = new byte[(int) size];
                    int readLength = is.read(data);
                    if (readLength == size) {
                        // Good that we read all the data
                    } else {
                        // FIXME What if the attachment's size has been changed
                        // in
                        // the mean time?
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
                    data = null;
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

    /**
     * This method decodes the attachment data from the incoming GenericArtifact
     * object and adds to the target TF system tracker's artifact.
     * 
     * @param sessionId
     * @param att
     * @param artifactId
     * @param userName
     * @param sourceForgeSoap
     * @throws RemoteException
     * @throws PlanningFolderRuleViolationException
     */
    public void handleAttachment(Connection connection, GenericArtifact att,
            String artifactId, String userName) throws RemoteException,
            PlanningFolderRuleViolationException {
        log.info("An attachment will be created for artifact id " + artifactId);
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
        ArtifactDO artifact = null;
        if (attAction == GenericArtifact.ArtifactActionValue.CREATE) {
            if (AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentMetaData.AttachmentType.DATA) {
                artifact = this.attachFileToArtifact(connection, artifactId,
                        attachDescription, attachmentName, attachmentMimeType,
                        att, null);
            } else if (AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentMetaData.AttachmentType.LINK) {
                attachmentName = attachmentName + "link.txt";
                artifact = this.attachFileToArtifact(connection, artifactId,
                        attachDescription, attachmentName,
                        AttachmentMetaData.TEXT_PLAIN, att,
                        attachmentURL.getBytes());
            } else if (AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentMetaData.AttachmentType.EMPTY) {
                // TODO What should I do now?
            }
            // else if(contentType ==
            // AttachmentMetaData.AttachmentType.UNKNOWN){
            // //TODO What should I do now?
            // }
        } else if (attAction == GenericArtifact.ArtifactActionValue.UNKNOWN) {
            // TODO What should be done if attachment action value is unknown
            log.error("Attachment action value is unknown");
        }
        if (artifact != null) {
            Date attachmentLastModifiedDate = artifact.getLastModifiedDate();
            AttachmentRow attachmentRow = getAttachmentMetaData(connection,
                    attachmentName, attachmentLastModifiedDate, artifactId);
            if (attachmentRow != null) {
                att.setTargetArtifactLastModifiedDate(DateUtil
                        .format(attachmentRow.getDateCreated()));
                att.setTargetArtifactVersion("1");
                att.setTargetArtifactId(attachmentRow.getAttachmentId());
            } else {
                att.setTargetArtifactLastModifiedDate(DateUtil
                        .format(attachmentLastModifiedDate));
                att.setTargetArtifactVersion("1");
                att.setTargetArtifactId(GenericArtifact.VALUE_UNKNOWN);
            }
        }
    }

    /**
     * This method retrieves all the attachments for all the artifacts present
     * in the artifactRows Set and encoded them into GenericArtifact attachment
     * format.
     * 
     * @param sessionId
     * @param lastModifiedDate
     * @param username
     *            artifacts from this user will be filtered out
     * @param resyncUsername
     *            artifacts from this user will be filtered out
     * @param artifactRows
     * @param sourceForgeSoap
     * @param maxAttachmentSizePerArtifact
     * @param artifactData
     * @return
     * @throws RemoteException
     */
    public List<GenericArtifact> listAttachments(Connection connection,
            Date lastModifiedDate, String username, String resyncUsername,
            List<String> artifactIds, long maxAttachmentSizePerArtifact,
            boolean shouldShipAttachmentsWithArtifact,
            GenericArtifact artifactData) throws RemoteException {
        List<GenericArtifact> attachmentGAs = new ArrayList<GenericArtifact>();
        for (String artifactId : artifactIds) {
            // String folderId = artifact.getFolderId();
            AttachmentList attachmentsList = connection.getTeamForgeClient()
                    .listAttachments(artifactId);
            AttachmentRow[] attachmentRows = attachmentsList.getDataRows();
            for (AttachmentRow row : attachmentRows) {
                String fileName = row.getFileName();
                String attachmentSizeStr = row.getFileSize();
                // String attachmentId = row.getStoredFileId();
                long attachmentSize = Long.parseLong(attachmentSizeStr);
                // FIXME: why not username.equalsIgnoreCase(row.getCreatedBy()) ?
                // Because this only works for TF >= 5.3
                if (fileName.startsWith(username + "_")) {
                    continue;
                }
                if (resyncUsername != null
                        && fileName.startsWith(resyncUsername + "_")) {
                    continue;
                }
                if (attachmentSize > maxAttachmentSizePerArtifact) {
                    log.warn("attachment size is more than the configured maxAttachmentSizePerArtifact "
                            + attachmentSizeStr);
                    continue;
                }
                Date createdDate = row.getDateCreated();
                if (createdDate.after(lastModifiedDate)) {
                    GenericArtifact ga = new GenericArtifact();
                    ga.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
                    ga.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);

                    ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
                    ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
                    ga.setDepParentSourceArtifactId(artifactId);
                    ga.setSourceArtifactId(row.getAttachmentId());
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
                    //gaAttachment.setAttachmentAction(GenericArtifactAttachment
                    // .AttachmentActionValue.CREATE);

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
                    attachmentData = this.getAttachmentData(connection,
                            row.getRawFileId(),
                            Long.parseLong(row.getFileSize()), artifactId,
                            shouldShipAttachmentsWithArtifact, ga);
                    if (shouldShipAttachmentsWithArtifact) {
                        if (attachmentData != null) {
                            ga.setRawAttachmentData(attachmentData);
                        }
                    }
                    attachmentGAs.add(ga);
                }
            }
        }
        return attachmentGAs;
    }

    private AttachmentRow getAttachmentMetaData(Connection connection,
            String fileName, Date createdDate, String artifactId)
            throws RemoteException {
        AttachmentList attachmentsList = connection.getTeamForgeClient()
                .listAttachments(artifactId);
        AttachmentRow[] attachmentRows = attachmentsList.getDataRows();
        AttachmentRow returnRow = null;
        // TODO We assume that attachments are ordered according to their creation date
        // and do not use the passed in argument
        for (int i = attachmentRows.length - 1; i > -1; --i) {
            AttachmentRow row = attachmentRows[i];
            if (row.getFileName().equals(fileName)) {
                returnRow = row;
            }
        }
        if (returnRow == null) {
            log.warn("No attachments match with the name " + fileName);
        }
        return returnRow;
    }
}
