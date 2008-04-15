package com.collabnet.ccf.pi.qc.v90.api;

import java.util.List;

import com.collabnet.ccf.pi.qc.v90.api.dcom.Filter;
import com.jacob.com.Dispatch;

public interface IAttachmentFactory extends ILifeCycle {
    public IAttachment getItem(String key);
    public IAttachment addItem();
}