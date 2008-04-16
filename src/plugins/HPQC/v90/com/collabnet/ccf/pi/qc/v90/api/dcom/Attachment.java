package com.collabnet.ccf.pi.qc.v90.api.dcom;

import org.apache.log4j.Logger;

import com.collabnet.ccf.pi.qc.v90.api.IAttachment;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Attachment extends ActiveXComponent implements IAttachment {
	public static Logger logger = Logger.getLogger(Attachment.class);

    public Attachment(Dispatch arg0)
    {
        super(arg0);
    }

    public void putFileName(String filename) {
        setProperty("FileName", filename);
    }
    public void putDirectLink(String link) {
    	setProperty("DirectLink", link);
    }

    public void putType(int type) {
        setProperty("Type", type);
    }

    public void post() {
        Dispatch.call(this, "Post");
    }

}
