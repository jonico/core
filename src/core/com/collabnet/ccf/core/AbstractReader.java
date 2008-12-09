package com.collabnet.ccf.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;

/**
 * AbstractReader provides the abstraction of shipping Generic Artifacts through
 * the wiring components.
 * 	It provides three methods for the plugin developers to implement. The plugin
 * developer should implement these methods by extending the AbstractReader.
 *
 * The AbstractReader then gets the changed artifacts from the implemented methods
 * and sorts them according to their last modified date and sends them across.
 * It sends the artifacts one per Open Adaptor cycle.
 *
 * The process method implements the streaming logic.
 *
 * @author madhusuthanan (madhusuthanan@collab.net)
 *
 */
public abstract class AbstractReader<T> extends LifecycleComponent implements IDataProcessor {
	private static final Log log = LogFactory.getLog(AbstractReader.class);
	private HashMap<String, RepositoryRecord> repositoryRecordHashMap = null;
	private ArrayList<RepositoryRecord> repositorySynchronizationWaitingList = null;
	private HashSet<String> repositoryRecordsInRepositorySynchronizationWaitingList = null;
	private long sleepInterval = -1;
	private boolean shipAttachments = true;
	private boolean shipAttachmentsWithArtifact = false;
	private boolean includeFieldMetaData = false;
	private Comparator<GenericArtifact> genericArtifactComparator = null;
	public static final long DEFAULT_MAX_ATTACHMENT_SIZE_PER_ARTIFACT = 10 * 1024 * 1024;
	private long maxAttachmentSizePerArtifact = DEFAULT_MAX_ATTACHMENT_SIZE_PER_ARTIFACT;
	private ConnectionManager<T> connectionManager;
	/**
	 * If the restart connector variable is set to true, all readers will begin
	 * to flush their buffers and exit with a special error code (42) that will
	 * cause service wrapper to restart the connector. 
	 */
	private static boolean restartConnector=false;
	
	/**
	 * If the shutDownConnector variable is set, this will cause service wrapper to flush all the buffers
	 * and signal the shutdown hook thread that it is ready to exit
	 */
	private static boolean shutDownConnector=false;

	public AbstractReader(){
		super();
		init();
	}
	public AbstractReader(String id){
		super(id);
		init();
	}

	/**
	 * Initializes the Reader with an empty repository records HashMap.
	 * The repositories synchronization waiting list and the repository ids in
	 * the waiting list are also initialized.
	 * It will also create a shutdownHookListener that will set the shutDownConnector variable
	 * It also creates a comparator that will be used to compare a set of
	 * GenericArtifacts.
	 * The comparator compares the GenericArtifacts according to the last
	 * modified date of the artifacts.
	 */
	private void init(){
		log.debug("Initializing the AbstractReader");
		repositoryRecordHashMap = new HashMap<String, RepositoryRecord>();
		repositorySynchronizationWaitingList = new ArrayList<RepositoryRecord>();
		repositoryRecordsInRepositorySynchronizationWaitingList = new HashSet<String>();
		genericArtifactComparator = new Comparator<GenericArtifact>(){
			public int compare(GenericArtifact first, GenericArtifact second) {
				String firstLastModifiedDateStr = first.getSourceArtifactLastModifiedDate();
				Date firstLastModifiedDate = DateUtil.parse(firstLastModifiedDateStr);

				String secondLastModifiedDateStr = first.getSourceArtifactLastModifiedDate();
				Date secondLastModifiedDate = DateUtil.parse(secondLastModifiedDateStr);
				if(firstLastModifiedDate.after(secondLastModifiedDate)){
					return 1;
				}
				else if(firstLastModifiedDate.before(secondLastModifiedDate)){
					return -1;
				}
				else{
					String firstSourceArtifactId = null;
					if(first.getArtifactType() == GenericArtifact.ArtifactTypeValue.PLAINARTIFACT){
						firstSourceArtifactId = first.getSourceArtifactId();
					}
					else {
						firstSourceArtifactId = first.getDepParentSourceArtifactId();
					}
					String secondSourceArtifactId = null;
					if(second.getArtifactType() == GenericArtifact.ArtifactTypeValue.PLAINARTIFACT){
						secondSourceArtifactId = second.getSourceArtifactId();
					}
					else {
						secondSourceArtifactId = second.getDepParentSourceArtifactId();
					}
					if(firstSourceArtifactId.equals(secondSourceArtifactId)){
						if(first.getArtifactType() == GenericArtifact.ArtifactTypeValue.PLAINARTIFACT){
							return -1;
						}
						else {
							return 1;
						}
					}
					else {
						return firstSourceArtifactId.compareTo(secondSourceArtifactId);
					}
				}
			}
		};
	}

