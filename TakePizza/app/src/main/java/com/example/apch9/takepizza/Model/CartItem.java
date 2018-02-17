package com.example.apch9.takepizza.Model;

/**
 * Created by Marek on 2018-02-17.
 */

public class CartItem {
    private String Amount,Name,Price,Promotion;

    public CartItem() {
    }

    public CartItem(String amount, String name, String price, String promotion) {
        Amount = amount;
        Name = name;
        Price = price;
        Promotion = promotion;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
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
}
