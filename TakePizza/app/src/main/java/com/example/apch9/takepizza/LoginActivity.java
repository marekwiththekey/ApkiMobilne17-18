package com.example.apch9.takepizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends StartActivity {

    private EditText inputEmail, inputPassword;
    //private FirebaseAuth auth;
    //private ProgressBar progressBar;
    private TextView btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

 /*       if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }*/

        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = (TextView) findViewById(R.id.btn_login);
        //btnReset = (Button) findViewById(R.id.btn_reset_password);

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


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        /*        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });*/

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

                /*if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                //progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                //progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
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