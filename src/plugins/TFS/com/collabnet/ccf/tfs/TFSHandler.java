package com.collabnet.ccf.tfs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.clients.workitem.fields.Field;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.project.ProjectCollection;
import com.microsoft.tfs.core.clients.workitem.query.WorkItemCollection;

public class TFSHandler {
	
	private static final Log log = LogFactory.getLog(TFSHandler.class);

	public String all_wi_query = "Select [Id] From WorkItems Where [Work Item Type] = '?' Order By [Changed Date] Asc";

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
				WorkItemCollection tasksQueryResults = workItemClient
						.query(all_wi_query.replace("?", workItemType));

				ArrayList<WorkItem> detailRowsFull = new ArrayList<WorkItem>();
				ArrayList<WorkItem> detailRowsNew = new ArrayList<WorkItem>();
				boolean duplicateFound = false;

				for (int i = 0; i < tasksQueryResults.size(); i++) {

					WorkItem workItem = tasksQueryResults.getWorkItem(i);

					// check last date sync

					Date workItemTimeStamp = (Date) workItem.getFields()
							.getField(CoreFieldReferenceNames.CHANGED_DATE)
							.getValue();

					Date artifactLastModifiedDate = new Date(0);

					if (workItemTimeStamp != null) {
						artifactLastModifiedDate = workItemTimeStamp;
					}

					// FIXME should be part of the query
					if (artifactLastModifiedDate
							.compareTo(lastModifiedDate) >= 0) {

						String workItemRevisionNumber = workItem
								.getFields()
								.getField(CoreFieldReferenceNames.REVISION)
								.getValue().toString();
						String id = String.valueOf(workItem.getID());
						if (id.equals(lastSynchedArtifactId) && workItemRevisionNumber.equals(lastSynchronizedVersion)) {
							duplicateFound = true;
						} else {
							if (duplicateFound) {
								detailRowsNew.add(workItem);
							}
							detailRowsFull.add(workItem);
						}
					}
				}
				
				List<WorkItem> workItems = duplicateFound?detailRowsNew:detailRowsFull;
				for (WorkItem wit : workItems) {
					ArtifactState artifactState = new ArtifactState();
					artifactState
					.setArtifactId(String.valueOf(wit.getID()));

					artifactState.setArtifactLastModifiedDate((Date) wit.getFields()
							.getField(CoreFieldReferenceNames.CHANGED_DATE)
							.getValue());

					artifactState.setArtifactVersion(Long.parseLong(wit
							.getFields()
							.getField(CoreFieldReferenceNames.REVISION)
							.getValue().toString()));
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
		
		
		
		WorkItem workItem = connection.getTpc().getWorkItemClient().getWorkItemByID(Integer.parseInt(artifactId));
		
		
		String lastModifiedBy = workItem.getFields().getField(CoreFieldReferenceNames.CHANGED_BY).getValue().toString();
		Date creationDate = (Date) workItem.getFields().getField(CoreFieldReferenceNames.CREATED_DATE).getValue();
		Date lasWorkItemtModifiedDate = (Date) workItem.getFields().getField(CoreFieldReferenceNames.CHANGED_DATE).getValue();
		String revisionNumber = workItem.getFields().getField(CoreFieldReferenceNames.REVISION).getValue().toString();
		
		boolean isResync = false;
		boolean isIgnore = false;
		
		if (lastModifiedBy.equalsIgnoreCase(TFSMetaData.extractUserNameFromFullUserName(userName)) && ignoreConnectorUserUpdates) {
			if (creationDate.after(lastModifiedDate)) {
				log.info(String.format(
						"resync is necessary, despite the artifact %s last being updated by the connector user",
						artifactId));
				isResync = true;
			} else {
				log.info(String.format(
						"artifact %s is an ordinary connector update, ignore it.",
						artifactId));
				isIgnore = true;
			}
		}
		
		Iterator<Field> it = workItem.getFields().iterator();
		
		while (it.hasNext()){
			
			Field field = it.next();
			addWorkItemField(genericArtifact, field);
		}
	
		
		if (isIgnore) {
			genericArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.IGNORE);
		} else if (isResync) {
			genericArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.RESYNC);
		}
		
		genericArtifact
		.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df
				.format(lasWorkItemtModifiedDate));
		genericArtifact.setSourceArtifactVersion(revisionNumber);
		
	}
	
	private void addWorkItemField(GenericArtifact genericArtifact,
			Field field) {
		
		// in the moment we find out that TFS supports more than one field with the same name, we have to use the field type to differentiate them
		GenericArtifactField gaField = genericArtifact.addNewField(field
				.getName(), "mandatoryField");
		gaField.setFieldValueType(TFSMetaData.translateTFSFieldValueTypeToCCFFieldValueType(field.getFieldDefinition().getFieldType()));
		gaField.setFieldAction(FieldActionValue.REPLACE);
		gaField.setFieldValue(field.getValue());
	}

}
