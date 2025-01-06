package com.ticketpro.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class StringUtil {
	public static String Left(String text, int length) {
		if (length > text.length())
			return text;

		try {
			return text.substring(0, length);
		} catch (Exception e) {
			return text;
		}
	}

	public static String Right(String text, int length) {
		if (length > text.length())
			return text;

		try {
			return text.substring(text.length() - length);
		} catch (Exception e) {
			return text;
		}
	}

	public static String Mid(String text, int start, int numberOfChars) {
		if (numberOfChars > text.length() || start <= 0)
			return "";

		try {
			return text.substring(start - 1, start + numberOfChars - 1);
		} catch (Exception e) {
			return text;
		}
	}

	public static String Mid(String text, int start) {
		if (start <= 0)
			return "";

		try {
			return text.substring(start - 1);
		} catch (Exception e) {
			return text;
		}
	}

	public static boolean isAlphaNumeric(String s) {
		String pattern = "^[a-zA-Z0-9][a-zA-Z0-9 ]+$";
		if (s.matches(pattern)) {
			return true;
		}

		return false;
	}

	public static String getDisplayString(String str) {
		if (str == null || str.equalsIgnoreCase("null")){
			return "";
		}
		
		return str.toUpperCase();
	}
	
	
	public static String escapeQuotes(String str) {
		if (str == null) 
			return null;

		return str.replaceAll("\'", "\\\'").replace("\"", "\\\"");
	}
	
	
	public static boolean isEmpty(String str) {
		if (str == null || str.equalsIgnoreCase("null")){
			return true;
		}

		return str.isEmpty();
	}

	public static String substringBetween(String str, String open, String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		
		int start = str.indexOf(open);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start + open.length(), end);
			}
		}
		
		return null;
	}

	public static int[] getIntArray(String values){
		if(StringUtil.isEmpty(values)){
			values="";
		}
		
		ArrayList<Integer> intValues = new ArrayList<Integer>();
		StringTokenizer token = new StringTokenizer(values, ",");
		while(token.hasMoreTokens()){
			try{
				intValues.add(Integer.valueOf(token.nextToken()));
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		
		int array[] = new int[intValues.size()];
		for(int i=0;i<intValues.size(); i++){
			array[i] = intValues.get(i);
		}
		
		return array;
	}
	
	public static String trim(String str){
		if(str!=null){
			str = str.trim();
		}
		
		return str;
	}
	
	public static boolean hasValidChars(String value){
		if(value.contains("I")||value.contains("O")||value.contains("Q"))
		return false;
		else
			return true;
	}
	
	public static boolean hasValidCharPlate(String plate){
		
		if(Character.toString(plate.charAt(1)).equals("I")||Character.toString(plate.charAt(1)).equals("O")||Character.toString(plate.charAt(1)).equals("Q") || Character.toString(plate.charAt(3)).equals("I")||Character.toString(plate.charAt(3)).equals("O")||Character.toString(plate.charAt(3)).equals("Q"))
		return false;
		return true;
	}
}