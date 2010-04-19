/**
 * 
 */
package com.collabnet.ccf.integration.tfswp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.collabnet.ce.soap50.webservices.cemain.TrackerFieldSoapDO;
import com.collabnet.teamforge.api.FieldValues;
import com.danube.scrumworks.api.client.types.BacklogItemWSO;
import com.danube.scrumworks.api.client.types.ProductWSO;
import com.danube.scrumworks.api.client.types.ReleaseWSO;
import com.danube.scrumworks.api.client.types.TaskWSO;
import com.danube.scrumworks.api.client.types.ThemeWSO;

/**
 * Creates a task item in TeamForge and verifies the task item in ScrumWorks after the synchronization. 
 * 
 * @author jnicolai
 */
public class TestTeamForgeCreateTaskInScrumWorks extends TFSWPIntegrationTest {

	/**
	 * Creates a task item in TeamForge and verifies the task item in ScrumWorks after the synchronization.   
	 * Fields for the backlog item: title, description, benefit, penalty, effort, release, and product themes.  
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testTaskCreation() throws Exception {
		// first we have to create a PBI
		String title = "TFTask";
		String description = "TFTaskDescription";
		String status = "Not Started";
		int remainingEffort = 42;
		String assignedToUser = getTeamForgeTester().getUserName(); 

		getTeamForgeTester().createTask(title, description, status, assignedToUser, remainingEffort);
		
		
		// verify
		final ProductWSO product = getSWPTester().getSWPEndpoint().getProductByName(getSWPTester().getSwpProduct());
		ReleaseWSO[] releases = getSWPTester().getSWPEndpoint().getReleases(product); 
		BacklogItemWSO[] pbis = null;
		for (int i = 0; i < getCcfMaxWaitTime(); i+=getCcfRetryInterval() ) {
			pbis = getSWPTester().getSWPEndpoint().getActiveBacklogItems(product);
			if (pbis == null) {
				Thread.sleep(getCcfRetryInterval());
			} else {
				break;
			}
		}
		
		assertEquals(1, pbis.length);
		BacklogItemWSO pbi = pbis[0];
		TaskWSO [] tasks = null;
		for (int i = 0; i < getCcfMaxWaitTime(); i+=getCcfRetryInterval() ) {
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
		assertEquals(remainingEffort, task.getEstimatedHours().intValue());
		assertEquals(assignedToUser+" ("+assignedToUser+")", task.getPointPerson());
	}

	/**
	 * Returns a list of the theme names. 
	 * 
	 * @param themes the theme web service objects
	 * @return the theme names
	 */
	private List<String> getThemeNames(final ThemeWSO[] themes) {
		final List<String> themeNames = new ArrayList<String>(); 
		for (int i = 0; i < themes.length; i++) {
			themeNames.add(themes[i].getName()); 
		}
		return themeNames; 
	}

	/**
	 * Returns the name of the release for the given backlog item. 
	 * 
	 * @param releases the releases 
	 * @param pbi the backlog item
	 * @return the name of the release if found, otherwise null
	 */
	private String getReleaseForBacklogItem(ReleaseWSO[] releases, Long releaseId) {
		for (int i = 0; i < releases.length; i++) {
			if (releases[i].getId().equals(releaseId)) {
				return releases[i].getTitle(); 
			}
		}
		return null;
	}


}
