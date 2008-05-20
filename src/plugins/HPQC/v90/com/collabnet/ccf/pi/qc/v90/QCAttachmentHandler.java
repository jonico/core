package com.collabnet.ccf.pi.qc.v90;

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactAttachment;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.AttachmentMetaData;
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
 * The tracker handler class provides support for listing and/or edit trackers
 * and artifacts.
 */
public class QCAttachmentHandler {

	private static final Log log = LogFactory.getLog(QCDefectHandler.class);
	GenericArtifact genericArtifact;
	/**
	 * This is the maximum size of the aggregate of attachments allowed to be
	 * shipped in one cycle.
	 */
	private static final long maxAttachmentSizePerCycle = 5000000;
	private static long cumulativeAttachmentSize = 0;

//	private static final String[] attachmentMetaData = {
//			"attachmentName", "attachmentId", "attachmentSize", 
//			"attachmentSourceUrl", "attachmentType",
//			"attachmentMIMEType", "attachmentValueHasChanged",
//			"attachmentValueType", "attachmentValueIsNull"};

	public static IRecordSet executeSQL(IConnection qcc, String sql) {
		ICommand command = qcc.getCommand();
		command.setCommandText(sql);
		return command.execute();
	}

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

	public void createAttachment(IConnection qcc, String entityId,
			String attachmentName, String contentTypeValue, byte[] data,
			String attachmentSourceUrl) {

		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.getItem(entityId);

		int type = 0;
		if (contentTypeValue
				.equals("File")) {
			type = 1;
			File attachmentFile = writeDataIntoFile(data, attachmentName);
			try {
				bug.createNewAttachment(attachmentFile.getAbsolutePath(), type);
			} catch (Exception e) {
				log
						.error("In QCAttachmentHandler.createAttachment method: Some inappropriate fileName/Type for creating an attachment");
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
			bug.createNewAttachment(attachmentSourceUrl, type);
		}

		return;
	}

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

	public GenericArtifact getArtifactAction(
			GenericArtifact latestDefectArtifact, IConnection qcc,
			String actionId, int entityId, String lastReadTime) {

		Date lastReadDate = DateUtil.parseQCDate(lastReadTime);
		Date createdOn = getDefectCreatedDate(qcc, entityId);
		if (createdOn!=null && createdOn.after(lastReadDate))
			latestDefectArtifact
					.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
		else
			latestDefectArtifact
					.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);

		return latestDefectArtifact;
	}

