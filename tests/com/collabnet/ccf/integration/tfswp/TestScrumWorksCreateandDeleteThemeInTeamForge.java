package com.collabnet.ccf.integration.tfswp;

import java.rmi.RemoteException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.danube.scrumworks.api2.client.Product;
import com.danube.scrumworks.api2.client.ScrumWorksException;
import com.danube.scrumworks.api2.client.Theme;

/**
 * Test creating and deleting theme in SWP and checking whether this synchs to
 * TF
 * 
 * @author jnicolai
 * 
 */
public class TestScrumWorksCreateandDeleteThemeInTeamForge extends
		TFSWPIntegrationTest {
	private Theme theme = null;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws ScrumWorksException, RemoteException {
		if (theme != null) {
			getSWPTester().getSWPEndpoint().deleteTheme(theme.getId());
		}
		super.tearDown();
	}

	/**
	 * Test creating and deleting a theme in SWP and checking whether changes
	 * synch to TF
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void testCreateAndDeleteTheme() throws Exception {
		Product swpProduct = getSWPTester().getProduct();
		// first we have to create a new theme
		String themeName = "TestTheme" + System.currentTimeMillis();
		Theme themeTemplate = new Theme();
		themeTemplate.setProductId(swpProduct.getId());
		themeTemplate.setName(themeName);
		theme = getSWPTester().getSWPEndpoint().createTheme(themeTemplate);
		// wait until new theme appears in TF
		getTeamForgeTester().waitForTrackerFieldValueToAppear(
				getTeamForgeTester().getPbiTracker(),
				TeamForgeTester.FIELD_THEME, themeName);
		// delete theme
		getSWPTester().getSWPEndpoint().deleteTheme(theme.getId());
		// wait until new theme disappears in TF
		getTeamForgeTester().waitForTrackerFieldValueToDisappear(
				getTeamForgeTester().getPbiTracker(),
				TeamForgeTester.FIELD_THEME, themeName);
	}
}
