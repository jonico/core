package com.collabnet.ccf.core.config;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.connector.jdbc.writer.JDBCWriteConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.IDataProcessor;

import com.collabnet.ccf.core.EntityService;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;

public class MappingDBUpdater implements IDataProcessor{
	private static final Log log = LogFactory.getLog(MappingDBUpdater.class);
	private JDBCWriteConnector mappingWriter = null;
	private JDBCReadConnector entityServiceReader = null;
	
	private JDBCReadConnector entityServiceMappingIdReader = null;
	private JDBCWriteConnector entityServiceWriteConnector = null;
	
	public Object[] process(Object data) {
		// I will expect a Generic Artifact object
		if(data instanceof Document){
			System.out.println(((Document)data).asXML());
			GenericArtifact ga = null;
			try {
				ga = GenericArtifactHelper.createGenericArtifactJavaObject((Document)data);
			} catch (GenericArtifactParsingException e1) {
				throw new RuntimeException(e1);
			}
			
			if(ga.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.UNKNOWN)) {
				Object [] result = {data};
				return result;
			}
			
			String lastModifiedDateString = ga.getArtifactLastModifiedDate();
			String lastReadTransactionId = ga.getLastReadTransactionId();
			String version = ga.getArtifactVersion();
			String sourceRepositoryId = ga.getSourceRepositoryId();
			String sourceSystemId  = ga.getSourceSystemId();
			String sourceSystemKind = ga.getSourceSystemKind();
			String sourceRepositoryKind = ga.getSourceRepositoryKind();
			
			String targetRepositoryId = ga.getTargetRepositoryId();
			String targetSystemId = ga.getTargetSystemId();
			String targetSystemKind = ga.getTargetSystemKind();
			String targetRepositoryKind = ga.getTargetRepositoryKind();
			
			String sourceArtifactId = ga.getSourceArtifactId();
			String targetArtifactId = ga.getTargetArtifactId();
			
			createMapping(sourceArtifactId,
					sourceRepositoryId,
					sourceRepositoryKind,
					sourceSystemId,
					sourceSystemKind,
					targetArtifactId,
					targetRepositoryId,
					targetRepositoryKind,
					targetSystemId,
					targetSystemKind);
			createMapping(targetArtifactId,
					targetRepositoryId,
					targetRepositoryKind,
					targetSystemId,
					targetSystemKind,
					sourceArtifactId,
					sourceRepositoryId,
					sourceRepositoryKind,
					sourceSystemId,
					sourceSystemKind);

			java.util.Date lastModifiedDate = null;
			if(lastModifiedDateString.equalsIgnoreCase("Unknown")){
				return new Object[]{};
			}
			else {
				lastModifiedDate = DateUtil.parse(lastModifiedDateString);
			}
			java.sql.Timestamp time = new java.sql.Timestamp(lastModifiedDate.getTime());
			
			IOrderedMap inputParameters = new OrderedHashMap();
			inputParameters.add(0,"LAST_READ_TIME",time);
			int transactionId = 0;
			if(StringUtils.isEmpty(lastReadTransactionId) || lastReadTransactionId.equals("unknown")){
				transactionId = 0;
			}
			else {
				try{
					transactionId = Integer.parseInt(lastReadTransactionId);
				}
				catch(NumberFormatException e){
					e.printStackTrace();
				}
			}
			inputParameters.add(1,"TRANSACTION_ID",transactionId);
			inputParameters.add(2,"VERSION",version);
			inputParameters.add(3,"A.REPOSITORY_ID",sourceRepositoryId);
			inputParameters.add(4,"B.REPOSITORY_ID",targetRepositoryId);
			inputParameters.add(5,"ARTIFACT_MAPPING.SOURCE_ARTIFACT_ID",sourceArtifactId);
			inputParameters.add(6,"ARTIFACT_MAPPING.TARGET_ARTIFACT_ID",targetArtifactId);

			IOrderedMap[] params = new IOrderedMap[]{inputParameters};
			mappingWriter.connect();
			mappingWriter.deliver(params);
			mappingWriter.disconnect();
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
			String targetSystemKind) {
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
		if(targetArtifactIdFromTable == null) {
			this.createMapping(mappingId, sourceArtifactId, targetArtifactId);
	    } else {
	    	log.info("Mapping already exists for source artifact id "+ sourceArtifactId+
	    			" target artifact id "+ targetArtifactId + " for mapping id " + mappingId);
	    }
		
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

	public void reset(Object context) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		// TODO Auto-generated method stub
		
	}

	public JDBCWriteConnector getMappingWriter() {
		return mappingWriter;
	}

	public void setMappingWriter(JDBCWriteConnector mappingWriter) {
		this.mappingWriter = mappingWriter;
	}
	public JDBCWriteConnector getEntityServiceWriteConnector() {
		return entityServiceWriteConnector;
	}

	public void setEntityServiceWriteConnector(
			JDBCWriteConnector entityServiceWriteConnector) {
		this.entityServiceWriteConnector = entityServiceWriteConnector;
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
