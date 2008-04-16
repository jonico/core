package com.collabnet.ccf.pi.sfee.v44;

import java.util.ArrayList;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class GenericArtifactToSFEEConverter implements IGAToArtifactConverter {
	
	
	public Object convert(GenericArtifact genericArtifact, TrackerFieldSoapDO[] flexFields) {
		ArtifactSoapDO artifactObj = null;
		if(genericArtifact != null){
			artifactObj = new ArtifactSoapDO();
			addFlexFields(artifactObj, flexFields);
			for(GenericArtifactField field:genericArtifact.getAllGenericArtifactFields())
			{
				String fieldName = field.getFieldName();
				SFEEArtifactMetaData.setFieldValue(fieldName, artifactObj, field.getFieldValue());
			}
		}
		return artifactObj;
	}

	private void addFlexFields(ArtifactSoapDO artifactObj,
			TrackerFieldSoapDO[] flexFields) {
		ArrayList<String> fieldNames = new ArrayList<String>();
		ArrayList<String> fieldTypes = new ArrayList<String>();
		ArrayList<Object> fieldValues = new ArrayList<Object>();
		
		for(int i=0; i < flexFields.length; i++){
			String fieldName = flexFields[i].getName();
			if(SFEEArtifactMetaData.isUserDefined(fieldName)){
				System.out.println("Adding flex Field... "+fieldName);
				fieldNames.add(SFEEArtifactMetaData.convertFieldName(fieldName));
				fieldTypes.add(flexFields[i].getValueType());
				fieldValues.add(null);
			}
		}
		SoapFieldValues soapFieldValues = new SoapFieldValues();
		soapFieldValues.setNames(fieldNames.toArray(new String[0]));
		soapFieldValues.setTypes(fieldTypes.toArray(new String[0]));
		soapFieldValues.setValues(fieldValues.toArray(new Object[0]));
		artifactObj.setFlexFields(soapFieldValues);
	}

}
