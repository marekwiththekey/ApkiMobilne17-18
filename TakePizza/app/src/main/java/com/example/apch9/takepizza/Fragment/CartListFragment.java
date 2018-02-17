package com.example.apch9.takepizza.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apch9.takepizza.Interface.ItemClickListener;
import com.example.apch9.takepizza.Model.CartItem;
import com.example.apch9.takepizza.Model.Restaurant;
import com.example.apch9.takepizza.R;
import com.example.apch9.takepizza.ViewHolder.CartViewHolder;
import com.example.apch9.takepizza.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by apch9 on 17.02.2018.
 */

public class CartListFragment extends android.support.v4.app.Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<CartItem, CartViewHolder> firebaseRecyclerAdapter;
    android.support.v4.app.Fragment fragment;
    private View view;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cart_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getCartList();

        return view;
    }

    private void getCartList() {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child("CartItem");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CartItem, CartViewHolder>(CartItem.class, R.layout.cart_item,
                CartViewHolder.class, dbRef) {
            @Override
            protected void populateViewHolder(CartViewHolder viewHolder, CartItem model, int position) {
                viewHolder.itemName.setText(model.getName());
                viewHolder.itemPrice.setText(model.getPrice());

                final CartItem local = model;

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
