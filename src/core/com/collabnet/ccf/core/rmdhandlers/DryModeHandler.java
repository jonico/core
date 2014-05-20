package com.collabnet.ccf.core.rmdhandlers;


public abstract class DryModeHandler {

    public final static String DRYRUN_OFF                                  = "off";
    public final static String DRYRUN_STOP                                 = "stop";
    public final static String DRYRUN_BEFORE_TRANSFORMATION                = "beforeTransformation";
    public final static String DRYRUN_AFTER_TRANSFORMATION                 = "afterTransformation";
    public final static String DRYRUN_KEY                                  = "ccf.repositoryMappingDirection.dryRun";
    public static final String REPOSITORY_MAPPING_DIRECTION_CONFIG_VAL     = "repositoryMappingDirectionConfig.VAL";
    public static final String REPOSITORY_MAPPING_DIRECTION                = "REPOSITORY_MAPPING_DIRECTION";
    public static final String REPOSITORY_MAPPING_DIRECTION_CONFIG_NAME    = "NAME";
    public static final String REPOSITORY_MAPPING_DIRECTION_CONFIG_OLD_VAL = "VAL";

    public abstract boolean isDryRunEqualsAfterTransformation(String rmdId);

    public abstract boolean isDryRunEqualsBeforeTransformation(String rmdId);

    public abstract boolean isDryRunEqualsStop(String rmdId);

    public abstract boolean isDryRunMode(String rmdId);

    public abstract void loadRMDAndRMDConfig(String rmdID);

    public abstract void updateRMDConfigToOff(String rmdId);
}
