package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.pi.sfee.v44.meta.ArtifactMetaData;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

/**
 * This class works with the ISourceForgeSoap object for the source
 * SFEE system to get the Comment Texts entered by the users for an
 * artifact.
 * 
 * @author madhusuthanan (madhusuthanan@collab.net)
 *
 */
public class SFEEAppHandler {
	
	
	private ISourceForgeSoap mSfSoap;
	private String mSessionId;
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(SFEEAppHandler.class);
	public SFEEAppHandler(ISourceForgeSoap sfSoap, String sessionId) {
		this.mSfSoap = sfSoap;
		this.mSessionId = sessionId;
	}

	/**
	 * This method retrieves all the comments added to a particular artifact
	 * (represented by the ArtifactSoapDO) and adds all the comments that are
	 * added after the lastModifiedDate into the ArtifcatSoapDO's flex fields
	 * with the field name as "Comment Text" [as this is the name displayed
	 * in the SFEE trackers for the Comments]
	 * 
	 * It calls the private method addComments which can add comments for a list
	 * of artifacts by querying the ISourceForgeSoap object for this particular
	 * SFEE system.
	 * 
	 * The comments added by the connector user are ignored by this method.
	 * 
	 * @param artifact - The AritfactSoapDO object whose comments need to be added
	 * @param lastModifiedDate - The last read time of this tracker
	 * @param connectorUser - The username that is configured to log into the SFEE
	 * 						to retrieve the artifact data.
	 */
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
					if(lastModifiedDate.after(createdDate)){
						continue;
					}
					String description = comment.getDescription();
					boolean commentSet = false;
					for(ArtifactSoapDO artifactDO:artifactHistory){
						//TODO If nothing is matching what will happen?
//						if(artifactDO.getLastModifiedDate().after(createdDate) ||
//								artifactDO.getLastModifiedDate().equals(createdDate)){
							//TODO If more than one comment is added, How this will behave?
							ArtifactMetaData.addFlexField(ArtifactMetaData.SFEEFields.commentText.getFieldName(), artifactDO, description);
							commentSet = true;
							break;
//						}
					}
					if(!commentSet){
						log.error("Comment "+description+" Could not be set "+createdDate);
					}
				}
			}
		} catch (RemoteException e) {
			log.error("Could not get comments list for artifact "+artifact.getId());
		}
	}
	
	public static HashMap<String, List<TrackerFieldSoapDO>> loadTrackerFieldsInHashMap(TrackerFieldSoapDO[] flexFields){
		HashMap<String, List<TrackerFieldSoapDO>> fieldsMap = 
							new HashMap<String, List<TrackerFieldSoapDO>>();
		for(TrackerFieldSoapDO field:flexFields){
			String fieldName = field.getName();
			if(fieldsMap.containsKey(fieldName)){
				List<TrackerFieldSoapDO> fieldsList = fieldsMap.get(fieldName);
				fieldsList.add(field);
			}
			else {
				List<TrackerFieldSoapDO> fieldsList = new ArrayList<TrackerFieldSoapDO>();
				fieldsList.add(field);
				fieldsMap.put(fieldName, fieldsList);
			}
		}
		return fieldsMap;
	}
	
	public static TrackerFieldSoapDO getTrackerFieldSoapDOForFlexField(
			HashMap<String, List<TrackerFieldSoapDO>> fieldsMap, String fieldName){
		List<TrackerFieldSoapDO> fieldsList = fieldsMap.get(fieldName);
		if(fieldsList != null){
			if(fieldsList.size() == 1){
				return fieldsList.get(0);
			}
			else if(fieldsList.size() > 1){
				// TODO We are in trouble. We have a configurable field and a
				// flex field with the same name
			}
			else if(fieldsList.size() == 0){
				// No way. This should never happen.
			}
		}
		else {
			log.warn("Field for "+fieldName+" does not exist.");
		}
		// TODO Should we return null here?
		return null;
	}

}
