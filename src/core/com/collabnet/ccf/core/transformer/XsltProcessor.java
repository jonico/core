package com.collabnet.ccf.core.transformer;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.openadaptor.core.Component;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ProcessingException;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.util.FileUtils;

import com.collabnet.ccf.core.RepositoryRecord;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.XPathUtils;

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
	 * file name of the XSLT dir
	 */
	private String xsltDir;
	
	/**
	 * file name of the XSLT file
	 */
	//private String xsltFile;

	/**
	 * XSLT transforming component
	 */
	//private Transformer transform;
	
	private static final String SOURCE_SYSTEM_ID = "sourceSystemId";
	private static final String TARGET_SYSTEM_ID = "targetSystemId";
	private static final String SOURCE_REPOSITORY_ID = "sourceRepositoryId";
	private static final String TARGET_REPOSITORY_ID = "targetRepositoryId";
	
	public static final String PARAM_DELIMITER = "+";
	
	/**
	 * Sets the location of the directory containing the XSLT
	 * 
	 * @param xsltDir
	 *            the path to the Directory
	 */
	public void setXsltDir(String xsltDir) {
		this.xsltDir = xsltDir;
	}
	
	/**
	 * Sets the location of the file containing the XSLT
	 * 
	 * @param xsltFile
	 *            the path to the file
	 */
	public void setXsltFile(String xsltFile) {
		//this.xsltFile = xsltFile;
	}
	
	private HashMap<String, Transformer> xsltFileNameTransformerMap = null;
	
	/**
	 * Hook to perform any validation of the component properties required by
	 * the implementation. Defult behaviour should be a no-op.
	 */
	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		/*try {
			loadXSLT();
		} catch (RuntimeException ex) {
			exceptions.add(ex);
		}*/
		if(xsltFileNameTransformerMap==null)
			xsltFileNameTransformerMap = new HashMap<String, Transformer>();
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
	private Transformer loadXSLT(String xsltFile) {
		if (xsltFile == null)
			throw new ValidationException("xsltFile property not set", this);

		// if the file doesn't exist try to get it via the classpath
		URL url = FileUtils.toURL(xsltFile);
		Transformer transform = null;
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
		return transform;
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

		//if (record instanceof String)
			//return transform((String) record);

		if (record instanceof Document) {
			Transformer transform = null;
			try {
				transform = constructFileNameAndFetchTransformer(record);
			} catch (GenericArtifactParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return transform((Document) record, transform);
		}

		// if we get this far then we cannot process the record
		throw new ProcessingException("Invalid record (type: "
				+ record.getClass().toString() + "). Cannot apply transform",
				this);
	}

	public Transformer constructFileNameAndFetchTransformer(Object record) throws GenericArtifactParsingException{
		
		String fileName = null;
		Transformer transform = null;
		Document document = (Document) record;
		Element element = XPathUtils.getRootElement(document);
		String sourceSystemId = XPathUtils.getAttributeValue(element, SOURCE_SYSTEM_ID);
		String targetSystemId = XPathUtils.getAttributeValue(element, TARGET_SYSTEM_ID);
		String sourceRepositoryId = XPathUtils.getAttributeValue(element, SOURCE_REPOSITORY_ID);
		String targetRepositoryId = XPathUtils.getAttributeValue(element, TARGET_REPOSITORY_ID);
		String xsltDir = this.xsltDir;
		fileName = xsltDir+sourceSystemId+PARAM_DELIMITER+sourceRepositoryId+PARAM_DELIMITER+targetSystemId+PARAM_DELIMITER+targetRepositoryId+".xsl";
		if(!xsltFileNameTransformerMap.containsKey(fileName)){
			transform = loadXSLT(fileName);
			xsltFileNameTransformerMap.put(fileName, transform);
		}
		else {
			transform = xsltFileNameTransformerMap.get(fileName);
		}
		
		return transform;
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
	private Object[] transform(String s, Transformer transform) {
		return transform(createDOMFromString(s), transform);
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
	private Object[] transform(Document d, Transformer transform) {
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
