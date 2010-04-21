package com.collabnet.ccf.integration.tfswp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import com.collabnet.ce.soap50.webservices.cemain.TrackerFieldSoapDO;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.Filter;
import com.collabnet.teamforge.api.PlanningFolderRuleViolationException;
import com.collabnet.teamforge.api.planning.PlanningFolderList;
import com.collabnet.teamforge.api.planning.PlanningFolderRow;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.ArtifactDependencyRow;
import com.collabnet.teamforge.api.tracker.ArtifactList;
import com.collabnet.teamforge.api.tracker.ArtifactRow;

/**
 * Helper methods for accessing the TeamForge API.
 * 
 * @author Kelley
 */
public class TeamForgeTester {
	/** Status for open backlog items. */
	public static final String STATUS_OPEN = "Open";

	/** User for None */ 
	public static final String NONE = "No user";
	
	/** Variable name for the backlog item's benefit field. */ 
	public static final String FIELD_BENEFIT = "Benefit"; 

	/** Variable name for the backlog item's penalty field. */ 
	public static final String FIELD_PENALTY = "Penalty"; 
	
	/** Variable name for the backlog item's effort field. */ 
	public static final String FIELD_EFFORT = "Backlog Effort"; 
	
	/** Variable name for the backlog item's theme field. */ 
	public static final String FIELD_THEME = "Themes"; 
	
	/** Variable name for the backlog item's key. */ 
	public static final String FIELD_KEY = "SWP-Key"; 
	
	/** Variable name for the backlog item's team. */ 
	public static final String FIELD_TEAM = "Team"; 
	
	/** Variable name for the backlog item's sprint's name. */ 
	public static final String FIELD_SPRINT_NAME = "Sprint"; 
	
	/** Variable name for the backlog item's sprint's start date. */ 
	public static final String FIELD_SPRINT_START = "Sprint Start"; 
	
	/** Variable name for the backlog item's sprint's end date. */ 
	public static final String FIELD_SPRINT_END = "Sprint End"; 
	
	/** Property file name. */
	public static final String PROPERTY_FILE = "tfswp.properties";

	/** Property for the maximum wait time. */ 
	private static final String CCF_MAX_WAIT_TIME = "CCFMaxWaitTime";
	
	/** Property for the interval retry time. */ 
	private static final String CCF_RETRY_INTERVAL = "CCFRetryInterval";
	
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
	
	/** Maximum wait time. */ 
	private int ccfMaxWaitTime;

	/** Time to retry. */ 
	private int ccfRetryInterval;

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
		
		ccfMaxWaitTime = Integer.parseInt(prop.getProperty(CCF_MAX_WAIT_TIME)); 
		ccfRetryInterval = Integer.parseInt(prop.getProperty(CCF_RETRY_INTERVAL)); 

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
	public ArtifactDO createBacklogItem(final String title,
			final String description, final String release,
			final FieldValues flexFields) throws RemoteException {
		return connection.getTrackerClient().createArtifact(pbiTracker, title,
				description, null, null, STATUS_OPEN, null, 0, 0, 0, false,
				null, null, getPlanningFolderId(release), flexFields, null,
				null, null);
	}

	public ArtifactDO createTask(String title, String description, String status,
			String assignedUsername, int remainingEffort, int originalEstimate)
			throws RemoteException, PlanningFolderRuleViolationException {
		FieldValues flexFields = new FieldValues();
		flexFields.setNames(new String[] {});
		flexFields.setTypes(new String[] {});
		flexFields.setValues(new String[] {});
		ArtifactDO pbi = createBacklogItem("TestTitle", "TestDescription", null,
				flexFields);
		ArtifactDO task = connection.getTrackerClient().createArtifact(
				taskTracker, title, description, null, null, status, null, 0,
				originalEstimate, remainingEffort, false, assignedUsername, null, null, flexFields,
				null, null, null);
		connection.getTrackerClient().createArtifactDependency(pbi.getId(),
				task.getId(), "Parent-child relationship created by unit test");
		return task;
	}
	
