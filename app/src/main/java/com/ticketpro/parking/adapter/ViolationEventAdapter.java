package com.ticketpro.parking.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.RetrofitClient;
import com.ticketpro.model.Color;
import com.ticketpro.model.CurveSenseZoneItemSelectedList;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Feature;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketPictureTemp;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.VioEventsBean;
import com.ticketpro.model.Violation;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.LPRNotifyActivity;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPUtility;
import com.ticketpro.vendors.curvesense.CurveSenseVioEventsListActivity;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ViolationEventAdapter extends RecyclerView.Adapter<ViolationEventAdapter.ViewHolder> {
    private List<VioEventsBean> violationEvents;
    private List<VioEventsBean> filteredList;
    private boolean isAscending = true;
    private boolean isVioTypeAscending = true;
    private boolean isPlateNoAscending = true;
    private boolean isDateAscending = true;
    private boolean isColorAscending = true;
    private Context context;
    private final ExecutorService executorService;
    private TPApplication TPApp;
    private Logger log = Logger.getLogger(getClass().getSimpleName());
    private long photoNumber;
    private String imageResolution;
    Ticket ticket;


    public ViolationEventAdapter(Context context, List<VioEventsBean> violationEvents) {
        this.context = context;
        this.violationEvents = violationEvents;
        this.executorService = Executors.newSingleThreadExecutor();
        this.TPApp = TPApplication.getInstance();
        this.filteredList = new ArrayList<>(violationEvents);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_violation_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VioEventsBean event = violationEvents.get(position);
        if (position < filteredList.size()) { // Ensure the position is within bounds
            VioEventsBean event1 = filteredList.get(position);
            // Bind event data to your view holder's views
        }
        int index = position;

        if ((index % 2) == 0) {
            holder.llRow.setBackgroundResource(R.drawable.tablerow_even);
        } else {
            holder.llRow.setBackgroundResource(R.drawable.tablerow_odd);
        }

        if (event.getIs_Violated().equalsIgnoreCase("Y")) {
            holder.tr_status_img.setBackgroundResource(R.drawable.color_red);
            holder.spaceName.setText(event.getSpace_name());
            if (event.getIs_Violated().equalsIgnoreCase("Y")) {
                holder.vioType.setText("Yes");
            } else {
                holder.vioType.setText("No");
            }

            holder.plateNo.setText(event.getPlate_no());
            String entryTime = entryTime(event.getEntry_time());
            holder.entryTime.setText(entryTime);
        } else {
            holder.tr_status_img.setBackgroundResource(R.drawable.color_green);
            holder.spaceName.setText(event.getSpace_name());
            if (event.getIs_Violated().equalsIgnoreCase("Y")) {
                holder.vioType.setText("Yes");
            } else {
                holder.vioType.setText("No");
            }
            holder.plateNo.setText(event.getPlate_no());
            String entryTime = entryTime(event.getEntry_time());
            holder.entryTime.setText(entryTime);
        }

        holder.itemView.setOnClickListener(v -> {
            // Call the method to show event details
            showEventDetails(context, event);
        });
    }

    public void filter(String query) {
        Log.d("Filter", "Query: " + query);
        Log.d("Filter", "Original List Size: " + violationEvents.size());

        filteredList.clear(); // Clear the current filtered list

        if (query.isEmpty()) {
            filteredList.addAll(violationEvents); // If query is empty, restore original
        } else {
            String filterPattern = query.toLowerCase().trim();

            for (VioEventsBean item : violationEvents) {
                if (item.getPlate_no() != null && item.getPlate_no().toLowerCase().contains(filterPattern)) {
                    filteredList.add(item); // Add matching items
                }
            }
        }

        Log.d("Filter", "Filtered List Size: " + filteredList.size());
        notifyDataSetChanged(); // Notify the adapter to refresh the view
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView spaceName, vioType, plateNo, entryTime;
        ImageView tr_status_img;
        LinearLayout llRow;

        ViewHolder(View itemView) {
            super(itemView);
            spaceName = itemView.findViewById(R.id.spaceName);
            vioType = itemView.findViewById(R.id.vioType);
            plateNo = itemView.findViewById(R.id.plateNo);
            entryTime = itemView.findViewById(R.id.entryTime);
            tr_status_img = itemView.findViewById(R.id.tr_status_img);
            llRow = itemView.findViewById(R.id.llRow);
        }
    }


    public void sortBySpaceName() {
        Collections.sort(violationEvents, (event1, event2) -> {
            if (isAscending) {
                return event1.getSpace_name().compareTo(event2.getSpace_name());
            } else {
                return event2.getSpace_name().compareTo(event1.getSpace_name());
            }
        });
        isAscending = !isAscending; // Toggle sort order
        notifyDataSetChanged();
    }

    public void sortByVioType() {
        Collections.sort(violationEvents, (event1, event2) -> {
            if (isVioTypeAscending) {
                return event1.getIs_Violated().compareTo(event2.getIs_Violated());
            } else {
                return event2.getIs_Violated().compareTo(event1.getIs_Violated());
            }
        });
        isVioTypeAscending = !isVioTypeAscending; // Toggle sort order
        notifyDataSetChanged();
    }

    public void sortByDate() {
        Collections.sort(violationEvents, (event1, event2) -> {
            if (isDateAscending) {
                return event1.getEntry_time().compareTo(event2.getEntry_time());
            } else {
                return event2.getEntry_time().compareTo(event1.getEntry_time());
            }
        });
        isDateAscending = !isDateAscending; // Toggle sort order
        notifyDataSetChanged();
    }

    public void sortByPlateNo() {
        Collections.sort(violationEvents, (event1, event2) -> {
            if (isPlateNoAscending) {
                return event1.getPlate_no().compareTo(event2.getPlate_no());
            } else {
                return event2.getPlate_no().compareTo(event1.getPlate_no());
            }
        });
        isPlateNoAscending = !isPlateNoAscending; // Toggle sort order
        notifyDataSetChanged();
    }


    private void showEventDetails(Context context, VioEventsBean event) {

        ImageView photo1ImageView;
        Preference preference;
        preference = Preference.getInstance(context);
        String token = preference.getString("CURVESENSE_TOKEN");


        View inputDlgView = LayoutInflater.from(context).inflate(R.layout.dialog_violation_event, null);

        photo1ImageView = inputDlgView.findViewById(R.id.photo1_imageview);
        TextView dialogue_heading = inputDlgView.findViewById(R.id.dialogue_heading);
        TextView dialogSpaceName = inputDlgView.findViewById(R.id.spaceName_textview);
        TextView dialogVioType = inputDlgView.findViewById(R.id.violation_desc_textview);
        TextView dialogPlateNo = inputDlgView.findViewById(R.id.plate_textview);
        TextView dialogState = inputDlgView.findViewById(R.id.state_textview);
        TextView dialogVioSubType = inputDlgView.findViewById(R.id.violation_code_textview);

        TextView dialogLocation = inputDlgView.findViewById(R.id.location_textview);
        TextView dialogZone = inputDlgView.findViewById(R.id.zone_textview);
        TextView dialogEntryTime = inputDlgView.findViewById(R.id.entryTime_textview);

        TextView dialogColor = inputDlgView.findViewById(R.id.color_textview);
        TextView dialogMake = inputDlgView.findViewById(R.id.make_textview);
        TextView dialogModel = inputDlgView.findViewById(R.id.model_textview);
        Button cancel_button = inputDlgView.findViewById(R.id.cancel_button);


        try {
            //  String location= getLocationFromCoordinates(event.getLatitude(),event.getLongitude());
            // Set data to views
            dialogSpaceName.setText(event.getSpace_name());
            dialogVioType.setText(event.getVio_type());
            dialogPlateNo.setText(event.getPlate_no());
            String entryTime = entryTime(event.getEntry_time());
            dialogEntryTime.setText(entryTime);
            // Set data to views
            dialogState.setText(event.getState());
            if(event.getVio_sub_type() == null || event.getVio_sub_type().equals("null")){
                dialogVioSubType.setText("NA");
            }else{
                dialogVioSubType.setText(String.valueOf(event.getVio_sub_type()));
            }

            String location = getAddressFromCoordinates(context, event.getLatitude(), event.getLongitude());
            dialogLocation.setText(location);
            dialogZone.setText(event.getZone());
            dialogColor.setText(event.getColor());
            dialogMake.setText(event.getMake());
            dialogModel.setText(event.getModel());
            dialogue_heading.setText(event.getZone());


            HttpURLConnection connection = (HttpURLConnection) new URL(event.getEntry_image()).openConnection();

            // Set the request method (GET, POST, etc.)
            connection.setRequestMethod("GET"); // Change to POST if needed

            // Add the token to the headers
            connection.setRequestProperty("Authorization", "Bearer " + token); // Adjust based on your API's requirements

            // Handle the response code
            int responseCode = connection.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null) {
                response.append(currentLine);
            }

            in.close();
            connection.disconnect(); // Disconnect when done
            loadBase64Image(photo1ImageView, response.toString());


        } catch (Exception e) {
            e.printStackTrace();


        }


        // Create dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(inputDlgView)
                .create();

        // Close dialog on button click
        dialog.setOnShowListener(dialogInterface -> {
            AlertDialog d = (AlertDialog) dialogInterface;
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        });


        cancel_button.setOnClickListener(view -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        });

        Button writeBtn = (Button) inputDlgView.findViewById(R.id.write_button);
        writeBtn.setOnClickListener(view -> {
            try {
                ticket = TPApp.createNewTicket();
                ticket.setStateCode(event.getState());
                ticket.setPlate(event.getPlate_no());
                String code = Color.getColorCodeByName(event.getColor());
                String make = MakeAndModel.getMakeCodeByName(event.getMake());
                ticket.setColorTitle(event.getColor());
                ticket.setColorCode(code);
                ticket.setMakeTitle(event.getMake());
                ticket.setMakeCode(make);
                ticket.setLocation(dialogLocation.getText().toString());
                ticket.setLatitude(String.valueOf(event.getLatitude()));
                ticket.setLongitude(String.valueOf(event.getLongitude()));
                ticket.setTimeZone(event.getZone());

                Uri curbSenseImage = imageViewToUri(photo1ImageView, context, ticket);


                if (event.getVio_type() != null) {
                    try {
                        TicketViolation ticketViolation = new TicketViolation();
                        ticketViolation.setTicketId(ticket.getTicketId());

//                        ticketViolation.setCitationNumber(ticket.getCitationNumber());
//                        ticketViolation.setViolationCode(lprNotify.getViolationCode());
                        ticketViolation.setViolationDesc(event.getVio_type());

                        // Update violation id
//                        try {
//                            ticketViolation.setViolationId(Integer.parseInt(lprNotify.getViolationId()));
//                        } catch (NumberFormatException e) {
//                            log.error(TPUtility.getPrintStackTrace(e));
//                        }


                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }


                TPApp.stickyViolations = false;
                TPApp.stickyComments = false;
                // discard active ticket
                if (TPApp.currentAcivity instanceof CurveSenseVioEventsListActivity) {
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
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
                dialog.dismiss();

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        });


        dialog.show();


    }


    public static String getAddressFromCoordinates(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String address = null;

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= fetchedAddress.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(fetchedAddress.getAddressLine(i)).append("\n");
                }
                address = stringBuilder.toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return address;
    }

    public interface LocationCallback {
        void onLocationReceived(String address);
    }

    private void loadBase64Image(ImageView imageView, String base64Image) {
        // Remove the data URI prefix if it exists
        if (base64Image.startsWith("data:image/png;base64,")) {
            base64Image = base64Image.replace("data:image/png;base64,", "");
        } else if (base64Image.startsWith("data:image/jpeg;base64,")) {
            base64Image = base64Image.replace("data:image/jpeg;base64,", "");
        }

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Set the decoded bitmap to ImageView
        ((Activity) context).runOnUiThread(() -> imageView.setImageBitmap(decodedByte));

    }

    private String entryTime(String entryTime) {
        String formattedDateTime = "";
        // Parse the date-time string
        LocalDateTime dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(entryTime);
        }

        // Define the desired format
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        }

        // Format the date-time
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDateTime = dateTime.format(formatter);
        }
        return formattedDateTime;
    }


    public Uri imageViewToUri(ImageView imageView, Context context, Ticket activeTicket) {
        // Step 1: Get the Bitmap from the ImageView
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            DeviceInfo deviceInfo = TPApp.getDeviceInfo();
            photoNumber = deviceInfo.getCurrentPhotoNumber() + 1;
            deviceInfo.setCurrentPhotoNumber(photoNumber);
            String filename = "";
            File file = null;
            imageResolution = "";
            filename = TPUtility.prefixZeros(activeTicket.getCitationNumber(), 8);
            filename = filename + "-TICKET-" + photoNumber + ".JPG";
            file = new File(TPUtility.getTicketsFolder(), filename);

            try (FileOutputStream out = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress the bitmap
                out.flush();
            } catch (IOException e) {
                log.error(TPUtility.getPrintStackTrace(e));
                return null;
            }

            TicketPicture picture = new TicketPicture();
            picture.setPictureDate(new Date());
            picture.setCitationNumber(activeTicket.getCitationNumber());
            picture.setMarkPrint("N");
            picture.setImagePath(Uri.fromFile(file).getPath());
            picture.setImageResolution(imageResolution);

            try {
                picture.setTicketId(activeTicket.getTicketId());
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            picture.setImageSize(TPUtility.getImageSize(Uri.fromFile(file).getPath()));
            picture.setCustId(TPApp.custId);
            picture.setSyncStatus("P");
         //   picture.insertTicketPicture(picture);
            activeTicket.getTicketPictures().add(picture);
            activeTicket.setPhotoCount(activeTicket.getTicketPictures().size());

            // Step 3: Get the Uri of the saved file
            return Uri.fromFile(file);
        }
        return null;
    }


}
