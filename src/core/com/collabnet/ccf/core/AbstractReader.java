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

package com.collabnet.ccf.core;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.Component;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;

/**
 * AbstractReader provides the abstraction of shipping Generic Artifacts through
 * the wiring components. It provides three methods for the plugin developers to
 * implement. The plugin developer should implement these methods by extending
 * the AbstractReader.
 * 
 * The AbstractReader then gets the changed artifacts from the implemented
 * methods and sorts them according to their last modified date and sends them
 * across. It sends the artifacts one per Open Adaptor cycle.
 * 
 * The process method implements the streaming logic.
 * 
 * @author madhusuthanan (madhusuthanan@collab.net)
 * @author Johannes Nicolai (jnicolai@collab.net)
 * 
 */
public abstract class AbstractReader<T> extends Component implements IDataProcessor {
    private static final String               FIELD_MAPPING_LANDSCAPE_DIRECTORY                       = "landscape";
    private static final String               FIELD_MAPPING_CORE_DIRECTORY                            = "core";
    private static final String               TARGET_SYSTEM_ENCODING                                  = "//TARGET_SYSTEM_ENCODING | //target_system_encoding";
    private static final String               SOURCE_SYSTEM_ENCODING                                  = "//SOURCE_SYSTEM_ENCODING | //source_system_encoding";
    private static final String               TARGET_SYSTEM_TIMEZONE                                  = "//TARGET_SYSTEM_TIMEZONE | //target_system_timezone";
    private static final String               SOURCE_SYSTEM_TIMEZONE                                  = "//SOURCE_SYSTEM_TIMEZONE | //source_system_timezone";
    private static final String               CONFLICT_RESOLUTION_PRIORITY                            = "//CONFLICT_RESOLUTION_PRIORITY | //conflict_resolution_priority";
    private static final String               TARGET_SYSTEM_KIND                                      = "//TARGET_SYSTEM_KIND | //target_system_kind";
    private static final String               TARGET_SYSTEM_ID                                        = "//TARGET_SYSTEM_ID | //target_system_id";
    private static final String               TARGET_REPOSITORY_KIND                                  = "//TARGET_REPOSITORY_KIND | //target_repository_kind";
    private static final String               TARGET_REPOSITORY_ID                                    = "//TARGET_REPOSITORY_ID | //target_repository_id";
    private static final String               SOURCE_SYSTEM_KIND                                      = "//SOURCE_SYSTEM_KIND | //source_system_kind";
    private static final String               SOURCE_SYSTEM_ID                                        = "//SOURCE_SYSTEM_ID | //source_system_id";
    private static final String               SOURCE_REPOSITORY_KIND                                  = "//SOURCE_REPOSITORY_KIND | //source_repository_kind";
    private static final String               SOURCE_REPOSITORY_ID                                    = "//SOURCE_REPOSITORY_ID | //source_repository_id";
    private static final String               LAST_SOURCE_ARTIFACT_ID                                 = "//LAST_SOURCE_ARTIFACT_ID | //last_source_artifact_id";
    private static final String               LAST_SOURCE_ARTIFACT_VERSION                            = "//LAST_SOURCE_ARTIFACT_VERSION | //last_source_artifact_version";
    private static final String               LAST_SOURCE_ARTIFACT_MODIFICATION_DATE                  = "//LAST_SOURCE_ARTIFACT_MODIFICATION_DATE | //last_source_artifact_modification_date";
    private static final String               FIELD_MAPPING_SCOPE                                     = "//FIELD_MAPPING_SCOPE | //field_mapping_scope";
    private static final String               FIELD_MAPPING_KIND                                      = "//FIELD_MAPPING_KIND | //field_mapping_kind";
    private static final String               FIELD_MAPPING_NAME                                      = "//FIELD_MAPPING_NAME | //field_mapping_name";
    private static final String               REPOSITORY_MAPPING_DIRECTION_ID                         = "//REPOSITORY_MAPPING_DIRECTION_ID | //repository_mapping_direction_id";
    private static final String               REPOSITORY_MAPPING_ID                                   = "//REPOSITORY_MAPPING_ID | //repository_mapping_id";
    private static final String               EXTERNAL_APP_LINK_ID                                    = "//EXTERNAL_APP_LINK_ID | //external_app_link_id";
    private static final String               REPOSITORY_MAPPING_DIRECTION_DIRECTION                  = "//REPOSITORY_MAPPING_DIRECTION_DIRECTION | //repository_mapping_direction_direction";
    private static final String               ARTIFACT_LAST_MODIFIED_DATE_ELEMENT                     = "ARTIFACT_LAST_MODIFIED_DATE";
    private static final String               ARTIFACT_LAST_MODIFIED_VERSION_ELEMENT                  = "ARTIFACT_LAST_MODIFIED_VERSION";
    private static final String               ARTIFACT_LAST_MODIFIED_DATE                             = "//ARTIFACT_LAST_MODIFIED_DATE | //artifact_last_modified_date";
    private static final String               ARTIFACT_LAST_MODIFIED_VERSION                          = "//ARTIFACT_LAST_MODIFIED_VERSION | //artifact_last_modified_version";

    private static final Log                  log                                                     = LogFactory
                                                                                                              .getLog(AbstractReader.class);
    private HashMap<String, RepositoryRecord> repositoryRecordHashMap                                 = null;
    private ArrayList<RepositoryRecord>       repositorySynchronizationWaitingList                    = null;
    private HashSet<String>                   repositoryRecordsInRepositorySynchronizationWaitingList = null;
    private long                              sleepInterval                                           = -1;
    private boolean                           shipAttachments                                         = true;
    private boolean                           shipAttachmentsWithArtifact                             = false;
    private boolean                           includeFieldMetaData                                    = false;
    private Comparator<GenericArtifact>       genericArtifactComparator                               = null;
    public static final long                  DEFAULT_MAX_ATTACHMENT_SIZE_PER_ARTIFACT                = 10 * 1024 * 1024;
    private long                              maxAttachmentSizePerArtifact                            = DEFAULT_MAX_ATTACHMENT_SIZE_PER_ARTIFACT;
    private ConnectionManager<T>              connectionManager;
    private static final String               ARTIFACT_TYPE_PLAIN_ARTIFACT                            = "plainArtifact";

    /**
     * This variable is set to false until we get the first reoccuring
     * synchronization status record which means that we have processed the
     * initial synchronization status records
     */
    private boolean                           connectorHasReadAllInitialSynchronizationStatusRecords  = false;

    /**
     * Setting this variable to true causes the CCF to shutdown after there are
     * no more out of date artifacts of all repositories to be synchronized at
     * this time. This property is useful for scenarios that just like to take a
     * snapshot of a repository.
     */
    private boolean                           shutdownCCFAfterInitialSync                             = false;

    /**
     * If the restart connector variable is set to true, all readers will begin
     * to flush their buffers and exit with a special error code (42) that will
     * cause service wrapper to restart the connector.
     */
    private static boolean                    restartConnector                                        = false;

    /**
     * If this property is set to something else but null, artifacts quarantined
     * by a component with this name (typically the entity service) will get
     * special treatment during artifact replay: The XSLTProcessor will
     * transform the payload again.
     */
    private String                            nameOfEntityService                                     = null;

    /**
     * If the shutDownConnector variable is set, this will cause service wrapper
     * to flush all the buffers and signal the shutdown hook thread that it is
     * ready to exit
     */
    private static boolean                    shutDownConnector                                       = false;

    // used for the polling hospital
    private JDBCReadConnector                 hospitalDatabaseReader                                  = null;

    /**
     * This field determines whether the reader component is used within a CCF
     * 2.x process Defaults to false
     */
    private boolean                           isCCF2xProcess                                          = false;

    /**
     * This (optional) property is used to retrieve the source/target artifact
     * information from the identity mapping table.syncinfo of the artifact is
     * updated with the artifact last modified time and version fetched from the
     * identity mapping.If this property is not set, no lookup will be done in
     * the identity mapping and Sync will be based on repository
     * lastmodifiedtimestamp or version where the leftover comments/attachments
     * are not shipped during stale artifact scenario.If this property is set
     * Sync is based on Artifact last modified timestamp or last modified
     * version and resolves the leftover comments/attachments during stale
     * artifact scenario.
     */
    private JDBCReadConnector                 identityMappingDatabaseReader                           = null;

