package com.collabnet.ccf.pi.sfee;

import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.types.SoapFilter;
import com.vasoftware.sf.soap44.types.SoapSortKey;
import com.vasoftware.sf.soap44.webservices.tracker.*;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The tracker handler class provides support for listing and/or edit trackers
 * and artifacts.
 */
public class SFEETrackerHandler {
	/** Tracker Soap API handle */
	private ITrackerAppSoap mTrackerApp;

	private static final Log log = LogFactory.getLog(SFEETrackerHandler.class);

	/**
	 * Class constructor.
	 * 
	 * @param serverUrl
	 *            Soap server URL.
	 */
	public SFEETrackerHandler(String serverUrl) {
		mTrackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
				ITrackerAppSoap.class, serverUrl);
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
	 * Return all changed tracker items in a map
	 * 
	 * @param sessionID
	 * @param trackerId
	 * @return null if only duplicates were found, else a list of changed
	 *         tracker items
	 * @throws RemoteException
	 */
	public List<ArtifactSoapDO> getChangedTrackerItems(String sessionID,
			String trackerId, Date lastModifiedDate, String lastArtifactId,
			int lastArtifactVersion) throws RemoteException {
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
				false, false).getDataRows();
		ArrayList<ArtifactSoapDO> detailRowsFull = new ArrayList<ArtifactSoapDO>();
		ArrayList<ArtifactSoapDO> detailRowsNew = new ArrayList<ArtifactSoapDO>();
		// retrieve artifact details
		// TODO Change this if user story is implemented

