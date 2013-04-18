package com.collabnet.ccf.rqp.enums;

/**
 * Contains all valid USR status that will be synchronized to RQP
 * 
 * @author jcuello
 * 
 */
public enum RQPUserStoryStatus {

	APPROVED("Approved"), PENDING("Pending"), REJECTED("Rejected");

	private String name;

	private RQPUserStoryStatus(String name) {
		this.name = name;
	}

	/**
	 * Establishes if a given status is inside of the enum values
	 * 
	 * @param status
	 * @return
	 */
	public static boolean contains(String status) {
		for (RQPUserStoryStatus userStoryStatus : RQPUserStoryStatus.values()) {
			if (userStoryStatus.name.equals(status)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}
}
