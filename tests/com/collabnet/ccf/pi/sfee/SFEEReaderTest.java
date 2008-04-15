package com.collabnet.ccf.pi.sfee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import com.collabnet.ccf.pi.sfee.v44.SFEEReader;
import com.collabnet.ccf.pi.sfee.v44.SFEEToGenericArtifactConverter;


public class SFEEReaderTest extends TestCase {
	SFEEReader sfeeReader = null;
	String username="mseethar";
    String password="password";
    String serverUrl="http://cu074.cubit.maa.collab.net:8080";
    String keepAlive = "true";
    SFEEToGenericArtifactConverter artifactConverter = new SFEEToGenericArtifactConverter();
	public void setUp() throws Exception {
		super.setUp();
		sfeeReader = new SFEEReader();
		sfeeReader.setUsername(username);
		sfeeReader.setPassword(password);
		sfeeReader.setServerUrl(serverUrl);
		sfeeReader.setKeepAlive(keepAlive);
		sfeeReader.setArtifactConverter(artifactConverter);
		sfeeReader.validate(new ArrayList());
	}

	public void testReadTrackerItems(){
		String projectTracker = "tracker1001";
		//"Mon Nov 05 00:00:00 GMT+05:30 2007"
		Date lastModifiedDate = new Date(2007-1900,10,05,0,0,0);
		String lastArtifactId = "artf1001";
		int lastArtifactVersion = -1;
		boolean firstTimeImport = false;
		try {
			sfeeReader.connect();
			sfeeReader.readTrackerItems(projectTracker, lastModifiedDate, lastArtifactId, lastArtifactVersion, firstTimeImport, null);
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
