package com.collabnet.ccf.pi.qc.api.dcom;

import java.util.ArrayList;
import java.util.List;


import com.collabnet.ccf.pi.qc.api.IAttachment;
import com.collabnet.ccf.pi.qc.api.IAttachmentFactory;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class AttachmentFactory extends ActiveXComponent implements IAttachmentFactory
{
    public AttachmentFactory(Dispatch arg0){
        super(arg0);
    }

    public IAttachment getItem(String key) {
        Variant res = Dispatch.call(this, "Item", key);
        return new Attachment(res.getDispatch());
	}
    
    public IAttachment addItem() {
		Variant var = new Variant();
		var.putNull();

        Variant res = Dispatch.call(this, "AddItem", var);
        return new Attachment(res.getDispatch());
    }


}
