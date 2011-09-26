package com.collabnet.ccf.tfs;


public class TFSMetaData {

	public static final String TFS_REPOSITORY_DELIMITER = "-";

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
}
