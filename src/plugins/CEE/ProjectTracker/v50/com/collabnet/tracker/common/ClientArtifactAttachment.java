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
