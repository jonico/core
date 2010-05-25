package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api2.client.BacklogItem;

/**
 * Tests that when a backlog item in TeamForge has a backlog item as its parent, 
 * 	1. synchronizations still work 
 *  2. when the synchronization from ScrumWorks to TeamForge occurs, 
 *     the backlog item's invalid parentage in TeamForge is removed.
 *   
 * @author Kelley
 *
 */

public class TestBacklogItemWithImproperParentage extends TFSWPIntegrationTest {
	/** Release name for all backlog items. */ 
	private static final String release = SWPTester.RELEASE_1;

	/** Backlog item in TeamForge that will be the parent. */ 
	private ArtifactRow teamForgeParentBacklogItem;
	
	/** Backlog item in TeamForge that will be the child. */ 
	private ArtifactRow teamForgeChildBacklogItem; 

	/** Backlog item in ScrumWorks that will be the parent. */ 
	private BacklogItem scrumWorksParentBacklogItem; 
	
	/** Backlog item in ScrumWorks that will be the child. */ 
	BacklogItem scrumWorksChildBacklogItem; 
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		scrumWorksParentBacklogItem = getSWPTester().createBacklogItem("parent backlog item", release); 
		scrumWorksChildBacklogItem = getSWPTester().createBacklogItem("child backlog item", release); 
		getSWPTester().waitForBacklogItemsToAppear(2); 
		
		ArtifactRow[] teamForgeBacklogItems = getTeamForgeTester().waitForBacklogItemsToAppear(2);
		if (teamForgeBacklogItems[0].getTitle().equals("parent backlog item")) {
			teamForgeParentBacklogItem = teamForgeBacklogItems[0];
			teamForgeChildBacklogItem = teamForgeBacklogItems[1]; 
		} else {
			teamForgeChildBacklogItem = teamForgeBacklogItems[0]; 
			teamForgeParentBacklogItem = teamForgeBacklogItems[1];
		}
		
