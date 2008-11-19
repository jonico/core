package com.collabnet.ccf.pi.cee.pt.v50;

import java.util.List;

import com.collabnet.tracker.common.ClientArtifactListXMLHelper;
import com.collabnet.tracker.common.ClientXMLOperationError;

public class ProjectTrackerHelper {
	private static ProjectTrackerHelper instance = null;
	private ProjectTrackerHelper(){
		
	}
	public static ProjectTrackerHelper getInstance(){
		synchronized(ProjectTrackerHelper.class){
			if(instance == null){
				instance = new ProjectTrackerHelper();
			}
		}
		return instance;
	}
	public String getArtifactIdFromFullyQualifiedArtifactId(String fullyQualifiedArtifactId){
		String artifactIdentifier =
			fullyQualifiedArtifactId.substring(fullyQualifiedArtifactId.lastIndexOf(":")+1);
		return artifactIdentifier;
	}
	public String getArtifactTypeTagNameFromFullyQualifiedArtifactId(String fullyQualifiedArtifactId){
		int lastIndexOfCurlyBrace = fullyQualifiedArtifactId.lastIndexOf("}");
		int lastIndexOfColon = fullyQualifiedArtifactId.lastIndexOf(":");
		if(lastIndexOfCurlyBrace != -1 && lastIndexOfColon != -1) {
			String artifactTypeTagName =
				fullyQualifiedArtifactId.substring(lastIndexOfCurlyBrace+1,
						lastIndexOfColon);
			return artifactTypeTagName;
		}
		else {
			return null;
		}
	}
	public String getArtifactTypeNamespaceFromFullyQualifiedArtifactId(String fullyQualifiedArtifactId){
		String artifactTypeNamespace =
			this.getArtifactTypeNamespaceFromFullyQualifiedArtifactType(fullyQualifiedArtifactId);
		return artifactTypeNamespace;
	}
	public String getArtifactTypeTagNameFromFullyQualifiedArtifactType(String fullyQualifiedArtifactType){
		String artifactTypeTagName =
			fullyQualifiedArtifactType.substring(fullyQualifiedArtifactType.lastIndexOf("}")+1);
		return artifactTypeTagName;
	}
	public String getArtifactTypeNamespaceFromFullyQualifiedArtifactType(String fullyQualifiedArtifactType){
		int indexOfClosingCurlyBrace = fullyQualifiedArtifactType.lastIndexOf("}");
		if(indexOfClosingCurlyBrace != -1) {
			String artifactTypeNamespace =
				fullyQualifiedArtifactType.substring(1,indexOfClosingCurlyBrace);
			return artifactTypeNamespace;
		}
		else {
			return null;
		}
	}
	public String getArtifactTypeNamespaceFromRepositoryId(String repositoryId){
		String artifactTypeNamespace = repositoryId.substring(repositoryId.indexOf('{')+1,
				repositoryId.indexOf('}'));
		return artifactTypeNamespace;
	}
	public String getArtifactTypeTagNameFromRepositoryId(String repositoryId){
		String artifactTypeNamespace = repositoryId.substring(repositoryId.indexOf('}')+1);
		return artifactTypeNamespace;
	}
	public String getNamespace(String input){
		int start = input.indexOf("{");
		int end = input.indexOf("}");
		String namespace = null;
		if(start >=0 && end >=2){
			namespace = input.substring(start+1, end);
		}
		return namespace;
	}
	public static String getNamespaceWithBraces(String input){
		int start = input.indexOf("{");
		int end = input.indexOf("}");
		String namespace = null;
		if(start >=0 && end >=2){
			namespace = input.substring(start, end+1);
		}
		else {
			namespace = "";
		}
		return namespace;
	}
	public String getEntityName(String input){
		int start = input.indexOf("}");
		String entityName = null;
		if(start < 0){
			entityName = input;
		}
		else if(start >= 0){
			entityName = input.substring(start+1);
		}
		return entityName;
	}
	public void processWSErrors(ClientArtifactListXMLHelper soapResponse) {
		List<ClientXMLOperationError> errors = soapResponse.getErrors();
		String cause = null;
		if(errors.size() > 0){
			cause = "";
			for(ClientXMLOperationError error:errors){
				String message = error.getMsg();
				String code = error.getCode();
				String trace = error.getTrace();
				cause = "Message: " +message + " Code: " + code + System.getProperty("line.sparator");
				cause += trace + System.getProperty("line.sparator") + System.getProperty("line.sparator");
			}
			throw new ProjectTrackerWebServiceException(cause);
		}
	}
}
