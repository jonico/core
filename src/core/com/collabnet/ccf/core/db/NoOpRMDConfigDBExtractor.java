package com.collabnet.ccf.core.db;

public class NoOpRMDConfigDBExtractor implements RMDConfigExtractor {

    @Override
    public String getRMDConfigValue(String rmdID, String key) {
        return null;
    }

    @Override
    public void populateRMDAndRMDConfigValues(String rmdID) {
        //Nothing to implement
    }

}
