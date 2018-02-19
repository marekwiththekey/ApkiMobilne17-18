package com.example.apch9.takepizza;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by apch9 on 22.11.2017.
 */

public class RegisterActivity extends StartActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private TextView tvCreateAccount;
    private DatabaseReference dbrDatabaseReference;
    private ProgressDialog pdProgress;

    public void onView(View view) {
        View onview = this.getCurrentFocus();
        if (onview != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onBack(View view) {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        dbrDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        pdProgress = new ProgressDialog(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        tvCreateAccount = (TextView) findViewById(R.id.tvCreateAccount);


        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });
    }

    private void startRegister() {
        if(auth.getCurrentUser() != null) auth.signOut();

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        SharedPreferences settings = getSharedPreferences("userLoginData", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.commit();

        if (password.length() < 6 || confirm.length() < 6) {
//            AlertDialog alertDialog = new AlertDialog.Builder(this).create();//            alertDialog.setTitle("Invalid password!");//            alertDialog.setMessage("Password and/or Confirm Password fields are invalid.\nValid password should be at least 6 characters long.");//            alertDialog.show();//            Toast toast = Toast.makeText(getApplicationContext(),"Invalid password: minimum length is 6 characters long.",Toast.LENGTH_LONG);//            toast.setGravity(Gravity.TOP|Gravity.LEFT, 100, 500);//            toast.show();            etPassword.setError("Password too short");
            etConfirmPassword.setError("Password too short");
        } else {

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm)) {

                pdProgress.setMessage("Creating a new account ...");
                pdProgress.show();
                if (TextUtils.equals(password, confirm)) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String user_id = auth.getCurrentUser().getUid();

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
