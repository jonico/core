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
	
	protected void init(){
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
		log.debug("Entering process method of AbstractReader");
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
					genericArtifact.setErrorCode(GenericArtifact.GENERIC_ARTIFACT_PARSING_ERROR);
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
					genericArtifact.setErrorCode(GenericArtifact.GENERIC_ARTIFACT_PARSING_ERROR);
					throw new CCFRuntimeException(cause, e);
				}
			}
			else {
				log.info("No changed artifacts reported for "+sourceRepositoryId
						+". Removing it from the waiting list");
				removeFromWaitingList(currentRecord);
			}
		}
		try {
			log.info("There are no artifacts to be shipped from any of the repositories. Sleeping");
			Thread.sleep(sleepInterval);
		} catch (InterruptedException e) {
			String cause = "Thread is interrupted";
			log.warn(cause, e);
		}
		return new Object[]{};
	}

	private void removeFromWaitingList(RepositoryRecord currentRecord) {
		repositorySynchronizationWaitingList.remove(currentRecord);
		String currentSourceRepositoryId = currentRecord.getRepositoryId(); 
		repositoryRecordsInRepositorySynchronizationWaitingList.remove(currentSourceRepositoryId);
	}
	
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
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		if(sleepInterval == -1){
			exceptions.add(new ValidationException("sleepInterval is not set",this));
		}
	}
	
	/**
	 * Extracts and returns the last read time from the sync info.
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
	

	protected String getLastSourceVersion(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//LAST_SOURCE_ARTIFACT_VERSION");
		if (node==null)
			return null;
		return node.getText();
	}

	protected String getLastSourceArtifactId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//LAST_SOURCE_ARTIFACT_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	
	/**
	 * Extracts and returns the Source repository id from the sync info.
	 * @param syncInfo
	 * @return
	 */
	public String getSourceRepositoryId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	
	/**
	 * Extracts and returns the Source repository kind from the sync info.
	 * @param syncInfo
	 * @return
	 */
	public String getSourceRepositoryKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the Source system id from the sync info.
	 * @param syncInfo
	 * @return
	 */
	public String getSourceSystemId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	
	/**
	 * Extracts and returns the source system kind from the sync info.
	 * @param syncInfo
	 * @return
	 */
	public String getSourceSystemKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the target repository id from the sync info.
	 * @param syncInfo
	 * @return
	 */
	public String getTargetRepositoryId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	
	/**
	 * Extracts and returns the target repository kind from the sync info.
	 * @param syncInfo
	 * @return
	 */
	public String getTargetRepositoryKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	/**
	 * Extracts and returns the Target system id from the sync info.
	 * @param syncInfo
	 * @return
	 */
	public String getTargetSystemId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	
	/**
	 * Extracts and returns the target system kind from the sync info.
	 * @param syncInfo
	 * @return
	 */
	public String getTargetSystemKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_SYSTEM_KIND");
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
	public boolean isShipAttachments() {
		return shipAttachments;
	}
	public void setShipAttachments(boolean shipAttachments) {
		this.shipAttachments = shipAttachments;
	}
	public long getMaxAttachmentSizePerArtifact() {
		return maxAttachmentSizePerArtifact;
	}
	public void setMaxAttachmentSizePerArtifact(long maxAttachmentPerArtifact) {
		this.maxAttachmentSizePerArtifact = maxAttachmentPerArtifact;
	}
	public boolean isIncludeFieldMetaData() {
		return includeFieldMetaData;
	}
	public void setIncludeFieldMetaData(boolean includeFieldMetaData) {
		this.includeFieldMetaData = includeFieldMetaData;
	}
}
