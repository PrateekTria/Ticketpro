package com.ticketpro.vendors;

import java.util.HashMap;

public class CachedMap {
	public static long cachedDuration = 10 * 60 * 1000;
	private HashMap<String, CachedResult> results;
	
	public CachedMap(){
		results = new HashMap<String, CachedResult>();
	}
	
	public Object getResults(String key) {
		return results.get(key);
	}
	
	public void setResults(String key, CachedResult results) {
		this.results.put(key, results);
	}
}
