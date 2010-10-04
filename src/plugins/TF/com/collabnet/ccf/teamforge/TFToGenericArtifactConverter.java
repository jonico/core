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

package com.collabnet.ccf.teamforge;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ce.soap50.webservices.cemain.TrackerFieldSoapDO;
import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.planning.PlanningFolderDO;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.TrackerFieldDO;
import com.collabnet.teamforge.api.tracker.TrackerFieldValueDO;

/**
 * The artifact data from the TF SOAP API calls are in the ArtifactSoapDO
 * object. This class converts the artifact data contained in the ArtifactSoapDO
 * objects into GenericArtifact object
 * 
 * @author madhusuthanan
 * 
 */
public class TFToGenericArtifactConverter {
	/**
	 * Converts the artifact data contained in the ArtifactSoapDO object into a
	 * GenricArtifact object
	 * 
	 * @param dataObject
	 *            - The ArtifactSoapDO object that needs to be converted into a
	 *            GenericArtifact object
	 * @param fieldsMap
	 *            - The custom/flex fields defined in the tracker
	 * @param lastReadDate
	 *            - Last read date for this tracker.
	 * @param includeFieldMetaData
	 * @return - The converted GenericArtifact object
	 */
	public static GenericArtifact convertArtifact(boolean supports53, boolean supports54,
			ArtifactDO dataObject,
			HashMap<String, List<TrackerFieldDO>> fieldsMap, Date lastReadDate,
			boolean includeFieldMetaData, String sourceSystemTimezone) {
		if (dataObject != null) {
			ArtifactDO artifactRow = dataObject;
			GenericArtifact genericArtifact = new GenericArtifact();
			genericArtifact
					.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			genericArtifact
					.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
			genericArtifact.setErrorCode("ok");
			if (includeFieldMetaData) {
				genericArtifact
						.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.TRUE);
			} else {
				genericArtifact
						.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
			}

			int actualEffort = artifactRow.getActualEffort();
			int estimatedEffort = artifactRow.getEstimatedEffort();
			createGenericArtifactField(
					TFArtifactMetaData.TFFields.actualHours, actualEffort,
					genericArtifact, fieldsMap, includeFieldMetaData);
			createGenericArtifactField(
					TFArtifactMetaData.TFFields.estimatedHours,
					estimatedEffort, genericArtifact, fieldsMap,
					includeFieldMetaData);

			if (supports53) {

				int remainingEffort = artifactRow.getRemainingEffort();
				createGenericArtifactField(
						TFArtifactMetaData.TFFields.remainingEffort,
						remainingEffort, genericArtifact, fieldsMap,
						includeFieldMetaData);

				Boolean autosumming = artifactRow.getAutosumming();
				createGenericArtifactField(
						TFArtifactMetaData.TFFields.autosumming, autosumming,
						genericArtifact, fieldsMap, includeFieldMetaData);

				String planningFolderId = artifactRow.getPlanningFolderId();
				createGenericArtifactField(
						TFArtifactMetaData.TFFields.planningFolder,
						planningFolderId, genericArtifact, fieldsMap,
						includeFieldMetaData);
			}
			
			if (supports54) {
				int storyPoints = artifactRow.getPoints();
				createGenericArtifactField(
						TFArtifactMetaData.TFFields.points,
						storyPoints, genericArtifact, fieldsMap,
						includeFieldMetaData);
			}

			String assignedTo = artifactRow.getAssignedTo();
			createGenericArtifactField(TFArtifactMetaData.TFFields.assignedTo,
					assignedTo, genericArtifact, fieldsMap,
					includeFieldMetaData);
			String category = artifactRow.getCategory();
			createGenericArtifactField(TFArtifactMetaData.TFFields.category,
					category, genericArtifact, fieldsMap, includeFieldMetaData);
			Date closeDate = artifactRow.getCloseDate();
			createGenericArtifactField(TFArtifactMetaData.TFFields.closeDate,
					closeDate, genericArtifact, fieldsMap, includeFieldMetaData);
			String customer = artifactRow.getCustomer();
			createGenericArtifactField(TFArtifactMetaData.TFFields.customer,
					customer, genericArtifact, fieldsMap, includeFieldMetaData);
			String description = artifactRow.getDescription();
			createGenericArtifactField(TFArtifactMetaData.TFFields.description,
					description, genericArtifact, fieldsMap,
					includeFieldMetaData);

			String group = artifactRow.getGroup();
			createGenericArtifactField(TFArtifactMetaData.TFFields.group,
					group, genericArtifact, fieldsMap, includeFieldMetaData);
			int priority = artifactRow.getPriority();
			createGenericArtifactField(TFArtifactMetaData.TFFields.priority,
					priority, genericArtifact, fieldsMap, includeFieldMetaData);
			String reportedReleaseId = artifactRow.getReportedReleaseId();
			createGenericArtifactField(
					TFArtifactMetaData.TFFields.reportedReleaseId,
					reportedReleaseId, genericArtifact, fieldsMap,
					includeFieldMetaData);
			String resolvedReleaseId = artifactRow.getResolvedReleaseId();
			createGenericArtifactField(
					TFArtifactMetaData.TFFields.resolvedReleaseId,
					resolvedReleaseId, genericArtifact, fieldsMap,
					includeFieldMetaData);
			String status = artifactRow.getStatus();
			createGenericArtifactField(TFArtifactMetaData.TFFields.status,
					status, genericArtifact, fieldsMap, includeFieldMetaData);
			String statusClass = artifactRow.getStatusClass();
			createGenericArtifactField(TFArtifactMetaData.TFFields.statusClass,
					statusClass, genericArtifact, fieldsMap,
					includeFieldMetaData);
			String folderId = artifactRow.getFolderId();
			createGenericArtifactField(TFArtifactMetaData.TFFields.folderId,
					folderId, genericArtifact, fieldsMap, includeFieldMetaData);
			String path = artifactRow.getPath();
			createGenericArtifactField(TFArtifactMetaData.TFFields.path, path,
					genericArtifact, fieldsMap, includeFieldMetaData);
			String title = artifactRow.getTitle();
			createGenericArtifactField(TFArtifactMetaData.TFFields.title,
					title, genericArtifact, fieldsMap, includeFieldMetaData);
			String createdBy = artifactRow.getCreatedBy();
			createGenericArtifactField(TFArtifactMetaData.TFFields.createdBy,
					createdBy, genericArtifact, fieldsMap, includeFieldMetaData);
			Date createdDate = artifactRow.getCreatedDate();
			createGenericArtifactField(TFArtifactMetaData.TFFields.createdDate,
					createdDate, genericArtifact, fieldsMap,
					includeFieldMetaData);
			if (createdDate.after(lastReadDate)) {
			genericArtifact.setArtifactAction(ArtifactActionValue.CREATE);
			} else {
				genericArtifact.setArtifactAction(ArtifactActionValue.UPDATE);
			}
			String id = artifactRow.getId();
			createGenericArtifactField(TFArtifactMetaData.TFFields.id, id,
					genericArtifact, fieldsMap, includeFieldMetaData);
			genericArtifact.setSourceArtifactId(id);
			String lastModifiedBy = artifactRow.getLastModifiedBy();
			createGenericArtifactField(
					TFArtifactMetaData.TFFields.lastModifiedBy, lastModifiedBy,
					genericArtifact, fieldsMap, includeFieldMetaData);
			Date lastModifiedDate = artifactRow.getLastModifiedDate();
			createGenericArtifactField(
					TFArtifactMetaData.TFFields.lastModifiedDate,
					lastModifiedDate, genericArtifact, fieldsMap,
					includeFieldMetaData);
			genericArtifact.setSourceArtifactLastModifiedDate(DateUtil
					.format(lastModifiedDate));
			int version = artifactRow.getVersion();
			createGenericArtifactField(TFArtifactMetaData.TFFields.version,
					version, genericArtifact, fieldsMap, includeFieldMetaData);

			genericArtifact.setSourceArtifactVersion(Integer.toString(version));

			FieldValues flexFields = artifactRow.getFlexFields();
			String[] flexFieldNames = flexFields.getNames();
			// FIXME This is where we get our type info for free, why not take
			// it from there?
			String[] flexFieldTypes = flexFields.getTypes();
			Object[] flexFieldValues = flexFields.getValues();
			for (int i = 0; i < flexFieldNames.length; i++) {
				GenericArtifactField field;
				GenericArtifactField.FieldValueTypeValue fieldValueType = TFArtifactMetaData
						.getFieldValueTypeForFieldType(flexFieldTypes[i]);
				field = genericArtifact.addNewField(flexFieldNames[i],
						GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				field
						.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				field.setFieldValueType(fieldValueType);
				if (flexFieldTypes[i]
						.equalsIgnoreCase(TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE)) {
					TFArtifactMetaData.setDateFieldValue(flexFieldNames[i],
							flexFieldValues[i], sourceSystemTimezone, field);
				} else {
					field.setFieldValue(flexFieldValues[i]);
				}
				if (includeFieldMetaData) {
					TrackerFieldDO fieldDO = TFAppHandler
							.getTrackerFieldSoapDOForFlexField(fieldsMap,
									flexFieldNames[i]);
					field.setAlternativeFieldName(flexFieldNames[i]);
					if (fieldDO.getRequired()) {
						field.setMinOccurs(1);
					} else {
						field.setMinOccurs(0);
					}
					if (flexFieldTypes[i]
							.equals(TrackerFieldSoapDO.FIELD_TYPE_MULTISELECT)) {
						TrackerFieldValueDO[] fieldValues = fieldDO
								.getFieldValues();
						field.setMaxOccurs(fieldValues.length);
					} else if (flexFieldTypes[i]
							.equals(TrackerFieldSoapDO.FIELD_TYPE_MULTISELECT_USER)) {
						field.setMaxOccursValue(GenericArtifactField.UNBOUNDED);
					} else {
						field.setMaxOccurs(1);
					}
				}
			}
			return genericArtifact;
		}
		return null;
	}

