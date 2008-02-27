package td2jira.td.api.dcom;

import java.util.ArrayList;
import java.util.List;

import td2jira.td.api.IBug;
import td2jira.td.api.IFactory;
import td2jira.td.api.IField;
import td2jira.td.api.IFilter;
import td2jira.td.api.IItem;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Factory extends ActiveXComponent implements IFactory
{
    public Factory(Dispatch arg0){
        super(arg0);
    }

    public IFilter getFilter(){
        return new Filter(this);
    }
    
    public List<IField> getFields()
    {
    	//List activeXComponentList = (List<ActiveXComponent>) getPropertyAsComponent("Fields");
    	List<IField> fieldList = (List <IField>)getPropertyAsComponent("Fields");
    	//for (int cnt = 0 ; cnt < activeXComponentList.size() ; cnt++)
    	//	fieldList.add((IField) fieldList.get(cnt))

    	return fieldList;
    }

    public IBug getItem(String key) {
        Variant res = Dispatch.call(this, "Item", key);
        return new Bug(res.getDispatch());
	}
    
    public IBug addItem(String item) {
        Variant res = Dispatch.call(this, "AddItem", item);
        return new Bug(res.getDispatch());
    }
}
