package com.ticketpro.parking.activity;

import java.util.HashMap;
import java.util.List;

import com.ticketpro.parking.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlateLookupListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;
	
	public PlateLookupListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
		this.context = context;
		this.listDataHeader = listDataHeader;
		this.listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition);
		final String parentText = (String) getGroup(groupPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.plate_lookup_list_item, null);
		}

		WebView webview = (WebView) convertView.findViewById(R.id.listItems);
		TextView textview = (TextView) convertView.findViewById(R.id.textItems);
		TextView tvColor = (TextView) convertView.findViewById(R.id.tv_color);

		RelativeLayout backLayout = (RelativeLayout) convertView.findViewById(R.id.rel_view);

		if (parentText.contains("Permit History")) {
			webview.setVisibility(View.GONE);
			textview.setVisibility(View.VISIBLE);
			textview.setText(childText);
			backLayout.setVisibility(View.VISIBLE);
			tvColor.setVisibility(View.INVISIBLE);
		}
		else if (parentText.contains("Prior Tickets")) {
			webview.setVisibility(View.GONE);
			textview.setVisibility(View.VISIBLE);
			//textview.setText(childText.substring(1));
			backLayout.setVisibility(View.VISIBLE);
			tvColor.setVisibility(View.VISIBLE);

			try {
				String ticketType = childText.substring(0, 1);
				if (ticketType.contains("D")) {
					tvColor.setText("D");
					textview.setText(childText.substring(1));
					tvColor.setBackgroundResource(R.drawable.color_orange_1);
				} else if (ticketType.contains("W")) {
					tvColor.setText("W");
					textview.setText(childText.substring(1));
					tvColor.setBackgroundResource(R.drawable.color_green_1);
				} else if (ticketType.contains("V")) {
					tvColor.setText("V");
					textview.setText(childText.substring(1));
					tvColor.setBackgroundResource(R.drawable.color_red_1);
				} else {
					ticketType = "";
					textview.setText(childText);
					tvColor.setText("");
					tvColor.setBackgroundResource(R.color.transparent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			webview.setVisibility(View.VISIBLE);
			textview.setVisibility(View.GONE);
			backLayout.setVisibility(View.GONE);
			tvColor.setVisibility(View.GONE);

			WebSettings webSettings = webview.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			webSettings.setBlockNetworkImage(true);
			webSettings.setLoadsImagesAutomatically(true);
			webSettings.setGeolocationEnabled(false);
			webSettings.setNeedInitialFocus(false);
			webSettings.setSaveFormData(false);

			webview.setScrollContainer(false);

			webview.setWebChromeClient(new WebChromeClient());
			webview.setBackgroundColor(0x00000000);
			webview.loadData(childText, "text/html", "UTF-8");
			webview.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {

				}
			});
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.plate_lookup_list_group, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.headerView);
		LinearLayout llHeaderAccordion = (LinearLayout) convertView.findViewById(R.id.llHeaderAccordion);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);
		if(headerTitle.equalsIgnoreCase("HOTLIST")){
			llHeaderAccordion.setBackground(context.getResources().getDrawable(R.drawable.btn_green));
		}
		else if(headerTitle.equalsIgnoreCase("STOLEN")){
			llHeaderAccordion.setBackground(context.getResources().getDrawable(R.drawable.btn_deep_red));
		}
		else if(headerTitle.equalsIgnoreCase("ALERT")){
			llHeaderAccordion.setBackground(context.getResources().getDrawable(R.drawable.btn_deep_yellow));
		}else{
			llHeaderAccordion.setBackground(context.getResources().getDrawable(R.drawable.btn_yellow));
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