	/**
	 * Updates and returns the updated backlog item. 
	 * 
	 * @param backlogItemId
	 * @return
	 * @throws RemoteException if TeamForge can not be accessed if TeamForge can not be accessed
	 * @throws PlanningFolderRuleViolationException if there is an error with the planning folder
	 */
	public ArtifactDO updateBacklogItem(final String backlogItemId, final String title, final String description, 
			final String release, final FieldValues flexFields) throws RemoteException, PlanningFolderRuleViolationException {
		ArtifactDO pbi = retrieveAndUpdateArtifactTitleAndDescription(backlogItemId, title, description); 
		pbi.setPlanningFolderId(getPlanningFolderId(release)); 
		pbi.setFlexFields(flexFields); 
		return updateArtifact(pbi, "updating pbi ..."); 
	}
	
	public ArtifactDO updateTask(String taskId, String title, String description, String status,
			String assignedUsername, int remainingEffort, int originalEstimate)
			throws RemoteException, PlanningFolderRuleViolationException {
		ArtifactDO task = retrieveAndUpdateArtifactTitleAndDescription(taskId, title,
				description);
		task.setStatus(status);
		task.setAssignedTo(assignedUsername);
		task.setRemainingEffort(remainingEffort);
		task.setEstimatedEffort(originalEstimate);
		return updateArtifact(task, "updating task ...");
	}

	/**
	 * Updates and returns the artifact in TeamForge.  
	 * 
	 * @param artifact the artifact
	 * @param updateString
	 * @return the updated artifact
	 * @throws RemoteException if TeamForge can not be accessed
	 * @throws PlanningFolderRuleViolationException if there is an error with the planning folder
	 */
	private ArtifactDO updateArtifact(final ArtifactDO artifact,
			final String updateString) throws RemoteException, PlanningFolderRuleViolationException {
		connection.getTrackerClient().setArtifactData(artifact, updateString, null, null, null);
		return connection.getTrackerClient().getArtifactData(artifact.getId());
	}

