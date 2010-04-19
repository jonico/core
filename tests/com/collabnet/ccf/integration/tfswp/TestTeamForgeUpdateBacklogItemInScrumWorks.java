package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.PlanningFolderRuleViolationException;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;

/**
 * Tests updating backlog item fields in TeamForge and verifying the synchronization in ScrumWorks.   
 * 
 * @author Kelley
 *
 */
public class TestTeamForgeUpdateBacklogItemInScrumWorks extends TFSWPIntegrationTest {
	private static final String[] names = new String[] {TeamForgeTester.FIELD_BENEFIT, 
		TeamForgeTester.FIELD_PENALTY, 
		TeamForgeTester.FIELD_EFFORT, 
		TeamForgeTester.FIELD_THEME, 
		TeamForgeTester.FIELD_THEME};

	private ArtifactDO backlogItemDO; 

	/**
	 * Creates a backlog item. 
	 */
	@Before
	public void setUp() throws Exception {
		final String title = "TFPBI";
		final String description = "TFPBIDescription";
		final String release = "Release 1.0";
		final String benefit = "10";
		final String penalty = "20";
		final String effort = "30";
		final String theme1 = "Core";
		final String theme2 = "GUI";

		super.setUp();

		// execute
		final FieldValues flexFields = getTeamForgeTester().convertToFlexField(names, 
				new String[] {benefit, penalty, effort, theme1, theme2}); 
		
		backlogItemDO = getTeamForgeTester().createBacklogItem(title, description, release, flexFields); 
	}
	
	/**
	 * Tests updating backlog item fields in TeamForge and verifies the update in ScrumWorks.  
	 * Fields for the backlog item: title, description, benefit, penalty, effort, release, and product themes.
	 *  
	 * @throws Exception if an error occurs 
	 */
	@Test
	public void testUpdateBacklogItem() throws Exception {
		// execute
		final String title = "updated TFPBI";
		final String description = "updated TFPBIDescription";
		final String release = "Release 2.0";
		final String benefit = "100";
		final String penalty = "200";
		final String effort = "300";
		final String theme1 = "DB";
		final String theme2 = "Documentation";
		final FieldValues flexFields = getTeamForgeTester().convertToFlexField(names, 
				new String[] {benefit, penalty, effort, theme1, theme2});
		
		getTeamForgeTester().updateBacklogItem(backlogItemDO.getId(), title, description, release, flexFields); 
		
		// verify 
		BacklogItemWSO[] allPbis = null;
		BacklogItemWSO updatedPbi = null; 
		getSWPTester().waitForBacklogItemToAppear(); 
		for (int i = 0; i < getCcfMaxWaitTime(); i += getCcfRetryInterval()) {
			allPbis = getSWPTester().getSWPEndpoint().getActiveBacklogItems(getSWPTester().getProduct()); 
			updatedPbi = allPbis[0];
			if (!updatedPbi.getTitle().equals(title)) {
				Thread.sleep(getCcfRetryInterval());
			} else {
				break;
			}
		}
		
		assertEquals(title, updatedPbi.getTitle()); 
		assertEquals(1, allPbis.length);
		assertEquals(title, updatedPbi.getTitle());
		assertEquals(description, updatedPbi.getDescription());
		assertEquals(benefit, updatedPbi.getBusinessWeight().getBenefit().toString()); 
		assertEquals(penalty, updatedPbi.getBusinessWeight().getPenalty().toString());
		assertEquals(effort, updatedPbi.getEstimate().toString());
		final List<String> themeNames = getSWPTester().getThemeNames(updatedPbi.getThemes()); 
		assertEquals(2, themeNames.size()); 
		assertTrue(themeNames.contains(theme1)); 
		assertTrue(themeNames.contains(theme2)); 
		assertEquals(release, getSWPTester().getReleaseForBacklogItem(updatedPbi.getReleaseId())); 
	}
}
