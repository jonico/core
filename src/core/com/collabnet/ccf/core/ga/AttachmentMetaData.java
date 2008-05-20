package com.collabnet.ccf.core.ga;
/**
 * 
 * The class AttachmentMetaData contains the fields of the attachment GenericArtifact along with the 
 * possbile values for AttachmnetType and AttachmentValueType.
 * 
 * @author venugopala
 * 
 */
public class AttachmentMetaData {

	
	private static final String ATTACHMENT_NAME = "attachmentName";
	private static final String ATTACHMENT_ID = "attachmentId";
	private static final String ATTACHMENT_SIZE = "attachmentSize";
	private static final String ATTACHMENT_SOURCE_URL = "attachmentSourceUrl";
	private static final String ATTACHMENT_TYPE = "attachmentType";
	private static final String ATTACHMENT_DESCRIPTION = "attachmentDescription";
	private static final String ATTACHMENT_MIME_TYPE = "attachmentMIMEType";
	private static final String ATTACHMENT_VALUE_HAS_CHANGED = "attachmentValueHasChanged";
	private static final String ATTACHMENT_VALUE_IS_NULL = "attachmentValueIsNull";
	
	public final String ATTACHMENT_VALUE_IS_NULL_TRUE = "true";
	public final String ATTACHMENT_VALUE_IS_NULL_FALSE = "false";
	/**
	 * 
	 * This contains an array of attachment fields.
	 * 
	 */
	private static final String[] attachmentMetaData = {
		ATTACHMENT_NAME, ATTACHMENT_ID, ATTACHMENT_SIZE, ATTACHMENT_SOURCE_URL, ATTACHMENT_TYPE,
		ATTACHMENT_MIME_TYPE, ATTACHMENT_VALUE_HAS_CHANGED, ATTACHMENT_VALUE_IS_NULL, 
		ATTACHMENT_DESCRIPTION
		};

	/**
	 * 
	 * Possible values for the attachmentType, "data", "link" and "empty"
	 * 
	 */
	public enum AttachmentType {
		DATA, LINK, EMPTY
	};

	public enum AttachmentValueIsNull {
		ATTACHMENT_VALUE_IS_NULL_TRUE, ATTACHMENT_VALUE_IS_NULL_FALSE
	};
	
	public static String[] getAttachmentMetaData() {
		return attachmentMetaData;
	}

	public static String getAttachmentName() {
		return ATTACHMENT_NAME;
	}

	public static String getAttachmentId() {
		return ATTACHMENT_ID;
	}

	public static String getAttachmentSize() {
		return ATTACHMENT_SIZE;
	}

	public static String getAttachmentSourceUrl() {
		return ATTACHMENT_SOURCE_URL;
	}

	public static String getAttachmentType() {
		return ATTACHMENT_TYPE;
	}

	public static String getAttachmentMimeType() {
		return ATTACHMENT_MIME_TYPE;
	}

	public static String getAttachmentValueHasChanged() {
		return ATTACHMENT_VALUE_HAS_CHANGED;
	}

	public static String getAttachmentValueIsNull() {
		return ATTACHMENT_VALUE_IS_NULL;
	};
	
	public static String getAttachmentDescription() {
		return ATTACHMENT_DESCRIPTION;
	}
	
	
}
