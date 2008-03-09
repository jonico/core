package com.collabnet.ccf.core.ga;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.xpath.DefaultXPath;
import org.openadaptor.auxil.connector.iostream.EncodingAwareObject;

import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;

/**
 * 
 * This class helps to generate and parse the schema defined generic xml
 * artifact format
 * 
 * @author jnicolai
 * 
 */
public class GenericArtifactHelper {

	// TODO: Think about a more compact datetime format that still preserves
	// timezone and seconds
	private static DateFormat df = DateFormat.getDateTimeInstance(
			DateFormat.LONG, DateFormat.LONG, new Locale("en"));
	private static final String ARTIFACT_ROOT_ELEMENT_NAME = "artifact";
	private static final String CCF_ARTIFACT_NAMESPACE = "http://ccf.open.collab.net/GenericArtifactV1.0";
	private static final String CCF_NAMESPACE_PREFIX = "ccf";
	private static Map<String, String> ccfNamespaceMap = Collections
			.singletonMap(CCF_NAMESPACE_PREFIX, CCF_ARTIFACT_NAMESPACE);

	private static final String ARTIFACT_ACTION = "artifactAction";
	private static final String ARTIFACT_LAST_MODIFIED_DATE = "artifactLastModifiedDate";
	private static final String ARTIFACT_MODE = "artifactMode";
	private static final String ARTIFACT_TYPE = "artifactType";
	private static final String ARTIFACT_VERSION = "artifactVersion";
	private static final String CONFLICT_RESOLUTION_POLICY = "conflicResolutionPolicy";
	private static final String DEP_CHILD_SOURCE_ARTIFACT_ID = "depChildSourceArtifactId";
	private static final String DEP_CHILD_SOURCE_REPOSITORY_ID = "depChildSourceRepositoryId";
	private static final String DEP_CHILD_SOURCE_REPOSITORY_KIND = "depChildSourceRepositoryKind";
	private static final String DEP_CHILD_TARGET_ARTIFACT_ID = "depChildTargetArtifactId";;
	private static final String DEP_CHILD_TARGET_REPOSITORY_ID = "depChildTargetRepositoryId";;
	private static final String DEP_CHILD_TARGET_REPOSITORY_KIND = "depChildTargetRepositoryKind";;
	private static final String DEP_PARENT_SOURCE_ARTIFACT_ID = "depParentSourceArtifactId";
	private static final String DEP_PARENT_SOURCE_REPOSITORY_ID = "depParentSourceRepositoryId";
	private static final String DEP_PARENT_SOURCE_REPOSITORY_KIND = "depParentSourceRepositoryKind";
	private static final String DEP_PARENT_TARGET_ARTIFACT_ID = "depParentTargetArtifactId";;
	private static final String DEP_PARENT_TARGET_REPOSITORY_ID = "depParentTargetRepositoryId";;
	private static final String DEP_PARENT_TARGET_REPOSITORY_KIND = "depParentTargetRepositoryKind";;
	private static final String SOURCE_ARTIFACT_ID = "sourceArtifactId";
	private static final String SOURCE_REPOSITORY_ID = "sourceRepositoryId";
	private static final String SOURCE_REPOSITORY_KIND = "sourceRepositoryKind";
	private static final String SOURCE_SYSTEM_ID = "sourceSystemId";
	private static final String SOURCE_SYSTEM_KIND = "sourceSystemKind";
	private static final String TARGET_ARTIFACT_ID = "targetArtifactId";
	private static final String TARGET_REPOSITORY_ID = "targetRepositoryId";
	private static final String TARGET_REPOSITORY_KIND = "targetRepositoryKind";
	private static final String TARGET_SYSTEM_ID = "targetSystemId";
	private static final String TARGET_SYSTEM_KIND = "targetSystemKind";

	private static final String ARTIFACT_ACTION_CREATE = "create";
	private static final String ARTIFACT_ACTION_DELETE = "delete";
	private static final String ARTIFACT_ACTION_IGNORE = "ignore";
	private static final String ARTIFACT_ACTION_UPDATE = "update";

	private static final String ARTIFACT_MODE_COMPLETE = "complete";
	private static final String ARTIFACT_MODE_CHANGED_FIELDS_ONLY = "changedFieldsOnly";

