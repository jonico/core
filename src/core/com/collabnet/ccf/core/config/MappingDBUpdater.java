package com.collabnet.ccf.core.config;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.connector.jdbc.writer.JDBCWriteConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.XPathUtils;

public class MappingDBUpdater extends LifecycleComponent implements IDataProcessor{
	private static final Log log = LogFactory.getLog(MappingDBUpdater.class);
	private JDBCWriteConnector synchronizationStatusDatabaseUpdater = null;
	private JDBCReadConnector identityMappingDatabaseReader = null;
	private JDBCWriteConnector identityMappingDatabaseUpdater = null;
	
	private JDBCWriteConnector identityMappingDatabaseInserter = null;
	private static final String NULL_VALUE = null;
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
	private static final String SOURCE_LAST_READ_TIME = "sourceArtifactLastModifiedDate";
	private static final String TARGET_LAST_READ_TIME = "targetArtifactLastModifiedDate";
	private static final String SOURCE_ARTIFACT_VERSION = "sourceArtifactVersion";
	private static final String TARGET_ARTIFACT_VERSION = "targetArtifactVersion";
	private static final String ARTIFACT_TYPE = "artifactType";
	private static final String ARTIFACT_TYPE_ATTACHMENT = "attachment";
	private static final String DEP_PARENT_SOURCE_ARTIFACT_ID = "depParentSourceArtifactId";
	private static final String DEP_PARENT_SOURCE_REPOSITORY_ID = "depParentSourceRepositoryId";
	private static final String DEP_PARENT_SOURCE_REPOSITORY_KIND = "depParentSourceRepositoryKind";
	private static final String DEP_PARENT_TARGET_ARTIFACT_ID = "depParentTargetArtifactId";;
	private static final String DEP_PARENT_TARGET_REPOSITORY_ID = "depParentTargetRepositoryId";;
	private static final String DEP_PARENT_TARGET_REPOSITORY_KIND = "depParentTargetRepositoryKind";;
	
