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



import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;

/**
 * @author madan@collab.net
 *
 */
public class QCConfigHelper {
	
	private static final Log log = LogFactory
	.getLog(QCConfigHelper.class);

	static final String sfColumnName = "SF_COLUMN_NAME";
	static final String sfColumnType = "SF_COLUMN_TYPE";
	static final String sfUserLabel = "SF_USER_LABEL";
	static final String sfEditStyle = "SF_EDIT_STYLE";
	static final String sfIsMultiValue = "SF_IS_MULTIVALUE";

	static final String sfRootId = "SF_ROOT_ID";
	static final String alDescription = "AL_DESCRIPTION";
	static final String usUsername = "US_USERNAME";

	static final String sfListComboValue = "ListCombo";
	static final String sfUserComboValue = "UserCombo";

	static final String numberDataType = "number";
	static final String charDataType = "char";
	static final String memoDataType = "memo";
	static final String dateDataType = "DATE";

	static final String bgBugIdFieldName = "BG_BUG_ID";
	static final String rqReqIdFieldName = "RQ_REQ_ID";
	static final String auActionIdFieldName = "AU_ACTION_ID";
	static final String apFieldNameFieldName = "AP_FIELD_NAME";
	static final String lastModifiedUserFieldName = "LAST_MODIFIED_USER";

	static final String apOldValueFieldName = "AP_OLD_VALUE";
	static final String apOldLongValueFieldName = "AP_OLD_LONG_VALUE";

	static final String CARDINALITY_GREATER_THAN_ONE = "GT1";
	
	/**
	 * if a field transported by CCF starts with this value, it is assumed to
	 * map to an HTMLString field in QC.
	 */
	static final String HTMLSTRING_PREFIX = "<html><body>";

	final static String QC_BG_DEV_COMMENTS = "BG_DEV_COMMENTS";

	final static String QC_RQ_DEV_COMMENTS = "RQ_DEV_COMMENTS";
	
	public Set<String> getFieldsNamesOfType(
			IConnection connection,
			String trackerType, // "BUG" or "REQ"
			String fieldValueType // "memo" for HTMLStrings
			) {

		Set<String> res = new HashSet<String>();

		String sql = String.format(
				"SELECT SF_COLUMN_NAME FROM SYSTEM_FIELD WHERE SF_TABLE_NAME='%s' AND SF_COLUMN_TYPE='%s' AND SF_USER_LABEL IS NOT NULL",
				trackerType,
				fieldValueType);
		IRecordSet rs = null;
		try {
			rs = QCHandler.executeSQL(connection, sql);
			int rc = rs.getRecordCount();
			for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
				String columnName = rs.getFieldValueAsString(sfColumnName);
				res.add(columnName);
			}
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
		return res;
	}

	private static GenericArtifactField.FieldValueTypeValue convertQCDataTypeToGADatatype(String dataType, String editStyle, String columnName) {

		// TODO: Convert the datatype, editStyle pair to a valid GA type
		if(dataType.equals("char") && (editStyle==null || ( editStyle!=null && editStyle.equals(""))) )
			return GenericArtifactField.FieldValueTypeValue.STRING;
		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("UserCombo")) )
			return GenericArtifactField.FieldValueTypeValue.USER;
		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("DateCombo")) )
			return GenericArtifactField.FieldValueTypeValue.DATE;
		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("ListCombo")) ) {
			//if(isMultiValue.equals("N"))
				return GenericArtifactField.FieldValueTypeValue.STRING;
			//if(isMultiValue.equals("Y")) // MULTI_SELECT_LIST
			//	return GenericArtifactField.FieldValueTypeValue.STRING;
		}
		if(dataType.equals("memo"))
			return GenericArtifactField.FieldValueTypeValue.HTMLSTRING;
		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("TreeCombo")) ) {
			//if(isMultiValue.equals("N"))
				return GenericArtifactField.FieldValueTypeValue.STRING;
			//if(isMultiValue.equals("Y")) // MULTI_SELECT_LIST
			//	return GenericArtifactField.FieldValueTypeValue.STRING;
		}
		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("ReqTreeCombo")) ) {
			//if(isMultiValue.equals("N"))
				return GenericArtifactField.FieldValueTypeValue.STRING;
			//if(isMultiValue.equals("Y")) // MULTI_SELECT_LIST
			//	return GenericArtifactField.FieldValueTypeValue.STRING;
		}

		if(dataType.equals("number"))
			return GenericArtifactField.FieldValueTypeValue.INTEGER;
		if(dataType.equals("DATE") && (editStyle!=null && editStyle.equals("DateCombo")) )
			return GenericArtifactField.FieldValueTypeValue.DATE;
		if(dataType.equals("DATE") && editStyle==null)
			return GenericArtifactField.FieldValueTypeValue.DATE;
		if(dataType.equals("time"))
			return GenericArtifactField.FieldValueTypeValue.DATETIME;

		log.debug("Unknown QC data type "+dataType + " of field "+columnName+" defaulting to STRING");
		return GenericArtifactField.FieldValueTypeValue.STRING;
	}

