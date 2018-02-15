package com.example.apch9.takepizza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.apch9.takepizza.Model.Restaurant;
import com.example.apch9.takepizza.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurantList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Restaurant");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_restaurant);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getRestaurantList();
    }

    private void getRestaurantList() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(Restaurant.class,R.layout.restaurant_item,
                RestaurantViewHolder.class, dbRef) {
            @Override
            protected void populateViewHolder(RestaurantViewHolder viewHolder, Restaurant model, int position) {
                viewHolder.restaurantName.setText(model.getName());
                viewHolder.restaurantCity.setText(model.getCity());
                viewHolder.restaurantAddress.setText(model.getAddress());

                final Restaurant local = model;

            }
        };
        Log.d("TAG",""+firebaseRecyclerAdapter.getItemCount());
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
