package com.collabnet.ccf.core;

import java.sql.Timestamp;
import java.util.Date;
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
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * This component will find out whether an artifact coming out of the source
 * system has to be created, updated, deleted or ignored within the target system.
 * It will also try to find out the correct id of the artifact on the target system.
 * 
 * It checks the version of the source artifact to see if that version of the artifact already
 * shipped there by avoiding duplicate artifact shipment.
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
			String sourceRepositoryId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_REPOSITORY_ID);
			String targetSystemId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_SYSTEM_ID);
			String targetRepositoryId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_REPOSITORY_ID);
			String sourceArtifactVersion = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_ARTIFACT_VERSION);
			String sourceArtifactLastModifiedDateStr = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_ARTIFACT_LAST_MODIFICATION_DATE);
			Date sourceArtifactLastModifiedDate = DateUtil.parse(sourceArtifactLastModifiedDateStr);
			// FIXME Artifact version is a string in table. If there is a system that uses some other
			// Notations than numbers for version, how will this succeed?
			int sourceArtifactVersionInt = Integer.parseInt(sourceArtifactVersion);
			String targetArtifactIdFromTable = null;
			if(sourceArtifactId.equalsIgnoreCase("Unknown")){
				return new Object[]{data};
			}
			Object[] results = lookupTargetArtifactId(sourceArtifactId, sourceSystemId, sourceRepositoryId, 
					targetSystemId, targetRepositoryId, artifactType);
			if(results!=null && results.length!=0) {
				if(results[0]!=null){
					targetArtifactIdFromTable = results[0].toString();
				}
				else {
					targetArtifactIdFromTable = null;
				}
				Date sourceArtifactLastModifiedDateFromTable = null;
				if(results.length >= 2 && results[1]!=null) {
					sourceArtifactLastModifiedDateFromTable = (Date) results[1];
				}
				else {
					sourceArtifactLastModifiedDateFromTable = null;
				}
				String sourceArtifactVersionFromTable = null;
				if(results.length >= 3 && results[2]!=null){
					sourceArtifactVersionFromTable = results[2].toString();
				}
				else {
					sourceArtifactVersionFromTable = null;
				}
				
				if(sourceArtifactLastModifiedDateFromTable != null && sourceArtifactVersionFromTable != null){
					int sourceArtifactVersionIntFromTable = Integer.parseInt(sourceArtifactVersionFromTable);
					if(sourceArtifactLastModifiedDateFromTable.equals(sourceArtifactLastModifiedDate) &&
							sourceArtifactVersionIntFromTable >= sourceArtifactVersionInt)
					{
						log.warn("Seems the artifact has already been shipped. Duplicately shipping "
								+ sourceArtifactId + " at " + sourceArtifactVersion);
						return null;
					}
				}
			}
			if(artifactType.equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)){
				String sourceParentArtifactId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.DEP_PARENT_SOURCE_ARTIFACT_ID);
				String sourceParentRepositoryId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.DEP_PARENT_SOURCE_REPOSITORY_ID);
				String targetParentRepositoryId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.DEP_PARENT_TARGET_REPOSITORY_ID);
				Object[] resultsDep = lookupTargetArtifactId(sourceParentArtifactId, sourceSystemId, sourceParentRepositoryId, 
						targetSystemId, targetParentRepositoryId, GenericArtifactHelper.ARTIFACT_TYPE_PLAIN_ARTIFACT);
				String targetParentArtifactId =  resultsDep[0].toString();
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
			
			if(targetArtifactIdFromTable != null) {
				XPathUtils.addAttribute(element, GenericArtifactHelper.TARGET_ARTIFACT_ID, targetArtifactIdFromTable);
				XPathUtils.addAttribute(element, GenericArtifactHelper.ARTIFACT_ACTION,	GenericArtifactHelper.ARTIFACT_ACTION_UPDATE);
		    }
			else {
				XPathUtils.addAttribute(element, GenericArtifactHelper.ARTIFACT_ACTION,	GenericArtifactHelper.ARTIFACT_ACTION_CREATE);
		    }
		}
		catch(GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the Document to extract specific attributes";
			log.error(cause, e);
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE, GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		
	    Object[] result = {data};
		return result;
	}
	
	
	/**
	 * For a given source artifact id, source repository and the target repository details,
	 * this method finds out the target artifact id mapped to the source artifact id.
	 * 
	 * If the source artifact id had ever passed through the wiring the identity mapping will
	 * contain the corresponding target artifact id. This method fetches the target artifact id 
	 * and returns. If there is no target artifact id mapped to this source artifact id this
	 * method return a null.
	 * 
	 * @param sourceArtifactId - The source artifact id that should be looked up for a target artifact id
	 * @param sourceSystemId - The system id of the source repository
	 * @param sourceRepositoryId - The repository id of the source artifact
	 * @param targetSystemId - The system id of the target repository
	 * @param targetRepositoryId - The repository id of the target artifact
	 * @param artifactType - The artifact type
	 * 
	 * @return the target artifact id for the source artifact id
	 */
	private Object[] lookupTargetArtifactId(String sourceArtifactId, String sourceSystemId, String sourceRepositoryId, 
			String targetSystemId, String targetRepositoryId, String artifactType) {
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
		Object[] results = null;
		if(resultSet == null || resultSet.length == 0){
			log.info(sourceArtifactId + "-" + sourceRepositoryId + "-"+sourceSystemId
					+ targetRepositoryId + "-" + targetSystemId + " are not mapped.");
		}
		else if(resultSet.length == 1){
			results = new Object[3];
			if(resultSet[0] instanceof OrderedHashMap){
				OrderedHashMap result = (OrderedHashMap) resultSet[0];
				if(result.size() == 3){
					results[0] = result.get(0).toString();
					Timestamp timeStamp = (Timestamp) result.get(1);
					Date date = new Date(timeStamp.getTime());
					results[1] = date;
					results[2] = result.get(2).toString();
				}
				else if(result.size() > 1){
					log.info("There are more than one target artifact ids returned from the table for "+sourceArtifactId);
				}
			}
		}
		else {
			log.warn("There are more than one target artifact ids returned from the table for "+sourceArtifactId);
		}
		return results;
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
