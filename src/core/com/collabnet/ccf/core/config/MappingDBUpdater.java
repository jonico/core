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

package com.collabnet.ccf.core.config;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.connector.jdbc.writer.JDBCWriteConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * The MappingDBUpdator is typically the last component in the pipeline. It
 * updates the synchronization status table and the identity mapping table after
 * the artifact has been saved in the target system.
 * 
 * @author jnicolai
 * 
 */
public class MappingDBUpdater extends LifecycleComponent implements IDataProcessor {

    // This properties are the column names within the data base
    // They must not used to retrieve attributes from the GenericArtifactFormat
    private static final String DEP_PARENT_TARGET_REPOSITORY_KIND    = "DEP_PARENT_TARGET_REPOSITORY_KIND";
    private static final String DEP_PARENT_TARGET_REPOSITORY_ID      = "DEP_PARENT_TARGET_REPOSITORY_ID";
    private static final String DEP_PARENT_TARGET_ARTIFACT_ID        = "DEP_PARENT_TARGET_ARTIFACT_ID";
    private static final String DEP_PARENT_SOURCE_REPOSITORY_KIND    = "DEP_PARENT_SOURCE_REPOSITORY_KIND";
    private static final String DEP_PARENT_SOURCE_REPOSITORY_ID      = "DEP_PARENT_SOURCE_REPOSITORY_ID";
    private static final String DEP_PARENT_SOURCE_ARTIFACT_ID        = "DEP_PARENT_SOURCE_ARTIFACT_ID";
    private static final String DEP_CHILD_TARGET_REPOSITORY_KIND     = "DEP_CHILD_TARGET_REPOSITORY_KIND";
    private static final String DEP_CHILD_TARGET_REPOSITORY_ID       = "DEP_CHILD_TARGET_REPOSITORY_ID";
    private static final String DEP_CHILD_TARGET_ARTIFACT_ID         = "DEP_CHILD_TARGET_ARTIFACT_ID";
    private static final String DEP_CHILD_SOURCE_REPOSITORY_KIND     = "DEP_CHILD_SOURCE_REPOSITORY_KIND";
    private static final String DEP_CHILD_SOURCE_REPOSITORY_ID       = "DEP_CHILD_SOURCE_REPOSITORY_ID";
    private static final String DEP_CHILD_SOURCE_ARTIFACT_ID         = "DEP_CHILD_SOURCE_ARTIFACT_ID";
    private static final String ARTIFACT_TYPE                        = "ARTIFACT_TYPE";
    private static final String TARGET_ARTIFACT_VERSION              = "TARGET_ARTIFACT_VERSION";
    private static final String SOURCE_ARTIFACT_VERSION              = "SOURCE_ARTIFACT_VERSION";
    private static final String TARGET_LAST_MODIFICATION_TIME        = "TARGET_LAST_MODIFICATION_TIME";
    private static final String SOURCE_LAST_MODIFICATION_TIME        = "SOURCE_LAST_MODIFICATION_TIME";
    private static final String TARGET_ARTIFACT_ID                   = "TARGET_ARTIFACT_ID";
    private static final String SOURCE_ARTIFACT_ID                   = "SOURCE_ARTIFACT_ID";
    private static final String TARGET_REPOSITORY_KIND               = "TARGET_REPOSITORY_KIND";
    private static final String TARGET_SYSTEM_KIND                   = "TARGET_SYSTEM_KIND";
    private static final String SOURCE_REPOSITORY_KIND               = "SOURCE_REPOSITORY_KIND";
    private static final String SOURCE_SYSTEM_KIND                   = "SOURCE_SYSTEM_KIND";
    private static final String TARGET_REPOSITORY_ID                 = "TARGET_REPOSITORY_ID";
    private static final String TARGET_SYSTEM_ID                     = "TARGET_SYSTEM_ID";
    private static final String SOURCE_REPOSITORY_ID                 = "SOURCE_REPOSITORY_ID";
    private static final String SOURCE_SYSTEM_ID                     = "SOURCE_SYSTEM_ID";