	private static final String ARTIFACT_TYPE_ATTACHMENT = "attachment";
	private static final String ARTIFACT_TYPE_DEPENDENCY = "dependency";
	private static final String ARTIFACT_TYPE_PLAIN_ARTIFACT = "plainArtifact";

	private static final String ARTIFACT_FIELD_ELEMENT_NAME = "field";
	private static final XPath fieldSelector = new DefaultXPath(
			CCF_NAMESPACE_PREFIX + ":" + ARTIFACT_FIELD_ELEMENT_NAME);

	private static final String FIELD_ACTION = "fieldAction";

	private static final String FIELD_ACTION_APPEND = "append";
	private static final String FIELD_ACTION_DELETE = "delete";
	private static final String FIELD_ACTION_REPLACE = "replace";
	private static final String FIELD_NAME = "fieldName";
	private static final String FIELD_TYPE = "fieldType";
	private static final String FIELD_VALUE_IS_NULL = "fieldValueIsNull";
	private static final String FIELD_VALUE_TYPE = "fieldValueType";
	private static final String FIELD_VALUE_TYPE_BASE64STRING = "Base64String";
	private static final String FIELD_VALUE_TYPE_BOOLEAN = "Boolean";
	private static final String FIELD_VALUE_TYPE_DATE = "Date";
	private static final String FIELD_VALUE_TYPE_DATETIME = "DateTime";
	private static final String FIELD_VALUE_TYPE_DOUBLE = "Double";
	private static final String FIELD_VALUE_TYPE_HTML_STRING = "HTMLString";
	private static final String FIELD_VALUE_TYPE_INTEGER = "Integer";
	private static final String FIELD_VALUE_TYPE_STRING = "String";
	private static final String FIELD_VALUE_TYPE_USER = "User";
	private static final String FIELD_VALUE_IS_NULL_TRUE = "true";
	private static final String FIELD_VALUE_IS_NULL_FALSE = "false";
	private static final String FIELD_VALUE_HAS_CHANGED = "fieldValueHasChanged";
	private static final String FIELD_VALUE_HAS_CHANGED_TRUE = "true";
	private static final String FIELD_VALUE_HAS_CHANGED_FALSE = "false";

