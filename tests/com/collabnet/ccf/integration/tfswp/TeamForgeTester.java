package com.collabnet.ccf.integration.tfswp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.PlanningFolderRuleViolationException;
import com.collabnet.teamforge.api.planning.PlanningFolderList;
import com.collabnet.teamforge.api.planning.PlanningFolderRow;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.ArtifactRow;

/**
 * Helper methods for accessing the TeamForge API.
 * 
 * @author Kelley
 */
public class TeamForgeTester {
	/** Status for open backlog items. */
	public static final String STATUS_OPEN = "Open";

	/** Property file name. */
	public static final String PROPERTY_FILE = "tfswp.properties";

	/** Property name for the server url. */
	private static final String SERVER_URL_PROPERTY = "TFServerUrl";

	/** Property name for the username. */
	private static final String USER_NAME_PROPERTY = "TFUserName";

	/** Property name for the password. */
	private static final String PASSWORD_PROPERTY = "TFPassword";

	/** Property name for the project id. */
	private static final String PROJECT_PROPERTY = "TFProject";

	/** Property name for the backlog item tracker id. */
	private static final String PBI_TRACKER_PROPERTY = "TFPBITracker";

	/** Property name for the task tracker id. */
	private static final String TASK_TRACKER_PROPERTY = "TFTaskTracker";

	/** User name to access TeamForge. */
	private String userName;

	/** Password to access TeamForge. */
	private String password;

	/** TeamForge url. */
	private String serverUrl;

	/** Project id. */
	private String project;

	/** Backlog item tracker id. */
	private String pbiTracker;

	/** Task tracker id. */
	private String taskTracker;

	/** Connect to TeamForge. */
	private Connection connection;

	/**
	 * Constructor.
	 * 
	 * @throws IOException
	 *             if the property file can not be accessed
	 * @throws FileNotFoundException
	 *             if the property file can not be found
	 */
	public TeamForgeTester() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(PROPERTY_FILE));
		setUserName(prop.getProperty(USER_NAME_PROPERTY));
		password = prop.getProperty(PASSWORD_PROPERTY);
		serverUrl = prop.getProperty(SERVER_URL_PROPERTY);
		project = prop.getProperty(PROJECT_PROPERTY);
		pbiTracker = prop.getProperty(PBI_TRACKER_PROPERTY);
		taskTracker = prop.getProperty(TASK_TRACKER_PROPERTY);

		// we pass the current system millis to work around a caching problem
		connection = Connection.getConnection(serverUrl, getUserName(),
				password, null, Long.toString(System.currentTimeMillis()),
				null, false);

	}

	/**
	 * Logs off TeamForge
	 * 
	 * @throws RemoteException
	 */
	public void logOff() throws RemoteException {
		if (connection.supports50()) {
			connection.getTeamForgeClient().logoff50(connection.getUserId());
		} else {
			connection.getTeamForgeClient().logoff44(connection.getUserId());
		}
	}

	/**
	 * Delete all backlog items in the TF project.
	 * 
	 * @throws RemoteException
	 *             if the TeamForge API can not be accessed
	 */
	public void deleteAllPBIsInTF() throws RemoteException {
		ArtifactRow[] tfRows = connection.getTrackerClient().getArtifactList(
				pbiTracker, null).getDataRows();
		for (ArtifactRow artifactRow : tfRows) {
			connection.getTrackerClient().deleteArtifact(artifactRow.getId());
		}
	}

	/**
	 * Delete all tasks in the TeamForge project.
	 * 
	 * @throws RemoteException
	 *             if the TeamForge API can not be accessed
	 */
	public void deleteAllTasksInTF() throws RemoteException {
		ArtifactRow[] tfRows = connection.getTrackerClient().getArtifactList(
				taskTracker, null).getDataRows();
		for (ArtifactRow artifactRow : tfRows) {
			connection.getTrackerClient().deleteArtifact(artifactRow.getId());
		}
	}

	/**
	 * Creates an open status backlog item.
	 * 
	 * @param title
	 *            the title
	 * @param description
	 *            the description
	 * @param release
	 *            the name of the planning folder for the backlog item's release
	 * @param flexFields
	 *            the custom fields
	 * @throws RemoteException
	 *             if the TeamForge API can not be accessed
	 */
	public String createBacklogItem(final String title,
			final String description, final String release,
			final FieldValues flexFields) throws RemoteException {
		return connection.getTrackerClient().createArtifact(pbiTracker, title,
				description, null, null, STATUS_OPEN, null, 0, 0, 0, false,
				null, null, getPlanningFolderId(release), flexFields, null,
				null, null).getId();
	}

	public String createTask(String title, String description, String status,
			String assignedUsername, int remainingEffort)
			throws RemoteException, PlanningFolderRuleViolationException {
		FieldValues flexFields = new FieldValues();
		flexFields.setNames(new String[] {});
		flexFields.setTypes(new String[] {});
		flexFields.setValues(new String[] {});
		String pbi = createBacklogItem("TestTitle", "TestDescription", null,
				flexFields);
		ArtifactDO task = connection.getTrackerClient().createArtifact(
				taskTracker, title, description, null, null, status, null, 0,
				0, remainingEffort, true, assignedUsername, null, null, flexFields,
				null, null, null);
		connection.getTrackerClient().createArtifactDependency(pbi,
				task.getId(), "Parent-child relationship created by unit test");
		// update task artifact to trigger synchronization again
		connection.getTrackerClient().setArtifactData(task,
				"Trigger synchronization again", null, null, null);
		return task.getId();
	}

	/**
	 * Returns the planning folder id by the given name.
	 * 
	 * @param release
	 *            the name of the release mapped to the planning folder
	 * @return the planning folder id if found, otherwise null
	 * @throws RemoteException
	 *             if the test can not connect to the TeamForge API
	 */
	private String getPlanningFolderId(final String release)
			throws RemoteException {
		if (release == null) {
			return null;
		}
		PlanningFolderList planningFolderList = connection.getPlanningClient()
				.getPlanningFolderList(project, true);
		PlanningFolderRow[] planningFolderDataRows = planningFolderList
				.getDataRows();
		for (int i = 0; i < planningFolderDataRows.length; i++) {
			if (planningFolderDataRows[i].getTitle().equals(release)) {
				return planningFolderDataRows[i].getId();
			}
		}
		return null;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
