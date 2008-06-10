package com.collabnet.ccf.pi.qc.v90;

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactAttachment;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.ICommand;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IFactory;
import com.collabnet.ccf.pi.qc.v90.api.IFactoryList;
import com.collabnet.ccf.pi.qc.v90.api.IFilter;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Bug;

/**
 * The Defect handler class provides support for listing and/or edit defects.
 *	
 */
public class QCDefectHandler {

	private static final Log log = LogFactory.getLog(QCDefectHandler.class);
	private QCAttachmentHandler attachmentHandler;

	public static IRecordSet executeSQL(IConnection qcc, String sql) {
		ICommand command = qcc.getCommand();
		command.setCommandText(sql);
		return command.execute();
	}

	public List<IQCDefect> getDefectsWithIds(IConnection qcc, List<Integer> ids) {
		IFactory bf = qcc.getBugFactory();
		IFilter filter = bf.getFilter();

		List<IQCDefect> tasks = new ArrayList<IQCDefect>();
		for (int i = 0; i < ids.size(); ++i) {
			filter.setFilter("BG_BUG_ID", ids.get(i).toString());
			IFactoryList fl = filter.getNewList();

			IBug bug = fl.getBug(1);
			QCDefect defect = new QCDefect((Bug) bug);

			tasks.add(defect);

			fl.safeRelease();
		}

		filter.safeRelease();
		bf.safeRelease();

		return tasks;
	}
	/**
	 * Fetches the QC defect given the connection object and the defect ID to be fetched.
	 * 
	 * @param qcc
	 *            The Connection object
	 * @param id
	 *            The defect ID which needs to be fetched             
	 * @return QCDefect
	 * 			  QCDefect object that represents a QC Defect	
	 * 
	 */
	public QCDefect getDefectWithId(IConnection qcc, int id) {
		IFactory bf = qcc.getBugFactory();
		IFilter filter = bf.getFilter();

		filter.setFilter(QCConfigHelper.bgBugIdFieldName, Integer.toString(id));
		IFactoryList fl = filter.getNewList();

		IBug bug = null;
		try {
			bug = fl.getBug(1);
		} catch (Exception e) {
			log.error("Exception caught in getDefectWithId of DefectHandler");
			return null;
		}
		QCDefect defect = new QCDefect((Bug) bug);

		fl.safeRelease();

		filter.safeRelease();
		bf.safeRelease();

		return defect;
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
			log.error("Exception while writing the byte array into the file"
					+ e);
		}

