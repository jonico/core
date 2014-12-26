package com.collabnet.ccf.ist.sandbox;

public class VersionRunner {

    public static void main(String[] args) {

        VersionTester vHelper = new VersionTester();
        print(
                "Hash",
                "FullVersion",
                "Version.Hash",
                "Result",
                "Comment");

        int hash = vHelper.getHash();

        print(
                String.valueOf(hash),
                String.valueOf(vHelper.getVersion()),
                String.valueOf(vHelper.getVersionString()),
                "",
                "no version set, hash only");

        long previousFullVersion = vHelper.getVersion();
        int previousHash = hash;
        for (int count = 1; count < 100; count++) {
            hash = vHelper.refreshHash();
            vHelper.getVersionPart();
            vHelper.setVersionPart(vHelper.getNextVersion());
            String result = (previousFullVersion < vHelper.getVersion()) ? "OK"
                    : "ERROR!";
            if (previousHash < hash) {
                result += " hash up";
            } else if (previousHash > hash) {
                result += " hash down";
            } else {
                result += " hash unchanged";
            }
            print(
                    String.valueOf(hash),
                    String.valueOf(vHelper.getVersion()),
                    String.valueOf(vHelper.getVersionString()),
                    result,
                    "version set to " + vHelper.getVersionPart());
            previousFullVersion = vHelper.getVersion();
        }

    }

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

}
