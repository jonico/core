package com.collabnet.ccf.pi.qc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;

public class QCWriter extends QCConnectHelper implements
		IDataProcessor {

	private static final Log log = LogFactory.getLog(QCWriter.class);
	private String CreateToken;
	private QCDefectHandler defectHandler;	

    public QCWriter(String id) {
	    super(id);
	}

	private Object[] processXMLDocument(Document data) {
		
		// Get Control information thusly
		// this field is probably added from a read processor or read connector component
		Boolean deleteArtifact=(Boolean)QCXMLHelper.asTypedValue(QCXMLHelper.getSingleValue(data, "deleteFlag", false), "Boolean");

		// Treat all fields as Strings for now
		List<String> defectFieldNames=new ArrayList<String>();
		List<String> defectFieldValues=new ArrayList<String>();
		for (NamesTypesAndValues key : QCXMLHelper.getAllDefectFields(data)) {
			defectFieldNames.add(key.name);
			defectFieldValues.add((String)QCXMLHelper.asTypedValue(key.value, key.type));
		}

		connect();
		// check whether we should create or update the artifact
		String bugId = QCXMLHelper.getSingleValue(data,"BG_BUG_ID",false); 
		if (bugId.equals(getCreateToken())) {
			// find out whether we should delete something, that is not even present here
			if (deleteArtifact.booleanValue()) {
				log.warn("Cannot delete an artifact that is not even mirrored (yet): "+data.asXML());
				return null;
			}
			try {
				// create the defect
				IQCDefect result = defectHandler.createDefect(qcc, defectFieldNames, defectFieldValues);
			} catch (Exception e) {
				log.error("Exception occured while creating defect in QC:"+data.asXML(),e);
				disconnect();
				return null;
			}
		}
		else {
			try {
					if (deleteArtifact.booleanValue()) {
						defectHandler.deleteDefect(QCXMLHelper.getSingleValue(data,"BG_BUG_ID",false));
					}
					else {
						IQCDefect result = defectHandler.updateDefect(qcc, bugId, defectFieldNames, defectFieldValues);
						if (result==null) {
							// conflict resolution has decided in favor of the target copy
							disconnect();
							return new Object[0];
						}
					}
			}
			catch (Exception e) {
				log.error("Exception occured while updating defect in QC:"+data.asXML(),e);
				disconnect();
				return null;
			}
		}
		
		Object[] result={data};
		disconnect();
		return result;
	}

	@Override
	public void connect()  {
		try {
			super.connect();
		} catch (IOException e) {
			// TODO Throw an exception?
			log.error("Could not login into QC: ",e);
		}
	}
	
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		// Capture the return exception list and validate the exceptions

		defectHandler = new QCDefectHandler();
	}

	public Object[] process(Object data) {
		if (data == null) {
		      throw new NullRecordException("Expected Document. Null record not permitted.");
		    }

		    if (!(data instanceof Document)) {
		      throw new RecordFormatException("Expected Document. Got [" + data.getClass().getName() + "]");
		    }
		    
		    return processXMLDocument((Document) data);
	}

	public void reset(Object context) {
	}

	private String getToTime(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TOTIME");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getCreateToken() {
		return CreateToken;
	}

	public void setCreateToken(String createToken) {
		CreateToken = createToken;
	}

	public QCWriter() {
		super();
	}
}
