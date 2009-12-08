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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IBugFactory;
import com.collabnet.ccf.pi.qc.v90.api.IFactoryList;
import com.collabnet.ccf.pi.qc.v90.api.IFilter;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.collabnet.ccf.pi.qc.v90.api.IRequirement;
import com.collabnet.ccf.pi.qc.v90.api.IRequirementsFactory;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Bug;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Requirement;
/**
 * This class contains several methods that are used by QCHandler, QCAttachmentHandler and QCWriter
 * and are Generic so that some of the functionalities could be reused in other components
 * if necessary. They deal with getting the defects, different fields from QC, writing into file and so on.
 *
 * @author venugopala
 *
 */
public class QCGAHelper {

	private static final Log log = LogFactory.getLog(QCGAHelper.class);
	
	/**
	 * Fetches the QC defect given the connection object and the defect ID to be
	 * fetched.
	 *
	 * @param qcc
	 *            The Connection object
	 * @param id
	 *            The defect ID which needs to be fetched
	 * @return QCDefect QCDefect object that represents a QC Defect
	 *
	 */
	public QCDefect getDefectWithId(IConnection qcc, int id) {
		IBugFactory bf = null;
		IFilter filter = null;
		IFactoryList fl = null;
		IBug bug = null;
		try {
			bf = qcc.getBugFactory();
			filter = bf.getFilter();
			filter.setFilter(QCConfigHelper.bgBugIdFieldName, Integer.toString(id));
			fl = filter.getNewList();
			bug = fl.getBug(1);
		} catch (Exception e) {
			String message = "Exception caught in getDefectWithId of DefectHandler";
			log.error(message);
			throw new CCFRuntimeException(message, e);
		} finally {
			if(fl != null) {
				fl.safeRelease();
			}
			if(filter != null) {
				filter.safeRelease();
			}
			bf = null;
		}
		QCDefect defect = new QCDefect((Bug) bug);

		return defect;
	}
	
	/**
	 * Fetches the QC requirement given the connection object and the requirement ID to be
	 * fetched.
	 *
	 * @param qcc
	 *            The Connection object
	 * @param id
	 *            The requirement ID which needs to be fetched
	 * @return QCDefect QCDefect object that represents a QC Defect
	 *
	 */
	public QCRequirement getRequirementWithId(IConnection qcc, int id) {
		IRequirementsFactory rf = null;
		IFilter filter = null;
		IFactoryList fl = null;
		IRequirement requirement = null;
		try {
			rf = qcc.getRequirementsFactory();
			filter = rf.getFilter();
			filter.setFilter(QCConfigHelper.rqReqIdFieldName, Integer.toString(id));
			fl = filter.getNewList();
			requirement = fl.getRequirement(1);
		} catch (Exception e) {
			String message = "Exception caught in getRequirementWithId of QCHandler";
			log.error(message);
			throw new CCFRuntimeException(message, e);
		} finally {
			if(fl != null) {
				fl.safeRelease();
			}
			if(filter != null) {
				filter.safeRelease();
			}
			rf = null;
		}
		QCRequirement req = new QCRequirement((Requirement) requirement);

		return req;
	}

	/**
	 * Stores the incoming data with the given file name
	 *
	 * @param data array
	 *            The data to be stored
	 * @param fileName
	 *            The Name of the file in a temp Dir to hold the data
	 * @return File object
	 * 			  Pointing to the created File
	 *
	 */
	public File writeDataIntoFile(byte[] data, String fileName) {
		File attachmentFile = null;
		String tempDir = System.getProperty("java.io.tmpdir");
		attachmentFile = new File(tempDir, fileName);
		try {
			FileOutputStream fs = new FileOutputStream(attachmentFile);
			fs.write(data);
			fs.close();
		} catch (Exception e) {
			log.error("Exception while writing the byte array into the file", e);
		}

		return attachmentFile;
	}

