/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.collabnet.teamforge.api.FieldValues;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;

/**
 * Creates a backlog item in TeamForge and verifies the backlog item in
 * ScrumWorks after the synchronization.
 * 
 * @author jnicolai
 */
public class TestTeamForgeCreateBacklogItemInScrumWorks extends
		TFSWPIntegrationTest {

	/**
	 * Creates a backlog item in TeamForge and verifies the backlog item in
	 * ScrumWorks after the synchronization. Fields for the backlog item: title,
	 * description, benefit, penalty, effort, release, and product themes.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void testPBICreation() throws Exception {
		final String title = "TFPBI";
		final String description = "TFPBIDescription";
		final String release = "Release 1.0";
		final String benefit = "10";
		final String penalty = "20";
		final String effort = "30";
		final String theme1 = "Core";
		final String theme2 = "GUI";
		// and now some bogus values that should be ignored
		String bogusKey = "bogusKey";
		String bogusSprint = "bogusSprint";
		String bogusTeam = "bogusTeam";

		// execute
		final FieldValues flexFields = getTeamForgeTester().convertToFlexField(
				new String[] { TeamForgeTester.FIELD_BENEFIT,
						TeamForgeTester.FIELD_PENALTY,
						TeamForgeTester.FIELD_EFFORT,
						TeamForgeTester.FIELD_THEME,
						TeamForgeTester.FIELD_THEME, TeamForgeTester.FIELD_KEY,
						TeamForgeTester.FIELD_TEAM,
						TeamForgeTester.FIELD_SPRINT_NAME },
				new String[] { benefit, penalty, effort, theme1, theme2,
						bogusKey, bogusTeam, bogusSprint });

		getTeamForgeTester().createBacklogItem(title, description, release,
				flexFields);

		// verify
		final ProductWSO product = getSWPTester().getProduct();
		BacklogItemWSO[] pbis = getSWPTester().waitForBacklogItemsToAppear(1);

		assertEquals(1, pbis.length);
		BacklogItemWSO pbi = pbis[0];
		assertEquals(title, pbi.getTitle());
		assertEquals(description, pbi.getDescription());
		assertEquals(benefit, pbi.getBusinessWeight().getBenefit().toString());
		assertEquals(penalty, pbi.getBusinessWeight().getPenalty().toString());
		assertEquals(effort, pbi.getEstimate().toString());

		// check whether bogus values have been transported
		assertNotSame(bogusKey, pbi.getKey());
		assertNull(pbi.getSprintId());

		final List<String> themeNames = getSWPTester().getThemeNames(
				pbi.getThemes());
		assertEquals(2, themeNames.size());
		assertTrue(themeNames.contains(theme1));
		assertTrue(themeNames.contains(theme2));
		assertEquals(release, getSWPTester().getReleaseName(pbi.getReleaseId()));
	}
}
