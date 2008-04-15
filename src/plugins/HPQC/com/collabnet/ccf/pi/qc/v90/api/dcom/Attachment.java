package com.collabnet.ccf.pi.qc.v90.api.dcom;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;


import com.collabnet.ccf.pi.qc.v90.api.IAttachment;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

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
