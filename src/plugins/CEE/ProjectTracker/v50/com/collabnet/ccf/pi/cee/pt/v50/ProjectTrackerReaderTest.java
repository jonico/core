package com.collabnet.ccf.pi.cee.pt.v50;

import junit.framework.TestCase;

public class ProjectTrackerReaderTest extends TestCase {

	public void testGetChangedArtifacts() {
		ProjectTrackerReader reader = new ProjectTrackerReader();
		reader.getChangedArtifacts(null);
	}

}
