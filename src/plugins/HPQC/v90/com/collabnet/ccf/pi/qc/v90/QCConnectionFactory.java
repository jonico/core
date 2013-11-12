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

package com.collabnet.ccf.pi.qc.v90;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;

/**
 * @author madhusuthanan
 * 
 */
public class QCConnectionFactory implements ConnectionFactory<IConnection> {
    public static final String PARAM_DELIMITER                              = "-";
    private static final Log   log                                          = LogFactory
                                                                                    .getLog(QCConnectionFactory.class);
    /**
     * This data structure maps the repository id to the corresponding
     * requirements type's technical id
     */
    static Map<String, String> repositoryIdToTechnicalRequirementsTypeIdMap = new HashMap<String, String>();

    public void closeConnection(IConnection connection) {
        try {
            connection.logout();
            connection.disconnect();
        } catch (Exception e) {
            log.warn(
                    "Could not close QC connection. So releasing the Connection COM dispatch",
                    e);
        }
        connection.safeRelease();
        connection = null;
    }

    /**
     * Connection Factory implementation for the QC adaptor. 1. connectionInfo
     * contains the server URL. 2. credentialInfo contains the user name and
     * password delimited by '-'
     * 
     * The Repository ID contains the QC domain name and QC project name
     * delimited by a '-'
     * 
     * @throws ConnectionException
     */
    public IConnection createConnection(String systemId, String systemKind,
            String repositoryId, String repositoryKind, String connectionInfo,
            String credentialInfo,
            ConnectionManager<IConnection> connectionManager)
            throws ConnectionException {
        String domain = null;
        String project = null;
        if (repositoryId != null) {
            String[] splitRepoId = repositoryId.split(PARAM_DELIMITER);
            if (splitRepoId != null) {
                // we now also accept a double hyphen to synchronize requirement types as well
                if (splitRepoId.length == 2 || splitRepoId.length == 3) {
                    domain = splitRepoId[0];
                    project = splitRepoId[1];
                } else {
                    throw new IllegalArgumentException("Repository Id "
                            + repositoryId + " is invalid.");
                }
            }
        } else {
            throw new IllegalArgumentException("Repository Id cannot be null");
        }

        String username = null;
        String password = null;
        if (credentialInfo != null) {
            String[] splitCredentials = credentialInfo.split(PARAM_DELIMITER);
            if (splitCredentials != null) {
                if (splitCredentials.length == 1) {
                    username = splitCredentials[0];
                    password = "";
                } else if (splitCredentials.length == 2) {
                    username = splitCredentials[0];
                    // this will also work if password ends with hyphen(s)
                    password = credentialInfo.substring(credentialInfo
                            .indexOf(PARAM_DELIMITER) + 1);
                } else if (splitCredentials.length > 2) {
                    username = splitCredentials[0];
                    password = credentialInfo.substring(credentialInfo
                            .indexOf(PARAM_DELIMITER) + 1);
                } else {
                    throw new IllegalArgumentException(
                            "Credentials info is not valid.");
                }
            }
        }

        IConnection connection = com.collabnet.ccf.pi.qc.v90.api.ConnectionFactory
                .getInstance(connectionInfo, domain, project, username,
                        password);
        return connection;
    }

    public boolean isAlive(IConnection connection) {
        return connection.isLoggedIn();
    }

    /**
     * Extracts the informal requirements type from the repository id (does some
     * parsing)
     * 
     * @param repositoryId
     *            repository id
     * @return informal requirements type
     */
    public static String extractInformalRequirementsType(String repositoryId) {
        if (repositoryId != null) {
            // we have to extract the requirements type now
            String[] splitRepoId = repositoryId.split(PARAM_DELIMITER);
            if (splitRepoId != null) {
                // we now also accept a double hyphen to synchronize requirement types as well
                if (splitRepoId.length == 3) {
                    return splitRepoId[2];
                } else {
                    throw new IllegalArgumentException("Repository Id "
                            + repositoryId + " is invalid.");
                }
            } else {
                throw new IllegalArgumentException("Repository Id "
                        + repositoryId + " is invalid.");
            }
        } else {
            throw new IllegalArgumentException("Repository Id " + repositoryId
                    + " is invalid.");
        }
    }

    /**
     * Extracts the technical requirements type from the repository id (does a
     * lookup and retrieve technical id for it)
     * 
     * @param repositoryId
     *            repository id
     * @param qcc
     *            HP QC connection
     * @return technical requirements type
     */
    public static String extractTechnicalRequirementsType(String repositoryId,
            IConnection qcc) {
        // first lookup the map
        String requirementsType = repositoryIdToTechnicalRequirementsTypeIdMap
                .get(repositoryId);
        if (requirementsType == null) {
            // we have to extract the requirements type now
            String[] splitRepoId = repositoryId.split(PARAM_DELIMITER);
            if (splitRepoId != null) {
                // we now also accept a double hyphen to synchronize requirement types as well
                if (splitRepoId.length == 3) {
                    requirementsType = splitRepoId[2];
                    if (QCHandler.REQUIREMENT_TYPE_ALL.equals(requirementsType)) {
                        //if requirementsType is "ALL" we cache it and return it
                        repositoryIdToTechnicalRequirementsTypeIdMap.put(
                                repositoryId, requirementsType);
                        return requirementsType;
                    }
                    // now we have to retrieve the technical id for the requirements type
                    String technicalId = QCHandler
                            .getRequirementTypeTechnicalId(qcc,
                                    requirementsType);
                    repositoryIdToTechnicalRequirementsTypeIdMap.put(
                            repositoryId, technicalId);
                    return technicalId;
                } else {
                    throw new IllegalArgumentException("Repository Id "
                            + repositoryId + " is invalid.");
                }
            } else {
                throw new IllegalArgumentException("Repository Id "
                        + repositoryId + " is invalid.");
            }
        } else {
            return requirementsType;
        }
    }

    /**
     * Generates the repository name of a dependent repository
     * 
     * @param repositoryId
     *            repository id of the original repository
     * @param dependentRequirementsType
     *            informal name of the dependent requirements type
     * @return
     */
    public static String generateDependentRepositoryId(String repositoryId,
            String dependentRequirementsType) {
        String[] splitRepoId = repositoryId.split(PARAM_DELIMITER);
        if (splitRepoId != null) {
            if (splitRepoId.length == 3) {
                String domain = splitRepoId[0];
                String project = splitRepoId[1];
                return domain + PARAM_DELIMITER + project + PARAM_DELIMITER
                        + dependentRequirementsType;
            } else {
                throw new IllegalArgumentException("Repository Id "
                        + repositoryId + " is invalid.");
            }
        }
        throw new IllegalArgumentException("Repository Id " + repositoryId
                + " is invalid.");
    }

    /**
     * Returns whether this repository id belongs to a defect repository If not,
     * it belongs to a requirements type
     * 
     * @param repositoryId
     *            repositoryId
     * @return true if repository id belongs to a defect repository
     */
    public static boolean isDefectRepository(String repositoryId) {
        String[] splitRepoId = repositoryId.split(PARAM_DELIMITER);
        if (splitRepoId != null) {
            // we now also accept a double hyphen to synchronize requirement types as well
            if (splitRepoId.length == 2) {
                return true;
            } else if (splitRepoId.length == 3) {
                return false;
            } else {
                throw new IllegalArgumentException("Repository Id "
                        + repositoryId + " is invalid.");
            }
        }
        throw new IllegalArgumentException("Repository Id " + repositoryId
                + " is invalid.");
    }

}
