package com.collabnet.ccf.pi.qc.api;

import java.util.Date;
import java.util.List;

import com.collabnet.ccf.pi.qc.AttachmentData;
import com.collabnet.ccf.pi.qc.api.dcom.Attachment;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public interface IAttachment extends ILifeCycle {
    public void putFileName(String filename);
    public void putDirectLink(String link);
    public void putType(int type);
    public void post();
}
