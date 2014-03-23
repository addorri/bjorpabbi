package com.bjor.db;

/**
 * @author arnar
 * @version 1
 *
 */
public class Session {
	
	/** Unique Session id, auto-incremented in sql */
	private long id;
	
	/** see Global */
	private String time;
	private int buzz;
	private int nod;
	private int duration;
	private int type;
	
	
	/**
	 * Constructor
	 */
	public Session() {
		
	}

	/** Getters and setters */
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String string) {
		this.time = string;
	}
	public int getBuzz() {
		return buzz;
	}
	public void setBuzz(int buzz) {
		this.buzz = buzz;
	}
	public int getNod() {
		return nod;
	}
	public void setNod(int nod) {
		this.nod = nod;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return time + "";
	}
}
