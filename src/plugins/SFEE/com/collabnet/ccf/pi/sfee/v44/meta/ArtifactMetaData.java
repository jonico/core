package com.collabnet.ccf.pi.sfee.v44.meta;

import java.util.HashMap;

import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;

public class ArtifactMetaData {
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
		id("id", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING),
		actualHours("actualHours",FIELD_TYPE.CONFIGURABLE,FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.INTEGER),
		assignedTo("assignedTo", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.USER),
		lastModifiedBy("lastModifiedBy", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.USER),
		createdBy("createdBy", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.USER),
		folderId("folderId", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING),
		version("version", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING),
		title("title", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING),
		path("path", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING),
		category("category", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING),
		/* This field is not set by the user. But SFEE automatically sets it when the state
		 * changes to CLOSED
		 * */
		closeDate("closeDate", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.DATE, FIELD_VALUE_TYPE.DATE),
		createdDate("createdDate", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.DATE, FIELD_VALUE_TYPE.DATE),
		lastModifiedDate("lastModifiedDate", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.DATE, FIELD_VALUE_TYPE.DATE),
		customer("customer", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING),
		description("description", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT,FIELD_VALUE_TYPE.STRING),
		estimatedHours("estimatedHours", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.INTEGER),
		//flexFields(),
		group("group", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING),
		priority("priority", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.INTEGER), 
		reportedReleaseId("reportedReleaseId", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING),
		resolvedReleaseId("resolvedReleaseId", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING),
		status("status", FIELD_TYPE.CONFIGURABLE, FIELD_INPUT_TYPE.SINGLE_SELECT, FIELD_VALUE_TYPE.STRING),
		statusClass("statusClass", FIELD_TYPE.SYSTEM_DEFINED, FIELD_INPUT_TYPE.TEXT, FIELD_VALUE_TYPE.STRING);
		
		private String fieldName;
		private FIELD_TYPE fieldType;
		private FIELD_INPUT_TYPE inputType;
		private FIELD_VALUE_TYPE valueType;
		private SFEEFields(String fieldName,
				FIELD_TYPE fieldType,
				FIELD_INPUT_TYPE inputType,
				FIELD_VALUE_TYPE valueType){
			this.fieldName = fieldName;
			this.fieldType = fieldType;
			this.inputType = inputType;
			this.valueType = valueType;
		}
		public String getFieldName() {
			return fieldName;
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
	}

	public static FieldValueTypeValue getFieldValueType(String fieldName) {
		SFEEFields field = null;
		try{
			field = SFEEFields.valueOf(Character.toLowerCase(fieldName.charAt(0))+fieldName.substring(1));
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		if(field != null){
			ArtifactMetaData.FIELD_VALUE_TYPE valueType = field.getValueType();
			ArtifactMetaData.FIELD_INPUT_TYPE inputType = field.getInputType();
			if(inputType == ArtifactMetaData.FIELD_INPUT_TYPE.MULTI_SELECT){
				return FieldValueTypeValue.MULTI_SELECT_LIST;
			}
			else if(valueType == ArtifactMetaData.FIELD_VALUE_TYPE.USER){
				return FieldValueTypeValue.USER;
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
				return FieldValueTypeValue.LIST;
			}
		}
		return null;
	}

	public static FieldValueTypeValue getFieldValueType(String fieldName,
			TrackerFieldSoapDO[] trackerFields) {
		FieldValueTypeValue fieldValueType = getFieldValueType(fieldName);
		if(fieldValueType == null){
			for(TrackerFieldSoapDO field:trackerFields){
				String name = field.getName();
				if(name.equals(fieldName)){
					String fieldType = field.getFieldType();
					String valueType = field.getValueType();
					fieldValueType = getGAFieldValueType(fieldType,valueType);
					break;
				}
			}
		}
		return fieldValueType;
	}
	public static FieldValueTypeValue getGAFieldValueType(String fieldType, String valueType){
		if(fieldType.equals(TrackerFieldSoapDO.FIELD_TYPE_MULTISELECT)){
			return FieldValueTypeValue.MULTI_SELECT_LIST;
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
			return FieldValueTypeValue.USER;
		}
		else if(valueType.equals(TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING)){
			return FieldValueTypeValue.STRING;
		}
		else if(fieldType.equals(TrackerFieldSoapDO.FIELD_TYPE_SINGLE_SELECT)){
			return FieldValueTypeValue.LIST;
		}
		else if(fieldType.equals(TrackerFieldSoapDO.FIELD_TYPE_TEXT)){
			return FieldValueTypeValue.STRING;
		}
		else{
			return FieldValueTypeValue.STRING;
		}
	}
}
