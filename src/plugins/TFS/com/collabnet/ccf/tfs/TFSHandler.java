package com.collabnet.ccf.tfs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.clients.workitem.fields.Field;
import com.microsoft.tfs.core.clients.workitem.fields.FieldDefinition;
import com.microsoft.tfs.core.clients.workitem.fields.FieldType;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.project.ProjectCollection;
import com.microsoft.tfs.core.clients.workitem.query.WorkItemCollection;
import com.microsoft.tfs.core.clients.workitem.revision.Revision;
import com.microsoft.tfs.core.clients.workitem.revision.RevisionCollection;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;
import com.microsoft.tfs.core.exceptions.TECoreException;

public class TFSHandler {

	private static final Log log = LogFactory.getLog(TFSHandler.class);

	public String all_wi_query = "Select [Id] From WorkItems Where [Work Item Type] = '?1' and [Changed Date] >= '?2' Order By [Changed Date] Asc";

	public void getChangedWorkItems(TFSConnection connection,
			String collectionName, String projectName, String workItemType,
			Date lastModifiedDate, String lastSynchronizedVersion,
			String lastSynchedArtifactId,
			ArrayList<ArtifactState> artifactStates, String userName,
			boolean ignoreConnectorUserUpdates) {
		// FIXME try to get only the ONE project (performance issue)
		ProjectCollection projectCollection = connection.getTpc()
				.getWorkItemClient().getProjects();

		for (Project project : projectCollection) {

			if (project.getName().equals(projectName)) {

				WorkItemClient workItemClient = project.getWorkItemClient();

				String finalQuery = all_wi_query
						.replace("?1", workItemType)
						.replace("?2", TFSMetaData.formatDate(lastModifiedDate));

				WorkItemCollection tasksQueryResults = workItemClient.query(
						finalQuery, null, false);

				ArrayList<WorkItem> detailRowsFull = new ArrayList<WorkItem>();
				ArrayList<WorkItem> detailRowsNew = new ArrayList<WorkItem>();
				boolean duplicateFound = false;

				for (int i = 0; i < tasksQueryResults.size(); i++) {

					WorkItem workItem = tasksQueryResults.getWorkItem(i);

					// check last date sync

					Date workItemTimeStamp = (Date) workItem.getFields()
							.getField(CoreFieldReferenceNames.CHANGED_DATE)
							.getOriginalValue();

					Date artifactLastModifiedDate = new Date(0);

					if (workItemTimeStamp != null) {
						artifactLastModifiedDate = workItemTimeStamp;
					}

					if (artifactLastModifiedDate.compareTo(lastModifiedDate) >= 0) {

						String workItemRevisionNumber = workItem.getFields()
								.getField(CoreFieldReferenceNames.REVISION)
								.getOriginalValue().toString();
						String id = String.valueOf(workItem.getID());
						if (id.equals(lastSynchedArtifactId)
								&& workItemRevisionNumber
										.equals(lastSynchronizedVersion)) {
							duplicateFound = true;
						} else {
							if (duplicateFound) {
								detailRowsNew.add(workItem);
							}
							detailRowsFull.add(workItem);
						}
					}
				}

				List<WorkItem> workItems = duplicateFound ? detailRowsNew
						: detailRowsFull;
				for (WorkItem wit : workItems) {
					ArtifactState artifactState = new ArtifactState();
					artifactState.setArtifactId(String.valueOf(wit.getID()));

					artifactState.setArtifactLastModifiedDate((Date) wit
							.getFields()
							.getField(CoreFieldReferenceNames.CHANGED_DATE)
							.getOriginalValue());

					artifactState.setArtifactVersion(Long.parseLong(wit
							.getFields()
							.getField(CoreFieldReferenceNames.REVISION)
							.getOriginalValue().toString()));
					artifactStates.add(artifactState);
				}
			}

		}

	}

