/**
 * 
 */
package com.collabnet.ccf.pi.qc;

import java.util.ArrayList;
import java.util.List;

import com.collabnet.ccf.core.config.Field;
import com.collabnet.ccf.pi.qc.api.IBug;
import com.collabnet.ccf.pi.qc.api.IConnection;
import com.collabnet.ccf.pi.qc.api.dcom.Bug;
import com.jacob.com.Dispatch;


/**
 * @author Collabnet (c) 2008
 *
 */
public class QCDefect extends Bug implements IQCDefect {
	List<Field> fields;
	List <byte []> attachmentData;

	public QCDefect(Dispatch arg0)
	{
		super(arg0);
	}

	public QCDefect(Bug bug){
		super(bug);
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public void fillFieldsFromBug(IConnection qcc) {
		fields = QCConfigHelper.getSchemaFields(qcc);

		for(int cnt = 0 ; cnt < fields.size() ; cnt++) {
			Field thisField = fields.get(cnt);
			String thisFieldsDatatype = thisField.getDatatype();
			if (thisFieldsDatatype.equals(QCConfigHelper.numberDataType)) {
				thisField.setSingleValue(Integer.toString(getFieldAsInt(thisField.getName())));				
			}
			else {
				/* datatype can be one of:
				 * QCConfigHelper.charDataType
				 * QCConfigHelper.memoDataType
				 * QCConfigHelper.dateDataType
				 * 
				 * Handle them all as strings
				 */ 
				thisField.setSingleValue(getFieldAsString(thisField.getName()));
			}
		}
	
	}

}
