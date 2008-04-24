package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.vasoftware.sf.soap44.webservices.sfmain.AuditHistorySoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.AuditHistorySoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class SFEEAppHandler {
	private ISourceForgeSoap mSfSoap;
	private String mSessionId;
	public SFEEAppHandler(ISourceForgeSoap sfSoap, String sessionId) {
		this.mSfSoap = sfSoap;
		this.mSessionId = sessionId;
	}

	public List<ArtifactSoapDO> getArtifactAuditHistory(ArtifactSoapDO artifact,
				Date lastModifiedDate, String connectorUser, TrackerFieldSoapDO[] trackerFields){
		try {
			String artifactId = artifact.getId();
			AuditHistorySoapList history = mSfSoap.getAuditHistoryList(mSessionId, artifactId);
			AuditHistorySoapRow[] rows = history.getDataRows();
			if(rows == null || rows.length == 0){
				SFEEArtifactMetaData.setFieldValue("ArtifactAction", artifact, GenericArtifact.ArtifactActionValue.CREATE);
				if(artifact.getLastModifiedBy().equals(connectorUser)){
					return null;
				}
			}
			List<ArtifactSoapDO> artifactHistory = new ArrayList<ArtifactSoapDO>();
			artifactHistory.add(artifact);
			ArtifactSoapDO lastHistory = artifact;
			//AuditHistorySoapRow lastHistoryRow = null;
			for(int i=0; i < rows.length; i++){
				// If the change is made by the connector user
				// ignore it. Do not ship the change
				if(rows[i].getModifiedBy().equals(connectorUser)){
					continue;
				}
				pr(rows[i]);
				
				ArtifactSoapDO historyEntry = null;
//				if(!lastHistory.getLastModifiedDate().equals(rows[i].getDateModified())){
//					lastHistory.setLastModifiedDate(rows[i].getDateModified());
//				}
//				else {
//					historyEntry = lastHistory;
//				}
				historyEntry = (ArtifactSoapDO) SFEEArtifactMetaData.cloneArtifactSoapDO(lastHistory);
				
				for(; i < rows.length;i++){
					if(lastModifiedDate.after(rows[i].getDateModified())){
						i = rows.length;
						break;
					}
					if(!historyEntry.getLastModifiedDate().equals(rows[i].getDateModified())){
						historyEntry.setLastModifiedDate(rows[i].getDateModified());
						i--;
						break;
					}
					else{
						String fieldName = rows[i].getPropertyName();
						String gaFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
						//Object value = ArtifactMetaData.parseFieldValue(fieldName, rows[i].getOldValue(), trackerFields);
						SFEEArtifactMetaData.setFieldValue(gaFieldName, historyEntry, rows[i].getOldValue());
						if(i == rows.length-1){
							SFEEArtifactMetaData.setFieldValue("ArtifactAction", historyEntry, GenericArtifact.ArtifactActionValue.CREATE);
						}
					}
				}
//				if(!lastHistory.getLastModifiedDate().equals(row.getDateModified())){
//				historyEntry.setLastModifiedDate(rows[i+1].getDateModified());
				lastHistory = historyEntry;
				artifactHistory.add(historyEntry);
//				}
			}
			Collections.reverse(artifactHistory);
			addComments(artifactHistory, artifact,
					lastModifiedDate,connectorUser);
			return artifactHistory;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private void addComments(List<ArtifactSoapDO> artifactHistory,
			ArtifactSoapDO artifact, Date lastModifiedDate, String connectorUser) {
		try {
			CommentSoapList commentList = mSfSoap.getCommentList(mSessionId, artifact.getId());
			CommentSoapRow[] comments = commentList.getDataRows();
			if(comments != null){
				for(CommentSoapRow comment:comments){
					String createdBy = comment.getCreatedBy();
					Date createdDate = comment.getDateCreated();
					if(createdBy.equals(connectorUser)){
						continue;
					}
					if(lastModifiedDate.before(createdDate)){
						continue;
					}
					String description = comment.getDescription();
					boolean commentSet = false;
					for(ArtifactSoapDO artifactDO:artifactHistory){
						//TODO If nothing is matching what will happen?
						if(artifactDO.getLastModifiedDate().after(createdDate)){
							//TODO If more than one comment is added, How this will behave?
							SFEEArtifactMetaData.setFieldValue("SF_Comment", artifactDO, description);
							commentSet = true;
							break;
						}
					}
					if(!commentSet){
						System.out.println("Comment "+description+" Could not be set "+createdDate);
					}
				}
			}
		} catch (RemoteException e) {
			System.out.println("Could not get Comments list for artifact "+artifact.getId());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void pr(AuditHistorySoapRow row){
		System.out.println("============================================================================");
		System.out.println("Property Name --> "+row.getPropertyName());
		System.out.println("Comment --> "+row.getComment());
		System.out.println("Date Modified --> "+row.getDateModified());
		System.out.println("Modified by --> "+row.getModifiedBy());
		System.out.println("Modifier full name --> "+row.getModifierFullName());
		System.out.println("New Value --> "+row.getNewValue());
		System.out.println("Old Value --> "+row.getOldValue());
		System.out.println("Operation --> "+row.getOperation());
		System.out.println("============================================================================");
	}
	public List<ArtifactSoapDO> loadArtifactAuditHistory(List<ArtifactSoapDO> artifactRows,
			Date lastModifiedDate, String connectorUser, TrackerFieldSoapDO[] trackerFields) {
		List<ArtifactSoapDO> artifactHistoryRows = new ArrayList<ArtifactSoapDO>();
		if(artifactRows != null){
			for(ArtifactSoapDO artifactRow:artifactRows){
				List<ArtifactSoapDO> artifactHistory = getArtifactAuditHistory(artifactRow,
								lastModifiedDate,connectorUser, trackerFields);
				if(artifactHistory != null){
					artifactHistoryRows.addAll(artifactHistory);
				}
			}
		}
		return artifactHistoryRows;
	} 
}
