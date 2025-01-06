package com.ticketpro.print;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.example.tscdll.TSCActivity;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.print.model.BluetoothInfo;
import com.ticketpro.print.model.TicketPROConstant;
import com.ticketpro.util.TPUtility;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class TSCBluetoothPrinter implements Runnable {
	private BluetoothInfo bluetoothInfo;
	private Context context;
	private String printString;
	private Logger logger = Logger.getLogger("TSCPrinter");
	private SharedPreferences mPreferences;
	
	public TSCBluetoothPrinter(Context context) {
		this.context = context;
	}

	public TSCBluetoothPrinter(Context context, String printString) {
		this.context = context;
		this.printString = printString;

		if (TPApplication.getInstance().printDebugMode) {
			return;
		}

		mPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		bluetoothInfo = getBluetoothConfigSetting();

		if (bluetoothInfo == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
			builder.setTitle("Printing Error");
			builder.setCancelable(true);
			builder.setMessage("Bluetooth Printer is not configured properly.");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			AlertDialog alert = builder.create();
			alert.show();
			return;
		}


		//new Thread(TSCBluetoothPrinter.this, "PrintingTask").start();
	}

	public BluetoothInfo getBluetoothConfigSetting() {
		BluetoothInfo bluetoothInfo = null;
		String deviceName = mPreferences.getString(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, null);

		if (deviceName != null) {
			bluetoothInfo = new BluetoothInfo(deviceName);
		} else {
			DeviceInfo device = TPApplication.getInstance().getDeviceInfo();
			if (device != null && device.getDefaultPrinterName() != null) {
				bluetoothInfo = new BluetoothInfo(device.getDefaultPrinterName());
			}
		}

		return bluetoothInfo;
	}
	
	public void print(boolean isAdvancePaper) {
		String MsgStr = "";
		BluetoothAdapter btAdapter = null;

		try {
			// Get BlueTooth Adapter
			btAdapter = BluetoothAdapter.getDefaultAdapter();
			if (btAdapter == null) {
				MsgStr = "Bluetooth not supported";
				mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));
				return;
			}

			// get the list of paired printer.
			Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

			// if there is any paired device, get out
			if (pairedDevices.size() == 0) {
				MsgStr = "Bluetooth not paired";
				mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));
				return;
			}

			// verify the current selected printer is in the list of the paired printer.
			BluetoothDevice selectedBTDevice = null;
			for (BluetoothDevice device : pairedDevices) {
				if (device.getName().compareTo(bluetoothInfo.getDeviceName()) == 0) {
					selectedBTDevice = device;
					break;
				}
			}

			// the current selected printer is not in the paired printer list
			if (selectedBTDevice == null) {
				MsgStr = bluetoothInfo.getDeviceName() + " Printer not paired in list";
				mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));
				return;
			}

			MsgStr = "Connecting to " + selectedBTDevice.getName() + "(" + selectedBTDevice.getAddress() + ")...";
			mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));

			print(printString, selectedBTDevice, isAdvancePaper);

			MsgStr = "Send data to printer";
			mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));

			Thread.sleep(200);

			MsgStr = "Done Printing";

		} catch (Exception e) {
			logger.error(TPUtility.getPrintStackTrace(e));
		}

		// display the last result to the UI here
		mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));
	}

	public void run() {
		print(false);
	}

	private void print(final String printData, final BluetoothDevice device,boolean isAdvancePaper) {
			String macAddress = "";
		try {
			/*TSCActivity TscDll = new TSCActivity();
			TscDll.openport(device.getAddress());*/
			macAddress = device.getAddress();
			AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
			builder.setTitle("TSC BLE");
			builder.setCancelable(true);
			builder.setMessage("TSCBluetooth Printer is configured properly.");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			AlertDialog alert = builder.create();
			alert.show();
/*
			TscDll.clearbuffer();
			/*Scanner scanner = new Scanner(printData);
			logger.error("after scanner execution");
			try{
			while (scanner.hasNextLine()) {

				logger.error("inside loop execution");
				String line = scanner.nextLine();
				if(line==null){
					logger.error("null line sometimes   ");
				}else if(line.equalsIgnoreCase("")){
					logger.error("empty line sometimes   ");
				}else
				TscDll.sendcommand(line);
				logger.error("command sent and passed");
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e);
				}
			}}catch (Exception e){
				e.printStackTrace();
				logger.error("error in command execution\n Trying another command");
			}
			try {
				logger.error("second command init");
				TscDll.sendcommand(TicketPROConstant.tscData);
				logger.error("second command passed");
			}catch (Exception e){
				e.printStackTrace();
				logger.error("error in second command execution\n closing port");
			}
			scanner.close();
			TscDll.closeport();*/
			final String macAdd = macAddress;
			logger.error("TSCBluetoothPrinter" + "macAddress"+macAddress);
			logger.debug("TSCBluetoothPrinter" + "macAddress"+macAddress);


			printtemplateWithListData(macAdd, printData,isAdvancePaper);
		} catch (Exception e) {
			logger.error("Print job failed " + TPUtility.getPrintStackTrace(e));
		}
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TicketPrinter.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					logger.error("TSCBluetoothPrinter" + "STATE_CONNECTED");
					break;

				case BluetoothChatService.STATE_CONNECTING:
					logger.error("TSCBluetoothPrinter" + "STATE_CONNECTING");
					break;

				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					break;
				}

				break;
			case TicketPrinter.MESSAGE_WRITE:
				logger.error("TSCBluetoothPrinter" + "MESSAGE_WRITE");
				break;

			case TicketPrinter.MESSAGE_READ:
				logger.error("TSCBluetoothPrinter" + "STATE_CONNECTING");
				break;

			case TicketPrinter.MESSAGE_DEVICE_NAME:
				logger.error("TSCBluetoothPrinter" + "STATE_CONNECTING");
				break;

			case TicketPrinter.MESSAGE_TOAST:
				final String message = (String) msg.obj;

				if (message != null && !message.isEmpty()) {
					try {
						Handler handler = new Handler(Looper.getMainLooper());
						handler.post(new Runnable() {
							public void run() {
								Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
								logger.error("TSCBluetoothPrinter" + message);
							}
						});
					}catch(Exception e) {
						e.printStackTrace();
					}
				}

				break;
			}
		}
	};

	private void printTextFromTSC(String macAddress){
		TSCActivity TscDll = null;
		try{
			TscDll = new TSCActivity();
			logger.error("Initiating printTextFromTSC with static mac");

			TscDll.openport("00:19:0E:A0:04:E1");
			TscDll.downloadpcx("UL.PCX");
			TscDll.downloadbmp("Triangle.bmp");
			TscDll.downloadttf("ARIAL.TTF");
			TscDll.setup(70, 110, 4, 4, 0, 0, 0);
			TscDll.clearbuffer();
				logger.error("Printer able to open port using static mac");
				TscDll.sendcommand("SET TEAR ON\n");
				TscDll.sendcommand("SET COUNTER @1 1\n");
				TscDll.sendcommand("@1 = \"0001\"\n");
				TscDll.sendcommand("TEXT 100,300,\"3\",0,1,1,@1\n");
				TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");
				TscDll.sendcommand("PUTBMP 100,520,\"Triangle.bmp\"\n");
				TscDll.sendcommand("TEXT 100,760,\"ARIAL.TTF\",0,15,15,\"THIS IS STATIC ARIAL FONT\"\n");
				TscDll.barcode(100, 100, "128", 100, 1, 0, 3, 3, "123456789");
				TscDll.printerfont(100, 250, "3", 0, 1, 1, "987654321");

			}catch (Exception e){
			e.printStackTrace();
			logger.error("failed in static mac");
			logger.error(TPUtility.getPrintStackTrace(e));
		}
		finally {
			TscDll.closeport();
			printTextWithMac(macAddress);
		}
	}

	private void printTextWithMac(String macAddress){
		try{
			TSCActivity TscDll = new TSCActivity();
			logger.error("Initiating printTextFromTSC with device mac");

			TscDll.openport(macAddress);
			TscDll.downloadpcx("UL.PCX");
			//TscDll.downloadbmp("Triangle.bmp");
			TscDll.downloadttf("ARIAL.TTF");
			TscDll.setup(70, 110, 4, 4, 0, 0, 0);
			TscDll.clearbuffer();
				logger.error("Printer able to open port using device mac "+macAddress);
				TscDll.sendcommand("SET TEAR ON\n");
				TscDll.sendcommand("SET COUNTER @1 1\n");
				TscDll.sendcommand("@1 = \"0001\"\n");
				TscDll.sendcommand("TEXT 100,300,\"3\",0,1,1,@1\n");
				TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");
				TscDll.sendcommand("TEXT Citation 88001122");
				TscDll.sendcommand("TEXT "+ TicketPROConstant.tscData);
				TscDll.barcode(100, 100, "128", 100, 1, 0, 3, 3, "123456789");
				TscDll.printerfont(100, 250, "3", 0, 1, 1, "987654321");
				TscDll.printlabel(2, 1);

			logger.error("Printer able to execute command using device mac \n"+System.currentTimeMillis());
				TscDll.closeport(5000);
			logger.error("After close port\n"+System.currentTimeMillis());
		}catch (Exception e){
			e.printStackTrace();
			logger.error("failed in device mac");

			logger.error(TPUtility.getPrintStackTrace(e));
		}
	}

	private void printtemplateWithListData(String macAddress,String templateData, boolean isAdvancePaper){
		try {
			TSCActivity tscDll = new TSCActivity();

			tscDll.openport(macAddress);

			tscDll.clearbuffer();

			try {
				List commands = new ArrayList<Object>();
				logger.error("preparing data sets for printing");
				if(isAdvancePaper){
					logger.error("advance paper printing");
					//commands = formatPrintAdvanceCommand(templateData);
					commands = formatPrintCommand(templateData);

				}else {
					logger.error("normal paper printing");
					 commands = formatPrintCommand(templateData);
				}
				for (Object cmd : commands) {
					if (cmd instanceof String)
						tscDll.sendcommand(cmd.toString());

					if (cmd instanceof byte[]) {
						tscDll.sendcommand((byte[]) cmd);
						logger.error("preparing data sets for printing"+cmd);
					}
					//Thread.sleep(50);
				}
				logger.error("preparing and executed loop to print");
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex.getMessage());
				logger.error("error in list data command");
			}
		}catch (Exception e){
			e.printStackTrace();
			logger.error("error in init List Data");
		}
	}

	/**
	 *
	 * The constant PRINT_CONTENT.
	 *
	 */
	public List<Object> formatPrintCommand(String templateData) {
		List<Object> commands = new ArrayList<Object>();
		try {
			String printData =  templateData.substring(templateData.indexOf("TEXT", templateData.indexOf("TEXT") + 1));
			String callibratingData = templateData.substring(0, templateData.indexOf("TEXT", templateData.indexOf("TEXT") + 1));;
			commands.addAll(Arrays.asList(
					//"CODEPAGE UTF-8\n", "SIZE 70 mm,120 mm\n", "SET PRINTKEY OFF\n", "DIRECTION 0\n", "CLS\n",


					//"TEXT 10,20,\"5.EFT\",0,1,1,\"",
					callibratingData,
					 String.format("%s", printData).getBytes("GB2312"),
					"\"\n"

					/*"TEXT 130,240,\"5.EFT\",0,1,1,\"",
					String.format("%s", TicketPROConstant.ticketDataToBePrinted).getBytes("GB2312"),
					"\"\n",


					"TEXT 15,1360,\"Font001\",0,2,2,\"",
					"Turn back paper to get details:400-862-9666".getBytes("GB2312"),
					"\"\n"*/

			));
			//commands.add("PRINT 1\n");
			commands.add("DELAY 10\n");
			//commands.add("PRINT 1\n");
		} catch (Exception ex) {
			ex.printStackTrace();


		}
		logger.error("TSCBluetoothPrinter" + commands);
		return commands;
	}


	/**
	 *
	 * The constant PRINT_CONTENT.
	 *
	 */
	public static List<Object> formatPrintAdvanceCommand(String templateData) {
		List<Object> commands = new ArrayList<Object>();
		try {
			commands.addAll(Arrays.asList(
					"CODEPAGE UTF-8\n",
					"SIZE 70 mm,40 mm\n",
					 "SET PRINTKEY OFF\n",
					"DIRECTION 0\n",
					"CLS\n",


					"TEXT 10,10,\"5.EFT\",0,1,1,\"",
					String.format("%s", templateData).getBytes("GB2312"),
					"\"\n"

					/*"TEXT 130,240,\"5.EFT\",0,1,1,\"",
					String.format("%s", TicketPROConstant.ticketDataToBePrinted).getBytes("GB2312"),
					"\"\n",


					"TEXT 15,1360,\"Font001\",0,2,2,\"",
					"Turn back paper to get details:400-862-9666".getBytes("GB2312"),
					"\"\n"*/

			));
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return commands;
	}
}