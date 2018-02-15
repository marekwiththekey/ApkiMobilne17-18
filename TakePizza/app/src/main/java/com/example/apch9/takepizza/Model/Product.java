package com.example.apch9.takepizza.Model;

/**
 * Created by Marek on 2018-02-15.
 */

public class Product {
    private String Desc, Image, Name,Price,Promotion,Rest_id;

    public Product() {
    }

    public Product(String desc, String image, String name, String price, String promotion, String rest_id) {
        Desc = desc;
        Image = image;
        Name = name;
        Price = price;
        Promotion = promotion;
        Rest_id = rest_id;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPromotion() {
        return Promotion;
    }

    public void setPromotion(String promotion) {
        Promotion = promotion;
    }

    public String getRest_id() {
        return Rest_id;
    }

    public void setRest_id(String rest_id) {
        Rest_id = rest_id;
    }
}
