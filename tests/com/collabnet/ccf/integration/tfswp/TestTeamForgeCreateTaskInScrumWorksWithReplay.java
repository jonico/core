/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.Task;

/**
 * Creates a task item in TeamForge and do not wait until PBI has been synchronized.
 * This will result in a quarantined artifact, but the automated replay should fix this 
 * problem after the replay timeout expired.
 * 
 * @author jnicolai
 */
public class TestTeamForgeCreateTaskInScrumWorksWithReplay extends TFSWPIntegrationTest {

	// ms that CCF will wait before it replays failed artifacts with error code parentArtifactNotPresentError
	public final static long CCF_ARTIFACT_REPLAY_TIMEOUT = 3 * 60 * 1000;
	/**
	 * Creates a task item in TeamForge and verifies the task item in ScrumWorks
	 * after the synchronization. Afterwards, we will update the task and look
	 * whether the update goes through as well
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void testTaskCreationWithReplay() throws Exception {
		// first we have to create a PBI
		String title = "TFTask";
		String description = "TFTaskDescription";
		String status = TaskStatus.NOT_STARTED.getStatus();
		int remainingEffort = 0;
		int originalEstimate = 0;
		String assignedToUser = getTeamForgeTester().getUserName();

		getTeamForgeTester().createTaskAndPBI(title, description,
				status, assignedToUser, remainingEffort, originalEstimate).getId();

		// verify
		List<BacklogItem> pbis = getSWPTester().waitForBacklogItemsToAppear(1); 

		assertEquals(1, pbis.size());
		BacklogItem pbi = pbis.get(0);
		
		// now we wait until CCF replay comes into play
		// This is not optimal because it can happen that no artifact got quarantined at all
		// To write a better test, we have to remotely pause/resume CCF project mappings
		Task task = getSWPTester().waitForTaskToAppear(pbi, title, 1, CCF_ARTIFACT_REPLAY_TIMEOUT);

		assertEquals(title, task.getName());
		assertEquals(description, task.getDescription());
		assertEquals(status, task.getStatus());
		assertNull(task.getCurrentEstimate());
		assertNull(task.getOriginalEstimate());
		assertEquals(assignedToUser + " (" + assignedToUser + ")", task
				.getPointPerson());
		
	}

}
