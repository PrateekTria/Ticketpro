package com.ticketpro.gpslibrary;

public class ADLocation {
    public double lat;
    public double longi;
    public String address;
    public String city;
    public String state;
    public String country;
    public String pincode;
    public String streetNumber;

    @Override
    public String toString() {
        return "ADLocation{" +
                "lat=" + lat +
                ", longt=" + longi +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pincode='" + pincode + '\'' +
                '}';
    }
}
