package com.collabnet.ccf.core.rmdhandlers;

public interface FilterHandler {

    public final static String FILTER_KEY    = "ccf.repositoryMappingDirection.filter";

    public final static String HOSPITAL_ONLY = "hospitalonly";

    public final static String REGEX         = "regex";

    public boolean containsId(String rmdID, String id);

    public boolean hospitalOnlyFilterEnabled(String rmdID);

    public void loadRMDAndRMDConfig(String rmdID);

}
