package com.mokh.deoon.models;

public class All_customers_model {

    String customer_name,customer_phone_number;

    public All_customers_model(String customer_name, String customer_phone_number) {
        this.customer_name = customer_name;
        this.customer_phone_number = customer_phone_number;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCustomer_phone_number() {
        return customer_phone_number;
    }
}
