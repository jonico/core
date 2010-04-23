package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;

/**
 * Tests that a backlog item updated in ScrumWorks is correctly synched in TeamForge.
 *  
 * @author Kelley
 */
public class TestScrumWorksUpdateBacklogItemInTeamForge extends TFSWPIntegrationTest {
	private BacklogItemWSO scrumWorksBacklogItem; 

	@Override
	@Before
	public void setUp() throws Exception {
		final String name = "pbi";
		final String description = "description";
		final String effort = "10";
		final String benefit = "20";
		final String penalty = "30";
		final String release = "Release 1.0";

		super.setUp();
		
		scrumWorksBacklogItem = getSWPTester().createBacklogItem(name, description, effort, benefit, penalty, release, Sprint.SPRINT_1_AUTOMATED_TEAM, null);
		getSWPTester().waitForBacklogItemsToAppear(1); 
	}
	
	/**
	 * Tests updating backlog item fields in TeamForge and verifies the update in ScrumWorks. 
	 * Fields for the backlog item: title, description, benefit, penalty, effort, release, and product themes.
	 * 
	 * @throws Exception if an error occurs 
	 */
	@Test
	public void updateBacklogItemInScrumWorks() throws Exception {
		final String title = "updated TFPBI";
		final String description = "updated TFPBIDescription";
		final String release = "Release 2.0";
		final String benefit = "100";
		final String penalty = "200";
		final String effort = "300";
		final String theme1 = "DB";
		final String theme2 = "Documentation";
		final Sprint sprint = Sprint.SPRINT_1_ONE_TEAM; 
		
		// execute
		BacklogItemWSO backlogItemToBeUpdated = new BacklogItemWSO(true, 
				scrumWorksBacklogItem.getBacklogItemId(), 
				getSWPTester().transformToBusinessWeightWSO(benefit, penalty), 
				null, 
				description, 
				Integer.parseInt(effort), 
				scrumWorksBacklogItem.getKey(), 
				scrumWorksBacklogItem.getProductId(), 
				scrumWorksBacklogItem.getRank(), 
				getSWPTester().getReleaseId(release), 
				getSWPTester().getSprintId(sprint.getName()), 
				null, 
				title);  
		final BacklogItemWSO scrumWorksPbiFromUpdate = getSWPTester().updateBacklogItem(backlogItemToBeUpdated); 

		// verify
		ArtifactRow teamForgePbi = waitForBacklogItemToUpdate(title);
		 
		assertEquals(title, teamForgePbi.getTitle()); 
		assertEquals(description, teamForgePbi.getDescription());
		assertEquals(getTeamForgeTester().getPlanningFolderId(release), teamForgePbi.getPlanningFolderId()); 
		
		assertEquals(TeamForgeTester.STATUS_OPEN, teamForgePbi.getStatus());
		assertEquals("", teamForgePbi.getCategory()); 
		assertEquals(0, teamForgePbi.getPriority()); 
		assertEquals(TeamForgeTester.NONE, teamForgePbi.getAssignedToFullname()); 
		assertTrue(teamForgePbi.getAutosumming()); 
		assertEquals(0, teamForgePbi.getRemainingEffort());
		
		final String artifactId = teamForgePbi.getId();
		assertEquals(Arrays.asList(benefit, 
				penalty, 
				effort, 
				scrumWorksPbiFromUpdate.getKey(),
				sprint.getTeam(),
				sprint.getName(),
//				Sprint.SPRINT_1_AUTOMATED_TEAM.getStartDate(), 
//				Sprint.SPRINT_1_AUTOMATED_TEAM.getEndDate(),
				null), 
				getTeamForgeTester().getFieldValues(artifactId, 
						TeamForgeTester.FIELD_BENEFIT, 
						TeamForgeTester.FIELD_PENALTY, 
						TeamForgeTester.FIELD_EFFORT, 
						TeamForgeTester.FIELD_KEY, 
						TeamForgeTester.FIELD_TEAM,
						TeamForgeTester.FIELD_SPRINT_NAME,
//						TeamForgeTester.FIELD_SPRINT_START, 
//						TeamForgeTester.FIELD_SPRINT_END, 
						TeamForgeTester.FIELD_THEME));
		
	}

	/**
	 * Returns the updated backlog item from TeamForge after the backlog item has been updated. 
	 * 
	 * @param title the updated title
	 * @return the updated backlog item
	 * @throws Exception if an error occurs 
	 */
	private ArtifactRow waitForBacklogItemToUpdate(final String title) throws Exception {
		ArtifactRow teamForgePbi = null; 
		for (int i = 0; i < getCcfMaxWaitTime(); i += getCcfRetryInterval()) {
			ArtifactRow[] artifacts = getTeamForgeTester().waitForBacklogItemsToAppear(1);
			teamForgePbi = artifacts[0]; 
			if (!teamForgePbi.getTitle().equals(title)) {
				artifacts = getTeamForgeTester().waitForBacklogItemsToAppear(1);
				teamForgePbi = artifacts[0]; 
				Thread.sleep(getCcfRetryInterval()); 
			} else {
				return teamForgePbi; 
			}
		}
		throw new RuntimeException("Backlog item was not updated in the time: " + getCcfMaxWaitTime()); 
	}
}
