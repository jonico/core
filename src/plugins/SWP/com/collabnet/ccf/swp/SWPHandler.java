package com.collabnet.ccf.swp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.swp.SWPMetaData.PBIFields;
import com.collabnet.ccf.swp.SWPMetaData.TaskFields;
import com.danube.scrumworks.api.client.ScrumWorksEndpoint;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.BusinessWeightWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;
import com.danube.scrumworks.api.client.types.ServerException;
import com.danube.scrumworks.api.client.types.TaskWSO;

/**
 * This class encapsulates all calls to the SWP backend
 * 
 * @author jnicolai
 * 
 */
public class SWPHandler {
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
}
