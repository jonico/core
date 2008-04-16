package com.collabnet.ccf.pi.qc.v90.api;

public interface IAttachment extends ILifeCycle {
    public void putFileName(String filename);
    public void putDirectLink(String link);
    public void putType(int type);
    public void post();
}
