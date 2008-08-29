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

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.sfee.v44.SFEEGAHelper;

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
public abstract class AbstractReader extends LifecycleComponent implements IDataProcessor {
	private static final Log log = LogFactory.getLog(AbstractReader.class);
	private HashMap<String, RepositoryRecord> repositoryRecordHashMap = null;
	private ArrayList<RepositoryRecord> repositorySynchronizationWaitingList = null;
	private HashSet<String> repositoryRecordsInRepositorySynchronizationWaitingList = null;
	private long sleepInterval = -1;
	private boolean shipAttachments = true;
	private boolean includeFieldMetaData = false;
	private Comparator<GenericArtifact> genericArtifactComparator = null;
	public static final long DEFAULT_MAX_ATTACHMENT_SIZE_PER_ARTIFACT = 10 * 1024 * 1024;
	private long maxAttachmentSizePerArtifact = DEFAULT_MAX_ATTACHMENT_SIZE_PER_ARTIFACT;

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
			else if(artifactsToBeReadList.isEmpty()){
				log.debug("There are no artifacts to be read. Checking if there are"+
						" changed artifacts in repository " + sourceRepositoryId);
				currentRecord.setSyncInfo(syncInfoIn);
				syncInfo = currentRecord.getSyncInfo();
				List<String> artifactsToBeRead = this.getChangedArtifacts(syncInfo);
				artifactsToBeReadList.addAll(artifactsToBeRead);
			}
			if(!artifactsToBeReadList.isEmpty()) {
				log.debug("There are "+artifactsToBeReadList.size()+
						"artifacts to be read.");
				String artifactId = artifactsToBeReadList.remove(0);
				log.debug("Getting the data for artifact "+artifactId);
				List<GenericArtifact> artifactData = this.getArtifactData(syncInfo, artifactId);
				List<GenericArtifact> artifactAttachments = null;
				if(shipAttachments){
					artifactAttachments = this.getArtifactAttachments(syncInfo, artifactId);
				} else {
					artifactAttachments = new ArrayList<GenericArtifact>();
				}
				List<GenericArtifact> artifactDependencies = this.getArtifactDependencies(syncInfo, artifactId);
				
				List<GenericArtifact> sortedGAs = combineAndSort(artifactData,artifactAttachments,artifactDependencies);
				artifactsToBeShippedList.addAll(sortedGAs);
				GenericArtifact genericArtifact = artifactsToBeShippedList.remove(0);
				try {
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
			List<GenericArtifact> artifactData,
			List<GenericArtifact> artifactAttachments,
			List<GenericArtifact> artifactDependencies) {
		ArrayList<GenericArtifact> gaList = new ArrayList<GenericArtifact>();
		gaList.addAll(artifactData);
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
	 * @param artifactId
	 * @return
	 */
	public abstract List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			String artifactId);
	
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
	public abstract List<GenericArtifact> getArtifactData(Document syncInfo,
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
		if(sleepInterval == -1){
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
			//TODO Change the SFEE related class here
			lastModifiedDate=(Date)SFEEGAHelper.asTypedValue(lastModifiedDateString, "DateTime");
			lastModifiedDate.setTime(lastModifiedDate.getTime()+1);
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
}
