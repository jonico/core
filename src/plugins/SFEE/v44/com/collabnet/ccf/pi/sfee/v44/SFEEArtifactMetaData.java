package com.collabnet.ccf.pi.sfee.v44;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.collabnet.ccf.pi.sfee.v44.meta.ArtifactMetaData;
import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class SFEEArtifactMetaData {
	private static HashMap<String,Class> artifactFieldsType = new HashMap<String, Class>();
	private static final Pattern getterMethodPattern = Pattern.compile("get[A-Z][a-zA-Z]*");
	private static final Pattern setterMethodPattern = Pattern.compile("set[A-Z][a-zA-Z]*");
	private static final String FLEX_FIELDS = "[gs]etFlexFields";
	static {
		loadFieldNames();
	}
	public static Set<String> getArtifactFields(){
		return artifactFieldsType.keySet();
	}
	public static Class getFieldType(String fieldName){
		return artifactFieldsType.get(fieldName);
	}
	public static void setFieldValue(String fieldName, ArtifactSoapDO artifactRow, 
										Object value) {
		System.out.println();
		Class c = artifactRow.getClass();
		if(value == null) return;
		try {
			Class fieldType = artifactFieldsType.get(fieldName);
			Class[] empty = new Class[]{fieldType};
			Object castedValue = null;
			if(!(fieldType == value.getClass()))
			{
				if(fieldType == String.class){
					castedValue = value.toString();
				}
				else if(fieldType == int.class){
					if(value instanceof String){
						String sValue = (String) value;
						int intValue = Integer.parseInt(sValue);
						castedValue = intValue;
					}
					else if(value instanceof Integer){
						castedValue = value;
					}
				}
				else if(fieldType == Date.class){
					if(value instanceof String){
						String dateString = (String) value;
						if(fieldName.equalsIgnoreCase(ArtifactMetaData.SFEEFields.closeDate.getFieldName())){
							castedValue = SFEEGAHelper.asTypedValue(dateString, "Time");
						}
						else {
							castedValue = SFEEGAHelper.asTypedValue(dateString, "DateTime");
						}
					}
				}
			} else{
				castedValue = value;
			}
			Method m = c.getMethod("set"+fieldName, empty);
			Object[] emptyObj = new Object[]{castedValue};
			Object o = m.invoke(artifactRow, emptyObj);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// I think this is a flex field so let me set the flex field for this DO
			setFlexFieldValue(fieldName, artifactRow, value);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setFlexFieldValue(String fieldName,
			ArtifactSoapDO artifactRow, Object value) {
		SoapFieldValues flexFields = artifactRow.getFlexFields();
		String[] fieldNames = null;
		Object[] fieldValues = null;
		String[] fieldTypes = null;
		if(flexFields != null){
			fieldNames = flexFields.getNames();
			fieldValues = flexFields.getValues();
			fieldTypes = flexFields.getTypes();
			for(int i=0; i < fieldNames.length; i++){
				if(fieldNames[i].equals(fieldName)){
					fieldValues[i] = value;
					return;
				}
			}
		}
		else {
			flexFields = new SoapFieldValues();
			fieldNames = flexFields.getNames();
			fieldValues = flexFields.getValues();
			fieldTypes = flexFields.getTypes();
		}
		if(fieldNames != null){
			String[] newFieldNames = new String[fieldNames.length+1];
			System.arraycopy(fieldNames, 0, newFieldNames, 0, fieldNames.length);
			newFieldNames[fieldNames.length] = fieldName;
			fieldNames = newFieldNames;
		}
		else {
			fieldNames = new String[]{fieldName};
		}
		if(fieldValues != null){
			Object[] newfieldValues = new Object[fieldValues.length+1];
			System.arraycopy(fieldValues, 0, newfieldValues, 0, fieldValues.length);
			newfieldValues[fieldValues.length] = value;
			fieldValues = newfieldValues;
		}
		else {
			fieldValues = new Object[]{value};
		}
		if(fieldTypes != null){
			String[] newfieldTypes = new String[fieldTypes.length+1];
			System.arraycopy(fieldTypes, 0, newfieldTypes, 0, fieldTypes.length);
			if(value instanceof Date){
				newfieldTypes[fieldTypes.length] = TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE;
			}
			else if(value instanceof String){
				//TODO Handle user data types
				newfieldTypes[fieldTypes.length] = TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING;
				//TrackerFieldSoapDO.FIELD_VALUE_TYPE_USER
			}
			fieldTypes = newfieldTypes;
		}
		else {
			String fieldType = null;
			if(value instanceof Date){
				fieldType = TrackerFieldSoapDO.FIELD_VALUE_TYPE_DATE;
			}
			else if(value instanceof String){
				//TODO Handle user data types
				fieldType = TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING;
				//TrackerFieldSoapDO.FIELD_VALUE_TYPE_USER
			}
			fieldTypes = new String[]{fieldType};
		}
		flexFields.setNames(fieldNames);
		flexFields.setValues(fieldValues);
		flexFields.setTypes(fieldTypes);
	}
	
	private static void loadFieldNames(){
		if(artifactFieldsType.isEmpty()){
			Class artifactSoapDOClass = ArtifactSoapDO.class;
			Method[] methods = artifactSoapDOClass.getMethods();
			for(Method method:methods){
				String name = method.getName();
				System.out.println(name);
				Matcher getterMatcher = getterMethodPattern.matcher(name);
				//Matcher propertyGrabber = fieldNamePattern.matcher(name);
				Matcher setterMatcher = setterMethodPattern.matcher(name);
				if(getterMatcher.matches() && (!Pattern.matches(FLEX_FIELDS, name))){
					String setterName = name.replaceAll("^[g]", "s");
					try {
						artifactSoapDOClass.getMethod(setterName, method.getReturnType());
						String fieldName = name.substring(3);
						artifactFieldsType.put(fieldName,method.getReturnType());
					} catch (SecurityException e) {
//						e.printStackTrace();
					} catch (NoSuchMethodException e) {
//						e.printStackTrace();
					}
				}
			}
			System.out.println(artifactFieldsType);
		}
	}
	
	public static ArtifactSoapDO cloneArtifactSoapDO(ArtifactSoapDO source){
		ArtifactSoapDO cloned = new ArtifactSoapDO();
		for(String field:artifactFieldsType.keySet()){
			Object value = getFieldValue(field, source);
			setFieldValue(field, cloned, value);
		}
		SoapFieldValues flexFields = source.getFlexFields();
		String[] flexFieldNames = flexFields.getNames();
		String[] flexFieldTypes = flexFields.getTypes();
		Object[] flexFieldValues = flexFields.getValues();
		Object[] clonedFlexFieldValues = new Object[flexFieldValues.length];
		System.arraycopy(flexFieldValues, 0, clonedFlexFieldValues, 0, flexFieldValues.length);
		SoapFieldValues clonedFlexFields = cloned.getFlexFields();
		if(clonedFlexFields == null){
			clonedFlexFields = new SoapFieldValues();
		}
		clonedFlexFields.setNames(flexFieldNames);
		clonedFlexFields.setTypes(flexFieldTypes);
		clonedFlexFields.setValues(clonedFlexFieldValues);
		cloned.setFlexFields(clonedFlexFields);
		return cloned;
	}
	public static Object getFieldValue(String fieldName, ArtifactSoapDO artifactRow) {
		Class c = artifactRow.getClass();
		try {
			Class[] empty = new Class[]{};
			Method m = c.getMethod("get"+fieldName, empty);
			Object[] emptyObj = new Object[]{};
			Object o = m.invoke(artifactRow, emptyObj);
			return o;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static boolean isUserDefined(String fieldName){
		Set<String> nonUserDefinedFields = artifactFieldsType.keySet();
		fieldName = convertFieldName(fieldName);
		if(nonUserDefinedFields.contains(fieldName)){
			return false;
		}
		else if(fieldName.equals("ReportedInRelease")){
			return false;
		}
		else if(fieldName.equals("ResolvedInRelease")){
			return false;
		}
		return true;
	}
	public static String convertFieldName(String fieldName){
		return Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
	}
	public static void main(String ar[]){
		SFEEArtifactMetaData.loadFieldNames();
	}
}
