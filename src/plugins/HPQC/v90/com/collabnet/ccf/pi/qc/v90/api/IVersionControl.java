package com.collabnet.ccf.pi.qc.v90.api;

public interface IVersionControl extends ILifeCycle {
	public boolean checkOut (String comment);
	public void checkIn (String comment);
}
