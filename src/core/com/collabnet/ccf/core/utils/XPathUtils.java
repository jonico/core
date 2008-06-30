package com.collabnet.ccf.core.utils;

import java.util.Collections;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.xpath.DefaultXPath;

import com.collabnet.ccf.core.ga.GenericArtifactParsingException;

public class XPathUtils {

	
	private static final String ARTIFACT_ROOT_ELEMENT_NAME = "artifact";
	private static final String CCF_ARTIFACT_NAMESPACE = "http://ccf.open.collab.net/GenericArtifactV1.0";
	private static final String CCF_NAMESPACE_PREFIX = "ccf";
	private static Map<String, String> ccfNamespaceMap = Collections
			.singletonMap(CCF_NAMESPACE_PREFIX, CCF_ARTIFACT_NAMESPACE);

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
	public static Element getRootElement(Document document)
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
	public static String getAttributeValue(Element element,
			String attributeName) throws GenericArtifactParsingException {
		XPath xpath = new DefaultXPath("@"+ attributeName);
		xpath.setNamespaceURIs(ccfNamespaceMap);
		Node attributeNode = xpath.selectSingleNode(element);
		if (attributeNode == null)
			throw new GenericArtifactParsingException("Missing attribute: "
					+ attributeName + " in element " + element.getName());
		else
			return attributeNode.getText();
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
	public static void addAttribute(Element element, String attributeName,
			String value) {
		element.addAttribute(attributeName, value);
		//element.remove();
	}
	
}
