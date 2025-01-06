package com.ticketpro.print;
 

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;

import com.example.tscdll.TSCActivity;

import com.ticketpro.parking.activity.TPApplication;

/**
 * This class print data for TSC BlueTooth printer
 */
public class TscPrinter   {

    private static final String TAG = "TSCPrinter";
    TSCActivity TscDll = new TSCActivity();
    Context context;
    private Activity activity;
    
    public TscPrinter(Activity activity) {
		this.activity = activity;
		this.context = TPApplication.getInstance();
	}
 
 
    public boolean processTSCAction(String action, JSONArray params) throws JSONException {
        if (action.equals("printBarCode")) {
            try {
                 this.printBarCode(params);
            }catch (Exception err){
                 err.printStackTrace();
            }
            return true;
        }else if(action.equals("connectPrinter")){
            String macAddress = params.getString(0);
            try {
                 this.connectPrinter(macAddress);
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }else if(action.equals("setupPrinter")){
            try {
                this.setupPrinter();
            }catch (Exception e){
               e.printStackTrace();
            }
            return true;
        }
        return false;
    }


    public String connectPrinter(String macAddress){
        return TscDll.openport(macAddress);
    }

    public String setupPrinter() {
        Integer width = 70;
        Integer height = 110;
        Integer speed = 4;
        Integer density = 4;
        Integer sensor = 0;
        Integer sensor_distance = 0;
        Integer sensor_offset = 0;
        return TscDll.setup(width,height,speed,density,sensor,sensor_distance,sensor_offset);
    }

    private String printBarCode(JSONArray args) throws JSONException {
        Integer x = args.getInt(0);
        Integer y = args.getInt(1);
        String type = args.getString(2);
        Integer height = args.getInt(3);
        Integer human_readable = args.getInt(4);
        Integer rotation = args.getInt(5);
        Integer narrow = args.getInt(6);
        Integer wide = args.getInt(7);
        String content = args.getString(8);
        TscDll.barcode(x,y,type,height,human_readable,rotation,narrow,wide,content);
        return this.printLabel(1,"");
    }

    public String printLabel(Integer quantity,String message){
    	if(!getStatus().equalsIgnoreCase("-1")){
    	TscDll.clearbuffer();
    	TscDll.sendcommand(message);
    	}
        return TscDll.printlabel(quantity, 1);
    }
    
    public void printSample(){
    	try{
    		TscDll.openport("00:19:0E:A1:D5:B6");
        	
        	TscDll.setup(70, 110, 4, 4, 0, 0, 0);
        	TscDll.clearbuffer();
        	if(!getStatus().equalsIgnoreCase("-1")){
        	TscDll.sendcommand("SET TEAR ON\n");
        	TscDll.sendcommand("SET COUNTER @1 1\n");
        	TscDll.sendcommand("@1 = \"0001\"\n");
        	TscDll.sendcommand("TEXT 100,300,\"3\",0,1,1,@1\n");
        	TscDll.barcode(100, 100, "128", 100, 1, 0, 3, 3, "123456789");
        	TscDll.printerfont(100, 250, "3", 0, 1, 1, "987654321");
        	TscDll.printlabel(2, 1);
        	
        	TscDll.closeport(700);
        	} 
    	}catch(Exception e){e.printStackTrace();}
    }

    private String Disconnect(){
        return TscDll.closeport();
    }

    private String getStatus(){
        return TscDll.status();
    }

}
