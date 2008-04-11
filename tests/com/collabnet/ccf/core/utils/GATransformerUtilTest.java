package com.collabnet.ccf.core.utils;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public class GATransformerUtilTest extends TestCase {

	public void testStripHTML() throws IOException {
		Class thisClass = this.getClass();
		ClassLoader loader = thisClass.getClassLoader();
		InputStream is = loader.getResourceAsStream("com/collabnet/ccf/core/utils/sample.html");
		InputStream isHtmlStripped = loader.getResourceAsStream("com/collabnet/ccf/core/utils/html-stripped.txt");
		int readChar = -1;
		StringBuffer buffer = new StringBuffer();
		while((readChar = is.read()) != -1){
			buffer.append((char) readChar);
		}
		is.close();
		
		readChar = -1;
		StringBuffer bufferHtmlStripped = new StringBuffer();
		while((readChar = isHtmlStripped.read()) != -1){
			bufferHtmlStripped.append((char) readChar);
		}
		isHtmlStripped.close();
		String htmlStripped = bufferHtmlStripped.toString();
		GATransformerUtil util = new GATransformerUtil();
		String stripped = util.stripHTML(buffer.toString());
		assertEquals(htmlStripped.trim(), stripped.trim());
	}

}
