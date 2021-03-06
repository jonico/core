package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.teamforge.api.tracker.ArtifactRow;
import com.danube.scrumworks.api2.client.BacklogItem;

/**
 * Tests that a task created in ScrumWorks is correctly synchronized in
 * TeamForge and deleted again if deleted in ScrumWorks.
 * 
 * @author Kelley
 * 
 */
public class TestScrumWorksCreateAndDeleteTaskInTeamForge extends TFSWPIntegrationTest {
    private BacklogItem  scrumWorksBacklogItem;
    private final String release = SWPTester.RELEASE_1;

    @Override
    @Before
    public void setUp() throws Exception {
        final String pbiTitle = "pbi";

        super.setUp();

        scrumWorksBacklogItem = getSWPTester().createBacklogItem(pbiTitle,
                release);
        // before back log item did not appear, we could run in a
        // parentArtifactNotPresentError
        getTeamForgeTester().waitForBacklogItemsToAppear(1);
    }

    /**
     * Tests creating a task in ScrumWorks and verifying that the task is
     * created in TeamForge.
     * 
     * @throws Exception
     *             if an error occurs
     */
    @Test
    public void testCreateTask() throws Exception {
        final String taskTitle = "title";
        final String description = "description";
        final String pointPerson = "Kelley (Kelley)";
        final String currentEstimate = "50";
        final TaskStatus status = TaskStatus.IN_PROGRESS;

        // execute
        getSWPTester().createTask(taskTitle, description, pointPerson, status,
                currentEstimate, scrumWorksBacklogItem.getId());

        // verify
        final ArtifactRow[] allTasks = getTeamForgeTester()
                .waitForTasksToAppear(1);
        final ArtifactRow task = allTasks[0];

        assertEquals(taskTitle, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(status.getStatus(), task.getStatus());
        assertEquals("", task.getCategory());
        assertEquals(0, task.getPriority());
        assertEquals(pointPerson, task.getAssignedToFullname());
        assertEquals(getTeamForgeTester().getPlanningFolderId(release),
                task.getPlanningFolderId());
        assertEquals(Integer.parseInt(currentEstimate),
                task.getEstimatedEffort());
        assertEquals(Integer.parseInt(currentEstimate),
                task.getRemainingEffort());

        final ArtifactRow[] allBacklogItems = getTeamForgeTester()
                .waitForBacklogItemsToAppear(1);
        final ArtifactRow backlogItem = allBacklogItems[0];
        assertEquals(backlogItem.getId(),
                getTeamForgeTester().getParentId(task.getId()));

        // now delete task again
        getSWPTester().deleteAllTasksInSWP();
        getTeamForgeTester().waitForTasksToDisappear(0);

    }

}
