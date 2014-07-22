package com.collabnet.ccf.splunk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.auxil.connector.iostream.writer.DynamicFileWriteConnector;

import com.collabnet.ccf.core.utils.DummyArtifactSoapDO;
import com.collabnet.ce.soap50.types.SoapFieldValues;
import com.collabnet.ce.soap50.webservices.scm.ScmFileSoapList;
import com.collabnet.ce.soap50.webservices.scm.ScmFileSoapRow;

public class FileSplunkWriter extends DynamicFileWriteConnector {

    public final static Log log             = LogFactory
                                                    .getLog(FileSplunkWriter.class);
    public boolean          isElasticSearch = false;
    public String           eventsDirectory = "/opt/collabnet/teamforge/splunk";

    @Override
    public Object deliver(Object[] data) {
        if (data[0] == null) {
            return null;
        }
        //Hope this is the one it comes
        DummyArtifactSoapDO artifactSoapDO = (DummyArtifactSoapDO) data[0];
        String type = artifactSoapDO.getType();
        String operation = artifactSoapDO.getOperation();
        String projectId = artifactSoapDO.getProjectIdString();
        Date currentDate = artifactSoapDO.getLastModifiedDate();

        //        File propertiesFile = determineEventsFile(eventsDirectory, type,
        //                operation, projectId == null ? "proj1000" : projectId,
        //                currentDate);

        String timeString = createTimeString(currentDate);
        setFilename(timeString + "--" + projectId + "-" + type + "-"
                + operation + ".properties");
        //        log.info("Going to create file  " + propertiesFile.getAbsolutePath());

        //            String comment = getEventContext().getComment();
        //            String userName = getEventContext().getUsername();
        //            String originalDataClassName = getOriginalData().getClass().toString();
        //            String updatedDataClassName = getUpdatedData().getClass().toString();

        Object originalData = artifactSoapDO.getOriginalData();
        Object updatedData = artifactSoapDO.getUpdatedData();
        // Properties eventProperties = new Properties();
        Map<String, String> eventProperties = new HashMap<String, String>();
        FieldNameAmbiguityDissolver dissolver = new FieldNameAmbiguityDissolver();

        eventProperties.put("timestamp", formatIfDate(currentDate));
        eventProperties.put("type", type);
        eventProperties.put("operation", operation);
        eventProperties
                .put("projectId", projectId == null ? "null" : projectId);
        //        eventProperties.put("user", userName);
        //        eventProperties.put("comment", comment == null ? "null" : comment);
        try {
            if (originalData != null) {
                //                eventProperties.put("operation", "update");
                Class originalDataClass = originalData.getClass();
                Method[] methods = originalDataClass.getMethods();
                for (Method m : methods) {
                    // we are only interested in getter methods
                    if (!m.getName().startsWith("get")) {
                        continue;
                    }
                    // we are only interested in methods that do not take any parameters
                    if (m.getParameterTypes().length != 0) {
                        continue;
                    }
                    // should we only invoke methods with a simple return type?
                    Object result = m.invoke(originalData);
                    if (result != null && result instanceof SoapFieldValues) {
                        SoapFieldValues fieldValues = (SoapFieldValues) result;
                        for (int i = 0; i < fieldValues.getNames().length; ++i) {
                            // String fieldValue =

                            String fieldName = fieldValues.getNames()[i];
                            eventProperties.put("_ofname_" + i,
                                    fieldName == null ? "null" : fieldName);
                            String fieldType = fieldValues.getTypes()[i];
                            eventProperties.put("_oftype_" + i,
                                    fieldType == null ? "null" : fieldType);
                            Object fieldValueObject = fieldValues.getValues()[i];
                            String fieldValueString = formatIfDate(fieldValueObject);
                            eventProperties.put("_ofvalue_" + i,
                                    fieldValueString);

                            if (fieldName != null) {
                                eventProperties.put(
                                        dissolver.generateNewFieldName("of_"
                                                + fieldName, true),
                                        fieldValueString);
                            }
                        }
                    } else {
                        eventProperties.put("o_" + m.getName().substring(3),
                                formatIfDate(result));
                    }
                }
            }
            Class updatedDataClass = updatedData.getClass();
            Method[] methods = updatedDataClass.getMethods();
            for (Method m : methods) {
                // we are only interested in getter methods
                if (!m.getName().startsWith("get")) {
                    continue;
                }
                // we are only interested in methods that do not take any parameters
                if (m.getParameterTypes().length != 0) {
                    continue;
                }
                // should we only invoke methods with a simple return type?
                Object result = m.invoke(updatedData);
                if (result != null && result instanceof SoapFieldValues) {
                    SoapFieldValues fieldValues = (SoapFieldValues) result;
                    for (int i = 0; i < fieldValues.getNames().length; ++i) {
                        // String fieldValue =

                        String fieldName = fieldValues.getNames()[i];
                        eventProperties.put("_fname_" + i,
                                fieldName == null ? "null" : fieldName);
                        String fieldType = fieldValues.getTypes()[i];
                        eventProperties.put("_ftype_" + i,
                                fieldType == null ? "null" : fieldType);
                        Object fieldValueObject = fieldValues.getValues()[i];
                        String fieldValueString = formatIfDate(fieldValueObject);
                        eventProperties.put("_fvalue_" + i, fieldValueString);

                        if (fieldName != null) {
                            eventProperties.put(
                                    dissolver.generateNewFieldName("f_"
                                            + fieldName, true),
                                    fieldValueString);
                        }
                    }
                } else if (result != null && result instanceof ScmFileSoapList) {
                    ScmFileSoapList scmList = (ScmFileSoapList) result;
                    ScmFileSoapRow[] scmDataRows = scmList.getDataRows();
                    for (int i = 0; i < scmDataRows.length; ++i) {
                        eventProperties.put(dissolver.generateNewFieldName(
                                "scm_commitMessage", false), scmDataRows[i]
                                .getCommitMessage());
                        eventProperties.put(dissolver.generateNewFieldName(
                                "scm_fileName", false), scmDataRows[i]
                                .getFilename());
                        eventProperties
                                .put(dissolver.generateNewFieldName("scm_id",
                                        false), scmDataRows[i].getId());
                        eventProperties.put(dissolver.generateNewFieldName(
                                "scm_refFileName", false), scmDataRows[i]
                                .getRefFilename());
                        eventProperties.put(dissolver.generateNewFieldName(
                                "scm_status", false), scmDataRows[i]
                                .getStatus());
                        eventProperties.put(dissolver.generateNewFieldName(
                                "scm_version", false), scmDataRows[i]
                                .getVersion());
                    }
                } else {
                    eventProperties.put("" + m.getName().substring(3),
                            formatIfDate(result));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            //            PrintWriter writer = new PrintWriter(eventsFile);
            //            if (!isElasticSearch) {
            //                writer.print(iso8601Date(currentDate) + " ");
            //            }
            List<String> collectionVaList = new ArrayList<String>();
            StringBuilder builder = new StringBuilder(iso8601Date(currentDate));
            builder.append(" ");
            for (String key : eventProperties.keySet()) {
                if (!key.equals("o_Description") && !key.equals("Description")) {
                    String valString = key + "="
                            + format(eventProperties.get(key));
                    builder.append(valString);
                    builder.append(" ");
                } else {
                    int fieldValueLength = eventProperties.get(key).length();
                    if (fieldValueLength > 512) {
                        String valString = key
                                + "="
                                + format(eventProperties.get(key).substring(0,
                                        512));
                        builder.append(valString);
                        builder.append(" ");
                    } else {
                        String valString = key + "="
                                + format(eventProperties.get(key));
                        builder.append(valString);
                        builder.append(" ");
                    }
                }
            }
            String keyVal = builder.toString().trim();
            collectionVaList.add(keyVal);
            super.deliver(collectionVaList.toArray());
            //            writer.close();
        } catch (Exception e) {
            //            log.error("Could not dump event " + eventsFile.getName() + ": "
            //                    + e.getMessage());
            e.printStackTrace();
        }
        return artifactSoapDO.getGenericArtifact();
    }

    public String getEventsDirectory() {
        return eventsDirectory;
    }

    public boolean isElasticSearch() {
        return isElasticSearch;
    }

    public void setElasticSearch(boolean isElasticSearch) {
        this.isElasticSearch = isElasticSearch;
    }

    public void setEventsDirectory(String eventsDirectory) {
        this.eventsDirectory = isElasticSearch() ? "/opt/collabnet/teamforge/events"
                : eventsDirectory;
    }

    //TODO: need to uncomment this one later
    public void validate(List exceptions) {
        //        super.validate(exceptions);
        //        if (! scriptProvided) {
        //          exceptions.add(new ValidationException("script property not set", this));
        //        }
    }

    protected String deriveFilename(Object[] data) {
        return (data != null && data.length > 0) ? getFilename()
                : getFilename();
    }

    protected OutputStream getOutputStream() {
        if (getFilename() != null) {
            if (getMoveExistingFileTo() != null) {
                //            super.moveOutputFile();
            }
            try {
                File dumpFile = new File(getEventsDirectory(), getFilename());
                return new FileOutputStream(dumpFile, isAppend());
            } catch (FileNotFoundException e) {
                throw new RuntimeException("FileNotFoundException, "
                        + e.getMessage(), e);
            }
        } else {
            return System.out;
        }
    }

    private String createTimeString(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        // DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(tz);
        return date.getTime() + "-" + df.format(date);
    }

    private File determineEventsFile(String directory, String type,
            String operation, String projectId, Date date) {
        String timeString = createTimeString(date);

        return new File(directory, timeString + "--" + projectId + "-" + type
                + "-" + operation + ".properties");
    }

    private File determineJSonFile(String directory, String type,
            String operation, String projectId, Date date) {
        String timeString = createTimeString(date);

        return new File(directory, timeString + "--" + projectId + "-" + type
                + "-" + operation + ".json");
    }

    private String format(String value) {
        if (value == null) {
            return "null";
        } else {
            return '"' + value.replace('"', ' ').replace('\n', ' ') + '"';
        }
    }

    private String formatIfDate(Object object) {
        if (object == null) {
            return "null";
        } else if (object instanceof Date) {
            return iso8601Date((Date) object);
        } else {
            return object.toString();
        }
    }

    //private void storeToJsonFile(File jsonFile,
    //            Map<String, String> eventProperties) {
    //    JSONObject root = new JSONObject();
    //
    //    for (String key : eventProperties.keySet()) {
    //            root = root.element(key, eventProperties.get(key));
    //    }
    //    try {
    //            PrintWriter writer = new PrintWriter(jsonFile);
    //            writer.println(root.toString());
    //            writer.close();
    //    } catch (FileNotFoundException e) {
    //            log.error("Could not dump event " + jsonFile.getName() + ": "
    //                            + e.getMessage());
    //    }
    //}

    private String iso8601Date(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = isElasticSearch ? new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss") : new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss:SSSZ");
        df.setTimeZone(tz);
        return df.format(date);
    }

    private void storeToPropertyFile(File eventsFile,
            Map<String, String> eventProperties, Date currentDate) {
        try {
            PrintWriter writer = new PrintWriter(eventsFile);
            if (!isElasticSearch) {
                writer.print(iso8601Date(currentDate) + " ");
            }
            for (String key : eventProperties.keySet()) {
                writer.print(key + "=" + format(eventProperties.get(key)));
                if (isElasticSearch) {
                    writer.println();
                } else {
                    writer.print(" "); // newlines seem to disturb Splunk
                }
                // encode the type into it for immature technologies
                if (isElasticSearch) {
                    typeEncoding(writer, key, eventProperties.get(key));
                }
            }
            writer.close();
        } catch (Exception e) {
            log.error("Could not dump event " + eventsFile.getName() + ": "
                    + e.getMessage());
        }
    }

    private void typeEncoding(PrintWriter writer, String key, String value) {
        if (value.equals("null")) {
            return;
        }
        try {
            int intValue = Integer.parseInt(value);
            writer.println("int_" + key + "="
                    + format(Integer.toString(intValue)));
            return;
        } catch (NumberFormatException e) {
        }
        try {
            float floatValue = Float.parseFloat(value);
            writer.println("float_" + key + "="
                    + format(Float.toString(floatValue)));
            return;
        } catch (NumberFormatException e) {
        }

    }

}
