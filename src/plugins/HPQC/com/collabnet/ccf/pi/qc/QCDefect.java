/**
 * 
 */
package com.collabnet.ccf.pi.qc;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.pi.qc.api.IBug;
import com.collabnet.ccf.pi.qc.api.IConnection;
import com.collabnet.ccf.pi.qc.api.dcom.Bug;
import com.jacob.com.Dispatch;


/**
 * @author Collabnet (c) 2008
 *
 */
public class QCDefect extends Bug implements IQCDefect {
	GenericArtifact genericArtifact;
	List <byte []> attachmentData;

	public QCDefect(Dispatch arg0)
	{
		super(arg0);
	}

	public QCDefect(Bug bug){
		super(bug);
	}

	public GenericArtifact getGenericArtifact() {
		return genericArtifact;
	}

	public void setGenericArtifact(GenericArtifact genericArtifact) {
		this.genericArtifact = genericArtifact;
	}

	public GenericArtifact getGenericArtifactObject(IConnection qcc) {
		genericArtifact = QCConfigHelper.getSchemaFields(qcc);

		List<GenericArtifactField> allFields = genericArtifact.getAllGenericArtifactFields();
		int noOfFields = allFields.size();
		for(int cnt = 0 ; cnt < noOfFields ; cnt++) {
			GenericArtifactField thisField = allFields.get(cnt);
			
			GenericArtifactField.FieldActionValue thisFieldsActionValue = thisField.getFieldAction();
			if (thisFieldsActionValue.equals(GenericArtifactField.FieldActionValue.REPLACE)) {
				thisField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			}
			if (thisFieldsActionValue.equals(GenericArtifactField.FieldActionValue.APPEND)) {
				thisField.setFieldAction(GenericArtifactField.FieldActionValue.APPEND);
			}
			else 
				thisField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			
			//thisField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			
			thisField.setFieldValueHasChanged(true);
			
			GenericArtifactField.FieldValueTypeValue thisFieldsDatatype = thisField.getFieldValueType();
			
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.DATE)) {
				thisField.setFieldValue(getFieldAsDate(thisField.getFieldName()));				
			}
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.INTEGER)) {
				thisField.setFieldValue(getFieldAsInt(thisField.getFieldName()));				
			}
			else {
				/* TODO: datatype can also be one of the other GenericArtifactField.FieldValueTypeValue
				 * types. Handle them appropriately.
				 */ 
				String fieldName = thisField.getFieldName();
				String fieldValueAsString = new String();
				if(fieldName.equals("BG_VTS")) {
					String fieldValue = getFieldAsString(fieldName);
					thisField.setFieldValue(fieldValue);
				}
				else {
					fieldValueAsString = getFieldAsString(fieldName);
					thisField.setFieldValue(fieldValueAsString);
				}
				
			}
			
		}
 	    
		return genericArtifact;
	
	}

}
