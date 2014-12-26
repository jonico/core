package com.collabnet.ccf.ist;

public abstract class ISTVersion {

    public static long getFullVersion(int incrementPart, int hashPart) {
        return (((long) incrementPart) << 32) | (hashPart & 0xffffffffL);
    }

    public static final int getHashPart(long fullVersion) {
        return (int) (fullVersion);
    }

    public static final int getHashPart(String fullVersion) {
        long fv = Long.parseLong(fullVersion);
        return (int) fv;
    }

    public static int getVersionPart(long fullVersion) {
        return (int) (fullVersion >> 32);
    }

    public static int getVersionPart(String fullVersion) {
        long fv = Long.parseLong(fullVersion);
        return (int) (fv >> 32);
    }

    public static String getVersionString(long version) {
        return getVersionPart(version) + "." + getHashPart(version);

    }

    public static String getVersionString(String version) {
        return (!version.equals("null")) ? getVersionPart(version) + "."
                + getHashPart(version) : "(not set)";

    }

    public static final String INITIALVERSION = "0";

    protected int              hash           = 0;

    private long               version        = 0;

    protected abstract int determineHash();

    public int getHash() {
        if (this.hash == 0) {
            this.hash = this.determineHash();
        }
        return this.hash;
    }

    /**
     * returns the version part incremented by 1.
     *
     * @return
     */
    public int getNextVersion() {
        return getVersionPart(this.version) + 1;
    }

    public long getVersion() {
        if (this.version == 0)
            this.setVersionPart(INITIALVERSION);
        return this.version;
    }

    public int getVersionPart() {
        return getVersionPart(this.version);
    }

    public String getVersionString() {
        return getVersionString(this.getVersion());
    }

    public int refreshHash() {
        this.hash = this.determineHash();
        return this.hash;
    }

    public void setHash(int newHash) {
        this.hash = newHash;
    }

    /**
     * writes the version Part
     *
     * left byte lastSynchedVersion - Version, a.k.a. incremental part
     *
     * right byte hash - crc of all field values , stay unchanged
     *
     * @param newVersion
     * @return
     */
    public void setVersionPart(int newVersion) {
        if (newVersion == Integer.valueOf(INITIALVERSION)) {
            this.version = getFullVersion(
                    Integer.parseInt(INITIALVERSION),
                    this.determineHash());
        } else {
            this.version = getFullVersion(
                    newVersion,
                    this.determineHash());
        }
    }

    /**
     * writes the version Part
     *
     * left byte lastSynchedVersion - Version, a.k.a. incremental part
     *
     * right byte hash - crc of all field values , stay unchanged
     *
     * @param newVersion
     * @return
     */
    public void setVersionPart(String newVersion) {
        this.setVersionPart(Integer.parseInt(newVersion));
    }

}
