/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.pi.qc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.pi.qc.v90.QCConnectHelper;

public class QCReaderOutputWriter extends QCConnectHelper implements IDataProcessor {
	
	private String fileName;
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
		@SuppressWarnings("unused")
		int intBugId=0;
		GenericArtifact changedArtifact= new GenericArtifact();
		@SuppressWarnings("unused")
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
			@SuppressWarnings("unused")
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
	
	@SuppressWarnings("unchecked")
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