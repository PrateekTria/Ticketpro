package com.ticketpro.parking.activity.handlers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.LPRNotify;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.User;
import com.ticketpro.model.Violation;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.ChalkedVehicleDetailsActivity;
import com.ticketpro.parking.activity.LPRNotifyActivity;
import com.ticketpro.parking.activity.LocationChalkDetailsActivity;
import com.ticketpro.parking.activity.PhotoChalkDetailsActivity;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.TPApplication.TicketSource;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.util.BitmapDownloaderTask;
import com.ticketpro.util.CallbackHandler;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

//import android.util.ArrayMap;

public class NotificationHandler {
    private Intent notificationIntent;
    private TPApplication TPApp;
    private Context context;
    private Logger log = Logger.getLogger(getClass().getSimpleName());
    private Dialog notificationDialog;
    private Dialog imageDialog;
    private CallbackHandler removeCallback;
    private int downloadCount = 0;
    private View inputDlgView;
    private ImageView photo1ImageView;
    private ImageView photo2ImageView;
    private ImageView photo3ImageView;
    private ImageView photo4ImageView;

    private int photocount = 0;

    public NotificationHandler(Context context) {
        this.TPApp = TPApplication.getInstance();
        this.context = context;
    }

    public NotificationHandler(Context context, Intent notificationIntent) {
        this.TPApp = TPApplication.getInstance();
        this.notificationIntent = notificationIntent;
        this.context = context;
    }

