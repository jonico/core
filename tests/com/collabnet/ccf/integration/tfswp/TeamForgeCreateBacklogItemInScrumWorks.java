/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import com.collabnet.ce.soap50.webservices.cemain.TrackerFieldSoapDO;
import com.collabnet.teamforge.api.FieldValues;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;
import com.danube.scrumworks.api.client.types.ReleaseWSO;
import com.danube.scrumworks.api.client.types.ThemeWSO;

/**
 * Creates a backlog item in TeamForge and verifies the backlog item in ScrumWorks after the synchronization. 
 * 
 * @author jnicolai
 */
public class TeamForgeCreateBacklogItemInScrumWorks extends TFSWPIntegrationTest {

	/**
	 * Do the session clean up
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		cleanUpArtifacts(); 
		
		super.tearDown();
	}

	/**
	 * Creates a backlog item in TeamForge and verifies the backlog item in ScrumWorks after the synchronization.   
	 * Fields for the backlog item: title, description, benefit, penalty, effort, release, and product themes.  
	 * 
	 * @throws RemoteException if the test can not connect to the TeamForge API
	 * @throws InterruptedException if the thread can not sleep
	 */
	@Test
	public void testPBICreation() throws RemoteException, InterruptedException {
		final String title = "TFPBI";
		final String description = "TFPBIDescription";
		final String release = "Release 1.0";
		final String benefit = "10";
		final String penalty = "20";
		final String effort = "30";
		final String theme1 = "Core";
		final String theme2 = "GUI";

		// execute
		final Map<String, String> releaseToPlanningFolder = new HashMap<String, String>(); 
		releaseToPlanningFolder.put(release, "plan1164"); 
		
		final FieldValues flexFields = new FieldValues();
		flexFields.setNames(new String[] {"Benefit", "Penalty", "Backlog Effort", "Themes", "Themes"}); 
		flexFields.setValues(new String[] {benefit, penalty, effort, theme1, theme2} );
		final int flexFieldsLength = flexFields.getNames().length;
		final String[] flexFieldTypes = new String[flexFieldsLength];
		for (int i = 0; i < flexFieldsLength; i++) {
			flexFieldTypes[i] = TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING; 
		}
		flexFields.setTypes(flexFieldTypes); 
		
		getTFConnection().getTrackerClient().createArtifact(getTfPBITracker(),
				title, description, null, null, "Open", null, 0, 0,
				42, false, null, null, releaseToPlanningFolder.get(release), flexFields, null, null, null);
		
		// verify
		final ProductWSO product = getSWPEndpoint().getProductByName(getSwpProduct());
		ReleaseWSO[] releases = getSWPEndpoint().getReleases(product); 
		BacklogItemWSO[] pbis = null;
		for (int i = 0; i < getCcfMaxWaitTime(); i+=getCcfRetryInterval() ) {
			pbis = getSWPEndpoint().getActiveBacklogItems(product);
			if (pbis == null) {
				Thread.sleep(getCcfRetryInterval());
			} else {
				break;
			}
		}
		
		assertEquals(1, pbis.length);
		BacklogItemWSO pbi = pbis[0];
		assertEquals(title, pbi.getTitle());
		assertEquals(description, pbi.getDescription());
		assertEquals(benefit, pbi.getBusinessWeight().getBenefit().toString()); 
		assertEquals(penalty, pbi.getBusinessWeight().getPenalty().toString());
		assertEquals(effort, pbi.getEstimate().toString());
		final List<String> themeNames = new ArrayList<String>(); 
		final ThemeWSO[] themes = pbi.getThemes();
		assertEquals(2, themes.length);
		for (int i = 0; i < themes.length; i++) {
			themeNames.add(themes[i].getName()); 
		}
		assertTrue(themeNames.contains(theme1)); 
		assertTrue(themeNames.contains(theme2)); 

		String releaseName = null; 
		for (int i = 0; i < releases.length; i++) {
			if (releases[i].getId().equals(pbi.getReleaseId())) {
				releaseName = releases[i].getTitle(); 
			}
		}
		assertEquals(release, releaseName); 
	}
}
