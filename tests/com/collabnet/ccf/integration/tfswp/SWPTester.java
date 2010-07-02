package com.collabnet.ccf.integration.tfswp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.commons.lang.Validate;

import com.danube.scrumworks.api.client.types.BusinessWeightWSO;
import com.danube.scrumworks.api.client.types.ServerException;
import com.danube.scrumworks.api.client.types.ThemeWSO;
import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.BusinessWeight;
import com.danube.scrumworks.api2.client.Product;
import com.danube.scrumworks.api2.client.Release;
import com.danube.scrumworks.api2.client.ScrumWorksAPIService;
import com.danube.scrumworks.api2.client.ScrumWorksException;
import com.danube.scrumworks.api2.client.Task;
import com.danube.scrumworks.api2.client.Theme;

/**
 * Helper methods for accessing the SWP API.
 * 
 * @author jnicolai
 */
public class SWPTester {
	/** Default release created with new products. */
	public static final String RELEASE_1 = "Release 1.0";

	/** 2nd release. */
	public static final String RELEASE_2 = "Release 2.0";
	
	/** Test release. */ 
	public static final String RELEASE_3 = "RenamedTestRelease"; 

	/** Core product theme. */
	public static final String THEME_CORE = "Core";

	/** DB product theme. */
	public static final String THEME_DB = "DB";

	/** GUI product theme. */
	public static final String THEME_GUI = "GUI";

	/** Documentation product theme. */
	public static final String THEME_DOCUMENTATION = "Documentation";

	/** Property file name. */
	private static final String PROPERTY_FILE = "tfswp.properties";

	private static final String CCF_MAX_WAIT_TIME = "CCFMaxWaitTime";

	private static final String CCF_RETRY_INTERVAL = "CCFRetryInterval";

	private static final String SWP_PRODUCT = "SWPProduct";

	private static final String SWP_SERVER_URL = "SWPServerUrl";

	private static final String SWP_PASSWORD = "SWPPassword";

	private static final String SWP_USER_NAME = "SWPUserName";

	// SWP connection
	private ScrumWorksAPIService endpoint;

	private String swpUserName;

	private String swpPassword;

	private String swpServerUrl;

	private String swpProduct;

	private int ccfMaxWaitTime;

	private int ccfRetryInterval;

