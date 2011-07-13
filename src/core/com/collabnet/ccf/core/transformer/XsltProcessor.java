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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.openadaptor.core.Component;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ProcessingException;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.CCFUtils;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * This component is very similar to the standard openAdaptor XSLT processor but
 * additionally allows to output XML documents encoded as Dom4J instances. This
 * implies that it will only work with XSLT files that will generate valid XML
 * output (a restriction the standard openAdaptor XSLT processor does not have).
 * 
 * Either the xsltDir or xsltFile property should be set. If the xsltDir
 * property is set then the XsltProcessor will look into the directory for valid
 * XSLT files that correspond to the following naming conventions.
 * 
 * For each source and target repository mappings the XSLT file name that will
 * be looked up is <b>&lt;<i>source system id</i>&gt;+&lt;<i>source repository
 * id</i>&gt;+&lt;<i>target system id</i>&gt;+&lt;<i>tartget repository
 * id</i>&gt;.xsl</b> If the xsltFile property is set then the XsltProcessor
 * loads that XSLT file for all the transformations.
 * 
 * If both the properties are set then the xsltDir property takes precedence.
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
	 * This property (true by default) determines whether the xslt file or all
	 * files in the specified xslt directory should be monitored. If yes, any
	 * new file, any deletion and any modification will cause a complete reload
	 * of all XSLT files.
	 */
	private boolean listenForFileUpdates = true;

	/**
	 * file name of the XSLT file
	 */
	private String xsltFile;

	private static final String SOURCE_SYSTEM_ID = "sourceSystemId";
	private static final String TARGET_SYSTEM_ID = "targetSystemId";
	private static final String SOURCE_REPOSITORY_ID = "sourceRepositoryId";
	private static final String TARGET_REPOSITORY_ID = "targetRepositoryId";

	public static final String PARAM_DELIMITER = "+";

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

	private Map<String, Transformer> xsltFileNameTransformerMap = null;

	/**
	 * Hook to perform any validation of the component properties required by
	 * the implementation. Default behaviour should be a no-op.
	 */
	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		// we have to make this map thread safe because it will be
		// updated asynchronously
		xsltFileNameTransformerMap = Collections
				.synchronizedMap(new HashMap<String, Transformer>());
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
						fileObject = fsManager.resolveFile(xsltDirFile.getAbsolutePath());
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
						fileObject = fsManager.resolveFile(xsltFileFile.getAbsolutePath());
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
	}

	/**
	 * Reset processor
	 */
	public void reset(Object context) {
	}

	/**
	 * Tries to load the XSLT from the file defined in the properties (will also
	 * try to find the file on the class path if it can).
	 * 
	 * @throws ValidationException
	 *             if the XSLT file is not defined in the properties, the file
	 *             cannot be found or there was an error parsing it
	 */
	private Transformer loadXSLT(File xsltFile, Element element) {
		if (xsltFile == null) {
			String cause = "xsltFile property not set";
			log.error(cause);
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_TRANSFORMER_FILE);
			throw new CCFRuntimeException(cause);
		}
		Transformer transform = null;

		// load the transform
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			transform = factory.newTransformer(new StreamSource(xsltFile));

			log.debug("Loaded XSLT [" + xsltFile + "] successfully");
		} catch (TransformerConfigurationException e) {
			String cause = "Failed to load XSLT: [" + xsltFile + " ]"
					+ e.getMessage();
			log.error(cause, e);
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_TRANSFORMER_FILE);
			throw new CCFRuntimeException(cause, e);
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

		Document document = null;
		Element element = null;

		if (record instanceof Document) {
			Transformer transform = null;
			try {
				document = (Document) record;
				element = document.getRootElement();
				try {
					String artifactAction = XPathUtils.getAttributeValue(
							element, GenericArtifactHelper.ARTIFACT_ACTION);
					String transactionId = XPathUtils.getAttributeValue(
							element, GenericArtifactHelper.TRANSACTION_ID);
					String errorCode = XPathUtils.getAttributeValue(
							element, GenericArtifactHelper.ERROR_CODE);
					
					// pass artifacts with ignore action
					if (artifactAction != null
							&& artifactAction
									.equals(GenericArtifactHelper.ARTIFACT_ACTION_IGNORE) ) {
						return new Object[] { document };
					}
					// do not transform artifacts to be replayed (unless specific error code is set)
					if (transactionId != null
							&& !transactionId
									.equals(GenericArtifact.VALUE_UNKNOWN) ) {
						if (errorCode == null || !errorCode.equals(GenericArtifact.ERROR_REPLAYED_WITH_TRANSFORMATION)) {
							return new Object[] { document };
						}
					}
				} catch (GenericArtifactParsingException e) {
					// do nothing, this artifact does not seem to be a generic
					// artifact
				}
				transform = constructFileNameAndFetchTransformer(document);
			} catch (GenericArtifactParsingException e) {
				String cause = "Problem occured while parsing the Document to contruct the file name and fetching transformer";
				log.error(cause, e);
				XPathUtils.addAttribute(element,
						GenericArtifactHelper.ERROR_CODE,
						GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
				throw new CCFRuntimeException(cause, e);
			}
			return transform((Document) record, transform, element);
		}

		// if we get this far then we cannot process the record
		String cause = "Invalid record (type: " + record.getClass().toString()
				+ "). Cannot apply transform";
		log.error(cause);
		throw new CCFRuntimeException(cause);
	}

	public Transformer constructFileNameAndFetchTransformer(Document record)
			throws GenericArtifactParsingException {
		String fileName = null;
		Transformer transform = null;
		// this branch is only used if the xslt dir is set, otherwise, the xslt
		// file will be used
		if (!StringUtils.isEmpty(this.xsltDir)) {
			Element element = XPathUtils.getRootElement(record);
			String sourceSystemId = XPathUtils.getAttributeValue(element,
					SOURCE_SYSTEM_ID);
			String targetSystemId = XPathUtils.getAttributeValue(element,
					TARGET_SYSTEM_ID);
			String sourceRepositoryId = XPathUtils.getAttributeValue(element,
					SOURCE_REPOSITORY_ID);
			String targetRepositoryId = XPathUtils.getAttributeValue(element,
					TARGET_REPOSITORY_ID);
			sourceRepositoryId = CCFUtils.getTempFileName(sourceRepositoryId);
			targetRepositoryId = CCFUtils.getTempFileName(targetRepositoryId);
			String xsltDir = this.xsltDir;
			fileName = xsltDir + sourceSystemId + PARAM_DELIMITER
					+ sourceRepositoryId + PARAM_DELIMITER + targetSystemId
					+ PARAM_DELIMITER + targetRepositoryId + ".xsl";
		} else if (!StringUtils.isEmpty(this.xsltFile)) {
			fileName = this.xsltFile;
		}
		transform = xsltFileNameTransformerMap.get(fileName);
		if (transform == null) {
			transform = loadXSLT(new File(fileName), record.getRootElement());
			xsltFileNameTransformerMap.put(fileName, transform);
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
	@SuppressWarnings("unused")
	private Object[] transform(String s, Transformer transform, Element element) {
		return transform(createDOMFromString(s, element), transform, element);
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
	private Object[] transform(Document d, Transformer transform,
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
	private Document createDOMFromString(String xml, Element element) {
		try {
			return DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			String cause = "Failed to parse XML: " + e.getMessage();
			log.error(cause, e);
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_TRANSFORMER_TRANSFORMATION);
			throw new CCFRuntimeException(cause, e);
		}
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
}
