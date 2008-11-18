package com.collabnet.ccf.pi.sfee.v44.meta;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;

public class ArtifactMetaData {
	private static final Log log = LogFactory.getLog(ArtifactMetaData.class);

	private static final HashMap<String, GenericArtifactField.FieldValueTypeValue>
			fieldValueTypeGAFieldTypeMap = new HashMap<String, GenericArtifactField.FieldValueTypeValue>();
	private static final HashMap<GenericArtifactField.FieldValueTypeValue, String>
			fieldGAValueTypeFieldTypeMap = new HashMap<GenericArtifactField.FieldValueTypeValue, String>();
	static {
		fieldValueTypeGAFieldTypeMap.put(TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE,
				GenericArtifactField.FieldValueTypeValue.DATE);
		fieldValueTypeGAFieldTypeMap.put(TrackerFieldSoapDO.FIELD_VALUE_TYPE_INTEGER,
				GenericArtifactField.FieldValueTypeValue.INTEGER);
		fieldValueTypeGAFieldTypeMap.put(TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING,
				GenericArtifactField.FieldValueTypeValue.STRING);
		fieldValueTypeGAFieldTypeMap.put(TrackerFieldSoapDO.FIELD_VALUE_TYPE_USER,
				GenericArtifactField.FieldValueTypeValue.USER);
	}
	static {
		fieldGAValueTypeFieldTypeMap.put(GenericArtifactField.FieldValueTypeValue.DATE,
				TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE);
		fieldGAValueTypeFieldTypeMap.put(GenericArtifactField.FieldValueTypeValue.INTEGER,
				TrackerFieldSoapDO.FIELD_VALUE_TYPE_INTEGER);
		fieldGAValueTypeFieldTypeMap.put(GenericArtifactField.FieldValueTypeValue.STRING, 
				TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING);
		fieldGAValueTypeFieldTypeMap.put(GenericArtifactField.FieldValueTypeValue.USER,
				TrackerFieldSoapDO.FIELD_VALUE_TYPE_USER);
	}

	public enum FIELD_TYPE {
		SYSTEM_DEFINED,
		CONFIGURABLE,
		USER_DEFINED /* Flex fields */
	}
	
	public enum SFEEFields {
		id("id", "ID", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.STRING, true,""),
		actualHours("actualHours", "Actual hours", FIELD_TYPE.CONFIGURABLE,GenericArtifactField.FieldValueTypeValue.INTEGER, false,""),
		assignedTo("assignedTo", "Assigned to",FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.USER, false,""),
		lastModifiedBy("lastModifiedBy", "Last modified by", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.USER, false,""),
		createdBy("createdBy", "Created by", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.USER, false,""),
		folderId("folderId", "Folder id",FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.STRING, false,""),
		version("version", "Version", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.STRING, false,""),
		title("title", "Title", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.STRING, true,""),
		path("path", "Path", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.STRING, false,""),
		category("category", "Category", FIELD_TYPE.CONFIGURABLE, GenericArtifactField.FieldValueTypeValue.STRING, false,""),
		/* This field is not set by the user. But SFEE automatically sets it when the state
		 * changes to CLOSED
		 * */
		closeDate("closeDate", "Close date", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.DATE, false,""),
		createdDate("createdDate", "Created date", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.DATE, false,""),
		lastModifiedDate("lastModifiedDate", "Last modified date", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.DATE, false,""),
		customer("customer", "Customer", FIELD_TYPE.CONFIGURABLE, GenericArtifactField.FieldValueTypeValue.STRING, false,""),
		description("description", "Description", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.STRING, true,""),
		estimatedHours("estimatedHours", "Estimated hours", FIELD_TYPE.CONFIGURABLE, GenericArtifactField.FieldValueTypeValue.INTEGER, false,""),
		//flexFields(),
		group("group", "Group", FIELD_TYPE.CONFIGURABLE, GenericArtifactField.FieldValueTypeValue.STRING, false,""),
		priority("priority", "Priority", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.INTEGER, false,""), 
		reportedReleaseId("reportedReleaseId", "Reported in release", FIELD_TYPE.CONFIGURABLE, GenericArtifactField.FieldValueTypeValue.STRING, false,"reportedInRelease"),
		resolvedReleaseId("resolvedReleaseId", "Resolved in release", FIELD_TYPE.CONFIGURABLE, GenericArtifactField.FieldValueTypeValue.STRING, false,"resolvedInRelease"),
		status("status", "Status", FIELD_TYPE.CONFIGURABLE, GenericArtifactField.FieldValueTypeValue.STRING, true,""),
		statusClass("statusClass", "Status class", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.STRING, false,""),
		commentText("Comment Text", "Comment Text", FIELD_TYPE.SYSTEM_DEFINED, GenericArtifactField.FieldValueTypeValue.STRING, false,"");
		
		private String fieldName;
		private String displayName;
		private FIELD_TYPE fieldType;
		private GenericArtifactField.FieldValueTypeValue valueType;
		private boolean required;
		private String alternateName;
		private SFEEFields(String fieldName,
				String displayName,
				FIELD_TYPE fieldType,
				GenericArtifactField.FieldValueTypeValue valueType,
				boolean required, String alternateName){
			this.fieldName = fieldName;
			this.displayName = displayName;
			this.fieldType = fieldType;
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
		public GenericArtifactField.FieldValueTypeValue getValueType() {
			return valueType;
		}
		public boolean isRequired() {
			return required;
		}
		public String getAlternateName() {
			return alternateName;
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
			return field.getValueType();
		}
		return null;
	}

	public static FieldValueTypeValue getFieldValueTypeForFieldType(String fieldType) {
		FieldValueTypeValue fieldValueType = fieldValueTypeGAFieldTypeMap.get(fieldType);
		if(fieldValueType != null){
			return fieldValueType;
		}
		else {
			log.error("No type found for " + fieldType);
		}
		return null;
	}
	
	public static String getSFEEFieldValueTypeForGAFieldType(FieldValueTypeValue fieldType) {
		String fieldValueType = fieldGAValueTypeFieldTypeMap.get(fieldType);
		if(fieldValueType != null){
			return fieldValueType;
		}
		else {
			log.error("No SFEE type found for GA type " + fieldType);
		}
		return null;
	}
	
	public static void setDateFieldValue(String fieldName, Object value,
			String sourceSystemTimezone, GenericArtifactField field){
		Date dateValue = null;
		if(value instanceof GregorianCalendar){
			dateValue = ((GregorianCalendar)value).getTime();
		}
		else if(value instanceof Date){
			dateValue = (Date) value;
		}
		if(value instanceof String){
			long dataValue = Long.parseLong((String) value)*1000;
			Date returnDate = new Date(dataValue);
			dateValue = returnDate;
		}
		if(DateUtil.isAbsoluteDateInTimezone(dateValue, sourceSystemTimezone)){
			dateValue = DateUtil.convertToGMTAbsoluteDate(dateValue, sourceSystemTimezone);
			field.setFieldValue(dateValue);
			field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.DATE);
		}
		else {
			field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.DATETIME);
			field.setFieldValue(dateValue);
		}
	}
}
