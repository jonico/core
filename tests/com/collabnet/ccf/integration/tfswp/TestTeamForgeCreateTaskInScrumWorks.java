/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.TaskWSO;

/**
 * Creates a task item in TeamForge and verifies the task item in ScrumWorks
 * after the synchronization.
 * 
 * @author jnicolai
 */
public class TestTeamForgeCreateTaskInScrumWorks extends TFSWPIntegrationTest {

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
		String status = "Not Started";
		int remainingEffort = 0;
		int originalEstimate = 0;
		String assignedToUser = getTeamForgeTester().getUserName();

		String taskId = getTeamForgeTester().createTask(title, description,
				status, assignedToUser, remainingEffort, originalEstimate).getId();

		// verify
		BacklogItemWSO[] pbis = getSWPTester().waitForBacklogItemsToAppear(1); 

		assertEquals(1, pbis.length);
		BacklogItemWSO pbi = pbis[0];
		
		// now that we can be sure that PBI has been created, update task again to trigger resynch
		getTeamForgeTester().updateTask(taskId, title, description,
				status, assignedToUser, remainingEffort, originalEstimate);

		TaskWSO[] tasks = null;
		for (int i = 0; i < getCcfMaxWaitTime(); i += getCcfRetryInterval()) {
			tasks = getSWPTester().getSWPEndpoint().getTasks(pbi);
			if (tasks == null) {
				Thread.sleep(getCcfRetryInterval());
			} else {
				break;
			}
		}

		assertEquals(1, tasks.length);
		TaskWSO task = tasks[0];
		assertEquals(title, task.getTitle());
		assertEquals(description, task.getDescription());
		assertEquals(status, task.getStatus());
		assertNull(task.getEstimatedHours());
		assertNull(task.getOriginalEstimate());
		assertEquals(assignedToUser + " (" + assignedToUser + ")", task
				.getPointPerson());

		// now we update the task
		String newTitle = "ChangedTFTask";
		String newDescription = "ChangedTFTaskDescription";
		String newStatus = "In Progress";
		int newRemainingEffort = 42;
		int newOriginalEstimate = 42;
		String newAssignedToUser = null;

		getTeamForgeTester().updateTask(taskId, newTitle, newDescription,
				newStatus, newAssignedToUser, newRemainingEffort, newOriginalEstimate);

		// now we have to wait for the update to come through
		for (int i = 0; i < getCcfMaxWaitTime(); i += getCcfRetryInterval()) {
			tasks = getSWPTester().getSWPEndpoint().getTasks(pbi);
			assertEquals(1, tasks.length);
			task = tasks[0];
			if (task.getTitle().equals(title)) {
				Thread.sleep(getCcfRetryInterval());
			} else {
				break;
			}
		}

		assertEquals(newTitle, task.getTitle());
		assertEquals(newDescription, task.getDescription());
		assertEquals(newStatus, task.getStatus());
		assertEquals(newRemainingEffort, task.getEstimatedHours().intValue());
		assertEquals(newOriginalEstimate, task.getOriginalEstimate().intValue());
		assertNull(task.getPointPerson());

		
		
		// now we set the estimated hours to zero and expect zero (instead of
		// null)
		int zeroEffort = 0;
		String yetAnotherTitle = "Yet another title";
		String yetAnotherStatus = "Done";
		String yetAnotherUser = getTeamForgeTester().getUserName();
		String yetAnotherDescription = "yetAnotherDescription";
		
		getTeamForgeTester().updateTask(taskId, yetAnotherTitle, yetAnotherDescription,
				yetAnotherStatus, yetAnotherUser, zeroEffort, newOriginalEstimate);
		
		// now we have to wait for the update to come through
		for (int i = 0; i < getCcfMaxWaitTime(); i += getCcfRetryInterval()) {
			tasks = getSWPTester().getSWPEndpoint().getTasks(pbi);
			assertEquals(1, tasks.length);
			task = tasks[0];
			if (task.getTitle().equals(newTitle)) {
				Thread.sleep(getCcfRetryInterval());
			} else {
				break;
			}
		}

		assertEquals(yetAnotherTitle, task.getTitle());
		assertEquals(yetAnotherDescription, task.getDescription());
		assertEquals(yetAnotherStatus, task.getStatus());
		assertEquals(zeroEffort, task.getEstimatedHours().intValue());
		assertEquals(newOriginalEstimate, task.getOriginalEstimate().intValue());
		assertEquals(assignedToUser + " (" + assignedToUser + ")", task
				.getPointPerson());
		
	}

}
