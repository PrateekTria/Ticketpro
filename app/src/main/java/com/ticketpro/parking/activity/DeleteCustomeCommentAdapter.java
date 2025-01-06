package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ticketpro.model.Comment;
import com.ticketpro.parking.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DeleteCustomeCommentAdapter extends RecyclerView.Adapter<DeleteCustomeCommentAdapter.ViewHolder>{
    public ArrayList<Comment> item_list;


    public DeleteCustomeCommentAdapter(ArrayList<Comment> arrayList) {
        item_list = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_delete_custom_row, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        final int pos = position;

        holder.item_name.setText(item_list.get(position).getTitle());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteItemFromList(v, pos);
            }
        });


    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }
    // confirmation dialog box to delete an unit
    private void deleteItemFromList(View v, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        //builder.setTitle("Dlete ");
        builder.setMessage("Delete Item ?")
                .setCancelable(false)
                .setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    Comment.removeById(item_list.get(position).getId());
                                    item_list.remove(position);
                                    notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }



                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });

        builder.show();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_name;
        public ImageButton btn_delete;
        public CheckBox chkSelected;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            item_name = (TextView) itemLayoutView.findViewById(R.id.txt_Name);
            btn_delete = (ImageButton) itemLayoutView.findViewById(R.id.btn_delete_unit);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chk_selected);

        }
    }
}
