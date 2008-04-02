package com.collabnet.ccf.pi.sfee;

import org.dom4j.Document;

import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
public interface IGAToArtifactConverter {
	public Object convert(Document dataObject, TrackerFieldSoapDO[] flexFields);
}
