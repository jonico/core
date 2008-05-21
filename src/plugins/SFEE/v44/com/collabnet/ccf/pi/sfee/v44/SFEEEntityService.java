package com.collabnet.ccf.pi.sfee.v44;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.connector.jdbc.writer.JDBCWriteConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;


/**
 * This component will find out whether an artifact coming out of a non-SFEE
 * system has to be created, updated, deleted or ignored within SFEE. It will
 * also try to find out the correct SFEE id for the artifact in question.
 * 
 * @author jnicolai
 * 
 */
public class SFEEEntityService /*extends SFEEConnectHelper*/ implements
		IDataProcessor {
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(SFEEEntityService.class);

	/**
	 * Token used to indicate that the SFEE tracker item has to be created
	 */
	private String createToken;

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
	
	private JDBCWriteConnector entityServiceWriteConnector = null;

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
		GenericArtifact genericArtifact = new GenericArtifact();
		try {
			genericArtifact = GenericArtifactHelper.createGenericArtifactJavaObject(data);
		}
		catch(Exception e) {
			System.out.println("GenericArtifact Parsing exception" + e);
		}
		String sourceArtifactId = genericArtifact.getSourceArtifactId();
		String sourceSystemId  = genericArtifact.getSourceSystemId();
		String sourceSystemKind = genericArtifact.getSourceSystemKind();
		String sourceRepositoryId = genericArtifact.getSourceRepositoryId();
		String sourceRepositoryKind = genericArtifact.getSourceRepositoryKind();
		
		//String targetArtifactId = genericArtifact.getTargetArtifactId();
		String targetSystemId = genericArtifact.getTargetSystemId();
		String targetSystemKind = genericArtifact.getTargetSystemKind();
		String targetRepositoryId = genericArtifact.getTargetRepositoryId();
		String targetRepositoryKind = genericArtifact.getTargetRepositoryKind();
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
	    	genericArtifact.setTargetArtifactId(targetArtifactIdFromTable);
	    }
		if(targetArtifactIdFromTable==null) {
			// TODO Review the logic here
//	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.UPDATE)) {
//	    		//Send this artifact to HOSPITAL
//	    	}
//	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.CREATE)) {
	    		Boolean insertStatus = true;
	    		this.createMapping(mappingId, sourceArtifactId, "NEW");
	    		if(insertStatus){
	    			log.debug("Artifact inserted into the mapping table");
	    		}
	    		else{
	    			log.warn("Artifact insertion failed...!");
	    		}
//	    	}
	    }
		
	    try {
	    	filledArtifactDocument = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
	    }
	    catch(Exception e) {
	    	log.error("Exception while converting the resultantGenericArtifact into the resultDocument in QCEntityService:"+e);
	    	throw new RuntimeException(e);
	    }
		
	    Object[] result = {filledArtifactDocument};
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
	
	private void createMapping(String mappingId, String sourceArtifactId, String targetArtifactId){
		IOrderedMap inputParameters = new OrderedHashMap();
		
		inputParameters.add(0,"MAPPING_ID",mappingId);
		inputParameters.add(1,"SOURCE_ARTIFACT_ID",sourceArtifactId);
		inputParameters.add(2,"TARGET_ARTIFACT_ID",targetArtifactId);
		IOrderedMap[] data = new IOrderedMap[]{inputParameters};
		entityServiceWriteConnector.connect();
		entityServiceWriteConnector.deliver(data);
		entityServiceWriteConnector.disconnect();
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

	public JDBCWriteConnector getEntityServiceWriteConnector() {
		return entityServiceWriteConnector;
	}

	public void setEntityServiceWriteConnector(
			JDBCWriteConnector entityServiceWriteConnector) {
		this.entityServiceWriteConnector = entityServiceWriteConnector;
	}
}
