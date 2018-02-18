package com.example.apch9.takepizza.Model;

/**
 * Created by Marek on 2018-02-18.
 */

public class Order {
    private String Address, Date, Worth;

    public Order() {
    }

    public Order(String address, String date, String worth) {
        Address = address;
        Date = date;
        Worth = worth;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getWorth() {
        return Worth;
    }

    public void setWorth(String worth) {
        Worth = worth;
    }
}
