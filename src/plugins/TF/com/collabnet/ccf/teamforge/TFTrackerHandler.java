/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.teamforge;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.apache.axis.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ce.soap50.webservices.tracker.ArtifactSoapDO;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.Filter;
import com.collabnet.teamforge.api.PlanningFolderRuleViolationException;
import com.collabnet.teamforge.api.SortKey;
import com.collabnet.teamforge.api.frs.PackageList;
import com.collabnet.teamforge.api.frs.PackageRow;
import com.collabnet.teamforge.api.frs.ReleaseList;
import com.collabnet.teamforge.api.frs.ReleaseRow;
import com.collabnet.teamforge.api.planning.PlanningFolderDO;
import com.collabnet.teamforge.api.planning.PlanningFolderRow;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.ArtifactDependencyRow;
import com.collabnet.teamforge.api.tracker.ArtifactDetailList;
import com.collabnet.teamforge.api.tracker.ArtifactDetailRow;
import com.collabnet.teamforge.api.tracker.TrackerDO;
import com.collabnet.teamforge.api.tracker.TrackerFieldDO;
import com.collabnet.teamforge.api.tracker.TrackerFieldValueDO;

/**
 * The tracker handler class provides support for listing and/or edit trackers
 * and artifacts.
 */
public class TFTrackerHandler {
    /**
     * Tracker Soap API handle
     */

    private static final Log log                  = LogFactory
                                                          .getLog(TFTrackerHandler.class);
    private static final Log logConflictResolutor = LogFactory
                                                          .getLog("com.collabnet.ccf.core.conflict.resolution");

    /**
     * Class constructor.
     * 
     * @param serverUrl
     *            - The source TF SOAP server URL
     * @param connectionManager
     */
    public TFTrackerHandler(String serverUrl,
            ConnectionManager<Connection> connectionManager) {
        // enable this if you do not like to have the retry code enabled

        // TODO SHould we implement a custom retry mechanism again?
    }

    /**
     * Creates an artifact.
     * 
     * @throws RemoteException
     *             when an error is encountered in creating the artifact.
     * @return Newly created artifact
     * @throws PlanningFolderRuleViolationException
     */
    public ArtifactDO createArtifact(Connection connection, String trackerId,
            String description, String category, String group, String status,
            String statusClass, String customer, int priority,
            int estimatedEfforts, int actualEfforts, Date closeDate,
            String assignedTo, String reportedReleaseId,
            String resolvedReleaseId, List<String> flexFieldNames,
            List<Object> flexFieldValues, List<String> flexFieldTypes,
            String title, String[] comments, int remainingEfforts,
            boolean autosumming, String planningFolderId, int points)
            throws RemoteException, PlanningFolderRuleViolationException {

        FieldValues flexFields = new FieldValues();
        flexFields.setNames(flexFieldNames.toArray(new String[0]));
        flexFields.setValues(flexFieldValues.toArray());
        flexFields.setTypes(flexFieldTypes.toArray(new String[0]));

        ArtifactDO artifactData = connection.getTrackerClient().createArtifact(
                trackerId, title, description, group,
                category, // category
                status, // status
                customer, // customer
                priority, // priority
                autosumming ? 0 : estimatedEfforts, // estimated efforts
                autosumming ? 0 : remainingEfforts, // remaining efforts
                autosumming,
                points, // story points
                assignedTo, // assigned user name
                reportedReleaseId, planningFolderId, flexFields, null, null,
                null);
        if (!autosumming) {
            artifactData.setActualEffort(actualEfforts);
        }
        artifactData.setStatusClass(statusClass);
        artifactData.setCloseDate(closeDate);
        artifactData.setResolvedReleaseId(resolvedReleaseId);
        FieldValues newFlexFields = new FieldValues();
        newFlexFields.setNames(flexFieldNames.toArray(new String[0]));
        newFlexFields.setValues(flexFieldValues.toArray());
        newFlexFields.setTypes(flexFieldTypes.toArray(new String[0]));
        artifactData.setFlexFields(newFlexFields);

        boolean initialUpdated = true;
        while (initialUpdated) {
            try {
                initialUpdated = false;
                connection.getTrackerClient().setArtifactData(artifactData,
                        null, null, null, null);
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
                    connection.getTrackerClient().setArtifactData(artifactData,
                            comment, null, null, null);
                    // artifactData = mTrackerApp.getArtifactData(sessionId,
                    // artifactData.getId());
                    artifactData.setVersion(artifactData.getVersion() + 1);
                } catch (AxisFault e) {
                    javax.xml.namespace.QName faultCode = e.getFaultCode();
                    if (!faultCode.getLocalPart()
                            .equals("VersionMismatchFault")) {
                        throw e;
                    }
                    logConflictResolutor.warn(
                            "Stale comment update, trying again ...:", e);
                    artifactData = connection.getTrackerClient()
                            .getArtifactData(artifactData.getId());
                    commentNotUpdated = true;
                }
            }
        }

