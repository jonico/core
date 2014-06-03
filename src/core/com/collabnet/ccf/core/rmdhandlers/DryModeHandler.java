package com.collabnet.ccf.core.rmdhandlers;

public interface DryModeHandler {

    public final static String DRYRUN_OFF                                  = "off";
    public final static String DRYRUN_STOP                                 = "stop";
    public final static String DRYRUN_BEFORE_TRANSFORMATION                = "beforeTransformation";
    public final static String DRYRUN_AFTER_TRANSFORMATION                 = "afterTransformation";
    public final static String DRYRUN_KEY                                  = "ccf.repositoryMappingDirection.dryRun";
    public static final String REPOSITORY_MAPPING_DIRECTION_CONFIG_VAL     = "repositoryMappingDirectionConfig.VAL";
    public static final String REPOSITORY_MAPPING_DIRECTION                = "REPOSITORY_MAPPING_DIRECTION";
    public static final String REPOSITORY_MAPPING_DIRECTION_CONFIG_NAME    = "NAME";
    public static final String REPOSITORY_MAPPING_DIRECTION_CONFIG_OLD_VAL = "VAL";

    public boolean isDryRunEqualsAfterTransformation(String rmdId);

    public boolean isDryRunEqualsBeforeTransformation(String rmdId);

    public boolean isDryRunEqualsStop(String rmdId);

    public boolean isDryRunMode(String rmdId);

    public void loadRMDAndRMDConfig(String rmdID);

    public void updateRMDConfigToOff(String rmdId);
}
