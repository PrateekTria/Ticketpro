package com.ticketpro.logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import android.content.Context;
import android.util.Log;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Implementer of logger functionality of the apps
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 */
public class LoggerConfigurator {
	public static void toggleDebugLog(Context context, boolean enableLogging){
		try {
			@SuppressWarnings("unchecked")
			List<Logger> loggers = Collections.<Logger> list(LogManager.getCurrentLoggers());
			loggers.add(LogManager.getRootLogger());
			
			for (Logger logger : loggers) {
				logger.setLevel(!enableLogging ? Level.OFF : Level.ALL);
			}
			
		} catch (Exception e) {
			Log.e("LoggerConfigurator", TPUtility.getPrintStackTrace(e));
		}
	}
	
	public void configLogger(String version) throws Exception{
		try{
			LogConfigurator logConfigurator = new LogConfigurator();
			File logFile=new File(TPUtility.getDataFolder() + TPConstant.LOG_FILE_NAME);
			if(!logFile.exists()) {
				try {
					logFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
			logConfigurator.setFileName(TPUtility.getDataFolder() + TPConstant.LOG_FILE_NAME);
		    logConfigurator.setRootLevel(Level.DEBUG);
		    logConfigurator.setLevel("org.apache", Level.ERROR);
		    logConfigurator.setFilePattern("[TP v"+version+"] %d - [%p::%c] - %m%n");
		    logConfigurator.configure();
		
		}catch (Exception e) {
			throw e;
		}
	}
}
