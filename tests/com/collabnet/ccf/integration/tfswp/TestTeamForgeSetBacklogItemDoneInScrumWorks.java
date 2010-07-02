package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api2.client.BacklogItem;

/**
 * Tests setting a backlog item to done in TeamForge and verifying the synchronization in ScrumWorks.   
 * 
 * @author Kelley
 *
 */
public class TestTeamForgeSetBacklogItemDoneInScrumWorks extends TFSWPIntegrationTest {
	private ArtifactRow backlogItemInTeamForge; 

	/**
	 * Creates a backlog item. 
	 */
	@Before
	public void setUp() throws Exception {
		final String title = "TFPBI";
		final String description = "TFPBIDescription";
		final String release = SWPTester.RELEASE_1;
		
		super.setUp();

		getSWPTester().createBacklogItem(title, description, null, null, null, release, Sprint.SPRINT_1_AUTOMATED_TEAM, null);  
		backlogItemInTeamForge = getTeamForgeTester().waitForBacklogItemToUpdate(title, 1);
	}
	
	/**
	 * Tests setting a backlog item to done in TeamForge and verifies the update in ScrumWorks.  
	 *  
	 * @throws Exception if an error occurs 
	 */
	@Test
	public void testUpdateBacklogItem() throws Exception {
		// execute
		final String revisedTitle = "done PBI";
		final ArtifactDO backlogItemInSprint = getTeamForgeTester().getArtifactDO(backlogItemInTeamForge.getId()); 
		backlogItemInSprint.setTitle(revisedTitle); 
		backlogItemInSprint.setStatus(TeamForgeTester.STATUS_DONE); 
		getTeamForgeTester().updateArtifact(backlogItemInSprint, "set status to done from test"); 

		// verify 
		BacklogItem doneDateUpdatedPbi = getSWPTester().waitForBacklogItemToUpdate(revisedTitle); 
		assertNotNull("Done date should have been set", doneDateUpdatedPbi.getCompletedDate()); 
	}
}
