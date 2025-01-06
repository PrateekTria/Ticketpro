package com.ticketpro.vendors.passport2_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PP2PlateVehicle {
    @SerializedName("vehicle_plate")
    @Expose
    private String vehiclePlate;
    @SerializedName("vehicle_state")
    @Expose
    private String vehicleState;

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public String getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(String vehicleState) {
        this.vehicleState = vehicleState;
    }
}
