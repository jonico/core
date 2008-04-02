package com.collabnet.ccf.pi.sfee;

import com.collabnet.ccf.core.ga.GenericArtifact;
public interface IArtifactToGAConverter {
	public GenericArtifact convert(Object dataObject);
}
