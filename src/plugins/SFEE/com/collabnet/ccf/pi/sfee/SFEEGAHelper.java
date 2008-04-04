package com.collabnet.ccf.pi.sfee;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;

import org.openadaptor.core.exception.RecordFormatException;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;

public class SFEEGAHelper {
	private static final DateFormat df = GenericArtifactHelper.df;

	public static boolean containsSingleField(GenericArtifact ga, String fieldName){
		List<GenericArtifactField> gaFolderIDs = ga.getAllGenericArtifactFieldsWithSameFieldName(fieldName);
		return gaFolderIDs!=null && gaFolderIDs.size() == 1;
	}

	public static void updateSingleField(GenericArtifact ga, String fieldName,
			String fieldValue) {
		List<GenericArtifactField> gaFolderIDs = ga.getAllGenericArtifactFieldsWithSameFieldName(fieldName);
		if(gaFolderIDs != null && gaFolderIDs.size() == 1){
			GenericArtifactField field = gaFolderIDs.get(0);
			field.setFieldValue(fieldValue);
		}
	}
	
	public static Object getSingleValue(GenericArtifact ga, String fieldName) {
		List<GenericArtifactField> gaFolderIDs = ga.getAllGenericArtifactFieldsWithSameFieldName(fieldName);
		if(gaFolderIDs != null && gaFolderIDs.size() == 1){
			GenericArtifactField field = gaFolderIDs.get(0);
			return field.getFieldValue();
		}
		return null;
	}

	public static void addField(GenericArtifact ga, String fieldName,
			String fieldValue, String fieldType) {
		GenericArtifactField newField = ga.addNewField(fieldName, fieldName, fieldType);
		newField.setFieldValue(fieldValue);
	}
	
	public static final Object asTypedValue(String value, String type) throws RecordFormatException {
		if (value==null)
			return null;
		// TODO Find out what types are possible as well
	    Object result = value;
	    if (type != null) {// Need to apply it.
	      String exceptionMessage = null;
	      Throwable t = null;
	      try {
	        if ("Double".equalsIgnoreCase(type))
	          result = new Double(value);
	        else if ("Integer".equalsIgnoreCase(type))
		          result = new Integer(value);
	        else if ("Long".equalsIgnoreCase(type))
	          result = new Long(value);
	        else if ("DateTime".equalsIgnoreCase(type))
	          result = df .parse(value);
	        else if ("Date".equalsIgnoreCase(type)) {
	        	GregorianCalendar cal=new GregorianCalendar();
	        	cal.setTime(df.parse(value));
	        	result=cal;
	        }
	        else if ("Boolean".equalsIgnoreCase(type))
		          result = new Boolean(value);
	        else if ("User".equalsIgnoreCase(type))
		          result = new String(value);
	        else if (!"String".equalsIgnoreCase(type))
	          throw new RecordFormatException("Type "+type+" unknown to XMLTypeConverter");
	      } catch (ParseException pe) {
	        exceptionMessage = "Failed to get typed value for " + value + ". Exception: " + pe.getMessage();
	        t = pe;
	      } catch (NumberFormatException nfe) {
	        exceptionMessage = "Failed to get typed value for " + value + ". Exception: " + nfe.getMessage();
	        t = nfe;
	      }
	      if (exceptionMessage != null) {
	        throw new RecordFormatException(exceptionMessage, t);
	      }
	    }
	    return result;
	  }
}
