package td2jira.td.api.dcom;

import td2jira.td.api.IField;
import td2jira.td.api.IRecordSet;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Field extends ActiveXComponent implements IField
{
    public Field(Dispatch arg0){
        super(arg0);
    }
}
