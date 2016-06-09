package com.full.feed;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("unused")
@PersistenceCapable
public class UpdateContents {
	
	@Persistent
	private long id;

	public long getId() {
		return id;
	}

	public long setId(long id) {
		return this.id = id;
	}


	@Persistent
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	@Persistent
	private String userMail;

	public String getMail() {
		return userMail;
	}

	public void setMail(String userMail) {
		this.userMail = userMail;
	}

	@Persistent
	private long date;

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	
	@Persistent
	private int like;

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}
	
	@Persistent
	private List<String> likedPeople;

	public List<String> getLikedPeople() {
		return likedPeople;
	}

	public void setLikedPeople(List<String> likedPeople) {
		this.likedPeople = likedPeople;
	}	
	
//	@Persistent
//	private HashSet<String> likedPeople;
//
//	public HashSet<String> getLikedPeople() {
//		return likedPeople;
//	}
//
//	public void setLikedPeople(HashSet<String> likedPeople) {
//		this.likedPeople = likedPeople;
//	}	
	}