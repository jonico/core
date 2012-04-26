/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.Comment;
import com.danube.scrumworks.api2.client.Task;

/**
 * Creates a task item in TeamForge and verifies the task item in ScrumWorks
 * after the synchronization.
 * 
 * @author jnicolai
 */
public class TestTeamForgeCreateAndUpdateTaskInScrumWorks extends TFSWPIntegrationTest {

	/**
	 * Creates a task item in TeamForge and verifies the task item in ScrumWorks
	 * after the synchronization. Afterwards, we will update the task and look
	 * whether the update goes through as well
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void testTaskCreationAndUpdate() throws Exception {
		// first we have to create a PBI
		String title = "TFTask";
		String description = "TFTaskDescription";
		String status = TaskStatus.NOT_STARTED.getStatus();
		int remainingEffort = 0;
		int originalEstimate = 0;
		String assignedToUser = getTeamForgeTester().getUserName();

		String taskId = getTeamForgeTester().createTaskAndPBI(title, description,
				status, assignedToUser, remainingEffort, originalEstimate).getId();

		// verify
		List<BacklogItem> pbis = getSWPTester().waitForBacklogItemsToAppear(1); 

		assertEquals(1, pbis.size());
		BacklogItem pbi = pbis.get(0);
		
		// now that we can be sure that PBI has been created, update task again to trigger resynch
		getTeamForgeTester().updateTask(taskId, title, description,
				status, assignedToUser, remainingEffort, originalEstimate);

		Task task = getSWPTester().waitForTaskToAppear(pbi, title, 1, null);

		assertEquals(title, task.getName());
		assertEquals(description, task.getDescription());
		assertEquals(status, task.getStatus());
		assertNull(task.getCurrentEstimate());
		assertNull(task.getOriginalEstimate());
		assertEquals(assignedToUser + " (" + assignedToUser + ")", task
				.getPointPerson());

		// now we update the task
		String newTitle = "ChangedTFTask";
		String newDescription = "ChangedTFTaskDescription";
		String newStatus = TaskStatus.IN_PROGRESS.getStatus();
		int newRemainingEffort = 42;
		int newOriginalEstimate = 42;
		String newAssignedToUser = null;

		getTeamForgeTester().updateTask(taskId, newTitle, newDescription,
				newStatus, newAssignedToUser, newRemainingEffort, newOriginalEstimate);

		// now we have to wait for the update to come through
		task = getSWPTester().waitForTaskToAppear(pbi, newTitle, 1, null);

		assertEquals(newTitle, task.getName());
		assertEquals(newDescription, task.getDescription());
		assertEquals(newStatus, task.getStatus());
		assertEquals(newRemainingEffort, task.getCurrentEstimate().intValue());
		assertEquals(newOriginalEstimate, task.getOriginalEstimate().intValue());
		assertNull(task.getPointPerson());

		
		
		// now we set the estimated hours to zero and expect zero (instead of
		// null)
		int zeroEffort = 0;
		String yetAnotherTitle = "Yet another title";
		String yetAnotherStatus = TaskStatus.DONE.getStatus();
		String yetAnotherUser = getTeamForgeTester().getUserName();
		String yetAnotherDescription = "yetAnotherDescription";
		
		getTeamForgeTester().updateTask(taskId, yetAnotherTitle, yetAnotherDescription,
				yetAnotherStatus, yetAnotherUser, zeroEffort, newOriginalEstimate);
		
		// now we have to wait for the update to come through
		task = getSWPTester().waitForTaskToAppear(pbi, yetAnotherTitle, 1, null); 

		assertEquals(yetAnotherTitle, task.getName());
		assertEquals(yetAnotherDescription, task.getDescription());
		assertEquals(yetAnotherStatus, task.getStatus());
		assertEquals(zeroEffort, task.getCurrentEstimate().intValue());
		assertEquals(newOriginalEstimate, task.getOriginalEstimate().intValue());
		assertEquals(assignedToUser + " (" + assignedToUser + ")", task
				.getPointPerson());

		// now we check whether comments have been transported
		List<Comment> comments = getSWPTester().getSWPEndpoint().getCommentsForTask(task.getId());
		
		// if multiple comments are added to TF in the same second, it might deliver it multiple times
		assertTrue(comments.size() > 2);
		for (Comment comment : comments) {
			assertTrue(comment.getText().contains("updating task ..."));
		}
		
	}

}
