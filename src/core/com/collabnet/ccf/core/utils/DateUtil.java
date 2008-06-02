package com.collabnet.ccf.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.collabnet.ccf.core.ga.GenericArtifactHelper;

public class DateUtil {
	private static final DateFormat dateFormat = GenericArtifactHelper.df;
	private static final SimpleDateFormat qcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//Conversion of String into Date for QC
	public static Date parseQCDate(String dateString){
		return parse(dateString, qcDateFormat);
	}
	//Conversion of Date into String for QC
	public static String formatQCDate(Date date){
		return format(date, qcDateFormat);
	}

	public static Date parse(String dateString){
		return parse(dateString, dateFormat);
	}
	
	public static String format(Date date){
		return format(date, dateFormat);
	}
	
	public static Date parse(String dateString, DateFormat dateFormat){
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String format(Date date, DateFormat dateFormat){
		return dateFormat.format(date);
	}
	public static Date convertDate(Date date, String toTimeZone) throws ParseException{
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(toTimeZone));
		cal.setTime(date);
		DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss.SSS Z");
		df.setCalendar(cal);
		System.out.println(df.format(cal.getTime()));
		
		return df.parse(df.format(cal.getTime()));
	}
}
