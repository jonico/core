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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.core.Component;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.rmdhandlers.DryModeHandler;
import com.collabnet.ccf.core.rmdhandlers.NoOpDryModeHandler;
import com.collabnet.ccf.core.utils.XPathUtils;

public abstract class AbstractWriter<T> extends Component implements IDataProcessor {
    private static final Log     log                  = LogFactory
                                                              .getLog(AbstractWriter.class);
    private static final Log     logConflictResolutor = LogFactory
                                                              .getLog("com.collabnet.ccf.core.conflict.resolution");

    private ConnectionManager<T> connectionManager    = null;

    private DryModeHandler       rmdDryModeHandler    = new NoOpDryModeHandler();

    public AbstractWriter() {
    }

    public abstract Document createArtifact(Document gaDocument);

    public abstract Document[] createAttachment(Document gaDocument);

    public abstract Document createDependency(Document gaDocument);

    public abstract Document deleteArtifact(Document gaDocument);

    public abstract Document[] deleteAttachment(Document gaDocument);

    public abstract Document deleteDependency(Document gaDocument);

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

    public DryModeHandler getRmdDryModeHandler() {
        return rmdDryModeHandler;
    }

    public boolean handleException(Throwable rootCause,
            ConnectionManager<T> connectionManager, Document ga) {
        return false;
    }

    public Object[] process(Object data) {
        Document gaDocument = null;
        if (data instanceof Document) {
            gaDocument = (Document) data;
        } else {
            return null;
        }
        Object[] result = processXMLDocument(gaDocument);
        return result;
    }

