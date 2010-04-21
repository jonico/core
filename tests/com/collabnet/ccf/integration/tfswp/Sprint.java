package com.collabnet.ccf.integration.tfswp;

public enum Sprint {
	/**
	 * First sprint for the automated test team.  
	 */
	SPRINT_1_AUTOMATED_TEAM ("Initial test sprint for AutomatedTestTeam", "4/15/2010", "5/12/2010", "AutomatedTestTeam"),
	
	/**
	 * Second sprint for the automated test team.  
	 */
	SPRINT_2_AUTOMATED_TEAM("Second test sprint", "4/15/2010", "6/18/2010", "AutomatedTestTeam"),  
	
	/**
	 * First sprint for the one team. 
	 */
	SPRINT_1_ONE_TEAM ("Initial sprint for OneTeam", "4/14/2010", "6/11/2010", "OneTeam"); 
	
	/** The team associated to the sprint. */ 
	private String team; 
	
	/** The name of the sprint. */ 
	private String name; 
	
	/** The start date of the sprint. */ 
	private String start; 
	
	/** The end date of the sprint. */ 
	private String end; 
	
	/**
	 * Constructs a new {@link Sprint} with the given string representation. 
	 * 
	 * @param name the name of this sprint 
	 * @param start the start date of this sprint 
	 * @param end the end date of this sprint
	 * @param team the name of the team associated to this sprint
	 */
	private Sprint(final String name, final String start, final String end, final String team) {
		assert start != null : "null start date"; 
		assert end != null : "null end date"; 
		assert team != null : "null team"; 
		
		this.name = name; 
		this.start = start; 
		this.end = end;
		this.team = team; 
	}
	
	/**
	 * @return the name 
	 */
	public String getName() {
		return name; 
	}
	
	/**
	 * @return the start date in XX/XX/XXXX format
	 */
	public String getStartDate() {
		return start; 
	}
	
	/**
	 * @return the end date in XX/XX/XXXX format
	 */
	public String getEndDate() {
		return end; 
	}
	
	/**
	 * @return the team associated to the sprint
	 */
	public String getTeam() {
		return team; 
	}
}
