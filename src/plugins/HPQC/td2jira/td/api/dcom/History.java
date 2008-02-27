package td2jira.td.api.dcom;

import td2jira.td.api.IHistory;
import td2jira.td.api.IFilter;

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
