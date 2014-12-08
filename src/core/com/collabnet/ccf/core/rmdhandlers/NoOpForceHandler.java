package com.collabnet.ccf.core.rmdhandlers;

import java.util.HashSet;
import java.util.Set;

import com.collabnet.ccf.core.db.RMDConfigExtractor;

public class NoOpForceHandler implements ForceHandler {

    public Set<String> getArtifactIdSet(String rmdID) {
        return new HashSet<String>();
    }

    public boolean isForceEnabled(String rmdID) {
        return false;
    }

    public void setRmdConfigExtractor(RMDConfigExtractor rmdConfigExtractor) {
        // Nothing to implement        
    }

    public void updateRMDConfigToOff(String rmdId) {
        // Nothing to implement

    }

}
