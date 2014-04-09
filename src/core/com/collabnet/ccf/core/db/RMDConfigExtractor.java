package com.collabnet.ccf.core.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;

public class RMDConfigExtractor {

    private static final Log                 log                        = LogFactory
                                                                                .getLog(RMDConfigExtractor.class);

    private Map<String, Map<String, String>> rmdAndRMDConfigMap         = new ConcurrentHashMap<String, Map<String, String>>();

    private JDBCReadConnector                rmdConfigJDBCReadConnector = null;

    public Map<String, Map<String, String>> getRmdAndRMDConfigMap() {
        return rmdAndRMDConfigMap;
    }

    public JDBCReadConnector getRmdConfigJDBCReadConnector() {
        return rmdConfigJDBCReadConnector;
    }

    public Map<String, String> getRMDConfigMap(String rmdID) {
        return getConfigValuesOfRMD(rmdID);
    }

    public Map<String, String> getRMDConfigMapFromCache(String rmdID) {
        return getRmdAndRMDConfigMap().get(rmdID);
    }

    public void setRmdAndRMDConfigMap(
            Map<String, Map<String, String>> rmdAndRMDConfigMap) {
        this.rmdAndRMDConfigMap = rmdAndRMDConfigMap;
    }

    public void setRmdConfigJDBCReadConnector(
            JDBCReadConnector rmdConfigJDBCReadConnector) {
        this.rmdConfigJDBCReadConnector = rmdConfigJDBCReadConnector;
    }

    protected Map<String, String> getConfigValuesOfRMD(String rmdID) {
        Map<String, String> associatedRMDConfig = new HashMap<String, String>();
        try {
            IOrderedMap inputParameters = new OrderedHashMap();
            inputParameters.add(rmdID);
            rmdConfigJDBCReadConnector.connect();
            Object[] resultSet = rmdConfigJDBCReadConnector.next(
                    inputParameters, 1000);
            for (int i = 0; i < resultSet.length; i++) {
                IOrderedMap rsentry = (IOrderedMap) resultSet[i];
                String key = (String) rsentry.get(0);
                String value = (String) rsentry.get(1);
                associatedRMDConfig.put(key, value);
            }
            this.getRmdAndRMDConfigMap().put(rmdID, associatedRMDConfig);
        } catch (Exception e) {
            log.error("Could not get RepositoryMappingDirectionConfig KeyValue set "
                    + e.getMessage());
        } finally {
            rmdConfigJDBCReadConnector.disconnect();
        }
        return associatedRMDConfig;

    }
}