	/**
	 * Updates the title and description of the artifact retrieved from TeamForge based on the artifact's id.   
	 * 
	 * @param artifactId the artifact id
	 * @param title the revised title
	 * @param description the revised description
	 * @return the artifact with the updated title and description
	 * @throws RemoteException if TeamForge can not be accessed
	 */
	private ArtifactDO retrieveAndUpdateArtifactTitleAndDescription(final String artifactId,
			final String title, final String description) throws RemoteException {
		ArtifactDO task = connection.getTrackerClient().getArtifactData(artifactId);
		task.setTitle(title);
		task.setDescription(description);
		return task;
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
	public String getPlanningFolderId(final String release)
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
	
	/**
	 * Returns a FieldValues based on the given String arrays.  
	 * The number of elements in the two arrays must match. 
	 * 
	 * @param names the names of the flex fields
	 * @param values the values for the flex fields
	 * @return the FieldValues 
	 */
	public FieldValues convertToFlexField(final String[] names, final String[] values) {
		final FieldValues flexFields = new FieldValues();
		flexFields.setNames(names); 
		flexFields.setValues(values);
		final int flexFieldsLength = flexFields.getNames().length;
		final String[] flexFieldTypes = new String[flexFieldsLength];
		for (int i = 0; i < flexFieldsLength; i++) {
			flexFieldTypes[i] = TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING; 
		}
		flexFields.setTypes(flexFieldTypes); 
		return flexFields; 
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * Moves a task from its existing parent to a new parent
	 * @param taskId
	 * @param newParentId
	 * @throws RemoteException
	 * @throws PlanningFolderRuleViolationException
	 */
	public void reparentTask(String taskId, String newParentId) throws RemoteException, PlanningFolderRuleViolationException {
		ArtifactDependencyRow rel = connection.getTrackerClient().getParentDependencyList(taskId).getDataRows()[0];
		connection.getTrackerClient().removeArtifactDependency(rel.getOriginId(), rel.getTargetId());
		connection.getTrackerClient().createArtifactDependency(newParentId, taskId, "reparenting ...");
	}
	
	
	
	/**
	 * Returns the backlog items after the backlog items appear in the planning folder mapped to ScrumWorks Pro. 
	 * Waits until the requested number of backlog items appear.
	 * 
	 * @return the {@link ArtifactRow} 
	 * @throws RemoteException if the TeamForge API can not be accessed
	 * @throws InterruptedException if the thread can not sleep
	 */
	public ArtifactRow[] waitForBacklogItemsToAppear(final int numberOfPbis) throws RemoteException, InterruptedException {
		ArtifactList artifactList; 
		ArtifactRow[] artifactRows; 
		for (int i = 0; i < ccfMaxWaitTime; i += ccfRetryInterval) {
			artifactList = connection.getTrackerClient().getArtifactList(pbiTracker, new Filter[] {});
			artifactRows = artifactList.getDataRows(); 
			if (artifactRows.length < numberOfPbis) {
				Thread.sleep(ccfRetryInterval); 
			} else {
				return artifactRows; 
			}
		}
		throw new RemoteException(numberOfPbis + " backlog item(s) were not found within the given time: " + ccfMaxWaitTime); 
	}
	
	/**
	 * Waits until parent has at least numberOfPFs child planning folders
	 * @param parent id of parent folder 
	 * @param numberOfPFs number of PFs to wait for
	 * @param recursive determines whether a recursive search should be done or not 
	 * @return
	 * @throws RemoteException 
	 * @throws InterruptedException 
	 */
	public PlanningFolderRow[] waitForPlanningFoldersToAppear (String parent, int numberOfPFs, boolean recursive) throws RemoteException, InterruptedException {
		PlanningFolderRow[] pfRows;
		for (int i = 0; i < ccfMaxWaitTime; i += ccfRetryInterval) {
			pfRows = connection.getPlanningClient().getPlanningFolderList(parent, recursive).getDataRows();
			if (pfRows.length < numberOfPFs) {
				Thread.sleep(ccfRetryInterval); 
			} else {
				return pfRows; 
			}
		}
		throw new RemoteException(numberOfPFs + " planning folder(s) were not found within the given time: " + ccfMaxWaitTime);
	}
	
	/**
	 * Returns the FieldValues for the given artifact. 
	 * 
	 * @param artifactId the id for the artifact
	 * @return the {@link FieldValues} for the artifact
	 * @throws RemoteException if the TeamForge API can not be accessed
	 */
	private FieldValues getFlexFields(final String artifactId) throws RemoteException {
		return connection.getTrackerClient().getArtifactData(artifactId).getFlexFields();
	}
	
	/**
	 * Returns the values for the given flex field names. 
	 * 
	 * @param artifactId the id for the artifact
	 * @param fieldNames the flex field names
	 * @return the list of values matching the name, if multiple values match the same name, the values are returned in alphabetical order
	 * @throws RemoteException if the TeamForge API can not be accessed
	 */
	public List<String> getFieldValues(final String artifactId, final String... fieldNames) throws RemoteException {
		final FieldValues flexFields = getFlexFields(artifactId);
		final String[] names = flexFields.getNames(); 
		final Object[] values = flexFields.getValues();
		final List<String> matchingValues = new ArrayList<String>();
		for (int i = 0; i < fieldNames.length; i++) {
			// add all themes
			if (fieldNames[i].equals(FIELD_THEME)) {
				for (int j = 0; j < names.length; j++) {
					if (names[j].equals(FIELD_THEME)) {
						matchingValues.add((String) values[j]);
					}
				}
			} else { 
				// add matching field names
				for (int j = 0; j < names.length; j++) {
					if (names[j].equals(fieldNames[i])) {
						matchingValues.add((String) values[j]);
						break; 
					}
				}
			}
		}
		return matchingValues; 
	}
	
	/**
	 * Returns the TF connection object
	 * @return
	 */
	Connection getConnection () {
		return connection;
	}
}
