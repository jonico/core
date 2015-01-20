package com.collabnet.ccf.ist.test;

import junit.framework.Assert;

import org.junit.Test;

import com.collabnet.ccf.ist.ISTVersion;

public class TestVersion {

    private static void print(String col1, String col2, String col3,
            String col4, String col5) {
        System.out.printf(
                "%-10s %-15s %-15s %-15s %s\n",
                col1,
                col2,
                col3,
                col4,
                col5);

    }

    @Test
    public final void testVersion() {

        VersionTestHelper vHelper = new VersionTestHelper();
        print(
                "Hash",
                "FullVersion",
                "Version.Hash",
                "Result",
                "Comment");

        int hash = vHelper.getVersionHash();

        print(
                String.valueOf(hash),
                String.valueOf(vHelper.getVersion()),
                vHelper.getVersionInfoString(),
                "(n/a)",
                "initial version & fresh hash");

        long previousFullVersion = vHelper.getVersion();
        int previousHash = hash;
        for (int count = 1; count < 100; count++) {
            vHelper.reset();
            hash = vHelper.getVersionHash();
            vHelper.setVersionCount(count);
            vHelper.incrementVersionCount();
            String result = (previousFullVersion < vHelper.getVersion()) ? "OK"
                    : "ERROR!";
            Assert.assertEquals(
                    "OK",
                    result);
            if (previousHash < hash) {
                result += " (hash up)";
            } else if (previousHash > hash) {
                result += " (hash down)";
            } else {
                result += " (same hash)";
            }
            print(
                    String.valueOf(hash),
                    String.valueOf(vHelper.getVersion()),
                    String.valueOf(vHelper.getVersionInfoString()),
                    result,
                    "version set to " + vHelper.getVersionCount());
            previousFullVersion = vHelper.getVersion();
            previousHash = ISTVersion.getHashPart(previousFullVersion);
        }

    }

}
