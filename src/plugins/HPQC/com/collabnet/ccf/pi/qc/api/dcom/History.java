package com.collabnet.ccf.pi.qc.api.dcom;


import com.collabnet.ccf.pi.qc.api.IFilter;
import com.collabnet.ccf.pi.qc.api.IHistory;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class History extends ActiveXComponent implements IHistory
{
    public History(Dispatch arg0){
        super(arg0);
    }

    public IFilter getFilter(){
        return new Filter(this);
    }
}
