package com.collabnet.ccf.ist;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

import com.collabnet.ccf.core.CCFRuntimeException;

public abstract class ISTVersion {

    public static int getCountPart(long fullVersion) {
        return (int) (fullVersion >> 32);
    }

    private static long getFullVersion(int incrementPart, int hashPart) {
        return (((long) incrementPart) << 32) | (hashPart & 0xffffffffL);
    }

    public static final int getHashPart(long fullVersion) {
        return (int) (fullVersion);
    }

    private static String getInfoString(long version) {
        return getCountPart(version) + "." + getHashPart(version);

    }

    private static int getVersionCounter(long fullVersion) {
        return (int) (fullVersion >> 32);
    }

    private static final int INITIALVERSION = 1;

    protected long           version        = 0;

    protected int calculateHash(String s) {
        int retHash = 0;
        try {
            CRC32 crc = new CRC32();
            byte[] bytesOfMessage = s.getBytes("UTF-8");
            crc.update(bytesOfMessage);
            long theDigest = crc.getValue();
            retHash = (int) (Long.valueOf(theDigest) % Math.pow(
                    2,
                    20));

        } catch (UnsupportedEncodingException e) {
            String cause = "Failed to generate hash";
            throw new CCFRuntimeException(cause, e);
        }
        return retHash;
    }

    protected void clearCache() {
        this.version = 0;
    }

    /**
     * this method determines the hash value and writes it to this.version
     */
    protected abstract void determineHash();

    /**
     * returns the combined version with counter and hash
     *
     * @return long
     */
    public long getVersion() {
        if (this.version == 0)
            this.determineHash();
        return this.version;
    }

    /**
     * determines the counting part of the version
     *
     * @return
     */
    public int getVersionCount() {
        return getCountPart(this.getVersion());
    }

    /**
     * gets the identifying hash for the incident
     *
     * @return
     */
    public int getVersionHash() {
        return getHashPart(this.getVersion());
    }

    public String getVersionInfoString() {
        return getInfoString(this.getVersion());
    }

    /**
     * determines by hash comparison if the last change was done by CCF or not.
     *
     * if the hash parts are equal, the last update was via ISTWriter
     *
     * UNLESS the vesion part is INITIALVERSION, then the incident was never
     * updated/created via ISTWriter
     *
     * @param previousVesion
     * @return
     */
    public boolean hashEquals(long otherVersion) {

        // initial shipment when incident was created in SpiraTest
        if (INITIALVERSION == getCountPart(otherVersion))
            return true;

        int previousHash = getHashPart(otherVersion);
        int currentHash = getHashPart(this.getVersionHash());

        return previousHash != currentHash;
    }

    /**
     * increments the version count by 1.
     *
     * @return
     */
    public void incrementVersionCount() {
        this.setVersionCount(this.getVersionCount() + 1);
    }

    protected void initializeHash(int newHash) {
        this.version = getFullVersion(
                INITIALVERSION,
                newHash);
    }

    /**
     * writes the version Counting part
     *
     * left byte lastSynchedVersion - Version, a.k.a. incremental part
     *
     * right byte hash - crc of all field values , stay unchanged
     *
     * @param newVersion
     * @return
     */
    public void setVersionCount(int newCount) {
        this.version = getFullVersion(
                newCount,
                this.getVersionHash());
    }

    protected void setVersionHash(int newHash) {
        this.version = getFullVersion(
                this.getVersionCount(),
                newHash);
    }

}
