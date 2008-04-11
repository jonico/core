package com.collabnet.ccf.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtils {
	public static Date convertDate(Date date, String toTimeZone) throws ParseException{
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(toTimeZone));
		cal.setTime(date);
		DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		df.setCalendar(cal);
		System.out.println(df.format(cal.getTime()));
		
		return df.parse(df.format(cal.getTime()));
	}
}
