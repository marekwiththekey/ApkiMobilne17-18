package com.example.apch9.takepizza.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apch9.takepizza.Interface.ItemClickListener;
import com.example.apch9.takepizza.Model.Restaurant;
import com.example.apch9.takepizza.R;
import com.example.apch9.takepizza.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurantListFragment extends android.support.v4.app.Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> firebaseRecyclerAdapter;
    android.support.v4.app.Fragment fragment;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_restaurant_list, container, false);


        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Restaurant");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_restaurant);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getRestaurantList();

        return view;
    }

    private void getRestaurantList() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(Restaurant.class, R.layout.restaurant_item,
                RestaurantViewHolder.class, dbRef) {
            @Override
            protected void populateViewHolder(RestaurantViewHolder viewHolder, Restaurant model, int position) {
                viewHolder.restaurantName.setText(model.getName());
                viewHolder.restaurantCity.setText(model.getCity());
                viewHolder.restaurantAddress.setText(model.getAddress());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Bundle bundle = new Bundle();
                        bundle.putString("restaurantId", firebaseRecyclerAdapter.getRef(position).getKey());
                        fragment = new ProductListFragment();
                        fragment.setArguments(bundle);
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.product_list_fragment, fragment).addToBackStack(null).commit();

                    }
                });

                final Restaurant local = model;

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
