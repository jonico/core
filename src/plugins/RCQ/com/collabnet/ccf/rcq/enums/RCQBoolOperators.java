package com.collabnet.ccf.rcq.enums;

public enum RCQBoolOperators {
	AND(1),
	OR(2);
	
	private int opValue;
	
	private RCQBoolOperators(int i) {
		opValue = i;
	}
	
	public int getValue() {
		return opValue;
	}
}
