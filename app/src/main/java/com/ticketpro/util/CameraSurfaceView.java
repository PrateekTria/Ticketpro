package com.ticketpro.util;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	private SurfaceHolder holder;
	private Camera camera;
	
	public CameraSurfaceView(Context context){
		super(context);
		this.holder = this.getHolder();
		this.holder.addCallback(this);
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
	        this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		try{
			this.camera = Camera.open();
			this.camera.setPreviewDisplay(this.holder);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(width, height);
		camera.setParameters(parameters);
		camera.startPreview();
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		camera.stopPreview();
		camera.release();
		camera = null;
	}
	
	public Camera getCamera(){
		return this.camera;
	}
}
