package com.collabnet.ccf.core.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;

public class RMDConfigDBExtractor implements RMDConfigExtractor {

    private static final Log                 log                        = LogFactory
                                                                                .getLog(RMDConfigDBExtractor.class);

    private Map<String, Map<String, String>> rmdAndRMDConfigMap         = new ConcurrentHashMap<String, Map<String, String>>();

    private JDBCReadConnector                rmdConfigJDBCReadConnector = null;

    public Map<String, Map<String, String>> getRmdAndRMDConfigMap() {
        return rmdAndRMDConfigMap;
    }

    public JDBCReadConnector getRmdConfigJDBCReadConnector() {
        return rmdConfigJDBCReadConnector;
    }

    public Map<String, String> getRMDConfigMapFromCache(String rmdID) {
        return getRmdAndRMDConfigMap().get(rmdID);
    }

    public String getRMDConfigValue(String rmdID, String key) {
        Map<String, String> rmdConfigCache = getRMDConfigMapFromCache(rmdID);
        if (rmdConfigCache != null) {
            return getRMDConfigMapFromCache(rmdID).get(key);
        }
        return null;
    }

    public void populateRMDAndRMDConfigValues(String rmdID) {
        Map<String, String> associatedRMDConfig = new HashMap<String, String>();
        if (rmdConfigJDBCReadConnector != null) {
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
        }
    }

    public void setRmdAndRMDConfigMap(
            Map<String, Map<String, String>> rmdAndRMDConfigMap) {
        this.rmdAndRMDConfigMap = rmdAndRMDConfigMap;
    }

    public void setRmdConfigJDBCReadConnector(
            JDBCReadConnector rmdConfigJDBCReadConnector) {
        this.rmdConfigJDBCReadConnector = rmdConfigJDBCReadConnector;
    }
}
