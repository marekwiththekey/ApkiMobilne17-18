package com.example.apch9.takepizza;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by apch9 on 22.11.2017.
 */

public class RegisterActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bBack = (ImageButton)findViewById(R.id.bBack);
    }
}
