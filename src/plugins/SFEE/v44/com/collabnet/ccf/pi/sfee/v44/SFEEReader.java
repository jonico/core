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
	
	private SFEEDBHelper dbHelper = new SFEEDBHelper();
	
	public Object[] process(Object data) {
		// TODO evaluate data to decide which items to fetch again
		if (!(data instanceof Document)) {
			log.error("Supplied data not in the expected dom4j format: "+data);
			return null;
		}
			
		Document document=(Document) data;
		String trackerId=getProjectTracker(document);
		if (StringUtils.isEmpty(trackerId)) {
			log.error("Could not extract TRACKERID: "+document.asXML());
			return null;
		}
		
		Date lastModifiedDate;
		boolean firstTimeImport=false;
		String lastModifiedDateString = getLastModifiedDateString(document);
		if (StringUtils.isEmpty(lastModifiedDateString)) {
			// TODO Implement a special first time logic
			log.warn("This seems to be a first time import ...: "+document.asXML());
			lastModifiedDate=new Date(0);
			firstTimeImport=true;
		}
		else {
			lastModifiedDate=(Date)SFEEGAHelper.asTypedValue(lastModifiedDateString, "DateTime");
			lastModifiedDate.setTime(lastModifiedDate.getTime()+1);
		}
		
		String lastArtifactId=getLastArtifactId(document);
		if (lastArtifactId==null) {
			if (!firstTimeImport) {
				log.warn("Seems as if we lost the artifactId for the last queried artifact for tracker "+trackerId);
			}
			lastArtifactId="";			
		}
		
		int lastArtifactVersion=-1;
		
		String lastArtifactVersionString=getLastArtifactVersionString(document);
		if (StringUtils.isEmpty(lastArtifactVersionString)) {
				if (!firstTimeImport) {
					log.warn("Seems as if we lost the version information fo the last queried artifact with id "+lastArtifactId+" for tracker "+trackerId);
				}
				lastArtifactVersion=-1;
		}
		else {
			try {
				lastArtifactVersion=Integer.parseInt(lastArtifactVersionString);
			}
			catch (NumberFormatException e) {
				log.error("Last version of last queried artifact "+lastArtifactId+" for tracker "+trackerId+" contained no numerical value",e);
				lastArtifactVersion=-1;
			}
		}
		
		try {
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
		
		Object[] result=readTrackerItems(trackerId,lastModifiedDate,lastArtifactId,lastArtifactVersion,firstTimeImport,document);
		for(Object doc: result){
			GenericArtifact ga = null;
			try {
				ga = GenericArtifactHelper.createGenericArtifactJavaObject((Document) doc);
			} catch (GenericArtifactParsingException e) {
				throw new RuntimeException(e);
			}
			String targetRepositoryId = ga.getTargetRepositoryId();
			String sourceRepositoryId = ga.getSourceRepositoryId();
			String sourceArtifactId = ga.getSourceArtifactId();
		}
		disconnect();
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
	
	public Object[] readTrackerItems(String projectTracker, Date lastModifiedDate, String lastArtifactId, int lastArtifactVersion, boolean firstTimeImport, Document dbDocument) {
		// TODO Use the information of the firstTimeImport flag
		
		List<Document> dataRows=new ArrayList<Document>();
		List<ArtifactSoapDO> artifactRows;
		try {
			artifactRows = trackerHandler.getChangedTrackerItems(getSessionId(), projectTracker,lastModifiedDate, lastArtifactId, lastArtifactVersion);
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
		
		if (artifactHistoryRows == null) {
			return new Object[]{};
		} else {
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
		for(Entry<Date,GenericArtifact> entry:attachments.entrySet()){
			GenericArtifact ga = entry.getValue();
			Date date = entry.getKey();
			if(dbDocument != null)
				populateSrcAndDest(dbDocument, ga);
			Document document = null;
			try {
				document = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
			} catch (GenericArtifactParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dataRows.add(document);
		}
		
		return dataRows.toArray();
	}
	
	private void populateSrcAndDest(Document dbDocument, GenericArtifact ga){
		String sourceArtifactId = ga.getSourceArtifactId();
		if(StringUtils.isEmpty(sourceArtifactId)){
			List<GenericArtifactField> fields = ga.getAllGenericArtifactFieldsWithSameFieldName("Id");
			for(GenericArtifactField field:fields){
				sourceArtifactId = field.getFieldValue().toString();
			}
		}
		ga.setSourceArtifactId(sourceArtifactId);
		ga.setSourceRepositoryId(dbHelper.getSourceRepositoryId(dbDocument));
		ga.setSourceRepositoryKind(dbHelper.getSourceRepositoryKind(dbDocument));
		ga.setSourceSystemId(dbHelper.getSourceSystemId(dbDocument));
		ga.setSourceSystemKind(dbHelper.getSourceSystemKind(dbDocument));
		
		ga.setTargetRepositoryId(dbHelper.getTargetRepositoryId(dbDocument));
		ga.setTargetRepositoryKind(dbHelper.getTargetRepositoryKind(dbDocument));
		ga.setTargetSystemId(dbHelper.getTargetSystemId(dbDocument));
		ga.setTargetSystemKind(dbHelper.getTargetSystemKind(dbDocument));
	}
	
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		
		trackerHandler = new SFEETrackerHandler(getServerUrl());
		attachmentHandler = new SFEEAttachmentHandler(getServerUrl());
	}

	
	private String getProjectTracker(Document document) {
		// TODO Let the user specify this value?
		return dbHelper.getSourceRepositoryId(document);
//		Node node= document.selectSingleNode("//SOURCE_REPOSITORY_ID");
//		if (node==null)
//			return null;
//		return node.getText();
		
	}
	
	private String getLastModifiedDateString(Document document) {
		// TODO Let the user specify this value?
		String dbTime = dbHelper.getFromTime(document);
		java.sql.Timestamp ts = java.sql.Timestamp.valueOf(dbTime);
		long time = ts.getTime();
		Date date = new Date(time);
		return DateUtil.format(date);
	}
	
	private String getLastArtifactId(Document document) {
		// TODO Let the user specify this value?
		return dbHelper.getSourceArtifactId(document);
//		Node node= document.selectSingleNode("//SOURCE_ARTIFACT_ID");
//		if (node==null)
//			return null;
//		return node.getText();
	}
	
	private String getLastArtifactVersionString(Document document) {
		// TODO I am not reading the artifact version ID from the DB. Refactor this method to DBHelper...?
		Node node= document.selectSingleNode("//ARTIFACTVERSION");
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

	public SFEEDBHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(SFEEDBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public SFEEAttachmentHandler getAttachmentHandler() {
		return attachmentHandler;
	}

	public void setAttachmentHandler(SFEEAttachmentHandler attachmentHandler) {
		this.attachmentHandler = attachmentHandler;
	}
}