	// translation tables
	private static HashMap<String, GenericArtifact.ArtifactModeValue> artifactModeHashMap = new HashMap<String, GenericArtifact.ArtifactModeValue>(
			2);
	private static HashMap<String, GenericArtifact.ArtifactActionValue> artifactActionHashMap = new HashMap<String, GenericArtifact.ArtifactActionValue>(
			4);
	private static HashMap<String, GenericArtifact.ArtifactTypeValue> artifactTypeHashMap = new HashMap<String, GenericArtifact.ArtifactTypeValue>(
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

		artifactActionHashMap.put(ARTIFACT_ACTION_CREATE,
				GenericArtifact.ArtifactActionValue.CREATE);
		artifactActionHashMap.put(ARTIFACT_ACTION_DELETE,
				GenericArtifact.ArtifactActionValue.DELETE);
		artifactActionHashMap.put(ARTIFACT_ACTION_IGNORE,
				GenericArtifact.ArtifactActionValue.IGNORE);
		artifactActionHashMap.put(ARTIFACT_ACTION_UPDATE,
				GenericArtifact.ArtifactActionValue.UPDATE);

		artifactTypeHashMap.put(ARTIFACT_TYPE_ATTACHMENT,
				GenericArtifact.ArtifactTypeValue.ATTACHMENT);
		artifactTypeHashMap.put(ARTIFACT_TYPE_DEPENDENCY,
				GenericArtifact.ArtifactTypeValue.DEPENDENCY);
		artifactTypeHashMap.put(ARTIFACT_TYPE_PLAIN_ARTIFACT,
				GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);

		fieldActionHashMap.put(FIELD_ACTION_APPEND,
				GenericArtifactField.FieldActionValue.APPEND);
		fieldActionHashMap.put(FIELD_ACTION_DELETE,
				GenericArtifactField.FieldActionValue.DELETE);
		fieldActionHashMap.put(FIELD_ACTION_REPLACE,
				GenericArtifactField.FieldActionValue.REPLACE);

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

		genericArtifact.setArtifactLastModifiedDate(getAttributeValue(root,
				ARTIFACT_LAST_MODIFIED_DATE));

		ArtifactModeValue artifactMode = translateAttributeValue(root,
				ARTIFACT_MODE, artifactModeHashMap);
		genericArtifact.setArtifactMode(artifactMode);

		ArtifactTypeValue artifactType = translateAttributeValue(root,
				ARTIFACT_TYPE, artifactTypeHashMap);
		genericArtifact.setArtifactType(artifactType);

		if (artifactType == GenericArtifact.ArtifactTypeValue.ATTACHMENT) {
			genericArtifact.setArtifactValue(getValue(root));
		}

		genericArtifact.setArtifactVersion(getAttributeValue(root,
				ARTIFACT_VERSION));
		genericArtifact.setConflictResolutionPolicy(getAttributeValue(root,
				CONFLICT_RESOLUTION_POLICY));

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
		if (fieldValueIsNull) {
			genericArtifactField.setFieldValue(null);
		} else
			switch (fieldValueType) {
			case BASE64STRING: {
				// TODO Better conversion?
				genericArtifactField.setFieldValue(new String(value));
				break;
			}
			case BOOLEAN: {
				genericArtifactField.setFieldValue(new Boolean(value));
				break;
			}
			case DATE: {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(df.parse(value));
				genericArtifactField.setFieldValue(cal);
				break;
			}
			case DATETIME: {
				genericArtifactField.setFieldValue(df.parse(value));
				break;
			}
			case DOUBLE: {
				genericArtifactField.setFieldValue(new Double(value));
				break;
			}
			case HTMLSTRING: {
				// TODO Better conversion?
				genericArtifactField.setFieldValue(new String(value));
				break;
			}
			case INTEGER: {
				// TODO Better conversion?
				genericArtifactField.setFieldValue(new String(value));
				break;
			}
			case STRING: {
				genericArtifactField.setFieldValue(new String(value));
				break;
			}
			case USER: {
				genericArtifactField.setFieldValue(new String(value));
				break;
			}
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
		XPath xpath = new DefaultXPath("@" + CCF_NAMESPACE_PREFIX + ":"
				+ attributeName);
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
		default: {
			throw new GenericArtifactParsingException(
					"Non valid value for root-attribute " + ARTIFACT_ACTION
							+ " specified.");
		}
		}
		addAttribute(root, ARTIFACT_LAST_MODIFIED_DATE, genericArtifact
				.getArtifactLastModifiedDate());

		switch (genericArtifact.getArtifactMode()) {
		case CHANGEDFIELDSONLY: {
			addAttribute(root, ARTIFACT_MODE, ARTIFACT_MODE_CHANGED_FIELDS_ONLY);
			break;
		}
		case COMPLETE: {
			addAttribute(root, ARTIFACT_MODE, ARTIFACT_MODE_COMPLETE);
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
		default: {
			throw new GenericArtifactParsingException(
					"Non valid value for root-attribute " + ARTIFACT_TYPE
							+ " specified.");
		}
		}

		addAttribute(root, ARTIFACT_VERSION, genericArtifact
				.getArtifactVersion());
		addAttribute(root, CONFLICT_RESOLUTION_POLICY, genericArtifact
				.getConflictResolutionPolicy());

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
			default: {
				throw new GenericArtifactParsingException(
						"Non valid value for field-attribute " + FIELD_ACTION
								+ " specified.");
			}
			}

			addAttribute(field, FIELD_NAME, genericArtifactField.getFieldName());
			addAttribute(field, FIELD_TYPE, genericArtifactField.getFieldType());
			if (genericArtifactField.getFieldValueHasChanged()) {
				addAttribute(field, FIELD_VALUE_HAS_CHANGED,
						FIELD_VALUE_HAS_CHANGED_TRUE);
			} else {
				addAttribute(field, FIELD_VALUE_HAS_CHANGED,
						FIELD_VALUE_HAS_CHANGED_FALSE);
			}

			setFieldValue(field, genericArtifactField.getFieldValue(),
					genericArtifactField.getFieldValueType());
		}
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
		// TODO TODO: Do the Base64 conversion here or in GenericArtifact?
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
		// TODO: Do the Base64 conversion here or in GenericArtifactXMLHelper?
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
				setValue(field, df.format((Date) fieldValue), false);
			else if (fieldValue instanceof Calendar)
				setValue(field, df.format(((Calendar) fieldValue).getTime()),
						false);
			else if (fieldValue instanceof XMLGregorianCalendar)
				setValue(field, df.format(((XMLGregorianCalendar) fieldValue)
						.toGregorianCalendar().getTime()), false);
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
}
