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

package com.collabnet.ccf.pi.qc.v90;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.collabnet.ccf.pi.qc.v90.QCGAHelper.ArtifactInformation;
import com.collabnet.ccf.core.utils.Obfuscator;
import com.collabnet.ccf.pi.qc.v90.api.AttachmentUploadStillInProgressException;
import com.collabnet.ccf.pi.qc.v90.api.DefectAlreadyLockedException;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;

/**
 * QCReader is responsible for reading the defect data from HP Quality Center
 * systems for a domain & project combination.
 * 
 * @author Venugopal Ananthakrishnan
 * 
 */
public class QCReader extends AbstractReader<IConnection> {

	private static final Log log = LogFactory.getLog(QCReader.class);

	private QCHandler artifactHandler;
	private QCGAHelper qcGAHelper;
	private QCAttachmentHandler attachmentHandler;
	private int countBeforeCOMReinitialization = 50000;

	private String serverUrl;

	private String userName;

	private String password;
	private int connectCounts = 0;
	private int commentDescriber = 0;
	private int commentQualifier = 0;

	private boolean comInitialized = false;

	/**
	 * Because QC sometimes reports incorrect attachment sizes if the attachment
	 * upload is still in progress, this property introduces a delay (in
	 * milliseconds, zero by default) before the attachment is downloaded. If
	 * you experience partial attachment downloads, please increase this value.
	 */
	private long delayBeforeAttachmentDownload = 0;
	
	/**
	 * Because QC loops multiple times(until max retry count is reached)
	 * for the same named multiple zero byte attachments,this property helps to configure 
	 * the maximum retry count with user defined value in case if the loop needs to be reduced 
	 * before the attachments are downloaded.
	 */
	private long maximumAttachmentRetryCount=5;
	
	private boolean useAlternativeFieldName = false;
	

	
	public QCReader() {
		super();
		// register clean up routine
		Runtime.getRuntime().addShutdownHook(new CleanUpCOMHookQCReader(this));
	}

	/**
	 * Calls tear-Down method of QCReader
	 * 
	 * @author jnicolai
	 * 
	 */
	private static class CleanUpCOMHookQCReader extends Thread {
		// private static final Log log =
		// LogFactory.getLog(CleanUpCOMHookQCReader.class);

		private QCReader qcReader;

		public CleanUpCOMHookQCReader(QCReader qcReader) {
			this.qcReader = qcReader;
		}

		public void run() {
			qcReader.tearDownCOM();
		}
	}

	/**
	 * Fetches the defect IDs that are changed depending on the synchronization
	 * parameters.
	 * 
	 * @param syncInfo
	 *            Document given by the polling reader of QC system containing
	 *            the source & target system and synchronization information.
	 * 
	 * @return List<String> List of strings that contains the defect ID(s)
	 * 
	 */
	@Override
	public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
		String sourceRepositoryId = getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = getSourceRepositoryKind(syncInfo);
		String sourceSystemId = getSourceSystemId(syncInfo);
		String sourceSystemKind = getSourceSystemKind(syncInfo);

		// String fromTime = convertIntoString(fromTimestamp);
		String transactionId = this.getLastSourceVersion(syncInfo);
		IConnection connection = null;
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, getServerUrl(),
					this.getUserName() + QCConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (ConnectionException e) {
			// The connection will be null if there is an exception thrown
			// if(connection != null) {
			// this.disconnect(connection);
			// }
			String cause = "Could not create connection to the HP QC system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the HP QC system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		// we have to extract defects
		List<ArtifactState> artifactIds = null;

		if (QCConnectionFactory.isDefectRepository(sourceRepositoryId)) {
			try {
				artifactIds = artifactHandler.getLatestChangedDefects(connection, transactionId);
			} catch (Exception e1) {
				String cause = "Error in fetching the defect Ids to be shipped from QC";
				log.error(cause, e1);
				throw new CCFRuntimeException(cause, e1);
			} finally {
				this.disconnect(connection);
			}
			return artifactIds;
		} else {
			try {
				// we have to extract requirements
				String technicalRequirementsId = QCConnectionFactory
						.extractTechnicalRequirementsType(sourceRepositoryId,
								connection);
				artifactIds = artifactHandler.getLatestChangedRequirements(
						connection,
						transactionId, technicalRequirementsId);
			} catch (Exception e1) {
				String cause = "Error in fetching the defect Ids to be shipped from QC";
				log.error(cause, e1);
				throw new CCFRuntimeException(cause, e1);
			} finally {
				this.disconnect(connection);
			}
			return artifactIds;
		}
	}

