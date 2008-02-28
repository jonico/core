package td2jira.td;

import java.util.List;

import com.collabnet.ccf.pi.qc.api.Comment;
import com.collabnet.ccf.pi.qc.api.IBug;
import com.collabnet.ccf.pi.qc.api.IComment;


public interface ITDConnector {

	void login(String url, String domainName,String projectName, String user, String password) throws Exception;
	void logout();
	
	List<IBug> getTasks();
	boolean assignTo(IBug tdIssue, String assignee);
	
	boolean hasAttachments(IBug tdIssue);
	List<String> getAttachmentsNames(IBug tdIssue);
	byte[] getAttachmentData(IBug tdIssue, String attachmentName);
	
	void lockIssue(IBug tdIssue);
	void updateIssue(IBug tdIssue);
	
	void addComment(IBug tdIssue,IComment comment);
	List<Comment> getComments(IBug bug);
}