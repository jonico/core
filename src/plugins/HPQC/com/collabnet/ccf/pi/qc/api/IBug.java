package com.collabnet.ccf.pi.qc.api;

import java.util.Date;
import java.util.List;

import com.collabnet.ccf.pi.qc.AttachmentData;

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
	public byte[] retrieveAttachmentData(String attachmentName);

	boolean hasAttachments();
	List<String> getAttachmentsNames();
	public List<AttachmentData> getAttachmentData();
	
    public String getFieldAsString(String field);
    public int getFieldAsInt(String field);
    public Date getFieldAsDate(String field);
    public void setField(String field, String value);
}