    public void reset(Object arg0) {
        // TODO Auto-generated method stub

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

    public void setRmdDryModeHandler(DryModeHandler rmdDryModeHandler) {
        this.rmdDryModeHandler = rmdDryModeHandler;
    }

    public void start() {

    }

    public void stop() {

    }

    /**
     * Updates the artifact. Do not forget to call the conflict resolution
     * method of this class once the current version of the target artifact is
     * known.
     * 
     * @param gaDocument
     * @return
     */
    public abstract Document updateArtifact(Document gaDocument);

    public abstract Document updateAttachment(Document gaDocument);

    public abstract Document updateDependency(Document gaDocument);

    @SuppressWarnings("unchecked")
    public void validate(List exceptions) {
        if (getConnectionManager() == null) {
            log.error("connectionManager property is not set");
            exceptions.add(new ValidationException(
                    "connectionManager property is not set", this));
        }
    }

    protected Object[] processXMLDocument(Document gaDocument) {
        Element element = null;
        int numberOfTries = 1;
        boolean retry = false;
        Object[] returnValue = null;
        do {
            int msToSleep = (numberOfTries - 1)
                    * connectionManager.getRetryIncrementTime();
            int maxMsToSleep = connectionManager.getMaximumRetryWaitingTime();

            try {
                element = XPathUtils.getRootElement(gaDocument);
                String artifactAction = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.ARTIFACT_ACTION);
                String artifactType = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.ARTIFACT_TYPE);
                String repositoryMappingDirectionId = XPathUtils
                        .getAttributeValue(element,
                                GenericArtifactHelper.SOURCE_SYSTEM_KIND);
                String dryRunModeValue = rmdDryModeHandler
                        .getDryRunModeValueFromCache(repositoryMappingDirectionId);

                if (!artifactAction
                        .equals(GenericArtifactHelper.ARTIFACT_ACTION_IGNORE)
                        && DryModeHandler
                                .isDryRunEqualsAfterTransformation(dryRunModeValue)) {
                    String cause = "Storing in hospital as dryrun mode is enabled for the repository mapping direction id:"
                            + repositoryMappingDirectionId;
                    XPathUtils.addAttribute(element,
                            GenericArtifactHelper.ERROR_CODE,
                            GenericArtifact.ERROR_IN_DRY_RUN_MODE);
                    log.warn(cause);
                    throw new CCFRuntimeException(cause);
                }
                if (artifactAction
                        .equals(GenericArtifactHelper.ARTIFACT_ACTION_IGNORE)) {
                    return new Object[] { gaDocument };
                } else if (artifactAction
                        .equals(GenericArtifactHelper.ARTIFACT_ACTION_CREATE)) {
                    if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_PLAIN_ARTIFACT)) {
                        gaDocument = this.createArtifact(gaDocument);
                    } else if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
                        returnValue = this.createAttachment(gaDocument);
                    } else if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_DEPENDENCY)) {
                        gaDocument = this.createDependency(gaDocument);
                    } else if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_UNKNOWN)) {
                        String message = "Incoming artifact's type is Unknown. Quarantining it";
                        log.error(message);
                        throw new CCFRuntimeException(message);
                    }
                } else if (artifactAction
                        .equals(GenericArtifactHelper.ARTIFACT_ACTION_UPDATE)
                        || artifactAction
                                .equals(GenericArtifactHelper.ARTIFACT_ACTION_RESYNC)) {
                    if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_PLAIN_ARTIFACT)) {
                        gaDocument = this.handleArtifactUpdate(gaDocument);
                    } else if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
                        gaDocument = this.updateAttachment(gaDocument);
                    } else if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_DEPENDENCY)) {
                        gaDocument = this.updateDependency(gaDocument);
                    } else if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_UNKNOWN)) {
                        String message = "Incoming artifact's type is Unknown. Ignoring it";
                        log.error(message);
                        throw new CCFRuntimeException(message);
                    }
                } else if (artifactAction
                        .equals(GenericArtifactHelper.ARTIFACT_ACTION_DELETE)) {
                    if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_PLAIN_ARTIFACT)) {
                        gaDocument = this.deleteArtifact(gaDocument);
                    } else if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
                        returnValue = this.deleteAttachment(gaDocument);
                    } else if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_DEPENDENCY)) {
                        gaDocument = this.deleteDependency(gaDocument);
                    } else if (artifactType
                            .equals(GenericArtifactHelper.ARTIFACT_TYPE_UNKNOWN)) {
                        String message = "Incoming artifact's type is Unknown. Ignoring it";
                        log.error(message);
                        throw new CCFRuntimeException(message);
                    }
                } else if (artifactAction
                        .equals(GenericArtifactHelper.ARTIFACT_ACTION_UNKNOWN)) {
                    String message = "Incoming artifact's action is Unknown. Ignoring it";
                    log.error(message);
                    throw new CCFRuntimeException(message);
                }
                retry = false;
            } catch (GenericArtifactParsingException e) {
                String message = "Could not parse incoming GenericArtifact XML";
                log.error(message, e);
                throw new CCFRuntimeException(message, e);
            } catch (Exception e) {
                boolean connectionException = connectionManager
                        .isUseStandardTimeoutHandlingCode()
                        && this.handleException(e, connectionManager,
                                gaDocument);
                if (!connectionException) {
                    retry = false;
                    if (e instanceof CCFRuntimeException) {
                        throw (CCFRuntimeException) e;
                    } else if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new CCFRuntimeException("An exception occured", e);
                    }
                } else {
                    retry = true;
                    if (numberOfTries == 1) {
                        // first try, long error message
                        log.warn(
                                "Network related problem occurred while connecting to external system. Try operation again",
                                e);
                    } else if (msToSleep < maxMsToSleep) {
                        // error occurred again, short error message, go to
                        // sleep
                        // we switched to a linear increase of the timeout
                        // value, may
                        // have to revisit this decision later
                        // int timeOut = (int) Math.pow(2, numberOfTries);
                        log.warn("Network related error occurred again ("
                                + e.getMessage()
                                + "), incremented timeout, now sleeping for "
                                + msToSleep + " milliseconds.");
                        try {
                            Thread.sleep(msToSleep);
                        } catch (InterruptedException e1) {
                            log.error("Interrupted sleep in timeout method: ",
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
                            log.error("Interrupted sleep in timeout method: ",
                                    e1);
                        }
                    }
                }
            }
            ++numberOfTries;
        } while (retry);

        if (returnValue != null) {
            return returnValue;
        } else if (gaDocument == null) {
            returnValue = new Object[] {};
        } else {
            returnValue = new Object[] { gaDocument };
        }
        return returnValue;
    }

    private Document handleArtifactUpdate(Document gaDocument) {
        return this.updateArtifact(gaDocument);
    }

    /**
     * This method should be called by every writer when the version of the
     * target artifact that should be updated is known It will return true if it
     * was able to solve the conflicts or no conflict has been detected. If it
     * returns false, the update must not happen but the generic artifact passed
     * to this method will be changed and has to be serialized to XML as return
     * value of the update method. It may also happen that this method throws a
     * CCFRuntimeException to quarantine an artifact.
     * 
     * @param artifactCurrentVersion
     *            version of the target artifact, the update method likes to
     *            change
     * @param gaDocument
     *            document passed to the update method
     * @return true if update can happen, false if update must not happen and
     *         modified generic artifact has to be returned
     */
    public static boolean handleConflicts(long artifactCurrentVersion,
            GenericArtifact gaDocument) {
        long lastSyncVersion = -1;
        String targetArtifactId = gaDocument.getTargetArtifactId();
        String lastSyncVersionStr = gaDocument.getTargetArtifactVersion();
        if (lastSyncVersionStr == null
                || lastSyncVersionStr
                        .equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
            lastSyncVersionStr = GenericArtifactHelper.ARTIFACT_VERSION_FORCE_RESYNC;
        }
        try {
            lastSyncVersion = Long.parseLong(lastSyncVersionStr);
        } catch (NumberFormatException e) {
            String message = "Last successful synchronization version of artifact "
                    + targetArtifactId
                    + " is not a number "
                    + lastSyncVersionStr;
            log.error(message, e);
            throw new CCFRuntimeException(message, e);
        }
        String conflictResolutionPriority = gaDocument
                .getConflictResolutionPriority();

        if (lastSyncVersion < artifactCurrentVersion) {
            String sourceArtifactId = gaDocument.getSourceArtifactId();
            String sourceSystemId = gaDocument.getSourceSystemId();
            String sourceRepositoryId = gaDocument.getSourceRepositoryId();
            String targetSystemId = gaDocument.getTargetSystemId();
            String targetRepositoryId = gaDocument.getTargetRepositoryId();

            String transactionId = gaDocument.getTransactionId();
            boolean replayedArtifact = (transactionId != null && !transactionId
                    .equals(GenericArtifact.VALUE_UNKNOWN));
            boolean resynchedArtifact = gaDocument.getArtifactAction().equals(
                    GenericArtifactHelper.ARTIFACT_ACTION_RESYNC);

            if (replayedArtifact || resynchedArtifact) {
                // no matter what the conflict resolution says, if this is a resync
                // or replay, this change has to get through
                logConflictResolutor
                        .warn("Conflict detected for artifact combination "
                                + sourceArtifactId
                                + "-"
                                + sourceRepositoryId
                                + "-"
                                + sourceSystemId
                                + "-"
                                + targetArtifactId
                                + "-"
                                + targetRepositoryId
                                + "-"
                                + targetSystemId
                                + ". However, changes are not ignored since this is an explicit "
                                + (replayedArtifact ? "replay" : "resync")
                                + " request.");
                return true;
            }

            if (conflictResolutionPriority
                    .equals(GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_IGNORE)) {
                logConflictResolutor
                        .warn("Conflict detected for artifact combination "
                                + sourceArtifactId + "-" + sourceRepositoryId
                                + "-" + sourceSystemId + "-" + targetArtifactId
                                + "-" + targetRepositoryId + "-"
                                + targetSystemId + ". Changes are ignored.");

                gaDocument
                        .setArtifactAction(GenericArtifact.ArtifactActionValue.IGNORE);
                return false;
            } else if (conflictResolutionPriority
                    .equals(GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_QUARANTINE_ARTIFACT)) {
                String message = "Conflict detected for artifact combination "
                        + sourceArtifactId + "-" + sourceRepositoryId + "-"
                        + sourceSystemId + "-" + targetArtifactId + "-"
                        + targetRepositoryId + "-" + targetSystemId;

                logConflictResolutor.warn(message
                        + ". Artifact is quarantined in hospital.");
                gaDocument
                        .setErrorCode(GenericArtifact.ERROR_CONFLICT_DETECTED);
                throw new CCFRuntimeException(message);
            } else if ((conflictResolutionPriority
                    .equals(GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_OVERRIDE))
                    || conflictResolutionPriority
                            .equals(GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_OVERRIDE_AND_IGNORE_LOCKS)) {
                logConflictResolutor
                        .warn("Conflict detected for artifact combination "
                                + sourceArtifactId + "-" + sourceRepositoryId
                                + "-" + sourceSystemId + "-" + targetArtifactId
                                + "-" + targetRepositoryId + "-"
                                + targetSystemId + ". Changes are overridden.");

                return true;
            } else {
                String message = "Conflict detected for artifact combination "
                        + sourceArtifactId + "-" + sourceRepositoryId + "-"
                        + sourceSystemId + "-" + targetArtifactId + "-"
                        + targetRepositoryId + "-" + targetSystemId;

                logConflictResolutor
                        .warn(message
                                + ". Since conflict resolution priority "
                                + conflictResolutionPriority
                                + " is unknown, the artifact is quarantined in the hospital.");

                gaDocument
                        .setErrorCode(GenericArtifact.ERROR_CONFLICT_DETECTED);
                throw new CCFRuntimeException(message);
            }
        } else {
            return true;
        }
    }

}
