package com.collabnet.ccf.swp;

import com.collabnet.ccf.core.ga.GenericArtifactField;

/**
 * This class contains all constants and helper methods for SWP
 * 
 * @author jnicolai
 * 
 */
public class SWPMetaData {
	/**
	 * This entity type denotes all possible SWP entities
	 * 
	 * @author jnicolai
	 * 
	 */
	public enum SWPType {
		PBI, TASK, SPRINT, TEAM, RELEASE, /* PROGRAM_RELEASE, */ PRODUCT, EPIC, /* PROGRAM, */ /* PROGRAM_EPIC ,*/ THEME, /*PROGRAM_THEME, */ IMPEDIMENT, USER, UNKNOWN
	}

	public final static String PBI = "PBI";
	public final static String TASK = "Task";
	public final static String SPRINT = "Sprint";
	public final static String TEAM = "Team";
	public final static String RELEASE = "Release";
	//public final static String PROGRAM_RELEASE = "ProgramRelease";
	public final static String PRODUCT = "Product";
	public final static String EPIC = "Epic";
	//public final static String PROGRAM = "Program";
	//public final static String PROGRAM_EPIC = "ProgramEpic";
	public final static String THEME = "Theme";
	//public final static String PROGRAM_THEME = "ProgramTheme";
	public final static String IMPEDIMENT = "Impediment";
	public final static String USER = "User";
	public final static String UNKNOWN = "UNKNOWN";

	/**
	 * Character used to separate the components of a repository id
	 */
	public final static String REPOSITORY_ID_SEPARATOR = "-";

	/**
	 * Parses an SWP repository id and returns the corresponding SWP type
	 * 
	 * @param repositoryId
	 * @return SWP entity type
	 */
	public static SWPType retrieveSWPTypeFromRepositoryId(String repositoryId) {
		if (repositoryId == null)
			return SWPType.UNKNOWN;
		if (repositoryId.endsWith(PBI))
			return SWPType.PBI;
		if (repositoryId.endsWith(TASK))
			return SWPType.TASK;
		if (repositoryId.endsWith(SPRINT))
			return SWPType.SPRINT;
		if (repositoryId.endsWith(TEAM))
			return SWPType.TEAM;
		if (repositoryId.endsWith(IMPEDIMENT))
			return SWPType.IMPEDIMENT;
		//if (repositoryId.endsWith(PROGRAM_RELEASE))
		//	return SWPType.PROGRAM_RELEASE;
		if (repositoryId.endsWith(RELEASE))
			return SWPType.RELEASE;
		if (repositoryId.endsWith(USER))
			return SWPType.USER;
		if (repositoryId.endsWith(PRODUCT))
			return SWPType.PRODUCT;
		//if (repositoryId.endsWith(PROGRAM))
		//	return SWPType.PROGRAM;
		//if (repositoryId.endsWith(PROGRAM_THEME))
		//	return SWPType.PROGRAM_THEME;
		if (repositoryId.endsWith(THEME))
			return SWPType.THEME;
		if (repositoryId.endsWith(EPIC))
			return SWPType.EPIC;
		//if (repositoryId.endsWith(PROGRAM_EPIC))
		//	return SWPType.PROGRAM_EPIC;
		return SWPType.UNKNOWN;
	}

	/**
	 * Parses the repository id and returns the part the encodes the product
	 * 
	 * @param repositoryId
	 * @return name of the product or null if product name could not be
	 *         extracted
	 */
	public final static String retrieveProductFromRepositoryId(String repositoryId) {
		int index = repositoryId.lastIndexOf(REPOSITORY_ID_SEPARATOR);
		if (index == -1) {
			return null;
		} else {
			return repositoryId.substring(0, index);
		}
	}
	
	public enum PBIFields {
		// TODO Differentiate between program and product releases
		id("id", GenericArtifactField.FieldValueTypeValue.STRING),
		benefit("benefit", GenericArtifactField.FieldValueTypeValue.INTEGER),
		penalty("penalty", GenericArtifactField.FieldValueTypeValue.INTEGER),
		completedDate("completedDate", GenericArtifactField.FieldValueTypeValue.DATE),
		sprintStart("sprintStart", GenericArtifactField.FieldValueTypeValue.DATE),
		sprintEnd("sprintEnd", GenericArtifactField.FieldValueTypeValue.DATE),
		description("description", GenericArtifactField.FieldValueTypeValue.STRING),
		estimate("estimate", GenericArtifactField.FieldValueTypeValue.INTEGER),
		//groupId("groupId", GenericArtifactField.FieldValueTypeValue.INTEGER),
		key("key", GenericArtifactField.FieldValueTypeValue.STRING),
		productId("productId", GenericArtifactField.FieldValueTypeValue.STRING),
		rank("rank", GenericArtifactField.FieldValueTypeValue.DOUBLE),
		releaseId("releaseId", GenericArtifactField.FieldValueTypeValue.STRING),
		sprint("sprint", GenericArtifactField.FieldValueTypeValue.STRING),
		sprintId("sprintId", GenericArtifactField.FieldValueTypeValue.STRING),
		team("team", GenericArtifactField.FieldValueTypeValue.STRING),
		title("title", GenericArtifactField.FieldValueTypeValue.STRING),
		theme("theme", GenericArtifactField.FieldValueTypeValue.STRING),
		comment("Comment Text", GenericArtifactField.FieldValueTypeValue.STRING),
		active("active", GenericArtifactField.FieldValueTypeValue.BOOLEAN);
		
		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;
		
