/**
 * 
 */
package com.collabnet.ccf.pi.qc;

import java.util.List;

import com.collabnet.ccf.pi.qc.api.IBug;
import com.collabnet.ccf.pi.qc.api.dcom.Bug;
import com.jacob.com.Dispatch;


/**
 * @author Collabnet (c) 2008
 *
 */
public class QCDefect extends Bug implements IQCDefect {
	String[] fields;
	List <byte []> attachmentData;

	public QCDefect(Dispatch arg0)
	{
		super(arg0);
	}

	public QCDefect(Bug bug){
		super(bug);
	}
	
	/* (non-Javadoc)
	 * @see com.collabnet.connector.qc.IQCDefect#getFields()
	 */
	public String[] getFields() {
		return fields;
	}

	/* (non-Javadoc)
	 * @see com.collabnet.connector.qc.IQCDefect#setFields(java.lang.String[])
	 */
	public void setFields(String[] fields) {
		this.fields = fields;
	}
}
