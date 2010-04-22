/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.collabnet.teamforge.api.FieldValues;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.TaskWSO;

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
		String status = "Not Started";
		int remainingEffort = 0;
		int originalEstimate = 0;
		String assignedToUser = getTeamForgeTester().getUserName();

		ArtifactDO taskTF = getTeamForgeTester().createTask(title, description,
				status, assignedToUser, remainingEffort, originalEstimate);

		// verify
		BacklogItemWSO[] pbisSWP = getSWPTester().waitForBacklogItemsToAppear(1); 

		assertEquals(1, pbisSWP.length);
		BacklogItemWSO firstSWPPBI = pbisSWP[0];
		
		// now that we can be sure that PBI has been created, update task again to trigger resynch
		getTeamForgeTester().updateTask(taskTF.getId(), title, description,
				status, assignedToUser, remainingEffort, originalEstimate);

		TaskWSO[] tasksSWP = null;
		tasksSWP = getSWPTester().waitForTaskToAppear(firstSWPPBI, title, 1); 

		assertEquals(1, tasksSWP.length);
		TaskWSO taskSWP = tasksSWP[0];
		
		// now we create a second PBI without any tasks
		FieldValues flexFields = new FieldValues();
		flexFields.setNames(new String[] {});
		flexFields.setTypes(new String[] {});
		flexFields.setValues(new String[] {});
		ArtifactDO secondTFPBI = getTeamForgeTester().createBacklogItem("SecondPBI", "Second task parent", null,
				flexFields);
		
		// wait until second pbi got created
		pbisSWP = getSWPTester().waitForBacklogItemsToAppear(2);
		assertEquals(2, pbisSWP.length);
		
		BacklogItemWSO secondSWPPBI = pbisSWP[1];
		
		// figure out the second SWP PBI
		if (!pbisSWP[0].getBacklogItemId().equals(firstSWPPBI.getBacklogItemId())) {
			secondSWPPBI = pbisSWP[0];
		}
		assertEquals("SecondPBI", secondSWPPBI.getTitle()); 
		
		// now we move the task to the second PBI
		getTeamForgeTester().reparentTask(taskTF.getId(), secondTFPBI.getId());
		
		// update task to trigger reparenting in SWP
		String newTitle = "Task has moved";
		getTeamForgeTester().updateTask(taskTF.getId(), newTitle, description, status, assignedToUser, remainingEffort, originalEstimate);
		
		
		// wait for title change to show up
		taskSWP = getSWPTester().waitForTaskToAppear(secondSWPPBI, newTitle, 1)[0]; 
		assertEquals(newTitle, taskSWP.getTitle());
		
		// check whether move operation took place
		assertEquals(secondSWPPBI.getBacklogItemId(), taskSWP.getBacklogItemId());
		
		// first SWP PBI should not have any children any more
		tasksSWP = getSWPTester().getSWPEndpoint().getTasks(firstSWPPBI);
		assertNull(tasksSWP);
		
		// second SWP PBI should have one child now
		tasksSWP = getSWPTester().getSWPEndpoint().getTasks(secondSWPPBI);
		assertEquals(1, tasksSWP.length);
		assertEquals(taskSWP.getId(), tasksSWP[0].getId());
	}

}
