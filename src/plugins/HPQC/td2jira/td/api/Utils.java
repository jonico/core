package td2jira.td.api;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


/**
 * @author madan
 *
 */
public class Utils {
	public static Logger logger = Logger.getLogger(Utils.class);

	public static List<Comment> splitComments(String s) {
		List<Comment> cl = new ArrayList<Comment>();
	
		if( s == null ) return cl;
	
		String p = "<font\\scolor=[\"']#[0-9a-fA-F]{6}[\"']><b>([^,<>]+),\\s([0-9\\:\\-\\s\\/\\.]+)";
		Pattern pattern = Pattern.compile(p,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	    
		Matcher matcher = pattern.matcher(s);
		
		String fullName = null;
		String created = null;
		String body = null;
		
		int prevEnd = -1;
		int begin = 0;
		int end = 0;
		while( matcher.find() ) {
			begin = matcher.start();
			end = matcher.end();
			
			if( prevEnd >= 0 ) {
				body = s.substring(prevEnd,begin);
				addCommentIfNotEmpty(cl, fullName, created, body);
			}
			
			fullName = matcher.group(1).trim();
			created = matcher.group(2).trim();
			if( created.endsWith(":") ) created = created.substring(0,created.length()-1);
	
			prevEnd = end;
		}
		
		if( prevEnd >= 0 ) {
			body = s.substring(end,s.length());
			addCommentIfNotEmpty(cl, fullName, created, body);
		}
		
		return cl;
	}

	private static void addCommentIfNotEmpty(List<Comment> cl, String fullName, String created, String body) {
		Comment tdc = new Comment();
		tdc.setAuthor(fullName.trim());
		tdc.setCreated(created.trim());
		tdc.setBody(normalize(body));

		if( tdc.getBody().length() != 0 && tdc.getAuthor().length() != 0 ) {
			cl.add(tdc);
		}
	}	
	
	public static String normalize(String s) {
		s = s.replaceAll("<font\\scolor=\"#[0-9a-fA-F]{6}\">", "");
		s = s.replaceAll("<html>", "");
		s = s.replaceAll("<body>", "");
		s = s.replaceAll("</html>", "");
		s = s.replaceAll("</body>", "");
		s = s.replaceAll("</font>", "");
		s = s.replaceAll("<i>", "");
		s = s.replaceAll("\\_{2,}", "");
		s = s.replaceAll("</i>", "");
		s = s.replaceAll("<u>", "");
		s = s.replaceAll("</u>", "");
		s = s.replaceAll("<b>", "");
		s = s.replaceAll("</b>", "");
		s = s.replaceAll("<br>", "\n");
		s = s.replaceAll("<br/>", "\n");
		
		s = s.replaceAll("&amp;", "&");
		s = s.replaceAll("&lt;", "<");
		s = s.replaceAll("&gt;", ">");
		
		return s.trim();
	}

/*	public static Comment fromJIRAComment(JIRAComment jiraComment) {
		Comment tdc = new Comment();
		
		String author = jiraComment.getAuthorFullName();
		author += " (";
		author += "@JIRA"+jiraComment.getId();
		author += ")";
		tdc.setAuthor(author);
		
		tdc.setCreated(jiraComment.getCreated());
		
		String body = jiraComment.getBody();
		body = body.replaceAll("2QA:\\s+", "").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\\n", "<br>");
		tdc.setBody(body);
		
		return tdc;
	}
*/ /* Commented for QC2SFEE */
	public static String formatComment(IComment comment) {
		StringBuilder sb = new StringBuilder("<font color='#333300'><b> ");
		sb.append(comment.getAuthor());
		sb.append(", ");
		sb.append(comment.getCreated());
		sb.append(":");
		sb.append("</b></font> ");
		sb.append(comment.getBody());
		return sb.toString();
	}

	public static String addCommentToHTML(String html, IComment comment) {
		if( html == null || html.trim().length() == 0 ) html = "<html><body></body></html>";
		
		int notBody = html.indexOf("</body></html>");
		html = html.substring(0,notBody);
		html += formatComment(comment);
		html += "</body></html>";
		return html;
	}
}
