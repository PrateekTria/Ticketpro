package com.ticketpro.vendors;

import java.util.Date;
import java.util.List;

public class CachedResult {
	private Date cachedTime;
	private List results;
	
	public CachedResult(){
		cachedTime=new Date();
	}
	
	public CachedResult(List results){
		this.cachedTime=new Date();
		this.results = results;
	}
	
	public Date getCachedTime() {
		return cachedTime;
	}
	
	public void setCachedTime(Date cachedTime) {
		this.cachedTime = cachedTime;
	}
	
	public List getResults() {
		return results;
	}
	
	public void setResults(List results) {
		this.results = results;
	}
	
	public boolean hasExpired(){
		long now = new Date().getTime();
		long cached = cachedTime.getTime();
		long diff = cached - now;
		long l = Math.abs( diff / (60 * 1000) % 60);
		return l > CachedMap.cachedDuration;
	}
}
