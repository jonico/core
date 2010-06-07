package com.collabnet.ccf.swp;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.danube.scrumworks.api2.client.Sprint;
import com.danube.scrumworks.api2.client.Team;

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
		PBI, TASK, SPRINT, TEAM, RELEASE, /* PROGRAM_RELEASE, */PRODUCT, EPIC, /*
																			 * PROGRAM,
																			 *//*
																				 * PROGRAM_EPIC
																				 * ,
																				 */METADATA, /*
																							 * PROGRAM_THEME,
																							 */IMPEDIMENT, USER, UNKNOWN
	}

	public final static String PBI = "PBI";
	public final static String TASK = "Task";
	public final static String SPRINT = "Sprint";
	public final static String TEAM = "Team";
	public final static String RELEASE = "Release";
	// public final static String PROGRAM_RELEASE = "ProgramRelease";
	public final static String PRODUCT = "Product";
	public final static String EPIC = "Epic";
	// public final static String PROGRAM = "Program";
	// public final static String PROGRAM_EPIC = "ProgramEpic";
	public final static String METADATA = "MetaData";
	// public final static String PROGRAM_THEME = "ProgramTheme";
	public final static String IMPEDIMENT = "Impediment";
	public final static String USER = "User";
	public final static String UNKNOWN = "UNKNOWN";

	public static final String SWP_METADATA_ID_PREFIX = "metaDataFor";

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
		// if (repositoryId.endsWith(PROGRAM_RELEASE))
		// return SWPType.PROGRAM_RELEASE;
		if (repositoryId.endsWith(RELEASE))
			return SWPType.RELEASE;
		if (repositoryId.endsWith(USER))
			return SWPType.USER;
		if (repositoryId.endsWith(PRODUCT))
			return SWPType.PRODUCT;
		// if (repositoryId.endsWith(PROGRAM))
		// return SWPType.PROGRAM;
		// if (repositoryId.endsWith(PROGRAM_THEME))
		// return SWPType.PROGRAM_THEME;
		if (repositoryId.endsWith(METADATA))
			return SWPType.METADATA;
		if (repositoryId.endsWith(EPIC))
			return SWPType.EPIC;
		// if (repositoryId.endsWith(PROGRAM_EPIC))
		// return SWPType.PROGRAM_EPIC;
		return SWPType.UNKNOWN;
	}

	/**
	 * Parses the repository id and returns the part the encodes the product
	 * 
	 * @param repositoryId
	 * @return name of the product or null if product name could not be
	 *         extracted
	 */
	public final static String retrieveProductFromRepositoryId(
			String repositoryId) {
		int index = repositoryId.lastIndexOf(REPOSITORY_ID_SEPARATOR);
		if (index == -1) {
			return null;
		} else {
			return repositoryId.substring(0, index);
		}
	}

	public enum PBIFields {
		// TODO Differentiate between program and product releases
		id("id", GenericArtifactField.FieldValueTypeValue.STRING), benefit(
				"benefit", GenericArtifactField.FieldValueTypeValue.INTEGER), penalty(
				"penalty", GenericArtifactField.FieldValueTypeValue.INTEGER), completedDate(
				"completedDate", GenericArtifactField.FieldValueTypeValue.DATE), sprintStart(
				"sprintStart", GenericArtifactField.FieldValueTypeValue.DATE), sprintEnd(
				"sprintEnd", GenericArtifactField.FieldValueTypeValue.DATE), description(
				"description", GenericArtifactField.FieldValueTypeValue.STRING), estimate(
				"estimate", GenericArtifactField.FieldValueTypeValue.INTEGER),
		// groupId("groupId", GenericArtifactField.FieldValueTypeValue.INTEGER),
		key("key", GenericArtifactField.FieldValueTypeValue.STRING), productId(
				"productId", GenericArtifactField.FieldValueTypeValue.STRING), rank(
				"rank", GenericArtifactField.FieldValueTypeValue.DOUBLE), releaseId(
				"releaseId", GenericArtifactField.FieldValueTypeValue.STRING), sprint(
				"sprint", GenericArtifactField.FieldValueTypeValue.STRING), sprintId(
				"sprintId", GenericArtifactField.FieldValueTypeValue.STRING), team(
				"team", GenericArtifactField.FieldValueTypeValue.STRING), teamSprint(
				"teamSprint", GenericArtifactField.FieldValueTypeValue.STRING), sprintTeam(
				"sprintTeam", GenericArtifactField.FieldValueTypeValue.STRING), title(
				"title", GenericArtifactField.FieldValueTypeValue.STRING), theme(
				"theme", GenericArtifactField.FieldValueTypeValue.STRING), comment(
				"Comment Text", GenericArtifactField.FieldValueTypeValue.STRING), active(
				"active", GenericArtifactField.FieldValueTypeValue.BOOLEAN);

		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;

		private PBIFields(String fieldName,
				GenericArtifactField.FieldValueTypeValue valueType) {
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
		id("id", GenericArtifactField.FieldValueTypeValue.STRING), backlogItemId(
				"backLogItemId",
				GenericArtifactField.FieldValueTypeValue.STRING), description(
				"description", GenericArtifactField.FieldValueTypeValue.STRING), estimatedHours(
				"estimatedHours",
				GenericArtifactField.FieldValueTypeValue.INTEGER), rank("rank",
				GenericArtifactField.FieldValueTypeValue.DOUBLE), status(
				"status", GenericArtifactField.FieldValueTypeValue.STRING), pointPerson(
				"pointPerson", GenericArtifactField.FieldValueTypeValue.STRING), originalEstimate(
				"originalEstimate",
				GenericArtifactField.FieldValueTypeValue.INTEGER), taskBoardStatusRank(
				"taskBoardStatusRank",
				GenericArtifactField.FieldValueTypeValue.DOUBLE), comment(
				"Comment Text", GenericArtifactField.FieldValueTypeValue.STRING), title(
				"title", GenericArtifactField.FieldValueTypeValue.STRING);

		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;

		private TaskFields(String fieldName,
				GenericArtifactField.FieldValueTypeValue valueType) {
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
		id("id", GenericArtifactField.FieldValueTypeValue.STRING), businessWeightUnits(
				"businessWeightUnits",
				GenericArtifactField.FieldValueTypeValue.STRING), effortUnits(
				"effortUnits", GenericArtifactField.FieldValueTypeValue.STRING), name(
				"name", GenericArtifactField.FieldValueTypeValue.STRING), trackTimeSpent(
				"trackTimeSpent",
				GenericArtifactField.FieldValueTypeValue.BOOLEAN), keyPrefix(
				"keyPrefix", GenericArtifactField.FieldValueTypeValue.STRING);

		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;

		private ProductFields(String fieldName,
				GenericArtifactField.FieldValueTypeValue valueType) {
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
		id("id", GenericArtifactField.FieldValueTypeValue.STRING), title(
				"title", GenericArtifactField.FieldValueTypeValue.STRING), startDate(
				"startDate", GenericArtifactField.FieldValueTypeValue.DATE), releaseDate(
				"releaseDate", GenericArtifactField.FieldValueTypeValue.DATE), description(
				"description", GenericArtifactField.FieldValueTypeValue.STRING), archived(
				"archived", GenericArtifactField.FieldValueTypeValue.BOOLEAN), productId(
				"productId", GenericArtifactField.FieldValueTypeValue.STRING), programId(
				"programId", GenericArtifactField.FieldValueTypeValue.STRING);

		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;

		private ReleaseFields(String fieldName,
				GenericArtifactField.FieldValueTypeValue valueType) {
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

	public enum MetaDataFields {
		theme("theme", GenericArtifactField.FieldValueTypeValue.STRING), teamSprint(
				"teamSprint", GenericArtifactField.FieldValueTypeValue.STRING), team(
				"team", GenericArtifactField.FieldValueTypeValue.STRING), sprintTeam(
				"sprintTeam", GenericArtifactField.FieldValueTypeValue.STRING);

		private GenericArtifactField.FieldValueTypeValue valueType;
		private String fieldName;

		private MetaDataFields(String fieldName,
				GenericArtifactField.FieldValueTypeValue valueType) {
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

	// this date formatter is used to produce string representations of sprint
	// start/end dates
	public final static SimpleDateFormat sprintDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	/**
	 * Returns the string representation of a sprint in a team if team should be
	 * mentioned first
	 * 
	 * @param sprint
	 * @param team
	 * @return
	 */
	public static String getTeamSprintStringRepresentation(Sprint sprint,
			Team team) {
		Date startDate = sprint.getStartDate().toGregorianCalendar().getTime();
		Date endDate = sprint.getEndDate().toGregorianCalendar().getTime();
		StringBuffer value = new StringBuffer(team.getName() + " "
				+ sprintDateFormat.format(startDate) + " - "
				+ sprintDateFormat.format(endDate));
		if (sprint.getName() != null && sprint.getName().trim().length() > 0) {
			value.append(" -- " + sprint.getName());
		}
		return value.toString();
	}
	
	/**
	 * Returns the string representation of a sprint in a team if team should be
	 * mentioned first
	 * 
	 * @param sprint
	 * @param team
	 * @return
	 */
	public static String getTeamSprintStringRepresentation(String sprintName, String startDate, String endDate,
			String teamName) {
		StringBuffer value = new StringBuffer(teamName + " "
				+ startDate + " - "
				+ endDate);
		if (sprintName != null && sprintName.trim().length() > 0) {
			value.append(" -- " + sprintName);
		}
		return value.toString();
	}

	/**
	 * Returns the string representation of a sprint in a team if sprint should
	 * be mentioned first
	 * 
	 * @param sprint
	 * @param team
	 * @return
	 */
	public static String getSprintTeamStringRepresentation(Sprint sprint,
			Team team) {
		Date startDate = sprint.getStartDate().toGregorianCalendar().getTime();
		Date endDate = sprint.getEndDate().toGregorianCalendar().getTime();
		StringBuffer value = new StringBuffer(sprintDateFormat
				.format(startDate)
				+ " - " + sprintDateFormat.format(endDate));
		if (sprint.getName() != null && sprint.getName().trim().length() > 0) {
			value.append(" -- " + sprint.getName());
		}
		value.append(" (" + team.getName() + ")");
		return value.toString();
	}

	// TODO Include meta info for the other SWP types
}