		/**
		 * We try to find the last entry from the last call of this function
		 * again If it is not in the list, then this artifact changed again and
		 * we have to return the whole list (this may result in some duplicates
		 * [artifacts modified before but in the same second, as our last item]
		 * we cannot detect currently) If it is unchanged in the list, we can
		 * ignore all items before (they are even older) If it is the last in
		 * the list, we will return null so that the calling component has the
		 * chance to increment the queuing time (it is all about performance)
		 * 
		 * Alternative: Increment lastModifedDate a second, but this may result
		 * in lost artifacts (that were created or modified in the same second
		 * the last call took place)
		 */
		boolean duplicateFound = false;
		for (int i = 0; i < rows.length; ++i) {
			String id = rows[i].getId();
			if (id.equals(lastArtifactId)
					&& lastArtifactVersion == rows[i].getVersion()) {
				duplicateFound = true;
			} else {
				ArtifactSoapDO artifactData = mTrackerApp.getArtifactData(
						sessionID, id);
				if (duplicateFound) {
					detailRowsNew.add(artifactData);
				}
				detailRowsFull.add(artifactData);
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
	 * Return all changed tracker items in a map
	 * 
	 * @param sessionID
	 * @param trackerId
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
	 * Return all tracker items that have the value fieldValue in field field
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
			String title, String comment,
			String lastSynchronizedWithOtherSystemSFEETargetFieldname)
			throws RemoteException {

		// TODO: Make conflict detection optional?
		
		// first of all, the lastSynchronized flexField to the existing
		// values
		flexFieldNames.add(lastSynchronizedWithOtherSystemSFEETargetFieldname);
		// TODO: Only works if created artifacts starts with number 100
		flexFieldValues.add("100");
		flexFieldTypes.add("String");

		// construct SOAPFieldValues
		SoapFieldValues flexFields = new SoapFieldValues();
		flexFields.setNames(flexFieldNames.toArray(new String[0]));
		flexFields.setValues(flexFieldValues.toArray());
		flexFields.setTypes(flexFieldTypes.toArray(new String[0]));

		// TODO Support attachments, consider setting version as well?
		ArtifactSoapDO artifactData = mTrackerApp.createArtifact(sessionId,
				trackerId, title, description, group, category, // category
				status, // status
				customer, // customer
				priority, // priority
				estimatedHours, // estimatedHours
				assignedTo, // assigned user name
				reportedReleaseId, flexFields, null, null, null);

		// update missing fields and adjust lastSynchronizedValue
		// TODO This is not atomic, and may override changes in the target system, wait for better API on SFEE 5.c
		artifactData.setActualHours(actualHours);
		artifactData.setStatusClass(statusClass);
		artifactData.setCloseDate(closeDate);
		artifactData.setResolvedReleaseId(resolvedReleaseId);
		// adjust the lastSynchronizedValue
		flexFieldValues.remove(flexFieldValues.size() - 1);
		flexFieldValues.add(Integer.toString(artifactData.getVersion()+1));
		// construct new flex fields
		SoapFieldValues newFlexFields = new SoapFieldValues();
		newFlexFields.setNames(flexFieldNames.toArray(new String[0]));
		newFlexFields.setValues(flexFieldValues.toArray());
		newFlexFields.setTypes(flexFieldTypes.toArray(new String[0]));
		artifactData.setFlexFields(newFlexFields);

		mTrackerApp.setArtifactData(sessionId, artifactData, comment, null,
				null, null);
		log.info("Artifact created: " + artifactData.getId());
		return artifactData;
	}

	/**
	 * Updates an artifact.
	 * 
	 * @throws RemoteException
	 *             when an error is encountered in creating the artifact.
	 * @return Newly created artifact
	 */
	public ArtifactSoapDO updateArtifact(String sessionId, String trackerId,
			String description, String category, String group, String status,
			String statusClass, String customer, int priority,
			int estimatedHours, int actualHours, Date closeDate,
			String assignedTo, String reportedReleaseId,
			String resolvedReleaseId, List<String> flexFieldNames,
			List<Object> flexFieldValues, List<String> flexFieldTypes,
			String title, String Id, String comment,
			String lastSynchronizedWithOtherSystemSFEETargetFieldname,
			String otherSystemVersion,
			String otherSystemVersionFieldName,
			String fallbackVersion,
			boolean forceOverride)
			throws RemoteException {
		
		// TODO: Make conflict detection optional?
		
		
		boolean tryItAgain = true;
		ArtifactSoapDO artifactData = null;
		int fallbackVersionInt=-1;
		if (StringUtils.isNotEmpty(fallbackVersion)) {
			try {
				fallbackVersionInt=Integer.parseInt(fallbackVersion);
			}
			catch (NumberFormatException e) {
				log.error("version-field for artifact with id "+Id+" in tracker "+trackerId+" contained non-numeric value",e);
				return null;
			}
		}
		else {
			fallbackVersion="-1";
		}
		
		// this loop enables us to update even if a stale update method occured
		while (tryItAgain) {
			try {
				tryItAgain = false;
				// TODO Support attachments
				artifactData = mTrackerApp.getArtifactData(sessionId, Id);
				/**
				 * Only after having retrieved the artifact data, we can be sure
				 * that it we do not miss a change on it This is very important
				 * for atomic updates
				 */
				try {
					/**
					 * Compare versions of the software artifacts
					 */
					String version=(String)getFlexFieldValue(lastSynchronizedWithOtherSystemSFEETargetFieldname, artifactData, "String");
					if (StringUtils.isEmpty(version)) {
						log
						.warn("Seems as if we lost version-information in flexField "+lastSynchronizedWithOtherSystemSFEETargetFieldname+ " in the target system for artifact with id: "
								+ artifactData.getId()+ " in tracker "+artifactData.getFolderId()+" or it is the first resync, falling back to value in the version-Field");
						version=fallbackVersion;
						if (fallbackVersionInt==-1) {
							log
							.error("Seems as if we lost version-information in version-Field in the source system for target artifact with id: "
									+ artifactData.getId()+" in tracker "+artifactData.getFolderId());
							if (forceOverride) {
								log.warn("Since we should override changes, we still try to update the artifact with id: "+artifactData.getId());
								version="-1";
							}
							else {
								log.error("Since we should not override changes, we do not update the artifact with id: "+artifactData.getId()+" in tracker "+artifactData.getFolderId());
								return null;
							}
						}
					}
					
					int versionInt = Integer.parseInt(version);
					// version is the max(ourOwnLastSynchronizedVersionWithOtherSystem,otherSystemLastSynchronizedVersionWithUs)
					versionInt=versionInt>fallbackVersionInt?versionInt:fallbackVersionInt;
					if (versionInt < artifactData.getVersion()) {
						// TODO Rethink conflict resolution
						if (forceOverride) {
							log
									.warn("Seems as if we are going to override a change for artifact with id: "
											+ artifactData.getId()
											+ " in tracker " + artifactData.getFolderId()
											+ " and version: "
											+ artifactData.getVersion()
											+ ", last synchronized version: "
											+ versionInt);
						} else {
							log
									.warn("Seems as if our copy of the artifact is too old to override a change for artifact with id: "
											+ artifactData.getId()
											+ " in tracker " + artifactData.getFolderId()
											+ " and version: "
											+ artifactData.getVersion()
											+ ", last synchronized version: "
											+ versionInt);
							return null;
						}
					}
					
					String currentOtherSystemVersion = (String) getFlexFieldValue(
							otherSystemVersionFieldName, artifactData, "String");
					if (StringUtils.isEmpty(currentOtherSystemVersion)
							|| StringUtils.isEmpty(otherSystemVersion)) {
						log
								.warn("Seems as if we lost version-information in the target system for artifact with id: "
										+ artifactData.getId()
										+ " in tracker " + artifactData.getFolderId()
										+ " or it is the first resync.");
					} else {
						int currentOtherSystemVersionInt = Integer
								.parseInt(currentOtherSystemVersion);
						int otherSystemVersionInt = Integer
								.parseInt(otherSystemVersion);
						if (otherSystemVersionInt <= currentOtherSystemVersionInt) {
							log
									.warn("Seems as if we updated the target system before for artifact with id: "
											+ artifactData.getId()
											+ " in tracker " + artifactData.getFolderId()
											+ " and current other system version: "
											+ currentOtherSystemVersionInt
											+ ", stored in flexField "
											+ otherSystemVersionFieldName
											+ " ,older or equal version: "
											+ otherSystemVersionInt);
							return null;
						}
					}
				} catch (NumberFormatException e) {
					log.error(
							"Version information not in numerical format for artifact with id: "
									+ artifactData.getId()+ " in tracker " + artifactData.getFolderId(), e);
					return null;
				}
				
				// add the lastSynchronized flexField to the existing
				// values
				flexFieldNames.add(lastSynchronizedWithOtherSystemSFEETargetFieldname);
				flexFieldValues.add(Integer.toString(artifactData.getVersion()+1));
				flexFieldTypes.add("String");

				// construct SOAPFieldValues
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
				mTrackerApp.setArtifactData(sessionId, artifactData, comment,
						null, null, null);
			} catch (com.vasoftware.sf.soap44.fault.VersionMismatchFault e) {
				if (forceOverride) {
					log.warn("Stale update, trying again ...:", e);
					// remove lastSynchronized flexField again
					flexFieldNames.remove(flexFieldNames.size() - 1);
					flexFieldValues.remove(flexFieldValues.size() - 1);
					flexFieldTypes.remove(flexFieldTypes.size() - 1);
					tryItAgain = true;
				}
				else {
					log
					.warn("Seems as if our copy of the artifact is too old to override a change for artifact with id: "
							+ artifactData.getId()+ " in tracker " + artifactData.getFolderId());
					return null;	
				}
			}
		}
		log.info("Artifact updated id: " + artifactData.getId()+ " in tracker " + artifactData.getFolderId());
		return artifactData;
	}

	public Object getFlexFieldValue(String sessionID, String fieldName,
			String artifactID, String fieldType) throws RemoteException {
		// first retrieve artifact
		ArtifactSoapDO artifact = getTrackerItem(sessionID, artifactID);
		return getFlexFieldValue(fieldName, artifact, fieldType);
	}

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
							+ flexFieldTypes[i] + " artifactID: "
							+ artifact.getId());
					return null;
				}
			}
		}
		log.warn("flexField " + fieldName + " with type " + fieldType
				+ " is missing for artifact with ID " + artifact.getId());
		return null;
	}
}
