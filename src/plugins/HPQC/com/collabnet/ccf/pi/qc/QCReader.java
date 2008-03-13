package com.collabnet.ccf.pi.qc;

import java.io.IOException;
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

public class QCReader extends QCConnectHelper implements
		IDataProcessor {
    
	private static final Log log = LogFactory.getLog(QCReader.class);
	
	private boolean isDry=true;

	private Object readerContext;

	private QCDefectHandler defectHandler;

    public QCReader(String id) {
	    super(id);
	}

	public Object[] process(Object data) {
		// TODO evaluate data to decide which items to fetch again
		if (!(data instanceof Document)) {
			log.error("Supplied data not in the expected dom4j format: "+data);
			return null;
		}
			
		Document document=(Document) data;
		if(document!=null) log.info(document.getText());
		try {
			connect();
		} catch (Exception e) {
			// TODO Declare exception so that it can be processed by OA exception handler
			log.error("Could not log into QC", e);
			return null;
		}

		// Fix these time operations
		String fromTime = getFromTime(document);
		String toTime = getToTime(document);
		log.error(fromTime);
		log.error(toTime);
		fromTime = toTime = null;
		Object[] result=readModifiedDefects(fromTime, toTime);
		disconnect();
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
		log.info("Before calling the parent connect()");
		super.connect();
		isDry=false;
	}
	
	public Object[] readModifiedDefects(String fromTime, String toTime) {
		// TODO Use the information of the firstTimeImport flag
		
		List<Document> dataRows=new ArrayList<Document>();
		List<GenericArtifact> defectRows;
		
		try {
			defectRows = defectHandler.getChangedDefects(qcc, fromTime, toTime);
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
			}
			else {
				log.error("DOCUMENT IS NULL");
			}
			
			dataRows.add(document);
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
		Node node= document.selectSingleNode("//TOTIME");
		if (node==null)
			return null;
		return node.getText();
	}

	private String getFromTime(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//FROMTIME");
		if (node==null)
			return null;
		return node.getText();
	}

	public void reset(Object context) {
	}

	public QCReader() {
		super();
	}
}