        // it looks as if since TF 5.3, not every update call automatically
        // increases the version number
        // hence we retrieve the artifact version here again
        if (comments.length == 0) {
            // artifactData.setVersion(artifactData.getVersion() + 1);
            artifactData = connection.getTrackerClient().getArtifactData(
                    artifactData.getId());
        }

        log.info("Artifact created: " + artifactData.getId() + " in "
                + trackerId);
        return artifactData;
    }

    public void createArtifactDependency(Connection connection,
            String originId, String targetId, String description)
            throws RemoteException, PlanningFolderRuleViolationException {
        connection.getTrackerClient().createArtifactDependency(originId,
                targetId, description);
    }

    /**
     * Get child artifact dependencies for a certain artifact
     * 
     * @throws RemoteException
     */
    public ArtifactDependencyRow[] getArtifactChildDependencies(
            Connection connection, String artifactId) throws RemoteException {
        return connection.getTrackerClient().getChildDependencyList(artifactId)
                .getDataRows();
    }

    /**
     * Get parent artifact dependencies for a certain artifact
     * 
     * @throws RemoteException
     */
    public ArtifactDependencyRow[] getArtifactParentDependencies(
            Connection connection, String artifactId) throws RemoteException {
        return connection.getTrackerClient()
                .getParentDependencyList(artifactId).getDataRows();
    }

    public List<PlanningFolderDO> getChangedPlanningFolders(
            Connection connection, String sourceRepositoryId,
            Date lastModifiedDate, String lastSynchronizedArtifactId,
            int version, String project) throws RemoteException {
        log.debug("Getting the changed planning folders from "
                + lastModifiedDate);

        PlanningFolderRow[] rows = connection.getPlanningClient()
                .getPlanningFolderList(project, true).getDataRows();
        ArrayList<PlanningFolderDO> detailRowsFull = new ArrayList<PlanningFolderDO>();
        ArrayList<PlanningFolderDO> detailRowsNew = new ArrayList<PlanningFolderDO>();
        // retrieve artifact details
        log.debug("Getting the details of the changed planning folders");
        boolean duplicateFound = false;
        if (rows != null) {
            for (int i = 0; i < rows.length; ++i) {
                if (!rows[i].getLastModifiedOn().after(lastModifiedDate)) {
                    continue;
                }
                String id = rows[i].getId();
                PlanningFolderDO planningFolder = connection
                        .getPlanningClient().getPlanningFolderData(id);
                if (id.equals(lastSynchronizedArtifactId)) {
                    if (version == planningFolder.getVersion()) {
                        duplicateFound = true;
                        continue;
                    }
                }
                if (duplicateFound) {
                    detailRowsNew.add(planningFolder);
                }
                detailRowsFull.add(planningFolder);
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
     * Return all changed tracker items in a List
     * 
     * @param sessionID
     * @param trackerId
     * @param connectorUser
     * @return null if only duplicates were found, else a list of changed
     *         tracker items
     * @throws RemoteException
     */
    public List<ArtifactDetailRow> getChangedTrackerItems(
            Connection connection, String trackerId, Date lastModifiedDate,
            String lastArtifactId, int lastArtifactVersion)
            throws RemoteException {
        log.debug("Getting the changed artifacts from " + lastModifiedDate);
        // only select ID of row because we have to get the details in any case
        String[] selectedColumns = { ArtifactSoapDO.COLUMN_ID,
                ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE,
                ArtifactSoapDO.COLUMN_VERSION };

        SortKey[] sortKeys = { new SortKey(
                ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE, true) };
        Filter[] filter = { new Filter("modifiedAfter",
                Filter.DATE_FORMAT.format(lastModifiedDate)) };

        ArtifactDetailRow[] rows = null;

        rows = connection
                .getTrackerClient()
                .getArtifactDetailList(trackerId, selectedColumns, filter,
                        sortKeys, 0, -1, false, true).getDataRows();

        if (rows != null) {
            log.debug("There were " + rows.length + " artifacts changed");
        }
        ArrayList<ArtifactDetailRow> detailRowsFull = new ArrayList<ArtifactDetailRow>();
        ArrayList<ArtifactDetailRow> detailRowsNew = new ArrayList<ArtifactDetailRow>();
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
                    if (duplicateFound) {
                        detailRowsNew.add(rows[i]);
                    }
                    detailRowsFull.add(rows[i]);
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
     * Returns the custom or flex fields for a particular tracker
     * 
     * @param sessionID
     * @param trackerId
     * @return
     * @throws RemoteException
     */
    public TrackerFieldDO[] getFlexFields(Connection connection,
            String trackerId) throws RemoteException {
        TrackerFieldDO[] rows = connection.getTrackerClient().getFields(
                trackerId);
        return rows;
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
    public Object getFlexFieldValue(String fieldName, ArtifactDO artifact,
            String fieldType) throws RemoteException {
        FieldValues flexFields = artifact.getFlexFields();
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

    /**
     * Returns the artifact with the artifactId as id. Uses pre SP 1 HF 1 web
     * service API
     * 
     * @param sessionID
     * @param artifactId
     * @return
     * @throws RemoteException
     */
    public ArtifactDO getTrackerItem(Connection connection, String artifactId)
            throws RemoteException {
        return connection.getTrackerClient().getArtifactData(artifactId);
    }

    /**
     * Returns the artifact with the artifactId as id. Uses SP 1 HF 1 web
     * service API
     * 
     * @param sessionID
     * @param artifactId
     * @return
     * @throws RemoteException
     */
    public ArtifactDO getTrackerItemFull(Connection connection,
            String artifactId) throws RemoteException {
        return connection.getTrackerClient().getArtifactDataFull(artifactId);
    }

    public void removeArtifact(Connection connection, String artifactId)
            throws RemoteException {
        connection.getTrackerClient().deleteArtifact(artifactId);
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
    /*
     * public Object getFlexFieldValue(String sessionID, String fieldName,
     * String artifactID, String fieldType) throws RemoteException {
     * ArtifactSoapDO artifact = getTrackerItem(sessionID, artifactID); return
     * getFlexFieldValue(fieldName, artifact, fieldType); }
     */

    public void removeArtifactDependency(Connection connection,
            String originId, String targetId) throws RemoteException {
        connection.getTrackerClient().removeArtifactDependency(originId,
                targetId);
    }

    /*
     * public GenericArtifact createGenericArtifactsForChild(String sessionId,
     * ArtifactDependencySoapRow child, SFEEToGenericArtifactConverter
     * artifactConverter, Date lastModifiedDate, String sourceSystemTimezone)
     * throws RemoteException { String childArtifactId = child.getTargetId();
     * String parentArtifactId = child.getOriginId(); ArtifactSoapDO artifact =
     * this.getTrackerItem(sessionId, childArtifactId); TrackerFieldSoapDO[]
     * trackerFields = this.getFlexFields(sessionId, artifact.getFolderId());
     * HashMap<String, List<TrackerFieldSoapDO>> fieldsMap = SFEEAppHandler
     * .loadTrackerFieldsInHashMap(trackerFields); // TODO As of now hard coding
     * includeFieldMetaData to false. Should be // changed when we include //
     * dependencies. GenericArtifact ga = artifactConverter.convert(artifact,
     * fieldsMap, lastModifiedDate, false, sourceSystemTimezone);
     * ga.setDepParentSourceArtifactId(parentArtifactId);
     * ga.setSourceArtifactId(childArtifactId);
     * ga.setSourceRepositoryId(artifact.getFolderId());
     * ga.setArtifactType(GenericArtifact.ArtifactTypeValue.DEPENDENCY); return
     * ga; }
     */

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
     * @param estimatedEfforts
     * @param actualEfforts
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
     * @param newParentId
     * @param associateWithParent
     * @param currentParentId
     * @param deleteOldParentAssociation
     * @param packageReleaseSeparatorString
     * @param conflictResolutionPriority
     * @return updated artifact or null if conflict resolution has decided not
     *         to update the artifact
     * @throws RemoteException
     * @throws PlanningFolderRuleViolationException
     */
    public ArtifactDO updateArtifact(GenericArtifact ga, Connection connection,
            GenericArtifactField trackerId, GenericArtifactField description,
            GenericArtifactField category, GenericArtifactField group,
            GenericArtifactField status, GenericArtifactField statusClass,
            GenericArtifactField customer, GenericArtifactField priority,
            GenericArtifactField estimatedEfforts,
            GenericArtifactField actualEfforts, GenericArtifactField closeDate,
            GenericArtifactField assignedTo,
            GenericArtifactField reportedReleaseId,
            GenericArtifactField resolvedReleaseId,
            List<String> flexFieldNames, List<Object> flexFieldValues,
            List<String> flexFieldTypes, Set<String> overriddenFlexFields,
            GenericArtifactField title, String Id, String[] comments,
            boolean translateTechnicalReleaseIds,
            GenericArtifactField remainingEfforts,
            GenericArtifactField autosumming, GenericArtifactField storyPoints,
            GenericArtifactField planningFolderId,
            boolean deleteOldParentAssociation, String currentParentId,
            boolean associateWithParent, String newParentId,
            String packageReleaseSeparatorString) throws RemoteException,
            PlanningFolderRuleViolationException {

        boolean mainArtifactNotUpdated = true;
        boolean oldParentRemoved = false;
        ArtifactDO artifactData = null;
        while (mainArtifactNotUpdated) {
            try {
                mainArtifactNotUpdated = false;
                artifactData = connection.getTrackerClient()
                        .getArtifactData(Id);
                // do conflict resolution
                if (!AbstractWriter.handleConflicts(artifactData.getVersion(),
                        ga)) {
                    return null;
                }

                if (deleteOldParentAssociation && !oldParentRemoved) {
                    removeArtifactDependency(connection, currentParentId, Id);
                    oldParentRemoved = true;
                }

                // here we store the values which will be really sent
                ArrayList<String> finalFlexFieldNames = new ArrayList<String>();
                ArrayList<String> finalFlexFieldTypes = new ArrayList<String>();
                ArrayList<Object> finalFlexFieldValues = new ArrayList<Object>();

                FieldValues currentFlexFields = artifactData.getFlexFields();
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

                FieldValues flexFields = new FieldValues();
                flexFields.setNames(finalFlexFieldNames.toArray(new String[0]));
                flexFields.setValues(finalFlexFieldValues.toArray());
                flexFields.setTypes(finalFlexFieldTypes.toArray(new String[0]));

                // we need this property to determine whether we may update the
                // efforts fields
                boolean autoSummingTurnedOn = artifactData.getAutosumming();

                if (autosumming != null
                        && autosumming.getFieldValueHasChanged()) {
                    Object fieldValueObj = autosumming.getFieldValue();
                    Boolean fieldValue = false;
                    if (fieldValueObj instanceof String) {
                        String fieldValueString = (String) fieldValueObj;
                        fieldValue = Boolean.parseBoolean(fieldValueString);
                    } else if (fieldValueObj instanceof Boolean) {
                        fieldValue = (Boolean) fieldValueObj;
                    }
                    autoSummingTurnedOn = fieldValue;
                    artifactData.setAutosumming(fieldValue);
                }

                // check if we do not support the autosumming flag at all
                if (!connection.supports53()) {
                    autoSummingTurnedOn = false;
                }

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
                        try {
                            fieldValue = Integer.parseInt(fieldValueString);
                        } catch (NumberFormatException e) {
                            throw new CCFRuntimeException(
                                    "Could not parse value of mandatory field priority: "
                                            + e.getMessage(), e);
                        }
                    } else if (fieldValueObj instanceof Integer) {
                        fieldValue = ((Integer) fieldValueObj).intValue();
                    }
                    artifactData.setPriority(fieldValue);
                }

                if (!autoSummingTurnedOn && estimatedEfforts != null
                        && estimatedEfforts.getFieldValueHasChanged()) {
                    Object fieldValueObj = estimatedEfforts.getFieldValue();
                    int fieldValue = 0;
                    if (fieldValueObj instanceof String) {
                        String fieldValueString = (String) fieldValueObj;
                        try {
                            fieldValue = Integer.parseInt(fieldValueString);
                        } catch (NumberFormatException e) {
                            throw new CCFRuntimeException(
                                    "Could not parse value of mandatory field estimatedEffort: "
                                            + e.getMessage(), e);
                        }
                    } else if (fieldValueObj instanceof Integer) {
                        fieldValue = ((Integer) fieldValueObj).intValue();
                    }
                    artifactData.setEstimatedEffort(fieldValue);
                }

                if (!autoSummingTurnedOn && actualEfforts != null
                        && actualEfforts.getFieldValueHasChanged()) {
                    Object fieldValueObj = actualEfforts.getFieldValue();
                    int fieldValue = 0;
                    if (fieldValueObj instanceof String) {
                        String fieldValueString = (String) fieldValueObj;
                        try {
                            fieldValue = Integer.parseInt(fieldValueString);
                        } catch (NumberFormatException e) {
                            throw new CCFRuntimeException(
                                    "Could not parse value of mandatory field actualEffort: "
                                            + e.getMessage(), e);
                        }
                    } else if (fieldValueObj instanceof Integer) {
                        fieldValue = ((Integer) fieldValueObj).intValue();
                    }
                    artifactData.setActualEffort(fieldValue);
                }

                if (!autoSummingTurnedOn && remainingEfforts != null
                        && remainingEfforts.getFieldValueHasChanged()) {
                    Object fieldValueObj = remainingEfforts.getFieldValue();
                    int fieldValue = 0;
                    if (fieldValueObj instanceof String) {
                        String fieldValueString = (String) fieldValueObj;
                        try {
                            fieldValue = Integer.parseInt(fieldValueString);
                        } catch (NumberFormatException e) {
                            throw new CCFRuntimeException(
                                    "Could not parse value of mandatory field remainingEffort: "
                                            + e.getMessage(), e);
                        }
                    } else if (fieldValueObj instanceof Integer) {
                        fieldValue = ((Integer) fieldValueObj).intValue();
                    }
                    artifactData.setRemainingEffort(fieldValue);
                }

                if (assignedTo != null && assignedTo.getFieldValueHasChanged()) {
                    artifactData.setAssignedTo((String) assignedTo
                            .getFieldValue());
                }

                if (planningFolderId != null
                        && planningFolderId.getFieldValueHasChanged()) {
                    artifactData.setPlanningFolderId((String) planningFolderId
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
                        reportedReleaseIdString = convertReleaseId(connection,
                                reportedReleaseIdString, folderIdString,
                                packageReleaseSeparatorString);
                    }
                    artifactData.setReportedReleaseId(reportedReleaseIdString);
                }

                if (resolvedReleaseId != null
                        && resolvedReleaseId.getFieldValueHasChanged()) {
                    String resolvedReleaseIdString = (String) resolvedReleaseId
                            .getFieldValue();
                    if (translateTechnicalReleaseIds) {
                        resolvedReleaseIdString = convertReleaseId(connection,
                                resolvedReleaseIdString, folderIdString,
                                packageReleaseSeparatorString);
                    }
                    artifactData.setResolvedReleaseId(resolvedReleaseIdString);
                }

                if (storyPoints != null
                        && storyPoints.getFieldValueHasChanged()) {
                    Object fieldValueObj = storyPoints.getFieldValue();
                    int fieldValue = 0;
                    if (fieldValueObj instanceof String) {
                        String fieldValueString = (String) fieldValueObj;
                        try {
                            fieldValue = Integer.parseInt(fieldValueString);
                        } catch (NumberFormatException e) {
                            throw new CCFRuntimeException(
                                    "Could not parse value of mandatory field points: "
                                            + e.getMessage(), e);
                        }
                    } else if (fieldValueObj instanceof Integer) {
                        fieldValue = ((Integer) fieldValueObj).intValue();
                    }
                    artifactData.setPoints(fieldValue);
                }

                artifactData.setFlexFields(flexFields);
                String firstComment = comments.length > 0 ? comments[0] : null;
                connection.getTrackerClient().setArtifactData(artifactData,
                        firstComment, null, null, null);
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

        boolean first = true;
        for (String comment : comments) {
            if (first) {
                // we already processed the first comment above.
                first = false;
                continue;
            }
            boolean commentNotUpdated = true;
            while (commentNotUpdated) {
                try {
                    commentNotUpdated = false;
                    if (StringUtils.isEmpty(comment)) {
                        continue;
                    }
                    connection.getTrackerClient().setArtifactData(artifactData,
                            comment, null, null, null);
                    artifactData.setVersion(artifactData.getVersion() + 1);
                } catch (AxisFault e) {
                    javax.xml.namespace.QName faultCode = e.getFaultCode();
                    if (!faultCode.getLocalPart()
                            .equals("VersionMismatchFault")) {
                        throw e;
                    }
                    logConflictResolutor.warn(
                            "Stale comment update, trying again ...:", e);
                    artifactData = connection.getTrackerClient()
                            .getArtifactData(Id);
                    commentNotUpdated = true;
                }
            }
        }

        // it looks as if since TF 5.3, not every update call automatically
        // increases the version number
        // hence we retrieve the artifact version here again
        //if (comments.length == 0) {
        // artifactData.setVersion(artifactData.getVersion() + 1);
        artifactData = connection.getTrackerClient().getArtifactData(Id);
        //}

        if (associateWithParent) {
            createArtifactDependency(connection, newParentId, Id,
                    "CCF generated parent-child relationship");
        }

        log.info("Artifact updated id: " + artifactData.getId() + " in "
                + artifactData.getFolderId());
        return artifactData;
    }

    /**
     * Updates fields of tracker definition This method currently does not
     * support conflict detection
     * 
     * @param ga
     * @param trackerId
     *            tracker in question
     * @param fieldsToBeChanged
     *            fields to be adjusted
     * @param connection
     * @return
     * @throws RemoteException
     */
    public TrackerDO updateTrackerMetaData(GenericArtifact ga,
            String trackerId, Map<String, SortedSet<String>> fieldsToBeChanged,
            Connection connection) throws RemoteException {
        Exception exception = null;
        for (String fieldName : fieldsToBeChanged.keySet()) {
            boolean updated = false;
            while (!updated) {
                updated = true;
                try {
                    // we have to refetch this data in the loop to avoid version
                    // mismatch exceptions
                    TrackerFieldDO[] fields = connection.getTrackerClient()
                            .getFields(trackerId);

                    // find field in question (we do not create new fields yet)
                    TrackerFieldDO trackerField = null;
                    for (TrackerFieldDO field : fields) {
                        if (field.getName().equals(fieldName)) {
                            trackerField = field;
                            break;
                        }
                    }
                    if (trackerField == null) {
                        throw new CCFRuntimeException("Field " + fieldName
                                + " of tracker " + trackerId
                                + " could not be found.");
                    }

                    // find out whether field is single select or multi select
                    boolean fieldIsSingleSelect = trackerField.getFieldType()
                            .equals(TrackerFieldDO.FIELD_TYPE_SINGLE_SELECT);

                    SortedSet<String> anticipatedFieldValues = fieldsToBeChanged
                            .get(fieldName);
                    List<TrackerFieldValueDO> deletedFieldValues = new ArrayList<TrackerFieldValueDO>();
                    Set<String> addedFieldValues = new HashSet<String>();
                    Map<String, String> currentValues = new HashMap<String, String>();
                    TrackerFieldValueDO[] currentFieldValues = trackerField
                            .getFieldValues();

                    for (TrackerFieldValueDO currentFieldValue : currentFieldValues) {
                        currentValues.put(currentFieldValue.getValue(),
                                currentFieldValue.getId());
                        if (!anticipatedFieldValues.contains(currentFieldValue
                                .getValue())) {
                            deletedFieldValues.add(currentFieldValue);
                        }
                    }

                    for (String anticipatedFieldValue : anticipatedFieldValues) {
                        if (!currentValues.containsKey(anticipatedFieldValue)) {
                            addedFieldValues.add(anticipatedFieldValue);
                        }
                    }

                    if (deletedFieldValues.isEmpty()
                            && addedFieldValues.isEmpty()) {
                        continue;
                    }

                    List<TrackerFieldValueDO> updatedValuesList = new ArrayList<TrackerFieldValueDO>();
                    for (String anticipatedFieldValue : anticipatedFieldValues) {
                        TrackerFieldValueDO fieldValue = new TrackerFieldValueDO(
                                connection.supports50());
                        fieldValue.setIsDefault(false);
                        fieldValue.setValue(anticipatedFieldValue);
                        fieldValue.setId(currentValues
                                .get(anticipatedFieldValue));
                        updatedValuesList.add(fieldValue);
                    }

                    // we cannot delete field values if those are still used by
                    // tracker
                    // items
                    for (TrackerFieldValueDO deletedFieldValue : deletedFieldValues) {
                        if (isFieldValueUsed(trackerId, fieldName,
                                deletedFieldValue, fieldIsSingleSelect,
                                connection)) {
                            log.warn("Could not delete field value "
                                    + deletedFieldValue.getValue()
                                    + " of field "
                                    + fieldName
                                    + " in tracker "
                                    + trackerId
                                    + " because there are still artifacts that use this value.");
                            int insertIndex = getInsertIndex(updatedValuesList,
                                    deletedFieldValue);
                            updatedValuesList.add(insertIndex,
                                    deletedFieldValue);
                        }
                    }
                    TrackerFieldValueDO[] fieldValues = new TrackerFieldValueDO[updatedValuesList
                            .size()];
                    updatedValuesList.toArray(fieldValues);
                    trackerField.setFieldValues(fieldValues);
                    connection.getTrackerClient().setField(trackerId,
                            trackerField);
                } catch (AxisFault e) {
                    javax.xml.namespace.QName faultCode = e.getFaultCode();
                    if (!faultCode.getLocalPart()
                            .equals("VersionMismatchFault")) {
                        // throw e;
                        // we do not throw an error yet since we like to give
                        // other fields the chance to be properly updated
                        log.error(
                                "During TF meta data update, an error occured, proceeding to give other fields a chance to be updated ..."
                                        + e.getMessage(), e);
                        exception = e;
                        continue;
                    }
                    updated = false;
                    // we currently do not support conflict detection for meta
                    // data updates
                    logConflictResolutor
                            .warn("Stale tracker meta data update, will override in any case ...:",
                                    e);
                }
            }
        }
        if (exception != null) {
            throw new CCFRuntimeException(
                    "During TF tracker meta data update, at least one exception occured.",
                    exception);
        }
        return connection.getTrackerClient().getTrackerData(trackerId);
    }

    private int getInsertIndex(List<TrackerFieldValueDO> updatedValuesList,
            TrackerFieldValueDO insertedValue) {
        int index = 0;
        for (TrackerFieldValueDO fieldValue : updatedValuesList) {
            if (fieldValue.getValue().compareTo(insertedValue.getValue()) > 0) {
                break;
            }
            ++index;
        }
        return index;
    }

    private boolean isFieldValueUsed(String trackerId, String fieldName,
            TrackerFieldValueDO fieldValue, boolean fieldIsSingleSelect,
            Connection connection) throws RemoteException {
        Filter filter = new Filter(fieldName,
                fieldIsSingleSelect ? fieldValue.getId()
                        : fieldValue.getValue());
        Filter[] filters = { filter };
        String[] selectedColumns = { ArtifactSoapDO.COLUMN_ID };
        ArtifactDetailList artifactList = connection.getTrackerClient()
                .getArtifactDetailList(trackerId, selectedColumns, filters,
                        null, 0, 1, false, true);
        return artifactList.getDataRows().length > 0;
    }

    public static String convertReleaseId(Connection connection,
            String releaseId, String trackerId,
            String packageReleaseSeparatorString) throws RemoteException {
        if (!StringUtils.isEmpty(releaseId) && !StringUtils.isEmpty(trackerId)) {
            TrackerDO trackerDO = connection.getTrackerClient().getTrackerData(
                    trackerId);
            String projectId = trackerDO.getProjectId();
            return convertReleaseIdForProject(connection, releaseId, projectId,
                    packageReleaseSeparatorString);
        }
        return null;
    }

    public static String convertReleaseIdForProject(Connection connection,
            String releaseId, String projectId,
            String packageReleaseSeparatorString) throws RemoteException {
        if (StringUtils.isEmpty(releaseId) || StringUtils.isEmpty(projectId)) {
            return null;
        }
        String packageTitle = null;
        if (packageReleaseSeparatorString != null) {
            // we have to extract the package title from the releaseId string
            int packageDelimiter = releaseId
                    .indexOf(packageReleaseSeparatorString);
            if (packageDelimiter != -1) {
                // found separator, now extract package and release title
                packageTitle = releaseId.substring(0, packageDelimiter);
                releaseId = releaseId.substring(packageDelimiter
                        + packageReleaseSeparatorString.length());
            } else {
                log.warn("Passed release string '" + releaseId
                        + "' does not contain package / release separator '"
                        + packageReleaseSeparatorString
                        + "', treating whole string as release title ...");
            }
        }
        PackageList packageList = connection.getFrsClient().getPackageList(
                projectId);
        if (packageList != null) {
            PackageRow[] packages = packageList.getDataRows();
            if (packages != null) {
                for (PackageRow packageRow : packages) {
                    if (packageTitle != null
                            && !packageTitle.equals(packageRow.getTitle())) {
                        // this is not the package we are looking for, proceed
                        continue;
                    }
                    String packageId = packageRow.getId();
                    ReleaseList releasesList = connection.getFrsClient()
                            .getReleaseList(packageId);
                    if (releasesList != null) {
                        ReleaseRow[] releases = releasesList.getDataRows();
                        if (releases != null) {
                            for (ReleaseRow release : releases) {
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
        return null;
    }

}
