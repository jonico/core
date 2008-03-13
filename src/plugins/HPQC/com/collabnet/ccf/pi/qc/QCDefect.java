/**
 * 
 */
package com.collabnet.ccf.pi.qc;

import java.util.ArrayList;
import java.util.List;

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
			
			thisField.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
			thisField.setFieldValueHasChanged(true);
						
			GenericArtifactField.FieldValueTypeValue thisFieldsDatatype = thisField.getFieldValueType();
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.INTEGER)) {
				thisField.setFieldValue(getFieldAsInt(thisField.getFieldName()));				
			}
			else {
				/* TODO: datatype can also be one of the other GenericArtifactField.FieldValueTypeValue
				 * types. Handle them appropriately.
				 */ 
				thisField.setFieldValue(getFieldAsString(thisField.getFieldName()));
			}
		}
 	    
		return genericArtifact;
	
	}

}
