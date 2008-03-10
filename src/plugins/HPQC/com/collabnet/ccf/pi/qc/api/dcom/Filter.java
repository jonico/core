package com.collabnet.ccf.pi.qc.api.dcom;


import com.collabnet.ccf.pi.qc.api.IFactoryList;
import com.collabnet.ccf.pi.qc.api.IFilter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Filter extends ActiveXComponent implements IFilter
{

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
