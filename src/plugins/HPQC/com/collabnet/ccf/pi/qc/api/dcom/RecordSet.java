package com.collabnet.ccf.pi.qc.api.dcom;


import com.collabnet.ccf.pi.qc.api.IRecordSet;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class RecordSet extends ActiveXComponent implements IRecordSet
{
    public RecordSet(Dispatch arg0){
        super(arg0);
    }

    public int getRecordCount() {
    	return getPropertyAsInt("RecordCount");
    }
 
    public int getColCount() {
    	return getPropertyAsInt("ColCount");
    }
    
    public String getFieldAsString(String field) {
        Variant res = Dispatch.call(this, "FieldValue", field);
        if(res.getvt() != 8 && res.getvt() != 0)
            System.err.println("Field is not a String type "+field+" "+res.getvt());
        return res.getString();
    }

    public String getFieldValue(String fieldName) {
    	return getFieldAsString(fieldName);
    }

    public String getColNameAsString(int index) {
        Variant res = Dispatch.call(this, "ColName", index);
        if(res.getvt() != 8 && res.getvt() != 0)
            System.err.println("Col Name is not a String type index:"+index+" "+res.getvt());
        return res.getString();
    }

    public String getColName(int index) {
    	return getColNameAsString(index);
    }

    public void next() {
    	Dispatch.call(this, "Next");
    }
}
