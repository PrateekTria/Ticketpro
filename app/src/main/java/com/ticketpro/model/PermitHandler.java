package com.ticketpro.model;

public interface PermitHandler {
    public void permitResponse(Permit permit);
    public void permitResponseFail();

    public void permitVinResponse(Permit permit,String Vin);
}