//	public static GenericArtifact getSchemaFieldsAndValues(IConnection qcc) {
//
//		// Get all the fields in the project represented
//		// by qcc
//		String sql = "SELECT * FROM SYSTEM_FIELD WHERE SF_TABLE_NAME='BUG'";
//
//		IRecordSet rs = null;
//		GenericArtifact genericArtifact = null;
//		try {
//			rs = QCDefectHandler.executeSQL(qcc, sql);
//			int rc = rs.getRecordCount();
//			genericArtifact = new GenericArtifact();
//			for(int cnt = 0 ; cnt < rc ; cnt++, rs.next())
//			{
//				String columnName = rs.getFieldValue(sfColumnName);
//				String columnType = rs.getFieldValue(sfColumnType);
//				//String fieldDisplayName = rs.getFieldValue(sfUserLabel);
//				String editStyle = rs.getFieldValue(sfEditStyle);
//				String isMultiValue = rs.getFieldValue(sfIsMultiValue);
//				GenericArtifactField field;
//
//				// obtain the GenericArtifactField datatype from the columnType and editStyle
//				GenericArtifactField.FieldValueTypeValue fieldValueTypeValue = convertQCDataTypeToGADatatype(columnType, editStyle, isMultiValue);
//				field = genericArtifact.addNewField(columnName, columnType);
//				field.setFieldValueType(fieldValueTypeValue);
//
//				// Only for the Comments field, the action value of the GenericArtifactField is set to APPEND. Later, this feature can be upgraded.
//				if(columnName!=null && columnName.equals("BG_DEV_COMMENTS"))
//					field.setFieldAction(GenericArtifactField.FieldActionValue.APPEND);
//				if(columnName!=null && !(columnName.equals("BG_DEV_COMMENTS")) )
//					field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
//
//				// Obtain the value to set in the field
//				if (editStyle == sfListComboValue ) {
//					// Get the list values
//					String rootId = rs.getFieldValue(sfRootId);
//
//					// Obtain the list values
//					String subSql = "SELECT * FROM ALL_LISTS WHERE AL_RATHER_ID = " + rootId;
//					IRecordSet subRs = null;
//					try {
//						subRs = QCDefectHandler.executeSQL(qcc, subSql);
//						int rsRc = subRs.getRecordCount();
//
//						if ( rsRc > 0 ) {
//							List<String> values = new ArrayList<String>();
//							for (int rsCnt = 0 ; rsCnt < rsRc ; subRs.next()) {
//								String listValue = subRs.getFieldValue(alDescription);
//								values.add(listValue);
//							}
//							field.setFieldValue(values);
//						}
//					}
//					finally {
//						if(subRs != null){
//							subRs.safeRelease();
//							subRs = null;
//						}
//					}
//				}
//				else if (editStyle == sfUserComboValue ) {
//					// get the user values
//					String subSql = "SELECT * FROM USERS";
//					IRecordSet subRs = null;
//					try {
//						subRs = QCDefectHandler.executeSQL(qcc, subSql);
//						int rsRc = subRs.getRecordCount();
//
//						if ( rsRc > 0 ) {
//							List<String> values = new ArrayList<String>();
//							for (int rsCnt = 0 ; rsCnt < rsRc ; subRs.next()) {
//								String userValue = subRs.getFieldValue(usUsername);
//								values.add(userValue);
//							}
//							field.setFieldValue(values);
//						}
//					}
//					finally {
//						if(subRs != null) {
//							subRs.safeRelease();
//							subRs = null;
//						}
//					}
//				}
//			}
//		}
//		finally {
//			if(rs != null){
//				rs.safeRelease();
//				rs = null;
//			}
//		}
//
//		return genericArtifact;
//	}

	public static GenericArtifact getSchemaFieldsForRequirement(IConnection qcc,
			String technicalReleaseTypeId, boolean isResync) {

		// Get all the fields in the project represented
		// by qcc
		String sql = "SELECT * FROM REQ_TYPE_FIELD rf, system_field sf where sf.sf_user_label is not null and"
				+ " rf.rtf_type_id = '"
				+ technicalReleaseTypeId
				+ "' and rf.rtf_sf_column_name = sf.sf_column_name";
		GenericArtifact genericArtifact = null;
		IRecordSet rs = null;
		try {
			rs = QCHandler.executeSQL(qcc, sql);
			int rc = rs.getRecordCount();
			genericArtifact = new GenericArtifact();
			for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
				String columnName = rs.getFieldValueAsString(sfColumnName);
				if (isResync && columnName.equals(QC_RQ_DEV_COMMENTS)) {
					continue;
				}
				String columnType = rs.getFieldValueAsString(sfColumnType);

				// we only transport fields that have been configured by the
				// user
				String fieldDisplayName = rs.getFieldValueAsString(sfUserLabel);
				if (fieldDisplayName == null) {
					continue;
				}

				String editStyle = rs.getFieldValueAsString(sfEditStyle);
				// String isMultiValue =
				// rs.getFieldValueAsString(sfIsMultiValue);
				GenericArtifactField field;

				// obtain the GenericArtifactField datatype from the columnType
				// and editStyle
				GenericArtifactField.FieldValueTypeValue fieldValueType = convertQCDataTypeToGADatatype(
						columnType, editStyle, columnName);

				boolean isMultiSelectField = false;
				if (fieldValueType
						.equals(GenericArtifactField.FieldValueTypeValue.STRING)) {
					String isMultiValue = rs
							.getFieldValueAsString(sfIsMultiValue);

					if (columnType.equals("char") && editStyle != null
							&& isMultiValue != null
							&& !StringUtils.isEmpty(isMultiValue)
							&& isMultiValue.equals("Y")) {
						if (editStyle.equals("ListCombo")
								|| editStyle.equals("TreeCombo")) {
							isMultiSelectField = true;
						}
					}
				}

				field = genericArtifact.addNewField(columnName,
						GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				field.setFieldValueType(fieldValueType);
				field
						.setMaxOccursValue(isMultiSelectField ? GenericArtifactField.UNBOUNDED
								: "1");

				field
						.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				
				// special treatment for some fields (have to find out whether this is only due to the agile accelarator)
				if (columnName.equals("RQ_TYPE_ID")) {
					field.setFieldValueType(FieldValueTypeValue.STRING);
				}
				else if (columnName.equals("RQ_REQ_TIME")) {
					field.setFieldValueType(FieldValueTypeValue.STRING);
				}
				else if (columnName.equals("RQ_VC_CHECKOUT_TIME")) {
					field.setFieldValueType(FieldValueTypeValue.STRING);
				}
			}
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}

		return genericArtifact;
	}	
	
	public static GenericArtifact getSchemaFieldsForDefect(IConnection qcc, boolean isResync) {

		// Get all the fields in the project represented
		// by qcc
		String sql = "SELECT * FROM SYSTEM_FIELD WHERE SF_TABLE_NAME='BUG' AND SF_USER_LABEL IS NOT NULL";
		GenericArtifact genericArtifact = null;
		IRecordSet rs = null;
		try {
			rs = QCHandler.executeSQL(qcc, sql);
			int rc = rs.getRecordCount();
			genericArtifact = new GenericArtifact();
			for(int cnt = 0 ; cnt < rc ; cnt++, rs.next())
			{
				String columnName = rs.getFieldValueAsString(sfColumnName);
				if(isResync && columnName.equals(QCConfigHelper.QC_BG_DEV_COMMENTS)) {
					continue;
				}
				String columnType = rs.getFieldValueAsString(sfColumnType);
				
				// we only transport fields that have been configured by the user
				String fieldDisplayName = rs.getFieldValueAsString(sfUserLabel);
				if (fieldDisplayName == null) {
					continue;
				}
				
				String editStyle = rs.getFieldValueAsString(sfEditStyle);
				// String isMultiValue = rs.getFieldValueAsString(sfIsMultiValue);
				GenericArtifactField field;

				// obtain the GenericArtifactField datatype from the columnType and editStyle
				GenericArtifactField.FieldValueTypeValue fieldValueType = convertQCDataTypeToGADatatype(columnType, editStyle, columnName);
				
				boolean isMultiSelectField=false;
				if (fieldValueType.equals(GenericArtifactField.FieldValueTypeValue.STRING)) {
					String isMultiValue = rs.getFieldValueAsString(sfIsMultiValue);

					if(columnType.equals("char") && editStyle!=null && isMultiValue!=null && !StringUtils.isEmpty(isMultiValue) &&
							isMultiValue.equals("Y")){
						if(editStyle.equals("ListCombo") || editStyle.equals("TreeCombo")) {
							isMultiSelectField=true;
						}
					}
				}
				
				field = genericArtifact.addNewField(columnName, GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				field.setFieldValueType(fieldValueType);
				field.setMaxOccursValue(isMultiSelectField?GenericArtifactField.UNBOUNDED:"1");

				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				
				if (columnName.equals("BG_VC_CHECKOUT_TIME")) {
					field.setFieldValueType(FieldValueTypeValue.STRING);
				}
				
			}
		}
		finally {
			if(rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}

		return genericArtifact;
	}


	/*public static GenericArtifact getSchemaAttachments(IConnection qcc, GenericArtifact genericArtifact, String actionId, String entityId, String attachmentName) {

		GenericArtifactAttachment attachment;
		List<String> attachmentIdAndType = QCDefectHandler.getFromTable(qcc, entityId, attachmentName);
		if(attachmentIdAndType!=null) {
			String attachmentId = attachmentIdAndType.get(0); // CR_REF_ID
			String attachmentContentType = attachmentIdAndType.get(1); // CR_REF_TYPE
			String attachmentDescription = attachmentIdAndType.get(2); // CR_DESCRIPTION

			if(attachmentContentType.equals("File")) {
				attachment = genericArtifact.addNewAttachment(attachmentName, attachmentId, attachmentDescription);
				attachment.setAttachmentContentType(GenericArtifactAttachment.AttachmentContentTypeValue.DATA);
			}
			else {
				attachment = genericArtifact.addNewAttachment(attachmentId, attachmentId, attachmentDescription);
				attachment.setAttachmentContentType(GenericArtifactAttachment.AttachmentContentTypeValue.LINK);
			}
			attachment.setAttachmentAction(GenericArtifactAttachment.AttachmentActionValue.CREATE);
		}

		return genericArtifact;

	}

	public static GenericArtifact getCompleteSchemaAttachments(IConnection qcc, String actionId, String entityId, List<String> attachmentNames) {

		GenericArtifact genericArtifact = new GenericArtifact();
		if(attachmentNames!=null) {
			for(int cnt=0; cnt < attachmentNames.size(); cnt++) {

			GenericArtifactAttachment attachment;
			List<String> attachmentIdAndType = QCDefectHandler.getFromTable(qcc, entityId, attachmentNames.get(cnt));
			if(attachmentIdAndType!=null) {
				String attachmentId = attachmentIdAndType.get(0); // CR_REF_ID
				String attachmentContentType = attachmentIdAndType.get(1); // CR_REF_TYPE
				String attachmentDescription = attachmentIdAndType.get(2); // CR_DESCRIPTION

				if(attachmentContentType.equals("File")) {
					attachment = genericArtifact.addNewAttachment(attachmentNames.get(cnt), attachmentId, attachmentDescription);
					attachment.setAttachmentContentType(GenericArtifactAttachment.AttachmentContentTypeValue.DATA);
				}
				else {
					attachment = genericArtifact.addNewAttachment(attachmentId, attachmentId, attachmentDescription);
					attachment.setAttachmentContentType(GenericArtifactAttachment.AttachmentContentTypeValue.LINK);
				}
				attachment.setAttachmentAction(GenericArtifactAttachment.AttachmentActionValue.CREATE);
			}

			}
		}
		return genericArtifact;

	}*/

}
