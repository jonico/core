package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.collabnet.ccf.swp.SWPMetaData;
import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api2.client.BacklogItem;

/**
 * Tests that a backlog item created in ScrumWorks is correctly synched in
 * TeamForge. Tests that a backlog item deleted in ScrumWorks is correctly
 * synched in TeamForge.
 * 
 * @author Kelley
 * 
 */

public class TestScrumWorksCreateAndDeleteBacklogItemInTeamForge extends
		TFSWPIntegrationTest {

	/**
	 * Tests creating a backlog item in ScrumWorks and synchronizing to
	 * TeamForge. The backlog item contains the following fields:
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void testBacklogItemCreationAndDeletionInScrumWorksToTeamForge()
			throws Exception {
		// execute
		final String title = "ScrumWorks Backlog Item";
		final String description = "ScrumWorks Backlog Item Description";
		final String release = SWPTester.RELEASE_1;
		final String benefit = "10";
		final String penalty = "20";
		final String effort = "30";
		final String theme1 = SWPTester.THEME_CORE;
		final String theme2 = SWPTester.THEME_GUI;
		Sprint sprint = Sprint.SPRINT_1_AUTOMATED_TEAM;

		BacklogItem pbiInScrumWorks = getSWPTester().createBacklogItem(title,
				description, effort, benefit, penalty, release, sprint, theme1,
				theme2);

		// verify
		final ArtifactRow[] artifacts = getTeamForgeTester()
				.waitForBacklogItemsToAppear(1);
		final ArtifactRow pbiInTeamForge = artifacts[0];
		assertEquals(title, pbiInTeamForge.getTitle());
		assertEquals(description, pbiInTeamForge.getDescription());
		assertEquals(getTeamForgeTester().getPlanningFolderId(release),
				pbiInTeamForge.getPlanningFolderId());

		assertEquals(TeamForgeTester.STATUS_OPEN, pbiInTeamForge.getStatus());
		assertEquals("", pbiInTeamForge.getCategory());
		assertEquals(0, pbiInTeamForge.getPriority());
		assertEquals(TeamForgeTester.NONE, pbiInTeamForge
				.getAssignedToFullname());
		assertTrue(pbiInTeamForge.getAutosumming());
		assertEquals(0, pbiInTeamForge.getRemainingEffort());

		final String artifactId = pbiInTeamForge.getId();
		assertEquals(Arrays.asList(benefit, penalty, effort, pbiInScrumWorks
				.getKey(), SWPMetaData.getTeamSprintStringRepresentation(sprint
				.getName(), sprint.getStartDateAsTwoDigitMonthAndDate(), 
				sprint.getEndDateAsTwoDigitMonthAndDate(), sprint
				.getTeam()), sprint.getStartDate(), sprint.getEndDate(),
				theme1, theme2), getTeamForgeTester().getFieldValues(
				artifactId, TeamForgeTester.FIELD_BENEFIT,
				TeamForgeTester.FIELD_PENALTY, TeamForgeTester.FIELD_EFFORT,
				TeamForgeTester.FIELD_KEY,
				TeamForgeTester.FIELD_TEAM_SPRINT_NAME,
				TeamForgeTester.FIELD_SPRINT_START,
				TeamForgeTester.FIELD_SPRINT_END, TeamForgeTester.FIELD_THEME,
				TeamForgeTester.FIELD_THEME));

		// now delete PBI again
		getSWPTester().deleteAllPBIsInSWP();

		// wait that PBI disappears in TF
		getTeamForgeTester().waitForBacklogItemsToDisappear(0);
	}
}
