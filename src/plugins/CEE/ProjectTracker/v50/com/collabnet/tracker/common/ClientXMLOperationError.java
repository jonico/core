/*
 * Copyright 2006 CollabNet, Inc.  All rights reserved.
 */

package com.collabnet.tracker.common;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * * Represents an error condition returned as part of an artifact list
 * 
 * @author Shawn Minto
 */
public class ClientXMLOperationError {

	private Node mNode;
	private String mMsg;
	private String mType;
	private String mCode;
	private String mTrace;

	/**
	 * Constructs the object from its XML representation. The (optional) child
	 * elements are:
	 * <ul>
	 * <li>node: the XML node that caused the failure</li>
	 * <li>error: the exception. This has the follwoing substructure:
	 * <ul>
	 * <li>msg</li>
	 * <li>type</li>
	 * <li>code (optional)</li>
	 * <li>trace</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @param errorNode
	 */
	public ClientXMLOperationError(Node errorNode) {
		Node child = errorNode.getFirstChild();
		while (child != null) {
			if (child.getLocalName().equals("node")) {
				mNode = child.getFirstChild();
			} else if (child.getLocalName().equals("error")) {
				parseError(child);
			}
			child = child.getNextSibling();
		}
	}

	/**
	 * @param errorNode
	 *            an error node. Contains msg, type, code and trace
	 */
	private void parseError(Node errorNode) {
		Node child = errorNode.getFirstChild();
		while (child != null) {
			if (child.getLocalName().equals("msg")) {
				mMsg = getTextValue(child);
			} else if (child.getLocalName().equals("type")) {
				mType = getTextValue(child);
			} else if (child.getLocalName().equals("code")) {
				mCode = getTextValue(child);
			} else if (child.getLocalName().equals("trace")) {
				mTrace = getTextValue(child);
			}
			child = child.getNextSibling();
		}
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return mCode;
	}

	/**
	 * @param code
	 *            The code to set.
	 */
	public void setCode(String code) {
		mCode = code;
	}

	/**
	 * @return Returns the msg.
	 */
	public String getMsg() {
		return mMsg;
	}

	/**
	 * @param msg
	 *            The msg to set.
	 */
	public void setMsg(String msg) {
		mMsg = msg;
	}

	/**
	 * @return Returns the node.
	 */
	public Node getNode() {
		return mNode;
	}

	/**
	 * @param node
	 *            The node to set.
	 */
	public void setNode(Node node) {
		mNode = node;
	}

	/**
	 * @return Returns the trace.
	 */
	public String getTrace() {
		return mTrace;
	}

	/**
	 * @param trace
	 *            The trace to set.
	 */
	public void setTrace(String trace) {
		mTrace = trace;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return mType;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		mType = type;
	}

	private String getTextValue(Node node) {
		if (node == null)
			return null;
		StringBuffer b = new StringBuffer();
		if (node.getNodeType() == Node.TEXT_NODE) {
			return ((Text) node).getData();
		} else if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
			return ((CDATASection) node).getData();
		} else {
			Node child = node.getFirstChild();
			while (child != null) {
				b.append(getTextValue(child));
				child = child.getNextSibling();
			}
		}
		return b.toString();
	}

	public String toString() {
		return "[code: " + this.mCode + ", type: " + this.mType + ", msg: " + this.mMsg + ", trace: " + this.mTrace
				+ "]";
	}
}
