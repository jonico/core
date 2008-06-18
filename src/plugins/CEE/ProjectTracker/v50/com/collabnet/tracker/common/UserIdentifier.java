package com.collabnet.tracker.common;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Convenience class for managing a complex user identifier consisting of user
 * name, project name and domain name.
 * 
 * @author Shawn Minto
 * @author sszego
 */
public class UserIdentifier {

	private static final String DELIMITER = "|";

	private String userName = null;
	private String domain = null;
	private String project = "www";

	/**
	 * Constructor
	 * 
	 * @param userAndDomain
	 *            string of the format {username}|{domain}|{project}
	 * @throws IOException
	 */
	public UserIdentifier(String userAndDomain) throws IOException {
		parse(userAndDomain);
	}

	/**
	 * Constructor
	 * 
	 * @param userName
	 *            the username
	 * @param domain
	 *            the domain
	 * @param project
	 *            the project
	 */
	public UserIdentifier(String userName, String domain, String project) {
		this.userName = userName;
		this.domain = domain;
		this.project = project;
	}

	/**
	 * Constructor. Uses default project (www).
	 * 
	 * @param userName
	 *            the username
	 * @param domain
	 *            the domain
	 */
	public UserIdentifier(String userName, String domain) {
		this.userName = userName;
		this.domain = domain;
	}

	/**
	 * Returns the domain portion of the identifier
	 * 
	 * @return the domain name, e.g., "extranet.collab.net"
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Returns the project portion of the identifier
	 * 
	 * @return the name of the project, e.g., "www"
	 */
	public String getProject() {
		return project;
	}

	/**
	 * Sets the domain portion of the identifier
	 * 
	 * @param domain
	 *            the domain, e.g., "extranet.collab.net"
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Returns the username portion of the identifier
	 * 
	 * @return the name of the project, e.g., "root"
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the project portion of the identifier
	 * 
	 * @param userName
	 *            the name of the user, e.g., "root"
	 */

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Converts this identifier to the appropriate string format E.g., <code>
	 * new UserIdentifier(aUserIdentifier.toString())</code>
	 * will produce a valid user identifier.
	 * 
	 * @return the String representation of this UserIdentifier
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return userName + DELIMITER + domain + DELIMITER + project;
	}

	private void parse(String userAndDomain) throws IOException {
		StringTokenizer st = new StringTokenizer(userAndDomain, DELIMITER);
		int numTokens = st.countTokens();

		if (numTokens < 2 || numTokens > 3) {
			throw new IOException("Invlaid UserIdentifier, " + "User and Domain must be specified ");
		} else {
			userName = st.nextToken();
			domain = st.nextToken();

			if (numTokens == 3) {
				project = st.nextToken();
			}
		}

	}

	/**
	 * Sets the project portion of this user identifier
	 * 
	 * @param project
	 *            the name of the project, e.g., 'www'
	 */
	public void setProject(String project) {
		this.project = project;
	}

}
