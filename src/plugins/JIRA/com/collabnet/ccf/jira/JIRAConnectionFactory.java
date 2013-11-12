package com.collabnet.ccf.jira;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class JIRAConnectionFactory implements ConnectionFactory<JIRAConnection> {

    public static final String PARAM_DELIMITER = ":";

    /**
     * Closes JIRA connection
     */
    public void closeConnection(JIRAConnection connection)
            throws ConnectionException {
        //FIXME find appropriate implementation for JIRA closeConnection()
    }

    /**
     * Create an JIRA connection object
     */
    public JIRAConnection createConnection(String systemId, String systemKind,
            String repositoryId, String repositoryKind, String connectionInfo,
            String credentialInfo,
            ConnectionManager<JIRAConnection> connectionManager)
            throws ConnectionException {
        try {

            return new JIRAConnection(repositoryId, repositoryKind,
                    connectionInfo, credentialInfo, connectionManager);
        } catch (Exception e) {
            throw new ConnectionException("Could not connect to JIRA", e);
        }
    }

    /**
     * Call method to check whether JIRA connection still works
     */
    public boolean isAlive(JIRAConnection connection) {
        try {
            //FIXME find appropriate implementation for JIRA ISAlive()
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
