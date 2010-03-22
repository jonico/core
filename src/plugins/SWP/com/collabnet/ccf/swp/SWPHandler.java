package com.collabnet.ccf.swp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.swp.SWPMetaData.PBIFields;
import com.collabnet.ccf.swp.SWPMetaData.SWPType;
import com.collabnet.ccf.swp.SWPMetaData.TaskFields;
import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.BusinessWeightWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;
import com.danube.scrumworks.api.client.types.ReleaseWSO;
import com.danube.scrumworks.api.client.types.ServerException;
import com.danube.scrumworks.api.client.types.TaskWSO;

/**
 * This class encapsulates all calls to the SWP backend
 * 
 * @author jnicolai
 * 
 */
public class SWPHandler {

	private static final Log log = LogFactory.getLog(SWPHandler.class);
	private ScrumWorksEndpoint endpoint;

	/**
	 * Constructor, receives a valid SWP connection
	 * 
	 * @param connection
	 *            SWP connection object
	 */
	public SWPHandler(Connection connection) {
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
	 * @throws ServerException
	 */
	public void retrievePBI(String id, String product, GenericArtifact ga)
			throws ServerException, NumberFormatException, RemoteException {
		BacklogItemWSO pbi = endpoint.getBacklogItem(Long.valueOf(id));
		addPBIField(ga, PBIFields.id, pbi.getBacklogItemId());
		addPBIField(ga, PBIFields.active, pbi.isActive());
		BusinessWeightWSO bw = pbi.getBusinessWeight();
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
		addPBIField(ga, PBIFields.sprintId, pbi.getSprintId());
		addPBIField(ga, PBIFields.title, pbi.getTitle());
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
	 * @throws ServerException
	 */
	public void retrieveTask(String id, String product,
			GenericArtifact genericArtifact) throws ServerException,
			NumberFormatException, RemoteException {
		long taskId = Long.valueOf(id);
		final TaskWSO task = endpoint.getTaskById(taskId);
		final BacklogItemWSO backlogItem = endpoint.getBacklogItem(task
				.getBacklogItemId());

		addTaskField(genericArtifact, TaskFields.id, taskId);
		addTaskField(genericArtifact, TaskFields.description, task
				.getDescription());
		addTaskField(genericArtifact, TaskFields.estimatedHours, task
				.getEstimatedHours());
		addTaskField(genericArtifact, TaskFields.backlogItemId, task
				.getBacklogItemId());
		addTaskField(genericArtifact, TaskFields.pointPerson, task
				.getPointPerson());
		addTaskField(genericArtifact, TaskFields.rank, task.getRank());
		addTaskField(genericArtifact, TaskFields.status, task.getStatus());
		addTaskField(genericArtifact, TaskFields.taskBoardStatusRank, task
				.getTaskBoardStatusRank());
		addTaskField(genericArtifact, TaskFields.title, task.getTitle());

		// set parent artifact (PBI)
		genericArtifact.setDepParentSourceArtifactId(backlogItem
				.getBacklogItemId().toString());
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
	 * Return the tasks that have been changed since the last query
	 * 
	 * @param swpProductName
	 *            SWP product to retrieve the tasks from
	 * @param artifactStates
	 *            list that will be populated with changed tasks
	 * @throws RemoteException
	 * @throws ServerException
	 */
	public void getChangedTasks(String swpProductName,
			List<ArtifactState> artifactStates) throws ServerException,
			RemoteException {
		ProductWSO product = endpoint.getProductByName(swpProductName);
		BacklogItemWSO[] pbis = endpoint.getActiveBacklogItems(product);
		if (pbis != null) {
			for (BacklogItemWSO pbi : pbis) {
				TaskWSO[] tasks = endpoint.getTasks(pbi);
				if (tasks != null) {
					for (TaskWSO task : tasks) {
						ArtifactState artifactState = new ArtifactState();
						artifactState.setArtifactId(task.getId().toString());
						artifactState.setArtifactLastModifiedDate(new Date(0));
						artifactState.setArtifactVersion(-1);
						artifactStates.add(artifactState);
					}
				}
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
	 * @throws ServerException
	 * @throws RemoteException
	 * @throws ServerException
	 */
	public void getChangedPBIs(String swpProductName,
			ArrayList<ArtifactState> artifactStates) throws ServerException,
			RemoteException {
		ProductWSO product = endpoint.getProductByName(swpProductName);
		BacklogItemWSO[] pbis = endpoint.getActiveBacklogItems(product);
		if (pbis != null) {
			for (BacklogItemWSO pbi : pbis) {
				ArtifactState artifactState = new ArtifactState();
				artifactState.setArtifactId(pbi.getBacklogItemId().toString());
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
	 * @param ga
	 * @return updated PBI
	 * @throws RemoteException
	 * @throws NumberFormatException
	 * @throws ServerException
	 */
	public BacklogItemWSO updatePBI(GenericArtifactField active,
			GenericArtifactField benefit, GenericArtifactField completedDate,
			GenericArtifactField description, GenericArtifactField estimate,
			GenericArtifactField penalty, GenericArtifactField title,
			GenericArtifact ga) throws ServerException, NumberFormatException,
			RemoteException {
		BacklogItemWSO pbi = endpoint.getBacklogItem(new Long(ga
				.getTargetArtifactId()));
		// TODO Do conflict resolution
		if (active != null && active.getFieldValueHasChanged()) {
			pbi.setActive((Boolean) active.getFieldValue());
		}

		if (completedDate != null && completedDate.getFieldValueHasChanged()) {
			pbi.setCompletedDate((Calendar) completedDate.getFieldValue());
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
			pbi.setTitle((String) title.getFieldValue());
		}

		boolean penaltyHasChanged = (penalty != null && penalty
				.getFieldValueHasChanged());
		boolean benefitHasChanged = (benefit != null && benefit
				.getFieldValueHasChanged());

		// only if at least one of penalty or benefit has changed, we have to
		// do the update
		if (penaltyHasChanged || benefitHasChanged) {
			BusinessWeightWSO bw = pbi.getBusinessWeight();
			if (penaltyHasChanged) {
				Object fieldValueObj = penalty.getFieldValue();
				if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
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
				if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
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
	 * @param ga
	 * @return
	 * @throws RemoteException
	 * @throws NumberFormatException
	 * @throws ServerException
	 */
	public TaskWSO updateTask(GenericArtifactField description,
			GenericArtifactField estimatedHours,
			GenericArtifactField pointPerson, GenericArtifactField status,
			GenericArtifactField title, GenericArtifact ga)
			throws ServerException, NumberFormatException, RemoteException {
		TaskWSO task = endpoint.getTaskById(new Long(ga.getTargetArtifactId()));
		// TODO Do conflict resolution
		if (description != null && description.getFieldValueHasChanged()) {
			task.setDescription((String) description.getFieldValue());
		}

		if (estimatedHours != null && estimatedHours.getFieldValueHasChanged()) {
			Object fieldValueObj = estimatedHours.getFieldValue();
			if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
				task.setEstimatedHours(null);
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
				task.setEstimatedHours(fieldValue);
			}
		}

		if (pointPerson != null && pointPerson.getFieldValueHasChanged()) {
			task.setPointPerson((String) pointPerson.getFieldValue());
		}

		if (status != null && status.getFieldValueHasChanged()) {
			task.setStatus((String) status.getFieldValue());
		}

		if (title != null && title.getFieldValueHasChanged()) {
			task.setTitle((String) title.getFieldValue());
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
	 * @param active
	 * @param benefit
	 * @param completedDate
	 * @param description
	 * @param estimate
	 * @param penalty
	 * @param title
	 * @param ga
	 * @return newly created PBI
	 * @throws RemoteException 
	 * @throws ServerException 
	 */
	public BacklogItemWSO createPBI(GenericArtifactField active,
			GenericArtifactField benefit, GenericArtifactField completedDate,
			GenericArtifactField description, GenericArtifactField estimate,
			GenericArtifactField penalty, GenericArtifactField title, String swpProductName,
			GenericArtifact ga) throws ServerException, RemoteException {
		BacklogItemWSO pbi = new BacklogItemWSO();
		if (active != null && active.getFieldValueHasChanged()) {
			pbi.setActive((Boolean) active.getFieldValue());
		}

		if (completedDate != null && completedDate.getFieldValueHasChanged()) {
			pbi.setCompletedDate((Calendar) completedDate.getFieldValue());
		}

		if (description != null && description.getFieldValueHasChanged()) {
			pbi.setDescription((String) description.getFieldValue());
		}

		if (estimate != null && estimate.getFieldValueHasChanged()) {
			pbi.setEstimate((Integer) estimate.getFieldValue());
		}

		if (title != null && title.getFieldValueHasChanged()) {
			pbi.setTitle((String) title.getFieldValue());
		}

		boolean penaltyHasChanged = (penalty != null && penalty
				.getFieldValueHasChanged());
		boolean benefitHasChanged = (benefit != null && benefit
				.getFieldValueHasChanged());

		// only if at least one of penalty or benefit has changed, we have to
		// do the update
		if (penaltyHasChanged || benefitHasChanged) {
			BusinessWeightWSO bw = pbi.getBusinessWeight();
			if (penaltyHasChanged) {
				Object fieldValueObj = penalty.getFieldValue();
				if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
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
				if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
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
		
		// now set the product 
		// TODO Do not use the symbolic product name but its id
		pbi.setProductId(endpoint.getProductByName(swpProductName).getId());
		
		if (pbi.getReleaseId() == null) {
			// we have to set a valid release for our testing purposes
			ReleaseWSO release = endpoint.getReleasesForProduct(endpoint.getProductByName(swpProductName))[0];
			log.info("No release specified, so assigning to first release in list: " + release.getTitle());
			pbi.setReleaseId(release.getId());
		}
		
		return endpoint.createBacklogItem(pbi);
	}

	/**
	 * Creates an SWP task
	 * @param description
	 * @param estimatedHours
	 * @param pointPerson
	 * @param status
	 * @param title
	 * @param swpProductName
	 * @param ga
	 * @return newly created task
	 * @throws RemoteException 
	 * @throws ServerException 
	 */
	public TaskWSO createTask(GenericArtifactField description,
			GenericArtifactField estimatedHours,
			GenericArtifactField pointPerson, GenericArtifactField status,
			GenericArtifactField title, String swpProductName,
			GenericArtifact ga) throws ServerException, RemoteException {
		TaskWSO task = new TaskWSO();
		if (description != null && description.getFieldValueHasChanged()) {
			task.setDescription((String) description.getFieldValue());
		}

		if (estimatedHours != null && estimatedHours.getFieldValueHasChanged()) {
			Object fieldValueObj = estimatedHours.getFieldValue();
			if (fieldValueObj == null || fieldValueObj.toString().length() == 0) {
				task.setEstimatedHours(null);
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
				task.setEstimatedHours(fieldValue);
			}
		}

		if (pointPerson != null && pointPerson.getFieldValueHasChanged()) {
			task.setPointPerson((String) pointPerson.getFieldValue());
		}

		if (status != null && status.getFieldValueHasChanged()) {
			task.setStatus((String) status.getFieldValue());
		}

		if (title != null && title.getFieldValueHasChanged()) {
			task.setTitle((String) title.getFieldValue());
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

}
