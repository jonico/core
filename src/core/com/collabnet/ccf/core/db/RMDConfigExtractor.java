package com.collabnet.ccf.core.db;

public interface RMDConfigExtractor {

    public String getRMDConfigValue(String rmdID, String key);

    public void populateRMDAndRMDConfigValues(String rmdID);

}
