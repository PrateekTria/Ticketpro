package com.ticketpro.print;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ticketpro.parking.R;

import java.util.Set;

public class BluetoothConfig extends Activity{
	
	Spinner pairedBTSpinner;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetoothconfig);
		pairedBTSpinner = findViewById(R.id.paired_printer_spinner);
		
		//populate paired devices
		PopulateBondedDevice();
		
		// Select the current pair device.
		Intent currIntent = getIntent();
		String DeviceName = currIntent.getStringExtra(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY);
		String SelectedDeviceName;
		for (int nIndex= 0; nIndex<pairedBTSpinner.getCount(); nIndex++){
			SelectedDeviceName = pairedBTSpinner.getItemAtPosition(nIndex).toString();
			if (DeviceName!=null && SelectedDeviceName.compareTo(DeviceName)==0){
				pairedBTSpinner.setSelection(nIndex);
				break;
			}
		}
		
        
		// handler for the save button click
		Button SavetButton = this.findViewById(R.id.save_button);
		SavetButton.setOnClickListener(v -> {
			// create new intent
			Intent intent = new Intent();

			if(pairedBTSpinner.getCount() > 0){
				// gather the updated data
				intent.putExtra(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, pairedBTSpinner.getSelectedItem().toString());
			}

			// return the value back to the caller
			setResult(RESULT_OK, intent);

			// close this Activity
			finish();
		});

		
		// handler for the cancel button click
		Button CancelButton = this.findViewById(R.id.cancel_button);
		CancelButton.setOnClickListener(v -> {
			setResult(RESULT_CANCELED, null);

			finish();
		});
	}
	
	// --------------------------------------------------------------------------
	// populate list of available files to be selected for printing for the
	// spinner
	// ---------------------------------------------------------------------------
	private void PopulateBondedDevice() 
	{
		// Get the local BlueTooth adapter
		BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		//get a list of device name.
		 if (pairedDevices.size() > 0) 
		 {
			  String[] listOfDeviceNames = new String[pairedDevices.size()];
			  int nIndex = 0;
			  for( BluetoothDevice device : pairedDevices)
			  //for (int nIndex =0; nIndex <pairedDevices.size(); nIndex++)
			  {
				  listOfDeviceNames[nIndex++]= device.getName();
			  }
			  
		  	// create an adapter to bind data from the list of the file to the  spinner
			ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfDeviceNames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			pairedBTSpinner.setAdapter(adapter);
		 }
	
	}

}
