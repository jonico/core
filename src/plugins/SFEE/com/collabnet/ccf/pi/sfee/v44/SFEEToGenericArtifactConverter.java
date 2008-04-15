package com.collabnet.ccf.pi.sfee.v44;

import java.util.Date;

import org.dom4j.Document;


import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.sfee.v44.IArtifactToGAConverter;
import com.collabnet.ccf.pi.sfee.meta.ArtifactMetaData;
import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class SFEEToGenericArtifactConverter implements IArtifactToGAConverter {
	
	
	public GenericArtifact convert(Object dataObject, TrackerFieldSoapDO[] trackerFields) {
		if(dataObject != null){
			ArtifactSoapDO artifactRow = (ArtifactSoapDO) dataObject;
			GenericArtifact genericArtifact = new GenericArtifact();
			for(String fieldName:SFEEArtifactMetaData.getArtifactFields())
			{
				//Class fieldType = SFEEArtifactMetaData.getFieldType(fieldName);
				//String fieldDisplayName = rs.getFieldValue(sfUserLabel);
				//String editStyle = rs.getFieldValue(sfEditStyle);
				GenericArtifactField field;

				// obtain the GenericArtifactField datatype from the columnType and editStyle
				GenericArtifactField.FieldValueTypeValue fieldValueType = ArtifactMetaData.getFieldValueType(fieldName, trackerFields);
				// CHANGE Field display name is not correct here.
				field = genericArtifact.addNewField(fieldName, "Unknown", GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
				// TODO The following attributes are set blindly. Please correct these
				
				genericArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
				genericArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				field.setFieldValueType(fieldValueType);
				//TODO Change this
				field.setFieldValueHasChanged(true);
				Object fieldValue = SFEEArtifactMetaData.getFieldValue(fieldName,artifactRow);
				if(fieldValue != null){
					field.setFieldValue(fieldValue);
				}
				if(fieldName.equals("LastModifiedDate")){
					genericArtifact.setArtifactLastModifiedDate(DateUtil.format((Date)fieldValue));
				}
				else if(fieldName.equals("Id")){
					genericArtifact.setSourceArtifactId(fieldValue.toString());
				}
				// Only for the Comments field, the action value of the GenericArtifactField is set to APPEND. Later, this feature can be upgraded.
				// TODO handle this FieldAction
//				if(columnName!=null && columnName.equals("BG_DEV_COMMENTS"))
//					field.setFieldAction(GenericArtifactField.FieldActionValue.APPEND);
//				if(columnName!=null && !(columnName.equals("BG_DEV_COMMENTS")) )
//					field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			}
			
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
				field.setFieldValue(flexFieldValues[i]);
			}
			return genericArtifact;
		}
		return null;
	}
	public FieldValueTypeValue convertSFDatdaTypeToGADatatype(Class fieldType) {
		if(fieldType == String.class){
			return GenericArtifactField.FieldValueTypeValue.STRING;
		}
		else if(fieldType == Date.class){
			return GenericArtifactField.FieldValueTypeValue.DATE;
		} else if(fieldType == int.class){
			return GenericArtifactField.FieldValueTypeValue.INTEGER;
		}
		else{
			System.err.println("Field type "+fieldType+" not handled.");
		}
			
//		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("UserCombo")) )
//			return GenericArtifactField.FieldValueTypeValue.USER;
//		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("ListCombo")) )
//			return GenericArtifactField.FieldValueTypeValue.LIST;
//		if(dataType.equals("memo"))
//			return GenericArtifactField.FieldValueTypeValue.HTMLSTRING;
//		if(dataType.equals("char") && ( editStyle!=null && editStyle.equals("TreeCombo")) )
//			return GenericArtifactField.FieldValueTypeValue.STRING;
//		if(dataType.equals("number")) 
//			return GenericArtifactField.FieldValueTypeValue.INTEGER;
//		if(dataType.equals("DATE") && (editStyle!=null && editStyle.equals("DateCombo")) ) 
			
		
		return GenericArtifactField.FieldValueTypeValue.STRING;
	}
}
