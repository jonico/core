package td2jira.td;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.collabnet.connector.qc.IQCDefect;
import com.collabnet.connector.qc.QCDefect;

import td2jira.td.api.Comment;
import td2jira.td.api.ConnectionFactory;
import td2jira.td.api.IBug;
import td2jira.td.api.IBugActions;
import td2jira.td.api.ICommand;
import td2jira.td.api.IComment;
import td2jira.td.api.IConnection;
import td2jira.td.api.IFactory;
import td2jira.td.api.IFactoryList;
import td2jira.td.api.IFilter;
import td2jira.td.api.IRecordSet;
import td2jira.td.api.Utils;
import td2jira.td.api.dcom.Bug;
import td2jira.td.api.dcom.RecordSet;

public class TDConnector implements ITDConnector {
	public static Logger logger = Logger.getLogger(TDConnector.class);
	
	private IConnection tc;
	private List<IBug> tasks;
	
	public void login(String url,String domainName,String projectName,String user,String password) throws Exception {
		tc = ConnectionFactory.getInstance(url,domainName,projectName,user,password);
	}
	
	public List<IBug> getTasks() {
		IFactory bf = tc.getBugFactory();
		IFilter filter = bf.getFilter();
		IFactoryList fl = filter.getNewList();
		
		tasks = new ArrayList<IBug>();
		for( int i=1; i<=fl.getCount(); ++i ) {
			IBug bug = fl.getBug(i);
			tasks.add(bug);
		}

		fl.safeRelease();
		filter.safeRelease();
		bf.safeRelease();
		
		return tasks;
	}

	public List<IQCDefect> getDefectsWithIds(List<Integer> ids) {
		IFactory bf = tc.getBugFactory();
		IFilter filter = bf.getFilter();
		
		List<IQCDefect> tasks = new ArrayList<IQCDefect>();
		for( int i = 0 ; i < ids.size() ; ++i ) {
			filter.setFilter("BG_BUG_ID", ids.get(i).toString());
			IFactoryList fl = filter.getNewList();

			IBug bug = fl.getBug(1);
			QCDefect defect = new QCDefect((Bug)bug);

			tasks.add(defect);
			
			fl.safeRelease();
		}

		filter.safeRelease();
		bf.safeRelease();
		
		return tasks;
	}

	public void logout() {
		if( tasks != null ) {
			for (IBug tdIssue : tasks) {
				tdIssue.safeRelease();
			}
		}
		if( tc != null ) {
			tc.logout();
			tc.disconnect();
			tc.safeRelease();
			tc = null;
		}
	}
	
	public boolean assignTo(IBug tdIssue,String assignee) {
		try {
			if( assignee.equals(tdIssue.getAssignedTo()) ) return true;

			lockIssue(tdIssue);
			tdIssue.setAssignedTo(assignee);
			updateIssue(tdIssue);

			logger.info("assigned TD "+tdIssue.getId()+" to "+assignee);
			return true;
		} catch( Exception ex ) {
			logger.warn("  failed to assign to "+assignee+":"+ex.getMessage());
			return false;
		}
	}

	public List<String> getAttachmentsNames(IBug tdIssue) {
		return tdIssue.getAttachmentsNames();
	}

	public boolean hasAttachments(IBug tdIssue) {
		return tdIssue.hasAttachments();
	}

	public byte[] getAttachmentData(IBug tdIssue, String attachmentName) {
		IBugActions ba = (IBugActions) tdIssue;
		return ba.retrieveAttachmentData(attachmentName);
	}

	public void lockIssue(IBug tdIssue) {
		IBugActions ba = (IBugActions) tdIssue;
		ba.lockObject();
	}

	public void updateIssue(IBug tdIssue) {
		IBugActions ba = (IBugActions) tdIssue;
		ba.post();
	}

	public void addComment(IBug bug,IComment comment) {
		IBugActions ba = (IBugActions) bug;
		try {
			lockIssue(ba);
			String html = ba.getCommentsAsHTML();
			html = Utils.addCommentToHTML(html, comment);
			ba.setCommentsAsHTML(html);
			updateIssue(ba);
		} catch( Exception ex ) {
			logger.warn("failed to add comment to "+bug.getId()+":"+ex.getMessage());
		}
	}

	public List<Comment> getComments(IBug bug) {
		IBugActions ba = (IBugActions) bug;
		String comments = ba.getCommentsAsHTML();
		return Collections.unmodifiableList(Utils.splitComments(comments));
	}
	
}
