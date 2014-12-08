package com.collabnet.ccf.core.rmdhandlers;

import com.collabnet.ccf.core.db.RMDConfigExtractor;

public interface FilterHandler {

    public final static String FILTER_KEY                       = "ccf.repositoryMappingDirection.filter";

    public final static String IGNORE_ORDINARY_ARTIFACT_UPDATES = "ignoreOrdinaryArtifactUpdates";

    public final static String REGEX                            = "regex";

    public boolean containsId(String rmdID, String id);

    public boolean ignoreOrdinaryArtifactUpdates(String rmdID);

    public void setRmdConfigExtractor(RMDConfigExtractor rmdConfigExtractor);

}
