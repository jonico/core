package com.collabnet.ccf.rcq.enums;

public enum RCQCompOperators {
	EQUALS(1),
	NOTEQUAL(2),
	LESSTHAN(3),
	LESSTHANOREQUAL(4),
	GREATERTHAN(5),
	GREATERTHANOREQUAL(6),
	LIKE(7),
	NOT_LIKE(8),
	BETWEEN(9),
	NOT_BETWEEN(10),
	IS_NULL(11),
	IS_NOT_NULL(12),
	IN(13), 
	NOT_IN(14);

private int opValue;

private RCQCompOperators(int i) {
	opValue=i;
}

public int getValue() {
	return opValue;
}
}
