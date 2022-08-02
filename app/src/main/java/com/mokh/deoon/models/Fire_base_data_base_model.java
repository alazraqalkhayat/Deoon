package com.mokh.deoon.models;

public class Fire_base_data_base_model {

    String area,date,owner_name,owner_phone_number,password,user_name;

    public Fire_base_data_base_model(String area, String date, String owner_name, String owner_phone_number, String password, String user_name) {
        this.area = area;
        this.date = date;
        this.owner_name = owner_name;
        this.owner_phone_number = owner_phone_number;
        this.password = password;
        this.user_name = user_name;
    }



    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_phone_number() {
        return owner_phone_number;
    }

    public void setOwner_phone_number(String owner_phone_number) {
        this.owner_phone_number = owner_phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
