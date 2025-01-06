package com.ticketpro.parking.bl;

import java.util.ArrayList;

import android.content.Context;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.TicketPicture;


public class PhotosBLProcessor extends BLProcessorImpl{
	
	public PhotosBLProcessor(){
		setLogger(PhotosBLProcessor.class.getName());
	}
	
	public boolean deleteTicketPicture(long citationNumber, String imageName){
		
		//Parse Image Name
		if(imageName!=null){
			String[] tokens=imageName.split("/");
			if(tokens.length > 0)
				imageName=tokens[tokens.length-1];
		}
		
		return proxy.deleteTicketPicture(citationNumber, imageName);
	}
	
	
	public boolean updateTicketPicture(long citationNumber, final TicketPicture picture){
		String imageOriginalPath = null;
		if (picture==null)
			return false;

		if (picture.getImagePath()!=null){
			imageOriginalPath = picture.getImagePath();
		}
		String finalImageOriginalPath = imageOriginalPath;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (picture!=null && picture.getImagePath()!=null) {
						ArrayList<String> uploadImages = new ArrayList<String>();
						uploadImages.add(finalImageOriginalPath);
						proxy.uploadImages(citationNumber, picture.getS_no(), uploadImages);
					}
					
				} catch (TPException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		//return proxy.updateTicketPicture(citationNumber, picture);
		String[] path = picture.getImagePath().split("/");
		picture.setImagePath(path[path.length - 1]);
		return proxy.updateTicketPicture1(citationNumber, picture);
	}
	
	public boolean updateSelfiPicture(long citationNumber, final TicketPicture picture, final Context ctx){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ArrayList<String> uploadImages=new ArrayList<String>();
					uploadImages.add(picture.getImagePath()); 
					proxy.uploadSelfi(uploadImages, ctx); 
					
				} catch (TPException e) {}
			}
		}).start();
		return true;
	}

}
