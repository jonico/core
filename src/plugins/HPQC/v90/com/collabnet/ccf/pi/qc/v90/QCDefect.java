/**
 * 
 */
package com.collabnet.ccf.pi.qc.v90;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactAttachment;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IFactory;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Bug;
import com.jacob.com.Dispatch;

/**
 * @author Collabnet (c) 2008
 * 
 */
public class QCDefect extends Bug implements IQCDefect {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GenericArtifact genericArtifact;
	List<byte[]> attachmentData;

	private static final Log log = LogFactory.getLog(QCDefect.class);
	/**
	 * This is the maximum size of the aggregate of attachments allowed to be
	 * shipped in one cycle.
	 */
	private static final long maxAttachmentSizePerCycle = 5000000;
	private static long cumulativeAttachmentSize = 0;

	public QCDefect(Dispatch arg0) {
		super(arg0);
	}

	public QCDefect(Bug bug) {
		super(bug);
	}

	public GenericArtifact getGenericArtifact() {
		return genericArtifact;
	}

	public void setGenericArtifact(GenericArtifact genericArtifact) {
		this.genericArtifact = genericArtifact;
	}

	public GenericArtifact getGenericArtifactObject(IConnection qcc) {
		GenericArtifact dummyArtifact = new GenericArtifact();
		return dummyArtifact;
	}

