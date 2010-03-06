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
import com.collabnet.ccf.pi.qc.v90.api.Utils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.DateUtilities;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Bug extends ActiveXComponent implements IBugActions {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger(Bug.class);

    public Bug(Dispatch arg0)
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

    public String getSummary()
    {
        return getPropertyAsString("Summary");
    }

    public String getAssignedTo()
    {
        return getPropertyAsString("AssignedTo");
    }

    public void setAssignedTo(String assignedTo)
    {
        setProperty("AssignedTo", assignedTo);
    }

    public String getDetectedBy()
    {
    	// getFieldAsString("BG_DETECTED_BY")
        return getPropertyAsString("DetectedBy");
    }

    public void setDetectedBy(String detectedBy)
    {
        setProperty("DetectedBy", detectedBy);
    }

    public String getFieldAsString(String field)
    {
        Variant res = Dispatch.call(this, "Field", field);
        if(res.isNull()){
        	return null;
        } else if(res.getvt() == 3){
        	int val = res.getInt();
        	return Integer.toString(val);
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
        if(res.isNull() || res.getvt() == 9){
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

    public String getSeverity() {
        return getFieldAsString("BG_SEVERITY");
    }

    public void setSeverity(String severity) {
        setProperty("BG_SEVERITY", severity);
    }

    public void setPriority(String priority) {
        setProperty("Priority", priority);
    }

    public String getProject() {
        return getPropertyAsString("Project");
    }

    public void setSummary(String summary) {
        setProperty("Summary", summary);
    }

    public boolean isAutoPost() {
        return getPropertyAsBoolean("AutoPost");
    }

    public void setAutoPost(boolean autoPost) {
        setProperty("Autopost", autoPost);
    }

    public void post() {
        Dispatch.call(this, "Post");
    }

    public void undo() {
        Dispatch.call(this, "Undo");
    }

    public void lockObject() throws DefectAlreadyLockedException {
        if( isLocked() ) {
            throw new DefectAlreadyLockedException("Bug locked by another user");
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

	@Deprecated
	public void addComment(Comment append) {
		try {
			lockObject();
			String s = getFieldAsString("BG_DEV_COMMENTS");
			if( s == null || s.trim().length() == 0 ) s = "<html><body></body></html>";

			int notBody = s.indexOf("</body></html>");
			s = s.substring(0,notBody);
			s += Utils.formatComment(append);
			s += "</body></html>";

			setField("BG_DEV_COMMENTS", s);
			post();
		} catch( Exception ex ) {
			logger.warn("failed to add comment to "+getId()+":"+ex.getMessage());
		} finally {
			unlockObject();
		}
	}

	public List<String> getAttachmentsNames() {
		IFactoryList attachments = new BugFactory(getPropertyAsComponent("Attachments")).getFilter().getNewList();
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
			IFactoryList attachments = new BugFactory(getPropertyAsComponent("Attachments")).getFilter().getNewList();
			for( int n=1; n<=attachments.getCount(); ++n ) {
				Dispatch item = attachments.getItem(n);
				String fileName = Dispatch.get(item, "FileName").toString();
				if( !fileName.endsWith(attachmentName) ) continue;
				//Dispatch.get(item, "Data");
				Dispatch.call(item,"Load",true,"");
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
							logger.warn("Thread interrupted while reading the attchment", e);
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
		IAttachmentFactory attachmentFactory = null;
		IAttachment attachment = null;
		try {
			attachmentFactory = new AttachmentFactory(getPropertyAsComponent("Attachments"));
			attachment = attachmentFactory.addItem();
	
			attachment.putFileName(fileName);
			File file = new File(fileName);
			
			attachment.putType(type);
			if(description != null) {
				attachment.putDescription(description);
			}
			attachment.post();
		} finally {
			if (attachment != null) {
				attachment.safeRelease();
				attachment = null;
			}
			if (attachmentFactory != null) {
				attachmentFactory.safeRelease();
				attachmentFactory = null;
			}
		}
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

	public String getCommentsAsHTML() {
		return getFieldAsString("BG_DEV_COMMENTS");
	}

	public void setCommentsAsHTML(String s) {
		setField("BG_DEV_COMMENTS", s);
	}

//	public List<AttachmentData> getAttachmentData() {
//
//		List<AttachmentData> attachmentDataList = new ArrayList<AttachmentData>();
//
//		List<String> attachmentNames = getAttachmentsNames();
//		for (String attachmentName:attachmentNames) {
//			byte [] contents = retrieveAttachmentData(attachmentName);
//			AttachmentData attachmentData = new AttachmentData();
//			attachmentData.setName(attachmentName);
//			attachmentData.setContents(contents);
//			attachmentDataList.add(attachmentData);
//		}
//
//		return attachmentDataList;
//	}
}
