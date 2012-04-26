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

package com.collabnet.ccf.core.transformer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileChangeEvent;
import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.impl.DefaultFileMonitor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.SimpleVariableContext;
import org.openadaptor.auxil.processor.script.ScriptProcessor;
import org.openadaptor.core.Component;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ProcessingException;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.thirdparty.dom4j.Dom4jUtils;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * This component is very similar to the standard openAdaptor XSLT processor but
 * additionally allows to output XML documents encoded as Dom4J instances. This
 * implies that it will only work with XSLT files that will generate valid XML
 * output (a restriction the standard openAdaptor XSLT processor does not have).
 * 
 * Furthermore, this XSLT processor allows to dynamically derive one or more
 * XSLT files from the payload.
 * 
 * Either the xsltDir or xsltFile property should be set. If the xsltDir
 * property is set then the XsltProcessor will look into the directory for valid
 * XSLT files that match the derived names
 * 
 * If both the properties are set then the xsltDir property takes precedence.
 * 
 * @author jnicolai
 * 
 */
public class DynamicXsltProcessor extends Component implements IDataProcessor {

	/**
	 * This class is used to throw an exception whenever XSLT validation
	 * encounters any issue It is used by the secure XSLT factory and its
	 * transformers when only white listed Java function calls should be
	 * allowed.
	 * 
	 * @author jnicolai
	 * 
	 */
	private class XsltValidationErrorListener implements ErrorListener {

		@Override
		public void warning(TransformerException exception)
				throws TransformerException {
			throw exception;
		}

		@Override
		public void error(TransformerException exception)
				throws TransformerException {
			throw exception;
		}

		@Override
		public void fatalError(TransformerException exception)
				throws TransformerException {
			throw exception;
		}
	}

	/**
	 * processors to dynamically derive the file name from the message payload
	 */
	private List<ScriptProcessor> scriptProcessors = new ArrayList<ScriptProcessor>();

	private static final Log log = LogFactory
			.getLog(DynamicXsltProcessor.class);
	/**
	 * file name of the XSLT dir
	 */
	private String xsltDir;

	/**
	 * This property (true by default) determines whether the xslt file or all
	 * files in the specified xslt directory should be monitored. If yes, any
	 * new file, any deletion and any modification will cause a complete reload
	 * of all XSLT files.
	 */
	private boolean listenForFileUpdates = true;

	/**
	 * If this property is set to false (default), the XsltProcessor will
	 * process arbitrary XSLT documents (including Xalan extensions). If set to
	 * false, generic artifacts will only pass if they do not trigger other Java
	 * function calls but the ones specified in the whiteListedJavaFunctionCalls
	 * property
	 */
	private boolean onlyAllowWhiteListedJavaFunctionCalls = false;

	/**
	 * file name of the XSLT file
	 */
	private String xsltFile;

