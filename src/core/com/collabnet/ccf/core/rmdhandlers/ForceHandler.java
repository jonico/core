package com.collabnet.ccf.core.rmdhandlers;

import java.util.Set;

public interface ForceHandler {

    public final static String FORCE_KEY = "ccf.repositoryMappingDirection.force";

    public Set<String> getArtifactIdSet(String rmdID);

    public boolean isForceEnabled(String rmdID);

    public void loadRMDAndRMDConfig(String rmdID);

    public void updateRMDConfigToOff(String rmdID);

}
