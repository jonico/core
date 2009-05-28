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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.DefectAlreadyLockedException;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.ICommand;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IFactory;
import com.collabnet.ccf.pi.qc.v90.api.IFactoryList;
import com.collabnet.ccf.pi.qc.v90.api.IFilter;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Bug;
import com.jacob.com.ComFailException;

/**
 * The Defect handler class provides support for listing and/or edit defects.
 *
 */
public class QCDefectHandler {

	private static final String FIRST_TAGS = "<html><body>";
	private static final String LAST_TAGS = "</body></html>";
	private static final Log log = LogFactory.getLog(QCDefectHandler.class);
	// private QCAttachmentHandler attachmentHandler;
	private QCGAHelper qcGAHelper = new QCGAHelper();
	private final static String QC_BUG_ID = "BG_BUG_ID";
	private final static String QC_DEV_COMMENTS = "BG_DEV_COMMENTS";
	private final static String QC_BUG_VER_STAMP = "BG_BUG_VER_STAMP";
	private final static String QC_ATTACHMENT = "BG_ATTACHMENT";
	private final static String QC_VTS = "BG_VTS";
	private final static String UNDERSCORE_STRING = "<font color=\"#000080\"><b>________________________________________</b></font>";

	public static IRecordSet executeSQL(IConnection qcc, String sql) {
		ICommand command = null;
		try {
			command = qcc.getCommand();
			command.setCommandText(sql);
			IRecordSet rs = command.execute();
			return rs;
		} finally {
			command = null;
		}
	}

	// TODO review
	public List<IQCDefect> getDefectsWithIds(IConnection qcc, List<Integer> ids) {
		IFactory bf = qcc.getBugFactory();
		IFilter filter = bf.getFilter();

		List<IQCDefect> tasks = new ArrayList<IQCDefect>();
		for (int i = 0; i < ids.size(); ++i) {
			filter.setFilter(QC_BUG_ID, ids.get(i).toString());
			IFactoryList fl = filter.getNewList();

			IBug bug = fl.getBug(1);
			QCDefect defect = new QCDefect((Bug) bug);

			tasks.add(defect);

			fl.safeRelease();
		}

		filter.safeRelease();
		bf = null;

		return tasks;
	}

