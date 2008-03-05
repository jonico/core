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

import com.collabnet.ccf.core.config.Field;

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
		super.connect();
		isDry=false;
	}
	
	public Object[] readModifiedDefects(String fromTime, String toTime) {
		// TODO Use the information of the firstTimeImport flag
		
		List<Document> dataRows=new ArrayList<Document>();
		List<IQCDefect> defectRows;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		Date today = new Date();
		
		// Take care of dates
		// If the last toTime exists, make this the
		// from time for this run
		//if(toTime != null && !toTime.equals(""))
			fromTime = toTime;
		// Current ToTime is always now
		toTime = df.format(today);
		
		try {
			defectRows = defectHandler.getChangedDefects(qcc, fromTime, toTime);
		} catch (Exception e) {
			// TODO Throw an exception?
			log.error("During the artifact retrieval process from QC, an error occured",e);
			return null;
		}
		
		if (defectRows==null || defectRows.size() == 0) {
			// we only received no entries
			log.info("no modifications received from QC. Updating the time fields.");
			/**
			 * Construct a fake duplicate data entry with a changed from and to time
			 */
			// TODO Set encoding by user
			Document document=QCXMLHelper.createXMLDocument(EncodingAwareObject.ISO_8859_1);
			
			//TODO let user specify rootTag
			Element root=document.addElement("QCArtifact"); 
			QCXMLHelper.addField(root,"deleteFlag","false","Boolean",false);
			QCXMLHelper.addField(root,"isDuplicate","true","Boolean",false);
			// update the new time intervals
			QCXMLHelper.addField(root, "fromTime", fromTime, "String", false);
			QCXMLHelper.addField(root, "toTime",toTime, "String", false);
			QCXMLHelper.addField(root, "project",getProjectName(), "String", false);
			return new Object[]{document};
		}
		
		String[] fieldNames = qcc.getFieldNames();
		String[] fieldTypes = qcc.getFieldTypes();
		List<Field> fields = QCConfigHelper.getSchemaFields(qcc);
		for (IQCDefect defectRow : defectRows) {
			// TODO Set encoding by user
			Document document=QCXMLHelper.createXMLDocument(EncodingAwareObject.ISO_8859_1);
			
			//TODO let user specify rootTag
			Element root=document.addElement("QCArtifact");
			String[] fieldValues = defectRow.getFieldValues(fieldNames, fieldTypes);
			for (int j=0;j< fieldNames.length;++j) {
				boolean isFlexField = true;
				if (fieldNames[j].equals("BG_BUG_ID"))
					isFlexField = false;
				
				QCXMLHelper.addField(root, fieldNames[j], fieldValues[j], "String", isFlexField);
			}

			QCXMLHelper.addField(root,"deleteFlag","false","Boolean",false);
			QCXMLHelper.addField(root,"isDuplicate","false","Boolean",false);
			// update the new time intervals
			QCXMLHelper.addField(root, "fromTime", fromTime, "String", false);
			QCXMLHelper.addField(root, "toTime",toTime, "String", false);
			QCXMLHelper.addField(root, "project",getProjectName(), "String", false);
			

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
