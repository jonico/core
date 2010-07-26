package com.collabnet.ccf.core.utils;
import net.htmlparser.jericho.Source;

/**
 * Provides utils used to do html to text conversion
 * @author jnicolai
 *
 */
public class JerichoUtils implements FormatterProxy {
	/**
	 * Converts html into formatted plain text
	 * @param html html content
	 * @return formatted plain text
	 */
	public static String htmlToText(String html) {
		if (html == null) {
			return "";
		}
		Source source=new Source(html);
		return source.getRenderer().toString();
	}

	public String convertHtmlToText(String original) {
		return htmlToText(original);
	}

	public String convertTextToHtml(String html) {
		return StringUtils.encodeHTMLToEntityReferences(html);
	}

	public String trimString(String stringToTrim) {
		return stringToTrim.trim();
	}
}