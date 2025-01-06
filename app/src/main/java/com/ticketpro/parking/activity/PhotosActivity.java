package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketPictureTemp;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.PhotosBLProcessor;
import com.ticketpro.util.CSVUtility;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.Preference;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.File;
import java.util.ArrayList;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class PhotosActivity extends BaseActivityImpl {

    final int REQUEST_TAKE_PICTURE = 1;
    final int REQUEST_VIEW_PICTURE = 2;
    final int REQUEST_TAKE_NEW_PICTURE = 3;
    private LinearLayout photosView;
    private Ticket activeTicket;
    private boolean editTicketPictures;
    private CheckBox chk_sticky_photo;
    private Preference preference;
    private Button takeNewPicture;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.photos);
            preference = Preference.getInstance(PhotosActivity.this);
            setLogger(PhotosActivity.class.getName());
            photosView = (LinearLayout) findViewById(R.id.photos_list);
            takeNewPicture = findViewById(R.id.takeNewPicture);

            setBLProcessor(new PhotosBLProcessor());
            setActiveScreen(this);
            isNetworkInfoRequired = true;

            editTicketPictures = getIntent().getBooleanExtra("EditTicketPictures", false);
            if (getIntent().hasExtra("SharedTicket")) {
                activeTicket = TPApp.getSharedTicket();
            } else {
                activeTicket = TPApp.getActiveTicket();
            }

            if (activeTicket == null) {
                return;
            }

            TextView citationTextView = (TextView) findViewById(R.id.photos_citation_number_textview);
            citationTextView.setText("#" + TPUtility.prefixZeros(activeTicket.getCitationNumber(), 8));

            chk_sticky_photo = (CheckBox) findViewById(R.id.chk_sticky_photo);
            chk_sticky_photo.setChecked(TPApplication.getInstance().stickyPhoto);
            chk_sticky_photo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    TPApplication.getInstance().stickyPhoto = isChecked;

                    preference.putBoolean(TPConstant.PREFS_KEY_STICKY_PHOTO, TPApp.stickyPhoto);
                    try {
                        if (!isChecked) {
                            //TPApp.setStickyPhotos(null);
                            TPApp.setLastPhotos(null);

                        } else {
                            //TPApp.setStickyPhotos(activeTicket.getTicketPictures().get(0));
                            TPApp.setLastPhotos(activeTicket.getTicketPictures());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if (activeTicket.getTicketPictures().size() == 0) {
                takeNewPicture(null);
            } else {
                displayPictures();
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {

    }

    public void displayPictures() {
        try {
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            int index = 0;

            for (final TicketPicture picture : activeTicket.getTicketPictures()) {
                LinearLayout rowView = (LinearLayout) layoutInflater.inflate(R.layout.photo_row_view, null, false);
                rowView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                CheckBox printChk = (CheckBox) rowView.findViewById(R.id.photo_row_print_chk);
                CheckBox sp = (CheckBox) rowView.findViewById(R.id.photo_sp);
                if (!Feature.isFeatureAllowed(Feature.PARK_STICKY_PHOTO)){
                    sp.setVisibility(View.GONE);
                }

                printChk.setChecked(picture.getMarkPrint().equals("Y"));

                printChk.setTag(R.id.pictureIndex, index);
                printChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int pictureIndex = (Integer) buttonView.getTag(R.id.pictureIndex);
                        if (isChecked) {
                            for (int i = 0; i < activeTicket.getTicketPictures().size(); i++) {
                                TicketPicture pic = activeTicket.getTicketPictures().get(i);
                                if (pictureIndex == i) {
                                    pic.setMarkPrint("Y");
                                } else {
                                    pic.setMarkPrint("N");
                                }
                            }

                            photosView.removeAllViews();
                            PhotosActivity.this.displayPictures();
                        }
                    }
                });

                sp.setChecked(picture.isPhotoSp());
                sp.setTag(R.id.pictureIndex, index);
                sp.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    int pictureIndex = (Integer) buttonView.getTag(R.id.pictureIndex);
                    if (isChecked) {
                        TicketPicture ticketPicture = activeTicket.getTicketPictures().get(pictureIndex);
                        ticketPicture.setPhotoSp(true);
                        //TPApp.getLastPhotos().add(ticketPicture);
                        preference.putBoolean(TPConstant.PREFS_KEY_STICKY_PHOTO, TPApp.stickyPhoto);
                        TPApplication.getInstance().stickyPhoto = isChecked;

                    }else {
                        if (TPApp.getLastPhotos().size()>0) {
                            TicketPicture ticketPicture = activeTicket.getTicketPictures().get(pictureIndex);
                            ticketPicture.setPhotoSp(false);
                            preference.putBoolean(TPConstant.PREFS_KEY_STICKY_PHOTO, false);
                            TPApplication.getInstance().stickyPhoto = false;
                            activeTicket.getTicketPictures().remove(pictureIndex);
                            photosView.removeAllViews();
                            displayPictures();
                        }

                    }
                });

                ImageView imgView = (ImageView) rowView.findViewById(R.id.photo_row_view_image);
                try {
                  //  File previewImg = new File(picture.getImagePath());
                    File previewImg = new File(activeTicket.getTicketPictures().get(index).getImagePath());
                    if (previewImg.exists()) {
                        sp.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(picture.getImagePath());
                        imgView.setImageBitmap(bitmap);
                    }

                    if (picture.getDownloadImageUrl() != null || !picture.getDownloadImageUrl().isEmpty()) {
                        sp.setVisibility(View.GONE);

                        Glide.with(PhotosActivity.this)
                                .load(picture.getDownloadImageUrl())
                                .into(imgView);
                        //Picasso.get().load(picture.getDownloadImageUrl()).into(imgView);
                    }
                } catch (Exception e) {
                    //log.error("Error loading preview image." + e.getMessage());
                }

                imgView.setTag(R.id.pictureIndex, index);
                imgView.setTag(R.id.pictureId, picture.getPictureId());
                imgView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pictureIndex = ((Integer) v.getTag(R.id.pictureIndex)).intValue();
                        int pictureId = ((Integer) v.getTag(R.id.pictureId)).intValue();

                        Intent i = new Intent();
                        i.setClass(PhotosActivity.this, ViewPictureActivity.class);
                        i.putExtra("PictureIndex", pictureIndex);
                        i.putExtra("PictureId", pictureId);
                        i.putExtra("SharedTicket", editTicketPictures);
                        i.putExtra("ImageUrl", picture.getDownloadImageUrl());
                        i.putExtra("", "");

                        startActivityForResult(i, REQUEST_VIEW_PICTURE);
                    }
                });
                String imagePath = "";
                try {
                    imagePath = picture.getImagePath().substring(picture.getImagePath().lastIndexOf('/') + 1).trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TextView dateText = (TextView) rowView.findViewById(R.id.photo_row_date_textview);
                TextView name = (TextView) rowView.findViewById(R.id.photo_name);
		       /* if(TPConstant.IS_DEVELOPMENT_BUILD) {
					dateText.setText(DateUtil.getSQLStringFromDate(picture.getPictureDate()) + "\n" + imagePath);
				}else {
					dateText.setText(DateUtil.getSQLStringFromDate(picture.getPictureDate()));
				}*/

                dateText.setText(DateUtil.getSQLStringFromDate(picture.getPictureDate()));

                if (picture.getLprImageName() != null && !TextUtils.isEmpty(picture.getLprImageName())) {
                    name.setVisibility(View.VISIBLE);
                }
                Button retakePicture = (Button) rowView.findViewById(R.id.photo_row_retake_btn);
                retakePicture.setTag(R.id.pictureIndex, index);
                retakePicture.setTag(R.id.pictureId, picture.getPictureId());
                retakePicture.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pictureIndex = ((Integer) v.getTag(R.id.pictureIndex)).intValue();
                        int pictureId = ((Integer) v.getTag(R.id.pictureId)).intValue();
                        String photoNumber = "";
                        try {
                            photoNumber = activeTicket.getTicketPictures().get(pictureIndex).getImagePath();
                            photoNumber = photoNumber.substring(activeTicket.getTicketPictures().get(pictureIndex).getImagePath().lastIndexOf('/') + 1).trim();
                            if (photoNumber.contains("LPR")) {
                                photoNumber = photoNumber.substring(photoNumber.indexOf("R-") + 2, photoNumber.indexOf(".JPG"));
                                Toast.makeText(getApplicationContext(), "LPR Picture Can not be edited", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                photoNumber = photoNumber.substring(photoNumber.indexOf("T-") + 2, photoNumber.indexOf(".JPG"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Intent i = new Intent();
                        i.setClass(PhotosActivity.this, TakePictureActivity.class);
                        i.putExtra("PictureIndex", pictureIndex);
                        i.putExtra("PictureId", pictureId);
                        i.putExtra(TPConstant.INTENT_RECAPTURE_CONSTANT, true);
                        i.putExtra("photoNumber", photoNumber);
                        i.putExtra("CitationNumber", activeTicket.getCitationNumber());

                        startActivityForResult(i, REQUEST_TAKE_PICTURE);
                    }
                });

                Button deletePicture = (Button) rowView.findViewById(R.id.photo_row_delete_btn);
                deletePicture.setTag(R.id.pictureIndex, index);
                deletePicture.setTag(R.id.pictureId, picture.getPictureId());

                deletePicture.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int pictureIndex = ((Integer) v.getTag(R.id.pictureIndex)).intValue();

                        AlertDialog.Builder builder = new AlertDialog.Builder(PhotosActivity.this);
                        builder.setTitle("Delete Confirmation")
                                .setMessage("Are you sure you want to delete this picture?")
                                .setCancelable(true)
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (editTicketPictures) {
                                            try {
                                                TicketPicture picture = activeTicket.getTicketPictures().get(pictureIndex);
                                                picture.setIsEdit("D");
                                                if (picture != null) {
                                                    if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                                                        isServiceAvailable = false;
                                                    }else {
                                                        isServiceAvailable = true;
                                                    }
                                                    if (isServiceAvailable) {
                                                        ((PhotosBLProcessor) screenBLProcessor).deleteTicketPicture(picture.getCitationNumber(), picture.getImagePath());
                                                    }
                                                    ArrayList<TicketPicture> tp = new ArrayList<>();
                                                    tp.add(picture);
                                                    CSVUtility.writePictureCSV(tp);
                                                    TPUtility.removeFile(picture.getImagePath());
                                                    TicketPicture.removePictureById(picture.getS_no());
                                                    ArrayList<TicketPicture> ticketPictures = TicketPicture.getTicketPicturesByCitation(activeTicket.getCitationNumber());

                                                    activeTicket.setTicketPictures(ticketPictures);

                                                }
                                            } catch (Exception e) {
                                                log.error(TPUtility.getPrintStackTrace(e));
                                            }
                                        } else {

                                            //Reset the LPR image flag
                                            TicketPicture picture = activeTicket.getTicketPictures().get(pictureIndex);
                                            if (picture.getImagePath() != null && picture.getImagePath().contains("LPR")) {
                                                Toast.makeText(getApplicationContext(), "LPR Picture Can't be deleted", Toast.LENGTH_SHORT).show();
                                                return;
                                                //activeTicket.setIsLPR("N");
                                            }

                                            if (picture.isPhotoSp()){
                                                TicketPicture ticketPicture = activeTicket.getTicketPictures().get(pictureIndex);
                                                ticketPicture.setPhotoSp(false);
                                                preference.putBoolean(TPConstant.PREFS_KEY_STICKY_PHOTO, false);
                                                TPApplication.getInstance().stickyPhoto = false;
                                                activeTicket.getTicketPictures().remove(pictureIndex);
                                                try {
                                                    TicketPictureTemp.removePictureById(picture.getS_no());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }else{
                                                TPUtility.removeFile(picture.getImagePath());
                                                activeTicket.getTicketPictures().remove(pictureIndex);
                                                try {
                                                    TicketPictureTemp.removePictureById(picture.getS_no());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        }

                                        photosView.removeAllViews();
                                        displayPictures();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });
                if (imagePath != null) {
                    if (imagePath.contains("LPR") || imagePath.contains("CHALK-LPR-") || imagePath.contains("CHALK")) {
                        retakePicture.setVisibility(View.INVISIBLE);
                        deletePicture.setVisibility(View.INVISIBLE);
                    } else {
                        if (Feature.isFeatureAllowed(Feature.PARK_STICKY_PHOTO)){
                            if (TPApp.getLastPhotos().size()>0){
                                retakePicture.setVisibility(View.GONE);
                                deletePicture.setVisibility(View.GONE);
                            }else {
                                retakePicture.setVisibility(View.VISIBLE);
                                deletePicture.setVisibility(View.VISIBLE);
                            }
                        }else {
                            retakePicture.setVisibility(View.VISIBLE);
                            deletePicture.setVisibility(View.VISIBLE);
                        }
                    }
                }
                photosView.addView(rowView);
                LinearLayout spacer = new LinearLayout(this);
                spacer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3));
                spacer.setBackgroundColor(Color.WHITE);
                photosView.addView(spacer);

                index++;
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_TAKE_PICTURE: {
                        String imagePath = data.getStringExtra("PicturePath");
                        int pictureIndex = data.getIntExtra("PictureIndex", 0);
                        if (imagePath != null) {
                            activeTicket.getTicketPictures().get(pictureIndex).setImagePath(imagePath);
                            photosView.removeAllViews();
                            displayPictures();
                        }
                        break;
                    }

                    case REQUEST_TAKE_NEW_PICTURE: {
                        String imagePath = data.getStringExtra("PicturePath");
                        if (imagePath != null) {
                            if (editTicketPictures) {
                                ArrayList<TicketPicture> ticketPictures = TicketPicture.getTicketPicturesByCitation(activeTicket.getCitationNumber());
                                activeTicket.setTicketPictures(ticketPictures);
                            }

                            photosView.removeAllViews();
                            displayPictures();
                        }

                        break;
                    }

                    case REQUEST_VIEW_PICTURE: {

                        photosView.removeAllViews();
                        displayPictures();
                        break;
                    }

                }
            }

            if (activeTicket.getTicketPictures().size() == 0) {
                backAction(null);
            }


        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        takeNewPicture.setClickable(true);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        if (getParent() == null) {
            setResult(RESULT_OK);
        } else {
            getParent().setResult(RESULT_OK);
        }

        finish();
    }

    public void backAction(View view) {
        if (getParent() == null) {
            setResult(RESULT_OK);
        } else {
            getParent().setResult(RESULT_OK);
        }

        finish();
    }

    public void takeNewPicture(View view) {
        try {
            int maxPhotos = 0;
            if (Feature.isFeatureAllowed(Feature.MAX_PHOTOS)) {
                String value = Feature.getFeatureValue(Feature.MAX_PHOTOS);
                try {

                    maxPhotos = Integer.parseInt(value);
                    if (activeTicket.getPhoto_count() > 0) {
                        maxPhotos = maxPhotos + activeTicket.getPhoto_count();
                    }
                    if (activeTicket.isLPR()) {
                        maxPhotos = maxPhotos + 1;
                    }
                } catch (Exception e) {
//					e.printStackTrace();
                }
            }

            if (maxPhotos > 0 && activeTicket.getTicketPictures().size() >= maxPhotos) {
                displayErrorMessage("Exceeded max photos limit.");
                return;
            }

            Intent i = new Intent();
            i.setClass(PhotosActivity.this, TakePictureActivity.class);
            i.putExtra("CitationNumber", activeTicket.getCitationNumber());
            i.putExtra("EditTicketPictures", editTicketPictures);
            i.putExtra("ticketID", activeTicket.getTicketId());
            startActivityForResult(i, REQUEST_TAKE_NEW_PICTURE);

            takeNewPicture.setClickable(false);

            return;

        } catch (Exception e) {
            //log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public void handleVoiceInput(String text) {
        if (text == null) return;

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")) {
            backAction(null);
        } else if (text.contains("NEW PICTURE") || text.contains("NEW PHOTO") || text.contains("TAKE PICTURE")) {
            takeNewPicture(null);
        }

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stub

    }
}
