package com.collabnet.ccf.pi.qc.v90.api;

public interface IVersionControl extends ILifeCycle {
    public void checkIn(String comment);

    public boolean checkOut(String comment);
}