		return attachmentFile;
	}
	/**
	 * Updates the defect identified by the incoming bugId in QC
	 * 
	 * @param qcc
	 *            The Connection object
	 * @param bugId
	 *            The ID of the defect to be updated
	 * @param List<GenericArtifactField>
	 *            The values of each fields of the defect that need to be updated on the old values.  
	 * @param connectorUser
	 *            The connectorUser name used while updating the comments                      
	 * @return IQCDefect
	 * 			  Updated defect object	
	 * 
	 */
	public IQCDefect updateDefect(IConnection qcc, String bugId,
			List<GenericArtifactField> allFields, String connectorUser)
			throws Exception {

		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.getItem(bugId);
		List<String> allFieldNames = new ArrayList<String>();

		String fieldValue = null;
		for (int cnt = 0; cnt < allFields.size(); cnt++) {

			GenericArtifactField thisField = allFields.get(cnt);
			String fieldName = thisField.getFieldName();
			if (thisField.getFieldValueType().equals(
					GenericArtifactField.FieldValueTypeValue.DATE))
				fieldValue = getProperFieldValue(thisField);
			else
				fieldValue = (String) thisField.getFieldValue();

			if (fieldName.equals("BG_DEV_COMMENTS")) {
				String oldFieldValue = bug.getFieldAsString(fieldName);
				if ((oldFieldValue != null && fieldValue != null && !oldFieldValue
						.equals(fieldValue))
						|| (oldFieldValue == null && fieldValue != null))
					fieldValue = oldFieldValue + " \n" + connectorUser + ":: "
							+ fieldValue;

			}
			if (!(allFieldNames.contains(allFields.get(cnt).getFieldName()))
					&& !(fieldName.equals("BG_BUG_ID")
							|| fieldName.equals("BG_BUG_VER_STAMP")
							|| fieldName.equals("BG_ATTACHMENT") || fieldName
							.equals("BG_VTS"))) {
				try {
					bug.setField(fieldName, fieldValue);
				} catch (Exception e) {
					log
							.error("In QCDefectHandler.updateDefect method: Some inappropriate value/dataType is being set to this fieldName");
					log.error("***For this fieldName: " + fieldName
							+ ", the value that is being set, fieldValue: "
							+ fieldValue);
					return null;
				}
			}

			allFieldNames.add(fieldName);
		}
		bug.post();

		return new QCDefect((Bug) bug);
	}
	/**
	 * Create the defect based on the incoming field values
	 * 
	 * @param qcc
	 *            The Connection object
	 * @param List<GenericArtifactField>
	 *            The values of each fields of the defect that need to be used while creation.  
	 * 
	 * @return IQCDefect
	 * 			  Created defect object	
	 * 
	 */
	public IQCDefect createDefect(IConnection qcc,
			List<GenericArtifactField> allFields) throws Exception {

		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.addItem("Created by the connector");

		List<String> allFieldNames = new ArrayList<String>();
		String fieldValue = null;
		for (int cnt = 0; cnt < allFields.size(); cnt++) {

			GenericArtifactField thisField = allFields.get(cnt);
			String fieldName = thisField.getFieldName();
			if (thisField.getFieldValueType().equals(
					GenericArtifactField.FieldValueTypeValue.DATE))
				fieldValue = getProperFieldValue(thisField);
			else
				fieldValue = (String) thisField.getFieldValue();

			/*
			 * The following fields cannot be set or have some conditions Cannot
			 * be set from here: 1. BG_BUG_ID 2. BG_BUG_VER_STAMP 3. BG_VTS Has
			 * some conditions: 1. BG_SUBJECT -> Can be set to a Valid value
			 * that is present in the list.
			 * 
			 */
			if (!(allFieldNames.contains(allFields.get(cnt).getFieldName()))
					&& !(fieldName.equals("BG_BUG_ID")
							|| fieldName.equals("BG_BUG_VER_STAMP")
							|| fieldName.equals("BG_ATTACHMENT") || fieldName
							.equals("BG_VTS"))) {
				try {
					bug.setField(fieldName, fieldValue);
				} catch (Exception e) {
					log
							.error("In QCDefectHandler.createDefect method: Some inappropriate value/dataType is being set to this fieldName");
					log.error("***For this fieldName: " + fieldName
							+ ", the value that is being set, fieldValue: "
							+ fieldValue);
					return null;
				}
			} else {
				log.info(fieldName);
				// dont do anything
			}

			allFieldNames.add(fieldName);
		}

		bug.post();

		return new QCDefect((Bug) bug);
	}
	/**
	 * DEPRECATED:
	 * 			  New methods to create and fetch attachments can be found in QCAttachmentHandler class.
	 * 
	 * Create the attachment for the defect identified by the incoming bugId in QC
	 * 
	 * @param qcc
	 *            The Connection object
	 * @param entityId
	 *            The ID of the defect to which the attachment need to be created
	 * @param List<GenericArtifactAttachment>
	 *            The values of each fields of the attachment that need to be created  
	 * 
	 */
	public void createAttachment(IConnection qcc, String entityId,
			List<GenericArtifactAttachment> allAttachments) {

		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.getItem(entityId);
		for (int cnt = 0; cnt < allAttachments.size(); cnt++) {
			GenericArtifactAttachment thisAttachment = allAttachments.get(cnt);
			GenericArtifactAttachment.AttachmentContentTypeValue contentTypeValue = thisAttachment
					.getAttachmentContentType();
			String actualFileName = thisAttachment.getAttachmentName();
			int type = 0;
			if (contentTypeValue
					.equals(GenericArtifactAttachment.AttachmentContentTypeValue.DATA)) {
				type = 1;
				byte[] data = thisAttachment.getRawAttachmentData();
				File attachmentFile = writeDataIntoFile(data, actualFileName);
				try {
					bug.createNewAttachment(attachmentFile.getAbsolutePath(),
							type);
				} catch (Exception e) {
					log
							.error("In QCDefectHandler.createAttachment method: Some inappropriate fileName/Type for creating an attachment");
					log.error("***For this fileName: "
							+ attachmentFile.getAbsolutePath()
							+ ", the type that is being set, type: " + type);
					return;
				}
				@SuppressWarnings("unused")
				Boolean deleteStatus = deleteTempFile(attachmentFile
						.getAbsolutePath());
			} else {
				type = 2;
				String link = thisAttachment.getAttachmentSourceUrl();
				bug.createNewAttachment(link, type);
			}

		}

		return;
	}
	/**
	 * Gets the value of the field in a suitable data type.
	 *
	 */
	public String getProperFieldValue(GenericArtifactField thisField) {

		String fieldValue = null;
		GenericArtifactField.FieldValueTypeValue fieldValueTypeValue = thisField
				.getFieldValueType();
		switch (fieldValueTypeValue) {

		case DATE: {
			GregorianCalendar gcal = (GregorianCalendar) thisField
					.getFieldValue();
			fieldValue = DateUtil.formatQCDate(gcal.getTime());
			break;
		}

		}
		return fieldValue;

	}
	/**
	 * Deletes the temporary file that was used to store the content of the data and use the fileName of that temporary file
	 * to actually ship the attachment(s).
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
	 * Return all defects modified between the given time range, in a map
	 * 
	 * @param from
	 *            Start time from which modifications have to be queried.
	 * @param to
	 *            End time upto which modifications have to be queried.
	 * @return null if no modifications were found, else a list of modified
	 *         defects
	 * @throws RemoteException (,
	 *             COMException?)
	 */
	public List<GenericArtifact> getChangedDefects(List<GenericArtifact> modifiedDefectArtifacts, IConnection qcc,
			String connectorUser, String transactionId, String lastReadTime, 
			String sourceArtifactId, String sourceRepositoryId,
			String sourceRepositoryKind, String sourceSystemId,
			String sourceSystemKind, String targetRepositoryId,
			String targetRepositoryKind, String targetSystemId,
			String targetSystemKind) throws Exception {

		// Obtain the transactions that happened within the from and to time
		// Test Values:
		// 1. from="2007-11-05 00:00:00"; to="2007-11-06 00:00:00";
		// 2. from="2007-09-15 00:00:00"; to="2007-10-02 00:00:00";
		int rc = 0;
		String sql = "SELECT AU_ACTION_ID, AU_ENTITY_ID, AU_DESCRIPTION FROM AUDIT_LOG WHERE AU_ENTITY_TYPE = 'BUG'";
		if (transactionId != null && !transactionId.equals(""))
			sql += " AND AU_ACTION_ID > '" + transactionId
					+ "' AND AU_USER !='" + connectorUser + "' ";
		sql += " ORDER BY AU_TIME ASC";
		log.info(sql);

		IRecordSet rs = executeSQL(qcc, sql);
		if (rs != null)
			rc = rs.getRecordCount();
		for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
			String thisTransactionId = rs.getFieldValue("AU_ACTION_ID");
			int actionId = Integer.parseInt(rs.getFieldValue("AU_ACTION_ID"));
			int entityId = Integer.parseInt(rs.getFieldValue("AU_ENTITY_ID"));
			String actionIdAsString = rs.getFieldValue("AU_ACTION_ID");
			String bugId = rs.getFieldValue("AU_ENTITY_ID");

			String auDescription = rs.getFieldValue("AU_DESCRIPTION");
			List<String> attachOperation = null;
			if (auDescription != null
					&& (auDescription != null && !auDescription.equals(""))) {
				try {
					attachOperation = getAttachmentOperation(auDescription);
				} catch (Exception e) {
					log
							.error("An Exception!!!!! caught in the getAttachmentOperation method in QCDefectHandler: "
									+ e);
					attachOperation = null;
				}
			}

			QCDefect latestDefect = getDefectWithId(qcc, entityId);
			GenericArtifact latestDefectArtifact = latestDefect
					.getGenericArtifactObject(qcc, actionIdAsString, bugId,
							attachOperation);

			latestDefectArtifact
					.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
			latestDefectArtifact
					.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);

			Boolean isNewDefect = checkForCreate(qcc, actionId);
			if (isNewDefect == true)
				latestDefectArtifact
						.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
			else
				latestDefectArtifact
						.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);

			/*
			 * if(attachOperation!=null) latestDefectArtifact =
			 * handleAttachments(qcc, bugId, attachOperation,
			 * latestDefectArtifact);
			 */

			sourceArtifactId = getBugIdValueFromGenericArtifactInDefectHandler(
					latestDefectArtifact, "BG_BUG_ID");
			latestDefectArtifact.setSourceArtifactId(sourceArtifactId);
			latestDefectArtifact.setSourceRepositoryId(sourceRepositoryId);
			latestDefectArtifact.setSourceRepositoryKind(sourceRepositoryKind);
			latestDefectArtifact.setSourceSystemId(sourceSystemId);
			latestDefectArtifact.setSourceSystemKind(sourceSystemKind);

			latestDefectArtifact.setTargetRepositoryId(targetRepositoryId);
			latestDefectArtifact.setTargetRepositoryKind(targetRepositoryKind);
			latestDefectArtifact.setTargetSystemId(targetSystemId);
			latestDefectArtifact.setTargetSystemKind(targetSystemKind);
			latestDefectArtifact.setLastReadTransactionId(thisTransactionId);
			latestDefectArtifact = getStateOfDefectAtActionID(qcc, entityId,
					actionId, transactionId, latestDefectArtifact);

			// if(latestDefectArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.CREATE))
			// insertIntoArtifactMapping(sourceArtifactId, sourceSystemId,
			// sourceSystemKind, sourceRepositoryId, sourceRepositoryKind,
			// targetSystemId, targetSystemKind, targetRepositoryId,
			// targetRepositoryKind);

			modifiedDefectArtifacts.add(latestDefectArtifact);
		}

		return modifiedDefectArtifacts;
	}

	/**
	 * Return all defects modified between the given time range, in a map
	 * 
	 * @param lastReadTime
	 *            from time from which modifications have to be queried.
	 * @return null if no modifications were found, else a list of modified
	 *         defects
	 * @throws RemoteException (,
	 *             COMException?)
	 */
	@SuppressWarnings("unchecked")
	public List<String> getLatestChangedDefects(
			List<GenericArtifact> modifiedDefectArtifacts, IConnection qcc,
			String connectorUser, String transactionId, String lastReadTime,
			String sourceArtifactId, String sourceRepositoryId,
			String sourceRepositoryKind, String sourceSystemId,
			String sourceSystemKind, String targetRepositoryId,
			String targetRepositoryKind, String targetSystemId,
			String targetSystemKind) throws Exception {

		attachmentHandler = new QCAttachmentHandler();
		int rc = 0;
		String sql = "SELECT DISTINCT(AU_ENTITY_ID) FROM AUDIT_LOG WHERE AU_ENTITY_TYPE = 'BUG' AND AU_ACTION_ID > '"
				+ transactionId + "' AND AU_USER != '"+ connectorUser+ "' AND AU_FATHER_ID = '-1'";

		log.info(sql);
		
		Map<String, String> defectIdTransactionIdMap = new LinkedHashMap<String, String>(); 
		
		IRecordSet rs = executeSQL(qcc, sql);
		if (rs != null)
			rc = rs.getRecordCount();

		// List<GenericArtifact> modifiedDefectArtifacts = new
		// ArrayList<GenericArtifact>();

		for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
			int entityId = Integer.parseInt(rs.getFieldValue("AU_ENTITY_ID"));
			String bugId = rs.getFieldValue("AU_ENTITY_ID");
			List<Object> transactionIdAndAttachOperation = getTxnIdAndAuDescription(
					bugId, transactionId, qcc, connectorUser);
			if(transactionIdAndAttachOperation==null)
				continue;
			String thisTransactionId = (String) transactionIdAndAttachOperation
					.get(0);
			List<String> attachmentNames = (List<String>) transactionIdAndAttachOperation
					.get(1);
			if(bugId!=null) {
				if(!defectIdTransactionIdMap.containsKey(bugId))
 					defectIdTransactionIdMap.put(bugId, thisTransactionId);
				else{
					defectIdTransactionIdMap.put(bugId, thisTransactionId);
				}
			}
			/*
			log.info("In getLatestChangedDefects, txnId=" + thisTransactionId
					+ " and attachmentNames=" + attachmentNames);
			QCDefect latestDefect = getDefectWithId(qcc, entityId);
			GenericArtifact latestDefectArtifact = latestDefect
					.getGenericArtifactObject(qcc, bugId, bugId,
							attachmentNames);
			if (latestDefectArtifact == null)
				continue;
			latestDefectArtifact = getArtifactAction(latestDefectArtifact, qcc,
					thisTransactionId, entityId, lastReadTime);
			latestDefectArtifact
					.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
			latestDefectArtifact
					.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);

			sourceArtifactId = getBugIdValueFromGenericArtifactInDefectHandler(
					latestDefectArtifact, "BG_BUG_ID");
			latestDefectArtifact = assignValues(latestDefectArtifact,
					sourceArtifactId, sourceRepositoryId, sourceRepositoryKind,
					sourceSystemId, sourceSystemKind, targetRepositoryId,
					targetRepositoryKind, targetSystemId, targetSystemKind,
					thisTransactionId);
			modifiedDefectArtifacts.add(latestDefectArtifact);
			
			if (attachmentNames != null) {
				for (int attachCount = 0; attachCount < attachmentNames.size(); attachCount++) {

					GenericArtifact latestAttachmentArtifact = attachmentHandler
							.getGenericArtifactObjectOfAttachment(qcc, bugId,
									attachmentNames.get(attachCount));
					if(latestAttachmentArtifact==null)
						continue;
					latestAttachmentArtifact = attachmentHandler
							.getArtifactAction(latestAttachmentArtifact, qcc,
									thisTransactionId, entityId, lastReadTime);
					latestAttachmentArtifact
							.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
					latestAttachmentArtifact
							.setArtifactType(GenericArtifact.ArtifactTypeValue.ATTACHMENT);

					latestAttachmentArtifact = assignValues(
							latestAttachmentArtifact, sourceArtifactId,
							sourceRepositoryId, sourceRepositoryKind,
							sourceSystemId, sourceSystemKind,
							targetRepositoryId, targetRepositoryKind,
							targetSystemId, targetSystemKind, thisTransactionId);

					modifiedDefectArtifacts.add(latestAttachmentArtifact);
				}
			}*/
		}
		
		List<String> orderedDefectIds = orderByLatestTransactionIds(defectIdTransactionIdMap);
		log.info("New ordering of defectIds::"+orderedDefectIds);
		return orderedDefectIds;
	}
	/**
	 * Orders the values of the incoming HashMap according to its keys.
	 * 
	 * @param HashMap
	 * 				This hashMap contains the transactionIds as values indexed by their defectIds.
	 * @return List<String>
	 * 				This list of strings is the ordering of the keys of the incoming HashMaps, which are the defectIds, 
	 * 				according to the order of their transactionIds
	 *
	 */
	public List<String> orderByLatestTransactionIds(Map<String, String> defectIdTransactionIdMap) {
		
		List<String> mapKeys = new ArrayList<String>(defectIdTransactionIdMap.keySet());
		List<String> mapValues = new ArrayList<String>(defectIdTransactionIdMap.values());

		defectIdTransactionIdMap.clear();
		TreeSet<String> sortedSet = new TreeSet<String>(mapValues);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;
		for (int i=0; i<size; i++)
		{
			defectIdTransactionIdMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), (String)sortedArray[i]);
		}
		
		List<String> orderedDefectList = new ArrayList<String>(defectIdTransactionIdMap.keySet());
		return orderedDefectList;
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
	 * @return List<Object> -> String TransactionId and List of attachment names for the given defectId after the given transactionId              
	 */
	public List<Object> getTxnIdAndAuDescription(String bugId, String txnId,
			IConnection qcc, String connectorUser) {

		List<Object> txnIdAndAuDescription = new ArrayList<Object>();
		String transactionId = null;
		List<String> attachmentNames = new ArrayList<String>();
		String sql = "select AU_ACTION_ID, AU_DESCRIPTION from audit_log where au_entity_id = '"
				+ bugId
				+ "' and au_entity_type='BUG' and au_user !='"+connectorUser+"' and au_father_id='-1' and au_action_id > '"
				+ txnId + "' order by au_action_id desc";
		IRecordSet newRs = executeSQL(qcc, sql);
		int newRc = newRs.getRecordCount();
		log.info("In QCDefectHandler.getTxnIdAndAuDescription, sql=" + sql);
		if(newRc>0) {
			for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
				if (newCnt == 0)
					transactionId = newRs.getFieldValue("AU_ACTION_ID");
				String auDescription = newRs.getFieldValue("AU_DESCRIPTION");
				List<String> attachDescription = getAttachmentOperation(auDescription);
				if (attachDescription != null && attachDescription.size() > 0) {
					if (attachDescription.get(1) != null
							&& attachDescription.get(1).equals("added"))
						attachmentNames.add(attachDescription.get(2));
					else
						return null;
				}
			}
		}
		txnIdAndAuDescription.add((Object) transactionId);
		txnIdAndAuDescription.add((Object) attachmentNames);

		return txnIdAndAuDescription;
	}
	/**
	 * Finds the state of the defect at a particular defectId and transactionId. This is used while finding a bunch of
	 * history artifacts for a defectId after a particular state represented by the transactionId.
	 * 
	 * @param entityId
	 * 			The defectId for which the search has to be made in QC
	 * @param actionId
	 * 			The transactionId starting from which the search has to be made for a particular defectId in QC
	 * @param transactionId
	 * 			The transactionId starting from which the search has to be made for a particular defectId in QC
	 * @param qcc
	 *            The Connection object
	 * @param latestDefectArtifact
	 *            The GenericArtifact into which the latest state information of the defect identified by entityId is captured.
	 * @return GenericArtifact
	 * 			The resultant GenericArtifact which has the latest state of the defect information in it.              
	 */
	public GenericArtifact getStateOfDefectAtActionID(IConnection qcc,
			int entityId, int actionId, String transactionId,
			GenericArtifact latestDefectArtifact) {

		String sql = "SELECT AU_ACTION_ID FROM AUDIT_LOG WHERE AU_ENTITY_TYPE = 'BUG' AND AU_ACTION_ID > '"
				+ actionId
				+ "' AND AU_ENTITY_TYPE= 'BUG' AND AU_ENTITY_ID = '"
				+ entityId + "'";
		if (transactionId != null && !transactionId.equals(""))
			sql += " AND AU_ACTION_ID >= '" + transactionId + "'";
		sql += " ORDER BY AU_ACTION_ID DESC";
		log.info(sql);

		IRecordSet rs = executeSQL(qcc, sql);
		int rc = rs.getRecordCount();
		int txnId = 0;
		for (int cnt = 0; cnt < rc; cnt++, rs.next()) {

			txnId = Integer.parseInt(rs.getFieldValue("AU_ACTION_ID"));
			sql = "SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID= '"
					+ txnId + "'";
			IRecordSet newRs = executeSQL(qcc, sql);
			int newRc = newRs.getRecordCount();

			for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
				String fieldName = newRs.getFieldValue("AP_FIELD_NAME");
				String oldFieldValue = null;
				if (!(fieldName.equals("BG_DESCRIPTION")))
					oldFieldValue = newRs.getFieldValue("AP_OLD_VALUE");
				else
					oldFieldValue = newRs.getFieldValue("AP_OLD_LONG_VALUE");

				List<GenericArtifactField> genArtifactFields = latestDefectArtifact
						.getAllGenericArtifactFieldsWithSameFieldName(fieldName);
				if (genArtifactFields != null
						& genArtifactFields.get(0) != null)
					genArtifactFields.get(0).setFieldValue(oldFieldValue);
				// genArtifactFields.get(0).setFieldValueHasChanged(true);
			}

		}
		String deltaComment = getDeltaOfComment(qcc, actionId);
		if (deltaComment != null) {
			List<GenericArtifactField> genArtifactFieldsForComments = latestDefectArtifact
					.getAllGenericArtifactFieldsWithSameFieldName("BG_DEV_COMMENTS");
			genArtifactFieldsForComments.get(0).setFieldValue(deltaComment);
		}

		List<GenericArtifactField> genArtifactFields = latestDefectArtifact
				.getAllGenericArtifactFieldsWithSameFieldName("BG_VTS");

		if (genArtifactFields != null && genArtifactFields.get(0) != null
				&& genArtifactFields.get(0).getFieldValue() != null
				&& !(genArtifactFields.get(0).getFieldValue().equals(""))) {
			Date newBgVts = DateUtil.parseQCDate((String) genArtifactFields
					.get(0).getFieldValue());
			latestDefectArtifact.setArtifactLastModifiedDate(DateUtil
					.format(newBgVts));
			return latestDefectArtifact;
		} else {
			// This means the BG_VTS field is null. So, find it, populate it &
			// ArtifactLastModifiedDate
			String bgVts = findBgVtsFromQC(qcc, actionId, entityId);
			genArtifactFields.get(0).setFieldValue((String) bgVts);
			Date newBgVts = DateUtil.parseQCDate(bgVts);
			String lastModifiedDate = DateUtil.format(newBgVts);
			latestDefectArtifact.setArtifactLastModifiedDate(lastModifiedDate);
		}

		// The ArtifactActionValue IGNORE and DELETE needs to be done.
		// latestDefectArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UNKNOWN);
		return latestDefectArtifact;

	}

	/**
	 * Given an action id (id for the AUDIT_LOG table
	 * 
	 * @param actionId
	 * @return the defect at the time of the actionId
	 */
	public QCDefect getArtifactStateFromActionId(int actionId) {
		// return the state of the given defect at transaction actionId

		QCDefect defect = null;
		return defect;
	}

	/**
	 * Assigns values of the incoming parameters to the incoming genericArtifact and returns the updated one.
	 * 
	 * @param latestDefectArtifact
	 * 		The GenericArtifact to which the following values need to be assigned.
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
	 * @return
	 * 		Assigned GenericArtifact
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
		latestDefectArtifact.setLastReadTransactionId(thisTransactionId);

		return latestDefectArtifact;
	}

	/**
	 * Gets the exact operation of the attachment i.e, added, deleted or updated
	 * 
	 * @param auDescription
	 * 			This is the field denoting the attachment operation in CROS_REF table of QC.
	 * @return List<String>
	 * 			This list contains 1. attachLabel ("attachment") 2. exact operation("added" or "updated") 3. attachment ID
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

			log.info(attachDescription);
			if (operation.equals("added"))
				return attachDescription;
		}
		return null;
	}

	/**
	 * Obtains the artifactAction based on the date at which that defect was created and the lastReadTime synchronization parameter.
	 * @param entityId
	 * 			The defectId for which the search has to be made in QC
	 * @param actionId
	 * 			The transactionId at which it needs to be determined if the defect is a create or update.
	 * @param qcc
	 *          The Connection object
	 * @param latestDefectArtifact
	 *          The GenericArtifact into which the artifactAction is populated after it is determined.
	 * @param lastReadTime
	 * 			This is synchronization parameter used to compare with the defect created date and find out the artifactAction.
	 * @return GenericArtifact
	 * 			Updated artifact
	 */
	public GenericArtifact getArtifactAction(
			GenericArtifact latestDefectArtifact, IConnection qcc,
			String actionId, int entityId, String lastReadTime) {

		List<GenericArtifactField> genArtifactFields = latestDefectArtifact
				.getAllGenericArtifactFieldsWithSameFieldName("BG_VTS");
		Date lastReadDate = DateUtil.parseQCDate(lastReadTime);
		Date createdOn = getDefectCreatedDate(qcc, entityId);
		if (genArtifactFields != null && genArtifactFields.get(0) != null) {
			if (genArtifactFields.get(0).getFieldValue()!=null) {
				Date newBgVts = DateUtil.parseQCDate((String) genArtifactFields
						.get(0).getFieldValue());
				latestDefectArtifact.setArtifactLastModifiedDate(DateUtil
						.format(newBgVts));
				if (createdOn.after(lastReadDate))
					latestDefectArtifact
							.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
				else
					latestDefectArtifact
							.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);

				return latestDefectArtifact;
			} else {
				// This means the BG_VTS field is null. So, find it, populate it
				// &
				// ArtifactLastModifiedDate
				String bgVts = findBgVtsFromQC(qcc, Integer.parseInt(actionId),
						entityId);
				genArtifactFields.get(0).setFieldValue((String) bgVts);
				Date newBgVts = DateUtil.parseQCDate(bgVts);
				String lastModifiedDate = DateUtil.format(newBgVts);
				latestDefectArtifact
						.setArtifactLastModifiedDate(lastModifiedDate);
				log.info("The newBgVts=" + newBgVts + ", and lastReadDate="
						+ lastReadDate);
				if (createdOn.after(lastReadDate))
					latestDefectArtifact
							.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
				else
					latestDefectArtifact
							.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
			}
		}

		String deltaComment = getDeltaOfComment(qcc, Integer.parseInt(actionId));
		if (deltaComment != null) {
			List<GenericArtifactField> genArtifactFieldsForComments = latestDefectArtifact
					.getAllGenericArtifactFieldsWithSameFieldName("BG_DEV_COMMENTS");
			genArtifactFieldsForComments.get(0).setFieldValue(deltaComment);
		}

		return latestDefectArtifact;
	}

	/**
	 * Gets the difference between the comment values of the previous and current transaction pointed by actionId
	 * @param qcc
	 * @param actionId
	 * @return
	 */
	public String getDeltaOfComment(IConnection qcc, int actionId) {

		String deltaComment = null;

		String sql = "SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID= '"
				+ actionId + "'";
		IRecordSet newRs = executeSQL(qcc, sql);
		int newRc = newRs.getRecordCount();

		for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
			String fieldName = newRs.getFieldValue("AP_FIELD_NAME");
			if (fieldName.equals("BG_DEV_COMMENTS")) {
				String oldFieldValue = newRs.getFieldValue("AP_OLD_LONG_VALUE");
				String newFieldValue = newRs.getFieldValue("AP_NEW_LONG_VALUE");
				if (oldFieldValue != null
						&& (oldFieldValue != null && !oldFieldValue.equals("")))
					deltaComment = (newFieldValue.substring(0, oldFieldValue
							.length()));
				log.info(deltaComment);
			}
		}

		return deltaComment;
	}

	/**
	 * Obtains the date at which the defect identified by the entityId was created
	 * 
	 * @param qcc
	 * @param entityId
	 * @return
	 */
	public Date getDefectCreatedDate(IConnection qcc, int entityId) {

		String sql = "SELECT AU_TIME FROM AUDIT_LOG WHERE AU_ENTITY_TYPE='BUG' AND AU_ENTITY_ID= '"
				+ entityId + "'";
		IRecordSet rs = executeSQL(qcc, sql);
		String fieldName = null;
		Date createdOn = null;
		int rc = rs.getRecordCount();

		for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
			if (cnt == 0) {
				fieldName = rs.getFieldValue("AU_TIME");
				break;
			}
		}
		if(fieldName!=null)
			createdOn = DateUtil.parseQCDate(fieldName);

		return createdOn;
	}

	/**
	 * Gets the BG_VTS value for the given defect from QC as the value is not populated in the GenericArtifact
	 * @param qcc
	 * @param actionId
	 * @param entityId
	 * @return
	 */
	public String findBgVtsFromQC(IConnection qcc, int actionId, int entityId) {

		String sql = "SELECT * FROM AUDIT_LOG WHERE AU_ACTION_ID='" + actionId
				+ "' AND AU_ENTITY_ID='" + entityId + "'";
		IRecordSet newRs = executeSQL(qcc, sql);
		String auTime = newRs.getFieldValue("AU_TIME");
		return auTime;
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
	 * Obtains the attachment ID, attachment Type (data or link) and attachment description for a given defectId
	 * 
	 * @param qcc
	 * @param entityId
	 * @param attachmentName
	 * @return
	 */
	public static List<String> getFromTable(IConnection qcc, String entityId,
			String attachmentName) {

		List<String> attachmentDetails = null;
		String sql = "SELECT CR_REF_ID, CR_REF_TYPE, CR_DESCRIPTION FROM CROS_REF WHERE CR_KEY_1='"
				+ entityId + "' AND CR_REFERENCE= '" + attachmentName + "'";
		IRecordSet newRs = executeSQL(qcc, sql);
		if (newRs != null && newRs.getRecordCount()!=0) {
			attachmentDetails = new ArrayList<String>();
			String crRefId = newRs.getFieldValue("CR_REF_ID");
			attachmentDetails.add(crRefId);
			String crRefType = newRs.getFieldValue("CR_REF_TYPE");
			attachmentDetails.add(crRefType);
			String crDescription = newRs.getFieldValue("CR_DESCRIPTION");
			attachmentDetails.add(crDescription);
		}

		return attachmentDetails;
	}

	/**
	 * For a given transactionId, this returns whether it is a create or update operation.
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
		IRecordSet newRs = executeSQL(qcc, sql);
		if (newRs != null)
			newRc = newRs.getRecordCount();

		for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
			String fieldName = newRs.getFieldValue("AP_FIELD_NAME");
			String oldFieldValue = null;
			if (!(fieldName.equals("BG_DESCRIPTION")))
				oldFieldValue = newRs.getFieldValue("AP_OLD_VALUE");
			else
				oldFieldValue = newRs.getFieldValue("AP_OLD_LONG_VALUE");

			if (fieldName.equals("BG_VTS")
					&& (oldFieldValue == null || (oldFieldValue != null && oldFieldValue
							.equals(""))))
				return true;
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

		return qcDefectArray;
	}
}
