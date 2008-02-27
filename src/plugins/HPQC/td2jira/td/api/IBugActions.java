package td2jira.td.api;

public interface IBugActions extends IBug {
	boolean hasChange();
	boolean isLocked();
	boolean isModified();
	boolean isAutoPost();
	void setAutoPost(boolean autoPost);
	void post();
	void lockObject();
	void unlockObject();
	void refresh();

	byte[] retrieveAttachmentData(String attachmentName);
	
	String getCommentsAsHTML();
	void setCommentsAsHTML(String s);
}
