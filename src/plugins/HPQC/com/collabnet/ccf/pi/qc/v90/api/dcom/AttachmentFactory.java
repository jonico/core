package com.collabnet.ccf.pi.qc.v90.api.dcom;

import com.collabnet.ccf.pi.qc.v90.api.IAttachment;
import com.collabnet.ccf.pi.qc.v90.api.IAttachmentFactory;
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
