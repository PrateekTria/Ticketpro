package com.ticketpro.parking.activity;

import android.app.Activity;
import android.os.Bundle;

public class DummyActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setResult(RESULT_OK);
	    finish();
	}

}
