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

import org.apache.log4j.Logger;

import com.collabnet.ccf.pi.qc.v90.api.IAttachment;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Attachment extends ActiveXComponent implements IAttachment {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
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

    public void putDescription(String description){
    	setProperty("Description", description);
    }

    public void post() {
        Dispatch.call(this, "Post");
    }
    
    public String getId() {
		return Integer.toString(getPropertyAsInt("ID"));
	}

}
