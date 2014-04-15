package com.collabnet.ccf.core.rmdhandlers;

import java.util.Map;

import com.collabnet.ccf.core.db.RMDConfigExtractor;

public class RMDDryModeHandler extends DryModeHandler {

    private RMDConfigExtractor rmdConfigExtractor = null;

    public String getDryRunModeValueFromCache(String rmdId) {
        return getDryRunValue(rmdConfigExtractor
                .getRMDConfigMapFromCache(rmdId));
    }

    public String getDryRunValue(Map<String, String> rmdConfigMap) {
        return rmdConfigMap.get(DryModeHandler.DRYRUN_KEY);
    }

    public RMDConfigExtractor getRmdConfigExtractor() {
        return rmdConfigExtractor;
    }

    public void setRmdConfigExtractor(RMDConfigExtractor rmdConfigExtractor) {
        this.rmdConfigExtractor = rmdConfigExtractor;
    }

}