    public void showNotification() throws Exception {
        try {
            final Intent data = this.notificationIntent;
            if (data == null) {
                return;
            }

            Bundle extras = data.getExtras();
            final String title = extras.getString("Title");
            final String message = extras.getString("Message");
            final String notificationType = extras.getString("Type");

            if (notificationType.equals("LPRNotify")) {
                lprNotification(title, message);
            } else if (notificationType.equalsIgnoreCase("Chalk")
                    || notificationType.equalsIgnoreCase("LocationChalk")
                    || notificationType.equalsIgnoreCase("PhotoChalk")) {

                chalkNotification(data);

            } else {
                systemNotification(data);
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void lprNotification(String title, String message) {
        try {

            JSONObject JSON = new JSONObject(message);
            LPRNotify lprNotify = new LPRNotify(JSON);

            showLPRNotify(lprNotify);
            //

        } catch (Exception e) {
            e.printStackTrace();
            //log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void showLPRNotify(final LPRNotify lprNotify) {

        try {
            final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            inputDlgView = layoutInflater.inflate(R.layout.lpr_notification_dialog, null, false);

            notificationDialog = new Dialog(getRunningActivity());
            notificationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            notificationDialog.setContentView(inputDlgView);
            notificationDialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
            notificationDialog.show();

            try {
                ((TextView) inputDlgView.findViewById(R.id.plate_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getPlate()));
                ((TextView) inputDlgView.findViewById(R.id.state_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getState()));
                ((TextView) inputDlgView.findViewById(R.id.make_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getMake()));
                ((TextView) inputDlgView.findViewById(R.id.model_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getModel()));
                ((TextView) inputDlgView.findViewById(R.id.body_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getBody()));
                ((TextView) inputDlgView.findViewById(R.id.location_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getLocation()));
                ((TextView) inputDlgView.findViewById(R.id.color_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getColor()));
                ((TextView) inputDlgView.findViewById(R.id.permit_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getPermit()));
                ((TextView) inputDlgView.findViewById(R.id.permit_type_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getPermitType()));
                ((TextView) inputDlgView.findViewById(R.id.permit_status_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getPermitStatus()));
                ((TextView) inputDlgView.findViewById(R.id.violation_code_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getViolationCode()));
                ((TextView) inputDlgView.findViewById(R.id.violation_desc_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getViolationDesc()));
                ((TextView) inputDlgView.findViewById(R.id.firstChalkTime_textview))
                        .setText(DateUtil.getStringFromDate(lprNotify.getFirstChalkTime()));
                ((TextView) inputDlgView.findViewById(R.id.lastSeen_textview))
                        .setText(DateUtil.getStringFromDate(lprNotify.getLastSeen()));
                ((TextView) inputDlgView.findViewById(R.id.zone_textview))
                        .setText(StringUtil.getDisplayString(lprNotify.getZone()));
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));

            }

            // Photo 1
            if (!StringUtil.isEmpty(lprNotify.getPhoto1())) {
                photo1ImageView = ((ImageView) inputDlgView.findViewById(R.id.photo1_imageview));
                photo1ImageView.setDrawingCacheEnabled(true);

                //Picasso.with(context).load(getCustomerImagesURL(lprNotify.getPhoto1())).into(photo1ImageView);
                //lazyLoadImage(getCustomerImagesURL(lprNotify.getPhoto1()), lprNotify.getPhoto1(), photo1ImageView);
                Glide.with(context)
                        .load(getCustomerImagesURL(lprNotify.getPhoto1()))
                        .into(photo1ImageView);

                photocount++;

                System.out.println("Image path=====>" + getCustomerImagesURL(lprNotify.getPhoto1()));

                photo1ImageView.setClickable(true);
                photo1ImageView.setOnClickListener(view -> {
                    notificationDialog.dismiss();

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        ImageView imageview = (ImageView) view;
                        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                        try {
                            showImagePreview(bitmap, lprNotify);
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    }, 100);
                });
            } else {
                ((LinearLayout) inputDlgView.findViewById(R.id.photo1_layout)).setVisibility(View.GONE);
            }

            // Photo 2
            if (!StringUtil.isEmpty(lprNotify.getPhoto2())) {
                photo2ImageView = ((ImageView) inputDlgView.findViewById(R.id.photo2_imageview));
                photo2ImageView.setDrawingCacheEnabled(true);

                Glide.with(context)
                        .load(getCustomerImagesURL(lprNotify.getPhoto2()))
                        .into(photo2ImageView);
                //Picasso.get().load(getCustomerImagesURL(lprNotify.getPhoto2())).into(photo2ImageView);
                //lazyLoadImage(getCustomerImagesURL(lprNotify.getPhoto2()), lprNotify.getPhoto2(), photo2ImageView);
                photocount++;

                photo2ImageView.setClickable(true);
                photo2ImageView.setOnClickListener(view -> {

                    notificationDialog.dismiss();

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        ImageView imageview = (ImageView) view;
                        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();

                        showImagePreview(bitmap, lprNotify);
                    }, 100);
                });

            } else {
                ((LinearLayout) inputDlgView.findViewById(R.id.photo2_layout)).setVisibility(View.GONE);
            }

            // Photo 3
            if (!StringUtil.isEmpty(lprNotify.getPhoto3())) {
                photo3ImageView = ((ImageView) inputDlgView.findViewById(R.id.photo3_imageview));
                photo3ImageView.setDrawingCacheEnabled(true);
                Glide.with(context)
                        .load(getCustomerImagesURL(lprNotify.getPhoto3()))
                        .into(photo3ImageView);
                //Picasso.get().load(getCustomerImagesURL(lprNotify.getPhoto3())).into(photo3ImageView);
                //lazyLoadImage(getCustomerImagesURL(lprNotify.getPhoto3()), lprNotify.getPhoto3(), photo3ImageView);
                photocount++;
                photo3ImageView.setClickable(true);
                photo3ImageView.setOnClickListener(view -> {

                    notificationDialog.dismiss();

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        ImageView imageview = (ImageView) view;
                        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();

                        showImagePreview(bitmap, lprNotify);
                    }, 100);
                });

            } else {
                ((LinearLayout) inputDlgView.findViewById(R.id.photo3_layout)).setVisibility(View.GONE);
            }

            // Photo 4
            if (!StringUtil.isEmpty(lprNotify.getPhoto4())) {
                photo4ImageView = ((ImageView) inputDlgView.findViewById(R.id.photo4_imageview));
                photo4ImageView.setDrawingCacheEnabled(true);
                Glide.with(context)
                        .load(getCustomerImagesURL(lprNotify.getPhoto4()))
                        .into(photo4ImageView);
                //Picasso.get().load(getCustomerImagesURL(lprNotify.getPhoto4())).into(photo4ImageView);
                //lazyLoadImage(getCustomerImagesURL(lprNotify.getPhoto4()), lprNotify.getPhoto4(), photo4ImageView);
                photocount++;
                photo4ImageView.setClickable(true);
                photo4ImageView.setOnClickListener(view -> {

                    notificationDialog.dismiss();

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        ImageView imageview = (ImageView) view;
                        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();

                        showImagePreview(bitmap, lprNotify);
                    }, 100);
                });

            } else {
                ((LinearLayout) inputDlgView.findViewById(R.id.photo4_layout)).setVisibility(View.GONE);
            }


            Button writeBtn = (Button) inputDlgView.findViewById(R.id.write_button);
            //writeBtn.setEnabled(false);
            //writeBtn.setBackgroundResource(R.drawable.btn_disabled);
            writeBtn.setOnClickListener(view -> {
                try {

                    /*if (!StringUtil.isEmpty(lprNotify.getPhoto1())) {

                        lazyLoadImage(getCustomerImagesURL(lprNotify.getPhoto1()), lprNotify.getPhoto1(),photo1ImageView);
                    }
                    if (!StringUtil.isEmpty(lprNotify.getPhoto2())) {

                        lazyLoadImage(getCustomerImagesURL(lprNotify.getPhoto2()), lprNotify.getPhoto2(),photo2ImageView);
                    }
                    if (!StringUtil.isEmpty(lprNotify.getPhoto3())) {

                        lazyLoadImage(getCustomerImagesURL(lprNotify.getPhoto3()), lprNotify.getPhoto3(),photo3ImageView);
                    }
                    if (!StringUtil.isEmpty(lprNotify.getPhoto4())) {

                        lazyLoadImage(getCustomerImagesURL(lprNotify.getPhoto4()), lprNotify.getPhoto4(),photo4ImageView);
                    }
*/
                    Ticket ticket = TPApp.createNewTicket();
                    ticket.setStateCode(lprNotify.getState());
                    ticket.setBodyCode(lprNotify.getBody());
                    ticket.setPlate(lprNotify.getPlate());
                    ticket.setMakeCode(lprNotify.getMake());
                    ticket.setColorCode(lprNotify.getColor());
                    ticket.setPermit(lprNotify.getPermit());
                    ticket.setLocation(lprNotify.getLocation());
                    ticket.setLatitude(lprNotify.getLatitude());
                    ticket.setLongitude(lprNotify.getLongitude());
                    ticket.setTimeMarked(lprNotify.getFirstChalkTime());
                    ticket.setTicketSource(TicketSource.LPR_NOTIFICATION);
                    ticket.setLprNotificationId(lprNotify.getNotificationId());
                    ticket.setPhoto_count(photocount);
                    ticket.setChalkTime(DateUtil.getStringFromDate4(lprNotify.getFirstChalkTime()));
                    ticket.setChalkLastSeen(DateUtil.getStringFromDate4(lprNotify.getLastSeen()));

                    if (lprNotify.getFirstChalkTime() != null) {
                        ticket.setIsChalked("Y");
                        ticket.setChalkId(1);
                        ticket.setTimeZone(lprNotify.getZone());
                        ticket.setChalkZone(lprNotify.getZone());

                        long milliseconds;
                        if (lprNotify.getLastSeen() != null) {
                            milliseconds = (lprNotify.getLastSeen().getTime() - lprNotify.getFirstChalkTime().getTime());
                        } else {
                            milliseconds = (new Date().getTime() - lprNotify.getFirstChalkTime().getTime());
                        }

                        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                        int hours = (int) ((milliseconds / (1000 * 60 * 60)));

                        String hrs = (hours < 10) ? ("0" + hours) : hours + "";
                        String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";
                        ticket.setElapsed(hrs + ":" + mins + " hrs/min");
                    }

                    if (lprNotify.getViolationCode() != null) {
                        try {
                            TicketViolation ticketViolation = new TicketViolation();
                            ticketViolation.setTicketId(ticket.getTicketId());
                            ticketViolation.setCitationNumber(ticket.getCitationNumber());
                            ticketViolation.setViolationCode(lprNotify.getViolationCode());
                            ticketViolation.setViolationDesc(lprNotify.getViolationDesc());

                            // Update violation id
                            try {
                                ticketViolation.setViolationId(Integer.parseInt(lprNotify.getViolationId()));
                            } catch (NumberFormatException e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }

                            Violation violation = Violation.getViolationById(Integer.parseInt(lprNotify.getViolationId()));
                            if (violation != null) {
                                ticketViolation.setFine(violation.getBaseFine());
                            } else if (ticketViolation.getViolationId() != 0) {
                                violation = Violation.getViolationById(ticketViolation.getViolationId());
                                if (violation != null) {
                                    ticketViolation.setFine(violation.getBaseFine());
                                }
                            }

                            ticket.getTicketViolations().add(ticketViolation);
                            List<TicketComment> commentList = ticketViolation.getTicketComments();
                            if (lprNotify.getComments() != null) {
                                if (!lprNotify.getComments().isEmpty()) {
                                    TicketComment tc = new TicketComment();
                                    tc.setComment(lprNotify.getComments());
                                    commentList.add(tc);
                                }
                            }

                            if (lprNotify.getComments2() != null) {
                                if (!lprNotify.getComments2().isEmpty()) {
                                    TicketComment tc1 = new TicketComment();
                                    tc1.setComment(lprNotify.getComments2());
                                    commentList.add(tc1);
                                }
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    }

                    //Create LPR Images
                    ImageView photo1ImageView = ((ImageView) inputDlgView.findViewById(R.id.photo1_imageview));
                    ImageView photo2ImageView = ((ImageView) inputDlgView.findViewById(R.id.photo2_imageview));
                    ImageView photo3ImageView = ((ImageView) inputDlgView.findViewById(R.id.photo3_imageview));
                    ImageView photo4ImageView = ((ImageView) inputDlgView.findViewById(R.id.photo4_imageview));

                    addLPRImage(ticket, photo1ImageView, lprNotify.getPhoto1(), lprNotify.getFirstChalkTime(), getCustomerImagesURL(lprNotify.getPhoto1()));
                    addLPRImage(ticket, photo2ImageView, lprNotify.getPhoto2(), lprNotify.getFirstChalkTime(), getCustomerImagesURL(lprNotify.getPhoto2()));
                    addLPRImage(ticket, photo3ImageView, lprNotify.getPhoto3(), lprNotify.getLastSeen(), getCustomerImagesURL(lprNotify.getPhoto3()));
                    addLPRImage(ticket, photo4ImageView, lprNotify.getPhoto4(), lprNotify.getLastSeen(), getCustomerImagesURL(lprNotify.getPhoto4()));
                    TPApp.setStickyViolation(null);
                    TPApp.setStickyComment(null);
                    TPApp.stickyViolations = false;
                    TPApp.stickyComments = false;
                    // discard active ticket
                    if (TPApp.currentAcivity instanceof LPRNotifyActivity) {
                        //((LPRNotifyActivity) TPApp.currentAcivity).discardTicket();
                        TPApp.currentAcivity.finish();
                    }


                    if (TPApp.currentAcivity instanceof WriteTicketActivity) {
                        ((WriteTicketActivity) TPApp.currentAcivity).discardTicket();
                        TPApp.currentAcivity.finish();
                    }

                    TPApp.setActiveTicket(ticket);

                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(context, WriteTicketActivity.class);
                    context.startActivity(intent);
                    getActiveActivity().finish();
                    notificationDialog.dismiss();

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.cancel_button);
            cancelBtn.setOnClickListener(view -> {
                if (notificationDialog.isShowing()) {
                    notificationDialog.dismiss();
                }
            });

            Button closeButton = (Button) inputDlgView.findViewById(R.id.close_button);
            closeButton.setOnClickListener(view -> {
                if (notificationDialog.isShowing()) {
                    notificationDialog.dismiss();
                }
            });

            Button deleteButton = (Button) inputDlgView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(view -> deleteNotification(lprNotify.getNotificationId()));

            if (TPApp.getUserInfo() == null) {
                closeButton.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.GONE);
                writeBtn.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            } else if (downloadCount <= 0) {
                //writeBtn.setEnabled(true);
                //writeBtn.setBackgroundResource(R.drawable.btn_yellow);
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    private void addLPRImage(Ticket ticket, ImageView photo1ImageView, String imageFile, Date pictureDate, String url) {
        if (StringUtil.isEmpty(imageFile) || photo1ImageView == null) {
            return;
        }

        String imagePath = ticket.getCitationNumber() + "-VLPR-" + imageFile;//TPUtility.getTicketsFolder() +ticket.getCitationNumber()+"-VLPR-"+ imageFile;

        TicketPicture picture = new TicketPicture();
        picture.setCitationNumber(ticket.getCitationNumber());
        picture.setTicketId(ticket.getTicketId());
        picture.setImagePath(imagePath);
        picture.setCustId(ticket.getCustId());
        picture.setDownloadImageUrl(url);
        picture.setImageName(imagePath);
        picture.setSyncStatus("L");
        //picture.setMarkPrint("Y");
        picture.setMarkPrint("N");
        if (pictureDate == null) {
            picture.setPictureDate(new Date());
        } else {
            picture.setPictureDate(pictureDate);
        }

        picture.setLprNotification("Y");
        picture.setLprImageName(ticket.getCitationNumber() + "-VLPR-" + imageFile);
		

		
		/*Bitmap bitmap = photo1ImageView.getDrawingCache();
		if(bitmap != null){
			try{
				bitmap.compress(CompressFormat.JPEG, 90, new FileOutputStream(imagePath));
				picture.setImageResolution(bitmap.getWidth() + "x" + bitmap.getHeight());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		picture.setImageSize(TPUtility.getImageSize(imagePath));*/
        ticket.getTicketPictures().add(picture);
    }

    public void deleteNotification(final String notificationId) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getRunningActivity());

            builder.setTitle("Delete Confirmation");
            builder.setMessage("Are you sure you want to delete LPR Notification?");
            builder.setCancelable(true).setNegativeButton("No", (dialog, which) -> {

            });

            builder.setPositiveButton("Yes", (dialog, which) -> {
                if (notificationDialog.isShowing()) {
                    notificationDialog.dismiss();
                }

                if (removeCallback != null) {
                    onRemoveNotification(notificationId);
                } else {
                    try {
                        LPRNotify.removeNotificationById(notificationId);
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRemoveNotification(String notificationId) {
        if (removeCallback != null) {
            removeCallback.success(notificationId);
        }
    }

    private void chalkNotification(Intent data) {
        try {
            Bundle extras = data.getExtras();
            final String title = extras.getString("Title");
            final String message = extras.getString("Message");
            final String notificationType = extras.getString("Type");
            final long chalkId = extras.getLong("ChalkId", 0);
            String username = "NA";

            // Get Chalk Details
            try {
                ChalkVehicle chalk = ChalkVehicle.getChalkVehicleById(chalkId);
                if (chalk == null) {
                    Toast.makeText(context, "Chalk details are not available.", Toast.LENGTH_LONG).show();
                    return;
                }

                User user = User.getUserInfo(chalk.getUserId());
                if (user != null) {
                    username = user.getUsername();
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            StringBuilder msg = new StringBuilder();
            StringBuilder values = new StringBuilder();
            StringBuilder header = new StringBuilder();

            header.append(title + "\n");

            header.append(message + "\n");

            msg.append("Officer" + username + "\n");
            values.append(": " + username + "\n");


            try {
				/*WebView wv = new WebView(getRunningActivity());
				wv.loadData(msg.toString(), "text/html", "utf-8");
				wv.setBackgroundColor(android.graphics.Color.TRANSPARENT);
				wv.getSettings().setDefaultTextEncodingName("utf-8");*/
                View view = LayoutInflater.from(getRunningActivity()).inflate(R.layout.item_view, null);
                TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
                TextView valueTV = (TextView) view.findViewById(R.id.valueTV);
                TextView headTV = (TextView) view.findViewById(R.id.headerTextTV);
                headTV.setVisibility(View.VISIBLE);
                headerTV.setText(msg.toString());
                valueTV.setText(values.toString());
                headTV.setText(header.toString());
                AlertDialog.Builder dialog = new AlertDialog.Builder(getRunningActivity());

                dialog.setView(view);
                dialog.setCancelable(true);
                dialog.setTitle("Notification");
                dialog.setNegativeButton("Write Ticket", (dialog1, which) -> {
                    dialog1.dismiss();

                    try {
                        ChalkVehicle chalk = ChalkVehicle.getChalkVehicleById(chalkId);

                        Ticket ticket = TPApp.createTicketForChalk(chalk);
                        ticket.setPhoto_count(chalk.getChalkPictures().size());
                        ticket.setTicketSource(TicketSource.CHALK_NOTIFICATION);

                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(context, WriteTicketActivity.class);
                        intent.putExtra("ChalkPicture", true);
                        intent.putExtra("ChalkId", chalkId);

                        context.startActivity(intent);

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                });

                dialog.setNeutralButton("Ignore", (dialog12, which) -> dialog12.dismiss());

                if (chalkId != 0) {
                    dialog.setPositiveButton("View", (dialog13, which) -> {
                        dialog13.dismiss();

                        Intent detailsIntent = null;
                        if (notificationType.equals("Chalk")) {
                            detailsIntent = new Intent(context, ChalkedVehicleDetailsActivity.class);
                            detailsIntent.putExtra("ChalkId", chalkId);
                        }

                        if (notificationType.equals("LocationChalk")) {
                            detailsIntent = new Intent(context, LocationChalkDetailsActivity.class);
                            detailsIntent.putExtra("ChalkId", chalkId);
                        }

                        if (notificationType.equals("PhotoChalk")) {
                            detailsIntent = new Intent(context, PhotoChalkDetailsActivity.class);
                            detailsIntent.putExtra("ChalkId", chalkId);
                        }

                        if (detailsIntent != null) {
                            detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(detailsIntent);
                        }
                    });
                }

                AlertDialog alertDialog = dialog.create();


                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setBackgroundResource(R.drawable.btn_yellow);
                    positiveButton.setTextAppearance(context, R.style.ButtonText);
                }

                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                if (negativeButton != null) {
                    negativeButton.setBackgroundResource(R.drawable.btn_blue);
                    negativeButton.setTextAppearance(context, R.style.ButtonText);
                }

                Button neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                if (neutralButton != null) {
                    neutralButton.setBackgroundResource(R.drawable.btn_yellow);
                    neutralButton.setTextAppearance(context, R.style.ButtonText);
                }
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void systemNotification(Intent data) throws Exception {
        try {
            Bundle extras = data.getExtras();
            final String title = extras.getString("Title");
            final String message = extras.getString("Message");
            View view = LayoutInflater.from(context).inflate(R.layout.item_view, null);
            TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
            TextView valueTV = (TextView) view.findViewById(R.id.valueTV);

            StringBuilder keys = new StringBuilder();
            StringBuilder values = new StringBuilder();


            StringBuffer msg = new StringBuffer();
            msg.append("" + title + "");
            msg.append("");
            msg.append(message);

			/*WebView wv = new WebView(getRunningActivity());
			wv.loadData(msg.toString(), "text/html", "utf-8");
			wv.setBackgroundColor(android.graphics.Color.TRANSPARENT);
			wv.getSettings().setDefaultTextEncodingName("utf-8");
*/

            AlertDialog.Builder dialog = new AlertDialog.Builder(getRunningActivity());

            dialog.setMessage(message);
            dialog.setCancelable(true);
            dialog.setTitle("Notification");
            dialog.setPositiveButton("OK", (dialog1, which) -> {
                dialog1.dismiss();
                TPApp.notificationIntents.clear();
            });

            AlertDialog alertDialog = dialog.create();


            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.setBackgroundResource(R.drawable.btn_yellow);
                positiveButton.setTextAppearance(context, R.style.ButtonText);
            }
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCustomerImagesURL(String filename) {
        CustomerInfo customerInfo = TPApp.getCustomerInfo();
        String contentFolder = customerInfo.getContentFolder();
        if (contentFolder == null || contentFolder.equals("")) {
            contentFolder = customerInfo.getCustId() + "";
        }

        return TPConstant.IMAGES_URL + "/" + contentFolder + "/" + filename;
    }

    private void lazyLoadImage(String url, String photoName, ImageView imageView) {
        File imgFile = new File(TPUtility.getLPRImagesFolder() + photoName);
        if (imgFile.exists()) {
            imageView.setImageURI(Uri.fromFile(imgFile));
            return;
        }

        downloadCount++;

        BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
        task.setCallback(new CallbackHandler() {
            @Override
            public void success(String data) {
                downloadCount--;
				
				/*if(downloadCount <= 0 && inputDlgView!=null){
					Button writeBtn = (Button) inputDlgView.findViewById(R.id.write_button);
					writeBtn.setEnabled(true);
					writeBtn.setBackgroundResource(R.drawable.btn_yellow);
				}*/
            }

            @Override
            public void failure(String message) {
            }
        });

        task.execute(url, photoName);
    }

    private void showImagePreview(Bitmap bitmap, final LPRNotify lprNotify) {
        try {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View imageDlgView = layoutInflater.inflate(R.layout.image_view_dialog, null, false);

            imageDialog = new Dialog(getRunningActivity());
            imageDialog.requestWindowFeature(Window.FEATURE_RIGHT_ICON);
            imageDialog.setContentView(imageDlgView);
            imageDialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
            imageDialog.getWindow().getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imageDialog.setCancelable(true);

            ImageView imageView = (ImageView) imageDlgView.findViewById(R.id.imageview);
            if (bitmap != null) {
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float scaleWidth = metrics.scaledDensity;
                float scaleHeight = metrics.scaledDensity;

                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);

                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                imageView.setImageBitmap(resizedBitmap);
            }

            Button cancelBtn = (Button) imageDlgView.findViewById(R.id.close_button);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageDialog.dismiss();

                    showLPRNotify(lprNotify);
                }
            });

            imageDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Activity getRunningActivity() throws Exception {
        if (TPApp.currentAcivity != null) {
            return TPApp.currentAcivity;
        }

        try {
            return getActiveActivity();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return null;
    }

    @SuppressLint("NewApi")
    public Activity getActiveActivity() throws Exception {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);

            ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return null;
    }

    public void ManageActivityStack(Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> alltasks = am.getRunningTasks(1);

        for (ActivityManager.RunningTaskInfo activeTask : alltasks) {
            String packageName = "com.ticketpro.parking";
            if (activeTask.topActivity.getClassName().equals(packageName + ".WriteTicketActivity")) {
//                 WriteTicketActivity.writeTicket.finish();
//                 WriteTicketActivity.writeTicket.discardTicket();
            }
        }
    }

    public Intent getNotificationIntent() {
        return notificationIntent;
    }

    public void setNotificationIntent(Intent notificationIntent) {
        this.notificationIntent = notificationIntent;
    }

    public TPApplication getTPApp() {
        return TPApp;
    }

    public void setTPApp(TPApplication tPApp) {
        TPApp = tPApp;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public Dialog getNotificationDialog() {
        return notificationDialog;
    }

    public void setNotificationDialog(Dialog notificationDialog) {
        this.notificationDialog = notificationDialog;
    }

    public Dialog getImageDialog() {
        return imageDialog;
    }

    public void setImageDialog(Dialog imageDialog) {
        this.imageDialog = imageDialog;
    }

    public CallbackHandler getRemoveCallback() {
        return removeCallback;
    }

    public void setRemoveCallback(CallbackHandler removeCallback) {
        this.removeCallback = removeCallback;
    }

}
