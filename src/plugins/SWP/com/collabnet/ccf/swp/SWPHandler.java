package com.collabnet.ccf.swp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.swp.SWPMetaData.PBIFields;
import com.collabnet.ccf.swp.SWPMetaData.ProductFields;
import com.collabnet.ccf.swp.SWPMetaData.ReleaseFields;
import com.collabnet.ccf.swp.SWPMetaData.SWPType;
import com.collabnet.ccf.swp.SWPMetaData.TaskFields;
import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.BusinessWeight;
import com.danube.scrumworks.api2.client.Product;
import com.danube.scrumworks.api2.client.Release;
import com.danube.scrumworks.api2.client.ScrumWorksAPIService;
import com.danube.scrumworks.api2.client.ScrumWorksException;
import com.danube.scrumworks.api2.client.Sprint;
import com.danube.scrumworks.api2.client.Task;
import com.danube.scrumworks.api2.client.Team;
import com.danube.scrumworks.api2.client.Theme;

/**
 * This class encapsulates all calls to the SWP backend
 * 
 * @author jnicolai
 * 
 */
public class SWPHandler {

	private static final Log log = LogFactory.getLog(SWPHandler.class);
	private ScrumWorksAPIService endpoint;
	private DatatypeFactory calendarConverter;

	/**
	 * Constructor, receives a valid SWP connection
	 * 
	 * @param connection
	 *            SWP connection object
	 * @throws DatatypeConfigurationException 
	 */
	public SWPHandler(Connection connection) throws DatatypeConfigurationException {
		calendarConverter = DatatypeFactory.newInstance();
		this.endpoint = connection.getEndpoint();
	}

	/**
	 * Populates the generic artifact data structure with the properties of the
	 * requested PBI
	 * 
	 * @param id
	 *            id of the PBI
	 * @param product
	 *            SWP product name
	 * @throws RemoteException
	 * @throws ScrumWorksException 
	 */
	public void retrievePBI(String id, String product, GenericArtifact ga)
			throws NumberFormatException, RemoteException, ScrumWorksException {
		BacklogItem pbi = endpoint.getBacklogItemById(Long.valueOf(id));
		addPBIField(ga, PBIFields.id, pbi.getId());
		addPBIField(ga, PBIFields.active, pbi.isActive());
		BusinessWeight bw = pbi.getBusinessWeight();
		addPBIField(ga, PBIFields.benefit, bw.getBenefit());
		addPBIField(ga, PBIFields.penalty, bw.getPenalty());
		// TODO time zone conversion necessary?
		addPBIField(ga, PBIFields.completedDate, pbi.getCompletedDate());
		addPBIField(ga, PBIFields.description, pbi.getDescription());
		addPBIField(ga, PBIFields.estimate, pbi.getEstimate());
		addPBIField(ga, PBIFields.key, pbi.getKey());
		addPBIField(ga, PBIFields.productId, pbi.getProductId());
		addPBIField(ga, PBIFields.rank, pbi.getRank());
		addPBIField(ga, PBIFields.releaseId, pbi.getReleaseId());
		addPBIField(ga, PBIFields.title, pbi.getName());

		// retrieve themes
		List<Theme> themes = pbi.getThemes();
		if (themes == null || themes.size() == 0) {
			addPBIField(ga, PBIFields.theme, null);
		} else {
			for (Theme themeWSO : themes) {
				addPBIField(ga, PBIFields.theme, themeWSO.getName());
			}
		}

		// set parent artifact (Product release)
		ga.setDepParentSourceArtifactId(pbi.getReleaseId().toString());
		ga.setDepParentSourceRepositoryId(product
				+ SWPMetaData.REPOSITORY_ID_SEPARATOR
				+ SWPMetaData.RELEASE);

		Long sprintId = pbi.getSprintId();
		if (sprintId == null) {
			addPBIField(ga, PBIFields.sprintId, null);
			addPBIField(ga, PBIFields.team, "");
			addPBIField(ga, PBIFields.sprint, "");
			addPBIField(ga, PBIFields.sprintStart, null);
			addPBIField(ga, PBIFields.sprintEnd, null);
		} else {
			addPBIField(ga, PBIFields.sprintId, sprintId);
			Sprint sprintWSO = endpoint.getSprintById(sprintId);
			addPBIField(ga, PBIFields.sprint, sprintWSO.getName());
			addPBIField(ga, PBIFields.sprintStart, sprintWSO
					.getStartDate());
			addPBIField(ga, PBIFields.sprintEnd, sprintWSO
					.getEndDate());
			// retrieve team name
			Team team = endpoint.getTeamById(sprintWSO.getTeamId());
			addPBIField(ga, PBIFields.team, team.getName());
		}
	}

