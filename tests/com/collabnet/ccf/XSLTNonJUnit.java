/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.openadaptor.util.FileUtils;

public class XSLTNonJUnit extends TestCase {
	private static String xsltFile = "C:/madhu-work/CCF/svn/ccf-qc/samples/QC-PT/bidirectionalMirroring/xslt/cu023+mseethar-test+10_2_1_114+QC_PLUGIN_TESTING-PRJ_4.xsl";
	private static String xmlFile = "C:/madhu-work/CCF/svn/ccf/temp.xml";
	private static Transformer transform;
	public static Document transform(Transformer transformer, Document d)
			throws TransformerException {
		DocumentSource source = new DocumentSource(d);
		DocumentResult result = new DocumentResult();
		// TODO: Allow the user to specify stylesheet parameters?
		transformer.transform(source, result);
		return result.getDocument();
	}
	private static void loadXSLT() {
		if (xsltFile == null)
			throw new RuntimeException("xsltFile property not set");

		// if the file doesn't exist try to get it via the classpath
		URL url = FileUtils.toURL(xsltFile);
		if (url == null)
			throw new RuntimeException("File not found: " + xsltFile);

		// load the transform
		try {
			// TODO Do not directly code the use of Saxon into the code?
			// TransformerFactory factory =
			// TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",
			// null);
			TransformerFactory factory = TransformerFactory.newInstance();
			transform = factory.newTransformer(new StreamSource(url.getPath()));

		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("Failed to load XSLT: "
					+ e.getMessage(), e);
		}
	}
	private static Document createDOMFromFile() {
		try {
			File file = new File(xmlFile);
			long size = file.length();
			byte[] byteArr = new byte[(int)size];
			FileInputStream is = new FileInputStream(xmlFile);
			is.read(byteArr);
			is.close();
			String xml = new String(byteArr);
			return DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			throw new RuntimeException("Failed to parse XML: "
					+ e.getMessage(), e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String args[]){
		loadXSLT();
		try {
			Document source = createDOMFromFile();
			Document doc = transform(transform, source);
			System.out.println(doc.asXML());
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
