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

package com.collabnet.tracker.common;

/**
 * This represents an attachment in the xml artifact returned.  
 * Used by the ClientArtifactListXMLHelper
 * 
 * @author Shawn Minto
 * 
 */
public class ClientArtifactAttachment {

	private String createdBy;
	private String createdOn;
	private String attachmentName;
	private String description;
	private String attachmentId;
	private String isFile;
	private String attachmentLocation;
	private String mimeType;

	public ClientArtifactAttachment(String createdBy, String createdOn, String attachmentName, String description,
			String attachmentId, String isFile, String attachmentLocation, String mimeType) {
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.attachmentName = attachmentName;
		this.description = description;
		this.attachmentId = attachmentId;
		this.isFile = isFile;
		this.attachmentLocation = attachmentLocation;
		this.mimeType = mimeType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public String getDescription() {
		return description;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public String getIsFile() {
		return isFile;
	}

	public String getAttachmentLocation() {
		return attachmentLocation;
	}

	public String getMimeType() {
		return mimeType;
	}
}
