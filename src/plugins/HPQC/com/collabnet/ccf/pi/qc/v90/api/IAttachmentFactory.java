package com.collabnet.ccf.pi.qc.v90.api;

public interface IAttachmentFactory extends ILifeCycle {
    public IAttachment getItem(String key);
    public IAttachment addItem();
}