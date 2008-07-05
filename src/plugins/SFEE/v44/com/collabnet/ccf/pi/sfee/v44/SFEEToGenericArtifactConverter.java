package com.collabnet.ccf.pi.sfee.v44;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.sfee.v44.meta.ArtifactMetaData;
import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.TrackerFieldValueSoapDO;

/**
 * The artifact data from the SFEE SOAP API calls are in the
 * ArtifactSoapDO object. This class converts the artifact data
 * contained in the ArtifactSoapDO objects into GenericArtifact object 
 * @author madhusuthanan
 *
 */
public class SFEEToGenericArtifactConverter {
	/**
	 * Converts the artifact data contained in the ArtifactSoapDO object
	 * into a GenricArtifact object
	 * 
	 * @param dataObject - The ArtifactSoapDO object that needs to be converted into
	 * 						a GenericArtifact object
	 * @param fieldsMap - The custom/flex fields defined in the tracker
	 * @param lastReadDate - Last read date for this tracker.
	 * @param includeFieldMetaData 
	 * @return - The converted GenericArtifact object
	 */
	public GenericArtifact convert(ArtifactSoapDO dataObject, HashMap<String, List<TrackerFieldSoapDO>> fieldsMap,
			Date lastReadDate, boolean includeFieldMetaData) {
		if(dataObject != null){
			ArtifactSoapDO artifactRow = dataObject;
			GenericArtifact genericArtifact = new GenericArtifact();
			genericArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			genericArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
			genericArtifact.setErrorCode("ok");
			if(includeFieldMetaData){
				genericArtifact.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.TRUE);
			}
			else {
				genericArtifact.setIncludesFieldMetaData(GenericArtifact.IncludesFieldMetaDataValue.FALSE);
			}
			
			// FIXME These fields will never change, why not add the fields directly?
			int actualHours = artifactRow.getActualHours();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.actualHours,
					actualHours, genericArtifact, fieldsMap, includeFieldMetaData);
			String assignedTo = artifactRow.getAssignedTo();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.assignedTo,
					assignedTo, genericArtifact, fieldsMap, includeFieldMetaData);
			String category = artifactRow.getCategory();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.category,
					category, genericArtifact, fieldsMap, includeFieldMetaData);
			Date closeDate = artifactRow.getCloseDate();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.closeDate,
					closeDate, genericArtifact, fieldsMap, includeFieldMetaData);
			String customer = artifactRow.getCustomer();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.customer,
					customer, genericArtifact, fieldsMap, includeFieldMetaData);
			String description = artifactRow.getDescription();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.description,
					description, genericArtifact, fieldsMap, includeFieldMetaData);
			int estimatedHours = artifactRow.getEstimatedHours();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.estimatedHours,
					estimatedHours, genericArtifact, fieldsMap, includeFieldMetaData);
			String group = artifactRow.getGroup();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.group,
					group, genericArtifact, fieldsMap, includeFieldMetaData);
			int priority = artifactRow.getPriority();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.priority,
					priority, genericArtifact, fieldsMap, includeFieldMetaData);
			String reportedReleaseId = artifactRow.getReportedReleaseId();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.reportedReleaseId,
					reportedReleaseId, genericArtifact, fieldsMap, includeFieldMetaData);
			String resolvedReleaseId = artifactRow.getResolvedReleaseId();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.resolvedReleaseId,
					resolvedReleaseId, genericArtifact, fieldsMap, includeFieldMetaData);
			String status = artifactRow.getStatus();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.status,
					status, genericArtifact, fieldsMap, includeFieldMetaData);
			String statusClass = artifactRow.getStatusClass();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.statusClass,
					statusClass, genericArtifact, fieldsMap, includeFieldMetaData);
			String folderId = artifactRow.getFolderId();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.folderId,
					folderId, genericArtifact, fieldsMap, includeFieldMetaData);
			String path = artifactRow.getPath();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.path,
					path, genericArtifact, fieldsMap, includeFieldMetaData);
			String title = artifactRow.getTitle();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.title,
					title, genericArtifact, fieldsMap, includeFieldMetaData);
			String createdBy = artifactRow.getCreatedBy();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.createdBy,
					createdBy, genericArtifact, fieldsMap, includeFieldMetaData);
			Date createdDate = artifactRow.getCreatedDate();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.createdDate,
					createdDate, genericArtifact, fieldsMap, includeFieldMetaData);
			if(createdDate.after(lastReadDate)){
				genericArtifact.setArtifactAction(ArtifactActionValue.CREATE);
			}
			else {
				genericArtifact.setArtifactAction(ArtifactActionValue.UPDATE);
			}
			String id = artifactRow.getId();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.id,
					id, genericArtifact, fieldsMap, includeFieldMetaData);
			genericArtifact.setSourceArtifactId(id);
			String lastModifiedBy = artifactRow.getLastModifiedBy();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.lastModifiedBy,
					lastModifiedBy, genericArtifact, fieldsMap, includeFieldMetaData);
			Date lastModifiedDate = artifactRow.getLastModifiedDate();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.lastModifiedDate,
					lastModifiedDate, genericArtifact, fieldsMap, includeFieldMetaData);
			genericArtifact.setSourceArtifactLastModifiedDate(DateUtil.format(lastModifiedDate));
			int version = artifactRow.getVersion();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.version,
					version, genericArtifact, fieldsMap, includeFieldMetaData);
			
			genericArtifact.setSourceArtifactVersion(Integer.toString(version));
			
			SoapFieldValues flexFields = artifactRow.getFlexFields();
			String[] flexFieldNames = flexFields.getNames();
			// FIXME This is where we get our type info for free, why not take it from there?
			String[] flexFieldTypes = flexFields.getTypes();
			Object[] flexFieldValues = flexFields.getValues();
			for(int i=0; i < flexFieldNames.length; i++){
				GenericArtifactField field;
				TrackerFieldSoapDO fieldDO =
					SFEEAppHandler.getTrackerFieldSoapDOForFlexField(fieldsMap, flexFieldNames[i]); 
				GenericArtifactField.FieldValueTypeValue fieldValueType =
					ArtifactMetaData.getFieldValueType(flexFieldNames[i],fieldDO);
				field = genericArtifact.addNewField(flexFieldNames[i], GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				field.setFieldValueType(fieldValueType);
				// FIXME Why not use the flexFieldType array for this?
				field.setFieldValue(ArtifactMetaData.getFieldValue(flexFieldNames[i], flexFieldValues[i], fieldValueType));
				if(includeFieldMetaData){
					field.setAlternativeFieldName(flexFieldNames[i]);
					if(fieldDO.getRequired()){
						field.setMinOccurs(1);
					}
					else {
						field.setMinOccurs(0);
					}
					if(flexFieldTypes[i].equals(TrackerFieldSoapDO.FIELD_TYPE_MULTISELECT)){
						TrackerFieldValueSoapDO[] fieldValues = fieldDO.getFieldValues();
						field.setMaxOccurs(fieldValues.length);
					}
					else if(flexFieldTypes[i].equals(TrackerFieldSoapDO.FIELD_TYPE_MULTISELECT_USER)){
						field.setMaxOccursValue(GenericArtifactField.UNBOUNDED);
					}
					else {
						field.setMaxOccurs(1);
					}
				}
			}
			return genericArtifact;
		}
		return null;
	}
	
	/**
	 * Creates a GenericArtifactField object with the appropriate values provided
	 * in the method call.
	 * 
	 * @param fieldName - Name of the field
	 * @param displayName - The field's display name
	 * @param value - Value of the field
	 * @param genericArtifact - The GenericArtifact object on which the new field should be created
	 * @param fieldsMap - The custom/flex fields defined for this tracker.
	 * @param includeFieldMetaData 
	 * @return - Returns the newly created GenericArtifactField object
	 */
	private GenericArtifactField createGenericArtifactField(ArtifactMetaData.SFEEFields sfField,
			Object value, GenericArtifact genericArtifact, HashMap<String,List<TrackerFieldSoapDO>> fieldsMap,
			boolean includeFieldMetaData){
		String fieldName = sfField.getFieldName();
		// FIXME Why do we call this method for non-flex fields?
		TrackerFieldSoapDO fieldSoapDO = SFEEAppHandler.getTrackerFieldSoapDOForFlexField(fieldsMap, fieldName);
		if(fieldSoapDO == null){
			fieldSoapDO = SFEEAppHandler.getTrackerFieldSoapDOForFlexField(fieldsMap, sfField.getAlternateName());
		}
		GenericArtifactField field = genericArtifact.addNewField(sfField.getFieldName(), GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
		GenericArtifactField.FieldValueTypeValue fieldValueType = ArtifactMetaData.getFieldValueType(fieldName, fieldSoapDO);
		field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
		field.setFieldValueType(fieldValueType);
		//TODO Change this
		field.setFieldValueHasChanged(true);
		if(value != null){
			field.setFieldValue(value);
		}
		if(includeFieldMetaData){
			field.setAlternativeFieldName(sfField.getDisplayName());
			//TODO How do we determine if a field value can be null or not?
//			field.setNullValueSupported(nullValueSupported);
			if(sfField.getFieldType() == ArtifactMetaData.FIELD_TYPE.SYSTEM_DEFINED){
				if(sfField.isRequired()){
					field.setMinOccurs(1);
				} else {
					field.setMinOccurs(0);
				}
				field.setMaxOccurs(1);
			}
			else if(sfField.getFieldType() == ArtifactMetaData.FIELD_TYPE.CONFIGURABLE){
				boolean required = false;
				if(fieldSoapDO != null){
					required = fieldSoapDO.getRequired();
				}
				if(required){
					field.setMinOccurs(1);
				}
				else {
					field.setMinOccurs(0);
				}
				field.setMaxOccurs(1);
			}
		}
		return field;
	}
}
