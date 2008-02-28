package com.collabnet.ccf.pi.qc.api;

import com.jacob.com.Dispatch;

public interface IFactoryList extends ILifeCycle {
	Dispatch getItem(int i);
	String getItemString(int i);
	int getCount();
	IBug getBug(int i);
}