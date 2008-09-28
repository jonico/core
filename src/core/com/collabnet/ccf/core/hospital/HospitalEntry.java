package com.collabnet.ccf.core.hospital;

import java.io.File;

public class HospitalEntry {
	private String sourceComponent = null;
	private String exceptionTrace = null;
	private File dataFile = null;
	public String getSourceComponent() {
		return sourceComponent;
	}
	public void setSourceComponent(String sourceComponent) {
		this.sourceComponent = sourceComponent;
	}
	public String getExceptionTrace() {
		return exceptionTrace;
	}
	public void setExceptionTrace(String exceptionTrace) {
		this.exceptionTrace = exceptionTrace;
	}
	public File getDataFile() {
		return dataFile;
	}
	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}
}
