package com.collabnet.connector.qc;

import td2jira.td.api.IBug;

public interface IQCDefect extends IBug {

	public abstract String[] getFields();

	public abstract void setFields(String[] fields);

}