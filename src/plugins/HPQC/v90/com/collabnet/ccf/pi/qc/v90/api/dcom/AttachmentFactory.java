/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.pi.qc.v90.api.dcom;

import com.collabnet.ccf.pi.qc.v90.api.IAttachment;
import com.collabnet.ccf.pi.qc.v90.api.IAttachmentFactory;
import com.collabnet.ccf.pi.qc.v90.api.IFilter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class AttachmentFactory extends ActiveXComponent implements IAttachmentFactory {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public AttachmentFactory(Dispatch arg0) {
        super(arg0);
    }

    public IAttachment addItem() {
        Variant var = new Variant();
        var.putNull();

        Variant res = Dispatch.call(this, "AddItem", var);
        return new Attachment(res.getDispatch());
    }

    public IFilter getFilter() {
        return new Filter(this);
    }

    public IAttachment getItem(String key) {
        Variant res = Dispatch.call(this, "Item", key);
        return new Attachment(res.getDispatch());
    }

    public void removeItem(String key) {
        Variant res = Dispatch.call(this, "RemoveItem", key);
        res.getvt();
    }

}
