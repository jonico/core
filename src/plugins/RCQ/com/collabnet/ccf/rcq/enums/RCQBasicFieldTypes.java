package com.collabnet.ccf.rcq.enums;

// possible fieldType values from cQ online docs:
// http://publib.boulder.ibm.com/infocenter/cqhelp/v7r0m1/topic/com.ibm.rational.clearquest.apiref.doc/r_entity_getfieldtype.htm

public enum RCQBasicFieldTypes {
	SHORT_STRING		(1),	// Simple text field (255 character limit)
	MULTILINE_STRING	(2),	// Arbitrarily long text
	INT					(3),	// Integer
	DATE_TIME			(4),	// Timestamp information
	REFERENCE			(5),	// A pointer to a stateless record type.
	REFERENCE_LIST		(6),	// A list of references
	ATTACHMENT_LIST		(7),	// A list of attached files
	ID					(8),	// A string ID for records (Entity objects)
	STATE				(9),	// The current state of a state-based record (that is, a request entity).
	JOURNAL				(10),	// A list of rows in a subtable that belongs exclusively to this record (Entity)
	DBID				(11),	// An internal numeric ID
	STATETYPE			(12),	// State type of record (entity). State types are defined by schema packages and 
								// are assigned to states within your schema. For example, UCM uses state types to 
								// determine when hooks should run. For more information about state types, see 
								// Administering Rational ClearQuest or Developing Schemas topics in the online help.
	RECORDTYPE			(13);	// The name of the record type (EntityDef) of the current record (Entity). 
								// For example, "Defect" or "Customer".

	private int opValue;
	
	private RCQBasicFieldTypes(int i) {
		opValue = i;
	}
	
	public int getValue() {
		return opValue;
	}
}

