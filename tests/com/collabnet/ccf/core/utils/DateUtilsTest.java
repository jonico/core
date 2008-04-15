package com.collabnet.ccf.core.utils;

import java.text.ParseException;
import java.util.Date;

import junit.framework.TestCase;

public class DateUtilsTest extends TestCase {

	public void testConvertDate() throws ParseException {
		Date now = new Date();
		System.out.println(now);
		Date convertedDate = DateUtil.convertDate(now, "GMT-8");
		System.out.println(convertedDate);
		assertEquals(now,convertedDate);
		
	}

}
