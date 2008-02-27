package td2jira.td.api.mock;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InvalidClassException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import td2jira.Config;
import td2jira.td.api.Comment;
import td2jira.td.api.IBug;
import td2jira.td.api.IBugActions;
import td2jira.td.api.IFactoryList;
import td2jira.td.api.Utils;

import com.jacob.com.Dispatch;

public class FactoryList implements IFactoryList {
	public static Logger logger = Logger.getLogger(FactoryList.class);
	
	private List<Long> bugs; 
	
	public IBug getBug(int i) {
		if( bugs == null ) {
			bugs = loadBugs();
		}
		
		Long id = bugs.get(i-1);
		return Bug.getById(id);
	}

	public int getCount() {
		if( bugs == null ) {
			bugs = loadBugs();
		}
		return bugs.size();
	}

	public String getItemString(int i) {
		return null;
	}

	public static final String DIRNAME = ".td2jira.td.api.mock.issues";
	public static final String FILENAME = DIRNAME+File.separatorChar+"ser";
	
	public void safeRelease() {
		try {
			ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(FILENAME));
			ois.writeObject(bugs);
			ois.close();
		} catch( Exception ex ) {
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	private static List<Long> loadBugs() {
		new File(DIRNAME).mkdirs();
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME));
			List<Long> bugs = (List<Long>) ois.readObject();
			ois.close();
			
			if( bugs == null ) bugs = new ArrayList<Long>();
			
			double newIssueProbability = Double.parseDouble(Config.TD_MOCK_NEW_ISSUE_PROBABILITY);
			double newCommentProbability = Double.parseDouble(Config.TD_MOCK_NEW_COMMENT_PROBABILITY);
			double changeStatusProbability = Double.parseDouble(Config.TD_MOCK_CHANGE_STATUS_PROBABILITY);
			double newAttachmentProbability = Double.parseDouble(Config.TD_MOCK_NEW_ATTACHMENT_PROBABILITY);
			
			while( bugs.size() < 10 ) {
				addNewBug(bugs);
			}

			if( Math.random()*100 < newIssueProbability ) {
				addNewBug(bugs);
			}

			for (Long bugId : bugs) {
				if( Math.random()*100 < newCommentProbability ) {
					addNewComment(bugId);
				}

				if( Math.random()*100 < newAttachmentProbability ) {
					addNewAttachment(bugId);
				}
				
				if( Math.random()*100 < changeStatusProbability ) {
					changeStatus(bugId);
				}
			}
			
