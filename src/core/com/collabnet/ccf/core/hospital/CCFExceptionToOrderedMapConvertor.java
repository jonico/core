package com.collabnet.ccf.core.hospital;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.auxil.convertor.exception.ExceptionToOrderedMapConvertor;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.core.IComponent;
import org.openadaptor.core.exception.MessageException;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * This class extends the openAdaptor exception convertor to add further CCF
 * specific information about an error. If an exception is not a
 * CCFRunTimeException, it will exactly behave like its superclass
 * 
 * @author jnicolai
 * 
 */
public class CCFExceptionToOrderedMapConvertor extends
		ExceptionToOrderedMapConvertor {
	private static final String ERROR_CODE = "ERROR_CODE";
	private static final String GENERICARTIFACT = "GENERIC_ARTIFACT";
	private static final String TARGET_ARTIFACT_ID = "TARGET_ARTIFACT_ID";
	private static final String SOURCE_ARTIFACT_ID = "SOURCE_ARTIFACT_ID";
	private static final String TARGET_REPOSITORY_KIND = "TARGET_REPOSITORY_KIND";
	private static final String TARGET_SYSTEM_KIND = "TARGET_SYSTEM_KIND";
	private static final String SOURCE_REPOSITORY_KIND = "SOURCE_REPOSITORY_KIND";
	private static final String SOURCE_SYSTEM_KIND = "SOURCE_SYSTEM_KIND";
	private static final String TARGET_REPOSITORY_ID = "TARGET_REPOSITORY_ID";
	private static final String TARGET_SYSTEM_ID = "TARGET_SYSTEM_ID";
	private static final String SOURCE_REPOSITORY_ID = "SOURCE_REPOSITORY_ID";
	private static final String SOURCE_SYSTEM_ID = "SOURCE_SYSTEM_ID";
	private static final String ARTIFACT_TYPE = "ARTIFACT_TYPE";
	private static final String TARGET_ARTIFACT_VERSION = "TARGET_ARTIFACT_VERSION";
	private static final String SOURCE_ARTIFACT_VERSION = "SOURCE_ARTIFACT_VERSION";
	private static final String TARGET_LAST_MODIFICATION_TIME = "TARGET_LAST_MODIFICATION_TIME";
	private static final String SOURCE_LAST_MODIFICATION_TIME = "SOURCE_LAST_MODIFICATION_TIME";

	static final String EXCEPTION_MESSAGE = "EXCEPTION_MESSAGE";

	static final String CAUSE_EXCEPTION_CLASS = "CAUSE_EXCEPTION_CLASS_NAME";

	static final String CAUSE_EXCEPTION_MESSAGE = "CAUSE_EXCEPTION_MESSAGE";

	static final String STACK_TRACE = "STACK_TRACE";

	static final String ADAPTOR_NAME = "ADAPTOR_NAME";

	// static final String THREAD_NAME = "THREAD_NAME";

	static final String DATA_TYPE = "DATA_TYPE";

	/*
	 * Field names, initialised to defaults These field names have been copied
	 * from openAdaptor since they are not supported any more in newer versions
	 */

	/* Optional property allowing to retrieve an adaptor's name */
	private IComponent adaptor;

	private String exceptionMessageColName = EXCEPTION_MESSAGE;

	private String causeExceptionClassColName = CAUSE_EXCEPTION_CLASS;

	private String causeExceptionMessageColName = CAUSE_EXCEPTION_MESSAGE;

	private String stackTraceColName = STACK_TRACE;

	private String adaptorColName = ADAPTOR_NAME;

	private String dataTypeColName = DATA_TYPE;

	// private String threadNameColName = THREAD_NAME;

	private static final String NO_CAUSE_EXCEPTION = "No cause exception detected.";
	private static final Log log = LogFactory
			.getLog(CCFExceptionToOrderedMapConvertor.class);

	/**
	 * Converts the <code>record</code> into an <code>IOrderedMap</code> .
	 * 
	 * @param record
	 *            Object which should be a MessageException instance
	 * @return an IOrderedMap representation of the MessageException contents
	 */
	@SuppressWarnings("unchecked")
	protected Object convert(Object record) {
		log.warn("Artifact reached ambulance");
		// first of all we pass the record in our parent method
		Object preprocessedMap = super.convert(record);
		if (preprocessedMap == null
				|| (!(preprocessedMap instanceof IOrderedMap))) {
			return preprocessedMap;
		}
		IOrderedMap map = (IOrderedMap) preprocessedMap;

		MessageException messageException = (MessageException) record;
		map.put(exceptionMessageColName, messageException.getException()
				.getMessage());

		Throwable cause = messageException.getException().getCause();
		if (cause != null) {
			map.put(causeExceptionClassColName, cause.getClass().getName());
			map.put(causeExceptionMessageColName, cause.getMessage());
		} else {
			map.put(causeExceptionClassColName, NO_CAUSE_EXCEPTION);
			map.put(causeExceptionMessageColName, NO_CAUSE_EXCEPTION);
		}

		Exception exception = messageException.getException();
		StringBuffer stackTraceBuf = new StringBuffer();
		StackTraceElement[] stackTrace = exception.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			stackTraceBuf.append(stackTrace[i]);
			stackTraceBuf.append("\n");
		}
		/* Append cause exception stack trace */
		if (cause != null) {
			stackTraceBuf.append("\n\n");
			stackTrace = cause.getStackTrace();
			for (int i = 0; i < stackTrace.length; i++) {
				stackTraceBuf.append(stackTrace[i]);
				stackTraceBuf.append("\n");
			}
		}
		map.put(stackTraceColName, stackTraceBuf.toString());

		String adaptorName = null == adaptor ? "Unknown" : adaptor.getId();
		map.put(adaptorColName, adaptorName);

		Object data = messageException.getData();
		String dataType = null;
		if (data != null) {
			dataType = data.getClass().getName();
		}
		map.put(dataTypeColName, dataType);

		if (data instanceof Document) {
			Document dataDoc = (Document) data;
			Element element = dataDoc.getRootElement();
			try {
				String sourceArtifactId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.SOURCE_ARTIFACT_ID);
				String sourceSystemId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.SOURCE_SYSTEM_ID);
				String sourceSystemKind = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.SOURCE_SYSTEM_KIND);
				String sourceRepositoryId = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.SOURCE_REPOSITORY_ID);
				String sourceRepositoryKind = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.SOURCE_REPOSITORY_KIND);

				String targetArtifactId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TARGET_ARTIFACT_ID);
				String targetSystemId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TARGET_SYSTEM_ID);
				String targetSystemKind = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TARGET_SYSTEM_KIND);
				String targetRepositoryId = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.TARGET_REPOSITORY_ID);
				String targetRepositoryKind = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.TARGET_REPOSITORY_KIND);
				
				String artifactErrorCode = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.ERROR_CODE);

				String sourceArtifactLastModifiedDateString = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_ARTIFACT_LAST_MODIFICATION_DATE);
				String targetArtifactLastModifiedDateString = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_ARTIFACT_LAST_MODIFICATION_DATE);
				String sourceArtifactVersion = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_ARTIFACT_VERSION);
				String targetArtifactVersion = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_ARTIFACT_VERSION);
				
				String artifactType = XPathUtils.getAttributeValue(element, GenericArtifactHelper.ARTIFACT_TYPE);
				
				java.util.Date sourceLastModifiedDate = null;
				if(!sourceArtifactLastModifiedDateString.equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)){
					sourceLastModifiedDate = DateUtil.parse(sourceArtifactLastModifiedDateString);
				}
				java.sql.Timestamp sourceTime = sourceLastModifiedDate == null?null:new java.sql.Timestamp(sourceLastModifiedDate.getTime());
				
				java.util.Date targetLastModifiedDate = null;
				if(!targetArtifactLastModifiedDateString.equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)){
					targetLastModifiedDate = DateUtil.parse(targetArtifactLastModifiedDateString);
				}
				java.sql.Timestamp targetTime = targetLastModifiedDate == null?null:new java.sql.Timestamp(targetLastModifiedDate.getTime());

				// TODO Should we allow to set different column names for these
				// properties?
				map.put(SOURCE_SYSTEM_ID, sourceSystemId);
				map.put(SOURCE_REPOSITORY_ID, sourceRepositoryId);
				map.put(TARGET_SYSTEM_ID, targetSystemId);
				map.put(TARGET_REPOSITORY_ID, targetRepositoryId);
				map.put(SOURCE_SYSTEM_KIND, sourceSystemKind);
				map.put(SOURCE_REPOSITORY_KIND, sourceRepositoryKind);
				map.put(TARGET_SYSTEM_KIND, targetSystemKind);
				map.put(TARGET_REPOSITORY_KIND, targetRepositoryKind);
				map.put(SOURCE_ARTIFACT_ID, sourceArtifactId);
				map.put(TARGET_ARTIFACT_ID, targetArtifactId);
				map.put(ERROR_CODE, artifactErrorCode);
				map.put(SOURCE_LAST_MODIFICATION_TIME,sourceTime);
				map.put(TARGET_LAST_MODIFICATION_TIME,targetTime);
				map.put(SOURCE_ARTIFACT_VERSION,sourceArtifactVersion);
				map.put(TARGET_ARTIFACT_VERSION,targetArtifactVersion);
				map.put(ARTIFACT_TYPE,artifactType);
				map.put(GENERICARTIFACT, dataDoc.asXML());
			} catch (GenericArtifactParsingException e) {
				log
						.warn("The data that reached the hospital is not a valid Generic Artifact");
				map.put(SOURCE_SYSTEM_ID, null);
				map.put(SOURCE_REPOSITORY_ID, null);
				map.put(TARGET_SYSTEM_ID, null);
				map.put(TARGET_REPOSITORY_ID, null);
				map.put(SOURCE_SYSTEM_KIND, null);
				map.put(SOURCE_REPOSITORY_KIND, null);
				map.put(TARGET_SYSTEM_KIND, null);
				map.put(TARGET_REPOSITORY_KIND, null);
				map.put(SOURCE_ARTIFACT_ID, null);
				map.put(TARGET_ARTIFACT_ID, null);
				map.put(GENERICARTIFACT, null);
				map.put(ERROR_CODE, null);
				map.put(SOURCE_LAST_MODIFICATION_TIME,null);
				map.put(TARGET_LAST_MODIFICATION_TIME,null);
				map.put(SOURCE_ARTIFACT_VERSION,null);
				map.put(TARGET_ARTIFACT_VERSION,null);
				map.put(ARTIFACT_TYPE,null);
			}
		} else {
			map.put(SOURCE_SYSTEM_ID, null);
			map.put(SOURCE_REPOSITORY_ID, null);
			map.put(TARGET_SYSTEM_ID, null);
			map.put(TARGET_REPOSITORY_ID, null);
			map.put(SOURCE_SYSTEM_KIND, null);
			map.put(SOURCE_REPOSITORY_KIND, null);
			map.put(TARGET_SYSTEM_KIND, null);
			map.put(TARGET_REPOSITORY_KIND, null);
			map.put(SOURCE_ARTIFACT_ID, null);
			map.put(TARGET_ARTIFACT_ID, null);
			map.put(GENERICARTIFACT, null);
			map.put(ERROR_CODE, null);
			map.put(SOURCE_LAST_MODIFICATION_TIME,null);
			map.put(TARGET_LAST_MODIFICATION_TIME,null);
			map.put(SOURCE_ARTIFACT_VERSION,null);
			map.put(TARGET_ARTIFACT_VERSION,null);
			map.put(ARTIFACT_TYPE,null);
		}
		return map;
	}

	/**
	 * Optional property that allows to retrieve the name of the adaptor.
	 * 
	 * @param adaptor
	 */
	public void setAdaptor(IComponent adaptor) {
		this.adaptor = adaptor;
	}
}
