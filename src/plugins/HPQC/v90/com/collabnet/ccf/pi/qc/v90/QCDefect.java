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

/**
 *
 */
package com.collabnet.ccf.pi.qc.v90;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
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
	private GenericArtifact genericArtifact;
	List<byte[]> attachmentData;

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(QCDefect.class);
	/**
	 * This is the maximum size of the aggregate of attachments allowed to be
	 * shipped in one cycle.
	 */
	// private static final long maxAttachmentSizePerCycle = 5000000;
	// private static long cumulativeAttachmentSize = 0;
	static final String CARDINALITY_GREATER_THAN_ONE = "GT1";
	static final String sfColumnName = "SF_COLUMN_NAME";
	static final String sfColumnType = "SF_COLUMN_TYPE";
	static final String sfUserLabel = "SF_USER_LABEL";
	static final String sfEditStyle = "SF_EDIT_STYLE";
	static final String sfIsMultiValue = "SF_IS_MULTIVALUE";

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

	/**
	 * Constructs the GenericArtifact Java object for the defect after getting
	 * the schema from getSchemaAttachment method It also populates all the
	 * values into the defect artifact.
	 * 
	 * The logic for including attachment fields have been DEPRECATED and not
	 * used. This piece of the code, however, has not been removed since it
	 * might be needed in future if there are any changes with respect to making
	 * attachments as first class citizens.
	 * 
	 * @param qcc
	 * @param actionId
	 * @param entityId
	 * @param attachmentNames (DEPRECATED)
	 * @param isResync
	 * @param lastModifiedBy 
	 * @return GenericArtifact Containing all the field values.
	 */
	public GenericArtifact getGenericArtifactObject(IConnection qcc,
			String actionId, String entityId, int commentDescriber,
			int commentQualifier, List<String> attachmentNames,
			String syncInfoTransactionId, String connectorUser,
			QCHandler defectHandler, String sourceSystemTimezone,
			boolean isResync, String lastModifiedBy) {
		genericArtifact = QCConfigHelper.getSchemaFieldsForDefect(qcc, isResync);

		List<GenericArtifactField> allFields = genericArtifact
				.getAllGenericArtifactFields();
		int noOfFields = allFields.size();
		for (int cnt = 0; cnt < noOfFields; cnt++) {
			GenericArtifactField thisField = allFields.get(cnt);

			thisField.setFieldValueHasChanged(true);
			GenericArtifactField.FieldValueTypeValue thisFieldsDatatype = thisField
					.getFieldValueType();

            String fieldName = thisField.getFieldName();
            boolean isJoinedField = QCConfigHelper.isJoinedField(qcc, true, fieldName); 
            if (isJoinedField) {
                // Assume the 'Name' property is what is wanted from these
                // referenced objects. Could maybe introduce something like
                // QCConfigHelper.mapJoinedFieldIdToDisplay() to do this, but it
                // looks like there's no way to map from OTA property names to
                // DB column names. In any case Bugs have only 4 reference props
                // and they all have a Name attribute, so the assumption is safe.
            	
            	
            	// set the referenced ID number as this field's value.
            	Integer thisFieldVal = getReferencedFieldAsInt(fieldName, "ID");
            	thisField.setFieldValue(thisFieldVal);
            	
				/*
				 * since joined fields map to an (integer) ID, but customers
				 * might be interested in the string value of the mapped field,
				 * we add a synthetic field to the genericArtifact, reflecting
				 * the attributes contained in this field. This field is of type
				 * STRING and has _HUMAN_READABLE appended to its name.
				 */
            	String syntheticFieldName = thisField.getFieldName() + QCConfigHelper.HUMAN_READABLE_SUFFIX;
                String syntheticFieldValue = getReferencedFieldAsString(fieldName, "Name");
            	GenericArtifactField syntheticField = genericArtifact.addNewField(syntheticFieldName, GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
                syntheticField.setAlternativeFieldName(syntheticFieldName);
                syntheticField.setFieldValue(syntheticFieldValue);
                syntheticField.setFieldValueType(FieldValueTypeValue.STRING);
                // copy over the other attributes.
                syntheticField.setFieldAction(thisField.getFieldAction());
                syntheticField.setFieldValueHasChanged(thisField.getFieldValueHasChanged());
                syntheticField.setMaxOccurs(thisField.getMaxOccurs());
                syntheticField.setMaxOccursValue(thisField.getMaxOccursValue());
                syntheticField.setMinOccurs(thisField.getMinOccurs());
                syntheticField.setMinOccursValue(thisField.getMinOccursValue());
                syntheticField.setNullValueSupported(thisField.getNullValueSupported());
            } else if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.DATE)
					|| thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.DATETIME)) {
				String connectorSystemTimeZone = TimeZone.getDefault()
						.getID();
				Date dateValue = getFieldAsDate(thisField.getFieldName());
				if (dateValue != null) {
					if (DateUtil.isAbsoluteDateInTimezone(dateValue,
							connectorSystemTimeZone)) {
						dateValue = DateUtil.convertToGMTAbsoluteDate(
								dateValue, connectorSystemTimeZone);
						thisField
								.setFieldValueType(GenericArtifactField.FieldValueTypeValue.DATE);
						thisField.setFieldValue(dateValue);
					} else {
						thisField
								.setFieldValueType(GenericArtifactField.FieldValueTypeValue.DATETIME);
						thisField.setFieldValue(dateValue);
					}
				}
			} else if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.INTEGER)) {
				thisField.setFieldValue(getFieldAsInt(thisField.getFieldName()));
			} else if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.HTMLSTRING)) {
				thisField.setFieldValue(getFieldAsString(thisField.getFieldName()));
			} else if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.USER)) {
				// INFO Changes for user attributes handling...
				String fieldValue = getFieldAsString(thisField.getFieldName());

				if (StringUtils.isEmpty(fieldValue) || fieldValue == null) {
					thisField.setFieldValue(null);
				} else {
					StringTokenizer st = new StringTokenizer(fieldValue, ";");
					boolean firstFieldValue = true;
					while (st.hasMoreTokens()) {
						String thisFieldValue = st.nextToken();
						if (firstFieldValue) {
							firstFieldValue = false;
							thisField.setFieldValue(thisFieldValue);
						} else {
							if (!StringUtils.isEmpty(thisFieldValue)) {
								GenericArtifactField field = genericArtifact
										.addNewField(
												thisField.getFieldName(),
												GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
								field
										.setFieldValueType(GenericArtifactField.FieldValueTypeValue.USER);
								field.setFieldValue(thisFieldValue);
							}
						}
					}
				}
			} else if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.BASE64STRING)) {
				thisField.setFieldValue(getFieldAsInt(thisField.getFieldName()));
			} else if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.STRING)) {
				if (thisField.getMaxOccurs() == GenericArtifactField.CARDINALITY_UNBOUNDED) {

					String fieldValue = getFieldAsString(thisField
							.getFieldName());
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
						field = genericArtifact
								.addNewField(
										thisField.getFieldName(),
										GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
						field
								.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
						field.setFieldValue(fieldValues.get(sizeCnt));
					}

				} else {
					String fieldValueAsString = null;
					if (fieldName.equals("BG_VTS")) {
						Date dateFieldValue = getFieldAsDate(fieldName);
						thisField.setFieldValue(dateFieldValue);
						// thisField.setFieldValue(DateUtil.formatQCDate(dateFieldValue));
						thisField
								.setFieldValueType(GenericArtifactField.FieldValueTypeValue.DATETIME);
					} else {
						fieldValueAsString = getFieldAsString(fieldName);
						thisField.setFieldValue(fieldValueAsString);
					}
				}
			}
		}
		// If this is a resync request do not ship comments
		if (!isResync) {
			IRecordSet auditPropertiesRS = null;
			String deltaComment = null;
			try {
				List<String> txnIds = defectHandler.getTransactionIdsInRangeForDefects(qcc,
					Integer.parseInt(entityId), Integer
					.parseInt(syncInfoTransactionId), Integer
					.parseInt(actionId), connectorUser);

				auditPropertiesRS = defectHandler.getAuditPropertiesRecordSet(
						qcc, txnIds);
				deltaComment = defectHandler
						.getDeltaOfCommentForDefects(auditPropertiesRS);
			} finally {
				if (auditPropertiesRS != null) {
					auditPropertiesRS.safeRelease();
				}
			}
			if (deltaComment != null) {
				genericArtifact.getAllGenericArtifactFieldsWithSameFieldName(
						QCConfigHelper.QC_BG_DEV_COMMENTS).get(0).setFieldValue(deltaComment);
			}
		}

		// add last modified user as a mappable field
		GenericArtifactField field;
		field = genericArtifact.addNewField(
					QCConfigHelper.lastModifiedUserFieldName,
					GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
		field.setFieldValueType(
				GenericArtifactField.FieldValueTypeValue.STRING);
		field.setFieldAction(FieldActionValue.REPLACE);
		field.setFieldValue(lastModifiedBy);

		
		return genericArtifact;

	}
	/*
	 * public boolean isMultiSelectField(String fieldName, IConnection qcc) {
	 * 
	 * String sql =
	 * "SELECT * FROM SYSTEM_FIELD WHERE SF_TABLE_NAME='BUG' AND SF_COLUMN_NAME='"
	 * +fieldName+"'"; int rsCount = 0; IRecordSet rs = null; try { rs =
	 * QCDefectHandler.executeSQL(qcc, sql); if(rs!=null) rsCount =
	 * rs.getRecordCount(); if(rsCount > 0) { String columnType =
	 * rs.getFieldValueAsString(sfColumnType); String editStyle =
	 * rs.getFieldValueAsString(sfEditStyle); String isMultiValue =
	 * rs.getFieldValueAsString(sfIsMultiValue);
	 * 
	 * if(columnType.equals("char") && editStyle!=null && isMultiValue!=null &&
	 * !StringUtils.isEmpty(isMultiValue) && isMultiValue.equals("Y")){
	 * if(editStyle.equals("ListCombo") || editStyle.equals("TreeCombo")) {
	 * return true; } } } } finally { if(rs != null) { rs.safeRelease(); } }
	 * 
	 * return false; }
	 */

	/**
	 * DEPRECATED: As the function is done by the QCAttachmentHandler class,
	 * this method is not used right now.
	 * 
	 * Constructs the GenericArtifact Java object for the attachment after
	 * getting the schema from getSchemaAttachment method of the QCConfigHelper.
	 * It also populates all the values into the attachment artifact.
	 * 
	 * @param qcc
	 * @param actionId
	 * @param entityId
	 * @param attachmentNames
	 * @return GenericArtifact Containing all the field values.
	 */
	/*
	 * public GenericArtifact getGenericArtifactObjectWithOnlyAttachments(
	 * IConnection qcc, String actionId, String entityId, List<String>
	 * attachmentNames) {
	 * 
	 * if (attachmentNames != null) genericArtifact =
	 * QCConfigHelper.getCompleteSchemaAttachments(qcc, actionId, entityId,
	 * attachmentNames);
	 * 
	 * List<GenericArtifactAttachment> allAttachments = genericArtifact
	 * .getAllGenericArtifactAttachments(); int noOfAttachments = 0; if
	 * (allAttachments != null) noOfAttachments = allAttachments.size(); for
	 * (int cnt = 0; cnt < noOfAttachments; cnt++) {
	 * 
	 * // if (attachOperation != null) { // filling the values for attachment
	 * IFactory bugFactory = qcc.getBugFactory(); IBug bug =
	 * bugFactory.getItem(entityId);
	 * 
	 * GenericArtifactAttachment thisAttachment = allAttachments.get(cnt);
	 * MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap(); String
	 * thisMimeType = mimeType.getContentType(thisAttachment
	 * .getAttachmentName()); thisAttachment.setMimeType(thisMimeType);
	 * 
	 * if (thisAttachment.getAttachmentContentType().equals(
	 * GenericArtifactAttachment.AttachmentContentTypeValue.DATA)) { byte data[]
	 * = null; try { data =
	 * bug.retrieveAttachmentData(attachmentNames.get(cnt)); } catch (Exception
	 * e) { log.error(
	 * "An Exception!!!!! occured in QCDefect while trying to do retrieveAttachmentData of an INVALID Filename"
	 * + e); return genericArtifact; }
	 * log.info("************************************************"); long
	 * attachmentSize = (long) data.length;
	 * thisAttachment.setAttachmentSize(attachmentSize);
	 * thisAttachment.setRawAttachmentData(data);
	 * thisAttachment.setAttachmentSourceUrl("VALUE_UNKNOWN"); } else {
	 * thisAttachment.setAttachmentSourceUrl(attachmentNames.get(cnt));
	 * thisAttachment.setAttachmentSize(0); } } if (allAttachments != null) {
	 * Boolean attachmentSizeChecker = checkForAttachmentSize(allAttachments);
	 * if (attachmentSizeChecker == false) return null; }
	 * 
	 * return genericArtifact; }
	 */

	public List<String> getFieldValues(String fieldValue) {

		List<String> fieldValues = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(fieldValue, ";");
		while (st.hasMoreTokens()) {
			String thisFieldValue = st.nextToken();
			fieldValues.add(thisFieldValue);
		}
		return fieldValues;
	}

	/*
	 * public boolean checkForAttachmentSize( List<GenericArtifactAttachment>
	 * allAttachments) {
	 * 
	 * for (int cnt = 0; cnt < allAttachments.size(); cnt++) { long
	 * thisAttachmentSize = allAttachments.get(cnt) .getAttachmentSize();
	 * cumulativeAttachmentSize += thisAttachmentSize; } if
	 * (cumulativeAttachmentSize < maxAttachmentSizePerCycle) return true; else
	 * return false; }
	 */

}
