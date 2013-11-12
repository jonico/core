package com.collabnet.ccf.jira;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.microsoft.tfs.core.clients.workitem.fields.FieldType;

public class JIRAMetaData {

    public enum JIRAType {
        ISSUE, UNKNOWN
    }

    public static final String            JIRA_REPOSITORY_DELIMITER = "-";

    private static final Log              log                       = LogFactory
                                                                            .getLog(JIRAHandler.class);

    private static final SimpleDateFormat isoLocal                  = new SimpleDateFormat(
                                                                            "yyyy-MM-dd'T'HH:mm:ss.SS");

    public static String extractIssueTypeFromRepositoryId(String repositoryId) {
        return repositoryId.split(JIRA_REPOSITORY_DELIMITER)[2];

    }

    public static String extractProjectKeyFromRepositoryId(String repositoryId) {
        return repositoryId.split(JIRA_REPOSITORY_DELIMITER)[1];
    }

    public static String extractUnusedFirstPartFromRepositoryId(
            String repositoryId) {
        return repositoryId.split(JIRA_REPOSITORY_DELIMITER)[0];

    }

    public static String formatDate(Date date) {
        String formattedDate = null;
        if (date != null) {
            String dateString = isoLocal.format(date);
            formattedDate = dateString.substring(0, dateString.length() - 1);
        }
        return formattedDate;
    }

    public static JIRAType retrieveJIRATypeFromRepositoryId(String repositoryId) {

        if (repositoryId == null) {
            return JIRAType.UNKNOWN;
        }

        int delimiterCounter = repositoryId.split(JIRA_REPOSITORY_DELIMITER).length;

        if (delimiterCounter != 3) {
            return JIRAType.UNKNOWN;
        } else {
            return JIRAType.ISSUE;
        }

    }

    public static GenericArtifactField.FieldValueTypeValue translateTFSFieldValueTypeToCCFFieldValueType(
            FieldType tfsFieldValueType) {

        if (FieldType.BOOLEAN.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.BOOLEAN;
        }

        if (FieldType.DATETIME.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.DATETIME;
        }

        if (FieldType.DOUBLE.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.DOUBLE;
        }

        if (FieldType.HTML.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.HTMLSTRING;
        }

        if (FieldType.INTEGER.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.INTEGER;
        }

        if (FieldType.STRING.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.STRING;
        }

        if (FieldType.GUID.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.STRING;
        }

        if (FieldType.HISTORY.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.STRING;
        }

        if (FieldType.PLAINTEXT.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.STRING;
        }

        if (FieldType.TREEPATH.equals(tfsFieldValueType)) {
            return FieldValueTypeValue.STRING;
        }

        log.warn("WorkenItem Field Type was not identified, returning String value");
        return FieldValueTypeValue.STRING;
    }

}
