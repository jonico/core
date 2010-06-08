package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.ccf.swp.SWPMetaData;
import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api2.client.BacklogItem;

/**
 * Tests that a backlog item updated in ScrumWorks is correctly synched in
 * TeamForge.
 * 
 * @author Kelley
 */
public class TestScrumWorksUpdateBacklogItemInTeamForge extends
		TFSWPIntegrationTest {
	private BacklogItem scrumWorksBacklogItem;

	@Override
	@Before
	public void setUp() throws Exception {
		final String name = "pbi";
		final String description = "description";
		final String effort = "10";
		final String benefit = "20";
		final String penalty = "30";
		final String release = SWPTester.RELEASE_1;
		final String theme = SWPTester.THEME_GUI;

		super.setUp();

		scrumWorksBacklogItem = getSWPTester().createBacklogItem(name,
				description, effort, benefit, penalty, release,
				Sprint.SPRINT_1_AUTOMATED_TEAM, theme);
		getSWPTester().waitForBacklogItemsToAppear(1);
	}

	/**
	 * Tests updating backlog item fields in TeamForge and verifies the update
	 * in ScrumWorks. Fields for the backlog item: title, description, benefit,
	 * penalty, effort, release, and product themes.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void updateBacklogItemInScrumWorks() throws Exception {
		final String title = "updated TFPBI";
		final String description = "updated TFPBIDescription";
		final String release = SWPTester.RELEASE_2;
		final String benefit = "100";
		final String penalty = "200";
		final String effort = "300";
		final String theme2 = SWPTester.THEME_DOCUMENTATION;
		final String theme1 = SWPTester.THEME_DB;
		final Sprint sprint = Sprint.SPRINT_1_ONE_TEAM;

		// execute
		BacklogItem backlogItem = new BacklogItem();
		backlogItem.setActive(true);
		backlogItem.setId(scrumWorksBacklogItem.getId());
		backlogItem.setBusinessWeight(getSWPTester()
				.transformToBusinessWeightWSO(benefit, penalty));
		backlogItem.setDescription(description);
		backlogItem.setEstimate(Integer.parseInt(effort));
		backlogItem.setKey(scrumWorksBacklogItem.getKey());
		backlogItem.setProductId(scrumWorksBacklogItem.getProductId());
		backlogItem.setRank(scrumWorksBacklogItem.getRank());
		backlogItem.setReleaseId(getSWPTester().getReleaseId(release));
		backlogItem.setSprintId(getSWPTester().getSprintId(sprint.getName()));
		backlogItem.setName(title);
		Calendar today = new GregorianCalendar();
		GregorianCalendar doneDate = new GregorianCalendar(today
				.get(Calendar.YEAR), today.get(Calendar.MONTH), today
				.get(Calendar.DAY_OF_MONTH));
		backlogItem.setCompletedDate(doneDate.getTime());
		backlogItem.setThemes(getSWPTester().transformToThemeWSO(theme1, theme2)); 
		final BacklogItem scrumWorksPbiFromUpdate = getSWPTester()
				.updateBacklogItem(backlogItem);

		// verify
		ArtifactRow teamForgePbi = getTeamForgeTester()
				.waitForBacklogItemToUpdate(title, 1);

		assertEquals(title, teamForgePbi.getTitle());
		assertEquals(description, teamForgePbi.getDescription());
		assertEquals(getTeamForgeTester().getPlanningFolderId(release),
				teamForgePbi.getPlanningFolderId());

		assertEquals(TeamForgeTester.STATUS_DONE, teamForgePbi.getStatus());
		assertEquals("", teamForgePbi.getCategory());
		assertEquals(0, teamForgePbi.getPriority());
		assertEquals(TeamForgeTester.NONE, teamForgePbi.getAssignedToFullname());
		assertTrue(teamForgePbi.getAutosumming());
		assertEquals(0, teamForgePbi.getRemainingEffort());

		final String artifactId = teamForgePbi.getId();
		assertEquals(Arrays.asList(benefit, penalty, effort,
				scrumWorksPbiFromUpdate.getKey(), SWPMetaData
						.getTeamSprintStringRepresentation(sprint.getName(),
								sprint.getStartDateAsTwoDigitMonthAndDate(), 
								sprint.getEndDateAsTwoDigitMonthAndDate(),
								sprint.getTeam()), sprint.getStartDate(),
				sprint.getEndDate(), theme1, theme2), getTeamForgeTester()
				.getFieldValues(artifactId, TeamForgeTester.FIELD_BENEFIT,
						TeamForgeTester.FIELD_PENALTY,
						TeamForgeTester.FIELD_EFFORT,
						TeamForgeTester.FIELD_KEY,
						TeamForgeTester.FIELD_TEAM_SPRINT_NAME,
						TeamForgeTester.FIELD_SPRINT_START,
						TeamForgeTester.FIELD_SPRINT_END,
						TeamForgeTester.FIELD_THEME,
						TeamForgeTester.FIELD_THEME));

	}
}
