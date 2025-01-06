package com.ticketpro.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ArrayOfValidParkingData", strict = false)
public class ArrayOfValidParkingData {

    @ElementList(name = "ValidParkingData",inline = true,required = false,empty = true)
    private List<ValidParkingData1> ValidParkingData;


    ArrayOfValidParkingData() {
    }

    public ArrayOfValidParkingData(List<ValidParkingData1> ValidParkingData) {
        this.ValidParkingData = ValidParkingData;
    }

    public List<ValidParkingData1> getValidParkingData() {
        return ValidParkingData;
    }

    public void setValidParkingData(List<ValidParkingData1> ValidParkingData) {
        this.ValidParkingData = ValidParkingData;
    }


}