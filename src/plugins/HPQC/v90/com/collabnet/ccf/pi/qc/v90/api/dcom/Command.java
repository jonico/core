package com.collabnet.ccf.pi.qc.v90.api.dcom;


import com.collabnet.ccf.pi.qc.v90.api.ICommand;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Command extends ActiveXComponent implements ICommand
{
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
