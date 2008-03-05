package com.collabnet.ccf.pi.qc.api.dcom;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;


import com.collabnet.ccf.pi.qc.AttachmentData;
import com.collabnet.ccf.pi.qc.api.Comment;
import com.collabnet.ccf.pi.qc.api.IBugActions;
import com.collabnet.ccf.pi.qc.api.IFactoryList;
import com.collabnet.ccf.pi.qc.api.Utils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Bug extends ActiveXComponent implements IBugActions {
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
        return getPropertyAsString("ID");
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
        return res.getString();
    }

    public Date getFieldAsDate(String field)
    {
        Variant res = Dispatch.call(this, "Field", field);
        double ddate = res.getDate();
        Date d = new Date((long)ddate);
        return d;
    }

    public int getFieldAsInt(String field)
    {
        Variant res = Dispatch.call(this, "Field", field);
        return res.getInt();
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

    public void lockObject() {
        if( isLocked() ) {
            throw new RuntimeException("Bug locked by another user");
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
		}
	}

	public List<String> getAttachmentsNames() {
		IFactoryList attachments = new Factory(getPropertyAsComponent("Attachments")).getFilter().getNewList();
		
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
	
	public byte[] retrieveAttachmentData(String attachmentName) {
		IFactoryList attachments = new Factory(getPropertyAsComponent("Attachments")).getFilter().getNewList();
		
		for( int n=1; n<=attachments.getCount(); ++n ) {
			Dispatch item = attachments.getItem(n);
			String fileName = Dispatch.get(item, "FileName").toString();
			
			if( !fileName.endsWith(attachmentName) ) continue;
			
			Dispatch.get(item, "Data");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
				byte[] buf = new byte[1024];
				while( fis.available() > 0 ) {
					 int cnt = fis.read(buf);
					 baos.write(buf, 0, cnt);
				}
				fis.close();
				baos.close();
			} catch( Exception ex ) {
				throw new RuntimeException(ex);
			}

			return baos.toByteArray();
		}
		
		throw new IllegalArgumentException("no data found");
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
	
	public List<AttachmentData> getAttachmentData() {

		List<AttachmentData> attachmentDataList = new ArrayList<AttachmentData>();

		List<String> attachmentNames = getAttachmentsNames();
		for (String attachmentName:attachmentNames) {
			byte [] contents = retrieveAttachmentData(attachmentName);
			AttachmentData attachmentData = new AttachmentData();
			attachmentData.setName(attachmentName);
			attachmentData.setContents(contents);
			attachmentDataList.add(attachmentData);
		}
		
		return attachmentDataList;
	}
}
