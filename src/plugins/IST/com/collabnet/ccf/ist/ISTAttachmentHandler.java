package com.collabnet.ccf.ist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.AttachmentMetaData.AttachmentType;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.utils.DateUtil;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocumentVersion;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteFilter;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportDocumentOpenFileServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocumentVersion;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteSort;

public class ISTAttachmentHandler {

    private static final Log  log = LogFactory
            .getLog(ISTAttachmentHandler.class);

    private static DateFormat df  = GenericArtifactHelper.df;

    /**
     * return all attachments associated with the artifact and modifed after
     * lastModifiedDate
     *
     * @param connection
     * @param lastModifiedDate
     * @param artifactData
     * @param shouldShipAttachmentsWithArtifact
     * @return List of GAs
     */
    public List<GenericArtifact> getAttachmentsForIncident(
            ISTConnection connection, Date lastModifiedDate,
            GenericArtifact artifactData,
            boolean shouldShipAttachmentsWithArtifact) {

        List<GenericArtifact> attachments = new ArrayList<GenericArtifact>();

        try {

            RemoteIncident inc = connection.getService().incidentRetrieveById(
                    Integer.valueOf(artifactData.getSourceArtifactId()));

            ArrayOfRemoteDocument documents = connection.getService()
                    .documentRetrieveForArtifact(
                            inc.getArtifactTypeId(),
                            inc.getIncidentId().getValue(),
                            new ArrayOfRemoteFilter(),
                            new RemoteSort());

            for (RemoteDocument doc : documents.getRemoteDocument()) {
                Date docDate = ISTHandler.toDate(doc.getEditedDate());
                // use doc.getEditedDate() gives the correct date.
                Date lastDate = docDate;
                int lastId = doc.getAttachmentId().getValue();
                String lastName = doc.getFilenameOrUrl().getValue();

                log.trace(String.format(
                        "%4d.%-4d `%s` has %s and was uploaded on %s",
                        inc.getIncidentId().getValue(),
                        doc.getAttachmentId().getValue(),
                        doc.getFilenameOrUrl().getValue(),
                        doc.getVersions().isNil() ? "no versions" : doc
                                .getVersions().getValue()
                                .getRemoteDocumentVersion().size()
                                + " versions",
                                df.format(docDate)));

                if (!doc.getVersions().isNil()) {
                    ArrayOfRemoteDocumentVersion docVersions = doc
                            .getVersions().getValue();
                    // versions are contained if I retrieve the doc by ID
                    for (RemoteDocumentVersion dv : docVersions
                            .getRemoteDocumentVersion()) {
                        Date versionDate = ISTHandler
                                .toDate(dv.getUploadDate());
                        log.trace(String.format(
                                "Version %s uploaded on %s",
                                dv.getVersionNumber().getValue(),
                                df.format(versionDate)));
                        if (versionDate.after(lastDate)) {
                            lastDate = versionDate;
                            lastId = dv.getAttachmentId();
                            lastName = dv.getFilenameOrUrl().getValue();
                        }
                    }
                }

                if (lastDate.after(lastModifiedDate)) {
                    byte[] data = connection.getService().documentOpenFile(
                            lastId);
                    GenericArtifact ga = new GenericArtifact();
                    ga.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
                    ga.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
                    ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
                    ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
                    ga.setDepParentSourceArtifactId(String.valueOf(inc
                            .getIncidentId().getValue()));
                    ga.setSourceArtifactId(String.valueOf(lastId));

                    if (artifactData != null) {
                        ga.setSourceArtifactVersion(artifactData
                                .getSourceArtifactVersion());
                        ga.setSourceArtifactLastModifiedDate(artifactData
                                .getSourceArtifactLastModifiedDate());
                    } else {
                        ga.setSourceArtifactVersion("1");
                        ga.setSourceArtifactLastModifiedDate(DateUtil
                                .format(lastDate));
                    }

                    // Content Type Field
                    GenericArtifactField cTypeField = ga.addNewField(
                            AttachmentMetaData.ATTACHMENT_TYPE,
                            GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
                    cTypeField
                            .setFieldValue(AttachmentMetaData.AttachmentType.DATA);
                    cTypeField
                            .setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
                    cTypeField
                            .setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

                    // Source URL Field
                    GenericArtifactField urlField = ga.addNewField(
                            AttachmentMetaData.ATTACHMENT_SOURCE_URL,
                            GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
                    urlField.setFieldValue(AttachmentMetaData.AttachmentType.LINK);
                    urlField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
                    urlField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

                    // Source Name Field
                    GenericArtifactField nameField = ga.addNewField(
                            AttachmentMetaData.ATTACHMENT_NAME,
                            GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
                    nameField.setFieldValue(lastName);
                    nameField
                            .setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
                    nameField
                            .setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

                    // Source Size Field
                    GenericArtifactField sizeField = ga.addNewField(
                            AttachmentMetaData.ATTACHMENT_SIZE,
                            GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
                    sizeField.setFieldValue(Long.valueOf(data.length));
                    sizeField
                            .setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
                    sizeField
                            .setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

                    // Source Mime Type Field
                    GenericArtifactField mimeField = ga.addNewField(
                            AttachmentMetaData.ATTACHMENT_MIME_TYPE,
                            GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
                    mimeField
                            .setFieldValue(AttachmentMetaData.AttachmentType.DATA);
                    mimeField
                            .setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
                    mimeField
                            .setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

                    if (shouldShipAttachmentsWithArtifact) {
                        if (data != null) {
                            ga.setRawAttachmentData(data);
                        }
                    }

                    attachments.add(ga);

                }

            }

        } catch (IImportExportDocumentRetrieveForArtifactServiceFaultMessageFaultFaultMessage e) {
            log.warn(
                    "Could not retrieve attachments for incident "
                            + artifactData.getSourceArtifactId(),
                            e);
        } catch (IImportExportDocumentOpenFileServiceFaultMessageFaultFaultMessage e) {
            String cause = "Could not download attachment";
            throw new CCFRuntimeException(cause, e);
        } catch (NumberFormatException e) {
            String cause = "Wrong Incident ID Format ˚"
                    + artifactData.getSourceArtifactId() + "`";
            throw new CCFRuntimeException(cause, e);
        } catch (IImportExportIncidentRetrieveByIdServiceFaultMessageFaultFaultMessage e) {
            String cause = "Could not retrieve incident data for ID "
                    + artifactData.getSourceArtifactId();
            throw new CCFRuntimeException(cause, e);
        }
        return attachments;
    }

    public void handleAttachment(ISTConnection connection,
            GenericArtifact attachmentGA, ISTIncident incident) {
        int parentIncidentId = Integer.valueOf(attachmentGA
                .getDepParentTargetArtifactId());
        log.info("An attachment will be created for artifact id #"
                + parentIncidentId);
        String contentType = GenericArtifactHelper.getStringFlexGAField(
                AttachmentMetaData.ATTACHMENT_TYPE,
                attachmentGA);
        String attachDescription = GenericArtifactHelper.getStringFlexGAField(
                AttachmentMetaData.ATTACHMENT_DESCRIPTION,
                attachmentGA);
        String attachmentName = GenericArtifactHelper.getStringFlexGAField(
                AttachmentMetaData.ATTACHMENT_NAME,
                attachmentGA);

        GenericArtifact.ArtifactActionValue attachmentAction = attachmentGA
                .getArtifactAction();

        if (attachmentAction == ArtifactActionValue.CREATE) {

            if (AttachmentMetaData.AttachmentType.valueOf(contentType) == AttachmentType.DATA) {

                String fileName = GenericArtifactHelper.getStringFlexGAField(
                        AttachmentMetaData.ATTACHMENT_DATA_FILE,
                        attachmentGA);

                File attachment = new File(fileName);
                log.trace("Attempting to read " + attachment.getPath());

                RemoteDocument newAttachment = incident.attachDocument(
                        readLocalFile(attachment),
                        attachmentName,
                        attachDescription);

                if (newAttachment != null) {
                    // update Generic Artifact info for attachment
                    attachmentGA.setTargetArtifactLastModifiedDate(df
                            .format(ISTHandler.toDate(newAttachment
                                    .getEditedDate())));
                    attachmentGA.setTargetArtifactVersion("1");
                    attachmentGA
                            .setTargetArtifactId(String.valueOf(newAttachment
                                    .getAttachmentId().getValue()));
                }

            } else {
                String cause = "Attachments of type " + contentType
                        + " are not supported";
                throw new CCFRuntimeException(cause);
            }
        } else if (attachmentAction == ArtifactActionValue.UPDATE) {
            String cause = "Attachment UPDATE is not supported";
            throw new CCFRuntimeException(cause);
        }
    }

    private byte[] readLocalFile(File file) {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new CCFRuntimeException(
                        "too early EOF while reading local file "
                                + file.getPath());
            }
        } catch (FileNotFoundException e) {
            String cause = "could not find local attachment " + file.getPath();
            throw new CCFRuntimeException(cause, e);
        } catch (IOException e) {
            String cause = "IO error with " + file.getPath();
            throw new CCFRuntimeException(cause, e);
        } finally {
            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
                String cause = "could not close " + file.getPath();
                throw new CCFRuntimeException(cause, e);
            }
        }
        return buffer;
    }
}
