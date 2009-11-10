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

package com.collabnet.ccf.pi.qc.v90.api.dcom;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.Comment;
import com.collabnet.ccf.pi.qc.v90.api.DefectAlreadyLockedException;
import com.collabnet.ccf.pi.qc.v90.api.IAttachment;
import com.collabnet.ccf.pi.qc.v90.api.IAttachmentFactory;
import com.collabnet.ccf.pi.qc.v90.api.IBugActions;
import com.collabnet.ccf.pi.qc.v90.api.IFactoryList;
import com.collabnet.ccf.pi.qc.v90.api.IRequirementsActions;
import com.collabnet.ccf.pi.qc.v90.api.Utils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.DateUtilities;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Requirement extends ActiveXComponent implements IRequirementsActions {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger(Requirement.class);

    public Requirement(Dispatch arg0)
    {
        super(arg0);
    }

    public String getStatus()
    {
        return getPropertyAsString("Status");
    }

    public void setStatus(String status)
    {
        setProperty("Status", status);
    }

    public String getId()
    {
    	return Integer.toString(getPropertyAsInt("ID"));
    }

    public String getFieldAsString(String field)
    {
        Variant res = Dispatch.call(this, "Field", field);
        if(res.isNull()){
        	return null;
        } else if(res.getvt() == 3){
        	int val = res.getInt();
        	return Integer.toString(val);
        } else if (res.getvt() == 7) {
        	logger.warn("Field "+field+" should have contained a string but contained a date: "+res.getDate());
        	return null;
        }
        else {
        	return res.getString();
        }
    }

    public Date getFieldAsDate(String field)
    {
        Variant res = Dispatch.call(this, "Field", field);
        double ddate = 0.0;
        if(res.isNull()){
        	return null;
        } 
        else if (res.getvt() == 8) {
        	logger.warn("Field "+field+" should have contained a date but contained a string: "+res.getString());
        }
        else {
        	ddate = res.getDate();
        }
        Date d = DateUtilities.convertWindowsTimeToDate(ddate);
        return d;
    }

    public Date getFieldAsDate(String field, String timeZone)
    {
        Variant res = Dispatch.call(this, "Field", field);
        double ddate = 0.0;
        if(res.isNull()){
        	return null;
        }
        else {
        	ddate = res.getDate();
        }
        Date d = DateUtilities.convertWindowsTimeToDate(ddate);
        d = DateUtil.convertDateToTimeZone(d, timeZone);
        return d;
    }

    public Integer getFieldAsInt(String field)
    {
        Variant res = Dispatch.call(this, "Field", field);
        if(res.isNull() || res.getvt() == 9) {
        	return null;
        } else if (res.getvt() == 8) {
        	logger.warn("Field "+field+" should have contained an int but contained a string: "+res.getString());
        	return null;
        }
        else {
        	return res.getInt();
        }
    }

    public void setField(String field, String value) {
        Dispatch.invoke(this, "Field", 4, new Object[] { field, value }, new int[2]);
    }

    public boolean hasChange() {
        return getPropertyAsBoolean("HasChange");
    }

    public boolean isLocked() {
        return getPropertyAsBoolean("IsLocked");
    }

    public boolean isModified() {
        return getPropertyAsBoolean("Modified");
    }

    public String getPriority() {
        return getPropertyAsString("Priority");
    }

    public void lockObject() throws DefectAlreadyLockedException {
        if( isLocked() ) {
            throw new DefectAlreadyLockedException("Requirement locked by another user");
        } else {
            Dispatch.call(this, "LockObject");
            return;
        }
    }

    public void unlockObject() {
        Dispatch.call(this, "UnlockObject");
    }

    public void refresh() {
        Dispatch.call(this, "Refresh");
    }

	public String getDescription() {
		return getFieldAsString("BG_DESCRIPTION");
	}

	public void setDescription(String desc) {
		setField("BG_DESCRIPTION", desc);
	}

	public List<String> getAttachmentsNames() {
		IFactoryList attachments = new RequirementsFactory(getPropertyAsComponent("Attachments")).getFilter().getNewList();
		List<String> att = new ArrayList<String>();
		for( int n=1; n<=attachments.getCount(); ++n ) {
			Dispatch item = attachments.getItem(n);
			String fileName = Dispatch.get(item, "FileName").toString();

			String shortName = fileName;
			int slash = shortName.lastIndexOf(File.separatorChar);
			if( slash >= 0 ) {
				shortName = shortName.substring(slash+1);
			}

			att.add(shortName);
		}

		return att;
	}

	public File retrieveAttachmentData(String attachmentName) {
		int size = 0;
		int maxAttachmentUploadWaitCount = 10;
		int waitCount = 0;
		do{
			IFactoryList attachments = new RequirementsFactory(getPropertyAsComponent("Attachments")).getFilter().getNewList();
			for( int n=1; n<=attachments.getCount(); ++n ) {
				Dispatch item = attachments.getItem(n);
				String fileName = Dispatch.get(item, "FileName").toString();
				if( !fileName.endsWith(attachmentName) ) continue;
				Dispatch.get(item, "Data");
				size = Dispatch.get(item, "FileSize").getInt();
				if(size == 0) {
					if(++waitCount > maxAttachmentUploadWaitCount){
						String message = "The QC attachment reader has waited enough. "
							+"But the attachment upload is still going on. "
							+"Probably the user is attaching a huge file. "
							+"So skipping this attachment.";
						logger.warn(message);
						return null;
					}
					else {
						logger.info("Attachment "+ attachmentName +" is still being uploaded. Wait cycle number "+waitCount);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							logger.warn("Thread interrupted while reading the attachment", e);
						}
						break;
					}
				}
				else {
					logger.debug("Attachment "+ attachmentName +" has been read.");
					File attachmentFile = new File(fileName);
					if(!attachmentFile.exists()){
						String message = "The attachment File " + fileName +" does not exist";
						logger.error(message);
						throw new CCFRuntimeException(message);
					}
					return attachmentFile;
				}
			}
		} while(size == 0);

		throw new IllegalArgumentException("no data found");
	}

	public void createNewAttachment(String fileName, String description, int type) {
		IAttachmentFactory attachmentFactory = new AttachmentFactory(getPropertyAsComponent("Attachments"));
		IAttachment attachment = attachmentFactory.addItem();

		attachment.putFileName(fileName);
		attachment.putType(type);
		if(description != null) {
			attachment.putDescription(description);
		}
		attachment.post();

		return;
	}

	public void createNewAttachment(String fileName, int type) {
		this.createNewAttachment(fileName, null, type);
		return;
	}

	public IAttachmentFactory getAttachmentFactory(){
		IAttachmentFactory attachmentFactory = new AttachmentFactory(getPropertyAsComponent("Attachments"));
		return attachmentFactory;
	}

	public boolean hasAttachments() {
        return getPropertyAsBoolean("HasAttachment");
    }

	public String getAuthor() {
		return getPropertyAsString("Author");
	}

	public String getComment() {
		return getPropertyAsString("Comment");
	}

	public int getCount() {
		return getPropertyAsInt("ParentId");
	}

	public String getName() {
		return getPropertyAsString("Name");
	}

	public String getParagraph() {
		return getPropertyAsString("Paragraph");
	}

	public String getParentId() {
		return Integer.toString(getPropertyAsInt("ParentId"));
	}

	public String getPath() {
		return getPropertyAsString("Path");
	}

	public String getProduct() {
		return getPropertyAsString("Product");
	}

	public String getReviewed() {
		return getPropertyAsString("Reviewed");
	}

	public String getType() {
		return getPropertyAsString("Type");
	}

	public String getTypeId() {
		return getPropertyAsString("TypeId");
	}

	public boolean getVirtual() {
		return getPropertyAsBoolean("Virtual");
	}

	public void post() {
        Dispatch.call(this, "Post");
    }

    public void undo() {
        Dispatch.call(this, "Undo");
    }

	public void setAuthor(String author) {
		setProperty("Author" , author);
		
	}

	public void setComment(String comment) {
		setProperty("Comment" , comment);
	}

	public void setName(String name) {
		setProperty("Name" , name);
		
	}

	public void setParagraph(String paragraph) {
		setProperty("Paragraph" , paragraph);
		
	}

	public void setParentId(String parentId) {
		setProperty("ParentId", Integer.parseInt(parentId));
	}

	public void setPriority(String priority) {
		setProperty("Priority", priority);
	}

	public void setProduct(String product) {
		setProperty("Product", product);
	}

	public void setReviewed(String reviewed) {
		setProperty("Reviewed", reviewed);
	}

	public void setTypeId(String typeId) {
		setProperty("TypeId", typeId);
	}

	public void move(int newParentId, int position) {
		// Dispatch.invoke(this, "Move", 4, new Object[] { newParentId, position }, new int[2]);
		Dispatch.call(this, "Move", newParentId, position);
	}

}
