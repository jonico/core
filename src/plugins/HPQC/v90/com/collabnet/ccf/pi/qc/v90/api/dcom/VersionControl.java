package com.collabnet.ccf.pi.qc.v90.api.dcom;

import com.collabnet.ccf.pi.qc.v90.api.IVersionControl;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class VersionControl extends ActiveXComponent implements IVersionControl {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public VersionControl(Dispatch arg0) {
        super(arg0);
    }

    public void checkIn(String comment) {
        Dispatch.call(this, "CheckIn", comment);
    }

    public boolean checkOut(String comment) {
        Variant result = null;
        try {
            result = Dispatch.call(this, "CheckOut", comment);
        } catch (IllegalStateException e) {
            return false;
        }
        if (result == null) {
            return false;
        } else {
            return true;
        }
    }

    public void safeRelease() {
        // TODO Auto-generated method stub
    }
}
