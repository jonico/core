package com.collabnet.ccf.swp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.swp.SWPMetaData.SWPType;
import com.danube.scrumworks.api2.client.RevisionInfo;

/**
 * SWP Reader component
 * 
 * @author jnicolai
 * 
 */
public class SWPReader extends AbstractReader<Connection> {
	/**
	 * This map is used to determine whether a specific entity type is ready for
	 * transmitting its artifacts. This prevents cases where child artifacts are
	 * created before their parent artifacts.
	 */
	private Map<String, Long> lastRevisionInQueue = new HashMap<String, Long>();

	private String username;
	private String password;
	private String serverUrl;
	private String resyncUserName;
	private SWPHandler swpHandler = null;

	/**
	 * Whenever an program is added to a product, new program epics can show up
	 * Since a program addition is only visible due to a change in the product
	 * (not in the epics) we use this variable to determine whether we have to
	 * resynchronize the meta data (themes, sprints, teams)
	 */
	private Map<String, Boolean> triggerMetaDataResynchronization = new HashMap<String, Boolean>();

	/**
	 * This variable determines whether PBIs have been shipped in the last
	 * getChangedPBIs call
	 */
	private Map<String, Boolean> shippedPBIsInLastCall = new HashMap<String, Boolean>();

	/**
	 * This variable is used to determine whether higher prioritized entity
	 * types are shipped first This property is set to false by default because
	 * this will not enable you to sync one SWP entity type to multiple target
	 * systems using the same CCF version or to just synch one low priority
	 * entity type
	 */
	private boolean serializeArtifactShipments = false;

	private boolean ignoreConnectorUserUpdates = true;

	/**
	 * Determines whether after every PBI shipment, SWP meta data should be
	 * resynched
	 */
	private boolean triggerMetaDataResynchronizationAfterPBIShipment = false;

	/**
	 * Sets whether updated and created artifacts from the connector user should
	 * be ignored This is the default behavior to avoid infinite update loops.
	 * However, in artifact export scenarios, where all artifacts should be
	 * extracted, this property should be set to false
	 * 
	 * @param ignoreConnectorUserUpdates
	 *            whether to ignore artifacts that have been created or lastly
	 *            modified by the connector user
	 */
	public void setIgnoreConnectorUserUpdates(boolean ignoreConnectorUserUpdates) {
		this.ignoreConnectorUserUpdates = ignoreConnectorUserUpdates;
	}

	/**
	 * Retrieves whether updated and created artifacts from the connector user
	 * should be ignored This is the default behavior to avoid infinite update
	 * loops. However, in artifact export scenarios, where all artifacts should
	 * be extracted, this property should is set to false
	 * 
	 * @return the ignoreConnectorUserUpdates whether to ignore artifacts that
	 *         have been created or lastly modified by the connector user
	 */
	public boolean isIgnoreConnectorUserUpdates() {
		return ignoreConnectorUserUpdates;
	}

