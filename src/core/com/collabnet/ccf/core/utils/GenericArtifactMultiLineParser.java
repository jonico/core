package com.collabnet.ccf.core.utils;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.openadaptor.core.Component;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ProcessingException;

/**
 * This component will take the content of an XML document (string) line by line
 * and converts the contained XML documents to a Dom4J document. It assumes that
 * the XML documents piped in line by line are all compliant to the Generic
 * Artifact XML schema (at least they should end with </artifact>).
 * 
 * @author jnicolai
 * 
 */
public class GenericArtifactMultiLineParser extends Component implements
		IDataProcessor {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(GenericArtifactMultiLineParser.class);

	/**
	 * Constructor
	 */
	public GenericArtifactMultiLineParser() {
	}

	/**
	 * Hook to perform any validation of the component properties required by
	 * the implementation. Default behaviour should be a no-op.
	 */
	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
	}

	/**
	 * Reset processor
	 */
	public void reset(Object context) {
	}

	/**
	 * This string buffer will store the strings line by line until all lines
	 * for one XML document have arrived. After this, the content of the buffer
	 * will be used to construct an XML document and empty the buffer again.
	 */
	private StringBuffer xmlDocument = new StringBuffer();

	/**
	 * Constructs XML documents line per line. The records can be either strings
	 * or a dom4j document object
	 * 
	 * @param record
	 *            the message record
	 * 
	 * @return a Document[] containing no entry if the current line has not
	 *         closed the top level element of the XML document or one entry
	 *         (the constructed XML document) if the line piped into this method
	 *         closes the top level element
	 * 
	 * @throws ProcessingException
	 *             if the record type is not supported
	 */
	@SuppressWarnings("unchecked")
	public Object[] process(Object record) throws ProcessingException {
		if (record == null)
			return null;

		Document dom4JXmlDocument;

		if (record instanceof String) {
			// trackerWorkflowXmlDocument=createDOMFromString("<?xml
			// version='1.0' encoding='UTF-8'?>\n"+(String) record);
			String xmlDocumentLine = (String) record;
			// check whether XML document was terminated by top-level element so
			// that we can finally parse it
			if (xmlDocumentLine.matches("</artifact>")) {
				xmlDocument.append(xmlDocumentLine);
				dom4JXmlDocument = createDOMFromString(xmlDocument.toString());
				xmlDocument = new StringBuffer();
			} else {
				// we append the line to the already stored content of the xml
				// document
				xmlDocument.append(xmlDocumentLine);
				xmlDocument.append('\n');
				return new Document[] {};
			}
		}

		else if (record instanceof Document)
			dom4JXmlDocument = (Document) record;

		// if we get this far then we cannot process the record
		else
			throw new ProcessingException("Invalid record (type: "
					+ record.getClass().toString()
					+ "). Cannot convert to XML document", this);

		return new Document[] { dom4JXmlDocument };
	}

	/**
	 * Use the XML supplied to create a DOM document
	 * 
	 * @param xml
	 *            valid XML
	 * 
	 * @return dom4j document object
	 * 
	 * @throws ProcessingException
	 *             if the supplied XML cannot be parsed
	 */
	private Document createDOMFromString(String xml) {
		try {
			return DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			log.error("Failed to parse XML document: " + xml);
			throw new ProcessingException("Failed to parse XML: "
					+ e.getMessage(), this);
		}
	}
}