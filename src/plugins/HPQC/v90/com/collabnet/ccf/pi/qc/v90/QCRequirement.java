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
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Bug;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Requirement;
import com.jacob.com.Dispatch;

/**
 * @author Collabnet (c) 2008
 * 
 */
public class QCRequirement extends Requirement implements IQCRequirement {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private GenericArtifact genericArtifact;
	List<byte[]> attachmentData;

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(QCRequirement.class);
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

	public QCRequirement(Dispatch arg0) {
		super(arg0);
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
	 * @param attachmentNames
	 *            (DEPRECATED)
	 * @param isResync
	 * @return GenericArtifact Containing all the field values.
	 */
	public GenericArtifact getGenericArtifactObject(IConnection qcc,
			String actionId, String entityId, int commentDescriber,
			int commentQualifier, List<String> attachmentNames,
			String syncInfoTransactionId, String connectorUser,
			QCHandler defectHandler, String sourceSystemTimezone,
			boolean isResync, String technicalRequirementsTypeID) {
		genericArtifact = QCConfigHelper.getSchemaFieldsForRequirement(qcc, technicalRequirementsTypeID, isResync);
		List<String> txnIds = defectHandler.getTransactionIdsInRangeForRequirements(qcc,
				Integer.parseInt(entityId), Integer
						.parseInt(syncInfoTransactionId), Integer
						.parseInt(actionId), connectorUser);

		List<GenericArtifactField> allFields = genericArtifact
				.getAllGenericArtifactFields();
		int noOfFields = allFields.size();
		for (int cnt = 0; cnt < noOfFields; cnt++) {
			GenericArtifactField thisField = allFields.get(cnt);

			thisField.setFieldValueHasChanged(true);
			GenericArtifactField.FieldValueTypeValue thisFieldsDatatype = thisField
					.getFieldValueType();

			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.DATE)
					|| thisFieldsDatatype
							.equals(GenericArtifactField.FieldValueTypeValue.DATETIME)) {
				String connectorSystemTimeZone = TimeZone.getDefault()
						.getDisplayName(false, TimeZone.SHORT);
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
			// INFO Changes for user attributes handling...
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.USER)) {
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
			}

			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.BASE64STRING)) {
				thisField
						.setFieldValue(getFieldAsInt(thisField.getFieldName()));
			}
			if (thisFieldsDatatype
					.equals(GenericArtifactField.FieldValueTypeValue.STRING)) {
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
					String fieldName = thisField.getFieldName();
					String fieldValueAsString = null;
					if (fieldName.equals("RQ_VTS")) {
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
				auditPropertiesRS = defectHandler.getAuditPropertiesRecordSet(
						qcc, txnIds);
				deltaComment = defectHandler
						.getDeltaOfCommentForRequirements(auditPropertiesRS);
			} finally {
				if (auditPropertiesRS != null) {
					auditPropertiesRS.safeRelease();
				}
			}
			if (deltaComment != null) {
				genericArtifact.getAllGenericArtifactFieldsWithSameFieldName(
						"RQ_DEV_COMMENTS").get(0).setFieldValue(deltaComment);
			}
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

}
