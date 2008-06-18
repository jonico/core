package com.collabnet.tracker.core.model;

import java.io.Serializable;

import com.collabnet.tracker.core.util.TrackerUtil;

/**
 * This represents a single attribute for an artifact in PT.
 * 
 * @author Shawn Minto
 * 
 */
public class TrackerAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String namespace;
	private String tagName;
	private String displayName;
	private String attributeType;

	private boolean isReadOnly = false;
	private boolean isHidden = false;
	private boolean isRequired = false;

	public TrackerAttribute(String displayName, String tagName, String namespace, String attributeType) {
		this.displayName = displayName;
		this.tagName = tagName;
		this.namespace = namespace;
		this.attributeType = attributeType;
	}

	public TrackerAttribute(String displayName, String tagName, String namespace, boolean isReadOnly, boolean isHidden) {
		this(displayName, tagName, namespace, "_SHORT_TEXT");
		this.isHidden = isHidden;
		this.isReadOnly = isReadOnly;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getTagName() {
		return tagName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getKey() {
		return TrackerUtil.getKey(namespace, tagName);
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

}
