package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.Comment;

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
		TeamForgeTester.FIELD_THEME,
		TeamForgeTester.FIELD_KEY,
		TeamForgeTester.FIELD_TEAM_SPRINT_NAME};

	private ArtifactDO backlogItemDO; 

	/**
	 * Creates a backlog item. 
	 */
	@Before
	public void setUp() throws Exception {
		final String title = "TFPBI";
		final String description = "TFPBIDescription";
		final String release = SWPTester.RELEASE_1;
		final String benefit = "10";
		final String penalty = "20";
		final String effort = "30";
		final String theme1 = SWPTester.THEME_CORE;
		final String theme2 = SWPTester.THEME_GUI;
		
		super.setUp();

		// execute, do not set bogus values here
		final FieldValues flexFields = getTeamForgeTester().convertToFlexField(names, 
				new String[] {benefit, penalty, effort, theme1, theme2, null, null}); 
		
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
		getSWPTester().waitForBacklogItemsToAppear(1);
		// execute
		final String title = "updated TFPBI";
		final String description = "updated TFPBIDescription";
		final String release = SWPTester.RELEASE_2;
		final String benefit = "100";
		final String penalty = "200";
		final String effort = "300";
		final String theme1 = SWPTester.THEME_DB;
		final String theme2 = SWPTester.THEME_DOCUMENTATION;
		// and now some bogus values that should be ignored
		String bogusKey = "bogusKey";
		
		final FieldValues flexFields = getTeamForgeTester().convertToFlexField(names, 
				new String[] {benefit, penalty, effort, theme1, theme2, bogusKey, null});
		
		getTeamForgeTester().updateBacklogItem(backlogItemDO.getId(), title, description, release, null, flexFields); 
		
		// verify 
		BacklogItem updatedPbi = getSWPTester().waitForBacklogItemToUpdate(title); 
		
		assertEquals(title, updatedPbi.getName()); 
		assertEquals(title, updatedPbi.getName());
		assertEquals(description, updatedPbi.getDescription());
		assertEquals(benefit, updatedPbi.getBusinessWeight().getBenefit().toString()); 
		assertEquals(penalty, updatedPbi.getBusinessWeight().getPenalty().toString());
		assertEquals(effort, updatedPbi.getEstimate().toString());
		final List<String> themeNames = getSWPTester().getThemeNames(updatedPbi.getThemes()); 
		assertEquals(2, themeNames.size()); 
		assertTrue(themeNames.contains(theme1)); 
		assertTrue(themeNames.contains(theme2)); 
		assertEquals(release, getSWPTester().getReleaseName(updatedPbi.getReleaseId()));
		
		// check whether bogus values have been transported
		assertNotSame(bogusKey, updatedPbi.getKey());
		assertNull(updatedPbi.getSprintId());
		
		// now we check whether comments have been transported
		List<Comment> comments = getSWPTester().getSWPEndpoint().getCommentsForBacklogItem(updatedPbi.getId());
		assertEquals(1, comments.size());
		for (Comment comment : comments) {
			assertTrue(comment.getText().contains("updating pbi ..."));
		} 
		
		final String updatedSprintTitle = "updatedSprintTitle";
		updatedPbi.setSprintId(getSWPTester().getSprintId(Sprint.SPRINT_1_AUTOMATED_TEAM.getName()));
		updatedPbi.setName(updatedSprintTitle);
		getSWPTester().updateBacklogItem(updatedPbi);
		getTeamForgeTester().waitForBacklogItemToUpdate(updatedSprintTitle, 1);
		
		// execute
		final String revisedTitle = "done PBI"; 
		getTeamForgeTester().updateBacklogItem(backlogItemDO.getId(), revisedTitle, description, release, TeamForgeTester.STATUS_DONE, flexFields); 

		// verify 
		BacklogItem doneDateUpdatedPbi = getSWPTester().waitForBacklogItemToUpdate(revisedTitle); 
		assertNotNull("Done date should have been set", doneDateUpdatedPbi.getCompletedDate()); 
	}
}
