package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.Validate;

import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.BusinessWeightWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;
import com.danube.scrumworks.api.client.types.ReleaseWSO;
import com.danube.scrumworks.api.client.types.ServerException;
import com.danube.scrumworks.api.client.types.SprintWSO;
import com.danube.scrumworks.api.client.types.TaskWSO;
import com.danube.scrumworks.api.client.types.ThemeWSO;

/**
 * Helper methods for accessing the SWP API. 
 * 
 * @author jnicolai
 */
public class SWPTester {
	/** Property file name. */
	private static final String PROPERTY_FILE = "tfswp.properties";
	
	private static final String CCF_MAX_WAIT_TIME = "CCFMaxWaitTime";
	
	private static final String CCF_RETRY_INTERVAL = "CCFRetryInterval";

	private static final String SWP_PRODUCT = "SWPProduct";

	private static final String SWP_SERVER_URL = "SWPServerUrl";

	private static final String SWP_PASSWORD = "SWPPassword";

	private static final String SWP_USER_NAME = "SWPUserName";
	
	// SWP connection
	private com.collabnet.ccf.swp.Connection swpConnection;
	
	private String swpUserName;

	private String swpPassword;

	private String swpServerUrl;

	private String swpProduct;
	
	private int ccfMaxWaitTime;

	private int ccfRetryInterval;

	
	/**
	 * Constructor. 
	 * 
	 * @throws IOException if the property file can not be accessed
	 * @throws FileNotFoundException if the property file can not be found 
	 * @throws ServiceException 
	 */
	public SWPTester() throws FileNotFoundException, IOException, ServiceException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(PROPERTY_FILE));
		
		setSwpUserName(prop.getProperty(SWP_USER_NAME));
		setSwpPassword(prop.getProperty(SWP_PASSWORD));
		setSwpServerUrl(prop.getProperty(SWP_SERVER_URL));
		setSwpProduct(prop.getProperty(SWP_PRODUCT));
		
		ccfMaxWaitTime = Integer.parseInt(prop.getProperty(CCF_MAX_WAIT_TIME)); 
		ccfRetryInterval = Integer.parseInt(prop.getProperty(CCF_RETRY_INTERVAL)); 
		
		swpConnection = new com.collabnet.ccf.swp.Connection(getSwpServerUrl(), getSwpUserName(), getSwpPassword()); 
	}
		
	/**
	 * Returns SWP endpoint used for tests
	 * @return
	 */
	public ScrumWorksEndpoint getSWPEndpoint() {
		return swpConnection.getEndpoint();
	}
	
	public void setSwpUserName(String swpUserName) {
		this.swpUserName = swpUserName;
	}

	public String getSwpUserName() {
		return swpUserName;
	}

	public void setSwpPassword(String swpPassword) {
		this.swpPassword = swpPassword;
	}

	public String getSwpPassword() {
		return swpPassword;
	}

	public void setSwpServerUrl(String swpServerUrl) {
		this.swpServerUrl = swpServerUrl;
	}

	public String getSwpServerUrl() {
		return swpServerUrl;
	}

	public void setSwpProduct(String swpProduct) {
		this.swpProduct = swpProduct;
	}

	public String getSwpProduct() {
		return swpProduct;
	}
	
	/**
	 * Delete all PBIs within the SWP product
	 * @throws ServerException
	 * @throws RemoteException
	 */
	public void deleteAllPBIsInSWP() throws ServerException, RemoteException {
		ProductWSO product = getSWPEndpoint().getProductByName(getSwpProduct());
		BacklogItemWSO[] pbis = getSWPEndpoint().getActiveBacklogItems(product);
		if (pbis != null) {
			for (BacklogItemWSO backlogItemWSO : pbis) {
				getSWPEndpoint().deleteBacklogItem(backlogItemWSO);
			}
		}
	}
	
	/**
	 * Delete all tasks within the SWP product
	 * @throws RemoteException 
	 * @throws ServerException 
	 */
	public void deleteAllTasksInSWP() throws ServerException, RemoteException {
		ProductWSO product = getSWPEndpoint().getProductByName(getSwpProduct());
		TaskWSO[] tasks = getSWPEndpoint().getTasksForProduct(product);
		if (tasks != null) {
			for (TaskWSO task : tasks) {
				getSWPEndpoint().deleteTask(task);
			}
		}
	}
	
	/**
	 * Returns the backlog items after the backlog items appear in a ScrumWorks Pro product. 
	 * Assumes that there are no backlog items before beginning to wait.
	 * 
	 * @param product the product
	 * @param numberOfPBIs number of PBIs to wait for
	 * @return the backlog items in the product
	 * @throws RemoteException if the ScrumWorks API can not be accessed
	 * @throws ServerException if an error occurs in ScrumWorks
	 * @throws InterruptedException if the thread can not sleep
	 * @throws IllegalArgumentException if product argument is <code>null</code>
	 */
	public BacklogItemWSO[] waitForBacklogItemToAppear(final ProductWSO product, int numberOfPBIs) throws ServerException, RemoteException, InterruptedException {
		Validate.notNull(product, "null product");
		
		BacklogItemWSO[] pbis = null;
		for (int i = 0; i < ccfMaxWaitTime; i += ccfRetryInterval) {
			pbis = getSWPEndpoint().getActiveBacklogItems(
					product);
			if (pbis == null || pbis.length < numberOfPBIs) {
				Thread.sleep(ccfRetryInterval);
			} else {
				return pbis; 
			}
		}
		throw new RuntimeException("Backlog items were not found within the given time: " + ccfMaxWaitTime); 
	}
	
	/**
	 * Returns the tasks for the given backlog item after the tasks are created in ScrumWorks.
	 *  
	 * @param backlogItemWSO the backlog item 
	 * @param expectedTaskTitle the expected task title for the first task 
	 * @param numberOfTasks the expected number of tasks for the backlog item 
	 * @return the tasks for the backlog item 
	 * @throws RemoteException if the ScrumWorks API can not be accessed 
	 * @throws ServerException if an error occurs in ScrumWorks
	 * @throws InterruptedException if the thread can not sleep
	 * @throws IllegalArgumentException if backlogItemWSO argument is <code>null</code>
	 */
	public TaskWSO[] waitForTaskToAppear(final BacklogItemWSO backlogItemWSO, final String expectedTaskTitle, final int numberOfTasks) throws ServerException, RemoteException, InterruptedException {
		Validate.notNull(backlogItemWSO, "null backlog item");
		
		TaskWSO[] tasks = null; 
		for (int i = 0; i < ccfMaxWaitTime; i += ccfRetryInterval) {
			tasks = getSWPEndpoint().getTasks(backlogItemWSO);
			if (tasks == null || tasks.length < numberOfTasks || !tasks[0].getTitle().equals(expectedTaskTitle)) {
				Thread.sleep(ccfRetryInterval);
			} else {
				assertEquals(numberOfTasks, tasks.length);
				return tasks;
			}
		}
		throw new RuntimeException("Tasks were not found within the given time: " + ccfMaxWaitTime + tasks); 
	}
	
	/**
	 * Returns the backlog items after the backlog items appear in a ScrumWorks Pro product. 
	 * Waits until the requested number of backlog items appear. 
	 * 
	 * @param numberOfPBIs number of PBIs to wait for
	 * @return the backlog items in the product
	 * @throws RemoteException if the ScrumWorks API can not be accessed
	 * @throws ServerException if an error occurs in ScrumWorks 
	 * @throws InterruptedException if the thread can not sleep
	 */
	public BacklogItemWSO[] waitForBacklogItemsToAppear(int numberOfPBIs) throws ServerException, RemoteException, InterruptedException {
		ProductWSO product = getProduct(); 
		return waitForBacklogItemToAppear(product, numberOfPBIs); 
	}

	/**
	 * Returns the product object for the given product name. 
	 * 
	 * @throws RemoteException if the ScrumWorks API can not be accessed 
	 * @throws ServerException if an error occurs in ScrumWorks 
	 */
	public ProductWSO getProduct() throws ServerException, RemoteException {
		return getSWPEndpoint().getProductByName(getSwpProduct());
	}
	
	/**
	 * Returns a list of the theme names. 
	 * 
	 * @param themes the theme web service objects
	 * @return the theme names
	 * @throws IllegalArgumentException if any argument is <code>null</code>
	 */
	public List<String> getThemeNames(final ThemeWSO[] themes) {
		Validate.noNullElements(themes, "null theme"); 
		
		final List<String> themeNames = new ArrayList<String>(); 
		for (int i = 0; i < themes.length; i++) {
			themeNames.add(themes[i].getName()); 
		}
		return themeNames; 
	}

	/**
	 * Returns the name of the release for the given backlog item in the ScrumWorks integration product. 
	 * @param pbi the backlog item
	 * 
	 * @return the name of the release if found, otherwise null
	 * @throws RemoteException if ScrumWorks can not be accessed
	 * @throws ServerException if there is an error from ScrumWorks
	 * @throws IllegalArgumentException if any argument is <code>null</code>
	 */
	public String getReleaseName(final Long releaseId) throws ServerException, RemoteException {
		Validate.notNull(releaseId, "null release id");
		
		ReleaseWSO[] allReleases = getReleasesInScrumWorks(); 
		ReleaseWSO release; 
		
		for (int i = 0; i < allReleases.length; i++) {
			release = allReleases[i];
			if (release.getId().equals(releaseId)) {
				return release.getTitle(); 
			}
		}
		return null;
	}

	/**
	 * Returns all of the releases in the ScrumWorks product. 
	 * 
	 * @return the id of the release if found, otherwise null
	 * @throws RemoteException if ScrumWorks can not be accessed
	 * @throws ServerException if there is an error from ScrumWorks
	 */
	private ReleaseWSO[] getReleasesInScrumWorks() throws RemoteException,
			ServerException {
		return getSWPEndpoint().getReleases(getProduct());
	}
	
	/**
	 * Returns the id for the release matching the given name. 
	 * 
	 * @throws RemoteException if ScrumWorks can not be accessed 
	 * @throws ServerException if there is an error from ScrumWorks
	 * 
	 */
	public Long getReleaseId(final String releaseName) throws ServerException, RemoteException {
		Validate.notNull(releaseName, "null release name");		
		
		ReleaseWSO[] allReleases = getReleasesInScrumWorks();
		ReleaseWSO release; 
		
		for (int i = 0; i < allReleases.length; i++) {
			release = allReleases[i];
			if (release.getTitle().equals(releaseName)) {
				return release.getId(); 
			}
		}
		return null; 
	}
	
	/**
	 * Creates a backlog item in the ScrumWorks test product and returns the created backlog item. 
	 * Unable to assign themes to a themeless PBI in the 4.3 API. 
	 * 
	 * @param title the backlog item name
	 * @param description the description 
	 * @param estimate the estimate
	 * @param benefit the benefit 
	 * @param penalty the penalty 
	 * @param releaseName the name of the release containing this backlog item 
	 * @param sprint the sprint containing this backlog item
	 * @param themes the themes
	 * @return the created backlog item in ScrumWorks
	 * @throws RemoteException if ScrumWorks can not be accessed 
	 * @throws ServerException if there is an error from ScrumWorks
	 * @throws IllegalArgumentException if title, or release argument is <code>null</code>
	 */
	public BacklogItemWSO createBacklogItem(final String title, final String description, final String estimate, final String benefit, final String penalty, 
			final String releaseName, Sprint sprint, final String... themes) throws ServerException, RemoteException {
		Validate.notNull(title, "null title");
		Validate.notNull(releaseName, "null release name");
		
		final BusinessWeightWSO businessWeight = transformToBusinessWeightWSO(benefit, penalty); 
		final ThemeWSO[] themeWSO = transformToThemeWSO(themes);
		final int pbiEstimate = estimate == null ? 0 : Integer.parseInt(estimate);  
		final Long sprintId = sprint == null ? null : getSprintId(sprint.getName()); 

		return getSWPEndpoint().createBacklogItem(new BacklogItemWSO(true, null, businessWeight, null, description, pbiEstimate, null, getProduct().getId(), 0, getReleaseId(releaseName), sprintId, null, title)); 
//		return getSWPEndpoint().createBacklogItem(new BacklogItemWSO(true, null, businessWeight, null, description, Integer.parseInt(estimate), null, getProduct().getId(), 0, getReleaseId(release), -737035264780005900L, themeWSO, title)); // TODO:  
	}
	
	/**
	 * Creates a backlog item in the Release 1.0 release with an estimate of 0.    
	 * 
	 * @param title the backlog item's title
	 * @throws RemoteException if the ScrumWorks API can not be accessed
	 * @throws ServerException if there is an error from ScrumWorks
	 * @throws IllegalArgumentException if any argument is <code>null</code>  
	 */
	public BacklogItemWSO createBacklogItem(final String title, final String release) throws ServerException, RemoteException {
		Validate.notNull(title, "null title");
		Validate.notNull(release, "null release");
		
		return createBacklogItem(title, null, null, null, null, release, null, new String[] {}); 
	}
	
	/**
	 * Creates a task in the ScrumWorks test product and returns the created task.  
	 * 
	 * @param title the title
	 * @param description the description, null for empty description
	 * @param pointPerson the person who volunteered for the task, null for (unspecified)
	 * @param status the status 
	 * @param currentEstimate the current estimate, null for 0
	 * @param backlogItemId the backlog item id this task is a child of 
	 * @throws RemoteException if the ScrumWorks API can not be accessed 
	 * @throws NumberFormatException if the currentEstimate or originalEstimate is not a valid string representation for a number
	 * @throws ServerException if there is an error from ScrumWorks
	 * @throws IllegalArgumentException if title, status, or backlogItemId argument is <code>null</code> 
	 */
	public TaskWSO createTask(final String title, final String description, final String pointPerson, final TaskStatus status, 
			final String currentEstimate, final Long backlogItemId) throws ServerException, NumberFormatException, RemoteException {
		Validate.notNull(title, "null title");
		Validate.notNull(status, "null status");
		Validate.notNull(backlogItemId, "null backlog item id");
		
		final Integer taskEstimate = currentEstimate == null ? 0 : Integer.parseInt(currentEstimate);  
		return getSWPEndpoint().createTask(new TaskWSO(backlogItemId, description, taskEstimate, null, null, pointPerson, 0, status.getStatus(), 0, title)); 
	}
	
	/**
	 * Updates a backlog item in the ScrumWorks test product and returns the updated backlog item. 
	 * 
	 * @param backlogItemToBeUpdated the backlog item to be updated
	 * @throws RemoteException if ScrumWorks can not be accessed
	 * @throws ServerException if there is an error from ScrumWorks
	 * @throws IllegalArgumentException if any argument is <code>null</code> 
	 */
	public BacklogItemWSO updateBacklogItem(final BacklogItemWSO backlogItemToBeUpdated) throws ServerException, RemoteException {
		Validate.notNull(backlogItemToBeUpdated, "null backlog item");
		
		return getSWPEndpoint().updateBacklogItem(backlogItemToBeUpdated); 
	}

	/**
	 * Transform theme name into ThemeWSO.  
	 * 
	 * @param themes the themes 
	 * @return the {@link ThemeWSO}, null if themes is null
	 * @throws RemoteException if the ScrumWorks API can not be accessed
	 * @throws ServerException if there is an error from ScrumWorks
	 */
	public ThemeWSO[] transformToThemeWSO(final String... themes)
			throws RemoteException, ServerException {
		if (themes == null) {
			return null; 
		}
		
		ThemeWSO[] allThemes = getSWPEndpoint().getThemes(getProduct()); 
		ThemeWSO[] pbiThemes = new ThemeWSO[themes.length]; 
		for (int i= 0; i < themes.length; i++) {
			for (int j = 0; j < allThemes.length; j++) {
				if (themes.equals(allThemes[j])) {
					pbiThemes[i] = allThemes[j]; 
				}
			}
		}
		return pbiThemes; 
	}

	/**
	 * Transform a benefit and penalty value into a BusinessWeightWSO.  Requires both benefit and penalty values to be set.  
	 * 
	 * @param benefit the benefit value
	 * @param penalty the penalty value 
	 * @return the {@link BusinessWeightWSO}
	 */
	public BusinessWeightWSO transformToBusinessWeightWSO(final String benefit, final String penalty) {
		if (benefit == null & penalty == null) {
			return null; 
		}
		Long businessWeightBenefit = benefit == null ? 0 : Long.parseLong(benefit); 
		Long businessWeightPenalty = penalty == null ? 0 : Long.parseLong(penalty); 
		
		BusinessWeightWSO businessWeight = new BusinessWeightWSO(businessWeightBenefit, businessWeightPenalty);
		return businessWeight;
	}
	
	
	/**
	 * Returns the sprint id matching the given sprint name. 
	 * 
	 * @param startName the sprint's name
	 * @return the sprint id, null if a match is not found
	 * @throws RemoteException if ScrumWorks can not be accessed
	 * @throws ServerException if there is an error from ScrumWorks 
	 */
	public Long getSprintId(final String sprintName) throws ServerException, RemoteException {
		final SprintWSO[] sprints = getSWPEndpoint().getSprints(getProduct()); 
		for (int i = 0; i < sprints.length; i++) {
			if (sprints[i].getName().equals(sprintName)) {
				return sprints[i].getId(); 
			}
		}
		return null; 
	}
}
