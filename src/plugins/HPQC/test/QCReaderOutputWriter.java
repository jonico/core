package test;

import java.io.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.auxil.connector.iostream.EncodingAwareObject;
import org.openadaptor.core.IDataProcessor;

import com.collabnet.ccf.pi.qc.QCWriter;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.pi.qc.QCConnectHelper;
import com.collabnet.ccf.pi.qc.QCDefect;
import com.collabnet.ccf.pi.qc.QCDefectHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QCReaderOutputWriter extends QCConnectHelper implements IDataProcessor {
	
	private String fileName;
	private QCDefectHandler defectHandler;
	private static final Log log = LogFactory.getLog(QCConnectHelper.class);
		
	public Object[] process(Object data) {
		
		
		//String fileName;
		if (data == null) {
		      throw new NullRecordException("Expected Document. Null record not permitted.");
		    }

		    if (!(data instanceof Document)) {
		      throw new RecordFormatException("Expected Document. Got [" + data.getClass().getName() + "]");
		    }
		Object[] result = processXMLDocuments((Object) data, this.getFileName()); 
		return result;
	}
	
	public Object[] processXMLDocuments(Object data, String fileName) {
		
		//List<List<Document>> retObj=new ArrayList<List<Document>>();
		//List<Document> incomingObj= new ArrayList<Document>();
		Document incomingDoc = (Document) data;
		incomingDoc = changeForQCWriter(incomingDoc);
		//retObj.add(incomingObj);
		
		try{
			FileOutputStream f = new FileOutputStream(fileName); 
			ObjectOutputStream s = new ObjectOutputStream(f);
			
			s.writeObject(incomingDoc);
			}
		catch(IOException e) {
			System.out.println("File handling exception" + e);		
		}
		
		Object[] result = {incomingDoc};
		return result;
		}
	
	
	public Document changeForQCWriter(Document incomingObj) {
		
		Document changedDoc = null;
		int intBugId=0;
		GenericArtifact changedArtifact= new GenericArtifact();
		String bugId = new String();
		System.out.println("------------------------------------------------------------");
		
		try {
			changedArtifact = GenericArtifactHelper.createGenericArtifactJavaObject((Document) incomingObj);
		}
		catch(Exception e) {
			System.out.println("GenericArtifact Parsing exception" + e);
		}		
		//intBugId = Integer.parseInt(QCWriter.getFieldValueFromGenericArtifact(changedArtifact, "BG_BUG_ID"));
		//intBugId = intBugId+100;
		List<GenericArtifactField> allFields = changedArtifact.getAllGenericArtifactFields();
		for (int cnt=0; cnt < allFields.size(); cnt++) {
			GenericArtifactField thisField = allFields.get(cnt);
			
			/*if(thisField.getFieldName().equals("BG_BUG_ID"))
				thisField.setFieldValue(intBugId);
			if(thisField.getFieldName().equals("BG_DESCRIPTION"))
				thisField.setFieldValue("Artifact "+intBugId+":"+thisField.getFieldValue());
			if(thisField.getFieldName().equals("BG_SUMMARY"))
				thisField.setFieldValue("Artifact "+intBugId+":"+thisField.getFieldValue());
			if(thisField.getFieldName().equals("BG_SUBJECT"))
				thisField.setFieldValue(intBugId);
			if(thisField.getFieldName().equals("BG_USER_01"))
				thisField.setFieldValue("Artifact "+intBugId+":"+thisField.getFieldValue());
			if(thisField.getFieldName().equals("BG_USER_02"))
				thisField.setFieldValue("Artifact "+intBugId+":"+thisField.getFieldValue());
			*/	
		}
		try {
		changedDoc = GenericArtifactHelper.createGenericArtifactXMLDocument(changedArtifact);
		}
		catch(Exception e) {
			System.out.println("GenericArtifact Parsing exception" + e);
		}
		//System.out.println("Changed Artifact::" + changedDoc.asXML());
		
		return changedDoc;
	}
	
	public void validate(List exceptions) {
		super.validate(exceptions);
		// Capture the return exception list and validate the exceptions
		if (getFileName() == null) {
			log.error("serverUrl-property not set");
			exceptions.add(new ValidationException("serverUrl-property not set",this));
		}
	}
	
	public void reset(Object context) {
	}

	public QCReaderOutputWriter() {
		super();
	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}