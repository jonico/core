package com.collabnet.ccf.pi.sfee;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.openadaptor.auxil.connector.iostream.EncodingAwareObject;
import org.openadaptor.core.IDataProcessor;

import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

public class SFEEReader extends SFEEConnectHelper implements
		IDataProcessor {
    
	private static final Log log = LogFactory.getLog(SFEEReader.class);
	
	private boolean isDry=true;

	private Object readerContext;

	private SFEETrackerHandler trackerHandler;
	
	public Object[] process(Object data) {
		// TODO evaluate data to decide which items to fetch again
		if (!(data instanceof Document)) {
			log.error("Supplied data not in the expected dom4j format: "+data);
			return null;
		}
			
		Document document=(Document) data;
		String trackerId=getProjectTracker(document);
		if (trackerId==null|| trackerId.isEmpty()) {
			log.error("Could not extract TRACKERID: "+document.asXML());
			return null;
		}
		
		Date lastModifiedDate;
		boolean firstTimeImport=false;
		String lastModifiedDateString = getLastModifiedDateString(document);
		if (lastModifiedDateString==null|| lastModifiedDateString.isEmpty()) {
			// TODO Implement a special first time logic
			log.warn("This seems to be a first time import ...: "+document.asXML());
			lastModifiedDate=new Date(0);
			firstTimeImport=true;
		}
		else {
			lastModifiedDate=(Date)SFEEXMLHelper.asTypedValue(lastModifiedDateString, "DateTime");
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
		if (lastArtifactVersionString==null||lastArtifactVersionString.isEmpty()) {
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
			return null;
		}
		catch (IOException ex) {
			// TODO postpone exception handling to OA framework
			log.error("During the connection process to SFEE, an IO-Error occured", ex);
			return null;
		}
		
		Object[] result=readTrackerItems(trackerId,lastModifiedDate,lastArtifactId,lastArtifactVersion,firstTimeImport);
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
		isDry=false;
	}
	
	public Object[] readTrackerItems(String projectTracker, Date lastModifiedDate, String lastArtifactId, int lastArtifactVersion, boolean firstTimeImport) {
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
		
		if (artifactRows==null) {
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
		}
		
		for (ArtifactSoapDO artifactRow : artifactRows) {
			// TODO Set encoding by user
			Document document=SFEEXMLHelper.createXMLDocument(EncodingAwareObject.ISO_8859_1);
			
			//TODO let user specify rootTag
			Element root=document.addElement("SFEEArtifact");
			// first of all, set the deletion field and duplicate field to false
			SFEEXMLHelper.addField(root,"deleteFlag","false","Boolean",false);
			SFEEXMLHelper.addField(root,"isDuplicate","false","Boolean",false);
			SFEEXMLHelper.addField(root,"description",artifactRow.getDescription(),"String",false);
			SFEEXMLHelper.addField(root,"category",artifactRow.getCategory(),"String",false);
			SFEEXMLHelper.addField(root,"group",artifactRow.getGroup(),"String",false);
			SFEEXMLHelper.addField(root,"status",artifactRow.getStatus(),"String",false);
			SFEEXMLHelper.addField(root,"statusClass",artifactRow.getStatusClass(),"String",false);
			SFEEXMLHelper.addField(root,"customer",artifactRow.getCustomer(),"String",false);
			SFEEXMLHelper.addField(root,"priority",artifactRow.getPriority(),"Integer",false);
			SFEEXMLHelper.addField(root,"estimatedHours",artifactRow.getEstimatedHours(),"Integer",false);
			SFEEXMLHelper.addField(root,"actualHours",artifactRow.getActualHours(),"Integer",false);
			SFEEXMLHelper.addField(root,"closeDate",artifactRow.getCloseDate()," DateTime",false);
			SFEEXMLHelper.addField(root,"assignedTo",artifactRow.getAssignedTo(),"String",false);
			//hashMap.add(11,"assignedToFullname",artifactRows[i].getAssignedToFullname(),"String",false);
			// hashMap.add(12,"reportedInReleaseTitle",artifactRows[i].getReportedInReleaseTitle(),"String",false);
			SFEEXMLHelper.addField(root,"reportedReleaseId",artifactRow.getReportedReleaseId(),"String",false);
			//hashMap.add(14,"resolvedInReleaseTitle",artifactRows[i].getResolvedInReleaseTitle(),"String",false);
			SFEEXMLHelper.addField(root,"resolvedReleaseId",artifactRow.getResolvedReleaseId(),"String",false);
			// now comes all the custom fields
			SoapFieldValues customFields=artifactRow.getFlexFields();
			String[] customFieldNames=customFields.getNames();
			Object[] customFieldValues=customFields.getValues();
			String[] customFieldTypes=customFields.getTypes();
			//OrderedHashMap customFieldHashMap=new OrderedHashMap(customFieldNames.length);
			for (int j=0;j<customFieldNames.length;++j) {
				//insert(customFieldHashMap,customFieldNames[j], customFieldValues[j]);
				SFEEXMLHelper.addField(root,customFieldNames[j], customFieldValues[j],customFieldTypes[j],true);
			}
			//SFEEXMLHelper.addTrackerField(root,"flexFields",customFieldHashMap);
			SFEEXMLHelper.addField(root,"title",artifactRow.getTitle(),"String",false);
			SFEEXMLHelper.addField(root,"folderId",artifactRow.getFolderId(),"String",false);
			SFEEXMLHelper.addField(root,"Id",artifactRow.getId(),"String",false);
			SFEEXMLHelper.addField(root,"createdBy",artifactRow.getCreatedBy(),"String",false);
			SFEEXMLHelper.addField(root,"lastModifiedBy",artifactRow.getLastModifiedBy(),"String",false);
			SFEEXMLHelper.addField(root,"createdDate",artifactRow.getCreatedDate(),"DateTime",false);
			SFEEXMLHelper.addField(root,"lastModifiedDate",artifactRow.getLastModifiedDate(),"DateTime",false);
			// SFEEXMLHelper.addField(root,"version",artifactRow.getVersion(),"Integer",false);
			// We treat version as if it was not an integer but a string (makes future conflict resolution easier)
			// TODO Rethink about that decision
			SFEEXMLHelper.addField(root,"version",artifactRow.getVersion(),"String",false);
			log.error(document.asXML());
			dataRows.add(document);
		}
		return dataRows.toArray();
	}
	
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		
		trackerHandler = new SFEETrackerHandler(getServerUrl());
	}

	
	private String getProjectTracker(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TRACKERID");
		if (node==null)
			return null;
		return node.getText();
		
	}
	
	private String getLastModifiedDateString(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TIMESTAMP");
		if (node==null)
			return null;
		return node.getText();
	}
	
	private String getLastArtifactId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//ARTIFACTID");
		if (node==null)
			return null;
		return node.getText();
	}
	
	private String getLastArtifactVersionString(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//ARTIFACTVERSION");
		if (node==null)
			return null;
		return node.getText();
	}

	public void reset(Object context) {
	}
}