	public Date getDefectCreatedDate(IConnection qcc, int entityId) {

		String sql = "SELECT AU_TIME FROM AUDIT_LOG WHERE AU_ENTITY_TYPE='BUG' AND AU_ENTITY_ID= '"
				+ entityId + "'";
		IRecordSet rs = executeSQL(qcc, sql);
		String fieldName = null;
		Date createdOn = new Date();
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

	public static List<String> getFromTable(IConnection qcc, String entityId,
			String attachmentName) {

		List<String> attachmentDetails = null;
		String sql = "SELECT CR_REF_ID, CR_REF_TYPE, CR_DESCRIPTION FROM CROS_REF WHERE CR_KEY_1='"
				+ entityId + "' AND CR_REFERENCE= '" + attachmentName + "'";
		IRecordSet newRs = executeSQL(qcc, sql);
		if (newRs != null) {
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

	public GenericArtifact getGenericArtifactObjectOfAttachment(
			IConnection qcc, String entityId, String attachmentName) {
		long attachmentSize = 0;
		String thisMimeType = null;
		if (attachmentName != null)
			genericArtifact = getSchemaAttachment(qcc, entityId, attachmentName);

		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.getItem(entityId);

		MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap();
		if (attachmentName != null)
			thisMimeType = mimeType.getContentType(attachmentName);
		byte data[] = null;

		String contentType = (String) genericArtifact
				.getAllGenericArtifactFieldsWithSameFieldName(
						"attachmentType").get(0).getFieldValue().toString();
		List<GenericArtifactField> allFields = genericArtifact
				.getAllGenericArtifactFields();
		int noOfFields = allFields.size();
		for (int cnt = 0; cnt < noOfFields; cnt++) {
			GenericArtifactField thisField = allFields.get(cnt);
			thisField
					.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			thisField.setFieldDisplayName(thisField.getFieldName());
			thisField.setFieldValueHasChanged(true);

			if (contentType.equals("DATA")) {
				thisField
						.setFieldValueType(GenericArtifactField.FieldValueTypeValue.BASE64STRING);
				try {
					data = bug.retrieveAttachmentData(attachmentName);
				} catch (Exception e) {
					log
							.error("An Exception!!!!! occured in QCAttachmentHandler.getGenericArtifactObjectOfAttachment"
									+ " while trying to do retrieveAttachmentData of an INVALID Filename"
									+ e);
					return genericArtifact;
				}
				// log.info("************************************************");
				attachmentSize = (long) data.length;
			} else {
				thisField
						.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				// attachmentName = "Unknown";
			}

			if (thisField.getFieldName().equals(AttachmentMetaData.getAttachmentName())) {
				if(contentType.equals("DATA"))
					thisField.setFieldValue(attachmentName);
				else
					thisField.setFieldValue("Unknown");
				thisField
						.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
			}
			if (thisField.getFieldName().equals(AttachmentMetaData.getAttachmentSize())) {
				thisField.setFieldValue(attachmentSize);
				thisField
						.setFieldValueType(GenericArtifactField.FieldValueTypeValue.INTEGER);
			}
			if (thisField.getFieldName().equals(AttachmentMetaData.getAttachmentSourceUrl())) {
				if(contentType.equals("DATA"))
					thisField.setFieldValue("Unknown");
				else
					thisField.setFieldValue(attachmentName);
				thisField
						.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
			}
			if (thisField.getFieldName().equals(AttachmentMetaData.getAttachmentMimeType())) {
				thisField.setFieldValue(thisMimeType);
				thisField
						.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
			}
			if (thisField.getFieldName().equals(AttachmentMetaData.getAttachmentValueIsNull())) {
				if(data!=null)
					thisField.setFieldValue(AttachmentMetaData.AttachmentValueIsNull.FALSE);
				else
					thisField.setFieldValue(AttachmentMetaData.AttachmentValueIsNull.TRUE);
				thisField
						.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
			}
			// if(thisField.getFieldName().equals("attachmentData"))
			// thisField.setFieldValue(data);

		}
		if (data != null) {
			log.info("Here is the data*****"+data.toString());
			genericArtifact.setRawAttachmentData(data);
		}
		// Boolean attachmentSizeChecker =
		// checkForAttachmentSize(allAttachments);
		// if (attachmentSizeChecker == false)
		// return null;

		return genericArtifact;
	}

	public boolean checkForAttachmentSize(
			List<GenericArtifactAttachment> allAttachments) {

		for (int cnt = 0; cnt < allAttachments.size(); cnt++) {
			long thisAttachmentSize = allAttachments.get(cnt)
					.getAttachmentSize();
			cumulativeAttachmentSize += thisAttachmentSize;
		}
		if (cumulativeAttachmentSize < maxAttachmentSizePerCycle)
			return true;
		else
			return false;
	}

	public static GenericArtifact getSchemaAttachment(IConnection qcc,
			String entityId, String attachmentName) {

		GenericArtifact genericArtifact = new GenericArtifact();
		String [] attachmentMetaData = AttachmentMetaData.getAttachmentMetaData();
		if (attachmentName != null) {
			List<String> attachmentIdAndType = QCDefectHandler.getFromTable(
					qcc, entityId, attachmentName);
			if (attachmentIdAndType != null) {
				String attachmentId = attachmentIdAndType.get(0); // CR_REF_ID
				String attachmentType = attachmentIdAndType.get(1); // CR_REF_TYPE
				String attachmentDescription = attachmentIdAndType.get(2); // CR_DESCRIPTION

				for (int cnt = 0; cnt < attachmentMetaData.length; cnt++) {

					GenericArtifactField field;
					field = genericArtifact.addNewField(
							attachmentMetaData[cnt], attachmentMetaData[cnt],
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					
					field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					if (attachmentMetaData[cnt].equals(AttachmentMetaData.getAttachmentType())) {
						if(attachmentType.equals("File"))
							field.setFieldValue(AttachmentMetaData.AttachmentType.DATA);
						else
							field.setFieldValue(AttachmentMetaData.AttachmentType.LINK);
						field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					}
					if (attachmentMetaData[cnt].equals(AttachmentMetaData.getAttachmentId())) {
						field.setFieldValue(attachmentId);
						field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.INTEGER);
					}
					if (attachmentMetaData[cnt].equals(AttachmentMetaData.getAttachmentDescription())) {
						field.setFieldValue(attachmentDescription);
						field
								.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
					}
				}
			}
		}
		return genericArtifact;

	}

	public void deleteAttachment(String id) {
		// Yet to implement
	}
}
