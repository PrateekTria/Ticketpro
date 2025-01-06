package com.ticketpro.parking.activity;

import java.util.ArrayList;

//import com.crashlytics.android.Crashlytics;
import com.ticketpro.model.MenuAction;
import com.ticketpro.parking.R;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

//import io.fabric.sdk.android.Fabric;

public class PhotoChalkMenuFragment extends Fragment{

	private ExpandListAdapter expandAdapter;
	private ArrayList<MenuAction> menus;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Fabric.with(getActivity(), new Crashlytics());
		View fragmentView = inflater.inflate(R.layout.menu_fragement, container, false);
		ExpandableListView expandListview = (ExpandableListView) fragmentView.findViewById(R.id.menus_listview);
		menus=new ArrayList<MenuAction>();
        
		// Action Menu
		MenuAction actionMenu=new MenuAction();
        actionMenu.setTitle("Actions");
        actionMenu.setParent(true);
        
        MenuAction m1=new MenuAction();
        m1.setTitle("Chalk Xchange");
        
        MenuAction m2=new MenuAction();
        m2.setTitle("Map View");
        
        MenuAction m4=new MenuAction();
        m4.setTitle("Clear Chalk Log");
        
        
        MenuAction m3=new MenuAction();
        if(TPApplication.getInstance().enableChalkAlerts){
        	m3.setTitle("Turn Off Chalk Alerts");
        }else{
        	m3.setTitle("Turn On Chalk Alerts");
        }
        
        actionMenu.getSubMenus().add(m1);
        actionMenu.getSubMenus().add(m2);
        actionMenu.getSubMenus().add(m3);
        actionMenu.getSubMenus().add(m4);
        
        menus.add(actionMenu);
        expandAdapter = new ExpandListAdapter(((PhotoChalkMenuActivity)getActivity()).getContent(), menus);
		expandListview.setAdapter(expandAdapter);
		expandListview.expandGroup(0);
		
		return fragmentView;
	}
	
	
	class ExpandListAdapter extends BaseExpandableListAdapter {

		private Context context;
		private ArrayList<MenuAction> actions;
		public ExpandListAdapter(Context context, ArrayList<MenuAction> actions) {
			this.context = context;
			this.actions = actions;
		}
		
		public void addItem(MenuAction menuAction, MenuAction parentMenu) {
			if (!actions.contains(parentMenu)) {
				actions.add(parentMenu);
			}
			
			int index = actions.indexOf(parentMenu);
			ArrayList<MenuAction> submenus = actions.get(index).getSubMenus();
			submenus.add(menuAction);
			actions.get(index).setSubMenus(submenus);
		}
		
		public Object getChild(int parentPosition, int childPosition) {
			ArrayList<MenuAction> submenus = actions.get(parentPosition).getSubMenus();
			return submenus.get(childPosition);
		}

		public long getChildId(int parentPosition, int childPosition) {
			return childPosition;
		}

		public View getChildView(int actionPosition, int childPosition, boolean isLastChild, View view,ViewGroup parent) {
			MenuAction child = (MenuAction) getChild(actionPosition, childPosition);
			if (view == null) {
				LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = infalInflater.inflate(R.layout.menu_action_row_item, null);
			}
			
			final String actionItem=child.getTitle();
			view.setClickable(true);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((PhotoChalkMenuActivity)getActivity()).getSlideoutHelper().close();
					((PhotoChalkMenuActivity)getActivity()).callAction(actionItem);
				}
			});
			
			TextView tv = (TextView) view.findViewById(R.id.action_title_textview);
			tv.setText(child.getTitle());
			
			return view;
		}

		public int getChildrenCount(int parentPosition) {
			ArrayList<MenuAction> submenus = actions.get(parentPosition).getSubMenus();
			return submenus.size();
		}

		public Object getGroup(int parentPosition) {
			return actions.get(parentPosition);
		}

		public int getGroupCount() {
			return actions.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isLastChild, View view,ViewGroup parent) {
			MenuAction group = (MenuAction) getGroup(groupPosition);
			if (view == null) {
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				view = inf.inflate(R.layout.menu_action_group_item, null);
			}
			
			TextView tvTitle = (TextView) view.findViewById(R.id.action_title_textview);
			tvTitle.setText(group.getTitle());
			return view;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}

	}
	
}
