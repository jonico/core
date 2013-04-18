package com.collabnet.ccf.rqp.enums;

/**
 * Contains all field types used in RequisitePro
 * 
 * @author jcuello
 * 
 */
public enum RQPRequirementField {

	NAME("Name"), TEXT("Text"), COMMENTS("Comments"), REQPRO_ID("ReqPro ID"), STATUS("Status"), ID("ID"), BUSINESS_VALUE(
			"Business Value"), DESCRIPTION("Description");

	private String name;

	private RQPRequirementField(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
