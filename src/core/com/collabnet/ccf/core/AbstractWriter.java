package com.collabnet.ccf.core;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.XPathUtils;

public abstract class AbstractWriter<T> extends LifecycleComponent implements
		IDataProcessor {
	private static final Log log = LogFactory.getLog(AbstractWriter.class);
	private static final Log logConflictResolutor = LogFactory
			.getLog("com.collabnet.ccf.core.conflict.resolution");
	private ConnectionManager<T> connectionManager = null;

	public AbstractWriter(String id) {
		super(id);
	}

	public AbstractWriter() {
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

	private Object[] processXMLDocument(Document gaDocument) {
		Element element = null;
		int numberOfTries = 1;
		boolean retry = false;

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
						gaDocument = this.createAttachment(gaDocument);
					} else if (artifactType
							.equals(GenericArtifactHelper.ARTIFACT_TYPE_DEPENDENCY)) {
						gaDocument = this.createDependency(gaDocument);
					} else if (artifactType
							.equals(GenericArtifactHelper.ARTIFACT_TYPE_UNKNOWN)) {
						String message = "Incoming artifact's type is Unknown. Ignoring it";
						log.error(message);
						throw new CCFRuntimeException(message);
					}
				} else if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_UPDATE) ||
						artifactAction
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
						gaDocument = this.deleteAttachment(gaDocument);
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
			} catch (GenericArtifactParsingException e) {
				String message = "Could not parse incoming GenericArtifact XML";
				log.error(message, e);
				throw new CCFRuntimeException(message, e);
			} catch (Exception e) {
				boolean connectionException = connectionManager
						.isUseStandardTimeoutHandlingCode()
						&& this.handleException(e, connectionManager);
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
						log
								.warn(
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
						log
								.warn("Network related error occurred again, switched to maximum waiting time ("
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
		Object[] returnValue = null;
		if (gaDocument == null) {
			returnValue = new Object[] {};
		} else {
			returnValue = new Object[] { gaDocument };
		}
		return returnValue;
	}

	public boolean handleException(Throwable rootCause,
			ConnectionManager<T> connectionManager) {
		return false;
	}

	public abstract Document deleteDependency(Document gaDocument);

	public abstract Document deleteAttachment(Document gaDocument);

	public abstract Document deleteArtifact(Document gaDocument);

	public abstract Document updateDependency(Document gaDocument);

	public abstract Document updateAttachment(Document gaDocument);

	private Document handleArtifactUpdate(Document gaDocument) {
		int artifactCurrentVersion = this.getArtifactVersion(gaDocument);
		int lastSyncVersion = -1;
		try {
			Element element = XPathUtils.getRootElement(gaDocument);
			String targetArtifactId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.TARGET_ARTIFACT_ID);
			String lastSyncVersionStr = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.TARGET_ARTIFACT_VERSION);
			if (lastSyncVersionStr == null
					|| lastSyncVersionStr
							.equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
				lastSyncVersionStr = GenericArtifactHelper.ARTIFACT_VERSION_FORCE_RESYNC;
			}
			try {
				lastSyncVersion = Integer.parseInt(lastSyncVersionStr);
			} catch (NumberFormatException e) {
				String message = "Last successful synchronization version of artifact "
						+ targetArtifactId
						+ " is not a number "
						+ lastSyncVersionStr;
				log.error(message, e);
				throw new CCFRuntimeException(message, e);
			}
			String conflictResolutionPriority = XPathUtils
					.getAttributeValue(element,
							GenericArtifactHelper.CONFLICT_RESOLUTION_PRIORITY);

			if (lastSyncVersion < artifactCurrentVersion) {
				String sourceArtifactId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.SOURCE_ARTIFACT_ID);
				String sourceSystemId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.SOURCE_SYSTEM_ID);
				String sourceRepositoryId = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.SOURCE_REPOSITORY_ID);
				String targetSystemId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TARGET_SYSTEM_ID);
				String targetRepositoryId = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.TARGET_REPOSITORY_ID);

				if (conflictResolutionPriority
						.equals(GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_IGNORE)) {
					logConflictResolutor
							.warn("Conflict detected for artifact combination"
									+ sourceArtifactId + "-"
									+ sourceRepositoryId + "-" + sourceSystemId
									+ "-" + targetArtifactId
									+ targetRepositoryId + "-" + targetSystemId
									+ ". Changes are ignored.");

					XPathUtils.addAttribute(element,
							GenericArtifactHelper.ARTIFACT_ACTION,
							GenericArtifactHelper.ARTIFACT_ACTION_IGNORE);

				} else if (conflictResolutionPriority
						.equals(GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_QUARANTINE_ARTIFACT)) {
					String message = "Conflict detected for artifact combination"
							+ sourceArtifactId
							+ "-"
							+ sourceRepositoryId
							+ "-"
							+ sourceSystemId
							+ "-"
							+ targetArtifactId
							+ targetRepositoryId + "-" + targetSystemId;

					logConflictResolutor.warn(message
							+ ". Artifact is quarantined in hospital.");

					XPathUtils.addAttribute(gaDocument.getRootElement(),
							GenericArtifactHelper.ERROR_CODE,
							GenericArtifact.ERROR_CONFLICT_DETECTED);
					throw new CCFRuntimeException(message);
				} else if ((conflictResolutionPriority
						.equals(GenericArtifact.VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_OVERRIDE))) {
					logConflictResolutor
							.warn("Conflict detected for artifact combination"
									+ sourceArtifactId + "-"
									+ sourceRepositoryId + "-" + sourceSystemId
									+ "-" + targetArtifactId
									+ targetRepositoryId + "-" + targetSystemId
									+ ". Changes are overridden.");

					gaDocument = this.updateArtifact(gaDocument,
							conflictResolutionPriority);
				} else {
					String message = "Conflict detected for artifact combination"
							+ sourceArtifactId
							+ "-"
							+ sourceRepositoryId
							+ "-"
							+ sourceSystemId
							+ "-"
							+ targetArtifactId
							+ targetRepositoryId + "-" + targetSystemId;

					logConflictResolutor
							.warn(message
									+ ". Since conflict resolution priority "
									+ conflictResolutionPriority
									+ " is unknown, the artifact is quarantined in the hospital.");

					XPathUtils.addAttribute(gaDocument.getRootElement(),
							GenericArtifactHelper.ERROR_CODE,
							GenericArtifact.ERROR_CONFLICT_DETECTED);
					throw new CCFRuntimeException(message);
				}
			} else {
				gaDocument = this.updateArtifact(gaDocument,
						conflictResolutionPriority);
			}
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the XML document to extract top-level attributes";
			log.error(cause, e);
			XPathUtils.addAttribute(gaDocument.getRootElement(),
					GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		return gaDocument;
	}

	/**
	 * Updates the artifact. The conflict detection and resolution has been
	 * already implemented in the Abstract writer. However, if concurrent
	 * updates happen the conflict resolution priority is needed again. Every
	 * writer component has to implement concurrent access detection and
	 * conflict resolution for this scenario by itself.
	 * 
	 * @param gaDocument
	 * @param conflictResolutionPriority
	 * @return
	 */
	public abstract Document updateArtifact(Document gaDocument,
			String conflictResolutionPriority);

	public abstract int getArtifactVersion(Document gaDocument);

	public abstract Document createDependency(Document gaDocument);

	public abstract Document createAttachment(Document gaDocument);

	public abstract Document createArtifact(Document gaDocument);

	public void reset(Object arg0) {
		// TODO Auto-generated method stub

	}

	public void start() {

	}

	public void stop() {

	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		super.validate(exceptions);
		if (getConnectionManager() == null) {
			log.error("connectionManager property is not set");
			exceptions.add(new ValidationException(
					"connectionManager property is not set", this));
		}
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
}