    private static final Log    log                                  = LogFactory
                                                                             .getLog(MappingDBUpdater.class);
    private JDBCWriteConnector  synchronizationStatusDatabaseUpdater = null;
    private JDBCReadConnector   identityMappingDatabaseReader        = null;
    private JDBCWriteConnector  identityMappingDatabaseUpdater       = null;

    private JDBCWriteConnector  identityMappingDatabaseInserter      = null;
    /*
     * This property indicates whether the synchronization status table should
     * be updated. The default value is true and should be only set to false for
     * the hospital replay scenario
     */
    private boolean             updateSynchronizationStatusTable     = true;

    private static final String NULL_VALUE                           = null;

    /**
     * Gets the data base inserter that is used to insert a new artifact mapping
     * into the identity mapping table
     * 
     * @return
     */
    public JDBCWriteConnector getIdentityMappingDatabaseInserter() {
        return identityMappingDatabaseInserter;
    }

    /**
     * Gets the (mandatory) data base reader that is used to retrieve the target
     * artifact id from the the identity mapping table.
     * 
     * @param identityMappingDatabaseReader
     */
    public JDBCReadConnector getIdentityMappingDatabaseReader() {
        return identityMappingDatabaseReader;
    }

    /**
     * Gets the data base updater that is used to update an existing artifact
     * mapping in the identity mapping table
     * 
     * @return
     */
    public JDBCWriteConnector getIdentityMappingDatabaseUpdater() {
        return identityMappingDatabaseUpdater;
    }

    /**
     * Gets the database updater to update the synchronization status table
     * 
     * @return
     */
    public JDBCWriteConnector getSynchronizationStatusDatabaseUpdater() {
        return synchronizationStatusDatabaseUpdater;
    }

    /**
     * Indicates whether the synchronization status table will be updated. The
     * default value is true and should be only set to false for the hospital
     * replay scenario
     */
    public boolean isUpdateSynchronizationStatusTable() {
        return updateSynchronizationStatusTable;
    }

    public Object[] process(Object data) {
        // I will expect a Generic Artifact object
        if (data instanceof Document) {

            String depParentSourceArtifactId = null;
            String depParentSourceRepositoryId = null;
            String depParentSourceRepositoryKind = null;
            String depParentTargetArtifactId = null;
            String depParentTargetRepositoryId = null;
            String depParentTargetRepositoryKind = null;

            try {
                Element element = XPathUtils.getRootElement((Document) data);

                String artifactAction = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.ARTIFACT_ACTION);

                String sourceArtifactId = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.SOURCE_ARTIFACT_ID);

                String sourceArtifactVersion = XPathUtils.getAttributeValue(
                        element, GenericArtifactHelper.SOURCE_ARTIFACT_VERSION);

                String sourceSystemId = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.SOURCE_SYSTEM_ID);

                String sourceRepositoryId = XPathUtils.getAttributeValue(
                        element, GenericArtifactHelper.SOURCE_REPOSITORY_ID);

