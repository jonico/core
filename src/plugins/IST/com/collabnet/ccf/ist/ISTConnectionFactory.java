package com.collabnet.ccf.ist;

import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;

public class ISTConnectionFactory implements ConnectionFactory<ISTConnectionFactory> {

    @Override
    public void closeConnection(ISTConnectionFactory connection)
            throws ConnectionException {
        // TODO Auto-generated method stub

    }

    @Override
    public ISTConnectionFactory createConnection(String systemId,
            String systemKind, String repositoryId, String repositoryKind,
            String connectionInfo, String credentialInfo,
            ConnectionManager<ISTConnectionFactory> connectionManager)
                    throws ConnectionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAlive(ISTConnectionFactory connection) {
        // TODO Auto-generated method stub
        return false;
    }

}
