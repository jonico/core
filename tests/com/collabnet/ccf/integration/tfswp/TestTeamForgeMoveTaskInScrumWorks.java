/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.Task;

/**
 * Creates a task item in TeamForge and moves it between PBIs (parent artifacts)
 * Checks whether task also moves in ScrumWorks.
 * 
 * @author jnicolai
 */
public class TestTeamForgeMoveTaskInScrumWorks extends TFSWPIntegrationTest {

	/**
	 * Creates a task item in TeamForge and verifies the task item in ScrumWorks
	 * after the synchronization. Afterwards, we will update the task and look
	 * whether the update goes through as well
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void testTaskMove() throws Exception {
		// first we have to create a task in its first parent
		String title = "TFTask";
		String description = "TFTaskDescription";
		String status = TaskStatus.NOT_STARTED.getStatus();
		int remainingEffort = 0;
		int originalEstimate = 0;
		String assignedToUser = getTeamForgeTester().getUserName();

		ArtifactDO taskTF = getTeamForgeTester().createTaskAndPBI(title, description,
				status, assignedToUser, remainingEffort, originalEstimate);

		// verify
		List<BacklogItem> pbisSWP = getSWPTester().waitForBacklogItemsToAppear(1); 

		assertEquals(1, pbisSWP.size());
		BacklogItem firstSWPPBI = pbisSWP.get(0);
		
		// now that we can be sure that PBI has been created, update task again to trigger resynch
		getTeamForgeTester().updateTask(taskTF.getId(), title, description,
				status, assignedToUser, remainingEffort, originalEstimate);

		Task taskSWP = getSWPTester().waitForTaskToAppear(firstSWPPBI, title, 1, null);
		
		// now we create a second PBI without any tasks
		FieldValues flexFields = new FieldValues();
		flexFields.setNames(new String[] {});
		flexFields.setTypes(new String[] {});
		flexFields.setValues(new String[] {});
		ArtifactDO secondTFPBI = getTeamForgeTester().createBacklogItem("SecondPBI", "Second task parent", null,
				flexFields, 0);
		
		// wait until second pbi got created
		pbisSWP = getSWPTester().waitForBacklogItemsToAppear(2);
		assertEquals(2, pbisSWP.size());
		
		BacklogItem secondSWPPBI = pbisSWP.get(1);
		
		// figure out the second SWP PBI
		if (!pbisSWP.get(0).getId().equals(firstSWPPBI.getId())) {
			secondSWPPBI = pbisSWP.get(0);
		}
		assertEquals("SecondPBI", secondSWPPBI.getName()); 
		
		// now we move the task to the second PBI
		getTeamForgeTester().reparentTask(taskTF.getId(), secondTFPBI.getId());
		
		// update task to trigger reparenting in SWP
		String newTitle = "Task has moved";
		getTeamForgeTester().updateTask(taskTF.getId(), newTitle, description, status, assignedToUser, remainingEffort, originalEstimate);
		
		
		// wait for title change to show up
		taskSWP = getSWPTester().waitForTaskToAppear(secondSWPPBI, newTitle, 1, null); 
		assertEquals(newTitle, taskSWP.getName());
		
		// check whether move operation took place
		assertEquals(secondSWPPBI.getId(), taskSWP.getBacklogItemId());
		
		// first SWP PBI should not have any children any more
		List<Task> tasksSWP = null;
		tasksSWP = getSWPTester().getSWPEndpoint().getTasks(firstSWPPBI.getId());
		assertTrue(tasksSWP.isEmpty());
		
		// second SWP PBI should have one child now
		tasksSWP = getSWPTester().getSWPEndpoint().getTasks(secondSWPPBI.getId());
		assertEquals(1, tasksSWP.size());
		assertEquals(taskSWP.getId(), tasksSWP.get(0).getId());
	}

}
