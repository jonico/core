package trial2;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.QCConfigHelper;
import com.collabnet.ccf.pi.qc.v90.QCConnectHelper;
import com.collabnet.ccf.pi.qc.v90.QCDefectHandler;

public class QCReaderTrial2 extends QCConnectHelper implements
		IDataProcessor {
    
	private static final Log log = LogFactory.getLog(QCReaderTrial2.class);
	
	private boolean isDry=true;

	private Object readerContext;

	private QCDefectHandler defectHandler;

    public QCReaderTrial2(String id) {
	    super(id);
	}

	public Object[] process(Object data) {
		log.error("=================================");
		log.error("Entering QCReaderTrial2");
		log.error(getServerUrl());
		log.error(getDomain());
		log.error(getProjectName());
		
		// TODO evaluate data to decide which items to fetch again
		if (!(data instanceof Document)) {
			log.error("Supplied data not in the expected dom4j format: "+data);
			return null;
		}
			
		Document document=(Document) data;
		
		// Print out the input values
		log.error(document.asXML());
		
		final int opGaArraySize = 3;
		Document[] docArray = new Document[opGaArraySize];
		
		try {
			connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0 ; i < opGaArraySize ; i++) {
			GenericArtifact ga = QCConfigHelper.getSchemaFields(getQcc());
			Document doc = null;
			ga.setArtifactLastModifiedDate(DateUtil.format(new Date()));
			ga.setArtifactVersion(Integer.toString(i));

			ga.setSourceArtifactId(this.getSourceArtifactId(document));
			ga.setSourceRepositoryId(this.getSourceRepositoryId(document));
			ga.setSourceRepositoryKind(this.getSourceRepositoryKind(document));
			ga.setSourceSystemId(this.getSourceSystemId(document));
			ga.setSourceSystemKind(this.getSourceSystemKind(document));

			ga.setTargetArtifactId(this.getTargetArtifactId(document));
			ga.setTargetRepositoryId(this.getTargetRepositoryId(document));
			ga.setTargetRepositoryKind(this.getTargetRepositoryKind(document));
			ga.setTargetSystemId(this.getTargetSystemId(document));
			ga.setTargetSystemKind(this.getTargetSystemKind(document));
			
			// mandatory fields to be set
			ga.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
			ga.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			ga.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
			try {
				doc = GenericArtifactHelper.createGenericArtifactXMLDocument(ga);
			}
			catch (GenericArtifactParsingException gape) {
				log.error("EXCEPTION:");
				log.error("Iteration:" + i);
				log.error(gape.toString());
				throw new RuntimeException(gape);
			}
			log.error(doc.asXML());
			docArray[i] = doc;
		}

		log.error("Exiting QCReaderTrial2");
		log.error("=================================");

		return docArray;
	}

	public Object getReaderContext() {
		return readerContext;
	}
	
	public boolean isDry() {
		return isDry;
	}

	@Override
	public void connect() throws IOException {
		log.info("Before calling the parent connect()");
		super.connect();
		isDry=false;
	}
	
	public Object[] readModifiedDefects(String fromTime, String toTime, String sourceArtifactId, String sourceRepositoryId, String sourceRepositoryKind, String sourceSystemId, String sourceSystemKind, String targetRepositoryId, String targetRepositoryKind, String targetSystemId, String targetSystemKind) {
		// TODO Use the information of the firstTimeImport flag
		
		List<Document> dataRows=new ArrayList<Document>();
		List<GenericArtifact> defectRows;
		
		try {
			log.error("The fromTime coming from HQSL DB is:" + fromTime + " and the toTime is" +toTime);
			defectRows = defectHandler.getChangedDefects(this.getQcc(), this.getUserName(), fromTime, sourceArtifactId, sourceRepositoryId, sourceRepositoryKind, sourceSystemId, sourceSystemKind, targetRepositoryId, targetRepositoryKind, targetSystemId, targetSystemKind);

		} catch (Exception e) {
			// TODO Throw an exception?
			log.error("During the artifact retrieval process from QC, an error occured",e);
			return null;
		}
		
		if (defectRows==null || defectRows.size() == 0) {

			// Return an empty generic artifact
			// TODO: Should we fill in the latest from and to time?
			GenericArtifact emptyGenericArtifact = new GenericArtifact();
			Document document = null;
			try {
				document = GenericArtifactHelper.createGenericArtifactXMLDocument(emptyGenericArtifact);
			}
			catch (GenericArtifactParsingException gape) {
				// TODO: Handle this appropriately
				log.error("GenericArtifactParsingException while creating a GenericArtifact XML from an empty GenericArtifact object");
			}
			return new Object[]{document};
		}
		
		try {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("resultantGenericArtifact.xml")));
		
		for (GenericArtifact defectRow: defectRows) {
			Document document = null;
			

			try {
				document = GenericArtifactHelper.createGenericArtifactXMLDocument(defectRow);
			}
			catch (GenericArtifactParsingException gape) {
				// TODO: Handle this appropriately
				log.error("GenericArtifactParsingException while creating a GenericArtifact XML from the GenericArtifact object");
			}

			if (document != null) {
				log.error(document.asXML());
				out.println(document.asXML());
			}
			else {
				log.error("DOCUMENT IS NULL");
			}
			
			dataRows.add(document);
		}
		}
		catch (IOException e) {
		log.error("IOException in QCReaderOutputChecker caught " + e);	
		}
		
		return dataRows.toArray();
	}
	
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		// Capture the return exception list and validate the exceptions
		
		defectHandler = new QCDefectHandler();
	}

	private String getToTime(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TO_TIME");
		if (node==null)
			return null;
		return node.getText();
	}

	private String getFromTime(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//FROM_TIME");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getSourceArtifactId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_ARTIFACT_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	private String getSourceRepositoryId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getSourceRepositoryKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	private String getSourceSystemId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getSourceSystemKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getTargetArtifactId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_ARTIFACT_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	private String getTargetRepositoryId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getTargetRepositoryKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	private String getTargetSystemId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getTargetSystemKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}
	
	
	public void reset(Object context) {
	}

	public QCReaderTrial2() {
		super();
	}
}
