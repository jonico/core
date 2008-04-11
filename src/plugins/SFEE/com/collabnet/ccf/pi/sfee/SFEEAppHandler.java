package com.collabnet.ccf.pi.sfee;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.pi.sfee.SFEEArtifactMetaData;
import com.vasoftware.sf.soap44.webservices.sfmain.AuditHistorySoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.AuditHistorySoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class SFEEAppHandler {
	private ISourceForgeSoap mSfSoap;
	private String mSessionId;
	public SFEEAppHandler(ISourceForgeSoap sfSoap, String sessionId) {
		this.mSfSoap = sfSoap;
		this.mSessionId = sessionId;
	}

	public List<ArtifactSoapDO> getArtifactAuditHistory(ArtifactSoapDO artifact,
				Date lastModifiedDate, String connectorUser){
		try {
			String artifactId = artifact.getId();
			AuditHistorySoapList history = mSfSoap.getAuditHistoryList(mSessionId, artifactId);
			AuditHistorySoapRow[] rows = history.getDataRows();
			if(rows == null || rows.length == 0){
				SFEEArtifactMetaData.setFieldValue("ArtifactAction", artifact, GenericArtifact.ArtifactActionValue.CREATE);
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
			return artifactHistory;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
			Date lastModifiedDate, String connectorUser) {
		List<ArtifactSoapDO> artifactHistoryRows = new ArrayList<ArtifactSoapDO>();
		if(artifactRows != null){
			for(ArtifactSoapDO artifactRow:artifactRows){
				List<ArtifactSoapDO> artifactHistory = getArtifactAuditHistory(artifactRow,
								lastModifiedDate,connectorUser);
				if(artifactHistory != null){
					artifactHistoryRows.addAll(artifactHistory);
				}
			}
		}
		return artifactHistoryRows;
	} 
}
