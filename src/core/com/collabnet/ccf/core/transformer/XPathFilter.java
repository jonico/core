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

	@SuppressWarnings("unchecked")
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
