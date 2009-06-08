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

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.DefectAlreadyLockedException;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;

/**
 * This class writes the incoming defect data into QC making.
 * 
 * @author venugopal
 * 
 */
public class QCWriter extends AbstractWriter<IConnection> implements
		IDataProcessor {

	/**
	 * Another user name that is used to login into the CEE instance. This user
	 * has to differ from the ordinary user used to log in in order to force
	 * initial resyncs with the source system once a new artifact has been
	 * created.
	 */
	private String resyncUserName;

	/**
	 * Password that belongs to the resync user. This user has to differ from
	 * the ordinary user used to log in in order to force initial resyncs with
	 * the source system once a new artifact has been created.
	 */
	private String resyncPassword;

	private static final Log log = LogFactory.getLog(QCWriter.class);
	// private static final Log logConflictResolutor =
	// LogFactory.getLog("com.collabnet.ccf.core.conflict.resolution");
	private QCDefectHandler defectHandler;
	private QCAttachmentHandler attachmentHandler;
	private QCGAHelper qcGAHelper;
	// private ConnectionManager<IConnection> connectionManager = null;
	public final String ARTIFACT_TYPE_PLAINARTIFACT = "plainartifact";
	public final String ARTIFACT_TYPE_ATTACHMENT = "attachment";
	private int connectCounts = 0;
	private int countBeforeCOMReinitialization = 500;

	private String serverUrl;

	private String userName;

	private String password;

	private boolean isConnected = false;

	private boolean comInitialized = false;

	/**
	 * If this property is set to true (true by default), locked defects will be
	 * quarantined and the operation will never be retried
	 */
	private boolean immediatelyQuarantineLockedDefects = true;

	public QCWriter() {
		super();
		Runtime.getRuntime().addShutdownHook(new CleanUpCOMHookQCWriter(this));
	}

	/**
	 * Calls tear-Down method of QCWriter
	 * 
	 * @author jnicolai
	 * 
	 */
	private static class CleanUpCOMHookQCWriter extends Thread {
		// private static final Log log =
		// LogFactory.getLog(CleanUpCOMHookQCReader.class);

		private QCWriter qcWriter;

		public CleanUpCOMHookQCWriter(QCWriter qcWriter) {
			this.qcWriter = qcWriter;
		}

		public void run() {
			qcWriter.tearDownCOM();
		}
	}

	public Object[] process(Object data) {
		Object[] result = null;
		if (this.connectCounts == 0) {
			initCOM();
		}
		try {
			result = super.process(data);
		} finally {
			if (this.connectCounts >= countBeforeCOMReinitialization) {
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
	 * Update the artifact and do conflict resolution
	 * 
	 * @param connection
	 * @param targetArtifactId
	 * @param genericArtifact
	 * @param allFields
	 * @throws Exception
	 */
	protected void updateDefect(IConnection connection,
			String targetArtifactId, GenericArtifact genericArtifact,
			List<GenericArtifactField> allFields) throws Exception {
		@SuppressWarnings("unused")
		String targetSystemTimezone = genericArtifact.getTargetSystemTimezone();

		// retrieve version to update
		List<String> targetAutimeAndTxnIdBeforeUpdate = getAutimeAndTxnId(
				connection, targetArtifactId, null, ARTIFACT_TYPE_PLAINARTIFACT);
		String targetTransactionIdBeforeUpdate = targetAutimeAndTxnIdBeforeUpdate
				.get(0);
		int targetTransactionIdBeforeUpdateInt = Integer
				.parseInt(targetTransactionIdBeforeUpdate);
		// now do conflict resolution
		if (!AbstractWriter.handleConflicts(targetTransactionIdBeforeUpdateInt,
				genericArtifact)) {
			return;
		}

		// IQCDefect updatedArtifact = defectHandler.updateDefect(
		// connection, targetArtifactId, allFields, this
		// .getUserName(), targetSystemTimezone);

		// FIXME This is not atomic
		defectHandler.updateDefect(connection, targetArtifactId, allFields,
				this.getUserName(), targetSystemTimezone);
		log.info("QC Defect " + targetArtifactId + " on "
				+ genericArtifact.getTargetRepositoryId()
				+ " is updated successfully with the changes from "
				+ genericArtifact.getSourceArtifactId() + " on "
				+ genericArtifact.getSourceRepositoryId());
		genericArtifact.setTargetArtifactId(targetArtifactId);
		// FIXME This is not atomic
		List<String> targetAutimeAndTxnId = getAutimeAndTxnId(connection,
				targetArtifactId, null, ARTIFACT_TYPE_PLAINARTIFACT);
		genericArtifact.setTargetArtifactVersion(targetAutimeAndTxnId.get(0));
		genericArtifact.setTargetArtifactLastModifiedDate(DateUtil
				.format(DateUtil.parseQCDate(targetAutimeAndTxnId.get(1))));
	}

	/**
	 * Converts the genericArtifactDocument into GenericArtifact Java object
	 * using the GenericArtifactHelper methods.
	 * 
	 * @param genericArtifactDocument
	 * @return GenericArtifact The converted GenericArtifact from the dom4j
	 *         Document.
	 */
	public static GenericArtifact getArtifactFromDocument(
			Document genericArtifactDocument) {

		GenericArtifact genericArtifact = null;

		try {
			genericArtifact = GenericArtifactHelper
					.createGenericArtifactJavaObject(genericArtifactDocument);
		} catch (GenericArtifactParsingException e) {
			String message = "Exception occured while parsing the Document into a GenericArtifact";
			log.error(message);
			throw new CCFRuntimeException(message, e);
		}

		return genericArtifact;
	}

	public List<String> getAutimeAndTxnId(IConnection qcc, String defectId,
			String attachmentName, String identifier) {

		List<String> txnIdAndAutime = new ArrayList<String>();
		String transactionId = null;
		String auTime = null;
		String sql = null;

		if (identifier.equals(ARTIFACT_TYPE_PLAINARTIFACT))
			sql = "select AU_TIME, AU_ACTION_ID, AU_DESCRIPTION from audit_log where au_entity_id = '"
					+ defectId
					+ "' and au_entity_type='BUG' and au_father_id='-1' order by au_action_id desc";
		else if (identifier.equals(ARTIFACT_TYPE_ATTACHMENT)) {
			if (attachmentName == null) {
				sql = "select AU_TIME, AU_ACTION_ID from audit_log where au_entity_id = '"
						+ defectId
						+ "' and au_entity_type='CROS_REF' and au_father_id != '-1' order by au_action_id desc";
			} else {
				sql = "select AU_TIME, AU_ACTION_ID from audit_log where au_entity_id = '"
						+ defectId
						+ "' and au_entity_type='CROS_REF' and au_description like '%"
						+ QCGAHelper.sanitizeStringForSQLLikeQuery(
								attachmentName, "\\")
						+ "' ESCAPE '\\' and au_father_id != '-1' order by au_action_id desc";
			}
		}

		IRecordSet newRs = null;
		try {
			newRs = QCDefectHandler.executeSQL(qcc, sql);
			int newRc = newRs.getRecordCount();
			log
					.debug("In QCDefectHandler.getTxnIdAndAuDescription, sql="
							+ sql);
			for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
				if (newCnt == 0) {
					transactionId = newRs.getFieldValueAsString("AU_ACTION_ID");
					auTime = newRs.getFieldValueAsString("AU_TIME");
					if (identifier.equals(ARTIFACT_TYPE_PLAINARTIFACT)) {
						String transactionDesc = newRs
								.getFieldValueAsString("AU_DESCRIPTION");
						if (transactionDesc != null) {
							if (transactionDesc.contains("Attachment added:")) {
								int transactionIdInt = Integer
										.parseInt(transactionId);
								transactionIdInt -= 2;
								transactionId = Integer
										.toString(transactionIdInt);
							} else if (transactionDesc
									.contains("Attachment deleted:")) {
								int transactionIdInt = Integer
										.parseInt(transactionId);
								transactionIdInt -= 1;
								transactionId = Integer
										.toString(transactionIdInt);
							}
						}
					}
					break;
				}
			}
		} finally {
			if (newRs != null) {
				newRs.safeRelease();
			}
		}
		txnIdAndAutime.add(transactionId);
		txnIdAndAutime.add(auTime);

		return txnIdAndAutime;
	}

	/**
	 * In the case of memo fields like Comments, the multiple values are
	 * concatinated before writing them into the target system.
	 * 
	 * @param genericArtifact
	 * @return
	 */
	public GenericArtifact concatValuesOfSameFieldNames(
			GenericArtifact genericArtifact) {

		List<GenericArtifactField> allFields = genericArtifact
				.getAllGenericArtifactFields();
		List<String> allFieldNames = new ArrayList<String>();
		for (int cnt = 0; cnt < allFields.size(); cnt++) {
			if (allFields.get(cnt).getFieldName().equals("BG_DEV_COMMENTS")) {
				continue;
			}
			if (!(allFieldNames.contains(allFields.get(cnt).getFieldName()))
					&& genericArtifact
							.getAllGenericArtifactFieldsWithSameFieldName(
									allFields.get(cnt).getFieldName()).size() > 1) {
				List<GenericArtifactField> allSameFields = genericArtifact
						.getAllGenericArtifactFieldsWithSameFieldName(allFields
								.get(cnt).getFieldName());
				HashSet<String> allValues = new HashSet<String>();
				for (GenericArtifactField field : allSameFields) {
					allValues.add((String) field.getFieldValue());
				}
				String EMPTY_STRING = "";
				String concatinatedString = EMPTY_STRING;
				Iterator<String> it = allValues.iterator();
				while (it.hasNext()) {
					String value = it.next();
					if (!StringUtils.isEmpty(value)) {
						if (concatinatedString != EMPTY_STRING) {
							concatinatedString += ";";
						}
						concatinatedString += value;
					}
				}
				genericArtifact.getAllGenericArtifactFieldsWithSameFieldName(
						allFields.get(cnt).getFieldName()).get(0)
						.setFieldValue(concatinatedString);
			}
			allFieldNames.add(allFields.get(cnt).getFieldName());

		}

		return genericArtifact;
	}

	/**
	 * Obtains the value of the specified field from the incoming Document.
	 * 
	 * @param individualGenericArtifact
	 * @param fieldName
	 * @return String
	 */
	public static String getFieldValueFromGenericArtifact(
			GenericArtifact individualGenericArtifact, String fieldName) {

		String fieldValue = null;
		if (individualGenericArtifact.getAllGenericArtifactFields() != null
				&& individualGenericArtifact
						.getAllGenericArtifactFieldsWithSameFieldName(fieldName) != null)
			fieldValue = (String) individualGenericArtifact
					.getAllGenericArtifactFieldsWithSameFieldName(fieldName)
					.get(0).getFieldValue();

		return fieldValue;
	}

	/**
	 * Checks if defect with the incoming defectId exists in the target system.
	 * 
	 * @param bugId
	 * @param connection
	 * @return boolean Returns true if the defect exists, false otherwise.
	 */
	public boolean checkForBugIdInQC(int bugId, IConnection connection) {

		QCDefect thisDefect = qcGAHelper.getDefectWithId(connection, bugId);

		if (thisDefect != null)
			return true;
		else
			return false;
	}

	public IConnection connect(GenericArtifact ga) {
		String targetSystemId = ga.getTargetSystemId();
		String targetSystemKind = ga.getTargetSystemKind();
		String targetRepositoryId = ga.getTargetRepositoryId();
		String targetRepositoryKind = ga.getTargetRepositoryKind();
		IConnection connection;
		try {
			if ((!ga.getArtifactAction().equals(
					GenericArtifact.ArtifactActionValue.CREATE))
					|| getResyncUserName() == null) {
				connection = connect(targetSystemId, targetSystemKind,
						targetRepositoryId, targetRepositoryKind, serverUrl,
						getUserName() + QCConnectionFactory.PARAM_DELIMITER
								+ getPassword(), false);
				connectCounts++;
			} else {
				connection = connect(targetSystemId, targetSystemKind,
						targetRepositoryId, targetRepositoryKind, serverUrl,
						getResyncUserName()
								+ QCConnectionFactory.PARAM_DELIMITER
								+ getResyncPassword(), true);
				connectCounts++;
			}
		} catch (MaxConnectionsReachedException e) {
			String cause = "Could not create connection to the QC system. Max connections reached for "
					+ serverUrl;
			log.error(cause, e);
			ga
					.setErrorCode(GenericArtifact.ERROR_MAX_CONNECTIONS_REACHED_FOR_POOL);
			throw new CCFRuntimeException(cause, e);
		} catch (ConnectionException e) {
			String cause = "Could not create connection to the QC system "
					+ serverUrl;
			log.error(cause, e);
			ga.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
			throw new CCFRuntimeException(cause, e);
		}
		return connection;
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
	 * @param forceResync
	 *            create artifacts by using another account to enforce an
	 *            initial resync after artifact creation
	 * @return IConnection The connection object
	 * 
	 */
	public IConnection connect(String systemId, String systemKind,
			String repositoryId, String repositoryKind, String connectionInfo,
			String credentialInfo, boolean forceResync)
			throws MaxConnectionsReachedException, ConnectionException {
		IConnection connection = null;
		ConnectionManager<IConnection> connectionManager = (ConnectionManager<IConnection>) this
				.getConnectionManager();
		if (forceResync) {
			connection = connectionManager.getConnectionToCreateArtifact(
					systemId, systemKind, repositoryId, repositoryKind,
					connectionInfo, credentialInfo);
		} else {
			connection = connectionManager
					.getConnectionToUpdateOrExtractArtifact(systemId,
							systemKind, repositoryId, repositoryKind,
							connectionInfo, credentialInfo);
		}
		isConnected = true;
		return connection;
	}

	/**
	 * Disconnects from the QC using the ConnectionManager.
	 * 
	 * @param connection
	 */
	protected void disconnect(IConnection connection) {
		ConnectionManager<IConnection> connectionManager = (ConnectionManager<IConnection>) this
				.getConnectionManager();
		if (connection != null) {
			connectionManager.releaseConnection(connection);
		}
		isConnected = false;
	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		super.validate(exceptions);

		if (getResyncUserName() == null) {
			log
					.warn("resyncUserName-property has not been set, so that initial resyncs after artifact creation are not possible.");
		}

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
			defectHandler = new QCDefectHandler();
			attachmentHandler = new QCAttachmentHandler();
			qcGAHelper = new QCGAHelper();
		}
	}

	/**
	 * Setters and geters for various private variables of this class.
	 * 
	 * 
	 */

	public void reset(Object context) {
	}

	@Override
	public void stop() {
		log.info("Got signal to stop QC connector ...");
		while (isConnected) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// Digest the exception
				break;
			}
		}
		tearDownCOM();
		super.stop();
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	protected Document returnDocument(GenericArtifact genericArtifact) {
		Document resultDoc = null;
		try {
			resultDoc = GenericArtifactHelper
					.createGenericArtifactXMLDocument(genericArtifact);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the GenericArtifact into Document";
			log.error(cause, e);
			genericArtifact
					.setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		return resultDoc;
	}

	protected List<GenericArtifactField> getAllGenericArtfactFields(
			GenericArtifact genericArtifact) {
		List<GenericArtifactField> allFields = genericArtifact
				.getAllGenericArtifactFields();
		if (allFields != null)
			genericArtifact = concatValuesOfSameFieldNames(genericArtifact);
		allFields = genericArtifact.getAllGenericArtifactFields();
		return allFields;
	}

	@Override
	public Document createArtifact(Document gaDocument) {
		String targetArtifactIdAfterCreation = null;
		GenericArtifact genericArtifact = getArtifactFromDocument(gaDocument);
		String targetSystemTimezone = genericArtifact.getTargetSystemTimezone();
		IConnection connection = null;
		List<GenericArtifactField> allFields = this
				.getAllGenericArtfactFields(genericArtifact);
		try {
			connection = this.connect(genericArtifact);
			IQCDefect createdArtifact = defectHandler.createDefect(connection,
					allFields, this.getUserName(), targetSystemTimezone);
			if (createdArtifact != null) {
				targetArtifactIdAfterCreation = createdArtifact.getId();
				log.info("Defect " + targetArtifactIdAfterCreation
						+ " is created on "
						+ genericArtifact.getSourceRepositoryId()
						+ " with the artifact details "
						+ genericArtifact.getSourceArtifactId() + " on "
						+ genericArtifact.getTargetRepositoryId());
				genericArtifact
						.setTargetArtifactId(targetArtifactIdAfterCreation);
				// send this artifact to RCDU (Read Connector Database Updater)
				// indicating a success in creating the artifact

				List<String> targetAutimeAndTxnId = getAutimeAndTxnId(
						connection, targetArtifactIdAfterCreation, null,
						ARTIFACT_TYPE_PLAINARTIFACT);
				genericArtifact.setTargetArtifactVersion(targetAutimeAndTxnId
						.get(0));
				genericArtifact.setTargetArtifactLastModifiedDate(DateUtil
						.format(DateUtil.parseQCDate(targetAutimeAndTxnId
								.get(1))));
			}

		} catch (RemoteException e) {
			String cause = "Error while creating defect in QC";
			log.error(cause, e);
			genericArtifact
					.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			throw new CCFRuntimeException(cause, e);
		} finally {
			disconnect(connection);
		}
		return this.returnDocument(genericArtifact);
	}

	@Override
	public Document[] createAttachment(Document gaDocument) {
		GenericArtifact genericArtifact = getArtifactFromDocument(gaDocument);
		String targetArtifactId = genericArtifact
				.getDepParentTargetArtifactId();
		String attachmentName = getFieldValueFromGenericArtifact(
				genericArtifact, AttachmentMetaData.getAttachmentName());
		String contentTypeValue = getFieldValueFromGenericArtifact(
				genericArtifact, AttachmentMetaData.getAttachmentType());
		String attachmentSourceUrl = getFieldValueFromGenericArtifact(
				genericArtifact, AttachmentMetaData.getAttachmentSourceUrl());

		String attachmentDataFileName = GenericArtifactHelper.getStringGAField(
				AttachmentMetaData.ATTACHMENT_DATA_FILE, genericArtifact);
		String contentDataType = GenericArtifactHelper.getStringGAField(
				AttachmentMetaData.ATTACHMENT_TYPE, genericArtifact);
		String attachmentDescription = GenericArtifactHelper.getStringGAField(
				AttachmentMetaData.ATTACHMENT_DESCRIPTION, genericArtifact);
		File attachmentFile = null;
		if (contentDataType.equals(AttachmentMetaData.AttachmentType.DATA
				.toString())) {
			if (StringUtils.isEmpty(attachmentDataFileName)) {
				byte[] attachmentData = null;
				attachmentData = genericArtifact.getRawAttachmentData();
				attachmentFile = qcGAHelper.writeDataIntoFile(attachmentData,
						attachmentName);
			} else {
				File attachmentDataFile = new File(attachmentDataFileName);
				String tempDir = System.getProperty("java.io.tmpdir");
				attachmentFile = new File(tempDir, attachmentName);
				if (attachmentDataFile.exists()) {
					if (attachmentFile.exists()) {
						boolean deletingSuccess = attachmentFile.delete();
						if (deletingSuccess) {
							log.debug("The file "
									+ attachmentFile.getAbsolutePath()
									+ " existed before moving "
									+ attachmentDataFile.getAbsolutePath()
									+ " to that name. So deleted the file "
									+ attachmentFile.getAbsolutePath());
						} else {
							log.info("The file "
									+ attachmentFile.getAbsolutePath()
									+ " exists. But it could not be deleted.");
						}
					} else {
						log.debug("The file "
								+ attachmentFile.getAbsolutePath()
								+ " does not exist. So "
								+ attachmentDataFile.getAbsolutePath()
								+ " can be moved to that name.");
					}
					boolean renamingSuccess = attachmentDataFile
							.renameTo(attachmentFile);
					if (!renamingSuccess) {
						String message = "Count not move file "
								+ attachmentDataFile.getAbsolutePath() + " to "
								+ attachmentFile.getAbsolutePath();
						log.error(message);
						throw new CCFRuntimeException(message);
					}
				} else {
					String message = "The attachment data file "
							+ attachmentDataFile.getAbsolutePath()
							+ " does not exists. So the attchment "
							+ attachmentName
							+ " can not be uploaded to the bug "
							+ targetArtifactId;
					log.error(message);
					throw new CCFRuntimeException(message);
				}
			}
			if (attachmentFile == null || (!attachmentFile.exists())) {
				String message = "The attachment data file "
						+ attachmentFile.getAbsolutePath()
						+ " does not exists. So the attchment "
						+ attachmentName + " can not be uploaded to the bug "
						+ targetArtifactId;
				log.error(message);
				throw new CCFRuntimeException(message);
			} else if (attachmentFile.length() == 0) {
				log.warn("The attchment file "
						+ attachmentFile.getAbsolutePath()
						+ " contains no data. It is uploaded to the bug "
						+ targetArtifactId + ", though.");
			}
		}

		IConnection connection = null;
		GenericArtifact parentArtifact = null;
		try {
			connection = this.connect(genericArtifact);
			attachmentHandler.createAttachment(connection, targetArtifactId,
					attachmentName, contentTypeValue, attachmentFile,
					attachmentSourceUrl, attachmentDescription);
			List<String> attachmentIdAndType = qcGAHelper.getFromTable(
					connection, targetArtifactId, attachmentName);
			String attachmentId = null;
			if (attachmentIdAndType != null) {
				attachmentId = attachmentIdAndType.get(0);
				genericArtifact.setTargetArtifactId(attachmentId);
			}
			log.info("Attachment " + attachmentName + " is created with id "
					+ attachmentId + " for defect " + targetArtifactId + " on "
					+ genericArtifact.getTargetRepositoryId());
			List<String> targetAutimeAndTxnId = getAutimeAndTxnId(connection,
					attachmentId, attachmentName, ARTIFACT_TYPE_ATTACHMENT);
			genericArtifact.setTargetArtifactVersion(targetAutimeAndTxnId
					.get(0));
			genericArtifact.setTargetArtifactLastModifiedDate(DateUtil
					.format(DateUtil.parseQCDate(targetAutimeAndTxnId.get(1))));

			List<String> targetAutimeAndTxnIdParentDefect = getAutimeAndTxnId(
					connection, targetArtifactId, null,
					ARTIFACT_TYPE_PLAINARTIFACT);
			parentArtifact = new GenericArtifact();
			parentArtifact
					.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			parentArtifact
					.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
			parentArtifact
					.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
			parentArtifact.setConflictResolutionPriority(genericArtifact
					.getConflictResolutionPriority());
			parentArtifact.setSourceArtifactId(genericArtifact
					.getDepParentSourceArtifactId());
			parentArtifact.setSourceArtifactLastModifiedDate(genericArtifact
					.getSourceArtifactLastModifiedDate());
			parentArtifact.setSourceArtifactVersion(genericArtifact
					.getSourceArtifactVersion());
			parentArtifact.setSourceRepositoryId(genericArtifact
					.getSourceRepositoryId());
			parentArtifact.setSourceSystemId(genericArtifact
					.getSourceSystemId());
			parentArtifact.setSourceSystemKind(genericArtifact
					.getSourceSystemKind());
			parentArtifact.setSourceRepositoryKind(genericArtifact
					.getSourceRepositoryKind());
			parentArtifact.setSourceSystemTimezone(genericArtifact
					.getSourceSystemTimezone());

			parentArtifact.setTargetArtifactId(targetArtifactId);
			parentArtifact.setTargetArtifactLastModifiedDate(DateUtil
					.format(DateUtil
							.parseQCDate(targetAutimeAndTxnIdParentDefect
									.get(1))));
			parentArtifact
					.setTargetArtifactVersion(targetAutimeAndTxnIdParentDefect
							.get(0));
			parentArtifact.setTargetRepositoryId(genericArtifact
					.getTargetRepositoryId());
			parentArtifact.setTargetRepositoryKind(genericArtifact
					.getTargetRepositoryKind());
			parentArtifact.setTargetSystemId(genericArtifact
					.getTargetSystemId());
			parentArtifact.setTargetSystemKind(genericArtifact
					.getTargetSystemKind());
			parentArtifact.setTargetSystemTimezone(genericArtifact
					.getTargetSystemTimezone());
		} catch (Exception e) {
			String cause = "Error while creating attachment in QC";
			log.error(cause, e);
			genericArtifact
					.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_CONNECTION);
			throw new CCFRuntimeException(cause, e);
		} finally {
			if (attachmentFile != null) {
				boolean deletingSuccess = attachmentFile.delete();
				if (!deletingSuccess) {
					log.warn("Could not delete the attachment file "
							+ attachmentFile.getAbsolutePath());
				}
			}
			disconnect(connection);
		}

		Document attachmentDoc = this.returnDocument(genericArtifact);
		Document bugDoc = parentArtifact == null ? null : this
				.returnDocument(parentArtifact);
		return new Document[] { attachmentDoc, bugDoc };
	}

	@Override
	public Document[] deleteAttachment(Document gaDocument) {
		GenericArtifact genericArtifact = getArtifactFromDocument(gaDocument);
		String targetArtifactId = genericArtifact.getTargetArtifactId();
		String bugId = genericArtifact.getDepParentTargetArtifactId();
		IConnection connection = null;
		GenericArtifact parentArtifact = null;
		try {
			connection = this.connect(genericArtifact);
			attachmentHandler.deleteAttachment(connection, bugId,
					targetArtifactId);
			String attachmentName = getFieldValueFromGenericArtifact(
					genericArtifact, AttachmentMetaData.getAttachmentName());
			log.info("Attachment " + targetArtifactId
					+ " is deleted from defect " + bugId + " on "
					+ genericArtifact.getTargetRepositoryId());
			List<String> targetAutimeAndTxnId = getAutimeAndTxnId(connection,
					targetArtifactId, attachmentName, ARTIFACT_TYPE_ATTACHMENT);
			genericArtifact.setTargetArtifactVersion(targetAutimeAndTxnId
					.get(0));
			genericArtifact.setTargetArtifactLastModifiedDate(DateUtil
					.format(DateUtil.parseQCDate(targetAutimeAndTxnId.get(1))));

			List<String> targetAutimeAndTxnIdParentDefect = getAutimeAndTxnId(
					connection, bugId, null, ARTIFACT_TYPE_PLAINARTIFACT);
			parentArtifact = new GenericArtifact();
			parentArtifact
					.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			parentArtifact
					.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
			parentArtifact
					.setArtifactMode(GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
			parentArtifact.setConflictResolutionPriority(genericArtifact
					.getConflictResolutionPriority());
			parentArtifact.setSourceArtifactId(genericArtifact
					.getDepParentSourceArtifactId());
			parentArtifact.setSourceArtifactLastModifiedDate(genericArtifact
					.getSourceArtifactLastModifiedDate());
			parentArtifact.setSourceArtifactVersion(genericArtifact
					.getSourceArtifactVersion());
			parentArtifact.setSourceRepositoryId(genericArtifact
					.getSourceRepositoryId());
			parentArtifact.setSourceSystemId(genericArtifact
					.getSourceSystemId());
			parentArtifact.setSourceSystemKind(genericArtifact
					.getSourceSystemKind());
			parentArtifact.setSourceRepositoryKind(genericArtifact
					.getSourceRepositoryKind());
			parentArtifact.setSourceSystemTimezone(genericArtifact
					.getSourceSystemTimezone());

			parentArtifact.setTargetArtifactId(bugId);
			parentArtifact.setTargetArtifactLastModifiedDate(DateUtil
					.format(DateUtil
							.parseQCDate(targetAutimeAndTxnIdParentDefect
									.get(1))));
			parentArtifact
					.setTargetArtifactVersion(targetAutimeAndTxnIdParentDefect
							.get(0));
			parentArtifact.setTargetRepositoryId(genericArtifact
					.getTargetRepositoryId());
			parentArtifact.setTargetRepositoryKind(genericArtifact
					.getTargetRepositoryKind());
			parentArtifact.setTargetSystemId(genericArtifact
					.getTargetSystemId());
			parentArtifact.setTargetSystemKind(genericArtifact
					.getTargetSystemKind());
			parentArtifact.setTargetSystemTimezone(genericArtifact
					.getTargetSystemTimezone());
		} catch (Exception e) {
			String cause = "Exception while trying to delete attachment with id "
					+ targetArtifactId + "from bug " + bugId;
			log.error(cause, e);
			throw new CCFRuntimeException(cause, e);
		} finally {
			if (connection != null)
				this.disconnect(connection);
		}
		Document attachmentDoc = this.returnDocument(genericArtifact);
		Document bugDoc = parentArtifact == null ? null : this
				.returnDocument(parentArtifact);
		return new Document[] { attachmentDoc, bugDoc };
	}

	@Override
	public Document updateArtifact(Document gaDocument) {
		GenericArtifact genericArtifact = getArtifactFromDocument(gaDocument);
		List<GenericArtifactField> allFields = this
				.getAllGenericArtfactFields(genericArtifact);
		String targetArtifactId = genericArtifact.getTargetArtifactId();
		IConnection connection = this.connect(genericArtifact);
		try {
			if (allFields != null) {
				this.updateDefect(connection, targetArtifactId,
						genericArtifact, allFields);
			} else {
				String cause = "No field for the bug " + targetArtifactId
						+ " is supplied";
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} catch (Exception e) {
			String cause = "Exception occured while updating defect in QC:"
					+ genericArtifact.toString();
			log.error(cause, e);
			genericArtifact
					.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			// sending this artifact to HOSPITAL
			throw new CCFRuntimeException(cause, e);

		} finally {
			this.disconnect(connection);
			connection = null;
		}
		return this.returnDocument(genericArtifact);
	}

	@Override
	public Document createDependency(Document gaDocument) {
		// throw new
		// CCFRuntimeException("createDependency is not implemented...!");
		log.warn("createDependency is not implemented...!");
		return null;
	}

	@Override
	public Document deleteArtifact(Document gaDocument) {
		// throw new
		// CCFRuntimeException("deleteArtifact is not implemented...!");
		log.warn("deleteArtifact is not implemented...!");
		return null;
	}

	@Override
	public Document deleteDependency(Document gaDocument) {
		// throw new
		// CCFRuntimeException("deleteDependency is not implemented...!");
		log.warn("deleteDependency is not implemented...!");
		return null;
	}

	@Override
	public Document updateAttachment(Document gaDocument) {
		// throw new
		// CCFRuntimeException("updateAttachment is not implemented...!");
		log.warn("updateAttachment is not implemented...!");
		return null;
	}

	@Override
	public Document updateDependency(Document gaDocument) {
		// throw new
		// CCFRuntimeException("updateDependency is not implemented...!");
		log.warn("updateDependency is not implemented...!");
		return null;
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
			} else if (message.contains("The Object is locked by")
					&& !immediatelyQuarantineLockedDefects) {
				// TODO Should we introduce another parameter in the connection
				// manager for this?
				connectionErrorOccured = true;
			} else if (message.contains("The server threw an exception.")) {
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
			if (connectionManager.isEnableRetryAfterNetworkTimeout()
					&& connectionErrorOccured) {
				return true;
			}
		} else if (rootCause instanceof DefectAlreadyLockedException
				&& !immediatelyQuarantineLockedDefects) {
			return true;
		} else if (rootCause instanceof CCFRuntimeException) {
			Throwable cause = rootCause.getCause();
			return handleException(cause, connectionManager);
		}
		return false;
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
		this.password = password;
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

	/**
	 * Sets the optional resync username
	 * 
	 * The resync user name is used to login into the HP QC instance whenever an
	 * artifact should be created. This user has to differ from the ordinary
	 * user used to log in in order to force initial resyncs with the source
	 * system once a new artifact has been created.
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
	 * created.
	 * 
	 * @return the resyncUserName
	 */
	private String getResyncUserName() {
		return resyncUserName;
	}

	/**
	 * Sets the optional resync password that belongs to the resync user
	 * 
	 * @param resyncPassword
	 *            the resyncPassword to set
	 */
	public void setResyncPassword(String resyncPassword) {
		this.resyncPassword = resyncPassword;
	}

	/**
	 * Gets the optional resync password that belongs to the resync user
	 * 
	 * @return the resyncPassword
	 */
	private String getResyncPassword() {
		return resyncPassword;
	}

	public QCDefectHandler getDefectHandler() {
		return defectHandler;
	}

	/**
	 * If this property is set to true (true by default), locked defects will be
	 * quarantined and the operation will never be retried
	 */
	public void setImmediatelyQuarantineLockedDefects(
			boolean immediatelyQuarantineLockedDefects) {
		this.immediatelyQuarantineLockedDefects = immediatelyQuarantineLockedDefects;
	}

	/**
	 * If this property is set to true (true by default), locked defects will be
	 * quarantined and the operation will never be retried
	 */
	public boolean isImmediatelyQuarantineLockedDefects() {
		return immediatelyQuarantineLockedDefects;
	}

}
