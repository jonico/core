package com.collabnet.ccf.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactHelper;

public class DateUtil {
	private static final DateFormat dateFormat = GenericArtifactHelper.df;
	private static final SimpleDateFormat qcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Log log = LogFactory.getLog(DateUtil.class);
	public static final String GMT_TIME_ZONE_STRING = "GMT";
	
	/**
	 * Parses the QC specific date string into java.util.Date
	 * object
	 * @param dateString - The date String in QC format
	 * @return - The Date object parsed from the dateString
	 */
	public static Date parseQCDate(String dateString){
		return parse(dateString, qcDateFormat);
	}

	/**
	 * Formats the incoming java.util.Date object into the QC
	 * specific format.
	 * 
	 * @param date - The Date object to be formatted in QC format.
	 * @return the formatted date String
	 */
	public static String formatQCDate(Date date){
		return format(date, qcDateFormat);
	}

	/**
	 * Parses a date String that is in the GenericArtifact compatible format
	 * into a java.util.Date object.
	 * 
	 * @param dateString - The date String in the GenerciArtifact compatible format.
	 * @return The java.util.Date object parsed from the GenericArtifact compatible
	 * 				date format.
	 */
	public static Date parse(String dateString){
		return parse(dateString, dateFormat);
	}
	
	/**
	 * Formats the given java.util.Date object into the GenericArtifact compatible
	 * date format String
	 * 
	 * @param date - The java.util.Date object that is to be formatted.
	 * @return the formatted String in the GenericArtifact date format.
	 */
	public static String format(Date date){
		return format(date, dateFormat);
	}
	
	private static Date parse(String dateString, DateFormat dateFormat){
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			log.error("ParseException: "+e.getMessage()+"\nStacktrace: "+e.getStackTrace());
		}
		return null;
	}
	
	private static String format(Date date, DateFormat dateFormat){
		return dateFormat.format(date);
	}
	
	/**
	 * Converts the given Date object into another date object with the
	 * specified time zone information.
	 * 
	 * @param date - The date that is to be converted to a different time zone.
	 * @param toTimeZone - the time zone to which the date object should be converted.
	 * @return the new Date object in the specified time zone.
	 * @throws ParseException
	 */
	public static Date convertDate(Date date, String toTimeZone) throws ParseException{
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(toTimeZone));
		cal.setTime(date);
		DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss.SSS Z");
		df.setCalendar(cal);
		return df.parse(df.format(cal.getTime()));
	}
	
	public static boolean isAbsoluteDateInTimezone(Date date, String timeZone){
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		cal.setLenient(false);
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		int milliseconds = cal.get(Calendar.MILLISECOND);
		if(hour ==0 && minutes == 0 && seconds == 0 && milliseconds == 0){
			return true;
		}
		else {
			return false;
		}
	}

	public static Date convertToGMTAbsoluteDate(Date dateValue,
			String sourceSystemTimezone) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(sourceSystemTimezone));
		cal.setLenient(false);
		cal.setTime(dateValue);
		Calendar newCal = new GregorianCalendar(TimeZone.getTimeZone(GMT_TIME_ZONE_STRING));
		newCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),0,0,0);
		newCal.set(Calendar.MILLISECOND, 0);
		Date returnDate = newCal.getTime();
		return returnDate;
	}
	public static Date convertGMTToTimezoneAbsoluteDate(Date dateValue,
			String sourceSystemTimezone) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(GMT_TIME_ZONE_STRING));
		cal.setLenient(false);
		cal.setTime(dateValue);
		Calendar newCal = new GregorianCalendar(TimeZone.getTimeZone(sourceSystemTimezone));
		newCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),0,0,0);
		newCal.set(Calendar.MILLISECOND, 0);
		Date returnDate = newCal.getTime();
		return returnDate;
	}
}