		getTeamForgeTester().createArtifactDependency(teamForgeParentBacklogItem.getId(), 
				teamForgeChildBacklogItem.getId(), 
				"Backlog item parent of backlog item"); 
	}
	
	/**
	 * Tests that data can be synchronized even though the backlog item is a child of another backlog item. 
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testBacklogItemParentOfBacklogItem() throws Exception {
		doTestTeamForgeCreateChildBacklogItem(); 
		doTestTeamForgeCreateBacklogItemWithoutPlanningFolder(); 
		doTestTeamForgeSynchronizeToScrumWorksParentBacklogItem(); 
		doTestTeamForgeSynchronizeToScrumWorksChildBacklogItem(); 
		doTestScrumWorksSynchronizeToTeamForgeParentBacklogItem(); 
		doTestScrumWorksSynchronizeToTeamForgeChildBacklogItem(); 
	}

	/** 
	 * Tests that a backlog item created in TeamForge without a planning folder 
	 * is synchronized from TeamForge to ScrumWorks.  The backlog item in ScrumWorks is created
	 * in the top-most release of the product backlog.
	 * 
	 * @throws Exception if an error occurs
	 */
	private void doTestTeamForgeCreateBacklogItemWithoutPlanningFolder() throws Exception {
		final String backlogItemTitle = "teamForge created PBI without planning folder";

		// execute 
		getTeamForgeTester().createBacklogItem(backlogItemTitle, 
				null, 
				null, 
				new FieldValues());
		
		// verify 
		BacklogItem scrumWorksBacklogItem = getSWPTester().waitForBacklogItemToUpdate(backlogItemTitle); 
		assertEquals(SWPTester.RELEASE_3, getSWPTester().getReleaseName(scrumWorksBacklogItem.getReleaseId())); 
	}

	/**
	 * Tests that a child backlog item of a parent backlog item is synchronized from 
	 * TeamForge to ScrumWorks. 
	 * 
	 * @throws Exception if an error occurs
	 */
	private void doTestTeamForgeCreateChildBacklogItem() throws Exception {
		// execute
		ArtifactDO teamForgeCreatedBacklogItem = getTeamForgeTester().createBacklogItem("teamForge created child PBI", 
				null, 
				release, 
				new FieldValues()); 
		getTeamForgeTester().createArtifactDependency(teamForgeParentBacklogItem.getId(), 
				teamForgeCreatedBacklogItem.getId(), 
				"created backlog item is child of another backlog item"); 
		
		// verify
		getSWPTester().waitForBacklogItemsToAppear(3); 
	}

	/**
	 * Tests that data can be synchronized from ScrumWorks to TeamForge for the child backlog item
	 * and that the child backlog item's parentage to the backlog item will be removed in TeamForge.
	 *  
	 * @throws Exception if an error occurs 
	 */
	private void doTestScrumWorksSynchronizeToTeamForgeChildBacklogItem() throws Exception {
		synchronizeUpdatedBacklogItemFromScrumWorksToTeamForge(scrumWorksChildBacklogItem, 
				"updated child title from ScrumWorks"); 
		
		// verify 
		assertNull(getTeamForgeTester().getParentId(teamForgeChildBacklogItem.getId())); 
	}

	/** 
	 * Tests that data can be synchronized from ScrumWorks to TeamForge for the parent backlog item.
	 *  
	 * @throws Exception if an error occurs 
	 */
	private void doTestScrumWorksSynchronizeToTeamForgeParentBacklogItem() throws Exception {
		synchronizeUpdatedBacklogItemFromScrumWorksToTeamForge(scrumWorksParentBacklogItem, 
				"updated parent title from ScrumWorks"); 
	}

	/** 
	 * Synchronizes data for the given backlog item from ScrumWorks to TeamForge after the 
	 * backlog item title has been updated in ScrumWorks.
	 *  
	 * @param updatedTitle the title to be given to the backlog item
	 * @param scrumWorksBacklogItem the synchronized backlog item in ScrumWorks
	 * @throws Exception if an error occurs
	 */
	private void synchronizeUpdatedBacklogItemFromScrumWorksToTeamForge(
			BacklogItem scrumWorksBacklogItem, final String updatedTitle)
			throws Exception {
		// execute 
		scrumWorksBacklogItem.setName(updatedTitle); 
		getSWPTester().updateBacklogItem(scrumWorksBacklogItem); 
		
		// verify 
		ArtifactRow updatedBacklogItemInTeamForge = getTeamForgeTester().waitForBacklogItemToUpdate(updatedTitle, 2);
		assertEquals(updatedTitle, updatedBacklogItemInTeamForge.getTitle());
	}

	/** Tests that data can be synchronized from TeamForge to ScrumWOrks for the parent backlog item.
	 * 
	 *  @throws Exception if an error occurs 
	 */
	private void doTestTeamForgeSynchronizeToScrumWorksParentBacklogItem() throws Exception {
		synchronizeUpdatedBacklogItemFromTeamForgeToScrumWorks(teamForgeParentBacklogItem, 
				"updated parent title from TeamForge"); 
	}

	/**
	 * Tests that data can be synchronized from TeamForge to ScrumWorks for the child backlog item. 
	 * 
	 * @throws Exception if an error occurs 
	 */
	private void doTestTeamForgeSynchronizeToScrumWorksChildBacklogItem() throws Exception {
		synchronizeUpdatedBacklogItemFromTeamForgeToScrumWorks(teamForgeChildBacklogItem, 
				"updated child title from TeamForge"); 
	}

	/** 
	 * Synchronizes data for the given backlog item from TeamForge to ScrumWorks after the 
	 * backlog item title has been updated in TeamForge.
	 *  
	 * @param teamForgeBacklogItem the backlog item to be updated
	 * @param updatedTitle the title to be given to the backlog item
	 * @throws Exception if an error occurs
	 */
	private void synchronizeUpdatedBacklogItemFromTeamForgeToScrumWorks(final ArtifactRow teamForgeBacklogItem, 
			final String updatedTitle) throws Exception {
	
		// execute
		getTeamForgeTester().updateBacklogItem(teamForgeBacklogItem.getId(), 
				updatedTitle, 
				teamForgeBacklogItem.getDescription(), 
				release, 
				getTeamForgeTester().getFlexFields(teamForgeBacklogItem.getId())); 
		
		// verify
		BacklogItem updatedScrumWorksBacklogItem = getSWPTester().waitForBacklogItemToUpdate(updatedTitle); 
		assertEquals(updatedTitle, updatedScrumWorksBacklogItem.getName());
	}
}
