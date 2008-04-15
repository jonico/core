/**
 * 
 */
package com.collabnet.ccf.pi.qc.v90;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.StringTokenizer;

import com.collabnet.ccf.core.ga.GenericArtifactAttachment;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IFactory;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Bug;
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
		GenericArtifact dummyArtifact = new GenericArtifact();
		return dummyArtifact;
	}
	
	public GenericArtifact getGenericArtifactObject(IConnection qcc, String actionId, String entityId, List<String> attachOperation) {
		genericArtifact = QCConfigHelper.getSchemaFields(qcc);
		if(attachOperation!=null)
			genericArtifact = QCConfigHelper.getSchemaAttachments(qcc, genericArtifact, actionId, entityId, attachOperation.get(2));
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
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.HTMLSTRING)) {
				thisField.setFieldValue(getFieldAsString(thisField.getFieldName()));				
			}
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.LIST)) {
				thisField.setFieldValue(getFieldAsString(thisField.getFieldName()));				
			}
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.USER)) {
				thisField.setFieldValue(getFieldAsString(thisField.getFieldName()));				
			}
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.BASE64STRING)) {
				thisField.setFieldValue(getFieldAsInt(thisField.getFieldName()));				
			}
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.MULTI_SELECT_LIST)) {
				String fieldValue = getFieldAsString(thisField.getFieldName());
				List<String> fieldValues = getFieldValues(fieldValue);
				int size = fieldValues.size();
				if(size>=1)thisField.setFieldValue(fieldValues.get(0));
				for(int sizeCnt=1; sizeCnt < size; sizeCnt++) {
					GenericArtifactField field;
					field = genericArtifact.addNewField(thisField.getFieldName(), thisField.getFieldDisplayName(), GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
					field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.MULTI_SELECT_LIST);
					field.setFieldValue(fieldValues.get(sizeCnt));
				}
			}
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.STRING)) {
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
			if (thisFieldsDatatype.equals(GenericArtifactField.FieldValueTypeValue.UNKNOWN)) {
				thisField.setFieldValue(getFieldAsString(thisField.getFieldName()));				
			}
			
		}
		if(attachOperation!=null) {
		// filling the values for attachment
		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.getItem(entityId);
				
		GenericArtifactAttachment thisAttachment = genericArtifact.getAllGenericArtifactAttachments().get(0);
		
		if(thisAttachment.getAttachmentContentType().equals(GenericArtifactAttachment.AttachmentContentTypeValue.DATA)) {
			byte data[] = bug.retrieveAttachmentData(attachOperation.get(2));
			System.out.println("************************************************");
			for (byte b : data) {
				System.out.print((char) b);
			}
			System.out.println("************************************************");
			long attachmentSize = (long) data.length;
			thisAttachment.setAttachmentSize(attachmentSize);
			thisAttachment.setRawAttachmentData(data);
			thisAttachment.setAttachmentSourceUrl("VALUE_UNKNOWN");
		}
		else {
			thisAttachment.setAttachmentSourceUrl(thisAttachment.getAttachmentName());
			thisAttachment.setAttachmentSize(0);
			//genericArtifactAttachment.setRawAttachmentData(null);
		}
		}
		
		return genericArtifact;
	
	}
	
	
	public List<String> getFieldValues(String fieldValue) {
		
		List<String> fieldValues = new ArrayList();
		StringTokenizer st = new StringTokenizer(fieldValue, ";"); 
		while(st.hasMoreTokens()) {
		String thisFieldValue = st.nextToken();
		fieldValues.add(thisFieldValue);
		}
		return fieldValues;
	}
	
	
	

}
