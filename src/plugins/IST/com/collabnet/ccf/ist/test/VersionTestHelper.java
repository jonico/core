package com.collabnet.ccf.ist.test;

import java.util.Random;

import com.collabnet.ccf.ist.ISTVersion;

public class VersionTestHelper extends ISTVersion {

    Random r    = new Random();
    int    hash = 0;

    @Override
    protected void determineHash() {
        int h = r.nextInt();
        if (h < 0)
            h = -h;

        if (r.nextBoolean()) {
            this.initializeHash(h);
            this.hash = getVersionHash();
        } else {
            if (this.hash == 0) {
                this.hash = h;
            }
            this.initializeHash(this.hash);
        }

    }

    public void reset() {
        this.clearCache();
    }
}
