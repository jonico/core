package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.collabnet.ccf.pi.sfee.v44.meta.ArtifactMetaData;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class SFEEAppHandler {
	private ISourceForgeSoap mSfSoap;
	private String mSessionId;
	public SFEEAppHandler(ISourceForgeSoap sfSoap, String sessionId) {
		this.mSfSoap = sfSoap;
		this.mSessionId = sessionId;
	}

	public void addComments(ArtifactSoapDO artifact, Date lastModifiedDate, String connectorUser){
		List<ArtifactSoapDO> artifactList = new ArrayList<ArtifactSoapDO>();
		artifactList.add(artifact);
		this.addComments(artifactList, artifact, lastModifiedDate, connectorUser);
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
							ArtifactMetaData.addFlexField("Comment Text", artifactDO, description);
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
}
