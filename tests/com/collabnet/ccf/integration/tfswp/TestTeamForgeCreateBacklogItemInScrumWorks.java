/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.collabnet.teamforge.api.FieldValues;
import com.danube.scrumworks.api2.client.BacklogItem;

/**
 * Creates a backlog item in TeamForge and verifies the backlog item in
 * ScrumWorks after the synchronization.
 * 
 * @author jnicolai
 */
public class TestTeamForgeCreateBacklogItemInScrumWorks extends TFSWPIntegrationTest {

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
        final String release = SWPTester.RELEASE_1;
        final String benefit = "10";
        final String penalty = "20";
        final Integer effort = 30;
        final String theme1 = SWPTester.THEME_CORE;
        final String theme2 = SWPTester.THEME_GUI;
        // and now some bogus values that should be ignored
        String bogusKey = "bogusKey";

        // execute
        final FieldValues flexFields = getTeamForgeTester()
                .convertToFlexField(
                        new String[] { TeamForgeTester.FIELD_BENEFIT,
                                TeamForgeTester.FIELD_PENALTY,
                                TeamForgeTester.FIELD_THEME,
                                TeamForgeTester.FIELD_THEME,
                                TeamForgeTester.FIELD_KEY },
                        new String[] { benefit, penalty, theme1, theme2,
                                bogusKey });

        getTeamForgeTester().createBacklogItem(title, description, release,
                flexFields, effort);

        // verify
        List<BacklogItem> pbis = getSWPTester().waitForBacklogItemsToAppear(1);

        assertEquals(1, pbis.size());
        BacklogItem pbi = pbis.get(0);
        assertEquals(title, pbi.getName());
        assertEquals(description, pbi.getDescription());
        assertEquals(benefit, pbi.getBusinessWeight().getBenefit().toString());
        assertEquals(penalty, pbi.getBusinessWeight().getPenalty().toString());
        assertEquals(effort, pbi.getEstimate());

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
