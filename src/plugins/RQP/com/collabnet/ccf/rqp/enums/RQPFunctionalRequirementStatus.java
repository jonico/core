package com.collabnet.ccf.rqp.enums;

/**
 * Contains all valid FNR status that will be synchronized to RQP
 * 
 * @author jcuello
 * 
 */
public enum RQPFunctionalRequirementStatus {

	APPROVED("Approved"), DEFERRED("Deferred"), REJECTED("Rejected");

	private String name;

	private RQPFunctionalRequirementStatus(String name) {
		this.name = name;
	}

	/**
	 * Establishes if a given status is inside of the enum values
	 * 
	 * @param status
	 * @return
	 */
	public static boolean contains(String status) {
		for (RQPFunctionalRequirementStatus functionalRequirementStatus : RQPFunctionalRequirementStatus.values()) {
			if (functionalRequirementStatus.name.equals(status)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}
}
