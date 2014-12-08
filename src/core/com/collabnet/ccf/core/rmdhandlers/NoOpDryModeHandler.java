package com.collabnet.ccf.core.rmdhandlers;

import com.collabnet.ccf.core.db.RMDConfigExtractor;

public class NoOpDryModeHandler implements DryModeHandler {

    public boolean isDryRunEqualsAfterTransformation(String rmdId) {
        return false;
    }

    public boolean isDryRunEqualsBeforeTransformation(String rmdId) {
        return false;
    }

    public boolean isDryRunEqualsStop(String rmdId) {
        return false;
    }

    public boolean isDryRunMode(String rmdId) {
        return false;
    }

    public void setRmdConfigExtractor(RMDConfigExtractor rmdConfigExtractor) {
        // Nothing to implement

    }

    public void updateRMDConfigToOff(String rmdId) {
        // Nothing to implement

    }

}
