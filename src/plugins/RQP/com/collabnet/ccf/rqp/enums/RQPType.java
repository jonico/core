package com.collabnet.ccf.rqp.enums;

/**
 * Contains all RequisitePro requirement types currently synchronized
 * 
 * @author jcuello
 * 
 */
public enum RQPType {
	USER_STORY("URS"), FUNCTIONAL_REQUIREMENT("FNR"),PACKAGE("PACKAGE");

	private String name;

	private RQPType(String name) {
		this.name = name;
	}

	/**
	 * Retrieves the enum type belonging to the name given as parameter, if
	 * exists
	 * 
	 * @param name
	 * @return
	 */
	public static RQPType getRQPTypeByName(String name) {
		for (RQPType type : RQPType.values()) {
			if (type.name.equals(name)) {
				return type;
			}
		}
		return null;
	}

	/**
	 * Establishes if a given type name is inside of the enum values
	 * 
	 * @param rqpType
	 * @return
	 */
	public static boolean contains(RQPType rqpType) {
		for (RQPType type : RQPType.values()) {
			if (type.equals(rqpType)) {
				return true;
			}
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public static boolean isRequirement(RQPType rqpType){
		return RQPType.USER_STORY.equals(rqpType) || RQPType.FUNCTIONAL_REQUIREMENT.equals(rqpType);
	}

}
