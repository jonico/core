package com.collabnet.ccf.core.rmdhandlers;

import java.util.Map;

public abstract class DryModeHandler {

    public final static String DRYRUN_OFF                   = "off";
    public final static String DRYRUN_STOP                  = "stop";
    public final static String DRYRUN_BEFORE_TRANSFORMATION = "beforeTransformation";
    public final static String DRYRUN_AFTER_TRANSFORMATION  = "afterTransformation";
    public final static String DRYRUN_KEY                   = "ccf.repositoryMappingDirection.dryRun";

    public abstract String getDryRunModeValueFromCache(String rmdId);

    public abstract String getDryRunValue(Map<String, String> rmdConfigMap);

    public static boolean isDryRunEqualsAfterTransformation(
            String dryRunModeValue) {
        return DRYRUN_AFTER_TRANSFORMATION.equals(dryRunModeValue);
    }

    public static boolean isDryRunEqualsBeforeTransformation(
            String dryRunModeValue) {
        return DRYRUN_BEFORE_TRANSFORMATION.equals(dryRunModeValue);
    }

    public static boolean isDryRunEqualsStop(String dryRunModeValue) {
        return DRYRUN_STOP.equals(dryRunModeValue);
    }

    public static boolean isDryRunMode(String dryRunModeValue) {
        return DRYRUN_AFTER_TRANSFORMATION.equals(dryRunModeValue)
                || DRYRUN_BEFORE_TRANSFORMATION.equals(dryRunModeValue);
    }
}
