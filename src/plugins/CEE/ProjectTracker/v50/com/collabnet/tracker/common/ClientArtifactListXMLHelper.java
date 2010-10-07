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

/*
 * Copyright 2006 CollabNet, Inc.  All rights reserved.
 */

package com.collabnet.tracker.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.collabnet.tracker.core.TrackerWebServicesClient;

/**
 * A {@link java.sql.ResultSet}like interface to artifactList xml documents.
 *
 * Typical use:
 *
 * <code><pre>
 * Document artifactList = execute(getArtifactListDocument);
 * ClientArtifactListXMLHelper helper = new ClientArtifactListXMLHelper(artifactList);
 * while (helper.hasMore()) {
 * 	String id = helper.getArtifactId();
 * 	String tagName = helper.getArtifactXMLName();
 * 	String namespace = helper.getArtifactNamespace();
 * 	String value = helper.getArtifactAttribute(attributeNamespace, attributeXMLName);
 * 	helper.nextArtifact();
 * }
 * </pre></code>
 *
 * @author Shawn Minto
 * @author sszego
 */
public class ClientArtifactListXMLHelper {

	public static final String NAMESPACE = TrackerWebServicesClient.DEFAULT_NAMESPACE;

	private static final String ERRORS_TAG = "errors";
	public static final String PAGE_INFO = "pageInfo";
	private static final String VALUE_TAG = "value";
	private static final String CREATED_BY_TAG = "createdBy";
	private static final String CREATED_ON_TAG = "createdOn";
	private static final String NAME_TAG = "name";
	private static final String DESCRIPTION_TAG = "description";
	private static final String ID_TAG = "id";
	private static final String IS_FILE_TAG = "isFile";
	private static final String LOCATION_TAG = "location";
	private static final String MIME_TAG = "mime";
	private static final String TEXT_TAG = "text";
	private static final String COMMENT_TAG = "comment";
	private static final String ATTACHMENT_TAG = "attachment";
	private static final String URL_TAG = "url";

	private List<ClientArtifact> mArtifacts = new ArrayList<ClientArtifact>();

	private Node mPageInfo;

	private List<ClientXMLOperationError> mErrors = new ArrayList<ClientXMLOperationError>();

	/**
	 * Constructs an instance of the helper object. The <code>document</code>
	 * must be an instance of an artifactList document; see
	 * {@link com.collabnet.tracker.xml.ArtifactListXMLHelper}
	 *
	 * @param document
	 *            the XML representation of an artifact list
	 */
	public ClientArtifactListXMLHelper(Document document) {
		parseResultDocument(document);
	}


