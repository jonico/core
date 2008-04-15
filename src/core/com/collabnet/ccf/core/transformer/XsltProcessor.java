package com.collabnet.ccf.core.transformer;

import java.net.URL;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.openadaptor.core.Component;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ProcessingException;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.util.FileUtils;

/**
 * This component is very similar to the standard openAdaptor XSLT processor but
 * additionally allows to output XML documents encoded as Dom4J instances. This
 * implies that it will only work with XSLT files that will generate valid XML
 * output (a restriction the standard openAdaptor XSLT processor does not have).
 * 
 * @author jnicolai
 * 
 */
public class XsltProcessor extends Component implements IDataProcessor {
	private static final Log log = LogFactory.getLog(XsltProcessor.class);

	/**
	 * file name of the XSLT file
	 */
	private String xsltFile;

	/**
	 * XSLT transforming component
	 */
	private Transformer transform;

	/**
	 * Sets the location of the file containing the XSLT
	 * 
	 * @param xsltFile
	 *            the path to the file
	 */
	public void setXsltFile(String xsltFile) {
		this.xsltFile = xsltFile;
	}

	/**
	 * Hook to perform any validation of the component properties required by
	 * the implementation. Defult behaviour should be a no-op.
	 */
	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		try {
			loadXSLT();
		} catch (RuntimeException ex) {
			exceptions.add(ex);
		}
	}

	/**
	 * Reset processor
	 */
	public void reset(Object context) {
	}

	/**
	 * Trys to load the XSLT from the file defined in the properties (will also
	 * try to find the file on the classpath if it can).
	 * 
	 * @throws ValidationException
	 *             if the XSLT file is not defined in the properties, the file
	 *             cannot be found or there was an error parsing it
	 */
	private void loadXSLT() {
		if (xsltFile == null)
			throw new ValidationException("xsltFile property not set", this);

		// if the file doesn't exist try to get it via the classpath
		URL url = FileUtils.toURL(xsltFile);
		if (url == null)
			throw new ValidationException("File not found: " + xsltFile, this);

		// load the transform
		try {
			// TODO Do not directly code the use of Saxon into the code?
			// TransformerFactory factory =
			// TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",
			// null);
			TransformerFactory factory = TransformerFactory.newInstance();
			transform = factory.newTransformer(new StreamSource(url.getPath()));

			log.info("Loaded XSLT [" + xsltFile + "] successfully");
		} catch (TransformerConfigurationException e) {
			throw new ValidationException("Failed to load XSLT: "
					+ e.getMessage(), this);
		}
	}

	/**
	 * Apply the transform to the record. The record can be either a XML string
	 * or a dom4j document object
	 * 
	 * @param record
	 *            the message record
	 * 
	 * @return a String[] with one String resulting from the transform
	 * 
	 * @throws ProcessingException
	 *             if the record type is not supported
	 */
	public Object[] process(Object record) throws ProcessingException {
		if (record == null)
			return null;

		if (record instanceof String)
			return transform((String) record);

		if (record instanceof Document)
			return transform((Document) record);

		// if we get this far then we cannot process the record
		throw new ProcessingException("Invalid record (type: "
				+ record.getClass().toString() + "). Cannot apply transform",
				this);
	}

	/**
	 * Applies the transform to the XML String
	 * 
	 * @param s
	 *            the XML text
	 * 
	 * @return an array containing a single XML string representing the
	 *         transformed XML string supplied
	 */
	private Object[] transform(String s) {
		return transform(createDOMFromString(s));
	}

	/**
	 * Applies the transform to the Dom4J document
	 * 
	 * @param d
	 *            the document to transform
	 * 
	 * @return an array containing a single XML string representing the
	 *         transformed document
	 */
	private Object[] transform(Document d) {
		try {
			return new Document[] { transform(transform, d) };
		} catch (TransformerException e) {
			throw new ProcessingException(
					"Transform failed: " + e.getMessage(), this);
		}
	}

	/**
	 * Applies the transform to the Dom4J document
	 * 
	 * @param transformer
	 * @param d
	 *            the document to transform
	 * 
	 * @return an array containing a single XML string representing the
	 *         transformed document
	 * @throws TransformerException
	 *             thrown if an XSLT runtime error happens during transformation
	 */
	public static Document transform(Transformer transformer, Document d)
			throws TransformerException {
		DocumentSource source = new DocumentSource(d);
		DocumentResult result = new DocumentResult();
		// TODO: Allow the user to specify stylesheet parameters?
		transformer.transform(source, result);
		System.out.println("1*******"+d.asXML());
		System.out.println("2*******"+result.getDocument().asXML());
		return result.getDocument();
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
			throw new ProcessingException("Failed to parse XML: "
					+ e.getMessage(), this);
		}
	}
}