	/**
	 * Creates a GenericArtifactField object with the appropriate values
	 * provided in the method call.
	 * 
	 * @param fieldName
	 *            - Name of the field
	 * @param displayName
	 *            - The field's display name
	 * @param value
	 *            - Value of the field
	 * @param genericArtifact
	 *            - The GenericArtifact object on which the new field should be
	 *            created
	 * @param fieldsMap
	 *            - The custom/flex fields defined for this tracker.
	 * @param includeFieldMetaData
	 * @return - Returns the newly created GenericArtifactField object
	 */
	public static GenericArtifactField createGenericArtifactField(
			TFArtifactMetaData.TFFields sfField, Object value,
			GenericArtifact genericArtifact,
			HashMap<String, List<TrackerFieldDO>> fieldsMap,
			boolean includeFieldMetaData) {
		String fieldName = sfField.getFieldName();

		GenericArtifactField field = genericArtifact.addNewField(sfField
				.getFieldName(),
				GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
		GenericArtifactField.FieldValueTypeValue fieldValueType = TFArtifactMetaData
				.getFieldValueType(fieldName);
		field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
		field.setFieldValueType(fieldValueType);
		field.setFieldValueHasChanged(true);
		if (value != null) {
			field.setFieldValue(value);
		}
		if (includeFieldMetaData) {
			field.setAlternativeFieldName(sfField.getDisplayName());
			// TODO How do we determine if a field value can be null or not?
			// field.setNullValueSupported(nullValueSupported);
			if (sfField.getFieldType() == TFArtifactMetaData.FIELD_TYPE.SYSTEM_DEFINED) {
				if (sfField.isRequired()) {
					field.setMinOccurs(1);
				} else {
					field.setMinOccurs(0);
				}
				field.setMaxOccurs(1);
			} else if (sfField.getFieldType() == TFArtifactMetaData.FIELD_TYPE.CONFIGURABLE) {
				TrackerFieldDO fieldSoapDO = TFAppHandler
						.getTrackerFieldSoapDOForFlexField(fieldsMap, fieldName);
				if (fieldSoapDO == null) {
					fieldSoapDO = TFAppHandler
							.getTrackerFieldSoapDOForFlexField(fieldsMap,
									sfField.getAlternateName());
				}
				boolean required = false;
				if (fieldSoapDO != null) {
					required = fieldSoapDO.getRequired();
				}
				if (required) {
					field.setMinOccurs(1);
				} else {
					field.setMinOccurs(0);
				}
				field.setMaxOccurs(1);
			}
		}
		return field;
	}

	public static GenericArtifact convertPlanningFolder(boolean supports54,
			PlanningFolderDO planningFolder, Date lastReadDate,
			boolean includeFieldMetaData, String sourceSystemTimezone) {
		if (planningFolder != null) {
			PlanningFolderDO planningRow = planningFolder;
			GenericArtifact genericArtifact = new GenericArtifact();
			genericArtifact
					.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			genericArtifact
					.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
			genericArtifact.setErrorCode("ok");
			if (includeFieldMetaData) {
				genericArtifact
						.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.TRUE);
			} else {
				genericArtifact
						.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
			}

			String description = planningRow.getDescription();
			createGenericArtifactField(TFArtifactMetaData.TFFields.description,
					description, genericArtifact, null, includeFieldMetaData);

			String parentFolderId = planningRow.getParentFolderId();
			createGenericArtifactField(
					TFArtifactMetaData.TFFields.parentFolderId, parentFolderId,
					genericArtifact, null, includeFieldMetaData);

			String projectId = planningRow.getProjectId();
			createGenericArtifactField(TFArtifactMetaData.TFFields.projectId,
					projectId, genericArtifact, null, includeFieldMetaData);

			String path = planningRow.getPath();

			createGenericArtifactField(TFArtifactMetaData.TFFields.path, path,
					genericArtifact, null, includeFieldMetaData);
			String title = planningRow.getTitle();
			createGenericArtifactField(TFArtifactMetaData.TFFields.title,
					title, genericArtifact, null, includeFieldMetaData);
			String createdBy = planningRow.getCreatedBy();

			createGenericArtifactField(TFArtifactMetaData.TFFields.createdBy,
					createdBy, genericArtifact, null, includeFieldMetaData);

			Date createdDate = planningRow.getCreatedDate();
			createGenericArtifactField(TFArtifactMetaData.TFFields.createdDate,
					createdDate, genericArtifact, null, includeFieldMetaData);
			if (createdDate.after(lastReadDate)) {
				genericArtifact.setArtifactAction(ArtifactActionValue.CREATE);
			} else {
				genericArtifact.setArtifactAction(ArtifactActionValue.UPDATE);
			}
			String id = planningRow.getId();
			createGenericArtifactField(TFArtifactMetaData.TFFields.id, id,
					genericArtifact, null, includeFieldMetaData);
			genericArtifact.setSourceArtifactId(id);
			String lastModifiedBy = planningRow.getLastModifiedBy();
			createGenericArtifactField(
					TFArtifactMetaData.TFFields.lastModifiedBy, lastModifiedBy,
					genericArtifact, null, includeFieldMetaData);
			Date lastModifiedDate = planningRow.getLastModifiedDate();
			createGenericArtifactField(
					TFArtifactMetaData.TFFields.lastModifiedDate,
					lastModifiedDate, genericArtifact, null,
					includeFieldMetaData);
			genericArtifact.setSourceArtifactLastModifiedDate(DateUtil
					.format(lastModifiedDate));
			int version = planningRow.getVersion();
			createGenericArtifactField(TFArtifactMetaData.TFFields.version,
					version, genericArtifact, null, includeFieldMetaData);

			genericArtifact.setSourceArtifactVersion(Integer.toString(version));

			Date startDate = planningRow.getStartDate();
			GenericArtifactField startDateField = createGenericArtifactField(TFArtifactMetaData.TFFields.startDate,
					startDate, genericArtifact, null, includeFieldMetaData);
			TFArtifactMetaData.setPFDateFieldValue(startDate, sourceSystemTimezone, startDateField);

			Date endDate = planningRow.getEndDate();
			GenericArtifactField endDateField = createGenericArtifactField(TFArtifactMetaData.TFFields.endDate,
					endDate, genericArtifact, null, includeFieldMetaData);
			TFArtifactMetaData.setPFDateFieldValue(endDate, sourceSystemTimezone, endDateField);
			
			if (supports54) {
				int capacity = planningRow.getCapacity();
				createGenericArtifactField(TFArtifactMetaData.TFFields.capacity, capacity, genericArtifact, null, includeFieldMetaData);
				String status = planningRow.getStatus();
				createGenericArtifactField(TFArtifactMetaData.TFFields.status, status, genericArtifact, null, includeFieldMetaData);
				String statusClass = planningRow.getStatusClass();
				createGenericArtifactField(TFArtifactMetaData.TFFields.statusClass, statusClass, genericArtifact, null, includeFieldMetaData);
				String releaseId = planningRow.getReleaseId();
				createGenericArtifactField(TFArtifactMetaData.TFFields.releaseId, releaseId, genericArtifact, null, includeFieldMetaData);				
			}

			return genericArtifact;
		}
		return null;
	}
}
