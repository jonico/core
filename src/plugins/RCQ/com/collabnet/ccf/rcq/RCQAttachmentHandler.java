package com.collabnet.ccf.rcq;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.rational.clearquest.cqjni.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.tfs.TFSAttachmentHandler;

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
		
		// iterate over all artifacts
		for ( String art : artifactList ) {
			
			CQEntity record;
			try {
				
				record = connection.getCqs().GetEntity(connection.getRecType(), art );
				
				
				// FIXME: name of attachment field should be user-configurable!
				CQAttachmentField attachmentField = record.GetAttachmentFields().ItemByName("Attachments" );
				
				CQAttachments allAttachments = attachmentField.GetAttachments();
				
				log.debug("record " + art + " has " + allAttachments.Count() + " files attached" );
				
				for ( long a = 0 ; a < allAttachments.Count() ; a++ ) {
					CQAttachment attachment = allAttachments.Item(a);
					log.debug("   #" + a + ": " + attachment.GetFileName());
				}
			} catch (CQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// get all attachments
			
		}
	
		
		return new ArrayList<GenericArtifact>();
	}
	
}
