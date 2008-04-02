package com.collabnet.ccf.pi.sfee;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.openadaptor.auxil.connector.iostream.EncodingAwareObject;
import org.openadaptor.core.IDataProcessor;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.pi.sfee.IArtifactToGAConverter;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class SFEEReader extends SFEEConnectHelper implements
		IDataProcessor {
    
	private static final Log log = LogFactory.getLog(SFEEReader.class);
	
	private boolean isDry=true;

	private Object readerContext;

	private SFEETrackerHandler trackerHandler;
	
	private SFEEAppHandler appHandler;
	
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
			lastModifiedDate=(Date)SFEEXMLHelper.asTypedValue(lastModifiedDateString, "DateTime");
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
			Document resDocument = (Document) doc;
			String targetRepositoryId = SFEEXMLHelper.getArtifactAttribute(resDocument, "targetRepositoryId");
			String sourceRepositoryId = SFEEXMLHelper.getArtifactAttribute(resDocument, "sourceRepositoryId");
			String sourceArtifactId = SFEEXMLHelper.getArtifactAttribute(resDocument, "sourceArtifactId");
			dbHelper.insertSourceArtifactID(sourceArtifactId, sourceRepositoryId, targetRepositoryId);
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
		appHandler = new SFEEAppHandler(mSfSoap, getSessionId());
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
		// Now we load the history of each artifact that got changed
		List<ArtifactSoapDO> artifactHistoryRows = appHandler.loadArtifactAuditHistory(artifactRows,lastModifiedDate);

		if (artifactHistoryRows == null) {
			// REFACTOR Refactor this part of converting the artifacts
			// we only received duplicates
			log.info("Only received duplicates, increasing lastModifiedDate...");
			/**
			 * Construct a fake duplicate data entry with a slightly higher lastModifiedTime
			 * to avoid querying the same duplicates over and over again
			 */
			// TODO Set encoding by user
			Document document=SFEEXMLHelper.createXMLDocument(EncodingAwareObject.ISO_8859_1);
			
			//TODO let user specify rootTag
			Element root=document.addElement("SFEEArtifact"); 
			// first of all, set the deletion field and duplicate field to false
			SFEEXMLHelper.addField(root,"deleteFlag","false","Boolean",false);
			SFEEXMLHelper.addField(root,"isDuplicate","true","Boolean",false);
			SFEEXMLHelper.addField(root,"Id",lastArtifactId,"String",false);
			SFEEXMLHelper.addField(root,"version",lastArtifactVersion,"String",false);
			// increase date for one second
			SFEEXMLHelper.addField(root,"lastModifiedDate",new Date(lastModifiedDate.getTime()+1000),"DateTime",false);
			SFEEXMLHelper.addField(root,"folderId",projectTracker,"String",false);
			return new Object[]{document};
		} else {
			for(ArtifactSoapDO artifactRow:artifactHistoryRows)
			{
				GenericArtifact genericArtifact = artifactConverter.convert(artifactRow);
				if(dbDocument != null)
					populateSrcAndDest(dbDocument, genericArtifact);
				Document document = null;
				try {
					document = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
				} catch (GenericArtifactParsingException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				log.error(document.asXML());
				dataRows.add(document);
			}
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
		return dbHelper.getFromTime(document);
//		Node node= document.selectSingleNode("//TO_TIME");
//		if (node==null)
//			return null;
//		return node.getText();
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
}
