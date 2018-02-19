package com.example.apch9.takepizza;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends StartActivity {

    private EditText inputEmail, inputPassword;
    private TextView btnLogin;

    public void onView(View view) {
        View onview = this.getCurrentFocus();
        if (onview != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onBack(View view) {
        finish();
    }

    public void onReg(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (TextView) findViewById(R.id.btn_login);

        SharedPreferences settings = getSharedPreferences("userLoginData", 0);
        if(settings.toString() != "null"){
            inputEmail.setText(settings.getString("Email",""));
            inputPassword.setText(settings.getString("Password",""));
            inputEmail.setCursorVisible(false);
            inputPassword.setCursorVisible(false);
            inputEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputEmail.setCursorVisible(true);
                }
            });
            inputPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputPassword.setCursorVisible(true);
                }
            });
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();

                final String password = inputPassword.getText().toString();
                SharedPreferences settings = getSharedPreferences("userLoginData", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Email", email);
                editor.putString("Password", password);
                editor.commit();

                if (!validateSignIn()) {
                    return;
                }


                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    // SignIn Validation
    private boolean validateSignIn() {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Required.");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        String password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Required.");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        return valid;
    }

}