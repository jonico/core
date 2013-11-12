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

package com.collabnet.ccf.core.ga;

/**
 * 
 * The class AttachmentMetaData contains the fields of the attachment
 * GenericArtifact along with the possbile values for AttachmnetType and
 * AttachmentValueType.
 * 
 * @author venugopala
 * 
 */
public class AttachmentMetaData {

    /**
     * 
     * Possible values for the attachmentType, "data", "link" and "empty"
     * 
     */
    public enum AttachmentType {
        DATA, LINK, EMPTY
    }

    public enum AttachmentValueIsNull {
        TRUE, FALSE
    }

    public static final String    ATTACHMENT_NAME              = "attachmentName";
    public static final String    ATTACHMENT_SIZE              = "attachmentSize";
    public static final String    ATTACHMENT_SOURCE_URL        = "attachmentSourceUrl";
    public static final String    ATTACHMENT_TYPE              = "attachmentType";
    public static final String    ATTACHMENT_DESCRIPTION       = "attachmentDescription";
    public static final String    ATTACHMENT_MIME_TYPE         = "attachmentMIMEType";
    public static final String    ATTACHMENT_VALUE_HAS_CHANGED = "attachmentValueHasChanged";

    public static final String    ATTACHMENT_VALUE_IS_NULL     = "attachmentValueIsNull";
    public static final String    ATTACHMENT_DATA_FILE         = "attachmentDataFile";

    /**
     * 
     * This contains an array of attachment fields.
     * 
     */
    private static final String[] attachmentMetaData           = {
            ATTACHMENT_NAME, ATTACHMENT_SIZE, ATTACHMENT_SOURCE_URL,
            ATTACHMENT_TYPE, ATTACHMENT_MIME_TYPE,
            ATTACHMENT_VALUE_HAS_CHANGED, ATTACHMENT_VALUE_IS_NULL,
            ATTACHMENT_DESCRIPTION                            };

    public static final String    TEXT_PLAIN                   = "text/plain";                ;

    public static final String    TEXT_HTML                    = "text/html";                 ;

    public static String getAttachmentDescription() {
        return ATTACHMENT_DESCRIPTION;
    }

    public static String[] getAttachmentMetaData() {
        return attachmentMetaData;
    }

    public static String getAttachmentMimeType() {
        return ATTACHMENT_MIME_TYPE;
    }

    public static String getAttachmentName() {
        return ATTACHMENT_NAME;
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

    public static String getAttachmentValueHasChanged() {
        return ATTACHMENT_VALUE_HAS_CHANGED;
    };

    public static String getAttachmentValueIsNull() {
        return ATTACHMENT_VALUE_IS_NULL;
    }

}
