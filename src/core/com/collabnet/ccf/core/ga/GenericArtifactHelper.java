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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.XPath;
import org.dom4j.xpath.DefaultXPath;
import org.openadaptor.auxil.connector.iostream.EncodingAwareObject;

import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.IncludesFieldMetaDataValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.utils.DateUtil;

/**
 * 
 * This class helps to generate and parse the schema defined generic xml
 * artifact format
 * 
 * @author jnicolai
 * 
 */
public class GenericArtifactHelper {

	private static final String SCHEMA_LOCATION_ATTRIBUTE = "schemaLocation";
	public static final DateFormat df = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss.SSS Z");// DateFormat.getDateTimeInstance(
	// DateFormat.FULL, DateFormat.FULL, new Locale("en"));
	public static final String ARTIFACT_ROOT_ELEMENT_NAME = "artifact";
	public static final String CCF_ARTIFACT_NAMESPACE = "http://ccf.open.collab.net/GenericArtifactV1.0";
	public static final String CCF_NAMESPACE_PREFIX = "ccf";
	public static final String SCHEMA_NAMESPACE_PREFIX = "xsi";
	public static final String SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String CCF_SCHEMA_LOCATION = "http://ccf.open.collab.net/GenericArtifactV1.0 http://ccf.open.collab.net/files/documents/177/1972/genericartifactschema.xsd";
	private static Map<String, String> ccfNamespaceMap = Collections
			.singletonMap(CCF_NAMESPACE_PREFIX, CCF_ARTIFACT_NAMESPACE);

	public static final String ARTIFACT_ACTION = "artifactAction";
	public static final String SOURCE_ARTIFACT_LAST_MODIFICATION_DATE = "sourceArtifactLastModifiedDate";
	public static final String TARGET_ARTIFACT_LAST_MODIFICATION_DATE = "targetArtifactLastModifiedDate";
	public static final String TRANSACTION_ID = "transactionId";
	public static final String ERROR_CODE = "errorCode";
	public static final String INCLUDES_FIELD_META_DATA = "includesFieldMetaData";
	public static final String ARTIFACT_MODE = "artifactMode";
	public static final String ARTIFACT_TYPE = "artifactType";
	public static final String SOURCE_ARTIFACT_VERSION = "sourceArtifactVersion";
	public static final String TARGET_ARTIFACT_VERSION = "targetArtifactVersion";
	public static final String CONFLICT_RESOLUTION_PRIORITY = "conflictResolutionPriority";
	public static final String DEP_CHILD_SOURCE_ARTIFACT_ID = "depChildSourceArtifactId";
	public static final String DEP_CHILD_SOURCE_REPOSITORY_ID = "depChildSourceRepositoryId";
	public static final String DEP_CHILD_SOURCE_REPOSITORY_KIND = "depChildSourceRepositoryKind";
	public static final String DEP_CHILD_TARGET_ARTIFACT_ID = "depChildTargetArtifactId";;
	public static final String DEP_CHILD_TARGET_REPOSITORY_ID = "depChildTargetRepositoryId";;
	public static final String DEP_CHILD_TARGET_REPOSITORY_KIND = "depChildTargetRepositoryKind";;
	public static final String DEP_PARENT_SOURCE_ARTIFACT_ID = "depParentSourceArtifactId";
	public static final String DEP_PARENT_SOURCE_REPOSITORY_ID = "depParentSourceRepositoryId";
	public static final String DEP_PARENT_SOURCE_REPOSITORY_KIND = "depParentSourceRepositoryKind";
	public static final String DEP_PARENT_TARGET_ARTIFACT_ID = "depParentTargetArtifactId";;
	public static final String DEP_PARENT_TARGET_REPOSITORY_ID = "depParentTargetRepositoryId";;
	public static final String DEP_PARENT_TARGET_REPOSITORY_KIND = "depParentTargetRepositoryKind";;
	public static final String SOURCE_ARTIFACT_ID = "sourceArtifactId";
	public static final String SOURCE_REPOSITORY_ID = "sourceRepositoryId";
	public static final String SOURCE_REPOSITORY_KIND = "sourceRepositoryKind";
	public static final String SOURCE_SYSTEM_ID = "sourceSystemId";
	public static final String SOURCE_SYSTEM_KIND = "sourceSystemKind";
	public static final String TARGET_ARTIFACT_ID = "targetArtifactId";
	public static final String TARGET_REPOSITORY_ID = "targetRepositoryId";
	public static final String TARGET_REPOSITORY_KIND = "targetRepositoryKind";
	public static final String TARGET_SYSTEM_ID = "targetSystemId";
	public static final String TARGET_SYSTEM_KIND = "targetSystemKind";
	public static final String SOURCE_SYSTEM_TIMEZONE = "sourceSystemTimezone";
	public static final String TARGET_SYSTEM_TIMEZONE = "targetSystemTimezone";
	// public static final String SOURCE_SYSTEM_ENCODING =
	// "sourceSystemEncoding";
	// public static final String TARGET_SYSTEM_ENCODING =
	// "targetSystemEncoding";

	public static final String ARTIFACT_VERSION_FORCE_RESYNC = "-1";

	public static final String ARTIFACT_ACTION_CREATE = "create";
	public static final String ARTIFACT_ACTION_DELETE = "delete";
	public static final String ARTIFACT_ACTION_IGNORE = "ignore";
	public static final String ARTIFACT_ACTION_UPDATE = "update";
	public static final String ARTIFACT_ACTION_RESYNC = "resync";
	public static final String ARTIFACT_ACTION_UNKNOWN = "unknown";

	public static final String ARTIFACT_MODE_COMPLETE = "complete";
	public static final String ARTIFACT_MODE_CHANGED_FIELDS_ONLY = "changedFieldsOnly";
	public static final String ARTIFACT_MODE_UNKNOWN = "unknown";

	public static final String INCLUDES_FIELD_META_DATA_TRUE = "true";
	public static final String INCLUDES_FIELD_META_DATA_FALSE = "false";
	// public static final String INCLUDES_FIELD_META_DATA_UNKNOWN = "unknown";

	public static final String ARTIFACT_TYPE_DEPENDENCY = "dependency";
	public static final String ARTIFACT_TYPE_PLAIN_ARTIFACT = "plainArtifact";
	public static final String ARTIFACT_TYPE_UNKNOWN = "unknown";

