package td2jira;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import td2jira.jira.IJIRAConnector;
import td2jira.jira.JIRAIssue;
import td2jira.jira.xmlrpc.JIRAXmlRpcConnector;
import td2jira.sync.IssuePair;
import td2jira.sync.SyncUtils;
import td2jira.sync.impl.IProcessIssuePair;
import td2jira.td.ITDConnector;
import td2jira.td.TDConnector;
import td2jira.td.api.Comment;
import td2jira.td.api.IBug;
import td2jira.td.api.Utils;

public class Sync {
	private static Logger logger = Logger.getLogger(Sync.class);
	
	private static List<JIRAIssue> jiraTasks;
	private static List<IBug> tdTasks;
	private static String jiraProject;
	private static IJIRAConnector jc;
	private static ITDConnector tc;
	
    public static void main(String[] args) throws Exception {
    	Config.load();
    	Long syncMinutes = Long.parseLong(Config.SYNC_MINUTES);
    	
    	while(true) {
	    	try {
	    		logger.info("new sync started");
	        	jc = (IJIRAConnector) Class.forName(Config.JIRA_CONNECTOR_IMPLEMENTATION).newInstance();
	        	tc = (ITDConnector) Class.forName(Config.TD_CONNECTOR_IMPLEMENTATION).newInstance();
	        	Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
	    		run(args);
	    		logger.info("sync done.");
	    		safeLogout();
	    	} catch( Exception ex ) {
	    		logger.error(ex);
	    	} finally {
    			logger.debug("sleeping for "+syncMinutes+"min");
    			try { Thread.sleep(syncMinutes*60*1000); } catch (InterruptedException e) {}
	    	}
    	} 
    }

