package com.taxidriver.doalf.taxidriver;

public class EntityRequest {
    public EntityCustomer customer;
    public double latitude;
    public double longitude;

    public EntityRequest(EntityCustomer customer, double latitude, double longitude) {
        this.customer = customer;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
