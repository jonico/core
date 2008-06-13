package com.collabnet.ccf.core;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;

import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * This component will find out whether an artifact coming out of the source
 * system has to be created, updated, deleted or ignored within the target system.
 * It will also try to find out the correct id of the artifact on the target system.
 * 
 * @author jnicolai
 * 
 */
public class EntityService implements
		IDataProcessor {
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(EntityService.class);

	/**
	 * Token used to indicate that the target repository item has to be created
	 */
	private String createToken;
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

	private JDBCReadConnector entityServiceReader = null;
	
	private JDBCReadConnector entityServiceMappingIdReader = null;

	/**
	 * openAdaptor Method to process all input and puts out the results This
	 * method will only handle Dom4J documents encoded in the generic XML schema
	 */
	public Object[] process(Object data) {
		if (data == null) {
			throw new NullRecordException(
					"Expected Document. Null record not permitted.");
		}

		if (!(data instanceof Document)) {
			throw new RecordFormatException("Expected Document. Got ["
					+ data.getClass().getName() + "]");
		}

		return processXMLDocument((Document) data);
	}

	/**
	 * Main method to handle the mapping and filtering of source artifacts to
	 * target repository artifact items
	 * 
	 * @param data
	 *            input XML document in generic XML artifact format
	 * @return array of generated XML documents compliant to generic XML
	 *         artifact schema
	 */
	private Object[] processXMLDocument(Document data) {
		try {
		Element element = XPathUtils.getRootElement(data);
		
		String sourceArtifactId = XPathUtils.getAttributeValue(element, SOURCE_ARTIFACT_ID);
		String sourceSystemId  = XPathUtils.getAttributeValue(element, SOURCE_SYSTEM_ID);
		String sourceSystemKind = XPathUtils.getAttributeValue(element, SOURCE_SYSTEM_KIND);
		String sourceRepositoryId = XPathUtils.getAttributeValue(element, SOURCE_REPOSITORY_ID);
		String sourceRepositoryKind = XPathUtils.getAttributeValue(element, SOURCE_REPOSITORY_KIND);
		
		String targetSystemId = XPathUtils.getAttributeValue(element, TARGET_SYSTEM_ID);
		String targetSystemKind = XPathUtils.getAttributeValue(element, TARGET_SYSTEM_KIND);
		String targetRepositoryId = XPathUtils.getAttributeValue(element, TARGET_REPOSITORY_ID);
		String targetRepositoryKind = XPathUtils.getAttributeValue(element, TARGET_REPOSITORY_KIND);
		
		log.info("The incoming artifact is*****"+data.asXML());
		
		if(sourceArtifactId.equalsIgnoreCase("Unknown")){
			return new Object[]{data};
		}
		String targetArtifactIdFromTable = lookupTargetArtifactId(sourceArtifactId, sourceSystemId, sourceRepositoryId, 
				targetSystemId, targetRepositoryId);
		log.info("The targetArtifactId in EntityService==="+targetArtifactIdFromTable);
		if(targetArtifactIdFromTable!=null && !(targetArtifactIdFromTable.equals("NEW")) && !(targetArtifactIdFromTable.equals("NULL"))) {
			XPathUtils.addAttribute(element, TARGET_ARTIFACT_ID, targetArtifactIdFromTable);
	    }
		if(targetArtifactIdFromTable==null) {
			XPathUtils.addAttribute(element, TARGET_ARTIFACT_ID, "NEW");
	    }
		}
		catch(GenericArtifactParsingException e) {
			log.error("There is some problem in extracting attributes from Document in EntityService!!!"+e);
		}
		
		log.debug("After manipulating dom4j document, new artifact is:"+data.asXML());
	    Object[] result = {data};
		return result;
	}
	
	
	private String lookupTargetArtifactId(String sourceArtifactId, String sourceSystemId, String sourceRepositoryId, 
			String targetSystemId, String targetRepositoryId) {
		String targetArtifactId = null;
		IOrderedMap inputParameters = new OrderedHashMap();
		
		inputParameters.add(sourceSystemId);
		inputParameters.add(sourceRepositoryId);
		inputParameters.add(targetSystemId);
		inputParameters.add(targetRepositoryId);
		inputParameters.add(sourceArtifactId);
	
		entityServiceReader.connect();
		Object[] resultSet = entityServiceReader.next(inputParameters, 1000);
		entityServiceReader.disconnect();
		
		if(resultSet == null || resultSet.length == 0){
			targetArtifactId = null;
		}
		else if(resultSet.length == 1){
			if(resultSet[0] instanceof OrderedHashMap){
				OrderedHashMap result = (OrderedHashMap) resultSet[0];
				if(result.size() == 1){
					targetArtifactId = result.get(0).toString();
				}
				else if(result.size() > 1){
					log.info("There are more than one target artifact ids returned from the table for "+sourceArtifactId);
				}
				else {
					targetArtifactId = null;
				}
			}
		}
		else {
			log.warn("There are more than one target artifact ids returned from the table for "+sourceArtifactId);
		}
		return targetArtifactId;
	}
	
	/**
	 * Reset the processor
	 */
	public void reset(Object context) {
	}

	@SuppressWarnings("unchecked")
	/**
	 * Validate whether all mandatory properties are set correctly
	 */
	public void validate(List exceptions) {
		
	}

	/**
	 * Set create token
	 * 
	 * @param createToken
	 *            see private attribute doc
	 */
	public void setCreateToken(String createToken) {
		this.createToken = createToken;
	}

	/**
	 * Get create token
	 * 
	 * @return see private attribute doc
	 */
	public String getCreateToken() {
		return createToken;
	}

	public JDBCReadConnector getEntityServiceReader() {
		return entityServiceReader;
	}

	public void setEntityServiceReader(JDBCReadConnector entityServiceReader) {
		this.entityServiceReader = entityServiceReader;
	}

	public JDBCReadConnector getEntityServiceMappingIdReader() {
		return entityServiceMappingIdReader;
	}

	public void setEntityServiceMappingIdReader(
			JDBCReadConnector entityServiceMappingIdReader) {
		this.entityServiceMappingIdReader = entityServiceMappingIdReader;
	}
}