	/**
	 * The process method queues the sync info documents from the sync info
	 * readers. It then takes each repository one by one and asks the repository
	 * reader (which is a sub class to this AbstractReader) if there are any
	 * changed artifacts.
	 * If the reader gives a list of artifacts that are changed then the AbstractReader
	 * requests the repository reader to get the data of the changed artifacts one by one
	 * along with the dependent artifact data such as attachments and dependent artifacts
	 * in the Generic Artifact xml format.
	 * On getting these data the abstract reader emits the artifacts one by one to the next
	 * component in the pipeline. It does this for each repository alternatively so that all
	 * the repositories get equal chance to ship their artifacts.
	 * If there are no artifacts to be shipped for all of the repositories configured
	 * then the AbstractReader pauses the processing for sleepInterval milliseconds.
	 */
	public Object[] process(Object data) {
		Document syncInfoIn = null;
		if(data instanceof Document){
			syncInfoIn = (Document) data;
		} else {
			return null;
		}
		
		if (getAutoRestartPeriod() > 0) {
			if (new Date().getTime() - startedDate.getTime() > getAutoRestartPeriod()) {
				log.info("Preparing to restart CCF, flushing buffers ...");
				setRestartConnector(true);
			}
		}
		
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfoIn);
		log.debug("Received the SyncInfo for repository with id " + sourceRepositoryId);
		RepositoryRecord record = repositoryRecordHashMap.get(sourceRepositoryId);
		if(record == null){
			log.debug("No RepositoryRecord available for "+sourceRepositoryId
					+". Creating one and registering");
			record = new RepositoryRecord(sourceRepositoryId, syncInfoIn);
			repositoryRecordHashMap.put(sourceRepositoryId, record);
		}
		else {
//			record.setSyncInfo(syncInfoIn);
		}
		if(!repositoryRecordsInRepositorySynchronizationWaitingList.contains(sourceRepositoryId)){
			log.debug(sourceRepositoryId + " is not on the waiting list. Adding....");
			repositorySynchronizationWaitingList.add(0, record);
			repositoryRecordsInRepositorySynchronizationWaitingList.add(sourceRepositoryId);
		}
		RepositoryRecord currentRecord = null;
		while(!repositorySynchronizationWaitingList.isEmpty()){
			//currentRecord = repositorySynchronizationWaitingList.get(0);
			currentRecord = repositorySynchronizationWaitingList.get(0);
			log.debug("Processing the current repository " + sourceRepositoryId +" record");
			Document syncInfo = currentRecord.getSyncInfo();
			//RepositoryRecord movedRecord = repositorySynchronizationWaitingList.remove(0);
			//repositorySynchronizationWaitingList.add(movedRecord);
			List<GenericArtifact> artifactsToBeShippedList = currentRecord.getArtifactsToBeShippedList();
			List<String> artifactsToBeReadList = currentRecord.getArtifactsToBeReadList();
			if(!artifactsToBeShippedList.isEmpty()){
				log.debug("There are "+artifactsToBeShippedList.size()+" artifacts to be shipped.");
				GenericArtifact genericArtifact = artifactsToBeShippedList.remove(0);
//				if(artifactsToBeShippedList.isEmpty()){
//					repositorySynchronizationWaitingList.remove(currentRecord);
//					repositorySynchronizationWaitingList.add(currentRecord);
//				}
				String artifactId = genericArtifact.getSourceArtifactId();
				try {
					String conflictResolution = this.getConflictResolutionPriority(syncInfo);
					genericArtifact.setConflictResolutionPriority(conflictResolution);
					genericArtifact.setSourceSystemTimezone(this.getSourceSystemTimezone(syncInfo));
					genericArtifact.setTargetSystemTimezone(this.getTargetSystemTimezone(syncInfo));
					//genericArtifact.setSourceSystemEncoding(this.getSourceSystemEncoding(syncInfo));
					//genericArtifact.setTargetSystemEncoding(this.getTargetSystemEncoding(syncInfo));
					Document returnDoc = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
					Object[] returnObjects = new Object[] {returnDoc};
					moveToTail(currentRecord);
					return returnObjects;
				} catch (GenericArtifactParsingException e) {
					String cause = "Could not parse the artifact for "+artifactId;
					log.error(cause,e);
					genericArtifact.setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
					throw new CCFRuntimeException(cause, e);
				}
			}
			else if(artifactsToBeReadList.isEmpty() && !isRestartConnector() && !isShutDownConnector()){
				log.debug("There are no artifacts to be read. Checking if there are"+
						" changed artifacts in repository " + sourceRepositoryId);
				currentRecord.setSyncInfo(syncInfoIn);
				syncInfo = currentRecord.getSyncInfo();
				// TODO Does it make sense to insert retry code here or is it better just to try it again later?
				int numberOfTries = 1;
				boolean retry = false;
				List<String> artifactsToBeRead = null;
				do{
					int msToSleep = (numberOfTries - 1)	* connectionManager.getRetryIncrementTime();
					int maxMsToSleep = connectionManager.getMaximumRetryWaitingTime();
					try {
						artifactsToBeRead = this.getChangedArtifacts(syncInfo);
					} catch(Exception e){
						boolean connectionException = connectionManager.isUseStandardTimeoutHandlingCode() && this.handleException(e, connectionManager);
						if(!connectionException){
							retry = false;
							if(e instanceof CCFRuntimeException){
								throw (CCFRuntimeException)e;
							}
							else if(e instanceof RuntimeException) {
								throw (RuntimeException)e;
							}
							else {
								throw new CCFRuntimeException("An exception occured", e);
							}
						}
						else {
							retry = true;
							if (numberOfTries == 1) {
								// first try, long error message
								log
										.warn(
												"Network related problem occurred while connecting to external system. Try operation again",
												e);
							} else if (msToSleep < maxMsToSleep) {
								// error occurred again, short error message, go to sleep
								// we switched to a linear increase of the timeout value, may
								// have to revisit this decision later
								// int timeOut = (int) Math.pow(2, numberOfTries);
								log.warn("Network related error occurred again ("
										+ e.getMessage()
										+ "), incremented timeout, now sleeping for "
										+ msToSleep + " milliseconds.");
								try {
									Thread.sleep(msToSleep);
								} catch (InterruptedException e1) {
									log.error("Interrupted sleep in timeout method: ", e1);
								}
							} else {
								log
										.warn("Network related error occurred again, switched to maximum waiting time ("
												+ e.getMessage()
												+ "), sleeping for "
												+ maxMsToSleep + " milliseconds.");
								try {
									Thread.sleep(maxMsToSleep);
								} catch (InterruptedException e1) {
									log.error("Interrupted sleep in timeout method: ", e1);
								}
							}
						}
					}
					++numberOfTries;
				} while(retry);
				artifactsToBeReadList.addAll(artifactsToBeRead);
			}
			if(!artifactsToBeReadList.isEmpty() && !isRestartConnector() && !isShutDownConnector()) {
				log.debug("There are "+artifactsToBeReadList.size()+
						"artifacts to be read.");
				String artifactId = artifactsToBeReadList.remove(0);
				log.debug("Getting the data for artifact "+artifactId);
				int numberOfTries = 1;
				List<GenericArtifact> sortedGAs = null;
				boolean retry = false;
				do{
					int msToSleep = (numberOfTries - 1)	* connectionManager.getRetryIncrementTime();
					int maxMsToSleep = connectionManager.getMaximumRetryWaitingTime();
					try {
						GenericArtifact artifactData = this.getArtifactData(syncInfo, artifactId);
						if(artifactData != null) {
							List<GenericArtifact> artifactAttachments = null;
							if(shipAttachments){
								artifactAttachments = this.getArtifactAttachments(syncInfo, artifactData);
							} else {
								artifactAttachments = new ArrayList<GenericArtifact>();
							}
							List<GenericArtifact> artifactDependencies = this.getArtifactDependencies(syncInfo, artifactId);

							sortedGAs = combineAndSort(artifactData,artifactAttachments,artifactDependencies);
						}
						else {
							sortedGAs = new ArrayList<GenericArtifact>();
						}
					} catch(Exception e){
						boolean connectionException = connectionManager.isUseStandardTimeoutHandlingCode() && this.handleException(e, connectionManager);
						if(!connectionException){
							retry = false;
							if(e instanceof CCFRuntimeException){
								throw (CCFRuntimeException)e;
							}
							else if(e instanceof RuntimeException) {
								throw (RuntimeException)e;
							}
							else {
								throw new CCFRuntimeException("An exception occured", e);
							}
						}
						else {
							retry = true;
							if (numberOfTries == 1) {
								// first try, long error message
								log
										.warn(
												"Network related problem occurred while connecting to external system. Try operation again",
												e);
							} else if (msToSleep < maxMsToSleep) {
								// error occurred again, short error message, go to sleep
								// we switched to a linear increase of the timeout value, may
								// have to revisit this decision later
								// int timeOut = (int) Math.pow(2, numberOfTries);
								log.warn("Network related error occurred again ("
										+ e.getMessage()
										+ "), incremented timeout, now sleeping for "
										+ msToSleep + " milliseconds.");
								try {
									Thread.sleep(msToSleep);
								} catch (InterruptedException e1) {
									log.error("Interrupted sleep in timeout method: ", e1);
								}
							} else {
								log
										.warn("Network related error occurred again, switched to maximum waiting time ("
												+ e.getMessage()
												+ "), sleeping for "
												+ maxMsToSleep + " milliseconds.");
								try {
									Thread.sleep(maxMsToSleep);
								} catch (InterruptedException e1) {
									log.error("Interrupted sleep in timeout method: ", e1);
								}
							}
						}
					}
					++numberOfTries;
				} while(retry);

				artifactsToBeShippedList.addAll(sortedGAs);
				if(artifactsToBeShippedList.isEmpty()) return new Object[]{};
				GenericArtifact genericArtifact = artifactsToBeShippedList.remove(0);
				try {
					String conflictResolution = this.getConflictResolutionPriority(syncInfo);
					genericArtifact.setConflictResolutionPriority(conflictResolution);
					genericArtifact.setSourceSystemTimezone(this.getSourceSystemTimezone(syncInfo));
					genericArtifact.setTargetSystemTimezone(this.getTargetSystemTimezone(syncInfo));
					//genericArtifact.setSourceSystemEncoding(this.getSourceSystemEncoding(syncInfo));
					//genericArtifact.setTargetSystemEncoding(this.getTargetSystemEncoding(syncInfo));
					Document returnDoc = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
					Object[] returnObjects = new Object[] {returnDoc};
					moveToTail(currentRecord);
					return returnObjects;
				} catch (GenericArtifactParsingException e) {
					String cause = "Could not parse the artifact for "+artifactId;
					log.error(cause, e);
					genericArtifact.setErrorCode(GenericArtifact.ERROR_GENERIC_ARTIFACT_PARSING);
					throw new CCFRuntimeException(cause, e);
				}
			}
			else {
				log.debug("No changed artifacts reported for "+sourceRepositoryId
						+". Removing it from the waiting list");
				removeFromWaitingList(currentRecord);
			}
		}
		try {
			if (isRestartConnector()) {
				log.info("All buffers are flushed now ..., exit with exit code "+RESTART_EXIT_CODE);
				System.exit(RESTART_EXIT_CODE);
			}
			if (isShutDownConnector()) {
				log.info("All buffers are flushed now ..., exit with exit code "+RESTART_EXIT_CODE);
				System.exit(0);
			}
			log.debug("There are no artifacts to be shipped from any of the repositories. Sleeping");
			Thread.sleep(sleepInterval);
		} catch (InterruptedException e) {
			String cause = "Thread is interrupted";
			log.warn(cause, e);
		}
		return new Object[]{};
	}

	/**
	 * Removes the record passed in the parameter from the waiting list so that
	 * in the further runs this repository will not be taken into account for artifact
	 * shipment (Unless the repository record is added again by the incoming synchronization
	 * record).
	 *
	 * @param currentRecord - The repository record to be removed from the waiting list.
	 */
	private void removeFromWaitingList(RepositoryRecord currentRecord) {
		repositorySynchronizationWaitingList.remove(currentRecord);
		String currentSourceRepositoryId = currentRecord.getRepositoryId();
		repositoryRecordsInRepositorySynchronizationWaitingList.remove(currentSourceRepositoryId);
	}

	/**
	 * Moves the repository to the tail of the waiting list so that in the
	 * next immediate run the repository will not be considered for synchronization
	 * (Unless it is the lone repository that is being sync-ed).
	 *
	 * @param currentRecord - The repository record to be moved to the tail.
	 */
	private void moveToTail(RepositoryRecord currentRecord){
		repositorySynchronizationWaitingList.remove(currentRecord);
		repositorySynchronizationWaitingList.add(currentRecord);
	}

	/**
	 * All the artifact data and dependent data generic artifacts are accumulated in
	 * a single List and are sorted according to their last modified date so that
	 * the artifact that was changed early will ship first.
	 *
	 * @param artifactData - The artifact's data
	 * @param artifactAttachments - Attachments of an artifact
	 * @param artifactDependencies - Dependent artifacts
	 *
	 * @return - The sorted list of Generic Artifact objects
	 */
	private List<GenericArtifact> combineAndSort(
			GenericArtifact artifactData,
			List<GenericArtifact> artifactAttachments,
			List<GenericArtifact> artifactDependencies) {
		ArrayList<GenericArtifact> gaList = new ArrayList<GenericArtifact>();
		gaList.add(artifactData);
		gaList.addAll(artifactAttachments);
		gaList.addAll(artifactDependencies);
		Collections.sort(gaList, genericArtifactComparator);
		return gaList;
	}

	/**
	 * Sub classes should implement this method.
	 * The implemented method should get all the dependent artifacts of the artifact
	 * with the artifact id artifactId and convert them into Generic Artifact
	 * object and return.
	 *
	 * If there are no dependent artifacts to be returned the implemented method
	 * should return an empty List. It should not return null.
	 *
	 * @param syncInfo - The synchronization info against which the changed artifacts
	 * 					to be fetched
	 * @param artifactId - The id of the artifact whose dependent artifacts should be
	 * 					retrieved and returned.
	 * @return - A List of dependent artifacts of this artifactId that are changed
	 */
	public abstract List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId);

	/**
	 * Queries the source repository for any attachment changes for the given artifactId
	 * and returns the changed attachments in a GenericArtifact object. If there are
	 * no attachments changed an empty list is returned.
	 *
	 * @param syncInfo
	 * @param artifactData
	 * @return
	 */
	public abstract List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			GenericArtifact artifactData);

	/**
	 * Returns the artifact data for the artifactId in a GenericArtifact
	 * object. If the reader can return the artifact change history it
	 * should be returned in the list.
	 * If the reader doesn't have the capability of returning the artifact
	 * change history it should return a list that contains the latest artifact
	 *  data in a single GenericArtifact object added to the list.
	 *
	 * @param syncInfo
	 * @param artifactId
	 * @return
	 */
	public abstract GenericArtifact getArtifactData(Document syncInfo,
			String artifactId);

	/**
	 * Returns a list of changed artifacts' ids.
	 *
	 * @param syncInfo
	 * @return
	 */
	public abstract List<String> getChangedArtifacts(Document syncInfo);

	public void reset(Object context) {
	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		super.validate(exceptions);
		if (getConnectionManager() == null) {
			log.error("connectionManager property is not set");
			exceptions.add(new ValidationException("connectionManager property is not set",this));
		}

		if(getSleepInterval() == -1){
			log.error("sleepInterval is not set");
			exceptions.add(new ValidationException("sleepInterval is not set",this));
		}
	}

	/**
	 * Extracts and returns the last modified time of the artifact that was
	 * sync-ed last.
	 * @param syncInfo
	 * @return
	 */
	public String getLastSourceArtifactModificationDate(Document syncInfo) {
		//LAST_SOURCE_ARTIFACT_MODIFICATION_DATE
		Node node= syncInfo.selectSingleNode("//LAST_SOURCE_ARTIFACT_MODIFICATION_DATE");
		if (node == null)
			return null;
		String dbTime = node.getText();
		if(!StringUtils.isEmpty(dbTime)){
			java.sql.Timestamp ts = java.sql.Timestamp.valueOf(dbTime);
			long time = ts.getTime();
			Date date = new Date(time);
			return DateUtil.format(date);
		}
		return null;
	}


	/**
	 * Returns the version of the artifact that was sync-ed in the last CCF cycle.
	 * @param syncInfo - The incoming sync info
	 * @return - The version of the artifact that was last sync-ed
	 */
	protected String getLastSourceVersion(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//LAST_SOURCE_ARTIFACT_VERSION");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Returns the source artifact id that was sync-ed in the last
	 * CCF cycle.
	 *
	 * @param syncInfo - The incoming sync info of a particular repository.
	 * @return - The source artifact id that was sync-ed last for this repository.
	 */
	protected String getLastSourceArtifactId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//LAST_SOURCE_ARTIFACT_ID");
		if (node==null)
			return null;
		return node.getText();
	}


	/**
	 * Extracts and returns the Source repository id from the sync info.
	 * @param syncInfo - The incoming sync info for the repository.
	 * @return - Returns the repository id from this sync info.
	 */
	public String getSourceRepositoryId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the Source repository kind from the sync info.
	 * @param syncInfo - The incoming sync info for this repository.
	 * @return - Returns the repository kind from the incoming sync info of this repository
	 */
	public String getSourceRepositoryKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the Source system id from the sync info.
	 *
	 * @param syncInfo - the incoming sync info for this repository
	 * @return - The source system id for this repository.
	 */
	public String getSourceSystemId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the source system kind from the sync info.
	 * @param syncInfo - The incoming sync info for this repository.
	 * @return - The source system kind for this repository.
	 */
	public String getSourceSystemKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the target repository id from the sync info.
	 * @param syncInfo - The incoming sync info for this repository.
	 * @return - the target repository id that is mapped to this repository.
	 */
	public String getTargetRepositoryId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the target repository kind from the sync info.
	 *
	 * @param syncInfo - The incoming sync info for this repository.
	 * @return - The target repository id extracted from this repository info.
	 */
	public String getTargetRepositoryKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the Target system id from the sync info.
	 * @param syncInfo - The incoming sync info for this repository.
	 * @return - The target system id from the sync info
	 */
	public String getTargetSystemId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the target system kind from the sync info.
	 * @param syncInfo - The incoming sync info for this repository.
	 * @return - the target system kind from this repository sync info.
	 */
	public String getTargetSystemKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	protected Date getLastModifiedDate(Document syncInfo){
		String lastModifiedDateString = this.getLastSourceArtifactModificationDate(syncInfo);
		Date lastModifiedDate = null;
		if (!StringUtils.isEmpty(lastModifiedDateString)) {
			lastModifiedDate=DateUtil.parse(lastModifiedDateString);
		} else {
			lastModifiedDate = new Date(0);
		}
		return lastModifiedDate;
	}

	/**
	 * Extracts and returns the conflictResolutionPriority for the
	 * source repository.
	 * @param syncInfo
	 * @return
	 */
	public String getConflictResolutionPriority(Document syncInfo){
		Node node= syncInfo.selectSingleNode("//CONFLICT_RESOLUTION_PRIORITY");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getSourceSystemTimezone(Document syncInfo){
		Node node= syncInfo.selectSingleNode("//SOURCE_SYSTEM_TIMEZONE");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getTargetSystemTimezone(Document syncInfo){
		Node node= syncInfo.selectSingleNode("//TARGET_SYSTEM_TIMEZONE");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getSourceSystemEncoding(Document syncInfo){
		Node node= syncInfo.selectSingleNode("//SOURCE_SYSTEM_ENCODING");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getTargetSystemEncoding(Document syncInfo){
		Node node= syncInfo.selectSingleNode("//TARGET_SYSTEM_ENCODING");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Returns the configured sleep interval in milliseconds
	 * @return
	 */
	public long getSleepInterval() {
		return sleepInterval;
	}

	/**
	 * Sets the sleep interval in milliseconds. Sleep interval is the time
	 * lag introduced by the AbstractReader when there are no artifacts
	 * to be shipped in any of the repositories configured.
	 *
	 * @param sleepInterval
	 */
	public void setSleepInterval(long sleepInterval) {
		this.sleepInterval = sleepInterval;
	}
	/**
	 * Returns whether the attachments of the artifact should be shipped by the Reader
	 * component.
	 *
	 * @return - true if the attachments will be shipped.
	 * 		   - false if the attachments won't be shipped.
	 */
	public boolean isShipAttachments() {
		return shipAttachments;
	}
	/**
	 * Sets the flag whether to ship the attachments or not.
	 *
	 * @param shipAttachments - true if the attachment should be shipped
	 * 						  - false if the attachments should not be shipped.
	 */
	public void setShipAttachments(boolean shipAttachments) {
		this.shipAttachments = shipAttachments;
	}
	/**
	 * Returns the maximum attachment size that will be shipped for an
	 * artifact. The max attachment size is configured in bytes.
	 *
	 * @return - The maximum attachment size per artifact in bytes
	 */
	public long getMaxAttachmentSizePerArtifact() {
		return maxAttachmentSizePerArtifact;
	}
	/**
	 * Sets the maximum attachment size to be shipped for an artifact.
	 * If the attachment size is more than this configured value it
	 * should not be shipped by the reader.
	 *
	 * @param maxAttachmentPerArtifact - the maximum attachment size that can
	 * be shipped.
	 */
	public void setMaxAttachmentSizePerArtifact(long maxAttachmentPerArtifact) {
		this.maxAttachmentSizePerArtifact = maxAttachmentPerArtifact;
	}
	/**
	 * Returns the flag that denotes if the Reader should include field meta data
	 * for the artifacts that are shipped.
	 *
	 * @return - true if the field meta data should be shipped
	 * 		   - false if the field meta data need not be shipped by the Reader.
	 */
	public boolean isIncludeFieldMetaData() {
		return includeFieldMetaData;
	}
	/**
	 * Sets if the field meta data should be included in the artifact by the
	 * Reader component.
	 * @param includeFieldMetaData - true if the Reader should include the meta data
	 * 									for the fields.
	 * 							   - flase if the Reader need not include the field meta data
	 * 								with the artifact fields.
	 */
	public void setIncludeFieldMetaData(boolean includeFieldMetaData) {
		this.includeFieldMetaData = includeFieldMetaData;
	}

	/**
	 * Get the connection manager. The connection manager is responsible to
	 * manage (create, close, pool) the connections from type T.
	 * Furthermore, it contains timeout settings and the settings
	 * for the retry code in case of network timeout and session fault
	 * related errors.
	 * @return the connection manager object
	 */
	public ConnectionManager<T> getConnectionManager() {
		return connectionManager;
	}

	/**
	 * Set the connection manager. The connection manager is responsible to
	 * manage (create, close, pool) the connections from type T.
	 * Furthermore, it contains timeout settings and the settings
	 * for the retry code in case of network timeout and session fault
	 * related errors.
	 * @param connectionManager the connection manager object
	 */
	public void setConnectionManager(ConnectionManager<T> connectionManager) {
		this.connectionManager = connectionManager;
	}

	public boolean handleException(Throwable rootCause,ConnectionManager<T> connectionManager){
		return false;
	}
	public boolean isShipAttachmentsWithArtifact() {
		return shipAttachmentsWithArtifact;
	}
	public void setShipAttachmentsWithArtifact(boolean shipAttachmentsWithArtifact) {
		this.shipAttachmentsWithArtifact = shipAttachmentsWithArtifact;
	}
	
	private static final int RESTART_EXIT_CODE = 42;
	
	/**
	 * This field contains the date when the CCF was started
	 */
	private Date startedDate=new Date();
	
	/**
	 * This property denotes after how many seconds the CCF will restart automatically
	 */
	private int autoRestartPeriod=-1;
	
	/**
	 * If you set this property, the CCF will exit (with exit code 42)
	 * after the number of seconds you have specified.
	 * If CCF is wrapped by service wrapper, it will be restarted automatically.
	 * This setting can be used to release resources from time to time.
	 * If you do not set this property or set it to a negative value, the CCF will never exit.
	 * @param autoRestartPeriod the autoRestartPeriod to set
	 */
	public void setAutoRestartPeriod(int autoRestartPeriod) {
		this.autoRestartPeriod = autoRestartPeriod * 1000;
	}

	/**
	 * Returns the number of seconds, after the CCF will exit with exit code 42 and will be restarted by the
	 * ServiceWrapper. If the return value is negative, it will never exit/restarted.
	 * @return the autoRestartPeriod
	 */
	public int getAutoRestartPeriod() {
		return autoRestartPeriod;
	}
	/**
	 * @param restartConnector the restartConnector to set
	 */
	public static void setRestartConnector(boolean restartConnector) {
		AbstractReader.restartConnector = restartConnector;
	}
	/**
	 * @return the restartConnector
	 */
	public static boolean isRestartConnector() {
		return restartConnector;
	}
	/**
	 * @param shutDownConnector the shutDownConnector to set
	 */
	public static void setShutDownConnector(boolean shutDownConnector) {
		AbstractReader.shutDownConnector = shutDownConnector;
	}
	/**
	 * @return the shutDownConnector
	 */
	public static boolean isShutDownConnector() {
		return shutDownConnector;
	}
	
}
