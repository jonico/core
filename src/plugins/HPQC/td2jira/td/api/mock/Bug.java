package td2jira.td.api.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import td2jira.td.api.IBugActions;

public class Bug implements IBugActions {
	private String id;
	private String assignedTo;
	private String status;
	private String summary;
	private String description;
	private String commentsAsHTML;
	private String detectedBy;
	private String severity;
	private Map<String,byte[]> attachments = new HashMap<String, byte[]>();
	
	public String getAssignedTo() {
		return assignedTo;
	}

	public String getDetectedBy() {
		return detectedBy;
	}

	public int getFieldAsInt(String field) {
		return 0;
	}

	public String getFieldAsString(String field) {
		return null;
	}

	public String getId() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}

	public String getPriority() {
		return null;
	}

	public String getProject() {
		return null;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	public String getStatus() {
		return status;
	}

	public String getSummary() {
		return summary;
	}

	public boolean hasAttachments() {
		return attachments.size()>0;
	}

	public boolean hasChange() {
		return false;
	}

	public boolean isAutoPost() {
		return false;
	}

	public boolean isLocked() {
		return false;
	}

	public boolean isModified() {
		return false;
	}

	public void lockObject() {
	}

	public void post() {
		try {
			ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(FactoryList.DIRNAME+File.separatorChar+getId()));
			ois.writeObject(this);
			ois.close();
		} catch( Exception ex ) {
			throw new RuntimeException(ex);
		}
	}

	public void refresh() {
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public void setAutoPost(boolean autoPost) {
	}

	public void setDetectedBy(String detectedBy) {
		this.detectedBy = detectedBy;
	}

	public void setField(String field, String value) {
	}

	public void setPriority(String priority) {
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void unlockObject() {
	}

	public void safeRelease() {
	}

	public static Bug getById(Long id) {
		String fileName = FactoryList.DIRNAME+File.separatorChar+id;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
			Bug bug = (Bug) ois.readObject();
			ois.close();
			return bug;
		} catch( InvalidClassException ex ) {
			new File(fileName).delete();
			return null;
		} catch( Exception ex ) {
			throw new RuntimeException(ex);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAttachmentsNames() {
		return new ArrayList<String>(attachments.keySet());
	}

	public String getCommentsAsHTML() {
		return commentsAsHTML;
	}

	public byte[] retrieveAttachmentData(String attachmentName) {
		return attachments.get(attachmentName);
	}

	public void setCommentsAsHTML(String s) {
		this.commentsAsHTML = s;
	}
	
	public void _addAttachment(String fileName,byte[] data) {
		attachments.put(fileName,data);
	}
}