	private static void safeLogout() {
		try {
			if( tc != null ) tc.logout();
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
    	tc = null;
    	
		try {
			if( jc != null ) jc.logout();
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
    	jc = null;
	}
	
	public static void run(String[] args) throws Exception {
    	String jiraUrl = Config.JIRA_URL;
    	String jiraUser = Config.JIRA_USER; 
    	String jiraPassword = Config.JIRA_PASSWORD;
    	jiraProject = Config.JIRA_PROJECT;
    	
    	String tdUrl = Config.TD_URL;
    	String tdDomain = Config.TD_DOMAIN;
    	String tdProject = Config.TD_PROJECT; 
    	String tdUser = Config.TD_USER;
    	String tdPassword = Config.TD_PASSWORD;
    	
    	jc.login(jiraUrl,jiraProject,jiraUser,jiraPassword);
    	logger.debug("logged into JIRA");
    	
    	tc.login(tdUrl, tdDomain, tdProject, tdUser, tdPassword);
    	logger.debug("logged into TD");
    	
    	jiraTasks = jc.findTasks(Config.JIRA_SUMMARY_PREFIX); 
		Comparator<JIRAIssue> recentFirst = new Comparator<JIRAIssue>() {
			public int compare(JIRAIssue arg0, JIRAIssue arg1) {
				JIRAIssue ji1 = (JIRAIssue) arg0;
				JIRAIssue ji2 = (JIRAIssue) arg1;
				return ji2.getKey().compareTo(ji1.getKey());
			}
		};		
		Collections.sort(jiraTasks,recentFirst);
    	jiraTasks = filterByProject(jiraTasks,jiraProject);
    	logger.debug("found "+jiraTasks.size()+" JIRA tasks came from TD in project "+jiraProject);
    	
    	tdTasks = getTDIssues(); 
    	logger.debug("found "+tdTasks.size()+" tasks in TD project "+tdProject);
    	
    	for(int item=0; item<tdTasks.size(); item++ ) {
    		IBug tdIssue = tdTasks.get(item);
    		if( Config.SYNC_ONLY_TD_ID != null && !Config.SYNC_ONLY_TD_ID.equals(tdIssue.getId()) ) continue; 
			JIRAIssue latestJiraIssue = findLatestJIRAIssue(tdIssue);
			processRules(tdIssue, latestJiraIssue);
    	}
	}

	private static void processRules(IBug tdIssue, JIRAIssue latestJiraIssue) throws Exception {
		String rules = Config.SYNC_RULES;
		String[] r = rules.split(";");
		
		for( int i=0; i<r.length; i++ ) {
			String rule = r[i].trim();
			
			if( rule.length() == 0 ) continue;
			
			boolean matches = true;
			String ruleClass = null;
			
			String[] tokens = rule.split(",");
			for (String token : tokens) {
				String[] keyValue = token.split("=");
				if( keyValue[0].startsWith("td") ) {
					if( !ruleMatches(tdIssue,keyValue[0],keyValue[1]) ) {
						matches = false;
						break;
					}
				} else if( keyValue[0].startsWith("jira") ) {
					if( !ruleMatches(latestJiraIssue,keyValue[0],keyValue[1]) ) {
						matches = false;
						break;
					}
				} else if( keyValue[0].equals("class") ) {
					ruleClass = keyValue[1];
				} 
			}
			
			if( matches && ruleClass != null ) {
				IProcessIssuePair processor = (IProcessIssuePair) Class.forName(ruleClass).newInstance();
				processor.setJiraConnector(jc);
				processor.setTdConnector(tc);
				IProcessIssuePair.ProcessingResult result = processor.process(latestJiraIssue, tdIssue);
				if( result == IProcessIssuePair.ProcessingResult.PROCESS_NEXT_ISSUE ) break;
			}
		}
	}

	private static boolean ruleMatches(Object issue, String key, String values) {
		// special case: jira=null or td=null
		if( key.indexOf('.') < 0 ) {
			return values.equals("null") && issue == null;
		}
		
		key = key.substring(key.indexOf('.')+1);
		
		String[] vals = values.split("\\|");
		for (String val : vals) {
			if( ruleMatchesProperty(issue,key,val) ) {
				return true;
			}
		}

		return false;
	}

	private static boolean ruleMatchesProperty(Object issue, String key, String val) {
		String prop = (""+key.charAt(0)).toUpperCase()+key.substring(1);
		
		try {
			Method m = issue.getClass().getMethod("get"+prop, new Class[]{});
			m.setAccessible(true);
			String v = (String) m.invoke(issue,new Object[]{});
			return val.equals(v);
		} catch( Exception ex ) {
		}
		
		try {
			Method m = issue.getClass().getMethod("is"+prop, new Class[]{});
			m.setAccessible(true);
			String v = (String) m.invoke(issue,new Object[]{});
			return val.equals(v);
		} catch( Exception ex ) {
		}
		
		return false;
	}
    
	private static List<IBug> getTDIssues() throws Exception {
    	List<IBug> tdIssues = tc.getTasks();
		return tdIssues;
	}
	
	private static JIRAIssue findLatestJIRAIssue(IBug tdTask) {
		JIRAIssue found = null;
		
    	for (JIRAIssue jiraTask : jiraTasks ) {
	    	String pattern1 = ".*"+Config.JIRA_SUMMARY_PREFIX.toUpperCase()+"\\s"+tdTask.getId()+"[^0-9].*";
			if( jiraTask.getSummary().toUpperCase().matches(pattern1) ) {
				if( found == null ) {
					found = jiraTask;
				} else {
					if( firstFresherThenSecond(jiraTask, found) ) found = jiraTask;
				}
			}
		}
    	
    	return found;
    }

	private static boolean firstFresherThenSecond(JIRAIssue i1, JIRAIssue i2) {
		if( i1.getStatus().equals("Closed") ) return false;
		if( i2.getStatus().equals("Closed") ) return true;
		return jiraIdAsNumber(i1.getKey()) > jiraIdAsNumber(i2.getKey());
	}
	
	private static Long jiraIdAsNumber(String id) {
		int dash = id.indexOf('-');
		return Long.parseLong(id.substring(dash+1));
	}
	
	private static List<JIRAIssue> filterByProject(List<JIRAIssue> tasks,String project) {
		Iterator<JIRAIssue> it = tasks.iterator();
		while( it.hasNext() ) {
			JIRAIssue task = it.next();
			if( !project.equals(task.getProjectId()) ) it.remove();
		}
		return tasks;
	}
}
