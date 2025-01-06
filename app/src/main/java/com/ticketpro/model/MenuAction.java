package com.ticketpro.model;

import java.util.ArrayList;

public class MenuAction {
	
	private String title;
	private String menuIcon;
	private boolean isParent;
	private ArrayList<MenuAction> subMenus;
	
	public MenuAction(){
		subMenus=new ArrayList<MenuAction>();
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMenuIcon() {
		return menuIcon;
	}
	
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}
	
	public boolean isParent() {
		return isParent;
	}
	
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public ArrayList<MenuAction> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(ArrayList<MenuAction> subMenus) {
		this.subMenus = subMenus;
	}
	
}
