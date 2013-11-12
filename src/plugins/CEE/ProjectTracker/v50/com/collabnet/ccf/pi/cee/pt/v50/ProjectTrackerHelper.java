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

package com.collabnet.ccf.pi.cee.pt.v50;

import java.util.List;

import com.collabnet.tracker.common.ClientArtifactListXMLHelper;
import com.collabnet.tracker.common.ClientXMLOperationError;

public class ProjectTrackerHelper {
    private static ProjectTrackerHelper instance = null;

    private ProjectTrackerHelper() {

    }

    public String getArtifactIdFromFullyQualifiedArtifactId(
            String fullyQualifiedArtifactId) {
        String artifactIdentifier = fullyQualifiedArtifactId
                .substring(fullyQualifiedArtifactId.lastIndexOf(":") + 1);
        return artifactIdentifier;
    }

    public String getArtifactTypeNamespaceFromFullyQualifiedArtifactId(
            String fullyQualifiedArtifactId) {
        String artifactTypeNamespace = this
                .getArtifactTypeNamespaceFromFullyQualifiedArtifactType(fullyQualifiedArtifactId);
        return artifactTypeNamespace;
    }

    public String getArtifactTypeNamespaceFromFullyQualifiedArtifactType(
            String fullyQualifiedArtifactType) {
        int indexOfClosingCurlyBrace = fullyQualifiedArtifactType
                .lastIndexOf("}");
        if (indexOfClosingCurlyBrace != -1) {
            String artifactTypeNamespace = fullyQualifiedArtifactType
                    .substring(1, indexOfClosingCurlyBrace);
            return artifactTypeNamespace;
        } else {
            return null;
        }
    }

    public String getArtifactTypeNamespaceFromRepositoryId(String repositoryId) {
        String artifactTypeNamespace = repositoryId.substring(
                repositoryId.indexOf('{') + 1, repositoryId.indexOf('}'));
        return artifactTypeNamespace;
    }

    public String getArtifactTypeTagNameFromFullyQualifiedArtifactId(
            String fullyQualifiedArtifactId) {
        int lastIndexOfCurlyBrace = fullyQualifiedArtifactId.lastIndexOf("}");
        int lastIndexOfColon = fullyQualifiedArtifactId.lastIndexOf(":");
        if (lastIndexOfCurlyBrace != -1 && lastIndexOfColon != -1) {
            String artifactTypeTagName = fullyQualifiedArtifactId.substring(
                    lastIndexOfCurlyBrace + 1, lastIndexOfColon);
            return artifactTypeTagName;
        } else {
            return null;
        }
    }

    public String getArtifactTypeTagNameFromFullyQualifiedArtifactType(
            String fullyQualifiedArtifactType) {
        String artifactTypeTagName = fullyQualifiedArtifactType
                .substring(fullyQualifiedArtifactType.lastIndexOf("}") + 1);
        return artifactTypeTagName;
    }

    public String getArtifactTypeTagNameFromRepositoryId(String repositoryId) {
        String artifactTypeNamespace = repositoryId.substring(repositoryId
                .indexOf('}') + 1);
        return artifactTypeNamespace;
    }

    public String getEntityName(String input) {
        int start = input.indexOf("}");
        String entityName = null;
        if (start < 0) {
            entityName = input;
        } else if (start >= 0) {
            entityName = input.substring(start + 1);
        }
        return entityName;
    }

    public String getNamespace(String input) {
        int start = input.indexOf("{");
        int end = input.indexOf("}");
        String namespace = null;
        if (start >= 0 && end >= 2) {
            namespace = input.substring(start + 1, end);
        }
        return namespace;
    }

    public void processWSErrors(ClientArtifactListXMLHelper soapResponse) {
        List<ClientXMLOperationError> errors = soapResponse.getErrors();
        String cause = null;
        if (errors.size() > 0) {
            cause = "";
            for (ClientXMLOperationError error : errors) {
                String message = error.getMsg();
                String code = error.getCode();
                String trace = error.getTrace();
                cause = "Message: " + message + " Code: " + code
                        + System.getProperty("line.sparator");
                cause += trace + System.getProperty("line.sparator")
                        + System.getProperty("line.sparator");
            }
            throw new ProjectTrackerWebServiceException(cause);
        }
    }

    public static ProjectTrackerHelper getInstance() {
        synchronized (ProjectTrackerHelper.class) {
            if (instance == null) {
                instance = new ProjectTrackerHelper();
            }
        }
        return instance;
    }

    public static String getNamespaceWithBraces(String input) {
        int start = input.indexOf("{");
        int end = input.indexOf("}");
        String namespace = null;
        if (start >= 0 && end >= 2) {
            namespace = input.substring(start, end + 1);
        } else {
            namespace = "";
        }
        return namespace;
    }
}
