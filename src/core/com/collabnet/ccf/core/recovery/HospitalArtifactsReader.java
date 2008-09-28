package com.collabnet.ccf.core.recovery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.hospital.HospitalEntry;

public class HospitalArtifactsReader {
	private String hospitalFileName = null;
	private BufferedReader reader = null;
	public static final String FAILURE_START = "<Failure>";
	public static final String FAILURE_END = "</Failure>";
	public static final String SOURCE_START = "<Source>";
	public static final String SOURCE_END = "</Source>";
	public static final String EXCEPTION_START = "<Exception>";
	public static final String EXCEPTION_END = "</Exception>";
	public static final String DATA_START = "<Data>";
	public static final String DATA_END = "</Data>";
	private static final Log log = LogFactory.getLog(HospitalArtifactsReader.class);
	
	public HospitalArtifactsReader(String hospitalFileName) throws FileNotFoundException{
		this.hospitalFileName = hospitalFileName;
		this.init();
	}
	private void init() throws FileNotFoundException{
		if(reader == null){
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(hospitalFileName)));
		}
	}
	public HospitalEntry getNextEntry() throws IOException{
		String line = null;
		String source = null;
		StringBuffer exceptionTrace = new StringBuffer();
		String gaFile = null;
		boolean readingFailure = false;
		boolean readingTrace = false;
		HospitalEntry entry = null;
		while((line = reader.readLine()) != null){
			if(line.contains(FAILURE_START)){
				readingFailure = true;
			}
			if(line.contains(SOURCE_START)){
				if(line.contains(SOURCE_END)){
					source = line.substring(line.indexOf(SOURCE_START)+SOURCE_START.length(),
							line.indexOf(SOURCE_END));
				}
			}
			if(line.contains(DATA_START)){
				if(line.contains(DATA_END)){
					gaFile = line.substring(line.indexOf(DATA_START)+DATA_START.length(),
							line.indexOf(DATA_END));
				}
			}
			if(line.contains(EXCEPTION_START)){
				readingTrace = true;
				exceptionTrace.append(line.substring(line.indexOf(EXCEPTION_START)+EXCEPTION_START.length()));
			}
			else if(readingTrace){
				exceptionTrace.append(line);
			}
			if(line.contains(EXCEPTION_END)){
				readingTrace = false;
				if(line.indexOf(EXCEPTION_START) > 0){
					exceptionTrace.append(line.substring(0,line.indexOf(EXCEPTION_START)));
				}
			}
			if(line.contains(FAILURE_END)){
				readingFailure = false;
				entry = new HospitalEntry();
				entry.setDataFile(new File(gaFile));
				entry.setExceptionTrace(exceptionTrace.toString());
				entry.setSourceComponent(source);
			}
		}
		return entry;
	}
	public void close() {
		if(reader != null){
			try {
				reader.close();
			} catch (IOException e) {
				log.warn("Could not close hospital file reader",e);
			}
		}
	}
}
