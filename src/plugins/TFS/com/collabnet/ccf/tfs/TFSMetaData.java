package com.collabnet.ccf.tfs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.microsoft.tfs.core.clients.workitem.fields.FieldType;


public class TFSMetaData {

	public static final String TFS_REPOSITORY_DELIMITER = "-";
	private static final Log log = LogFactory.getLog(TFSHandler.class);

	public enum TFSType {
		WORKITEM, UNKNOWN
	}

	public static TFSType retrieveTFSTypeFromRepositoryId(String repositoryId) {
		
		if (repositoryId == null) {
			return TFSType.UNKNOWN;
		}

		int delimiterCounter = repositoryId.split(TFS_REPOSITORY_DELIMITER).length;

		if (delimiterCounter != 3) {
			return TFSType.UNKNOWN;
		} else {
			return TFSType.WORKITEM;
		}
		
	}
	
	public static String extractCollectionNameFromRepositoryId(String repositoryId){
		return repositoryId.split(TFS_REPOSITORY_DELIMITER)[0];
		
	}
	
	public static String extractProjectNameFromRepositoryId(String repositoryId){
		return repositoryId.split(TFS_REPOSITORY_DELIMITER)[1];
	}
	
	public static String extractWorkItemTypeFromRepositoryId(String repositoryId){
		return repositoryId.split(TFS_REPOSITORY_DELIMITER)[2];
		
	}
	
	public static GenericArtifactField.FieldValueTypeValue translateTFSFieldValueTypeToCCFFieldValueType(FieldType tfsFieldValueType) {
		
		if (FieldType.BOOLEAN.equals(tfsFieldValueType)){
			return FieldValueTypeValue.BOOLEAN;
		}
		
		if (FieldType.DATETIME.equals(tfsFieldValueType)){
			return FieldValueTypeValue.DATETIME;
		}
		
		if (FieldType.DOUBLE.equals(tfsFieldValueType)){
			return FieldValueTypeValue.DOUBLE;
		}
		
		if (FieldType.HTML.equals(tfsFieldValueType)){
			return FieldValueTypeValue.HTMLSTRING;
		}
		
		if (FieldType.INTEGER.equals(tfsFieldValueType)){
			return FieldValueTypeValue.INTEGER;
		}
		
		if (FieldType.STRING.equals(tfsFieldValueType)){
			return FieldValueTypeValue.STRING;
		}
		
		if (FieldType.GUID.equals(tfsFieldValueType)){
			return FieldValueTypeValue.STRING;
		}
		
		if (FieldType.HISTORY.equals(tfsFieldValueType)){
			return FieldValueTypeValue.STRING;
		}
		
		if (FieldType.PLAINTEXT.equals(tfsFieldValueType)){
			return FieldValueTypeValue.STRING;
		}
		
		if (FieldType.TREEPATH.equals(tfsFieldValueType)){
			return FieldValueTypeValue.STRING;
		}
		
		log.warn("WorkenItem Field Type was not identified, returning String value");
		return FieldValueTypeValue.STRING;
	}
	
	public static String extractDomainFromFullUserName(String userName){
		return userName.split(TFSMetaData.DOMAIM_DELIMETER)[0];
	}
	
	public static String extractUserNameFromFullUserName(String userName){
		return userName.split(TFSMetaData.DOMAIM_DELIMETER)[1];
	}

	public static final String DOMAIM_DELIMETER = "\\\\";
	
	
}
