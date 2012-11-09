package com.collabnet.ccf.jira;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

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
import com.atlassian.jira.rest.client.domain.Field;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.domain.input.IssueInputBuilder;
import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;

public class JIRAHandler {

	private static final Log log = LogFactory.getLog(JIRAHandler.class);
	private static final Log logConflictResolutor = LogFactory
			.getLog("com.collabnet.ccf.core.conflict.resolution");
	
	public String get_all_issue_query = "project = ?1 AND TYPE=?2 AND UPDATED >= '?3' order by UPDATED asc";
	static final NullProgressMonitor pm = new NullProgressMonitor();
	
	public void getChangedWorkItems(JIRAConnection connection, String projectKey, String issueType,
			Date lastModifiedDate, String lastSynchronizedVersion,
			String lastSynchedArtifactId,
			ArrayList<ArtifactState> artifactStates, String userName,
			boolean ignoreConnectorUserUpdates, String jiraTimezone) {

		 	
		    DateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
		    df.setTimeZone(TimeZone.getTimeZone(jiraTimezone));
		    
		 
			String finalQuery = get_all_issue_query
					.replace("?1", projectKey).replace("?2",issueType).replace("?3", df.format(lastModifiedDate));

			SearchResult<BasicIssue> result= connection.getJiraRestClient().getSearchClient().searchJql(finalQuery, pm);
	    	Iterable<BasicIssue>  basicIssueList=result.getIssues();
	    	
			
			ArrayList<Issue> detailRowsFull = new ArrayList<Issue>();
			ArrayList<Issue> detailRowsNew = new ArrayList<Issue>();
			boolean duplicateFound = false;

			for(BasicIssue basicIssue : basicIssueList){
				Issue workItem = connection.getJiraRestClient().getIssueClient().getIssue(basicIssue.getKey(), pm);
				
				

				// check last date sync

				Date workItemTimeStamp = workItem.getUpdateDate().toDate();

				Date artifactLastModifiedDate = new Date(0);

				if (workItemTimeStamp != null) {
					artifactLastModifiedDate = workItemTimeStamp;
				}

				if (artifactLastModifiedDate.compareTo(lastModifiedDate) >= 0) {

					String workItemRevisionNumber = String.valueOf(workItem.getUpdateDate().getMillis());
					String id = String.valueOf(workItem.getId());
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

			List<Issue> issues = duplicateFound ? detailRowsNew
					: detailRowsFull;
			for (Issue issue : issues) {
				ArtifactState artifactState = new ArtifactState();
				artifactState.setArtifactId(issue.getId());

				artifactState.setArtifactLastModifiedDate(issue.getUpdateDate().toDate());

				artifactState.setArtifactVersion(issue.getUpdateDate().getMillis());
				artifactStates.add(artifactState);
			}
	}

	public void getIssue(JIRAConnection connection,
			String projectKey, String issueType, Date lastModifiedDate,
			String lastSynchronizedVersion, String lastSynchedArtifactId,
			String artifactId, String userName,
			boolean ignoreConnectorUserUpdates,
			GenericArtifact genericArtifact, String sourceRepositoryId) {

		Issue issue = connection.getJiraRestClient().getIssueClient().getIssue(artifactId, pm);

		String lastModifiedBy = "ccfteam";
		Date creationDate = issue.getCreationDate().toDate();
		Date lasWorkItemtModifiedDate = issue.getUpdateDate().toDate();
		String revisionNumber = String.valueOf(issue.getUpdateDate().getMillis());

		boolean isResync = false;
		boolean isIgnore = false;

		/*if (lastModifiedBy.equalsIgnoreCase(userName)
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
		}*/
		
		Iterator<com.atlassian.jira.rest.client.domain.Field> it = issue.getFields().iterator();
		
		while (it.hasNext()) {

			com.atlassian.jira.rest.client.domain.Field field = it.next();
			addIssueField(genericArtifact, field);
		}
		GenericArtifactField summaryField = genericArtifact.addNewField(
				"description", "mandatoryField");
		summaryField.setFieldValueType(FieldValueTypeValue.STRING);
		summaryField.setFieldAction(FieldActionValue.REPLACE);
		summaryField.setFieldValue(issue.getDescription());
		
		GenericArtifactField descriptionField = genericArtifact.addNewField(
				"summary", "mandatoryField");
		descriptionField.setFieldValueType(FieldValueTypeValue.STRING);
		descriptionField.setFieldAction(FieldActionValue.REPLACE);
		descriptionField.setFieldValue(issue.getSummary());
		
		GenericArtifactField statusField = genericArtifact.addNewField(
				"status", "mandatoryField");
		statusField.setFieldValueType(FieldValueTypeValue.STRING);
		statusField.setFieldAction(FieldActionValue.REPLACE);
		statusField.setFieldValue(issue.getStatus().getName());

		final Iterable<CimProject> metadataProjects = connection.getJiraRestClient().getIssueClient().getCreateIssueMetadata(
				new GetCreateIssueMetadataOptionsBuilder().withProjectKeys(projectKey).withIssueTypeNames(issueType).withExpandedIssueTypesFields().build(), pm);
		final CimProject project = metadataProjects.iterator().next();
		final CimIssueType createIssueType = EntityHelper.findEntityByName(project.getIssueTypes(), issueType);
		
		Map<String, CimFieldInfo> fieldMap=createIssueType.getFields();
		 Iterator<Entry<String, CimFieldInfo>> fieldsit = fieldMap.entrySet().iterator();
		while(fieldsit.hasNext()){
			 CimFieldInfo field = fieldsit.next().getValue();
			/*if(field.getId().equalsIgnoreCase("description")){
				GenericArtifactField gaField = genericArtifact.addNewField(
						field.getId(), "mandatoryField");
				gaField.setFieldValueType(FieldValueTypeValue.STRING);
				gaField.setFieldAction(FieldActionValue.REPLACE);
				gaField.setFieldValue(issue.getf.getDescription());
				
			}
			if(field.getId().equalsIgnoreCase("summary")){
				GenericArtifactField gaField = genericArtifact.addNewField(
						field.getId(), "mandatoryField");
				gaField.setFieldValueType(FieldValueTypeValue.STRING);
				gaField.setFieldAction(FieldActionValue.REPLACE);
				gaField.setFieldValue(issue.getSummary());
				
			}
			if(field.getId().equalsIgnoreCase("status")){
				GenericArtifactField gaField = genericArtifact.addNewField(
						field.getId(), "mandatoryField");
				gaField.setFieldValueType(FieldValueTypeValue.STRING);
				gaField.setFieldAction(FieldActionValue.REPLACE);
				gaField.setFieldValue(issue.getStatus().getName());
				
			}*/
			//addIssueField(genericArtifact, field, issue);
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
			Iterator<Comment> commIt = issue.getComments().iterator();
		
			while (commIt.hasNext()) {

				Comment comment = commIt.next();
				
				String changedBy = comment.getAuthor().getName();
				if (changedBy.equalsIgnoreCase(userName)
						&& ignoreConnectorUserUpdates) {
					continue;
				}

				Date changedDate = comment.getUpdateDate().toDate();
				if (lastModifiedDate.compareTo(changedDate) >= 0) {
					continue;
				}

				String history = comment.getBody();
				if (StringUtils.isEmpty(history)) {
					continue;
				}
				addComment(genericArtifact,comment);
				
			}
		}

		genericArtifact
				.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df
						.format(lasWorkItemtModifiedDate));
		genericArtifact.setSourceArtifactVersion(revisionNumber);

		// looking for a parent-child relationship
		/*if (workItem.getLinks().size() > 0) {

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
		}*/

	}

	private void addIssueField(GenericArtifact genericArtifact,
			CimFieldInfo field, Issue issue) {
		Field actualField = issue.getField(field.getId());
		GenericArtifactField gaField = genericArtifact.addNewField(
				field.getId(), "mandatoryField");
		// FIXME Type conversion
		gaField.setFieldValueType(FieldValueTypeValue.STRING);
		gaField.setFieldAction(FieldActionValue.REPLACE);
		
		if (actualField != null) {
			Object fieldValue = actualField.getValue();
			if (fieldValue != null) {
				gaField.setFieldValue(fieldValue.toString());
				return;
			}
		}
		gaField.setFieldValue(null);
	}

	private void addComment(GenericArtifact genericArtifact, Comment comment) {
		GenericArtifactField gaField = genericArtifact.addNewField(
				"comment", "mandatoryField");
		gaField.setFieldValueType(FieldValueTypeValue.STRING);
		gaField.setFieldAction(FieldActionValue.APPEND);
		gaField.setFieldValue(comment.getBody());
	}

	private void addIssueField(GenericArtifact genericArtifact, Field field) {
		
		// in the moment we find out that TFS supports more than one field with
		// the same name, we have to use the field type to differentiate them
		GenericArtifactField gaField = genericArtifact.addNewField(
				field.getId(), "mandatoryField");
		gaField.setFieldValueType(FieldValueTypeValue.STRING);
		gaField.setFieldAction(FieldActionValue.REPLACE);
		gaField.setFieldValue(field.getValue());
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
