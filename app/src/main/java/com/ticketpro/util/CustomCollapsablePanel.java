package com.ticketpro.util;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ticketpro.model.TicketViolation;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.AddCommentActivity;

public class CustomCollapsablePanel {

	Context ctx;
	public CustomCollapsablePanel(Context ctx){
		this.ctx=ctx;
	}

	public LinearLayout getPanel(final TicketViolation violation){

		final LinearLayout parentPanel=new LinearLayout(ctx);
		parentPanel.setOrientation(LinearLayout.VERTICAL);
		parentPanel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		parentPanel.setBackgroundColor(Color.WHITE);


		final LinearLayout mainPanel=new LinearLayout(ctx);
		mainPanel.setOrientation(LinearLayout.HORIZONTAL);
		mainPanel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 90));
		mainPanel.setBackgroundResource(R.drawable.background_listview);

		final ImageView plusBtn=new ImageView(ctx);
		plusBtn.setImageResource(R.drawable.button_plus_green);
		plusBtn.setPadding(5, 5, 5, 5);

		TextView violationText=new TextView(ctx);
		violationText.setText(violation.getViolationCode());
		violationText.setPadding(5, 0, 0, 0);
		violationText.setTextColor(Color.WHITE);
		violationText.setTextSize(18);
		violationText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		TextView violationDescription=new TextView(ctx);
		violationDescription.setText(violation.getViolationDesc());
		violationDescription.setPadding(5, 0, 0, 0);
		violationDescription.setTextColor(Color.WHITE);
		violationDescription.setTextSize(12);
		violationDescription.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		final LinearLayout vertPanel=new LinearLayout(ctx);
		vertPanel.setOrientation(LinearLayout.VERTICAL);
		vertPanel.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
		vertPanel.setBackgroundResource(R.drawable.background_listview);
		vertPanel.addView(violationText);
		vertPanel.addView(violationDescription);
		
		
		TextView violationNo=new TextView(ctx);
		violationNo.setText(violation.getViolationId());
		violationNo.setTextColor(Color.WHITE);
		violationNo.setPadding(0, 10, 0, 0);
		
		final ImageView trashBtn=new ImageView(ctx);
		trashBtn.setImageResource(R.drawable.trash);
		trashBtn.setPadding(0, 10, 0, 0);
		
		mainPanel.addView(plusBtn);
		mainPanel.addView(vertPanel);
		mainPanel.addView(violationNo);
		mainPanel.addView(trashBtn);

		final LinearLayout subPanel=new LinearLayout(ctx);
		subPanel.setOrientation(LinearLayout.VERTICAL);
		subPanel.setBackgroundColor(Color.WHITE);
		subPanel.setVisibility(View.INVISIBLE);

		LinearLayout.LayoutParams linLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 76);
		RelativeLayout.LayoutParams relLP = new RelativeLayout.LayoutParams(42, 42);

		/**************New Comment**************/
		final RelativeLayout newCommentPanel=new RelativeLayout(ctx);
		linLP.setMargins(0, 1, 0, 0);
		newCommentPanel.setLayoutParams(linLP);
		newCommentPanel.setBackgroundResource(R.drawable.background_listview);
		newCommentPanel.setVisibility(View.VISIBLE);

		ImageView plus_comment_icon=new ImageView(ctx);
		plus_comment_icon.setImageResource(R.drawable.plus_2);
		plus_comment_icon.setPadding(0, 10, 0, 0);
		newCommentPanel.addView(plus_comment_icon);

		TextView newComment=new TextView(ctx);
		newComment.setText("New Comment");
		newComment.setTextColor(Color.WHITE);
		newComment.setGravity(Gravity.LEFT);
		newComment.setPadding(40, 10, 0, 0);
		newCommentPanel.addView(newComment);


		ImageView  newComRightArrow=new ImageView(ctx);
		newComRightArrow.setImageResource(R.drawable.right_arrow);
		relLP = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		relLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		newComRightArrow.setPadding(5, 5, 5, 5);
		
		newComRightArrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//start a new activity
				Intent i = new Intent();
				//i.setClass(splashScreen, WriteTicketScreen.class);
				i.setClass(ctx, AddCommentActivity.class);
				i.putExtra("type", "public");
				i.putExtra("violationNo", violation.getViolationId());
				ctx.startActivity(i);
			}
		});
		
		
		newCommentPanel.addView(newComRightArrow,relLP);
		subPanel.addView(newCommentPanel);


		/**************New Private Comment**************/
		final RelativeLayout newPrivateCommentPanel=new RelativeLayout(ctx);
		//newPrivateCommentPanel.setOrientation(LinearLayout.HORIZONTAL);
		newPrivateCommentPanel.setLayoutParams(linLP);
		newPrivateCommentPanel.setBackgroundResource(R.drawable.background_listview);
		newPrivateCommentPanel.setVisibility(View.VISIBLE);

		ImageView private_plus_comment_icon=new ImageView(ctx);
		private_plus_comment_icon.setImageResource(R.drawable.plus_2);
		private_plus_comment_icon.setPadding(0, 10, 0, 0);
		newPrivateCommentPanel.addView(private_plus_comment_icon);


		TextView newPrivateComment=new TextView(ctx);
		newPrivateComment.setText("New Private Comment");
		newPrivateComment.setTextColor(Color.WHITE);
		newPrivateComment.setGravity(Gravity.LEFT);
		newPrivateComment.setPadding(40, 10, 0, 0);
		newPrivateCommentPanel.addView(newPrivateComment);


		ImageView  newPrivateComRightArrow=new ImageView(ctx);
		newPrivateComRightArrow.setImageResource(R.drawable.right_arrow);
		newPrivateComRightArrow.setPadding(5, 5, 5, 5);
		newPrivateCommentPanel.addView(newPrivateComRightArrow,relLP);
		subPanel.addView(newPrivateCommentPanel);

		
		newPrivateComRightArrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//start a new activity
				Intent i = new Intent();
				//i.setClass(splashScreen, WriteTicketScreen.class);
				i.setClass(ctx, AddCommentActivity.class);
				i.putExtra("type", "private");
				i.putExtra("violationNo", violation.getViolationId());
				ctx.startActivity(i);
			}
		});




		parentPanel.addView(mainPanel);
		parentPanel.addView(subPanel);


       //redine parent panels height
		final int expandLength=mainPanel.getLayoutParams().height+subPanel.getLayoutParams().height+3;
		final int collapseLength=mainPanel.getLayoutParams().height+3;

		ViewGroup.LayoutParams params = parentPanel.getLayoutParams();
		params.height = collapseLength;
		parentPanel.setLayoutParams(params);


		plusBtn.setOnClickListener(new OnClickListener() {



			@Override
			public void onClick(View v) {

				if(subPanel.getVisibility()==View.VISIBLE){
					subPanel.setVisibility(View.INVISIBLE);
					ViewGroup.LayoutParams params = parentPanel.getLayoutParams();
					params.height = collapseLength;
					parentPanel.setLayoutParams(params);
					parentPanel.setMinimumHeight(60);

					plusBtn.setImageResource(R.drawable.button_plus_green);

				}else{
					subPanel.setVisibility(View.VISIBLE);
					ViewGroup.LayoutParams params = parentPanel.getLayoutParams();
					params.height = expandLength;
					parentPanel.setLayoutParams(params);
					parentPanel.setMinimumHeight(60);
					plusBtn.setImageResource(R.drawable.minus_icon_button);

				}
			}
		});

		return parentPanel;
	}
}
