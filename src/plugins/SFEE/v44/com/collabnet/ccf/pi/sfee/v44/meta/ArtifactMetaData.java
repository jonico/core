package com.collabnet.ccf.pi.sfee.v44.meta;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class ArtifactMetaData {
	private static final Log log = LogFactory.getLog(ArtifactMetaData.class);
	public enum FIELD_INPUT_TYPE {
		TEXT,
		DATE,
		SINGLE_SELECT,
		/*USER, */
		MULTI_SELECT /*, 
		MULTISELECT_USER */
	}
	public enum FIELD_VALUE_TYPE {
		DATE,
		USER,
		INTEGER,
		STRING
	}
	public enum FIELD_TYPE {
		SYSTEM_DEFINED,
		CONFIGURABLE,
		USER_DEFINED /* Flex fields */
	}
	
	public enum SFEEFields {
		id("id", "ID", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING, true,""),
		actualHours("actualHours", "Actual hours", FIELD_TYPE.CONFIGURABLE,FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.INTEGER, false,""),
		assignedTo("assignedTo", "Assigned to",FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.USER, false,""),
		lastModifiedBy("lastModifiedBy", "Last modified by", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.USER, false,""),
		createdBy("createdBy", "Created by", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.USER, false,""),
		folderId("folderId", "Folder id",FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING, false,""),
		version("version", "Version", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING, false,""),
		title("title", "Title", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING, true,""),
		path("path", "Path", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING, false,""),
		category("category", "Category", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING, false,""),
		/* This field is not set by the user. But SFEE automatically sets it when the state
		 * changes to CLOSED
		 * */
		closeDate("closeDate", "Close date", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.DATE, FIELD_VALUE_TYPE.DATE, false,""),
		createdDate("createdDate", "Created date", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.DATE, FIELD_VALUE_TYPE.DATE, false,""),
		lastModifiedDate("lastModifiedDate", "Last modified date", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.DATE, FIELD_VALUE_TYPE.DATE, false,""),
		customer("customer", "Customer", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING, false,""),
		description("description", "Description", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT,FIELD_VALUE_TYPE.STRING, true,""),
		estimatedHours("estimatedHours", "Estimated hours", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.INTEGER, false,""),
		//flexFields(),
		group("group", "Group", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING, false,""),
		priority("priority", "Priority", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.INTEGER, false,""), 
		reportedReleaseId("reportedReleaseId", "Reported in release", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING, false,"reportedInRelease"),
		resolvedReleaseId("resolvedReleaseId", "Resolved in release", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING, false,"resolvedInRelease"),
		status("status", "Status", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING, true,""),
		statusClass("statusClass", "Status class", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING, false,""),
		commentText("Comment Text", "Comment Text", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING, false,"");
		
		private String fieldName;
		private String displayName;
		private FIELD_TYPE fieldType;
		private FIELD_INPUT_TYPE inputType;
		private FIELD_VALUE_TYPE valueType;
		private boolean required;
		private String alternateName;
		private SFEEFields(String fieldName,
				String displayName,
				FIELD_TYPE fieldType,
				FIELD_INPUT_TYPE inputType,
				FIELD_VALUE_TYPE valueType,
				boolean required, String alternateName){
			this.fieldName = fieldName;
			this.displayName = displayName;
			this.fieldType = fieldType;
			this.inputType = inputType;
			this.valueType = valueType;
			this.required = required;
			this.alternateName = alternateName;
		}
		public String getFieldName() {
			return fieldName;
		}
		public String getDisplayName(){
			return displayName;
		}
		public FIELD_TYPE getFieldType() {
			return fieldType;
		}
		public FIELD_INPUT_TYPE getInputType() {
			return inputType;
		}
		public FIELD_VALUE_TYPE getValueType() {
			return valueType;
		}
		public boolean isRequired() {
			return required;
		}
		public void setRequired(boolean required) {
			this.required = required;
		}
		public String getAlternateName() {
			return alternateName;
		}
		public void setAlternateName(String alternateName) {
			this.alternateName = alternateName;
		}
	}

	public static FieldValueTypeValue getFieldValueType(String fieldName) {
		SFEEFields field = null;
		try{
			field = SFEEFields.valueOf(fieldName);
		}
		catch(IllegalArgumentException e){
			log.warn("Field "+fieldName+" is not found in ArtifactMetaData");
		}
		if(fieldName.equals(SFEEFields.commentText.getFieldName())){
			field = SFEEFields.commentText;
		}
		if(field != null){
			ArtifactMetaData.FIELD_VALUE_TYPE valueType = field.getValueType();
			ArtifactMetaData.FIELD_INPUT_TYPE inputType = field.getInputType();
			if(inputType == ArtifactMetaData.FIELD_INPUT_TYPE.MULTI_SELECT){
				return FieldValueTypeValue.STRING;
			}
			else if(valueType == ArtifactMetaData.FIELD_VALUE_TYPE.USER){
				return FieldValueTypeValue.STRING;
			}
			else if(valueType == ArtifactMetaData.FIELD_VALUE_TYPE.DATE){
				return FieldValueTypeValue.DATE;
			}
			else if(valueType == ArtifactMetaData.FIELD_VALUE_TYPE.INTEGER){
				return FieldValueTypeValue.INTEGER;
			}
			else if(valueType == ArtifactMetaData.FIELD_VALUE_TYPE.STRING){
				return FieldValueTypeValue.STRING;
			}
			else if(inputType == ArtifactMetaData.FIELD_INPUT_TYPE.SINGLE_SELECT){
				return FieldValueTypeValue.STRING;
			}
		}
		return null;
	}

	public static FieldValueTypeValue getFieldValueType(String fieldName,
			TrackerFieldSoapDO field) {
		FieldValueTypeValue fieldValueType = getFieldValueType(fieldName);
		if(fieldValueType == null){
			String fieldType = field.getFieldType();
			String valueType = field.getValueType();
			fieldValueType = getGAFieldValueType(fieldType,valueType);
		}
		if(fieldValueType == null){
			if(fieldName.equals(ArtifactMetaData.SFEEFields.commentText.getFieldName())){
				return FieldValueTypeValue.STRING;
			}
		}
		return fieldValueType;
	}
	
	public static Object getFieldValue(String fieldName, Object value, FieldValueTypeValue fieldType){
		if(fieldType == FieldValueTypeValue.DATE || 
				fieldType == FieldValueTypeValue.DATETIME){
			if(value instanceof GregorianCalendar){
				return ((GregorianCalendar)value).getTime();
			}
			else if(value instanceof Date){
				return value;
			}
			if(value instanceof String){
				long dataValue = Long.parseLong((String) value)*1000;
				Date returnDate = new Date(dataValue);
				return returnDate;
			}
		}
		return value;
	}
	public static FieldValueTypeValue getGAFieldValueType(String fieldType, String valueType){
		if(fieldType.equals(TrackerFieldSoapDO.FIELD_TYPE_MULTISELECT)){
			return FieldValueTypeValue.STRING;
		}
		else if(valueType.equals(TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE) ||
				fieldType.equals(TrackerFieldSoapDO.FIELD_TYPE_DATE)){
			return FieldValueTypeValue.DATE;
		}
		else if(valueType.equals(TrackerFieldSoapDO.FIELD_VALUE_TYPE_INTEGER)){
			return FieldValueTypeValue.INTEGER;
		}
		else if(fieldType.equals(TrackerFieldSoapDO.FIELD_TYPE_MULTISELECT_USER)
				|| valueType.equals(TrackerFieldSoapDO.FIELD_VALUE_TYPE_USER)){
			return FieldValueTypeValue.STRING;
		}
		else if(valueType.equals(TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING)){
			return FieldValueTypeValue.STRING;
		}
		else if(fieldType.equals(TrackerFieldSoapDO.FIELD_TYPE_SINGLE_SELECT)){
			return FieldValueTypeValue.STRING;
		}
		else if(fieldType.equals(TrackerFieldSoapDO.FIELD_TYPE_TEXT)){
			return FieldValueTypeValue.STRING;
		}
		else{
			return FieldValueTypeValue.STRING;
		}
	}
	
	public static Object parseFieldValue(String fieldName, String value,
			TrackerFieldSoapDO[] trackerFields) {
		for(TrackerFieldSoapDO field:trackerFields){
			String name = field.getName();
			if(name.equals(fieldName)){
				//String fieldType = field.getFieldType();
				String valueType = field.getValueType();
				if(valueType.equals(TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE)){
					long time = Long.parseLong(value);
					Date dateTime = new Date(time);
					return dateTime;
				}
				else if(valueType.equals(TrackerFieldSoapDO.FIELD_VALUE_TYPE_INTEGER)){
					return Integer.parseInt(value);
				}
				else{
					return value;
				}
			}
		}
		return value;
	}
	
	public static boolean isUserDefined(String fieldName){
		SFEEFields field = null;
		try{
			field = SFEEFields.valueOf(Character.toLowerCase(fieldName.charAt(0))+fieldName.substring(1));
		}
		catch(IllegalArgumentException e){
			log.error("Field "+fieldName+" is not found in ArtifactMetaData");
		}
		if(field != null){
			return false;
		}
		else {
			return true;
		}
	}
	
	// FIXME This is very inperformant
	public static void addFlexField(String fieldName,
			ArtifactSoapDO artifactRow, Object value) {
		SoapFieldValues flexFields = artifactRow.getFlexFields();
		String[] fieldNames = null;
		Object[] fieldValues = null;
		String[] fieldTypes = null;
		if(flexFields != null){
			fieldNames = flexFields.getNames();
			fieldValues = flexFields.getValues();
			fieldTypes = flexFields.getTypes();
			for(int i=0; i < fieldNames.length; i++){
				if(fieldNames[i].equals(fieldName)){
					fieldValues[i] = value;
					return;
				}
			}
		}
		else {
			flexFields = new SoapFieldValues();
			fieldNames = flexFields.getNames();
			fieldValues = flexFields.getValues();
			fieldTypes = flexFields.getTypes();
		}
		if(fieldNames != null){
			String[] newFieldNames = new String[fieldNames.length+1];
			System.arraycopy(fieldNames, 0, newFieldNames, 0, fieldNames.length);
			newFieldNames[fieldNames.length] = fieldName;
			fieldNames = newFieldNames;
		}
		else {
			fieldNames = new String[]{fieldName};
		}
		if(fieldValues != null){
			Object[] newfieldValues = new Object[fieldValues.length+1];
			System.arraycopy(fieldValues, 0, newfieldValues, 0, fieldValues.length);
			newfieldValues[fieldValues.length] = value;
			fieldValues = newfieldValues;
		}
		else {
			fieldValues = new Object[]{value};
		}
		if(fieldTypes != null){
			String[] newfieldTypes = new String[fieldTypes.length+1];
			System.arraycopy(fieldTypes, 0, newfieldTypes, 0, fieldTypes.length);
			if(value instanceof Date){
				newfieldTypes[fieldTypes.length] = TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE;
			}
			else if(value instanceof String){
				//TODO Handle user data types
				newfieldTypes[fieldTypes.length] = TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING;
				//TrackerFieldSoapDO.FIELD_VALUE_TYPE_USER
			}
			fieldTypes = newfieldTypes;
		}
		else {
			String fieldType = null;
			if(value instanceof Date){
				fieldType = TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE;
			}
			else if(value instanceof String){
				//TODO Handle user data types
				fieldType = TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING;
				//TrackerFieldSoapDO.FIELD_VALUE_TYPE_USER
			}
			fieldTypes = new String[]{fieldType};
		}
		flexFields.setNames(fieldNames);
		flexFields.setValues(fieldValues);
		flexFields.setTypes(fieldTypes);
	}
}