	public static final String ARTIFACT_TYPE_ATTACHMENT = "attachment";

	public static final String ARTIFACT_FIELD_ELEMENT_NAME = "field";
	private static final XPath fieldSelector = new DefaultXPath(
			CCF_NAMESPACE_PREFIX + ":" + ARTIFACT_FIELD_ELEMENT_NAME);

	public static final String FIELD_ACTION = "fieldAction";

	public static final String FIELD_ACTION_APPEND = "append";
	public static final String FIELD_ACTION_DELETE = "delete";
	public static final String FIELD_ACTION_REPLACE = "replace";
	public static final String FIELD_ACTION_UNKNOWN = "unknown";

	public static final String FIELD_NAME = "fieldName";
	public static final String FIELD_TYPE = "fieldType";
	public static final String FIELD_VALUE_IS_NULL = "fieldValueIsNull";
	public static final String FIELD_VALUE_TYPE = "fieldValueType";
	public static final String FIELD_VALUE_TYPE_BASE64STRING = "Base64String";
	public static final String FIELD_VALUE_TYPE_BOOLEAN = "Boolean";
	public static final String FIELD_VALUE_TYPE_DATE = "Date";
	public static final String FIELD_VALUE_TYPE_DATETIME = "DateTime";
	public static final String FIELD_VALUE_TYPE_DOUBLE = "Double";
	public static final String FIELD_VALUE_TYPE_HTML_STRING = "HTMLString";
	public static final String FIELD_VALUE_TYPE_INTEGER = "Integer";
	public static final String FIELD_VALUE_TYPE_STRING = "String";
	private static final String FIELD_VALUE_TYPE_USER = "User";
	// private static final String FIELD_VALUE_TYPE_LIST = "List";
	// private static final String FIELD_VALUE_TYPE_MULTI_SELECT_LIST =
	// "Multi_Select_String";
	// private static final String FIELD_VALUE_TYPE_UNKNOWN = "Unknown";
	public static final String FIELD_VALUE_IS_NULL_TRUE = "true";
	public static final String FIELD_VALUE_IS_NULL_FALSE = "false";
	public static final String FIELD_VALUE_HAS_CHANGED = "fieldValueHasChanged";
	public static final String FIELD_VALUE_HAS_CHANGED_TRUE = "true";
	public static final String FIELD_VALUE_HAS_CHANGED_FALSE = "false";

	public static final String MIN_OCCURS = "minOccurs";
	public static final String MAX_OCCURS = "maxOccurs";
	public static final String NULL_VALUE_SUPPORTED = "nullValueSupported";
	public static final String ALTERNATIVE_FIELD_NAME = "alternativeFieldName";

	// translation tables
	private static HashMap<String, GenericArtifact.ArtifactModeValue> artifactModeHashMap = new HashMap<String, GenericArtifact.ArtifactModeValue>(
			2);
	private static HashMap<String, GenericArtifact.ArtifactActionValue> artifactActionHashMap = new HashMap<String, GenericArtifact.ArtifactActionValue>(
			4);
	private static HashMap<String, GenericArtifact.ArtifactTypeValue> artifactTypeHashMap = new HashMap<String, GenericArtifact.ArtifactTypeValue>(
			4);
	private static HashMap<String, GenericArtifact.IncludesFieldMetaDataValue> includesFieldMetaDataHashMap = new HashMap<String, GenericArtifact.IncludesFieldMetaDataValue>(
			3);

	private static HashMap<String, GenericArtifactField.FieldActionValue> fieldActionHashMap = new HashMap<String, GenericArtifactField.FieldActionValue>(
			3);
	private static HashMap<String, GenericArtifactField.FieldValueTypeValue> fieldValueTypeHashMap = new HashMap<String, GenericArtifactField.FieldValueTypeValue>(
			9);
	private static HashMap<String, Boolean> fieldValueIsNullHashMap = new HashMap<String, Boolean>(
			2);
	private static HashMap<String, Boolean> fieldValueHasChangedHashMap = new HashMap<String, Boolean>(
			2);

