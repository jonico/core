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
    private String            namespace;
    private String            tagName;
    private String            displayName;
    private String            attributeType;

    private boolean           isReadOnly       = false;
    private boolean           isHidden         = false;
    private boolean           isRequired       = false;

    public TrackerAttribute(String displayName, String tagName,
            String namespace, boolean isReadOnly, boolean isHidden) {
        this(displayName, tagName, namespace, "_SHORT_TEXT");
        this.isHidden = isHidden;
        this.isReadOnly = isReadOnly;
    }

    public TrackerAttribute(String displayName, String tagName,
            String namespace, String attributeType) {
        this.displayName = displayName;
        this.tagName = tagName;
        this.namespace = namespace;
        this.attributeType = attributeType;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getKey() {
        return TrackerUtil.getKey(namespace, tagName);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getTagName() {
        return tagName;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

}
