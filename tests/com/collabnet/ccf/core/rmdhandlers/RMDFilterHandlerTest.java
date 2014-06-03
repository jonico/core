package com.collabnet.ccf.core.rmdhandlers;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.collabnet.ccf.core.db.RMDConfigDBExtractor;

public class RMDFilterHandlerTest {

    private static final String rmdID = "1";

    @Test
    public void test11Values() {
        RMDFilterHandler filterHandler = getFilterHandler("values:artf1234");
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1234"));
        Assert.assertFalse(filterHandler.containsId(rmdID, "testValue"));

    }

    @Test
    public void test12CommaSeperatedForValues() {
        RMDFilterHandler filterHandler = getFilterHandler("values:artf111,artf1234");
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1234"));
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf111"));
        Assert.assertFalse(filterHandler.containsId(rmdID, "testValue"));

    }

    @Test
    public void test13EmptyQueryForValues() {
        RMDFilterHandler filterHandler = getFilterHandler("values:");
        validateAssert(filterHandler);
    }

    @Test
    public void test14EmptyForValues() {
        RMDFilterHandler filterHandler = getFilterHandler("");
        validateAssert(filterHandler);
    }

    @Test
    public void test15NullForValues() {
        RMDFilterHandler filterHandler = getFilterHandler(null);
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1234")); //containsId returns true if filter is not enabled
        Assert.assertTrue(filterHandler.containsId(rmdID, ""));//containsId returns true if filter is not enabled

    }

    @Test
    public void test20Regex() {
        RMDFilterHandler filterHandler = getFilterHandler("regex:^artf123.$");
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1234"));
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1235"));
        Assert.assertFalse(filterHandler.containsId(rmdID, "artf12345"));
        Assert.assertFalse(filterHandler.containsId(rmdID, "testValue"));

    }

    @Test
    public void test21RegexWithMistake() {
        RMDFilterHandler filterHandler = getFilterHandler("regex:^artf123.+*$");
        Assert.assertFalse(filterHandler.containsId(rmdID, "artf1234"));
        Assert.assertFalse(filterHandler.containsId(rmdID, "testValue"));

    }

    @Test
    public void test24EmptyQueryForRegex() {
        RMDFilterHandler filterHandler = getFilterHandler("regex:");
        validateAssert(filterHandler);
    }

    @Test
    public void test30Ranges() {
        RMDFilterHandler filterHandler = getFilterHandler("ranges:jira-1234$jira-1237");
        filterHandler.setRangeSeperator("$");
        Assert.assertTrue(filterHandler.containsId(rmdID, "jira-1234"));
        Assert.assertTrue(filterHandler.containsId(rmdID, "jira-1235"));
        Assert.assertTrue(filterHandler.containsId(rmdID, "jira-1236"));
        Assert.assertTrue(filterHandler.containsId(rmdID, "jira-1237"));
        Assert.assertFalse(filterHandler.containsId(rmdID, "testValue"));

    }

    @Test
    public void test31Ranges() {
        RMDFilterHandler filterHandler = getFilterHandler("ranges:artf111-artf1234");
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1234"));
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf112"));
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1230"));
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf111"));
        Assert.assertFalse(filterHandler.containsId(rmdID, "testValue"));

    }

    @Test
    public void test32EmptyQueryForRanges() {
        RMDFilterHandler filterHandler = getFilterHandler("ranges:");
        validateAssert(filterHandler);
    }

    @Test
    public void test33MisMatchQueryForRanges() {
        RMDFilterHandler filterHandler = getFilterHandler("ranges:artf1234-abc1235");
        Assert.assertFalse(filterHandler.containsId(rmdID, "artf1234"));
        Assert.assertFalse(filterHandler.containsId(rmdID, "abc1235"));
    }

    @Test
    public void test33RangeSeperatorAsQueryRanges() {
        RMDFilterHandler filterHandler = getFilterHandler("ranges: - ");
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1234"));
    }

    @Test
    public void test40HospitalOnly() {
        RMDFilterHandler filterHandler = getFilterHandler("ignoreOrdinaryArtifactUpdates");
        Assert.assertTrue(filterHandler.ignoreOrdinaryArtifactUpdates(rmdID));
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1234"));
    }

    @Test
    public void test41NullForHospitalOnly() {
        RMDFilterHandler filterHandler = getFilterHandler(null);
        Assert.assertFalse(filterHandler.ignoreOrdinaryArtifactUpdates(rmdID));
    }

    @Test
    public void test42EmptyForHospitalOnly() {
        RMDFilterHandler filterHandler = getFilterHandler("");
        Assert.assertFalse(filterHandler.ignoreOrdinaryArtifactUpdates(rmdID));
    }

    @Test
    public void test50QueryWithoutFormat() {
        RMDFilterHandler filterHandler = getFilterHandler("abc");
        validateAssert(filterHandler);
    }

    @Test
    public void test51QueryWithSymbols() {
        RMDFilterHandler filterHandler = getFilterHandler("abc !@#!@#!@#$%%^%&^&**^**(");
        validateAssert(filterHandler);
    }

    @Test
    public void test54QueryWithAplhaNumeric() {
        RMDFilterHandler filterHandler = getFilterHandler("1234qwer");
        validateAssert(filterHandler);
    }

    @Test
    public void test55QueryWithColonAndAphen() {
        RMDFilterHandler filterHandler = getFilterHandler(" : - ");
        validateAssert(filterHandler);
    }

    private Map<String, String> getDummyRMDConfigMap(String filterValue) {
        Map<String, String> rmdconfig = new HashMap<String, String>();
        rmdconfig.put(FilterHandler.FILTER_KEY, filterValue);
        return rmdconfig;
    }

    private RMDConfigDBExtractor getDummyRMDExtractor(String rmdIdString,
            String filterValue) {
        Map<String, Map<String, String>> rmdAndRmdConfigMap = new HashMap<String, Map<String, String>>();
        rmdAndRmdConfigMap.put(rmdIdString, getDummyRMDConfigMap(filterValue));
        RMDConfigDBExtractor dummyExtractor = new RMDConfigDBExtractor();
        dummyExtractor.setRmdAndRMDConfigMap(rmdAndRmdConfigMap);
        return dummyExtractor;
    }

    private RMDFilterHandler getFilterHandler(String rmdConfigVal) {
        RMDFilterHandler filterHandler = new RMDFilterHandler();
        RMDConfigDBExtractor dummyExtractor = getDummyRMDExtractor(rmdID,
                rmdConfigVal);
        filterHandler.setRmdConfigExtractor(dummyExtractor);
        return filterHandler;
    }

    private void validateAssert(RMDFilterHandler filterHandler) {
        Assert.assertTrue(filterHandler.containsId(rmdID, "artf1234"));
        Assert.assertTrue(filterHandler.containsId(rmdID, "testValue"));
    }
}
