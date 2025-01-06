package com.ticketpro.parking.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ticketpro.model.Comment;
import com.ticketpro.parking.R;

import java.util.ArrayList;

public class DeleteCustomComment extends Activity {

    private RecyclerView recyclerView;
    private CheckBox chk_select_all;
    private Button btn_delete_all;

    private ArrayList<Comment> item_list = new ArrayList<>();
    private DeleteCustomeCommentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_custom_comment);

        initControls();
        getActionBar().setTitle("Custom Comments");
    }
    private void initControls() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
       // chk_select_all = (CheckBox) findViewById(R.id.chk_select_all);
        btn_delete_all = (Button) findViewById(R.id.btn_delete_all);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            item_list = Comment.getCustomComments();
            if (item_list.size()>0) {
                mAdapter = new DeleteCustomeCommentAdapter(item_list);
                recyclerView.setAdapter(mAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item_list.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    //builder.setTitle("Dlete ");
                    builder.setMessage("Delete All Items ?")
                            .setCancelable(false)
                            .setPositiveButton("CONFIRM",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
                                                Comment.removeAddedComments();
                                                mAdapter.notifyDataSetChanged();
                                                recyclerView.setVisibility(View.GONE);
                                                //DeleteCustomComment.this.finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    builder.show();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.notifyDataSetChanged();

    }

    public void backAction(View view) {
        //setResult(RESULT_OK);
        this.finish();
    }
}