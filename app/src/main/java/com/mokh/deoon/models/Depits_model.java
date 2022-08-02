package com.mokh.deoon.models;

public class Depits_model {

    int depit_id,deserved_amount;
    String employee_name;
    String customer_name;
    String description;
    String hand;
    String date;

    public int getDepit_id() {
        return depit_id;
    }


    public int getDeserved_amount() {
        return deserved_amount;
    }


    public String getEmployee_name() {
        return employee_name;
    }


    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHand() {
        return hand;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Depits_model(int depit_id, int deserved_amount, String employee_name, String customer_name, String description, String hand, String date) {
        this.depit_id = depit_id;
        this.deserved_amount = deserved_amount;
        this.employee_name = employee_name;
        this.customer_name = customer_name;
        this.description = description;
        this.hand = hand;
        this.date = date;
    }



}
