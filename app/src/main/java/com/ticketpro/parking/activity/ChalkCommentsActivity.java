package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.ChalkComment;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.parking.R;
import com.ticketpro.util.TPUtility;

public class ChalkCommentsActivity extends BaseActivityImpl{

	private Handler dataHandler;
	private ListView listView;
	private ChalkVehicle activeChalk;
	private Dialog dialog;
	private EditText inputText;
	private boolean isReadOnly;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.chalk_comments);
			setLogger(ChalkCommentsActivity.class.getName());
			setActiveScreen(this);
			
			long chalkId=getIntent().getLongExtra("ChalkId", 0);
			if(chalkId==0){
				activeChalk=TPApp.getActiveChalk();
				isReadOnly=false;
			}else{
				activeChalk=ChalkVehicle.getChalkVehicleById(chalkId);
				isReadOnly=true;
			}
			
			if(isReadOnly){
				Button removeBtn=(Button)findViewById(R.id.remove_comment_btn);
				Button addBtn=(Button)findViewById(R.id.add_comment_btn);
				removeBtn.setVisibility(View.GONE);
				addBtn.setVisibility(View.GONE);
			}
			
			listView=(ListView)findViewById(R.id.comments_list);
			listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id) {
	            	if(isReadOnly){
	            		return false;
	            	}
	            	
	            	try{
	            		ChalkComment chalkComment=(ChalkComment)listView.getItemAtPosition(pos);
						editCommentAction(chalkComment.getComment(), pos , chalkComment.isPrivateComment());
					}catch (Exception e) {}
	            	
	                return true;
	            }
	        });
			
			dataHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(activeChalk==null){
						return;
					}
					
					ListAdapter listAdapter=new ListAdapter(ChalkCommentsActivity.this, R.id.title_textview, activeChalk.getComments());
					listView.setAdapter(listAdapter);
				}
			};

			bindDataAtLoadingTime();

		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	}    

	public void bindDataAtLoadingTime(){
		if(activeChalk!=null && activeChalk.getComments().size()==0){
			addCommentAction(null);
			return;
		}
		
		dataHandler.sendEmptyMessage(1);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if(data.hasExtra("Comment")){
	    		if(activeChalk.getComments().size()>=6){
					displayErrorMessage("Exceeded max comments limit.");
					return;
				}
				
				boolean isPrivate=data.getBooleanExtra("PrivateComment", false);
				int commentId=data.getIntExtra("CommentId", 0);
				String comment=data.getStringExtra("Comment");
				
				if(isPrivate){
					if(isDuplicatePrivateComment(commentId)){
						displayErrorMessage("Private comment for this violation is already exists.");
						return;
					}	
				}
				
				if(isDuplicateComment(activeChalk.getComments(),commentId)){
					displayErrorMessage("Selected comment already exists. Please select another.");
					return;
				}
				
				ChalkComment chalkcomment=new ChalkComment();
				chalkcomment.setChalkId(activeChalk.getChalkId());
				chalkcomment.setComment(comment);
				chalkcomment.setCommentId(commentId);
				chalkcomment.setIsPrivate(isPrivate?"Y":"N");
				chalkcomment.setCustId(TPApp.getCustId());
				
				activeChalk.getComments().add(chalkcomment);
				dataHandler.sendEmptyMessage(1);
			}
		}
		
		TPUtility.hideSoftKeyboard(this);
		
		if(activeChalk!=null && activeChalk.getComments().size()==0){
			setResult(RESULT_OK);
			finish();
		}
	}
	
	private boolean isDuplicateComment(List<ChalkComment> commentList, int commentId){
		boolean result=false;
		for(ChalkComment chalkComment:commentList){
			if(chalkComment.getCommentId()==commentId){
				return true;
			}
		}
		
		return result;
	}
	
	private boolean isDuplicatePrivateComment(int commentId){
		boolean result=false;
		
		List<ChalkComment> commentList=activeChalk.getComments();
		for(ChalkComment chalkComment:commentList){
			if(chalkComment.isPrivateComment()){
				return true;
			}
		}
				
		return result;
	}
	
	public void addCommentAction(View view){
		Intent i = new Intent();
		i.setClass(this, AddCommentActivity.class);
		startActivityForResult(i, 0);
	}
	
	public void clearAction(View view){
		if(activeChalk==null){
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Delete Confirmation")
	    .setMessage("Are you sure you want to delete?")
	    .setCancelable(true)
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
	 	})
	 	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activeChalk.getComments().clear();
				dataHandler.sendEmptyMessage(1);
			}
	 	});
	    
	    AlertDialog alert = builder.create();
	    alert.show();
	}
	
	public void editCommentAction(String commentText, final int position, boolean isPrivate){
		try{
			dialog = new Dialog(this);
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
			View inputDlgView = layoutInflater.inflate(R.layout.add_comment_input_dialog, null, false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(inputDlgView);
			dialog.show();
			
			TextView titleTextview=(TextView)inputDlgView.findViewById(R.id.add_comment_dialog_title);
			titleTextview.setText("Edit Comment");
			
			Button enterBtn=(Button)inputDlgView.findViewById(R.id.add_comment_input_dialog_enter_btn);
			inputText=(EditText)inputDlgView.findViewById(R.id.add_comment_input_dialog_text_field);
			if(isPrivate){
				inputText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(100)});
			}else{
				inputText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(32)});
			}
			
			inputText.setText(commentText);
			inputText.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					inputText.setText("");
					return true;
				}
			});
			
			inputText.setOnKeyListener(new View.OnKeyListener() {
			    public boolean onKey(View v, int keyCode, KeyEvent event) {
			        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
			            TPUtility.hideSoftKeyboard(ChalkCommentsActivity.this);
			            return true;
			        }

			        return false;
			    }
			});
			
			inputText.requestFocus();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
			@Override
			   public void run(){
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
			   }
			}, 50);
			
			CheckBox newCommentPrivateChk=(CheckBox)inputDlgView.findViewById(R.id.add_comment_private_chk);
			newCommentPrivateChk.setVisibility(View.GONE);
			
			enterBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					TPUtility.hideSoftKeyboard(ChalkCommentsActivity.this);
					
					try{
						ChalkComment comment=activeChalk.getComments().get(position);
						comment.setComment(inputText.getText().toString());
					}catch(Exception e){}
					
					if(dialog.isShowing())
						dialog.dismiss();
				}
			});
			
			Button cancelBtn=(Button)inputDlgView.findViewById(R.id.add_comment_input_dialog_cancel_btn);
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					TPUtility.hideSoftKeyboard(ChalkCommentsActivity.this);
					
					if(dialog.isShowing())
						dialog.dismiss();
				}
			});
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	}
	
	
	public void backAction(View view){
		onBackPressed();
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public void handleVoiceInput(String text) {
		if(text==null) return;
		
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		if(text.contains("BACK") || text.contains("CLOSE")){
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	public void handleVoiceMode(boolean voiceMode) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void handleNetworkStatus(boolean connected,boolean isFastConnection) {
		// TODO Auto-generated method stub
	}
	
	
	public class ListAdapter extends ArrayAdapter<ChalkComment>{
		private Context context;
		private ArrayList<ChalkComment> items;
		
		public ListAdapter(Context context, int textViewResourceId, ArrayList<ChalkComment> items){
			super(context, textViewResourceId, items);
			
			this.context=context;
			this.items=items;
	    }

	    @SuppressLint("ViewHolder")
		@Override
	    public View getView(final int position, View convertView, ViewGroup parent){
	    	LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    	View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);

	    	TextView titleView = (TextView) itemView.findViewById(R.id.title_textview);
	    	titleView.setText(this.items.get(position).getComment());
	    	
	    	ImageView deleteBtn = (ImageView) itemView.findViewById(R.id.delete_imageview);
			deleteBtn.setClickable(true);
			deleteBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
				    builder.setTitle("Delete Confirmation")
				    .setMessage("Are you sure you want to delete?")
				    .setCancelable(true)
				    .setNegativeButton("No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
				 	})
				 	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							items.remove(position);
							notifyDataSetChanged();
						}
				 	});
				    
				    AlertDialog alert = builder.create();
				    alert.show();
				}
			});
			
			if(isReadOnly){
				deleteBtn.setVisibility(View.GONE);
			}
			
	        return itemView;
	    }
	 }
}