                String targetSystemId = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.TARGET_SYSTEM_ID);

                String targetRepositoryId = XPathUtils.getAttributeValue(
                        element, GenericArtifactHelper.TARGET_REPOSITORY_ID);

                String sourceArtifactLastModifiedDateString = XPathUtils
                        .getAttributeValue(
                                element,
                                GenericArtifactHelper.SOURCE_ARTIFACT_LAST_MODIFICATION_DATE);
                String artifactType = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.ARTIFACT_TYPE);

                String transactionId = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.TRANSACTION_ID);

                boolean replayedArtifact = (transactionId != null && !transactionId
                        .equals(GenericArtifact.VALUE_UNKNOWN));

                java.util.Date sourceLastModifiedDate = null;
                if (sourceArtifactLastModifiedDateString
                        .equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
                    String message = "Source artifact last modified date is populated as: "
                            + sourceArtifactLastModifiedDateString;
                    log.warn(message);
                    // use the earliest date possible
                    sourceLastModifiedDate = new Date(0);
                } else {
                    sourceLastModifiedDate = DateUtil
                            .parse(sourceArtifactLastModifiedDateString);
                }

                java.sql.Timestamp sourceTime = new java.sql.Timestamp(
                        sourceLastModifiedDate.getTime());

                if (artifactAction
                        .equals(GenericArtifactHelper.ARTIFACT_ACTION_IGNORE)) {
                    if (!replayedArtifact
                            && updateSynchronizationStatusTable
                            && !artifactType
                                    .equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
                        updateSynchronizationStatusTable(data,
                                sourceArtifactId, sourceArtifactVersion,
                                sourceSystemId, sourceRepositoryId,
                                targetSystemId, targetRepositoryId, sourceTime);
                    }
                    return new Object[] { data };
                }

                String sourceSystemKind = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.SOURCE_SYSTEM_KIND);
                String sourceRepositoryKind = XPathUtils.getAttributeValue(
                        element, GenericArtifactHelper.SOURCE_REPOSITORY_KIND);

                String targetArtifactId = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.TARGET_ARTIFACT_ID);

                String targetSystemKind = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.TARGET_SYSTEM_KIND);

                String targetRepositoryKind = XPathUtils.getAttributeValue(
                        element, GenericArtifactHelper.TARGET_REPOSITORY_KIND);

                String targetArtifactLastModifiedDateString = XPathUtils
                        .getAttributeValue(
                                element,
                                GenericArtifactHelper.TARGET_ARTIFACT_LAST_MODIFICATION_DATE);

                String targetArtifactVersion = XPathUtils.getAttributeValue(
                        element, GenericArtifactHelper.TARGET_ARTIFACT_VERSION);

                if (artifactType
                        .equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
                    depParentSourceArtifactId = XPathUtils
                            .getAttributeValue(
                                    element,
                                    GenericArtifactHelper.DEP_PARENT_SOURCE_ARTIFACT_ID);
                    depParentSourceRepositoryId = XPathUtils
                            .getAttributeValue(
                                    element,
                                    GenericArtifactHelper.DEP_PARENT_SOURCE_REPOSITORY_ID);
                    depParentSourceRepositoryKind = XPathUtils
                            .getAttributeValue(
                                    element,
                                    GenericArtifactHelper.DEP_PARENT_SOURCE_REPOSITORY_KIND);
                    depParentTargetArtifactId = XPathUtils
                            .getAttributeValue(
                                    element,
                                    GenericArtifactHelper.DEP_PARENT_TARGET_ARTIFACT_ID);
                    depParentTargetRepositoryId = XPathUtils
                            .getAttributeValue(
                                    element,
                                    GenericArtifactHelper.DEP_PARENT_TARGET_REPOSITORY_ID);
                    depParentTargetRepositoryKind = XPathUtils
                            .getAttributeValue(
                                    element,
                                    GenericArtifactHelper.DEP_PARENT_TARGET_REPOSITORY_KIND);
                }

                java.util.Date targetLastModifiedDate = null;

                if (targetArtifactLastModifiedDateString
                        .equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
                    String message = "Target artifact last modified date is populated as: "
                            + targetArtifactLastModifiedDateString;
                    log.warn(message);
                    // use the earliest date possible
                    targetLastModifiedDate = new Date(0);
                } else {
                    // targetArtifactLastModifiedDateString = "June 26, 2008
                    // 11:02:26 AM GMT+05:30";
                    targetLastModifiedDate = DateUtil
                            .parse(targetArtifactLastModifiedDateString);
                }

                java.sql.Timestamp targetTime = new java.sql.Timestamp(
                        targetLastModifiedDate.getTime());

                createMapping(element, sourceArtifactId, sourceRepositoryId,
                        sourceRepositoryKind, sourceSystemId, sourceSystemKind,
                        targetArtifactId, targetRepositoryId,
                        targetRepositoryKind, targetSystemId, targetSystemKind,
                        sourceTime, sourceArtifactVersion, targetTime,
                        targetArtifactVersion, artifactType,
                        depParentSourceArtifactId, depParentSourceRepositoryId,
                        depParentSourceRepositoryKind,
                        depParentTargetArtifactId, depParentTargetRepositoryId,
                        depParentTargetRepositoryKind, NULL_VALUE, NULL_VALUE,
                        NULL_VALUE, NULL_VALUE, NULL_VALUE, NULL_VALUE);
                // we also have to create the opposite mapping
                // this is necessary to get around the duplicate detection
                // mechanism in case of initial resyncs
                if (artifactAction
                        .equals(GenericArtifactHelper.ARTIFACT_ACTION_CREATE)
                        && artifactType
                                .equals(GenericArtifactHelper.ARTIFACT_TYPE_PLAIN_ARTIFACT)) {
                    targetArtifactVersion = GenericArtifactHelper.ARTIFACT_VERSION_FORCE_RESYNC;
                }
                createMapping(element, targetArtifactId, targetRepositoryId,
                        targetRepositoryKind, targetSystemId, targetSystemKind,
                        sourceArtifactId, sourceRepositoryId,
                        sourceRepositoryKind, sourceSystemId, sourceSystemKind,
                        targetTime, targetArtifactVersion, sourceTime,
                        sourceArtifactVersion, artifactType,
                        depParentTargetArtifactId, depParentTargetRepositoryId,
                        depParentTargetRepositoryKind,
                        depParentSourceArtifactId, depParentSourceRepositoryId,
                        depParentSourceRepositoryKind, NULL_VALUE, NULL_VALUE,
                        NULL_VALUE, NULL_VALUE, NULL_VALUE, NULL_VALUE);

                if (!replayedArtifact
                        && updateSynchronizationStatusTable
                        && !artifactType
                                .equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
                    updateSynchronizationStatusTable(data, sourceArtifactId,
                            sourceArtifactVersion, sourceSystemId,
                            sourceRepositoryId, targetSystemId,
                            targetRepositoryId, sourceTime);
                }
            } catch (GenericArtifactParsingException e) {
                log.error("There is some problem in extracting attributes from Document in EntityService!!!"
                        + e);
            }
        } else {
            String message = "The Mapping updater needs a GenericArtifact object";
            message += " But it got something else.";
            log.error(message);
            throw new CCFRuntimeException(message);
        }

        return new Object[] { data };
    }

    public void reset(Object context) {
        // TODO Auto-generated method stub

    }

    /**
     * Sets the data base inserter that is used to insert a new artifact mapping
     * into the identity mapping table
     * 
     * @param identityMappingDatabaseInserter
     */
    public void setIdentityMappingDatabaseInserter(
            JDBCWriteConnector identityMappingDatabaseInserter) {
        this.identityMappingDatabaseInserter = identityMappingDatabaseInserter;
    }

    /**
     * Sets the (mandatory) data base reader that is used to retrieve the target
     * artifact id from the the identity mapping table.
     * 
     * @param identityMappingDatabaseReader
     */
    public void setIdentityMappingDatabaseReader(
            JDBCReadConnector identityMappingDatabaseReader) {
        this.identityMappingDatabaseReader = identityMappingDatabaseReader;
    }

    /**
     * Sets the data base updater that is used to update an existing artifact
     * mapping in the identity mapping table
     * 
     * @param identityMappingUpdater
     */
    public void setIdentityMappingDatabaseUpdater(
            JDBCWriteConnector identityMappingUpdater) {
        this.identityMappingDatabaseUpdater = identityMappingUpdater;
    }

    /**
     * Sets the database updater to update the synchronization status table
     * 
     * @param synchronizationStatusDatabaseUpdater
     */
    public void setSynchronizationStatusDatabaseUpdater(
            JDBCWriteConnector synchronizationStatusDatabaseUpdater) {
        this.synchronizationStatusDatabaseUpdater = synchronizationStatusDatabaseUpdater;
    }

    /**
     * 
     * Define whether the synchronization status table should be updated. The
     * default value is true and should be only set to false for the hospital
     * replay scenario
     * 
     * @param updateSynchronizationStatusTable
     *            true if update should happen
     */
    public void setUpdateSynchronizationStatusTable(
            boolean updateSynchronizationStatusTable) {
        this.updateSynchronizationStatusTable = updateSynchronizationStatusTable;
    }

    @SuppressWarnings("unchecked")
    public void validate(List exceptions) {
        if (getSynchronizationStatusDatabaseUpdater() == null) {
            log.error("synchronizationStatusDatabaseUpdater-property not set");
            exceptions.add(new ValidationException(
                    "synchronizationStatusDatabaseUpdater-property not set",
                    this));
        }

        if (getIdentityMappingDatabaseReader() == null) {
            log.error("identityMappingDatabaseReader-property not set");
            exceptions.add(new ValidationException(
                    "identityMappingDatabaseReader-property not set", this));
        }

        if (getSynchronizationStatusDatabaseUpdater() == null) {
            log.error("synchronizationStatusDatabaseUpdater-property not set");
            exceptions.add(new ValidationException(
                    "synchronizationStatusDatabaseUpdater-property not set",
                    this));
        }

        if (getIdentityMappingDatabaseUpdater() == null) {
            log.error("getIdentityMappingDatabaseUpdater-property not set");
            exceptions
                    .add(new ValidationException(
                            "getIdentityMappingDatabaseUpdater-property not set",
                            this));
        }

        if (getIdentityMappingDatabaseInserter() == null) {
            log.error("getIdentityMappingDatabaseInserter-property not set");
            exceptions
                    .add(new ValidationException(
                            "getIdentityMappingDatabaseInserter-property not set",
                            this));
        }

    }

    private void createIdentityMapping(String sourceSystemId,
            String sourceRepositoryId, String targetSystemId,
            String targetRepositoryId, String sourceSystemKind,
            String sourceRepositoryKind, String targetSystemKind,
            String targetRepositoryKind, String sourceArtifactId,
            String targetArtifactId, java.sql.Timestamp sourceTime,
            java.sql.Timestamp targetTime, String sourceArtifactVersion,
            String targetArtifactVersion, String artifactType,
            String depParentSourceArtifactId,
            String depParentSourceRepositoryId,
            String depParentSourceRepositoryKind,
            String depParentTargetArtifactId,
            String depParentTargetRepositoryId,
            String depParentTargetRepositoryKind,
            String depChildSourceArtifactId, String depChildSourceRepositoryId,
            String depChildSourceRepositoryKind,
            String depChildTargetArtifactId, String depChildTargetRepositoryId,
            String depChildTargetRepositoryKind) {
        IOrderedMap inputParameters = new OrderedHashMap();

        inputParameters.add(0, SOURCE_SYSTEM_ID, sourceSystemId);
        inputParameters.add(1, SOURCE_REPOSITORY_ID, sourceRepositoryId);
        inputParameters.add(2, TARGET_SYSTEM_ID, targetSystemId);
        inputParameters.add(3, TARGET_REPOSITORY_ID, targetRepositoryId);
        inputParameters.add(4, SOURCE_SYSTEM_KIND, sourceSystemKind);
        inputParameters.add(5, SOURCE_REPOSITORY_KIND, sourceRepositoryKind);
        inputParameters.add(6, TARGET_SYSTEM_KIND, targetSystemKind);
        inputParameters.add(7, TARGET_REPOSITORY_KIND, targetRepositoryKind);
        inputParameters.add(8, SOURCE_ARTIFACT_ID, sourceArtifactId);
        inputParameters.add(9, TARGET_ARTIFACT_ID, targetArtifactId);
        inputParameters.add(10, SOURCE_LAST_MODIFICATION_TIME, sourceTime);
        inputParameters.add(11, TARGET_LAST_MODIFICATION_TIME, targetTime);
        inputParameters.add(12, SOURCE_ARTIFACT_VERSION, sourceArtifactVersion);
        inputParameters.add(13, TARGET_ARTIFACT_VERSION, targetArtifactVersion);
        inputParameters.add(14, ARTIFACT_TYPE, artifactType);
        inputParameters.add(15, DEP_CHILD_SOURCE_ARTIFACT_ID,
                depChildSourceArtifactId);
        inputParameters.add(16, DEP_CHILD_SOURCE_REPOSITORY_ID,
                depChildSourceRepositoryId);
        inputParameters.add(17, DEP_CHILD_SOURCE_REPOSITORY_KIND,
                depChildSourceRepositoryKind);
        inputParameters.add(18, DEP_CHILD_TARGET_ARTIFACT_ID,
                depChildTargetArtifactId);
        inputParameters.add(19, DEP_CHILD_TARGET_REPOSITORY_ID,
                depChildTargetRepositoryId);
        inputParameters.add(20, DEP_CHILD_TARGET_REPOSITORY_KIND,
                depChildTargetRepositoryKind);
        inputParameters.add(21, DEP_PARENT_SOURCE_ARTIFACT_ID,
                depParentSourceArtifactId);
        inputParameters.add(22, DEP_PARENT_SOURCE_REPOSITORY_ID,
                depParentSourceRepositoryId);
        inputParameters.add(23, DEP_PARENT_SOURCE_REPOSITORY_KIND,
                depParentSourceRepositoryKind);
        inputParameters.add(24, DEP_PARENT_TARGET_ARTIFACT_ID,
                depParentTargetArtifactId);
        inputParameters.add(25, DEP_PARENT_TARGET_REPOSITORY_ID,
                depParentTargetRepositoryId);
        inputParameters.add(26, DEP_PARENT_TARGET_REPOSITORY_KIND,
                depParentTargetRepositoryKind);

        IOrderedMap[] data = new IOrderedMap[] { inputParameters };
        identityMappingDatabaseInserter.connect();
        identityMappingDatabaseInserter.deliver(data);
        // identityMappingDatabaseInserter.disconnect();
    }

    private void createMapping(Element element, String sourceArtifactId,
            String sourceRepositoryId, String sourceRepositoryKind,
            String sourceSystemId, String sourceSystemKind,
            String targetArtifactId, String targetRepositoryId,
            String targetRepositoryKind, String targetSystemId,
            String targetSystemKind, java.sql.Timestamp sourceTime,
            String sourceArtifactVersion, java.sql.Timestamp targetTime,
            String targetArtifactVersion, String artifactType,
            String depParentSourceArtifactId,
            String depParentSourceRepositoryId,
            String depParentSourceRepositoryKind,
            String depParentTargetArtifactId,
            String depParentTargetRepositoryId,
            String depParentTargetRepositoryKind,
            String depChildSourceArtifactId, String depChildSourceRepositoryId,
            String depChildSourceRepositoryKind,
            String depChildTargetArtifactId, String depChildTargetRepositoryId,
            String depChildTargetRepositoryKind) {

        OrderedHashMap identityMappingTableRow = lookupIdentiyMappingRow(
                element, sourceArtifactId, sourceSystemId, sourceRepositoryId,
                targetSystemId, targetRepositoryId, artifactType);
        String targetArtifactIdFromTable = null;
        if (identityMappingTableRow != null) {
            targetArtifactIdFromTable = (String) identityMappingTableRow.get(0);
        }
        if (targetArtifactIdFromTable == null) {
            this.createIdentityMapping(sourceSystemId, sourceRepositoryId,
                    targetSystemId, targetRepositoryId, sourceSystemKind,
                    sourceRepositoryKind, targetSystemKind,
                    targetRepositoryKind, sourceArtifactId, targetArtifactId,
                    sourceTime, targetTime, sourceArtifactVersion,
                    targetArtifactVersion, artifactType,
                    depParentSourceArtifactId, depParentSourceRepositoryId,
                    depParentSourceRepositoryKind, depParentTargetArtifactId,
                    depParentTargetRepositoryId, depParentTargetRepositoryKind,
                    depChildSourceArtifactId, depChildSourceRepositoryId,
                    depChildSourceRepositoryKind, depChildTargetArtifactId,
                    depChildTargetRepositoryId, depChildTargetRepositoryKind);
        } else {
            if (identityMappingTableRow.size() > 3) {
                //Timestamp sourceLastModificationTimeFromTable = (Timestamp) identityMappingTableRow.get(1);
                long sourceArtifactVersionFromTable = parseVersionNumber(identityMappingTableRow
                        .get(2));
                long targetArtifactVersionFromTable = parseVersionNumber(identityMappingTableRow
                        .get(3));
                long sourceArtifactVersionConverted = parseVersionNumber(sourceArtifactVersion);
                long targetArtifactVersionConverted = parseVersionNumber(targetArtifactVersion);

                if (sourceArtifactVersionConverted < sourceArtifactVersionFromTable) {
                    log.debug("overriding incoming source artifact version ("
                            + sourceArtifactVersionConverted
                            + ") with version from id-mapping-table ("
                            + sourceArtifactVersionFromTable + ")");
                    sourceArtifactVersion = Long
                            .toString(sourceArtifactVersionFromTable);
                    //sourceTime = sourceLastModificationTimeFromTable;
                }

                if (targetArtifactVersionConverted < targetArtifactVersionFromTable) {
                    log.debug("overriding incoming target artifact version ("
                            + targetArtifactVersionConverted
                            + ") with version from id-mapping-table ("
                            + targetArtifactVersionFromTable + ")");
                    targetArtifactVersion = Long
                            .toString(targetArtifactVersionFromTable);
                }
            }
            this.updateIdentityMapping(sourceSystemId, sourceRepositoryId,
                    targetSystemId, targetRepositoryId, sourceArtifactId,
                    sourceTime, targetTime, sourceArtifactVersion,
                    targetArtifactVersion, artifactType);
            log.debug("Mapping already exists for source artifact id "
                    + sourceArtifactId + " target artifact id "
                    + targetArtifactId + " for repository info "
                    + sourceArtifactId + "+" + sourceSystemId + "+"
                    + sourceRepositoryId + "+" + targetSystemId);
        }

    }

    private OrderedHashMap lookupIdentiyMappingRow(Element element,
            String sourceArtifactId, String sourceSystemId,
            String sourceRepositoryId, String targetSystemId,
            String targetRepositoryId, String artifactType) {
        OrderedHashMap result = null;
        IOrderedMap inputParameters = new OrderedHashMap();

        inputParameters.add(sourceSystemId);
        inputParameters.add(sourceRepositoryId);
        inputParameters.add(targetSystemId);
        inputParameters.add(targetRepositoryId);
        inputParameters.add(sourceArtifactId);
        inputParameters.add(artifactType);

        identityMappingDatabaseReader.connect();
        Object[] resultSet = identityMappingDatabaseReader.next(
                inputParameters, 1000);
        //identityMappingDatabaseReader.disconnect();

        if (resultSet == null || resultSet.length == 0) {
            result = null;
        } else if (resultSet.length == 1) {
            if (resultSet[0] instanceof OrderedHashMap) {
                result = (OrderedHashMap) resultSet[0];
                if (result.size() > 0) {
                    return result;
                } else {
                    String cause = "Seems as if the SQL statement for identityMappingDatabase reader does not return values.";
                    XPathUtils
                            .addAttribute(
                                    element,
                                    GenericArtifactHelper.ERROR_CODE,
                                    GenericArtifact.ERROR_INTERNAL_DATABASE_TABLE_CORRUPT);
                    log.error(cause);
                    throw new CCFRuntimeException(cause);
                }
            } else {
                String cause = "SQL query on identity mapping table did not return data in correct format!";
                XPathUtils.addAttribute(element,
                        GenericArtifactHelper.ERROR_CODE,
                        GenericArtifact.ERROR_INTERNAL_DATABASE_TABLE_CORRUPT);
                log.error(cause);
                throw new CCFRuntimeException(cause);
            }
        } else {
            String cause = "There is more than one mapping for the combination "
                    + sourceArtifactId
                    + "-"
                    + sourceRepositoryId
                    + "-"
                    + sourceSystemId
                    + targetRepositoryId
                    + "-"
                    + targetSystemId + " in the identity mapping table.";
            XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE,
                    GenericArtifact.ERROR_INTERNAL_DATABASE_TABLE_CORRUPT);
            log.error(cause);
            throw new CCFRuntimeException(cause);
        }
        return result;
    }

    /**
     * Tries to convert the input to a number. If input is null or
     * input.toString() cannot be parsed, returns -2, because -1 is already in
     * use to indicate systems that don't support version control.
     * 
     * @param input
     * @return the parsed value of the input, or -2 if input is null or its
     *         string representation cannot be parsed.
     */
    private long parseVersionNumber(Object input) {
        if (input == null)
            return -2;
        try {
            return Long.parseLong(input.toString());
        } catch (NumberFormatException nfe) {
            return -2;
        }
    }

    private void updateIdentityMapping(String sourceSystemId,
            String sourceRepositoryId, String targetSystemId,
            String targetRepositoryId, String sourceArtifactId,
            java.sql.Timestamp sourceTime, java.sql.Timestamp targetTime,
            String sourceArtifactVersion, String targetArtifactVersion,
            String artifactType) {
        IOrderedMap inputParameters = new OrderedHashMap();

        inputParameters.add(0, SOURCE_LAST_MODIFICATION_TIME, sourceTime);
        inputParameters.add(1, TARGET_LAST_MODIFICATION_TIME, targetTime);
        inputParameters.add(2, SOURCE_ARTIFACT_VERSION, sourceArtifactVersion);
        inputParameters.add(3, TARGET_ARTIFACT_VERSION, targetArtifactVersion);
        inputParameters.add(4, SOURCE_SYSTEM_ID, sourceSystemId);
        inputParameters.add(5, SOURCE_REPOSITORY_ID, sourceRepositoryId);
        inputParameters.add(6, TARGET_SYSTEM_ID, targetSystemId);
        inputParameters.add(7, TARGET_REPOSITORY_ID, targetRepositoryId);
        inputParameters.add(8, SOURCE_ARTIFACT_ID, sourceArtifactId);
        inputParameters.add(9, ARTIFACT_TYPE, artifactType);

        IOrderedMap[] params = new IOrderedMap[] { inputParameters };
        identityMappingDatabaseUpdater.connect();
        identityMappingDatabaseUpdater.deliver(params);
        // identityMappingDatabaseUpdater.disconnect();
    }

    private void updateSynchronizationStatusTable(Object data,
            String sourceArtifactId, String sourceArtifactVersion,
            String sourceSystemId, String sourceRepositoryId,
            String targetSystemId, String targetRepositoryId,
            java.sql.Timestamp sourceTime) {
        IOrderedMap inputParameters = new OrderedHashMap();
        inputParameters.add(0, "LAST_SOURCE_ARTIFACT_MODIFICATION_DATE",
                sourceTime);
        inputParameters.add(1, "LAST_SOURCE_ARTIFACT_VERSION",
                sourceArtifactVersion);
        inputParameters.add(2, "LAST_SOURCE_ARTIFACT_ID", sourceArtifactId);
        inputParameters.add(3, SOURCE_SYSTEM_ID, sourceSystemId);
        inputParameters.add(4, SOURCE_REPOSITORY_ID, sourceRepositoryId);
        inputParameters.add(5, TARGET_SYSTEM_ID, targetSystemId);
        inputParameters.add(6, TARGET_REPOSITORY_ID, targetRepositoryId);

        IOrderedMap[] params = new IOrderedMap[] { inputParameters };
        synchronizationStatusDatabaseUpdater.connect();
        synchronizationStatusDatabaseUpdater.deliver(params);
        //synchronizationStatusDatabaseUpdater.disconnect();
    }

}