	/**
	 * Updates the defect identified by the incoming bugId in QC
	 *
	 * @param qcc
	 *            The Connection object
	 * @param bugId
	 *            The ID of the defect to be updated
	 * @param List
	 *            <GenericArtifactField> The values of each fields of the defect
	 *            that need to be updated on the old values.
	 * @param connectorUser
	 *            The connectorUser name used while updating the comments
	 * @return IQCDefect Updated defect object
	 *
	 */
	public IQCDefect updateDefect(IConnection qcc, String bugId,
			List<GenericArtifactField> allFields, String connectorUser,
			String targetSystemTimezone) {

		IFactory bugFactory = null;
		IBug bug = null;
		try {
			bugFactory = qcc.getBugFactory();
			bug = bugFactory.getItem(bugId);
			bug.lockObject();
			List<String> allFieldNames = new ArrayList<String>();
			String fieldValue = null;
			for (int cnt = 0; cnt < allFields.size(); cnt++) {

				GenericArtifactField thisField = allFields.get(cnt);
				String fieldName = thisField.getFieldName();
				if (thisField.getFieldValueType().equals(
						GenericArtifactField.FieldValueTypeValue.DATE)
						|| thisField
								.getFieldValueType()
								.equals(
										GenericArtifactField.FieldValueTypeValue.DATETIME))
					fieldValue = getProperFieldValue(thisField,
							targetSystemTimezone);
				else
					fieldValue = (String) thisField.getFieldValue();

				if (fieldName.equals(QC_DEV_COMMENTS)) {
					String oldFieldValue = bug.getFieldAsString(fieldName);
					if ((!StringUtils.isEmpty(oldFieldValue)
							&& !StringUtils.isEmpty(fieldValue) && !oldFieldValue
							.equals(fieldValue))
							|| (StringUtils.isEmpty(oldFieldValue) && !StringUtils
									.isEmpty(fieldValue))) {
						fieldValue = getConcatinatedCommentValue(oldFieldValue,
								fieldValue, connectorUser);
					}
				}
				if (!(allFieldNames.contains(allFields.get(cnt).getFieldName()))
						&& !(fieldName.equals(QC_BUG_ID)
								|| fieldName.equals(QC_BUG_VER_STAMP)
								|| fieldName.equals(QC_ATTACHMENT) || fieldName
								.equals(QC_VTS))) {
					try {
						bug.setField(fieldName, fieldValue);
					} catch (ComFailException e) {
						String message = "Exception while setting the value of field "
								+ fieldName + " to " + fieldValue + ": "+ e.getMessage();
						log.error(message, e);
						throw new CCFRuntimeException(message, e);
					}
				}
				if (!fieldName.equals(QC_DEV_COMMENTS))
					allFieldNames.add(fieldName);
			}
			bug.post();
		} catch (DefectAlreadyLockedException e) {
			String message = "Attempt to lock the defect with id " + bugId
					+ " failed";
			log.error(message, e);
			throw new CCFRuntimeException(message + ": "+e.getMessage(), e);
		} catch (ComFailException e) {
			bug.undo();
			String message = "ComFailException while updating the defect with id "
					+ bugId;
			log.error(message, e);
			throw new CCFRuntimeException(message + ": "+e.getMessage(), e);
		} catch (Exception e) {
			bug.undo();
			String message = "Exception while updating the defect with id "
					+ bugId;
			log.error(message, e);
			throw new CCFRuntimeException(message + ": "+e.getMessage(), e);
		} finally {
			if (bug != null) {
				bug.unlockObject();
			}
			bugFactory = null;
		}
		return new QCDefect((Bug) bug);
	}

	/**
	 * Create the defect based on the incoming field values
	 *
	 * @param qcc
	 *            The Connection object
	 * @param List
	 *            <GenericArtifactField> The values of each fields of the defect
	 *            that need to be used while creation.
	 *
	 * @return IQCDefect Created defect object
	 *
	 */
	public IQCDefect createDefect(IConnection qcc,
			List<GenericArtifactField> allFields, String connectorUser,
			String targetSystemTimezone) throws RemoteException {

		IFactory bugFactory = null;
		IBug bug = null;
		try {
			bugFactory = qcc.getBugFactory();
			bug = bugFactory.addItem("Defect created by Connector");
			bug.lockObject();
			List<String> allFieldNames = new ArrayList<String>();
			String fieldValue = null;
			for (int cnt = 0; cnt < allFields.size(); cnt++) {

				GenericArtifactField thisField = allFields.get(cnt);
				String fieldName = thisField.getFieldName();
				if (thisField.getFieldValueType().equals(
						GenericArtifactField.FieldValueTypeValue.DATE)
						|| thisField
								.getFieldValueType()
								.equals(
										GenericArtifactField.FieldValueTypeValue.DATETIME))
					fieldValue = getProperFieldValue(thisField,
							targetSystemTimezone);
				else
					fieldValue = (String) thisField.getFieldValue();

				if (fieldName.equals(QC_DEV_COMMENTS)) {
					String oldFieldValue = bug.getFieldAsString(fieldName);
					if ((!StringUtils.isEmpty(oldFieldValue)
							&& !StringUtils.isEmpty(fieldValue) && !oldFieldValue
							.equals(fieldValue))
							|| (StringUtils.isEmpty(oldFieldValue) && !StringUtils
									.isEmpty(fieldValue))) {
						fieldValue = getConcatinatedCommentValue(oldFieldValue,
								fieldValue, connectorUser);
					}
				}
				/*
				 * The following fields cannot be set or have some conditions
				 * Cannot be set from here: 1. BG_BUG_ID 2. BG_BUG_VER_STAMP 3.
				 * BG_VTS Has some conditions: 1. BG_SUBJECT -> Can be set to a
				 * Valid value that is present in the list.
				 *
				 */
				if (!(allFieldNames.contains(allFields.get(cnt).getFieldName()))
						&& !(fieldName.equals(QC_BUG_ID)
								|| fieldName.equals(QC_BUG_VER_STAMP)
								|| fieldName.equals(QC_ATTACHMENT) || fieldName
								.equals(QC_VTS))) {
					try {
						bug.setField(fieldName, fieldValue);
					} catch (Exception e) {
						String message = "Exception while setting the value of field "
								+ fieldName + " to " + fieldValue + ": "+ e.getMessage();
						log.error(message, e);
						throw new CCFRuntimeException(message, e);
					}
				} else {
					log.debug(fieldName);
				}
				if (!fieldName.equals(QC_DEV_COMMENTS))
					allFieldNames.add(fieldName);
			}
			bug.post();
		} catch (Exception e) {
			String bugId = null;
			if (bug != null) {
				bugId = bug.getId();
				bugFactory.removeItem(bugId);
				bug = null;
			}
			String message = "Exception while creating Bug " + bugId;
			log.error(message, e);
			throw new CCFRuntimeException(message + ": "+e.getMessage(), e);
		} finally {
			if (bug != null) {
				bug.unlockObject();
			}
			bugFactory = null;
		}

		return new QCDefect((Bug) bug);
	}

