/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.pi.sfee.v44;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.axis.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.utils.DateUtil;
import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.types.SoapFilter;
import com.vasoftware.sf.soap44.types.SoapSortKey;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.frs.IFrsAppSoap;
import com.vasoftware.sf.soap44.webservices.frs.PackageSoapList;
import com.vasoftware.sf.soap44.webservices.frs.PackageSoapRow;
import com.vasoftware.sf.soap44.webservices.frs.ReleaseSoapDO;
import com.vasoftware.sf.soap44.webservices.frs.ReleaseSoapList;
import com.vasoftware.sf.soap44.webservices.frs.ReleaseSoapRow;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactDependencySoapRow;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactDetailSoapRow;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ITrackerAppSoap;
import com.vasoftware.sf.soap44.webservices.tracker.TrackerSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.TrackerSoapList;
import com.vasoftware.sf.soap44.webservices.tracker.TrackerSoapRow;

/**
 * The tracker handler class provides support for listing and/or edit trackers
 * and artifacts.
 */
public class SFEETrackerHandler {
	/**
	 * Tracker Soap API handle
	 */
	private ITrackerAppSoap mTrackerApp;
	IFrsAppSoap fileReleaseApp = null;

	private static final Log log = LogFactory.getLog(SFEETrackerHandler.class);
	private static final Log logConflictResolutor = LogFactory
			.getLog("com.collabnet.ccf.core.conflict.resolution");

