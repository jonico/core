package com.collabnet.ccf.core.ga;

import org.apache.commons.codec.binary.Base64;

/**
 * An attachment has a name contains the CDATA value of the attachment. The content of the attachment element 
 * is always in the attachmentValueType of BASE64STRING.
 *  
 * @author jnicolai
 */
public class GenericArtifactAttachment {
	
	// TODO: Change the below attachmentType Strings to enum and use only enums everywhere
	/**
	 * Constant value for attachment type "mandatoryAttachment"
	 */
	public static final String VALUE_ATTACHMENT_TYPE_MANDATORY_ATTACHMENT = "mandatoryAttachment";

	/**
	 * Constant value for attachment type "flexAttachment"
	 */
	public static final String VALUE_ATTACHMENT_TYPE_FLEX_ATTACHMENT = "flexAttachment";

	/**
	 * Constant value for attachment type "integrationData"
	 */
	public static final String VALUE_ATTACHMENT_TYPE_INTEGRATION_DATA = "integrationData";

	/**
	 * This is the constant attribute values should set to if the value is not
	 * (yet) known or the whole functionality is not supported
	 */
	public static final String VALUE_UNKNOWN = "unknown";

	public static final String TEXT_PLAIN = "text/plain";

	/**
	 * 
	 * Possible values for the attachment action, "append", "replace" and "delete"
	 * 
	 * @author venugopala
	 * 
	 */
	public enum AttachmentActionValue {
		CREATE, RENAME, DELETE, UNKNOWN
	};
	/**
	 * 
	 * Possible values for the attachment content-type, "data", "link" and "empty"
	 * 
	 * @author venugopala
	 * 
	 */
	public enum AttachmentContentTypeValue {
		DATA, LINK, EMPTY, UNKNOWN
	};

	
	/**
	 * 
	 * Possible values for the attachment value type, "Integer", "Double",
	 * "DateTime", "Date", "String", "HTMLString", "Base64String", "Boolean" and
	 * "User"
	 * 
	 * @author venugopala
	 */
	public enum AttachmentValueTypeValue {
		BASE64STRING, UNKNOWN
	};

	public void getStringRepresentationOfattachmentValueTypeValue(GenericArtifactAttachment.AttachmentValueTypeValue attachmentValueTypeValue) {
		attachmentValueTypeValue.toString();
	}
	
	/**
	 * This attribute contains the name of the attachment.
	 */
	private String attachmentName = VALUE_UNKNOWN;
	/**
	 * This attribute contains the id of the attachment.
	 */
	private String attachmentId = VALUE_UNKNOWN;
	/**
	 * This attribute contains the description of the attachment.
	 */
	private String attachmentDescription = VALUE_UNKNOWN;
	/**
	 * This attribute contains the size of the attachment.
	 */
	private long attachmentSize = 0;
	/**
	 * This attribute contains the type of the attachment. This is not the type of
	 * the attachment's value but a mechanism to differentiate different artifact
	 * properties with the same name. Values might be "mandatoryAttachment",
	 * "flexAttachment", "integrationData" or just a plain number.
	 */
	private String attachmentType = VALUE_UNKNOWN;

	/**
	 * This attribute determines whether the content of the value tag should be
	 * "append"ed, "replace"d, or "delete"d. Valid values are "append",
	 * "replace" and "delete". Note that most systems will only support the
	 * "replace"-action and that source systems usually do not know anything
	 * about the characteristics of the target system.
	 */
	private AttachmentActionValue attachmentAction = AttachmentActionValue.UNKNOWN;
	/**
	 * This attribute determines whether the content of the value tag is either
	 * data or a link. If neither, it is an empty attachment.
	 */
	private AttachmentContentTypeValue attachmentContentType = AttachmentContentTypeValue.UNKNOWN;
	
	/**
	 * This attribute may have the values "true" or "false" to state whether
	 * this property has been changed since the last update. This attribute can
	 * be used by the target system as a hint which attachments to update.
	 */
	private boolean attachmentValueHasChanged = true;

	/**
	 * This element contains the type of the value of the surrounding attachment
	 * element. If there are multiple occurrences of the attachment element with the
	 * same name and attachment type, their values may have different types.
	 * Currently supported types are Integer, Double, DateTime, Date, String,
	 * HTMLString, Base64String, Boolean and User.
	 */
	private AttachmentValueTypeValue attachmentValueType = AttachmentValueTypeValue.BASE64STRING;
	
