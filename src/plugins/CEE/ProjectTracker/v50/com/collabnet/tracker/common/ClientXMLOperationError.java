/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

/*
 * Copyright 2006 CollabNet, Inc. All rights reserved.
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

    private Node   mNode;
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
     * @return Returns the code.
     */
    public String getCode() {
        return mCode;
    }

    /**
     * @return Returns the msg.
     */
    public String getMsg() {
        return mMsg;
    }

    /**
     * @return Returns the node.
     */
    public Node getNode() {
        return mNode;
    }

    /**
     * @return Returns the trace.
     */
    public String getTrace() {
        return mTrace;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return mType;
    }

    /**
     * @param code
     *            The code to set.
     */
    public void setCode(String code) {
        mCode = code;
    }

    /**
     * @param msg
     *            The msg to set.
     */
    public void setMsg(String msg) {
        mMsg = msg;
    }

    /**
     * @param node
     *            The node to set.
     */
    public void setNode(Node node) {
        mNode = node;
    }

    /**
     * @param trace
     *            The trace to set.
     */
    public void setTrace(String trace) {
        mTrace = trace;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(String type) {
        mType = type;
    }

    public String toString() {
        return "[code: " + this.mCode + ", type: " + this.mType + ", msg: "
                + this.mMsg + ", trace: " + this.mTrace + "]";
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
}
