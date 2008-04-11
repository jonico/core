package com.collabnet.ccf.pi.sfee;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
public interface IArtifactToGAConverter {
	public GenericArtifact convert(Object dataObject, TrackerFieldSoapDO[] trackerFields);
}
