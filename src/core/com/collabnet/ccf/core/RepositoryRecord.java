package com.collabnet.ccf.core;

import java.util.ArrayList;

import org.dom4j.Document;
import com.collabnet.ccf.core.ga.GenericArtifact;

public class RepositoryRecord {
	private String repositoryId;
	private Document syncInfo = null;
	private ArrayList<GenericArtifact> artifactsToBeShippedList = null;
	private ArrayList<String> artifactsToBeReadList = null;
	public RepositoryRecord(String repositoryId, Document syncInfo){
		this.repositoryId = repositoryId;
		this.syncInfo = syncInfo;
		artifactsToBeShippedList = new ArrayList<GenericArtifact>();
		artifactsToBeReadList = new ArrayList<String>();
	}
	public String getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}
	public Document getSyncInfo() {
		return syncInfo;
	}
	public void setSyncInfo(Document syncInfo) {
		this.syncInfo = syncInfo;
	}
	public ArrayList<GenericArtifact> getArtifactsToBeShippedList() {
		return artifactsToBeShippedList;
	}
	public void setArtifactsToBeShippedList(
			ArrayList<GenericArtifact> artifactsToBeShippedList) {
		this.artifactsToBeShippedList = artifactsToBeShippedList;
	}
	public ArrayList<String> getArtifactsToBeReadList() {
		return artifactsToBeReadList;
	}
	public void setArtifactsToBeReadList(ArrayList<String> artifactsToBeReadList) {
		this.artifactsToBeReadList = artifactsToBeReadList;
	}
}
