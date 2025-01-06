package com.ticketpro.print;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ticketpro.model.DeviceInfo;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.print.model.BluetoothInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;

public class BluetoothPrinter implements Runnable {

	private BluetoothInfo bluetoothInfo;
	private final Context context;
	private final String printString;

	// use to update the UI information.
	private final Handler handler = new Handler();
	private SharedPreferences mPreferences;

	public BluetoothPrinter(Context context, String printString) {
		this.context = context;
		this.printString = printString;

		if (TPApplication.getInstance().printDebugMode) {
			return;
		}

		mPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		bluetoothInfo = GetBluetoothConfigSetting();
		if (bluetoothInfo == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
			builder.setTitle("Printing Error");
			builder.setCancelable(true);
			builder.setMessage("Bluetooth Printer is not configured properly.");
			builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

			AlertDialog alert = builder.create();
			alert.show();
			return;
		}
		new Thread(BluetoothPrinter.this, "PrintingTask").start();
	}

	BluetoothInfo GetBluetoothConfigSetting() {
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

	public void BluetoothPrint() {
		BluetoothAdapter btAdapter;
		String MsgStr = "";
		InputStream inStream = null;
		BluetoothChatService BTService = null;
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

			// create a BT Service object
			BTService = new BluetoothChatService(this.context, mHandler);

			// Start the BT Service
			BTService.start();

			// connecting to device - generate Bluetooth Socket, connecting to the device,
			// and get the output stream for the socket.
			MsgStr = "connecting to " + selectedBTDevice.getName() + "(" + selectedBTDevice.getAddress() + ")...";
			// mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST,
			// MsgStr));

			BTService.connect(selectedBTDevice);

			// wait for the connection has established
			// Check that we're actually connected before trying anything
			int nWaitTime = 8;
			while (BTService.getState() != BluetoothChatService.STATE_CONNECTED) {
				Thread.sleep(1000);
				nWaitTime--;
				if (nWaitTime == 0) {
					// DisplayPrintingStatusMessage("Unable to connect!");
					throw (new Throwable("Unable to connect to " + selectedBTDevice.getName() + "!"));
				}
			}

			if (this.printString == null)
				return;

			MsgStr = "Send data to printer";
			// mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST,
			// MsgStr));

			BTService.write(this.printString.getBytes());
			Thread.sleep(2000);

			MsgStr = "Done Printing";
		} catch (SocketException e) {
			// MsgStr = e.getMessage();
			Log.e(TicketPrinter.LOGTAG, e.getMessage(), e);
		} catch (UnknownHostException e) {
			// MsgStr = e.getMessage();
			Log.e(TicketPrinter.LOGTAG, e.getMessage(), e);
		} catch (IOException e) {
			// MsgStr = e.getMessage();
			Log.e(TicketPrinter.LOGTAG, e.getMessage(), e);
		} catch (Throwable e) {
			// MsgStr = e.getMessage();
			Log.e(TicketPrinter.LOGTAG, e.getMessage(), e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
					inStream = null;
				} catch (IOException e) {
				}
			}

			if (BTService != null)
				BTService.stop();
		}

		// display the last result to the UI here
		mHandler.handleMessage(mHandler.obtainMessage(TicketPrinter.MESSAGE_TOAST, MsgStr));
	}// BluetoothPrint()

	// -----------------------------------------------------------------
	// Printing a selected file using a selected communication method.
	// ------------------------------------------------------------------
	// @Override
	public void run() {
		BluetoothPrint();
	}// run()

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case TicketPrinter.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					// DisplayPrintingStatusMessage("Connected");
					break;

				case BluetoothChatService.STATE_CONNECTING:
					// DisplayPrintingStatusMessage("Connecting...");
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
				// Toast.makeText(context, "Connected to "+ mConnectedBTDeviceName,
				// Toast.LENGTH_SHORT).show();
				break;

			case TicketPrinter.MESSAGE_TOAST:
				final String message = (String) msg.obj;
				if (message != null && !message.isEmpty()) {
					try {
						Handler handler = new Handler(Looper.getMainLooper());
						handler.post(new Runnable() {
							public void run() {
								Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				break;
			}
		}
	};

}