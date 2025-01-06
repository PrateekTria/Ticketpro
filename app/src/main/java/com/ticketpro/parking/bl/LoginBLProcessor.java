package com.ticketpro.parking.bl;

import java.util.ArrayList;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.User;

public class LoginBLProcessor extends BLProcessorImpl{

	public LoginBLProcessor(){
		setLogger(LoginBLProcessor.class.getName());
	}
	
	public ArrayList<User> getUsers() throws TPException{
		return proxy.getUsers();
	}
	
	public User doLogin(String username,String password) throws TPException {
		return proxy.doLogin(username,password);
	}
}