	/**
	 * This is used to listen for changes on files
	 */
	private DefaultFileMonitor fileMonitor = null;
	private FileSystemManager fsManager = null;

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
		this.xsltFile = xsltFile;
	}

	private Map<String, List<Transformer>> xsltFileNameTransformerMap = null;

	/**
	 * List of strings that contain the Java function calls which should be allowed
	 * if onlyAllowWhiteListedJavaFunctionCalls is on. Those Java function calls can only be used
	 * in the select attribute of the xsl:value element and the calling convention has to match exactly.
	 * E. g. stringutil:stripHTML(string(.)) and stringutil:encodeHTMLToEntityReferences(string(.))
	 * will only match the XSLT elements <xsl:value-of select="stringutil:stripHTML(string(.))"/> 
	 * and <xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
	 */
	private List<String> whiteListedJavaFunctionCalls = new ArrayList<String>();

	/**
	 * Hook to perform any validation of the component properties required by
	 * the implementation. Default behaviour should be a no-op.
	 */
	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		// we have to make this map thread safe because it will be
		// updated asynchronously
		xsltFileNameTransformerMap = Collections
				.synchronizedMap(new HashMap<String, List<Transformer>>());
		if (isListenForFileUpdates()) {
			try {
				fsManager = VFS.getManager();
			} catch (FileSystemException e) {
				exceptions.add(new ValidationException(
						"could not initialize file manager: " + e.getMessage(),
						this));
				return;
			}
			fileMonitor = new DefaultFileMonitor(new FileListener() {
				public void fileChanged(
						org.apache.commons.vfs.FileChangeEvent arg0)
						throws Exception {
					xsltFileNameTransformerMap.clear();
				}

				public void fileCreated(FileChangeEvent arg0) throws Exception {
					xsltFileNameTransformerMap.clear();
				}

				public void fileDeleted(FileChangeEvent arg0) throws Exception {
					xsltFileNameTransformerMap.clear();
				}
			});
		}

		String xsltDir = this.getXsltDir();
		String xsltFile = this.getXsltFile();
		if (!StringUtils.isEmpty(xsltDir)) {
			File xsltDirFile = new File(xsltDir);
			if (xsltDirFile.exists() && xsltDirFile.isDirectory()) {
				log.debug("xsltDir property " + xsltDir
						+ " is a valid directory");
				if (listenForFileUpdates) {
					FileObject fileObject = null;
					try {
						fileObject = fsManager.resolveFile(xsltDirFile
								.getAbsolutePath());
					} catch (FileSystemException e) {
						exceptions.add(new ValidationException(
								"xsltDir property " + xsltDir
										+ " is not a valid directory: "
										+ e.getMessage(), this));
						return;
					}
					fileMonitor.setRecursive(true);
					fileMonitor.addFile(fileObject);
					fileMonitor.start();
				}
				if (scriptProcessors.isEmpty()) {
					log.warn("No scripts supplied, so dynamic XSLT processor will not change data at all");
				}
			} else {
				exceptions.add(new ValidationException("xsltDir property "
						+ xsltDir + " is not a valid directory...!", this));
				return;
			}
		} else if (!StringUtils.isEmpty(xsltFile)) {
			File xsltFileFile = new File(xsltFile);
			if (xsltFileFile.exists() && xsltFileFile.isFile()) {
				log.debug("xsltFile property " + xsltFile + " is a valid file");
				if (listenForFileUpdates) {
					FileObject fileObject = null;
					try {
						fileObject = fsManager.resolveFile(xsltFileFile
								.getAbsolutePath());
					} catch (FileSystemException e) {
						exceptions.add(new ValidationException(
								"xsltFile property " + xsltFile
										+ " is not a valid file...:"
										+ e.getMessage(), this));
						return;
					}
					fileMonitor.addFile(fileObject);
					fileMonitor.start();
				}
			} else {
				exceptions.add(new ValidationException("xsltFile property "
						+ xsltFile + " is not a valid file...!", this));
				return;
			}
		}
		factory = TransformerFactory.newInstance();
		if (isOnlyAllowWhiteListedJavaFunctionCalls()) {
			try {
				secureFactory = TransformerFactory.newInstance();
				secureFactory.setFeature(
						XMLConstants.FEATURE_SECURE_PROCESSING, true);
				secureFactory
						.setErrorListener(new XsltValidationErrorListener());
			} catch (TransformerConfigurationException e) {
				exceptions
						.add(new ValidationException(
								"Setting secure processing feature on XSLT processor failed, bailing out since this feature is required by onlyAllowWhiteListedJavaFunctions property",
								this));
				return;
			}
		}
	}

	/**
	 * Reset processor
	 */
	public void reset(Object context) {
	}

	/**
	 * Tries to load the XSLT from the file defined in the properties
	 * 
	 * @throws ValidationException
	 *             if the XSLT file is not defined in the properties, the file
	 *             cannot be found or there was an error parsing it
	 */
	private List<Transformer> loadXSLT(File xsltFile, Element element) {
		List<Transformer> transformerList = new ArrayList<Transformer>();
		if (xsltFile == null) {
			String cause = "xsltFile property not set";
			log.error(cause);
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_TRANSFORMER_FILE);
			throw new CCFRuntimeException(cause);
		}

		try {
			Source source = null;
			if (isOnlyAllowWhiteListedJavaFunctionCalls()) {
				SAXReader reader = new SAXReader();
				Document originalDocument = reader.read(xsltFile);
				Document clonedDocument = (Document) originalDocument.clone();
				Element clonedRootElement = clonedDocument.getRootElement();
				// replace white listed Java functions in XPath expressions with
				// "."
				for (String functionCall : getWhiteListedJavaFunctionCalls()) {
					List<Element> nodes = findFunctionCalls(clonedRootElement,
							functionCall);
					for (Element e : nodes) {
						e.addAttribute("select", ".");
					}
				}
				Transformer secureTransform = secureFactory
						.newTransformer(new DocumentSource(clonedDocument));
				secureTransform
						.setErrorListener(new XsltValidationErrorListener());
				log.debug("Loaded sanitized version of XSLT [" + xsltFile
						+ "] successfully");
				transformerList.add(secureTransform);
				source = new DocumentSource(originalDocument);
			} else {
				source = new StreamSource(xsltFile);
			}
			Transformer transform = factory.newTransformer(source);
			log.debug("Loaded original XSLT [" + xsltFile + "] successfully");
			transformerList.add(transform);
		} catch (Exception e) {
			String cause = "Failed to load XSLT: [" + xsltFile + " ]"
					+ e.getMessage();
			log.error(cause, e);
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_TRANSFORMER_FILE);
			throw new CCFRuntimeException(cause, e);
		}

		return transformerList;
	}

	static List<Element> findFunctionCalls(Element xslt,
			final String functionCall) {
		XPath xpath = buildXpath(xslt, functionCall);
		@SuppressWarnings("unchecked")
		// jaxen doesn't do generics
		List<Element> nodes = xpath.selectNodes(xslt);
		return nodes;
	}

	private static XPath buildXpath(Element xslt, final String functionCall) {
		XPath xpath = xslt.createXPath(String.format(
				"//xsl:value-of[@select='%s']", functionCall));
		SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();
		namespaceContext.addNamespace("xsl",
				"http://www.w3.org/1999/XSL/Transform");
		xpath.setNamespaceContext(namespaceContext);
		return xpath;
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

		Document document = null;
		Element element = null;

		if (record instanceof Document) {
			document = (Document) record;
			element = document.getRootElement();
			try {
				String artifactAction = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.ARTIFACT_ACTION);
				String transactionId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TRANSACTION_ID);
				String errorCode = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.ERROR_CODE);
				// pass artifacts with ignore action
				if (artifactAction != null
						&& artifactAction
								.equals(GenericArtifactHelper.ARTIFACT_ACTION_IGNORE)) {
					return new Object[] { document };
				}
				// do not transform artifacts to be replayed (unless specific
				// error code is set)
				if (transactionId != null
						&& !transactionId.equals(GenericArtifact.VALUE_UNKNOWN)) {
					if (errorCode == null
							|| !errorCode
									.equals(GenericArtifact.ERROR_REPLAYED_WITH_TRANSFORMATION)) {
						return new Object[] { document };
					}
				}
			} catch (GenericArtifactParsingException e) {
				// do nothing, this artifact does not seem to be a generic
				// artifact
			}

			// now transform document
			String fileName = null;
			List<Transformer> transform = null;

			// only derive file name automatically if xslt dir is set
			if (!StringUtils.isEmpty(this.xsltDir)) {
				Document result = document;
				for (ScriptProcessor scriptProcessor : scriptProcessors) {
					fileName = deriveFilename(element, scriptProcessor);
					// do not do anything if file name == null
					if (fileName != null) {
						transform = lookupTransformer(result.getRootElement(),
								xsltDir + fileName);
						result = (Document) transform(result, transform,
								result.getRootElement())[0];
						if (log.isDebugEnabled()) {
							log.debug("(Intermediate) transformation result: "
									+ result.asXML());
						}
					}
				}
				// make sure users did not tamper with immutable top level
				// attributes
				restoreImmutableTopLevelAttributes(element,
						result.getRootElement());
				return new Document[] { result };
			} else {
				fileName = xsltFile;
				transform = lookupTransformer(element, fileName);
				return transform(document, transform, element);
			}
		}

		// if we get this far then we cannot process the record
		String cause = "Invalid record (type: " + record.getClass().toString()
				+ "). Cannot apply transform";
		log.error(cause);
		throw new CCFRuntimeException(cause);
	}

	/**
	 * These attributes must not be changed by user defined XSLT scripts
	 */
	static final String[] immutableAttributes = {
			GenericArtifactHelper.ARTIFACT_TYPE,
			GenericArtifactHelper.CONFLICT_RESOLUTION_PRIORITY,
			GenericArtifactHelper.DEP_CHILD_SOURCE_ARTIFACT_ID,
			GenericArtifactHelper.DEP_CHILD_SOURCE_REPOSITORY_ID,
			GenericArtifactHelper.DEP_CHILD_SOURCE_REPOSITORY_KIND,
			GenericArtifactHelper.DEP_CHILD_TARGET_ARTIFACT_ID,
			GenericArtifactHelper.DEP_CHILD_TARGET_REPOSITORY_ID,
			GenericArtifactHelper.DEP_CHILD_TARGET_REPOSITORY_KIND,
			GenericArtifactHelper.DEP_PARENT_SOURCE_ARTIFACT_ID,
			GenericArtifactHelper.DEP_PARENT_SOURCE_REPOSITORY_ID,
			GenericArtifactHelper.DEP_PARENT_SOURCE_REPOSITORY_KIND,
			GenericArtifactHelper.DEP_PARENT_TARGET_ARTIFACT_ID,
			GenericArtifactHelper.DEP_PARENT_TARGET_REPOSITORY_ID,
			GenericArtifactHelper.DEP_PARENT_TARGET_REPOSITORY_KIND,
			GenericArtifactHelper.ERROR_CODE,
			GenericArtifactHelper.INCLUDES_FIELD_META_DATA,
			GenericArtifactHelper.SOURCE_ARTIFACT_ID,
			GenericArtifactHelper.SOURCE_ARTIFACT_LAST_MODIFICATION_DATE,
			GenericArtifactHelper.SOURCE_ARTIFACT_VERSION,
			GenericArtifactHelper.SOURCE_REPOSITORY_ID,
			GenericArtifactHelper.SOURCE_REPOSITORY_KIND,
			GenericArtifactHelper.SOURCE_SYSTEM_ID,
			GenericArtifactHelper.SOURCE_SYSTEM_KIND,
			GenericArtifactHelper.SOURCE_SYSTEM_TIMEZONE,
			GenericArtifactHelper.TARGET_ARTIFACT_ID,
			GenericArtifactHelper.TARGET_ARTIFACT_LAST_MODIFICATION_DATE,
			GenericArtifactHelper.TARGET_ARTIFACT_VERSION,
			GenericArtifactHelper.TARGET_REPOSITORY_ID,
			GenericArtifactHelper.TARGET_REPOSITORY_KIND,
			GenericArtifactHelper.TARGET_SYSTEM_ID,
			GenericArtifactHelper.TARGET_SYSTEM_KIND,
			GenericArtifactHelper.TARGET_SYSTEM_TIMEZONE,
			GenericArtifactHelper.TRANSACTION_ID };

	private TransformerFactory secureFactory;

	private TransformerFactory factory;

	/**
	 * This message is used to restore the top level attributes that must not be
	 * changed by user defined transformations
	 * 
	 * @param originalElement
	 * @param newRootElement
	 */
	private static void restoreImmutableTopLevelAttributes(
			Element originalRootElement, Element newRootElement) {
		try {
			for (String immutableAttribute : immutableAttributes) {
				restoreAttribute(immutableAttribute, originalRootElement,
						newRootElement);
			}
			// special handling for artifact action that is only overridden if
			// it is not set to ignore
			String newArtifactAction = XPathUtils.getAttributeValue(
					newRootElement, GenericArtifactHelper.ARTIFACT_ACTION,
					false);
			if (!GenericArtifactHelper.ARTIFACT_ACTION_IGNORE
					.equals(newArtifactAction)) {
				restoreAttribute(GenericArtifactHelper.ARTIFACT_ACTION,
						originalRootElement, newRootElement);
			}
		} catch (GenericArtifactParsingException e) {
			throw new CCFRuntimeException(
					"While restoring immutable top level attributes after transformation, an error occured: "
							+ e.getMessage(), e);
		}
	}

	/**
	 * Copies one attribute from the original element to the new element
	 * 
	 * @param attributeName
	 * @param originalElement
	 * @param newElement
	 * @throws GenericArtifactParsingException
	 */
	private static void restoreAttribute(String attributeName,
			Element originalElement, Element newElement)
			throws GenericArtifactParsingException {
		newElement.addAttribute(attributeName, XPathUtils.getAttributeValue(
				originalElement, attributeName, false));
	}

	private List<Transformer> lookupTransformer(Element element, String fileName) {
		List<Transformer> transform;
		transform = xsltFileNameTransformerMap.get(fileName);

		if (transform == null) {
			transform = loadXSLT(new File(fileName), element);
			xsltFileNameTransformerMap.put(fileName, transform);
		}

		return transform;
	}

	/**
	 * Derives a dynamic XSLT filename based on message payload. Uses a standard
	 * {@link ScriptProcessor} to execute/evaluate the script.
	 * 
	 */
	protected String deriveFilename(Element rootElement,
			ScriptProcessor scriptProcessor) {

		Object[] scriptResArray = scriptProcessor.process(rootElement);
		String filename = null;
		if (null != scriptResArray && scriptResArray.length > 0) {
			Object dynamicFilename = scriptResArray[0];
			if (dynamicFilename != null) {
				filename = dynamicFilename.toString();
			}
		} else {
			log.debug("Script returns no XSLT file name, skipping this transformation step ...");
		}
		return filename;
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
	private Object[] transform(Document d, List<Transformer> transform,
			Element element) {
		try {
			return new Document[] { transform(transform, d) };
		} catch (TransformerException e) {
			String cause = "Transform failed: " + e.getMessage();
			log.error(cause, e);
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_TRANSFORMER_TRANSFORMATION);
			throw new CCFRuntimeException(cause, e);
		}
	}

	/**
	 * Applies the transform to the Dom4J document
	 * 
	 * @param transformer
	 *            List of transformers to be applied, all transformers have to
	 *            execute properly, but only the result from latest one is
	 *            returned
	 * @param d
	 *            the document to transform
	 * 
	 * @return an array containing a single XML string representing the
	 *         transformed document
	 * @throws TransformerException
	 *             thrown if an XSLT runtime error happens during transformation
	 */
	public static Document transform(List<Transformer> transformer, Document d)
			throws TransformerException {
		DocumentSource source = null;
		DocumentResult result = null;
		/**
		 * We will run through all transformers, so that we can have specially
		 * configured transformers that check things like calls to external
		 * functions Only the result from the last transformer is returned
		 */
		for (Transformer trans : transformer) {
			// TODO: Allow the user to specify stylesheet parameters?
			source = new DocumentSource(d);
			result = new DocumentResult();
			trans.transform(source, result);
		}

		return result.getDocument();
	}

	public String getXsltFile() {
		return xsltFile;
	}

	public String getXsltDir() {
		return this.xsltDir;
	}

	/**
	 * This property (true by default) determines whether the xslt file or all
	 * files in the specified xslt directory should be monitored. If yes, any
	 * new file, any deletion and any modification will cause a complete reload
	 * of all XSLT files.
	 */
	public void setListenForFileUpdates(boolean listenForFileUpdates) {
		this.listenForFileUpdates = listenForFileUpdates;
	}

	/**
	 * This property (true by default) determines whether the xslt file or all
	 * files in the specified xslt directory should be monitored. If yes, any
	 * new file, any deletion and any modification will cause a complete reload
	 * of all XSLT files.
	 */
	public boolean isListenForFileUpdates() {
		return listenForFileUpdates;
	}

	/**
	 * Sets the script that will derive dynamic XSLT filenames based on message
	 * payload.
	 * 
	 * @param scripts
	 *            list with scripts to derive XSLT file names that should be
	 *            executed in a row
	 */
	@SuppressWarnings("unchecked")
	public void setScripts(List<String> scripts) {
		for (String script : scripts) {
			ScriptProcessor scriptProcessor = new ScriptProcessor();
			scriptProcessor.setScript(script);
			scriptProcessor.validate(new java.util.ArrayList());
			scriptProcessors.add(scriptProcessor);
		}

	}

	/**
	 * If this property is set to false (default), the XsltProcessor will
	 * process arbitrary XSLT documents (including Xalan extensions). If set to
	 * false, generic artifacts will only pass if they do not trigger other Java
	 * function calls but the ones specified in the whiteListedJavaFunctionCalls
	 * property
	 */
	public void setOnlyAllowWhiteListedJavaFunctionCalls(
			boolean onlyAllowWhiteListedJavaFunctions) {
		this.onlyAllowWhiteListedJavaFunctionCalls = onlyAllowWhiteListedJavaFunctions;
	}

	/**
	 * If this property is set to false (default), the XsltProcessor will
	 * process arbitrary XSLT documents (including Xalan extensions). If set to
	 * false, generic artifacts will only pass if they do not trigger other Java
	 * function calls but the ones specified in the whiteListedJavaFunctionCalls
	 * property
	 */
	public boolean isOnlyAllowWhiteListedJavaFunctionCalls() {
		return onlyAllowWhiteListedJavaFunctionCalls;
	}

	/**
	 * List of strings that contain the Java function calls which should be allowed
	 * if onlyAllowWhiteListedJavaFunctionCalls is on. Those Java function calls can only be used
	 * in the select attribute of the xsl:value element and the calling convention has to match exactly.
	 * E. g. stringutil:stripHTML(string(.)) and stringutil:encodeHTMLToEntityReferences(string(.))
	 * will only match the XSLT elements <xsl:value-of select="stringutil:stripHTML(string(.))"/> 
	 * and <xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
	 */
	public void setWhiteListedJavaFunctionCalls(
			List<String> whiteListedJavaFunctionCalls) {
		this.whiteListedJavaFunctionCalls = whiteListedJavaFunctionCalls;
	}

	/**
	 * List of strings that contain the Java function calls which should be allowed
	 * if onlyAllowWhiteListedJavaFunctionCalls is on. Those Java function calls can only be used
	 * in the select attribute of the xsl:value element and the calling convention has to match exactly.
	 * E. g. stringutil:stripHTML(string(.)) and stringutil:encodeHTMLToEntityReferences(string(.))
	 * will only match the XSLT elements <xsl:value-of select="stringutil:stripHTML(string(.))"/> 
	 * and <xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
	 */
	public List<String> getWhiteListedJavaFunctionCalls() {
		return whiteListedJavaFunctionCalls;
	}
}
