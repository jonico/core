package com.collabnet.ccf.pi.qc.api.dcom;


import com.collabnet.ccf.pi.qc.api.IField;
import com.collabnet.ccf.pi.qc.api.IRecordSet;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Field extends ActiveXComponent implements IField
{
    public Field(Dispatch arg0){
        super(arg0);
    }
}