	/**
	 * Populates the generic artifact data structure with the properties of the
	 * requested task
	 * 
	 * @param id
	 *            id of the task
	 * @param product
	 *            SWP product name
	 * @throws RemoteException
	 * @throws NumberFormatException
	 * @throws ScrumWorksException 
	 */
	public void retrieveTask(String id, String product,
			GenericArtifact genericArtifact) throws
			NumberFormatException, RemoteException, ScrumWorksException {
		long taskId = Long.valueOf(id);
		final Task task = endpoint.getTaskById(taskId);

		addTaskField(genericArtifact, TaskFields.id, taskId);
		addTaskField(genericArtifact, TaskFields.description, task
				.getDescription());
		addTaskField(genericArtifact, TaskFields.estimatedHours, task
				.getCurrentEstimate());
		addTaskField(genericArtifact, TaskFields.originalEstimate, task
				.getOriginalEstimate());
		addTaskField(genericArtifact, TaskFields.backlogItemId, task
				.getBacklogItemId());
		addTaskField(genericArtifact, TaskFields.pointPerson, task
				.getPointPerson());
		addTaskField(genericArtifact, TaskFields.rank, task.getRank());
		addTaskField(genericArtifact, TaskFields.status, task.getStatus());
		//addTaskField(genericArtifact, TaskFields.taskBoardStatusRank, task
		//		.get);
		addTaskField(genericArtifact, TaskFields.title, task.getName());

		// set parent artifact (PBI)
		genericArtifact.setDepParentSourceArtifactId(task.getBacklogItemId()
				.toString());
		genericArtifact.setDepParentSourceRepositoryId(product
				+ SWPMetaData.REPOSITORY_ID_SEPARATOR + SWPMetaData.PBI);
	}

	/**
	 * Adds a task field to a generic artifact
	 * 
	 * @param genericArtifact
	 * @param field
	 *            task field
	 * @param value
	 *            value of the task field
	 */
	private void addTaskField(GenericArtifact genericArtifact,
			TaskFields field, Object value) {
		// all fields are from field type "mandatoryField" since SWP has a
		// static field model
		GenericArtifactField gaField = genericArtifact.addNewField(field
				.getFieldName(), "mandatoryField");
		gaField.setFieldValueType(field.getValueType());
		gaField.setFieldAction(FieldActionValue.REPLACE);
		gaField.setFieldValue(value);
	}

	/**
	 * Adds a product field to a generic artifact
	 * 
	 * @param genericArtifact
	 * @param field
	 *            task field
	 * @param value
	 *            value of the product field
	 */
	private void addProductField(GenericArtifact genericArtifact,
			ProductFields field, Object value) {
		// all fields are from field type "mandatoryField" since SWP has a
		// static field model
		GenericArtifactField gaField = genericArtifact.addNewField(field
				.getFieldName(), "mandatoryField");
		gaField.setFieldValueType(field.getValueType());
		gaField.setFieldAction(FieldActionValue.REPLACE);
		gaField.setFieldValue(value);
	}

	/**
	 * Adds a PBI field to a generic artifact
	 * 
	 * @param genericArtifact
	 * @param field
	 *            PBI field
	 * @param value
	 *            value of the PBI field
	 */
	private void addPBIField(GenericArtifact genericArtifact, PBIFields field,
			Object value) {
		// all fields are from field type "mandatoryField" since SWP has a
		// static field model
		GenericArtifactField gaField = genericArtifact.addNewField(field
				.getFieldName(), "mandatoryField");
		gaField.setFieldValueType(field.getValueType());
		gaField.setFieldAction(FieldActionValue.REPLACE);
		gaField.setFieldValue(value);
	}

	/**
	 * Adds a product release field to a generic artifact
	 * 
	 * @param genericArtifact
	 * @param field
	 *            product release field
	 * @param value
	 *            value of the product release field
	 */
	private void addProductReleaseField(GenericArtifact genericArtifact,
			ReleaseFields field, Object value) {
		// all fields are from field type "mandatoryField" since SWP has a
		// static field model
		GenericArtifactField gaField = genericArtifact.addNewField(field
				.getFieldName(), "mandatoryField");
		gaField.setFieldValueType(field.getValueType());
		gaField.setFieldAction(FieldActionValue.REPLACE);
		gaField.setFieldValue(value);
	}