	public String getConcatinatedCommentValue(String oldFieldValue,
			String fieldValue, String connectorUser) {

		String concatinatedFieldValue = null;
		java.util.Date date = new java.util.Date();
		String currentDateString = DateUtil.formatQCDate(date);
		fieldValue = fieldValue.replaceAll("\t", "        ");
		if (!StringUtils.isEmpty(oldFieldValue)) {
			oldFieldValue = this.stripStartAndEndTags(oldFieldValue);
			concatinatedFieldValue = FIRST_TAGS + oldFieldValue + "<br>"
					+ UNDERSCORE_STRING + "<br>"
					+ "<font color=\"#000080\"><b>" + connectorUser + ", "
					+ currentDateString + ": " + "</b></font>" + fieldValue
					+ LAST_TAGS;
		} else {
			concatinatedFieldValue = FIRST_TAGS + "<font color=\"#000080\"><b>"
					+ connectorUser + ", " + currentDateString + ": "
					+ "</b></font>" + fieldValue + LAST_TAGS;
		}

		return concatinatedFieldValue;
	}

	// public String stripLastTags(String oldFieldValue) {
	// if (oldFieldValue.endsWith(LAST_TAGS)) {
	// return oldFieldValue.substring(0, oldFieldValue.length() -
	// LAST_TAGS.length());
	// }
	// else {
	// return oldFieldValue;
	// }
	// }

	/**
	 * Gets the value of the field in a suitable data type.
	 *
	 */
	public String getProperFieldValue(GenericArtifactField thisField,
			String targetSystemTimezone) {

		String fieldValue = null;
		GenericArtifactField.FieldValueTypeValue fieldValueTypeValue = thisField
				.getFieldValueType();
		switch (fieldValueTypeValue) {
		case DATE: {
			GregorianCalendar gcal = (GregorianCalendar) thisField
					.getFieldValue();
			if (gcal != null) {
				Date targetTimezoneDate = gcal.getTime();
				if (DateUtil.isAbsoluteDateInTimezone(targetTimezoneDate,
						DateUtil.GMT_TIME_ZONE_STRING)) {
					targetTimezoneDate = DateUtil
							.convertGMTToTimezoneAbsoluteDate(
									targetTimezoneDate, TimeZone.getDefault()
											.getDisplayName(false,
													TimeZone.SHORT));
				}
				fieldValue = DateUtil.formatQCDate(targetTimezoneDate);
			}
			break;
		}
		case DATETIME: {
			Date targetTimezoneDate = (Date) thisField.getFieldValue();
			if (targetTimezoneDate != null) {
				fieldValue = DateUtil.formatQCDate(targetTimezoneDate);
			}
			break;
		}

		}
		return fieldValue;

	}

