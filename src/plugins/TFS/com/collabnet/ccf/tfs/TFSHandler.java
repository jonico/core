package com.collabnet.ccf.tfs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.teamforge.api.tracker.ArtifactDetailRow;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.project.ProjectCollection;
import com.microsoft.tfs.core.clients.workitem.query.WorkItemCollection;
import com.microsoft.tfs.util.datetime.CalendarUtils;

public class TFSHandler {

	public String WI_QUERY = "Select [Id] From WorkItems Where [Work Item Type] = '?' Order By [Changed Date] Desc";

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
						.query(WI_QUERY.replace("?", workItemType));

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

						String id = String.valueOf(workItem.getID());
						if (id.equals(lastSynchedArtifactId)) {

							String workItemRevisionNumber = workItem
									.getFields()
									.getField(CoreFieldReferenceNames.REVISION)
									.getValue().toString();

							if (workItemRevisionNumber
									.equals(lastSynchronizedVersion)) {
								duplicateFound = true;
							}
						}

						if (duplicateFound) {
							detailRowsNew.add(workItem);
						}
						detailRowsFull.add(workItem);
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

}
