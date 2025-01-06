package com.ticketpro.vendors.dpt.StallInfoService;


public class WS_Enums {

    public enum SoapProtocolVersion{
        	Default(0),
        	Soap11(1),
        	Soap12(2);
        
        private int code;
        
        SoapProtocolVersion(int code){
            this.code = code;
        }
        
        public int getCode(){
            return code;
        }
        
        public static SoapProtocolVersion fromString(String str)
        {
            if (str.equals("Default"))
                return Default;
            if (str.equals("Soap11"))
                return Soap11;
            if (str.equals("Soap12"))
                return Soap12;
            return null;
        }
    }
    public enum Bystatus{
        	All(0),
        	Valid(1),
        	Expired(2);
        
        private int code;
        
        Bystatus(int code){
            this.code = code;
        }
        
        public int getCode(){
            return code;
        }
        
        public static Bystatus fromString(String str)
        {
            if (str.equals("All"))
                return All;
            if (str.equals("Valid"))
                return Valid;
            if (str.equals("Expired"))
                return Expired;
            return null;
        }
    }
}
