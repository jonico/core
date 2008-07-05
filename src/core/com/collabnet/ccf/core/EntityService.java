package com.collabnet.ccf.core;

import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
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
public class EntityService extends LifecycleComponent implements
		IDataProcessor  {
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(EntityService.class);

	/**
	 * Token used to indicate that the target repository item has to be created
	 */
	private String createToken = "NEW";
	
	private JDBCReadConnector identityMappingDatabaseReader = null;
	

	/**
	 * openAdaptor Method to process all input and puts out the results This
	 * method will only handle Dom4J documents encoded in the generic XML schema
	 */
	public Object[] process(Object data) {
		if (data == null) {
			String cause = "Expected Document. Null record not permitted.";
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		if (!(data instanceof Document)) {
			String cause = "Expected Document. Got ["+ data.getClass().getName() + "]";
			log.error(cause);
			throw new CCFRuntimeException(cause);
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
		Element element = null;
		try {
		element = XPathUtils.getRootElement(data);
		
		String artifactType = XPathUtils.getAttributeValue(element, GenericArtifactHelper.ARTIFACT_TYPE);
		String sourceArtifactId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_ARTIFACT_ID);
		String sourceSystemId  = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_SYSTEM_ID);
		String sourceSystemKind = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_SYSTEM_KIND);
		String sourceRepositoryId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_REPOSITORY_ID);
		String sourceRepositoryKind = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_REPOSITORY_KIND);
		
		String targetSystemId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_SYSTEM_ID);
		String targetSystemKind = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_SYSTEM_KIND);
		String targetRepositoryId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_REPOSITORY_ID);
		String targetRepositoryKind = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_REPOSITORY_KIND);
		
		if(sourceArtifactId.equalsIgnoreCase("Unknown")){
			return new Object[]{data};
		}
		String targetArtifactIdFromTable = lookupTargetArtifactId(sourceArtifactId, sourceSystemId, sourceRepositoryId, 
				targetSystemId, targetRepositoryId, artifactType);
		if(artifactType.equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)){
			String sourceParentArtifactId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.DEP_PARENT_SOURCE_ARTIFACT_ID);
			String sourceParentRepositoryId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.DEP_PARENT_SOURCE_REPOSITORY_ID);
			String targetParentRepositoryId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.DEP_PARENT_TARGET_REPOSITORY_ID);
			String targetParentArtifactId = lookupTargetArtifactId(sourceParentArtifactId, sourceSystemId, sourceParentRepositoryId, 
					targetSystemId, targetParentRepositoryId, GenericArtifactHelper.ARTIFACT_TYPE_PLAIN_ARTIFACT);
			if(StringUtils.isEmpty(targetParentArtifactId)){
				String cause = "Parent artifact "+sourceParentArtifactId+" for attachment "+
									sourceArtifactId +" is not created on the target";
				Throwable e = new CCFRuntimeException(cause);
				log.error(cause, e);
			}
			else {
				XPathUtils.addAttribute(element, GenericArtifactHelper.DEP_PARENT_TARGET_ARTIFACT_ID,
						targetParentArtifactId);
			}
		}
		
		if(targetArtifactIdFromTable!=null && !(targetArtifactIdFromTable.equals(createToken)) && !(targetArtifactIdFromTable.equals("NULL"))) {
			XPathUtils.addAttribute(element, GenericArtifactHelper.TARGET_ARTIFACT_ID, targetArtifactIdFromTable);
	    }
		if(targetArtifactIdFromTable==null) {
			XPathUtils.addAttribute(element, GenericArtifactHelper.TARGET_ARTIFACT_ID, createToken);
	    }
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the Document to extract specific attributes";
			log.error(cause, e);
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE, GenericArtifact.GENERIC_ARTIFACT_PARSING_ERROR);
			throw new CCFRuntimeException(cause, e);
		}
		
	    Object[] result = {data};
		return result;
	}
	
	
	private String lookupTargetArtifactId(String sourceArtifactId, String sourceSystemId, String sourceRepositoryId, 
			String targetSystemId, String targetRepositoryId, String artifactType) {
		String targetArtifactId = null;
		IOrderedMap inputParameters = new OrderedHashMap();
		
		inputParameters.add(sourceSystemId);
		inputParameters.add(sourceRepositoryId);
		inputParameters.add(targetSystemId);
		inputParameters.add(targetRepositoryId);
		inputParameters.add(sourceArtifactId);
		inputParameters.add(artifactType);
		identityMappingDatabaseReader.disconnect();
		identityMappingDatabaseReader.connect();
		Object[] resultSet = identityMappingDatabaseReader.next(inputParameters, 1000);
		identityMappingDatabaseReader.disconnect();
		
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
		if (getIdentityMappingDatabaseReader() == null) {
			log.error("identityMappingDatabaseReader-property not set");
			exceptions.add(new ValidationException(
					"identityMappingDatabaseReader-property not set", this));
		}
	}

	public JDBCReadConnector getIdentityMappingDatabaseReader() {
		return identityMappingDatabaseReader;
	}

	public void setIdentityMappingDatabaseReader(JDBCReadConnector identityMappingDatabaseReader) {
		this.identityMappingDatabaseReader = identityMappingDatabaseReader;
	}

}
