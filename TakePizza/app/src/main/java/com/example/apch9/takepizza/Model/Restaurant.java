package com.example.apch9.takepizza.Model;

/**
 * Created by Marek on 2018-02-15.
 */

public class Restaurant {

    private String Name, City, Address;

    public Restaurant() {
    }

    public Restaurant(String name, String city, String address) {
        Name = name;
        City = city;
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
