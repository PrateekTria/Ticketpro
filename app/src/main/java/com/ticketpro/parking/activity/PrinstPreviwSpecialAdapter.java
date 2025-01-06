package com.ticketpro.parking.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.parking.R;

import java.util.ArrayList;

public class PrinstPreviwSpecialAdapter extends RecyclerView.Adapter<PrinstPreviwSpecialAdapter.ViewHolder> {
    int index = -1;
    private ArrayList<SpecialActivityReport> specialActivityReports;
    private PrintPreviewSpecialActivity context;

    public PrinstPreviwSpecialAdapter(ArrayList<SpecialActivityReport> specialActivityReports, PrintPreviewSpecialActivity context) {
        this.specialActivityReports = specialActivityReports;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_print_special_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.activity.setText(specialActivityReports.get(position).getActivityName());
        holder.count.setText(specialActivityReports.get(position).getTicketCount());
        holder.start.setText(specialActivityReports.get(position).getStartTime());
        holder.end.setText(specialActivityReports.get(position).getEndTime());
        holder.duration.setText(specialActivityReports.get(position).getDuration());
    }


    @Override
    public int getItemCount() {
        return specialActivityReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView activity,start,end,duration,count;
        public ViewHolder(View view) {
            super(view);
            activity = (TextView) view.findViewById(R.id.txtActivity);
            start = (TextView) view.findViewById(R.id.txtStart);
            end = (TextView) view.findViewById(R.id.txtEnd);
            duration = (TextView) view.findViewById(R.id.txtDuration);
            count = (TextView) view.findViewById(R.id.txtTicketCount);

        }
    }
}