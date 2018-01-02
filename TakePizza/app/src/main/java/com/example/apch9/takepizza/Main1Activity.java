package com.example.apch9.takepizza;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;

public class Main1Activity extends AppCompatActivity {

    protected Button bLog;
    protected ImageButton bBack;
    protected ImageButton bSkip;
    protected ImageButton bSignUp;
    protected Button bSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        bLog = (Button)findViewById(R.id.bLog);
        setHost();

        bLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main1Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setHost(){
        TabHost host = (TabHost)findViewById(R.id.tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Kontakt");
        spec.setContent(R.id.Pizzas);
        spec.setIndicator("Pizzas");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Zwiedzanie");
        spec.setContent(R.id.Drinks);
        spec.setIndicator("Drinks");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Zwiedzanie");
        spec.setContent(R.id.Others);
        spec.setIndicator("Others");
        host.addTab(spec);
    }

    public void onMain(View view) {
        finish();
    }

    public void onView(View view) {
        View onview = this.getCurrentFocus();
        if (onview != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }
}
