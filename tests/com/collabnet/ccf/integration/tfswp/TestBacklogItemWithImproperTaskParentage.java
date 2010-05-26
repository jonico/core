package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.Task;

/**
 * Tests that when a backlog item in TeamForge has a task as its parent, 
 * 	1. synchronizations still work 
 *  2. when the synchronization from ScrumWorks to TeamForge occurs, 
 *     the backlog item's invalid parentage in TeamForge is removed
 *  3. when the synchronization from ScrumWorks to TeamForge occurs, 
 *     the task's parentage in TeamForge is added if the is no dependency 
 *     between the backlog item and task
 *   
 * @author Kelley
 *
 */

public class TestBacklogItemWithImproperTaskParentage extends TFSWPIntegrationTest {
	/** Release name for all backlog items. */ 
	private static final String release = SWPTester.RELEASE_1;

	/** Backlog item in TeamForge that will be the child. */ 
	private ArtifactRow teamForgeBacklogItem;
	
	/** Task in TeamForge that will be the parent. */ 
	private ArtifactRow teamForgeTask; 

	/** Backlog item in ScrumWorks that will be the child. */ 
	private BacklogItem scrumWorksBacklogItem; 
	
	/** Task in ScrumWorks that will be the parent. */ 
	Task scrumWorksTask; 
	
	@Override
	@Before
	public void setUp() throws Exception {
		final String taskTitle = "parent task";
		
		super.setUp();
		
		scrumWorksBacklogItem = getSWPTester().createBacklogItem("child backlog item", release); 
		scrumWorksTask = getSWPTester().createTask(taskTitle, null, null, null, null, scrumWorksBacklogItem.getId()); 
		
		ArtifactRow[] teamForgeBacklogItems = getTeamForgeTester().waitForBacklogItemsToAppear(1);
		teamForgeBacklogItem = teamForgeBacklogItems[0]; 
		ArtifactRow[] teamForgeTasks = getTeamForgeTester().waitForTasksToAppear(1);
		teamForgeTask = teamForgeTasks[0]; 
		
		// FIXME Why do we have a sleep here? (race condition for dependency creation)?
		// In this case, it would be better to update one of the child tasks again and wait for this update to come through
		Thread.sleep(2000);
		
		getTeamForgeTester().getConnection().getTrackerClient().removeArtifactDependency(teamForgeBacklogItem.getId(), 
				teamForgeTask.getId()); 
		getTeamForgeTester().createArtifactDependency(teamForgeTask.getId(), 
				teamForgeBacklogItem.getId(), 
				"Task item parent of backlog item");
	}
	
	/**
	 * Tests that data can be synchronized even though the backlog item is a child of another backlog item. 
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testBacklogItemParentOfBacklogItem() throws Exception {
		doTestTeamForgeSynchronizeToScrumWorksChildBacklogItem(); 
		doTestTeamForgeSynchronizeToScrumWorksParentTask(); 
		doTestScrumWorksSynchronizeToTeamForgeParentTaskKeepsInvalidParentage(); 
		doTestScrumWorksSynchronizeToTeamForgeChildBacklogItemRemovesParentage(); 
		doTestScrumWorksSynchronizeToTeamForgeParentTaskResetParentage(); 
	}

	/**
	 * Tests that data can be synchronized from ScrumWorks to TeamForge for the child backlog item
	 * and that the child backlog item's parentage to the backlog item will be removed in TeamForge.
	 *  
	 * @throws Exception if an error occurs 
	 */
	private void doTestScrumWorksSynchronizeToTeamForgeChildBacklogItemRemovesParentage() throws Exception {
		final String updatedTitle = "updated child backlog item title from ScrumWorks"; 
		
		// execute 
		scrumWorksBacklogItem.setName(updatedTitle); 
		getSWPTester().updateBacklogItem(scrumWorksBacklogItem); 
		
		// verify 
		ArtifactRow updatedBacklogItemInTeamForge = getTeamForgeTester().waitForBacklogItemToUpdate(updatedTitle, 1);
		assertEquals(updatedTitle, updatedBacklogItemInTeamForge.getTitle());
		assertNull(getTeamForgeTester().getParentId(updatedBacklogItemInTeamForge.getId())); 
	}

	/** 
	 * Tests that data can be synchronized from ScrumWorks to TeamForge for the parent task and 
	 * the task will be a dependency of the backlog item when the task has no parentage.
	 *  
	 * @throws Exception if an error occurs 
	 */
	private void doTestScrumWorksSynchronizeToTeamForgeParentTaskResetParentage() throws Exception {
		final String updatedTitle = "updated parent task title from ScrumWorks resets parentage"; 
		
		// execute 
		scrumWorksTask.setName(updatedTitle); 
		getSWPTester().updateTask(scrumWorksTask); 
		
		// verify 
		ArtifactRow updatedTaskInTeamForge = getTeamForgeTester().waitForTaskToUpdate(updatedTitle, 1);
		assertEquals(updatedTitle, updatedTaskInTeamForge.getTitle());
		assertEquals(teamForgeBacklogItem.getId(), getTeamForgeTester().getParentId(updatedTaskInTeamForge.getId())); 
	}

	/** 
	 * Tests that data can be synchronized from ScrumWorks to TeamForge for the parent task and 
	 * the task parentage will not change because the task is still parent to the backlog item.
	 * (TF detects a cycle here and throws an exception) 
	 *  
	 * @throws Exception if an error occurs 
	 */
	private void doTestScrumWorksSynchronizeToTeamForgeParentTaskKeepsInvalidParentage() throws Exception {
		final String updatedTitle = "updated parent task title from ScrumWorks"; 
		
		// execute 
		scrumWorksTask.setName(updatedTitle); 
		getSWPTester().updateTask(scrumWorksTask); 
		
		// verify 
		ArtifactRow updatedTaskInTeamForge = getTeamForgeTester().waitForTaskToUpdate(updatedTitle, 1);
		assertEquals(updatedTitle, updatedTaskInTeamForge.getTitle());
		assertEquals(updatedTaskInTeamForge.getId(), getTeamForgeTester().getParentId(teamForgeBacklogItem.getId())); 
	}

	/** Tests that data can be synchronized from TeamForge to ScrumWOrks for the parent backlog item.
	 * 
	 *  @throws Exception if an error occurs 
	 */
	private void doTestTeamForgeSynchronizeToScrumWorksParentTask() throws Exception {
		final String updatedTitle = "updated parent task title from TeamForge"; 
		
		// execute
		getTeamForgeTester().updateTask(teamForgeTask.getId(), 
				updatedTitle, 
				teamForgeTask.getDescription(), 
				teamForgeTask.getStatus(), 
				teamForgeTask.getAssignedToUsername(), 
				teamForgeTask.getRemainingEffort(), 
				teamForgeTask.getEstimatedEffort()); 
		
		// verify
		Task updatedScrumWorksTask = getSWPTester().waitForTaskToAppear(scrumWorksBacklogItem, updatedTitle, 1, null); 
		assertEquals(updatedTitle, updatedScrumWorksTask.getName());
	}

	/**
	 * Tests that data can be synchronized from TeamForge to ScrumWorks for the child backlog item. 
	 * 
	 * @throws Exception if an error occurs 
	 */
	private void doTestTeamForgeSynchronizeToScrumWorksChildBacklogItem() throws Exception {
		final String updatedTitle = "updated child backlog item title from TeamForge"; 
		
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
