package com.collabnet.ccf.core.transformer;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.openadaptor.core.IDataProcessor;

public class XPathFilter implements IDataProcessor {
	private String xPath = null;
	private String value = null;
	public Object[] process(Object data) {
		if(data instanceof Document){
			Document document = (Document) data;
			Node node = document.selectSingleNode(xPath);
			if(node != null){
				return new Object[]{data};
			}
			else{
				return null;
			}
		}
		return null;
	}

	public void reset(Object context) {
		// TODO Auto-generated method stub
		
	}

	public void validate(List exceptions) {
		// TODO Auto-generated method stub
		
	}

	public String getXPath() {
		return xPath;
	}

	public void setXPath(String path) {
		xPath = path;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
