package com.collabnet.ccf.core.config;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.openadaptor.auxil.connector.jdbc.writer.JDBCWriteConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.IDataProcessor;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;

public class MappingDBUpdater implements IDataProcessor{
	private JDBCWriteConnector mappingWriter = null;
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
			String targetRepositoryId = ga.getTargetRepositoryId();
			String sourceArtifactId = ga.getSourceArtifactId();
			String targetArtifactId = ga.getTargetArtifactId();
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

}
