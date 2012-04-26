/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.pi.qc.v90.api.dcom;


import com.collabnet.ccf.pi.qc.v90.api.IFactoryList;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class FactoryList extends ActiveXComponent implements IFactoryList {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FactoryList(Dispatch arg0) {
        super(arg0);
    }

    public Dispatch getItem(int i) {
        return Dispatch.call(this, "Item", Integer.valueOf(i)).getDispatch();
    }

    public String getItemString(int i){
        return Dispatch.call(this, "Item", Integer.valueOf(i)).getString();
    }

    public int getCount() {
        return getPropertyAsInt("Count");
    }

    public Bug getBug(int i) {
        return new Bug(getItem(i));
    }
    
    public Requirement getRequirement(int i) {
        return new Requirement(getItem(i));
    }
}