    private static final int                  RESTART_EXIT_CODE                                       = 42;

    /**
     * This field contains the date when the CCF was started
     */
    private Date                              startedDate                                             = new Date();

    /**
     * This property denotes after how many seconds the CCF will restart
     * automatically
     */
    private int                               autoRestartPeriod                                       = -1;

    /**
     * Determines whether concrete readers can assume that this scenario is just
     * used for bulk imports, e. g. no frequent artifact data change is going to
     * happen
     */
    private boolean                           isBulkImport                                            = false;

    public AbstractReader() {
        super();
        init();
    }

    /**
     * Queries the source repository for any attachment changes for the given
     * artifactId and returns the changed attachments in a GenericArtifact
     * object. If there are no attachments changed an empty list is returned.
     * 
     * @param syncInfo
     * @param artifactData
     * @return
     */
    public abstract List<GenericArtifact> getArtifactAttachments(
            Document syncInfo, GenericArtifact artifactData);

    /**
     * Returns the artifact data for the artifactId in a GenericArtifact object.
     * If the reader can return the artifact change history it should be
     * returned in the list. If the reader doesn't have the capability of
     * returning the artifact change history it should return a list that
     * contains the latest artifact data in a single GenericArtifact object
     * added to the list.
     * 
     * @param syncInfo
     * @param artifactId
     * @return
     */
    public abstract GenericArtifact getArtifactData(Document syncInfo,
            String artifactId);

    /**
     * Sub classes should implement this method. The implemented method should
     * get all the dependent artifacts of the artifact with the artifact id
     * artifactId and convert them into Generic Artifact object and return.
     * 
     * If there are no dependent artifacts to be returned the implemented method
     * should return an empty List. It should not return null.
     * 
     * @param syncInfo
     *            - The synchronization info against which the changed artifacts
     *            to be fetched
     * @param artifactId
     *            - The id of the artifact whose dependent artifacts should be
     *            retrieved and returned.
     * @return - A List of dependent artifacts of this artifactId that are
     *         changed
     */
    public abstract List<GenericArtifact> getArtifactDependencies(
            Document syncInfo, String artifactId);

    public String getArtifactLastModifiedTime(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(ARTIFACT_LAST_MODIFIED_DATE);
        if (node == null)
            return null;
        String dbTime = node.getText();
        if (!StringUtils.isEmpty(dbTime)) {
            java.sql.Timestamp ts = java.sql.Timestamp.valueOf(dbTime);
            long time = ts.getTime();
            Date date = new Date(time);
            return DateUtil.format(date);
        }
        return null;
    }

