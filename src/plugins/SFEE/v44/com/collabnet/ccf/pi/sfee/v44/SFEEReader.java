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

import com.collabnet.ccf.core.db.DBHelper;
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
	
	private IArtifactToGAConverter artifactConverter;
	
	private DBHelper dbHelper = new DBHelper();
	
	private String tracker = null;
	
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
		
		Object[] result=readTrackerItems(tracker,lastModifiedDate, firstTimeImport,document);
		disconnect();
		log.debug("Disconnected from SFEE");
		return result;
	}

	public SFEEReader() {
	    super();
	}

    public SFEEReader(String id) {
	    super(id);
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
		List<ArtifactSoapDO> artifactHistoryRows = appHandler.loadArtifactAuditHistory(artifactRows,
				lastModifiedDate,getUsername(), trackerFields);
		TreeMap<Date, GenericArtifact> attachments = null;
		try {
			attachments = attachmentHandler.listAttachments(getSessionId(),
					lastModifiedDate,getUsername(),artifactRows, this.getMSfSoap());
		} catch (RemoteException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		
		if (artifactHistoryRows == null && (attachments==null || attachments.size() == 0)) {
			return new Object[]{};
		} else {
			if(artifactHistoryRows != null){
				for(ArtifactSoapDO artifactRow:artifactHistoryRows)
				{
					GenericArtifact genericArtifact = artifactConverter.convert(artifactRow, trackerFields);
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
						ga.setArtifactAction(ArtifactActionValue.UPDATE);
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
		String sourceRepositoryId = dbHelper.getSourceRepositoryId(dbDocument);
		String sourceRepositoryKind = dbHelper.getSourceRepositoryKind(dbDocument);
		String sourceSystemId = dbHelper.getSourceSystemId(dbDocument);
		String sourceSystemKind = dbHelper.getSourceSystemKind(dbDocument);
		
		String targetRepositoryId = dbHelper.getTargetRepositoryId(dbDocument);
		String targetRepositoryKind = dbHelper.getTargetRepositoryKind(dbDocument);
		String targetSystemId = dbHelper.getTargetSystemId(dbDocument);
		String targetSystemKind = dbHelper.getTargetSystemKind(dbDocument);
		String targetArtifactId = DBHelper.getTargetArtifactIdFromTable(sourceArtifactId,
				sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind,
				targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
		if(StringUtils.isEmpty(targetArtifactId)){
			ga.setArtifactAction(ArtifactActionValue.CREATE);
		}
		else {
			ga.setArtifactAction(ArtifactActionValue.UPDATE);
		}
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
		String dbTime = dbHelper.getFromTime(document);
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

	public void reset(Object context) {
	}

	public IArtifactToGAConverter getArtifactConverter() {
		return artifactConverter;
	}

	public void setArtifactConverter(IArtifactToGAConverter artifactConverter) {
		this.artifactConverter = artifactConverter;
	}

	public DBHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(DBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public SFEEAttachmentHandler getAttachmentHandler() {
		return attachmentHandler;
	}

	public void setAttachmentHandler(SFEEAttachmentHandler attachmentHandler) {
		this.attachmentHandler = attachmentHandler;
	}

	public String getTracker() {
		return tracker;
	}

	public void setTracker(String tracker) {
		this.tracker = tracker;
	}
}
