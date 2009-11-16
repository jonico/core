/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.core;

import java.util.Date;

import com.collabnet.ccf.core.ga.GenericArtifact;

public class ArtifactState {
	private String artifactId;
	private long artifactVersion;
	private Date artifactLastModifiedDate;
	
	private String errorCode = GenericArtifact.ERROR_OK; 
	
	/**
	 * this property is used to indicate whether this is a replayed (quarantined artifact) or not
	 */
	private boolean replayedArtifact=false;
	
	/**
	 * this property is used to store the serialized XML of the replayed artifact
	 */
	private String replayedArtifactData=null;
	
	/**
	 * this property stores the transaction id from the hospital
	 */
	private String transactionId=null;
	
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public long getArtifactVersion() {
		return artifactVersion;
	}
	public void setArtifactVersion(long artifactVersion) {
		this.artifactVersion = artifactVersion;
	}
	public Date getArtifactLastModifiedDate() {
		return artifactLastModifiedDate;
	}
	public void setArtifactLastModifiedDate(Date artifactLastModifiedDate) {
		this.artifactLastModifiedDate = artifactLastModifiedDate;
	}
	public void setReplayedArtifact(boolean replayedArtifact) {
		this.replayedArtifact = replayedArtifact;
	}
	public boolean isReplayedArtifact() {
		return replayedArtifact;
	}
	public void setReplayedArtifactData(String replayedArtifactData) {
		this.replayedArtifactData = replayedArtifactData;
	}
	public String getReplayedArtifactData() {
		return replayedArtifactData;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorCode() {
		return errorCode;
	}
}
