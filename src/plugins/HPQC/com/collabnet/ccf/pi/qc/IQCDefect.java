package com.collabnet.ccf.pi.qc;

import java.util.List;

import com.collabnet.ccf.core.config.Field;
import com.collabnet.ccf.pi.qc.api.IBug;
import com.collabnet.ccf.pi.qc.api.IConnection;

public interface IQCDefect extends IBug {

	public List<Field> getFields();
	public void setFields(List<Field> fields);
	public void fillFieldsFromBug(IConnection qcc);
}