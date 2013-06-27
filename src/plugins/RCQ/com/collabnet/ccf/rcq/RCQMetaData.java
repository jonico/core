package com.collabnet.ccf.rcq;

import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;

public class RCQMetaData {

	
	public static boolean isDateType( long n ) {
		return n == 4;
	}
	
	public static GenericArtifactField.FieldValueTypeValue getFieldType ( long n ) {
		/*
		
		Constant			#	Description
		_SHORT_STRING		1	Simple text field (255 character limit)
		_MULTILINE_STRING	2	Arbitrarily long text
		_INT				3	Integer
		_DATE_TIME			4	Timestamp information
		_REFERENCE			5	A pointer to a stateless record type.
		_REFERENCE_LIST		6	A list of references
		_ATTACHMENT_LIST	7	A list of attached files
		_ID					8	A string ID for records (Entity objects)
		_STATE				9	The current state of a state-based record (that is, a request entity).
		_JOURNAL			10	A list of rows in a subtable that belongs exclusively to this record (Entity)
		_DBID				11	An internal numeric ID
		_STATETYPE			12	State type of record (entity). State types are defined by schema packages and are assigned to states within your schema. For example, UCM uses state types to determine when hooks should run. For more information about state types, see Administering Rational ClearQuest or Developing Schemas topics in the online help.
		_RECORDTYPE			13	The name of the record type (EntityDef) of the current record (Entity). For example, "Defect" or "Customer".
		
		Possible values for CCF are:
		INTEGER, DOUBLE, DATETIME, DATE, STRING, HTMLSTRING, BASE64STRING, BOOLEAN, USER
		*/
		
		switch ( (int) n ) {
		case 1:
			return FieldValueTypeValue.STRING;
		case 2:
			return FieldValueTypeValue.STRING;
		case 3:
			return FieldValueTypeValue.INTEGER;
		case 4:
			return FieldValueTypeValue.DATETIME;
		case 5:
			return FieldValueTypeValue.USER;
		case 6:
			return FieldValueTypeValue.USER;
		case 7:
			return FieldValueTypeValue.USER;
		case 8:
			return FieldValueTypeValue.STRING;
		case 9:
			return FieldValueTypeValue.STRING;
		case 10:
			return FieldValueTypeValue.USER;
		case 11:
			return FieldValueTypeValue.DOUBLE;
		case 12:
			return FieldValueTypeValue.STRING;
		case 13:
			return FieldValueTypeValue.STRING;
		}
		return FieldValueTypeValue.STRING;
	}
}