	/**
	 * Deletes the temporary file that was used to store the content of the data
	 * and use the fileName of that temporary file to actually ship the
	 * attachment(s).
	 *
	 */
	public boolean deleteTempFile(String fileName) {

		File f = new File(fileName);

		// Make sure the file or directory exists and isn't write protected
		if (!f.exists())
			throw new IllegalArgumentException(
					"Delete: no such file or directory: " + fileName);

		if (!f.canWrite())
			throw new IllegalArgumentException("Delete: write protected: "
					+ fileName);

		// If it is a directory, make sure it is empty
		if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0)
				throw new IllegalArgumentException(
						"Delete: directory not empty: " + fileName);
		}

		// Attempt to delete it
		boolean success = f.delete();

		if (!success)
			throw new IllegalArgumentException("Delete: deletion failed");

		return success;
	}

	/**
	 * Obtains the date at which the defect identified by the entityId was
	 * created
	 *
	 * @param qcc
	 * @param entityId
	 * @return
	 */
	public Date getDefectCreatedDate(IConnection qcc, int entityId) {
		String sql = "SELECT AU_TIME FROM AUDIT_LOG WHERE AU_ENTITY_TYPE='BUG' AND AU_ACTION!='DELETE' AND AU_ENTITY_ID= '"
				+ entityId + "'";
		IRecordSet rs = null;
		String fieldValue = null;
		Date createdOn = new Date();
		try {
			rs = QCHandler.executeSQL(qcc, sql);
			int rc = rs.getRecordCount();

			for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
				if (cnt == 0) {
					fieldValue = rs.getFieldValueAsString("AU_TIME");
					break;
				}
			}
		}
		finally {
			if(rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
		if (fieldValue != null)
			createdOn = DateUtil.parseQCDate(fieldValue);

		return createdOn;
	}

	/**
	 * Gets the BG_VTS value for the given defect from QC as the value is not
	 * populated in the GenericArtifact
	 *
	 * @param qcc
	 * @param actionId
	 * @param entityId
	 * @return
	 */
	public String findVtsFromQC(IConnection qcc, int actionId, int entityId) {

		String sql = "SELECT * FROM AUDIT_LOG WHERE AU_ACTION_ID='" + actionId
				+ "' AND AU_ACTION!='DELETE' AND AU_ENTITY_ID='" + entityId + "'";
		IRecordSet newRs = null;
		try {
			newRs = QCHandler.executeSQL(qcc, sql);
			String auTime = newRs.getFieldValueAsString("AU_TIME");
			return auTime;
		}
		finally {
			if(newRs != null){
				newRs.safeRelease();
				newRs = null;
			}
		}
	}

	/**
	 * Obtains the attachment ID, attachment Type (data or link) and attachment
	 * description for a given entity
	 *
	 * @param qcc
	 * @param entityId
	 * @param attachmentName
	 * @return
	 */
	public List<String> getFromTable(IConnection qcc, String entityId,
			String attachmentName) {

		List<String> attachmentDetails = null;
		String sql = "SELECT CR_REF_ID, CR_REF_TYPE, CR_DESCRIPTION FROM CROS_REF WHERE CR_KEY_1='"
				+ entityId + "' AND CR_REFERENCE like '%" + sanitizeStringForSQLLikeQuery(attachmentName,"\\") + "%' ESCAPE '\\'";
		IRecordSet newRs = null;
		try {
			newRs = QCHandler.executeSQL(qcc, sql);
			if (newRs != null && newRs.getRecordCount() != 0) {
				attachmentDetails = new ArrayList<String>();
				String crRefId = newRs.getFieldValueAsString("CR_REF_ID");
				attachmentDetails.add(crRefId);
				String crRefType = newRs.getFieldValueAsString("CR_REF_TYPE");
				attachmentDetails.add(crRefType);
				String crDescription = newRs.getFieldValueAsString("CR_DESCRIPTION");
				attachmentDetails.add(crDescription);
			}
		}
		finally {
			if(newRs != null){
				newRs.safeRelease();
			}
		}

		return attachmentDetails;
	}

	/**
	 * This functions modifies a string that still contains special characters
	 * in a format that it can be put into an SQL LIKE query
	 * This method is necessary because we cannot use prepared statements for the
	 * HP QC COM API.
	 *
	 * Security: The method must escape dangerous characters to prevent SQL injection attacks.
	 * Unfortunately, we do not know the underlying data base so that we can only
	 * guess all dangerous characters.
	 *
	 * @param unsanitizedString String that still contains special character
	 * that may conflict with our SQL statement
	 * @param escapeCharacter character that should be used to escape dangerous characters
	 * @return sanitized string ready to use within an SQL LIKE statement
	 */
	public static String sanitizeStringForSQLLikeQuery(String unsanitizedString, String escapeCharacter) {
		// TODO Find a more performant way for string substitution
		unsanitizedString=unsanitizedString.replace(escapeCharacter,escapeCharacter+escapeCharacter);
		unsanitizedString=unsanitizedString.replace("%",escapeCharacter+"%");
		unsanitizedString=unsanitizedString.replace("_",escapeCharacter+"_");
		unsanitizedString=unsanitizedString.replace("'","''");
		return unsanitizedString;
	}

	public String getDeletedAttachmentId(IConnection qcc, String entityId,
			String attachmentName, String deleteTransactionId) {

		String attachmentId = null;
		String sql = "SELECT AU_ENTITY_ID FROM AUDIT_LOG WHERE AU_FATHER_ID="+ deleteTransactionId +" AND AU_ENTITY_TYPE='CROS_REF' AND AU_DESCRIPTION LIKE '%"+sanitizeStringForSQLLikeQuery(attachmentName,"\\")+"' ESCAPE '\\'";
		IRecordSet newRs = null;
		try {
			newRs = QCHandler.executeSQL(qcc, sql);
			if (newRs != null && newRs.getRecordCount() != 0) {
				attachmentId = newRs.getFieldValueAsString("AU_ENTITY_ID");
			}
		}
		finally {
			if(newRs != null){
				newRs.safeRelease();
				newRs = null;
			}
		}
		return attachmentId;
	}

	/**
	 * Gets the value of the lastTransactionId of a defect and a list of descriptions for attachments (if any)
	 *
	 * @param bugId
	 * 			The defectId for which the search has to be made in QC
	 * @param txnId
	 * 			The transactionId starting from which the search has to be made for a particular defectId in QC
	 * @param qcc
	 *            The Connection object
	 * @param connectorUser
	 *            The connectorUser name used while updating the comments
	 * @param resyncUser resync user name
	 * @return List<Object> -> String TransactionId and List of attachment names for the given defectId after the given transactionId
	 */
	public List<Object> getTxnIdAndAuDescriptionForDefect(String bugId, String txnId,
			IConnection qcc, String connectorUser, String resyncUser) {

		List<Object> txnIdAndAuDescription = new ArrayList<Object>();
		String transactionId = null;
		String modifiedBy = null;
		String resyncUserFilter = "";
		if(!StringUtils.isEmpty(resyncUser)){
			resyncUserFilter = "' and au_user !='"+resyncUser;
		}
		Map<String, Map<String, String>> addedAttachmentNames = new TreeMap<String, Map<String, String>>();
		Map<String, Map<String, String>> deletedAttachmentNames = new TreeMap<String, Map<String, String>>();
		String sql = "select AU_TIME, AU_ACTION_ID, AU_DESCRIPTION, AU_USER from audit_log where au_entity_id = '"
				+ bugId
				//Removed and au_father_id='-1'
				+ "' and au_action!='DELETE' and au_entity_type='BUG' and au_user !='"+connectorUser + resyncUserFilter +"' and au_action_id > '"
				+ txnId + "' order by au_action_id desc";
		IRecordSet newRs = null;
		try {
			newRs = QCHandler.executeSQL(qcc, sql);
			int newRc = newRs.getRecordCount();
			log.debug("In QCHandler.getTxnIdAndAuDescriptionForDefect, sql=" + sql);
			if(newRc>0) {
				for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
					if (newCnt == 0){
						transactionId = newRs.getFieldValueAsString("AU_ACTION_ID");
						modifiedBy = newRs.getFieldValueAsString("AU_USER");
					}
					String auDescription = newRs.getFieldValueAsString("AU_DESCRIPTION");
					List<String> attachDescription = getAttachmentOperation(auDescription);
					if (attachDescription != null && attachDescription.size() > 0) {
						if (attachDescription.get(1) != null){
							if(attachDescription.get(1).equals("added")) {
								String addTransactionId = newRs.getFieldValueAsString("AU_ACTION_ID");
								String addTime = newRs.getFieldValueAsString("AU_TIME");
								Map<String, String> values = new TreeMap<String, String>();
								values.put("AU_ACTION_ID", addTransactionId);
								values.put("AU_TIME", addTime);
								addedAttachmentNames.put(attachDescription.get(2), values);
							}
							else if(attachDescription.get(1).equals("deleted")){
								String deleteTransactionId = newRs.getFieldValueAsString("AU_ACTION_ID");
								String deleteTime = newRs.getFieldValueAsString("AU_TIME");
								Map<String, String> values = new TreeMap<String, String>();
								values.put("AU_ACTION_ID", deleteTransactionId);
								values.put("AU_TIME", deleteTime);
								deletedAttachmentNames.put(attachDescription.get(2), values);
							}
						}
					}
				}
			}
		}
		finally {
			if(newRs != null){
				newRs.safeRelease();
				newRs = null;
			}
		}
		txnIdAndAuDescription.add((Object) transactionId);
		txnIdAndAuDescription.add((Object) addedAttachmentNames);
		txnIdAndAuDescription.add((Object) deletedAttachmentNames);
		txnIdAndAuDescription.add((Object) modifiedBy);
		return txnIdAndAuDescription;
	}
	
	/**
	 * Gets the value of the lastTransactionId of a defect and a list of descriptions for attachments (if any)
	 *
	 * @param requirementId
	 * 			The  requirement Id for which the search has to be made in QC
	 * @param txnId
	 * 			The transactionId starting from which the search has to be made for a particular requirement id in QC
	 * @param qcc
	 *            The Connection object
	 * @param connectorUser
	 *            The connectorUser name used while updating the comments
	 * @param resyncUser resync user name
	 * @return List<Object> -> String TransactionId and List of attachment names for the given  requirement id after the given transactionId
	 */
	public List<Object> getTxnIdAndAuDescriptionForRequirement(String requirementId, String txnId,
			IConnection qcc, String connectorUser, String resyncUser) {

		List<Object> txnIdAndAuDescription = new ArrayList<Object>();
		String transactionId = null;
		String modifiedBy = null;
		String resyncUserFilter = "";
		if(!StringUtils.isEmpty(resyncUser)){
			resyncUserFilter = "' and au_user !='"+resyncUser;
		}
		Map<String, Map<String, String>> addedAttachmentNames = new TreeMap<String, Map<String, String>>();
		Map<String, Map<String, String>> deletedAttachmentNames = new TreeMap<String, Map<String, String>>();
		// this property decides whether artifact can already be returned or whether attachment transactions are still not completed due to missing check in
		boolean shipArtifact = false;
		
		String sql = "select AU_TIME, AU_ACTION_ID, AU_DESCRIPTION, AU_USER from audit_log where au_entity_id = '"
				+ requirementId
				//Removed and au_father_id='-1'
				+ "' and au_action!='DELETE' and au_entity_type='REQ' and au_user !='"+connectorUser + resyncUserFilter +"' and au_action_id > '"
				+ txnId + "' order by au_action_id desc";
		IRecordSet newRs = null;
		try {
			newRs = QCHandler.executeSQL(qcc, sql);
			int newRc = newRs.getRecordCount();
			log.debug("In QCHandler.getTxnIdAndAuDescriptionForRequirement, sql=" + sql);
			if(newRc>0) {
				for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
					if (!shipArtifact){
						transactionId = newRs.getFieldValueAsString("AU_ACTION_ID");
						modifiedBy = newRs.getFieldValueAsString("AU_USER");
					}
					String auDescription = newRs.getFieldValueAsString("AU_DESCRIPTION");
					List<String> attachDescription = getAttachmentOperation(auDescription);
					if (attachDescription != null && attachDescription.size() > 0) {
						if (attachDescription.get(1) != null){
							if(attachDescription.get(1).equals("added")) {
								String attachmentName = attachDescription.get(2);
								// here we have to check whether attachment already exists
								if (getFromTable(qcc, requirementId, attachmentName) != null) {
									String addTransactionId = newRs.getFieldValueAsString("AU_ACTION_ID");
									String addTime = newRs.getFieldValueAsString("AU_TIME");
									Map<String, String> values = new TreeMap<String, String>();
									values.put("AU_ACTION_ID", addTransactionId);
									values.put("AU_TIME", addTime);
									addedAttachmentNames.put(attachmentName, values);
									shipArtifact = true;
								} else {
									log.debug("Looks as if attachment transaction has not yet completed.");
								}
							}
							else if(attachDescription.get(1).equals("deleted")){
								String attachmentName = attachDescription.get(2);
								// here we have to check whether attachment already exists
								if (getFromTable(qcc, requirementId, attachmentName) != null) {
									String deleteTransactionId = newRs.getFieldValueAsString("AU_ACTION_ID");
									String deleteTime = newRs.getFieldValueAsString("AU_TIME");
									Map<String, String> values = new TreeMap<String, String>();
									values.put("AU_ACTION_ID", deleteTransactionId);
									values.put("AU_TIME", deleteTime);
									deletedAttachmentNames.put(attachDescription.get(2), values);
									shipArtifact = true;
								} else {
									log.debug("Looks as if attachment transaction has not yet completed.");
								}
							}
							else {
								shipArtifact = true;
							}
						}
					} else {
						shipArtifact = true;
					}
				}
			}
		}
		finally {
			if(newRs != null){
				newRs.safeRelease();
				newRs = null;
			}
		}
		if (shipArtifact) {
			txnIdAndAuDescription.add((Object) transactionId);
			txnIdAndAuDescription.add((Object) addedAttachmentNames);
			txnIdAndAuDescription.add((Object) deletedAttachmentNames);
			txnIdAndAuDescription.add((Object) modifiedBy);
			return txnIdAndAuDescription;
		} else {
			return null;
		}
	}
	
	
	/**
	 * Gets the exact operation of the attachment i.e, added, deleted or updated
	 *
	 * @param auDescription
	 *            This is the field denoting the attachment operation in
	 *            CROS_REF table of QC.
	 * @return List<String> This list contains 1. attachLabel ("attachment") 2.
	 *         exact operation("added" or "updated") 3. attachment ID
	 */
	public List<String> getAttachmentOperation(String auDescription) {

		List<String> attachDescription = new ArrayList<String>();
		if (auDescription != null) {
			int colonPosition = auDescription.indexOf(": ");
			String attachLabelAndOperation = auDescription.substring(0,
					colonPosition);
			String crReference = auDescription.substring(colonPosition + 2,
					auDescription.length());
			/*
			 * StringTokenizer st = new StringTokenizer(auDescription, ": ");
			 *
			 * String attachLabelAndOperation = st.nextToken().trim(); String
			 * crReference = st.nextToken().trim();
			 */
			StringTokenizer newSt = new StringTokenizer(
					attachLabelAndOperation, " ");
			String attachLabel = newSt.nextToken().trim();
			attachDescription.add(attachLabel);
			String operation = newSt.nextToken().trim();
			attachDescription.add(operation);
			attachDescription.add(crReference);

			log.debug(attachDescription);
//			if (operation.equals("added"))
//				return attachDescription;
		}
		return attachDescription;
	}




}
