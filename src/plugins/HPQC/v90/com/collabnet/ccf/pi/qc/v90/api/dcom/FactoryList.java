package com.collabnet.ccf.pi.qc.v90.api.dcom;


import com.collabnet.ccf.pi.qc.v90.api.IFactoryList;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class FactoryList extends ActiveXComponent implements IFactoryList {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FactoryList(Dispatch arg0) {
        super(arg0);
    }

    public Dispatch getItem(int i) {
        return Dispatch.call(this, "Item", Integer.valueOf(i)).getDispatch();
    }

    public String getItemString(int i){
        return Dispatch.call(this, "Item", Integer.valueOf(i)).getString();
    }

    public int getCount() {
        return getPropertyAsInt("Count");
    }

    public Bug getBug(int i) {
        return new Bug(getItem(i));
    }
}