	public GenericArtifact getGenericArtifactObject(IConnection qcc,
			String actionId, String entityId, List<String> attachmentNames) {
		genericArtifact = QCConfigHelper.getSchemaFields(qcc);
		// if (attachmentNames != null)
		// genericArtifact = QCConfigHelper
		// .getCompleteSchemaAttachments(qcc, genericArtifact, actionId,
		// entityId, attachmentNames);
		List<GenericArtifactField> allFields = genericArtifact
				.getAllGenericArtifactFields();
		int noOfFields = allFields.size();
		for (int cnt = 0; cnt < noOfFields; cnt++) {
			GenericArtifactField thisField = allFields.get(cnt);

			GenericArtifactField.FieldActionValue thisFieldsActionValue = thisField
					.getFieldAction();
			if (thisFieldsActionValue
					.equals(GenericArtifactField.FieldActionValue.REPLACE)) {
				thisField
						.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			}
			if (thisFieldsActionValue
					.equals(GenericArtifactField.FieldActionValue.APPEND)) {
				thisField
						.setFieldAction(GenericArtifactField.FieldActionValue.APPEND);
			} else
				thisField
						.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);

			// thisField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);

			thisField.setFieldValueHasChanged(true);

			GenericArtifactField.FieldValueTypeValue thisFieldsDatatype = thisField
					.getFieldValueType();

			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.DATE)) {
				thisField
						.setFieldValue(getFieldAsDate(thisField.getFieldName()));
			}
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.INTEGER)) {
				thisField
						.setFieldValue(getFieldAsInt(thisField.getFieldName()));
			}
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.HTMLSTRING)) {
				thisField.setFieldValue(getFieldAsString(thisField
						.getFieldName()));
			}
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.LIST)) {
				thisField.setFieldValue(getFieldAsString(thisField
						.getFieldName()));
			}
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.USER)) {
				thisField.setFieldValue(getFieldAsString(thisField
						.getFieldName()));
			}
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.BASE64STRING)) {
				thisField
						.setFieldValue(getFieldAsInt(thisField.getFieldName()));
			}
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.MULTI_SELECT_LIST)) {
				String fieldValue = getFieldAsString(thisField.getFieldName());
				List<String> fieldValues = new ArrayList<String>();
				int size = 0;
				if (fieldValue != null) {
					fieldValues = getFieldValues(fieldValue);
					size = fieldValues.size();
				}
				if (size >= 1)
					thisField.setFieldValue(fieldValues.get(0));
				for (int sizeCnt = 1; sizeCnt < size; sizeCnt++) {
					GenericArtifactField field;
					field = genericArtifact.addNewField(thisField
							.getFieldName(), thisField.getFieldDisplayName(),
							GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					field
							.setFieldValueType(GenericArtifactField.FieldValueTypeValue.MULTI_SELECT_LIST);
					field.setFieldValue(fieldValues.get(sizeCnt));
				}
			}
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.STRING)) {
				/*
				 * TODO: datatype can also be one of the other
				 * GenericArtifactField.FieldValueTypeValue types. Handle them
				 * appropriately.
				 */
				String fieldName = thisField.getFieldName();
				String fieldValueAsString = new String();
				if (fieldName.equals("BG_VTS")) {
					String fieldValue = getFieldAsString(fieldName);
					thisField.setFieldValue(fieldValue);
				} else {
					fieldValueAsString = getFieldAsString(fieldName);
					thisField.setFieldValue(fieldValueAsString);
				}

			}
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.UNKNOWN)) {
				thisField.setFieldValue(getFieldAsString(thisField
						.getFieldName()));
			}

		}

		List<GenericArtifactAttachment> allAttachments = genericArtifact
				.getAllGenericArtifactAttachments();
		int noOfAttachments = 0;
		if (allAttachments != null)
			noOfAttachments = allAttachments.size();
		for (int cnt = 0; cnt < noOfAttachments; cnt++) {

			// if (attachOperation != null) {
			// filling the values for attachment
			IFactory bugFactory = qcc.getBugFactory();
			IBug bug = bugFactory.getItem(entityId);

			GenericArtifactAttachment thisAttachment = allAttachments.get(cnt);
			MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap();
			String thisMimeType = mimeType.getContentType(thisAttachment
					.getAttachmentName());
			thisAttachment.setMimeType(thisMimeType);

			if (thisAttachment.getAttachmentContentType().equals(
					GenericArtifactAttachment.AttachmentContentTypeValue.DATA)) {
				byte data[] = null;
				try {
					data = bug.retrieveAttachmentData(attachmentNames.get(cnt));
				} catch (Exception e) {
					log
							.error("An Exception!!!!! occured in QCDefect while trying to do retrieveAttachmentData of an INVALID Filename"
									+ e);
					return genericArtifact;
				}
				log.info("************************************************");
				long attachmentSize = (long) data.length;
				thisAttachment.setAttachmentSize(attachmentSize);
				thisAttachment.setRawAttachmentData(data);
				thisAttachment.setAttachmentSourceUrl("VALUE_UNKNOWN");
			} else {
				thisAttachment.setAttachmentSourceUrl(attachmentNames.get(cnt));
				thisAttachment.setAttachmentSize(0);
			}
		}

		if (allAttachments != null) {
			Boolean attachmentSizeChecker = checkForAttachmentSize(allAttachments);
			if (attachmentSizeChecker == false)
				return null;
		}

		return genericArtifact;

	}

	public GenericArtifact getGenericArtifactObjectWithOnlyAttachments(
			IConnection qcc, String actionId, String entityId,
			List<String> attachmentNames) {

		if (attachmentNames != null)
			genericArtifact = QCConfigHelper.getCompleteSchemaAttachments(qcc, actionId, entityId, attachmentNames);

		List<GenericArtifactAttachment> allAttachments = genericArtifact
				.getAllGenericArtifactAttachments();
		int noOfAttachments = 0;
		if (allAttachments != null)
			noOfAttachments = allAttachments.size();
		for (int cnt = 0; cnt < noOfAttachments; cnt++) {

			// if (attachOperation != null) {
			// filling the values for attachment
			IFactory bugFactory = qcc.getBugFactory();
			IBug bug = bugFactory.getItem(entityId);

			GenericArtifactAttachment thisAttachment = allAttachments.get(cnt);
			MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap();
			String thisMimeType = mimeType.getContentType(thisAttachment
					.getAttachmentName());
			thisAttachment.setMimeType(thisMimeType);

			if (thisAttachment.getAttachmentContentType().equals(
					GenericArtifactAttachment.AttachmentContentTypeValue.DATA)) {
				byte data[] = null;
				try {
					data = bug.retrieveAttachmentData(attachmentNames.get(cnt));
				} catch (Exception e) {
					log
							.error("An Exception!!!!! occured in QCDefect while trying to do retrieveAttachmentData of an INVALID Filename"
									+ e);
					return genericArtifact;
				}
				log.info("************************************************");
				long attachmentSize = (long) data.length;
				thisAttachment.setAttachmentSize(attachmentSize);
				thisAttachment.setRawAttachmentData(data);
				thisAttachment.setAttachmentSourceUrl("VALUE_UNKNOWN");
			} else {
				thisAttachment.setAttachmentSourceUrl(attachmentNames.get(cnt));
				thisAttachment.setAttachmentSize(0);
			}
		}
		if (allAttachments != null) {
			Boolean attachmentSizeChecker = checkForAttachmentSize(allAttachments);
			if (attachmentSizeChecker == false)
				return null;
		}

		return genericArtifact;
	}

	public List<String> getFieldValues(String fieldValue) {

		List<String> fieldValues = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(fieldValue, ";");
		while (st.hasMoreTokens()) {
			String thisFieldValue = st.nextToken();
			fieldValues.add(thisFieldValue);
		}
		return fieldValues;
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

}