	/**
	 * Return all defects modified between the given time range, in a map
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<ArtifactState> getLatestChangedDefects(IConnection qcc,
			String connectorUser, String transactionId) {

		int rc = 0;
		String sql = "SELECT AU_ENTITY_ID, AU_ACTION_ID, AU_TIME FROM AUDIT_LOG WHERE AU_ENTITY_TYPE = 'BUG' AND AU_ACTION_ID > '"
				+ transactionId
				+ "' AND AU_ACTION!='DELETE' AND AU_USER != '"
				+ connectorUser
				+ "' AND AU_FATHER_ID = '-1' ORDER BY AU_ACTION_ID";

		log.debug(sql);
		ArrayList<ArtifactState> changedDefects = new ArrayList<ArtifactState>();
		HashMap <String, ArtifactState> artifactIdStateMap = new HashMap<String, ArtifactState>();
		IRecordSet rs = null;
		try {
			rs = executeSQL(qcc, sql);
			if (rs != null)
				rc = rs.getRecordCount();

			for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
				String bugId = rs.getFieldValueAsString("AU_ENTITY_ID");
				String actionIdStr = rs.getFieldValueAsString("AU_ACTION_ID");
				int actionId = Integer.parseInt(actionIdStr);
				String actionDateStr = rs.getFieldValueAsString("AU_TIME");
				Date actionDate = DateUtil.parseQCDate(actionDateStr);
				if (artifactIdStateMap.containsKey(bugId)) {
					ArtifactState state = artifactIdStateMap.get(bugId);
					changedDefects.remove(state);
					state.setArtifactLastModifiedDate(actionDate);
					state.setArtifactVersion(actionId);
					changedDefects.add(state);
				}
				else {
					ArtifactState state = new ArtifactState();
					state.setArtifactId(bugId);
					state.setArtifactLastModifiedDate(actionDate);
					state.setArtifactVersion(actionId);
					changedDefects.add(state);
					artifactIdStateMap.put(bugId, state);
				}
			}
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
		return changedDefects;
	}

	/**
	 * Orders the values of the incoming HashMap according to its keys.
	 *
	 * @param HashMap
	 *            This hashMap contains the transactionIds as values indexed by
	 *            their defectIds.
	 * @return List<String> This list of strings is the ordering of the keys of
	 *         the incoming HashMaps, which are the defectIds, according to the
	 *         order of their transactionIds
	 *
	 */
	public List<String> orderByLatestTransactionIds(
			Map<String, String> defectIdTransactionIdMap) {

		List<String> mapKeys = new ArrayList<String>(defectIdTransactionIdMap
				.keySet());
		List<String> mapValues = new ArrayList<String>(defectIdTransactionIdMap
				.values());

		defectIdTransactionIdMap.clear();
		TreeSet<String> sortedSet = new TreeSet<String>(mapValues);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;
		for (int i = 0; i < size; i++) {
			defectIdTransactionIdMap.put(mapKeys.get(mapValues
					.indexOf(sortedArray[i])), (String) sortedArray[i]);
		}

		List<String> orderedDefectList = new ArrayList<String>(
				defectIdTransactionIdMap.keySet());
		return orderedDefectList;
	}

