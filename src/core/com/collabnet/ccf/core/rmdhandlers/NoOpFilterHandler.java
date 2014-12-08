package com.collabnet.ccf.core.rmdhandlers;

import com.collabnet.ccf.core.db.RMDConfigExtractor;

public class NoOpFilterHandler implements FilterHandler {

    public boolean containsId(String rmdID, String id) {
        return true;
    }

    public boolean ignoreOrdinaryArtifactUpdates(String rmdID) {
        return false;
    }

    public void setRmdConfigExtractor(RMDConfigExtractor rmdConfigExtractor) {
        // Nothing to implement
    }

}
