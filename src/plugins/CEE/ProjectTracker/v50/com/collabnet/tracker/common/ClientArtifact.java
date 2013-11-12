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

    private String                         mTagName;

    private String                         mNamespaceURI;

    // attributes where their value was text
    private Map<String, List<String>>      textAttributes = new HashMap<String, List<String>>();

    private Map<String, String>            namespaceMap   = new HashMap<String, String>();

    private List<ClientArtifactComment>    comments       = new ArrayList<ClientArtifactComment>();

    private List<ClientArtifactAttachment> attachments    = new ArrayList<ClientArtifactAttachment>();

    public ClientArtifact() {
        super();
    }

    public ClientArtifact(String id) {
        this();
        addAttributeValue("urn:ws.tracker.collabnet.com", "id", id);
    }

    public void addAttachment(ClientArtifactAttachment attachment) {
        attachments.add(attachment);
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

    public void addComment(ClientArtifactComment comment) {
        comments.add(comment);

    }

    public boolean equals(Object o) {
        if (!(o instanceof ClientArtifact))
            return false;
        return ((ClientArtifact) o).getArtifactID().equalsIgnoreCase(
                getArtifactID());
    }

    public String getArtifactID() {
        return getAttributeValue("urn:ws.tracker.collabnet.com", "id");
    }

    public List<ClientArtifactAttachment> getAttachments() {
        Collections.sort(attachments,
                new Comparator<ClientArtifactAttachment>() {

                    public int compare(ClientArtifactAttachment o1,
                            ClientArtifactAttachment o2) {
                        Long date1 = Long.parseLong(o1.getCreatedOn());
                        Long date2 = Long.parseLong(o2.getCreatedOn());
                        return date1.compareTo(date2);
                    }

                });
        return attachments;
    }

    public Map<String, List<String>> getAttributes() {
        return textAttributes;
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
        List<String> values = textAttributes.get(TrackerUtil.getKey(namespace,
                tagname));
        if (values == null) {
            return null;
        }
        return (String) values.get(0);
    }

    /**
     * @param namespace
     *            the namespace URI of the attribute
     * @param tagName
     *            the XML name of the attribute
     * @return the values of the attribute
     */
    public String[] getAttributeValues(String namespace, String tagName) {
        List<String> values = textAttributes.get(TrackerUtil.getKey(namespace,
                tagName));
        if (values == null) {
            return null;
        }
        String[] result = new String[values.size()];
        values.toArray(result);
        return result;
    }

    public List<ClientArtifactComment> getComments() {
        Collections.sort(comments, new Comparator<ClientArtifactComment>() {

            public int compare(ClientArtifactComment o1,
                    ClientArtifactComment o2) {
                Long date1 = Long.parseLong(o1.getCommentDate());
                Long date2 = Long.parseLong(o2.getCommentDate());
                return date1.compareTo(date2);
            }

        });
        return comments;
    }

    /**
     * @return Returns the namespaceURI.
     */
    public String getNamespace() {
        return mNamespaceURI;
    }

    public String getNamespacesForTagName(String tagName) {
        return namespaceMap.get(tagName);
    }

    public String getProject() {
        return getAttributeValue("urn:ws.tracker.collabnet.com", "project");
    }

    /**
     * @return Returns the tagName.
     */
    public String getTagName() {
        return mTagName;
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
     * Sets the XML name of the artifact
     * 
     * @param tagName
     *            the XML name
     */
    public void setTagName(String tagName) {
        mTagName = tagName;

    }

    private void addToNamespaceMap(String namespace, String tagName) {
        namespaceMap.put(tagName, namespace);
    }
}
