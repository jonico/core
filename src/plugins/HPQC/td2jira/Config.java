package td2jira;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import td2jira.td.ITDConnector;

public class Config {
	public static Logger logger = Logger.getLogger(Config.class);
	
	private static Properties props = new Properties();
	private static Map<String,String> tdDevelopersToJiraDevelopers = new HashMap<String, String>();

	public static String HTTP_PROXY_HOST="http.proxyHost";
	public static String HTTP_PROXY_PORT="http.proxyPort";
	public static String HTTP_PROXY_USER="http.proxyUser";
	public static String HTTP_PROXY_PASSWORD="http.proxyPassword";	
	
	public static String JIRA_USER = "jira.user";
	public static String JIRA_URL = "jira.url";
	public static String TD_DEV_JIRA_DEV = "td.developers.jira.developers";
	public static String SYNC_MINUTES = "sync.minutes=5";
	public static String JIRA_SUMMARY_PREFIX = "jira.summary.prefix=DEFAULT";
	public static String JIRA_PROJECT = "jira.project";
	public static String TD_ROBOT_NAME = "td.robot.name=Robot@JIRA";
	public static String JIRA_PASSWORD = "jira.password";
	public static String TD_URL = "http://10.2.1.114:8080/qcbin";
	public static String TD_DOMAIN = "DOMAIN_PRJ_1";
	public static String TD_PROJECT = "PRJ_1";
	public static String TD_USER = "admin";
	public static String TD_PASSWORD = "collabnet";
	public static String JIRA_ASSIGN_NEW_ISSUE_TO = "jira.assign.to";
	public static String TD_LEAD = "td.lead";

	public static String SYNC_RULES = "sync.rules";
	public static String SYNC_ONLY_TD_ID = "sync.only.td.id";
	public static String SYNC_ATTACH_COMMENT_PREFIX = "sync.attach.comment.prefix=@TD:see attachment:";

	public static String TD_MOCK_TRANSITIONS = "td.mock.transitions";
	public static String TD_MOCK_NEW_ISSUE_PROBABILITY = "td.mock.new.issue.probability=5";
	public static String TD_MOCK_NEW_COMMENT_PROBABILITY = "td.mock.new.comment.probability=5";
	public static String TD_MOCK_CHANGE_STATUS_PROBABILITY = "td.mock.change.status.probability=1";
	public static String TD_MOCK_NEW_ATTACHMENT_PROBABILITY = "td.mock.new.attachment.probability=1";

	public static String TD_CONNECTOR_IMPLEMENTATION = "td2jira.td.TDConnector";
	public static String TD_IMPLEMENTATION = "td2jira.td.api.dcom.Connection";
	public static String JIRA_CONNECTOR_IMPLEMENTATION = "jira.connector.implementation=td2jira.jira.xmlrpc.JIRAXmlRpcConnector";
	
	public static void load() throws Exception {
		if( props.size() != 0 ) return;
		props.load(new FileInputStream("td2jira.properties"));
		
		Field[] fields = Config.class.getDeclaredFields();
		for (Field field : fields) {
			if( !field.getType().equals(String.class) ) continue;
			if( !field.getName().matches("[A-Z_0-9]+") ) continue;
			if( !Modifier.isStatic(field.getModifiers()) ) continue;
			
			String val = (String) field.get(null);

			int eqSign = val.indexOf('=');
			String propertyName = null;
			String propertyDefaultValue = null;
			if( eqSign > 0 ) {
				 propertyName = val.substring(0,eqSign);
				 propertyDefaultValue = val.substring(eqSign+1);
			} else {
				 propertyName = val;
			}
			
			String propValue = (String) props.get(propertyName);
			if( propValue != null ) {
				field.set(null,propValue.trim());
			} else {
				field.set(null,propertyDefaultValue);
			}
			
			logger.debug("config: "+field.getName()+"="+field.get(null));
		}
		
		String dev2dev = TD_DEV_JIRA_DEV;
		if( dev2dev != null ) {
			String[] pairs = dev2dev.split("[,;]");
			for (String d2d : pairs) {
				String[] names = d2d.split("=");
				tdDevelopersToJiraDevelopers.put(names[0], names[1]);
			}
		}
		logger.debug("developers map:"+tdDevelopersToJiraDevelopers);
		
		if( HTTP_PROXY_HOST != null ) {
			logger.debug("using http proxy "+HTTP_PROXY_HOST+":"+HTTP_PROXY_PORT);
			System.setProperty("http.proxyHost", HTTP_PROXY_HOST);
			System.setProperty("http.proxyPort", HTTP_PROXY_PORT);
			if( HTTP_PROXY_USER != null ) {
				logger.debug("using http proxy user "+HTTP_PROXY_USER);
				System.setProperty("http.proxyUser", HTTP_PROXY_USER);
				System.setProperty("http.proxyPassword", HTTP_PROXY_PASSWORD);
			}
		}
	}
	
	public static Set<String> getDevelopersInJIRA() {
		Set<String> ret = new HashSet<String>();
		Iterator<String> it = tdDevelopersToJiraDevelopers.keySet().iterator();
		while( it.hasNext() ) {
			String td = it.next();
			String jira = tdDevelopersToJiraDevelopers.get(td);
			if( jira != null ) ret.add(jira);
		}
		return ret;
	}
	
	public static Set<String> getDevelopersNamesInTD() {
		Set<String> ret = new HashSet<String>();
		Iterator<String> it = tdDevelopersToJiraDevelopers.keySet().iterator();
		while( it.hasNext() ) {
			String td = it.next();
			ret.add(td);
		}
		return ret;
	}	
	
//	public static String getProperty(String key) {
//		return props.getProperty(key);
//	}
	
//	public static Long getLong(String key) {
//		String val = props.getProperty(key);
//		if( val == null ) return null;
//		if( !val.matches("[0-9]+") ) throw new IllegalArgumentException(key+" is not a number:"+val);
//		return Long.parseLong(val);
//	}

	public static String getTDDeveloperForJIRADeveloper(String assignedInJIRA) {
		Iterator<String> it = tdDevelopersToJiraDevelopers.keySet().iterator();
		while( it.hasNext() ) {
			String td = it.next();
			if( tdDevelopersToJiraDevelopers.get(td).equals(assignedInJIRA) ) return td;
		}
		return null;
	}

//	public static String getProperty(String key, String defaultValue) {
//		String val = getProperty(key);
//		return val == null? defaultValue:val.trim();
//	}
}
