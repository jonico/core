package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.Task;

/**
 * Tests that when a task in TeamForge has a task as its parent, 
 * 	1. synchronizations still work 
 *  2. when the synchronization from ScrumWorks to TeamForge occurs, 
 *     the parent task's invalid parentage in TeamForge remains
 *  3. when the synchronization from ScrumWorks to TeamForge occurs, 
 *     the child task's parentage is reset to the backlog item. 
 *   
 * @author Kelley
 *
 */

public class TestTaskWithImproperParentage extends TFSWPIntegrationTest {
	/** Backlog item in TeamForge.*/ 
	private ArtifactRow teamForgeBacklogItem;
	
	/** Task in TeamForge that will be the parent task. */ 
	private ArtifactRow teamForgeParentTask;
	
	/** Task in TeamForge that will be the child task. */ 
	private ArtifactRow teamForgeChildTask; 

	/** Backlog item in ScrumWorks.*/ 
	private BacklogItem scrumWorksBacklogItem; 
	
	/** Task in ScrumWorks that will be the parent task. */ 
	Task scrumWorksParentTask;
	
	/** Task in ScrumWorks that will be the child task. */ 
	Task scrumWorksChildTask; 
	
	@Override
	@Before
	public void setUp() throws Exception {
		final String parentTaskTitle = "parent task";
		final String childTaskTitle = "child task"; 
		
		super.setUp();
		
		scrumWorksBacklogItem = getSWPTester().createBacklogItem("backlog item", SWPTester.RELEASE_1); 
		scrumWorksParentTask = getSWPTester().createTask(parentTaskTitle, null, null, null, null, scrumWorksBacklogItem.getId());
		scrumWorksChildTask = getSWPTester().createTask(childTaskTitle, null, null, null, null, scrumWorksBacklogItem.getId()); 
		
		ArtifactRow[] teamForgeBacklogItems = getTeamForgeTester().waitForBacklogItemsToAppear(1);
		teamForgeBacklogItem = teamForgeBacklogItems[0]; 
		ArtifactRow[] teamForgeTasks = getTeamForgeTester().waitForTasksToAppear(2);
		if (teamForgeTasks[0].getTitle().equals(parentTaskTitle)) {
			teamForgeParentTask = teamForgeTasks[0]; 
			teamForgeChildTask = teamForgeTasks[1]; 
		} else {
			teamForgeChildTask = teamForgeTasks[0]; 
			teamForgeParentTask = teamForgeTasks[1]; 
		}
		
		// FIXME Why do we have a sleep here? (race condition for dependency creation)?
		// In this case, it would be better to update one of the child tasks again and wait for this update to come through
		Thread.sleep(2000);
		
		getTeamForgeTester().getConnection().getTrackerClient().removeArtifactDependency(teamForgeBacklogItem.getId(), 
				teamForgeChildTask.getId()); 
		getTeamForgeTester().createArtifactDependency(teamForgeParentTask.getId(), 
				teamForgeChildTask.getId(), 
				"Task parent of task"); 
	}
	
	/**
	 * Tests that data can be synchronized even though the task is a child of another task. 
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testBacklogItemParentOfBacklogItem() throws Exception {
		doTestTeamForgeCreateTaskWithoutBacklogItemParent(); 
		doTestTeamForgeCreateTaskWithChildParent(); 
		doTestTeamForgeSynchronizeToScrumWorksChildTask(); 
		doTestTeamForgeSynchronizeToScrumWorksParentTask(); 
		doTestScrumWorksSynchronizeToTeamForgeParentTaskKeepsInvalidParentage(); 
		doTestScrumWorksSynchronizeToTeamForgeChildTaskResetBacklogItemParentage(); 
	}

	/** 
	 * Tests that a task created in TeamForge with a child as its parent 
	 * will not be created in ScrumWorks. 
	 * 
	 * @throws Exception if an error occurs
	 */
	private void doTestTeamForgeCreateTaskWithChildParent() throws Exception {
		// execute 
		ArtifactDO createdTask = getTeamForgeTester().getConnection().getTrackerClient().createArtifact(
				getTeamForgeTester().getTaskTracker(), "task with parent task", "<blank>", null, null, 
				TaskStatus.NOT_STARTED.getStatus(), null,
				0, 0, 0, false, getTeamForgeTester().getUserName(),
				null, teamForgeParentTask.getPlanningFolderId(), new FieldValues(), null, null, null);
		getTeamForgeTester().createArtifactDependency(teamForgeParentTask.getId(), createdTask.getId(), 
				"created task with parent task"); 
		
		// verify 
		Thread.sleep(getCcfMaxWaitTime()); 
		assertEquals(2, getSWPTester().getTasksForBacklogItem(scrumWorksBacklogItem.getId()).size()); 
	}

