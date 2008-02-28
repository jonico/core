package com.collabnet.ccf.pi.qc;

import com.collabnet.ccf.pi.qc.api.IBug;

public interface IQCDefect extends IBug {

	public abstract String[] getFields();

	public abstract void setFields(String[] fields);

}