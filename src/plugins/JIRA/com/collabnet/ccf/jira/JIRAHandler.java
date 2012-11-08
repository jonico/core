package com.collabnet.ccf.jira;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.GetCreateIssueMetadataOptionsBuilder;
import com.atlassian.jira.rest.client.IssueRestClient;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.CimFieldInfo;
import com.atlassian.jira.rest.client.domain.CimIssueType;
import com.atlassian.jira.rest.client.domain.CimProject;
import com.atlassian.jira.rest.client.domain.Comment;
import com.atlassian.jira.rest.client.domain.EntityHelper;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.input.IssueInputBuilder;
import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.utils.DateUtil;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
import com.microsoft.tfs.core.clients.workitem.fields.Field;
import com.microsoft.tfs.core.clients.workitem.internal.link.RelatedLinkImpl;
import com.microsoft.tfs.core.clients.workitem.link.Link;
import com.microsoft.tfs.core.clients.workitem.query.WorkItemCollection;
import com.microsoft.tfs.core.clients.workitem.revision.Revision;
import com.microsoft.tfs.core.clients.workitem.revision.RevisionCollection;

public class JIRAHandler {

	private static final Log log = LogFactory.getLog(JIRAHandler.class);
	private static final Log logConflictResolutor = LogFactory
			.getLog("com.collabnet.ccf.core.conflict.resolution");
	
	public String get_all_wi_query = "Select [Id] From WorkItems Where [Team Project] = '?3' and [Work Item Type] = '?1' and [Changed Date] >= '?2' Order By [Changed Date] Asc";
	static final NullProgressMonitor pm = new NullProgressMonitor();
	
