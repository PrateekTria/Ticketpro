package com.ticketpro.parking.bl;

import java.util.List;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.State;

public class LookupBLProcessor extends BLProcessorImpl{

	public LookupBLProcessor(){
		setLogger(LookupBLProcessor.class.getName());
	}
	
	public List<State> getStates() throws TPException{
		return proxy.getStates();
	}
	
	public List<MakeAndModel> getMakeAndModels() throws TPException{
		return proxy.getMakeAndModels();
	}
	
}
