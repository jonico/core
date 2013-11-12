/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.teamforge;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.openadaptor.core.exception.RecordFormatException;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;

/**
 * This is a utility class that is used to extract fields from Generic artifact,
 * get the value of a field, etc.
 * 
 * @author madhusuthanan
 * 
 */
public class TFGAHelper {
    private static final DateFormat df = GenericArtifactHelper.df;

    public static void addField(GenericArtifact ga, String fieldName,
            String fieldValue, String fieldType,
            FieldValueTypeValue fieldValueTypeValue) {
        GenericArtifactField newField = ga.addNewField(fieldName, fieldType);
        newField.setFieldValueType(fieldValueTypeValue);
        newField.setFieldValue(fieldValue);
    }

    public static final Object asTypedValue(String value, String type)
            throws RecordFormatException {
        if (value == null)
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
                else if ("DateTime".equalsIgnoreCase(type)) {
                    synchronized (df) {
                        result = df.parse(value);
                    }
                } else if ("Date".equalsIgnoreCase(type)) {
                    GregorianCalendar cal = new GregorianCalendar();
                    synchronized (df) {
                        Date date = df.parse(value);
                        cal.setTime(date);
                    }
                    result = cal;
                } else if ("Time".equalsIgnoreCase(type)) {
                    long time = Long.parseLong(value);
                    Date date = new Date(time);
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(date);
                    result = cal;
                } else if ("Boolean".equalsIgnoreCase(type))
                    result = Boolean.valueOf(value);
                else if ("User".equalsIgnoreCase(type))
                    result = value;
                else if (!"String".equalsIgnoreCase(type))
                    throw new RecordFormatException("Type " + type
                            + " unknown to XMLTypeConverter");
            } catch (ParseException pe) {
                exceptionMessage = "Failed to get typed value for " + value
                        + ". Exception: " + pe.getMessage();
                t = pe;
            } catch (NumberFormatException nfe) {
                exceptionMessage = "Failed to get typed value for " + value
                        + ". Exception: " + nfe.getMessage();
                t = nfe;
            }
            if (exceptionMessage != null) {
                throw new RecordFormatException(exceptionMessage, t);
            }
        }
        return result;
    }

    public static boolean containsSingleField(GenericArtifact ga,
            String fieldName) {
        List<GenericArtifactField> gaFolderIDs = ga
                .getAllGenericArtifactFieldsWithSameFieldName(fieldName);
        return gaFolderIDs != null && gaFolderIDs.size() == 1;
    }

    public static boolean containsSingleMandatoryField(GenericArtifact ga,
            String fieldName) {
        List<GenericArtifactField> gaFolderIDs = ga
                .getAllGenericArtifactFieldsWithSameFieldTypeAndFieldName(
                        GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
                        fieldName);
        return (gaFolderIDs != null && gaFolderIDs.size() > 0);
    }

    public static Object getSingleValue(GenericArtifact ga, String fieldName) {
        List<GenericArtifactField> fields = ga
                .getAllGenericArtifactFieldsWithSameFieldName(fieldName);
        if (fields != null && fields.size() == 1) {
            GenericArtifactField field = fields.get(0);
            return field.getFieldValue();
        }
        return null;
    }

    public static void updateSingleField(GenericArtifact ga, String fieldName,
            String fieldValue) {
        List<GenericArtifactField> gaFolderIDs = ga
                .getAllGenericArtifactFieldsWithSameFieldName(fieldName);
        if (gaFolderIDs == null) {
            throw new CCFRuntimeException(
                    "Field "
                            + fieldName
                            + " does not exist in Generic Artifact. Cannot update field");
        }
        if (gaFolderIDs != null && gaFolderIDs.size() == 1) {
            GenericArtifactField field = gaFolderIDs.get(0);
            field.setFieldValue(fieldValue);
        }
    }

    public static void updateSingleMandatoryField(GenericArtifact ga,
            String fieldName, String fieldValue) {
        boolean fieldUpdated = false;
        List<GenericArtifactField> gaFolderIDs = ga
                .getAllGenericArtifactFieldsWithSameFieldTypeAndFieldName(
                        GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD,
                        fieldName);
        if (gaFolderIDs != null) {
            for (GenericArtifactField field : gaFolderIDs) {
                field.setFieldValue(fieldValue);
                fieldUpdated = true;
            }
        }
        if (gaFolderIDs == null || (!fieldUpdated)) {
            throw new CCFRuntimeException(
                    "Field "
                            + fieldName
                            + " does not exist in Generic Artifact. Cannot update field");
        }
    }
}
