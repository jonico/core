package com.collabnet.tracker.core.util;

import java.util.Date;

import com.collabnet.tracker.common.ClientArtifact;
import com.collabnet.tracker.core.TrackerReportElement;
import com.collabnet.tracker.core.TrackerWebServicesClient;
import com.collabnet.tracker.ws.Query;

/**
 * Utility class for PT artifacts
 * 
 * @author Shawn Minto
 * 
 */
public class TrackerUtil {

	private static final String DISPLAY_NAME_ATTR = "displayName";
	private static final String NAMESPACE_ATTR = "namespace";
	private static final String TAG_NAME_ATTR = "tagName";
	private static final String ATTR_SEPARATOR = "&";
	private static final String VAL_SEPARATOR = "=";
	private static final boolean DEBUG_MODE = false;

	/**
	 * simple debugging method for displaying info to console
	 * note that DEBUG_MODE must be set to true for this to work.
	 * @param msg
	 */
	public static void debug(String msg) {
		if(DEBUG_MODE) {
			System.out.println((new Date()).toString() + ": " + msg);
		}
	}
	
	
	/**
	 * Create a PT query into a string that can be stored
	 * @param query
	 * @return
	 */
	public static String queryToString(Query query) {
		String queryString = "";

		String tagName = query.getTagName();
		String nameSpace = query.getNamespace();
		String displayName = query.getDisplayName();

		queryString += TAG_NAME_ATTR + VAL_SEPARATOR + tagName + ATTR_SEPARATOR;
		queryString += NAMESPACE_ATTR + VAL_SEPARATOR + nameSpace + ATTR_SEPARATOR;
		queryString += DISPLAY_NAME_ATTR + VAL_SEPARATOR + displayName;

		return queryString;
	}

	/**
	 * Turn the string representation of a query into a query that can be used
	 * for querying PT
	 * 
	 * @param queryString
	 * @return
	 */
	public static Query stringToQuery(String queryString) {
		String[] attributes = queryString.split(ATTR_SEPARATOR);

		String tagName = null;
		String namespace = null;
		String displayName = null;

		for (String attribute : attributes) {
			String[] parts = attribute.split("=");
			if (parts.length == 2) {
				String attr = parts[0];
				if (attr.compareTo(DISPLAY_NAME_ATTR) == 0) {
					displayName = parts[1];
				}
				if (attr.compareTo(NAMESPACE_ATTR) == 0) {
					namespace = parts[1];
				}
				if (attr.compareTo(TAG_NAME_ATTR) == 0) {
					tagName = parts[1];
				}
			}
		}

		Query query = new Query(tagName, namespace, displayName);
		return query;
	}

	/**
	 * Create the attribute key given the tag and namesoace
	 * 
	 * @param namespace
	 * @param tagName
	 * @return
	 */
	public static String getKey(String namespace, String tagName) {
		return "{" + namespace + "}" + tagName;
	}

	/**
	 * Get the attribute namespace from the attribute key
	 * 
	 * @param key
	 * @return
	 */
	public static String getNamespaceFromKey(String key) {
		String[] parts = key.split("}");
		if (parts.length == 2) {
			return parts[0].substring(1);
		} else {
			return null;
		}
	}

	/**
	 * Get the attribute local name from the key
	 * 
	 * @param key
	 * @return
	 */
	public static String getTagNameFromKey(String key) {
		String[] parts = key.split("}");
		if (parts.length == 2) {
			return parts[1];
		} else {
			return null;
		}
	}

	/**
	 * Get the id of the artifact
	 * 
	 * @param artifact
	 * @return
	 */
	public static String getId(ClientArtifact artifact) {
		String s = artifact.getAttributeValue(TrackerWebServicesClient.DEFAULT_NAMESPACE,
				TrackerReportElement.ID.getTagName());
		return s;
	}

	/**
	 * Get the priority of the artifact
	 * 
	 * @param artifact
	 * @return
	 */
	public static String getPriority(ClientArtifact artifact) {
		String namespace = artifact.getNamespacesForTagName(TrackerReportElement.PRIORITY.getTagName());
		if(namespace == null){
			namespace = artifact.getNamespace();
		}
		String s = artifact.getAttributeValue(namespace, TrackerReportElement.PRIORITY.getTagName());
		if (s != null)
			s = s.toUpperCase();
		return s == null?"":s;
	}

	/**
	 * Get the artifacts summary
	 * 
	 * @param artifact
	 * @return
	 */
	public static String getSummary(ClientArtifact artifact) {
		String namespace = artifact.getNamespacesForTagName(TrackerReportElement.PRIORITY.getTagName());
		if(namespace == null){
			namespace = artifact.getNamespace();
		}
		String s = artifact.getAttributeValue(namespace, TrackerReportElement.SUMMARY.getTagName());
		return s == null?"":s;
	}

	/**
	 * Determine if the artifact is completed
	 * 
	 * @param artifact
	 * @return
	 */
	public static boolean isCompleted(ClientArtifact artifact) {
		String namespace = artifact.getNamespacesForTagName(TrackerReportElement.PRIORITY.getTagName());
		if(namespace == null){
			namespace = artifact.getNamespace();
		}
		String status = artifact.getAttributeValue(namespace, TrackerReportElement.STATUS.getTagName());
		if (status == null)
			return false;
		if (isCompletedString(status.toLowerCase()))
			return true;
		else
			return false;
	}

	/**
	 * Strings that represent the completed state of an artifact so that Mylyn can know it is complete
	 */
	private static final String[] completedStrings = { "completed", "complete", "fixed", "done", "finished" };

	private static boolean isCompletedString(String statusString) {
		for (String completedString : completedStrings) {
			if (statusString.equals(completedString)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine if the given namespace is the global namespace or not
	 * 
	 * @param namespace
	 * @return
	 */
	public static boolean isGlobalNamespace(String namespace) {
		return namespace.equals(TrackerWebServicesClient.DEFAULT_NAMESPACE);
	}
}
