package com.collabnet.ccf.core.rmdhandlers;

import java.util.HashSet;
import java.util.Set;

public class NoOpForceHandler implements ForceHandler {

    public Set<String> getArtifactIdSet(String rmdID) {
        return new HashSet<String>();
    }

    public boolean isForceEnabled(String rmdID) {
        return false;
    }

    public void loadRMDAndRMDConfig(String rmdID) {
        // Nothing to implement

    }

    public void updateRMDConfigToOff(String rmdId) {
        // Nothing to implement

    }

}