    public String getArtifactLastModifiedVersion(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(ARTIFACT_LAST_MODIFIED_VERSION);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * Returns the number of seconds, after the CCF will exit with exit code 42
     * and will be restarted by the ServiceWrapper. If the return value is
     * negative, it will never exit/restarted.
     * 
     * @return the autoRestartPeriod
     */
    public int getAutoRestartPeriod() {
        return autoRestartPeriod;
    }

    /**
     * Returns a list of changed artifacts' ids.
     * 
     * @param syncInfo
     * @return
     */
    public abstract List<ArtifactState> getChangedArtifacts(Document syncInfo);

    /**
     * Extracts and returns the conflictResolutionPriority for the source
     * repository.
     * 
     * @param syncInfo
     * @return
     */
    public String getConflictResolutionPriority(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(CONFLICT_RESOLUTION_PRIORITY);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * Get the connection manager. The connection manager is responsible to
     * manage (create, close, pool) the connections from type T. Furthermore, it
     * contains timeout settings and the settings for the retry code in case of
     * network timeout and session fault related errors.
     * 
     * @return the connection manager object
     */
    public ConnectionManager<T> getConnectionManager() {
        return connectionManager;
    }

    /**
     * Returns the currently processed sync info of the synchronization status
     * record in question
     * 
     * @param sourceSystemId
     *            source system id
     * @param sourceRepositoryId
     *            source repository id
     * @param targetSystemId
     *            target system id
     * @param targetRepositoryId
     *            target repository id
     * @return synch info for record in question
     */
    public Document getCurrentSynchInfo(String sourceSystemId,
            String sourceRepositoryId, String targetSystemId,
            String targetRepositoryId) {
        String repositoryKey = sourceSystemId + ":" + sourceRepositoryId + ":"
                + targetSystemId + ":" + targetRepositoryId;
        RepositoryRecord record = repositoryRecordHashMap.get(repositoryKey);
        if (record == null) {
            return null;
        } else {
            return record.getSyncInfo();
        }
    }

    /**
     * Gets the (optional) data base reader that is used to poll quarantined
     * artifacts ready for replay If this property is not set, no lookup will be
     * done in the hospital.
     * 
     * @return
     */
    public JDBCReadConnector getHospitalDatabaseReader() {
        return hospitalDatabaseReader;
    }

    /**
     * Gets the (optional) data base reader that is used to retrieve the
     * source/target artifact information from the identity mapping table.If
     * this property is not set, no lookup will be done in the identity mapping.
     * 
     * @return
     */
    public JDBCReadConnector getIdentityMappingDatabaseReader() {
        return identityMappingDatabaseReader;
    }

    /**
     * Extracts and returns the last modified time of the artifact that was
     * sync-ed last.
     * 
     * @param syncInfo
     * @return
     */
    public String getLastSourceArtifactModificationDate(Document syncInfo) {
        // LAST_SOURCE_ARTIFACT_MODIFICATION_DATE
        Node node = syncInfo
                .selectSingleNode(LAST_SOURCE_ARTIFACT_MODIFICATION_DATE);
        if (node == null)
            return null;
        String dbTime = node.getText();
        if (!StringUtils.isEmpty(dbTime)) {
            java.sql.Timestamp ts = java.sql.Timestamp.valueOf(dbTime);
            long time = ts.getTime();
            Date date = new Date(time);
            return DateUtil.format(date);
        }
        return null;
    }

    /**
     * Returns the maximum attachment size that will be shipped for an artifact.
     * The max attachment size is configured in bytes.
     * 
     * @return - The maximum attachment size per artifact in bytes
     */
    public long getMaxAttachmentSizePerArtifact() {
        return maxAttachmentSizePerArtifact;
    }

    /**
     * Returns the name of the component that should be recognized to be the
     * entity service during artifact replay Only if the originating component
     * of a quarantined artifact record matches this name, the payload will be
     * transformed again
     * 
     * @return name of the component that should be treated as the entity
     *         service component (null by default)
     */
    public String getNameOfEntityService() {
        return nameOfEntityService;
    }

    /**
     * Returns the number of artifacts waiting to be synchronized per
     * synchronization status record
     * 
     * @param sourceSystemId
     *            source system id
     * @param sourceRepositoryId
     *            source repository id
     * @param targetSystemId
     *            target system id
     * @param targetRepositoryId
     *            target repository id
     * @return number of artifacts waiting for specific synchronization status
     *         record
     */
    public String getNumberOfWaitingArtifacts(String sourceSystemId,
            String sourceRepositoryId, String targetSystemId,
            String targetRepositoryId) {
        String repositoryKey = sourceSystemId + ":" + sourceRepositoryId + ":"
                + targetSystemId + ":" + targetRepositoryId;
        RepositoryRecord record = repositoryRecordHashMap.get(repositoryKey);
        if (record == null) {
            return "Not in queue at the moment.";
        } else {
            List<ArtifactState> list = record.getArtifactsToBeReadList();
            if (list == null) {
                return "0";
            } else {
                return Integer.toString(list.size());
            }
        }
    }

    /**
     * Returns the number of artifacts waiting to be synchronized for all target
     * systems connected to the source repository
     * 
     * @param sourceSystemId
     *            source system id
     * @param sourceRepositoryId
     *            source repository id
     * @return number of artifacts waiting for specific synchronization status
     *         record
     */
    public int getNumberOfWaitingArtifactsForAllTargetSystems(
            String sourceSystemId, String sourceRepositoryId) {
        String repositoryKey = sourceSystemId + ":" + sourceRepositoryId + ":";
        Set<String> keys = repositoryRecordHashMap.keySet();
        int number = 0;
        for (String key : keys) {
            if (key.startsWith(repositoryKey)) {
                RepositoryRecord record = repositoryRecordHashMap.get(key);
                if (record != null) {
                    List<ArtifactState> list = record
                            .getArtifactsToBeReadList();
                    if (list != null) {
                        number += list.size();
                    }
                    List<GenericArtifact> shippedList = record
                            .getArtifactsToBeShippedList();
                    if (shippedList != null) {
                        number += shippedList.size();
                    }
                }
            }
        }
        return number;
    }

    /**
     * Returns the configured sleep interval in milliseconds
     * 
     * @return
     */
    public long getSleepInterval() {
        return sleepInterval;
    }

    /**
     * Extracts and returns the Source repository id from the sync info.
     * 
     * @param syncInfo
     *            - The incoming sync info for the repository.
     * @return - Returns the repository id from this sync info.
     */
    public String getSourceRepositoryId(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(SOURCE_REPOSITORY_ID);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * Extracts and returns the Source repository kind from the sync info.
     * 
     * @param syncInfo
     *            - The incoming sync info for this repository.
     * @return - Returns the repository kind from the incoming sync info of this
     *         repository If this is a CCF 2.x process, it will return the
     *         directory of the XSLT file(s) to be used for the field mapping
     */
    public String getSourceRepositoryKind(Document syncInfo) {
        if (isCCF2xProcess()) {
            // first figure out the scope
            String fieldMappingScopeString = getFieldMappingScope(syncInfo);
            String xsltDirectory = "";
            try {
                FieldMappingScope fieldMappingScope = FieldMappingScope
                        .valueOf(fieldMappingScopeString);
                Directions direction = Directions.values()[Integer
                        .parseInt(getRepositoryMappingDirectionDirection(syncInfo))];
                switch (fieldMappingScope) {
                    case CCF_CORE: {
                        xsltDirectory = direction.name() + File.separatorChar
                                + FIELD_MAPPING_CORE_DIRECTORY
                                + File.separatorChar
                                + getFieldMappingName(syncInfo)
                                + File.separatorChar;
                        break;
                    }
                    case EXTERNAL_APP: {
                        xsltDirectory = direction.name() + File.separatorChar
                                + getExternalAppLinkId(syncInfo)
                                + File.separatorChar
                                + getFieldMappingName(syncInfo)
                                + File.separatorChar;
                        break;
                    }
                    case LANDSCAPE: {
                        xsltDirectory = direction.name() + File.separatorChar
                                + FIELD_MAPPING_LANDSCAPE_DIRECTORY
                                + File.separatorChar
                                + getFieldMappingName(syncInfo)
                                + File.separatorChar;
                        break;
                    }
                    case REPOSITORY_MAPPING_DIRECTION: {
                        xsltDirectory = direction.name() + File.separatorChar
                                + getRepositoryMappingDirectionId(syncInfo)
                                + File.separatorChar
                                + getFieldMappingName(syncInfo)
                                + File.separatorChar;
                        break;
                    }
                }
            } catch (Exception e) {
                log.warn("Unknown fieldMappingScope " + fieldMappingScopeString
                        + " or direction for synch info " + syncInfo.asXML());
            }
            return xsltDirectory;
        }
        Node node = syncInfo.selectSingleNode(SOURCE_REPOSITORY_KIND);
        if (node == null)
            return null;
        return node.getText();
    }

    public String getSourceSystemEncoding(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(SOURCE_SYSTEM_ENCODING);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * Extracts and returns the Source system id from the sync info.
     * 
     * @param syncInfo
     *            - the incoming sync info for this repository
     * @return - The source system id for this repository.
     */
    public String getSourceSystemId(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(SOURCE_SYSTEM_ID);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * Extracts and returns the source system kind from the sync info.
     * 
     * @param syncInfo
     *            - The incoming sync info for this repository.
     * @return - The source system kind for this repository.
     * 
     *         If this is a CCF 2.x process, this attribute is populated with
     *         the repository mapping direction id
     */
    public String getSourceSystemKind(Document syncInfo) {
        if (isCCF2xProcess()) {
            return getRepositoryMappingDirectionId(syncInfo);
        }
        Node node = syncInfo.selectSingleNode(SOURCE_SYSTEM_KIND);
        if (node == null)
            return null;
        return node.getText();
    }

    public String getSourceSystemTimezone(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(SOURCE_SYSTEM_TIMEZONE);
        if (node == null)
            return null;
        return convertTimeZoneFromCCF2xDataBaseFormat(node.getText());
    }

    /**
     * Extracts and returns the target repository id from the sync info.
     * 
     * @param syncInfo
     *            - The incoming sync info for this repository.
     * @return - the target repository id that is mapped to this repository.
     */
    public String getTargetRepositoryId(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(TARGET_REPOSITORY_ID);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * Extracts and returns the target repository kind from the sync info.
     * 
     * @param syncInfo
     *            - The incoming sync info for this repository.
     * @return - The target repository id extracted from this repository info.
     * 
     *         If this is a CCF 2.x process, it will return the kind of field
     *         mapping to be applied
     */
    public String getTargetRepositoryKind(Document syncInfo) {
        if (isCCF2xProcess()) {
            return getFieldMappingKind(syncInfo);
        }
        Node node = syncInfo.selectSingleNode(TARGET_REPOSITORY_KIND);
        if (node == null)
            return null;
        return node.getText();
    }

    public String getTargetSystemEncoding(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(TARGET_SYSTEM_ENCODING);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * Extracts and returns the Target system id from the sync info.
     * 
     * @param syncInfo
     *            - The incoming sync info for this repository.
     * @return - The target system id from the sync info
     */
    public String getTargetSystemId(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(TARGET_SYSTEM_ID);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * Extracts and returns the target system kind from the sync info.
     * 
     * @param syncInfo
     *            - The incoming sync info for this repository.
     * @return - the target system kind from this repository sync info.
     * 
     *         If this is a CCF 2.x process, this method returns the repository
     *         mapping id
     */
    public String getTargetSystemKind(Document syncInfo) {
        if (isCCF2xProcess()) {
            return getRepositoryMappingId(syncInfo);
        }
        Node node = syncInfo.selectSingleNode(TARGET_SYSTEM_KIND);
        if (node == null)
            return null;
        return node.getText();
    }

    public String getTargetSystemTimezone(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(TARGET_SYSTEM_TIMEZONE);
        if (node == null)
            return null;
        return convertTimeZoneFromCCF2xDataBaseFormat(node.getText());
    }

    public boolean handleException(Throwable rootCause,
            ConnectionManager<T> connectionManager) {
        return false;
    }

    /**
     * Returns whether this scenario is just used for bulk import and no
     * frequent artifact changes are currently done on the repository. This
     * allows to do certain query optimizations in the concrete reader classes.
     * 
     * @return false if no query optimizations can be done (default), true if
     *         query optimizations can be done since repository data does not
     *         change frequently
     */
    public boolean isBulkImport() {
        return isBulkImport;
    }

    /**
     * Determines whether this reader component is used within a CCF 2.x process
     * Defaults to false
     * 
     * @return
     */
    public boolean isCCF2xProcess() {
        return isCCF2xProcess;
    }

    /**
     * Returns the flag that denotes if the Reader should include field meta
     * data for the artifacts that are shipped.
     * 
     * @return - true if the field meta data should be shipped - false if the
     *         field meta data need not be shipped by the Reader.
     */
    public boolean isIncludeFieldMetaData() {
        return includeFieldMetaData;
    }

    /**
     * Returns whether the attachments of the artifact should be shipped by the
     * Reader component.
     * 
     * @return - true if the attachments will be shipped. - false if the
     *         attachments won't be shipped.
     */
    public boolean isShipAttachments() {
        return shipAttachments;
    }

    public boolean isShipAttachmentsWithArtifact() {
        return shipAttachmentsWithArtifact;
    }

    /**
     * If this property is set to true, the CCF shuts down after there are no
     * more out of date artifacts of all repositories to be synchronized at this
     * time. This property is useful for scenarios that just like to take a
     * snapshot of a repository. The default value is false (connector will not
     * stop)
     * 
     * @return true if connector stops after initial export
     */
    public boolean isShutdownCCFAfterInitialSync() {
        return shutdownCCFAfterInitialSync;
    }

    /**
     * The process method queues the sync info documents from the sync info
     * readers. It then takes each repository one by one and asks the repository
     * reader (which is a sub class to this AbstractReader) if there are any
     * changed artifacts. If the reader gives a list of artifacts that are
     * changed then the AbstractReader requests the repository reader to get the
     * data of the changed artifacts one by one along with the dependent
     * artifact data such as attachments and dependent artifacts in the Generic
     * Artifact xml format. On getting these data the abstract reader emits the
     * artifacts one by one to the next component in the pipeline. It does this
     * for each repository alternatively so that all the repositories get equal
     * chance to ship their artifacts. If there are no artifacts to be shipped
     * for all of the repositories configured then the AbstractReader pauses the
     * processing for sleepInterval milliseconds.
     */
    public Object[] process(Object data) {
        Document syncInfoIn = null;
        if (data instanceof Document) {
            syncInfoIn = (Document) data;
        } else {
            return null;
        }

        if (getAutoRestartPeriod() > 0) {
            if (new Date().getTime() - startedDate.getTime() > getAutoRestartPeriod()) {
                log.debug("Preparing to restart CCF, flushing buffers ...");
                setRestartConnector(true);
            }
        }

        String sourceRepositoryId = this.getSourceRepositoryId(syncInfoIn);
        String sourceSystemId = this.getSourceSystemId(syncInfoIn);
        String targetSystemId = this.getTargetSystemId(syncInfoIn);
        String targetRepositoryId = this.getTargetRepositoryId(syncInfoIn);
        String repositoryKey = sourceSystemId + ":" + sourceRepositoryId + ":"
                + targetSystemId + ":" + targetRepositoryId;
        log.debug("Received the SyncInfo for repository with source system id"
                + sourceSystemId + ", source repository id "
                + sourceRepositoryId + ", target systemId " + targetSystemId
                + " and target repository id " + targetRepositoryId);
        RepositoryRecord record = repositoryRecordHashMap.get(repositoryKey);
        if (record == null) {
            log.debug("No RepositoryRecord available for source system id"
                    + sourceSystemId + ", source repository id "
                    + sourceRepositoryId + ", target systemId "
                    + targetSystemId + " and target repository id "
                    + targetRepositoryId + "... Creating one and registering");
            record = new RepositoryRecord(repositoryKey, syncInfoIn);
            repositoryRecordHashMap.put(repositoryKey, record);
        } else {
            connectorHasReadAllInitialSynchronizationStatusRecords = true;
            record.setNewSyncInfo(syncInfoIn);
        }
        if (!repositoryRecordsInRepositorySynchronizationWaitingList
                .contains(repositoryKey)) {
            log.debug(repositoryKey + " is not on the waiting list. Adding....");
            repositorySynchronizationWaitingList.add(0, record);
            repositoryRecordsInRepositorySynchronizationWaitingList
                    .add(repositoryKey);
        }
        RepositoryRecord currentRecord = null;
        while (!repositorySynchronizationWaitingList.isEmpty()) {
            currentRecord = repositorySynchronizationWaitingList.get(0);
            log.debug("Processing the current repository "
                    + currentRecord.getRepositoryId() + " record");
            // immediately move record to tail so that exceptions do not prevent
            // other repositories
            // from being synched
            moveToTail(currentRecord);
            Document syncInfo = currentRecord.getSyncInfo();
            // RepositoryRecord movedRecord =
            // repositorySynchronizationWaitingList.remove(0);
            // repositorySynchronizationWaitingList.add(movedRecord);
            List<GenericArtifact> artifactsToBeShippedList = currentRecord
                    .getArtifactsToBeShippedList();
            List<ArtifactState> artifactsToBeReadList = currentRecord
                    .getArtifactsToBeReadList();
            if (!artifactsToBeShippedList.isEmpty()) {
                log.debug("There are " + artifactsToBeShippedList.size()
                        + " artifacts to be shipped.");
                GenericArtifact genericArtifact = artifactsToBeShippedList
                        .remove(0);
                // if(artifactsToBeShippedList.isEmpty()){
                // repositorySynchronizationWaitingList.remove(currentRecord);
                // repositorySynchronizationWaitingList.add(currentRecord);
                // }
                String artifactId = genericArtifact.getSourceArtifactId();
                try {
                    String conflictResolution = this
                            .getConflictResolutionPriority(syncInfo);
                    genericArtifact
                            .setConflictResolutionPriority(conflictResolution);
                    genericArtifact.setSourceSystemTimezone(this
                            .getSourceSystemTimezone(syncInfo));
                    genericArtifact.setTargetSystemTimezone(this
                            .getTargetSystemTimezone(syncInfo));
                    // genericArtifact.setSourceSystemEncoding(this.
                    // getSourceSystemEncoding(syncInfo));
                    // genericArtifact.setTargetSystemEncoding(this.
                    // getTargetSystemEncoding(syncInfo));
                    Document returnDoc = GenericArtifactHelper
                            .createGenericArtifactXMLDocument(genericArtifact);
                    Object[] returnObjects = new Object[] { returnDoc };
                    return returnObjects;
                } catch (GenericArtifactParsingException e) {
                    String cause = "Could not parse the artifact for "
                            + artifactId;
                    log.error(cause, e);
                    genericArtifact
                            .setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
                    throw new CCFRuntimeException(cause, e);
                }
            } else if (artifactsToBeReadList.isEmpty()
                    && !isRestartConnector()
                    && !isShutDownConnector()
                    && !(isShutdownCCFAfterInitialSync() && connectorHasReadAllInitialSynchronizationStatusRecords)) {
                // our buffer is empty, so we can ask for new synch info again
                currentRecord.readyForNewSynchInfo();
                if (!currentRecord.isNewSyncInfoReceived()) {
                    log.debug("Have to wait until sync info for "
                            + currentRecord.getRepositoryId()
                            + " is up to date again ...");
                    return new Object[] {};
                }
                log.debug("There are no artifacts to be read. Checking if there are"
                        + " changed artifacts in repository or in hospital for "
                        + currentRecord.getRepositoryId());
                // all our buffers are flushed, we take new syncInfo now
                currentRecord.switchToNewSyncInfo();
                syncInfo = currentRecord.getSyncInfo();
                // TODO Does it make sense to insert retry code here or is it
                // better just to try it again later?
                int numberOfTries = 1;
                boolean retry = false;
                List<ArtifactState> artifactsToBeRead = null;
                do {
                    int msToSleep = (numberOfTries - 1)
                            * connectionManager.getRetryIncrementTime();
                    int maxMsToSleep = connectionManager
                            .getMaximumRetryWaitingTime();
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Retrieving artifacts that have been changed for sync record "
                                    + syncInfo.asXML());
                        }
                        artifactsToBeRead = this
                                .getChangedArtifactsFromHospitalAndRepository(syncInfo);
                        retry = false;
                    } catch (Exception e) {
                        boolean connectionException = connectionManager
                                .isUseStandardTimeoutHandlingCode()
                                && this.handleException(e, connectionManager);
                        if (!connectionException) {
                            retry = false;
                            // remove repository record from list again
                            log.debug("Temporarily removing "
                                    + "from waiting list since an exception occured.");
                            removeFromWaitingList(currentRecord);
                            if (e instanceof CCFRuntimeException) {
                                throw (CCFRuntimeException) e;
                            } else if (e instanceof RuntimeException) {
                                throw (RuntimeException) e;
                            } else {
                                throw new CCFRuntimeException(
                                        "An exception occured", e);
                            }
                        } else {
                            retry = true;
                            if (numberOfTries == 1) {
                                // first try, long error message
                                log.warn(
                                        "Network related problem occurred while connecting to external system. Try operation again",
                                        e);
                            } else if (msToSleep < maxMsToSleep) {
                                // error occurred again, short error message, go
                                // to sleep
                                // we switched to a linear increase of the
                                // timeout value, may
                                // have to revisit this decision later
                                // int timeOut = (int) Math.pow(2,
                                // numberOfTries);
                                log.warn("Network related error occurred again ("
                                        + e.getMessage()
                                        + "), incremented timeout, now sleeping for "
                                        + msToSleep + " milliseconds.");
                                try {
                                    Thread.sleep(msToSleep);
                                } catch (InterruptedException e1) {
                                    log.error(
                                            "Interrupted sleep in timeout method: ",
                                            e1);
                                }
                            } else {
                                log.warn("Network related error occurred again, switched to maximum waiting time ("
                                        + e.getMessage()
                                        + "), sleeping for "
                                        + maxMsToSleep + " milliseconds.");
                                try {
                                    Thread.sleep(maxMsToSleep);
                                } catch (InterruptedException e1) {
                                    log.error(
                                            "Interrupted sleep in timeout method: ",
                                            e1);
                                }
                            }
                        }
                    }
                    ++numberOfTries;
                } while (retry);
                if (!artifactsToBeRead.isEmpty()) {
                    artifactsToBeReadList.addAll(artifactsToBeRead);
                    /*
                     * we ship artifacts, so the retrieved synch info for this
                     * project mapping may not be up to date until the artifacts
                     * to be read buffer has been completely emptied
                     */
                    currentRecord.notReadyForNewSynchInfo();
                }
            }
            if (!artifactsToBeReadList.isEmpty() && !isRestartConnector()
                    && !isShutDownConnector()) {
                log.debug("There are " + artifactsToBeReadList.size()
                        + " artifacts to be read.");
                ArtifactState artifactState = artifactsToBeReadList.remove(0);
                List<GenericArtifact> sortedGAs = null;
                if (artifactState.isReplayedArtifact()) {
                    sortedGAs = new ArrayList<GenericArtifact>();
                    log.debug("Parsing quarantined artifact with transaction id "
                            + artifactState.getTransactionId());
                    try {
                        GenericArtifact replayedArtifact = GenericArtifactHelper
                                .createGenericArtifactJavaObject(DocumentHelper
                                        .parseText(artifactState
                                                .getReplayedArtifactData()));
                        log.debug("Successfully parsed quarantined artifact with transaction id "
                                + artifactState.getTransactionId());
                        // reset error code and transaction id
                        replayedArtifact.setErrorCode(artifactState
                                .getErrorCode());
                        replayedArtifact.setTransactionId(artifactState
                                .getTransactionId());
                        sortedGAs.add(replayedArtifact);
                    } catch (GenericArtifactParsingException e) {
                        log.warn(
                                "Could not parse quarantined artifact with transaction id "
                                        + artifactState.getTransactionId(), e);
                    } catch (DocumentException e) {
                        log.warn(
                                "Could not parse quarantine artifact with transaction id "
                                        + artifactState.getTransactionId(), e);
                    }
                } else {
                    log.debug("Getting the data for artifact "
                            + artifactState.getArtifactId());
                    int numberOfTries = 1;

                    boolean retry = false;
                    do {
                        int msToSleep = (numberOfTries - 1)
                                * connectionManager.getRetryIncrementTime();
                        int maxMsToSleep = connectionManager
                                .getMaximumRetryWaitingTime();
                        try {
                            String artifactId = artifactState.getArtifactId();
                            // To avoid tampering SyncInfo object we have created a clone
                            // As per java documentation Document.clone() method provides
                            //detached and deep copy of the Object
                            Document tempSyncInfo = (Document) syncInfo.clone();
                            if (getIdentityMappingDatabaseReader() != null) {
                                // Update the syncinfo with the artifact lastmodifiedtime and lastModifiedVersion
                                // fetched from the identity mapping.Modifying the syncinfo does not has any side effects
                                // because artifactsToBeShippedList has already been populated with the artifact data
                                updateSyncInfoFromIdentityMapping(tempSyncInfo,
                                        artifactId,
                                        ARTIFACT_TYPE_PLAIN_ARTIFACT);
                            }
                            GenericArtifact artifactData = this
                                    .getArtifactData(tempSyncInfo, artifactId);
                            if (artifactData != null) {
                                log.debug("Finding out whether artifact data is stale ...");
                                if (isArtifactStale(artifactState, artifactData)) {
                                    log.debug("Artifact data is stale, pick up in next update cycle ...");
                                    sortedGAs = new ArrayList<GenericArtifact>();
                                } else {
                                    List<GenericArtifact> artifactAttachments = new ArrayList<GenericArtifact>();
                                    List<GenericArtifact> artifactDependencies = new ArrayList<GenericArtifact>();
                                    try {
                                        if (shipAttachments) {
                                            artifactAttachments = this
                                                    .getArtifactAttachments(
                                                            tempSyncInfo,
                                                            artifactData);
                                        }
                                        artifactDependencies = this
                                                .getArtifactDependencies(
                                                        tempSyncInfo,
                                                        artifactId);
                                    } catch (Exception e) {
                                        // if this is a connection exception, we
                                        // will retry, otherwise, we will
                                        // proceed
                                        // with a warning
                                        boolean connectionException = connectionManager
                                                .isUseStandardTimeoutHandlingCode()
                                                && this.handleException(e,
                                                        connectionManager);
                                        if (connectionException) {
                                            // this will trigger a retry
                                            throw e;
                                        } else {
                                            log.warn("Could not retrieve all attachments/dependencies for artifact "
                                                    + artifactId
                                                    + ". Only plain artifact is synchronized ...");
                                        }
                                    }

                                    sortedGAs = combineAndSort(artifactData,
                                            artifactAttachments,
                                            artifactDependencies);
                                }
                            } else {
                                log.debug("No artifact data has been retrieved for id "
                                        + artifactState.getArtifactId());
                                sortedGAs = new ArrayList<GenericArtifact>();
                            }
                            retry = false;
                        } catch (Exception e) {
                            boolean connectionException = connectionManager
                                    .isUseStandardTimeoutHandlingCode()
                                    && this.handleException(e,
                                            connectionManager);
                            if (!connectionException) {
                                retry = false;
                                log.error("Error retrieving artifact "
                                        + artifactState.getArtifactId());
                                if (e instanceof CCFRuntimeException) {
                                    throw (CCFRuntimeException) e;
                                } else if (e instanceof RuntimeException) {
                                    throw (RuntimeException) e;
                                } else {
                                    throw new CCFRuntimeException(
                                            "An exception occured ", e);
                                }
                            } else {
                                retry = true;
                                if (numberOfTries == 1) {
                                    // first try, long error message
                                    log.warn(
                                            "Network related problem occurred while retrieving data for artifact "
                                                    + artifactState
                                                            .getArtifactId()
                                                    + ". Try operation again",
                                            e);
                                } else if (msToSleep < maxMsToSleep) {
                                    // error occurred again, short error
                                    // message, go
                                    // to sleep
                                    // we switched to a linear increase of the
                                    // timeout value, may
                                    // have to revisit this decision later
                                    // int timeOut = (int) Math.pow(2,
                                    // numberOfTries);
                                    log.warn("Network related error for artifact "
                                            + artifactState.getArtifactId()
                                            + " occurred again ("
                                            + e.getMessage()
                                            + "), incremented timeout, now sleeping for "
                                            + msToSleep + " milliseconds.");
                                    try {
                                        Thread.sleep(msToSleep);
                                    } catch (InterruptedException e1) {
                                        log.error(
                                                "Interrupted sleep in timeout method: ",
                                                e1);
                                    }
                                } else {
                                    log.warn("Network related error for artifact "
                                            + artifactState.getArtifactId()
                                            + " occurred again, switched to maximum waiting time ("
                                            + e.getMessage()
                                            + "), sleeping for "
                                            + maxMsToSleep
                                            + " milliseconds.");
                                    try {
                                        Thread.sleep(maxMsToSleep);
                                    } catch (InterruptedException e1) {
                                        log.error(
                                                "Interrupted sleep in timeout method: ",
                                                e1);
                                    }
                                }
                            }
                        }
                        ++numberOfTries;
                    } while (retry);
                }

                artifactsToBeShippedList.addAll(sortedGAs);
                if (artifactsToBeShippedList.isEmpty())
                    return new Object[] {};

                GenericArtifact genericArtifact = artifactsToBeShippedList
                        .remove(0);
                try {
                    String conflictResolution = this
                            .getConflictResolutionPriority(syncInfo);
                    genericArtifact
                            .setConflictResolutionPriority(conflictResolution);
                    genericArtifact.setSourceSystemTimezone(this
                            .getSourceSystemTimezone(syncInfo));
                    genericArtifact.setTargetSystemTimezone(this
                            .getTargetSystemTimezone(syncInfo));
                    // genericArtifact.setSourceSystemEncoding(this.
                    // getSourceSystemEncoding(syncInfo));
                    // genericArtifact.setTargetSystemEncoding(this.
                    // getTargetSystemEncoding(syncInfo));
                    Document returnDoc = GenericArtifactHelper
                            .createGenericArtifactXMLDocument(genericArtifact);
                    Object[] returnObjects = new Object[] { returnDoc };
                    return returnObjects;
                } catch (GenericArtifactParsingException e) {
                    String cause = "Could not parse the artifact for "
                            + artifactState.getArtifactId();
                    log.error(cause, e);
                    genericArtifact
                            .setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
                    throw new CCFRuntimeException(cause, e);
                }
            } else {
                log.debug("No changed artifacts reported for "
                        + currentRecord.getRepositoryId()
                        + ". Removing it from the waiting list");
                removeFromWaitingList(currentRecord);
            }
        }
        try {
            if (isRestartConnector()) {
                log.debug("All buffers are flushed now ..., exit with exit code "
                        + RESTART_EXIT_CODE);
                ShutDownCCF.exitCCF(RESTART_EXIT_CODE);
            } else if (isShutDownConnector()) {
                log.debug("All buffers are flushed now ..., exit with exit code " + 0);
                ShutDownCCF.exitCCF(0);
            } else if (connectorHasReadAllInitialSynchronizationStatusRecords) {
                if (isShutdownCCFAfterInitialSync()) {
                    log.info("All repositories are in synch at this time, shutting down");
                    setShutDownConnector(true);
                } else {
                    log.debug("There are no artifacts to be shipped from any of the repositories. Sleeping");
                    Thread.sleep(sleepInterval);
                }
            }
        } catch (InterruptedException e) {
            String cause = "Thread is interrupted";
            log.warn(cause, e);
        }
        return new Object[] {};
    }

    public void reset(Object context) {
        // do nothing here
    }

    /**
     * If you set this property, the CCF will exit (with exit code 42) after the
     * number of seconds you have specified. If CCF is wrapped by service
     * wrapper, it will be restarted automatically. This setting can be used to
     * release resources from time to time. If you do not set this property or
     * set it to a negative value, the CCF will never exit.
     * 
     * @param autoRestartPeriod
     *            the autoRestartPeriod to set
     */
    public void setAutoRestartPeriod(int autoRestartPeriod) {
        this.autoRestartPeriod = autoRestartPeriod * 1000;
    }

    /**
     * Sets whether this scenario is just used for bulk import and no frequent
     * artifact changes are currently done on the repository. This allows to do
     * certain query optimizations in the concrete reader classes.
     * 
     * @param isBulkImport
     *            false if no query optimizations can be done (default), true if
     *            query optimizations can be done since repository data does not
     *            change frequently
     */
    public void setBulkImport(boolean isBulkImport) {
        this.isBulkImport = isBulkImport;
    }

    /**
     * Defines whether this reader component is used within a CCF 2.x process
     * Defaults to false
     * 
     * @param isCCF2xProcess
     */
    public void setCCF2xProcess(boolean isCCF2xProcess) {
        this.isCCF2xProcess = isCCF2xProcess;
    }

    /**
     * Set the connection manager. The connection manager is responsible to
     * manage (create, close, pool) the connections from type T. Furthermore, it
     * contains timeout settings and the settings for the retry code in case of
     * network timeout and session fault related errors.
     * 
     * @param connectionManager
     *            the connection manager object
     */
    public void setConnectionManager(ConnectionManager<T> connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Sets the (optional) data base reader that is used to poll quarantined
     * artifacts ready for replay If this property is not set, no lookup will be
     * done in the hospital.
     * 
     * @return
     */
    public void setHospitalDatabaseReader(
            JDBCReadConnector hospitalDatabaseReader) {
        this.hospitalDatabaseReader = hospitalDatabaseReader;
    }

    /**
     * Sets the (optional) data base reader that is used to retrieve the
     * source/target artifact information from the identity mapping table.If
     * this property is not set, no lookup will be done in the identity mapping.
     * 
     * @param identityMappingDatabaseReader
     */
    public void setIdentityMappingDatabaseReader(
            JDBCReadConnector identityMappingDatabaseReader) {
        this.identityMappingDatabaseReader = identityMappingDatabaseReader;
    }

    /**
     * Sets if the field meta data should be included in the artifact by the
     * Reader component.
     * 
     * @param includeFieldMetaData
     *            - true if the Reader should include the meta data for the
     *            fields. - flase if the Reader need not include the field meta
     *            data with the artifact fields.
     */
    public void setIncludeFieldMetaData(boolean includeFieldMetaData) {
        this.includeFieldMetaData = includeFieldMetaData;
    }

    /**
     * Sets the maximum attachment size to be shipped for an artifact. If the
     * attachment size is more than this configured value it should not be
     * shipped by the reader.
     * 
     * @param maxAttachmentPerArtifact
     *            - the maximum attachment size that can be shipped.
     */
    public void setMaxAttachmentSizePerArtifact(long maxAttachmentPerArtifact) {
        this.maxAttachmentSizePerArtifact = maxAttachmentPerArtifact;
    }

    /**
     * Sets the name of the component that should be recognized to be the entity
     * service during artifact replay Only if the originating component of a
     * quarantined artifact record matches this name, the payload will be
     * transformed again
     * 
     * @param nameOfEntityService
     *            name of the component that should be treated as the entity
     *            service component (null by default)
     */
    public void setNameOfEntityService(String nameOfEntityService) {
        this.nameOfEntityService = nameOfEntityService;
    }

    /**
     * Sets the flag whether to ship the attachments or not.
     * 
     * @param shipAttachments
     *            - true if the attachment should be shipped - false if the
     *            attachments should not be shipped.
     */
    public void setShipAttachments(boolean shipAttachments) {
        this.shipAttachments = shipAttachments;
    }

    public void setShipAttachmentsWithArtifact(
            boolean shipAttachmentsWithArtifact) {
        this.shipAttachmentsWithArtifact = shipAttachmentsWithArtifact;
    }

    /**
     * Setting this variable to true causes the CCF to shutdown after there are
     * no more out of date artifacts of all repositories to be synchronized at
     * this time. This property is useful for scenarios that just like to take a
     * snapshot of a repository. The default value is false (connector will not
     * stop)
     * 
     * @param shutdownCCFAfterInitialSync
     *            controls whether to stop after initial export
     */
    public void setShutdownCCFAfterInitialSync(
            boolean shutdownCCFAfterInitialSync) {
        this.shutdownCCFAfterInitialSync = shutdownCCFAfterInitialSync;
    }

    /**
     * Sets the sleep interval in milliseconds. Sleep interval is the time lag
     * introduced by the AbstractReader when there are no artifacts to be
     * shipped in any of the repositories configured.
     * 
     * @param sleepInterval
     */
    public void setSleepInterval(long sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

    @SuppressWarnings("unchecked")
    public void validate(List exceptions) {
        if (getConnectionManager() == null) {
            log.error("connectionManager property is not set");
            exceptions.add(new ValidationException(
                    "connectionManager property is not set", this));
        }

        if (getSleepInterval() == -1) {
            log.error("sleepInterval is not set");
            exceptions.add(new ValidationException("sleepInterval is not set",
                    this));
        }

        if (getHospitalDatabaseReader() == null) {
            log.warn("Reader will not poll hospital for quarantined entries since hospitalDatabaseReader property has not been set.");
        }

        if (getNameOfEntityService() == null) {
            log.warn("Retransformation of replayed artifacts is not configured since nameOfEntityService property has not been set.");
        }
    }

    protected Date getArtifactLastModifiedDate(Document syncInfo) {
        String lastModifiedDateString = this
                .getArtifactLastModifiedTime(syncInfo);
        Date lastModifiedDate = null;
        if (!StringUtils.isEmpty(lastModifiedDateString)) {
            lastModifiedDate = DateUtil.parse(lastModifiedDateString);
        } else {
            lastModifiedDate = new Date(0);
        }
        return lastModifiedDate;
    }

    protected String getExternalAppLinkId(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(EXTERNAL_APP_LINK_ID);
        if (node == null)
            return null;
        return node.getText();
    }

    protected String getFieldMappingKind(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(FIELD_MAPPING_KIND);
        if (node == null)
            return null;
        return node.getText();
    }

    protected String getFieldMappingName(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(FIELD_MAPPING_NAME);
        if (node == null)
            return null;
        return node.getText();
    }

    protected String getFieldMappingScope(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(FIELD_MAPPING_SCOPE);
        if (node == null)
            return null;
        return node.getText();
    }

    protected Date getLastModifiedDate(Document syncInfo) {
        String lastModifiedDateString = this
                .getLastSourceArtifactModificationDate(syncInfo);
        Date lastModifiedDate = null;
        if (!StringUtils.isEmpty(lastModifiedDateString)) {
            lastModifiedDate = DateUtil.parse(lastModifiedDateString);
        } else {
            lastModifiedDate = new Date(0);
        }
        return lastModifiedDate;
    }

    /**
     * Returns the source artifact id that was sync-ed in the last CCF cycle.
     * 
     * @param syncInfo
     *            - The incoming sync info of a particular repository.
     * @return - The source artifact id that was sync-ed last for this
     *         repository.
     */
    protected String getLastSourceArtifactId(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(LAST_SOURCE_ARTIFACT_ID);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * Returns the version of the artifact that was sync-ed in the last CCF
     * cycle.
     * 
     * @param syncInfo
     *            - The incoming sync info
     * @return - The version of the artifact that was last sync-ed
     */
    protected String getLastSourceVersion(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(LAST_SOURCE_ARTIFACT_VERSION);
        if (node == null)
            return null;
        return node.getText();
    }

    protected String getRepositoryMappingDirectionDirection(Document syncInfo) {
        Node node = syncInfo
                .selectSingleNode(REPOSITORY_MAPPING_DIRECTION_DIRECTION);
        if (node == null)
            return null;
        return node.getText();
    }

    protected String getRepositoryMappingDirectionId(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(REPOSITORY_MAPPING_DIRECTION_ID);
        if (node == null)
            return null;
        return node.getText();
    }

    protected String getRepositoryMappingId(Document syncInfo) {
        Node node = syncInfo.selectSingleNode(REPOSITORY_MAPPING_ID);
        if (node == null)
            return null;
        return node.getText();
    }

    /**
     * All the artifact data and dependent data generic artifacts are
     * accumulated in a single List and are sorted according to their last
     * modified date so that the artifact that was changed early will ship
     * first.
     * 
     * @param artifactData
     *            - The artifact's data
     * @param artifactAttachments
     *            - Attachments of an artifact
     * @param artifactDependencies
     *            - Dependent artifacts
     * 
     * @return - The sorted list of Generic Artifact objects
     */
    private List<GenericArtifact> combineAndSort(GenericArtifact artifactData,
            List<GenericArtifact> artifactAttachments,
            List<GenericArtifact> artifactDependencies) {
        ArrayList<GenericArtifact> gaList = new ArrayList<GenericArtifact>();
        gaList.add(artifactData);
        gaList.addAll(artifactAttachments);
        gaList.addAll(artifactDependencies);
        Collections.sort(gaList, genericArtifactComparator);
        return gaList;
    }

    /**
     * This method fetches the first quarantined artifact that is ready to
     * replay from the hospital
     * 
     * @param syncInfo
     *            only quarantined artifacts that match the sync info will be
     *            fetched
     * @return
     */
    private List<ArtifactState> getChangedArtifactsFromHospital(
            Document syncInfo) {
        ArrayList<ArtifactState> quarantinedArtifact = new ArrayList<ArtifactState>();
        // only if a connection to the hospital table is present, we can query
        // quarantined artifacts
        if (getHospitalDatabaseReader() != null) {
            IOrderedMap inputParameters = new OrderedHashMap();

            if (!isCCF2xProcess()) {
                inputParameters.add(getSourceSystemId(syncInfo));
                inputParameters.add(getSourceRepositoryId(syncInfo));
                inputParameters.add(getTargetSystemId(syncInfo));
                inputParameters.add(getTargetRepositoryId(syncInfo));
            } else {
                /**
                 * In CCF 2.x, we only need the repository mapping direction id
                 * (stored within the source system kind top level attribute) to
                 * retrieve all matching hospital entries
                 */
                inputParameters.add(getSourceSystemKind(syncInfo));
            }

            hospitalDatabaseReader.connect();
            Object[] resultSet = hospitalDatabaseReader
                    .next(inputParameters, 1);

            if (resultSet.length != 0) {
                OrderedHashMap result = (OrderedHashMap) resultSet[0];

                ArtifactState artifactState = new ArtifactState();
                artifactState.setTransactionId(result.get(0).toString());
                artifactState.setReplayedArtifactData(result.get(1).toString());
                // if we know the name of the entity service, we can decide
                // whether the artifact should be transformed
                // again. Otherwise, we will not change the default error code
                // (ok) and no transformation will take place
                if (getNameOfEntityService() != null) {
                    Object originatingComponent = result.get(2);
                    if (originatingComponent == null
                            || !originatingComponent
                                    .equals(getNameOfEntityService())) {
                        log.debug("Do not trigger a further transformation of quarantined artifact's payload.");
                        artifactState
                                .setErrorCode(GenericArtifact.ERROR_REPLAYED_WITHOUT_TRANSFORMATION);
                    } else {
                        // quarantined artifact should be transformed again
                        log.debug("Trigger a further transformation of quarantined artifact's payload.");
                        artifactState
                                .setErrorCode(GenericArtifact.ERROR_REPLAYED_WITH_TRANSFORMATION);
                    }
                }
                artifactState.setReplayedArtifact(true);
                quarantinedArtifact.add(artifactState);
            }
            // hospitalDatabaseReader.disconnect();
        }
        return quarantinedArtifact;
    }

    /**
     * This method will first read one artifact from the hospital and then
     * populates the rest of the list with changes from the repository
     * 
     * @param syncInfo
     * @return list with changed artifacts
     */
    private List<ArtifactState> getChangedArtifactsFromHospitalAndRepository(
            Document syncInfo) {
        List<ArtifactState> changedArtifacts = getChangedArtifactsFromHospital(syncInfo);
        changedArtifacts.addAll(getChangedArtifacts(syncInfo));
        return changedArtifacts;
    }

    /**
     * Initializes the Reader with an empty repository records HashMap. The
     * repositories synchronization waiting list and the repository ids in the
     * waiting list are also initialized. It will also create a
     * shutdownHookListener that will set the shutDownConnector variable It also
     * creates a comparator that will be used to compare a set of
     * GenericArtifacts. The comparator compares the GenericArtifacts according
     * to the last modified date of the artifacts.
     */
    private void init() {
        log.debug("Initializing the AbstractReader");
        repositoryRecordHashMap = new HashMap<String, RepositoryRecord>();
        repositorySynchronizationWaitingList = new ArrayList<RepositoryRecord>();
        repositoryRecordsInRepositorySynchronizationWaitingList = new HashSet<String>();
        genericArtifactComparator = new Comparator<GenericArtifact>() {
            public int compare(GenericArtifact first, GenericArtifact second) {
                String firstLastModifiedDateStr = first
                        .getSourceArtifactLastModifiedDate();
                Date firstLastModifiedDate = DateUtil
                        .parse(firstLastModifiedDateStr);

                String secondLastModifiedDateStr = first
                        .getSourceArtifactLastModifiedDate();
                Date secondLastModifiedDate = DateUtil
                        .parse(secondLastModifiedDateStr);
                if (firstLastModifiedDate.after(secondLastModifiedDate)) {
                    return 1;
                } else if (firstLastModifiedDate.before(secondLastModifiedDate)) {
                    return -1;
                } else {
                    String firstSourceArtifactId = null;
                    if (first.getArtifactType() == GenericArtifact.ArtifactTypeValue.PLAINARTIFACT) {
                        firstSourceArtifactId = first.getSourceArtifactId();
                    } else {
                        firstSourceArtifactId = first
                                .getDepParentSourceArtifactId();
                    }
                    String secondSourceArtifactId = null;
                    if (second.getArtifactType() == GenericArtifact.ArtifactTypeValue.PLAINARTIFACT) {
                        secondSourceArtifactId = second.getSourceArtifactId();
                    } else {
                        secondSourceArtifactId = second
                                .getDepParentSourceArtifactId();
                    }
                    if (firstSourceArtifactId.equals(secondSourceArtifactId)) {
                        if (first.getArtifactType() == GenericArtifact.ArtifactTypeValue.PLAINARTIFACT) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else {
                        return firstSourceArtifactId
                                .compareTo(secondSourceArtifactId);
                    }
                }
            }
        };
    }

    private boolean isArtifactStale(ArtifactState artifactState,
            GenericArtifact artifactData) {
        // In a bulk import scenario, stale updates should not happen at all
        if (isBulkImport()) {
            return false;
        }
        String newVersionStr = artifactData.getSourceArtifactVersion();
        long newVersion = Long.parseLong(newVersionStr);
        String newLastModifiedDateStr = artifactData
                .getSourceArtifactLastModifiedDate();
        Date newLastModifiedDate = DateUtil.parse(newLastModifiedDateStr);
        if (artifactState.getArtifactVersion() < newVersion
                && artifactState.getArtifactLastModifiedDate().before(
                        newLastModifiedDate)) {
            log.debug("Stale update on artifact "
                    + artifactState.getArtifactId() + ": Old version: "
                    + artifactState.getArtifactVersion() + " new version: "
                    + newVersion + " old time stamp: "
                    + artifactState.getArtifactLastModifiedDate()
                    + " new time stamp: " + newLastModifiedDate);
            return true;
        }
        return false;
    }

    private void modifySyncInfo(Document syncInfo,
            String lastArtifactModifiedTime, String lastArtifactModifiedVersion) {
        if (lastArtifactModifiedTime == null
                && lastArtifactModifiedVersion == null) {
            return;
        }
        //Cloned syncInfo is used to create two new element - ARTIFACT_LAST_MODIFIED_DATE and ARTIFACT_LAST_MODIFIED_VERSION
        Element rootElement = syncInfo.getRootElement();
        Element artifactLastModifiedTimeElement = rootElement
                .addElement(ARTIFACT_LAST_MODIFIED_DATE_ELEMENT);
        artifactLastModifiedTimeElement.setText(lastArtifactModifiedTime);
        Element artifactLastModifiedVersionElement = rootElement
                .addElement(ARTIFACT_LAST_MODIFIED_VERSION_ELEMENT);
        artifactLastModifiedVersionElement.setText(lastArtifactModifiedVersion);
    }

    /**
     * Moves the repository to the tail of the waiting list so that in the next
     * immediate run the repository will not be considered for synchronization
     * (Unless it is the lone repository that is being sync-ed).
     * 
     * @param currentRecord
     *            - The repository record to be moved to the tail.
     */
    private void moveToTail(RepositoryRecord currentRecord) {
        repositorySynchronizationWaitingList.remove(currentRecord);
        repositorySynchronizationWaitingList.add(currentRecord);
    }

    /**
     * Removes the record passed in the parameter from the waiting list so that
     * in the further runs this repository will not be taken into account for
     * artifact shipment (Unless the repository record is added again by the
     * incoming synchronization record).
     * 
     * @param currentRecord
     *            - The repository record to be removed from the waiting list.
     */
    private void removeFromWaitingList(RepositoryRecord currentRecord) {
        repositorySynchronizationWaitingList.remove(currentRecord);
        String repositoryKey = currentRecord.getRepositoryId();
        repositoryRecordsInRepositorySynchronizationWaitingList
                .remove(repositoryKey);
    }

    /**
     * Update the sync info with the artifact lastmodifiedtime and
     * lastModifiedVersion fetched from the identity mapping. This will makes
     * reader to sync all the comments and attachment in an artifact, as Sync is
     * based on Artifact last modified timestamp or last modified version as
     * previously it was based on repository lastmodifiedtime or version. This
     * will resolve the leftover comments/attachments during stale scenario.
     * 
     * @param syncInfo
     * @param artifactId
     * @param artifactType
     */
    private void updateSyncInfoFromIdentityMapping(Document syncInfo,
            String artifactId, String artifactType) {
        IOrderedMap inputParameters = new OrderedHashMap();
        String lastModifiedTime = null, lastModifiedVersion = null;
        if (!isCCF2xProcess) {
            inputParameters.add(this.getSourceSystemId(syncInfo));//sourceSystemId
            inputParameters.add(this.getSourceRepositoryId(syncInfo));//sourceRepositoryId
            inputParameters.add(this.getTargetSystemId(syncInfo));//targetSystemId
            inputParameters.add(this.getTargetRepositoryId(syncInfo));//targetRepositoryId
        } else {
            inputParameters.add(this.getRepositoryMappingId(syncInfo));//repositorymappingid
        }
        inputParameters.add(artifactId);//artifactId
        inputParameters.add(artifactType);//artifactType
        try {
            identityMappingDatabaseReader.connect();
            Object[] resultSet = identityMappingDatabaseReader.next(
                    inputParameters, 1000);
            if (resultSet == null || resultSet.length == 0) {
                lastModifiedTime = new Timestamp(0).toString();
                lastModifiedVersion = "0";
                log.debug("Setting the lastModifiedTime and lastModifiedVersion to default values");
            } else {
                IOrderedMap resultSetMap = (OrderedHashMap) resultSet[0];
                Timestamp lastModifiedTimestamp = (java.sql.Timestamp) resultSetMap
                        .get(1);
                lastModifiedVersion = (String) resultSetMap.get(2);
                lastModifiedTime = lastModifiedTimestamp.toString();
            }
            modifySyncInfo(syncInfo, lastModifiedTime, lastModifiedVersion);
        } catch (Exception e) {
            log.debug(
                    "Update syncInfo from IdentityMapping failed due to following exception ",
                    e);
        }
    }

    public static String convertTimeZoneFromCCF2xDataBaseFormat(String timezone) {
        return timezone.replace("_MINUS_", "-").replace("_PLUS_", "+")
                .replace("_SLASH_", "/");
    }

    /**
     * @return the restartConnector
     */
    public static boolean isRestartConnector() {
        return restartConnector;
    }

    /**
     * @return the shutDownConnector
     */
    public static boolean isShutDownConnector() {
        return shutDownConnector;
    }

    /**
     * @param restartConnector
     *            the restartConnector to set
     */
    public static void setRestartConnector(boolean restartConnector) {
        AbstractReader.restartConnector = restartConnector;
    }

    /**
     * @param shutDownConnector
     *            the shutDownConnector to set
     */
    public static void setShutDownConnector(boolean shutDownConnector) {
        AbstractReader.shutDownConnector = shutDownConnector;
    }
}
