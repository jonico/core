package com.collabnet.ccf.swp;

import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.swp.SWPMetaData.PBIFields;
import com.collabnet.ccf.swp.SWPMetaData.ProductFields;
import com.collabnet.ccf.swp.SWPMetaData.ReleaseFields;
import com.collabnet.ccf.swp.SWPMetaData.SWPType;
import com.collabnet.ccf.swp.SWPMetaData.TaskFields;
import com.danube.scrumworks.api2.client.AggregateVersionedData;
import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.BacklogItemChanges;
import com.danube.scrumworks.api2.client.BusinessWeight;
import com.danube.scrumworks.api2.client.Product;
import com.danube.scrumworks.api2.client.ProductChanges;
import com.danube.scrumworks.api2.client.Release;
import com.danube.scrumworks.api2.client.ReleaseChanges;
import com.danube.scrumworks.api2.client.RevisionInfo;
import com.danube.scrumworks.api2.client.ScrumWorksAPIService;
import com.danube.scrumworks.api2.client.ScrumWorksException;
import com.danube.scrumworks.api2.client.Sprint;
import com.danube.scrumworks.api2.client.Task;
import com.danube.scrumworks.api2.client.TaskChanges;
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
	private DatatypeFactory calendarConverter;

	// caching data structures
	// AbstractMap.SimpleEntry is only used because it comes close to the Pair
	// construct in C++
	private Map<String, Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, BacklogItem>>> pbiCache = new HashMap<String, Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, BacklogItem>>>();
	private Map<String, Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Product>>> productCache = new HashMap<String, Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Product>>>();
	private Map<String, Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Release>>> releaseCache = new HashMap<String, Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Release>>>();
	private Map<String, Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Task>>> taskCache = new HashMap<String, Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Task>>>();
	/**
	 * This constant is used as a factor for SWP revission numbers The rationale
	 * is to be able to differentiate between artifacts that have been changed
	 * in the same SWP revision. By multiplying with the SWP_REVISION_FACTOR and
	 * adding the sorted list index to the artifact in question, we have a
	 * unique version number for every single change.
	 */
	static final long SWP_REVISION_FACTOR = 10000000;

	/**
	 * Constructor, receives a valid SWP connection
	 * 
	 * @param connection
	 *            SWP connection object
	 * @throws DatatypeConfigurationException
	 */
	public SWPHandler() throws DatatypeConfigurationException {
		calendarConverter = DatatypeFactory.newInstance();
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
	public void retrievePBI(ScrumWorksAPIService endpoint, String id,
			String product, GenericArtifact ga) throws NumberFormatException,
			RemoteException, ScrumWorksException {
		// BacklogItem pbi = endpoint.getBacklogItemById(Long.valueOf(id));
		AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, BacklogItem> cachedPBI = pbiCache
				.get(product).get(Long.valueOf(id));
		if (cachedPBI == null) {
			throw new CCFRuntimeException("Could not retrieve PBI " + id
					+ "from the cache.");
		}
		long artificialVersionNumber = cachedPBI.getKey().getKey();
		RevisionInfo pbiRevision = cachedPBI.getKey().getValue();
		BacklogItem pbi = cachedPBI.getValue();

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
				+ SWPMetaData.REPOSITORY_ID_SEPARATOR + SWPMetaData.RELEASE);

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
			addPBIField(ga, PBIFields.sprintStart, sprintWSO.getStartDate());
			addPBIField(ga, PBIFields.sprintEnd, sprintWSO.getEndDate());
			// retrieve team name
			Team team = endpoint.getTeamById(sprintWSO.getTeamId());
			addPBIField(ga, PBIFields.team, team.getName());
		}

		ga.setSourceArtifactVersion(Long.toString(artificialVersionNumber));
		Date artifactLastModifiedDate = new Date(0);
		if (pbiRevision.getTimeStamp() != null) {
			artifactLastModifiedDate = pbiRevision.getTimeStamp()
					.toGregorianCalendar().getTime();
		}
		ga.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df
				.format(artifactLastModifiedDate));
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
	public void retrieveTask(ScrumWorksAPIService endpoint, String id,
			String product, GenericArtifact genericArtifact)
			throws NumberFormatException, RemoteException, ScrumWorksException {
		long taskId = Long.valueOf(id);
		// final Task task = endpoint.getTaskById(taskId);
		AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Task> cachedTask = taskCache
				.get(product).get(taskId);
		if (cachedTask == null) {
			throw new CCFRuntimeException("Could not retrieve task " + id
					+ "from the cache.");
		}
		long artificialVersionNumber = cachedTask.getKey().getKey();
		RevisionInfo taskRevision = cachedTask.getKey().getValue();
		Task task = cachedTask.getValue();

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
		// addTaskField(genericArtifact, TaskFields.taskBoardStatusRank, task
		// .get);
		addTaskField(genericArtifact, TaskFields.title, task.getName());

		// set parent artifact (PBI)
		genericArtifact.setDepParentSourceArtifactId(task.getBacklogItemId()
				.toString());
		genericArtifact.setDepParentSourceRepositoryId(product
				+ SWPMetaData.REPOSITORY_ID_SEPARATOR + SWPMetaData.PBI);

		genericArtifact.setSourceArtifactVersion(Long
				.toString(artificialVersionNumber));
		Date artifactLastModifiedDate = new Date(0);
		if (taskRevision.getTimeStamp() != null) {
			artifactLastModifiedDate = taskRevision.getTimeStamp()
					.toGregorianCalendar().getTime();
		}
		genericArtifact
				.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df
						.format(artifactLastModifiedDate));
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
	public void getChangedTasks(ScrumWorksAPIService endpoint,
			String swpProductName, List<ArtifactState> artifactStates,
			long majorVersion, long minorVersion, String connectorUser)
			throws RemoteException, ScrumWorksException {
		// Product product = endpoint.getProductByName(swpProductName);
		/*
		 * find out whether we have to start querying at the next revision or
		 * whether there are still some pending shipments of the current
		 * revision
		 */
		if (minorVersion % 2 == 1) {
			++majorVersion;
			minorVersion = 0;
		}

		Product product = endpoint.getProductByName(swpProductName);

		// now do the query, passed revision number is not included in the
		// result set
		int queryVersion = new Long(majorVersion == 0 ? 0 : majorVersion - 1)
				.intValue();
		AggregateVersionedData changesSinceCurrentRevision = endpoint
				.getChangesSinceRevision(product.getId(), queryVersion);

		// initialize some data structures to capture deleted and inserted
		// artifacts
		Set<Long> alreadyProcessedArtifacts = new HashSet<Long>();

		List<TaskChanges> pbiSpecificChanges = changesSinceCurrentRevision
				.getTaskChanges();
		ListIterator<TaskChanges> it = pbiSpecificChanges
				.listIterator(pbiSpecificChanges.size());
		while (it.hasPrevious()) {
			TaskChanges taskSpecificChangesInRevision = it.previous();

			RevisionInfo processedRevisionInfo = taskSpecificChangesInRevision
					.getRevisionInfo();
			int processedRevisionNumber = processedRevisionInfo
					.getRevisionNumber();
			List<Task> changedOrAddedTasks = taskSpecificChangesInRevision
					.getAddedOrChangedEntities();
			List<Long> deletedArtifacts = taskSpecificChangesInRevision
					.getDeletedIds();

			// insert all already deleted artifacts in the processed artifacts
			// list
			alreadyProcessedArtifacts.addAll(deletedArtifacts);

			// check whether this revision have been caused by the connector
			// user itself
			if (processedRevisionInfo.getUserName().equals(connectorUser)) {
				// insert all changed and created artifacts in the processed
				// artifacts list
				for (Task task : changedOrAddedTasks) {
					alreadyProcessedArtifacts.add(task.getId());
				}
			} else if (!changedOrAddedTasks.isEmpty()) {
				// now we have to sort the changed/created artifacts
				Collections.sort(changedOrAddedTasks, new Comparator<Task>() {

					@Override
					public int compare(Task arg0, Task arg1) {
						return arg0.getId().compareTo(arg1.getId());
					}
				});
			}

			int numberOfChangedOrAddedArtifacts = changedOrAddedTasks.size();

			ListIterator<Task> it2 = changedOrAddedTasks
					.listIterator(numberOfChangedOrAddedArtifacts);
			int processedMinorVersion = numberOfChangedOrAddedArtifacts * 2;
			boolean lastItemInTransaction = true;
			while (it2.hasPrevious()) {
				Task artifact = it2.previous();
				/*
				 * Check whether we already transported this artifact or if the
				 * current revision number is greater than the major version the
				 * change has to transported. If this condition is not true, the
				 * major revision is equal to the current revision number (since
				 * earlier versions are not returned by the API) and we have to
				 * use the minor version to determine what has been already
				 * shipped
				 */
				if (majorVersion < processedRevisionNumber
						|| (majorVersion == processedRevisionNumber && minorVersion < processedMinorVersion)) {
					// check whether artifact has been already processed
					if (!alreadyProcessedArtifacts.contains(artifact.getId())) {
						// now prepend item to the list
						ArtifactState artifactState = new ArtifactState();
						artifactState
								.setArtifactId(artifact.getId().toString());
						XMLGregorianCalendar xmlTimestamp = processedRevisionInfo
								.getTimeStamp();
						Date artifactLastModifiedDate = new Date(0);
						if (xmlTimestamp != null) {
							artifactLastModifiedDate = xmlTimestamp
									.toGregorianCalendar().getTime();
						}
						artifactState
								.setArtifactLastModifiedDate(artifactLastModifiedDate);
						long artificialRevisionNumber = processedRevisionNumber
								* SWP_REVISION_FACTOR + processedMinorVersion
								+ (lastItemInTransaction ? 1 : 0);
						artifactState
								.setArtifactVersion(artificialRevisionNumber);
						artifactStates.add(0, artifactState);
						// now update the cache
						Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Task>> productSpecificCache = taskCache
								.get(swpProductName);
						if (productSpecificCache == null) {
							productSpecificCache = new HashMap<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Task>>();
							taskCache.put(swpProductName, productSpecificCache);
						}
						productSpecificCache
								.put(
										artifact.getId(),
										new AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Task>(
												new AbstractMap.SimpleEntry<Long, RevisionInfo>(
														artificialRevisionNumber,
														processedRevisionInfo),
												artifact));
						lastItemInTransaction = false;
					}
				}
				processedMinorVersion -= 2;
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
	public void getChangedPBIs(ScrumWorksAPIService endpoint,
			String swpProductName, ArrayList<ArtifactState> artifactStates,
			long majorVersion, long minorVersion, String connectorUser)
			throws RemoteException, ScrumWorksException {
		/*
		 * find out whether we have to start querying at the next revision or
		 * whether there are still some pending shipments of the current
		 * revision
		 */
		if (minorVersion % 2 == 1) {
			++majorVersion;
			minorVersion = 0;
		}

		Product product = endpoint.getProductByName(swpProductName);

		// now do the query, passed revision number is not included in the
		// result set
		int queryVersion = new Long(majorVersion == 0 ? 0 : majorVersion - 1)
				.intValue();
		AggregateVersionedData changesSinceCurrentRevision = endpoint
				.getChangesSinceRevision(product.getId(), queryVersion);

		// initialize some data structures to capture deleted and inserted
		// artifacts
		Set<Long> alreadyProcessedArtifacts = new HashSet<Long>();

		List<BacklogItemChanges> pbiSpecificChanges = changesSinceCurrentRevision
				.getBacklogItemChanges();
		ListIterator<BacklogItemChanges> it = pbiSpecificChanges
				.listIterator(pbiSpecificChanges.size());
		while (it.hasPrevious()) {
			BacklogItemChanges pbiSpecificChangesInRevision = it.previous();

			RevisionInfo processedRevisionInfo = pbiSpecificChangesInRevision
					.getRevisionInfo();
			int processedRevisionNumber = processedRevisionInfo
					.getRevisionNumber();
			List<BacklogItem> changedOrAddedPBIs = pbiSpecificChangesInRevision
					.getAddedOrChangedEntities();
			List<Long> deletedArtifacts = pbiSpecificChangesInRevision
					.getDeletedIds();

			// insert all already deleted artifacts in the processed artifacts
			// list
			alreadyProcessedArtifacts.addAll(deletedArtifacts);

			// check whether this revision have been caused by the connector
			// user itself
			if (processedRevisionInfo.getUserName().equals(connectorUser)) {
				// insert all changed and created artifacts in the processed
				// artifacts list
				for (BacklogItem pbi : changedOrAddedPBIs) {
					alreadyProcessedArtifacts.add(pbi.getId());
				}
			} else if (!changedOrAddedPBIs.isEmpty()) {
				// now we have to sort the changed/created artifacts
				Collections.sort(changedOrAddedPBIs,
						new Comparator<BacklogItem>() {

							@Override
							public int compare(BacklogItem arg0,
									BacklogItem arg1) {
								return arg0.getId().compareTo(arg1.getId());
							}
						});
			}

			int numberOfChangedOrAddedArtifacts = changedOrAddedPBIs.size();

			ListIterator<BacklogItem> it2 = changedOrAddedPBIs
					.listIterator(numberOfChangedOrAddedArtifacts);
			int processedMinorVersion = numberOfChangedOrAddedArtifacts * 2;
			boolean lastItemInTransaction = true;
			while (it2.hasPrevious()) {
				BacklogItem artifact = it2.previous();
				/*
				 * Check whether we already transported this artifact or if the
				 * current revision number is greater than the major version the
				 * change has to transported. If this condition is not true, the
				 * major revision is equal to the current revision number (since
				 * earlier versions are not returned by the API) and we have to
				 * use the minor version to determine what has been already
				 * shipped
				 */
				if (majorVersion < processedRevisionNumber
						|| (majorVersion == processedRevisionNumber && minorVersion < processedMinorVersion)) {
					// check whether artifact has been already processed
					if (!alreadyProcessedArtifacts.contains(artifact.getId())) {
						// now prepend item to the list
						ArtifactState artifactState = new ArtifactState();
						artifactState
								.setArtifactId(artifact.getId().toString());
						XMLGregorianCalendar xmlTimestamp = processedRevisionInfo
								.getTimeStamp();
						Date artifactLastModifiedDate = new Date(0);
						if (xmlTimestamp != null) {
							artifactLastModifiedDate = xmlTimestamp
									.toGregorianCalendar().getTime();
						}
						artifactState
								.setArtifactLastModifiedDate(artifactLastModifiedDate);
						long artificialRevisionNumber = processedRevisionNumber
								* SWP_REVISION_FACTOR + processedMinorVersion
								+ (lastItemInTransaction ? 1 : 0);
						artifactState
								.setArtifactVersion(artificialRevisionNumber);
						artifactStates.add(0, artifactState);
						// now update the cache
						Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, BacklogItem>> productSpecificCache = pbiCache
								.get(swpProductName);
						if (productSpecificCache == null) {
							productSpecificCache = new HashMap<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, BacklogItem>>();
							pbiCache.put(swpProductName, productSpecificCache);
						}
						productSpecificCache
								.put(
										artifact.getId(),
										new AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, BacklogItem>(
												new AbstractMap.SimpleEntry<Long, RevisionInfo>(
														artificialRevisionNumber,
														processedRevisionInfo),
												artifact));
						lastItemInTransaction = false;
					}
				}
				processedMinorVersion -= 2;
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
	public BacklogItem updatePBI(ScrumWorksAPIService endpoint,
			GenericArtifactField active, GenericArtifactField benefit,
			GenericArtifactField completedDate,
			GenericArtifactField description, GenericArtifactField estimate,
			GenericArtifactField penalty, GenericArtifactField title,
			List<GenericArtifactField> themes, String swpProductName,
			GenericArtifact ga) throws NumberFormatException, RemoteException,
			ScrumWorksException {
		BacklogItem pbi = endpoint.getBacklogItemById(new Long(ga
				.getTargetArtifactId()));
		// TODO Do conflict resolution
		if (active != null && active.getFieldValueHasChanged()) {
			pbi.setActive((Boolean) active.getFieldValue());
		}

		if (completedDate != null && completedDate.getFieldValueHasChanged()) {
			GregorianCalendar completedDateFieldValue = (GregorianCalendar) completedDate
					.getFieldValue();
			if (completedDateFieldValue == null) {
				pbi.setCompletedDate(null);
			} else {
				pbi.setCompletedDate(calendarConverter
						.newXMLGregorianCalendar(completedDateFieldValue));
			}
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
				List<Theme> swpThemes = endpoint.getThemesForProduct(product
						.getId());
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
				// if the null value was set specifically, we have to wipe all
				// assigned themes
				// otherwise this just does mean that no theme value has been
				// changed since the last update
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
	public Task updateTask(ScrumWorksAPIService endpoint,
			GenericArtifactField description,
			GenericArtifactField estimatedHours,
			GenericArtifactField originalEstimate,
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
				// we only set the estimated hours to zero if the previous value
				// was not null
				if (task.getCurrentEstimate() != null || fieldValue != 0) {
					task.setCurrentEstimate(fieldValue);
				}
			}
		}

		if (originalEstimate != null
				&& originalEstimate.getFieldValueHasChanged()) {
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
				// we only set the original estimate to zero if the previous
				// value was not null
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
	public BacklogItem createPBI(ScrumWorksAPIService endpoint,
			GenericArtifactField active, GenericArtifactField benefit,
			GenericArtifactField completedDate,
			GenericArtifactField description, GenericArtifactField estimate,
			GenericArtifactField penalty, GenericArtifactField title,
			List<GenericArtifactField> themes, String swpProductName,
			GenericArtifact ga) throws RemoteException, ScrumWorksException {
		BacklogItem pbi = new BacklogItem();
		if (active != null) {
			pbi.setActive((Boolean) active.getFieldValue());
		}

		if (completedDate != null) {
			GregorianCalendar completedDateFieldValue = (GregorianCalendar) completedDate
					.getFieldValue();
			if (completedDateFieldValue == null) {
				pbi.setCompletedDate(null);
			} else {
				pbi.setCompletedDate(calendarConverter
						.newXMLGregorianCalendar(completedDateFieldValue));
			}
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
				List<Theme> swpThemes = endpoint.getThemesForProduct(product
						.getId());
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
			Release release = endpoint.getReleasesForProduct(
					endpoint.getProductByName(swpProductName).getId()).get(0);
			log
					.warn(parentArtifact
							+ " of parent repository "
							+ ga.getDepParentTargetRepositoryId()
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
	public Task createTask(ScrumWorksAPIService endpoint,
			GenericArtifactField description,
			GenericArtifactField estimatedHours,
			GenericArtifactField originalEstimate,
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
	public void getChangedProducts(ScrumWorksAPIService endpoint,
			String swpProductName, ArrayList<ArtifactState> artifactStates,
			long majorVersion, long minorVersion, String connectorUser)
			throws RemoteException, ScrumWorksException {
		// TODO Can we do some optimizations here since we should only get one product item back?
		
		/*
		 * find out whether we have to start querying at the next revision or
		 * whether there are still some pending shipments of the current
		 * revision
		 */
		if (minorVersion % 2 == 1) {
			++majorVersion;
			minorVersion = 0;
		}

		Product product = endpoint.getProductByName(swpProductName);

		// now do the query, passed revision number is not included in the
		// result set
		int queryVersion = new Long(majorVersion == 0 ? 0 : majorVersion - 1)
				.intValue();
		AggregateVersionedData changesSinceCurrentRevision = endpoint
				.getChangesSinceRevision(product.getId(), queryVersion);

		// initialize some data structures to capture deleted and inserted
		// artifacts
		Set<Long> alreadyProcessedArtifacts = new HashSet<Long>();

		List<ProductChanges> productSpecificChanges = changesSinceCurrentRevision
				.getProductChanges();
		ListIterator<ProductChanges> it = productSpecificChanges
				.listIterator(productSpecificChanges.size());
		while (it.hasPrevious()) {
			ProductChanges productSpecificChangesInRevision = it.previous();

			RevisionInfo processedRevisionInfo = productSpecificChangesInRevision
					.getRevisionInfo();
			int processedRevisionNumber = processedRevisionInfo
					.getRevisionNumber();
			List<Product> changedOrAddedProducts = productSpecificChangesInRevision
					.getAddedOrChangedEntities();
			List<Long> deletedArtifacts = productSpecificChangesInRevision
					.getDeletedIds();

			// insert all already deleted artifacts in the processed artifacts
			// list
			alreadyProcessedArtifacts.addAll(deletedArtifacts);

			// check whether this revision have been caused by the connector
			// user itself
			if (processedRevisionInfo.getUserName().equals(connectorUser)) {
				// insert all changed and created artifacts in the processed
				// artifacts list
				for (Product prod : changedOrAddedProducts) {
					alreadyProcessedArtifacts.add(prod.getId());
				}
			} else if (!changedOrAddedProducts.isEmpty()) {
				// now we have to sort the changed/created artifacts
				Collections.sort(changedOrAddedProducts,
						new Comparator<Product>() {

							@Override
							public int compare(Product arg0,
									Product arg1) {
								return arg0.getId().compareTo(arg1.getId());
							}
						});
			}

			int numberOfChangedOrAddedArtifacts = changedOrAddedProducts.size();

			ListIterator<Product> it2 = changedOrAddedProducts
					.listIterator(numberOfChangedOrAddedArtifacts);
			int processedMinorVersion = numberOfChangedOrAddedArtifacts * 2;
			boolean lastItemInTransaction = true;
			while (it2.hasPrevious()) {
				Product artifact = it2.previous();
				/*
				 * Check whether we already transported this artifact or if the
				 * current revision number is greater than the major version the
				 * change has to transported. If this condition is not true, the
				 * major revision is equal to the current revision number (since
				 * earlier versions are not returned by the API) and we have to
				 * use the minor version to determine what has been already
				 * shipped
				 */
				if (majorVersion < processedRevisionNumber
						|| (majorVersion == processedRevisionNumber && minorVersion < processedMinorVersion)) {
					// check whether artifact has been already processed
					if (!alreadyProcessedArtifacts.contains(artifact.getId())) {
						// now prepend item to the list
						ArtifactState artifactState = new ArtifactState();
						artifactState
								.setArtifactId(artifact.getId().toString());
						XMLGregorianCalendar xmlTimestamp = processedRevisionInfo
								.getTimeStamp();
						Date artifactLastModifiedDate = new Date(0);
						if (xmlTimestamp != null) {
							artifactLastModifiedDate = xmlTimestamp
									.toGregorianCalendar().getTime();
						}
						artifactState
								.setArtifactLastModifiedDate(artifactLastModifiedDate);
						long artificialRevisionNumber = processedRevisionNumber
								* SWP_REVISION_FACTOR + processedMinorVersion
								+ (lastItemInTransaction ? 1 : 0);
						artifactState
								.setArtifactVersion(artificialRevisionNumber);
						artifactStates.add(0, artifactState);
						// now update the cache
						Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Product>> productSpecificCache = productCache
								.get(swpProductName);
						if (productSpecificCache == null) {
							productSpecificCache = new HashMap<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Product>>();
							productCache.put(swpProductName, productSpecificCache);
						}
						productSpecificCache
								.put(
										artifact.getId(),
										new AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Product>(
												new AbstractMap.SimpleEntry<Long, RevisionInfo>(
														artificialRevisionNumber,
														processedRevisionInfo),
												artifact));
						lastItemInTransaction = false;
					}
				}
				processedMinorVersion -= 2;
			}
		}
	}

	/**
	 * Returns the product releases whenever their properties change
	 * 
	 * @param swpProductName
	 * @param artifactStates
	 * @param version
	 * @throws RemoteException
	 * @throws ScrumWorksException
	 */
	public void getChangedReleases(ScrumWorksAPIService endpoint,
			String swpProductName, ArrayList<ArtifactState> artifactStates,
			long majorVersion, long minorVersion, String connectorUser)
			throws RemoteException, ScrumWorksException {
		// TODO Implement polling
		/*
		 * find out whether we have to start querying at the next revision or
		 * whether there are still some pending shipments of the current
		 * revision
		 */
		if (minorVersion % 2 == 1) {
			++majorVersion;
			minorVersion = 0;
		}

		Product product = endpoint.getProductByName(swpProductName);

		// now do the query, passed revision number is not included in the
		// result set
		int queryVersion = new Long(majorVersion == 0 ? 0 : majorVersion - 1)
				.intValue();
		AggregateVersionedData changesSinceCurrentRevision = endpoint
				.getChangesSinceRevision(product.getId(), queryVersion);

		// initialize some data structures to capture deleted and inserted
		// artifacts
		Set<Long> alreadyProcessedArtifacts = new HashSet<Long>();

		List<ReleaseChanges> releaseSpecificChanges = changesSinceCurrentRevision
				.getReleaseChanges();
		ListIterator<ReleaseChanges> it = releaseSpecificChanges
				.listIterator(releaseSpecificChanges.size());
		while (it.hasPrevious()) {
			ReleaseChanges releaseSpecificChangesInRevision = it.previous();

			RevisionInfo processedRevisionInfo = releaseSpecificChangesInRevision
					.getRevisionInfo();
			int processedRevisionNumber = processedRevisionInfo
					.getRevisionNumber();
			List<Release> changedOrAddedReleases = releaseSpecificChangesInRevision
					.getAddedOrChangedEntities();
			List<Long> deletedArtifacts = releaseSpecificChangesInRevision
					.getDeletedIds();

			// insert all already deleted artifacts in the processed artifacts
			// list
			alreadyProcessedArtifacts.addAll(deletedArtifacts);

			// check whether this revision have been caused by the connector
			// user itself
			if (processedRevisionInfo.getUserName().equals(connectorUser)) {
				// insert all changed and created artifacts in the processed
				// artifacts list
				for (Release release : changedOrAddedReleases) {
					alreadyProcessedArtifacts.add(release.getId());
				}
			} else if (!changedOrAddedReleases.isEmpty()) {
				// now we have to sort the changed/created artifacts
				Collections.sort(changedOrAddedReleases,
						new Comparator<Release>() {

							@Override
							public int compare(Release arg0, Release arg1) {
								return arg0.getId().compareTo(arg1.getId());
							}
						});
			}

			int numberOfChangedOrAddedArtifacts = changedOrAddedReleases.size();

			ListIterator<Release> it2 = changedOrAddedReleases
					.listIterator(numberOfChangedOrAddedArtifacts);
			int processedMinorVersion = numberOfChangedOrAddedArtifacts * 2;
			boolean lastItemInTransaction = true;
			while (it2.hasPrevious()) {
				Release artifact = it2.previous();
				/*
				 * Check whether we already transported this artifact or if the
				 * current revision number is greater than the major version the
				 * change has to transported. If this condition is not true, the
				 * major revision is equal to the current revision number (since
				 * earlier versions are not returned by the API) and we have to
				 * use the minor version to determine what has been already
				 * shipped
				 */
				if (majorVersion < processedRevisionNumber
						|| (majorVersion == processedRevisionNumber && minorVersion < processedMinorVersion)) {
					// check whether artifact has been already processed
					if (!alreadyProcessedArtifacts.contains(artifact.getId())) {
						// now prepend item to the list
						ArtifactState artifactState = new ArtifactState();
						artifactState
								.setArtifactId(artifact.getId().toString());
						XMLGregorianCalendar xmlTimestamp = processedRevisionInfo
								.getTimeStamp();
						Date artifactLastModifiedDate = new Date(0);
						if (xmlTimestamp != null) {
							artifactLastModifiedDate = xmlTimestamp
									.toGregorianCalendar().getTime();
						}
						artifactState
								.setArtifactLastModifiedDate(artifactLastModifiedDate);
						long artificialRevisionNumber = processedRevisionNumber
								* SWP_REVISION_FACTOR + processedMinorVersion
								+ (lastItemInTransaction ? 1 : 0);
						artifactState
								.setArtifactVersion(artificialRevisionNumber);
						artifactStates.add(0, artifactState);
						// now update the cache
						Map<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Release>> productSpecificCache = releaseCache
								.get(swpProductName);
						if (productSpecificCache == null) {
							productSpecificCache = new HashMap<Long, AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Release>>();
							releaseCache.put(swpProductName,
									productSpecificCache);
						}
						productSpecificCache
								.put(
										artifact.getId(),
										new AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Release>(
												new AbstractMap.SimpleEntry<Long, RevisionInfo>(
														artificialRevisionNumber,
														processedRevisionInfo),
												artifact));
						lastItemInTransaction = false;
					}
				}
				processedMinorVersion -= 2;
			}
		}
	}

	/**
	 * Retrieves the properties of an SWP product and stores them into the
	 * passed generic artifact
	 * 
	 * @param id
	 * @param swpProductName
	 * @param genericArtifact
	 * @throws RemoteException
	 * @throws ScrumWorksException
	 */
	public void retrieveProduct(ScrumWorksAPIService endpoint,
			String id, String swpProductName,
			GenericArtifact genericArtifact) throws RemoteException,
			ScrumWorksException {
		AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Product> cachedProduct = productCache
		.get(swpProductName).get(Long.valueOf(id));
		if (cachedProduct == null) {
			throw new CCFRuntimeException("Could not retrieve product " + id
					+ "from the cache.");
		}
		long artificialVersionNumber = cachedProduct.getKey().getKey();
		RevisionInfo productRevision = cachedProduct.getKey().getValue();
		Product product = cachedProduct.getValue();
		
		
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
		
		genericArtifact.setSourceArtifactVersion(Long.toString(artificialVersionNumber));
		Date artifactLastModifiedDate = new Date(0);
		if (productRevision.getTimeStamp() != null) {
			artifactLastModifiedDate = productRevision.getTimeStamp()
					.toGregorianCalendar().getTime();
		}
		genericArtifact.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df
				.format(artifactLastModifiedDate));
	}

	/**
	 * Populates the generic artifact data structure with the properties of the
	 * requested release
	 * 
	 * @param id
	 *            id of the PBI
	 * @param product
	 *            SWP product name
	 * @throws RemoteException
	 * @throws ScrumWorksException
	 */
	public void retrieveRelease(ScrumWorksAPIService endpoint, String id,
			String swpProductName, GenericArtifact ga) throws NumberFormatException,
			RemoteException, ScrumWorksException {
		
		AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Long, RevisionInfo>, Release> cachedRelease = releaseCache
		.get(swpProductName).get(Long.valueOf(id));
		if (cachedRelease == null) {
			throw new CCFRuntimeException("Could not retrieve release " + id
					+ "from the cache.");
		}
		long artificialVersionNumber = cachedRelease.getKey().getKey();
		RevisionInfo releaseRevision = cachedRelease.getKey().getValue();
		Release release = cachedRelease.getValue();

		addProductReleaseField(ga, ReleaseFields.id, release.getId());
		addProductReleaseField(ga, ReleaseFields.archived, release
				.isArchived());
		addProductReleaseField(ga, ReleaseFields.description, release
				.getDescription());
		addProductReleaseField(ga, ReleaseFields.productId, release
				.getProductId());
		addProductReleaseField(ga, ReleaseFields.programId, release
				.getProgramId());
		addProductReleaseField(ga, ReleaseFields.releaseDate, release
				.getEndDate());
		addProductReleaseField(ga, ReleaseFields.startDate, release
				.getStartDate());
		addProductReleaseField(ga, ReleaseFields.title, release.getName());

		// set parent artifact (Product)
		Product product = endpoint.getProductByName(swpProductName);
		ga.setDepParentSourceArtifactId(product.getId().toString());
		ga.setDepParentSourceRepositoryId(product
				+ SWPMetaData.REPOSITORY_ID_SEPARATOR + SWPMetaData.PRODUCT);
		
		ga.setSourceArtifactVersion(Long.toString(artificialVersionNumber));
		Date artifactLastModifiedDate = new Date(0);
		if (releaseRevision.getTimeStamp() != null) {
			artifactLastModifiedDate = releaseRevision.getTimeStamp()
					.toGregorianCalendar().getTime();
		}
		ga.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df
				.format(artifactLastModifiedDate));
	}

}