	/**
	 * Return the tasks that have been changed since the last query
	 * 
	 * @param swpProductName
	 *            SWP product to retrieve the tasks from
	 * @param artifactStates
	 *            list that will be populated with changed tasks
	 * @throws RemoteException
	 * @throws ScrumWorksException 
	 */
	public void getChangedTasks(String swpProductName,
			List<ArtifactState> artifactStates) throws
			RemoteException, ScrumWorksException {
		Product product = endpoint.getProductByName(swpProductName);
		List<Task> tasks = endpoint.getTasksForProduct(product.getId());
		if (tasks != null) {
			for (Task task : tasks) {
				ArtifactState artifactState = new ArtifactState();
				artifactState.setArtifactId(task.getId().toString());
				artifactState.setArtifactLastModifiedDate(new Date(0));
				artifactState.setArtifactVersion(-1);
				artifactStates.add(artifactState);
			}
		}
	}

	/**
	 * Return the PBIs that have been changed since the last query
	 * 
	 * @param swpProductName
	 *            SWP product to retrieve the PBIs from
	 * @param artifactStates
	 *            list that will be populated with changed PBIs
	 * @throws RemoteException
	 * @throws RemoteException
	 * @throws ScrumWorksException 
	 */
	public void getChangedPBIs(String swpProductName,
			ArrayList<ArtifactState> artifactStates) throws
			RemoteException, ScrumWorksException {
		Product product = endpoint.getProductByName(swpProductName);
		List<BacklogItem> pbis = endpoint.getBacklogItemsInProduct(product.getId(), false);
		if (pbis != null) {
			for (BacklogItem pbi : pbis) {
				ArtifactState artifactState = new ArtifactState();
				artifactState.setArtifactId(pbi.getId().toString());
				artifactState.setArtifactLastModifiedDate(new Date(0));
				artifactState.setArtifactVersion(-1);
				artifactStates.add(artifactState);
			}
		}
	}

	/**
	 * Updates SWP PBI
	 * 
	 * @param active
	 * @param benefit
	 * @param completedDate
	 * @param description
	 * @param estimate
	 * @param penalty
	 * @param title
	 * @param themes
	 * @param ga
	 * @return updated PBI
	 * @throws RemoteException
	 * @throws NumberFormatException
	 * @throws ScrumWorksException 
	 */
	public BacklogItem updatePBI(GenericArtifactField active,
			GenericArtifactField benefit, GenericArtifactField completedDate,
			GenericArtifactField description, GenericArtifactField estimate,
			GenericArtifactField penalty, GenericArtifactField title,
			List<GenericArtifactField> themes, String swpProductName, GenericArtifact ga)
			throws NumberFormatException, RemoteException, ScrumWorksException {
		BacklogItem pbi = endpoint.getBacklogItemById(new Long(ga
				.getTargetArtifactId()));
		// TODO Do conflict resolution
		if (active != null && active.getFieldValueHasChanged()) {
			pbi.setActive((Boolean) active.getFieldValue());
		}

		if (completedDate != null && completedDate.getFieldValueHasChanged()) {
			pbi.setCompletedDate(calendarConverter.newXMLGregorianCalendar((GregorianCalendar)completedDate.getFieldValue()));
		}

		if (description != null && description.getFieldValueHasChanged()) {
			pbi.setDescription((String) description.getFieldValue());
		}

		if (estimate != null && estimate.getFieldValueHasChanged()) {
			Object fieldValueObj = estimate.getFieldValue();
			if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
				pbi.setEstimate(null);
			} else {
				int fieldValue = 0;
				if (fieldValueObj instanceof String) {
					String fieldValueString = (String) fieldValueObj;
					try {
						fieldValue = Integer.parseInt(fieldValueString);
					} catch (NumberFormatException e) {
						throw new CCFRuntimeException(
								"Could not parse value of field estimate: "
										+ e.getMessage(), e);
					}
				} else if (fieldValueObj instanceof Integer) {
					fieldValue = ((Integer) fieldValueObj).intValue();
				}
				pbi.setEstimate(fieldValue);
			}
		}

		if (title != null && title.getFieldValueHasChanged()) {
			pbi.setName((String) title.getFieldValue());
		}

		boolean penaltyHasChanged = (penalty != null && penalty
				.getFieldValueHasChanged());
		boolean benefitHasChanged = (benefit != null && benefit
				.getFieldValueHasChanged());

