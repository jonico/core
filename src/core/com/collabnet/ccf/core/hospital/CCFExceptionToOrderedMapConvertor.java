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

package com.collabnet.ccf.core.hospital;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.auxil.convertor.exception.ExceptionToOrderedMapConvertor;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.core.IComponent;
import org.openadaptor.core.exception.MessageException;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.XPathUtils;

/**
 * This class extends the openAdaptor exception convertor to add further CCF
 * specific information about an error. If an exception is not a
 * CCFRunTimeException, it will exactly behave like its superclass
 * 
 * @author jnicolai
 * 
 */
public class CCFExceptionToOrderedMapConvertor extends ExceptionToOrderedMapConvertor {
    private static final String ERROR_CODE                     = "ERROR_CODE";
    private static final String GENERICARTIFACT                = "GENERIC_ARTIFACT";
    private static final String TARGET_ARTIFACT_ID             = "TARGET_ARTIFACT_ID";
    private static final String SOURCE_ARTIFACT_ID             = "SOURCE_ARTIFACT_ID";
    private static final String TARGET_REPOSITORY_KIND         = "TARGET_REPOSITORY_KIND";
    private static final String TARGET_SYSTEM_KIND             = "TARGET_SYSTEM_KIND";
    private static final String SOURCE_REPOSITORY_KIND         = "SOURCE_REPOSITORY_KIND";
    private static final String SOURCE_SYSTEM_KIND             = "SOURCE_SYSTEM_KIND";
    private static final String TARGET_REPOSITORY_ID           = "TARGET_REPOSITORY_ID";
    private static final String TARGET_SYSTEM_ID               = "TARGET_SYSTEM_ID";
    private static final String SOURCE_REPOSITORY_ID           = "SOURCE_REPOSITORY_ID";
    private static final String SOURCE_SYSTEM_ID               = "SOURCE_SYSTEM_ID";
    private static final String ARTIFACT_TYPE                  = "ARTIFACT_TYPE";
    private static final String TARGET_ARTIFACT_VERSION        = "TARGET_ARTIFACT_VERSION";
    private static final String SOURCE_ARTIFACT_VERSION        = "SOURCE_ARTIFACT_VERSION";
    private static final String TARGET_LAST_MODIFICATION_TIME  = "TARGET_LAST_MODIFICATION_TIME";
    private static final String SOURCE_LAST_MODIFICATION_TIME  = "SOURCE_LAST_MODIFICATION_TIME";
    private static final String REPOSITORY_MAPPING_DIRECTION   = "REPOSITORY_MAPPING_DIRECTION";
    private static final String DESCRIPTION                    = "DESCRIPTION";
    private static final String VERSION                        = "VERSION";

    static final String         EXCEPTION_MESSAGE              = "EXCEPTION_MESSAGE";

    static final String         CAUSE_EXCEPTION_CLASS          = "CAUSE_EXCEPTION_CLASS_NAME";

    static final String         CAUSE_EXCEPTION_MESSAGE        = "CAUSE_EXCEPTION_MESSAGE";

    static final String         STACK_TRACE                    = "STACK_TRACE";

    static final String         ADAPTOR_NAME                   = "ADAPTOR_NAME";

    // static final String THREAD_NAME = "THREAD_NAME";

    static final String         DATA_TYPE                      = "DATA_TYPE";

    /*
     * Field names, initialised to defaults These field names have been copied
     * from openAdaptor since they are not supported any more in newer versions
     */

    /* Optional property allowing to retrieve an adaptor's name */
    private IComponent          adaptor;

    private String              exceptionMessageColName        = EXCEPTION_MESSAGE;

    private String              causeExceptionClassColName     = CAUSE_EXCEPTION_CLASS;

    private String              causeExceptionMessageColName   = CAUSE_EXCEPTION_MESSAGE;

    private String              stackTraceColName              = STACK_TRACE;

    private String              adaptorColName                 = ADAPTOR_NAME;

    private String              dataTypeColName                = DATA_TYPE;
    /**
     * This property (false by default) determines whether only exceptions that
     * contain a generic artifact as payload will be quarantined
     */
    private boolean             onlyQuarantineGenericArtifacts = false;

