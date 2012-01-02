/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.core.config;

import java.util.Date;
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

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * 
 * This mapping DB updater implementation is designed to work with CCF 2.x only
 * 
 * The MappingDBUpdator is typically the last component in the pipeline. It
 * updates the synchronization status table and the identity mapping table after
 * the artifact has been saved in the target system.
 * 
 * @author jnicolai
 * 
 */
public class MappingDBUpdater2 extends LifecycleComponent implements
		IDataProcessor {

	// This properties are the column names within the data base
	// They must not used to retrieve attributes from the GenericArtifactFormat
	private static final String DEP_PARENT_TARGET_REPOSITORY_KIND = "DEP_PARENT_TARGET_REPOSITORY_KIND";
	private static final String DEP_PARENT_TARGET_REPOSITORY_ID = "DEP_PARENT_TARGET_REPOSITORY_ID";
	private static final String DEP_PARENT_TARGET_ARTIFACT_ID = "DEP_PARENT_TARGET_ARTIFACT_ID";
	private static final String DEP_PARENT_SOURCE_REPOSITORY_KIND = "DEP_PARENT_SOURCE_REPOSITORY_KIND";
	private static final String DEP_PARENT_SOURCE_REPOSITORY_ID = "DEP_PARENT_SOURCE_REPOSITORY_ID";
	private static final String DEP_PARENT_SOURCE_ARTIFACT_ID = "DEP_PARENT_SOURCE_ARTIFACT_ID";
	private static final String DEP_CHILD_TARGET_REPOSITORY_KIND = "DEP_CHILD_TARGET_REPOSITORY_KIND";
	private static final String DEP_CHILD_TARGET_REPOSITORY_ID = "DEP_CHILD_TARGET_REPOSITORY_ID";
	private static final String DEP_CHILD_TARGET_ARTIFACT_ID = "DEP_CHILD_TARGET_ARTIFACT_ID";
	private static final String DEP_CHILD_SOURCE_REPOSITORY_KIND = "DEP_CHILD_SOURCE_REPOSITORY_KIND";
	private static final String DEP_CHILD_SOURCE_REPOSITORY_ID = "DEP_CHILD_SOURCE_REPOSITORY_ID";
	private static final String DEP_CHILD_SOURCE_ARTIFACT_ID = "DEP_CHILD_SOURCE_ARTIFACT_ID";
	private static final String ARTIFACT_TYPE = "ARTIFACT_TYPE";
	private static final String TARGET_ARTIFACT_VERSION = "TARGET_ARTIFACT_VERSION";
	private static final String SOURCE_ARTIFACT_VERSION = "SOURCE_ARTIFACT_VERSION";
	private static final String TARGET_LAST_MODIFICATION_TIME = "TARGET_LAST_MODIFICATION_TIME";
	private static final String SOURCE_LAST_MODIFICATION_TIME = "SOURCE_LAST_MODIFICATION_TIME";
	private static final String TARGET_ARTIFACT_ID = "TARGET_ARTIFACT_ID";
	private static final String SOURCE_ARTIFACT_ID = "SOURCE_ARTIFACT_ID";
	private static final String TARGET_REPOSITORY_KIND = "TARGET_REPOSITORY_KIND";
	private static final String TARGET_SYSTEM_KIND = "TARGET_SYSTEM_KIND";
	private static final String SOURCE_REPOSITORY_KIND = "SOURCE_REPOSITORY_KIND";
	private static final String SOURCE_SYSTEM_KIND = "SOURCE_SYSTEM_KIND";
	private static final String TARGET_REPOSITORY_ID = "TARGET_REPOSITORY_ID";
	private static final String TARGET_SYSTEM_ID = "TARGET_SYSTEM_ID";
	private static final String SOURCE_REPOSITORY_ID = "SOURCE_REPOSITORY_ID";
	private static final String SOURCE_SYSTEM_ID = "SOURCE_SYSTEM_ID";
	private static final String REPOSITORY_MAPPING_DIRECTION = "REPOSITORY_MAPPING_DIRECTION";
	private static final String REPOSITORY_MAPPING  = "REPOSITORY_MAPPING";
	private static final String VERSION  = "VERSION";
	private static final String DESCRIPTION  = "DESCRIPTION";

	private static final Log log = LogFactory.getLog(MappingDBUpdater2.class);
	private JDBCWriteConnector synchronizationStatusDatabaseUpdater = null;
	private JDBCReadConnector identityMappingDatabaseReader = null;
	private JDBCWriteConnector identityMappingDatabaseUpdater = null;

	private JDBCWriteConnector identityMappingDatabaseInserter = null;
	/*
	 * This property indicates whether the synchronization status table should
	 * be updated. The default value is true and should be only set to false for
	 * the hospital replay scenario
	 */
	private boolean updateSynchronizationStatusTable = true;
	
	/**
	 *  determines whether TF is the source system in this scenario
	 */
	private Boolean sourceSystemIsTeamForge = null;

	private static final String NULL_VALUE = null;

	public Object[] process(Object data) {
		// I will expect a Generic Artifact object
		if (data instanceof Document) {

			String depParentSourceArtifactId = null;
			String depParentSourceRepositoryId = null;
			String depParentSourceRepositoryKind = null;
			String depParentTargetArtifactId = null;
			String depParentTargetRepositoryId = null;
			String depParentTargetRepositoryKind = null;

			try {
				Element element = XPathUtils.getRootElement((Document) data);
				
				/*
				 *  CCF 2.x needs an id for the repository mapping and for the repository mapping direction
				 *  Those are stored in the source system kind and target system kind top level attributes.
				 *  These top level attributes have not been used in CCF 1.x but have already been mapped so
				 *  that CCF 1.x mappings will continue to work with CCF 2.x without any modifications.
				 */
				// FIXME Do we have to parse these ids before passing them to the DB layer?
				String repositoryMappingDirectionId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.SOURCE_SYSTEM_KIND);
				String repositoryMappingId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TARGET_SYSTEM_KIND);
				

				String artifactAction = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.ARTIFACT_ACTION);

				String sourceArtifactId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.SOURCE_ARTIFACT_ID);

				String sourceArtifactVersion = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.SOURCE_ARTIFACT_VERSION);

				String sourceSystemId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.SOURCE_SYSTEM_ID);

				String sourceRepositoryId = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.SOURCE_REPOSITORY_ID);

				String targetSystemId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TARGET_SYSTEM_ID);

				String targetRepositoryId = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.TARGET_REPOSITORY_ID);

				String sourceArtifactLastModifiedDateString = XPathUtils
						.getAttributeValue(
								element,
								GenericArtifactHelper.SOURCE_ARTIFACT_LAST_MODIFICATION_DATE);
				String artifactType = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.ARTIFACT_TYPE);

				String transactionId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TRANSACTION_ID);

				boolean replayedArtifact = (transactionId != null && !transactionId
						.equals(GenericArtifact.VALUE_UNKNOWN));

				java.util.Date sourceLastModifiedDate = null;
				if (sourceArtifactLastModifiedDateString
						.equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
					String message = "Source artifact last modified date is populated as: "
							+ sourceArtifactLastModifiedDateString;
					log.warn(message);
					// use the earliest date possible
					sourceLastModifiedDate = new Date(0);
				} else {
					sourceLastModifiedDate = DateUtil
							.parse(sourceArtifactLastModifiedDateString);
				}

				java.sql.Timestamp sourceTime = new java.sql.Timestamp(
						sourceLastModifiedDate.getTime());

				if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_IGNORE)) {
					if (!replayedArtifact
							&& updateSynchronizationStatusTable
							&& !artifactType
									.equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
						updateSynchronizationStatusTable(data,
								sourceArtifactId, sourceArtifactVersion, sourceTime, repositoryMappingDirectionId);
					}
					return new Object[] { data };
				}

				String sourceSystemKind = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.SOURCE_SYSTEM_KIND);
				String sourceRepositoryKind = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.SOURCE_REPOSITORY_KIND);

				String targetArtifactId = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TARGET_ARTIFACT_ID);

				String targetSystemKind = XPathUtils.getAttributeValue(element,
						GenericArtifactHelper.TARGET_SYSTEM_KIND);

				String targetRepositoryKind = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.TARGET_REPOSITORY_KIND);

				String targetArtifactLastModifiedDateString = XPathUtils
						.getAttributeValue(
								element,
								GenericArtifactHelper.TARGET_ARTIFACT_LAST_MODIFICATION_DATE);

				String targetArtifactVersion = XPathUtils.getAttributeValue(
						element, GenericArtifactHelper.TARGET_ARTIFACT_VERSION);

				if (artifactType
						.equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
					depParentSourceArtifactId = XPathUtils
							.getAttributeValue(
									element,
									GenericArtifactHelper.DEP_PARENT_SOURCE_ARTIFACT_ID);
					depParentSourceRepositoryId = XPathUtils
							.getAttributeValue(
									element,
									GenericArtifactHelper.DEP_PARENT_SOURCE_REPOSITORY_ID);
					depParentSourceRepositoryKind = XPathUtils
							.getAttributeValue(
									element,
									GenericArtifactHelper.DEP_PARENT_SOURCE_REPOSITORY_KIND);
					depParentTargetArtifactId = XPathUtils
							.getAttributeValue(
									element,
									GenericArtifactHelper.DEP_PARENT_TARGET_ARTIFACT_ID);
					depParentTargetRepositoryId = XPathUtils
							.getAttributeValue(
									element,
									GenericArtifactHelper.DEP_PARENT_TARGET_REPOSITORY_ID);
					depParentTargetRepositoryKind = XPathUtils
							.getAttributeValue(
									element,
									GenericArtifactHelper.DEP_PARENT_TARGET_REPOSITORY_KIND);
				}

				java.util.Date targetLastModifiedDate = null;

				if (targetArtifactLastModifiedDateString
						.equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
					String message = "Target artifact last modified date is populated as: "
							+ targetArtifactLastModifiedDateString;
					log.warn(message);
					// use the earliest date possible
					targetLastModifiedDate = new Date(0);
				} else {
					// targetArtifactLastModifiedDateString = "June 26, 2008
					// 11:02:26 AM GMT+05:30";
					targetLastModifiedDate = DateUtil
							.parse(targetArtifactLastModifiedDateString);
				}

				java.sql.Timestamp targetTime = new java.sql.Timestamp(
						targetLastModifiedDate.getTime());

				createMapping(element, sourceArtifactId, sourceRepositoryId,
						sourceRepositoryKind, sourceSystemId, sourceSystemKind,
						targetArtifactId, targetRepositoryId,
						targetRepositoryKind, targetSystemId, targetSystemKind,
						sourceTime, sourceArtifactVersion, targetTime,
						targetArtifactVersion, artifactType,
						depParentSourceArtifactId, depParentSourceRepositoryId,
						depParentSourceRepositoryKind,
						depParentTargetArtifactId, depParentTargetRepositoryId,
						depParentTargetRepositoryKind, NULL_VALUE, NULL_VALUE,
						NULL_VALUE, NULL_VALUE, NULL_VALUE, NULL_VALUE, repositoryMappingId);

				if (!replayedArtifact
						&& updateSynchronizationStatusTable
						&& !artifactType
								.equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
					updateSynchronizationStatusTable(data, sourceArtifactId,
							sourceArtifactVersion, sourceTime, repositoryMappingDirectionId);
				}
			} catch (GenericArtifactParsingException e) {
				log
						.error("There is some problem in extracting attributes from Document in EntityService!!!"
								+ e);
			}
		} else {
			String message = "The Mapping updater needs a GenericArtifact object";
			message += " But it got something else.";
			log.error(message);
			throw new CCFRuntimeException(message);
		}

		return new Object[] { data };
	}

	private void updateSynchronizationStatusTable(Object data,
			String sourceArtifactId, String sourceArtifactVersion,
			java.sql.Timestamp sourceTime, String repositoryMappingDirectionId) {
		IOrderedMap inputParameters = new OrderedHashMap();
		inputParameters.add(0, "LAST_SOURCE_ARTIFACT_MODIFICATION_DATE",
				sourceTime);
		inputParameters.add(1, "LAST_SOURCE_ARTIFACT_VERSION",
				sourceArtifactVersion);
		inputParameters.add(2, "LAST_SOURCE_ARTIFACT_ID", sourceArtifactId);
		inputParameters.add(3, REPOSITORY_MAPPING_DIRECTION, repositoryMappingDirectionId);
		
		IOrderedMap[] params = new IOrderedMap[] { inputParameters };
		synchronizationStatusDatabaseUpdater.connect();
		synchronizationStatusDatabaseUpdater.deliver(params);
		//synchronizationStatusDatabaseUpdater.disconnect();
	}

	private void createMapping(Element element, String sourceArtifactId,
			String sourceRepositoryId, String sourceRepositoryKind,
			String sourceSystemId, String sourceSystemKind,
			String targetArtifactId, String targetRepositoryId,
			String targetRepositoryKind, String targetSystemId,
			String targetSystemKind, java.sql.Timestamp sourceTime,
			String sourceArtifactVersion, java.sql.Timestamp targetTime,
			String targetArtifactVersion, String artifactType,
			String depParentSourceArtifactId,
			String depParentSourceRepositoryId,
			String depParentSourceRepositoryKind,
			String depParentTargetArtifactId,
			String depParentTargetRepositoryId,
			String depParentTargetRepositoryKind,
			String depChildSourceArtifactId, String depChildSourceRepositoryId,
			String depChildSourceRepositoryKind,
			String depChildTargetArtifactId, String depChildTargetRepositoryId,
			String depChildTargetRepositoryKind, String repositoryMappingId) {

		OrderedHashMap identityMappingTableRow = lookupIdentiyMappingRow(element,
				sourceArtifactId, sourceSystemId, sourceRepositoryId,
				targetSystemId, targetRepositoryId, artifactType, repositoryMappingId); 
		String targetArtifactIdFromTable = null;
		if (identityMappingTableRow != null) {
			targetArtifactIdFromTable = (String) identityMappingTableRow.get(0);
		}
		if (targetArtifactIdFromTable == null) {
			this.createIdentityMapping(sourceArtifactId, targetArtifactId,
					sourceTime, targetTime, sourceArtifactVersion,
					targetArtifactVersion, artifactType,
					depParentSourceArtifactId, depParentSourceRepositoryId,
					depParentSourceRepositoryKind, depParentTargetArtifactId,
					depParentTargetRepositoryId, depParentTargetRepositoryKind,
					depChildSourceArtifactId, depChildSourceRepositoryId,
					depChildSourceRepositoryKind, depChildTargetArtifactId,
					depChildTargetRepositoryId, depChildTargetRepositoryKind, repositoryMappingId);
		} else {
			if (identityMappingTableRow.size() > 3) {
				//Timestamp sourceLastModificationTimeFromTable = (Timestamp) identityMappingTableRow.get(1);
				long sourceArtifactVersionFromTable = parseVersionNumber(identityMappingTableRow.get(2));
				long targetArtifactVersionFromTable = parseVersionNumber(identityMappingTableRow.get(3));
				long sourceArtifactVersionConverted = parseVersionNumber(sourceArtifactVersion);
				long targetArtifactVersionConverted = parseVersionNumber(targetArtifactVersion);
				
				if (sourceArtifactVersionConverted < sourceArtifactVersionFromTable) {
					log.debug("overriding incoming source artifact version ("+sourceArtifactVersionConverted+
							") with version from id-mapping-table ("+sourceArtifactVersionFromTable+")");
					sourceArtifactVersion = Long.toString(sourceArtifactVersionFromTable);
					//sourceTime = sourceLastModificationTimeFromTable;
				}
				
				if (targetArtifactVersionConverted < targetArtifactVersionFromTable) {
					log.debug("overriding incoming target artifact version ("+targetArtifactVersionConverted+
							") with version from id-mapping-table ("+targetArtifactVersionFromTable+")");
					targetArtifactVersion = Long.toString(targetArtifactVersionFromTable);
				}
			}
			this.updateIdentityMapping(sourceArtifactId,
					sourceTime, targetTime, sourceArtifactVersion,
					targetArtifactVersion, artifactType, repositoryMappingId);
			log.debug("Mapping already exists for source artifact id "
					+ sourceArtifactId + " target artifact id "
					+ targetArtifactId + " for repository info "
					+ sourceArtifactId + "+" + sourceSystemId + "+"
					+ sourceRepositoryId + "+" + targetSystemId);
		}

	}
	
	
	/**
	 * Tries to convert the input to a number. If input is null or input.toString() cannot be parsed,
	 * returns -2, because -1 is already in use to indicate systems that don't support version control.
	 * @param input 
	 * @return the parsed value of the input, or -2 if input is null or its string representation cannot be parsed.
	 */
	private long parseVersionNumber(Object input) {
		if (input == null) return -2;
		try {
			return Long.parseLong(input.toString());
		} catch (NumberFormatException nfe) {
			return -2;
		}
	}

	private OrderedHashMap lookupIdentiyMappingRow(Element element,
			String sourceArtifactId, String sourceSystemId,
			String sourceRepositoryId, String targetSystemId,
			String targetRepositoryId, String artifactType, String repositoryMappingId) {
		OrderedHashMap result = null;
		IOrderedMap inputParameters = new OrderedHashMap();

		inputParameters.add(repositoryMappingId);
		inputParameters.add(sourceArtifactId);
		inputParameters.add(artifactType);

		identityMappingDatabaseReader.connect();
		Object[] resultSet = identityMappingDatabaseReader.next(
				inputParameters, 1000);
		//identityMappingDatabaseReader.disconnect();

		if (resultSet == null || resultSet.length == 0) {
			result = null;
		} else if (resultSet.length == 1) {
			if (resultSet[0] instanceof OrderedHashMap) {
				result = (OrderedHashMap) resultSet[0];
				if (result.size() > 0) {
					return result;
				} else {
					String cause = "Seems as if the SQL statement for identityMappingDatabase reader does not return values.";
					XPathUtils
							.addAttribute(
									element,
									GenericArtifactHelper.ERROR_CODE,
									GenericArtifact.ERROR_INTERNAL_DATABASE_TABLE_CORRUPT);
					log.error(cause);
					throw new CCFRuntimeException(cause);
				}
			} else {
				String cause = "SQL query on identity mapping table did not return data in correct format!";
				XPathUtils.addAttribute(element,
						GenericArtifactHelper.ERROR_CODE,
						GenericArtifact.ERROR_INTERNAL_DATABASE_TABLE_CORRUPT);
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		} else {
			String cause = "There is more than one mapping for the combination "
					+ sourceArtifactId
					+ "-"
					+ sourceRepositoryId
					+ "-"
					+ sourceSystemId
					+ targetRepositoryId
					+ "-"
					+ targetSystemId + " in the identity mapping table.";
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_INTERNAL_DATABASE_TABLE_CORRUPT);
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}
		return result;
	}

	private void createIdentityMapping(String sourceArtifactId,
			String targetArtifactId, java.sql.Timestamp sourceTime,
			java.sql.Timestamp targetTime, String sourceArtifactVersion,
			String targetArtifactVersion, String artifactType,
			String depParentSourceArtifactId,
			String depParentSourceRepositoryId,
			String depParentSourceRepositoryKind,
			String depParentTargetArtifactId,
			String depParentTargetRepositoryId,
			String depParentTargetRepositoryKind,
			String depChildSourceArtifactId, String depChildSourceRepositoryId,
			String depChildSourceRepositoryKind,
			String depChildTargetArtifactId, String depChildTargetRepositoryId,
			String depChildTargetRepositoryKind, String repositoryMapping) {
		IOrderedMap inputParameters = new OrderedHashMap();
		
		/*
		 * Starting from CCF 2.x we only have one identity mapping row for both directions
		 * Consequently, we have to know whether this is a forward or reverse process to properly
		 * create the entry
		 */
		boolean b = isSourceSystemIsTeamForge();

		inputParameters.add(0, REPOSITORY_MAPPING, repositoryMapping);
		inputParameters.add(1, VERSION, 0);
		inputParameters.add(2, DESCRIPTION, "This identity mapping has been inserted by CCF Core");
		
		inputParameters.add(3, SOURCE_ARTIFACT_ID, b?sourceArtifactId:targetArtifactId);
		inputParameters.add(4, TARGET_ARTIFACT_ID, b?targetArtifactId:sourceArtifactId);
		inputParameters.add(5, SOURCE_LAST_MODIFICATION_TIME, b?sourceTime:targetTime);
		inputParameters.add(6, TARGET_LAST_MODIFICATION_TIME, b?targetTime:sourceTime);
		inputParameters.add(7, SOURCE_ARTIFACT_VERSION, b?sourceArtifactVersion:targetArtifactVersion);
		inputParameters.add(8, TARGET_ARTIFACT_VERSION, b?targetArtifactVersion:sourceArtifactVersion);
		inputParameters.add(9, ARTIFACT_TYPE, artifactType);
		inputParameters.add(10, DEP_CHILD_SOURCE_ARTIFACT_ID,
				b?depChildSourceArtifactId:depChildTargetArtifactId);
		inputParameters.add(11, DEP_CHILD_SOURCE_REPOSITORY_ID,
				b?depChildSourceRepositoryId:depChildTargetRepositoryId);
		inputParameters.add(12, DEP_CHILD_SOURCE_REPOSITORY_KIND,
				b?depChildSourceRepositoryKind:depChildTargetRepositoryKind);
		inputParameters.add(13, DEP_CHILD_TARGET_ARTIFACT_ID,
				b?depChildTargetArtifactId:depChildSourceArtifactId);
		inputParameters.add(14, DEP_CHILD_TARGET_REPOSITORY_ID,
				b?depChildTargetRepositoryId:depChildSourceRepositoryId);
		inputParameters.add(15, DEP_CHILD_TARGET_REPOSITORY_KIND,
				b?depChildTargetRepositoryKind:depChildSourceRepositoryKind);
		inputParameters.add(16, DEP_PARENT_SOURCE_ARTIFACT_ID,
				b?depParentSourceArtifactId:depParentTargetArtifactId);
		inputParameters.add(17, DEP_PARENT_SOURCE_REPOSITORY_ID,
				b?depParentSourceRepositoryId:depParentTargetRepositoryId);
		inputParameters.add(18, DEP_PARENT_SOURCE_REPOSITORY_KIND,
				b?depParentSourceRepositoryKind:depParentTargetRepositoryKind);
		inputParameters.add(19, DEP_PARENT_TARGET_ARTIFACT_ID,
				b?depParentTargetArtifactId:depParentSourceArtifactId);
		inputParameters.add(20, DEP_PARENT_TARGET_REPOSITORY_ID,
				b?depParentTargetRepositoryId:depParentSourceRepositoryId);
		inputParameters.add(21, DEP_PARENT_TARGET_REPOSITORY_KIND,
				b?depParentTargetRepositoryKind:depParentSourceArtifactId);

		IOrderedMap[] data = new IOrderedMap[] { inputParameters };
		identityMappingDatabaseInserter.connect();
		identityMappingDatabaseInserter.deliver(data);
		// identityMappingDatabaseInserter.disconnect();
	}

	private void updateIdentityMapping(String sourceArtifactId,
			java.sql.Timestamp sourceTime, java.sql.Timestamp targetTime,
			String sourceArtifactVersion, String targetArtifactVersion,
			String artifactType, String repositoryMappingId) {
		IOrderedMap inputParameters = new OrderedHashMap();

		inputParameters.add(0, SOURCE_LAST_MODIFICATION_TIME, sourceTime);
		inputParameters.add(1, TARGET_LAST_MODIFICATION_TIME, targetTime);
		inputParameters.add(2, SOURCE_ARTIFACT_VERSION, sourceArtifactVersion);
		inputParameters.add(3, TARGET_ARTIFACT_VERSION, targetArtifactVersion);
		inputParameters.add(4, REPOSITORY_MAPPING, repositoryMappingId);
		inputParameters.add(5, SOURCE_ARTIFACT_ID, sourceArtifactId);
		inputParameters.add(6, ARTIFACT_TYPE, artifactType);

		IOrderedMap[] params = new IOrderedMap[] { inputParameters };
		identityMappingDatabaseUpdater.connect();
		identityMappingDatabaseUpdater.deliver(params);
		// identityMappingDatabaseUpdater.disconnect();
	}

	public void reset(Object context) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		if (getSynchronizationStatusDatabaseUpdater() == null) {
			log.error("synchronizationStatusDatabaseUpdater-property not set");
			exceptions.add(new ValidationException(
					"synchronizationStatusDatabaseUpdater-property not set",
					this));
		}

		if (getIdentityMappingDatabaseReader() == null) {
			log.error("identityMappingDatabaseReader-property not set");
			exceptions.add(new ValidationException(
					"identityMappingDatabaseReader-property not set", this));
		}

		if (getSynchronizationStatusDatabaseUpdater() == null) {
			log.error("synchronizationStatusDatabaseUpdater-property not set");
			exceptions.add(new ValidationException(
					"synchronizationStatusDatabaseUpdater-property not set",
					this));
		}

		if (getIdentityMappingDatabaseUpdater() == null) {
			log.error("getIdentityMappingDatabaseUpdater-property not set");
			exceptions
					.add(new ValidationException(
							"getIdentityMappingDatabaseUpdater-property not set",
							this));
		}

		if (getIdentityMappingDatabaseInserter() == null) {
			log.error("getIdentityMappingDatabaseInserter-property not set");
			exceptions
					.add(new ValidationException(
							"getIdentityMappingDatabaseInserter-property not set",
							this));
		}
		
		if (sourceSystemIsTeamForge == null) {
			log.error("sourceSystemIsTeamForge-property not set");
			exceptions
					.add(new ValidationException(
							"sourceSystemIsTeamForge-property not set",
							this));
		}
	}

	/**
	 * Gets the database updater to update the synchronization status table
	 * 
	 * @return
	 */
	public JDBCWriteConnector getSynchronizationStatusDatabaseUpdater() {
		return synchronizationStatusDatabaseUpdater;
	}

	/**
	 * Sets the database updater to update the synchronization status table
	 * 
	 * @param synchronizationStatusDatabaseUpdater
	 */
	public void setSynchronizationStatusDatabaseUpdater(
			JDBCWriteConnector synchronizationStatusDatabaseUpdater) {
		this.synchronizationStatusDatabaseUpdater = synchronizationStatusDatabaseUpdater;
	}

	/**
	 * Gets the data base inserter that is used to insert a new artifact mapping
	 * into the identity mapping table
	 * 
	 * @return
	 */
	public JDBCWriteConnector getIdentityMappingDatabaseInserter() {
		return identityMappingDatabaseInserter;
	}

	/**
	 * Sets the data base inserter that is used to insert a new artifact mapping
	 * into the identity mapping table
	 * 
	 * @param identityMappingDatabaseInserter
	 */
	public void setIdentityMappingDatabaseInserter(
			JDBCWriteConnector identityMappingDatabaseInserter) {
		this.identityMappingDatabaseInserter = identityMappingDatabaseInserter;
	}

	/**
	 * Gets the (mandatory) data base reader that is used to retrieve the target
	 * artifact id from the the identity mapping table.
	 * 
	 * @param identityMappingDatabaseReader
	 */
	public JDBCReadConnector getIdentityMappingDatabaseReader() {
		return identityMappingDatabaseReader;
	}

	/**
	 * Sets the (mandatory) data base reader that is used to retrieve the target
	 * artifact id from the the identity mapping table.
	 * 
	 * @param identityMappingDatabaseReader
	 */
	public void setIdentityMappingDatabaseReader(
			JDBCReadConnector identityMappingDatabaseReader) {
		this.identityMappingDatabaseReader = identityMappingDatabaseReader;
	}

	/**
	 * Gets the data base updater that is used to update an existing artifact
	 * mapping in the identity mapping table
	 * 
	 * @return
	 */
	public JDBCWriteConnector getIdentityMappingDatabaseUpdater() {
		return identityMappingDatabaseUpdater;
	}

	/**
	 * Sets the data base updater that is used to update an existing artifact
	 * mapping in the identity mapping table
	 * 
	 * @param identityMappingUpdater
	 */
	public void setIdentityMappingDatabaseUpdater(
			JDBCWriteConnector identityMappingUpdater) {
		this.identityMappingDatabaseUpdater = identityMappingUpdater;
	}

	/**
	 * 
	 * Define whether the synchronization status table should be updated. The
	 * default value is true and should be only set to false for the hospital
	 * replay scenario
	 * 
	 * @param updateSynchronizationStatusTable
	 *            true if update should happen
	 */
	public void setUpdateSynchronizationStatusTable(
			boolean updateSynchronizationStatusTable) {
		this.updateSynchronizationStatusTable = updateSynchronizationStatusTable;
	}

	/**
	 * Indicates whether the synchronization status table will be updated. The
	 * default value is true and should be only set to false for the hospital
	 * replay scenario
	 */
	public boolean isUpdateSynchronizationStatusTable() {
		return updateSynchronizationStatusTable;
	}

	/**
	 * Determines whether TF is the source system in this scenario
	 * @param sourceSystemIsTeamForge
	 */
	public void setSourceSystemIsTeamForge(boolean sourceSystemIsTeamForge) {
		this.sourceSystemIsTeamForge = sourceSystemIsTeamForge;
	}

	/**
	 * 
	 * Determines whether TF is the source system in this scenario
	 * 
	 * @return
	 */
	public boolean isSourceSystemIsTeamForge() {
		return sourceSystemIsTeamForge;
	}

}
