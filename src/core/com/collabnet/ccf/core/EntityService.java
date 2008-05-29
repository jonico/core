package com.collabnet.ccf.core;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.connector.jdbc.writer.JDBCWriteConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * This component will find out whether an artifact coming out of a non-SFEE
 * system has to be created, updated, deleted or ignored within SFEE. It will
 * also try to find out the correct SFEE id for the artifact in question.
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
	 * Token used to indicate that the SFEE tracker item has to be created
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

	/**
	 * User that was used in the non-SFEE system to store SFEE's tracker items.
	 * If an artifact was lastly modified by this user and not just created, it
	 * will be ignored to prevent endless update loops.
	 */
	//private String synchronizationUser;

	/**
	 * field name that is used within SFEE to store the artifact id used in the
	 * source (non-SFEE) system
	 */
	private String otherSystemInSFEETargetFieldname;
	
	private JDBCReadConnector entityServiceReader = null;
	
	private JDBCReadConnector entityServiceMappingIdReader = null;

	/**
	 * SFEE tracker handler instance
	 */
	

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
	 * Main method to handle the mapping and filtering of non-SFEE artifacts to
	 * SFEE tracker items
	 * 
	 * @param data
	 *            input XML document in generic XML artifact format
	 * @return array of generated XML documents compliant to generic XML
	 *         artifact schema
	 */
	private Object[] processXMLDocument(Document data) {
		Document filledArtifactDocument = null;
		/*GenericArtifact genericArtifact = null;
		try {
			genericArtifact = GenericArtifactHelper.createGenericArtifactJavaObject(data);
		}
		catch(Exception e) {
			System.out.println("GenericArtifact Parsing exception" + e);
		}*/
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
		String mappingId = lookupMappingId(sourceRepositoryId,
				sourceRepositoryKind,
				sourceSystemId,
				sourceSystemKind,
				targetRepositoryId,
				targetRepositoryKind,
				targetSystemId,
				targetSystemKind);
		String targetArtifactIdFromTable = lookupTargetArtifactId(sourceArtifactId,
				mappingId);
		
		if(targetArtifactIdFromTable!=null && !(targetArtifactIdFromTable.equals("NEW")) && !(targetArtifactIdFromTable.equals("NULL"))) {
	    	//genericArtifact.setTargetArtifactId(targetArtifactIdFromTable);
			XPathUtils.addAttribute(element, TARGET_ARTIFACT_ID, targetArtifactIdFromTable);
	    }
		if(targetArtifactIdFromTable==null) {
   			//genericArtifact.setTargetArtifactId("NEW");
			XPathUtils.addAttribute(element, TARGET_ARTIFACT_ID, "NEW");
	    }
		}
		catch(GenericArtifactParsingException e) {
			log.error("There is some problem in extracting attributes from Document in EntityService!!!"+e);
		}
		
	    /*try {
	    	filledArtifactDocument = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
	    }
	    catch(Exception e) {
	    	log.error("Exception while converting the resultantGenericArtifact into the resultDocument in QCEntityService:"+e);
	    	throw new RuntimeException(e);
	    }*/
		log.info("After manipulating dom4j document, new artifact is:"+data.asXML());
	    Object[] result = {data};
		return result;
	}
	
	private String lookupMappingId(String sourceRepositoryId, String sourceRepositoryKind,
			String sourceSystemId, String sourceSystemKind,
			String targetRepositoryId, String targetRepositoryKind,
			String targetSystemId, String targetSystemKind){
		String mappingId = null;
		IOrderedMap inputParameters = new OrderedHashMap();
		inputParameters.add(sourceRepositoryId);
		inputParameters.add(sourceRepositoryKind);
		inputParameters.add(sourceSystemId);
		inputParameters.add(sourceSystemKind);
		inputParameters.add(targetRepositoryId);
		inputParameters.add(targetRepositoryKind);
		inputParameters.add(targetSystemId);
		inputParameters.add(targetSystemKind);
		
		entityServiceMappingIdReader.connect();
		Object[] resultSet = entityServiceMappingIdReader.next(inputParameters, 1000);
		entityServiceMappingIdReader.disconnect();
		
		if(resultSet == null || resultSet.length == 0){
			mappingId = null;
		}
		else if(resultSet.length == 1){
			if(resultSet[0] instanceof OrderedHashMap){
				OrderedHashMap result = (OrderedHashMap) resultSet[0];
				if(result.size() == 1){
					mappingId = result.get(0).toString();
				}
				else if(result.size() > 1){
					log.warn("There are more than one mapping ids returned from the tables"
							+" for source repository "+sourceRepositoryId
							+" and target repository "+targetRepositoryId);
				}
				else {
					mappingId = null;
				}
			}
		}
		else {
			log.warn("There are more than one mapping ids returned from the tables"
					+" for source repository "+sourceRepositoryId
					+" and target repository "+targetRepositoryId);
		}
		
		return mappingId;
	}

	private String lookupTargetArtifactId(String sourceArtifactId,
			String mappingId) {
		String targetArtifactId = null;
		IOrderedMap inputParameters = new OrderedHashMap();
		
		inputParameters.add(sourceArtifactId);
		inputParameters.add(mappingId);

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
					log.warn("There are more than one target artifact ids returned from the table for "+sourceArtifactId);
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
	 * Set otherSystemInSFEETargetFieldName
	 * 
	 * @param otherSystemInSFEETargetFieldName
	 *            see private attribute doc
	 */
	public void setOtherSystemInSFEETargetFieldname(String otherSystemInSFEETargetFieldName) {
		this.otherSystemInSFEETargetFieldname = otherSystemInSFEETargetFieldName;
	}

	/**
	 * Get otherSystemInSFEETargetFieldname
	 * 
	 * @return see private attribute doc
	 */
	public String getOtherSystemInSFEETargetFieldname() {
		return otherSystemInSFEETargetFieldname;
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
