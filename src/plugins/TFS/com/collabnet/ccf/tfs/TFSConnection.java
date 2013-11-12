package com.collabnet.ccf.tfs;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.microsoft.tfs.core.TFSTeamProjectCollection;

public class TFSConnection {

    private String                   username;
    private String                   password;
    private TFSTeamProjectCollection tpc;

    public TFSConnection(String repositoryId, String repositoryKind,
            String connectionInfo, String credentialInfo,
            ConnectionManager<TFSConnection> connectionManager) {
        if (credentialInfo != null) {
            String[] splitCredentials = credentialInfo
                    .split(TFSConnectionFactory.PARAM_DELIMITER);
            if (splitCredentials != null) {
                if (splitCredentials.length == 1) {
                    username = splitCredentials[0];
                    password = "";
                } else if (splitCredentials.length == 2) {
                    username = splitCredentials[0];
                    password = splitCredentials[1];
                } else {
                    throw new IllegalArgumentException(
                            "Credentials info is not valid.");
                }
            }
        }

        String realUserName = TFSMetaData
                .extractUserNameFromFullUserName(username);
        String domain = TFSMetaData.extractDomainFromFullUserName(username);

        setTpc(new TFSTeamProjectCollection(connectionInfo, realUserName,
                domain, password, null, //SnippetSettings.HTTP_PROXY_URL,
                "",//Settings.HTTP_PROXY_USERNAME,
                ""));

    }

    public TFSTeamProjectCollection getTpc() {
        return tpc;
    }

    private void setTpc(TFSTeamProjectCollection tpc) {
        this.tpc = tpc;
    }

}
