package com.collabnet.ccf.core.ga;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.xml.datatype.XMLGregorianCalendar;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.openadaptor.auxil.connector.iostream.EncodingAwareObject;
import org.openadaptor.core.exception.RecordFormatException;

import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;

/**
 * @author jnicolai
 *
 * This class helps to generate and parse the schema defined generic xml artifact format
 */
public class GenericArtifactHelper {

	// TODO: Think about a more compact datetime format that still preserves timezone and seconds
	private static DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,new Locale("en"));	
	private static final String ARTIFACT_ROOT_ELEMENT_NAME = "artifact";
	private static final String CCF_ARTIFACT_NAMESPACE = "http://ccf.open.collab.net/GenericArtifactV1.0";
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

	/**
	 * Parses an XML document that complies to the generic artifact schema and
	 * represents it as a Java object
	 * 
	 * @param document
	 *            XML representation of the generic artifact
	 * @return Java object representing the artifact
	 */
	public static GenericArtifact createGenericArtifactJavaObject(
			Document document) {
		GenericArtifact genericArtifact = new GenericArtifact();
		// TODO Parse the XML document and populate the Java objects
		return genericArtifact;
	}

	/**
	 * Creates a generic artifact XML representation out of the Java object
	 * 
	 * @param genericArtifact
	 *            Java object that will be represented as XML document
	 * @return XML representation of generic artifact
	 */
	public static Document createGenericArtifactXMLDocument(
			GenericArtifact genericArtifact) throws GenericArtifactParsingException {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(EncodingAwareObject.UTF_8);
		//  Create XML elements with attributes
		Element root=document.addElement(ARTIFACT_ROOT_ELEMENT_NAME, CCF_ARTIFACT_NAMESPACE);
		
		switch (genericArtifact.getArtifactAction()) {
			case CREATE: {
				root.addAttribute(ARTIFACT_ACTION,ARTIFACT_ACTION_CREATE);
				break;
			}
			case DELETE: {
				root.addAttribute(ARTIFACT_ACTION,ARTIFACT_ACTION_DELETE);
				break;
			}
			case IGNORE: {
				root.addAttribute(ARTIFACT_ACTION,ARTIFACT_ACTION_IGNORE);
				break;
			}
			case UPDATE: {
				root.addAttribute(ARTIFACT_ACTION,ARTIFACT_ACTION_UPDATE);
				break;
			}
			default: {
				throw new GenericArtifactParsingException("Non valid value for root-attribute "+ARTIFACT_ACTION+ " specified.");
			}
		}
		root.addAttribute(ARTIFACT_LAST_MODIFIED_DATE,genericArtifact.getArtifactLastModifiedDate());
		
		switch (genericArtifact.getArtifactMode()) {
			case CHANGEDFIELDSONLY: {
				root.addAttribute(ARTIFACT_MODE, ARTIFACT_MODE_CHANGED_FIELDS_ONLY);
				break;
			}
			case COMPLETE: {
				root.addAttribute(ARTIFACT_MODE, ARTIFACT_MODE_COMPLETE);
				break;
			}
			default: {
				throw new GenericArtifactParsingException("Non valid value for root-attribute "+ARTIFACT_MODE+ "specified.");
			}
		}

		ArtifactTypeValue artifactType=genericArtifact.getArtifactType();
		switch (artifactType) {
			case ATTACHMENT: {
				root.addAttribute(ARTIFACT_TYPE, ARTIFACT_TYPE_ATTACHMENT);
				String content=genericArtifact.getArtifactValue();
				// TODO BASE64 validation?
				if (content!=null)
					root.addCDATA(content);
				break;
			}
			case DEPENDENCY: {
				root.addAttribute(ARTIFACT_TYPE, ARTIFACT_TYPE_DEPENDENCY);
				break;
			}
			case PLAINARTIFACT: {
				root.addAttribute(ARTIFACT_TYPE, ARTIFACT_TYPE_PLAIN_ARTIFACT);
				break;
			}
			default: {
				throw new GenericArtifactParsingException("Non valid value for root-attribute "+ARTIFACT_TYPE+ " specified.");
			}
		}
		
		root.addAttribute(ARTIFACT_VERSION,genericArtifact.getArtifactVersion());
		root.addAttribute(CONFLICT_RESOLUTION_POLICY,genericArtifact.getConflictResolutionPolicy());
		
		// only create optional attributes if necessary
		if (artifactType==ArtifactTypeValue.DEPENDENCY || artifactType==ArtifactTypeValue.ATTACHMENT) {
			root.addAttribute(DEP_PARENT_SOURCE_ARTIFACT_ID,genericArtifact.getDepParentSourceArtifactId());
			root.addAttribute(DEP_PARENT_SOURCE_REPOSITORY_ID,genericArtifact.getDepParentSourceRepositoryId());
			root.addAttribute(DEP_PARENT_SOURCE_REPOSITORY_KIND,genericArtifact.getDepParentSourceRepositoryKind());
			root.addAttribute(DEP_PARENT_TARGET_ARTIFACT_ID,genericArtifact.getDepParentTargetArtifactId());
			root.addAttribute(DEP_PARENT_TARGET_REPOSITORY_ID,genericArtifact.getDepParentTargetRepositoryId());
			root.addAttribute(DEP_PARENT_TARGET_REPOSITORY_KIND,genericArtifact.getDepParentTargetRepositoryKind());
		}
		
		// dependencies have even more optional attributes
		if (artifactType==ArtifactTypeValue.DEPENDENCY) {
			root.addAttribute(DEP_CHILD_SOURCE_ARTIFACT_ID,genericArtifact.getDepChildSourceArtifactId());
			root.addAttribute(DEP_CHILD_SOURCE_REPOSITORY_ID,genericArtifact.getDepChildSourceRepositoryId());
			root.addAttribute(DEP_CHILD_SOURCE_REPOSITORY_KIND,genericArtifact.getDepChildSourceRepositoryKind());
			root.addAttribute(DEP_CHILD_TARGET_ARTIFACT_ID,genericArtifact.getDepChildTargetArtifactId());
			root.addAttribute(DEP_CHILD_TARGET_REPOSITORY_ID,genericArtifact.getDepChildTargetRepositoryId());
			root.addAttribute(DEP_CHILD_TARGET_REPOSITORY_KIND,genericArtifact.getDepChildTargetRepositoryKind());
		}
		
		root.addAttribute(SOURCE_ARTIFACT_ID,genericArtifact.getSourceArtifactId());
		root.addAttribute(SOURCE_REPOSITORY_ID,genericArtifact.getSourceRepositoryId());
		root.addAttribute(SOURCE_REPOSITORY_KIND,genericArtifact.getSourceRepositoryKind());
		root.addAttribute(SOURCE_SYSTEM_ID,genericArtifact.getSourceSystemId());
		root.addAttribute(SOURCE_SYSTEM_KIND,genericArtifact.getSourceSystemKind());
		root.addAttribute(TARGET_ARTIFACT_ID,genericArtifact.getTargetArtifactId());
		root.addAttribute(TARGET_REPOSITORY_ID,genericArtifact.getTargetRepositoryId());
		root.addAttribute(TARGET_REPOSITORY_KIND,genericArtifact.getTargetRepositoryKind());
		root.addAttribute(TARGET_SYSTEM_ID,genericArtifact.getTargetSystemId());
		root.addAttribute(TARGET_SYSTEM_KIND,genericArtifact.getTargetSystemKind());
		
		// now add fields
		for (GenericArtifactField genericArtifactField : genericArtifact.getAllGenericArtifactFields()) {
			Element field=root.addElement(ARTIFACT_FIELD_ELEMENT_NAME, CCF_ARTIFACT_NAMESPACE);
			switch (genericArtifactField.getFieldAction()) {
				case APPEND: {
					field.addAttribute(FIELD_ACTION,FIELD_ACTION_APPEND);
					break;
				}
				case DELETE: {
					field.addAttribute(FIELD_ACTION,FIELD_ACTION_DELETE);
					break;
				}
				case REPLACE: {
					field.addAttribute(FIELD_ACTION,FIELD_ACTION_REPLACE);
					break;
				}
				default: {
					throw new GenericArtifactParsingException("Non valid value for field-attribute "+FIELD_ACTION+ " specified.");
				}
			}
			
			field.addAttribute(FIELD_NAME,genericArtifactField.getFieldName());
			field.addAttribute(FIELD_TYPE,genericArtifactField.getFieldType());
			
			setFieldValue(field, genericArtifactField.getFieldValue(), genericArtifactField.getFieldValueType());
		}
		return document;
	}

	/**
	 * Converts native Java types to the XML format
	 * @param field XML element for the field
	 * @param fieldValue Java object for the field's value
	 * @param fieldValueType CCF data type that should be used
	 * @throws GenericArtifactParsingException Will be thrown if attribute values were not set appropriately
	 */
	private static void setFieldValue(Element field, Object fieldValue,
			FieldValueTypeValue fieldValueType) throws GenericArtifactParsingException {
		// TODO Carefully specify conversion for every single type
		
		switch (fieldValueType) {
			case BASE64STRING: {
				field.addAttribute(FIELD_VALUE_TYPE,FIELD_VALUE_TYPE_BASE64STRING);
				break;
			}
			case BOOLEAN: {
				field.addAttribute(FIELD_VALUE_TYPE,FIELD_VALUE_TYPE_BOOLEAN);
				break;
			}
			case DATE: {
				field.addAttribute(FIELD_VALUE_TYPE,FIELD_VALUE_TYPE_DATE);
				break;
			}
			case DATETIME: {
				field.addAttribute(FIELD_VALUE_TYPE,FIELD_VALUE_TYPE_DATETIME);
				break;
			}
			case DOUBLE: {
				field.addAttribute(FIELD_VALUE_TYPE,FIELD_VALUE_TYPE_DOUBLE);
				break;
			}
			case HTMLSTRING: {
				field.addAttribute(FIELD_VALUE_TYPE,FIELD_VALUE_TYPE_HTML_STRING);
				break;
			}
			case INTEGER: {
				field.addAttribute(FIELD_VALUE_TYPE,FIELD_VALUE_TYPE_INTEGER);
				break;
			}
			case STRING: {
				field.addAttribute(FIELD_VALUE_TYPE,FIELD_VALUE_TYPE_STRING);
				break;
			}
			case USER: {
				field.addAttribute(FIELD_VALUE_TYPE,FIELD_VALUE_TYPE_USER);
				break;
			}
			default: {
				throw new GenericArtifactParsingException("Non valid value for field-attribute "+FIELD_VALUE_TYPE+ " specified.");
			}
		}
		if (fieldValue==null) {
			field.addAttribute(FIELD_VALUE_IS_NULL, "true");
		}
		else {
			field.addAttribute(FIELD_VALUE_IS_NULL, "true");
			if (fieldValue instanceof Date)
				field.setText(df.format((Date)fieldValue));
			else if (fieldValue instanceof Calendar)
				field.setText(df.format(((Calendar)fieldValue).getTime()));
			else if (fieldValue instanceof XMLGregorianCalendar)
				field.setText(df.format(((XMLGregorianCalendar)fieldValue).toGregorianCalendar().getTime()));
			else
				field.setText(fieldValue.toString());
		}	
	}
}
