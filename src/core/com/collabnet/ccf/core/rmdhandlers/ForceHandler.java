package com.collabnet.ccf.core.rmdhandlers;

import java.util.Set;

import com.collabnet.ccf.core.db.RMDConfigExtractor;

public interface ForceHandler {

    public final static String FORCE_KEY = "ccf.repositoryMappingDirection.force";

    public Set<String> getArtifactIdSet(String rmdID);

    public boolean isForceEnabled(String rmdID);

    public void setRmdConfigExtractor(RMDConfigExtractor rmdConfigExtractor);

    public void updateRMDConfigToOff(String rmdID);

}