	// /**
	// * Finds the state of the defect at a particular defectId and
	// transactionId.
	// * This is used while finding a bunch of history artifacts for a defectId
	// * after a particular state represented by the transactionId.
	// *
	// * @param entityId
	// * The defectId for which the search has to be made in QC
	// * @param actionId
	// * The transactionId starting from which the search has to be
	// * made for a particular defectId in QC
	// * @param transactionId
	// * The transactionId starting from which the search has to be
	// * made for a particular defectId in QC
	// * @param qcc
	// * The Connection object
	// * @param latestDefectArtifact
	// * The GenericArtifact into which the latest state information of
	// * the defect identified by entityId is captured.
	// * @return GenericArtifact The resultant GenericArtifact which has the
	// * latest state of the defect information in it.
	// */
	// public GenericArtifact getStateOfDefectAtActionID(IConnection qcc,
	// int entityId, int actionId, String transactionId,
	// GenericArtifact latestDefectArtifact) {
	//
	// String sql = "SELECT AU_ACTION_ID FROM AUDIT_LOG WHERE AU_ENTITY_TYPE =
	// 'BUG' AND AU_ACTION_ID > '"
	// + actionId
	// + "' AND AU_ACTION!='DELETE' AND AU_ENTITY_TYPE= 'BUG' AND AU_ENTITY_ID =
	// '"
	// + entityId + "'";
	// if (transactionId != null && !transactionId.equals(""))
	// sql += " AND AU_ACTION_ID >= '" + transactionId + "'";
	// sql += " ORDER BY AU_ACTION_ID DESC";
	// log.debug(sql);
	//
	// IRecordSet rs = executeSQL(qcc, sql);
	// int rc = rs.getRecordCount();
	// int txnId = 0;
	// for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
	//
	// txnId = Integer.parseInt(rs.getFieldValue("AU_ACTION_ID"));
	// sql = "SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID= '"
	// + txnId + "'";
	// IRecordSet newRs = executeSQL(qcc, sql);
	// int newRc = newRs.getRecordCount();
	//
	// for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
	// String fieldName = newRs.getFieldValue("AP_FIELD_NAME");
	// String oldFieldValue = null;
	// if (!(fieldName.equals("BG_DESCRIPTION")))
	// oldFieldValue = newRs.getFieldValue("AP_OLD_VALUE");
	// else
	// oldFieldValue = newRs.getFieldValue("AP_OLD_LONG_VALUE");
	//
	// List<GenericArtifactField> genArtifactFields = latestDefectArtifact
	// .getAllGenericArtifactFieldsWithSameFieldName(fieldName);
	// if (genArtifactFields != null
	// && genArtifactFields.get(0) != null)
	// genArtifactFields.get(0).setFieldValue(oldFieldValue);
	// // genArtifactFields.get(0).setFieldValueHasChanged(true);
	// }
	//
	// }
	// List<String> txnIds = getTransactionIdsInRange(qcc, entityId, actionId,
	// actionId, null);
	// IRecordSet newRs = getAuditPropertiesRecordSet(qcc, txnIds);
	// String deltaComment = getDeltaOfComment(newRs);
	// if (deltaComment != null) {
	// List<GenericArtifactField> genArtifactFieldsForComments =
	// latestDefectArtifact
	// .getAllGenericArtifactFieldsWithSameFieldName("BG_DEV_COMMENTS");
	// genArtifactFieldsForComments.get(0).setFieldValue(deltaComment);
	// }
	//
	// List<GenericArtifactField> genArtifactFields = latestDefectArtifact
	// .getAllGenericArtifactFieldsWithSameFieldName("BG_VTS");
	//
	// if (genArtifactFields != null && genArtifactFields.get(0) != null
	// && genArtifactFields.get(0).getFieldValue() != null
	// && !(genArtifactFields.get(0).getFieldValue().equals(""))) {
	// Date newBgVts = DateUtil.parseQCDate((String) genArtifactFields
	// .get(0).getFieldValue());
	// latestDefectArtifact.setSourceArtifactLastModifiedDate(DateUtil
	// .format(newBgVts));
	// return latestDefectArtifact;
	// } else {
	// // This means the BG_VTS field is null. So, find it, populate it &
	// // ArtifactLastModifiedDate
	// String bgVts = qcGAHelper.findBgVtsFromQC(qcc, actionId, entityId);
	// genArtifactFields.get(0).setFieldValue((String) bgVts);
	// Date newBgVts = DateUtil.parseQCDate(bgVts);
	// String lastModifiedDate = DateUtil.format(newBgVts);
	// latestDefectArtifact
	// .setSourceArtifactLastModifiedDate(lastModifiedDate);
	// }
	//
	// // The ArtifactActionValue IGNORE and DELETE needs to be done.
	// //
	// latestDefectArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UNKNOWN);
	// return latestDefectArtifact;
	//
	// }
	//
	// /**
	// * Given an action id (id for the AUDIT_LOG table
	// *
	// * @param actionId
	// * @return the defect at the time of the actionId
	// */
	// public QCDefect getArtifactStateFromActionId(int actionId) {
	// // return the state of the given defect at transaction actionId
	//
	// QCDefect defect = null;
	// return defect;
	// }

