package com.collabnet.ccf.core.rmdhandlers;

import java.util.Map;

public class NoOpDryModeHandler extends DryModeHandler {

    @Override
    public String getDryRunModeValueFromCache(String rmdId) {
        return null;
    }

    @Override
    public String getDryRunValue(Map<String, String> rmdConfigMap) {
        return null;
    }

}