	/**
	 * Constructor.
	 * 
	 * @throws IOException
	 *             if the property file can not be accessed
	 * @throws FileNotFoundException
	 *             if the property file can not be found
	 * @throws ServiceException
	 */
	public SWPTester() throws FileNotFoundException, IOException,
			ServiceException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(PROPERTY_FILE));

		setSwpUserName(prop.getProperty(SWP_USER_NAME));
		setSwpPassword(prop.getProperty(SWP_PASSWORD));
		setSwpServerUrl(prop.getProperty(SWP_SERVER_URL));
		setSwpProduct(prop.getProperty(SWP_PRODUCT));

		ccfMaxWaitTime = Integer.parseInt(prop.getProperty(CCF_MAX_WAIT_TIME));
		ccfRetryInterval = Integer.parseInt(prop
				.getProperty(CCF_RETRY_INTERVAL));

		Service service = Service.create(new URL(getSwpServerUrl()), new QName(
				"http://api2.scrumworks.danube.com/",
				"ScrumWorksAPIBeanService"));
		endpoint = service.getPort(ScrumWorksAPIService.class);
		setUserNameAndPassword("administrator", "password");
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 */
	private void setUserNameAndPassword(String userName, String password) {
		final BindingProvider bindingProvider = (BindingProvider) endpoint;
		Map<String, Object> requestContext = bindingProvider
				.getRequestContext();
		requestContext.put(BindingProvider.USERNAME_PROPERTY, userName);
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, password);
	}

	/**
	 * Returns SWP endpoint used for tests
	 * 
	 * @return
	 */
	public ScrumWorksAPIService getSWPEndpoint() {
		return endpoint;
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
	 * 
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 */
	public void deleteAllPBIsInSWP() throws ScrumWorksException {
		Product product = getSWPEndpoint().getProductByName(getSwpProduct());
		List<BacklogItem> pbis = getSWPEndpoint().getBacklogItemsInProduct(
				product.getId(), false);
		if (pbis != null) {
			for (BacklogItem backlogItem : pbis) {
				getSWPEndpoint().deleteBacklogItem(backlogItem.getId(), false);
			}
		}
	}

	/**
	 * Delete all tasks within the SWP product
	 * 
	 * @throws RemoteException
	 * @throws ServerException
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 */
	public void deleteAllTasksInSWP() throws ScrumWorksException {
		Product product = getSWPEndpoint().getProductByName(getSwpProduct());
		List<Task> tasks = getSWPEndpoint().getTasksForProduct(product.getId());
		if (tasks != null) {
			for (Task task : tasks) {
				getSWPEndpoint().deleteTask(task.getId());
			}
		}
	}

	/**
	 * Returns the backlog items after the backlog items appear in a ScrumWorks
	 * Pro product. Assumes that there are no backlog items before beginning to
	 * wait.
	 * 
	 * @param product
	 *            the product
	 * @param numberOfPBIs
	 *            number of PBIs to wait for
	 * @param customTimeout
	 *            if not null, custom maximum timeout will be used
	 * @return the backlog items in the product
	 * @throws InterruptedException
	 *             if the thread can not sleep
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 * @throws IllegalArgumentException
	 *             if product argument is <code>null</code>
	 */
	public List<BacklogItem> waitForBacklogItemToAppear(final Product product,
			int numberOfPBIs, Long customTimeout) throws InterruptedException,
			ScrumWorksException {
		Validate.notNull(product, "null product");

		List<BacklogItem> pbis = null;
		for (int i = 0; i < (customTimeout == null ? ccfMaxWaitTime
				: customTimeout); i += ccfRetryInterval) {
			pbis = getSWPEndpoint().getBacklogItemsInProduct(product.getId(),
					false);
			if (pbis == null || pbis.size() < numberOfPBIs) {
				Thread.sleep(ccfRetryInterval);
			} else {
				return pbis;
			}
		}
		throw new RuntimeException(
				"Backlog items were not found within the given time: "
						+ (customTimeout == null ? ccfMaxWaitTime
								: customTimeout));
	}

	/**
	 * Returns the task for the given backlog item after the task is created or
	 * updated in ScrumWorks.
	 * 
	 * @param backlogItemWSO
	 *            the backlog item
	 * @param expectedTaskTitle
	 *            the expected task title for the first task
	 * @param numberOfTasks
	 *            the expected number of tasks for the backlog item
	 * @param customTimeout
	 *            if not null, custom maximum timeout will be used
	 * @return the tasks for the backlog item
	 * @throws InterruptedException
	 *             if the thread can not sleep
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 * @throws IllegalArgumentException
	 *             if backlogItemWSO argument is <code>null</code>
	 */
	public Task waitForTaskToAppear(final BacklogItem backlogItem,
			final String expectedTaskTitle, final int numberOfTasks,
			Long customTimeout) throws InterruptedException,
			ScrumWorksException {
		Validate.notNull(backlogItem, "null backlog item");

		List<Task> tasks = null;
		for (int i = 0; i < (customTimeout == null ? ccfMaxWaitTime
				: customTimeout); i += ccfRetryInterval) {
			tasks = getTasksForBacklogItem(backlogItem.getId());
			List<String> taskNames = new ArrayList<String>(); 
			for (Task task : tasks) {
				taskNames.add(task.getName()); 
			}
			int indexOfMatchingTaskTitle = taskNames.indexOf(expectedTaskTitle); 
			if (tasks == null || tasks.size() < numberOfTasks
					|| indexOfMatchingTaskTitle == -1) {
				Thread.sleep(ccfRetryInterval);
			} else {
				return tasks.get(indexOfMatchingTaskTitle);
			}
		}
		throw new RuntimeException(
				"Tasks were not found within the given time: "
						+ (customTimeout == null ? ccfMaxWaitTime
								: customTimeout) + tasks);
	}

	/**
	 * Returns the tasks for the backlog item. 
	 * 
	 * @param backlogItem
	 * @return the tasks for the backlog item 
	 * @throws ScrumWorksException if an error occurs from ScrumWorks
	 */
	public List<Task> getTasksForBacklogItem(final Long backlogItemId)
			throws ScrumWorksException {
		return getSWPEndpoint().getTasks(backlogItemId);
	}

	/**
	 * Returns the backlog items after the backlog items appear in a ScrumWorks
	 * Pro product. Waits until the requested number of backlog items appear.
	 * 
	 * @param numberOfPBIs
	 *            number of PBIs to wait for
	 * @return the backlog items in the product
	 * @throws ScrumWorksException
	 *             if an error occurs from ScrumWorks
	 * @throws InterruptedException
	 *             if the thread can not sleep
	 */
	public List<BacklogItem> waitForBacklogItemsToAppear(int numberOfPBIs)
			throws InterruptedException, ScrumWorksException {
		Product product = getProduct();
		return waitForBacklogItemToAppear(product, numberOfPBIs, null);
	}
	
	/**
	 * Returns the backlog items after the title of a backlog item has been updated 
	 * in ScrumWorks.  
	 * @throws InterruptedException if the thread can not sleep
	 * @throws ScrumWorksException if an error occurs from ScrumWorks
	 */
	public BacklogItem waitForBacklogItemToUpdate(final String title) throws InterruptedException, ScrumWorksException {
		List<BacklogItem> backlogItems = new ArrayList<BacklogItem>(); 
		for (int i = 0; i < ccfMaxWaitTime; i += ccfRetryInterval) {
			backlogItems = getSWPEndpoint().getBacklogItemsInProduct(getProduct().getId(), false); 
			List<String> backlogItemNames = new ArrayList<String>(); 
			for (BacklogItem backlogItem : backlogItems) {
				backlogItemNames.add(backlogItem.getName()); 
			}
			int indexOfUpdateBacklogItem = backlogItemNames.indexOf(title);
			if (indexOfUpdateBacklogItem == -1) {
				Thread.sleep(ccfRetryInterval);
			} else {
				return backlogItems.get(indexOfUpdateBacklogItem); 
			}
		}
		throw new RuntimeException("Backlog items with the given title: " + title 
				+ " were not found within the given time: " + ccfMaxWaitTime);  
	}
	
	/**
	 * Returns the product object for the given product name.
	 * 
	 * @throws ScrumWorksException
	 *             if an error occurs from ScrumWorks
	 */
	public Product getProduct() throws ScrumWorksException {
		return getSWPEndpoint().getProductByName(getSwpProduct());
	}

	/**
	 * Returns a list of the theme names.
	 * 
	 * @param themes
	 *            the theme web service objects
	 * @return the theme names
	 * @throws IllegalArgumentException
	 *             if any argument is <code>null</code>
	 */
	public List<String> getThemeNames(final List<Theme> themes) {
		Validate.noNullElements(themes, "null theme");

		final List<String> themeNames = new ArrayList<String>();
		for (Theme theme : themes) {
			themeNames.add(theme.getName());
		}
		return themeNames;
	}

	/**
	 * Returns the name of the release for the given backlog item in the
	 * ScrumWorks integration product.
	 * 
	 * @param pbi
	 *            the backlog item
	 * 
	 * @return the name of the release if found, otherwise null
	 * @throws ScrumWorksException
	 *             if an error occurs from ScrumWorks
	 * @throws IllegalArgumentException
	 *             if any argument is <code>null</code>
	 */
	public String getReleaseName(final Long releaseId)
			throws ScrumWorksException {
		Validate.notNull(releaseId, "null release id");

		List<Release> allReleases = getReleasesInScrumWorks();
		for (Release release : allReleases) {
			if (release.getId().equals(releaseId)) {
				return release.getName();
			}
		}
		return null;

	}

	/**
	 * Returns all of the releases in the ScrumWorks product.
	 * 
	 * @return the id of the release if found, otherwise null
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 */
	private List<Release> getReleasesInScrumWorks() throws ScrumWorksException {
		return getSWPEndpoint().getReleasesForProduct(getProduct().getId());
	}

	/**
	 * Returns the id for the release matching the given name.
	 * 
	 * @throws ScrumWorksException
	 *             if an error occurs from ScrumWorks
	 * 
	 */
	public Long getReleaseId(final String releaseName)
			throws ScrumWorksException {
		Validate.notNull(releaseName, "null release name");

		List<Release> allReleases = getReleasesInScrumWorks();

		for (Release release : allReleases) {
			if (release.getName().equals(releaseName)) {
				return release.getId();
			}
		}

		return null;
	}

	/**
	 * Creates a backlog item in the ScrumWorks test product and returns the
	 * created backlog item. Unable to assign themes to a themeless PBI in the
	 * 4.3 API.
	 * 
	 * @param title
	 *            the backlog item name
	 * @param description
	 *            the description
	 * @param estimate
	 *            the estimate
	 * @param benefit
	 *            the benefit
	 * @param penalty
	 *            the penalty
	 * @param releaseName
	 *            the name of the release containing this backlog item
	 * @param sprint
	 *            the sprint containing this backlog item
	 * @param themes
	 *            the themes
	 * @return the created backlog item in ScrumWorks
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 * @throws IllegalArgumentException
	 *             if title, or release argument is <code>null</code>
	 */
	public BacklogItem createBacklogItem(final String title,
			final String description, final String estimate,
			final String benefit, final String penalty,
			final String releaseName, Sprint sprint, final String... themes)
			throws ScrumWorksException {
		Validate.notNull(title, "null title");
		Validate.notNull(releaseName, "null release name");

		final BusinessWeight businessWeight = transformToBusinessWeightWSO(
				benefit, penalty);
		final List<Theme> themeForBacklogItem = transformToThemeWSO(themes);
		final int pbiEstimate = estimate == null ? 0 : Integer
				.parseInt(estimate);
		final Long sprintId = sprint == null ? null : getSprintId(sprint
				.getName());

		BacklogItem backlogItem = new BacklogItem();
		backlogItem.setActive(true);
		backlogItem.setBusinessWeight(businessWeight);
		backlogItem.setDescription(description);
		backlogItem.setEstimate(pbiEstimate);
		backlogItem.setProductId(getProduct().getId());
		backlogItem.setReleaseId(getReleaseId(releaseName));
		backlogItem.setSprintId(sprintId);
		backlogItem.setName(title);
		backlogItem.setThemes(themeForBacklogItem); 
		return getSWPEndpoint().createBacklogItem(backlogItem);
		// return getSWPEndpoint().createBacklogItem(new BacklogItemWSO(true,
		// null, businessWeight, null, description, Integer.parseInt(estimate),
		// null, getProduct().getId(), 0, getReleaseId(release),
		// -737035264780005900L, themeWSO, title)); // TODO:
	}

	/**
	 * Creates a backlog item in the Release 1.0 release with an estimate of 0.
	 * 
	 * @param title
	 *            the backlog item's title
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 * @throws IllegalArgumentException
	 *             if any argument is <code>null</code>
	 */
	public BacklogItem createBacklogItem(final String title,
			final String release) throws ScrumWorksException {
		Validate.notNull(title, "null title");
		Validate.notNull(release, "null release");

		return createBacklogItem(title, null, null, null, null, release, null,
				new String[] {});
	}

	/**
	 * Creates a task in the ScrumWorks test product and returns the created
	 * task.
	 * 
	 * @param title
	 *            the title
	 * @param description
	 *            the description, null for empty description
	 * @param pointPerson
	 *            the person who volunteered for the task, null for
	 *            (unspecified)
	 * @param status
	 *            the status, null for Not Started
	 * @param currentEstimate
	 *            the current estimate, null for 0
	 * @param backlogItemId
	 *            the backlog item id this task is a child of
	 * @throws NumberFormatException
	 *             if the currentEstimate or originalEstimate is not a valid
	 *             string representation for a number
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 * @throws IllegalArgumentException
	 *             if title, status, or backlogItemId argument is
	 *             <code>null</code>
	 */
	public Task createTask(final String title, final String description,
			final String pointPerson, TaskStatus status,
			final String currentEstimate, final Long backlogItemId)
			throws NumberFormatException, ScrumWorksException {
		Validate.notNull(title, "null title");
		Validate.notNull(backlogItemId, "null backlog item id");

		if (status == null) {
			status = TaskStatus.NOT_STARTED; 
		}
		
		final Integer taskEstimate = currentEstimate == null ? 0 : Integer
				.parseInt(currentEstimate);
		Task task = new Task();
		task.setName(title);
		task.setDescription(description);
		task.setPointPerson(pointPerson);
		task.setStatus(status.getStatus());
		task.setCurrentEstimate(taskEstimate);
		task.setBacklogItemId(backlogItemId);
		return getSWPEndpoint().createTask(task);
		// return getSWPEndpoint().createTask(new Task(backlogItemId,
		// description, taskEstimate, null, null, pointPerson, 0,
		// status.getStatus(), 0, title));
	}

	/**
	 * Updates a task.
	 * 
	 * @param taskToBeUpdated
	 *            the task to be updated
	 * @return the updated task from ScrumWorks
	 * @throws ScrumWorksException
	 *             if an error occurs from ScrumWorks
	 * @throws IllegalArgumentException
	 *             if any argument is <code>null</code>
	 */
	public Task updateTask(final Task taskToBeUpdated)
			throws ScrumWorksException {
		Validate.notNull(taskToBeUpdated, "null taskToBeUpdated");

		return getSWPEndpoint().updateTask(taskToBeUpdated);
	}

	/**
	 * Updates a backlog item in the ScrumWorks test product and returns the
	 * updated backlog item.
	 * 
	 * @param backlogItemToBeUpdated
	 *            the backlog item to be updated
	 * @throws ScrumWorksException
	 *             if an error occurs from ScrumWorks
	 * @throws IllegalArgumentException
	 *             if any argument is <code>null</code>
	 */
	public BacklogItem updateBacklogItem(
			final BacklogItem backlogItemToBeUpdated)
			throws ScrumWorksException {
		Validate.notNull(backlogItemToBeUpdated, "null backlog item");

		return getSWPEndpoint().updateBacklogItem(backlogItemToBeUpdated);
	}

	/**
	 * Transform theme name into ThemeWSO.
	 * 
	 * @param themes
	 *            the themes
	 * @return the {@link ThemeWSO}, null if themes is null
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 */
	public List<Theme> transformToThemeWSO(final String... themes)
			throws ScrumWorksException {
		if (themes == null) {
			return null;
		}

		List<Theme> allThemes = getSWPEndpoint().getThemesForProduct(
				getProduct().getId());
		List<Theme> pbiThemes = new ArrayList<Theme>();
		for (int i = 0; i < themes.length; i++) {
			for (Theme theme : allThemes) {
				if (themes[i].equals(theme.getName())) {
					pbiThemes.add(theme);
				}
			}
		}
		return pbiThemes;
	}

	/**
	 * Transform a benefit and penalty value into a BusinessWeightWSO. Requires
	 * both benefit and penalty values to be set.
	 * 
	 * @param benefit
	 *            the benefit value
	 * @param penalty
	 *            the penalty value
	 * @return the {@link BusinessWeightWSO}
	 */
	public BusinessWeight transformToBusinessWeightWSO(final String benefit,
			final String penalty) {
		if (benefit == null & penalty == null) {
			return null;
		}
		Long businessWeightBenefit = benefit == null ? 0 : Long
				.parseLong(benefit);
		Long businessWeightPenalty = penalty == null ? 0 : Long
				.parseLong(penalty);

		// BusinessWeight businessWeight = new
		// BusinessWeight(businessWeightBenefit, businessWeightPenalty); TODO:
		// fix in SWP api
		BusinessWeight businessWeight = new BusinessWeight();
		businessWeight.setBenefit(businessWeightBenefit);
		businessWeight.setPenalty(businessWeightPenalty);
		return businessWeight;
	}

	/**
	 * Returns the sprint id matching the given sprint name.
	 * 
	 * @param startName
	 *            the sprint's name
	 * @return the sprint id, null if a match is not found
	 * @throws ScrumWorksException
	 *             if there is an error from ScrumWorks
	 */
	public Long getSprintId(final String sprintName) throws ScrumWorksException {
		final List<com.danube.scrumworks.api2.client.Sprint> sprints = getSWPEndpoint()
				.getSprintsForProduct(getProduct().getId());
		for (com.danube.scrumworks.api2.client.Sprint sprint : sprints) {
			if (sprint.getName().equals(sprintName)) {
				return sprint.getId();
			}
		}
		return null;
	}
}
