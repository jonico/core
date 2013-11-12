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

import java.util.List;

import com.collabnet.ccf.pi.qc.v90.api.IField;
import com.collabnet.ccf.pi.qc.v90.api.IFilter;
import com.collabnet.ccf.pi.qc.v90.api.IRequirement;
import com.collabnet.ccf.pi.qc.v90.api.IRequirementsFactory;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class RequirementsFactory extends ActiveXComponent implements IRequirementsFactory {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public RequirementsFactory(Dispatch arg0) {
        super(arg0);
    }

    public IRequirement addItem(String item) {
        Variant res = Dispatch.call(this, "AddItem", item);
        return new Requirement(res.getDispatch());
    }

    @SuppressWarnings("unchecked")
    public List<IField> getFields() {
        //List activeXComponentList = (List<ActiveXComponent>) getPropertyAsComponent("Fields");
        List<IField> fieldList = (List<IField>) getPropertyAsComponent("Fields");
        //for (int cnt = 0 ; cnt < activeXComponentList.size() ; cnt++)
        //	fieldList.add((IField) fieldList.get(cnt))

        return fieldList;
    }

    public IFilter getFilter() {
        return new Filter(this);
    }

    public IRequirement getItem(String key) {
        Variant res = Dispatch.call(this, "Item", key);
        return new Requirement(res.getDispatch());
    }

    public void removeItem(String item) {
        Dispatch.call(this, "RemoveItem", item);
    }
}
