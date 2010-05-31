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

/**
 * Tests creating, updating and deleting releases in ScrumWorks and whether these operations synch to TF   
 * 
 * @author Kelley
 *
 */
public class TestScrumWorksCreateUpdateAndDeleteReleaseInTeamForge extends TFSWPIntegrationTest {
	private Release swpRelease = null;
	PlanningFolderRow tfRelease = null;

	/**
	 * Creates a backlog item. 
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@After
	public void tearDown() throws ScrumWorksException, RemoteException {
		if (swpRelease != null) {
			getSWPTester().getSWPEndpoint().deleteEmptyRelease(swpRelease.getId());
		}
		if (tfRelease != null) {
			getTeamForgeTester().getConnection().getPlanningClient().deletePlanningFolder(tfRelease.getId());
		}
		super.tearDown();
	}
	
	/**
	 * Test creating and updating a release in SWP and checking whether changes synch to TF
	 *  
	 * @throws Exception if an error occurs 
	 */
	@Test
	public void testCreateAndUpdateRelease() throws Exception {
		Product swpProduct = getSWPTester().getProduct();
		// first, we have to figure out the SWP product PF in TF
		String productPFId = getTeamForgeTester().getPlanningFolderId(swpProduct.getName());
		// now we have to check for the number of existing planning folders in TF
		PlanningFolderRow[] pfRows = getTeamForgeTester().waitForPlanningFoldersToAppear(productPFId, 0, false);
		int alreadyExistingReleasesInTF = pfRows.length;
		
		// now create new SWP release
		// use random title with system millis inside to identify
		String releaseTitle = "Release" + System.currentTimeMillis();
		String releaseDescription = "Release automatically created by a unit test";
		XMLGregorianCalendar releaseDate = null;
		XMLGregorianCalendar startDate = null;
		swpRelease = new Release();
		swpRelease.setArchived(false); 
		swpRelease.setName(releaseTitle); 
		swpRelease.setDescription(releaseDescription);
		swpRelease.setProductId(swpProduct.getId());
		swpRelease.setStartDate(startDate); 
		swpRelease.setEndDate(releaseDate); 
//		swpRelease = new Release(false, releaseDescription, null, swpProduct.getId(), null, releaseDate ,startDate, releaseTitle); //TODO fix after SWP API is updated
		swpRelease = getSWPTester().getSWPEndpoint().createRelease(swpRelease);
		
		// now we have to query the planning folder api again and again until we find the new release
		pfRows = getTeamForgeTester().waitForPlanningFoldersToAppear(productPFId, alreadyExistingReleasesInTF + 1, false);
		assertEquals(alreadyExistingReleasesInTF + 1, pfRows.length);
		
		// now we have to identify the newly created PF
		for (int i = 0; i < pfRows.length; i++) {
			if (pfRows[i].getTitle().equals(releaseTitle)) {
				tfRelease = pfRows[i];
				break;
			}
		}
		assertNotNull(tfRelease);
		assertNull(tfRelease.getStartDate());
		assertNull(tfRelease.getEndDate());
		assertEquals(releaseDescription, tfRelease.getDescription());
		
		// now we will update the release
		String newReleaseTitle = "RenamedRelease" + System.currentTimeMillis();
		String newReleaseDescription = "Renamed Release automatically created by a unit test";
		Calendar today = new GregorianCalendar();
		GregorianCalendar newStartDate = new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH) ,today.get(Calendar.DAY_OF_MONTH));
		GregorianCalendar newReleaseDate = (GregorianCalendar) newStartDate.clone(); 
		newReleaseDate.add(Calendar.DAY_OF_MONTH, 1);
		XMLGregorianCalendar xmlNewStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(newStartDate); 
		XMLGregorianCalendar xmlNewReleaseDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(newReleaseDate);  
		
		swpRelease.setName(newReleaseTitle);
		swpRelease.setDescription(newReleaseDescription);
		swpRelease.setStartDate(xmlNewStartDate); 
		swpRelease.setEndDate(xmlNewReleaseDate);
		swpRelease = getSWPTester().getSWPEndpoint().updateRelease(swpRelease);
		
		// now we have to wait until the change went through
		PlanningFolderDO updatedTFRelease = getTeamForgeTester().waitForPFTitleToChange(tfRelease);
		
		assertEquals(newReleaseTitle, updatedTFRelease.getTitle());
		assertEquals(newReleaseDescription, updatedTFRelease.getDescription());
		assertEquals(newStartDate.getTime(), updatedTFRelease.getStartDate());
		assertEquals(newReleaseDate.getTime(), updatedTFRelease.getEndDate());
		
		// now delete release in SWP
		getSWPTester().getSWPEndpoint().deleteEmptyRelease(swpRelease.getId());
		// do not delete object twice
		swpRelease = null;
		getTeamForgeTester().waitForPlanningFoldersToDisappear(productPFId, alreadyExistingReleasesInTF, false);
		// do not delete object twice
		tfRelease = null;
	}
}
