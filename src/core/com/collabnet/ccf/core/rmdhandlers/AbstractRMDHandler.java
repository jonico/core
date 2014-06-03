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

public abstract class AbstractRMDHandler {

    private static final Log   log                 = LogFactory
                                                           .getLog(AbstractRMDHandler.class);

    public final static String VALUES              = "values";

    public final static String RANGES              = "ranges";

    public final static String EMPTY_STRING        = "";

    public final static String ALPHA_NUMERIC_REGEX = "^([a-zA-Z]+?.)([0-9]+?)$";             // matches a1,A1,artf1234,jira-1234

    public final static String NUMERIC_REGEX       = "^([0-9]+)$";

    public final static String COMMAS              = ",";

    private String             valueSeparator      = ":";

    private String             rangeSeperator      = "-";

    private RMDConfigExtractor rmdConfigExtractor  = null;

    public abstract String getValForRMDConfig(String rmdID);

    public String getRangeSeperator() {
        return rangeSeperator;
    }

    public RMDConfigExtractor getRmdConfigExtractor() {
        return rmdConfigExtractor;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void loadRMDAndRMDConfig(String rmdID) {
        if (this.getRmdConfigExtractor() != null) {
            this.getRmdConfigExtractor().populateRMDAndRMDConfigValues(rmdID);
        }
    }

    public boolean rangesFilterEnabled(String type, String query) {
        return RANGES.equals(type) && query.contains(getRangeSeperator()); // validating format of range expression
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

    public boolean valuesFilterEnabled(String type) {
        return VALUES.equals(type);
    }

    protected String getFilterQuery(String rmdID) {
        String query = EMPTY_STRING;
        String filterVal = getValForRMDConfig(rmdID);
        String[] filterVals = splitFilterValToArray(filterVal);
        if (filterVals != null && filterVals.length == 2) {
            query = filterVals[1];
        }
        return query;
    }

    protected String getFilterType(String rmdID) {
        String filterType = EMPTY_STRING;
        String filterVal = getValForRMDConfig(rmdID);
        String[] filterVals = splitFilterValToArray(filterVal);
        if (filterVals != null && filterVals.length == 2) {
            filterType = filterVals[0];
        }
        return filterType;
    }

    protected boolean isAlphaNumeric(String[] ranges) {
        boolean isAlphaNumeric = false;
        for (String alphanumericRange : ranges) {
            isAlphaNumeric = regexMatches(ALPHA_NUMERIC_REGEX,
                    alphanumericRange);
            if (!isAlphaNumeric)
                break;
        }
        return isAlphaNumeric;
    }

    protected boolean isNumeric(String[] ranges) {
        boolean isNumeric = false;
        for (String numericRange : ranges) {
            isNumeric = regexMatches(NUMERIC_REGEX, numericRange);
            if (!isNumeric)
                break;
        }
        return isNumeric;
    }

    protected Set<String> populateIds(String val) {
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

    protected Set<String> populateIdsForAlphaNumericRange(String[] ranges) {
        Set<String> artifactIdSet = new HashSet<String>();
        Matcher startRangeMatcher = Pattern.compile(ALPHA_NUMERIC_REGEX)
                .matcher(ranges[0]);
        Matcher endRangeMatcher = Pattern.compile(ALPHA_NUMERIC_REGEX).matcher(
                ranges[1]);
        if (startRangeMatcher.matches() && endRangeMatcher.matches()) { //both range values matches
            if (startRangeMatcher.group(1).equals(endRangeMatcher.group(1))) { //both prefix matches
                String prefix = startRangeMatcher.group(1);
                int startRange = Integer.parseInt(startRangeMatcher.group(2));
                int endRange = Integer.parseInt(endRangeMatcher.group(2));
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

    protected Set<String> populateIdsForNumericRange(String[] ranges) {
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

    protected boolean regexMatches(String regex, String val) {
        try {
            return Pattern.matches(regex, val);
        } catch (PatternSyntaxException e) {
            log.error("Invalid regex expression " + regex);
            return false;
        }
    }

    private String[] splitFilterValToArray(String filterVal) {
        if (filterVal.contains(getValueSeparator())) {
            return StringUtils.split(filterVal, getValueSeparator());
        }
        return null;
    }

}
