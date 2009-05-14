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

package com.collabnet.ccf.pi.qc.v90.api;

import java.io.File;
import java.util.Date;
import java.util.List;

public interface IBug extends ILifeCycle {
	String getStatus();
	void setStatus(String status);

	String getSummary();
	void setSummary(String summary);

	String getDescription();
	void setDescription(String desc);

	String getAssignedTo();
	void setAssignedTo(String assignedTo);

	String getDetectedBy();
	void setDetectedBy(String detectedBy);

	String getPriority();
	void setPriority(String priority);

	String getSeverity();
	void setSeverity(String severity);

	String getProject();
	String getId();

	// Added by Madan
	public void post();
	public void undo();
	public void lockObject() throws DefectAlreadyLockedException;
	public void unlockObject();
	public File retrieveAttachmentData(String attachmentName);
	public void createNewAttachment(String fileName, int type);
	public void createNewAttachment(String filename, String description, int type);
	boolean hasAttachments();
	List<String> getAttachmentsNames();
//	public List<AttachmentData> getAttachmentData();
	public IAttachmentFactory getAttachmentFactory();

    public String getFieldAsString(String field);
    public Integer getFieldAsInt(String field);
    public Date getFieldAsDate(String field);
    public void setField(String field, String value);
}
