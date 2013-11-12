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

package com.collabnet.ccf.pi.qc.v90.api;

import java.io.File;
import java.util.Date;
import java.util.List;

public interface IBug extends ILifeCycle {
    public String createNewAttachment(String fileName, int type);

    public String createNewAttachment(String filename, String description,
            int type);

    //	public List<AttachmentData> getAttachmentData();
    public IAttachmentFactory getAttachmentFactory();

    public Date getFieldAsDate(String field);

    public Integer getFieldAsInt(String field);

    public String getFieldAsString(String field);

    public void lockObject() throws DefectAlreadyLockedException;

    // Added by Madan
    public void post();

    public File retrieveAttachmentData(String attachmentName,
            long delayBeforeAttachmentDownload, long maximumAttachmentRetryCount);

    public void setField(String field, String value);

    public void undo();

    public void unlockObject();

    String getAssignedTo();

    List<String> getAttachmentsNames();

    String getDescription();

    String getDetectedBy();

    String getId();

    String getPriority();

    String getProject();

    String getSeverity();

    String getStatus();

    String getSummary();

    boolean hasAttachments();

    void setAssignedTo(String assignedTo);

    void setDescription(String desc);

    void setDetectedBy(String detectedBy);

    void setPriority(String priority);

    void setSeverity(String severity);

    void setStatus(String status);

    void setSummary(String summary);
}
