package com.collabnet.ccf.core.rmdhandlers;

public interface FilterHandler {

    public final static String FILTER_KEY          = "ccf.repositoryMappingDirection.filter";

    public final static String HOSPITAL_ONLY       = "hospitalonly";

    public final static String REGEX               = "regex";

    public final static String VALUES              = "values";

    public final static String RANGES              = "ranges";

    public final static String EMPTY_STRING        = "";

    public final static String ALPHA_NUMERIC_REGEX = "^([a-zA-Z]+?.)([0-9]+?)$";             // matches a1,A1,artf1234,jira-1234

    public final static String NUMERIC_REGEX       = "^([0-9]+)$";

    public final static String COMMAS              = ",";

    public final static String RANGE_SEPARATOR     = "-";

    public boolean containsId(String rmdID, String id);

    public boolean hospitalOnlyFilterEnabled(String rmdID);

    public void loadRMDAndRMDConfig(String rmdID);

}
