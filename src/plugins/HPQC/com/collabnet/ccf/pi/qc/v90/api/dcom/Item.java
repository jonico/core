package com.collabnet.ccf.pi.qc.v90.api.dcom;


import com.collabnet.ccf.pi.qc.v90.api.IItem;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Item extends ActiveXComponent implements IItem
{
    public Item(Dispatch arg0){
        super(arg0);
    }
}