	/**
	 * The content of the attachment-element is the value of the property that is described by this attachment-element.
	 */
	private byte[] attachmentData=null;
	/**
	 * The source url will give the URL at which the attachmentData is available. If the attachmentContentType is LINK,
	 * this sourceUrl value will give the link. If neither, it will be empty.
	 */
	private String attachmentSourceUrl = VALUE_UNKNOWN;
	
	private String mimeType = VALUE_UNKNOWN;
	
	public GenericArtifactAttachment() {
	}
	
	/**
	 * Creates a new generic artifact attachment with all attributes but attachment name
	 * and attachment type set to unknown This constructor should not be called by
	 * any user of the CCF framework directly.
	 * 
	 * @param attachmentName
	 * @param attachmentType
	 */
	protected GenericArtifactAttachment(String attachmentName, String attachmentId, String attachmentDescription) {
		this.setAttachmentName(attachmentName);
		this.setAttachmentId(attachmentId);
		this.setAttachmentDescription(attachmentDescription);
	}

	/**
	 * @param attachmentValueHasChanged
	 *            the attachmentValueHasChanged to set
	 */
	public void setAttachmentValueHasChanged(boolean attachmentValueHasChanged) {
		this.attachmentValueHasChanged = attachmentValueHasChanged;
	}

	/**
	 * @return the attachmentValueHasChanged
	 */
	public boolean getAttachmentValueHasChanged() {
		return attachmentValueHasChanged;
	}

	/**
	 * @param attachmentAction
	 *            the attachmentAction to set
	 */
	public void setAttachmentAction(AttachmentActionValue attachmentAction) {
		this.attachmentAction = attachmentAction;
	}

	/**
	 * @return the attachmentAction
	 */
	public AttachmentActionValue getAttachmentAction() {
		return attachmentAction;
	}

	/**
	 * @param attachmentType
	 *            the attachmentType to set
	 * 
	 * This method may only be used in the constructor, otherwise, the indexing
	 * of the attachments will be destroyed
	 */
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	/**
	 * @return the attachmentType
	 */
	public String getAttachmentType() {
		return attachmentType;
	}

	/**
	 * @param attachmentName
	 *            the attachmentName to set
	 * 
	 * This method may only be used in the constructor, otherwise, the indexing
	 * of the attachments will be destroyed
	 */
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	/**
	 * @return the attachmentName
	 */
	public String getAttachmentName() {
		return attachmentName;
	}

	/**
	 * @param attachmentValueType the attachmentValueType to set
	 */
	public void setAttachmentValueType(AttachmentValueTypeValue attachmentValueType) {
		this.attachmentValueType = attachmentValueType;
	}

	/**
	 * @return the attachmentValueType
	 */
	public AttachmentValueTypeValue getAttachmentValueType() {
		return attachmentValueType;
	}
	
	/**
	 * @param attachmentValue the attachmentValue to set
	 */
	public void setRawAttachmentData(byte[] attachmentData) {
		this.attachmentData = Base64.encodeBase64(attachmentData);
	}
	
	/**
	 * @param attachmentValue the attachmentValue to set
	 */
	public void setAttachmentData(byte[] attachmentData) {
		this.attachmentData = attachmentData;
	}

	/**
	 * @return the fieldValue
	 */
	public byte[] getAttachmentData() {
		return attachmentData;
	}
	
	/**
	 * @return the fieldValue
	 */
	public byte[] getRawAttachmentData() {
		return Base64.decodeBase64(attachmentData);
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public long getAttachmentSize() {
		return attachmentSize;
	}

	public void setAttachmentSize(long attachmentSize) {
		this.attachmentSize = attachmentSize;
	}

	public String getAttachmentSourceUrl() {
		return attachmentSourceUrl;
	}

	public void setAttachmentSourceUrl(String attachmentSourceUrl) {
		this.attachmentSourceUrl = attachmentSourceUrl;
	}

	public AttachmentContentTypeValue getAttachmentContentType() {
		return attachmentContentType;
	}

	public void setAttachmentContentType(
			AttachmentContentTypeValue attachmentContentType) {
		this.attachmentContentType = attachmentContentType;
	}

	public String getAttachmentDescription() {
		return attachmentDescription;
	}

	public void setAttachmentDescription(String attachmentDescription) {
		this.attachmentDescription = attachmentDescription;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

		
}
