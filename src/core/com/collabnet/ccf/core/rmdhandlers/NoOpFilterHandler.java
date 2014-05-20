package com.collabnet.ccf.core.rmdhandlers;

public class NoOpFilterHandler implements FilterHandler {

    public boolean containsId(String rmdID, String id) {
        return true;
    }

    public boolean hospitalOnlyFilterEnabled(String rmdID) {
        return false;
    }

    public void loadRMDAndRMDConfig(String rmdID) {
        // Nothing to implement
    }

}