	public void getWorkItem(TFSConnection connection, String collectionName,
			String projectName, String workItemType, Date lastModifiedDate,
			String lastSynchronizedVersion, String lastSynchedArtifactId,
			String artifactId, String userName,
			boolean ignoreConnectorUserUpdates, GenericArtifact genericArtifact) {

		WorkItem workItem = connection.getTpc().getWorkItemClient()
				.getWorkItemByID(Integer.parseInt(artifactId));

		String lastModifiedBy = workItem.getFields()
				.getField(CoreFieldReferenceNames.CHANGED_BY)
				.getOriginalValue().toString();
		Date creationDate = (Date) workItem.getFields()
				.getField(CoreFieldReferenceNames.CREATED_DATE)
				.getOriginalValue();
		Date lasWorkItemtModifiedDate = (Date) workItem.getFields()
				.getField(CoreFieldReferenceNames.CHANGED_DATE)
				.getOriginalValue();
		String revisionNumber = workItem.getFields()
				.getField(CoreFieldReferenceNames.REVISION).getOriginalValue()
				.toString();

		boolean isResync = false;
		boolean isIgnore = false;

		if (lastModifiedBy.equalsIgnoreCase(TFSMetaData
				.extractUserNameFromFullUserName(userName))
				&& ignoreConnectorUserUpdates) {
			if (creationDate.after(lastModifiedDate)) {
				log.info(String
						.format("resync is necessary, despite the artifact %s last being updated by the connector user",
								artifactId));
				isResync = true;
			} else {
				log.info(String
						.format("artifact %s is an ordinary connector update, ignore it.",
								artifactId));
				isIgnore = true;
			}
		}

		Iterator<Field> it = workItem.getFields().iterator();

		while (it.hasNext()) {

			Field field = it.next();
			// FIXME Do time conversion in case of dates
			if (field.getReferenceName()
					.equals(CoreFieldReferenceNames.HISTORY)) {
				continue;
			}
			addWorkItemField(genericArtifact, field);
		}

		if (isIgnore) {
			genericArtifact
					.setArtifactAction(GenericArtifact.ArtifactActionValue.IGNORE);
		} else {
			if (isResync) {
				genericArtifact
						.setArtifactAction(GenericArtifact.ArtifactActionValue.RESYNC);
			}
			// now fetch the comments
			RevisionCollection revs = workItem.getRevisions();
			Iterator<Revision> revIt = revs.iterator();
			while (revIt.hasNext()) {

				Revision rev = revIt.next();

				String changedBy = rev
						.getField(CoreFieldReferenceNames.CHANGED_BY)
						.getValue().toString();
				if (changedBy.equalsIgnoreCase(TFSMetaData
						.extractUserNameFromFullUserName(userName))
						&& ignoreConnectorUserUpdates) {
					continue;
				}

				Date changedDate = (Date) rev.getField(
						CoreFieldReferenceNames.CHANGED_DATE).getValue();
				if (lastModifiedDate.compareTo(changedDate) >= 0) {
					continue;
				}

				String history = (String) rev.getField(
						CoreFieldReferenceNames.HISTORY).getValue();
				if (StringUtils.isEmpty(history)) {
					continue;
				}

				addComment(genericArtifact, history, changedDate, changedBy);

			}
		}

		genericArtifact
				.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df
						.format(lasWorkItemtModifiedDate));
		genericArtifact.setSourceArtifactVersion(revisionNumber);

	}

	private void addComment(GenericArtifact genericArtifact, String history,
			Date changedDate, String changedBy) {
		GenericArtifactField gaField = genericArtifact.addNewField(
				CoreFieldReferenceNames.HISTORY, "mandatoryField");
		// FIXME Use Date instead of DateTime in case of dates
		gaField.setFieldValueType(FieldValueTypeValue.HTMLSTRING);
		gaField.setFieldAction(FieldActionValue.APPEND);
		gaField.setFieldValue("User \"" + changedBy + "\" commented on "
				+ changedDate + ":<br> " + history);
	}

	private void addWorkItemField(GenericArtifact genericArtifact, Field field) {

		// in the moment we find out that TFS supports more than one field with
		// the same name, we have to use the field type to differentiate them
		GenericArtifactField gaField = genericArtifact.addNewField(
				field.getReferenceName(), "mandatoryField");
		// FIXME Use Date instead of DateTime in case of dates
		gaField.setFieldValueType(TFSMetaData
				.translateTFSFieldValueTypeToCCFFieldValueType(field
						.getFieldDefinition().getFieldType()));
		gaField.setFieldAction(FieldActionValue.REPLACE);
		gaField.setFieldValue(field.getOriginalValue());
	}

	public WorkItem createWorkItem(GenericArtifact ga, String collectionName,
			String projectName, String workItemTypeString,
			TFSConnection connection) {

		Project project = connection.getTpc().getWorkItemClient().getProjects()
				.get(projectName);
		WorkItemType workItemType = project.getWorkItemTypes().get(
				workItemTypeString);

		WorkItem newWorkItem = project.getWorkItemClient().newWorkItem(
				workItemType);

		// newWorkItem.setTitle("Example Work Item");
		// newWorkItem.getFields().getField(CoreFieldReferenceNames.HISTORY).setValue(
		// "<p>Created automatically by a sample</p>");

		Iterator<FieldDefinition> it = workItemType.getFieldDefinitions()
				.iterator();

		Object state = null;
		while (it.hasNext()) {

			FieldDefinition fieldDef = it.next();

			String fieldName = fieldDef.getReferenceName();

			if (fieldName.equals(CoreFieldReferenceNames.HISTORY)) {
				continue;
			}

			List<GenericArtifactField> gaFields = ga
					.getAllGenericArtifactFieldsWithSameFieldName(fieldName);

			if (gaFields != null) {
				Object fieldValue = gaFields.get(0).getFieldValue(); // If there
																		// more
																		// than
																		// 1,
																		// it's
																		// a
																		// multi
																		// select
																		// field
				if (fieldDef.getReferenceName().equals(
						CoreFieldReferenceNames.STATE)) {
					state = fieldValue;
				} else {
					// FIXME Date conversion
					// FIXME HTML conversion (preserve semantically unchanged
					// content)
					// FIXME More complicated data types (like TreePath)
					// FIXME Comments
					newWorkItem.getFields()
							.getField(fieldDef.getReferenceName())
							.setValue(fieldValue);
				}
			}

		}

		newWorkItem.save();
		if (state != null) {
			newWorkItem.getFields().getField(CoreFieldReferenceNames.STATE)
					.setValue(state);
			try {
				newWorkItem.save();
			} catch (TECoreException e) {
				if (!e.getMessage().contains("TF51650")) {
					throw e;
				}
			}
		}

		List<GenericArtifactField> comments = ga
				.getAllGenericArtifactFieldsWithSameFieldName(CoreFieldReferenceNames.HISTORY);
		if (comments != null) {

			for (ListIterator<GenericArtifactField> iterator = comments
					.listIterator(comments.size()); iterator.hasPrevious();) {

				GenericArtifactField comment = iterator.previous();

				String commentValue = (String) comment.getFieldValue();
				if (StringUtils.isEmpty(commentValue)) {
					continue;
				}
				newWorkItem.getFields()
						.getField(CoreFieldReferenceNames.HISTORY)
						.setValue(commentValue);

				try {
					// FIXME Handle midair conflicts
					newWorkItem.save();
				} catch (TECoreException e) {
					if (!e.getMessage().contains("TF51650")) {
						throw e;
					}
				}
			}
		}

		ga.setTargetArtifactVersion(newWorkItem.getFields()
				.getField(CoreFieldReferenceNames.REVISION).getOriginalValue()
				.toString());
		// FIXME: Validate date null value
		ga.setTargetArtifactLastModifiedDate(GenericArtifactHelper.df
				.format(newWorkItem.getFields()
						.getField(CoreFieldReferenceNames.CHANGED_DATE)
						.getOriginalValue()));
		ga.setTargetArtifactId(String.valueOf(newWorkItem.getID()));

		return newWorkItem;
	}

	public WorkItem updateWorkItem(GenericArtifact ga, String collectionName,
			String projectName, String workItemTypeString,
			TFSConnection connection) {

		String workItemId = ga.getTargetArtifactId();

		// FIXME see cast validation
		WorkItem workItem = connection.getTpc().getWorkItemClient()
				.getWorkItemByID(Integer.parseInt(workItemId));

		Long workItemRvision = Long.valueOf(workItem.getFields()
				.getField(CoreFieldReferenceNames.REVISION).getOriginalValue()
				.toString());

		if (!AbstractWriter.handleConflicts(workItemRvision, ga)) {
			return null;
		}

		Project project = connection.getTpc().getWorkItemClient().getProjects()
				.get(projectName);
		WorkItemType workItemType = project.getWorkItemTypes().get(
				workItemTypeString);

		Iterator<FieldDefinition> it = workItemType.getFieldDefinitions()
				.iterator();

		while (it.hasNext()) {

			FieldDefinition fieldDef = it.next();

			String fieldName = fieldDef.getReferenceName();

			if (fieldName.equals(CoreFieldReferenceNames.HISTORY)) {
				continue;
			}

			List<GenericArtifactField> gaFields = ga
					.getAllGenericArtifactFieldsWithSameFieldName(fieldName);

			if (gaFields != null && gaFields.get(0).getFieldValueHasChanged()) {

				boolean shouldBeOverwritten = true;

				// If there more than 1, it's a multi select field
				Object fieldValue = gaFields.get(0).getFieldValue();

				// FIXME Date conversion
				// FIXME More complicated data types (like TreePath)

				if (fieldDef.getFieldType().equals(FieldType.HTML)) {

					String originalValue = com.collabnet.ccf.core.utils.StringUtils
							.convertHTML(workItem.getFields()
									.getField(fieldDef.getReferenceName())
									.getOriginalValue().toString());
					String newValue = com.collabnet.ccf.core.utils.StringUtils
							.convertHTML(fieldValue.toString());
					shouldBeOverwritten = !originalValue.equals(newValue);

				}

				if (shouldBeOverwritten) {
					workItem.getFields().getField(fieldDef.getReferenceName())
							.setValue(fieldValue);
				}

			}

		}

		List<GenericArtifactField> comments = ga
				.getAllGenericArtifactFieldsWithSameFieldName(CoreFieldReferenceNames.HISTORY);

		boolean calledSaveAtLeastOnce = false;
		if (comments != null) {

			for (ListIterator<GenericArtifactField> iterator = comments
					.listIterator(comments.size()); iterator.hasPrevious();) {

				GenericArtifactField comment = iterator.previous();

				String commentValue = (String) comment.getFieldValue();
				if (StringUtils.isEmpty(commentValue)) {
					continue;
				}
				workItem.getFields().getField(CoreFieldReferenceNames.HISTORY)
						.setValue(commentValue);

				try {
					// FIXME Handle midair conflicts
					calledSaveAtLeastOnce = true;
					workItem.save();
				} catch (TECoreException e) {
					if (!e.getMessage().contains("TF51650")) {
						throw e;
					}
				}
			}
		}

		if (!calledSaveAtLeastOnce) {
			// update history field
			try {
				// FIXME Handle midair conflicts
				workItem.save();
			} catch (TECoreException e) {
				if (!e.getMessage().contains("TF51650")) {
					throw e;
				}
			}
		}
		ga.setTargetArtifactVersion(workItem.getFields()
				.getField(CoreFieldReferenceNames.REVISION).getOriginalValue()
				.toString());
		// FIXME: Validate date null value
		ga.setTargetArtifactLastModifiedDate(GenericArtifactHelper.df
				.format(workItem.getFields()
						.getField(CoreFieldReferenceNames.CHANGED_DATE)
						.getOriginalValue()));

		return workItem;
	}

}
