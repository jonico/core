package com.collabnet.ccf.rcq;


public class RCQMetaData {

	public static final String RCQ_REPOSITORY_DELIMITER = "-";

	public enum RCQType {
		WORKITEM, UNKNOWN
	}

	public static RCQType retrieveRCQTypeFromRepositoryId(String repositoryId) {
		
		if (repositoryId == null) {
			return RCQType.UNKNOWN;
		}

		int delimiterCounter = repositoryId.split(RCQ_REPOSITORY_DELIMITER).length;

		if (delimiterCounter != 3) {
			return RCQType.UNKNOWN;
		} else {
			return RCQType.WORKITEM;
		}
		
	}
	
	public static String extractCollectionNameFromRepositoryId(String repositoryId){
		return repositoryId.split(RCQ_REPOSITORY_DELIMITER)[0];
		
	}
	
	public static String extractProjectNameFromRepositoryId(String repositoryId){
		return repositoryId.split(RCQ_REPOSITORY_DELIMITER)[1];
	}
	
	public static String extractWorkItemTypeFromRepositoryId(String repositoryId){
		return repositoryId.split(RCQ_REPOSITORY_DELIMITER)[2];
		
	}
}
