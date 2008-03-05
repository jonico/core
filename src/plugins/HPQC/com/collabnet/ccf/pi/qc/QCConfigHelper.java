/**
 * 
 */
package com.collabnet.ccf.pi.qc;

import java.util.ArrayList;
import java.util.List;

import com.collabnet.ccf.core.config.Field;
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
	
	public static IRecordSet executeSQL(IConnection qcc, String sql){
		ICommand command = qcc.getCommand();
		command.setCommandText(sql);
		return command.execute();
	}
	
	public static List<Field> getSchemaFields(IConnection qcc) {

		// Get all the fields in the project represented
		// by qcc
		String sql = "SELECT * FROM SYSTEM_FIELD WHERE SF_TABLE_NAME='BUG'";

		IRecordSet rs = executeSQL(qcc, sql);

		int rc = rs.getRecordCount();
		List<Field> fields = new ArrayList<Field>();
		for(int cnt = 0 ; cnt < rc ; cnt++, rs.next())
		{
			String columnName = rs.getFieldValue(sfColumnName);
			String columnType = rs.getFieldValue(sfColumnType);
			String fieldDisplayName = rs.getFieldValue(sfUserLabel);
			String editStyle = rs.getFieldValue(sfEditStyle);
			Field field;

			if (editStyle == sfListComboValue ) {
				// Get the list values
				String rootId = rs.getFieldValue(sfRootId);
				
				// Obtain the list values
				String subSql = "SELECT * FROM ALL_LISTS WHERE AL_RATHER_ID = " + rootId;
				IRecordSet subRs = executeSQL(qcc, subSql);
				int rsRc = subRs.getRecordCount();

				if ( rsRc <= 0 ) {
					// Create field with empty value
					field = new Field(columnName, fieldDisplayName, columnType, editStyle, "", false);
				}
				else {
					List<String> values = new ArrayList<String>();
					for (int rsCnt = 0 ; rsCnt < rsRc ; subRs.next()) {
						String listValue = subRs.getFieldValue(alDescription);
						values.add(listValue);
					}
					// Create the field	
					field = new Field(columnName, fieldDisplayName, columnType, editStyle, values, false);
				}
			}
			else if (editStyle == sfUserComboValue ) {
				// get the user values
				String subSql = "SELECT * FROM USERS";
				IRecordSet subRs = executeSQL(qcc, subSql);
				int rsRc = subRs.getRecordCount();

				if ( rsRc <= 0 ) {
					// Create field with empty value
					field = new Field(columnName, fieldDisplayName, columnType, editStyle, "", false);
				}
				else {
					List<String> values = new ArrayList<String>();
					for (int rsCnt = 0 ; rsCnt < rsRc ; subRs.next()) {
						String userValue = subRs.getFieldValue(usUsername);
						values.add(userValue);
					}
					// Create the field	
					field = new Field(columnName, fieldDisplayName, columnType, editStyle, values, false);
				}
			}
			else {
				// Create field with empty value
				field = new Field(columnName, fieldDisplayName, columnType, editStyle, "", false);
			}
			
			fields.add(field);
		}

		return fields;
	}
}
