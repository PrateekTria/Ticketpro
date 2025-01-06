package com.ticketpro.parking.service;

import com.ticketpro.model.GPSLocation;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;

public class GPSResultReceiver extends ResultReceiver implements Parcelable{
  private Receiver receiver;

  public GPSResultReceiver(Handler handler) {
      super(handler);
  }

  public void setReceiver(Receiver receiver) {
      this.receiver = receiver;
  }

  public interface Receiver {
      public void onReceiveResult(Location location, Bundle resultData);
      public void onReceiveResult(GPSLocation location, Bundle resultData);
      public void onTimeout();
  }

  @Override
  protected void onReceiveResult(int resultCode, Bundle resultData) {
      if (receiver != null) {
    	  Location location=resultData.getParcelable("Location");
    	  receiver.onReceiveResult(location, resultData);
      }
  }
  
  protected void onReceiveResult(Location location, Bundle resultData) {
      if (receiver != null) {
    	  receiver.onReceiveResult(location, resultData);
      }
  }
  
  protected void onReceiveResult(GPSLocation location, Bundle resultData) {
      if (receiver != null) {
    	  receiver.onReceiveResult(location, resultData);
      }
  }
  
  protected void onTimeout() {
      if (receiver != null) {
    	  receiver.onTimeout();
      }
  }
}