		// only if at least one of penalty or benefit has changed, we have to
		// do the update
		if (penaltyHasChanged || benefitHasChanged) {
			BusinessWeight bw = pbi.getBusinessWeight();
			if (penaltyHasChanged) {
				Object fieldValueObj = penalty.getFieldValue();
				if (fieldValueObj == null
						|| fieldValueObj.toString().length() == 0) {
					bw.setPenalty(null);
				} else {
					int fieldValue = 0;
					if (fieldValueObj instanceof String) {
						String fieldValueString = (String) fieldValueObj;
						try {
							fieldValue = Integer.parseInt(fieldValueString);
						} catch (NumberFormatException e) {
							throw new CCFRuntimeException(
									"Could not parse value of field penalty: "
											+ e.getMessage(), e);
						}
					} else if (fieldValueObj instanceof Integer) {
						fieldValue = ((Integer) fieldValueObj).intValue();
					}
					bw.setPenalty(new Long(fieldValue));
				}
			}
			if (benefitHasChanged) {
				Object fieldValueObj = benefit.getFieldValue();
				if (fieldValueObj == null
						|| fieldValueObj.toString().length() == 0) {
					bw.setBenefit(null);
				} else {
					int fieldValue = 0;
					if (fieldValueObj instanceof String) {
						String fieldValueString = (String) fieldValueObj;
						try {
							fieldValue = Integer.parseInt(fieldValueString);
						} catch (NumberFormatException e) {
							throw new CCFRuntimeException(
									"Could not parse value of field benefit: "
											+ e.getMessage(), e);
						}
					} else if (fieldValueObj instanceof Integer) {
						fieldValue = ((Integer) fieldValueObj).intValue();
					}
					bw.setBenefit(new Long(fieldValue));
				}
			}
			pbi.setBusinessWeight(bw);
		}
		
		Product product = endpoint.getProductByName(swpProductName);
		
		List<Theme> currentlySetThemes = pbi.getThemes();
		// now updates the themes
		if (themes != null && !themes.isEmpty()) {
			Set<String> themeSet = new HashSet<String>();
			boolean nullValueSet = false;
			for (GenericArtifactField field : themes) {
				if (field.getFieldValueHasChanged()) {
					if (field.getFieldValue() != null) {
						themeSet.add(field.getFieldValue().toString());
					} else {
						nullValueSet = true;
					}
				}
			}
			if (!themeSet.isEmpty()) {
				// retrieve all themes of the product
				List<Theme> swpThemes = endpoint.getThemesForProduct(product.getId());
				if (swpThemes == null || swpThemes.size() == 0) {
					log.warn("Attempt to set themes not present in SWP.");
					for (String theme : themeSet) {
						log.warn("Missing theme: " + theme);
					}
				} else {
					List<Theme> swpThemeWSOs = new ArrayList<Theme>();
					for (Theme themeWSO : swpThemes) {
						if (themeSet.contains(themeWSO.getName())) {
							themeSet.remove(themeWSO.getName());
							swpThemeWSOs.add(themeWSO);
						}
					}
					currentlySetThemes.clear();
					currentlySetThemes.addAll(swpThemeWSOs);
					if (!themeSet.isEmpty()) {
						log.warn("Attempt to set themes not present in SWP.");
						for (String theme : themeSet) {
							log.warn("Missing theme: " + theme);
						}
					}
				}
			} else if (nullValueSet) {
				// if the null value was set specifically, we have to wipe all assigned themes
				// otherwise this just does mean that no theme value has been changed since the last update
				currentlySetThemes.clear();
			}
		} else {
			currentlySetThemes.clear();
		}

		// now determine the release (parent artifact)
		String parentArtifact = ga.getDepParentTargetArtifactId();
		if (parentArtifact == null
				|| parentArtifact.equals(GenericArtifact.VALUE_UNKNOWN)
				|| parentArtifact.equals(GenericArtifact.VALUE_NONE)
				|| !SWPMetaData.retrieveSWPTypeFromRepositoryId(
						ga.getDepParentTargetRepositoryId()).equals(
						SWPMetaData.SWPType.RELEASE)) {
			log
					.warn(parentArtifact
							+ " of repository "
							+ " is no valid release, so we do not change the associated release.");
		} else {
			pbi.setReleaseId(new Long(parentArtifact));
		}

