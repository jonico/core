package com.collabnet.ccf.core.rmdhandlers;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.db.RMDConfigExtractor;

/**
 * RMDFilterHandler
 * 
 * 
 * @author kbalaji
 * 
 */
public class RMDFilterHandler implements FilterHandler {

    private static final Log   log                = LogFactory
                                                          .getLog(RMDFilterHandler.class);

    private String             valueSeparator     = ":";

    private String             rangeSeperator     = "-";

    private RMDConfigExtractor rmdConfigExtractor = null;

    public boolean containsId(String rmdID, String id) {
        boolean isIdExists = false;
        if (rmdID == null || id == null) {
            return true;
        }
        if (!isFilterEnabled(rmdID)) {
            return true;
        }
        if (regexFilterEnabled(rmdID)) {
            isIdExists = regexMatches(getFilterQuery(rmdID), id);
        } else if (valuesFilterEnabled(rmdID)) {
            String query = getFilterQuery(rmdID);
            isIdExists = populateIds(query).contains(id);
        } else if (rangesFilterEnabled(rmdID)) {
            String[] ranges = StringUtils.split(getFilterQuery(rmdID),
                    getRangeSeperator());
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

    public String getRangeSeperator() {
        return rangeSeperator;
    }

    public RMDConfigExtractor getRmdConfigExtractor() {
        return rmdConfigExtractor;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public boolean hospitalOnlyFilterEnabled(String rmdID) {
        return HOSPITAL_ONLY.equals(getFilterValue(rmdID));
    }

    public void loadRMDAndRMDConfig(String rmdID) {
        if (this.getRmdConfigExtractor() != null) {
            this.getRmdConfigExtractor().populateRMDAndRMDConfigValues(rmdID);
        }
    }

    public void setRangeSeperator(String rangeSeperator) {
        this.rangeSeperator = rangeSeperator;
    }

    public void setRmdConfigExtractor(RMDConfigExtractor rmdConfigExtractor) {
        this.rmdConfigExtractor = rmdConfigExtractor;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    private String getFilterQuery(String rmdID) {
        String query = EMPTY_STRING;
        String filterVal = getFilterValue(rmdID);
        if (filterVal.contains(getValueSeparator())) {
            String[] filterVals = StringUtils.split(filterVal,
                    getValueSeparator());
            if (filterVals.length == 2) {
                query = filterVals[1];
            }
        }
        return query;
    }

    private String getFilterType(String rmdID) {
        String filterType = EMPTY_STRING;
        String filterVal = getFilterValue(rmdID);
        if (filterVal.contains(getValueSeparator())) {
            String[] filterVals = StringUtils.split(filterVal,
                    getValueSeparator());
            if (filterVals.length == 2) {
                filterType = filterVals[0];
            }
        }
        return filterType;
    }

    private String getFilterValue(String rmdID) {
        String val = EMPTY_STRING;
        if (rmdID != null && isFilterEnabled(rmdID)) {
            val = this.getRmdConfigExtractor().getRMDConfigValue(rmdID,
                    FILTER_KEY);
        }
        return val;
    }

    private boolean isAlphaNumeric(String[] ranges) {
        boolean isAlphaNumeric = false;
        for (String alphanumericRange : ranges) {
            isAlphaNumeric = regexMatches(ALPHA_NUMERIC_REGEX,
                    alphanumericRange);
            if (!isAlphaNumeric)
                break;
        }
        return isAlphaNumeric;
    }

    private boolean isFilterEnabled(String rmdID) {
        return this.getRmdConfigExtractor() != null
                && this.getRmdConfigExtractor().getRMDConfigValue(rmdID,
                        FILTER_KEY) != null;
    }

    private boolean isNumeric(String[] ranges) {
        boolean isNumeric = false;
        for (String numericRange : ranges) {
            isNumeric = regexMatches(NUMERIC_REGEX, numericRange);
            if (!isNumeric)
                break;
        }
        return isNumeric;
    }

    private Set<String> populateIds(String val) {
        Set<String> artifactIdSet = new HashSet<String>();
        if (val.contains(COMMAS)) {
            String[] values = StringUtils.split(val, COMMAS);
            for (String id : values) {
                artifactIdSet.add(id);
            }
        } else {
            artifactIdSet.add(val);
        }
        return artifactIdSet;
    }

    private Set<String> populateIdsForAlphaNumericRange(String[] ranges) {
        Set<String> artifactIdSet = new HashSet<String>();
        Matcher prefixMatcher = Pattern.compile(ALPHA_NUMERIC_REGEX).matcher(
                ranges[0]);
        Matcher suffixMatcher = Pattern.compile(ALPHA_NUMERIC_REGEX).matcher(
                ranges[1]);
        if (prefixMatcher.matches() && suffixMatcher.matches()) { //both range values matches
            if (prefixMatcher.group(1).equals(suffixMatcher.group(1))) { //is prefix matches
                String prefix = prefixMatcher.group(1);
                int startRange = Integer.parseInt(prefixMatcher.group(2));
                int endRange = Integer.parseInt(suffixMatcher.group(2));
                do {
                    String id = String.format("%s%s", prefix,
                            String.valueOf(startRange));
                    artifactIdSet.add(id);
                    startRange++;
                } while (startRange <= endRange);
            }
        }
        return artifactIdSet;
    }

    private Set<String> populateIdsForNumericRange(String[] ranges) {
        Set<String> artifactIdSet = new HashSet<String>();
        int startRange = Integer.parseInt(ranges[0]);
        int endRange = Integer.parseInt(ranges[1]);
        do {
            String id = String.valueOf(startRange);
            artifactIdSet.add(id);
            startRange++;
        } while (startRange <= endRange);
        return artifactIdSet;
    }

    private boolean rangesFilterEnabled(String rmdID) {
        return RANGES.equals(getFilterType(rmdID))
                && getFilterValue(rmdID).contains(getRangeSeperator()); // validating format of range expression
    }

    private boolean regexFilterEnabled(String rmdID) {
        return REGEX.equals(getFilterType(rmdID));
    }

    private boolean regexMatches(String regex, String val) {
        try {
            return Pattern.matches(regex, val);
        } catch (PatternSyntaxException e) {
            log.error("Invalid regex expression " + regex);
            return false;
        }
    }

    private boolean valuesFilterEnabled(String rmdID) {
        return VALUES.equals(getFilterType(rmdID));
    }
}
