package com.collabnet.ccf.pi.qc.api;

import java.util.List;

public interface IFactory extends ILifeCycle {
	IFilter getFilter();
	List<IField> getFields();
    public IBug getItem(String key);
    public IBug addItem(String item);
}