package com.ticketpro.util;

import android.os.AsyncTask;

public abstract class TPTask{
	public AsyncTask asyncTask;
	
	public abstract void execute();
	
	public void cancel(){
		if(asyncTask!=null){
			asyncTask.cancel(true);
		}
		
		try{
			Thread.currentThread().interrupt();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}