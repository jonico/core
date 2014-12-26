package com.collabnet.ccf.ist.sandbox;

import java.util.Random;

import com.collabnet.ccf.ist.ISTVersion;

public class VersionTester extends ISTVersion {

    Random r = new Random();

    @Override
    protected int determineHash() {
        if (this.hash == 0 || this.r.nextBoolean()) {
            this.hash = r.nextInt();
            this.hash = this.hash < 0 ? -this.hash : this.hash;
        }
        return this.hash;
    }
}