	public void getChangedWorkItems(JIRAConnection connection, String projectName, String workItemType,
			Date lastModifiedDate, String lastSynchronizedVersion,
			String lastSynchedArtifactId,
			ArrayList<ArtifactState> artifactStates, String userName,
			boolean ignoreConnectorUserUpdates) {

			WorkItemClient workItemClient = connection.getTpc().getWorkItemClient();

			String finalQuery = get_all_wi_query
					.replace("?1", workItemType).replace("?2",
							JIRAMetaData.formatDate(lastModifiedDate)).replace("?3", projectName);

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

	public void getWorkItem(JIRAConnection connection, String collectionName,
			String projectName, String workItemType, Date lastModifiedDate,
			String lastSynchronizedVersion, String lastSynchedArtifactId,
			String artifactId, String userName,
			boolean ignoreConnectorUserUpdates,
			GenericArtifact genericArtifact, String sourceRepositoryId) {

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

		if (lastModifiedBy.equalsIgnoreCase(userName)
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
			if (field.getReferenceName()
					.equals(CoreFieldReferenceNames.HISTORY)) {
				continue;
			}
			
			// date fix: If date time does not contain hours, minutes and
			// seconds, transform into GMT (without hours/minutes/seconds) and
			// use DATE instead of DATETIME.
			if (field.getReferenceName().equals(CoreFieldReferenceNames.CHANGED_DATE)
					|| field.getReferenceName().equals(CoreFieldReferenceNames.CHANGED_DATE)){

				Date convertedDate = null;
				Date dateValue = (Date)field.getOriginalValue();
				
				if (DateUtil.isAbsoluteDateInTimezone(dateValue, "GMT")) {
				convertedDate = DateUtil.convertGMTToTimezoneAbsoluteDate(
				      dateValue, genericArtifact.getTargetSystemTimezone());
				} else {
					convertedDate = dateValue;
				}
				
				// updating the field
				GenericArtifactField gaField = genericArtifact.addNewField(
						field.getReferenceName(), "mandatoryField");
				gaField.setFieldValueType(JIRAMetaData
						.translateTFSFieldValueTypeToCCFFieldValueType(field
								.getFieldDefinition().getFieldType()));
				gaField.setFieldAction(FieldActionValue.REPLACE);
				gaField.setFieldValue(convertedDate);
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
				if (changedBy.equalsIgnoreCase(userName)
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

		// looking for a parent-child relationship
		if (workItem.getLinks().size() > 0) {

			workItem.getLinks().iterator().next();
			Iterator<Link> linkIterator = workItem.getLinks().iterator();

			while (linkIterator.hasNext()) {

				Link link = linkIterator.next();

				// it looks for a Related relationship
				if (link.getLinkID() == -1) {

					RelatedLinkImpl relatedLink = (RelatedLinkImpl) link;

					// it looks for a Parent relationship
					if (relatedLink.getWorkItemLinkTypeID() == -2) {

						WorkItem fatherWorkItem = connection
								.getTpc()
								.getWorkItemClient()
								.getWorkItemByID(
										relatedLink.getTargetWorkItemID());
						String parentSourceRepositoryId = sourceRepositoryId
								.substring(0,
										sourceRepositoryId.lastIndexOf("-"))
								+ "-" + fatherWorkItem.getType().getName();

						genericArtifact
								.setDepParentSourceRepositoryId(parentSourceRepositoryId);
						genericArtifact.setDepParentSourceArtifactId(String
								.valueOf(fatherWorkItem.getID()));
					}
				}
			}
		}

	}

	private void addComment(GenericArtifact genericArtifact, String history,
			Date changedDate, String changedBy) {
		GenericArtifactField gaField = genericArtifact.addNewField(
				CoreFieldReferenceNames.HISTORY, "mandatoryField");
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
		gaField.setFieldValueType(JIRAMetaData
				.translateTFSFieldValueTypeToCCFFieldValueType(field
						.getFieldDefinition().getFieldType()));
		gaField.setFieldAction(FieldActionValue.REPLACE);
		gaField.setFieldValue(field.getOriginalValue());
	}

	public BasicIssue createIssue(GenericArtifact ga, String projectKey,
			String issueTypeString , JIRAConnection connection) {
		
		//FIXME implement retry logic for mid-air conflicts
		//FIXME Adding comments
		//FIXME Setting all fields
		//FIXME Creating Parent-Child dependencies
		//FIXME to find out correct version number
		//FIXME html to plain text conversion
		//FIXME get issue type IDs instead of name
		
		DateTime createdDate=null;
		 
		JiraRestClient restClient=connection.getJiraRestClient();
		IssueRestClient issueClient = restClient.getIssueClient();
		
		final IssueInputBuilder issueInputBuilder = getIssueInputBuilder(ga,
				projectKey, issueTypeString, issueClient);
		final BasicIssue basicCreatedIssue = issueClient.createIssue(issueInputBuilder.build(), pm);
		Issue issue = issueClient.getIssue(basicCreatedIssue.getKey(), pm);
		
		Iterable<com.atlassian.jira.rest.client.domain.Field> fields= issue.getFields();
		issue = addIssueComment(ga, issueClient, basicCreatedIssue.getKey(), issue);
		
		
		if (issue.getComments()!=null) {
			createdDate=getUpdatedDate(connection, issue);
		}
		
		//TODO Code will be used on Updateissue()
		/*if (issue.getChangelog() != null) {
			Iterator<ChangelogGroup> it = issue.getChangelog().iterator();
			while (it.hasNext()) {
				// FIXME Find out the change log items order
				ChangelogGroup changeLogGroup = it.next();
				BasicUser createdUser = changeLogGroup.getAuthor();
				if (connection.getUsername().equalsIgnoreCase(
						createdUser.getName())) {
					createdDate = changeLogGroup.getCreated();
					break;
				}
			}
		}*/
		
		 createdDate=issue.getCreationDate();
		 if (createdDate==null) {
			 throw new CCFRuntimeException("Could not determine creation date for newly created issue "+ issue.getKey());
		 }
		ga.setTargetArtifactVersion(String.valueOf(createdDate.getMillis()));
		
		
	try{	
		ga.setTargetArtifactLastModifiedDate(GenericArtifactHelper.df
				.format(createdDate.toDate()));
		
	} catch (NullPointerException e){
		
		String message = "Null pointer setting the new workItem changedDate";
		throw new CCFRuntimeException(message);
	}
		
		ga.setTargetArtifactId(String.valueOf(basicCreatedIssue.getId()));

		return basicCreatedIssue;
	}

	/**
	 * @param ga
	 * @param projectKey
	 * @param issueTypeString
	 * @param issueClient
	 * @return
	 */
	private IssueInputBuilder getIssueInputBuilder(GenericArtifact ga,
			String projectKey, String issueTypeString,
			IssueRestClient issueClient) {
		final Iterable<CimProject> metadataProjects = issueClient.getCreateIssueMetadata(
				new GetCreateIssueMetadataOptionsBuilder().withProjectKeys(projectKey).withIssueTypeNames(issueTypeString).withExpandedIssueTypesFields().build(), pm);
		final CimProject project = metadataProjects.iterator().next();
		final CimIssueType createIssueType = EntityHelper.findEntityByName(project.getIssueTypes(), issueTypeString);
		
		final IssueInputBuilder issueInputBuilder = new IssueInputBuilder(projectKey, createIssueType.getId());
		Map<String, CimFieldInfo> fieldMap=createIssueType.getFields();
		 Iterator<Entry<String, CimFieldInfo>> fieldsit = fieldMap.entrySet().iterator();
		while(fieldsit.hasNext()){
			 Entry<String, CimFieldInfo> field = fieldsit.next();
			String fieldId=field.getValue().getId();
			List<GenericArtifactField> gaFields = ga
					.getAllGenericArtifactFieldsWithSameFieldName(fieldId);
			if (gaFields != null && gaFields.get(0).getFieldValueHasChanged()) {
				issueInputBuilder.setFieldValue(fieldId, gaFields.get(0).getFieldValue());
			}
		}
		return issueInputBuilder;
	}

	/**
	 * @param connection
	 * @param issue
	 */
	private DateTime getUpdatedDate(JIRAConnection connection, Issue issue) {
		DateTime updatedDate = null;
		Iterator<Comment> it = issue.getComments().iterator();
		while (it.hasNext()) {
			Comment comment = it.next();
			if (connection.getUsername().equalsIgnoreCase(
					comment.getAuthor().getName())) {
				 updatedDate = comment.getCreationDate();
				continue;
			}
		}
		return updatedDate;
	}

	/**
	 * @param ga
	 * @param issueClient
	 * @param basicCreatedIssue
	 * @param issue
	 * @return
	 */
	private Issue addIssueComment(GenericArtifact ga,
			IssueRestClient issueClient, String key,
			Issue issue) {
		List<GenericArtifactField> comments = ga
				.getAllGenericArtifactFieldsWithSameFieldName("comments");
		if (comments != null) {
			for (ListIterator<GenericArtifactField> iterator = comments
					.listIterator(comments.size()); iterator.hasPrevious();) {

				GenericArtifactField comment = iterator.previous();

				String commentValue = (String) comment.getFieldValue();
				if (StringUtils.isEmpty(commentValue)) {
					continue;
				}
				issueClient.addComment(pm, issue.getCommentsUri(),Comment.valueOf(commentValue));
			}
			issue = issueClient.getIssue(key, pm);
		}
		return issue;
	}

	public Issue updateIssue(GenericArtifact ga, 
			String projectKey, String issueTypeString,
			JIRAConnection connection) {
		
		JiraRestClient restClient=connection.getJiraRestClient();
		IssueRestClient issueClient = restClient.getIssueClient();
		DateTime updatedDate =null;
		String issueId = ga.getTargetArtifactId();
		Issue issue = issueClient.getIssue(issueId, pm);
		Long issueRevision = issue.getUpdateDate().getMillis();

		if (!AbstractWriter.handleConflicts(issueRevision, ga)) {
			return null;
		}
		
		final IssueInputBuilder issueInputBuilder = getIssueInputBuilder(ga,
				projectKey, issueTypeString, issueClient);
		issueClient.updateIssue(issue.getKey(), issueInputBuilder.build(), null);
		issue = issueClient.getIssue(issueId, pm);
		
		issue = addIssueComment(ga, issueClient, issue.getKey(), issue);
		updatedDate = issue.getUpdateDate();
		//FIXME calculate update date based on MAX(latest ccf entry from change log, latest ccf entry from comments,latest know update date)
		
		ga.setTargetArtifactVersion(String.valueOf(updatedDate.getMillis()));
		
		try {
			ga.setTargetArtifactLastModifiedDate(GenericArtifactHelper.df
					.format(updatedDate.toDate()));
		} catch (NullPointerException e){
			
			String message = "Null pointer getting the workItem changedDate in workItem (" + issueId +")";
			throw new CCFRuntimeException(message);
		}

		return issue;
	
	
	}

}