    // private String threadNameColName = THREAD_NAME;

    private static final String NO_CAUSE_EXCEPTION             = "No cause exception detected.";
    private static final Log    log                            = LogFactory
                                                                       .getLog(CCFExceptionToOrderedMapConvertor.class);

    static final String         FIXED                          = "FIXED";
    static final String         REPROCESSED                    = "REPROCESSED";

    /**
     * Defines the message template used to construct the warning message issued
     * whenever an artifacts gets quarantined in the hospital. Every column in
     * the hospital data base table can be referenced by setting its name in
     * angle brackets. Example:"It looks as if at <TIMESTAMP> a problem with
     * <ERROR_CODE> has occurred. Stacktrace: <STACKTRACE>"
     */
    private String              logMessageTemplate             = "Artifact reached hospital. Characteristics: \n"
                                                                       + "SOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n"
                                                                       + "SOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n"
                                                                       + "SOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n"
                                                                       + "TARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n"
                                                                       + "ERROR_CODE: <ERROR_CODE>\n"
                                                                       + "TARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n"
                                                                       + "TARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n"
                                                                       + "TIMESTAMP: <TIMESTAMP>\n"
                                                                       + "EXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n"
                                                                       + "EXCEPTION_MESSAGE: <EXCEPTION_MESSAGE>\n"
                                                                       + "CAUSE_EXCEPTION_CLASS_NAME: <CAUSE_EXCEPTION_CLASS_NAME>\n"
                                                                       + "CAUSE_EXCEPTION_MESSAGE: <CAUSE_EXCEPTION_MESSAGE>\n"
                                                                       + "STACK_TRACE: <STACK_TRACE>\n"
                                                                       + "ADAPTOR_NAME: <ADAPTOR_NAME>\n"
                                                                       + "ORIGINATING_COMPONENT: <ORIGINATING_COMPONENT>\n"
                                                                       + "DATA_TYPE: <DATA_TYPE>\n"
                                                                       + "DATA: <DATA>\n"
                                                                       + "THREAD_NAME: <THREAD_NAME>\n"
                                                                       + "FIXED: <FIXED>\n"
                                                                       + "REPROCESSED: <REPROCESSED>\n"
                                                                       + "SOURCE_SYSTEM_KIND: <SOURCE_SYSTEM_KIND>\n"
                                                                       + "SOURCE_REPOSITORY_KIND: <SOURCE_REPOSITORY_KIND>\n"
                                                                       + "TARGET_SYSTEM_KIND: <TARGET_SYSTEM_KIND>\n"
                                                                       + "TARGET_REPOSITORY_KIND: <TARGET_REPOSITORY_KIND>\n"
                                                                       + "SOURCE_LAST_MODIFICATION_TIME: <SOURCE_LAST_MODIFICATION_TIME>\n"
                                                                       + "TARGET_LAST_MODIFICATION_TIME: <TARGET_LAST_MODIFICATION_TIME>\n"
                                                                       + "SOURCE_ARTIFACT_VERSION: <SOURCE_ARTIFACT_VERSION>\n"
                                                                       + "TARGET_ARTIFACT_VERSION: <TARGET_ARTIFACT_VERSION>\n"
                                                                       + "ARTIFACT_TYPE: <ARTIFACT_TYPE>\n"
                                                                       + "GENERIC_ARTIFACT: <GENERIC_ARTIFACT>";

    /**
     * Gets the message template used to construct the warning message issued
     * whenever an artifacts gets quarantined in the hospital. Every column in
     * the hospital data base table can be referenced by setting its name in
     * angle brackets. Example:"It looks as if at <TIMESTAMP> a problem with
     * <ERROR_CODE> has occurred. Stacktrace: <STACKTRACE>"
     */
    public String getLogMessageTemplate() {
        return logMessageTemplate;
    }

    /**
     * Gets whether only exception records that contain a generic artifact as
     * payload should be quaratined The default behavior is to quarantine all
     * exception records
     * 
     * @return
     */
    public boolean isOnlyQuarantineGenericArtifacts() {
        return onlyQuarantineGenericArtifacts;
    }

