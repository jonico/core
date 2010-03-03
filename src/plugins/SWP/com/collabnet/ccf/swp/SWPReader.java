package com.collabnet.ccf.swp;

import java.util.List;

import org.dom4j.Document;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.ga.GenericArtifact;

/**
 * SWP Reader component
 * @author jnicolai
 *
 */
public class SWPReader extends AbstractReader<Connection> {

	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			GenericArtifact artifactData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setUserName(String userName) {
		// TODO implement me
	}
	
	public void setPassword(String password) {
		// TODO implement me
	}

	public void setServerUrl(String serverUrl) {
		// TODO implement me
	}
	
	public void setResyncUserName(String resyncUserName) {
		// TODO implement me
	}
}
