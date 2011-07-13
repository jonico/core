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

package com.collabnet.tracker.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.collabnet.tracker.core.TrackerReportElement;
import com.collabnet.tracker.core.util.TrackerUtil;
import com.collabnet.tracker.ws.ArtifactType;
import com.collabnet.tracker.ws.ArtifactTypeMetadata;
import com.collabnet.tracker.ws.Attribute;
import com.collabnet.tracker.ws.AttributeType;

/**
 * This represents an artifact type in PT(defect, enhancement, etc).  All of the
 * valid attributes and stored here.
 * 
 * @author Shawn Minto
 * 
 */
public class TrackerArtifactType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String namespace;
	private String tagName;
	private String displayName;
	private Map<String, TrackerAttribute> attributes;

	public TrackerArtifactType(String displayName, String tagName, String namespace) {
		this.displayName = displayName;
		this.tagName = tagName;
		this.namespace = namespace;
		attributes = new HashMap<String, TrackerAttribute>();
	}
	
	public TrackerArtifactType(ArtifactType aType) {
		this(aType.getDisplayName(), aType.getTagName(), aType.getNamespace());
	}

	public String getNamespace() {
		return namespace;
	}

	public String getTagName() {
		return tagName;
	}

	public String getKey() {
		return TrackerUtil.getKey(namespace, tagName);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void populateAttributes(ArtifactTypeMetadata artifactMetaData) {
		displayName = artifactMetaData.getArtifactType().getDisplayName();
		namespace = artifactMetaData.getArtifactType().getNamespace();
		tagName = artifactMetaData.getArtifactType().getTagName();
		
		Attribute[] attributes = artifactMetaData.getAttribute();

		for (Attribute attribute : attributes) {
			String attributeDisplayName = attribute.getDisplayName();
			String attributeNamespace = attribute.getNamespace();
			String attributeTagName = attribute.getTagName();
			AttributeType attributeType = attribute.getType();

			TrackerAttribute trackerAttribute = new TrackerAttribute(attributeDisplayName, attributeTagName,
					attributeNamespace, attributeType.getValue());
			if(attribute.getRequired() != null)
				trackerAttribute.setRequired(attribute.getRequired());
			setReadOnlyAndHidden(trackerAttribute);
			addAttribute(trackerAttribute);
		}

		for (TrackerReportElement element : TrackerReportElement.values()) {
			if (element.isGlobal()) {
				TrackerAttribute trackerAttribute = new TrackerAttribute(element.toString(), element.getTagName(),
						element.getNamespace(), AttributeType.SHORT_TEXT.toString());

				setReadOnlyAndHidden(trackerAttribute);
				addAttribute(trackerAttribute);
			}
		}

		addAttribute(new TrackerAttribute(TrackerReportElement.NEW_COMMENT.toString(),
				TrackerReportElement.NEW_COMMENT.getTagName(), TrackerReportElement.NEW_COMMENT.getNamespace(), true,
				true));

		addAttribute(new TrackerAttribute(TrackerReportElement.REASON_FOR_CHANGE.toString(),
				TrackerReportElement.REASON_FOR_CHANGE.getTagName(),
				TrackerReportElement.REASON_FOR_CHANGE.getNamespace(), true, true));
	}

	private void setReadOnlyAndHidden(TrackerAttribute trackerAttribute) {
		String key = trackerAttribute.getKey();
		if (key.equals(TrackerReportElement.ID.getKeyString())) {
			trackerAttribute.setHidden(true);
			trackerAttribute.setReadOnly(true);
		} else if (key.equals(TrackerReportElement.MODIFIEDBY.getKeyString())) {
			trackerAttribute.setReadOnly(true);
			trackerAttribute.setHidden(false);
		} else if (key.equals(TrackerReportElement.CREATEDON.getKeyString())) {
			trackerAttribute.setReadOnly(true);
			trackerAttribute.setHidden(true);
		} else if (key.equals(TrackerReportElement.CREATEDBY.getKeyString())) {
			trackerAttribute.setReadOnly(true);
			trackerAttribute.setHidden(false);
		} else if (key.equals(TrackerReportElement.MODIFIEDON.getKeyString())) {
			trackerAttribute.setReadOnly(true);
			trackerAttribute.setHidden(true);
		} else if (key.endsWith("}" + TrackerReportElement.SUMMARY.getTagName())) {
			trackerAttribute.setReadOnly(false);
			trackerAttribute.setHidden(true);
		} else if (key.endsWith("}" + TrackerReportElement.DESCRIPTION.getTagName())) {
			trackerAttribute.setReadOnly(false);
			trackerAttribute.setHidden(true);
		} else if (key.endsWith("}" + TrackerReportElement.ASSIGNED_TO.getTagName())) {
			trackerAttribute.setReadOnly(false);
			trackerAttribute.setHidden(false);
		} else if (key.endsWith("}" + TrackerReportElement.CARBON_COPY.getTagName())) {
			trackerAttribute.setReadOnly(false);
			trackerAttribute.setHidden(false);
		} else if (key.endsWith("}" + TrackerReportElement.STATUS.getTagName())) {
			trackerAttribute.setReadOnly(true);
			trackerAttribute.setHidden(true);
		}
	}

	public void addAttribute(TrackerAttribute attribute) {
		addToNamespaceMap(attribute.getNamespace(), attribute.getTagName());
		attributes.put(attribute.getKey(), attribute);
	}

	public Map<String, TrackerAttribute> getAttributes() {
		return attributes;
	}

	public TrackerAttribute getAttribute(String key) {

		TrackerAttribute attribute = attributes.get(key);
		if (attribute != null)
			return attribute;
		return null;
	}

	
	private Map<String, String> namespaceMap = new HashMap<String, String>();
	
	private void addToNamespaceMap(String namespace, String tagName) {
		namespaceMap.put(tagName, namespace);	
	}
	
	public String getNamespacesForTagName(String tagName) {
		return namespaceMap.get(tagName);
	}
	
	/**
	 * Tries to see if there is a key that ends with the tagName.
	 * ie. we really  want "summary" but we'll settle for "c1.0_summary"
	 * @param tagName
	 * @return
	 */
	public String getFuzzyTagName(String tagName) {
		if(tagName == null)
			return null;
		for(String testKey : namespaceMap.keySet()){
			if(testKey.endsWith(tagName))
				return testKey;
		}
		
		return null;
	}
	
	
}