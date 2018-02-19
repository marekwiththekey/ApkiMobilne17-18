package com.example.apch9.takepizza;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.example.apch9.takepizza.Fragment.CartListFragment;
import com.example.apch9.takepizza.Fragment.HistoryListFragment;
import com.example.apch9.takepizza.Fragment.RestaurantListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected FirebaseAuth auth;
    protected FirebaseUser currentUser;
    NavigationView navigationView;

    ImageButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.Fragment fragment = new CartListFragment();
                if (fragment != null) {
                    fab.setVisibility(View.INVISIBLE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment, fragment);
                    ft.commit();
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        auth = FirebaseAuth.getInstance();

        currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user) {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        if (user == null) {
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
        }
        else nav_Menu.findItem(R.id.nav_logout).setVisible(true);
    }


    @Override
    public void onBackPressed() {

        getFragmentManager().popBackStack();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        dispalySelectedScreen(id);

        return true;
    }

    private void dispalySelectedScreen(int id) {
        for (android.support.v4.app.Fragment f : getSupportFragmentManager().getFragments()) {

            getSupportFragmentManager().beginTransaction().remove(f).commit();
        }
        android.support.v4.app.Fragment fragment = null;
        fab.setVisibility(View.VISIBLE);
        switch (id) {
            case R.id.nav_logout:
                if (auth.getCurrentUser() != null) {
                    auth.signOut();
                    fab.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.nav_menu:
                fragment = new RestaurantListFragment();
                break;
            case R.id.nav_bask:
                fragment = new CartListFragment();
                break;
            case R.id.nav_history:
                fragment = new HistoryListFragment();
                break;
            case R.id.nav_home:
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    public void onBack(View view) {
        finish();
    }

    public void onView(View view) {
        View onview = this.getCurrentFocus();
        if (onview != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }

}
