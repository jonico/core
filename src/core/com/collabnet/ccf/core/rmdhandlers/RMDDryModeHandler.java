package com.collabnet.ccf.core.rmdhandlers;

import org.openadaptor.auxil.connector.jdbc.writer.JDBCWriteConnector;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;

import com.collabnet.ccf.core.db.RMDConfigExtractor;

public class RMDDryModeHandler extends DryModeHandler {

    private RMDConfigExtractor rmdConfigExtractor                           = null;

    private JDBCWriteConnector repositoryMappingDirectionConfigTableUpdater = null;

    public JDBCWriteConnector getRepositoryMappingDirectionConfigTableUpdater() {
        return repositoryMappingDirectionConfigTableUpdater;
    }

    public RMDConfigExtractor getRmdConfigExtractor() {
        return rmdConfigExtractor;
    }

    public boolean isDryRunEqualsAfterTransformation(String rmdId) {
        return DRYRUN_AFTER_TRANSFORMATION.equals(getDryRunValue(rmdId));
    }

    public boolean isDryRunEqualsBeforeTransformation(String rmdId) {
        return DRYRUN_BEFORE_TRANSFORMATION.equals(getDryRunValue(rmdId));
    }

    public boolean isDryRunEqualsStop(String rmdId) {
        return DRYRUN_STOP.equals(getDryRunValue(rmdId));
    }

    public boolean isDryRunMode(String rmdId) {
        String dryRunVal = getDryRunValue(rmdId);
        return DRYRUN_AFTER_TRANSFORMATION.equals(dryRunVal)
                || DRYRUN_BEFORE_TRANSFORMATION.equals(dryRunVal);
    }

    public void loadRMDAndRMDConfig(String rmdID) {
        if (this.getRmdConfigExtractor() != null) {
            this.getRmdConfigExtractor().populateRMDAndRMDConfigValues(rmdID);
        }
    }

    public void setRepositoryMappingDirectionConfigTableUpdater(
            JDBCWriteConnector repositoryMappingDirectionConfigTableUpdater) {
        this.repositoryMappingDirectionConfigTableUpdater = repositoryMappingDirectionConfigTableUpdater;
    }

    public void setRmdConfigExtractor(RMDConfigExtractor rmdConfigExtractor) {
        this.rmdConfigExtractor = rmdConfigExtractor;
    }

    public void updateRMDConfigToOff(String rmdId) {
        String oldDryMode = this.getDryRunValue(rmdId);
        updateRepositoryMappingDirectionConfig(DryModeHandler.DRYRUN_OFF,
                rmdId, DRYRUN_KEY, oldDryMode);
    }

    private String getDryRunValue(String rmdId) {
        String val = null;
        if (this.getRmdConfigExtractor() != null) {
            val = rmdConfigExtractor.getRMDConfigValue(rmdId, DRYRUN_KEY);
        }
        return val;
    }

    private void updateRepositoryMappingDirectionConfig(String value,
            String rmdId, String name, String oldValue) {
        if (repositoryMappingDirectionConfigTableUpdater != null) {
            IOrderedMap inputParameters = new OrderedHashMap();

            inputParameters.add(0, REPOSITORY_MAPPING_DIRECTION_CONFIG_VAL,
                    value);
            inputParameters.add(1, REPOSITORY_MAPPING_DIRECTION, rmdId);
            inputParameters.add(2, REPOSITORY_MAPPING_DIRECTION_CONFIG_NAME,
                    name);
            inputParameters.add(3, REPOSITORY_MAPPING_DIRECTION_CONFIG_OLD_VAL,
                    oldValue);

            IOrderedMap[] params = new IOrderedMap[] { inputParameters };
            repositoryMappingDirectionConfigTableUpdater.connect();
            repositoryMappingDirectionConfigTableUpdater.deliver(params);
        }
    }
}
