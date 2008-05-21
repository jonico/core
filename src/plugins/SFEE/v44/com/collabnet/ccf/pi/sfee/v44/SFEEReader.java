package com.collabnet.ccf.pi.sfee.v44;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.openadaptor.core.IDataProcessor;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class SFEEReader extends SFEEConnectHelper implements
		IDataProcessor {
    
	private static final Log log = LogFactory.getLog(SFEEReader.class);
	
	private boolean isDry=true;

	private Object readerContext;

	private SFEETrackerHandler trackerHandler;
	
	private SFEEAppHandler appHandler;
	
	private SFEEAttachmentHandler attachmentHandler = null;
	
	private SFEEToGenericArtifactConverter artifactConverter;
	
	public SFEEReader() {
	    super();
	    artifactConverter = new SFEEToGenericArtifactConverter();
	}

    public SFEEReader(String id) {
	    super(id);
	    artifactConverter = new SFEEToGenericArtifactConverter();
	}
    
	public Object[] process(Object data) {
		if (!(data instanceof Document)) {
			log.error("Supplied data not in the expected dom4j format: "+data);
			return null;
		}
		log.debug("Start Processing");
		Document document=(Document) data;
		
		Date lastModifiedDate;
		boolean firstTimeImport=false;
		String lastModifiedDateString = getLastModifiedDateString(document);
		if (StringUtils.isEmpty(lastModifiedDateString)) {
			lastModifiedDate=new Date(0);
			firstTimeImport=true;
			log.info("This seems to be a first time import. Fetching artifacts from "+lastModifiedDate);
		}
		else {
			log.debug("Artifacts to be fetched from "+lastModifiedDateString);
			lastModifiedDate=(Date)SFEEGAHelper.asTypedValue(lastModifiedDateString, "DateTime");
			lastModifiedDate.setTime(lastModifiedDate.getTime()+1);
		}

		try {
			log.debug("Connecting to SFEE "+this.getServerUrl()+" with user name "+this.getUsername());
			connect();
		} catch (RemoteException e) {
			// TODO Declare exception so that it can be processed by OA exception handler
			log.error("Could not log into SFEE", e);
			throw new RuntimeException(e);
		}
		catch (IOException ex) {
			// TODO postpone exception handling to OA framework
			log.error("During the connection process to SFEE, an IO-Error occured", ex);
			throw new RuntimeException(ex);
		}
		String tracker = this.getSourceRepositoryId(document);
		Object[] result=readTrackerItems(tracker,lastModifiedDate, firstTimeImport,document);
		disconnect();
		log.debug("Disconnected from SFEE");
		return result;
	}

	public Object getReaderContext() {
		return readerContext;
	}
	
	public boolean isDry() {
		return isDry;
	}

	@Override
	public void connect() throws IOException {	
		super.connect();
		appHandler = new SFEEAppHandler(getMSfSoap(), getSessionId());
		isDry=false;
	}
	
	public Object[] readTrackerItems(String projectTracker, Date lastModifiedDate, boolean firstTimeImport, Document dbDocument) {
		// TODO Use the information of the firstTimeImport flag
		
		List<Document> dataRows=new ArrayList<Document>();
		List<ArtifactSoapDO> artifactRows;
		try {
			artifactRows = trackerHandler.getChangedTrackerItems(getSessionId(), projectTracker,lastModifiedDate);
		} catch (RemoteException e) {
			// TODO Throw an exception?
			log.error("During the artifact retrieval process to SFEE, an error occured",e);
			return null;
		}
		TrackerFieldSoapDO[] trackerFields = null;
		try {
			trackerFields = trackerHandler.getFlexFields(getSessionId(), projectTracker);
		} catch (RemoteException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		// Now we load the history of each artifact that got changed
		// List<ArtifactSoapDO> artifactHistoryRows = appHandler.loadArtifactAuditHistory(artifactRows,
		//		lastModifiedDate,getUsername(), trackerFields);
		List<ArtifactSoapDO> artifactHistoryRows = null;
		TreeMap<Date, GenericArtifact> attachments = null;
		if(artifactRows != null){
			for(ArtifactSoapDO artifact:artifactRows){
				appHandler.addComments(artifact,
						lastModifiedDate,this.getUsername());
			}
			artifactHistoryRows = artifactRows;
			try {
				attachments = attachmentHandler.listAttachments(getSessionId(),
						lastModifiedDate,getUsername(),artifactRows, this.getMSfSoap());
			} catch (RemoteException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}
		}
		
		if (artifactHistoryRows == null && (attachments==null || attachments.size() == 0)) {
			return new Object[]{};
		} else {
			if(artifactHistoryRows != null){
				for(ArtifactSoapDO artifactRow:artifactHistoryRows)
				{
					if(artifactRow.getCreatedBy().equals(this.getUsername())){
						continue;
					}
					GenericArtifact genericArtifact = artifactConverter.convert(artifactRow, trackerFields, lastModifiedDate);
					
					if(dbDocument != null)
						populateSrcAndDest(dbDocument, genericArtifact);
					Document document = null;
					try {
						document = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
					} catch (GenericArtifactParsingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dataRows.add(document);
				}
			}
			if(attachments != null){
				for(Entry<Date,GenericArtifact> entry:attachments.entrySet()){
					GenericArtifact ga = entry.getValue();
					if(dbDocument != null){
						populateSrcAndDest(dbDocument, ga);
					}
					Document document = null;
					try {
						document = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
					} catch (GenericArtifactParsingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dataRows.add(document);
				}
			}
		}
		return dataRows.toArray();
	}
	
	private void populateSrcAndDest(Document dbDocument, GenericArtifact ga){
		String sourceArtifactId = ga.getSourceArtifactId();
		String sourceRepositoryId = this.getSourceRepositoryId(dbDocument);
		String sourceRepositoryKind = this.getSourceRepositoryKind(dbDocument);
		String sourceSystemId = this.getSourceSystemId(dbDocument);
		String sourceSystemKind = this.getSourceSystemKind(dbDocument);
		
		String targetRepositoryId = this.getTargetRepositoryId(dbDocument);
		String targetRepositoryKind = this.getTargetRepositoryKind(dbDocument);
		String targetSystemId = this.getTargetSystemId(dbDocument);
		String targetSystemKind = this.getTargetSystemKind(dbDocument);
		
		if(StringUtils.isEmpty(sourceArtifactId)){
			List<GenericArtifactField> fields = ga.getAllGenericArtifactFieldsWithSameFieldName("Id");
			for(GenericArtifactField field:fields){
				sourceArtifactId = field.getFieldValue().toString();
			}
		}
		ga.setSourceArtifactId(sourceArtifactId);
		ga.setSourceRepositoryId(sourceRepositoryId);
		ga.setSourceRepositoryKind(sourceRepositoryKind);
		ga.setSourceSystemId(sourceSystemId);
		ga.setSourceSystemKind(sourceSystemKind);
		
		ga.setTargetRepositoryId(targetRepositoryId);
		ga.setTargetRepositoryKind(targetRepositoryKind);
		ga.setTargetSystemId(targetSystemId);
		ga.setTargetSystemKind(targetSystemKind);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		
		trackerHandler = new SFEETrackerHandler(getServerUrl());
		attachmentHandler = new SFEEAttachmentHandler(getServerUrl());
	}

	private String getLastModifiedDateString(Document document) {
		// TODO Let the user specify this value?
		String dbTime = this.getFromTime(document);
		if(!StringUtils.isEmpty(dbTime)){
			java.sql.Timestamp ts = java.sql.Timestamp.valueOf(dbTime);
			long time = ts.getTime();
			Date date = new Date(time);
			return DateUtil.format(date);
		}
		return null;
	}
	
	private String getLastArtifactVersionString(Document document) {
		// TODO I am not reading the artifact version ID from the DB. Refactor this method to DBHelper...?
		Node node= document.selectSingleNode("//VERSION");
		if (node==null)
			return null;
		return node.getText();
	}
	
	public String getToTime(Document document) {
		Node node= document.selectSingleNode("//TO_TIME");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getFromTime(Document document) {
		Node node= document.selectSingleNode("//FROM_TIME");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getSourceRepositoryId(Document document) {
		Node node= document.selectSingleNode("//SOURCE_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getSourceRepositoryKind(Document document) {
		Node node= document.selectSingleNode("//SOURCE_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getSourceSystemId(Document document) {
		Node node= document.selectSingleNode("//SOURCE_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getSourceSystemKind(Document document) {
		Node node= document.selectSingleNode("//SOURCE_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getTargetRepositoryId(Document document) {
		Node node= document.selectSingleNode("//TARGET_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getTargetRepositoryKind(Document document) {
		Node node= document.selectSingleNode("//TARGET_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getTargetSystemId(Document document) {
		Node node= document.selectSingleNode("//TARGET_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getTargetSystemKind(Document document) {
		Node node= document.selectSingleNode("//TARGET_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	public void reset(Object context) {
	}

	public SFEEAttachmentHandler getAttachmentHandler() {
		return attachmentHandler;
	}

	public void setAttachmentHandler(SFEEAttachmentHandler attachmentHandler) {
		this.attachmentHandler = attachmentHandler;
	}
}
