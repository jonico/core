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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Connection;

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
	
	static final String sfJoinTable = "SF_REFERENCE_TABLE";
	static final String sfJoinRowKey = "SF_REFERENCE_ID_COLUMN";
	static final String sfJoinRowDisplayName = "SF_REFERENCE_NAME_COLUMN";
	static final String sfJoinParentTable = "SF_TABLE_NAME";

	/**
	 * Describe which columns in a child table are joined to which columns in a
	 * parent table in the internal QC DB. The join is always done from the
	 * parent to the child via an 'ID' column. But in the QC GUI (or in CCF) the
	 * user only deals with human-readable 'display' columns
	 */
	static class JoinedField {
		private String m_parentTable;
		private String m_parentCol;
		private String m_childTable;
		private String m_childIdCol;
		private String m_childDisplayCol;

		JoinedField(String parentTable, String parentCol, String childTable,
				String childIdCol, String childDisplayCol) {
			//Log log = LogFactory.getLog(JoinedField.class);
			m_parentTable = parentTable;
			m_parentCol = parentCol;
			m_childTable = childTable;
			m_childIdCol = childIdCol;
			m_childDisplayCol = childDisplayCol;

			if (m_parentTable == null)
				throw new IllegalArgumentException(
						"Parent table name for joined field can't be null");
			if (m_parentCol == null)
				throw new IllegalArgumentException(
						"Parent column name for joined field can't be null");
			if (m_childTable == null)
				throw new IllegalArgumentException(
						"Child table name of joined field can't be null");
			if (m_childIdCol == null)
				throw new IllegalArgumentException(
						"ID column of joined field can't be null");
			if (m_childDisplayCol == null)
				throw new IllegalArgumentException(
						"Display column of joined field can't be null");
		}

		String parentTable() {
			return m_parentTable;
		}

		String parentCol() {
			return m_parentCol;
		}

		String childTable() {
			return m_childTable;
		}

		String childIdCol() {
			return m_childIdCol;
		}

		String childDisplayCol() {
			return m_childDisplayCol;
		}
	}

	/** Contains info on ALL joined columns in ALL tables. */
	private static ArrayList<JoinedField> s_JoinedFields;

	/**
	 * Fill the s_JoinedFields list, which contains info on all joined (a.k.a
	 * 'referenced') fields in all tables. The info in s_JoinedFields describes
	 * how the join is performed (from which parent to which child cols) (e.g.
	 * BUG.BG_DETECTED_IN_REL) Assume this information can not change while HPQC
	 * is running; cache the result so we don't have to repeatedly to SQL
	 * queries
	 */
	private static synchronized ArrayList<JoinedField> queryAllJoinedFields(
			IConnection qcc) {
		final String sql = "SELECT "
			+ sfColumnName + ", "
			+ sfJoinParentTable + ", "
			+ sfJoinTable + ", "
			+ sfJoinRowKey + ", "
			+ sfJoinRowDisplayName
			+" FROM SYSTEM_FIELD WHERE (SF_TABLE_NAME='BUG' OR SF_TABLE_NAME='REQ') AND SF_REFERENCE_TABLE IS NOT NULL";

		ArrayList<JoinedField> joinedFields = s_JoinedFields;
		if (s_JoinedFields == null) {
			// we're changing a static member: make sure only 1 thread at a
			// time
			joinedFields = new ArrayList<JoinedField>();
			IRecordSet rs = null;
			try {
				rs = qcc.executeSQL( sql);
				int rc = rs.getRecordCount();
				for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
					String colName = rs.getFieldValueAsString(sfColumnName);
					String tableName = rs.getFieldValueAsString(sfJoinParentTable);
					String joinTableName = rs.getFieldValueAsString(sfJoinTable);
					String joinRowKey = rs.getFieldValueAsString(sfJoinRowKey);
					String joinRowDisplayName = rs.getFieldValueAsString(sfJoinRowDisplayName);
					joinedFields.add(new JoinedField(tableName, colName,
							joinTableName, joinRowKey, joinRowDisplayName));
					if ("BG_TARGET_REL".equals(colName)
							|| "BG_TARGET_RCYC".equals(colName)) {
						// special case: add corresponding RQ joined fields,
						// because QC doesn't populate
						// the columns for RQ_TARGET_*.
						colName = colName.replaceFirst("BG", "RQ");
						joinedFields.add(new JoinedField("REQ", colName,
								joinTableName, joinRowKey, joinRowDisplayName));
					}
				}
				s_JoinedFields = joinedFields;
			} finally {
				if (rs != null) {
					rs.safeRelease();
				}
			}
		}
		return joinedFields;
	}

	static List<String> getJoinedFieldDisplayNames(IConnection qcc,boolean isDefect, String fieldName) {
		String parentTable = isDefect ? "BUG" : "REQ";
		List<String> result = new ArrayList<String>();
		IRecordSet rs = null;
		try {
			JoinedField f = getJoinedField(qcc, parentTable, fieldName);
			if (f == null) {
				throw new IllegalArgumentException("Not a joined field: \""
						+ String.valueOf(parentTable) + "\".\""
						+ String.valueOf(fieldName) + "\"");
			}

			String sql = "SELECT " + f.childDisplayCol() + " FROM "
					+ f.childTable();
			rs = qcc.executeSQL( sql);
			//String id = rs.getFieldValueAsString(f.childIdCol());
			if (rs != null)  {
				int count = rs.getRecordCount();
				for (int i = 0; i < count; ++i, rs.next()) {
					result.add(rs.getFieldValueAsString(f.childDisplayCol()));
				}
			}
		} finally {
			if (rs != null)
				rs.safeRelease();
		}
		return result;
	}
	
	/**
	 * Map the 'displayed value' of a joined column to the actual primary-key-id
	 * of the child table. In the QC gui, you always see a 'display value', i.e.
	 * something human-readable like a string. This is the same when using CCF -
	 * the foreign (exporting) system will not know the internal primary key
	 * values, but only the 'display value'. But the internal QC database record
	 * actually uses a join (FK) column containing the primary-key value of the
	 * child record. When writing a QC record (Defect, Requirement, etc.) you
	 * need this primary-key value.
	 * 
	 * @param qcc
	 *            QualityCenter OTA connection
	 * @param parentTableName
	 *            the table the joined column is in
	 * @param parentCol
	 *            the name of the column in the parent table
	 * @param parentDisplayColValue
	 *            the value that is 'displayed' in the QC gui
	 * @return the primary key value in the child table corresponding to
	 *         parentDisplayColValue
	 */
	static String mapJoinedFieldDisplayToId(IConnection qcc,
			boolean isDefect, String parentCol, String displayValue) {
		String parentTable = isDefect ? "BUG" : "REQ";
		String id = null;
		if (displayValue != null) {
			IRecordSet rs = null;
			try {
				JoinedField f = getJoinedField(qcc, parentTable, parentCol);
				if (f == null) {
					throw new IllegalArgumentException("Not a joined field: \""
							+ String.valueOf(parentTable) + "\".\""
							+ String.valueOf(parentCol) + "\"");
				}

				String sql = "SELECT " + f.childIdCol() + " FROM "
						+ f.childTable() + " WHERE " + f.childDisplayCol()
						+ " = '" + displayValue + "'";
				rs = qcc.executeSQL( sql);
				id = rs.getFieldValueAsString(f.childIdCol());
			} finally {
				if (rs != null)
					rs.safeRelease();
			}
		}

		if (id == null) {
			String msg = new StringBuilder(
					"Invalid value for Quality Center field ").append(
					parentTable).append(".").append(parentCol).append(": ")
					.append(displayValue).toString();
			//log.error(msg);
			throw new CCFRuntimeException(msg);
		}

		return id;
	}

	/**
	 * Get the JoinedField corresponding to the given (parent) table and column
	 * name
	 */
	static JoinedField getJoinedField(IConnection qcc, String parentTableName,
			String parentColName) {
		JoinedField jf = null;
		for (JoinedField f : queryAllJoinedFields(qcc)) {
			if (f.parentTable().equals(parentTableName)
					&& f.parentCol().equals(parentColName)) {
				jf = f;
				break;
			}
		}
		return jf;
	}

	/**
	 * Certain fields are not directly contained in a given COM object/DB
	 * record; instead their values are obtained by JOINing a row from another
	 * table. These are also known as 'referenced' fields
	 * 
	 * @param tableName
	 *            name of the (parent) table containing joined fields
	 * @param fieldName
	 *            name of the column
	 * @return true if the field is not a simple scalar but instead a
	 *         'referenced' field
	 */
	static boolean isJoinedField(IConnection qcc, String tableName,
			String fieldName) {
		boolean isRField = false;
		if (fieldName != null && tableName != null) {
			JoinedField jf = getJoinedField(qcc, tableName, fieldName);
			isRField = jf != null;
		}
		return isRField;
	}
	
	static boolean isJoinedField(IConnection qcc, boolean isDefect, String fieldName) {
		return isJoinedField(qcc, isDefect ? "BUG" : "REQ", fieldName);
	}

	
	/**
	 * if a field transported by CCF starts with this value, it is assumed to
	 * map to an HTMLString field in QC.
	 */
	static final String HTMLSTRING_PREFIX = "<html><body>";

	final static String QC_BG_DEV_COMMENTS = "BG_DEV_COMMENTS";

	final static String QC_RQ_DEV_COMMENTS = "RQ_DEV_COMMENTS";

	static final String HUMAN_READABLE_SUFFIX = "_HUMAN_READABLE";
	
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
			rs = connection.executeSQL( sql);
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
			String technicalReleaseTypeId) {

		// Get all the fields in the project represented
		// by qcc
		String sql = "SELECT * FROM REQ_TYPE_FIELD rf, system_field sf where sf.sf_user_label is not null and"
				+ " rf.rtf_type_id = '"
				+ technicalReleaseTypeId
				+ "' and rf.rtf_sf_column_name = sf.sf_column_name";
		GenericArtifact genericArtifact = null;
		IRecordSet rs = null;
		try {
			rs = qcc.executeSQL( sql);
			int rc = rs.getRecordCount();
			genericArtifact = new GenericArtifact();
			for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
				String columnName = rs.getFieldValueAsString(sfColumnName);
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
							&& "Y".equals(isMultiValue)) {
						if (editStyle.equals("ListCombo")
								|| editStyle.equals("TreeCombo")) {
							isMultiSelectField = true;
						}
					}
				} else if(fieldValueType.equals(GenericArtifactField.FieldValueTypeValue.INTEGER)) {
					// these edit styles are used for RQ_TARGET_* fields and allow multi-select,
					// even though the column sfIsMultiValue is set to "N".
					isMultiSelectField = "ReleaseCycleMultiTreeCombo".equals(editStyle) ||
										 "ReleaseMultiTreeCombo".equals(editStyle);
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
	
	public static GenericArtifact getSchemaFieldsForDefect(IConnection qcc) {

		// Get all the fields in the project represented
		// by qcc
		String sql = "SELECT * FROM SYSTEM_FIELD WHERE SF_TABLE_NAME='BUG' AND SF_USER_LABEL IS NOT NULL";
		GenericArtifact genericArtifact = null;
		IRecordSet rs = null;
		try {
			rs = qcc.executeSQL( sql);
			int rc = rs.getRecordCount();
			genericArtifact = new GenericArtifact();
			for(int cnt = 0 ; cnt < rc ; cnt++, rs.next())
			{
				String columnName = rs.getFieldValueAsString(sfColumnName);
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
