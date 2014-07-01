package com.collabnet.ccf.splunk;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.RepositoryRecord;
import com.collabnet.ccf.core.ShutDownCCF;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DummyArtifactSoapDO;
import com.collabnet.ccf.teamforge.TFReader;
import com.collabnet.ccf.teamforge.TFToGenericArtifactConverter;
import com.collabnet.ce.soap60.webservices.cemain.AuditHistorySoapRow;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.AuditHistoryList;
import com.collabnet.teamforge.api.main.AuditHistoryRow;
import com.collabnet.teamforge.api.main.TeamForgeClient;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.TrackerClient;

public class SplunkTFReader extends TFReader {
    private static final Log log = LogFactory.getLog(SplunkTFReader.class);

    public String formatIFDate(Date date) {
        return GenericArtifactHelper.df.format(date);
    }

    @Override
    public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
        // TODO Auto-generated method stub
        return super.getArtifactData(syncInfo, artifactId);
    }

    public List<DummyArtifactSoapDO> getArtifactData1(Document syncInfo,
            String artifactId) {

        List<DummyArtifactSoapDO> allVersions = new ArrayList<DummyArtifactSoapDO>();
        Connection connection = connect(syncInfo);
        try {
            TrackerClient trackerClient = connection.getTrackerClient();
            TeamForgeClient tfClient = connection.getTeamForgeClient();
            ArtifactDO currentArtifactData = trackerClient
                    .getArtifactData(artifactId);

            ArtifactDO cloneCurrentArtifact = artifactClone(connection,
                    currentArtifactData);
            AuditHistoryList auditSaopList = tfClient.getAuditHistoryList(
                    artifactId, true);
            //            tfClient.getCommentList(artifactId).getDataRows();
            String projectID = tfClient.getProjectDataByPath(
                    currentArtifactData.getPath().split("/")[0]).getId();
            GenericArtifact ga = TFToGenericArtifactConverter.convertArtifact(
                    connection.supports53(), connection.supports54(),
                    currentArtifactData, null,
                    this.getLastModifiedDate(syncInfo), false,
                    this.getSourceSystemTimezone(syncInfo), null, null, null);
            List<AuditHistoryRow> auditClone = cloneAuditHistory(auditSaopList);
            List<List<AuditHistoryRow>> changeSet = extractNewAndOldValueSet(
                    auditSaopList, auditClone);

            ArtifactDO currentDo = cloneCurrentArtifact, oldDO = null;
            while (changeSet.size() != 0) {
                DummyArtifactSoapDO dummy = new DummyArtifactSoapDO();
                dummy.setType("artifact");
                dummy.setOperation("update");
                dummy.setProjectIdString(projectID);
                Date lastModifiedDate = currentDo.getLastModifiedDate();
                dummy.setUpdatedData(artifactClone(connection, currentDo));
                ga.setArtifactAction(ArtifactActionValue.UPDATE);
                ga.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);
                ga.setSourceArtifactId(artifactId);
                ga.setSourceArtifactLastModifiedDate(formatIFDate(lastModifiedDate));
                ga.setSourceArtifactVersion(dummy.getLastVersion());
                ga.setTargetArtifactId(artifactId);
                super.populateSrcAndDest(syncInfo, ga);
                dummy.setGenericArtifact(ga);
                boolean runBelowLoop = true;
                while (runBelowLoop) {
                    oldDO = applyOldValueChanges(currentDo, changeSet.remove(0));
                    dummy.setOriginalData(artifactClone(connection, oldDO));
                    runBelowLoop = false;
                }
                allVersions.add(dummy);
                if (oldDO != null) {
                    currentDo = artifactClone(connection, oldDO);
                }
            }
            if (changeSet.size() == 0) { // while create
                DummyArtifactSoapDO dummy1 = new DummyArtifactSoapDO();
                dummy1.setType("artifact");
                dummy1.setOperation("create");
                dummy1.setProjectIdString(projectID);
                dummy1.setUpdatedData(currentDo);
                dummy1.setOriginalData(currentDo);
                ga.setArtifactAction(ArtifactActionValue.CREATE);
                ga.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);
                ga.setSourceArtifactId(artifactId);
                ga.setSourceArtifactLastModifiedDate(formatIFDate(cloneCurrentArtifact
                        .getLastModifiedDate()));
                ga.setSourceArtifactVersion(dummy1.getLastVersion());
                ga.setTargetArtifactId(artifactId);
                super.populateSrcAndDest(syncInfo, ga);
                dummy1.setGenericArtifact(ga);
                allVersions.add(dummy1);

            }
            Collections.reverse(allVersions);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            this.disconnect(connection);
        }

        return allVersions;
    }

    @Override
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
        String repositoryMappingDirectionID = this
                .getRepositoryMappingDirectionId(syncInfoIn);
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
            rmdDryModeHandler.loadRMDAndRMDConfig(repositoryMappingDirectionID);
            rmdFilterHandler.loadRMDAndRMDConfig(repositoryMappingDirectionID);
            rmdForceHandler.loadRMDAndRMDConfig(repositoryMappingDirectionID);
            // RepositoryRecord movedRecord =
            // repositorySynchronizationWaitingList.remove(0);
            // repositorySynchronizationWaitingList.add(movedRecord);
            List<DummyArtifactSoapDO> artifactsToBeShippedList = currentRecord
                    .getDummyArtifactSoapToBeShippedList();
            List<ArtifactState> artifactsToBeReadList = currentRecord
                    .getArtifactsToBeReadList();

            // If dryrun mode is set to stop,clear all already fetched artifacts for the RMD 
            // and set dry-mode to off
            if (rmdDryModeHandler
                    .isDryRunEqualsStop(repositoryMappingDirectionID)) {
                artifactsToBeReadList.clear();
                rmdDryModeHandler
                        .updateRMDConfigToOff(repositoryMappingDirectionID);
            }

            if (!artifactsToBeShippedList.isEmpty()) { //TODO: this needs to rewritten with dummyArtifactSoapToBeShippedList
                log.debug("There are " + artifactsToBeShippedList.size()
                        + " artifacts to be shipped.");
                DummyArtifactSoapDO genericArtifact = artifactsToBeShippedList
                        .remove(0);
                // if(artifactsToBeShippedList.isEmpty()){
                // repositorySynchronizationWaitingList.remove(currentRecord);
                // repositorySynchronizationWaitingList.add(currentRecord);
                // }
                //                String artifactId = genericArtifact.getSourceArtifactId();
                try {
                    //                    String conflictResolution = this
                    //                            .getConflictResolutionPriority(syncInfo);
                    //                    genericArtifact
                    //                            .setConflictResolutionPriority(conflictResolution);
                    //                    genericArtifact.setSourceSystemTimezone(this
                    //                            .getSourceSystemTimezone(syncInfo));
                    //                    genericArtifact.setTargetSystemTimezone(this
                    //                            .getTargetSystemTimezone(syncInfo));
                    //                    // genericArtifact.setSourceSystemEncoding(this.
                    //                    // getSourceSystemEncoding(syncInfo));
                    //                    // genericArtifact.setTargetSystemEncoding(this.
                    //                    // getTargetSystemEncoding(syncInfo));
                    //                    Document returnDoc = GenericArtifactHelper
                    //                            .createGenericArtifactXMLDocument(genericArtifact);
                    Object[] returnObjects = new Object[] { genericArtifact };
                    return returnObjects;
                } catch (Exception e) {
                    //                    String cause = "Could not parse the artifact for "
                    //                            + artifactId;
                    //                    log.error(cause, e);
                    //                    genericArtifact
                    //                            .setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
                    throw new CCFRuntimeException("testing artifactshipped", e);
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
                if (rmdForceHandler
                        .isForceEnabled(repositoryMappingDirectionID)
                        && artifactsToBeReadList.isEmpty()) {
                    rmdForceHandler
                            .updateRMDConfigToOff(repositoryMappingDirectionID);
                }
                List<GenericArtifact> sortedGAs = null;
                if (artifactState.isReplayedArtifact()) { //TODO this if loop will be void need to check
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
                            if (!rmdFilterHandler.containsId(
                                    repositoryMappingDirectionID, artifactId)) {
                                return new Object[] {};
                            }
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
                            List<DummyArtifactSoapDO> artifactDatas = this
                                    .getArtifactData1(tempSyncInfo, artifactId);
                            for (DummyArtifactSoapDO arifactDatArtifactSoapDO : artifactDatas) {
                                artifactsToBeShippedList
                                        .add(arifactDatArtifactSoapDO);

                            }
                            //                            if (artifactData != null) {
                            //                                log.debug("Finding out whether artifact data is stale ...");
                            //                                if (isArtifactStale(artifactState, artifactData)) {
                            //                                    log.debug("Artifact data is stale, pick up in next update cycle ...");
                            //                                    sortedGAs = new ArrayList<GenericArtifact>();
                            //                                } else {
                            //                                    List<GenericArtifact> artifactAttachments = new ArrayList<GenericArtifact>();
                            //                                    List<GenericArtifact> artifactDependencies = new ArrayList<GenericArtifact>();
                            //                                    try {
                            //                                        if (shipAttachments) {
                            //                                            artifactAttachments = this
                            //                                                    .getArtifactAttachments(
                            //                                                            tempSyncInfo,
                            //                                                            artifactData);
                            //                                        }
                            //                                        artifactDependencies = this
                            //                                                .getArtifactDependencies(
                            //                                                        tempSyncInfo,
                            //                                                        artifactId);
                            //                                    } catch (Exception e) {
                            //                                        // if this is a connection exception, we
                            //                                        // will retry, otherwise, we will
                            //                                        // proceed
                            //                                        // with a warning
                            //                                        boolean connectionException = connectionManager
                            //                                                .isUseStandardTimeoutHandlingCode()
                            //                                                && this.handleException(e,
                            //                                                        connectionManager);
                            //                                        if (connectionException) {
                            //                                            // this will trigger a retry
                            //                                            throw e;
                            //                                        } else {
                            //                                            log.warn("Could not retrieve all attachments/dependencies for artifact "
                            //                                                    + artifactId
                            //                                                    + ". Only plain artifact is synchronized ...");
                            //                                        }
                            //                                    }
                            //
                            //                                    sortedGAs = combineAndSort(artifactData,
                            //                                            artifactAttachments,
                            //                                            artifactDependencies);
                            //                                }
                            //                            } else {
                            //                                log.debug("No artifact data has been retrieved for id "
                            //                                        + artifactState.getArtifactId());
                            //                                sortedGAs = new ArrayList<GenericArtifact>();
                            //                            }
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

                //                artifactsToBeShippedList.addAll(sortedGAs);
                if (artifactsToBeShippedList.isEmpty())
                    return new Object[] {};

                DummyArtifactSoapDO genericArtifact = artifactsToBeShippedList
                        .remove(0);
                try {
                    //                    String conflictResolution = this
                    //                            .getConflictResolutionPriority(syncInfo);
                    //                    genericArtifact
                    //                            .setConflictResolutionPriority(conflictResolution);
                    //                    genericArtifact.setSourceSystemTimezone(this
                    //                            .getSourceSystemTimezone(syncInfo));
                    //                    genericArtifact.setTargetSystemTimezone(this
                    //                            .getTargetSystemTimezone(syncInfo));
                    //                    // genericArtifact.setSourceSystemEncoding(this.
                    //                    // getSourceSystemEncoding(syncInfo));
                    //                    // genericArtifact.setTargetSystemEncoding(this.
                    //                    // getTargetSystemEncoding(syncInfo));
                    //                    Document returnDoc = GenericArtifactHelper
                    //                            .createGenericArtifactXMLDocument(genericArtifact);
                    Object[] returnObjects = new Object[] { genericArtifact };
                    return returnObjects;
                } catch (Exception e) {
                    //                    String cause = "Could not parse the artifact for "
                    //                            + artifactState.getArtifactId();
                    //                    log.error(cause, e);
                    //                    genericArtifact
                    //                            .setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
                    throw new CCFRuntimeException(
                            "testing the code implementation", e);
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

    private ArtifactDO applyOldValueChanges(ArtifactDO cloneCurrentArtifact,
            List<AuditHistoryRow> changeList) throws IllegalAccessException,
            InvocationTargetException {
        Class kClass = cloneCurrentArtifact.getClass();
        Method[] methods = kClass.getMethods();
        for (AuditHistoryRow auditHistoryRow : changeList) {
            String propertyName = auditHistoryRow.getPropertyName();
            for (Method m : methods) {
                // we are only interested in getter methods
                if (!m.getName().startsWith("set")) {
                    continue;
                }
                // we are only interested in methods that do not take any parameters
                if (m.getParameterTypes().length != 1) {
                    continue;
                }

                if (m.getName().substring(3).equalsIgnoreCase(propertyName)) {
                    String oldVal = auditHistoryRow.getOldValue();
                    Class parameterClass = m.getParameterTypes()[0];
                    if ("java.lang.String".equals(parameterClass.getName())) {
                        m.invoke(cloneCurrentArtifact, oldVal);
                    } else if ("int".equals(parameterClass.getName())) {
                        m.invoke(cloneCurrentArtifact, Integer.valueOf(oldVal));
                    } else if ("java.util.Date"
                            .equals(parameterClass.getName())) {
                        //TODO: need to format the oldvalue to Date                        
                    } else if ("boolean".equals(parameterClass.getName())) {
                        m.invoke(cloneCurrentArtifact, Boolean.valueOf(oldVal));

                    } else if ("com.collabnet.ce.soap50.types.SoapFieldValues"
                            .equals(parameterClass.getName())) {
                        //TODO: need to format the oldvalue to SoapFieldValues;                        
                    }
                }
            }
        }
        if (!changeList.isEmpty()) {
            AuditHistoryRow auditRow = changeList.get(0);
            cloneCurrentArtifact.setLastModifiedBy(auditRow.getModifiedBy());
            cloneCurrentArtifact
                    .setLastModifiedDate(auditRow.getDateModified());
        }
        return cloneCurrentArtifact;
    }

    private ArtifactDO artifactClone(Connection connection,
            ArtifactDO originalArtifact) {
        ArtifactDO newArtifact = new ArtifactDO(connection.supports61(),
                connection.supports54(), connection.supports53(),
                connection.supports50());
        newArtifact.setActualEffort(originalArtifact.getActualEffort());
        newArtifact.setAssignedTo(originalArtifact.getAssignedTo());
        newArtifact.setAutosumming(originalArtifact.getAutosumming());
        newArtifact.setCategory(originalArtifact.getCategory());
        newArtifact.setCloseDate(originalArtifact.getCloseDate());
        newArtifact.setCreatedBy(originalArtifact.getCreatedBy());
        newArtifact.setCreatedDate(originalArtifact.getCreatedDate());
        newArtifact.setCustomer(originalArtifact.getCustomer());
        newArtifact.setDescription(originalArtifact.getDescription());
        newArtifact.setEstimatedEffort(originalArtifact.getEstimatedEffort());
        newArtifact.setFlexFields(originalArtifact.getFlexFields());
        newArtifact.setFolderId(originalArtifact.getFolderId());
        newArtifact.setGroup(originalArtifact.getGroup());
        newArtifact.setId(originalArtifact.getId());
        newArtifact.setLastModifiedBy(originalArtifact.getLastModifiedBy());
        newArtifact.setPath(originalArtifact.getPath());
        newArtifact.setLastModifiedDate(originalArtifact.getLastModifiedDate());
        newArtifact.setPlanningFolderId(originalArtifact.getPlanningFolderId());
        newArtifact.setPoints(originalArtifact.getPoints());
        newArtifact.setPriority(originalArtifact.getPriority());
        newArtifact.setRemainingEffort(originalArtifact.getRemainingEffort());
        newArtifact.setReportedReleaseId(originalArtifact
                .getReportedReleaseId());
        newArtifact.setResolvedReleaseId(originalArtifact
                .getResolvedReleaseId());
        newArtifact.setStatus(originalArtifact.getStatus());
        newArtifact.setStatusClass(originalArtifact.getStatusClass());
        newArtifact.setTitle(originalArtifact.getTitle());
        newArtifact.setVersion(originalArtifact.getVersion());
        return newArtifact;
    }

    private List<AuditHistoryRow> cloneAuditHistory(AuditHistoryList auditList) {
        List<AuditHistoryRow> auditClone = new ArrayList<AuditHistoryRow>();
        for (AuditHistoryRow row : auditList.getDataRows()) {
            AuditHistoryRow auditRow = cloneAuditHistorySoapRow(row);
            auditClone.add(auditRow);
        }
        return auditClone;
    }

    private AuditHistoryRow cloneAuditHistorySoapRow(AuditHistoryRow row) {
        AuditHistorySoapRow auditRow = new AuditHistorySoapRow();
        auditRow.setComment(row.getComment());
        auditRow.setDateModified(row.getDateModified());
        auditRow.setModifiedBy(row.getModifiedBy());
        auditRow.setModifierFullName(row.getModifierFullName());
        auditRow.setNewValue(row.getNewValue());
        auditRow.setOldValue(row.getOldValue());
        auditRow.setOperation(row.getOperation());
        auditRow.setPropertyName(row.getPropertyName());
        return new AuditHistoryRow(auditRow);
    }

    private List<List<AuditHistoryRow>> extractNewAndOldValueSet(
            AuditHistoryList auditSoapList, List<AuditHistoryRow> auditClone) {
        // collect all the changelog done at same period by same user
        List<List<AuditHistoryRow>> changeCollection = new ArrayList<List<AuditHistoryRow>>();
        while (!auditClone.isEmpty()) {
            List<AuditHistoryRow> changeLogList = new ArrayList<AuditHistoryRow>();
            int index = auditSoapList.getDataRows().length - auditClone.size();
            AuditHistoryRow row1 = auditSoapList.getDataRows()[index];
            Date lastModifiedDate = row1.getDateModified();
            String lastModifier = row1.getModifiedBy();
            boolean sizeExist = !auditClone.isEmpty();
            while (sizeExist) {
                AuditHistoryRow row = auditClone.get(0);
                if (lastModifiedDate.equals(row.getDateModified())
                        && lastModifier.equals(row.getModifiedBy())) {
                    changeLogList.add(auditClone.remove(0));
                    sizeExist = !auditClone.isEmpty();
                } else {
                    sizeExist = false;
                }
            }
            changeCollection.add(changeLogList);
        }
        return changeCollection;
    }

    /*
     * private void logArtifact(Artifact3SoapDO originalArtifact) {
     * log.info("actual effort " + originalArtifact.getActualEffort());
     * log.info("getAssignedTo " + originalArtifact.getAssignedTo());
     * log.info("getAutosumming " + originalArtifact.getAutosumming());
     * log.info("getCategory " + originalArtifact.getCategory());
     * log.info("getCloseDate " + originalArtifact.getCloseDate());
     * log.info("getCreatedBy " + originalArtifact.getCreatedBy());
     * log.info("getCreatedDate " + originalArtifact.getCreatedDate());
     * log.info("getCustomer " + originalArtifact.getCustomer());
     * log.info("getDescription " + originalArtifact.getDescription());
     * log.info("getEstimatedEffort " + originalArtifact.getEstimatedEffort());
     * log.info("getFlexFields " + originalArtifact.getFlexFields());
     * log.info("getFolderId " + originalArtifact.getFolderId());
     * log.info("getGroup " + originalArtifact.getGroup()); log.info("getId " +
     * originalArtifact.getId()); log.info("getLastModifiedBy " +
     * originalArtifact.getLastModifiedBy()); log.info("getLastModifiedDate " +
     * originalArtifact.getLastModifiedDate()); log.info("getPath " +
     * originalArtifact.getPath()); log.info("getPlanningFolderId " +
     * originalArtifact.getPlanningFolderId()); log.info("getPoints " +
     * originalArtifact.getPoints()); log.info("getPriority " +
     * originalArtifact.getPriority()); log.info("getRemainingEffort " +
     * originalArtifact.getRemainingEffort()); log.info("getReportedReleaseId "
     * + originalArtifact.getReportedReleaseId());
     * log.info("getResolvedReleaseId" +
     * originalArtifact.getResolvedReleaseId()); log.info("getStatus " +
     * originalArtifact.getStatus()); log.info("getStatusClass " +
     * originalArtifact.getStatusClass()); log.info("getTitle " +
     * originalArtifact.getTitle()); log.info("getVersion " +
     * originalArtifact.getVersion()); } private void
     * logAuditHistorySoapRow(AuditHistorySoapRow row) { log.info("getComment "
     * + row.getComment()); log.info("getDateModified " +
     * row.getDateModified()); log.info("getModifiedBy " + row.getModifiedBy());
     * log.info("getModifierFullName " + row.getModifierFullName());
     * log.info("getNewValue " + row.getNewValue()); log.info("getOldValue " +
     * row.getOldValue()); log.info("getOperation " + row.getOperation());
     * log.info("getPropertyName " + row.getPropertyName()); }
     */

}
