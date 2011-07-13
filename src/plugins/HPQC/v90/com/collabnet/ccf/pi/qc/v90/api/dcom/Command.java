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


import com.collabnet.ccf.pi.qc.v90.api.ICommand;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Command extends ActiveXComponent implements ICommand
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Command(Dispatch arg0){
        super(arg0);
    }
    
    public void setCommandText(String cmd) {
    	setProperty("CommandText", cmd);
    }

    public IRecordSet execute() {
        Variant rsv = Dispatch.call(this, "Execute");
        return new RecordSet(rsv.getDispatch());
    }
}
