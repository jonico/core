package com.collabnet.ccf.pi.qc.v90.api;

import java.util.Date;
import java.util.List;

import com.collabnet.ccf.pi.qc.v90.AttachmentData;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Attachment;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public interface IAttachment extends ILifeCycle {
    public void putFileName(String filename);
    public void putDirectLink(String link);
    public void putType(int type);
    public void post();
}
