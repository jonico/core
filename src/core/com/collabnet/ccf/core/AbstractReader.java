package com.collabnet.ccf.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;

public abstract class AbstractReader extends LifecycleComponent implements IDataProcessor {
	private HashMap<String, RepositoryRecord> repositoryRecordHashMap = null;
	private ArrayList<RepositoryRecord> repositorySynchronizationWaitingList = null;
	private HashSet<String> repositoryRecordsInRepositorySynchronizationWaitingList = null;
	private long sleepInterval;
	private Comparator<GenericArtifact> genericArtifactComparator = null;

	public AbstractReader(){
		super();
		init();
	}
	public AbstractReader(String id){
		super(id);
		init();
	}
	public void init(){
		repositoryRecordHashMap = new HashMap<String, RepositoryRecord>();
		repositorySynchronizationWaitingList = new ArrayList<RepositoryRecord>();
		repositoryRecordsInRepositorySynchronizationWaitingList = new HashSet<String>();
		genericArtifactComparator = new Comparator<GenericArtifact>(){
			public int compare(GenericArtifact first, GenericArtifact second) {
				String firstLastModifiedDateStr = first.getArtifactLastModifiedDate();
				Date firstLastModifiedDate = DateUtil.parse(firstLastModifiedDateStr);
				
				String secondLastModifiedDateStr = first.getArtifactLastModifiedDate();
				Date secondLastModifiedDate = DateUtil.parse(secondLastModifiedDateStr);
				if(firstLastModifiedDate.after(secondLastModifiedDate)){
					return 1;
				}
				else if(firstLastModifiedDate.before(secondLastModifiedDate)){
					return -1;
				}
				else{
					String firstSourceArtifactId = first.getSourceArtifactId();
					String secondSourceArtifactId = second.getSourceArtifactId();
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
	public Object[] process(Object data) {
		Document syncInfoIn = null;
		if(data instanceof Document){
			syncInfoIn = (Document) data;
		} else {
			return null;
		}
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfoIn);
		RepositoryRecord record = repositoryRecordHashMap.get(sourceRepositoryId);
		if(record == null){
			record = new RepositoryRecord(sourceRepositoryId, syncInfoIn);
			repositoryRecordHashMap.put(sourceRepositoryId, record);
		}
		else {
			record.setSyncInfo(syncInfoIn);
		}
		if(!repositoryRecordsInRepositorySynchronizationWaitingList.contains(sourceRepositoryId)){
			repositorySynchronizationWaitingList.add(0, record);
			repositoryRecordsInRepositorySynchronizationWaitingList.add(sourceRepositoryId);
		}
		RepositoryRecord currentRecord = null;
		while(!repositorySynchronizationWaitingList.isEmpty()){
			currentRecord = repositorySynchronizationWaitingList.get(0);
			Document syncInfo = currentRecord.getSyncInfo();
			RepositoryRecord movedRecord = repositorySynchronizationWaitingList.remove(0);
			repositorySynchronizationWaitingList.add(movedRecord);
			List<GenericArtifact> artifactsToBeShippedList = currentRecord.getArtifactsToBeShippedList();
			List<String> artifactsToBeReadList = currentRecord.getArtifactsToBeReadList();
			if(!artifactsToBeShippedList.isEmpty()){
				GenericArtifact genericArtifact = artifactsToBeShippedList.remove(0);
//				if(artifactsToBeShippedList.isEmpty()){
//					repositorySynchronizationWaitingList.remove(currentRecord);
//					repositorySynchronizationWaitingList.add(currentRecord);
//				}
				try {
					Document returnDoc = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
					Object[] returnObjects = new Object[] {returnDoc};
					return returnObjects;
				} catch (GenericArtifactParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(artifactsToBeReadList.isEmpty()){
				List<String> artifactsToBeRead = this.getChangedArtifacts(syncInfo);
				artifactsToBeReadList.addAll(artifactsToBeRead);
			}
			if(!artifactsToBeReadList.isEmpty()) {
				String artifactId = artifactsToBeReadList.remove(0);
				List<GenericArtifact> artifactData = this.getArtifactData(syncInfo, artifactId);
				List<GenericArtifact> artifactAttachments = this.getArtifactAttachments(syncInfo, artifactId);
				List<GenericArtifact> artifactDependencies = this.getArtifactDependencies(syncInfo, artifactId);
				
				List<GenericArtifact> sortedGAs = combineAndSort(artifactData,artifactAttachments,artifactDependencies);
				artifactsToBeShippedList.addAll(sortedGAs);
				GenericArtifact genericArtifact = artifactsToBeShippedList.remove(0);
				try {
					Document returnDoc = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
					Object[] returnObjects = new Object[] {returnDoc};
					return returnObjects;
				} catch (GenericArtifactParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				repositorySynchronizationWaitingList.remove(currentRecord);
				String currentSourceRepositoryId = currentRecord.getRepositoryId(); 
				repositoryRecordsInRepositorySynchronizationWaitingList.remove(currentSourceRepositoryId);
			}
		}
		try {
			Thread.sleep(sleepInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Object[]{};
	}

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
	public abstract List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId);
	public abstract List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			String artifactId);
	public abstract List<GenericArtifact> getArtifactData(Document syncInfo,
			String artifactId);
	public abstract List<String> getChangedArtifacts(Document syncInfo);
	
	public void reset(Object context) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		// TODO Auto-generated method stub

	}
	
	public String getLastModifiedDateString(Document syncInfo) {
		// TODO Let the user specify this value?
		String dbTime = this.getFromTime(syncInfo);
		if(!StringUtils.isEmpty(dbTime)){
			java.sql.Timestamp ts = java.sql.Timestamp.valueOf(dbTime);
			long time = ts.getTime();
			Date date = new Date(time);
			return DateUtil.format(date);
		}
		return null;
	}
	
	public String getToTime(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TO_TIME");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getFromTime(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//FROM_TIME");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getSourceRepositoryId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getSourceRepositoryKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getSourceSystemId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getSourceSystemKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//SOURCE_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getTargetRepositoryId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getTargetRepositoryKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getTargetSystemId(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getTargetSystemKind(Document syncInfo) {
		Node node= syncInfo.selectSingleNode("//TARGET_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}
	public long getSleepInterval() {
		return sleepInterval;
	}
	public void setSleepInterval(long sleepInterval) {
		this.sleepInterval = sleepInterval;
	}

}
