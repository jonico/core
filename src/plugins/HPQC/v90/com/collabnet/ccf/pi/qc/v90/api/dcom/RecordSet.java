/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.pi.qc.v90.api.dcom;


import java.util.Date;

import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.DateUtilities;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class RecordSet extends ActiveXComponent implements IRecordSet
{
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public RecordSet(Dispatch arg0){
        super(arg0);
    }

    public int getRecordCount() {
    	return getPropertyAsInt("RecordCount");
    }

    public int getColCount() {
    	return getPropertyAsInt("ColCount");
    }

    public String getFieldValueAsString(String field) {
        Variant res = getFieldValueAsVariant(field);
        if(res != null) {
	        if(res.getvt() != Variant.VariantString)
	            System.err.println("Field is not a String type "+field+" "+res.getvt());
	        if(res.isNull()){
	        	return null;
	        }
	        return res.getString();
        }
        return null;
    }

    public Date getFieldValueAsDate(String field) {
        Variant res = getFieldValueAsVariant(field);
        if(res != null) {
	        if(res.getvt() != Variant.VariantDate)
	            System.err.println("Field is not a Date type "+field+" "+res.getvt());
	        if(res.isNull()){
	        	return null;
	        }
	        double ddate = res.getDate();
	        Date date = DateUtilities.convertWindowsTimeToDate(ddate);
	        return date;
        }
        return null;
    }

    public Integer getFieldValueAsInt(String field) {
        Variant res = getFieldValueAsVariant(field);
        if(res != null) {
	        if(res.getvt() != Variant.VariantInt
	        		&& res.getvt() != Variant.VariantLongInt
	        		&& res.getvt() != Variant.VariantShort)
	            System.err.println("Field is not an int type "+field+" "+res.getvt());
	        if(res.isNull()){
	        	return null;
	        }
	        int value = res.getInt();
	        return Integer.valueOf(value);
        }
        return null;
    }

    private Variant getFieldValueAsVariant(String field){
    	Variant res = Dispatch.call(this, "FieldValue", field);
    	if(res.getvt() == Variant.VariantEmpty) {
    		return null;
    	}
    	else if(res.getvt() == Variant.VariantError){
    		return null;
    	}
    	return res;
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
