package com.full.feed;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
@PersistenceCapable
public class CommentTable {

	@Persistent
	private long commentId;

	public long getcommentId() {
		return commentId;
	}

	public long setcommentId(long id) {
		return this.commentId = id;
	}


	@Persistent
	private String comment;

	public String getcommentMessage() {
		return comment;
	}

	public void setcommentMessage(String message) {
		this.comment = message;
	}
	
	@Persistent
	private String forMessage;

	public String getMessage() {
		return forMessage;
	}

	public void setMessage(String userMessage) {
		this.forMessage = userMessage;
	}
}