	/**
	 * Tests that a task created in TeamForge without a backlog item as its parent 
	 * will not be created in ScrumWorks. 
	 * 
	 * @throws Exception if an error occurs 
	 */
	private void doTestTeamForgeCreateTaskWithoutBacklogItemParent() throws Exception {
		// execute 
		getTeamForgeTester().createTask("task without parent", null, null, null, 0, 0);
		
		// verify
		Thread.sleep(getCcfMaxWaitTime()); 
		assertEquals(2, getSWPTester().getTasksForBacklogItem(scrumWorksBacklogItem.getId()).size()); 
	}

	/**
	 * Tests that data can be synchronized from ScrumWorks to TeamForge for the child task
	 * and that the child task's parentage will be reset to the backlog item. 
	 *  
	 * @throws Exception if an error occurs 
	 */
	private void doTestScrumWorksSynchronizeToTeamForgeChildTaskResetBacklogItemParentage() throws Exception {
		final String updatedTitle = "updated child task title from ScrumWorks"; 
		
		// execute 
		scrumWorksChildTask.setName(updatedTitle); 
		getSWPTester().updateTask(scrumWorksChildTask); 
		
		// verify 
		ArtifactRow updatedTaskInTeamForge = getTeamForgeTester().waitForTaskToUpdate(updatedTitle, 2);
		assertEquals(updatedTitle, updatedTaskInTeamForge.getTitle());
		assertNull(getTeamForgeTester().getParentId(teamForgeBacklogItem.getId())); 
	}

	/** 
	 * Tests that data can be synchronized from ScrumWorks to TeamForge for the parent task and 
	 * the task parentage will not change because the task is still parent to the child task.
	 *  
	 *  
	 * @throws Exception if an error occurs 
	 */
	private void doTestScrumWorksSynchronizeToTeamForgeParentTaskKeepsInvalidParentage() throws Exception {
		final String updatedTitle = "updated parent task title from ScrumWorks"; 
		
		// execute 
		scrumWorksParentTask.setName(updatedTitle); 
		getSWPTester().updateTask(scrumWorksParentTask); 
		
		// verify 
		ArtifactRow updatedTaskInTeamForge = getTeamForgeTester().waitForTaskToUpdate(updatedTitle, 2);
		assertEquals(updatedTitle, updatedTaskInTeamForge.getTitle());
		assertEquals(updatedTaskInTeamForge.getId(), getTeamForgeTester().getParentId(teamForgeChildTask.getId())); 
	}

	/** Tests that data can be synchronized from TeamForge to ScrumWorks for the parent task.
	 * 
	 *  @throws Exception if an error occurs 
	 */
	private void doTestTeamForgeSynchronizeToScrumWorksParentTask() throws Exception {
		final String updatedTitle = "updated parent task from TeamForge"; 
		
		// execute
		getTeamForgeTester().updateTask(teamForgeParentTask.getId(), 
				updatedTitle, 
				teamForgeParentTask.getDescription(), 
				teamForgeParentTask.getStatus(), 
				teamForgeParentTask.getAssignedToUsername(), 
				teamForgeParentTask.getRemainingEffort(), 
				teamForgeParentTask.getEstimatedEffort()); 
		
		// verify
		Task updatedScrumWorksTask = getSWPTester().waitForTaskToAppear(scrumWorksBacklogItem, updatedTitle, 2, null); 
		assertEquals(updatedTitle, updatedScrumWorksTask.getName());
	}

	/**
	 * Tests that data can be synchronized from TeamForge to ScrumWorks for the child task. 
	 * 
	 * @throws Exception if an error occurs 
	 */
	private void doTestTeamForgeSynchronizeToScrumWorksChildTask() throws Exception {
		final String updatedTitle = "updated child task from TeamForge"; 
		
		// execute
		getTeamForgeTester().updateTask(teamForgeChildTask.getId(), 
				updatedTitle, 
				teamForgeChildTask.getDescription(), 
				teamForgeChildTask.getStatus(), 
				teamForgeChildTask.getAssignedToUsername(), 
				teamForgeChildTask.getRemainingEffort(), 
				teamForgeChildTask.getEstimatedEffort()); 
		
		// verify
		Task updatedScrumWorksTask = getSWPTester().waitForTaskToAppear(scrumWorksBacklogItem, updatedTitle, 2, null);  
		assertEquals(updatedTitle, updatedScrumWorksTask.getName());
	}
}
