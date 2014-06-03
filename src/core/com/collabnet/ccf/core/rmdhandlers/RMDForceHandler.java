package com.collabnet.ccf.core.rmdhandlers;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openadaptor.auxil.connector.jdbc.writer.JDBCWriteConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;

public class RMDForceHandler extends AbstractRMDHandler implements ForceHandler {

    public static final String REPOSITORY_MAPPING_DIRECTION_CONFIG_VAL      = "repositoryMappingDirectionConfig.VAL";
    public static final String REPOSITORY_MAPPING_DIRECTION                 = "REPOSITORY_MAPPING_DIRECTION";
    public static final String REPOSITORY_MAPPING_DIRECTION_CONFIG_NAME     = "NAME";
    public static final String REPOSITORY_MAPPING_DIRECTION_CONFIG_OLD_VAL  = "VAL";
    public final static String OFF                                          = "off";

    private JDBCWriteConnector repositoryMappingDirectionConfigTableUpdater = null;

    public Set<String> getArtifactIdSet(String rmdID) {
        Set<String> ids = new HashSet<String>();
        String query = getFilterQuery(rmdID);
        String type = getFilterType(rmdID);
        if (valuesFilterEnabled(type)) {
            ids = populateIds(query);
        } else if (rangesFilterEnabled(type, query)) {
            String[] ranges = StringUtils.split(query, getRangeSeperator());
            //TODO: need to validate ranges value whether it exists or not
            if (isAlphaNumeric(ranges)) {
                ids = populateIdsForAlphaNumericRange(ranges);
            } else if (isNumeric(ranges)) {
                ids = populateIdsForNumericRange(ranges);
            }
        }
        return ids;
    }

    public JDBCWriteConnector getRepositoryMappingDirectionConfigTableUpdater() {
        return repositoryMappingDirectionConfigTableUpdater;
    }

    public String getValForRMDConfig(String rmdID) {
        String val = EMPTY_STRING;
        if (rmdID != null && isForceEnabled(rmdID)) {
            val = this.getRmdConfigExtractor().getRMDConfigValue(rmdID,
                    FORCE_KEY);
        }
        return val;
    }

    public boolean isForceEnabled(String rmdID) {
        return this.getRmdConfigExtractor() != null
                && this.getRmdConfigExtractor().getRMDConfigValue(rmdID,
                        FORCE_KEY) != null;
    }

    public void setRepositoryMappingDirectionConfigTableUpdater(
            JDBCWriteConnector repositoryMappingDirectionConfigTableUpdater) {
        this.repositoryMappingDirectionConfigTableUpdater = repositoryMappingDirectionConfigTableUpdater;
    }

    public void updateRMDConfigToOff(String rmdId) {
        String oldVal = this.getValForRMDConfig(rmdId);
        if (!OFF.equals(oldVal))
            updateRepositoryMappingDirectionConfig(OFF, rmdId, FORCE_KEY,
                    oldVal);

    }

    private void updateRepositoryMappingDirectionConfig(String newVal,
            String rmdId, String name, String oldVal) {
        if (repositoryMappingDirectionConfigTableUpdater != null) {
            IOrderedMap inputParameters = new OrderedHashMap();

            inputParameters.add(0, REPOSITORY_MAPPING_DIRECTION_CONFIG_VAL,
                    newVal);
            inputParameters.add(1, REPOSITORY_MAPPING_DIRECTION, rmdId);
            inputParameters.add(2, REPOSITORY_MAPPING_DIRECTION_CONFIG_NAME,
                    name);
            inputParameters.add(3, REPOSITORY_MAPPING_DIRECTION_CONFIG_OLD_VAL,
                    oldVal);

            IOrderedMap[] params = new IOrderedMap[] { inputParameters };
            repositoryMappingDirectionConfigTableUpdater.connect();
            repositoryMappingDirectionConfigTableUpdater.deliver(params);
        }
    }

}
