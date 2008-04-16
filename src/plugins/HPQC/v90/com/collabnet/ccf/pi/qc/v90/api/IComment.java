package com.collabnet.ccf.pi.qc.v90.api;

import java.io.Serializable;

public interface IComment extends Serializable {
	String getAuthor();
	String getCreated();
	String getBody();
	void setAuthor(String author);
	void setCreated(String created);
	void setBody(String body);
}
