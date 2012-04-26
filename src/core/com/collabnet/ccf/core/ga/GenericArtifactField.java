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

package com.collabnet.ccf.core.ga;

import com.collabnet.ccf.core.CCFRuntimeException;

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
	 * Flex fields are user defined custom fields.
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
		INTEGER, DOUBLE, DATETIME, DATE, STRING, HTMLSTRING, BASE64STRING, BOOLEAN, USER
	};

	public void getStringRepresentationOfFieldValueTypeValue(
			GenericArtifactField.FieldValueTypeValue fieldValueTypeValue) {
		fieldValueTypeValue.toString();
	}

	/**
	 * This attribute contains the name of the field.
	 */
	private String fieldName = VALUE_UNKNOWN;

	/**
	 * This attribute contains the type of the field. This is not the type of
	 * the field's value but a mechanism to differentiate different artifact
	 * properties with the same name. Values might be "mandatoryField",
	 * "flexField", "integrationData" or just a plain number.
	 * Flex fields are user defined custom fields.
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
	private FieldValueTypeValue fieldValueType = FieldValueTypeValue.STRING;
	
	public static final int CARDINALITY_UNBOUNDED = -1;
	
	public static final String UNBOUNDED = "unbounded";
	
	public static final int CARDINALITY_UNKNOWN = -2;

	/**
	 * This optional attribute describes the minimal occurence of this field
	 * element. Typical values are "0", "1" and "unknown". For mandatory fields,
	 * the value is at least "1", for optional fields the value has to be set to
	 * "0". This attribute is typically only set if the root element attribute
	 * "includesFieldMetaData" is set to "true".
	 */
	private int minOccurs = 0;
	/**
	 * This optional attribute describes the maximal occurence of this field
	 * element. Typical values are "1", any number greater one, "unknown" and
	 * "unbounded". This attribute is typically only set if the root element
	 * attribute "includesFieldMetaData" is set to "true".
	 */
	private int maxOccurs = CARDINALITY_UNBOUNDED;
	/**
	 * This optional attribute indicates whether this property of the artifact
	 * supports a null value as field value. Allowed values are "true", "false"
	 * and "unknown". This attribute is typically only set if the root element
	 * attribute "includesFieldMetaData" is set to "true".
	 */
	private String nullValueSupported = VALUE_UNKNOWN;
	/**
	 * This optional attribute is used to have another option to uniquely
	 * identify the field with another name but the field name. This option
	 * might be used by systems where the actual field name is very technical
	 * and not directly displayed to the user or if the field name should be
	 * converted to an XML-element name to facilitate graphical mapings. This
	 * attribute is typically only set if the root element attribute
	 * "includesFieldMetaData" is set to "true".
	 */
	private String alternativeFieldName = VALUE_UNKNOWN;

	/**
	 * The content of the field-element is the value of the property that is
	 * described by this field-element.
	 */
	private Object fieldValue = null;

	/**
	 * Creates a new generic artifact field with all attributes but field name
	 * and field type set to unknown This constructor should not be called by
	 * any user of the CCF framework directly.
	 * 
	 * @param fieldName
	 * @param fieldType
	 */
	protected GenericArtifactField(String fieldName,
			String fieldType) {
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
	 * @param fieldValueType
	 *            the fieldValueType to set
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
	 * @param fieldValue
	 *            the fieldValue to set
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

	public int getMinOccurs() {
		return minOccurs;
	}
	
	public String getMinOccursValue() {
		if(minOccurs == CARDINALITY_UNKNOWN){
			return VALUE_UNKNOWN;
		}
		return Integer.toString(minOccurs);
	}
	
	public String getMaxOccursValue() {
		if(maxOccurs == CARDINALITY_UNKNOWN){
			return VALUE_UNKNOWN;
		}
		else if(maxOccurs == CARDINALITY_UNBOUNDED){
			return UNBOUNDED;
		}
		return Integer.toString(maxOccurs);
	}
	
	public void setMinOccursValue(String minOccursStr) {
		if(minOccursStr.equals(VALUE_UNKNOWN)){
			minOccurs = CARDINALITY_UNKNOWN;
		}
		else {
			try{
				minOccurs = Integer.parseInt(minOccursStr);
			}
			catch(NumberFormatException e){
				String cause = "Cardinality value minOccurs " + minOccursStr + " is not supported.";
				CCFRuntimeException exception = new CCFRuntimeException(cause, e);
				throw exception;
			}
		}
	}
	
	public void setMaxOccursValue(String maxOccursStr) {
		if(maxOccursStr.equals(VALUE_UNKNOWN)){
			maxOccurs = CARDINALITY_UNKNOWN;
		}
		else if(maxOccursStr.equals(UNBOUNDED)){
			maxOccurs = CARDINALITY_UNBOUNDED;
		}
		else {
			try{
				maxOccurs = Integer.parseInt(maxOccursStr);
			}
			catch(NumberFormatException e){
				String cause = "Cardinality value " + maxOccursStr + " is not supported.";
				CCFRuntimeException exception = new CCFRuntimeException(cause, e);
				throw exception;
			}
		}
	}

	public void setMinOccurs(int minOccurs) {
		this.minOccurs = minOccurs;
	}

	public int getMaxOccurs() {
		return maxOccurs;
	}

	public void setMaxOccurs(int maxOccurs) {
		this.maxOccurs = maxOccurs;
	}

	public String getNullValueSupported() {
		return nullValueSupported;
	}

	public void setNullValueSupported(String nullValueSupported) {
		this.nullValueSupported = nullValueSupported;
	}

	public String getAlternativeFieldName() {
		return alternativeFieldName;
	}

	public void setAlternativeFieldName(String alternativeFieldName) {
		this.alternativeFieldName = alternativeFieldName;
	}

}
