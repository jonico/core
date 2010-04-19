/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
public class TestTeamForgeCreateBacklogItemInScrumWorks extends TFSWPIntegrationTest {

	/**
	 * Creates a backlog item in TeamForge and verifies the backlog item in ScrumWorks after the synchronization.   
	 * Fields for the backlog item: title, description, benefit, penalty, effort, release, and product themes.  
	 * 
	 * @throws Exception if an error occurs
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

		// execute
		final String[][] test = new String[2][4]; 
		test[0] = new String[] {"Benefit", "Penalty", "Backlog Effort", "Themes", "Themes"}; 
		test[1] = new String[] {benefit, penalty, effort, theme1, theme2}; 
		
		final FieldValues flexFields = new FieldValues();
		flexFields.setNames(test[0]); 
		flexFields.setValues(test[1]);
		final int flexFieldsLength = flexFields.getNames().length;
		final String[] flexFieldTypes = new String[flexFieldsLength];
		for (int i = 0; i < flexFieldsLength; i++) {
			flexFieldTypes[i] = TrackerFieldSoapDO.FIELD_VALUE_TYPE_STRING; 
		}
		flexFields.setTypes(flexFieldTypes); 
		
		getTeamForgeTester().createBacklogItem(title, description, release, flexFields);
		
		// verify
		final ProductWSO product = getSWPTester().getProduct(getSWPTester().getSwpProduct());
		ReleaseWSO[] releases = getSWPTester().getSWPEndpoint().getReleases(product); 
		BacklogItemWSO[] pbis = getSWPTester().waitForBacklogItemToAppear(product); 
		
		assertEquals(1, pbis.length);
		BacklogItemWSO pbi = pbis[0];
		assertEquals(title, pbi.getTitle());
		assertEquals(description, pbi.getDescription());
		assertEquals(benefit, pbi.getBusinessWeight().getBenefit().toString()); 
		assertEquals(penalty, pbi.getBusinessWeight().getPenalty().toString());
		assertEquals(effort, pbi.getEstimate().toString());
		final List<String> themeNames = getThemeNames(pbi.getThemes()); 
		assertEquals(2, themeNames.size()); 
		assertTrue(themeNames.contains(theme1)); 
		assertTrue(themeNames.contains(theme2)); 
		assertEquals(release, getReleaseForBacklogItem(releases, pbi.getReleaseId())); 
	}

	/**
	 * Returns a list of the theme names. 
	 * 
	 * @param themes the theme web service objects
	 * @return the theme names
	 */
	private List<String> getThemeNames(final ThemeWSO[] themes) {
		final List<String> themeNames = new ArrayList<String>(); 
		for (int i = 0; i < themes.length; i++) {
			themeNames.add(themes[i].getName()); 
		}
		return themeNames; 
	}

	/**
	 * Returns the name of the release for the given backlog item. 
	 * 
	 * @param releases the releases 
	 * @param pbi the backlog item
	 * @return the name of the release if found, otherwise null
	 */
	private String getReleaseForBacklogItem(ReleaseWSO[] releases, Long releaseId) {
		for (int i = 0; i < releases.length; i++) {
			if (releases[i].getId().equals(releaseId)) {
				return releases[i].getTitle(); 
			}
		}
		return null;
	}


}