	/**
	 * Class constructor.
	 * 
	 * @param serverUrl -
	 *            The source TF SOAP server URL
	 * @param connectionManager
	 */
	public SFEETrackerHandler(String serverUrl,
			ConnectionManager<Connection> connectionManager) {
		// enable this if you do not like to have the retry code enabled

		if (connectionManager.isUseStandardTimeoutHandlingCode()) {
			mTrackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
					ITrackerAppSoap.class, serverUrl);
		} else {
			mTrackerApp = new TrackerAppSoapTimeoutWrapper(serverUrl,
					connectionManager);
		}
		fileReleaseApp = (IFrsAppSoap) ClientSoapStubFactory.getSoapStub(
				IFrsAppSoap.class, serverUrl);
	}

	/**
	 * Get parent artifact dependencies for a certain artifact
	 * 
	 * @throws RemoteException
	 */
	public ArtifactDependencySoapRow[] getArtifactParentDependencies(
			String sessionId, String artifactId) throws RemoteException {
		ArtifactDependencySoapRow[] result = mTrackerApp
				.getParentDependencyList(sessionId, artifactId).getDataRows();
		return result;
	}

	/**
	 * Get child artifact dependencies for a certain artifact
	 * 
	 * @throws RemoteException
	 */
	public ArtifactDependencySoapRow[] getArtifactChildDependencies(
			String sessionId, String artifactId) throws RemoteException {
		ArtifactDependencySoapRow[] result = mTrackerApp
				.getChildDependencyList(sessionId, artifactId).getDataRows();
		return result;
	}

	/**
	 * Verifies tracker ID
	 * 
	 * @param sessionId
	 *            User session id.
	 * @param projectId
	 *            Project from which to list trackers.
	 * @return true if tracker ID could be verified
	 * @throws RemoteException
	 *             when an error is encountered in listing trackers.
	 * @throws IOException
	 *             when an error is encountered in reading user input.
	 */
	public boolean verifyTracker(String sessionId, String projectId,
			String trackerId) throws RemoteException, IOException {
		TrackerSoapList trackerList = mTrackerApp.getTrackerList(sessionId,
				projectId);
		TrackerSoapRow[] trackerRows = trackerList.getDataRows();

		if (trackerRows.length == 0) {
			throw new RemoteException("No trackers available!");
		}
		for (int i = 0; i < trackerRows.length; i++) {
			TrackerSoapRow trackerRow = trackerRows[i];
			if (trackerId.equals(trackerRow.getId())) {
				log.info("Pulling out artifacts from tracker item "
						+ trackerRow.getTitle());
				return true;
			}
		}
		return false;
	}

	/**
	 * Return all changed tracker items in a List
	 * 
	 * @param sessionID
	 * @param trackerId
	 * @param connectorUser
	 * @return null if only duplicates were found, else a list of changed
	 *         tracker items
	 * @throws RemoteException
	 */
	public List<ArtifactSoapDO> getChangedTrackerItems(String sessionID,
			String trackerId, Date lastModifiedDate, String lastArtifactId,
			int lastArtifactVersion, String connectorUser)
			throws RemoteException {
		log.debug("Getting the changed artifacts from " + lastModifiedDate);
		// only select ID of row because we have to get the details in any case
		String[] selectedColumns = { ArtifactSoapDO.COLUMN_ID,
				ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE,
				ArtifactSoapDO.COLUMN_VERSION };
		SoapSortKey[] sortKeys = { new SoapSortKey(
				ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE, true) };
		SoapFilter[] filter = { new SoapFilter("modifiedAfter",
				SoapFilter.DATE_FORMAT.format(lastModifiedDate)) };
		ArtifactDetailSoapRow[] rows = mTrackerApp.getArtifactDetailList(
				sessionID, trackerId, selectedColumns, filter, sortKeys, 0, -1,
				false, true).getDataRows();
		if (rows != null) {
			log.debug("There were " + rows.length + " artifacts changed");
		}
		ArrayList<ArtifactSoapDO> detailRowsFull = new ArrayList<ArtifactSoapDO>();
		ArrayList<ArtifactSoapDO> detailRowsNew = new ArrayList<ArtifactSoapDO>();
		// retrieve artifact details
		log.debug("Getting the details of the changed artifacts");
		boolean duplicateFound = false;
		if (rows != null) {
			for (int i = 0; i < rows.length; ++i) {
				String id = rows[i].getId();
				if (id.equals(lastArtifactId)
						&& lastArtifactVersion == rows[i].getVersion()) {
					duplicateFound = true;
				} else {
					ArtifactSoapDO artifactData = mTrackerApp.getArtifactData(
							sessionID, id);
					if (!artifactData.getLastModifiedBy().equals(connectorUser)) {
						if (duplicateFound) {
							detailRowsNew.add(artifactData);
						}
						detailRowsFull.add(artifactData);
					}
				}
			}
		}
		if (!duplicateFound)
			return detailRowsFull;
		else if (detailRowsNew.isEmpty())
			return null;
		else
			return detailRowsNew;
	}

	/**
	 * Returns the artifact with the artifactId as id.
	 * 
	 * @param sessionID
	 * @param artifactId
	 * @return
	 * @throws RemoteException
	 */
	public ArtifactSoapDO getTrackerItem(String sessionID, String artifactId)
			throws RemoteException {
		return mTrackerApp.getArtifactDataFull(sessionID, artifactId);
	}

	public void createArtifactDependency(String sessionId, String originId,
			String targetId, String description) throws RemoteException {
		mTrackerApp.createArtifactDependency(sessionId, originId, targetId,
				description);
	}

	public void removeArtifactDependency(String sessionId, String originId,
			String targetId) throws RemoteException {
		mTrackerApp.removeArtifactDependency(sessionId, originId, targetId);
	}

	public void removeArtifact(String sessionId, String artifactId)
			throws RemoteException {
		mTrackerApp.deleteArtifact(sessionId, artifactId);
	}

	/**
	 * Return all tracker items that have the value fieldValue in field
	 * 
	 * @param sessionID
	 * @param trackerId
	 * @param fieldname
	 * @param fieldValue
	 * @return
	 * @throws RemoteException
	 */
	public ArtifactDetailSoapRow[] getFilteredTrackerItems(String sessionID,
			String trackerId, String fieldname, String fieldValue)
			throws RemoteException {
		String[] selectedColumns = { ArtifactSoapDO.COLUMN_ID,
				ArtifactSoapDO.COLUMN_FLEX_FIELDS };
		SoapFilter[] filter = { new SoapFilter(fieldname, fieldValue) };
		ArtifactDetailSoapRow[] rows = mTrackerApp.getArtifactDetailList(
				sessionID, trackerId, selectedColumns, filter, null, 0, -1,
				false, false).getDataRows();
		return rows;
	}

	/**
	 * Returns the custom or flex fields for a particular tracker
	 * 
	 * @param sessionID
	 * @param trackerId
	 * @return
	 * @throws RemoteException
	 */
	public TrackerFieldSoapDO[] getFlexFields(String sessionID, String trackerId)
			throws RemoteException {
		TrackerFieldSoapDO[] rows = mTrackerApp.getFields(sessionID, trackerId);
		return rows;
	}

	/**
	 * Creates an artifact.
	 * 
	 * @throws RemoteException
	 *             when an error is encountered in creating the artifact.
	 * @return Newly created artifact
	 */
	public ArtifactSoapDO createArtifact(String sessionId, String trackerId,
			String description, String category, String group, String status,
			String statusClass, String customer, int priority,
			int estimatedHours, int actualHours, Date closeDate,
			String assignedTo, String reportedReleaseId,
			String resolvedReleaseId, List<String> flexFieldNames,
			List<Object> flexFieldValues, List<String> flexFieldTypes,
			String title, String[] comments) throws RemoteException {

		SoapFieldValues flexFields = new SoapFieldValues();
		flexFields.setNames(flexFieldNames.toArray(new String[0]));
		flexFields.setValues(flexFieldValues.toArray());
		flexFields.setTypes(flexFieldTypes.toArray(new String[0]));

		ArtifactSoapDO artifactData = mTrackerApp.createArtifact(sessionId,
				trackerId, title, description, group, category, // category
				status, // status
				customer, // customer
				priority, // priority
				estimatedHours, // estimatedHours
				assignedTo, // assigned user name
				reportedReleaseId, flexFields, null, null, null);
		artifactData.setActualHours(actualHours);
		artifactData.setStatusClass(statusClass);
		artifactData.setCloseDate(closeDate);
		artifactData.setResolvedReleaseId(resolvedReleaseId);
		SoapFieldValues newFlexFields = new SoapFieldValues();
		newFlexFields.setNames(flexFieldNames.toArray(new String[0]));
		newFlexFields.setValues(flexFieldValues.toArray());
		newFlexFields.setTypes(flexFieldTypes.toArray(new String[0]));
		artifactData.setFlexFields(newFlexFields);

		boolean initialUpdated = true;
		while (initialUpdated) {
			try {
				initialUpdated = false;
				mTrackerApp.setArtifactData(sessionId, artifactData, null,
						null, null, null);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("VersionMismatchFault")) {
					throw e;
				}
				logConflictResolutor.warn(
						"Stale initial update, will override in any case ...:",
						e);
				artifactData.setVersion(artifactData.getVersion() + 1);
				initialUpdated = true;
			}
		}

		// we have to increase the version number to add the comments
		if (comments.length != 0) {
			artifactData.setVersion(artifactData.getVersion() + 1);
		}

		for (String comment : comments) {
			boolean commentNotUpdated = true;
			while (commentNotUpdated) {
				try {
					commentNotUpdated = false;
					if (StringUtils.isEmpty(comment)) {
						continue;
					}
					mTrackerApp.setArtifactData(sessionId, artifactData,
							comment, null, null, null);
					// artifactData = mTrackerApp.getArtifactData(sessionId,
					// artifactData.getId());
					artifactData.setVersion(artifactData.getVersion() + 1);
				} catch (AxisFault e) {
					javax.xml.namespace.QName faultCode = e.getFaultCode();
					if (!faultCode.getLocalPart().equals("VersionMismatchFault")) {
						throw e;
					}
					logConflictResolutor.warn(
							"Stale comment update, trying again ...:", e);
					artifactData = mTrackerApp.getArtifactData(sessionId,
							artifactData.getId());
					commentNotUpdated = true;
				}
			}
		}

		// we have to increase the version after the update
		// TODO Find out whether this really works if last modified date differs
		// from actual last modified date
		if (comments.length == 0) {
			artifactData.setVersion(artifactData.getVersion() + 1);
		}
		log.info("Artifact created: " + artifactData.getId());
		return artifactData;
	}

	/**
	 * Updates the artifact if conflict resolution priority allows it
	 * 
	 * @param ga
	 *            generic artifact passed to the update method
	 * @param sessionId
	 * @param trackerId
	 * @param description
	 * @param category
	 * @param group
	 * @param status
	 * @param statusClass
	 * @param customer
	 * @param priority
	 * @param estimatedHours
	 * @param actualHours
	 * @param closeDate
	 * @param assignedTo
	 * @param reportedReleaseId
	 * @param resolvedReleaseId
	 * @param flexFieldNames
	 * @param flexFieldValues
	 * @param flexFieldTypes
	 * @param flexFieldPreserve
	 * @param title
	 * @param Id
	 * @param comments
	 * @param conflictResolutionPriority
	 * @return updated artifact or null if conflict resolution has decided not
	 *         to update the artifact
	 * @throws RemoteException
	 */
	public ArtifactSoapDO updateArtifact(GenericArtifact ga, String sessionId,
			GenericArtifactField trackerId, GenericArtifactField description,
			GenericArtifactField category, GenericArtifactField group,
			GenericArtifactField status, GenericArtifactField statusClass,
			GenericArtifactField customer, GenericArtifactField priority,
			GenericArtifactField estimatedHours,
			GenericArtifactField actualHours, GenericArtifactField closeDate,
			GenericArtifactField assignedTo,
			GenericArtifactField reportedReleaseId,
			GenericArtifactField resolvedReleaseId,
			List<String> flexFieldNames, List<Object> flexFieldValues,
			List<String> flexFieldTypes, Set<String> overriddenFlexFields,
			GenericArtifactField title, String Id, String[] comments,
			boolean translateTechnicalReleaseIds) throws RemoteException {

		boolean mainArtifactNotUpdated = true;
		ArtifactSoapDO artifactData = null;
		while (mainArtifactNotUpdated) {
			try {
				mainArtifactNotUpdated = false;
				artifactData = mTrackerApp.getArtifactData(sessionId, Id);
				// do conflict resolution
				if (!AbstractWriter.handleConflicts(artifactData.getVersion(),
						ga)) {
					return null;
				}

				// here we store the values which will be really sent
				ArrayList<String> finalFlexFieldNames = new ArrayList<String>();
				ArrayList<String> finalFlexFieldTypes = new ArrayList<String>();
				ArrayList<Object> finalFlexFieldValues = new ArrayList<Object>();

				SoapFieldValues currentFlexFields = artifactData
						.getFlexFields();
				String[] currentFlexFieldNames = currentFlexFields.getNames();
				Object[] currentFlexFieldValues = currentFlexFields.getValues();
				String[] currentFlexFieldTypes = currentFlexFields.getTypes();

				// first we filter out all current flex fields that should be
				// overridden
				for (int i = 0; i < currentFlexFieldNames.length; ++i) {
					if (!overriddenFlexFields
							.contains(currentFlexFieldNames[i])) {
						finalFlexFieldNames.add(currentFlexFieldNames[i]);
						finalFlexFieldTypes.add(currentFlexFieldTypes[i]);
						finalFlexFieldValues.add(currentFlexFieldValues[i]);
					}
				}

				// now we have to add all anticipated flex fields
				finalFlexFieldNames.addAll(flexFieldNames);
				finalFlexFieldValues.addAll(flexFieldValues);
				finalFlexFieldTypes.addAll(flexFieldTypes);

				SoapFieldValues flexFields = new SoapFieldValues();
				flexFields.setNames(finalFlexFieldNames.toArray(new String[0]));
				flexFields.setValues(finalFlexFieldValues.toArray());
				flexFields.setTypes(finalFlexFieldTypes.toArray(new String[0]));

				String folderIdString = artifactData.getFolderId();
				if (trackerId != null && trackerId.getFieldValueHasChanged()) {
					folderIdString = (String) trackerId.getFieldValue();
					artifactData.setFolderId(folderIdString);
				}

				if (title != null && title.getFieldValueHasChanged()) {
					artifactData.setTitle((String) title.getFieldValue());
				}

				if (description != null
						&& description.getFieldValueHasChanged()) {
					artifactData.setDescription((String) description
							.getFieldValue());
				}

				if (group != null && group.getFieldValueHasChanged()) {
					artifactData.setGroup((String) group.getFieldValue());
				}

				if (category != null && category.getFieldValueHasChanged()) {
					artifactData.setCategory((String) category.getFieldValue());
				}

				if (status != null && status.getFieldValueHasChanged()) {
					artifactData.setStatus((String) status.getFieldValue());
				}

				if (customer != null && customer.getFieldValueHasChanged()) {
					artifactData.setCustomer((String) customer.getFieldValue());
				}

				if (priority != null && priority.getFieldValueHasChanged()) {
					Object fieldValueObj = priority.getFieldValue();
					int fieldValue = 0;
					if (fieldValueObj instanceof String) {
						String fieldValueString = (String) fieldValueObj;
						fieldValue = Integer.parseInt(fieldValueString);
					} else if (fieldValueObj instanceof Integer) {
						fieldValue = ((Integer) fieldValueObj).intValue();
					}
					artifactData.setPriority(fieldValue);
				}

				if (estimatedHours != null
						&& estimatedHours.getFieldValueHasChanged()) {
					Object fieldValueObj = estimatedHours.getFieldValue();
					int fieldValue = 0;
					if (fieldValueObj instanceof String) {
						String fieldValueString = (String) fieldValueObj;
						fieldValue = Integer.parseInt(fieldValueString);
					} else if (fieldValueObj instanceof Integer) {
						fieldValue = ((Integer) fieldValueObj).intValue();
					}
					artifactData.setEstimatedHours(fieldValue);
				}

				if (actualHours != null
						&& actualHours.getFieldValueHasChanged()) {
					Object fieldValueObj = actualHours.getFieldValue();
					int fieldValue = 0;
					if (fieldValueObj instanceof String) {
						String fieldValueString = (String) fieldValueObj;
						fieldValue = Integer.parseInt(fieldValueString);
					} else if (fieldValueObj instanceof Integer) {
						fieldValue = ((Integer) fieldValueObj).intValue();
					}
					artifactData.setActualHours(fieldValue);
				}

				if (assignedTo != null && assignedTo.getFieldValueHasChanged()) {
					artifactData.setAssignedTo((String) assignedTo
							.getFieldValue());
				}

				if (statusClass != null
						&& statusClass.getFieldValueHasChanged()) {
					artifactData.setStatusClass((String) statusClass
							.getFieldValue());
				}

				if (closeDate != null && closeDate.getFieldValueHasChanged()) {
					Object fieldValueObj = closeDate.getFieldValue();
					Date fieldValue = null;
					if (fieldValueObj instanceof String) {
						String fieldValueString = (String) fieldValueObj;
						fieldValue = DateUtil.parse(fieldValueString);
					} else if (fieldValueObj instanceof Date) {
						fieldValue = (Date) fieldValueObj;
					}
					artifactData.setCloseDate(fieldValue);
				}

				if (reportedReleaseId != null
						&& reportedReleaseId.getFieldValueHasChanged()) {
					String reportedReleaseIdString = (String) reportedReleaseId
							.getFieldValue();
					if (translateTechnicalReleaseIds) {
						reportedReleaseIdString = convertReleaseId(sessionId,
								reportedReleaseIdString, folderIdString);
					}
					artifactData.setReportedReleaseId(reportedReleaseIdString);
				}

				if (resolvedReleaseId != null
						&& resolvedReleaseId.getFieldValueHasChanged()) {
					String resolvedReleaseIdString = (String) resolvedReleaseId
							.getFieldValue();
					if (translateTechnicalReleaseIds) {
						resolvedReleaseIdString = convertReleaseId(sessionId,
								resolvedReleaseIdString, folderIdString);
					}
					artifactData.setResolvedReleaseId(resolvedReleaseIdString);
				}

				artifactData.setFlexFields(flexFields);

				mTrackerApp.setArtifactData(sessionId, artifactData, null,
						null, null, null);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("VersionMismatchFault")) {
					throw e;
				}
				logConflictResolutor.warn("Stale update for TF tracker item "
						+ Id + " in tracker " + trackerId
						+ ". Trying again ...", e);
				mainArtifactNotUpdated = true;
			}
		}
		// increase version number for comment updates
		if (comments.length != 0) {
			artifactData.setVersion(artifactData.getVersion() + 1);
		}

		for (String comment : comments) {
			boolean commentNotUpdated = true;
			while (commentNotUpdated) {
				try {
					commentNotUpdated = false;
					if (StringUtils.isEmpty(comment)) {
						continue;
					}
					mTrackerApp.setArtifactData(sessionId, artifactData,
							comment, null, null, null);
					artifactData.setVersion(artifactData.getVersion() + 1);
				} catch (AxisFault e) {
					javax.xml.namespace.QName faultCode = e.getFaultCode();
					if (!faultCode.getLocalPart().equals("VersionMismatchFault")) {
						throw e;
					}
					logConflictResolutor.warn(
							"Stale comment update, trying again ...:", e);
					artifactData = mTrackerApp.getArtifactData(sessionId, Id);
					commentNotUpdated = true;
				}
			}
		}

		// we have to increase the version after the update
		// TODO Find out whether this really works if last modified date differs
		// from actual last modified date
		if (comments.length == 0) {
			artifactData.setVersion(artifactData.getVersion() + 1);
		}
		log.info("Artifact updated id: " + artifactData.getId()
				+ " in tracker " + artifactData.getFolderId());
		return artifactData;
	}

	/**
	 * Returns the value of the flex field with the name fieldName for the
	 * artifact with the artifact id artifactId
	 * 
	 * @param sessionID
	 * @param fieldName
	 * @param artifactID
	 * @param fieldType
	 * @return
	 * @throws RemoteException
	 */
	public Object getFlexFieldValue(String sessionID, String fieldName,
			String artifactID, String fieldType) throws RemoteException {
		ArtifactSoapDO artifact = getTrackerItem(sessionID, artifactID);
		return getFlexFieldValue(fieldName, artifact, fieldType);
	}

	/**
	 * Returns the value of the flex field with the name fieldName from the
	 * ArtifactSoapDO object
	 * 
	 * @param fieldName
	 * @param artifact
	 * @param fieldType
	 * @return
	 * @throws RemoteException
	 */
	public Object getFlexFieldValue(String fieldName, ArtifactSoapDO artifact,
			String fieldType) throws RemoteException {
		SoapFieldValues flexFields = artifact.getFlexFields();
		String[] flexFieldNames = flexFields.getNames();
		String[] flexFieldTypes = flexFields.getTypes();
		Object[] flexFieldValues = flexFields.getValues();
		for (int i = 0; i < flexFieldNames.length; ++i) {
			if (flexFieldNames[i].equals(fieldName)) {
				if (flexFieldTypes[i].equals(fieldType))
					return flexFieldValues[i];
				else {
					log.error("Wrong type of flexField " + fieldName
							+ ", expected: " + fieldType + " received: "
							+ flexFieldTypes[i] + " for artifactID: "
							+ artifact.getId());
					return null;
				}
			}
		}
		log.warn("flexField " + fieldName + " with type " + fieldType
				+ " is missing for artifact with ID " + artifact.getId());
		return null;
	}

	public GenericArtifact createGenericArtifactsForChild(String sessionId,
			ArtifactDependencySoapRow child,
			SFEEToGenericArtifactConverter artifactConverter,
			Date lastModifiedDate, String sourceSystemTimezone)
			throws RemoteException {
		String childArtifactId = child.getTargetId();
		String parentArtifactId = child.getOriginId();
		ArtifactSoapDO artifact = this.getTrackerItem(sessionId,
				childArtifactId);
		TrackerFieldSoapDO[] trackerFields = this.getFlexFields(sessionId,
				artifact.getFolderId());
		HashMap<String, List<TrackerFieldSoapDO>> fieldsMap = SFEEAppHandler
				.loadTrackerFieldsInHashMap(trackerFields);
		// TODO As of now hard coding includeFieldMetaData to false. Should be
		// changed when we include
		// dependencies.
		GenericArtifact ga = artifactConverter.convert(artifact, fieldsMap,
				lastModifiedDate, false, sourceSystemTimezone);
		ga.setDepParentSourceArtifactId(parentArtifactId);
		ga.setSourceArtifactId(childArtifactId);
		ga.setSourceRepositoryId(artifact.getFolderId());
		ga.setArtifactType(GenericArtifact.ArtifactTypeValue.DEPENDENCY);
		return ga;
	}

	public void convertReleaseIds(String sessionId, ArtifactSoapDO artifact)
			throws RemoteException {
		String reportedReleaseId = artifact.getReportedReleaseId();
		String resolvedReleaseId = artifact.getResolvedReleaseId();
		if (!StringUtils.isEmpty(reportedReleaseId)) {
			ReleaseSoapDO releaseDO = fileReleaseApp.getReleaseData(sessionId,
					reportedReleaseId);
			String title = releaseDO.getTitle();
			artifact.setReportedReleaseId(title);
		}
		if (!StringUtils.isEmpty(resolvedReleaseId)) {
			ReleaseSoapDO releaseDO = fileReleaseApp.getReleaseData(sessionId,
					resolvedReleaseId);
			String title = releaseDO.getTitle();
			artifact.setResolvedReleaseId(title);
		}

	}

	public String convertReleaseId(String sessionId, String releaseId,
			String trackerId) throws RemoteException {
		if (!StringUtils.isEmpty(releaseId) && !StringUtils.isEmpty(trackerId)) {
			TrackerSoapDO trackerDO = mTrackerApp.getTrackerData(sessionId,
					trackerId);
			String projectId = trackerDO.getProjectId();
			PackageSoapList packageList = fileReleaseApp.getPackageList(
					sessionId, projectId);
			if (packageList != null) {
				PackageSoapRow[] packages = packageList.getDataRows();
				if (packages != null && packages.length > 0) {
					for (PackageSoapRow packageRow : packages) {
						String packageId = packageRow.getId();
						ReleaseSoapList releasesList = fileReleaseApp
								.getReleaseList(sessionId, packageId);
						if (releasesList != null) {
							ReleaseSoapRow[] releases = releasesList
									.getDataRows();
							if (releases != null && releases.length > 0) {
								for (ReleaseSoapRow release : releases) {
									String title = release.getTitle();
									if (title.equals(releaseId)) {
										return release.getId();
									}
								}
							}
						}
					}
				}
			}
		}
		return releaseId;
	}
}
