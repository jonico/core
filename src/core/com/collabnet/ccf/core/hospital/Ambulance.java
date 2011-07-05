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

package com.collabnet.ccf.core.hospital;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.MessageException;
import org.openadaptor.core.exception.ValidationException;
import org.openadaptor.core.lifecycle.LifecycleComponent;

import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.CCFUtils;

/**
 * This is the exception handler class that catches
 * and logs the exception, the origin of the exception
 * and the input xml document that resulted these exceptions
 * 
 * @author madhusuthanan (madhusuthanan@collab.net)
 *
 */
public class Ambulance extends LifecycleComponent implements
		IDataProcessor {
    
	private static final Log log = LogFactory.getLog(Ambulance.class);
	private String hospitalFileName = null;
	private FileOutputStream fos = null;
	private String artifactsDirectory = null;
	private File artifactsDirectoryFile = null;
	
	public Ambulance(String id) {
	    super(id);
	}
	
	public Ambulance() {
		super();
	}
	public static Document createXMLDocument(String encoding) {
		Document document=DocumentHelper.createDocument();
		document.setXMLEncoding(encoding);
		return document;
	}
	public Object[] process(Object data) {
		log.warn("Artifact reached ambulance");
		if(data instanceof MessageException){
					MessageException exception = (MessageException) data;
					Object dataObj = exception.getData();
					String source = exception.getOriginatingModule();
					Exception rootCause = exception.getException();
					Document doc = createXMLDocument("UTF-8");
					Element failure = doc.addElement("Failure");
					Element failureSource = failure.addElement("Source");
					if(source != null)
					failureSource.setText(source);
					Element exceptionDetail = failure.addElement("Exception");
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					PrintStream st = new PrintStream(bos);
					rootCause.printStackTrace(st);
					exceptionDetail.setText(new String(bos.toByteArray()));
					Element dataElement = failure.addElement("Data");
					if(dataObj instanceof Document){
						Document dataDoc = (Document) dataObj;
						String artifactFileName = null;
						try {
							artifactFileName = CCFUtils.getTempFileName(dataDoc);
						} catch (GenericArtifactParsingException e) {
							log.warn("The data that reached the hospital is not a Generic Artifact");
						}
						if(artifactFileName == null){
							artifactFileName = "sync-info";
						}
						String tempFilePath = null;
						FileOutputStream fos = null;
						try {
							File tempFile = File.createTempFile(artifactFileName, ".xml", artifactsDirectoryFile);
							fos = new FileOutputStream(tempFile);
							String dataXML = dataDoc.asXML();
							fos.write(dataXML.getBytes());
							fos.flush();
							fos.close();
							tempFilePath = tempFile.getAbsolutePath();
						} catch (IOException e) {
							log.error("Could not create temporary File", e);
						}
						finally {
							if(fos != null){
								try {
									fos.close();
								} catch (IOException e) {
									log.warn("Could not close temp file stream",e);
								}
							}
						}
						dataElement.setText(tempFilePath);
					}
					String writeData = failure.asXML();
					try {
						fos.write(writeData.getBytes());
						fos.write(System.getProperty("line.separator").getBytes());
						fos.flush();
					} catch (IOException e) {
						log.error("An IO-Exception occured in the hospital: "+e.getMessage());
						return null;
					}
				}
		return new Object[0];
	}

	public void reset(Object context) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public void validate(List exceptions) {
		if (hospitalFileName==null) {
			exceptions.add(new ValidationException(
					"hospitalFileName-property not set", this));
		}
		
		if (fos==null) {
			exceptions.add(new ValidationException(
					"Could not open hospital file "+hospitalFileName, this));
		}
		if(artifactsDirectory == null){
			exceptions.add(new ValidationException(
					"Artifacts directory is not set ", this));
		}
		else {
			File artifactsDirFile = new File(artifactsDirectory);
			if(artifactsDirFile.exists()){
				if(artifactsDirFile.isDirectory()){
					artifactsDirectoryFile = artifactsDirFile;
				}
				else {
					exceptions.add(new ValidationException(
							"Artifacts directory "+artifactsDirectory+" is not a valid directory", this));
				}
			}
			else {
				if(artifactsDirFile.mkdirs()){
					artifactsDirectoryFile = artifactsDirFile;
				}
				else {
					exceptions.add(new ValidationException(
							"Could not create artifacts directory "+artifactsDirectory, this));
				}
			}
			
		}
		
	}
	public void stop() {
	    if(fos != null){
	    	try {
				fos.close();
			} catch (IOException e) {
				log.error("Exception when trying to close the hospital file stream", e);
			}
	    }
	    super.stop();
	}

	public String getHospitalFileName() {
		return hospitalFileName;
	}

	public void setHospitalFileName(String hospitalFileName) {
		this.hospitalFileName = hospitalFileName;
		try {
			fos = new FileOutputStream(hospitalFileName);
		} catch (FileNotFoundException e) {
			log.error("Could not open hospital file "+hospitalFileName+ " :"+hospitalFileName);
		}
	}

	public String getArtifactsDirectory() {
		return artifactsDirectory;
	}

	public void setArtifactsDirectory(String artifactsDirectory) {
		this.artifactsDirectory = artifactsDirectory;
	}

}
