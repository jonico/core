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

public interface IRequirement extends ILifeCycle {
    public void clearListValuedField(String fieldName);

    public String createNewAttachment(String fileName, int type);

    public String createNewAttachment(String filename, String description,
            int type);

    public IAttachmentFactory getAttachmentFactory();

    //public void setFieldAsInt(String field, Integer value);
    public Date getFieldAsDate(String field);

    public Integer getFieldAsInt(String field);

    public String getFieldAsString(String field);

    public void lockObject() throws DefectAlreadyLockedException;

    public void move(int newParentId, int position);

    public void post();

    public File retrieveAttachmentData(String attachmentName,
            long delayBeforeAttachmentDownload, long maximumAttachmentRetryCount);

    public void setField(String field, String value);

    public void setListValuedField(String fieldName, String fieldValue);

    public void undo();

    public void unlockObject();

    String getAuthor();

    String getComment();

    int getCount();

    String getDescription();

    String getId();

    String getName();

    String getParagraph();

    String getParentId();

    String getPath();

    //public void setFieldAsDate(String field, Date value);
    String getPriority();

    String getProduct();

    String getReviewed();

    String getStatus();

    String getType();

    String getTypeId();

    IVersionControl getVersionControlObject();

    boolean getVirtual();

    boolean hasAttachments();

    boolean isLocked();

    boolean isModified();

    void setAuthor(String author);

    void setComment(String comment);

    void setDescription(String description);

    void setName(String name);

    void setParagraph(String paragraph);

    void setParentId(String parentId);

    void setPriority(String priority);

    void setProduct(String product);

    void setReviewed(String reviewed);

    void setStatus(String status);

    void setTypeId(String typeId);
}
