package com.ticketpro.parking.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.model.ZonePoleModel;
import com.ticketpro.parking.OnItemClickListener;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.vendors.ZonePoleList;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZonePoleAdapter extends RecyclerView.Adapter<ZonePoleAdapter.ZoneViewHolder> {

    private List<ZonePoleModel> zoneList;
    private TPApplication TPApp;
    RequestBody requestBody;
    private Context context;
    public ZonePoleAdapter(Context context,List<ZonePoleModel> zoneList) {
        this.zoneList = zoneList;
        this.TPApp = TPApplication.getInstance();
        this.context = context;
    }

    @Override
    public ZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false); // You can customize this layout
        return new ZoneViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ZoneViewHolder holder, int position) {
        ZonePoleModel zonePoleModel = zoneList.get(position);
        holder.user_name.setText(zonePoleModel.getZone_Name());

        holder.itemView.setOnClickListener(v -> {
            fetchItemDetails(zonePoleModel);
        });
     //   holder.zoneIdTextView.setText("Zone ID: " + zonePoleModel.getZone_Id() + ", Active: " + zonePoleModel.isActive());
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    public static class ZoneViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;

        public ZoneViewHolder(View view) {
            super(view);
            user_name = view.findViewById(R.id.user_name); // You can use your custom views here
         //   zoneIdTextView = view.findViewById(android.R.id.text2);
        }
    }

    private void fetchItemDetails(ZonePoleModel zonePoleModel) {
        // Assuming the item has a unique ID, make an API call to fetch more details for the clicked item
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://genetecapidev.ticketproweb.com/api/") // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_Id", TPApp.getCustId());
            jsonObject.put("zone_Id", zonePoleModel.getZone_Id());
            jsonObject.put("device_Id", TPApp.getDeviceId());


            // Convert JSONObject to RequestBody
            requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        ApiRequest apiService = retrofit.create(ApiRequest.class);

        // Example API call for fetching item details
        Call<ZonePoleModel> call = apiService.createZone(requestBody); // Replace with the actual API method
        call.enqueue(new Callback<ZonePoleModel>() {
            @Override
            public void onResponse(Call<ZonePoleModel> call, Response<ZonePoleModel> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(context, WriteTicketActivity.class);
                    intent.putExtra("zone_id", "Zone "+zonePoleModel.getZone_Name()+" Selected");
                    context.startActivity(intent);
                } else {
                }
            }

            @Override
            public void onFailure(Call<ZonePoleModel> call, Throwable t) {
                // Toast.makeText(MainActivity.this, "Failed to load item details", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

