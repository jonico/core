package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.TaskWSO;

/**
 * Tests updating an existing task in ScrumWorks and verifying that the task is updated in TeamForge. 
 * @author Kelley
 *
 */
public class TestScrumWorksUpdateTaskInTeamForge extends TFSWPIntegrationTest {
	private String release = SWPTester.RELEASE_1;
	private TaskWSO task; 
	private ArtifactRow teamForgeBacklogItem; 
	
	/**
	 * Creates a task in ScrumWorks.
	 */
	@Before
	public void setUp() throws Exception {
		final String pbiTitle = "pbi";  
		
		super.setUp();
		
		BacklogItemWSO backlogItem = getSWPTester().createBacklogItem(pbiTitle, release); 
		task = getSWPTester().createTask("taskTitle", null, null, TaskStatus.NOT_STARTED, null, backlogItem.getBacklogItemId()); 
		
		teamForgeBacklogItem = getTeamForgeTester().waitForBacklogItemsToAppear(1)[0]; 
	}
	
	/**
	 * Tests updating fields in a task in ScrumWorks and verifies the task is updated in TeamForge.
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testUpdateTask() throws Exception {
		final String taskTitle = "title updated";
		final String description = "description";
		final String pointPerson = "Kelley (Kelley)";
		final String currentEstimate = "50";
		final String originalEstimate = "100";
		final TaskStatus status = TaskStatus.IN_PROGRESS;
		
		// execute
		TaskWSO taskToBeUpdated = new TaskWSO(task.getBacklogItemId(), 
				description, 
				Integer.parseInt(currentEstimate), 
				task.getId(), 
				Integer.parseInt(originalEstimate), 
				pointPerson, 
				task.getRank(), 
				status.getStatus(), 
				task.getTaskBoardStatusRank(), 
				taskTitle); 
		getSWPTester().updateTask(taskToBeUpdated); 
		
		// verify 
		ArtifactRow teamForgeTask = waitForTaskToUpdate(taskTitle);
		
		assertEquals(taskTitle, teamForgeTask.getTitle()); 
		assertEquals(description, teamForgeTask.getDescription()); 
		assertEquals(status.getStatus(), teamForgeTask.getStatus()); 
		assertEquals("", teamForgeTask.getCategory()); 
		assertEquals(0, teamForgeTask.getPriority()); 
		assertEquals(pointPerson, teamForgeTask.getAssignedToFullname()); 
		assertEquals(getTeamForgeTester().getPlanningFolderId(release), teamForgeTask.getPlanningFolderId()); 
		assertEquals(Integer.parseInt(originalEstimate), teamForgeTask.getEstimatedEffort()); 
		assertEquals(Integer.parseInt(currentEstimate), teamForgeTask.getRemainingEffort()); 
		
		assertEquals(teamForgeBacklogItem.getId(), getTeamForgeTester().getParentId(teamForgeTask.getId())); 
		
	}

	/**
	 * Returns the updateTask from TeamForge after the task has been updated. 
	 * 
	 * @param taskTitle
	 * @throws Exception
	 */
	private ArtifactRow waitForTaskToUpdate(final String taskTitle)throws Exception {
		ArtifactRow teamForgeTask;
		for (int i = 0; i < getCcfMaxWaitTime(); i++) {
			ArtifactRow[] artifacts = getTeamForgeTester().waitForTasksToAppear(1); 
			teamForgeTask = artifacts[0]; 
			if (!teamForgeTask.getTitle().equals(taskTitle)) {
				artifacts = getTeamForgeTester().waitForTasksToAppear(1); 
				teamForgeTask = artifacts[0]; 
				Thread.sleep(getCcfRetryInterval()); 
			} else {
				return teamForgeTask;  
			}
		}
		throw new RuntimeException("Task not updated within the timeout: " + getCcfMaxWaitTime()); 
	}
}
