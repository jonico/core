/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.FieldValues;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;

/**
 * This class contains the tests from the TF to SWP
 * 
 * @author jnicolai
 * 
 */
public class TF2SWPTests extends TFSWPIntegrationTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Do the session clean up
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testPBICreation() throws RemoteException, InterruptedException {
		// create a tracker item in the TF PBI tracker, sleep some seconds
		// and look whether a PBI with the same name had been created in SWP as
		// well
		cleanUpArtifacts();
		FieldValues flexFields = new FieldValues();
		getTFConnection().getTrackerClient().createArtifact(getTfPBITracker(),
				"TFPBI", "TFPBIDescription", null, null, "Open", null, 0, 0,
				42, false, null, null, null, flexFields, null, null, null);
		Thread.sleep(1000);
		ProductWSO product = getSWPEndpoint().getProductByName(getSwpProduct());
		BacklogItemWSO[] pbis = getSWPEndpoint().getActiveBacklogItems(product);
		assertEquals(1, pbis.length);
		BacklogItemWSO pbi = pbis[0];
		assertEquals("TFPBI", pbi.getTitle());
		assertEquals("TFPBIDescription", pbi.getDescription());
	}
}