	/**
	 * Assigns values of the incoming parameters to the incoming genericArtifact
	 * and returns the updated one.
	 *
	 * @param latestDefectArtifact
	 *            The GenericArtifact to which the following values need to be
	 *            assigned.
	 * @param sourceArtifactId
	 * @param sourceRepositoryId
	 * @param sourceRepositoryKind
	 * @param sourceSystemId
	 * @param sourceSystemKind
	 * @param targetRepositoryId
	 * @param targetRepositoryKind
	 * @param targetSystemId
	 * @param targetSystemKind
	 * @param thisTransactionId
	 * @return Assigned GenericArtifact
	 */
	public GenericArtifact assignValues(GenericArtifact latestDefectArtifact,
			String sourceArtifactId, String sourceRepositoryId,
			String sourceRepositoryKind, String sourceSystemId,
			String sourceSystemKind, String targetRepositoryId,
			String targetRepositoryKind, String targetSystemId,
			String targetSystemKind, String thisTransactionId) {

		latestDefectArtifact.setSourceArtifactId(sourceArtifactId);
		latestDefectArtifact.setSourceRepositoryId(sourceRepositoryId);
		latestDefectArtifact.setSourceRepositoryKind(sourceRepositoryKind);
		latestDefectArtifact.setSourceSystemId(sourceSystemId);
		latestDefectArtifact.setSourceSystemKind(sourceSystemKind);

		latestDefectArtifact.setTargetRepositoryId(targetRepositoryId);
		latestDefectArtifact.setTargetRepositoryKind(targetRepositoryKind);
		latestDefectArtifact.setTargetSystemId(targetSystemId);
		latestDefectArtifact.setTargetSystemKind(targetSystemKind);
		latestDefectArtifact.setSourceArtifactVersion(thisTransactionId);

		return latestDefectArtifact;
	}

	/**
	 * Obtains the artifactAction based on the date at which that defect was
	 * created and the lastReadTime synchronization parameter.
	 *
	 * @param entityId
	 *            The defectId for which the search has to be made in QC
	 * @param actionId
	 *            The transactionId at which it needs to be determined if the
	 *            defect is a create or update.
	 * @param qcc
	 *            The Connection object
	 * @param latestDefectArtifact
	 *            The GenericArtifact into which the artifactAction is populated
	 *            after it is determined.
	 * @param lastReadTime
	 *            This is synchronization parameter used to compare with the
	 *            defect created date and find out the artifactAction.
	 * @return GenericArtifact Updated artifact
	 */
	public GenericArtifact getArtifactAction(
			GenericArtifact latestDefectArtifact, IConnection qcc,
			String syncInfoTransactionId, String actionId, int entityId,
			String lastReadTime) {

		List<GenericArtifactField> genArtifactFields = latestDefectArtifact
				.getAllGenericArtifactFieldsWithSameFieldName("BG_VTS");
		// Date lastReadDate = DateUtil.parse(lastReadTime);
		// Date createdOn = qcGAHelper.getDefectCreatedDate(qcc, entityId);
		if (genArtifactFields != null && genArtifactFields.get(0) != null) {
			String bgVts = qcGAHelper.findBgVtsFromQC(qcc, Integer
					.parseInt(actionId), entityId);
			genArtifactFields.get(0).setFieldValue((String) bgVts);
			Date newBgVts = DateUtil.parseQCDate(bgVts);
			String lastModifiedDate = DateUtil.format(newBgVts);
			latestDefectArtifact
					.setSourceArtifactLastModifiedDate(lastModifiedDate);
		}
		return latestDefectArtifact;
	}

