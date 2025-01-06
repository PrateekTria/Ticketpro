package com.ticketpro.parking.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.ticketpro.model.Feature;
import com.ticketpro.model.SpecialActivityPicture;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.PhotosBLProcessor;
import com.ticketpro.util.TPUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpecialActivityPhotoView extends BaseActivityImpl {
    final int REQUEST_TAKE_NEW_PICTURE = 3;
    final int REQUEST_TAKE_PICTURE = 1;
    final int REQUEST_VIEW_PICTURE = 2;
    private SpecialActivityPicture activeChalk;
    private boolean editTicketPictures;
    private LinearLayout photosView;
    private String CIT;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_photo_view);

        setLogger(PhotoChalkEditActivity.class.getName());
        this.photosView = (LinearLayout) findViewById(R.id.photos_list);

        mListView = (ListView) findViewById(R.id.listView);
        setBLProcessor(new PhotosBLProcessor());
        setActiveScreen(this);
        displayListImage();
    }

    public void takePictureAction(View view) {

        int maxPhotos = 0;
        if (Feature.isFeatureAllowed(Feature.MAX_PHOTOS)) {
            try {
                maxPhotos = Integer.parseInt(Feature.getFeatureValue(Feature.MAX_PHOTOS));
            } catch (Exception e) {
            }
        }
        if (maxPhotos > 0) {
            if (SpecialActivityPicture.getListOfImage(SpecialActivityReport.getLastInsertId() + 1).size() >= maxPhotos) {
                displayErrorMessage("Exceeded max photos limit.");
                return;
            }
        }
        Intent i = new Intent();
        i.setClass(this, TakePictureActivity.class);
        i.putExtra("NewSpecialPicture", true);
        i.putExtra("ReportId", SpecialActivityReport.getLastInsertId() + 1);

        startActivityForResult(i, REQUEST_TAKE_PICTURE);

       /* Intent i = new Intent();
        i.setClass(this, TakePictureActivity.class);
        i.putExtra("NewSpecialPicture", true);
        i.putExtra("isPhotoChalkEditScreen", true);
        startActivityForResult(i, REQUEST_TAKE_PICTURE);*/

        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_TAKE_PICTURE: {
                        String imagePath = data.getStringExtra("PicturePath");
                        if (imagePath != null) {
                            //displayPictures();
                            displayListImage();
                        }

                        break;
                    }

                    case REQUEST_TAKE_NEW_PICTURE: {
                        String imagePath = data.getStringExtra("PicturePath");
                        if (imagePath != null) {
                            //displayPictures();
                            displayListImage();
                        }

                        break;
                    }

                    case REQUEST_VIEW_PICTURE: {
                        //displayPictures();
                        displayListImage();
                        break;
                    }

                }
            }
        } catch (Exception e) {
            Log.e("Photos", TPUtility.getPrintStackTrace(e));
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void displayListImage() {

        ArrayList<SpecialActivityPicture> chalkPictures = SpecialActivityPicture.getListOfImage(SpecialActivityReport.getLastInsertId() + 1);

        for (int i = 0; i < chalkPictures.size(); i++) {

            if (chalkPictures.get(i).getImagePath().contains("SIGN-")) {
                chalkPictures.remove(i);
            }

        }
        mListView.setAdapter(new ImageAdapter(chalkPictures, SpecialActivityPhotoView.this));
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    public void bindDataAtLoadingTime() throws Exception {

    }

    @Override
    public void handleVoiceInput(String text) {

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

    }

    private class ImageAdapter extends BaseAdapter {
        private List<SpecialActivityPicture> listData;
        private LayoutInflater layoutInflater;
        private Context context;
        private String ImgName;

        public ImageAdapter(List<SpecialActivityPicture> listData, Context context) {
            this.listData = listData;
            this.context = context;

            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.photo_row_view, null);
                holder = new ViewHolder();
                holder.siteImage = (ImageView) convertView.findViewById(R.id.photo_row_view_image);
                holder.dateText = (TextView) convertView.findViewById(R.id.photo_row_date_textview);
                holder.reTake = (Button) convertView.findViewById(R.id.photo_row_retake_btn);
                holder.delete = (Button) convertView.findViewById(R.id.photo_row_delete_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.dateText.setVisibility(View.INVISIBLE);

            final SpecialActivityPicture adminTicketPicture = this.listData.get(position);
            /*if (adminTicketPicture.getImagePath().contains("SIGN-")){
				listData.remove(position);
			}*/

            File previewImg = new File(adminTicketPicture.getImagePath());
            if (previewImg.exists()) {
                Picasso.get()
                        .load(previewImg)
                        .resize(120, 120)
                        .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(holder.siteImage);


            }

            //holder.dateText.setText(adminTicketPicture.getImgName());
            holder.reTake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setClass(SpecialActivityPhotoView.this, TakePictureActivity.class);
                    i.putExtra("PictureIndex", position);
                    i.putExtra("PictureId", adminTicketPicture.getPictureId());
                    i.putExtra("PictureName", adminTicketPicture.getImageName());
                    i.putExtra("EditPictureSPA", true);
                    i.putExtra("ReportId", SpecialActivityReport.getLastInsertId() + 1);

                    startActivityForResult(i, REQUEST_TAKE_PICTURE);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SpecialActivityPhotoView.this);
                    builder.setTitle("Delete Confirmation").setMessage("Are you sure ? You want to delete this picture.").setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        File previewImg = new File(adminTicketPicture.getImagePath());
                                        ImgName = previewImg.getName();
                                        SpecialActivityPicture.removeSPAPictureById(adminTicketPicture.getPictureId());
                                        TPUtility.removeImage(adminTicketPicture.getImagePath());
                                        listData.remove(position);
                                        displayListImage();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            holder.siteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setClass(SpecialActivityPhotoView.this, ViewPictureActivity.class);
                    i.putExtra("PictureIndex", position);
                    i.putExtra("ChalkId", "");
                    i.putExtra("isChalkPicturePrev", true);
                    i.putExtra("ImageUrl", adminTicketPicture.getImagePath());

                    startActivityForResult(i, REQUEST_VIEW_PICTURE);
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView siteImage;
            TextView dateText;
            Button delete;
            Button reTake;
        }
    }
}