			return bugs;
		} catch( InvalidClassException ex ) {
			new File(FILENAME).delete();
			return new ArrayList<Long>();
		} catch( FileNotFoundException ex ) {
			return new ArrayList<Long>();
		} catch( Exception ex ) {
			throw new RuntimeException(ex);
		}
	}

	private static void addNewAttachment(Long bugId) {
		Bug bug = Bug.getById(bugId);
		
		String fileName = randomFileName();
		byte[] data = randomFileData(fileName);
		logger.debug("TD "+bugId+": adding attachment "+fileName+" of size "+data.length);
		bug._addAttachment(fileName, data);
		bug.post();
	}

	private static void changeStatus(Long bugId) {
		IBug bug = Bug.getById(bugId);
		String transitions = Config.TD_MOCK_TRANSITIONS;
		if( transitions == null ) throw new IllegalArgumentException("missed td.mock.transitions property");
		String[] trans = transitions.split(",");
		
		List<String> possibleTransitions = new ArrayList<String>();
		for (String t : trans) {
			String[] statuses = t.split(">");
			if( bug.getStatus().equals(statuses[0]) ) {
				possibleTransitions.add(statuses[1]);
			}
		}
		
		if( possibleTransitions.size() == 0 ) return;

		String oldStatus = bug.getStatus();
		int pos = (int) (Math.random()*possibleTransitions.size());
		bug.setStatus(possibleTransitions.get(pos));
		((IBugActions) bug).post();
		logger.warn("TD bug "+bug.getId()+" changed status from "+oldStatus+" to "+bug.getStatus());
	}

	private static void addNewComment(Long id) {
		IBug bug = Bug.getById(id);
		IBugActions ba = (IBugActions) bug;
		Comment c = new Comment();
		c.setAuthor(FactoryList.randomTdUser());
		c.setBody(FactoryList.randomSentence(10000000));
		c.setCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		String s = ba.getCommentsAsHTML();
		if( s == null || s.trim().length() == 0 ) s = "<html><body></body></html>";
		
		int notBody = s.indexOf("</body></html>");
		s = s.substring(0,notBody);
		s += Utils.formatComment(c);
		s += "</body></html>";
		
		ba.setCommentsAsHTML(s);
		ba.post();
		
		logger.warn("TD bug "+bug.getId()+" got new comment");
	}	
	
	private static void addNewBug(List<Long> bugs) {
		Long maxId = 0l;
		for (Long id : bugs) {
			maxId = Math.max(maxId, id);
		}
		maxId++;
		
		Bug bug = new Bug();
		bug.setID(""+maxId);
		bug.setStatus(randomOf("Assigned","Open","Reopen","Fixed","Closed","New","Rejected"));
		bug.setAssignedTo(randomTdUser());
		bug.setSummary(randomSentence(100));

		// description
		StringBuilder sb = new StringBuilder();
		int paras = (int) (1+Math.random()*10);
		for(int i=0; i<paras; ++i) {
			String para = randomSentence(1000000);
			sb.append(para);
			sb.append("\n\n");
		}
		bug.setDescription(sb.toString());
		
		// comments
		String html = "";
		paras = (int) (1+Math.random()*10);
		for(int i=0; i<paras; ++i) {
			String para = randomSentence(1000000);
			Comment c = new Comment();
			c.setAuthor(randomTdUser());
			c.setBody(para);
			c.setCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(new Date().getTime()+(-20+i)*100000)));

			html = Utils.addCommentToHTML(html, c);
		}
		bug.setCommentsAsHTML(html);
		
		bug.post();
		
		bugs.add(maxId);
	}

	private static String randomOf(String ... vals) {
		int pos = (int) (Math.random()*vals.length);
		return vals[pos];
	}
	
	public static String randomJiraUser() {
		List<String> users = new ArrayList<String>(Config.getDevelopersInJIRA());
		if( users.size() == 0 ) throw new IllegalArgumentException("should call Config.load() first and specify td.developers.jira.developers property");
		int pos = (int) (Math.random()*users.size());
		return users.get(pos);
	}

	public static String randomTdUser() {
		List<String> users = new ArrayList<String>(Config.getDevelopersNamesInTD());
		if( users.size() == 0 ) throw new IllegalArgumentException("should call Config.load() first and specify td.developers.jira.developers property");
		int pos = (int) (Math.random()*users.size());
		return users.get(pos);
	}
	
	private static List<String> sentences;
	public static String randomSentence(int maxLen) {
		if( sentences == null ) {
			sentences = new ArrayList<String>(10000);
			try {
				LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader("Master.And.Margarita.txt")));
				StringBuilder sb = new StringBuilder();
				while(true) {
					String line = lnr.readLine(); 
					if( line == null ) break;

					if( line.startsWith("    ") ) {
						String s = sb.toString().replaceAll("[\\r\\n\\s]+"," ").trim();
						sentences.add(s);
						sb = new StringBuilder();
					}
					
					sb.append(line).append("\n");
				}
			} catch( Exception ex ) {
				sentences.add("Oops");
			}
		}
		int pos = (int) (Math.random()*sentences.size());
		String ret = sentences.get(pos);
		if( ret.length() > maxLen ) return ret.substring(0,maxLen);
		return ret;
	}

	public Dispatch getItem(int i) {
		return null;
	}

	private static byte[] randomFileData(String fileName) {
		
		if( !fileName.endsWith(".txt") ) {
			try {
				int size = (int) (Math.random()*100000)+1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				for(int i=0; i<size;++i ) baos.write((int) (Math.random()*256));
				baos.close();
				return baos.toByteArray();
			} catch( Exception ex ) {
				throw new RuntimeException(ex);
			}
		} else {
			StringBuffer sb = new StringBuffer();
			
			int n = (int) (Math.random()*100)+1;
			for( int i=0; i<n; ++i ) sb.append(randomSentence(10000)).append("\n\n");
			return sb.toString().getBytes();
		}
	}

	private static String randomFileName() {
		String[] ext = {"doc","jar","xls","zip","ppt","jpg","png","gif","txt","klo"};
		String fileExtension = (ext[(int) (Math.random()*ext.length)]);

		int size = (int) (Math.random()*50)+1;
		StringBuffer sb = new StringBuffer();
		addMatchingChar(sb,1,"[a-zA-Z0-9]");
		addMatchingChar(sb,size,"[a-zA-Z0-9\\.\\-\\[\\]\\{\\}\\(\\)\\_ ]");
		addMatchingChar(sb,1,"[a-zA-Z0-9]");
		sb.append('.');
		
		sb.append(fileExtension);
		
		return sb.toString().replaceAll("[\\t\\r\\n]+"," ");
	}

	private static void addMatchingChar(StringBuffer sb,int size,String pattern) {
		while( size > 0 ) {
			char c = (char) (Math.random()*256);
			if( !(""+c).matches(pattern) ) continue;
			sb.append(c);
			size--;
		}
	}
}
