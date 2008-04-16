package com.collabnet.ccf.pi.sfee.v44;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
public interface IGAToArtifactConverter {
	public Object convert(GenericArtifact dataObject, TrackerFieldSoapDO[] flexFields);
}
