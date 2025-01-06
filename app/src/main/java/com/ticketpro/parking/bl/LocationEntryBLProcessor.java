package com.ticketpro.parking.bl;

import java.util.ArrayList;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.Direction;
import com.ticketpro.model.StreetPrefix;
import com.ticketpro.model.StreetSuffix;


public class LocationEntryBLProcessor extends BLProcessorImpl{
	
	public LocationEntryBLProcessor() throws TPException{
		setLogger(BLProcessorImpl.class.getName());
	}
	
	public ArrayList<Direction> getDirections() throws TPException{
		ArrayList<Direction> directions=new ArrayList<Direction>();
		directions=proxy.getDirections();
		return directions;
	}
	
	
	public ArrayList<StreetPrefix> getPrefixes() throws TPException{
		ArrayList<StreetPrefix> prefixes=new ArrayList<StreetPrefix>();
		prefixes=proxy.getStreetPrefixes();
		return prefixes;
	}
	
	
	public ArrayList<StreetSuffix> getSuffixes() throws TPException{
		ArrayList<StreetSuffix> suffixes=new ArrayList<StreetSuffix>();
		suffixes=proxy.getStreetSuffixes();
		return suffixes;
	}

}
