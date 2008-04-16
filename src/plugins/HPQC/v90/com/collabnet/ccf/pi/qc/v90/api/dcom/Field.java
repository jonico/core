package com.collabnet.ccf.pi.qc.v90.api.dcom;


import com.collabnet.ccf.pi.qc.v90.api.IField;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Field extends ActiveXComponent implements IField
{
    public Field(Dispatch arg0){
        super(arg0);
    }
}
