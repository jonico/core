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

package com.collabnet.ccf.core;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * This component will find out whether an artifact coming out of the source
 * system has to be created, updated, deleted or ignored within the target
 * system. It will also try to find out the correct id of the artifact on the
 * target system.
 * 
 * It checks the version of the source artifact to see if that version of the
 * artifact already shipped there by avoiding duplicate artifact shipment.
 * 
 * Optionally, it can also find out whether the artifact has been quarantined in
 * the hospital and skip it.
 * 
 * @author jnicolai
 * 
 */
public class EntityService extends LifecycleComponent implements IDataProcessor {
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(EntityService.class);

	private JDBCReadConnector identityMappingDatabaseReader = null;

	private JDBCReadConnector hospitalDatabaseReader = null;

	private boolean skipNewerVersionsOfQuarantinedAttachments;

	private long identityMapEventWaitTime = 500L;

	private int identityMapEventWaitCount = 4;

	/**
	 * openAdaptor Method to process all input and puts out the results This
	 * method will only handle Dom4J documents encoded in the generic XML schema
	 */
	public Object[] process(Object data) {
		if (data == null) {
			String cause = "Expected Document. Null record not permitted.";
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		if (!(data instanceof Document)) {
			String cause = "Expected Document. Got ["
					+ data.getClass().getName() + "]";
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

		return processXMLDocument((Document) data);
	}

	/**
	 * Main method to handle the mapping and filtering of source artifacts to
	 * target repository artifact items Also include the quarantined artifact
	 * lookup code
	 * 
	 * @param data
	 *            input XML document in generic XML artifact format
	 * @return array of generated XML documents compliant to generic XML
	 *         artifact schema
	 */
	private Object[] processXMLDocument(Document data) {
		Element element = null;
		try {
			element = XPathUtils.getRootElement(data);
			String artifactAction = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.ARTIFACT_ACTION);
			if (artifactAction
					.equals(GenericArtifactHelper.ARTIFACT_ACTION_IGNORE)) {
				return new Object[] { data };
			}

			// get top level attributes
			String artifactType = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.ARTIFACT_TYPE);
			String sourceArtifactId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.SOURCE_ARTIFACT_ID);
			String sourceSystemId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.SOURCE_SYSTEM_ID);
			String sourceRepositoryId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.SOURCE_REPOSITORY_ID);
			String targetSystemId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.TARGET_SYSTEM_ID);
			String targetRepositoryId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.TARGET_REPOSITORY_ID);
			String sourceArtifactVersion = XPathUtils.getAttributeValue(
					element, GenericArtifactHelper.SOURCE_ARTIFACT_VERSION);
			String transactionId = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.TRANSACTION_ID);

			boolean replayedArtifact = (transactionId != null && !transactionId
					.equals(GenericArtifact.VALUE_UNKNOWN));

			if (sourceArtifactVersion == null
					|| sourceArtifactVersion
							.equals(GenericArtifact.VALUE_UNKNOWN)) {
				sourceArtifactVersion = GenericArtifactHelper.ARTIFACT_VERSION_FORCE_RESYNC;
			}

			String sourceArtifactLastModifiedDateStr = XPathUtils
					.getAttributeValue(
							element,
							GenericArtifactHelper.SOURCE_ARTIFACT_LAST_MODIFICATION_DATE);

			Date sourceArtifactLastModifiedDate = null;
			if (!sourceArtifactLastModifiedDateStr
					.equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
				sourceArtifactLastModifiedDate = DateUtil
						.parse(sourceArtifactLastModifiedDateStr);
			}
			// use the earliest date possible
			else
				sourceArtifactLastModifiedDate = new Date(0);

			long sourceArtifactVersionLong = Long
					.parseLong(sourceArtifactVersion);
			String targetArtifactIdFromTable = null;
			String targetArtifactVersion = null;
			if (sourceArtifactId
					.equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
				return new Object[] { data };
			}

			// find out whether to skip the artifact because it has been
			// quarantined
			if (!replayedArtifact && skipQuarantinedArtifact(element, sourceArtifactId,
					sourceSystemId, sourceRepositoryId, targetSystemId,
					targetRepositoryId, artifactType,
					sourceArtifactLastModifiedDate, sourceArtifactVersionLong)) {
				XPathUtils.addAttribute(element,
						GenericArtifactHelper.ARTIFACT_ACTION,
						GenericArtifactHelper.ARTIFACT_ACTION_IGNORE);
				return new Object[] { data };
			}

			Object[] results = lookupTargetArtifact(element, sourceArtifactId,
					sourceSystemId, sourceRepositoryId, targetSystemId,
					targetRepositoryId, artifactType);

			if (results != null && results.length != 0) {
				targetArtifactIdFromTable = results[0].toString();
				Date sourceArtifactLastModifiedDateFromTable = (Date) results[1];
				String sourceArtifactVersionFromTable = results[2].toString();
				if (sourceArtifactVersionFromTable
						.equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
					sourceArtifactVersionFromTable = GenericArtifactHelper.ARTIFACT_VERSION_FORCE_RESYNC;
				}
				long sourceArtifactVersionLongFromTable = Long
						.parseLong(sourceArtifactVersionFromTable);
				//if (sourceArtifactLastModifiedDateFromTable
						//.after(sourceArtifactLastModifiedDate)
						//|| sourceArtifactVersionLongFromTable >= sourceArtifactVersionLong) {
				if (sourceArtifactVersionLongFromTable >= sourceArtifactVersionLong) {
					if (sourceArtifactVersionLong == -1
							&& sourceArtifactVersionLongFromTable == -1) {
						log
								.warn("It seems as if artifact synchronization is done exclusively with a system that does not support version control for combination "
										+ sourceArtifactId
										+ "-"
										+ sourceRepositoryId
										+ "-"
										+ sourceSystemId
										+ targetRepositoryId
										+ "-"
										+ targetSystemId
										+ " so artifact will not be skipped.");
					} else {
						// only skip if this is not an intended artifact resync
						// obvious resync duplicates are still filtered out
						if ((!artifactAction
								.equals(GenericArtifactHelper.ARTIFACT_ACTION_RESYNC))
								|| (sourceArtifactVersionLongFromTable > sourceArtifactVersionLong)) {
							log
									.warn("\nSource artifact last modified date in table "
											+ DateUtil
													.format(sourceArtifactLastModifiedDateFromTable)
											+ "\nSource artifact last modified date "
											+ DateUtil
													.format(sourceArtifactLastModifiedDate)
											+ "\nSource artifact version from table "
											+ sourceArtifactVersionLongFromTable
											+ "\nSource artifact version "
											+ sourceArtifactVersionLong);
							log
									.warn("Seems the artifact has already been shipped in newer or same version. Skipped artifact with source artifact id "
											+ sourceArtifactId
											+ " and version "
											+ sourceArtifactVersion
											+ " for combination "
											+ sourceArtifactId
											+ "-"
											+ sourceRepositoryId
											+ "-"
											+ sourceSystemId
											+ targetRepositoryId
											+ "-"
											+ targetSystemId);

							XPathUtils
									.addAttribute(
											element,
											GenericArtifactHelper.ARTIFACT_ACTION,
											GenericArtifactHelper.ARTIFACT_ACTION_IGNORE);
							return new Object[] { data };
						}
					}
				}
				targetArtifactVersion = results[3].toString();
			}
			if (artifactType
					.equals(GenericArtifactHelper.ARTIFACT_TYPE_ATTACHMENT)) {
				String sourceParentArtifactId = XPathUtils.getAttributeValue(
						element,
						GenericArtifactHelper.DEP_PARENT_SOURCE_ARTIFACT_ID);
				String sourceParentRepositoryId = XPathUtils.getAttributeValue(
						element,
						GenericArtifactHelper.DEP_PARENT_SOURCE_REPOSITORY_ID);
				String targetParentRepositoryId = XPathUtils.getAttributeValue(
						element,
						GenericArtifactHelper.DEP_PARENT_TARGET_REPOSITORY_ID);
				Object[] resultsDep = lookupTargetArtifact(element,
						sourceParentArtifactId, sourceSystemId,
						sourceParentRepositoryId, targetSystemId,
						targetParentRepositoryId,
						GenericArtifactHelper.ARTIFACT_TYPE_PLAIN_ARTIFACT);
				String targetParentArtifactId = null;
				if (resultsDep != null && resultsDep[0] != null) {
					targetParentArtifactId = resultsDep[0].toString();
				}
				if (StringUtils.isEmpty(targetParentArtifactId)) {
					String cause = "Parent artifact "
							+ sourceParentArtifactId
							+ " for attachment "
							+ sourceArtifactId
							+ " is not created on the target system for combination "
							+ sourceArtifactId + "-" + sourceRepositoryId + "-"
							+ sourceSystemId + targetRepositoryId + "-"
							+ targetSystemId;
					log.error(cause);
					XPathUtils
							.addAttribute(
									element,
									GenericArtifactHelper.ERROR_CODE,
									GenericArtifact.ERROR_INTERNAL_DATABASE_TABLE_CORRUPT);
					throw new CCFRuntimeException(cause);
				} else {
					XPathUtils
							.addAttribute(
									element,
									GenericArtifactHelper.DEP_PARENT_TARGET_ARTIFACT_ID,
									targetParentArtifactId);
				}
			}

			if (targetArtifactIdFromTable != null) {
				XPathUtils.addAttribute(element,
						GenericArtifactHelper.TARGET_ARTIFACT_ID,
						targetArtifactIdFromTable);
				XPathUtils.addAttribute(element,
						GenericArtifactHelper.TARGET_ARTIFACT_VERSION,
						targetArtifactVersion);
				if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_UNKNOWN)) {
					XPathUtils.addAttribute(element,
							GenericArtifactHelper.ARTIFACT_ACTION,
							GenericArtifactHelper.ARTIFACT_ACTION_UPDATE);
				} else if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_UPDATE)) {
					// Do nothing. Because the artifact is already marked as
					// update
				} else if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_CREATE)) {
					String cause = "The artifact action is marked as "
							+ artifactAction
							+ ".\nBut the Entity Service found a target artifact id "
							+ targetArtifactIdFromTable
							+ " for source artifact id " + sourceArtifactId;
					log.warn(cause);
					XPathUtils.addAttribute(element,
							GenericArtifactHelper.ARTIFACT_ACTION,
							GenericArtifactHelper.ARTIFACT_ACTION_UPDATE);
				} else if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_DELETE)) {
					XPathUtils.addAttribute(element,
							GenericArtifactHelper.ARTIFACT_ACTION,
							GenericArtifactHelper.ARTIFACT_ACTION_DELETE);
				} else if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_RESYNC)) {
					// Do nothing. Because the artifact is already marked as
					// update
				}
			} else {
				if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_UNKNOWN)) {
					XPathUtils.addAttribute(element,
							GenericArtifactHelper.ARTIFACT_ACTION,
							GenericArtifactHelper.ARTIFACT_ACTION_CREATE);
				} else if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_UPDATE)) {
					String cause = "The artifact action is marked as "
							+ artifactAction
							+ ".\nBut the Entity Service could not find a target artifact id for source artifact id "
							+ sourceArtifactId
							+ ". Marking the artifact to create";
					log.warn(cause);
					XPathUtils.addAttribute(element,
							GenericArtifactHelper.ARTIFACT_ACTION,
							GenericArtifactHelper.ARTIFACT_ACTION_CREATE);
				} else if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_CREATE)) {
					// Do nothing. Because the artifact is already marked as
					// create
				} else if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_DELETE)) {
					String cause = "The artifact action is marked as "
							+ artifactAction
							+ ".\nBut the Entity Service could not find a target artifact id for source artifact id "
							+ sourceArtifactId;
					log.error(cause);
					throw new CCFRuntimeException(cause);
				} else if (artifactAction
						.equals(GenericArtifactHelper.ARTIFACT_ACTION_RESYNC)) {
					String cause = "The artifact action is marked as "
							+ artifactAction
							+ ".\nBut the Entity Service could not find a target artifact id for source artifact id "
							+ sourceArtifactId + ". Discarding the artifact.";
					log.warn(cause);
					XPathUtils.addAttribute(element,
							GenericArtifactHelper.ARTIFACT_ACTION,
							GenericArtifactHelper.ARTIFACT_ACTION_IGNORE);
					return new Object[] { data };
				}
			}
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the Document to extract specific attributes";
			log.error(cause, e);
			XPathUtils.addAttribute(data.getRootElement(),
					GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}

		Object[] result = { data };
		return result;
	}

	/**
	 * For a given source artifact id, source repository and the target
	 * repository details, this method finds out the target artifact id mapped
	 * to the source artifact id by looking up the identity mapping table.
	 * 
	 * If the source artifact id had ever passed through the wiring the identity
	 * mapping will contain the corresponding target artifact id. This method
	 * fetches the target artifact id and returns. If there is no target
	 * artifact id mapped to this source artifact id this method return a null.
	 * 
	 * @param sourceArtifactId
	 *            - The source artifact id that should be looked up for a target
	 *            artifact id
	 * @param sourceSystemId
	 *            - The system id of the source repository
	 * @param sourceRepositoryId
	 *            - The repository id of the source artifact
	 * @param targetSystemId
	 *            - The system id of the target repository
	 * @param targetRepositoryId
	 *            - The repository id of the target artifact
	 * @param artifactType
	 *            - The artifact type
	 * 
	 * @return array with target artifactId, sourceArtifactLastModifiedDate,
	 *         sourceArtifactVersion, targetArtifactVersion (in this order)
	 */
	private Object[] lookupTargetArtifact(Element element,
			String sourceArtifactId, String sourceSystemId,
			String sourceRepositoryId, String targetSystemId,
			String targetRepositoryId, String artifactType) {
		IOrderedMap inputParameters = new OrderedHashMap();

		inputParameters.add(sourceSystemId);
		inputParameters.add(sourceRepositoryId);
		inputParameters.add(targetSystemId);
		inputParameters.add(targetRepositoryId);
		inputParameters.add(sourceArtifactId);
		inputParameters.add(artifactType);
		String artifactAction = null;
		try {
			artifactAction = XPathUtils.getAttributeValue(element,
					GenericArtifactHelper.ARTIFACT_ACTION);
		} catch (GenericArtifactParsingException e) {
			String cause = "Problem occured while parsing the Document to extract specific attributes";
			log.error(cause, e);
			XPathUtils.addAttribute(element, GenericArtifactHelper.ERROR_CODE,
					GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
			throw new CCFRuntimeException(cause, e);
		}
		boolean waitForIdentityMappedEvent = false;
		int waitCount = 0;
		//identityMappingDatabaseReader.disconnect();
		Object[] resultSet = null;
		do {
			identityMappingDatabaseReader.connect();
			resultSet = identityMappingDatabaseReader.next(inputParameters,
					1000);
			// identityMappingDatabaseReader.disconnect();
			if (artifactAction
					.equals(GenericArtifactHelper.ARTIFACT_ACTION_RESYNC)) {
				if (resultSet == null || resultSet.length == 0) {
					if (++waitCount < this.getIdentityMapEventWaitCount()) {
						waitForIdentityMappedEvent = true;
						try {
							log
									.debug("No identity mapping found for resync request of "
											+ sourceArtifactId
											+ "-"
											+ sourceRepositoryId
											+ "-"
											+ sourceSystemId
											+ targetRepositoryId
											+ "-"
											+ targetSystemId
											+ ". Sleeping for "
											+ this
													.getIdentityMapEventWaitTime()
											+ " milliseconds...");
							Thread.sleep(this.getIdentityMapEventWaitTime());
						} catch (InterruptedException e) {
							// INFO Digesting the interrupt
						}
					} else {
						log
								.warn("There was no prior identity mapping for the resync request "
										+ sourceArtifactId
										+ "-"
										+ sourceRepositoryId
										+ "-"
										+ sourceSystemId
										+ targetRepositoryId
										+ "-"
										+ targetSystemId
										+ " in the identity mapping table even after the entity service waited for "
										+ (waitCount * this
												.getIdentityMapEventWaitTime())
										+ " milliseconds. Discarding the artifact.");
						waitForIdentityMappedEvent = false;
					}
				} else {
					waitForIdentityMappedEvent = false;
				}
			}
		} while (waitForIdentityMappedEvent);
		Object[] results = null;
		if (resultSet == null || resultSet.length == 0) {
			log.debug(sourceArtifactId + "-" + sourceRepositoryId + "-"
					+ sourceSystemId + targetRepositoryId + "-"
					+ targetSystemId + " are not mapped.");
		} else if (resultSet.length == 1) {
			if (resultSet[0] instanceof OrderedHashMap) {
				OrderedHashMap result = (OrderedHashMap) resultSet[0];
				if (result.size() == 4) {
					results = new Object[4];
					results[0] = result.get(0);
					Timestamp timeStamp = (Timestamp) result.get(1);
					Date date;
					if (timeStamp == null) {
						// use earliest date possible
						date = new Date(0);
					} else {
						date = new Date(timeStamp.getTime());
					}

					results[1] = date;
					results[2] = result.get(2);
					results[3] = result.get(3);
				} else {
					String cause = "Seems as if the SQL statement for identityMappingDatabase reader does not return 4 values.";
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
		return results;
	}

	/**
	 * This method looks up whether the artifact has been quarantined in the
	 * hospital and has not been reprocessed yet. If so, it will find out
	 * whether to skip the artifact or not.
	 * 
	 * @param sourceArtifactId
	 *            - The source artifact id that should be looked up for a target
	 *            artifact id
	 * @param sourceSystemId
	 *            - The system id of the source repository
	 * @param sourceRepositoryId
	 *            - The repository id of the source artifact
	 * @param targetSystemId
	 *            - The system id of the target repository
	 * @param targetRepositoryId
	 *            - The repository id of the target artifact
	 * @param artifactType
	 *            - The artifact type
	 * @param sourceArtifactVersionInt
	 *            version of the current artifact
	 * @param sourceArtifactLastModifiedDate
	 *            last modified date of the current artifact
	 * 
	 * @return true if artifact should be skipped, false if not
	 */
	private boolean skipQuarantinedArtifact(Element element,
			String sourceArtifactId, String sourceSystemId,
			String sourceRepositoryId, String targetSystemId,
			String targetRepositoryId, String artifactType,
			Date sourceArtifactLastModifiedDate, long sourceArtifactVersionLong) {

		// only if a connection to the hospital table is possible we can skip
		// artifacts
		if (getHospitalDatabaseReader() == null) {
			return false;
		}

		IOrderedMap inputParameters = new OrderedHashMap();

		inputParameters.add(sourceSystemId);
		inputParameters.add(sourceRepositoryId);
		inputParameters.add(targetSystemId);
		inputParameters.add(targetRepositoryId);
		inputParameters.add(sourceArtifactId);
		inputParameters.add(artifactType);
		//hospitalDatabaseReader.disconnect();
		hospitalDatabaseReader.connect();
		// TODO Find out whether 10000 is enough in hard cases
		Object[] resultSet = hospitalDatabaseReader
				.next(inputParameters, 10000);
		// hospitalDatabaseReader.disconnect();
		if (resultSet == null || resultSet.length == 0) {
			// artifact is not in the hospital
			return false;
		} else if (isSkipNewerVersionsOfQuarantinedArtifacts()) {
			// we do not have to compare version numbers since we skip every
			// version of the artifact
			log
					.warn("At least one entry for combination "
							+ sourceArtifactId
							+ "-"
							+ sourceRepositoryId
							+ "-"
							+ sourceSystemId
							+ targetRepositoryId
							+ "-"
							+ targetSystemId
							+ " is still in the hospital and not yet reprocessed, skipping it ...");
			return true;
		}

		for (Object resultObject : resultSet) {
			if (resultObject instanceof OrderedHashMap) {
				OrderedHashMap result = (OrderedHashMap) resultObject;
				if (result.size() == 3) {
					Date sourceArtifactLastModifiedDateFromTable;
					String hospitalId = result.get(0).toString();
					Timestamp timeStamp = (Timestamp) result.get(1);
					if (timeStamp == null) {
						// use earliest date possible
						sourceArtifactLastModifiedDateFromTable = new Date(0);
					} else {
						sourceArtifactLastModifiedDateFromTable = new Date(
								timeStamp.getTime());
					}
					String sourceArtifactVersionFromTable = result.get(2)
							.toString();
					if (sourceArtifactVersionFromTable
							.equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
						sourceArtifactVersionFromTable = GenericArtifactHelper.ARTIFACT_VERSION_FORCE_RESYNC;
					}
					long sourceArtifactVersionLongFromTable = Long
							.parseLong(sourceArtifactVersionFromTable);
					if (sourceArtifactLastModifiedDateFromTable
							.after(sourceArtifactLastModifiedDate)
							|| sourceArtifactVersionLongFromTable >= sourceArtifactVersionLong) {
						if (sourceArtifactVersionLong == -1
								&& sourceArtifactVersionLongFromTable == -1) {
							log
									.warn("It seems as if artifact synchronization is done exclusively with a system that does not support version control, so artifact from combination "
											+ sourceArtifactId
											+ "-"
											+ sourceRepositoryId
											+ "-"
											+ sourceSystemId
											+ targetRepositoryId
											+ "-"
											+ targetSystemId
											+ " will not be skipped despite it is in the hospital. Hospital ID: "
											+ hospitalId);
						} else {
							log
									.warn("Non re-processed artifact from combination "
											+ sourceArtifactId
											+ "-"
											+ sourceRepositoryId
											+ "-"
											+ sourceSystemId
											+ targetRepositoryId
											+ "-"
											+ targetSystemId
											+ " is still in the hospital in a newer or equal version, so skipping it. Hospital ID: "
											+ hospitalId);
							return true;
						}
					}
				} else {
					String cause = "Seems as if the SQL statement for hospitalDatabase reader does not return 3 values.";
					XPathUtils
							.addAttribute(
									element,
									GenericArtifactHelper.ERROR_CODE,
									GenericArtifact.ERROR_INTERNAL_DATABASE_TABLE_CORRUPT);
					log.error(cause);
					throw new CCFRuntimeException(cause);
				}
			} else {
				String cause = "SQL query on hospital table did not return data in correct format!";
				XPathUtils.addAttribute(element,
						GenericArtifactHelper.ERROR_CODE,
						GenericArtifact.ERROR_INTERNAL_DATABASE_TABLE_CORRUPT);
				log.error(cause);
				throw new CCFRuntimeException(cause);
			}
		}
		log.info("Only older non-reprocessed versions of artifact combination "
				+ sourceArtifactId + "-" + sourceRepositoryId + "-"
				+ sourceSystemId + targetRepositoryId + "-" + targetSystemId
				+ " are in the hospital, so pass artifact ...");
		return false;
	}

	/**
	 * Reset the processor
	 */
	public void reset(Object context) {
	}

	@SuppressWarnings("unchecked")
	/*
	 * Validate whether all mandatory properties are set correctly
	 */
	public void validate(List exceptions) {
		super.validate(exceptions);
		if (getIdentityMappingDatabaseReader() == null) {
			log.error("identityMappingDatabaseReader-property not set");
			exceptions.add(new ValidationException(
					"identityMappingDatabaseReader-property not set", this));
		}
		if (getHospitalDatabaseReader() == null) {
			log
					.warn("Entity service does not check whether artifacts are quarantined since hospitalDatabaseReader property has not been set.");
		}
	}

	/**
	 * Gets the (mandatory) data base reader that is used to retrieve the target
	 * artifact id from the the identity mapping table. If the artifact has been
	 * already mapped to a target artifact this information will be inserted. If
	 * the processed artifact has been already mapped in a newer or equal
	 * version the duplicate shipment detection algorithm will skip the
	 * artifact.
	 * 
	 * @param identityMappingDatabaseReader
	 */
	public JDBCReadConnector getIdentityMappingDatabaseReader() {
		return identityMappingDatabaseReader;
	}

	/**
	 * Sets the (mandatory) data base reader that is used to retrieve the target
	 * artifact id from the the identity mapping table. If the artifact has been
	 * already mapped to a target artifact this information will be inserted. If
	 * the processed artifact has been already mapped in a newer or equal
	 * version the duplicate shipment detection algorithm will skip the
	 * artifact.
	 * 
	 * @param identityMappingDatabaseReader
	 */
	public void setIdentityMappingDatabaseReader(
			JDBCReadConnector identityMappingDatabaseReader) {
		this.identityMappingDatabaseReader = identityMappingDatabaseReader;
	}

	/**
	 * Gets the (optional) data base reader that is used to find out whether the
	 * artifact is currently quarantined in the hospital and has not been
	 * reprocessed yet. If this property is not set, no lookup will be done in
	 * the hospital. If the artifact is in the hospital its version in the
	 * hospital will be compared with the current version. If the current
	 * artifact version is less or equal to the quarantined artifact, no
	 * artifact will be shipped. Otherwise the
	 * skipNewerVersionsOfQuarantinedArtifacts property will determine whether
	 * to skip newer versions as well.
	 * 
	 * @return
	 */
	public JDBCReadConnector getHospitalDatabaseReader() {
		return hospitalDatabaseReader;
	}

	/**
	 * Sets the (optional) data base reader that is used to find out whether the
	 * artifact is currently quarantined in the hospital and has not been
	 * reprocessed yet. If this property is not set, no lookup will be done in
	 * the hospital. If the artifact is in the hospital its version in the
	 * hospital will be compared with the current version. If the current
	 * artifact version is less or equal to the quarantined artifact, no
	 * artifact will be shipped. Otherwise the
	 * skipNewerVersionsOfQuarantinedArtifacts property will determine whether
	 * to skip newer versions as well.
	 * 
	 * @param hospitalDatabaseReader
	 */
	public void setHospitalDatabaseReader(
			JDBCReadConnector hospitalDatabaseReader) {
		this.hospitalDatabaseReader = hospitalDatabaseReader;
	}

	/**
	 * This optional property (default value false) determines whether
	 * non-reprocessed artifacts in the hospital should be skipped even if a
	 * newer version of the artifact is available in the source system.
	 * 
	 * @param skipNewerVersionsOfQuarantinedAttachments
	 */
	public void setSkipNewerVersionsOfQuarantinedArtifacts(
			boolean skipNewerVersionsOfQuarantinedAttachments) {
		this.skipNewerVersionsOfQuarantinedAttachments = skipNewerVersionsOfQuarantinedAttachments;
	}

	/**
	 * This optional property (default value false) determines whether
	 * non-reprocessed artifacts in the hospital should be skipped even if a
	 * newer version of the artifact is available in the source system.
	 * 
	 * @param skipNewerVersionsOfQuarantinedAttachments
	 */
	public boolean isSkipNewerVersionsOfQuarantinedArtifacts() {
		return skipNewerVersionsOfQuarantinedAttachments;
	}

	public long getIdentityMapEventWaitTime() {
		return identityMapEventWaitTime;
	}

	public void setIdentityMapEventWaitTime(long identityMapEventWaitTime) {
		this.identityMapEventWaitTime = identityMapEventWaitTime;
	}

	public int getIdentityMapEventWaitCount() {
		return identityMapEventWaitCount;
	}

	public void setIdentityMapEventWaitCount(int identityMapEventWaitCount) {
		this.identityMapEventWaitCount = identityMapEventWaitCount;
	}

}