		return endpoint.updateBacklogItem(pbi);
	}

	/**
	 * Updates SWP task
	 * 
	 * @param description
	 * @param estimatedHours
	 * @param pointPerson
	 * @param status
	 * @param taskBoardStatusRank
	 * @param title
	 * @param title2 
	 * @param ga
	 * @return
	 * @throws RemoteException
	 * @throws NumberFormatException
	 * @throws ScrumWorksException 
	 */
	public Task updateTask(GenericArtifactField description,
			GenericArtifactField estimatedHours, GenericArtifactField originalEstimate,
			GenericArtifactField pointPerson, GenericArtifactField status,
			GenericArtifactField title, GenericArtifact ga)
			throws NumberFormatException, RemoteException, ScrumWorksException {
		Task task = endpoint.getTaskById(new Long(ga.getTargetArtifactId()));
		// TODO Do conflict resolution
		if (description != null && description.getFieldValueHasChanged()) {
			task.setDescription((String) description.getFieldValue());
		}

		if (estimatedHours != null && estimatedHours.getFieldValueHasChanged()) {
			Object fieldValueObj = estimatedHours.getFieldValue();
			if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
				task.setCurrentEstimate(null);
			} else {
				int fieldValue = 0;
				if (fieldValueObj instanceof String) {
					String fieldValueString = (String) fieldValueObj;
					try {
						fieldValue = Integer.parseInt(fieldValueString);
					} catch (NumberFormatException e) {
						throw new CCFRuntimeException(
								"Could not parse value of field estimatedHours: "
										+ e.getMessage(), e);
					}
				} else if (fieldValueObj instanceof Integer) {
					fieldValue = ((Integer) fieldValueObj).intValue();
				}
				// we only set the estimated hours to zero if the previous value was not null
				if (task.getCurrentEstimate() != null || fieldValue != 0) {
					task.setCurrentEstimate(fieldValue);
				}
			}
		}
		
		if (originalEstimate != null && originalEstimate.getFieldValueHasChanged()) {
			Object fieldValueObj = originalEstimate.getFieldValue();
			if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
				task.setOriginalEstimate(null);
			} else {
				int fieldValue = 0;
				if (fieldValueObj instanceof String) {
					String fieldValueString = (String) fieldValueObj;
					try {
						fieldValue = Integer.parseInt(fieldValueString);
					} catch (NumberFormatException e) {
						throw new CCFRuntimeException(
								"Could not parse value of field originalEstimate: "
										+ e.getMessage(), e);
					}
				} else if (fieldValueObj instanceof Integer) {
					fieldValue = ((Integer) fieldValueObj).intValue();
				}
				// we only set the original estimate to zero if the previous value was not null
				if (task.getOriginalEstimate() != null || fieldValue != 0) {
					task.setOriginalEstimate(fieldValue);
				}
			}
		}

		if (pointPerson != null && pointPerson.getFieldValueHasChanged()) {
			task.setPointPerson((String) pointPerson.getFieldValue());
		}

		if (status != null && status.getFieldValueHasChanged()) {
			task.setStatus((String) status.getFieldValue());
		}

		if (title != null && title.getFieldValueHasChanged()) {
			task.setName((String) title.getFieldValue());
		}

		// decide whether we have to move the task to another PBI
		String parent = ga.getDepParentTargetArtifactId();
		String parentRepository = ga.getDepParentTargetRepositoryId();
		if (parent == null
				|| parentRepository == null
				|| parent.equals(GenericArtifact.VALUE_NONE)
				|| !SWPMetaData.retrieveSWPTypeFromRepositoryId(
						parentRepository).equals(SWPType.PBI)) {
			// do nothing
			log
					.warn("It looks as if somebody has deleted the parent child relationship between task "
							+ task.getId()
							+ " and PBI "
							+ task.getBacklogItemId()
							+ " in the source system and replaced it with a reference to unsupported parent artifact "
							+ parent
							+ " in repository "
							+ parentRepository
							+ ". Ignoring the change ...");
		} else {
			// compare current and anticipated parent id to decide whether to
			// move or not
			Long parentId = new Long(parent);
			if (!task.getBacklogItemId().equals(parentId)) {
				// TODO Check whether we have to use the move method here
				task.setBacklogItemId(parentId);
			}
		}

		return endpoint.updateTask(task);
	}

	/**
	 * Creates an SWP PBI
	 * 
	 * @param active
	 * @param benefit
	 * @param completedDate
	 * @param description
	 * @param estimate
	 * @param penalty
	 * @param title
	 * @param themes
	 * @param ga
	 * @return newly created PBI
	 * @throws RemoteException
	 * @throws ScrumWorksException 
	 */
	public BacklogItem createPBI(GenericArtifactField active,
			GenericArtifactField benefit, GenericArtifactField completedDate,
			GenericArtifactField description, GenericArtifactField estimate,
			GenericArtifactField penalty, GenericArtifactField title,
			List<GenericArtifactField> themes, String swpProductName,
			GenericArtifact ga) throws RemoteException, ScrumWorksException {
		BacklogItem pbi = new BacklogItem();
		if (active != null) {
			pbi.setActive((Boolean) active.getFieldValue());
		}

		if (completedDate != null) {
			pbi.setCompletedDate(calendarConverter.newXMLGregorianCalendar((GregorianCalendar) completedDate.getFieldValue()));
		}

		if (description != null) {
			pbi.setDescription((String) description.getFieldValue());
		}

		if (estimate != null) {
			Object fieldValueObj = estimate.getFieldValue();
			if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
				pbi.setEstimate(null);
			} else {
				int fieldValue = 0;
				if (fieldValueObj instanceof String) {
					String fieldValueString = (String) fieldValueObj;
					try {
						fieldValue = Integer.parseInt(fieldValueString);
					} catch (NumberFormatException e) {
						throw new CCFRuntimeException(
								"Could not parse value of field estimate: "
										+ e.getMessage(), e);
					}
				} else if (fieldValueObj instanceof Integer) {
					fieldValue = ((Integer) fieldValueObj).intValue();
				}
				pbi.setEstimate(fieldValue);
			}
		}

		if (title != null) {
			pbi.setName((String) title.getFieldValue());
		}

		boolean penaltyHasChanged = (penalty != null);
		boolean benefitHasChanged = (benefit != null);

		// only if at least one of penalty or benefit has changed, we have to
		// do the update
		if (penaltyHasChanged || benefitHasChanged) {
			BusinessWeight bw = pbi.getBusinessWeight();
			if (bw == null) {
				bw = new BusinessWeight();
				pbi.setBusinessWeight(bw);
			}
			if (penaltyHasChanged) {
				Object fieldValueObj = penalty.getFieldValue();
				if (fieldValueObj == null
						|| fieldValueObj.toString().length() == 0) {
					bw.setPenalty(null);
				} else {
					int fieldValue = 0;
					if (fieldValueObj instanceof String) {
						String fieldValueString = (String) fieldValueObj;
						try {
							fieldValue = Integer.parseInt(fieldValueString);
						} catch (NumberFormatException e) {
							throw new CCFRuntimeException(
									"Could not parse value of field penalty: "
											+ e.getMessage(), e);
						}
					} else if (fieldValueObj instanceof Integer) {
						fieldValue = ((Integer) fieldValueObj).intValue();
					}
					bw.setPenalty(new Long(fieldValue));
				}
			}
			if (benefitHasChanged) {
				Object fieldValueObj = benefit.getFieldValue();
				if (fieldValueObj == null
						|| fieldValueObj.toString().length() == 0) {
					bw.setBenefit(null);
				} else {
					int fieldValue = 0;
					if (fieldValueObj instanceof String) {
						String fieldValueString = (String) fieldValueObj;
						try {
							fieldValue = Integer.parseInt(fieldValueString);
						} catch (NumberFormatException e) {
							throw new CCFRuntimeException(
									"Could not parse value of field benefit: "
											+ e.getMessage(), e);
						}
					} else if (fieldValueObj instanceof Integer) {
						fieldValue = ((Integer) fieldValueObj).intValue();
					}
					bw.setBenefit(new Long(fieldValue));
				}
			}
		}

		Product product = endpoint.getProductByName(swpProductName);

		// now set the themes
		if (themes != null && !themes.isEmpty()) {
			Set<String> themeSet = new HashSet<String>();
			for (GenericArtifactField field : themes) {
				if (field.getFieldValue() != null) {
					themeSet.add(field.getFieldValue().toString());
				}
			}
			if (!themeSet.isEmpty()) {
				List<Theme> currentlySetThemes = pbi.getThemes();
				// retrieve all themes of the product
				List<Theme> swpThemes = endpoint.getThemesForProduct(product.getId());
				if (swpThemes == null || swpThemes.size() == 0) {
					log.warn("Attempt to set themes not present in SWP.");
					for (String theme : themeSet) {
						log.warn("Missing theme: " + theme);
					}
				} else {
					List<Theme> swpThemeWSOs = new ArrayList<Theme>();
					for (Theme themeWSO : swpThemes) {
						if (themeSet.contains(themeWSO.getName())) {
							themeSet.remove(themeWSO.getName());
							swpThemeWSOs.add(themeWSO);
						}
					}
					currentlySetThemes.clear();
					currentlySetThemes.addAll(swpThemeWSOs);
					if (!themeSet.isEmpty()) {
						log.warn("Attempt to set themes not present in SWP.");
						for (String theme : themeSet) {
							log.warn("Missing theme: " + theme);
						}
					}
				}
			}
		}

		// now set the product
		// TODO Do not use the symbolic product name but its id
		pbi.setProductId(product.getId());

		// now determine the release (parent artifact)
		String parentArtifact = ga.getDepParentTargetArtifactId();
		if (parentArtifact == null
				|| parentArtifact.equals(GenericArtifact.VALUE_UNKNOWN)
				|| parentArtifact.equals(GenericArtifact.VALUE_NONE)
				|| !SWPMetaData.retrieveSWPTypeFromRepositoryId(
						ga.getDepParentTargetRepositoryId()).equals(
						SWPMetaData.SWPType.RELEASE)) {
			// parent id is no release, we assign the first release in the list
			Release release = endpoint.getReleasesForProduct(endpoint
					.getProductByName(swpProductName).getId()).get(0);
			log
					.warn(parentArtifact
							+ " of parent repository " + ga.getDepParentTargetRepositoryId()
							+ " is no valid release, so assigning newly created PBI to first release in list: "
							+ release.getName());
			pbi.setReleaseId(release.getId());
		} else {
			pbi.setReleaseId(new Long(parentArtifact));
		}

		return endpoint.createBacklogItem(pbi);
	}

	/**
	 * Creates an SWP task
	 * 
	 * @param description
	 * @param estimatedHours
	 * @param pointPerson
	 * @param status
	 * @param title
	 * @param swpProductName
	 * @param ga
	 * @return newly created task
	 * @throws RemoteException
	 * @throws ScrumWorksException 
	 */
	public Task createTask(GenericArtifactField description,
			GenericArtifactField estimatedHours, GenericArtifactField originalEstimate,
			GenericArtifactField pointPerson, GenericArtifactField status,
			GenericArtifactField title, String swpProductName,
			GenericArtifact ga) throws RemoteException, ScrumWorksException {
		Task task = new Task();
		if (description != null) {
			task.setDescription((String) description.getFieldValue());
		}

		if (estimatedHours != null) {
			Object fieldValueObj = estimatedHours.getFieldValue();
			if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
				task.setCurrentEstimate(null);
			} else {
				int fieldValue = 0;
				if (fieldValueObj instanceof String) {
					String fieldValueString = (String) fieldValueObj;
					try {
						fieldValue = Integer.parseInt(fieldValueString);
					} catch (NumberFormatException e) {
						throw new CCFRuntimeException(
								"Could not parse value of field estimatedHours: "
										+ e.getMessage(), e);
					}
				} else if (fieldValueObj instanceof Integer) {
					fieldValue = ((Integer) fieldValueObj).intValue();
				}
				task.setCurrentEstimate(fieldValue);
			}
		}
		
		if (originalEstimate != null) {
			Object fieldValueObj = originalEstimate.getFieldValue();
			if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
				task.setOriginalEstimate(null);
			} else {
				int fieldValue = 0;
				if (fieldValueObj instanceof String) {
					String fieldValueString = (String) fieldValueObj;
					try {
						fieldValue = Integer.parseInt(fieldValueString);
					} catch (NumberFormatException e) {
						throw new CCFRuntimeException(
								"Could not parse value of field originalEstimate: "
										+ e.getMessage(), e);
					}
				} else if (fieldValueObj instanceof Integer) {
					fieldValue = ((Integer) fieldValueObj).intValue();
				}
				task.setOriginalEstimate(fieldValue);
			}
		}

		if (pointPerson != null) {
			task.setPointPerson((String) pointPerson.getFieldValue());
		}

		if (status != null) {
			task.setStatus((String) status.getFieldValue());
		}

		if (title != null) {
			task.setName((String) title.getFieldValue());
		}

		// now set the parent PBI
		String parent = ga.getDepParentTargetArtifactId();
		String parentRepository = ga.getDepParentTargetRepositoryId();
		if (parent == null
				|| parentRepository == null
				|| parent.equals(GenericArtifact.VALUE_NONE)
				|| !SWPMetaData.retrieveSWPTypeFromRepositoryId(
						parentRepository).equals(SWPType.PBI)) {
			String error = "It looks as if somebody created a task without a valid parent"
					+ " in the source system (unsupported parent artifact: "
					+ parent
					+ " in repository "
					+ parentRepository
					+ "). Bailing out ...";
			log.error(error);
			ga.setErrorCode(GenericArtifact.ERROR_PARENT_ARTIFACT_NOT_PRESENT);
			throw new CCFRuntimeException(error);
		} else {
			task.setBacklogItemId(new Long(parent));
		}

		return endpoint.createTask(task);
	}

	/**
	 * Returns the product whenever its properties change
	 * 
	 * @param swpProductName
	 * @param artifactStates
	 * @throws RemoteException
	 * @throws ScrumWorksException 
	 */
	public void getChangedProducts(String swpProductName,
			ArrayList<ArtifactState> artifactStates) throws
			RemoteException, ScrumWorksException {
		// TODO Implement polling
		Product product = endpoint.getProductByName(swpProductName);
		ArtifactState artifactState = new ArtifactState();
		artifactState.setArtifactId(product.getId().toString());
		artifactState.setArtifactLastModifiedDate(new Date(0));
		artifactState.setArtifactVersion(-1);
		artifactStates.add(artifactState);
	}

	/**
	 * Returns the product releases whenever their properties change
	 * 
	 * @param swpProductName
	 * @param artifactStates
	 * @throws RemoteException
	 * @throws ScrumWorksException 
	 */
	public void getChangedProductReleases(String swpProductName,
			ArrayList<ArtifactState> artifactStates) throws
			RemoteException, ScrumWorksException {
		// TODO Implement polling
		Product product = endpoint.getProductByName(swpProductName);
		List<Release> releases = endpoint.getReleasesForProduct(product.getId());
		if (releases != null) {
			for (Release releaseWSO : releases) {
				// currently, we treat program releases like product releases
				// since we have to duplicate them anyway
				ArtifactState artifactState = new ArtifactState();
				artifactState.setArtifactId(releaseWSO.getId().toString());
				artifactState.setArtifactLastModifiedDate(new Date(0));
				artifactState.setArtifactVersion(-1);
				artifactStates.add(artifactState);
			}
		}
	}

	/**
	 * Retrieves the properties of an SWP product and stores them into the
	 * passed generic artifact
	 * 
	 * @param artifactId
	 * @param swpProductName
	 * @param genericArtifact
	 * @throws RemoteException
	 * @throws ScrumWorksException 
	 */
	public void retrieveProduct(String artifactId, String swpProductName,
			GenericArtifact genericArtifact) throws
			RemoteException, ScrumWorksException {
		Product product = endpoint.getProductByName(swpProductName);
		addProductField(genericArtifact, ProductFields.id, product.getId());
		addProductField(genericArtifact, ProductFields.effortUnits, product
				.getEffortUnits());
		addProductField(genericArtifact, ProductFields.businessWeightUnits,
				product.getBusinessWeightUnits());
		addProductField(genericArtifact, ProductFields.keyPrefix, product
				.getKeyPrefix());
		addProductField(genericArtifact, ProductFields.name, product.getName());
		addProductField(genericArtifact, ProductFields.trackTimeSpent, product
				.isTrackTimeSpent());
	}

	/**
	 * Populates the generic artifact data structure with the properties of the
	 * requested product release
	 * 
	 * @param id
	 *            id of the PBI
	 * @param product
	 *            SWP product name
	 * @throws RemoteException
	 * @throws ScrumWorksException 
	 */
	public void retrieveProductRelease(String id, String product,
			GenericArtifact ga) throws NumberFormatException,
			RemoteException, ScrumWorksException {
		/**
		 * Currently, we treat program releases like product releases since we
		 * duplicate them anyways
		 */
		// TODO This is very inefficient code, we need better API calls here
		Product productWSO = endpoint.getProductByName(product);
		List<Release> releases = endpoint.getReleasesForProduct(productWSO.getId());
		if (releases != null) {
			for (Release releaseWSO : releases) {
				if (releaseWSO.getId().toString().equals(id)) {
					addProductReleaseField(ga, ReleaseFields.id,
							releaseWSO.getId());
					addProductReleaseField(ga, ReleaseFields.archived,
							releaseWSO.isArchived());
					addProductReleaseField(ga,
							ReleaseFields.description, releaseWSO
									.getDescription());
					addProductReleaseField(ga, ReleaseFields.productId,
							releaseWSO.getProductId());
					addProductReleaseField(ga, ReleaseFields.programId,
							releaseWSO.getProgramId());
					addProductReleaseField(ga,
							ReleaseFields.releaseDate, releaseWSO
									.getEndDate());
					addProductReleaseField(ga, ReleaseFields.startDate,
							releaseWSO.getStartDate());
					addProductReleaseField(ga, ReleaseFields.title,
							releaseWSO.getName());

					// set parent artifact (Product)
					ga.setDepParentSourceArtifactId(productWSO.getId()
							.toString());
					ga.setDepParentSourceRepositoryId(product
							+ SWPMetaData.REPOSITORY_ID_SEPARATOR
							+ SWPMetaData.PRODUCT);
					return;
				}
			}
		}
		throw new CCFRuntimeException("Could not find release with id: " + id);
	}

}
