package com.ticketpro.print;

import com.ticketpro.parking.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class TCPIPConfig extends Activity {
	public int result_code = RESULT_CANCELED;
	
	EditText IPEditText;
	EditText TCPIPPortEditText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tcpipconfig);

		// populate data into edit text field in the form
		Intent currIntent = getIntent();
		String PrinterIPAddress = currIntent.getStringExtra(TicketPrinter.PRINTER_IPADDRESS_KEY);
		int PrinterTCPIPPort = currIntent.getIntExtra(TicketPrinter.PRINTER_TCPIPPORT_KEY, 5000);
		
		IPEditText = (EditText) findViewById(R.id.ip_address_edittext);
		IPEditText.setText(PrinterIPAddress);
		
		TCPIPPortEditText = (EditText) findViewById(R.id.ip_port_edittext);
		TCPIPPortEditText.setText(String.valueOf(PrinterTCPIPPort));

		// handler for the save button click
		Button SavetButton = (Button) this.findViewById(R.id.save_button);
		SavetButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// create new intent
				Intent intent = new Intent();

				// gather the updated data
				intent.putExtra(TicketPrinter.PRINTER_IPADDRESS_KEY,IPEditText.getText().toString());
				String TCPIPPort = TCPIPPortEditText.getText().toString();
				intent.putExtra(TicketPrinter.PRINTER_TCPIPPORT_KEY,Integer.valueOf(TCPIPPort));
				
				// return the value back to the caller
				setResult(RESULT_OK, intent);
				
				// close this Activity
				finish();
			}
		});

		// handler for the cancel button click
		Button CancelButton = (Button) this.findViewById(R.id.cancel_button);
		CancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED, null);
				
				finish();
			}
		});

	}

}
