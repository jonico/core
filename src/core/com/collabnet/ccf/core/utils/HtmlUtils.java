package com.collabnet.ccf.core.utils;
import net.htmlparser.jericho.Source;

/**
 * Provides utils used to do html to text conversion
 * @author jnicolai
 *
 */
public class HtmlUtils {
	/**
	 * Converts html into formatted plain text
	 * @param html html content
	 * @return formatted plain text
	 */
	public static String htmlToText(String html) {
		Source source=new Source(html);
		return source.getRenderer().toString();
	}
}