		private PBIFields(String fieldName, GenericArtifactField.FieldValueTypeValue valueType) {
			this.fieldName = fieldName;
			this.valueType = valueType;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		
		public GenericArtifactField.FieldValueTypeValue getValueType() {
			return valueType;
		}
	}
	
	public enum TaskFields {
		id("id", GenericArtifactField.FieldValueTypeValue.STRING),
		backlogItemId("backLogItemId", GenericArtifactField.FieldValueTypeValue.STRING),
		description("description", GenericArtifactField.FieldValueTypeValue.STRING),
		estimatedHours("estimatedHours", GenericArtifactField.FieldValueTypeValue.INTEGER),
		rank("rank", GenericArtifactField.FieldValueTypeValue.DOUBLE),
		status("status", GenericArtifactField.FieldValueTypeValue.STRING),
		pointPerson("pointPerson", GenericArtifactField.FieldValueTypeValue.STRING),
		originalEstimate("originalEstimate", GenericArtifactField.FieldValueTypeValue.INTEGER),
		taskBoardStatusRank("taskBoardStatusRank", GenericArtifactField.FieldValueTypeValue.DOUBLE),
		comment("Comment Text", GenericArtifactField.FieldValueTypeValue.STRING),
		title("title", GenericArtifactField.FieldValueTypeValue.STRING);
		
		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;
		
		private TaskFields(String fieldName, GenericArtifactField.FieldValueTypeValue valueType) {
			this.fieldName = fieldName;
			this.valueType = valueType;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		
		public GenericArtifactField.FieldValueTypeValue getValueType() {
			return valueType;
		}
	}
	
	public enum ProductFields {
		id("id", GenericArtifactField.FieldValueTypeValue.STRING),
		businessWeightUnits("businessWeightUnits", GenericArtifactField.FieldValueTypeValue.STRING),
		effortUnits("effortUnits", GenericArtifactField.FieldValueTypeValue.STRING),
		name("name", GenericArtifactField.FieldValueTypeValue.STRING),
		trackTimeSpent("trackTimeSpent", GenericArtifactField.FieldValueTypeValue.BOOLEAN),
		keyPrefix("keyPrefix", GenericArtifactField.FieldValueTypeValue.STRING);
		
		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;
		
		private ProductFields(String fieldName, GenericArtifactField.FieldValueTypeValue valueType) {
			this.fieldName = fieldName;
			this.valueType = valueType;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		
		public GenericArtifactField.FieldValueTypeValue getValueType() {
			return valueType;
		}
	}
	
	public enum ReleaseFields {
		id("id", GenericArtifactField.FieldValueTypeValue.STRING),
		title("title", GenericArtifactField.FieldValueTypeValue.STRING),
		startDate("startDate", GenericArtifactField.FieldValueTypeValue.DATE),
		releaseDate("releaseDate", GenericArtifactField.FieldValueTypeValue.DATE),
		description("description", GenericArtifactField.FieldValueTypeValue.STRING),
		archived("archived", GenericArtifactField.FieldValueTypeValue.BOOLEAN),
		productId("productId", GenericArtifactField.FieldValueTypeValue.STRING),
		programId("programId", GenericArtifactField.FieldValueTypeValue.STRING);
		
		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;
		
		private ReleaseFields(String fieldName, GenericArtifactField.FieldValueTypeValue valueType) {
			this.fieldName = fieldName;
			this.valueType = valueType;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		
		public GenericArtifactField.FieldValueTypeValue getValueType() {
			return valueType;
		}
	}
	
	public enum ThemeFields {
		name("name", GenericArtifactField.FieldValueTypeValue.STRING);
		
		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;
		
		private ThemeFields(String fieldName, GenericArtifactField.FieldValueTypeValue valueType) {
			this.fieldName = fieldName;
			this.valueType = valueType;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		
		public GenericArtifactField.FieldValueTypeValue getValueType() {
			return valueType;
		}
	}
	
	// TODO Include meta info for the other SWP types
}

	
