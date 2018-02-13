package com.example.apch9.takepizza;

import java.util.Random;

/**
 * Created by apch9 on 13.02.2018.
 */

public class FoodItem {
    private String mTitle;

    // statyczne tablice, na podstawie których zostaną uzupełnione obiekty artykułów
    private static String[] sTitles = {"Lorem ipsum dolor sit amet",
            "Etiam sit", "Cras vel lorem",
            "Cras suscipit, urna at aliquam rhoncus",
            "Phasellus congue lacus eget neque",
            "Phasellus pharetra nulla ac diam"};

    public FoodItem() {
        Random random = new Random();

        // ustawiamy losowy tytuł i treść artykułu
        mTitle = sTitles[random.nextInt(sTitles.length)];
    }

    public String getTitle() {
        return mTitle;
    }
}