package com.collabnet.ccf.pi.qc.v90.api;


public interface IFilter extends ILifeCycle {
	void setFilter(String field, String value);
	IFactoryList getNewList();
	IFactoryList getNewFilteredList(String field, String value);
	void refresh();
	void clear();
	IFactoryList getFields();
	String getText();
}