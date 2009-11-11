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

public interface IRequirement extends ILifeCycle {	
	public void post();
	public void undo();
	public void move(int newParentId, int position);
	public void lockObject() throws DefectAlreadyLockedException;
	public void unlockObject();
	public void createNewAttachment(String fileName, int type);
	public void createNewAttachment(String filename, String description, int type);
	public IAttachmentFactory getAttachmentFactory();
	public File retrieveAttachmentData(String attachmentName);
	boolean hasAttachments();
	
	public String getFieldAsString(String field);
	public void setField(String field, String value);
	public Integer getFieldAsInt(String field);
	//public void setFieldAsInt(String field, Integer value);
	public Date getFieldAsDate(String field);
	//public void setFieldAsDate(String field, Date value);
	String getPriority();
	void setPriority(String priority);
	String getDescription();
	void setDescription(String description);
	String getStatus();
	void setStatus(String status);
	String getTypeId();
	void setTypeId(String typeId);
	String getAuthor();
	void setAuthor(String author);
	String getComment();
	void setComment(String comment);
	int getCount();
	String getName();
	void setName(String name);
	String getParagraph();
	void setParagraph(String paragraph);
	String getParentId();
	void setParentId(String parentId);
	String getPath();
	String getReviewed();
	void setReviewed(String reviewed);
	String getType();
	boolean getVirtual();
	String getProduct();
	void setProduct(String product);
	String getId();
	boolean isLocked();
	boolean isModified();
}
