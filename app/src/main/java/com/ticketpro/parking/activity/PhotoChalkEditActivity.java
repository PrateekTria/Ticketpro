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
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Feature;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.PhotosBLProcessor;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoChalkEditActivity extends BaseActivityImpl {
    final int REQUEST_TAKE_NEW_PICTURE = 3;
    final int REQUEST_TAKE_PICTURE = 1;
    final int REQUEST_VIEW_PICTURE = 2;
    private ChalkVehicle activeChalk;
    private boolean editTicketPictures;
    private LinearLayout photosView;
    private String CIT;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_chalk);

        setLogger(PhotoChalkEditActivity.class.getName());
        this.photosView = (LinearLayout) findViewById(R.id.photos_list);

        mListView = (ListView) findViewById(R.id.listView);
        setBLProcessor(new PhotosBLProcessor());
        setActiveScreen(this);
        displayListImage();

    }

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
    public void takePictureAction(View view) {
        if (activeChalk == null)
            return;
        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                return;
            }

            if (!Feature.isFeatureAllowed(Feature.TAKE_PICTURES)) {
                Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                return;
            }

            int maxPhotos = 0;
            if (Feature.isFeatureAllowed(Feature.MAX_PHOTOS)) {
                String value = Feature.getFeatureValue(Feature.MAX_PHOTOS);
                try {
                    maxPhotos = Integer.parseInt(value);
                    for (int i = 0; i < activeChalk.getChalkPictures().size(); i++) {
                        if (activeChalk.getChalkPictures().get(i).getImagePath().contains("LPR")) {
                            maxPhotos = maxPhotos + 1;
                        }
                    }
                } catch (Exception e) {
                }
            }

            if (maxPhotos > 0 && activeChalk.getChalkPictures().size() >= maxPhotos) {
                displayErrorMessage("Exceeded max photos limit.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TPApp.setActiveChalk(activeChalk);
        Intent i = new Intent();
        i.setClass(this, TakePictureActivity.class);
        i.putExtra("NewChalkPicture", true);
        i.putExtra("isPhotoChalkEditScreen", true);
        i.putExtra("ChalkId", activeChalk.getChalkId());
        startActivityForResult(i, REQUEST_TAKE_PICTURE);

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

    public void displayListImage(){

        activeChalk = TPApp.getActiveChalk();
        ArrayList<ChalkPicture> chalkPictures = activeChalk.getChalkPictures();
        for (int i = 0; i <chalkPictures.size() ; i++) {

            if (chalkPictures.get(i).getImagePath().contains("SIGN-")){
                chalkPictures.remove(i);
            }

        }
        mListView.setAdapter(new ImageAdapter(chalkPictures,PhotoChalkEditActivity.this));
    }

    private class ImageAdapter extends BaseAdapter {
        private List<ChalkPicture> listData;
        private LayoutInflater layoutInflater;
        private Context context;
        private String ImgName;

        public ImageAdapter(List<ChalkPicture> listData,Context context) {
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

            final ChalkPicture adminTicketPicture = this.listData.get(position);
            /*if (adminTicketPicture.getImagePath().contains("SIGN-")){
				listData.remove(position);
			}*/
            if (!adminTicketPicture.getImagePath().contains("SIGN-")) {

                File previewImg = new File(adminTicketPicture.getImagePath());
                if (previewImg.exists()) {
                    Picasso.get()
                            .load(previewImg)
                            .resize(120, 120)
                            .centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(holder.siteImage);


                }

                if (adminTicketPicture.getImgName().contains("CHALK-LPR-")){
                    holder.reTake.setVisibility(View.GONE);
                    holder.delete.setVisibility(View.GONE);
                }else {
                    holder.reTake.setVisibility(View.VISIBLE);
                    holder.delete.setVisibility(View.VISIBLE);
                }

                //holder.dateText.setText(adminTicketPicture.getImgName());
                holder.reTake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.setClass(PhotoChalkEditActivity.this, TakePictureActivity.class);
                        i.putExtra("PictureIndex", position);
                        i.putExtra("ChalkId", adminTicketPicture.getChalkId());
                        i.putExtra("PictureName", adminTicketPicture.getImgName());
                        i.putExtra("EditChalkPictures", true);
                        i.putExtra("NewChalkPicture", true);
                        i.putExtra(TPConstant.INTENT_RECAPTURE_CONSTANT, true);
                        startActivityForResult(i, REQUEST_TAKE_PICTURE);
                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoChalkEditActivity.this);
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
                        i.setClass(PhotoChalkEditActivity.this, ViewPictureActivity.class);
                        i.putExtra("PictureIndex", position);
                        i.putExtra("ChalkId", adminTicketPicture.getChalkId());
                        i.putExtra("isChalkPicturePrev", true);
                        i.putExtra("ImageUrl", adminTicketPicture.getImagePath());

                        startActivityForResult(i, REQUEST_VIEW_PICTURE);
                    }
                });

            }
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
