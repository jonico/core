package com.collabnet.ccf.ist;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class ISTConnectionFactory implements ConnectionFactory<ISTConnection> {

    public static final String PARAM_DELIMITER           = "::";
    private static final Log   log                       = LogFactory
            .getLog(ISTConnectionFactory.class);
    public static final String CONNECTION_INFO_DELIMITER = "::";

    @Override
    public void closeConnection(ISTConnection connection) {

        connection.close();

        connection = null;

    }

    @Override
    public ISTConnection createConnection(String systemId, String systemKind,
            String repositoryId, String repositoryKind, String connectionInfo,
            String credentialInfo,
            ConnectionManager<ISTConnection> connectionManager)
                    throws ConnectionException {

        log.debug("creating connection for: " + systemId + "-" + systemKind
                + "-" + repositoryId + "-" + repositoryKind + "-"
                + connectionInfo + "-" + credentialInfo);

        return new ISTConnection(credentialInfo, connectionInfo);
    }

    @Override
    public boolean isAlive(ISTConnection connection) {
        return connection.isAlive();
    }

}