	private void parseResultDocument(Document document){
		setPageInfo(null);
		Node rootElement = document.getDocumentElement();
		Node child = rootElement.getFirstChild();
		while (child != null) {
			if (child.getLocalName().equals(PAGE_INFO) && child.getNamespaceURI().equals(NAMESPACE)) {
				setPageInfo(child);
			} else if (child.getLocalName().equals(ERRORS_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
				parseErrors(child);
			} else {
				ClientArtifact artifactInfo = createArtifactInfo(child);
				mArtifacts.add(artifactInfo);
			}
			child = child.getNextSibling();
		}
	}

	public String getQueryReference(Node pageInfo)  {
		if(pageInfo == null)
			return null;
		Node child = pageInfo.getFirstChild();
		String name;
		while(child != null) {
			name = child.getNodeName();
			if(name.contains("queryReference")) {
				return child.getTextContent();
			}
			child = child.getNextSibling();
		}
		return null;
	}

	/**
	 * @param child
	 * @return the ClientArtifact
	 */
	private ClientArtifact createArtifactInfo(Node child) {
		ClientArtifact clientArtifact = new ClientArtifact();
		clientArtifact.setTagName(child.getLocalName());
		clientArtifact.setNamespace(child.getNamespaceURI());

		Node attr = child.getFirstChild();
		while (attr != null) {
			if (attr.getLocalName().equals(COMMENT_TAG) && attr.getNamespaceURI().equals(NAMESPACE)) {
				ClientArtifactComment comment = getComment(attr);
				if (comment != null)
					clientArtifact.addComment(comment);
			} else if (attr.getLocalName().equals(ATTACHMENT_TAG) && attr.getNamespaceURI().equals(NAMESPACE)) {
				ClientArtifactAttachment attachment = getAttachment(attr);
				if (attachment != null)
					clientArtifact.addAttachment(attachment);
			} else if (attr.getLocalName().equals(URL_TAG) && attr.getNamespaceURI().equals(NAMESPACE)) {
				ClientArtifactAttachment attachment = getURLAttachment(attr);
				if (attachment != null)
					clientArtifact.addAttachment(attachment);
			} else {
				List<String> values = getTextValue(attr);
				if (values != null) {
					if (values.isEmpty()) {
						clientArtifact.addAttributeValue(attr.getNamespaceURI(), attr.getLocalName(), "");
					} else {
						for (String attributeValue : values) {
							clientArtifact.addAttributeValue(attr.getNamespaceURI(), attr.getLocalName(),
									attributeValue);
						}
					}
				}
			}
			attr = attr.getNextSibling();
		}

		return clientArtifact;
	}

	private ClientArtifactAttachment getAttachment(Node node) {
		ClientArtifactAttachment attachment = null;
		Node child = node.getFirstChild();
		String createdBy = null;
		String createdOn = null;
		String attachmentName = null;
		String description = null;
		String attachmentId = null;
		String isFile = null;
		String attachmentLocation = null;
		String mimeType = null;

		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getLocalName().equals(CREATED_BY_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					createdBy = getNodeValue(n);
				} else if (child.getLocalName().equals(CREATED_ON_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					createdOn = getNodeValue(n);
				} else if (child.getLocalName().equals(NAME_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					attachmentName = getNodeValue(n);
				} else if (child.getLocalName().equals(DESCRIPTION_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					description = getNodeValue(n);
				} else if (child.getLocalName().equals(ID_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					attachmentId = getNodeValue(n);
				} else if (child.getLocalName().equals(IS_FILE_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					isFile = getNodeValue(n);
				} else if (child.getLocalName().equals(LOCATION_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					attachmentLocation = getNodeValue(n);
				} else if (child.getLocalName().equals(MIME_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					mimeType = getNodeValue(n);
				}
			}
			child = child.getNextSibling();
		}
		if (createdBy != null && createdOn != null && attachmentName != null && description != null
				&& attachmentId != null && attachmentLocation != null && mimeType != null)
			attachment = new ClientArtifactAttachment(createdBy, createdOn, attachmentName, description, attachmentId,
					isFile, attachmentLocation, mimeType);
		return attachment;
	}

	private ClientArtifactAttachment getURLAttachment(Node node) {
		ClientArtifactAttachment attachment = null;
		Node child = node.getFirstChild();
		String createdBy = null;
		String createdOn = null;
		String attachmentName = null;
		String description = null;
		String attachmentId = null;
		String isFile = null;
		String attachmentLocation = null;

		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getLocalName().equals(CREATED_BY_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					createdBy = getNodeValue(n);
				} else if (child.getLocalName().equals(CREATED_ON_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					createdOn = getNodeValue(n);
				} else if (child.getLocalName().equals(NAME_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					attachmentName = getNodeValue(n);
				} else if (child.getLocalName().equals(DESCRIPTION_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					description = getNodeValue(n);
				} else if (child.getLocalName().equals(ID_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					attachmentId = getNodeValue(n);
				} else if (child.getLocalName().equals(IS_FILE_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					isFile = getNodeValue(n);
				} else if (child.getLocalName().equals(LOCATION_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					attachmentLocation = getNodeValue(n);
				}
			}
			child = child.getNextSibling();
		}
		if (createdBy != null && createdOn != null && attachmentName != null && description != null
				&& attachmentId != null && attachmentLocation != null)
			attachment = new ClientArtifactAttachment(createdBy, createdOn, attachmentName, description, attachmentId,
					isFile, attachmentLocation, null);
		return attachment;
	}

	private ClientArtifactComment getComment(Node node) {
		ClientArtifactComment comment = null;
		Node child = node.getFirstChild();
		String commentId = null;
		String commentDate = null;
		String commenter = null;
		String commentText = null;
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getLocalName().equals(ID_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					commentId = n.getNodeValue();
				} else if (child.getLocalName().equals(CREATED_BY_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					commenter = n.getNodeValue();
				} else if (child.getLocalName().equals(CREATED_ON_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					commentDate = n.getNodeValue();
				} else if (child.getLocalName().equals(TEXT_TAG) && child.getNamespaceURI().equals(NAMESPACE)) {
					Node n = child.getFirstChild();
					if (n == null) {
						commentText = "";
					} else {
						commentText = n.getNodeValue();
					}
				}
			}
			child = child.getNextSibling();
		}
		if (commentId != null && commentDate != null && commentText != null && commenter != null)
			comment = new ClientArtifactComment(commentId, commentDate, commentText, commenter);
		return comment;
	}

	/**
	 * @param node
	 * @return the text value of the node
	 */
	private List<String> getTextValue(Node node) {
		Node child = node.getFirstChild();
		List<String> values = new ArrayList<String>();
		StringBuffer result = new StringBuffer();
		if (child == null) {
			return Collections.emptyList();
		}

		if (child.getNodeType() == Node.TEXT_NODE) {
			result.append(child.getNodeValue());
			values.add(result.toString());
		} else if (child.getNodeType() == Node.ELEMENT_NODE && child.getLocalName().equals(VALUE_TAG)
				&& child.getNamespaceURI().equals(NAMESPACE)) {

			Node val = child.getFirstChild();
			result.append(val.getNodeValue());
			values.add(result.toString());

			child = child.getNextSibling();
			while (child != null) {
				if(child.getLocalName().equals(VALUE_TAG)) {
					result = new StringBuffer();
					val = child.getFirstChild();
					result.append(val.getNodeValue());
					values.add(result.toString());
				}
				child = child.getNextSibling();
			}
		} else {
			return null;
		}

		return values;
	}

	/**
	 * @param child
	 */
	private void setPageInfo(Node child) {
		mPageInfo = child;
	}

	/**
	 * Returns true if the artifact list contains paging information
	 *
	 * @return true if the artifact list contains paging information
	 */
	public boolean hasPageInfo() {
		return (mPageInfo != null);
	}


	public List<ClientArtifact> getAllArtifacts() {
		return mArtifacts;
	}

	public List<String> getAllArtifactIds() {
		List<String> ids = new ArrayList<String>(mArtifacts.size());
		for(ClientArtifact artifact: mArtifacts){
			ids.add(artifact.getArtifactID());
		}
		return ids;
	}

	private void parseErrors(Node errorsNode) {
		Node errorNode = errorsNode.getFirstChild();
		while (errorNode != null) {
			if (errorNode.getLocalName().equals("error") && errorNode.getNamespaceURI().equals(NAMESPACE)) {
				ClientXMLOperationError error = new ClientXMLOperationError(errorNode);
				mErrors.add(error);
			}
			errorNode = errorNode.getNextSibling();
		}
	}

	/**
	 * Returns true if there is at least one error
	 *
	 * @return true if there is at least one error
	 */
	public boolean hasErrors() {
		return !mErrors.isEmpty();
	}

	/**
	 * Returns the list of errors
	 *
	 * @return the list of errors
	 */
	public List<ClientXMLOperationError> getErrors() {
		return mErrors;
	}

	/**
	 * Returns the ith error. See {@link List#get} for exception behavior.
	 *
	 * @param i
	 *            the index into the errors array
	 * @return the ith error
	 */
	public ClientXMLOperationError getError(int i) {
		return (ClientXMLOperationError) mErrors.get(i);
	}

	/**
	 * Returns the number of errors.
	 *
	 * @return the number of errors
	 */
	public int getErrorSize() {
		return mErrors.size();
	}

	private String getNodeValue(Node n) {
		if (n == null)
			return null;
		else return n.getNodeValue();
	}
}