	@Override
	public boolean handleException(Throwable rootCause,
			ConnectionManager<IConnection> connectionManager) {
		if (rootCause == null)
			return false;
		if (rootCause instanceof ConnectionException) {
			Throwable cause = rootCause.getCause();
			handleException(cause, connectionManager);
			if (connectionManager.isEnableRetryAfterNetworkTimeout()) {
				return true;
			}
		} else if (rootCause instanceof com.jacob.com.ComFailException) {
			com.jacob.com.ComFailException comEx = (com.jacob.com.ComFailException) rootCause;
			String message = comEx.getMessage();
			boolean connectionErrorOccured = false;
			if (message.contains("Server is not available")) {
				connectionErrorOccured = true;
			} else if (message
					.contains("Your Quality Center session has been disconnected")) {
				connectionErrorOccured = true;
				this.reInitCOM();
			} else if (message.contains("The Object is locked by")) {
				// TODO Should we introduce another parameter in the connection
				// manager for this?
				connectionErrorOccured = true;
			} else if (message.contains("The server threw an exception.")) {
				connectionErrorOccured = true;
				this.reInitCOM();
			} else if (message.contains("Invalid field name")) {
				connectionErrorOccured = true;
				this.reInitCOM();
			} else if (message.contains("cannot be used with type")) {
				connectionErrorOccured = true;
				this.reInitCOM();
			} else if (message.contains("Session authenticity broken")) {
				connectionErrorOccured = true;
				this.reInitCOM();
			} else if (message.contains("Server has been disconnected")) {
				connectionErrorOccured = true;
				this.reInitCOM();
			} else if (message.contains("Project is not connected")) {
				connectionErrorOccured = true;
				this.reInitCOM();
			} else if (message.contains("Failed to Connect Project")) {
				log
						.warn("The QC Project might have been de-activated. Please activate this project");
				connectionErrorOccured = true;
				this.reInitCOM();
			}
			// Failed to Connect Project
			if (connectionManager.isEnableRetryAfterNetworkTimeout()
					&& connectionErrorOccured) {
				return true;
			}
		} else if (rootCause instanceof DefectAlreadyLockedException) {
			return true;
		} else if (rootCause instanceof AttachmentUploadStillInProgressException) {
			return true;
		} else if (rootCause instanceof CCFRuntimeException) {
			Throwable cause = rootCause.getCause();
			return handleException(cause, connectionManager);
		}
		return false;
	}