	// populate translation tables
	static {
		artifactModeHashMap.put(ARTIFACT_MODE_CHANGED_FIELDS_ONLY,
				GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY);
		artifactModeHashMap.put(ARTIFACT_MODE_COMPLETE,
				GenericArtifact.ArtifactModeValue.COMPLETE);
		artifactModeHashMap.put(ARTIFACT_MODE_UNKNOWN,
				GenericArtifact.ArtifactModeValue.UNKNOWN);

		artifactActionHashMap.put(ARTIFACT_ACTION_CREATE,
				GenericArtifact.ArtifactActionValue.CREATE);
		artifactActionHashMap.put(ARTIFACT_ACTION_DELETE,
				GenericArtifact.ArtifactActionValue.DELETE);
		artifactActionHashMap.put(ARTIFACT_ACTION_IGNORE,
				GenericArtifact.ArtifactActionValue.IGNORE);
		artifactActionHashMap.put(ARTIFACT_ACTION_UPDATE,
				GenericArtifact.ArtifactActionValue.UPDATE);
		artifactActionHashMap.put(ARTIFACT_ACTION_RESYNC,
				GenericArtifact.ArtifactActionValue.RESYNC);
		artifactActionHashMap.put(ARTIFACT_ACTION_UNKNOWN,
				GenericArtifact.ArtifactActionValue.UNKNOWN);

		artifactTypeHashMap.put(ARTIFACT_TYPE_ATTACHMENT,
				GenericArtifact.ArtifactTypeValue.ATTACHMENT);
		artifactTypeHashMap.put(ARTIFACT_TYPE_DEPENDENCY,
				GenericArtifact.ArtifactTypeValue.DEPENDENCY);
		artifactTypeHashMap.put(ARTIFACT_TYPE_PLAIN_ARTIFACT,
				GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
		artifactTypeHashMap.put(ARTIFACT_TYPE_UNKNOWN,
				GenericArtifact.ArtifactTypeValue.UNKNOWN);

		includesFieldMetaDataHashMap.put(INCLUDES_FIELD_META_DATA_TRUE,
				GenericArtifact.IncludesFieldMetaDataValue.TRUE);
		includesFieldMetaDataHashMap.put(INCLUDES_FIELD_META_DATA_FALSE,
				GenericArtifact.IncludesFieldMetaDataValue.FALSE);
		// includesFieldMetaDataHashMap.put(INCLUDES_FIELD_META_DATA_UNKNOWN,
		// GenericArtifact.IncludesFieldMetaDataValue.UNKNOWN);

		fieldActionHashMap.put(FIELD_ACTION_APPEND,
				GenericArtifactField.FieldActionValue.APPEND);
		fieldActionHashMap.put(FIELD_ACTION_DELETE,
				GenericArtifactField.FieldActionValue.DELETE);
		fieldActionHashMap.put(FIELD_ACTION_REPLACE,
				GenericArtifactField.FieldActionValue.REPLACE);
		fieldActionHashMap.put(FIELD_ACTION_UNKNOWN,
				GenericArtifactField.FieldActionValue.UNKNOWN);

		fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_BASE64STRING,
				GenericArtifactField.FieldValueTypeValue.BASE64STRING);
		fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_BOOLEAN,
				GenericArtifactField.FieldValueTypeValue.BOOLEAN);
		fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_DATE,
				GenericArtifactField.FieldValueTypeValue.DATE);
		fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_DATETIME,
				GenericArtifactField.FieldValueTypeValue.DATETIME);
		fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_DOUBLE,
				GenericArtifactField.FieldValueTypeValue.DOUBLE);
		fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_HTML_STRING,
				GenericArtifactField.FieldValueTypeValue.HTMLSTRING);
		fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_INTEGER,
				GenericArtifactField.FieldValueTypeValue.INTEGER);
		fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_STRING,
				GenericArtifactField.FieldValueTypeValue.STRING);
		fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_USER,
				GenericArtifactField.FieldValueTypeValue.USER);
		// fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_LIST,
		// GenericArtifactField.FieldValueTypeValue.LIST);
		// fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_MULTI_SELECT_LIST,
		// GenericArtifactField.FieldValueTypeValue.MULTI_SELECT_LIST);
		// fieldValueTypeHashMap.put(FIELD_VALUE_TYPE_UNKNOWN,
		// GenericArtifactField.FieldValueTypeValue.UNKNOWN);

		fieldValueIsNullHashMap.put(FIELD_VALUE_IS_NULL_TRUE, Boolean.TRUE);
		fieldValueIsNullHashMap.put(FIELD_VALUE_IS_NULL_FALSE, Boolean.FALSE);

		fieldValueHasChangedHashMap.put(FIELD_VALUE_HAS_CHANGED_TRUE,
				Boolean.TRUE);
		fieldValueHasChangedHashMap.put(FIELD_VALUE_HAS_CHANGED_FALSE,
				Boolean.FALSE);

		// set CCF namespace in order to select nodes properly
		fieldSelector.setNamespaceURIs(ccfNamespaceMap);

	}

	/**
	 * Parses an XML document that complies to the generic artifact schema and
	 * represents it as a Java object
	 * 
	 * @param document
	 *            XML representation of the generic artifact
	 * @return Java object representing the artifact
	 * @throws GenericArtifactParsingException
	 *             exception is thrown if XML document does not comply to the
	 *             schema
	 */
	public static GenericArtifact createGenericArtifactJavaObject(
			Document document) throws GenericArtifactParsingException {
		GenericArtifact genericArtifact = new GenericArtifact();
		// TODO Parse the XML document and populate the Java objects
		// fetch the artifact-root-element
		Element root = getRootElement(document);
		// get and set attributes

		ArtifactActionValue artifactAction = translateAttributeValue(root,
				ARTIFACT_ACTION, artifactActionHashMap);
		genericArtifact.setArtifactAction(artifactAction);

		genericArtifact.setSourceArtifactLastModifiedDate(getAttributeValue(
				root, SOURCE_ARTIFACT_LAST_MODIFICATION_DATE));
		genericArtifact.setTargetArtifactLastModifiedDate(getAttributeValue(
				root, TARGET_ARTIFACT_LAST_MODIFICATION_DATE));
		// genericArtifact.setLastReadTransactionId(getAttributeValue(root,
		// ARTIFACT_LAST_READ_TRANSACTION_ID));
		genericArtifact.setErrorCode(getAttributeValue(root, ERROR_CODE));

		ArtifactModeValue artifactMode = translateAttributeValue(root,
				ARTIFACT_MODE, artifactModeHashMap);
		genericArtifact.setArtifactMode(artifactMode);

		ArtifactTypeValue artifactType = translateAttributeValue(root,
				ARTIFACT_TYPE, artifactTypeHashMap);
		genericArtifact.setArtifactType(artifactType);

		IncludesFieldMetaDataValue includesFieldMetaData = translateAttributeValue(
				root, INCLUDES_FIELD_META_DATA, includesFieldMetaDataHashMap);
		genericArtifact.setIncludesFieldMetaData(includesFieldMetaData);

		if (artifactType == GenericArtifact.ArtifactTypeValue.ATTACHMENT) {
			genericArtifact.setArtifactValue(getValue(root));
		}

		genericArtifact.setSourceArtifactVersion(getAttributeValue(root,
				SOURCE_ARTIFACT_VERSION));
		genericArtifact.setTargetArtifactVersion(getAttributeValue(root,
				TARGET_ARTIFACT_VERSION));
		genericArtifact.setConflictResolutionPriority(getAttributeValue(root,
				CONFLICT_RESOLUTION_PRIORITY));
		genericArtifact
				.setTransactionId(getAttributeValue(root, TRANSACTION_ID));
		// only read optional attributes if necessary
		if (artifactType == ArtifactTypeValue.DEPENDENCY
				|| artifactType == ArtifactTypeValue.ATTACHMENT) {
			genericArtifact.setDepParentSourceArtifactId(getAttributeValue(
					root, DEP_PARENT_SOURCE_ARTIFACT_ID));
			genericArtifact.setDepParentSourceRepositoryId(getAttributeValue(
					root, DEP_PARENT_SOURCE_REPOSITORY_ID));
			genericArtifact.setDepParentSourceRepositoryKind(getAttributeValue(
					root, DEP_PARENT_SOURCE_REPOSITORY_KIND));
			genericArtifact.setDepParentTargetArtifactId(getAttributeValue(
					root, DEP_PARENT_TARGET_ARTIFACT_ID));
			genericArtifact.setDepParentTargetRepositoryId(getAttributeValue(
					root, DEP_PARENT_TARGET_REPOSITORY_ID));
			genericArtifact.setDepParentTargetRepositoryKind(getAttributeValue(
					root, DEP_PARENT_TARGET_REPOSITORY_KIND));
		}

		// dependencies have even more optional attributes
		if (artifactType == ArtifactTypeValue.DEPENDENCY) {
			genericArtifact.setDepChildSourceArtifactId(getAttributeValue(root,
					DEP_CHILD_SOURCE_ARTIFACT_ID));
			genericArtifact.setDepChildSourceRepositoryId(getAttributeValue(
					root, DEP_CHILD_SOURCE_REPOSITORY_ID));
			genericArtifact.setDepChildSourceRepositoryKind(getAttributeValue(
					root, DEP_CHILD_SOURCE_REPOSITORY_KIND));
			genericArtifact.setDepChildTargetArtifactId(getAttributeValue(root,
					DEP_CHILD_TARGET_ARTIFACT_ID));
			genericArtifact.setDepChildTargetRepositoryId(getAttributeValue(
					root, DEP_CHILD_TARGET_REPOSITORY_ID));
			genericArtifact.setDepChildTargetRepositoryKind(getAttributeValue(
					root, DEP_CHILD_TARGET_REPOSITORY_KIND));
		}

		genericArtifact.setSourceArtifactId(getAttributeValue(root,
				SOURCE_ARTIFACT_ID));
		genericArtifact.setSourceRepositoryId(getAttributeValue(root,
				SOURCE_REPOSITORY_ID));
		genericArtifact.setSourceRepositoryKind(getAttributeValue(root,
				SOURCE_REPOSITORY_KIND));
		genericArtifact.setSourceSystemId(getAttributeValue(root,
				SOURCE_SYSTEM_ID));
		genericArtifact.setSourceSystemKind(getAttributeValue(root,
				SOURCE_SYSTEM_KIND));
		genericArtifact.setTargetArtifactId(getAttributeValue(root,
				TARGET_ARTIFACT_ID));
		genericArtifact.setTargetRepositoryId(getAttributeValue(root,
				TARGET_REPOSITORY_ID));
		genericArtifact.setTargetRepositoryKind(getAttributeValue(root,
				TARGET_REPOSITORY_KIND));
		genericArtifact.setTargetSystemId(getAttributeValue(root,
				TARGET_SYSTEM_ID));
		genericArtifact.setTargetSystemKind(getAttributeValue(root,
				TARGET_SYSTEM_KIND));
		genericArtifact.setSourceSystemTimezone(getAttributeValue(root,
				SOURCE_SYSTEM_TIMEZONE));
		genericArtifact.setTargetSystemTimezone(getAttributeValue(root,
				TARGET_SYSTEM_TIMEZONE));
		// genericArtifact.setSourceSystemEncoding(getAttributeValue(root,
		// SOURCE_SYSTEM_ENCODING));
		// genericArtifact.setTargetSystemEncoding(getAttributeValue(root,
		// TARGET_SYSTEM_ENCODING));

		// now add fields
		List<Element> fields = getAllFieldElements(root);
		for (Element field : fields) {
			GenericArtifactField.FieldActionValue fieldAction = translateAttributeValue(
					field, FIELD_ACTION, fieldActionHashMap);
			GenericArtifactField.FieldValueTypeValue fieldValueType = translateAttributeValue(
					field, FIELD_VALUE_TYPE, fieldValueTypeHashMap);
			String fieldName = getAttributeValue(field, FIELD_NAME);
			String fieldType = getAttributeValue(field, FIELD_TYPE);
			Boolean fieldValueIsNull = translateAttributeValue(field,
					FIELD_VALUE_IS_NULL, fieldValueIsNullHashMap);
			Boolean fieldValueHasChanged = translateAttributeValue(field,
					FIELD_VALUE_HAS_CHANGED, fieldValueHasChangedHashMap);
			String fieldValue = getValue(field);

			// we cannot change these two attributes later because this would
			// influence the indexing data structures for fast lookup
			GenericArtifactField genericArtifactField = genericArtifact
					.addNewField(fieldName, fieldType);
			genericArtifactField.setFieldAction(fieldAction);
			genericArtifactField.setFieldValueType(fieldValueType);
			genericArtifactField.setFieldValueHasChanged(fieldValueHasChanged);

			if (includesFieldMetaData
					.equals(GenericArtifact.IncludesFieldMetaDataValue.TRUE)) {
				String minOccurs = getAttributeValue(field, MIN_OCCURS);
				String maxOccurs = getAttributeValue(field, MAX_OCCURS);
				String nullValueSupported = getAttributeValue(field,
						NULL_VALUE_SUPPORTED);
				String alternativeFieldName = getAttributeValue(field,
						ALTERNATIVE_FIELD_NAME);

				genericArtifactField.setMinOccursValue(minOccurs);
				genericArtifactField.setMaxOccursValue(maxOccurs);
				genericArtifactField.setNullValueSupported(nullValueSupported);
				genericArtifactField
						.setAlternativeFieldName(alternativeFieldName);
			}

			try {
				convertFieldValue(genericArtifactField, fieldValueIsNull,
						fieldValueType, fieldValue);
			} catch (ParseException e) {
				throw new GenericArtifactParsingException("Value " + fieldValue
						+ " for field-element with name " + fieldName
						+ " and field type " + fieldType
						+ " was not convertible to an instance of value type "
						+ fieldValueType + " because: " + e.getMessage());
			}
		}

		// finally set reference to source XML document
		genericArtifact.setSourceDocument(document);
		return genericArtifact;
	}

	/**
	 * Convert the string value
	 * 
	 * @param genericArtifactField
	 *            field for which to set the new value
	 * @param fieldValueIsNull
	 *            if true, the field's value should be null
	 * @param fieldValueType
	 *            type of the field's value
	 * @param value
	 *            String encoded value out of XML element
	 * @throws ParseException
	 */
	private static void convertFieldValue(
			GenericArtifactField genericArtifactField,
			Boolean fieldValueIsNull, FieldValueTypeValue fieldValueType,
			String value) throws ParseException {
		// TODO Think carefully about all type conversions
		if (value.length() == 0 && fieldValueIsNull) {
			genericArtifactField.setFieldValue(null);
		} else
			switch (fieldValueType) {
			case BASE64STRING: {
				// TODO Better conversion?
				genericArtifactField.setFieldValue(value);
				break;
			}
			case BOOLEAN: {
				genericArtifactField.setFieldValue(Boolean.valueOf(value));
				break;
			}
			case DATE: {
				GregorianCalendar cal = new GregorianCalendar();
				synchronized (df) {
					cal.setTime(df.parse(value));
				}
				genericArtifactField.setFieldValue(cal);
				break;
			}
			case DATETIME: {
				synchronized (df) {
					genericArtifactField.setFieldValue(df.parse(value));
				}
				break;
			}
			case DOUBLE: {
				genericArtifactField.setFieldValue(new Double(value));
				break;
			}
			case HTMLSTRING: {
				// TODO Better conversion?
				genericArtifactField.setFieldValue(value);
				break;
			}
			case INTEGER: {
				// TODO Better conversion?
				genericArtifactField.setFieldValue(value);
				break;
			}
			case STRING: {
				genericArtifactField.setFieldValue(value);
				break;
			}
			case USER: {
				genericArtifactField.setFieldValue(value);
				break;
			}
				// case LIST: {
				// genericArtifactField.setFieldValue(value);
				// break;
				// }
				// case MULTI_SELECT_LIST: {
				// genericArtifactField.setFieldValue(value);
				// break;
				// }
			}
	}

	/**
	 * Extracts all field from the
	 * 
	 * @param root
	 *            generic artifact root-element
	 * @return a list with all field-elements of the root-element
	 */
	@SuppressWarnings("unchecked")
	private static List<Element> getAllFieldElements(Element root) {
		// our XPath-Expression matches only elements, so this conversion is
		// type-safe
		List<Element> fieldElements = fieldSelector.selectNodes(root);
		if (fieldElements == null)
			return new ArrayList<Element>();
		else
			return fieldElements;
	}

	/**
	 * Retrieves the value of the specified attribute of the supplied XML
	 * element and translate it from its String representation to type T by
	 * using the lookup table
	 * 
	 * @param <T>
	 *            type the mapped values have
	 * @param element
	 *            XML element
	 * @param attributeName
	 *            name of the attribute in question
	 * @param translationTable
	 *            lookup table from String to type T
	 * @return mapped attribute value
	 * @throws GenericArtifactParsingException
	 *             thrown if attribute is not present or value not in lookup
	 *             table
	 */
	private static <T> T translateAttributeValue(Element element,
			String attributeName, HashMap<String, T> translationTable)
			throws GenericArtifactParsingException {
		String attributeValueString = getAttributeValue(element, attributeName);
		T translatedAttributeValue = translationTable.get(attributeValueString);
		if (translatedAttributeValue == null)
			throw new GenericArtifactParsingException("Non-valid value "
					+ attributeValueString + " for attribute " + attributeName
					+ " of element " + element.getName());
		else
			return translatedAttributeValue;
	}

	/**
	 * Extracts the value of the supplied attribute
	 * 
	 * @param element
	 *            element with attribute in question
	 * @param attributeName
	 *            name of the attribute in question
	 * @return value of the attribute in question
	 * @throws GenericArtifactParsingException
	 *             exception s thrown is attribute is missing
	 */
	private static String getAttributeValue(Element element,
			String attributeName) throws GenericArtifactParsingException {
		// TODO Cash constructed XPath objects?
		// XPath xpath = new DefaultXPath("@" + CCF_NAMESPACE_PREFIX + ":" +
		// attributeName);
		XPath xpath = new DefaultXPath("@" + attributeName);
		xpath.setNamespaceURIs(ccfNamespaceMap);
		Node attributeNode = xpath.selectSingleNode(element);
		if (attributeNode == null)
			throw new GenericArtifactParsingException("Missing attribute: "
					+ attributeName + " in element " + element.getName());
		else
			return attributeNode.getText();
	}

	/**
	 * Extracts the artifact-root-element out of a Dom4J XML document
	 * 
	 * @param document
	 *            XML document in question
	 * @return generic artifact root-element
	 * @throws GenericArtifactParsingException
	 *             thrown if document is not compliant to the generic artifact
	 *             schema
	 */
	private static Element getRootElement(Document document)
			throws GenericArtifactParsingException {
		Element rootElement = document.getRootElement();
		if (!ARTIFACT_ROOT_ELEMENT_NAME.equals(rootElement.getName()))
			throw new GenericArtifactParsingException(
					"Root-element of XML document is not named "
							+ ARTIFACT_ROOT_ELEMENT_NAME + "but "
							+ rootElement.getName());
		if (!CCF_ARTIFACT_NAMESPACE.equals(rootElement.getNamespaceURI()))
			throw new GenericArtifactParsingException(
					"Namespace-URI of root-element of XML document is not named "
							+ CCF_ARTIFACT_NAMESPACE + "but "
							+ rootElement.getNamespaceURI());
		return rootElement;
	}

	/**
	 * Creates a generic artifact XML representation out of the Java object
	 * 
	 * @param genericArtifact
	 *            Java object that will be represented as XML document
	 * @return XML representation of generic artifact
	 */
	public static Document createGenericArtifactXMLDocument(
			GenericArtifact genericArtifact)
			throws GenericArtifactParsingException {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(EncodingAwareObject.UTF_8);
		// Create XML elements with attributes
		Element root = addRootElement(document, ARTIFACT_ROOT_ELEMENT_NAME,
				CCF_ARTIFACT_NAMESPACE);

		switch (genericArtifact.getArtifactAction()) {
		case CREATE: {
			addAttribute(root, ARTIFACT_ACTION, ARTIFACT_ACTION_CREATE);
			break;
		}
		case DELETE: {
			addAttribute(root, ARTIFACT_ACTION, ARTIFACT_ACTION_DELETE);
			break;
		}
		case IGNORE: {
			addAttribute(root, ARTIFACT_ACTION, ARTIFACT_ACTION_IGNORE);
			break;
		}
		case UPDATE: {
			addAttribute(root, ARTIFACT_ACTION, ARTIFACT_ACTION_UPDATE);
			break;
		}
		case RESYNC: {
			addAttribute(root, ARTIFACT_ACTION, ARTIFACT_ACTION_RESYNC);
			break;
		}
		case UNKNOWN: {
			addAttribute(root, ARTIFACT_ACTION, ARTIFACT_ACTION_UNKNOWN);
			break;
		}
		default: {
			throw new GenericArtifactParsingException(
					"Non valid value for root-attribute " + ARTIFACT_ACTION
							+ " specified.");
		}
		}

		switch (genericArtifact.getArtifactMode()) {
		case CHANGEDFIELDSONLY: {
			addAttribute(root, ARTIFACT_MODE, ARTIFACT_MODE_CHANGED_FIELDS_ONLY);
			break;
		}
		case COMPLETE: {
			addAttribute(root, ARTIFACT_MODE, ARTIFACT_MODE_COMPLETE);
			break;
		}
		case UNKNOWN: {
			addAttribute(root, ARTIFACT_MODE, ARTIFACT_MODE_UNKNOWN);
			break;
		}
		default: {
			throw new GenericArtifactParsingException(
					"Non valid value for root-attribute " + ARTIFACT_MODE
							+ "specified.");
		}
		}

		ArtifactTypeValue artifactType = genericArtifact.getArtifactType();
		switch (artifactType) {
		case ATTACHMENT: {
			addAttribute(root, ARTIFACT_TYPE, ARTIFACT_TYPE_ATTACHMENT);
			String content = genericArtifact.getArtifactValue();
			// TODO BASE64 validation?
			if (content != null)
				// embed content in CDATA section
				setValue(root, content, true);
			break;
		}
		case DEPENDENCY: {
			addAttribute(root, ARTIFACT_TYPE, ARTIFACT_TYPE_DEPENDENCY);
			break;
		}
		case PLAINARTIFACT: {
			addAttribute(root, ARTIFACT_TYPE, ARTIFACT_TYPE_PLAIN_ARTIFACT);
			break;
		}
		case UNKNOWN: {
			addAttribute(root, ARTIFACT_TYPE, ARTIFACT_TYPE_UNKNOWN);
			break;
		}
		default: {
			throw new GenericArtifactParsingException(
					"Non valid value for root-attribute " + ARTIFACT_TYPE
							+ " specified.");
		}
		}

		switch (genericArtifact.getIncludesFieldMetaData()) {
		case TRUE: {
			addAttribute(root, INCLUDES_FIELD_META_DATA,
					INCLUDES_FIELD_META_DATA_TRUE);
			break;
		}
		case FALSE: {
			addAttribute(root, INCLUDES_FIELD_META_DATA,
					INCLUDES_FIELD_META_DATA_FALSE);
			break;
		}
		default: {
			throw new GenericArtifactParsingException(
					"Non valid value for root-attribute " + ARTIFACT_MODE
							+ "specified.");
		}
		}

		addAttribute(root, SOURCE_ARTIFACT_LAST_MODIFICATION_DATE,
				genericArtifact.getSourceArtifactLastModifiedDate());
		addAttribute(root, TARGET_ARTIFACT_LAST_MODIFICATION_DATE,
				genericArtifact.getTargetArtifactLastModifiedDate());
		// addAttribute(root, ARTIFACT_LAST_READ_TRANSACTION_ID, genericArtifact
		// .getLastReadTransactionId());
		addAttribute(root, ERROR_CODE, genericArtifact.getErrorCode());
		addAttribute(root, SOURCE_ARTIFACT_VERSION, genericArtifact
				.getSourceArtifactVersion());
		addAttribute(root, TARGET_ARTIFACT_VERSION, genericArtifact
				.getTargetArtifactVersion());
		addAttribute(root, CONFLICT_RESOLUTION_PRIORITY, genericArtifact
				.getConflictResolutionPriority());

		// only create optional attributes if necessary
		if (artifactType == ArtifactTypeValue.DEPENDENCY
				|| artifactType == ArtifactTypeValue.ATTACHMENT) {
			addAttribute(root, DEP_PARENT_SOURCE_ARTIFACT_ID, genericArtifact
					.getDepParentSourceArtifactId());
			addAttribute(root, DEP_PARENT_SOURCE_REPOSITORY_ID, genericArtifact
					.getDepParentSourceRepositoryId());
			addAttribute(root, DEP_PARENT_SOURCE_REPOSITORY_KIND,
					genericArtifact.getDepParentSourceRepositoryKind());
			addAttribute(root, DEP_PARENT_TARGET_ARTIFACT_ID, genericArtifact
					.getDepParentTargetArtifactId());
			addAttribute(root, DEP_PARENT_TARGET_REPOSITORY_ID, genericArtifact
					.getDepParentTargetRepositoryId());
			addAttribute(root, DEP_PARENT_TARGET_REPOSITORY_KIND,
					genericArtifact.getDepParentTargetRepositoryKind());
		}

		// dependencies have even more optional attributes
		if (artifactType == ArtifactTypeValue.DEPENDENCY) {
			addAttribute(root, DEP_CHILD_SOURCE_ARTIFACT_ID, genericArtifact
					.getDepChildSourceArtifactId());
			addAttribute(root, DEP_CHILD_SOURCE_REPOSITORY_ID, genericArtifact
					.getDepChildSourceRepositoryId());
			addAttribute(root, DEP_CHILD_SOURCE_REPOSITORY_KIND,
					genericArtifact.getDepChildSourceRepositoryKind());
			addAttribute(root, DEP_CHILD_TARGET_ARTIFACT_ID, genericArtifact
					.getDepChildTargetArtifactId());
			addAttribute(root, DEP_CHILD_TARGET_REPOSITORY_ID, genericArtifact
					.getDepChildTargetRepositoryId());
			addAttribute(root, DEP_CHILD_TARGET_REPOSITORY_KIND,
					genericArtifact.getDepChildTargetRepositoryKind());
		}

		addAttribute(root, SOURCE_ARTIFACT_ID, genericArtifact
				.getSourceArtifactId());
		addAttribute(root, SOURCE_REPOSITORY_ID, genericArtifact
				.getSourceRepositoryId());
		addAttribute(root, SOURCE_REPOSITORY_KIND, genericArtifact
				.getSourceRepositoryKind());
		addAttribute(root, SOURCE_SYSTEM_ID, genericArtifact
				.getSourceSystemId());
		addAttribute(root, SOURCE_SYSTEM_KIND, genericArtifact
				.getSourceSystemKind());
		addAttribute(root, TARGET_ARTIFACT_ID, genericArtifact
				.getTargetArtifactId());
		addAttribute(root, TARGET_REPOSITORY_ID, genericArtifact
				.getTargetRepositoryId());
		addAttribute(root, TARGET_REPOSITORY_KIND, genericArtifact
				.getTargetRepositoryKind());
		addAttribute(root, TARGET_SYSTEM_ID, genericArtifact
				.getTargetSystemId());
		addAttribute(root, TARGET_SYSTEM_KIND, genericArtifact
				.getTargetSystemKind());
		addAttribute(root, SOURCE_SYSTEM_TIMEZONE, genericArtifact
				.getSourceSystemTimezone());
		addAttribute(root, TARGET_SYSTEM_TIMEZONE, genericArtifact
				.getTargetSystemTimezone());
		// addAttribute(root, SOURCE_SYSTEM_ENCODING, genericArtifact
		// .getSourceSystemEncoding());
		// addAttribute(root, TARGET_SYSTEM_ENCODING, genericArtifact
		// .getTargetSystemEncoding());
		addAttribute(root, TRANSACTION_ID, genericArtifact.getTransactionId());

		if (genericArtifact.getAllGenericArtifactFields() != null) {
			// now add fields
			for (GenericArtifactField genericArtifactField : genericArtifact
					.getAllGenericArtifactFields()) {
				Element field = addElement(root, ARTIFACT_FIELD_ELEMENT_NAME,
						CCF_ARTIFACT_NAMESPACE);
				switch (genericArtifactField.getFieldAction()) {
				case APPEND: {
					addAttribute(field, FIELD_ACTION, FIELD_ACTION_APPEND);
					break;
				}
				case DELETE: {
					addAttribute(field, FIELD_ACTION, FIELD_ACTION_DELETE);
					break;
				}
				case REPLACE: {
					addAttribute(field, FIELD_ACTION, FIELD_ACTION_REPLACE);
					break;
				}
				case UNKNOWN: {
					addAttribute(field, FIELD_ACTION, FIELD_ACTION_UNKNOWN);
					break;
				}
				default: {
					throw new GenericArtifactParsingException(
							"Non valid value for field-attribute "
									+ FIELD_ACTION + " specified.");
				}
				}

				addAttribute(field, FIELD_NAME, genericArtifactField
						.getFieldName());
				addAttribute(field, FIELD_TYPE, genericArtifactField
						.getFieldType());
				if (genericArtifactField.getFieldValueHasChanged()) {
					addAttribute(field, FIELD_VALUE_HAS_CHANGED,
							FIELD_VALUE_HAS_CHANGED_TRUE);
				} else {
					addAttribute(field, FIELD_VALUE_HAS_CHANGED,
							FIELD_VALUE_HAS_CHANGED_FALSE);
				}

				if (genericArtifact.getIncludesFieldMetaData().equals(
						GenericArtifact.IncludesFieldMetaDataValue.TRUE)) {
					addAttribute(field, MIN_OCCURS, genericArtifactField
							.getMinOccursValue());
					addAttribute(field, MAX_OCCURS, genericArtifactField
							.getMaxOccursValue());
					addAttribute(field, NULL_VALUE_SUPPORTED,
							genericArtifactField.getNullValueSupported());
					addAttribute(field, ALTERNATIVE_FIELD_NAME,
							genericArtifactField.getAlternativeFieldName());
				}

				setFieldValue(field, genericArtifactField.getFieldValue(),
						genericArtifactField.getFieldValueType());
			}
		}
		root.addAttribute(new QName(SCHEMA_LOCATION_ATTRIBUTE, new Namespace(
				SCHEMA_NAMESPACE_PREFIX, SCHEMA_NAMESPACE)),
				CCF_SCHEMA_LOCATION);
		return document;
	}

	/**
	 * Set the content of the element
	 * 
	 * @param element
	 *            XML element
	 * @param content
	 *            content, encoded as String
	 * @param useCDATASection
	 *            if true, embed content into CDATA-section
	 */
	private static void setValue(Element element, String content,
			boolean useCDATASection) {
		if (useCDATASection)
			element.addCDATA(content);
		else
			element.setText(content);
	}

	/**
	 * Extracts the content of the supplied element
	 * 
	 * @param element
	 *            XML element
	 * @return Content of the element encoded as String
	 */
	private static String getValue(Element element) {
		return element.getText();
	}

	/**
	 * Adds the root-element to the XML document
	 * 
	 * @param document
	 *            XML document
	 * @param rootElementName
	 *            name of root-element
	 * @param rootElementNamespace
	 *            name namespace, root-element belongs to
	 * @return newly create root-element of XML document
	 */
	private static Element addRootElement(Document document,
			String rootElementName, String rootElementNamespace) {
		return document.addElement(rootElementName, rootElementNamespace);
	}

	/**
	 * Adds a sub-element to parent-element
	 * 
	 * @param parentElement
	 *            parent element that should get a new sub-element
	 * @param subElementName
	 *            name of the new created sub-element
	 * @param subElementNamespace
	 *            namespace of the new sub-element
	 * @return newly created sub-element
	 */
	private static Element addElement(Element parentElement,
			String subElementName, String subElementNamespace) {
		return parentElement.addElement(subElementName, subElementNamespace);
	}

	/**
	 * Converts native Java types to the XML format
	 * 
	 * @param field
	 *            XML element for the field
	 * @param fieldValue
	 *            Java object for the field's value
	 * @param fieldValueType
	 *            CCF data type that should be used
	 * @throws GenericArtifactParsingException
	 *             Will be thrown if attribute values were not set appropriately
	 */
	private static void setFieldValue(Element field, Object fieldValue,
			FieldValueTypeValue fieldValueType)
			throws GenericArtifactParsingException {
		// TODO Carefully specify conversion for every single type
		if (fieldValueType == null) {
			throw new GenericArtifactParsingException(
					"Non valid value for field-attribute "
							+ field.attributeValue(FIELD_NAME) + " specified.");
		}
		switch (fieldValueType) {
		case BASE64STRING: {
			addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_BASE64STRING);
			break;
		}
		case BOOLEAN: {
			addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_BOOLEAN);
			break;
		}
		case DATE: {
			addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_DATE);
			break;
		}
		case DATETIME: {
			addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_DATETIME);
			break;
		}
		case DOUBLE: {
			addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_DOUBLE);
			break;
		}
		case HTMLSTRING: {
			addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_HTML_STRING);
			break;
		}
		case INTEGER: {
			addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_INTEGER);
			break;
		}
		case STRING: {
			addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_STRING);
			break;
		}
		case USER: {
			addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_USER);
			break;
		}
			// case LIST: {
			// addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_LIST);
			// break;
			// }
			// case MULTI_SELECT_LIST: {
			// addAttribute(field, FIELD_VALUE_TYPE,
			// FIELD_VALUE_TYPE_MULTI_SELECT_LIST);
			// break;
			// }
			// case UNKNOWN: {
			// addAttribute(field, FIELD_VALUE_TYPE, FIELD_VALUE_TYPE_UNKNOWN);
			// break;
			// }
		default: {
			throw new GenericArtifactParsingException(
					"Non valid value for field-attribute " + FIELD_VALUE_TYPE
							+ " specified.");
		}
		}
		if (fieldValue == null) {
			addAttribute(field, FIELD_VALUE_IS_NULL, FIELD_VALUE_IS_NULL_TRUE);
		} else {
			addAttribute(field, FIELD_VALUE_IS_NULL, FIELD_VALUE_IS_NULL_FALSE);
			if (fieldValue instanceof Date)
				synchronized (df) {
					setValue(field, df.format((Date) fieldValue), false);
				}
			else if (fieldValue instanceof Calendar)
				synchronized (df) {
					setValue(field, df
							.format(((Calendar) fieldValue).getTime()), false);
				}
			else if (fieldValue instanceof XMLGregorianCalendar)
				synchronized (df) {
					setValue(field, df
							.format(((XMLGregorianCalendar) fieldValue)
									.toGregorianCalendar().getTime()), false);
				}
			else
				setValue(field, fieldValue.toString(), false);
		}
	}

	/**
	 * Adds an attribute with the supplied value to the supplied element
	 * 
	 * @param element
	 *            element in question
	 * @param attributeName
	 *            attribute name in question
	 * @param value
	 *            value of the attribute
	 */
	private static void addAttribute(Element element, String attributeName,
			String value) {
		element.addAttribute(attributeName, value);
	}

	public static GenericArtifactField getMandatoryGAField(String name,
			GenericArtifact ga) {
		List<GenericArtifactField> gaFields = ga
				.getAllGenericArtifactFieldsWithSameFieldTypeAndFieldName(
						GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
						name);
		if (gaFields == null || gaFields.size() == 0) {
			return null;
		} else if (gaFields.size() == 1) {
			GenericArtifactField field = gaFields.get(0);
			return field;
		} else {
			throw new RuntimeException(
					"More than one mandatory field with the same field name: "
							+ name);
		}
	}

	public static GenericArtifactField getFlexGAField(String name,
			GenericArtifact ga) {
		List<GenericArtifactField> gaFields = ga
				.getAllGenericArtifactFieldsWithSameFieldTypeAndFieldName(
						GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD, name);
		if (gaFields == null || gaFields.size() == 0) {
			return null;
		} else if (gaFields.size() == 1) {
			GenericArtifactField field = gaFields.get(0);
			return field;
		} else {
			throw new RuntimeException(
					"More than one flex field with the same field name: "
							+ name);
		}
	}

	public static String getStringMandatoryGAField(String fieldName,
			GenericArtifact ga) {
		String fieldValue = null;
		GenericArtifactField gaField = getMandatoryGAField(fieldName, ga);
		if (gaField != null) {
			fieldValue = (String) gaField.getFieldValue();
		}
		return fieldValue;
	}

	public static String getStringFlexGAField(String fieldName,
			GenericArtifact ga) {
		String fieldValue = null;
		GenericArtifactField gaField = getFlexGAField(fieldName, ga);
		if (gaField != null) {
			fieldValue = (String) gaField.getFieldValue();
		}
		return fieldValue;
	}

	public static int getIntMandatoryGAField(String fieldName,
			GenericArtifact ga) {
		int fieldValue = 0;
		GenericArtifactField gaField = getMandatoryGAField(fieldName, ga);
		if (gaField != null) {
			Object fieldValueObj = gaField.getFieldValue();
			if (fieldValueObj instanceof String) {
				String fieldValueString = (String) fieldValueObj;
				fieldValue = Integer.parseInt(fieldValueString);
			} else if (fieldValueObj instanceof Integer) {
				fieldValue = ((Integer) fieldValueObj).intValue();
			}
		}
		return fieldValue;
	}

	public static Date getDateMandatoryGAField(String fieldName,
			GenericArtifact ga) {
		Date fieldValue = null;
		GenericArtifactField gaField = getMandatoryGAField(fieldName, ga);
		if (gaField != null) {
			Object fieldValueObj = gaField.getFieldValue();
			if (fieldValueObj instanceof String) {
				String fieldValueString = (String) fieldValueObj;
				fieldValue = DateUtil.parse(fieldValueString);
			} else if (fieldValueObj instanceof Date) {
				fieldValue = (Date) fieldValueObj;
			}
		}
		return fieldValue;
	}

	public static String getStringGAField(String fieldName, GenericArtifact ga) {
		String fieldValue = null;
		GenericArtifactField gaField = GenericArtifactHelper.getGAField(
				fieldName, ga);
		if (gaField != null) {
			fieldValue = (String) gaField.getFieldValue();
		}
		return fieldValue;
	}

	public static GenericArtifactField getGAField(String name,
			GenericArtifact ga) {
		List<GenericArtifactField> gaFields = ga
				.getAllGenericArtifactFieldsWithSameFieldName(name);
		if (gaFields == null || gaFields.size() == 0) {
			return null;
		} else if (gaFields.size() == 1) {
			GenericArtifactField field = gaFields.get(0);
			return field;
		} else {
			throw new RuntimeException(
					"More than one flex field with the same field name: "
							+ name);
		}
	}
}
