package td2jira.td.api.dcom;

import td2jira.td.api.ICommand;
import td2jira.td.api.IRecordSet;

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
