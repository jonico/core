package com.collabnet.ccf.core.rmdhandlers;

import org.apache.commons.lang.StringUtils;

/**
 * RMDFilterHandler
 * 
 * 
 * @author kbalaji
 * 
 */
public class RMDFilterHandler extends AbstractRMDHandler implements FilterHandler {

    public boolean containsId(String rmdID, String id) {
        boolean isIdExists = true;
        if (rmdID == null || id == null) {
            return true;
        }
        if (!isFilterEnabled(rmdID)) {
            return true;
        }
        String query = getFilterQuery(rmdID);
        String type = getFilterType(rmdID);
        if (regexFilterEnabled(type)) {
            isIdExists = regexMatches(query, id);
        } else if (valuesFilterEnabled(type)) {
            isIdExists = populateIds(query).contains(id);
        } else if (rangesFilterEnabled(type, query)) {
            String[] ranges = StringUtils.split(query, getRangeSeperator());
            //TODO: need to validate ranges value whether it exists or not
            if (isAlphaNumeric(ranges)) {
                isIdExists = populateIdsForAlphaNumericRange(ranges).contains(
                        id);
            } else if (isNumeric(ranges)) {
                isIdExists = populateIdsForNumericRange(ranges).contains(id);
            }
        }
        return isIdExists;
    }

    public String getValForRMDConfig(String rmdID) {
        String val = EMPTY_STRING;
        if (rmdID != null && isFilterEnabled(rmdID)) {
            val = this.getRmdConfigExtractor().getRMDConfigValue(rmdID,
                    FILTER_KEY);
        }
        return val;
    }

    public boolean ignoreOrdinaryArtifactUpdates(String rmdID) {
        return IGNORE_ORDINARY_ARTIFACT_UPDATES
                .equals(getValForRMDConfig(rmdID));
    }

    private boolean isFilterEnabled(String rmdID) {
        return this.getRmdConfigExtractor() != null
                && this.getRmdConfigExtractor().getRMDConfigValue(rmdID,
                        FILTER_KEY) != null;
    }

    private boolean regexFilterEnabled(String type) {
        return REGEX.equals(type);
    }

}
