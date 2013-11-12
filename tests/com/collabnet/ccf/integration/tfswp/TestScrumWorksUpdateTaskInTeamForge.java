package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api2.client.BacklogItem;
import com.danube.scrumworks.api2.client.Task;

/**
 * Tests updating an existing task in ScrumWorks and verifying that the task is
 * updated in TeamForge.
 * 
 * @author Kelley
 * 
 */
public class TestScrumWorksUpdateTaskInTeamForge extends TFSWPIntegrationTest {
    private String      release = SWPTester.RELEASE_1;
    private Task        task;
    private ArtifactRow teamForgeBacklogItem;
    private BacklogItem newBacklogItemForTask;

    /**
     * Creates a task in ScrumWorks.
     */
    @Before
    public void setUp() throws Exception {
        final String pbiTitle = "pbi";
        final String newBacklogItemForTaskTitle = "newPbiForTask";

        super.setUp();

        BacklogItem backlogItem = getSWPTester().createBacklogItem(pbiTitle,
                release);
        getTeamForgeTester().waitForBacklogItemsToAppear(1);
        task = getSWPTester().createTask("taskTitle", null, null,
                TaskStatus.NOT_STARTED, null, backlogItem.getId());
        getTeamForgeTester().waitForTasksToAppear(1);

        newBacklogItemForTask = getSWPTester().createBacklogItem(
                newBacklogItemForTaskTitle, release);
        ArtifactRow[] waitForBacklogItemsToAppear = getTeamForgeTester()
                .waitForBacklogItemsToAppear(2);
        for (int i = 0; i < waitForBacklogItemsToAppear.length; i++) {
            if (waitForBacklogItemsToAppear[i].getTitle().equals(
                    newBacklogItemForTaskTitle)) {
                teamForgeBacklogItem = waitForBacklogItemsToAppear[i];
            }
        }
    }

    /**
     * Tests updating fields in a task in ScrumWorks and verifies the task is
     * updated in TeamForge.
     * 
     * @throws Exception
     *             if an error occurs
     */
    @Test
    public void testUpdateTask() throws Exception {
        final String taskTitle = "title updated";
        final String description = "description";
        final String pointPerson = "a (a)";
        final String currentEstimate = "50";
        final String originalEstimate = "100";
        final TaskStatus status = TaskStatus.IN_PROGRESS;

        // execute
        //		TaskWSO taskToBeUpdated = new TaskWSO(task.getBacklogItemId(), 
        //				description, 
        //				Integer.parseInt(currentEstimate), 
        //				task.getId(), 
        //				Integer.parseInt(originalEstimate), 
        //				pointPerson, 
        //				task.getRank(), 
        //				status.getStatus(), 
        //				task.getTaskBoardStatusRank(), 
        //				taskTitle); 
        Task taskToBeUpdated = new Task();
        taskToBeUpdated.setBacklogItemId(newBacklogItemForTask.getId());
        taskToBeUpdated.setDescription(description);
        taskToBeUpdated.setCurrentEstimate(Integer.parseInt(currentEstimate));
        taskToBeUpdated.setOriginalEstimate(Integer.parseInt(originalEstimate));
        taskToBeUpdated.setId(task.getId());
        taskToBeUpdated.setPointPerson(pointPerson);
        taskToBeUpdated.setStatus(status.getStatus());
        taskToBeUpdated.setName(taskTitle);
        taskToBeUpdated.setRank(task.getRank());
        getSWPTester().updateTask(taskToBeUpdated);

        // verify 
        ArtifactRow teamForgeTask = getTeamForgeTester().waitForTaskToUpdate(
                taskTitle, 1);

        assertEquals(taskTitle, teamForgeTask.getTitle());
        assertEquals(description, teamForgeTask.getDescription());
        assertEquals(status.getStatus(), teamForgeTask.getStatus());
        assertEquals("", teamForgeTask.getCategory());
        assertEquals(0, teamForgeTask.getPriority());
        assertEquals(pointPerson, teamForgeTask.getAssignedToFullname());
        assertEquals(getTeamForgeTester().getPlanningFolderId(release),
                teamForgeTask.getPlanningFolderId());
        assertEquals(Integer.parseInt(originalEstimate),
                teamForgeTask.getEstimatedEffort());
        assertEquals(Integer.parseInt(currentEstimate),
                teamForgeTask.getRemainingEffort());

        assertEquals(teamForgeBacklogItem.getId(), getTeamForgeTester()
                .getParentId(teamForgeTask.getId()));

    }
}
