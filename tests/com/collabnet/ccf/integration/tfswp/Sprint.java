package com.collabnet.ccf.integration.tfswp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Sprints in ScrumWorks used for testing purposes. 
 * 
 * @author Kelley
 *
 */

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
	SPRINT_1_ONE_TEAM ("Initial sprint for OneTeam", "4/15/2010", "6/11/2010", "OneTeam"); 

	/** Date format of M/D/YYYY. */ 
	private final SimpleDateFormat dateFormatMonthDayYear = new SimpleDateFormat("M/d/yyyy");
	
	/** Date format of YYYY-MM-DD. */ 
	private final SimpleDateFormat dateFormatYearMonthYear = new SimpleDateFormat("yyyy-MM-dd"); 
	
	/** The team associated to the sprint. */ 
	private String team; 
	
	/** The name of the sprint. */ 
	private String name; 
	
	/** The start date of the sprint. */ 
	private Calendar start; 
	
	/** The end date of the sprint. */ 
	private Calendar end; 
	
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
		this.start = convertStringToDate(start); 
		this.end = convertStringToDate(end);
		this.team = team; 
	}

	/**
	 * Converts a string formatted as M/D/YYYY to a Calendar. 
	 * 
	 * @param dateString the date string to be converted
	 * @return the Calendar 
	 */
	private Calendar convertStringToDate(final String dateString) {
		final String[] splitDateString = dateString.split("/"); 
		final int startMonth = Integer.parseInt(splitDateString[0]) - 1; 
		final int startDay = Integer.parseInt(splitDateString[1]); 
		final int startYear = Integer.parseInt(splitDateString[2]); 
		Calendar date = new GregorianCalendar(startYear, startMonth, startDay);
		return date;
	}
	
	/**
	 * @return the name 
	 */
	public String getName() {
		return name; 
	}
	
	/**
	 * @return the start date in M/D/YYYY format without leading 0 for single digit month or day
	 */
	public String getStartDate() {
		return dateFormatMonthDayYear.format(start.getTime()); 
	}
	
	/**
	 * @return the start date in M/D/YYYY format with two digits for month and day
	 */
	public String getStartDateAsTwoDigitMonthAndDate() {
		return dateFormatYearMonthYear.format(start.getTime());
	}
	
	/**
	 * @return the end date in YYYY-MM-DD format without leading 0 for single digit month or day
	 */
	public String getEndDate() {
		return dateFormatMonthDayYear.format(end.getTime());
	}
	
	/**
	 * @return the end date in YYYY-MM-DD format with two digits for month and day 
	 */
	public String getEndDateAsTwoDigitMonthAndDate() {
		return dateFormatYearMonthYear.format(end.getTime()); 
	}

	/**
	 * @return the team associated to the sprint
	 */
	public String getTeam() {
		return team; 
	}
}