	public Object[] process(Object data) {
		// I will expect a Generic Artifact object
		if(data instanceof Document){
			
			String depParentSourceArtifactId = null;
			String depParentSourceRepositoryId = null;
			String depParentSourceRepositoryKind = null;
			String depParentTargetArtifactId = null;
			String depParentTargetRepositoryId = null;
			String depParentTargetRepositoryKind = null;
			
			try {
			Element element = XPathUtils.getRootElement((Document)data);
			
			String artifactAction = XPathUtils.getAttributeValue(element, GenericArtifactHelper.ARTIFACT_ACTION);
			if(artifactAction.equals(GenericArtifactHelper.ARTIFACT_ACTION_IGNORE)){
				return new Object[]{data};
			}
			
			String sourceArtifactId = XPathUtils.getAttributeValue(element, SOURCE_ARTIFACT_ID);
			String sourceSystemId  = XPathUtils.getAttributeValue(element, SOURCE_SYSTEM_ID);
			String sourceSystemKind = XPathUtils.getAttributeValue(element, SOURCE_SYSTEM_KIND);
			String sourceRepositoryId = XPathUtils.getAttributeValue(element, SOURCE_REPOSITORY_ID);
			String sourceRepositoryKind = XPathUtils.getAttributeValue(element, SOURCE_REPOSITORY_KIND);
			
			String targetArtifactId = XPathUtils.getAttributeValue(element, TARGET_ARTIFACT_ID);
			String targetSystemId = XPathUtils.getAttributeValue(element, TARGET_SYSTEM_ID);
			String targetSystemKind = XPathUtils.getAttributeValue(element, TARGET_SYSTEM_KIND);
			String targetRepositoryId = XPathUtils.getAttributeValue(element, TARGET_REPOSITORY_ID);
			String targetRepositoryKind = XPathUtils.getAttributeValue(element, TARGET_REPOSITORY_KIND);
			
			String sourceArtifactLastModifiedDateString = XPathUtils.getAttributeValue(element, SOURCE_LAST_READ_TIME);
			String targetArtifactLastModifiedDateString = XPathUtils.getAttributeValue(element, TARGET_LAST_READ_TIME);
			String sourceArtifactVersion = XPathUtils.getAttributeValue(element, SOURCE_ARTIFACT_VERSION);
			String targetArtifactVersion = XPathUtils.getAttributeValue(element, TARGET_ARTIFACT_VERSION);
			
			// this is necessary to get around the duplicate detection mechanism in case of initial resyncs
			if (artifactAction.equals(GenericArtifactHelper.ARTIFACT_ACTION_CREATE)) {
				targetArtifactVersion = GenericArtifactHelper.ARTIFACT_VERSION_FORCE_RESYNC;
			}
			
			String artifactType = XPathUtils.getAttributeValue(element, ARTIFACT_TYPE);
			//String lastReadTransactionId = XPathUtils.getAttributeValue(element, TRANSACTION_ID);
			
			if(artifactType.equals(ARTIFACT_TYPE_ATTACHMENT)) {
				depParentSourceArtifactId = XPathUtils.getAttributeValue(element, DEP_PARENT_SOURCE_ARTIFACT_ID);
				depParentSourceRepositoryId = XPathUtils.getAttributeValue(element, DEP_PARENT_SOURCE_REPOSITORY_ID);
				depParentSourceRepositoryKind = XPathUtils.getAttributeValue(element, DEP_PARENT_SOURCE_REPOSITORY_KIND);
				depParentTargetArtifactId = XPathUtils.getAttributeValue(element, DEP_PARENT_TARGET_ARTIFACT_ID);
				depParentTargetRepositoryId = XPathUtils.getAttributeValue(element, DEP_PARENT_TARGET_REPOSITORY_ID);
				depParentTargetRepositoryKind = XPathUtils.getAttributeValue(element, DEP_PARENT_TARGET_REPOSITORY_KIND);
			}
			
			java.util.Date sourceLastModifiedDate = null;
			if(sourceArtifactLastModifiedDateString.equalsIgnoreCase("Unknown")){
				return new Object[]{};
			}
			else {
				sourceLastModifiedDate = DateUtil.parse(sourceArtifactLastModifiedDateString);
			}
			java.sql.Timestamp sourceTime = new java.sql.Timestamp(sourceLastModifiedDate.getTime());
			
			java.util.Date targetLastModifiedDate = null;
			
			if(targetArtifactLastModifiedDateString.equalsIgnoreCase("Unknown")){
				return new Object[]{};
			}
			else {
				//targetArtifactLastModifiedDateString = "June 26, 2008 11:02:26 AM GMT+05:30";
				targetLastModifiedDate = DateUtil.parse(targetArtifactLastModifiedDateString);
			}
			
			java.sql.Timestamp targetTime = new java.sql.Timestamp(targetLastModifiedDate.getTime());
			
			createMapping(sourceArtifactId,
					sourceRepositoryId,
					sourceRepositoryKind,
					sourceSystemId,
					sourceSystemKind,
					targetArtifactId,
					targetRepositoryId,
					targetRepositoryKind,
					targetSystemId,
					targetSystemKind,
					sourceTime,
					sourceArtifactVersion,
					targetTime,
					targetArtifactVersion,
					artifactType,
					depParentSourceArtifactId,
					depParentSourceRepositoryId,
					depParentSourceRepositoryKind,
					depParentTargetArtifactId,
					depParentTargetRepositoryId,
					depParentTargetRepositoryKind,
					NULL_VALUE,
					NULL_VALUE,
					NULL_VALUE,
					NULL_VALUE,
					NULL_VALUE,
					NULL_VALUE);
			// we also have to create the opposite mapping,
			createMapping(targetArtifactId,
					targetRepositoryId,
					targetRepositoryKind,
					targetSystemId,
					targetSystemKind,
					sourceArtifactId,
					sourceRepositoryId,
					sourceRepositoryKind,
					sourceSystemId,
					sourceSystemKind,
					targetTime,
					targetArtifactVersion,
					sourceTime,
					sourceArtifactVersion,
					artifactType,
					depParentTargetArtifactId,
					depParentTargetRepositoryId,
					depParentTargetRepositoryKind,
					depParentSourceArtifactId,
					depParentSourceRepositoryId,
					depParentSourceRepositoryKind,
					NULL_VALUE,
					NULL_VALUE,
					NULL_VALUE,
					NULL_VALUE,
					NULL_VALUE,
					NULL_VALUE);

			
			IOrderedMap inputParameters = new OrderedHashMap();
			inputParameters.add(0,"LAST_SOURCE_ARTIFACT_MODIFICATION_DATE",sourceTime);
			inputParameters.add(1,"LAST_SOURCE_ARTIFACT_VERSION",sourceArtifactVersion);
			inputParameters.add(2,"LAST_SOURCE_ARTIFACT_ID",sourceArtifactId);
			inputParameters.add(3,"SOURCE_SYSTEM_ID",sourceSystemId);
			inputParameters.add(4,"SOURCE_REPOSITORY_ID",sourceRepositoryId);
			inputParameters.add(5,"TARGET_SYSTEM_ID",targetSystemId);
			inputParameters.add(6,"TARGET_REPOSITORY_ID",targetRepositoryId);
			
			IOrderedMap[] params = new IOrderedMap[]{inputParameters};
			synchronizationStatusDatabaseUpdater.connect();
			synchronizationStatusDatabaseUpdater.deliver(params);
			synchronizationStatusDatabaseUpdater.disconnect();
		}
		catch(GenericArtifactParsingException e) {
			log.error("There is some problem in extracting attributes from Document in EntityService!!!"+e);
		}
		}
		else {
			String message = "The Mapping updater needs a GenericArtifact object";
			message += " But it got something else.";
			throw new RuntimeException(message);
		}
		
		// TODO Auto-generated method stub
		return null;
	}
	
