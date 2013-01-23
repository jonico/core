package com.collabnet.ccf.rcq;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;

import com.rational.clearquest.cqjni.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.utils.DateUtil;

public class RCQAttachmentHandler {

	private static final Log log = LogFactory
			.getLog(RCQAttachmentHandler.class);

	public List<GenericArtifact> listAttachments( RCQConnection connection ,
			Date lastModifiedDate,
			String ccfUsername , 
			List<String> artifactList,
			long maxAttachmentSizePerArtifact,
			boolean shouldShipAttachmentsWithArtifact,
			GenericArtifact artifactData)  {

		// return object
		List<GenericArtifact> attachmentGAs = new ArrayList<GenericArtifact>();

		// iterate over all artifacts
		for ( String recordId : artifactList ) {
			
			CQEntity record;
			try {
				
				record = connection.getCqSession().GetEntity(connection.getRecType(), recordId );
				
				
				// FIXME: name of attachment field should be user-configurable!
				CQAttachmentField attachmentField = record.GetAttachmentFields().ItemByName("Attachments" );
				
				CQAttachments allAttachments = attachmentField.GetAttachments();
				
				log.debug("record " + recordId + " has " + allAttachments.Count() + " files attached" );
				
				for ( long a = 0 ; a < allAttachments.Count() ; a++ ) {
					CQAttachment attachment = allAttachments.Item(a);
					String fileName = attachment.GetFileName();
					log.debug("\t #" + a + ": " + fileName);
					Long fileSize = attachment.GetFileSize();
					
					if ( fileSize > maxAttachmentSizePerArtifact ) {
						log.warn( "attachment size " + fileSize + " exceeds maximum size " + maxAttachmentSizePerArtifact);
						continue;
					}

					// TODO: check the user  (if ccfUser, skip it)
					
					// TODO: should we and if yes, how can we identify the date of an attachment?
					GenericArtifact ga = new GenericArtifact();
					ga.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
					ga.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
					ga.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
					ga.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);
					ga.setDepParentSourceArtifactId(recordId);
					// 
					ga.setSourceArtifactId( recordId + ":" + fileName );

					
					if ( artifactData != null ) {
						ga.setSourceArtifactVersion( artifactData.getSourceArtifactVersion() );
						ga.setSourceArtifactLastModifiedDate( artifactData.getSourceArtifactLastModifiedDate() );
					} else {
						ga.setSourceArtifactVersion( "1" );
						// FIXME is this clever??
						ga.setSourceArtifactLastModifiedDate(DateUtil.format(lastModifiedDate));
						
					}
					
					// now add field infos
					
					GenericArtifactField contentTypeField = ga.addNewField( 
							AttachmentMetaData.ATTACHMENT_TYPE , 
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD );
					
					contentTypeField.setFieldValue( AttachmentMetaData.AttachmentType.DATA );
					contentTypeField.setFieldAction( GenericArtifactField.FieldActionValue.REPLACE );
					contentTypeField.setFieldValueType( GenericArtifactField.FieldValueTypeValue.STRING );
 
					GenericArtifactField nameField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_NAME,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					nameField.setFieldValue(fileName);
					nameField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					nameField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

					GenericArtifactField sizeField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_SIZE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					sizeField.setFieldValue( attachment.GetFileSize() );
					sizeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					sizeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);

					GenericArtifactField mimeTypeField = ga.addNewField(
							AttachmentMetaData.ATTACHMENT_MIME_TYPE,
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					mimeTypeField.setFieldValue( new  MimetypesFileTypeMap().getContentType(fileName));
					mimeTypeField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
					mimeTypeField.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);


					byte[] attachmentData = null;
					
					// save the file into a temporary directory
					File tempFile = null;
					try {
						tempFile = File.createTempFile( recordId + "_attachment_" + a + "_" + fileName,"rcq");
					} catch (IOException e) {
						log.error("Could not create temporary attachment file" , e);
					}
					String attachmentDataFile = tempFile.getAbsolutePath();
					// this is the actual file save operation
					attachment.Load(attachmentDataFile);
					
					GenericArtifactField attachmentDataFileField = ga
							.addNewField(AttachmentMetaData.ATTACHMENT_DATA_FILE , 
									GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD );
					attachmentDataFileField.setFieldValueType( GenericArtifactField.FieldValueTypeValue.STRING );
					attachmentDataFileField.setFieldValue(attachmentDataFile);
					attachmentDataFileField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
		
					attachmentGAs.add(ga);
				}
			} catch (CQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// get all attachments
			
		}
	
		
		return attachmentGAs;
	}
	
	
	
}
