package com.collabnet.ccf.pi.sfee.v44;

import java.util.Date;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.sfee.v44.meta.ArtifactMetaData;
import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class SFEEToGenericArtifactConverter {
	public GenericArtifact convert(ArtifactSoapDO dataObject, TrackerFieldSoapDO[] trackerFields, Date lastReadDate) {
		if(dataObject != null){
			ArtifactSoapDO artifactRow = dataObject;
			GenericArtifact genericArtifact = new GenericArtifact();
			genericArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			genericArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
			int actualHours = artifactRow.getActualHours();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.actualHours.getFieldName(),
					ArtifactMetaData.SFEEFields.actualHours.getDisplayName(),
					actualHours, genericArtifact, trackerFields);
			String assignedTo = artifactRow.getAssignedTo();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.assignedTo.getFieldName(),
					ArtifactMetaData.SFEEFields.assignedTo.getDisplayName(),
					assignedTo, genericArtifact, trackerFields);
			String category = artifactRow.getCategory();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.category.getFieldName(),
					ArtifactMetaData.SFEEFields.category.getDisplayName(),
					category, genericArtifact, trackerFields);
			Date closeDate = artifactRow.getCloseDate();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.closeDate.getFieldName(),
					ArtifactMetaData.SFEEFields.closeDate.getDisplayName(),
					closeDate, genericArtifact, trackerFields);
			String customer = artifactRow.getCustomer();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.customer.getFieldName(),
					ArtifactMetaData.SFEEFields.customer.getDisplayName(),
					customer, genericArtifact, trackerFields);
			String description = artifactRow.getDescription();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.description.getFieldName(),
					ArtifactMetaData.SFEEFields.description.getDisplayName(),
					description, genericArtifact, trackerFields);
			int estimatedHours = artifactRow.getEstimatedHours();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.estimatedHours.getFieldName(),
					ArtifactMetaData.SFEEFields.estimatedHours.getDisplayName(),
					estimatedHours, genericArtifact, trackerFields);
			String group = artifactRow.getGroup();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.group.getFieldName(),
					ArtifactMetaData.SFEEFields.group.getDisplayName(),
					group, genericArtifact, trackerFields);
			int priority = artifactRow.getPriority();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.priority.getFieldName(),
					ArtifactMetaData.SFEEFields.priority.getDisplayName(),
					priority, genericArtifact, trackerFields);
			String reportedReleaseId = artifactRow.getReportedReleaseId();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.reportedReleaseId.getFieldName(),
					ArtifactMetaData.SFEEFields.reportedReleaseId.getDisplayName(),
					reportedReleaseId, genericArtifact, trackerFields);
			String resolvedReleaseId = artifactRow.getResolvedReleaseId();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.resolvedReleaseId.getFieldName(),
					ArtifactMetaData.SFEEFields.resolvedReleaseId.getDisplayName(),
					resolvedReleaseId, genericArtifact, trackerFields);
			String status = artifactRow.getStatus();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.status.getFieldName(),
					ArtifactMetaData.SFEEFields.status.getDisplayName(),
					status, genericArtifact, trackerFields);
			String statusClass = artifactRow.getStatusClass();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.statusClass.getFieldName(),
					ArtifactMetaData.SFEEFields.statusClass.getDisplayName(),
					statusClass, genericArtifact, trackerFields);
			String folderId = artifactRow.getFolderId();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.folderId.getFieldName(),
					ArtifactMetaData.SFEEFields.folderId.getDisplayName(),
					folderId, genericArtifact, trackerFields);
			String path = artifactRow.getPath();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.path.getFieldName(),
					ArtifactMetaData.SFEEFields.path.getDisplayName(),
					path, genericArtifact, trackerFields);
			String title = artifactRow.getTitle();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.title.getFieldName(),
					ArtifactMetaData.SFEEFields.title.getDisplayName(),
					title, genericArtifact, trackerFields);
			String createdBy = artifactRow.getCreatedBy();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.createdBy.getFieldName(),
					ArtifactMetaData.SFEEFields.createdBy.getDisplayName(),
					createdBy, genericArtifact, trackerFields);
			Date createdDate = artifactRow.getCreatedDate();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.createdDate.getFieldName(),
					ArtifactMetaData.SFEEFields.createdDate.getDisplayName(),
					createdDate, genericArtifact, trackerFields);
			if(createdDate.after(lastReadDate)){
				genericArtifact.setArtifactAction(ArtifactActionValue.CREATE);
			}
			else {
				genericArtifact.setArtifactAction(ArtifactActionValue.UPDATE);
			}
			String id = artifactRow.getId();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.id.getFieldName(),
					ArtifactMetaData.SFEEFields.id.getDisplayName(),
					id, genericArtifact, trackerFields);
			genericArtifact.setSourceArtifactId(id);
			String lastModifiedBy = artifactRow.getLastModifiedBy();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.lastModifiedBy.getFieldName(),
					ArtifactMetaData.SFEEFields.lastModifiedBy.getDisplayName(),
					lastModifiedBy, genericArtifact, trackerFields);
			Date lastModifiedDate = artifactRow.getLastModifiedDate();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.lastModifiedDate.getFieldName(),
					ArtifactMetaData.SFEEFields.lastModifiedDate.getDisplayName(),
					lastModifiedDate, genericArtifact, trackerFields);
			genericArtifact.setArtifactLastModifiedDate(DateUtil.format(lastModifiedDate));
			int version = artifactRow.getVersion();
			this.createGenericArtifactField(ArtifactMetaData.SFEEFields.version.getFieldName(),
					ArtifactMetaData.SFEEFields.version.getDisplayName(),
					version, genericArtifact, trackerFields);
			
			
			
			SoapFieldValues flexFields = artifactRow.getFlexFields();
			String[] flexFieldNames = flexFields.getNames();
			String[] flexFieldTypes = flexFields.getTypes();
			Object[] flexFieldValues = flexFields.getValues();
			for(int i=0; i < flexFieldNames.length; i++){
				System.out.println(flexFieldNames[i]+"-"+flexFieldTypes[i]+"-"+flexFieldValues[i]);
				if(flexFieldNames[i].equals("ArtifactAction")){
					genericArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
					continue;
				}
				else{
					genericArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
				}
				GenericArtifactField field;

				GenericArtifactField.FieldValueTypeValue fieldValueType =
					ArtifactMetaData.getFieldValueType(flexFieldNames[i], trackerFields);
//				 CHANGE Field display name is not correct here.
				field = genericArtifact.addNewField(flexFieldNames[i], flexFieldNames[i], GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				genericArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
				genericArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				field.setFieldValueType(fieldValueType);
				// TODO setting the field value as is is not correct. Please change this.
				field.setFieldValue(ArtifactMetaData.getFieldValue(flexFieldNames[i], flexFieldValues[i], fieldValueType));
			}
			return genericArtifact;
		}
		return null;
	}
	
	private GenericArtifactField createGenericArtifactField(String fieldName, String displayName, Object value, GenericArtifact genericArtifact, TrackerFieldSoapDO[] trackerFields){
		GenericArtifactField field = genericArtifact.addNewField(fieldName, displayName, GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
		GenericArtifactField.FieldValueTypeValue fieldValueType = ArtifactMetaData.getFieldValueType(ArtifactMetaData.SFEEFields.actualHours.getFieldName(), trackerFields);
		field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
		field.setFieldValueType(fieldValueType);
		//TODO Change this
		field.setFieldValueHasChanged(true);
		if(value != null){
			field.setFieldValue(value);
		}
		else {
			
		}
		return null;
	}
}
