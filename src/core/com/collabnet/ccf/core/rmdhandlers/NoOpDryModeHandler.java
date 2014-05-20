package com.collabnet.ccf.core.rmdhandlers;

public class NoOpDryModeHandler extends DryModeHandler {

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

    public void loadRMDAndRMDConfig(String rmdID) {
        // Nothing to implement

    }

    public void updateRMDConfigToOff(String rmdId) {
        // Nothing to implement

    }

}
