package com.collabnet.ccf.core.utils;

/**
 * This class is a utility class that provides methods
 * for String manipulations in the XSLT transformers.
 * 
 * @author Madhusuthanan Seetharam (madhusuthanan@collab.net)
 *
 */
public class GATransformerUtil {
	/**
	 * This method strips all the HTML tags present int the original
	 * String that is passed.
	 * 
	 * @param original - the String that is to be stripped off the
	 * HTML tags present in it.
	 * @return the original String with all the HTML tags stripped off.
	 */
	public static String stripHTML(String original){
		return StringUtils.stripHTML(original);
	}
	
	/**
	 * Trims off the leading and trailing white spaces of the String
	 * that is passed in.
	 * 
	 * @param stringToTrim - The String to be trimmed
	 * @return the trimmed String.
	 */
	public static String trim(String stringToTrim){
		return stringToTrim.trim();
	}
}
