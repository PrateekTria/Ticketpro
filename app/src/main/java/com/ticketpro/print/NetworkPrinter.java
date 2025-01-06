package com.ticketpro.print;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.print.model.TCPIPInfo;

public class NetworkPrinter implements Runnable {
	
	private TCPIPInfo TCPIPInfo;
	private Context context;
	private String printString;

	// use to update the UI information.
	private Handler handler = new Handler(); 
	private SharedPreferences mPreferences;
	
	public NetworkPrinter(Context context, String printString) {
		this.context=context;
		this.printString=printString;
		
		if(TPApplication.getInstance().printDebugMode){
			return;
		}
		
		mPreferences=context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		TCPIPInfo = GetTCPIPConfigSetting();
		if(TCPIPInfo==null){
			AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		    builder.setTitle("Printing Error");
		    builder.setCancelable(true);
		    builder.setMessage("Network Printer is not configured properly.");
		    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					dialog.dismiss();
				}
			});
		    
		    AlertDialog alert = builder.create();
		    alert.show();
			return;
		}
			
		new Thread(NetworkPrinter.this, "PrintingTask").start();
	}

	public TCPIPInfo GetTCPIPConfigSetting() {
		TCPIPInfo tcpIPInfo = null;
		String ipAddress=mPreferences.getString(TicketPrinter.PRINTER_IPADDRESS_KEY, null);
		int port=mPreferences.getInt(TicketPrinter.PRINTER_TCPIPPORT_KEY, 0);
		if(ipAddress!=null && port!=0){
			tcpIPInfo=new TCPIPInfo(ipAddress,port);
		}
		
		return tcpIPInfo;
	}

	public void TCPIPPrint(){
		Looper.prepare();
		
		String MsgStr = "";
		InputStream inStream = null;
		DataOutputStream dOutStream = null;
		try {
			// target printer
			String deviceAddr = TCPIPInfo.getIPAddress();
			int TCPIPPort = TCPIPInfo.getTCPIPPort();
			
			byte[] finalArray = this.printString.getBytes();

			// create a socket to communicate with selected print ip address:
			// port number
			MsgStr = "Create TCP/IP socket";
			//mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));
			
			Socket socket = null;
			socket = new Socket(deviceAddr, TCPIPPort);

			// associate the output stream with the open socket
			MsgStr = "Send data to printer";
			//mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));
			
			dOutStream = new DataOutputStream(socket.getOutputStream());

			// send the label sample to printer, in turn, printer will print
			// this label.
			dOutStream.write(finalArray, 0, finalArray.length);
			dOutStream.flush();
			
			MsgStr = "Done Printing.";
		} catch (SocketException se) {
			MsgStr = se.getMessage();
		} catch (UnknownHostException uhe) {
			MsgStr = uhe.getMessage();
		} catch (IOException e) {
			MsgStr = e.getMessage();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
					inStream = null;
				} catch (IOException e) {}
			}
			if (dOutStream != null) {
				try {
					dOutStream.close();
					dOutStream = null;
				} catch (IOException e) {}
			}
		}

		// display the last result to the UI here
		mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));
		
		Looper.loop();
	}//TCPIPPrint
	
	// -----------------------------------------------------------------
	// Printing a selected file using a selected communication method.
	// ------------------------------------------------------------------
	// @Override
	public void run() {
		TCPIPPrint();
	}// run()
	
	 // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what) 
            {
	            case TicketPrinter.MESSAGE_STATE_CHANGE:
	                switch (msg.arg1) 
	                {
		                case BluetoothChatService.STATE_CONNECTED:
		                	//DisplayPrintingStatusMessage("Connected");
		                    break;
		                
		                case BluetoothChatService.STATE_CONNECTING:
		                	//DisplayPrintingStatusMessage("Connecting...");
		                    break;
		               
		                case BluetoothChatService.STATE_LISTEN:
		                case BluetoothChatService.STATE_NONE:
		                	break;
	                }
	                break;
	            
	            case TicketPrinter.MESSAGE_WRITE:
	                break;
	            
	            case TicketPrinter.MESSAGE_READ:
	                break;
	            
	            case TicketPrinter.MESSAGE_DEVICE_NAME:
	                //Toast.makeText(context, "Connected to "+ mConnectedBTDeviceName, Toast.LENGTH_SHORT).show();
	                break;
	                
	            case TicketPrinter.MESSAGE_TOAST:
	            	String message=(String)msg.obj;
	            	if(message!=null && !message.isEmpty()){
	            		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	            	}
	            	
	                break;
            }
        }
    };

}