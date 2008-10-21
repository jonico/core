package com.collabnet.ccf.pi.sfee.v44;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.types.SoapFilter;
import com.vasoftware.sf.soap44.types.SoapSortKey;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactDependencySoapRow;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactDetailSoapRow;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ITrackerAppSoap;
import com.vasoftware.sf.soap44.webservices.tracker.TrackerSoapList;
import com.vasoftware.sf.soap44.webservices.tracker.TrackerSoapRow;

/**
 * The tracker handler class provides support for listing and/or edit trackers
 * and artifacts.
 */
public class SFEETrackerHandler {
	/**
	 * Tracker Soap API handle
	 */
	private ITrackerAppSoap mTrackerApp;

	private static final Log log = LogFactory.getLog(SFEETrackerHandler.class);
	private static final Log logConflictResolutor = LogFactory.getLog("com.collabnet.ccf.core.conflict.resolution");
	
	/**
	 * Class constructor.
	 * 
	 * @param serverUrl - The source SFEE SOAP server URL
	 * @param connectionManager 
	 */
	public SFEETrackerHandler(String serverUrl, ConnectionManager<Connection> connectionManager) {
		// enable this if you do not like to have the retry code enabled
		
		if (connectionManager.isUseStandardTimeoutHandlingCode()) {
			mTrackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
				ITrackerAppSoap.class, serverUrl);
		}
		else {
			mTrackerApp = new TrackerAppSoapTimeoutWrapper(serverUrl,connectionManager);
		}
	}

	/**
	 * Get parent artifact dependencies for a certain artifact
	 * 
	 * @throws RemoteException
	 */
	public ArtifactDependencySoapRow[] getArtifactParentDependencies(
			String sessionId, String artifactId) throws RemoteException {
		ArtifactDependencySoapRow[] result = mTrackerApp
				.getParentDependencyList(sessionId, artifactId).getDataRows();
		return result;
	}

	/**
	 * Get child artifact dependencies for a certain artifact
	 * 
	 * @throws RemoteException
	 */
	public ArtifactDependencySoapRow[] getArtifactChildDependencies(
			String sessionId, String artifactId) throws RemoteException {
		ArtifactDependencySoapRow[] result = mTrackerApp
				.getChildDependencyList(sessionId, artifactId).getDataRows();
		return result;
	}

	/**
	 * Verifies tracker ID
	 * 
	 * @param sessionId
	 *            User session id.
	 * @param projectId
	 *            Project from which to list trackers.
	 * @return true if tracker ID could be verified
	 * @throws RemoteException
	 *             when an error is encountered in listing trackers.
	 * @throws IOException
	 *             when an error is encountered in reading user input.
	 */
	public boolean verifyTracker(String sessionId, String projectId,
			String trackerId) throws RemoteException, IOException {
		TrackerSoapList trackerList = mTrackerApp.getTrackerList(sessionId,
				projectId);
		TrackerSoapRow[] trackerRows = trackerList.getDataRows();

		if (trackerRows.length == 0) {
			throw new RemoteException("No trackers available!");
		}
		for (int i = 0; i < trackerRows.length; i++) {
			TrackerSoapRow trackerRow = trackerRows[i];
			if (trackerId.equals(trackerRow.getId())) {
				log.info("Pulling out artifacts from tracker item "
						+ trackerRow.getTitle());
				return true;
			}
		}
		return false;
	}

	/**
	 * Return all changed tracker items in a List
	 * 
	 * @param sessionID
	 * @param trackerId
	 * @param connectorUser 
	 * @return null if only duplicates were found, else a list of changed
	 *         tracker items
	 * @throws RemoteException
	 */
	public List<ArtifactSoapDO> getChangedTrackerItems(String sessionID,
			String trackerId, Date lastModifiedDate, String lastArtifactId,
			int lastArtifactVersion, String connectorUser) throws RemoteException {
		log.debug("Getting the changed artifacts from "+lastModifiedDate);
		// only select ID of row because we have to get the details in any case
		String[] selectedColumns = { ArtifactSoapDO.COLUMN_ID,
				ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE,
				ArtifactSoapDO.COLUMN_VERSION };
		SoapSortKey[] sortKeys = { new SoapSortKey(
				ArtifactSoapDO.COLUMN_LAST_MODIFIED_DATE, true) };
		SoapFilter[] filter = { new SoapFilter("modifiedAfter",
				SoapFilter.DATE_FORMAT.format(lastModifiedDate)) };
		ArtifactDetailSoapRow[] rows = mTrackerApp.getArtifactDetailList(
				sessionID, trackerId, selectedColumns, filter, sortKeys, 0, -1,
				false, true).getDataRows();
		if(rows != null){
			log.debug("There were " + rows.length +" artifacts changed");
		}
		ArrayList<ArtifactSoapDO> detailRowsFull = new ArrayList<ArtifactSoapDO>();
		ArrayList<ArtifactSoapDO> detailRowsNew = new ArrayList<ArtifactSoapDO>();
		// retrieve artifact details
		log.debug("Getting the details of the changed artifacts");
		boolean duplicateFound = false;
		if(rows != null) {
			for (int i = 0; i < rows.length; ++i) {
				String id = rows[i].getId();
				if (id.equals(lastArtifactId)
						&& lastArtifactVersion == rows[i].getVersion()) {
					duplicateFound = true;
				} else {
					ArtifactSoapDO artifactData = mTrackerApp.getArtifactData(
							sessionID, id);
					if(!artifactData.getLastModifiedBy().equals(connectorUser)){
						if (duplicateFound) {
							detailRowsNew.add(artifactData);
						}
						detailRowsFull.add(artifactData);
					}
				}
			}
		}
		if (!duplicateFound)
			return detailRowsFull;
		else if (detailRowsNew.isEmpty())
			return null;
		else
			return detailRowsNew;
	}

	/**
	 * Returns the artifact with the artifactId as id.
	 * 
	 * @param sessionID
	 * @param artifactId
	 * @return
	 * @throws RemoteException
	 */
	public ArtifactSoapDO getTrackerItem(String sessionID, String artifactId)
			throws RemoteException {
		return mTrackerApp.getArtifactData(sessionID, artifactId);
	}

	public void createArtifactDependency(String sessionId, String originId,
			String targetId, String description) throws RemoteException {
		mTrackerApp.createArtifactDependency(sessionId, originId, targetId,
				description);
	}

	public void removeArtifactDependency(String sessionId, String originId,
			String targetId) throws RemoteException {
		mTrackerApp.removeArtifactDependency(sessionId, originId, targetId);
	}

	public void removeArtifact(String sessionId, String artifactId)
			throws RemoteException {
		mTrackerApp.deleteArtifact(sessionId, artifactId);
	}

	/**
	 * Return all tracker items that have the value fieldValue in field
	 * 
	 * @param sessionID
	 * @param trackerId
	 * @param fieldname
	 * @param fieldValue
	 * @return
	 * @throws RemoteException
	 */
	public ArtifactDetailSoapRow[] getFilteredTrackerItems(String sessionID,
			String trackerId, String fieldname, String fieldValue)
			throws RemoteException {
		String[] selectedColumns = { ArtifactSoapDO.COLUMN_ID,
				ArtifactSoapDO.COLUMN_FLEX_FIELDS };
		SoapFilter[] filter = { new SoapFilter(fieldname, fieldValue) };
		ArtifactDetailSoapRow[] rows = mTrackerApp.getArtifactDetailList(
				sessionID, trackerId, selectedColumns, filter, null, 0, -1,
				false, false).getDataRows();
		return rows;
	}
	
	/**
	 * Returns the custom or flex fields for a particular tracker
	 * 
	 * @param sessionID
	 * @param trackerId
	 * @return
	 * @throws RemoteException
	 */
	public TrackerFieldSoapDO[] getFlexFields(String sessionID,
			String trackerId)
			throws RemoteException {
		TrackerFieldSoapDO[] rows = mTrackerApp.getFields(sessionID, trackerId);
		return rows;
	}

	/**
	 * Creates an artifact.
	 * 
	 * @throws RemoteException
	 *             when an error is encountered in creating the artifact.
	 * @return Newly created artifact
	 */
	public ArtifactSoapDO createArtifact(String sessionId, String trackerId,
			String description, String category, String group, String status,
			String statusClass, String customer, int priority,
			int estimatedHours, int actualHours, Date closeDate,
			String assignedTo, String reportedReleaseId,
			String resolvedReleaseId, List<String> flexFieldNames,
			List<Object> flexFieldValues, List<String> flexFieldTypes,
			String title, String[] comments)
			throws RemoteException {

		SoapFieldValues flexFields = new SoapFieldValues();
		flexFields.setNames(flexFieldNames.toArray(new String[0]));
		flexFields.setValues(flexFieldValues.toArray());
		flexFields.setTypes(flexFieldTypes.toArray(new String[0]));

		ArtifactSoapDO artifactData = mTrackerApp.createArtifact(sessionId,
				trackerId, title, description, group, category, // category
				status, // status
				customer, // customer
				priority, // priority
				estimatedHours, // estimatedHours
				assignedTo, // assigned user name
				reportedReleaseId, flexFields, null, null, null);
		artifactData.setActualHours(actualHours);
		artifactData.setStatusClass(statusClass);
		artifactData.setCloseDate(closeDate);
		artifactData.setResolvedReleaseId(resolvedReleaseId);
		SoapFieldValues newFlexFields = new SoapFieldValues();
		newFlexFields.setNames(flexFieldNames.toArray(new String[0]));
		newFlexFields.setValues(flexFieldValues.toArray());
		newFlexFields.setTypes(flexFieldTypes.toArray(new String[0]));
		artifactData.setFlexFields(newFlexFields);
		
		boolean initialUpdated = true;
		while (initialUpdated) {
			try {
				initialUpdated=false;
				mTrackerApp.setArtifactData(sessionId, artifactData, "Synchronized by Connector User", null,
						null, null);
			} catch (com.vasoftware.sf.soap44.fault.VersionMismatchFault e) {
				log.warn("Stale initial update, trying again ...:", e);
				artifactData.setVersion(artifactData.getVersion()+1);
				initialUpdated = true;
			}
		}
		
		for(String comment:comments) {
			boolean commentNotUpdated = true;
			while (commentNotUpdated) {
				try {
					commentNotUpdated = false;
					//artifactData = mTrackerApp.getArtifactData(sessionId, artifactData.getId());
					artifactData.setVersion(artifactData.getVersion()+1);
					mTrackerApp.setArtifactData(sessionId, artifactData, comment, null,
					null, null);
				} catch (com.vasoftware.sf.soap44.fault.VersionMismatchFault e) {
					log.warn("Stale comment update, trying again ...:", e);
					commentNotUpdated = true;
				}
			}
		}
		
		// we have to increase the version after the update
		// TODO Find out whether this really works if last modified date differs from actual last modified date
		artifactData.setVersion(artifactData.getVersion()+1);
		log.info("Artifact created: " + artifactData.getId());
		return artifactData;
	}

	/**
	 * Updates an artifact.
	 * 
	 * @throws RemoteException
	 *             when an error is encountered in creating the artifact.
	 * @return the Updated artifact's ArtifactSoapDO object
	 */
	public ArtifactSoapDO updateArtifact(String sessionId, String trackerId,
			String description, String category, String group, String status,
			String statusClass, String customer, int priority,
			int estimatedHours, int actualHours, Date closeDate,
			String assignedTo, String reportedReleaseId,
			String resolvedReleaseId, List<String> flexFieldNames,
			List<Object> flexFieldValues, List<String> flexFieldTypes,
			String title, String Id, String[] comments,
			boolean forceOverride)
			throws RemoteException {
		
		boolean mainArtifactNotUpdated = true;
		ArtifactSoapDO artifactData = null;
		while (mainArtifactNotUpdated) {
			try {
				mainArtifactNotUpdated = false;
				artifactData = mTrackerApp.getArtifactData(sessionId, Id);
				SoapFieldValues flexFields = new SoapFieldValues();
				flexFields.setNames(flexFieldNames.toArray(new String[0]));
				flexFields.setValues(flexFieldValues.toArray());
				flexFields.setTypes(flexFieldTypes.toArray(new String[0]));
				
				artifactData.setFolderId(trackerId);
				artifactData.setTitle(title);
				artifactData.setDescription(description);
				artifactData.setGroup(group);
				artifactData.setCategory(category);
				artifactData.setStatus(status);
				artifactData.setCustomer(customer);
				artifactData.setPriority(priority);
				artifactData.setEstimatedHours(estimatedHours);
				artifactData.setAssignedTo(assignedTo);
				artifactData.setReportedReleaseId(reportedReleaseId);
				artifactData.setFlexFields(flexFields);
				artifactData.setActualHours(actualHours);
				artifactData.setStatusClass(statusClass);
				artifactData.setCloseDate(closeDate);
				artifactData.setResolvedReleaseId(resolvedReleaseId);
				mTrackerApp.setArtifactData(sessionId, artifactData, "Synchronized by Connector User", null,
						null, null);
			} catch (com.vasoftware.sf.soap44.fault.VersionMismatchFault e) {
				if (forceOverride) {
					log.warn("Stale update, trying again ...:", e);
					mainArtifactNotUpdated = true;
				}
				else {
					
					logConflictResolutor
					.warn("Seems as if our copy of the artifact is too old to override a change for artifact with id: "
							+ artifactData.getId()+ " in tracker " + artifactData.getFolderId());
					return null;	
				}
			}
		}
		
		for(String comment:comments) {
			boolean commentNotUpdated = true;
			while (commentNotUpdated) {
				try {
					commentNotUpdated = false;
					//artifactData = mTrackerApp.getArtifactData(sessionId, Id);
					artifactData.setVersion(artifactData.getVersion()+1);
					mTrackerApp.setArtifactData(sessionId, artifactData, comment, null,
					null, null);
				} catch (com.vasoftware.sf.soap44.fault.VersionMismatchFault e) {
					log.warn("Stale comment update, trying again ...:", e);
					commentNotUpdated = true;
				}
			}
		}
		
		// we have to increase the version after the update
		// TODO Find out whether this really works if last modified date differs from actual last modified date
		artifactData.setVersion(artifactData.getVersion()+1);
		log.info("Artifact updated id: " + artifactData.getId()+ " in tracker " + artifactData.getFolderId());
		return artifactData;
	}

	/**
	 * Returns the value of the flex field with the name fieldName
	 * for the artifact with the artifact id artifactId
	 * 
	 * @param sessionID
	 * @param fieldName
	 * @param artifactID
	 * @param fieldType
	 * @return
	 * @throws RemoteException
	 */
	public Object getFlexFieldValue(String sessionID, String fieldName,
			String artifactID, String fieldType) throws RemoteException {
		ArtifactSoapDO artifact = getTrackerItem(sessionID, artifactID);
		return getFlexFieldValue(fieldName, artifact, fieldType);
	}

	/**
	 * Returns the value of the flex field with the name fieldName
	 * from the ArtifactSoapDO object
	 * 
	 * @param fieldName
	 * @param artifact
	 * @param fieldType
	 * @return
	 * @throws RemoteException
	 */
	public Object getFlexFieldValue(String fieldName, ArtifactSoapDO artifact,
			String fieldType) throws RemoteException {
		SoapFieldValues flexFields = artifact.getFlexFields();
		String[] flexFieldNames = flexFields.getNames();
		String[] flexFieldTypes = flexFields.getTypes();
		Object[] flexFieldValues = flexFields.getValues();
		for (int i = 0; i < flexFieldNames.length; ++i) {
			if (flexFieldNames[i].equals(fieldName)) {
				if (flexFieldTypes[i].equals(fieldType))
					return flexFieldValues[i];
				else {
					log.error("Wrong type of flexField " + fieldName
							+ ", expected: " + fieldType + " received: "
							+ flexFieldTypes[i] + " for artifactID: "
							+ artifact.getId());
					return null;
				}
			}
		}
		log.warn("flexField " + fieldName + " with type " + fieldType
				+ " is missing for artifact with ID " + artifact.getId());
		return null;
	}

	public GenericArtifact createGenericArtifactsForChild(String sessionId,
			ArtifactDependencySoapRow child, SFEEToGenericArtifactConverter artifactConverter,
			Date lastModifiedDate) throws RemoteException {
		String childArtifactId = child.getTargetId();
		String parentArtifactId = child.getOriginId();
		ArtifactSoapDO artifact = this.getTrackerItem(sessionId, childArtifactId);
		TrackerFieldSoapDO[] trackerFields = this.getFlexFields(sessionId, artifact.getFolderId());
		HashMap<String, List<TrackerFieldSoapDO>> fieldsMap = 
			SFEEAppHandler.loadTrackerFieldsInHashMap(trackerFields);
		//TODO As of now hard coding includeFieldMetaData to false. Should be changed when we include
		// dependencies.
		GenericArtifact ga = artifactConverter.convert(artifact, fieldsMap, lastModifiedDate, false);
		ga.setDepParentSourceArtifactId(parentArtifactId);
		ga.setSourceArtifactId(childArtifactId);
		ga.setSourceRepositoryId(artifact.getFolderId());
		ga.setArtifactType(GenericArtifact.ArtifactTypeValue.DEPENDENCY);
		return ga;
	}
}
