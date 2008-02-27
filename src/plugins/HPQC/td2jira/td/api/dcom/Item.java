package td2jira.td.api.dcom;

import td2jira.td.api.IItem;
import td2jira.td.api.IRecordSet;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Item extends ActiveXComponent implements IItem
{
    public Item(Dispatch arg0){
        super(arg0);
    }
}
