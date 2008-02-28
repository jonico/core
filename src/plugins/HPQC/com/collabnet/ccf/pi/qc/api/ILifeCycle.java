package com.collabnet.ccf.pi.qc.api;

import java.io.Serializable;

public interface ILifeCycle extends Serializable {
	void safeRelease();
}
