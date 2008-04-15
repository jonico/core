package com.collabnet.ccf.pi.qc.v90;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;

public interface IQCDefect extends IBug {

	public GenericArtifact getGenericArtifact();
	public void setGenericArtifact(GenericArtifact genericArtifact);
	public GenericArtifact getGenericArtifactObject(IConnection qcc);
}