	private static final Log log = LogFactory.getLog(SWPReader.class);

	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			GenericArtifact artifactData) {
		// TODO return attachments
		return new ArrayList<GenericArtifact>();
	}

	@Override
	public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
		
		boolean ignoreResyncUser = false;

		if (lastSynchronizedVersion == null
				|| lastSynchronizedVersion.equals("0")
				|| !ignoreConnectorUserUpdates) {
			ignoreResyncUser = true;
		}

		// find out what to extract
		SWPType swpType = SWPMetaData
				.retrieveSWPTypeFromRepositoryId(sourceRepositoryId);
		String swpProductName = SWPMetaData
				.retrieveProductFromRepositoryId(sourceRepositoryId);
		if (swpType.equals(SWPMetaData.SWPType.UNKNOWN)
				|| swpProductName == null) {
			String cause = "Invalid repository format: " + sourceRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		GenericArtifact genericArtifact = new GenericArtifact();
		
		Connection connection;
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, serverUrl,
					getUsername() + SWPConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SWP system. Max connections reached for "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SWP system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}

		try {
			/**
			 * Create a new generic artifact data structure
			 */
			populateSrcAndDest(syncInfo, genericArtifact);
			genericArtifact.setSourceArtifactId(artifactId);
			genericArtifact.setArtifactMode(ArtifactModeValue.COMPLETE);
			genericArtifact.setArtifactType(ArtifactTypeValue.PLAINARTIFACT);

			if (swpType.equals(SWPMetaData.SWPType.TASK)) {
				swpHandler.retrieveTask(connection.getEndpoint(), artifactId,
						swpProductName, getResyncUserName() == null ? ""
								: getResyncUserName(), ignoreResyncUser,
						genericArtifact);
			} else if (swpType.equals(SWPMetaData.SWPType.PBI)) {
				swpHandler.retrievePBI(connection.getEndpoint(), artifactId,
						swpProductName, getResyncUserName() == null ? ""
								: getResyncUserName(), ignoreResyncUser,
						genericArtifact);
			} else if (swpType.equals(SWPMetaData.SWPType.PRODUCT)) {
				swpHandler.retrieveProduct(connection.getEndpoint(),
						artifactId, swpProductName,
						getResyncUserName() == null ? "" : getResyncUserName(),
						ignoreResyncUser, genericArtifact);
			} else if (swpType.equals(SWPMetaData.SWPType.RELEASE)) {
				swpHandler.retrieveRelease(connection.getEndpoint(),
						artifactId, swpProductName,
						getResyncUserName() == null ? "" : getResyncUserName(),
						ignoreResyncUser, genericArtifact);
			} else if (swpType.equals(SWPMetaData.SWPType.METADATA)) {
				swpHandler.retrieveMetaData(connection.getEndpoint(), artifactId,
						swpProductName, genericArtifact);
			} else {
				String cause = "Unsupported repository format: "
						+ sourceRepositoryId;
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} catch (Exception e) {
			String cause = "During the artifact retrieval process from SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return genericArtifact;
	}

	/**
	 * Releases the connection to the ConnectionManager.
	 * 
	 * @param connection
	 *            - The connection to be released to the ConnectionManager
	 */
	public void disconnect(Connection connection) {
		getConnectionManager().releaseConnection(connection);
	}

	/**
	 * Populates the source and destination attributes for this GenericArtifact
	 * object from the Sync Info database document.
	 * 
	 * @param syncInfo
	 * @param ga
	 */
	private void populateSrcAndDest(Document syncInfo, GenericArtifact ga) {
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String conflictResolutionPriority = this
				.getConflictResolutionPriority(syncInfo);
		
		String sourceSystemTimezone = this.getSourceSystemTimezone(syncInfo);
		String targetSystemTimezone = this.getTargetSystemTimezone(syncInfo);

		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
		String targetRepositoryKind = this.getTargetRepositoryKind(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetSystemKind = this.getTargetSystemKind(syncInfo);

		ga.setSourceRepositoryId(sourceRepositoryId);
		ga.setSourceRepositoryKind(sourceRepositoryKind);
		ga.setSourceSystemId(sourceSystemId);
		ga.setSourceSystemKind(sourceSystemKind);
		ga.setConflictResolutionPriority(conflictResolutionPriority);
		ga.setSourceSystemTimezone(sourceSystemTimezone);

		ga.setTargetRepositoryId(targetRepositoryId);
		ga.setTargetRepositoryKind(targetRepositoryKind);
		ga.setTargetSystemId(targetSystemId);
		ga.setTargetSystemKind(targetSystemKind);
		ga.setTargetSystemTimezone(targetSystemTimezone);
	}

	@Override
	public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId) {
		// SWP does not support dependencies at the moment
		return new ArrayList<GenericArtifact>();
	}

	@Override
	public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);

		String repositoryKey = sourceSystemId + ":" + sourceRepositoryId + ":"
				+ targetSystemId + ":" + targetRepositoryId;

		long majorVersion = 0;
		long minorVersion = 0;
		try {
			majorVersion = Long.parseLong(lastSynchronizedVersion);
			// this happens if CCF terminated during initial artifact synch
			if (majorVersion < 0) {
				majorVersion = 0;
			}
			/*
			 * The minor version determines the last artifact that has been
			 * transported within a single SWP revision The first artifact will
			 * have minor version 2, the second version 4, ... and the last one
			 * will have (number of artifacts in revision) * 2 + 1 to indicate
			 * that this is the last one
			 */
			minorVersion = majorVersion % SWPHandler.SWP_REVISION_FACTOR;
			// normalize major revision number
			majorVersion /= SWPHandler.SWP_REVISION_FACTOR;
		} catch (NumberFormatException e) {
			log.warn("Version string is not a number "
					+ lastSynchronizedVersion, e);
		}

		// find out what to extract
		SWPType swpType = SWPMetaData
				.retrieveSWPTypeFromRepositoryId(sourceRepositoryId);
		String swpProductName = SWPMetaData
				.retrieveProductFromRepositoryId(sourceRepositoryId);
		String correspondingProductRepositoryId = swpProductName
				+ SWPMetaData.REPOSITORY_ID_SEPARATOR + SWPMetaData.PRODUCT;
		String correspondingReleaseRepositoryId = swpProductName
				+ SWPMetaData.REPOSITORY_ID_SEPARATOR + SWPMetaData.RELEASE;
		String correspondingMetaDataRepositoryId = swpProductName
				+ SWPMetaData.REPOSITORY_ID_SEPARATOR + SWPMetaData.METADATA;
		String correspondingPBIRepositoryId = swpProductName
				+ SWPMetaData.REPOSITORY_ID_SEPARATOR + SWPMetaData.PBI;

		if (swpType.equals(SWPMetaData.SWPType.UNKNOWN)
				|| swpProductName == null) {
			String cause = "Invalid repository format: " + sourceRepositoryId;
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		Connection connection;
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, serverUrl,
					getUsername() + SWPConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the SWP system. Max connections reached for "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the SWP system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}

		ArrayList<ArtifactState> artifactStates = new ArrayList<ArtifactState>();
		try {
			if (swpType.equals(SWPType.TASK)) {
				if (!isSerializeArtifactShipments()) {
					swpHandler.getChangedTasks(connection.getEndpoint(),
							swpProductName, artifactStates, majorVersion,
							minorVersion, getUsername(),
							isIgnoreConnectorUserUpdates());
				} else {
					// determine whether higher priority items are still in the
					// queue
					if (getNumberOfWaitingArtifactsForAllTargetSystems(
							sourceSystemId, correspondingPBIRepositoryId) != 0) {
						log
								.debug("Do not query new tasks for "
										+ repositoryKey
										+ " since changes of higher prioritized entity types are still in the queue ...");
					} else {
						// first check out last PBI revision in queue
						Long lastPBIRevisionInQueue = lastRevisionInQueue
								.get(correspondingPBIRepositoryId);
						if (lastPBIRevisionInQueue == null) {
							log
									.debug("Do not query new tasks for "
											+ repositoryKey
											+ " since PBI changes are not yet synched ...");
							return new ArrayList<ArtifactState>();
						}
						// now retrieve changed tasks since last known revision
						swpHandler.getChangedTasks(connection.getEndpoint(),
								swpProductName, artifactStates, majorVersion,
								minorVersion, getUsername(),
								isIgnoreConnectorUserUpdates());
						// check whether new tasks are present
						if (artifactStates.isEmpty()) {
							return new ArrayList<ArtifactState>();
						}
						// now check whether new PBIs have popped up since last
						// task synch that are not synchronized yet
						List<ArtifactState> pbiStates = new ArrayList<ArtifactState>();

						// first, retrieve current revision
						RevisionInfo currentRevison = swpHandler
								.getCurrentRevision(connection.getEndpoint());
						Long currentArtificialRevisionNumber = (currentRevison
								.getRevisionNumber() + 1)
								* SWPHandler.SWP_REVISION_FACTOR;

						swpHandler.getChangedPBIs(connection.getEndpoint(),
								swpProductName, pbiStates,
								lastPBIRevisionInQueue
										/ SWPHandler.SWP_REVISION_FACTOR,
								lastPBIRevisionInQueue
										% SWPHandler.SWP_REVISION_FACTOR,
								getUsername(), isIgnoreConnectorUserUpdates());
						if (!pbiStates.isEmpty()) {
							// determine lastPBIInProduct revision
							Long lastPBIRevisionInProduct = pbiStates.get(
									pbiStates.size() - 1).getArtifactVersion();
							if (lastPBIRevisionInQueue < lastPBIRevisionInProduct) {
								log
										.debug("Do not query new tasks for "
												+ repositoryKey
												+ " since some newer PBI changes are not yet synched ...");
								return new ArrayList<ArtifactState>();
							}
						} else {
							lastRevisionInQueue.put(
									correspondingPBIRepositoryId,
									currentArtificialRevisionNumber);
						}
					}
				}
			} else if (swpType.equals(SWPType.PBI)) {
				if (!isSerializeArtifactShipments()) {
					swpHandler.getChangedPBIs(connection.getEndpoint(),
							swpProductName, artifactStates, majorVersion,
							minorVersion, getUsername(),
							isIgnoreConnectorUserUpdates());
				} else {
					if (isTriggerMetaDataResynchronizationAfterPBIShipment()) {
						Boolean shippedPBIsInLastCallUnboxed = shippedPBIsInLastCall
								.get(swpProductName);
						if (shippedPBIsInLastCallUnboxed != null
								&& shippedPBIsInLastCallUnboxed == true) {
							shippedPBIsInLastCall.put(swpProductName, false);
							// trigger theme resynch
							triggerMetaDataResynchronization.put(swpProductName,
									true);
							return new ArrayList<ArtifactState>();
						}
					}

					// determine whether higher priority items are still in the
					// queue
					if (getNumberOfWaitingArtifactsForAllTargetSystems(
							sourceSystemId, correspondingReleaseRepositoryId) != 0
							|| getNumberOfWaitingArtifactsForAllTargetSystems(
									sourceSystemId,
									correspondingMetaDataRepositoryId) != 0) {
						// reset entity type priority
						log
								.debug("Do not query new PBIs for "
										+ repositoryKey
										+ " since changes of higher prioritized entity types are still in the queue ...");
					} else {
						// first check out last release revision in queue
						Long lastReleaseRevisionInQueue = lastRevisionInQueue
								.get(correspondingReleaseRepositoryId);
						if (lastReleaseRevisionInQueue == null) {
							log
									.debug("Do not query new PBIs for "
											+ repositoryKey
											+ " since release changes are not yet synched ...");
							return new ArrayList<ArtifactState>();
						}

						// now check out last meta data revision in queue
						Long lastMetaDataRevisionInQueue = lastRevisionInQueue
								.get(correspondingMetaDataRepositoryId);

						Boolean triggerThemeResynchronizationUnboxed = triggerMetaDataResynchronization
								.get(swpProductName);
						if (lastMetaDataRevisionInQueue == null
								|| triggerThemeResynchronizationUnboxed == null
								|| triggerThemeResynchronizationUnboxed == true) {
							log
									.debug("Do not query new PBIs for "
											+ repositoryKey
											+ " since theme changes are not yet synched ...");
							return new ArrayList<ArtifactState>();
						}

						// now retrieve changed revisions since last known
						// revision
						// first, retrieve current revision
						RevisionInfo currentRevison = swpHandler
								.getCurrentRevision(connection.getEndpoint());
						Long currentArtificialRevisionNumber = (currentRevison
								.getRevisionNumber() + 1)
								* SWPHandler.SWP_REVISION_FACTOR;

						swpHandler.getChangedPBIs(connection.getEndpoint(),
								swpProductName, artifactStates, majorVersion,
								minorVersion, getUsername(),
								isIgnoreConnectorUserUpdates());

						// check whether new PBIs are present
						if (artifactStates.isEmpty()) {
							lastRevisionInQueue.put(
									correspondingPBIRepositoryId,
									currentArtificialRevisionNumber);
							return new ArrayList<ArtifactState>();
						}
						// now check whether new releases have popped up since
						// last
						// pbi synch that are not synchronized yet
						List<ArtifactState> releaseStates = new ArrayList<ArtifactState>();
						swpHandler.getChangedReleases(connection.getEndpoint(),
								swpProductName, releaseStates,
								lastReleaseRevisionInQueue
										/ SWPHandler.SWP_REVISION_FACTOR,
								lastReleaseRevisionInQueue
										% SWPHandler.SWP_REVISION_FACTOR,
								getUsername(), isIgnoreConnectorUserUpdates());
						if (!releaseStates.isEmpty()) {
							// determine lastReleaseInProduct revision
							Long lastReleaseRevisionInProduct = releaseStates
									.get(releaseStates.size() - 1)
									.getArtifactVersion();
							if (lastReleaseRevisionInQueue < lastReleaseRevisionInProduct) {
								log
										.debug("Do not query new PBIs for "
												+ repositoryKey
												+ " since some newer release changes are not yet synched ...");
								return new ArrayList<ArtifactState>();
							}
						} else {
							lastRevisionInQueue.put(
									correspondingReleaseRepositoryId,
									currentArtificialRevisionNumber);
						}

						// now check whether new themes have popped up since
						// last
						// pbi synch that are not synchronized yet
						List<ArtifactState> metaDataStates = new ArrayList<ArtifactState>();
						swpHandler.getChangedMetaData(connection.getEndpoint(),
								swpProductName, metaDataStates,
								lastMetaDataRevisionInQueue
										/ SWPHandler.SWP_REVISION_FACTOR,
								lastMetaDataRevisionInQueue
										% SWPHandler.SWP_REVISION_FACTOR,
								getUsername(), triggerMetaDataResynchronization
										.get(swpProductName));
						if (!metaDataStates.isEmpty()) {
							// determine lastMeataDataInProduct revision
							Long lastMetaDataRevisionInProduct = metaDataStates.get(
									metaDataStates.size() - 1)
									.getArtifactVersion();
							if (lastMetaDataRevisionInQueue < lastMetaDataRevisionInProduct) {
								log
										.debug("Do not query new PBIs for "
												+ repositoryKey
												+ " since some newer meta data changes are not yet synched ...");
								return new ArrayList<ArtifactState>();
							}
						} else {
							lastRevisionInQueue.put(
									correspondingMetaDataRepositoryId,
									currentArtificialRevisionNumber);
						}
						// update last revision in queue
						lastRevisionInQueue.put(correspondingPBIRepositoryId,
								artifactStates.get(artifactStates.size() - 1)
										.getArtifactVersion());
						// indicate that PBIs have been shipped in this call
						shippedPBIsInLastCall.put(swpProductName, true);
					}
				}
			} else if (swpType.equals(SWPType.PRODUCT)) {
				// first, retrieve current revision
				RevisionInfo currentRevison = swpHandler
						.getCurrentRevision(connection.getEndpoint());
				Long currentArtificialRevisionNumber = (currentRevison
						.getRevisionNumber() + 1)
						* SWPHandler.SWP_REVISION_FACTOR;

				swpHandler.getChangedProducts(connection.getEndpoint(),
						swpProductName, artifactStates, majorVersion,
						minorVersion, getUsername(),
						isIgnoreConnectorUserUpdates());
				// update last revision in queue
				if (!artifactStates.isEmpty()) {
					lastRevisionInQueue.put(correspondingProductRepositoryId,
							artifactStates.get(artifactStates.size() - 1)
									.getArtifactVersion());
				} else {
					lastRevisionInQueue.put(correspondingProductRepositoryId,
							currentArtificialRevisionNumber);
				}
			} else if (swpType.equals(SWPType.RELEASE)) {
				if (!isSerializeArtifactShipments()) {
					swpHandler.getChangedReleases(connection.getEndpoint(),
							swpProductName, artifactStates, majorVersion,
							minorVersion, getUsername(),
							isIgnoreConnectorUserUpdates());
				} else {
					// determine whether higher priority items are still in the
					// queue
					if (getNumberOfWaitingArtifactsForAllTargetSystems(
							sourceSystemId, correspondingProductRepositoryId) != 0) {
						log
								.debug("Do not query new releases for "
										+ repositoryKey
										+ " since changes of higher prioritized entity types are still in the queue ...");
					} else {
						// first check out last product revision in queue
						Long lastProductRevisionInQueue = lastRevisionInQueue
								.get(correspondingProductRepositoryId);
						if (lastProductRevisionInQueue == null) {
							log
									.debug("Do not query new releases for "
											+ repositoryKey
											+ " since product changes are not yet synched ...");
							return new ArrayList<ArtifactState>();
						}

						// first, retrieve current revision
						RevisionInfo currentRevison = swpHandler
								.getCurrentRevision(connection.getEndpoint());
						Long currentArtificialRevisionNumber = (currentRevison
								.getRevisionNumber() + 1)
								* SWPHandler.SWP_REVISION_FACTOR;

						// now retrieve changed revisions since last known
						// revision
						swpHandler.getChangedReleases(connection.getEndpoint(),
								swpProductName, artifactStates, majorVersion,
								minorVersion, getUsername(),
								isIgnoreConnectorUserUpdates());

						// check whether new releases are present
						if (artifactStates.isEmpty()) {
							lastRevisionInQueue.put(
									correspondingReleaseRepositoryId,
									currentArtificialRevisionNumber);
							return new ArrayList<ArtifactState>();
						}
						// now check whether new product changes have popped
						// up since
						// last
						// release synch that are not synchronized yet
						List<ArtifactState> productStates = new ArrayList<ArtifactState>();
						swpHandler.getChangedProducts(connection.getEndpoint(),
								swpProductName, productStates,
								lastProductRevisionInQueue
										/ SWPHandler.SWP_REVISION_FACTOR,
								lastProductRevisionInQueue
										% SWPHandler.SWP_REVISION_FACTOR,
								getUsername(), isIgnoreConnectorUserUpdates());
						if (!productStates.isEmpty()) {
							// determine lastProductInProduct revision
							Long lastProductRevisionInProduct = productStates
									.get(productStates.size() - 1)
									.getArtifactVersion();
							if (lastProductRevisionInQueue < lastProductRevisionInProduct) {
								log
										.debug("Do not query new releases for "
												+ repositoryKey
												+ " since some newer product changes are not yet synched ...");
								return new ArrayList<ArtifactState>();
							}
						} else {
							lastRevisionInQueue.put(
									correspondingProductRepositoryId,
									currentArtificialRevisionNumber);
						}
						// update last revision in queue
						lastRevisionInQueue.put(
								correspondingReleaseRepositoryId,
								artifactStates.get(artifactStates.size() - 1)
										.getArtifactVersion());
					}
				}
			} else if (swpType.equals(SWPType.METADATA)) {
				// first, retrieve current revision
				RevisionInfo currentRevison = swpHandler
						.getCurrentRevision(connection.getEndpoint());
				Long currentArtificialRevisionNumber = (currentRevison
						.getRevisionNumber() + 1)
						* SWPHandler.SWP_REVISION_FACTOR;

				swpHandler.getChangedMetaData(connection.getEndpoint(),
						swpProductName, artifactStates, majorVersion,
						minorVersion, getUsername(),
						triggerMetaDataResynchronization.get(swpProductName));
				triggerMetaDataResynchronization.put(swpProductName, false);
				// update last revision in queue
				if (!artifactStates.isEmpty()) {
					lastRevisionInQueue.put(correspondingMetaDataRepositoryId,
							artifactStates.get(artifactStates.size() - 1)
									.getArtifactVersion());
				} else {
					lastRevisionInQueue.put(correspondingMetaDataRepositoryId,
							currentArtificialRevisionNumber);
				}
			} else {
				String cause = "Unsupported repository format: "
						+ sourceRepositoryId;
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} catch (Exception e) {
			String cause = "During the artifact retrieval process from SWP, an error occured";
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return artifactStates;
	}

	/**
	 * Sets user name for SWP connector user
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.username = userName;
	}

	/**
	 * Sets password for SWP connector user
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = Obfuscator.deObfuscatePassword(password);
	}

	/**
	 * Returns user name for SWP connector user
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns password for SWP connector user
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets URL for SWP server
	 * 
	 * @param serverUrl
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * Returns URL for SWP server
	 * 
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Sets user name of SWP resync user
	 * 
	 * @param resyncUserName
	 */
	public void setResyncUserName(String resyncUserName) {
		this.resyncUserName = resyncUserName;
	}

	@Override
	public boolean handleException(Throwable cause,
			ConnectionManager<Connection> connectionManager) {
		if (cause == null)
			return false;
		if ((cause instanceof java.net.SocketException || cause instanceof java.net.UnknownHostException)
				&& connectionManager.isEnableRetryAfterNetworkTimeout()) {
			return true;
		} else if (cause instanceof ConnectionException
				&& connectionManager.isEnableRetryAfterNetworkTimeout()) {
			return true;
		} else if (cause instanceof RemoteException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		} else if (cause instanceof CCFRuntimeException) {
			Throwable innerCause = cause.getCause();
			return handleException(innerCause, connectionManager);
		}
		return false;
	}

	/**
	 * Gets the user name of the SWP resync user
	 * 
	 * @return
	 */
	public String getResyncUserName() {
		return resyncUserName;
	}

	/**
	 * Connects to the source SWP system using the connectionInfo and
	 * credentialInfo details.
	 * 
	 * This method uses the ConnectionManager configured in the wiring file for
	 * the SWPReader
	 * 
	 * @param systemId
	 *            - The system id of the source SWP system
	 * @param systemKind
	 *            - The system kind of the source SWP system
	 * @param repositoryId
	 *            - The tracker id in the source SWP system
	 * @param repositoryKind
	 *            - The repository kind for the tracker
	 * @param connectionInfo
	 *            - The SWP server URL
	 * @param credentialInfo
	 *            - User name and password concatenated with a delimiter.
	 * @return - The connection object obtained from the ConnectionManager
	 * @throws MaxConnectionsReachedException
	 * @throws ConnectionException
	 */
	public Connection connect(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) throws MaxConnectionsReachedException,
			ConnectionException {
		// log.info("Before calling the parent connect()");
		Connection connection = null;
		connection = getConnectionManager()
				.getConnectionToUpdateOrExtractArtifact(systemId, systemKind,
						repositoryId, repositoryKind, connectionInfo,
						credentialInfo);
		return connection;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);

		if (getResyncUserName() == null) {
			log
					.warn("resyncUserName-property has not been set, so that initial resyncs after artifact creation are not possible.");
		}

		if (StringUtils.isEmpty(getServerUrl())) {
			exceptions.add(new ValidationException(
					"serverUrl-property not set", this));
		}
		if (StringUtils.isEmpty(getUsername())) {
			exceptions.add(new ValidationException("username-property not set",
					this));
		}
		if (getPassword() == null) {
			exceptions.add(new ValidationException("password-property not set",
					this));
		}
		try {
			swpHandler = new SWPHandler();
		} catch (DatatypeConfigurationException e) {
			log.error("Could not initialize SWPHandler");
			exceptions.add(new ValidationException(
					"Could not initialize SWPHandler", this));
		}
	}

	/**
	 * This method is used to define whether higher prioritized entity types are
	 * shipped first This property is set to false by default because this will
	 * not enable you to sync one SWP entity type to multiple target systems
	 * using the same CCF version or to just synch one low priority entity type
	 */
	public void setSerializeArtifactShipments(boolean serializeArtifactShipments) {
		this.serializeArtifactShipments = serializeArtifactShipments;
	}

	/**
	 * This method is used to determine whether higher prioritized entity types
	 * are shipped first This property is set to false by default because this
	 * will not enable you to sync one SWP entity type to multiple target
	 * systems using the same CCF version or to just synch one low priority
	 * entity type
	 */
	public boolean isSerializeArtifactShipments() {
		return serializeArtifactShipments;
	}

	/**
	 * Defines whether after every PBI shipment, SWP meta data should be
	 * resynched
	 */
	public void setTriggerMetaDataResynchronizationAfterPBIShipment(
			boolean triggerMetaDataResynchronizationAfterPBIShipment) {
		this.triggerMetaDataResynchronizationAfterPBIShipment = triggerMetaDataResynchronizationAfterPBIShipment;
	}

	/**
	 * Tells whether after every PBI shipment, SWP meta data should be resynched
	 */
	public boolean isTriggerMetaDataResynchronizationAfterPBIShipment() {
		return triggerMetaDataResynchronizationAfterPBIShipment;
	}
}
