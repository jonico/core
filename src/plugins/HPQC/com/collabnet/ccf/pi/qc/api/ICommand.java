package com.collabnet.ccf.pi.qc.api;

public interface ICommand extends ILifeCycle {
    public void setCommandText(String cmd);
    public IRecordSet execute();
}