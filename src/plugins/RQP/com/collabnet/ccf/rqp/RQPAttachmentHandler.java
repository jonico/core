package com.collabnet.ccf.rqp;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.utils.DateUtil;
import com.microsoft.tfs.core.clients.workitem.files.Attachment;
import com.rational.reqpro.rpx._AttrValue;
import com.rational.reqpro.rpx._Requirement;
import com.rational.reqpro.rpx.enumAttrValueLookups;
import com.rational.reqpro.rpx.enumRequirementFlags;
import com.rational.reqpro.rpx.enumRequirementLookups;
import com.rational.reqpro.rpx.enumRequirementsWeights;

public class RQPAttachmentHandler {

	@SuppressWarnings("unused")
	private ConnectionManager<RQPConnection> connectionManager = null;

	public RQPAttachmentHandler(String serverUrl,
			ConnectionManager<RQPConnection> connectionManager) {
		this.connectionManager = connectionManager;
	}

	public List<GenericArtifact> listAttachments(RQPConnection connection,
			Date lastModifiedDate, String username, List<String> artifactIds,
			long maxAttachmentSizePerArtifact,
			boolean shouldShipAttachmentsWithArtifact,
			GenericArtifact artifactData) throws RemoteException {

		return null;
	}

	public byte[] getAttachmentData(RQPConnection connection,
			Attachment attachment, boolean shouldShipAttachmentsWithArtifact,
			GenericArtifact ga) throws RemoteException {
		return null;
	}

	public void handleAttachment(RQPConnection connection, GenericArtifact ga,
			String targetParentArtifactId, String userName)
			throws RemoteException {

		boolean attached = true;
		
		String attachmentName = GenericArtifactHelper.getStringFlexGAField(
				AttachmentMetaData.ATTACHMENT_NAME, ga);
		
		_Requirement req = null;
		
		try {
			
			req = connection.getProjectConnection().GetRequirement(targetParentArtifactId,
					enumRequirementLookups.eReqLookup_Key,
					enumRequirementsWeights.eReqWeight_Heavy,
					enumRequirementFlags.eReqFlag_Refresh);

			String attachmentValue = userName + " attached '" + attachmentName + "' (" + req.getVersionDateTime() + ")";
			
			if (req != null) {
				
				_AttrValue attrVal = req.getAttrValue("Attachment", enumAttrValueLookups.eAttrValueLookup_Label);
				String oldAttachments = attrVal.getText();
				
				if (oldAttachments != null && !"null".equals(oldAttachments)){
					attrVal.setText(oldAttachments + ", " + attachmentValue);
				} else {
					attrVal.setText(attachmentValue);
				}
				
				req.Save();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			attached = false;
		}
		
		if (attached) {
			
			Date attachmentLastModifiedDate = null;
			try {
				
				attachmentLastModifiedDate = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).parse(req.getVersionDateTime());
				
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			ga.setTargetArtifactLastModifiedDate(DateUtil.format(attachmentLastModifiedDate));
			ga.setTargetArtifactVersion("1");
			ga.setTargetArtifactId(GenericArtifact.VALUE_UNKNOWN);
		}
		
	}

}
