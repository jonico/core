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
import com.collabnet.ccf.pi.qc.v90.api.IFilter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Filter extends ActiveXComponent implements IFilter
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Filter(ActiveXComponent factory)
    {
        super(factory.getPropertyAsComponent("Filter"));
    }

    public void setFilter(String field, String value)
    {
        Dispatch.invoke(this, "Filter", 4, new Object[] { field, value }, new int[2]);
    }

    public IFactoryList getNewList()
    {
        return new FactoryList(Dispatch.call(this, "NewList").getDispatch());
    }

    public IFactoryList getNewFilteredList(String field, String value)
    {
        clear();
        setFilter(field, value);
        return new FactoryList(Dispatch.call(this, "NewList").getDispatch());
    }

    public void refresh()
    {
        Dispatch.call(this, "Refresh");
    }

    public void clear()
    {
        Dispatch.call(this, "Clear");
    }

    public IFactoryList getFields()
    {
        return new FactoryList(getProperty("Fields").getDispatch());
    }

    public String getText()
    {
        return getPropertyAsString("Text");
    }
}