	private void createMapping(String sourceArtifactId, String sourceRepositoryId,
			String sourceRepositoryKind, String sourceSystemId,
			String sourceSystemKind, String targetArtifactId, String targetRepositoryId,
			String targetRepositoryKind, String targetSystemId,
			String targetSystemKind, java.sql.Timestamp sourceTime, String sourceArtifactVersion,java.sql.Timestamp targetTime, String targetArtifactVersion,
			String artifactType,String depParentSourceArtifactId,String depParentSourceRepositoryId,String depParentSourceRepositoryKind,
			String depParentTargetArtifactId,String depParentTargetRepositoryId,String depParentTargetRepositoryKind,
			String depChildSourceArtifactId,String depChildSourceRepositoryId,String depChildSourceRepositoryKind,
			String depChildTargetArtifactId,String depChildTargetRepositoryId,String depChildTargetRepositoryKind) {
		
		String targetArtifactIdFromTable = lookupTargetArtifactId(sourceArtifactId, sourceSystemId, sourceRepositoryId, 
				targetSystemId, targetRepositoryId, artifactType);
		if(targetArtifactIdFromTable == null) {
				this.createIdentityMapping(sourceSystemId, sourceRepositoryId, 
						targetSystemId, targetRepositoryId, sourceSystemKind, sourceRepositoryKind, 
						targetSystemKind, targetRepositoryKind, sourceArtifactId, targetArtifactId,
						sourceTime, targetTime, sourceArtifactVersion, targetArtifactVersion,
						artifactType, depParentSourceArtifactId, depParentSourceRepositoryId, depParentSourceRepositoryKind,
						 depParentTargetArtifactId, depParentTargetRepositoryId, depParentTargetRepositoryKind,
						 depChildSourceArtifactId, depChildSourceRepositoryId, depChildSourceRepositoryKind,
						 depChildTargetArtifactId, depChildTargetRepositoryId, depChildTargetRepositoryKind );
	    } else {
	    	this.updateIdentityMapping(sourceSystemId, sourceRepositoryId, 
			 targetSystemId,  targetRepositoryId,  sourceArtifactId, 
			 sourceTime, targetTime,  sourceArtifactVersion, targetArtifactVersion, artifactType);
	    	log.debug("Mapping already exists for source artifact id "+ sourceArtifactId+
	    			" target artifact id "+ targetArtifactId + " for repository info " + sourceArtifactId+"+"+ sourceSystemId+"+"+ sourceRepositoryId+"+"+ 
					targetSystemId);
	    }
		
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

		identityMappingDatabaseReader.connect();
		Object[] resultSet = identityMappingDatabaseReader.next(inputParameters, 1000);
		identityMappingDatabaseReader.disconnect();
		
		
		if(resultSet == null || resultSet.length == 0){
			targetArtifactId = null;
		}
		else if(resultSet.length == 1){
			if(resultSet[0] instanceof OrderedHashMap){
				OrderedHashMap result = (OrderedHashMap) resultSet[0];
				if(result.size() > 0){
					targetArtifactId = result.get(0).toString();
					log.info("The value of targetArtifactId="+targetArtifactId);
					return targetArtifactId;
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
	
	private void createIdentityMapping(String sourceSystemId, String sourceRepositoryId, 
			String targetSystemId, String targetRepositoryId, String sourceSystemKind, String sourceRepositoryKind, 
			String targetSystemKind, String targetRepositoryKind, String sourceArtifactId, String targetArtifactId,
			java.sql.Timestamp sourceTime, java.sql.Timestamp targetTime, String sourceArtifactVersion, String targetArtifactVersion,
			String artifactType, String depParentSourceArtifactId, String depParentSourceRepositoryId,String depParentSourceRepositoryKind,
			String depParentTargetArtifactId, String depParentTargetRepositoryId, String depParentTargetRepositoryKind,
			String depChildSourceArtifactId, String depChildSourceRepositoryId, String depChildSourceRepositoryKind,
			String depChildTargetArtifactId, String depChildTargetRepositoryId, String depChildTargetRepositoryKind ){
		IOrderedMap inputParameters = new OrderedHashMap();
		
		inputParameters.add(0,"SOURCE_SYSTEM_ID",sourceSystemId);
		inputParameters.add(1,"SOURCE_REPOSITORY_ID",sourceRepositoryId);
		inputParameters.add(2,"TARGET_SYSTEM_ID",targetSystemId);
		inputParameters.add(3,"TARGET_REPOSITORY_ID",targetRepositoryId);
		inputParameters.add(4,"SOURCE_SYSTEM_KIND",sourceSystemKind);
		inputParameters.add(5,"SOURCE_REPOSITORY_KIND",sourceRepositoryKind);
		inputParameters.add(6,"TARGET_SYSTEM_KIND",targetSystemKind);
		inputParameters.add(7,"TARGET_REPOSITORY_KIND",targetRepositoryKind);
		inputParameters.add(8,"SOURCE_ARTIFACT_ID",sourceArtifactId);
		inputParameters.add(9,"TARGET_ARTIFACT_ID",targetArtifactId);
		inputParameters.add(10,"SOURCE_LAST_MODIFICATION_TIME",sourceTime);
		inputParameters.add(11,"TARGET_LAST_MODIFICATION_TIME",targetTime);
		inputParameters.add(12,"SOURCE_ARTIFACT_VERSION",sourceArtifactVersion);
		inputParameters.add(13,"TARGET_ARTIFACT_VERSION",targetArtifactVersion);
		inputParameters.add(14,"ARTIFACT_TYPE",artifactType);
		inputParameters.add(15,"DEP_CHILD_SOURCE_ARTIFACT_ID", depChildSourceArtifactId);
		inputParameters.add(16,"DEP_CHILD_SOURCE_REPOSITORY_ID", depChildSourceRepositoryId);
		inputParameters.add(17,"DEP_CHILD_SOURCE_REPOSITORY_KIND", depChildSourceRepositoryKind);
		inputParameters.add(18,"DEP_CHILD_TARGET_ARTIFACT_ID", depChildTargetArtifactId);
		inputParameters.add(19,"DEP_CHILD_TARGET_REPOSITORY_ID", depChildTargetRepositoryId);
		inputParameters.add(20,"DEP_CHILD_TARGET_REPOSITORY_KIND", depChildTargetRepositoryKind);		
		inputParameters.add(21,"DEP_PARENT_SOURCE_ARTIFACT_ID", depParentSourceArtifactId);
		inputParameters.add(22,"DEP_PARENT_SOURCE_REPOSITORY_ID", depParentSourceRepositoryId);
		inputParameters.add(23,"DEP_PARENT_SOURCE_REPOSITORY_KIND", depParentSourceRepositoryKind);
		inputParameters.add(24,"DEP_PARENT_TARGET_ARTIFACT_ID", depParentTargetArtifactId);
		inputParameters.add(25,"DEP_PARENT_TARGET_REPOSITORY_ID", depParentTargetRepositoryId);
		inputParameters.add(26,"DEP_PARENT_TARGET_REPOSITORY_KIND", depParentTargetRepositoryKind);
		
		IOrderedMap[] data = new IOrderedMap[]{inputParameters};
		identityMappingDatabaseInserter.connect();
		identityMappingDatabaseInserter.deliver(data);
		identityMappingDatabaseInserter.disconnect();
	}

	private void updateIdentityMapping(String sourceSystemId, String sourceRepositoryId, 
			String targetSystemId, String targetRepositoryId, String sourceArtifactId, 
			java.sql.Timestamp sourceTime,java.sql.Timestamp targetTime, String sourceArtifactVersion,
			String targetArtifactVersion, String artifactType){
		IOrderedMap inputParameters = new OrderedHashMap();
		
		inputParameters.add(0,"SOURCE_LAST_MODIFICATION_TIME",sourceTime);
		inputParameters.add(1,"TARGET_LAST_MODIFICATION_TIME",targetTime);
		inputParameters.add(2,"SOURCE_ARTIFACT_VERSION",sourceArtifactVersion);
		inputParameters.add(3,"TARGET_ARTIFACT_VERSION",targetArtifactVersion);
		inputParameters.add(4,"SOURCE_SYSTEM_ID",sourceSystemId);
		inputParameters.add(5,"SOURCE_REPOSITORY_ID",sourceRepositoryId);
		inputParameters.add(6,"TARGET_SYSTEM_ID",targetSystemId);
		inputParameters.add(7,"TARGET_REPOSITORY_ID",targetRepositoryId);
		inputParameters.add(8,"SOURCE_ARTIFACT_ID",sourceArtifactId);
		inputParameters.add(9,"ARTIFACT_TYPE",artifactType);
		
		IOrderedMap[] params = new IOrderedMap[]{inputParameters};
		identityMappingDatabaseUpdater.connect();
		identityMappingDatabaseUpdater.deliver(params);
		identityMappingDatabaseUpdater.disconnect();
	}
	
	
	public void reset(Object context) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		if (getSynchronizationStatusDatabaseUpdater() == null) {
			log.error("synchronizationStatusDatabaseUpdater-property not set");
			exceptions.add(new ValidationException(
					"synchronizationStatusDatabaseUpdater-property not set", this));
		}
		
		if (getIdentityMappingDatabaseReader() == null) {
			log.error("identityMappingDatabaseReader-property not set");
			exceptions.add(new ValidationException(
					"identityMappingDatabaseReader-property not set", this));
		}
		
		if (getSynchronizationStatusDatabaseUpdater() == null) {
			log.error("synchronizationStatusDatabaseUpdater-property not set");
			exceptions.add(new ValidationException(
					"synchronizationStatusDatabaseUpdater-property not set", this));
		}
		
		if (getIdentityMappingDatabaseUpdater() == null) {
			log.error("getIdentityMappingDatabaseUpdater-property not set");
			exceptions.add(new ValidationException(
					"getIdentityMappingDatabaseUpdater-property not set", this));
		}
		
		if (getIdentityMappingDatabaseInserter() == null) {
			log.error("getIdentityMappingDatabaseInserter-property not set");
			exceptions.add(new ValidationException(
					"getIdentityMappingDatabaseInserter-property not set", this));
		}
		
	}

	public JDBCWriteConnector getSynchronizationStatusDatabaseUpdater() {
		return synchronizationStatusDatabaseUpdater;
	}

	public void setSynchronizationStatusDatabaseUpdater(JDBCWriteConnector synchronizationStatusDatabaseUpdater) {
		this.synchronizationStatusDatabaseUpdater = synchronizationStatusDatabaseUpdater;
	}
	public JDBCWriteConnector getIdentityMappingDatabaseInserter() {
		return identityMappingDatabaseInserter;
	}

	public void setIdentityMappingDatabaseInserter(
			JDBCWriteConnector identityMappingDatabaseInserter) {
		this.identityMappingDatabaseInserter = identityMappingDatabaseInserter;
	}

	public JDBCReadConnector getIdentityMappingDatabaseReader() {
		return identityMappingDatabaseReader;
	}

	public void setIdentityMappingDatabaseReader(JDBCReadConnector identityMappingDatabaseReader) {
		this.identityMappingDatabaseReader = identityMappingDatabaseReader;
	}

	public JDBCWriteConnector getIdentityMappingDatabaseUpdater() {
		return identityMappingDatabaseUpdater;
	}

	public void setIdentityMappingDatabaseUpdater(JDBCWriteConnector identityMappingUpdater) {
		this.identityMappingDatabaseUpdater = identityMappingUpdater;
	}

}
