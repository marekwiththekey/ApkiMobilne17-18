package com.example.apch9.takepizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by apch9 on 12.02.2018.
 */

public class StartActivity extends MainActivity {

    protected FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_start);
    }

    public void onSignIn(View view) {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    public void onSignUp(View view) {
        Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
        //Jeśli zalogowany
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
         */
    }
}

