package com.example.apch9.takepizza;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by apch9 on 22.11.2017.
 */

public class RegisterActivity extends MainActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private TextView tvCreateAccount;

    private FirebaseAuth mAuth;
    private DatabaseReference dbrDatabaseReference;

    private ProgressDialog pdProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        dbrDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        pdProgress = new ProgressDialog(this);

        bBack = (ImageButton)findViewById(R.id.bBack);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);
        tvCreateAccount = (TextView)findViewById(R.id.tvCreateAccount);


        tvCreateAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startRegister();
            }
        });



    }

    private void startRegister() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (password.length() < 6 || confirm.length() < 6) {
//            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//            alertDialog.setTitle("Invalid password!");
//            alertDialog.setMessage("Password and/or Confirm Password fields are invalid.\nValid password should be at least 6 characters long.");
//            alertDialog.show();
            Toast toast = Toast.makeText(getApplicationContext(),"Invalid password: minimum length is 6 characters long.",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.LEFT, 100, 500);
            toast.show();
        } else {

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm)) {

                pdProgress.setMessage("Creating a new account ...");
                pdProgress.show();
                if (TextUtils.equals(password, confirm)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String user_id = mAuth.getCurrentUser().getUid();

                                dbrDatabaseReference.child(user_id);

                                pdProgress.dismiss();

                                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(loginIntent);

                            }
                        }
                    });
                }
            }

        }
    }
}
