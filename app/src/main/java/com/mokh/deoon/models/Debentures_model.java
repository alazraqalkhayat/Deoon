package com.mokh.deoon.models;

public class Debentures_model {

    int debenture_id,money_paied;
    String employee_name;
    String date;

    public int getDebenture_id() {
        return debenture_id;
    }

    public void setDebenture_id(int debenture_id) {
        this.debenture_id = debenture_id;
    }

    public int getMoney_paied() {
        return money_paied;
    }

    public void setMoney_paied(int money_paied) {
        this.money_paied = money_paied;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    String customer_name;

    public Debentures_model(int debenture_id, int money_paied, String employee_name, String date, String customer_name) {
        this.debenture_id = debenture_id;
        this.money_paied = money_paied;
        this.employee_name = employee_name;
        this.date = date;
        this.customer_name = customer_name;
    }
}
