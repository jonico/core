/**
 * 
 */
package com.collabnet.ccf.pi.qc;

import java.util.ArrayList;
import java.util.List;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactAttachment;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.pi.qc.api.ICommand;
import com.collabnet.ccf.pi.qc.api.IConnection;
import com.collabnet.ccf.pi.qc.api.IRecordSet;

/**
 * @author madan@collab.net
 *
 */
public class QCConfigHelper {

	static final String sfColumnName = "SF_COLUMN_NAME";
	static final String sfColumnType = "SF_COLUMN_TYPE";
	static final String sfUserLabel = "SF_USER_LABEL";
	static final String sfEditStyle = "SF_EDIT_STYLE";
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
	static final String auActionIdFieldName = "AU_ACTION_ID"; 
	static final String apFieldNameFieldName = "AP_FIELD_NAME";

	static final String apOldValueFieldName = "AP_OLD_VALUE";
	static final String apOldLongValueFieldName = "AP_OLD_LONG_VALUE";

	public static IRecordSet executeSQL(IConnection qcc, String sql){
		ICommand command = qcc.getCommand();
		command.setCommandText(sql);
		return command.execute();
	}
	
	private static GenericArtifactField.FieldValueTypeValue convertQCDataTypeToGADatatype(String dataType, String editStyle) {
		
		// TODO: Convert the datatype, editStyle pair to a valid GA type
		if(dataType.equals("char") && (editStyle==null || ( editStyle!=null && editStyle.equals(""))) ) 
			return GenericArtifactField.FieldValueTypeValue.STRING;
		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("UserCombo")) )
			return GenericArtifactField.FieldValueTypeValue.USER;
		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("ListCombo")) )
			return GenericArtifactField.FieldValueTypeValue.LIST;
		if(dataType.equals("memo"))
			return GenericArtifactField.FieldValueTypeValue.HTMLSTRING;
		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("TreeCombo")) )
			return GenericArtifactField.FieldValueTypeValue.STRING;
		if(dataType.equals("number")) 
			return GenericArtifactField.FieldValueTypeValue.INTEGER;
		if(dataType.equals("DATE") && (editStyle!=null && editStyle.equals("DateCombo")) ) 
			return GenericArtifactField.FieldValueTypeValue.DATE;
		
		return GenericArtifactField.FieldValueTypeValue.STRING;
	}
	
	public static GenericArtifact getSchemaFieldsAndValues(IConnection qcc) {

		// Get all the fields in the project represented
		// by qcc
		String sql = "SELECT * FROM SYSTEM_FIELD WHERE SF_TABLE_NAME='BUG'";

		IRecordSet rs = executeSQL(qcc, sql);

		int rc = rs.getRecordCount();
		GenericArtifact genericArtifact = new GenericArtifact();
		for(int cnt = 0 ; cnt < rc ; cnt++, rs.next())
		{
			String columnName = rs.getFieldValue(sfColumnName);
			String columnType = rs.getFieldValue(sfColumnType);
			String fieldDisplayName = rs.getFieldValue(sfUserLabel);
			String editStyle = rs.getFieldValue(sfEditStyle);
			GenericArtifactField field;

			// obtain the GenericArtifactField datatype from the columnType and editStyle
			GenericArtifactField.FieldValueTypeValue fieldValueTypeValue = convertQCDataTypeToGADatatype(columnType, editStyle);
			field = genericArtifact.addNewField(columnName, fieldDisplayName, columnType);
			field.setFieldValueType(fieldValueTypeValue);
			
			// Only for the Comments field, the action value of the GenericArtifactField is set to APPEND. Later, this feature can be upgraded.
			if(columnName!=null && columnName.equals("BG_DEV_COMMENTS"))
				field.setFieldAction(GenericArtifactField.FieldActionValue.APPEND);
			if(columnName!=null && !(columnName.equals("BG_DEV_COMMENTS")) )
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			
			// Obtain the value to set in the field
			if (editStyle == sfListComboValue ) {
				// Get the list values
				String rootId = rs.getFieldValue(sfRootId);
				
				// Obtain the list values
				String subSql = "SELECT * FROM ALL_LISTS WHERE AL_RATHER_ID = " + rootId;
				IRecordSet subRs = executeSQL(qcc, subSql);
				int rsRc = subRs.getRecordCount();

				if ( rsRc > 0 ) {
					List<String> values = new ArrayList<String>();
					for (int rsCnt = 0 ; rsCnt < rsRc ; subRs.next()) {
						String listValue = subRs.getFieldValue(alDescription);
						values.add(listValue);
					}
					field.setFieldValue(values);
				}
			}
			else if (editStyle == sfUserComboValue ) {
				// get the user values
				String subSql = "SELECT * FROM USERS";
				IRecordSet subRs = executeSQL(qcc, subSql);
				int rsRc = subRs.getRecordCount();

				if ( rsRc > 0 ) {
					List<String> values = new ArrayList<String>();
					for (int rsCnt = 0 ; rsCnt < rsRc ; subRs.next()) {
						String userValue = subRs.getFieldValue(usUsername);
						values.add(userValue);
					}
					field.setFieldValue(values);
				}
			}
		}

		return genericArtifact;
	}

	public static GenericArtifact getSchemaFields(IConnection qcc) {

		// Get all the fields in the project represented
		// by qcc
		String sql = "SELECT * FROM SYSTEM_FIELD WHERE SF_TABLE_NAME='BUG'";

		IRecordSet rs = executeSQL(qcc, sql);

		int rc = rs.getRecordCount();
		GenericArtifact genericArtifact = new GenericArtifact();
		for(int cnt = 0 ; cnt < rc ; cnt++, rs.next())
		{
			String columnName = rs.getFieldValue(sfColumnName);
			String columnType = rs.getFieldValue(sfColumnType);
			String fieldDisplayName = rs.getFieldValue(sfUserLabel);
			String editStyle = rs.getFieldValue(sfEditStyle);
			GenericArtifactField field;

			// obtain the GenericArtifactField datatype from the columnType and editStyle
			GenericArtifactField.FieldValueTypeValue fieldValueType = convertQCDataTypeToGADatatype(columnType, editStyle);
			field = genericArtifact.addNewField(columnName, fieldDisplayName, GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
			field.setFieldValueType(fieldValueType);
			
			// Only for the Comments field, the action value of the GenericArtifactField is set to APPEND. Later, this feature can be upgraded.
			if(columnName!=null && columnName.equals("BG_DEV_COMMENTS"))
				field.setFieldAction(GenericArtifactField.FieldActionValue.APPEND);
			if(columnName!=null && !(columnName.equals("BG_DEV_COMMENTS")) )
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
		}

		return genericArtifact;
	}
	
	
	public static GenericArtifact getSchemaAttachments(IConnection qcc, GenericArtifact genericArtifact, String actionId, String entityId, String attachmentName) {
		
		GenericArtifactAttachment attachment;
		List<String> attachmentIdAndType = QCDefectHandler.getFromTable(qcc, entityId, attachmentName); 
		if(attachmentIdAndType!=null) {
			String attachmentId = attachmentIdAndType.get(0); // CR_REF_ID
			String attachmentContentType = attachmentIdAndType.get(1); // CR_REF_TYPE
			String attachmentDescription = attachmentIdAndType.get(2); // CR_DESCRIPTION
			
			attachment = genericArtifact.addNewAttachment(attachmentName, attachmentId, attachmentDescription);
			attachment.setAttachmentAction(GenericArtifactAttachment.AttachmentActionValue.CREATE);
			if(attachmentContentType.equals("File"))
				attachment.setAttachmentContentType(GenericArtifactAttachment.AttachmentContentTypeValue.DATA);
			else
				attachment.setAttachmentContentType(GenericArtifactAttachment.AttachmentContentTypeValue.LINK);
		}
		
		return genericArtifact;
		
	}

}
