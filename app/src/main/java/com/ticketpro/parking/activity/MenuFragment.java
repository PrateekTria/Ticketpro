package com.ticketpro.parking.activity;

import java.util.ArrayList;

import com.ticketpro.model.Feature;
import com.ticketpro.model.MenuAction;
import com.ticketpro.parking.R;
import com.ticketpro.util.TPUtility;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class MenuFragment extends Fragment{

	private ExpandListAdapter expandAdapter;
	private ArrayList<MenuAction> menus;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View fragmentView = inflater.inflate(R.layout.menu_fragement, container, false);
		ExpandableListView expandListview = (ExpandableListView) fragmentView.findViewById(R.id.menus_listview);
		menus=new ArrayList<MenuAction>();
        
		// Action Menu
		MenuAction actionMenu=new MenuAction();
        actionMenu.setTitle("Actions");
        actionMenu.setParent(true);
        
        try {
        	if(TPUtility.isTrafficInstallled(getActivity().getPackageManager())){
				MenuAction traffic=new MenuAction();
				traffic.setTitle("Switch To Traffic");
			    
				actionMenu.getSubMenus().add(traffic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        MenuAction m0=new MenuAction();
        m0.setTitle("Change Duty/Route");
        
        MenuAction m1=new MenuAction();
        m1.setTitle("Reprint Last Ticket");
        
        MenuAction m2=new MenuAction();
        m2.setTitle("Take Picture");

		MenuAction m3=new MenuAction();
		m3.setTitle("Use Same Plate/Vin");

		MenuAction m4=new MenuAction();
        m4.setTitle("Use Same Violation");
        
        MenuAction m5=new MenuAction();
        m5.setTitle("Void Last Ticket");
        
        MenuAction m6=new MenuAction();
        m6.setTitle("Make Last Ticket A Driveaway");
        
        MenuAction m7=new MenuAction();
        m7.setTitle("Make Last Ticket A Warning");

		/*MenuAction mo3=new MenuAction();
		mo3.setTitle("Hotlist");
*/
        MenuAction m8=new MenuAction();
        m8.setTitle("Advance Paper");
        
        MenuAction m9=new MenuAction();
        m9.setTitle("Clear Fields");
        
        MenuAction m10=new MenuAction();
        m10.setTitle("Chalk"); 
         
        actionMenu.getSubMenus().add(m0);
        actionMenu.getSubMenus().add(m1);
        //actionMenu.getSubMenus().add(mo3);
        actionMenu.getSubMenus().add(m3);
        actionMenu.getSubMenus().add(m4);
        actionMenu.getSubMenus().add(m5);
        actionMenu.getSubMenus().add(m6);  
        
        if(!Feature.isHiddenField("warning")){
        	actionMenu.getSubMenus().add(m7);
        }
        
        actionMenu.getSubMenus().add(m8);
        //actionMenu.getSubMenus().add(m9);
        //actionMenu.getSubMenus().add(m10);
        
        //Administration Menu
        MenuAction adminMenu=new MenuAction();
        adminMenu.setTitle("Administration");
        adminMenu.setParent(true);
        
        MenuAction m11=new MenuAction();
        m11.setTitle("Duty Logs");
        
        MenuAction m12=new MenuAction();
        m12.setTitle("Ticket Logs");
        
        MenuAction m13=new MenuAction();
        m13.setTitle("Messages");
        
        MenuAction m14=new MenuAction();
        m14.setTitle("LPRNotify Logs");
        
        MenuAction m151=new MenuAction();
        m151.setTitle("Check Zone");
        
        //adminMenu.getSubMenus().add(m11);
        adminMenu.getSubMenus().add(m12);
        adminMenu.getSubMenus().add(m13);
        adminMenu.getSubMenus().add(m14);
        adminMenu.getSubMenus().add(m151);
        
        MenuAction specialMenu=new MenuAction();
        specialMenu.setTitle("Special Options");
        specialMenu.setParent(true);
        
        MenuAction m15=new MenuAction();
        m15.setTitle("Aid Citizen");
        
        MenuAction m16=new MenuAction();
        m16.setTitle("Maintenance");

        MenuAction m17=new MenuAction();
        m17.setTitle("View Map");
        
        MenuAction m18=new MenuAction();
        m18.setTitle("Contact List");
        
        MenuAction m19 = new MenuAction();
        m19.setTitle("LPR");
        
        MenuAction m20=new MenuAction();
        m20.setTitle("Special Activity");
        
        MenuAction m41=new MenuAction();
        m41.setTitle("Selfie");
        
        specialMenu.getSubMenus().add(m15);
        specialMenu.getSubMenus().add(m16);
        specialMenu.getSubMenus().add(m17);
        specialMenu.getSubMenus().add(m18);
        specialMenu.getSubMenus().add(m41);
        //specialMenu.getSubMenus().add(m19);
        //specialMenu.getSubMenus().add(m20);
        
        MenuAction extrasMenu=new MenuAction();
        extrasMenu.setTitle("Extras");
        extrasMenu.setParent(true);
        
        MenuAction m21=new MenuAction();
        m21.setTitle("Printer Settings");
        
        MenuAction m22=new MenuAction();
        m22.setTitle("Print Disclaimer");
        
        MenuAction m23=new MenuAction();
        m23.setTitle("Print Scofflaw");
        
        MenuAction m24=new MenuAction();
        m24.setTitle("Send Tow Notify");
        
        MenuAction m25=new MenuAction();
        m25.setTitle("Send Support E-Mail");
        
        //Special Options
        /*String stickyComments="Sticky Comment";
		if(TPApplication.getInstance().stickyComments)
			stickyComments="Sticky Comment  \u2713";
		
		String stickyViolations="Sticky Violation";
		if(TPApplication.getInstance().stickyViolations)
			stickyViolations="Sticky Violation  \u2713";
        
		MenuAction m26=new MenuAction();
	    m26.setTitle(stickyComments);
	        
	    MenuAction m27=new MenuAction();
	    m27.setTitle(stickyViolations);*/
	    
	    MenuAction m28=new MenuAction();
	    if(TPApplication.getInstance().enableChalkAlerts){
        	m28.setTitle("Turn Off Chalk Alerts");
        }else{
        	m28.setTitle("Turn On Chalk Alerts");
        }
	    
	    MenuAction m29=new MenuAction();
	    if(TPApplication.getInstance().enableMobileNow){
        	m29.setTitle("Turn Off MobileNow");
        }else{
        	m29.setTitle("Turn On MobileNow");
        }
	     	    
	    MenuAction m30=new MenuAction();
	    MenuAction m37=new MenuAction();
	    MenuAction m38=new MenuAction();
	    MenuAction m39=new MenuAction();
	    
	    if(TPApplication.getInstance().enablePassportParking){
	    	m30.setTitle("Turn Off PassportParking"); 
        }else{
        	m30.setTitle("Turn On PassportParking"); 
        }  
	    
	    MenuAction m31=new MenuAction();
	    if(TPApplication.getInstance().enableParkMobile){
	    	m31.setTitle("Turn Off ParkMobile");
        }else{
        	m31.setTitle("Turn On ParkMobile");
        }
	   
	    MenuAction m32=new MenuAction();
	    if(TPApplication.getInstance().enablePayByPhone){
	    	m32.setTitle("Turn Off PayByPhone");
        }else{
        	m32.setTitle("Turn On PayByPhone");
        }
	    
	    
	    MenuAction m33=new MenuAction();
	    if(TPApplication.getInstance().enableDPT){
	    	m33.setTitle("Turn Off T2");
        }else{
        	m33.setTitle("Turn On T2");
        }
	    
	    MenuAction m35=new MenuAction();
	    if(TPApplication.getInstance().enableIPS){
	    	m35.setTitle("Turn Off IPS");
        }else{
        	m35.setTitle("Turn On IPS");
        }

		MenuAction m26=new MenuAction();
		if(TPApplication.getInstance().enableCale){
			m26.setTitle("Turn Off Cale");
		}else{
			m26.setTitle("Turn On Cale");
		}

		MenuAction m261=new MenuAction();
		if(TPApplication.getInstance().enableCale2){
			m261.setTitle("Turn Off Cale2");
		}else{
			m261.setTitle("Turn On Cale2");
		}

		MenuAction m27=new MenuAction();
		if(TPApplication.getInstance().enableParkeon){
			m27.setTitle("Turn Off Parkeon");
		}else{
			m27.setTitle("Turn On Parkeon");
		}
		MenuAction m36=new MenuAction();
		if(TPApplication.getInstance().enableSamtrans){
			m36.setTitle("Turn Off Samtrans");
		}else{
			m36.setTitle("Turn On Samtrans");
		}

		MenuAction m391=new MenuAction();
		if(TPApplication.getInstance().enablePassportParking2){
			m391.setTitle("Turn Off PP2");
		}else{
			m391.setTitle("Turn On PP2");
		}

		MenuAction m392=new MenuAction();
		if(TPApplication.getInstance().enableCubtrac){
			m392.setTitle("Turn Off Curbtrac");
		}else{
			m392.setTitle("Turn On Curbtrac");
		}

		MenuAction m393=new MenuAction();
		if(TPApplication.getInstance().enableOffStreet){
			m393.setTitle("Turn Off OffStreet");
		}else{
			m393.setTitle("Turn On OffStreet");
		}

		MenuAction m394=new MenuAction();
		if(TPApplication.getInstance().enableCurbsense){
			m394.setTitle("Turn Off CurbAlert");
		}else{
			m394.setTitle("Turn On CurbAlert");
		}

	    extrasMenu.getSubMenus().add(m21);
	    extrasMenu.getSubMenus().add(m22);
	    extrasMenu.getSubMenus().add(m23);
	    extrasMenu.getSubMenus().add(m24);
	    extrasMenu.getSubMenus().add(m25);


	    extrasMenu.getSubMenus().add(m28);
	    if(Feature.isFeatureAllowed(Feature.MOBILE_NOW)){
	    	extrasMenu.getSubMenus().add(m29);
		}
		if(Feature.isFeatureAllowed(Feature.CALE)){
			extrasMenu.getSubMenus().add(m26);
		}
		if(Feature.isFeatureAllowed(Feature.CALE2)){
			extrasMenu.getSubMenus().add(m261);
		}
		if(Feature.isFeatureAllowed(Feature.PARKEON)){
			extrasMenu.getSubMenus().add(m27);
		}
		if(Feature.isFeatureAllowed(Feature.SAMTRANS)){
			extrasMenu.getSubMenus().add(m36);
		}if(Feature.isFeatureAllowed(Feature.PASSPORT_PARKING2)){
			extrasMenu.getSubMenus().add(m391);
		}if(Feature.isFeatureAllowed(Feature.PARK_CUBTRAC)){
			extrasMenu.getSubMenus().add(m392);
		}if(Feature.isFeatureAllowed(Feature.PARK_OFFSTREET)){
			extrasMenu.getSubMenus().add(m393);
		}if(Feature.isFeatureAllowed(Feature.PARK_CURBSENSE)){
			extrasMenu.getSubMenus().add(m394);
		}
	    
	    if(Feature.isFeatureAllowed(Feature.PASSPORT_PARKING)){
	    	if(Feature.isFeatureAllowed(Feature.DISABLE_PASSPORT_PARKING_SWITCH)){
	    		//TODO
	    	}else{
	    		extrasMenu.getSubMenus().add(m30);
	    	}
	    	
	    	if(TPApplication.getInstance().enablePassportParking){
	    		 if(TPApplication.getInstance().enableVinPassportParking){
	 	 	    	m37.setTitle("Turn Off Vin PassportP"); 
	 	 	     }else{ 
	 	         	m37.setTitle("Turn On Vin PassportP"); 
	 	         }
	    	
	    		 extrasMenu.getSubMenus().add(m37);
	        }
	    }
	    
	    if(Feature.isFeatureAllowed(Feature.PROGRESSIVE)){
	    	MenuAction progressive=new MenuAction();
	    	extrasMenu.getSubMenus().add(progressive);
	    	
	    	if(TPApplication.getInstance().enableProgressive){
	    		progressive.setTitle("Turn Off Progressive"); 
	 	 	}else{ 
	 	 		progressive.setTitle("Turn On Progressive"); 
	 	    }
	    }
	    
	    if(Feature.isFeatureAllowed(Feature.PARK_MOBILE)){
	    	extrasMenu.getSubMenus().add(m31);
		}
	    
	    if(Feature.isFeatureAllowed(Feature.PAYBY_PHONE)){
	    	extrasMenu.getSubMenus().add(m32);
		}
	    if(Feature.isFeatureAllowed(Feature.DIGITAL_PAYTECH)){
	    	extrasMenu.getSubMenus().add(m33);
		}
	    
	    if(Feature.isFeatureAllowed(Feature.IPS_GROUP)){
	    	extrasMenu.getSubMenus().add(m35);
		}
	    
	    extrasMenu.getSubMenus().add(m38);
        m38.setTitle("Brightness");
        
        MenuAction settings=new MenuAction();
	    settings.setTitle("Settings");
	    settings.setParent(true);
	    
	    MenuAction m34=new MenuAction();
	    m34.setTitle("User Settings");
	    
	   /* MenuAction m36=new MenuAction();
	    m36.setTitle("IPS SpaceGroup");*/
	     
	    settings.getSubMenus().add(m34); 
	  /*
	    if(Feature.isFeatureAllowed(Feature.IPS_GROUP)){
	    	settings.getSubMenus().add(m36);
		}*/
	    
	    menus.add(actionMenu);
        menus.add(adminMenu);
        menus.add(extrasMenu);
        menus.add(specialMenu);
        menus.add(settings);
        
		expandAdapter = new ExpandListAdapter(((MenuActivity)getActivity()).getContent(), menus);
		expandListview.setAdapter(expandAdapter);
		
		expandListview.expandGroup(0);		//Expand Action Menu
		//expandListview.expandGroup(1);	//Expand Admin Menu
		//expandListview.expandGroup(2);	//Expand Extras Menu
		//expandListview.expandGroup(3);	// Expand Special Menu
        
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
					((MenuActivity)getActivity()).getSlideoutHelper().close();
					((MenuActivity)getActivity()).callAction(actionItem);
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
