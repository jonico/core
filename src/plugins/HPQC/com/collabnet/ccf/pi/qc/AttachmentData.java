package com.collabnet.ccf.pi.qc;

public class AttachmentData {

	String name;
	byte [] contents;

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {	
		this.contents = contents;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
