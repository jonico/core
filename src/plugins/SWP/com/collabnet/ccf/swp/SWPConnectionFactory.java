package com.collabnet.ccf.swp;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class SWPConnectionFactory implements ConnectionFactory<Connection> {

    public static final String PARAM_DELIMITER = ":";

    /**
     * Closes SWP connection
     */
    public void closeConnection(Connection connection)
            throws ConnectionException {
        // since there are no SWP sessions for WS calls, we do not have to do
        // anything here
    }

    /**
     * Create an SWP connection object
     */
    public Connection createConnection(String systemId, String systemKind,
            String repositoryId, String repositoryKind, String connectionInfo,
            String credentialInfo,
            ConnectionManager<Connection> connectionManager)
            throws ConnectionException {
        try {
            return new Connection(repositoryId, repositoryKind, connectionInfo,
                    credentialInfo, connectionManager);
        } catch (Exception e) {
            throw new ConnectionException("Could not connect to SWP", e);
        }
    }

    /**
     * Call method to check whether SWP connection still works
     */
    public boolean isAlive(Connection connection) {
        // we return true because ScrumWorks does not have any sessions
        return true;
    }

}
