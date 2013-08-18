package com.collabnet.ccf.rcq;

public final class RCQPluginVersion {

	private static final String major = "1";
	private static final String minor = "1";
	private static final String revision = "6";
	private static final String candidate = "2";
	private static final String sep = ".";
	
	
	public static final String current() {
		return major + sep + minor + sep + revision + " RC" + candidate;
	}
}