	/**
	 * Fetches the attachment(s) for a given artifactId from the system. *
	 * 
	 * @param syncInfo
	 *            Document given by the polling reader of QC system containing
	 *            the source & target system and synchronization information.
	 * @param artifactId
	 *            A string that represents the defectId for which the
	 *            attachments need to be fetched.
	 * @return List<GenericArtifact> List of GenericArtifact Java Object that
	 *         contains the complete information about the attachment(s)
	 * 
	 */
	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			GenericArtifact artifactData) {
		String sourceArtifactId = artifactData.getSourceArtifactId();
		String sourceRepositoryId = getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = getSourceRepositoryKind(syncInfo);
		String sourceSystemId = getSourceSystemId(syncInfo);
		String sourceSystemKind = getSourceSystemKind(syncInfo);
		String targetRepositoryId = getTargetRepositoryId(syncInfo);
		String targetRepositoryKind = getTargetRepositoryKind(syncInfo);
		String targetSystemId = getTargetSystemId(syncInfo);
		String targetSystemKind = getTargetSystemKind(syncInfo);

		String fromTimestamp = getLastSourceArtifactModificationDate(syncInfo);
		// String fromTime = convertIntoString(fromTimestamp);
		String transactionId = getLastSourceVersion(syncInfo);
		String artifactTransactionID = getArtifactLastModifiedVersion(syncInfo);
		IConnection connection = null;
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, getServerUrl(),
					getUserName() + QCConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the HP QC system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the HP QC system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		List<String> artifactIds = new ArrayList<String>();
		artifactIds.add(sourceArtifactId);
		List<GenericArtifact> attachments = new ArrayList<GenericArtifact>();
		boolean isDefectRepository = QCConnectionFactory
				.isDefectRepository(sourceRepositoryId);
		try {
			if(getIdentityMappingDatabaseReader() != null && artifactTransactionID != null) {
				transactionId = artifactTransactionID;
			}
			attachments = attachmentHandler.getLatestChangedAttachments(
					attachments, connection,
					isIgnoreConnectorUserUpdates() ? getUserName() : " ",
					isIgnoreConnectorUserUpdates() ? getResyncUserName() : "",
					transactionId, fromTimestamp, sourceArtifactId,
					sourceRepositoryId, sourceRepositoryKind, sourceSystemId,
					sourceSystemKind, targetRepositoryId, targetRepositoryKind,
					targetSystemId, targetSystemKind, this
							.getMaxAttachmentSizePerArtifact(), this
							.isShipAttachmentsWithArtifact(),
					isDefectRepository,
					artifactData.getSourceArtifactVersion());
			if (attachments != null) {
				for (GenericArtifact ga : attachments) {
					ga.setSourceArtifactLastModifiedDate(artifactData
							.getSourceArtifactLastModifiedDate());
					ga.setSourceArtifactVersion(ga.getSourceArtifactVersion());
				}
			}
		} catch (AttachmentUploadStillInProgressException e) {
			throw e;
		}catch (Exception e1) {
			String cause = "Error in fetching the attachments from QC";
			log.error(cause, e1);
			throw new CCFRuntimeException(cause, e1);
		} finally {
			this.disconnect(connection);
		}
		return attachments;
	}

	/**
	 * Fetches the defect(s) data for a given artifactId from the system. In the
	 * case of shipping only the latest state of a defect, it returns only 1
	 * artifact element in the list. Whereas in the case of shipping the history
	 * or incremental updates, it ships the history information as individual
	 * GenericArtifacts.
	 * 
	 * @param syncInfo
	 *            Document given by the polling reader of QC system containing
	 *            the source & target system and synchronization information.
	 * @param artifactId
	 *            A string that represents the defectId for which the defects
	 *            need to be fetched.
	 * @return List<GenericArtifact> List of GenericArtifact Java Object that
	 *         contains the complete information about the defect(s)
	 * 
	 */
	@Override
	public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
		String sourceRepositoryId = getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = getSourceRepositoryKind(syncInfo);
		String sourceSystemId = getSourceSystemId(syncInfo);
		String sourceSystemKind = getSourceSystemKind(syncInfo);
		String targetRepositoryId = getTargetRepositoryId(syncInfo);
		String targetRepositoryKind = getTargetRepositoryKind(syncInfo);
		String targetSystemId = getTargetSystemId(syncInfo);
		String targetSystemKind = getTargetSystemKind(syncInfo);
		String sourceSystemTimezone = this.getSourceSystemTimezone(syncInfo);
		String targetSystemTimezone = this.getTargetSystemTimezone(syncInfo);

		// String fromTime = convertIntoString(fromTimestamp);
		String syncInfoTransactionId = this.getLastSourceVersion(syncInfo);
		String artifactLastModifiedVersion = this.getArtifactLastModifiedVersion(syncInfo);
		IConnection connection = null;
		try {
			connection = connect(sourceSystemId, sourceSystemKind,
					sourceRepositoryId, sourceRepositoryKind, getServerUrl(),
					getUserName() + QCConnectionFactory.PARAM_DELIMITER
							+ getPassword());
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the HP QC system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the HP QC system "
					+ serverUrl;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		}
		GenericArtifact latestArtifact = null;
		if (QCConnectionFactory.isDefectRepository(sourceRepositoryId)) {
			try {
				// we do not like to filter the resync user at this place, so we
				// pass an empty string
				ArtifactInformation info = QCGAHelper.getDefectInformation(connection, artifactId, syncInfoTransactionId);

				boolean isResync = false;
				boolean ignoreArtifact = false;
				// we like to get the comments in case of an export even if the
				// last
				// user was the resync user
				if (info.lastModifiedBy == null || info.lastTransactionId == null) {
					return null;
				}
				if (info.lastModifiedBy.equalsIgnoreCase(this.getResyncUserName())
						&& isIgnoreConnectorUserUpdates()) {
					isResync = true;
				} else if (info.lastModifiedBy.equalsIgnoreCase(getUserName()) && isIgnoreConnectorUserUpdates()) {
					if (Integer.parseInt(info.creationTransactionId) > Integer.parseInt(syncInfoTransactionId)) {
						log.info(String.format(
								"resync is necessary, despite the defect %s last being updated by the connector user",
								artifactId));
						isResync = true;
					} else {
						log.info(String.format(
								"defect %s is an ordinary connector update, ignore it.",
								artifactId));
						ignoreArtifact = true;
					}
				}
				QCDefect latestDefect = null;
				try {
					if(getIdentityMappingDatabaseReader() != null && artifactLastModifiedVersion != null) {
						syncInfoTransactionId = artifactLastModifiedVersion;
					}
					if (ignoreArtifact) {
						latestArtifact = new GenericArtifact();
						latestArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.IGNORE);
					} else {
						latestDefect = qcGAHelper.getDefectWithId(connection,
								Integer.parseInt(artifactId));
						latestArtifact = latestDefect.getGenericArtifactObject(
								connection, 
								info.lastTransactionId, 
								artifactId,
								getCommentDescriber(),
								getCommentQualifier(),
								null,
								syncInfoTransactionId,
								isIgnoreConnectorUserUpdates() ? getUserName() : " ",
								isIgnoreConnectorUserUpdates() ? getResyncUserName() : " ",
								artifactHandler,
								sourceSystemTimezone,
								info.lastModifiedBy);
					}
					
					artifactHandler.adjustLastModificationDate(latestArtifact, info.lastModifiedDate, true);
					latestArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
					latestArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
					latestArtifact.setErrorCode("ok");
					latestArtifact.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
					artifactHandler.assignValues(latestArtifact,
							artifactId, sourceRepositoryId,
							sourceRepositoryKind, sourceSystemId,
							sourceSystemKind, targetRepositoryId,
							targetRepositoryKind, targetSystemId,
							targetSystemKind, info.lastTransactionId,
							sourceSystemTimezone, targetSystemTimezone);

					if (isResync) {
						latestArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.RESYNC);
					}
					
				} finally {
					if (latestDefect != null) {
						latestDefect.safeRelease();
						latestDefect = null;
					}
				}
			} catch (Exception e1) {
				String cause = "Error in fetching the defects from QC";
				log.error(cause, e1);
				throw new CCFRuntimeException(cause, e1);
			} finally {
				this.disconnect(connection);
			}
		} else {
			// we are going to extract requirements
			try {
				String technicalRequirementsTypeId = QCConnectionFactory
						.extractTechnicalRequirementsType(sourceRepositoryId,
								connection);
				QCGAHelper.ArtifactInformation info = QCGAHelper.getRequirementInformation(connection, artifactId, syncInfoTransactionId);
				boolean isResync = false;
				if (info == null || info.lastModifiedBy == null || info.lastTransactionId == null) {
					return null;
				}
				// we like to get the comments in case of an export even if the
				// last
				// user was the resync user
				boolean ignoreArtifact = false;
				if (info.lastModifiedBy.equalsIgnoreCase(getResyncUserName())
						&& isIgnoreConnectorUserUpdates()) {
					isResync = true;
				} else if (info.lastModifiedBy.equalsIgnoreCase(getUserName())
						&& isIgnoreConnectorUserUpdates()) {
					if (Integer.parseInt(info.creationTransactionId) > Integer.parseInt(syncInfoTransactionId)) {
						log.info(String.format(
								"resync is necessary, despite the requirement %s last being updated by the connector user",
								artifactId));
						isResync = true;
					} else {
						log.info(String.format(
								"requirement %s is an ordinary connector update, ignore it.",
								artifactId));
						ignoreArtifact = true;
					}
				}
				
				QCRequirement latestRequirement = null;
				try {
					if(getIdentityMappingDatabaseReader() != null && artifactLastModifiedVersion != null) {
						syncInfoTransactionId = artifactLastModifiedVersion;
					}
					// don't retrieve the complete artifact if we'll just ignore it anyway
					if (ignoreArtifact) {
						latestArtifact = new GenericArtifact();
						latestArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.IGNORE);
					} else {
						latestRequirement = qcGAHelper.getRequirementWithId(
								connection, Integer.parseInt(artifactId));

						latestArtifact = latestRequirement.getGenericArtifactObject(
								connection,
								info.lastTransactionId,
								artifactId,
								this.getCommentDescriber(),
								this.getCommentQualifier(),
								null,
								syncInfoTransactionId,
								isIgnoreConnectorUserUpdates() ? getUserName() : " ",
								isIgnoreConnectorUserUpdates() ? getResyncUserName() : " ",
								artifactHandler,
								sourceSystemTimezone,
								technicalRequirementsTypeId,
								info.lastModifiedBy);
						// set information about parent artifact
						String parentId = latestRequirement.getParentId();
						if (parentId != null) {
							if (parentId.equals("-1")) {
								latestArtifact.setDepParentSourceArtifactId(GenericArtifact.VALUE_NONE);
							} else {
								latestArtifact.setDepParentSourceArtifactId(parentId);
								// find out requirement type of parent
								QCRequirement parentRequirement = null;
								try {
									parentRequirement = qcGAHelper.getRequirementWithId(
											connection,
											Integer.parseInt(parentId));
									String parentRequirementType = parentRequirement.getTypeId();
									latestArtifact.setDepParentSourceRepositoryId(
											QCConnectionFactory.generateDependentRepositoryId(
															sourceRepositoryId,
															parentRequirementType));
								} finally {
									if (parentRequirement != null) {
										parentRequirement.safeRelease();
										parentRequirement = null;
									}
								}
							}
						}

					}
					
					// set common attributes for which we have the values anyway.
					artifactHandler.adjustLastModificationDate(latestArtifact, info.lastModifiedDate, false);
					latestArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
					latestArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
					latestArtifact.setErrorCode("ok");
					latestArtifact.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);

					artifactHandler.assignValues(latestArtifact,
							artifactId, sourceRepositoryId,
							sourceRepositoryKind, sourceSystemId,
							sourceSystemKind, targetRepositoryId,
							targetRepositoryKind, targetSystemId,
							targetSystemKind, info.lastTransactionId,
							sourceSystemTimezone, targetSystemTimezone);


					if (isResync) {
						latestArtifact
								.setArtifactAction(GenericArtifact.ArtifactActionValue.RESYNC);
					}
				} finally {
					if (latestRequirement != null) {
						latestRequirement.safeRelease();
						latestRequirement = null;
					}
				}
			} catch (Exception e1) {
				String cause = "Error in fetching the artifacts from QC";
				log.error(cause, e1);
				throw new CCFRuntimeException(cause, e1);
			} finally {
				this.disconnect(connection);
			}
		}
		return latestArtifact;
	}

	/**
	 * TO BE DONE: Fetches the dependencies that are changed depending on the
	 * synchronization parameters.
	 * 
	 * @param syncInfo
	 *            Document given by the polling reader of QC system containing
	 *            the source & target system and synchronization information.
	 * 
	 * @return List<GenericArtifact> List of strings that contains the
	 *         GenericArtifact Java objects containing the complete information
	 *         about the dependencies.
	 * 
	 */
	@Override
	public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId) {
		// TODO Auto-generated method stub
		return new ArrayList<GenericArtifact>();
	}

	/**
	 * Disconnects from the QC using the ConnectionManager.
	 * 
	 * @param connection
	 */
	private void disconnect(IConnection connection) {
		getConnectionManager().releaseConnection(connection);
	}

	public Object[] process(Object data) {
		Object[] result = null;
		if (this.connectCounts == 0) {
			initCOM();
		}
		try {
			result = super.process(data);
		} finally {
			if (this.connectCounts >= getCountBeforeCOMReinitialization()) {
				this.connectCounts = 0;
				tearDownCOM();
			}
		}
		return result;
	}

	private void reInitCOM() {
		this.tearDownCOM();
		this.initCOM();
	}

	private void initCOM() {
		synchronized (log) {
			if (!comInitialized) {
				ComHandle.initCOM();
				comInitialized = true;
			}
		}
	}

	public void tearDownCOM() {
		synchronized (log) {
			if (comInitialized) {
				getConnectionManager().tearDown();
				ComHandle.tearDownCOM();
				comInitialized = false;
			}
		}
	}

	/**
	 * Establish a connection with QC system
	 * 
	 * @param systemId
	 *            Id indicating a QC system.
	 * @param systemKind
	 *            Indicates whether it is a DEFECT or TEST and so on.
	 * @param repositoryId
	 *            Specifies the name of DOMAIN and PROJECT in QC system to which
	 *            connection needs to established.
	 * @param repositoryKind
	 *            Indicates which version of QC like QC 9.0, QC9.2.
	 * @param connectionInfo
	 *            The server URL
	 * @param credentialInfo
	 *            Username and password needed for establishing a connection
	 * @return IConnection The connection object
	 * @throws MaxConnectionsReachedException
	 * 
	 */
	public IConnection connect(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo) throws ConnectionException,
			MaxConnectionsReachedException {
		log.debug("Getting QC connection...!!!");
		IConnection connection = null;
		connection = getConnectionManager()
				.getConnectionToUpdateOrExtractArtifact(systemId, systemKind,
						repositoryId, repositoryKind, connectionInfo,
						credentialInfo);
		this.connectCounts++;
		return connection;
	}

	/**
	 * Checks if the server URL, username and password for the QC system is set.
	 */
	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		super.validate(exceptions);

		/*if (getResyncUserName() == null) {
			log
					.warn("resyncUserName-property has not been set, so that initial resyncs after artifact creation are not possible.");
		}*/

		if (this.getServerUrl() == null) {
			exceptions.add(new ValidationException(
					"serverUrl property is not set for the QCReader", this));
		}
		if (this.getUserName() == null) {
			exceptions.add(new ValidationException(
					"userName property is not set for the QCReader", this));
		}
		if (this.getPassword() == null) {
			exceptions.add(new ValidationException(
					"password property is not set for the QCReader", this));
		}

		if (exceptions.size() == 0) {
			artifactHandler = new QCHandler(isUseAlternativeFieldName());
			attachmentHandler = new QCAttachmentHandler();
			attachmentHandler.setDelayBeforeAttachmentDownload(getDelayBeforeAttachmentDownload());
			attachmentHandler.setMaximumAttachmentRetryCount(getMaximumAttachmentRetryCount());
			qcGAHelper = new QCGAHelper();
		}
	}


	/**
	 * To convert a string into QC specific timestamp
	 * 
	 * @param string
	 *            newFromTime The string to be converted
	 * @return Timestamp The converted timestamp corresponding to string
	 * 
	 */
	public Timestamp convertIntoTimestamp(String newFromTime) {
		Timestamp fromDateInTimestamp = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date fromDate = sdf.parse(newFromTime); // like 2008-01-11 22:09:54
			fromDateInTimestamp = new Timestamp(fromDate.getTime());
			log.info("After parsing, the timestamp is:" + fromDateInTimestamp);
		} catch (Exception e) {
			log.error("Exception while parsing the string into Date" + e);
			// throw new RuntimeException(e);
		}
		return fromDateInTimestamp;
	}

	/**
	 * To convert a QC specific timestamp into a string
	 * 
	 * @param String
	 *            fromTimestamp The timestamp to be converted into its
	 *            corresponding string value
	 * @return string The string representation of the incoming timeStamp
	 */
	public String convertIntoString(String fromTimeStamp) {
		String finalString = null;
		try {
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			Date fromDate = sdf.parse(fromTimeStamp);
			finalString = ft.format(fromDate);
			// System.out.println(finalString);
		} catch (Exception e) {
			log.error("Exception while parsing the string into Date" + e);
			// throw new RuntimeException(e);
		}
		return finalString;
	}

	public void reset(Object context) {
		log.info("Reset request received...!!!");
	}

	/*
	 * @Override public void stop(){ log.info("Got signal to stop QC connector
	 * ..."); while(isConnected){ // This will ensure that the release will be
	 * called // if the thread is in the process method this.connectCounts =
	 * this.countBeforeCOMReinitialization; try { log.info("Got signal to
	 * stop... waiting for the connections to be reliquished");
	 * Thread.sleep(50); } catch (InterruptedException e) { //Digest the
	 * exception break; } } log.info("Stopping...."); tearDownCOM(); }
	 */

	/**
	 * Returns the server URL of the source HP QC system that is configured in
	 * the wiring file.
	 * 
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Sets the source HP QC system's server URL.
	 * 
	 * @param serverUrl
	 *            - the URL of the source HP QC system.
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * Gets the mandatory password that belongs to the username
	 * 
	 * @return the password
	 */
	private String getPassword() {
		return password;
	}

	/**
	 * Sets the password that belongs to the username
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = Obfuscator.deObfuscatePassword(password);
	}

	/**
	 * Gets the mandatory user name The user name is used to login into the HP
	 * QC instance whenever an artifact should be updated or extracted. This
	 * user has to differ from the resync user in order to force initial resyncs
	 * with the source system once a new artifact has been created.
	 * 
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the mandatory username
	 * 
	 * The user name is used to login into the HP QC instance whenever an
	 * artifact should be updated or extracted. This user has to differ from the
	 * resync user in order to force initial resyncs with the source system once
	 * a new artifact has been created.
	 * 
	 * @param userName
	 *            the user name to set
	 */
	public void setUserName(String username) {
		this.userName = username;
	}

	public int getCommentDescriber() {
		return commentDescriber;
	}

	public void setCommentDescriber(int commentDescriber) {
		this.commentDescriber = commentDescriber;
	}

	public int getCommentQualifier() {
		return commentQualifier;
	}

	public void setCommentQualifier(int commentQualifier) {
		this.commentQualifier = commentQualifier;
	}

	/**
	 * Sets the optional resync username
	 * 
	 * The resync user name is used to login into the HP QC instance whenever an
	 * artifact should be created. This user has to differ from the ordinary
	 * user used to log in in order to force initial resyncs with the source
	 * system once a new artifact has been created. This property can also be
	 * set for the reader component in order to be able to differentiate between
	 * artifacts created by ordinary users and artifacts to be resynced.
	 * 
	 * @param resyncUserName
	 *            the resyncUserName to set
	 */
	public void setResyncUserName(String resyncUserName) {
		this.resyncUserName = resyncUserName;
	}

	/**
	 * Gets the optional resync username The resync user name is used to login
	 * into the HP QC instance whenever an artifact should be created. This user
	 * has to differ from the ordinary user used to log in in order to force
	 * initial resyncs with the source system once a new artifact has been
	 * created. This property can also be set for the reader component in order
	 * to be able to differentiate between artifacts created by ordinary users
	 * and artifacts to be resynced.
	 * 
	 * @return the resyncUserName
	 */
	private String getResyncUserName() {
		return resyncUserName;
	}

	/**
	 * Another user name that is used to login into the CEE instance. This user
	 * has to differ from the ordinary user used to log in in order to force
	 * initial resyncs with the source system once a new artifact has been
	 * created.
	 */
	private String resyncUserName;

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

	/**
	 * @param countBeforeCOMReinitialization
	 *            sets the number of CCF operations that will be performed
	 *            before CCF will destroy all COM objects This is to avoid COM
	 *            memory leaks
	 */
	public void setCountBeforeCOMReinitialization(
			int countBeforeCOMReinitialization) {
		this.countBeforeCOMReinitialization = countBeforeCOMReinitialization;
	}

	/**
	 * @return the number of CCF operations that will be performed before CCF
	 *         will destroy all COM objects This is to avoid COM memory leaks
	 */
	public int getCountBeforeCOMReinitialization() {
		return countBeforeCOMReinitialization;
	}

	/**
	 * Because QC sometimes reports incorrect attachment sizes if the attachment
	 * upload is still in progress, this property introduces a delay (in
	 * milliseconds, zero by default) before the attachment is downloaded. If
	 * you experience partial attachment downloads, please increase this value.
	 */
	public void setDelayBeforeAttachmentDownload(
			long delayBeforeAttachmentDownload) {
		this.delayBeforeAttachmentDownload = delayBeforeAttachmentDownload;
	}

	/**
	 * Because QC sometimes reports incorrect attachment sizes if the attachment
	 * upload is still in progress, this property introduces a delay (in
	 * milliseconds, zero by default) before the attachment is downloaded. If
	 * you experience partial attachment downloads, please increase this value.
	 */
	public long getDelayBeforeAttachmentDownload() {
		return delayBeforeAttachmentDownload;
	}

	
	public long getMaximumAttachmentRetryCount() {
		return maximumAttachmentRetryCount;
	}

	public void setMaximumAttachmentRetryCount(long maximumAttachmentRetryCount) {
		this.maximumAttachmentRetryCount = maximumAttachmentRetryCount;
	}

	private boolean ignoreConnectorUserUpdates = true;



	public boolean isUseAlternativeFieldName() {
		return useAlternativeFieldName;
	}

	public void setUseAlternativeFieldName(boolean useAlternativeFieldName) {
		this.useAlternativeFieldName = useAlternativeFieldName;
	}
}