	public List<String> getTransactionIdsInRange(IConnection qcc, int entityId,
			int syncInfoTxnId, int actionId, String connectorUser) {

		List<String> listOfTxnIds = new ArrayList<String>();

		String sql = "SELECT AU_ACTION_ID FROM AUDIT_LOG WHERE AU_ACTION_ID > '"
				+ syncInfoTxnId
				+ "' AND AU_ACTION_ID <= '"
				+ actionId
				+ "' AND AU_ACTION!='DELETE' AND AU_ENTITY_TYPE='BUG' AND AU_USER!='"
				+ connectorUser
				+ "' AND AU_FATHER_ID='-1' AND AU_ENTITY_ID='"
				+ entityId + "'";
		IRecordSet newRs = null;
		try {
			newRs = executeSQL(qcc, sql);
			log.debug("The SQL query in getTransactionIdsInRange::" + sql);
			int newRc = newRs.getRecordCount();

			for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
				String fieldValue = newRs.getFieldValueAsString("AU_ACTION_ID");
				listOfTxnIds.add(fieldValue);
			}
		} finally {
			if (newRs != null) {
				newRs.safeRelease();
				newRs = null;
			}
		}
		return listOfTxnIds;
	}

	public IRecordSet getAuditPropertiesRecordSet(IConnection qcc,
			List<String> txnIds) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID in (");
		int len = txnIds.size();
		for (int cnt = 0; cnt < len; cnt++) {
			if (cnt != (len - 1))
				sql.append("'" + txnIds.get(cnt) + "',");
			else
				sql.append("'" + txnIds.get(cnt) + "'");
		}
		sql.append(")");
		log.debug("New SQL in getDeltaOfComment is:" + sql);
		IRecordSet newRs = executeSQL(qcc, sql.toString());
		return newRs;
	}

	/**
	 * Gets the difference between the comment values of the previous and
	 * current transaction pointed by actionId
	 *
	 * @param qcc
	 * @param actionId
	 * @return
	 */
	public String getDeltaOfComment(IRecordSet newRs) {

		String deltaComment = "";
		String newFieldValue = null;
		String emptyString = "";

		int newRc = newRs.getRecordCount();

		for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
			String fieldName = newRs.getFieldValueAsString("AP_FIELD_NAME");
			if (fieldName.equals("BG_DEV_COMMENTS")) {
				String oldFieldValue = newRs.getFieldValueAsString("AP_OLD_LONG_VALUE");
				newFieldValue = newRs.getFieldValueAsString("AP_NEW_LONG_VALUE");
				if (!StringUtils.isEmpty(oldFieldValue)) {
					if (newFieldValue.length() > oldFieldValue.length()) {
						String strippedOldValue = this
								.stripStartAndEndTags(oldFieldValue);
						String strippedNewValue = this
								.stripStartAndEndTags(newFieldValue);
						deltaComment += (strippedNewValue
								.substring(strippedOldValue.length()));
					} else if (newFieldValue.length() == oldFieldValue.length()) {
						log.warn("QC comments not changed");
					} else {
						log.warn("New comment is smaller than old comment");
					}
				} else {
					if (!StringUtils.isEmpty(newFieldValue)) {
						deltaComment = newFieldValue;
					}
				}
			}
		}
		if (StringUtils.isEmpty(newFieldValue))
			return emptyString;
		else {
			deltaComment = deltaComment
					.replaceAll(
							"<[fF][oO][Nn][Tt]\\s*[cC][oO][lL][oO][rR]=[\"']#[0-9]{6,6}[\"']><b>_+</b></[fF][oO][Nn][Tt]>",
							emptyString);
			deltaComment = FIRST_TAGS + deltaComment + LAST_TAGS;
			return deltaComment;
		}
	}

	private String stripStartAndEndTags(String fieldValue) {
		if (StringUtils.isEmpty(fieldValue)) {
			return "";
		}
		int startTagsIndex = FIRST_TAGS.length();
		int endTagsLength = LAST_TAGS.length();
		String strippedOldValue = fieldValue.substring(startTagsIndex);
		strippedOldValue = strippedOldValue.substring(0, strippedOldValue
				.length()
				- endTagsLength);
		return strippedOldValue;
	}

	public String getOldFieldValue(IRecordSet newRs, String fieldName) {

		int newRc = newRs.getRecordCount();
		String oldFieldValue = null;
		for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
			String fieldNameRs = newRs.getFieldValueAsString("AP_FIELD_NAME");
			if (fieldNameRs.equals(fieldName)) {
				oldFieldValue = newRs.getFieldValueAsString("AP_OLD_VALUE");
			}
		}
		return oldFieldValue;
	}

	/**
	 * Returns the value of a particular field in the given GenericArtifact.
	 *
	 * @param individualGenericArtifact
	 * @param fieldName
	 * @return
	 */
	public String getBugIdValueFromGenericArtifactInDefectHandler(
			GenericArtifact individualGenericArtifact, String fieldName) {

		Integer intFieldValue = (Integer) individualGenericArtifact
				.getAllGenericArtifactFieldsWithSameFieldName(fieldName).get(0)
				.getFieldValue();
		String fieldValue = Integer.toString(intFieldValue.intValue());
		return fieldValue;
	}

	/**
	 * For a given transactionId, this returns whether it is a create or update
	 * operation.
	 *
	 * @param qcc
	 * @param txnId
	 * @return
	 */
	public boolean checkForCreate(IConnection qcc, int txnId) {

		Boolean check = false;
		int newRc = 0;
		String sql = "SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID= '"
				+ txnId + "'";
		IRecordSet newRs = null;
		try {
			newRs = executeSQL(qcc, sql);
			if (newRs != null)
				newRc = newRs.getRecordCount();

			for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
				String fieldName = newRs.getFieldValueAsString("AP_FIELD_NAME");
				String oldFieldValue = null;
				if (!(fieldName.equals("BG_DESCRIPTION")))
					oldFieldValue = newRs.getFieldValueAsString("AP_OLD_VALUE");
				else
					oldFieldValue = newRs.getFieldValueAsString("AP_OLD_LONG_VALUE");

				if (fieldName.equals("BG_VTS")
						&& (oldFieldValue == null || (oldFieldValue != null && oldFieldValue
								.equals(""))))
					return true;
			}
		} finally {
			if (newRs != null) {
				newRs.safeRelease();
				newRs = null;
			}
		}
		return check;
	}

	public void deleteDefect(String id) {
		// Yet to implement
	}

	public IQCDefect[] getDefectsWithOtherSystemId(IConnection qcc,
			String otherSystemIdField, String otherSystemIdValue) {
		IFactory bugFactory = qcc.getBugFactory();
		IFilter filter = bugFactory.getFilter();
		IFactoryList factoryList;

		log.error("--------------");
		log.error(otherSystemIdField);
		log.error(otherSystemIdValue);
		log.error("--------------");
		filter.setFilter(otherSystemIdField, otherSystemIdValue);
		factoryList = filter.getNewList();

		int factoryListCount = factoryList.getCount();
		IQCDefect[] qcDefectArray = new IQCDefect[factoryListCount];
		for (int i = 1; i <= factoryListCount; ++i) {
			IBug bug = factoryList.getBug(i);
			qcDefectArray[i - 1] = new QCDefect((Bug) bug);
		}
		filter.safeRelease();
		return qcDefectArray;
	}
}
