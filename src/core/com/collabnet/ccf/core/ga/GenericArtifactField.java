package com.collabnet.ccf.core.ga;

/**
 * A field has a name and a field type and contains a typed value for the
 * property of the artifact that is described by this field. If a field with the
 * same name and field type occurs more than once, this indicates a multi-value
 * property within the artifact. The content of the field element varies from
 * value type to value type, but it is always encoded as a string, so structured
 * values are currently not allowed.
 * 
 * @author jnicolai
 */
public class GenericArtifactField {
	/**
	 * Constant value for field type "mandatoryField"
	 */
	public static final String VALUE_FIELD_TYPE_MANDATORY_FIELD = "mandatoryField";

	/**
	 * Constant value for field type "flexField"
	 */
	public static final String VALUE_FIELD_TYPE_FLEX_FIELD = "flexField";

	/**
	 * Constant value for field type "integrationData"
	 */
	public static final String VALUE_FIELD_TYPE_INTEGRATION_DATA = "integrationData";

	/**
	 * This is the constant attribute values should set to if the value is not
	 * (yet) known or the whole functionality is not supported
	 */
	public static final String VALUE_UNKNOWN = "unknown";

	/**
	 * 
	 * Possible values for the field action, "append", "replace" and "delete"
	 * 
	 * @author jnicolai
	 * 
	 */
	public enum FieldActionValue {
		APPEND, REPLACE, DELETE, UNKNOWN
	};

	/**
	 * 
	 * Possible values for the field value type, "Integer", "Double",
	 * "DateTime", "Date", "String", "HTMLString", "Base64String", "Boolean" and
	 * "User"
	 * 
	 * @author jnicolai
	 */
	public enum FieldValueTypeValue {
		INTEGER, DOUBLE, DATETIME, DATE, STRING, HTMLSTRING, BASE64STRING, BOOLEAN, USER, UNKNOWN
	};

	/**
	 * This attribute contains the name of the field.
	 */
	private String fieldName = VALUE_UNKNOWN;

	/**
	 * This attribute contains the type of the field. This is not the type of
	 * the field's value but a mechanism to differentiate different artifact
	 * properties with the same name. Values might be "mandatoryField",
	 * "flexField", "integrationData" or just a plain number.
	 */
	private String fieldType = VALUE_UNKNOWN;

	/**
	 * This attribute determines whether the content of the value tag should be
	 * "append"ed, "replace"d, or "delete"d. Valid values are "append",
	 * "replace" and "delete". Note that most systems will only support the
	 * "replace"-action and that source systems usually do not know anything
	 * about the characteristics of the target system.
	 */
	private FieldActionValue fieldAction = FieldActionValue.UNKNOWN;

	/**
	 * This attribute may have the values "true" or "false" to state whether
	 * this property has been changed since the last update. This attribute can
	 * be used by the target system as a hint which fields to update.
	 */
	private boolean fieldValueHasChanged = true;

	/**
	 * This element contains the type of the value of the surrounding field
	 * element. If there are multiple occurrences of the field element with the
	 * same name and field type, their values may have different types.
	 * Currently supported types are Integer, Double, DateTime, Date, String,
	 * HTMLString, Base64String, Boolean and User.
	 */
	private FieldValueTypeValue fieldValueType = FieldValueTypeValue.UNKNOWN;
	
	/**
	 * The content of the field-element is the value of the property that is described by this field-element.
	 */
	private Object fieldValue=null;

	/**
	 * Creates a new generic artifact field with all attributes but field name
	 * and field type set to unknown This constructor should not be called by
	 * any user of the CCF framework directly.
	 * 
	 * @param fieldName
	 * @param fieldType
	 */
	protected GenericArtifactField(String fieldName, String fieldType) {
		this.setFieldName(fieldName);
		this.setFieldType(fieldType);
	}

	/**
	 * @param fieldValueHasChanged
	 *            the fieldValueHasChanged to set
	 */
	public void setFieldValueHasChanged(boolean fieldValueHasChanged) {
		this.fieldValueHasChanged = fieldValueHasChanged;
	}

	/**
	 * @return the fieldValueHasChanged
	 */
	public boolean getFieldValueHasChanged() {
		return fieldValueHasChanged;
	}

	/**
	 * @param fieldAction
	 *            the fieldAction to set
	 */
	public void setFieldAction(FieldActionValue fieldAction) {
		this.fieldAction = fieldAction;
	}

	/**
	 * @return the fieldAction
	 */
	public FieldActionValue getFieldAction() {
		return fieldAction;
	}

	/**
	 * @param fieldType
	 *            the fieldType to set
	 * 
	 * This method may only be used in the constructor, otherwise, the indexing
	 * of the fields will be destroyed
	 */
	private void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 * 
	 * This method may only be used in the constructor, otherwise, the indexing
	 * of the fields will be destroyed
	 */
	private void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldValueType the fieldValueType to set
	 */
	public void setFieldValueType(FieldValueTypeValue fieldValueType) {
		this.fieldValueType = fieldValueType;
	}

	/**
	 * @return the fieldValueType
	 */
	public FieldValueTypeValue getFieldValueType() {
		return fieldValueType;
	}

	/**
	 * @param fieldValue the fieldValue to set
	 */
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * @return the fieldValue
	 */
	public Object getFieldValue() {
		return fieldValue;
	}
	
}