    /**
     * Optional property that allows to retrieve the name of the adaptor.
     * 
     * @param adaptor
     */
    public void setAdaptor(IComponent adaptor) {
        this.adaptor = adaptor;
    }

    /**
     * Sets the message template used to construct the warning message issued
     * whenever an artifacts gets quarantined in the hospital. Every column in
     * the hospital data base table can be referenced by setting its name in
     * angle brackets. Example:"It looks as if at <TIMESTAMP> a problem with
     * <ERROR_CODE> has occurred. Stacktrace: <STACKTRACE>"
     */
    public void setLogMessageTemplate(String logMessageTemplate) {
        this.logMessageTemplate = logMessageTemplate;
    }

    /**
     * Sets whether only exception records that contain a generic artifact as
     * payload should be quaratined The default behavior is to quarantine all
     * exception records
     * 
     * @param onlyQuarantineGenericArtifacts
     */
    public void setOnlyQuarantineGenericArtifacts(
            boolean onlyQuarantineGenericArtifacts) {
        this.onlyQuarantineGenericArtifacts = onlyQuarantineGenericArtifacts;
    }

    /**
     * Converts the <code>record</code> into an <code>IOrderedMap</code> .
     * 
     * @param record
     *            Object which should be a MessageException instance
     * @return an IOrderedMap representation of the MessageException contents
     */
    protected Object convert(Object record) {
        try {
            boolean quarantineException = true;

            // we have to set these columns due to an SQL incompatibility issue
            // with
            // these fields
            setFixedColName(FIXED);
            setReprocessedColName(REPROCESSED);

            // log.warn("Artifact reached ambulance");
            // first of all we pass the record in our parent method
            Object preprocessedMap = super.convert(record);
            if (preprocessedMap == null
                    || (!(preprocessedMap instanceof IOrderedMap))) {
                return preprocessedMap;
            }
            IOrderedMap map = (IOrderedMap) preprocessedMap;

            // remove entities with wrong data type (string instead of boolean)
            map.remove(FIXED);
            map.remove(REPROCESSED);

            map.put(FIXED, false);
            map.put(REPROCESSED, false);

            MessageException messageException = (MessageException) record;
            map.put(exceptionMessageColName, messageException.getException()
                    .getMessage());

            Throwable cause = messageException.getException().getCause();
            if (cause != null) {
                map.put(causeExceptionClassColName, cause.getClass().getName());
                map.put(causeExceptionMessageColName, cause.getMessage());
            } else {
                map.put(causeExceptionClassColName, NO_CAUSE_EXCEPTION);
                map.put(causeExceptionMessageColName, NO_CAUSE_EXCEPTION);
            }

            Exception exception = messageException.getException();
            StringBuffer stackTraceBuf = new StringBuffer();
            StackTraceElement[] stackTrace = exception.getStackTrace();
            for (int i = 0; i < stackTrace.length; i++) {
                stackTraceBuf.append(stackTrace[i]);
                stackTraceBuf.append("\n");
            }
            /* Append cause exception stack trace */
            if (cause != null) {
                stackTraceBuf.append("\n\n");
                stackTrace = cause.getStackTrace();
                for (int i = 0; i < stackTrace.length; i++) {
                    stackTraceBuf.append(stackTrace[i]);
                    stackTraceBuf.append("\n");
                }
            }
            map.put(stackTraceColName, stackTraceBuf.toString());

            String adaptorName = null == adaptor ? "Unknown" : adaptor.getId();
            map.put(adaptorColName, adaptorName);

            Object data = messageException.getData();
            String dataType = null;
            if (data != null) {
                dataType = data.getClass().getName();
            }
            map.put(dataTypeColName, dataType);
            Element element = null;
            Document dataDoc = null;
            if (data instanceof Document) {
                dataDoc = (Document) data;
                element = dataDoc.getRootElement();
            }
            if (element != null) {
                try {
                    GenericArtifact ga = GenericArtifactHelper
                            .createGenericArtifactJavaObject(dataDoc);

                    String sourceArtifactId = ga.getSourceArtifactId();
                    String sourceSystemId = ga.getSourceSystemId();
                    String sourceSystemKind = ga.getSourceSystemKind();
                    String sourceRepositoryId = ga.getSourceRepositoryId();
                    String sourceRepositoryKind = ga.getSourceRepositoryKind();

                    String targetArtifactId = ga.getTargetArtifactId();
                    String targetSystemId = ga.getTargetSystemId();
                    String targetSystemKind = ga.getTargetSystemKind();
                    String targetRepositoryId = ga.getTargetRepositoryId();
                    String targetRepositoryKind = ga.getTargetRepositoryKind();

                    String artifactErrorCode = ga.getErrorCode();

                    String sourceArtifactLastModifiedDateString = ga
                            .getSourceArtifactLastModifiedDate();
                    String targetArtifactLastModifiedDateString = ga
                            .getTargetArtifactLastModifiedDate();
                    String sourceArtifactVersion = ga
                            .getSourceArtifactVersion();
                    String targetArtifactVersion = ga
                            .getTargetArtifactVersion();

                    String artifactType = XPathUtils.getAttributeValue(element,
                            GenericArtifactHelper.ARTIFACT_TYPE);

                    Date sourceLastModifiedDate = null;
                    if (!sourceArtifactLastModifiedDateString
                            .equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
                        sourceLastModifiedDate = DateUtil
                                .parse(sourceArtifactLastModifiedDateString);
                    } else {
                        // use the earliest date possible
                        sourceLastModifiedDate = new Date(0);
                    }
                    if (sourceLastModifiedDate == null) {
                        sourceLastModifiedDate = new Date(0);
                    }
                    java.sql.Timestamp sourceTime = new Timestamp(
                            sourceLastModifiedDate.getTime());

                    java.util.Date targetLastModifiedDate = null;
                    if (!targetArtifactLastModifiedDateString
                            .equalsIgnoreCase(GenericArtifact.VALUE_UNKNOWN)) {
                        targetLastModifiedDate = DateUtil
                                .parse(targetArtifactLastModifiedDateString);
                    } else {
                        // use the earliest date possible
                        targetLastModifiedDate = new Date(0);
                    }
                    java.sql.Timestamp targetTime = new Timestamp(
                            targetLastModifiedDate.getTime());

                    // TODO Should we allow to set different column names for
                    // these
                    // properties?
                    map.put(SOURCE_SYSTEM_ID, sourceSystemId);
                    map.put(SOURCE_REPOSITORY_ID, sourceRepositoryId);
                    map.put(TARGET_SYSTEM_ID, targetSystemId);
                    map.put(TARGET_REPOSITORY_ID, targetRepositoryId);
                    map.put(SOURCE_SYSTEM_KIND, sourceSystemKind);
                    map.put(SOURCE_REPOSITORY_KIND, sourceRepositoryKind);
                    map.put(TARGET_SYSTEM_KIND, targetSystemKind);
                    map.put(TARGET_REPOSITORY_KIND, targetRepositoryKind);
                    map.put(SOURCE_ARTIFACT_ID, sourceArtifactId);
                    map.put(TARGET_ARTIFACT_ID, targetArtifactId);
                    map.put(ERROR_CODE, artifactErrorCode);
                    map.put(SOURCE_LAST_MODIFICATION_TIME, sourceTime);
                    map.put(TARGET_LAST_MODIFICATION_TIME, targetTime);
                    map.put(SOURCE_ARTIFACT_VERSION, sourceArtifactVersion);
                    map.put(TARGET_ARTIFACT_VERSION, targetArtifactVersion);
                    map.put(ARTIFACT_TYPE, artifactType);
                    //log.info("Removing invalid XML characters if any before we proceed ...");
                    map.put(GENERICARTIFACT,
                            removeInvalidXmlCharacters(dataDoc.asXML()));

                    // these attributes will be considered for CCF 2.x only
                    map.put(DESCRIPTION,
                            "This hospital entry has been inserted by CCF Core.");
                    map.put(REPOSITORY_MAPPING_DIRECTION, sourceSystemKind);
                    map.put(VERSION, 0);

                } catch (GenericArtifactParsingException e) {
                    // log
                    // .warn(
                    // "The data that reached the hospital is not a valid Generic Artifact"
                    // );
                    if (isOnlyQuarantineGenericArtifacts()) {
                        quarantineException = false;
                    }
                    map.put(SOURCE_SYSTEM_ID, null);
                    map.put(SOURCE_REPOSITORY_ID, null);
                    map.put(TARGET_SYSTEM_ID, null);
                    map.put(TARGET_REPOSITORY_ID, null);
                    map.put(SOURCE_SYSTEM_KIND, null);
                    map.put(SOURCE_REPOSITORY_KIND, null);
                    map.put(TARGET_SYSTEM_KIND, null);
                    map.put(TARGET_REPOSITORY_KIND, null);
                    map.put(SOURCE_ARTIFACT_ID, null);
                    map.put(TARGET_ARTIFACT_ID, null);
                    //log.info("Removing invalid XML characters if any before we proceed ...");
                    map.put(GENERICARTIFACT,
                            removeInvalidXmlCharacters(dataDoc.asXML()));
                    map.put(ERROR_CODE, null);
                    map.put(SOURCE_LAST_MODIFICATION_TIME, null);
                    map.put(TARGET_LAST_MODIFICATION_TIME, null);
                    map.put(SOURCE_ARTIFACT_VERSION, null);
                    map.put(TARGET_ARTIFACT_VERSION, null);
                    map.put(ARTIFACT_TYPE, null);
                }
            } else {
                if (isOnlyQuarantineGenericArtifacts()) {
                    quarantineException = false;
                }
                map.put(SOURCE_SYSTEM_ID, null);
                map.put(SOURCE_REPOSITORY_ID, null);
                map.put(TARGET_SYSTEM_ID, null);
                map.put(TARGET_REPOSITORY_ID, null);
                map.put(SOURCE_SYSTEM_KIND, null);
                map.put(SOURCE_REPOSITORY_KIND, null);
                map.put(TARGET_SYSTEM_KIND, null);
                map.put(TARGET_REPOSITORY_KIND, null);
                map.put(SOURCE_ARTIFACT_ID, null);
                map.put(TARGET_ARTIFACT_ID, null);
                map.put(GENERICARTIFACT, null);
                map.put(ERROR_CODE, null);
                map.put(SOURCE_LAST_MODIFICATION_TIME, null);
                map.put(TARGET_LAST_MODIFICATION_TIME, null);
                map.put(SOURCE_ARTIFACT_VERSION, null);
                map.put(TARGET_ARTIFACT_VERSION, null);
                map.put(ARTIFACT_TYPE, null);
            }

            if (quarantineException) {
                // TODO Do we have to care about the fact that the substituted
                // values
                // could potentially contain the place holders again?
                String logMessage = logMessageTemplate;
                for (Object key : map.keys()) {
                    Object value = map.get(key);
                    logMessage = logMessage.replace("<" + key.toString() + ">",
                            value == null ? "undefined" : value.toString());
                }
                log.error(logMessage);
                return map;
            } else {
                StringBuffer errorMessage = new StringBuffer();
                errorMessage
                        .append("Exception caught that is not going to be quarantined. Characteristics:\n");
                for (Object key : map.keys()) {
                    errorMessage.append(key.toString() + ": " + map.get(key)
                            + "\n");
                }
                log.warn(errorMessage.toString());
                return null;
            }
        } catch (Exception e) {
            log.error(
                    "While trying to quarantine an exception, an exception occured",
                    e);
            return null;
        }
    }

    public static final String removeInvalidXmlCharacters(String input) {
        if (input == null) {
            return input;
        }
        char character;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < input.length(); i++) {
            character = input.charAt(i);
            //see http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char for valid XML character list.
            if ((character == 0x9) || (character == 0xA) || (character == 0xD)
                    || ((character >= 0x20) && (character <= 0xD7FF))
                    || ((character >= 0xE000) && (character <= 0xFFFD))
                    || ((character >= 0x10000) && (character <= 0x10FFFF))) {
                sb.append(character);
            }
        }
        return sb.toString();
    }
}
