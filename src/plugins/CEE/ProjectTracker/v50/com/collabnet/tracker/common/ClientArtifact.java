/*
 * Copyright 2006 CollabNet, Inc.  All rights reserved.
 */

package com.collabnet.tracker.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.collabnet.tracker.core.util.TrackerUtil;

/**
 * Simple helper class to represent an artifact - its name, and all its
 * attributes
 * 
 * @author Shawn Minto
 * @author sszego
 */
public class ClientArtifact {

	private String mTagName;

	private String mNamespaceURI;

	// attributes where their value was text
	private Map<String, List<String>> textAttributes = new HashMap<String, List<String>>();

	public ClientArtifact() {
		super();
	}
	
	public ClientArtifact(String id) {
		this();
		addAttributeValue("urn:ws.tracker.collabnet.com", "id", id);
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof ClientArtifact))
			return false;
		return ((ClientArtifact)o).getArtifactID().equalsIgnoreCase(getArtifactID());
	}
	
	/**
	 * Sets the XML name of the artifact
	 * 
	 * @param tagName
	 *            the XML name
	 */
	public void setTagName(String tagName) {
		mTagName = tagName;

	}

	/**
	 * Sets the namespace URI of the artifact
	 * 
	 * @param namespaceURI
	 *            the namespace URI
	 */
	public void setNamespace(String namespaceURI) {
		mNamespaceURI = namespaceURI;
	}

	/**
	 * Adds a value to the attribute
	 * 
	 * @param namespace
	 *            the namespace of the attribute
	 * @param tagName
	 *            the XML name of the attribute
	 * @param value
	 *            the value to add
	 */
	public void addAttributeValue(String namespace, String tagName, String value) {
		addToNamespaceMap(namespace, tagName);
		String fullyQualifiedName = TrackerUtil.getKey(namespace, tagName);
		List<String> values = textAttributes.get(fullyQualifiedName);
		if (values == null) {
			values = new ArrayList<String>();
			textAttributes.put(fullyQualifiedName, values);
		}
		values.add(value);
	}

	private Map<String, String> namespaceMap = new HashMap<String, String>();
	
	private void addToNamespaceMap(String namespace, String tagName) {
		namespaceMap.put(tagName, namespace);	
	}
	
	public String getNamespacesForTagName(String tagName){
		return namespaceMap.get(tagName);
	}

	/**
	 * @return Returns the namespaceURI.
	 */
	public String getNamespace() {
		return mNamespaceURI;
	}

	/**
	 * @return Returns the tagName.
	 */
	public String getTagName() {
		return mTagName;
	}

	/**
	 * @param namespace
	 *            the namespace URI of the attribute
	 * @param tagname
	 *            the XML name of the attribute
	 * @return the String value of the attribute. If addAttributeValue was
	 *         called more than once for the same attribute, this returns the
	 *         value set in the first call; i.e., this is similar to :
	 *         <code><pre>
	 *  getAttributeValues()[0];
	 * </pre></code>
	 */
	public String getAttributeValue(String namespace, String tagname) {
		List<String> values = textAttributes.get(TrackerUtil.getKey(namespace, tagname));
		if (values == null) {
			return null;
		}
		return (String) values.get(0);
	}
	
	public String getArtifactID(){
		return getAttributeValue("urn:ws.tracker.collabnet.com", "id");
	}

	/**
	 * @param namespace
	 *            the namespace URI of the attribute
	 * @param tagName
	 *            the XML name of the attribute
	 * @return the values of the attribute
	 */
	public String[] getAttributeValues(String namespace, String tagName) {
		List<String> values = textAttributes.get(TrackerUtil.getKey(namespace, tagName));
		if (values == null) {
			return null;
		}
		String[] result = new String[values.size()];
		values.toArray(result);
		return result;
	}

	public Map<String, List<String>> getAttributes() {
		return textAttributes;
	}

	private List<ClientArtifactComment> comments = new ArrayList<ClientArtifactComment>();

	public void addComment(ClientArtifactComment comment) {
		comments.add(comment);

	}

	public List<ClientArtifactComment> getComments() {
		Collections.sort(comments, new Comparator<ClientArtifactComment>() {

			public int compare(ClientArtifactComment o1, ClientArtifactComment o2) {
				Long date1 = Long.parseLong(o1.getCommentDate());
				Long date2 = Long.parseLong(o2.getCommentDate());
				return date1.compareTo(date2);
			}

		});
		return comments;
	}

	private List<ClientArtifactAttachment> attachments = new ArrayList<ClientArtifactAttachment>();

	public void addAttachment(ClientArtifactAttachment attachment) {
		attachments.add(attachment);
	}

	public List<ClientArtifactAttachment> getAttachments() {
		Collections.sort(attachments, new Comparator<ClientArtifactAttachment>() {

			public int compare(ClientArtifactAttachment o1, ClientArtifactAttachment o2) {
				Long date1 = Long.parseLong(o1.getCreatedOn());
				Long date2 = Long.parseLong(o2.getCreatedOn());
				return date1.compareTo(date2);
			}

		});
		return attachments;
	}
}
