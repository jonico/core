package com.collabnet.ccf.pi.qc.api;


public class Comment implements IComment {
	private String author;
	private String created;
	private String body;
	
	public String getAuthor() {
		return author;
	}

	public String getBody() {
		return body;
	}

	public String getCreated() {
		return created;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setCreated(String created) {
		this.created = created;
	}	
}
