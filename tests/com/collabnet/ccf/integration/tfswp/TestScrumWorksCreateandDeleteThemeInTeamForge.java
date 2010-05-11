package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.planning.PlanningFolderDO;
import com.collabnet.teamforge.api.planning.PlanningFolderRow;
import com.danube.scrumworks.api2.client.Product;
import com.danube.scrumworks.api2.client.Release;
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
			getSWPTester().getSWPEndpoint().deleteTheme(theme);
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
		getSWPTester().getSWPEndpoint().deleteTheme(theme);
		// wait until new theme disappears in TF
		getTeamForgeTester().waitForTrackerFieldValueToDisappear(
				getTeamForgeTester().getPbiTracker(),
				TeamForgeTester.FIELD_THEME, themeName);
	}
}
