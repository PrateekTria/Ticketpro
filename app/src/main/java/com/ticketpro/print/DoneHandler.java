package com.ticketpro.print;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;

import com.twotechnologies.n5library.printer.Fonts;
import com.twotechnologies.n5library.printer.PrtEOJListener;
import com.twotechnologies.n5library.printer.PrtFormatting;
import com.twotechnologies.n5library.printer.PrtTextStream;

public class DoneHandler extends PrtEOJListener {
	private boolean printMarker = false;

	public void setPrintMarker() {
		printMarker = true;
	}

	public DoneHandler(Context context) {
		super(context);
	}

	/** onReceive handler - prints marker */
	@Override
	public void onReceive(Context context, Intent intent) {
		// abort if not expected
		if (!printMarker) {
			return;
		}

		// clear marker print
		printMarker = false;

		// stop listening
		stopListening();

		// load info
		createEndMarker(intent);
	}

	private void createEndMarker(Intent intent) {
		try {
			// set font
			PrtFormatting.setFont(Fonts.COURIER_16_9_CPI);

			// add thick line
			N5TicketPrinter.addGraphicLine(4);

			// new line
			PrtTextStream.formfeed();

			// flush buffers
			PrtTextStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}