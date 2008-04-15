package com.collabnet.ccf.pi.qc.v90.api.dcom;

import java.util.List;


import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.IFactory;
import com.collabnet.ccf.pi.qc.v90.api.IField;
import com.collabnet.ccf.pi.qc.v90.api.IFilter;
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
