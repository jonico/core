package com.collabnet.tracker.common;

/**
 * This represents an artifact in the artifact xml document
 * Used by the ClientArtifactListXMLHelper
 * 
 * @author Shawn Minto
 * 
 */
public class ClientArtifactComment {

	private String commentId;
	private String commentDate;
	private String commentText;
	private String commenter;

	public ClientArtifactComment(String commentId, String commentDate, String commentText, String commenter) {
		this.commentId = commentId;
		this.commentDate = commentDate;
		this.commentText = commentText;
		this.commenter = commenter;
	}

	public String getCommentId() {
		return commentId;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public String getCommentText() {
		return commentText;
	}

	public String getCommenter() {
		return commenter;
	}

}
