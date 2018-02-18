package com.example.apch9.takepizza.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.apch9.takepizza.Model.Order;
import com.example.apch9.takepizza.R;
import com.example.apch9.takepizza.ViewHolder.HistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Marek on 2018-02-18.
 */

public class HistoryListFragment extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<Order, HistoryViewHolder> firebaseRecyclerAdapter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_history_list, container, false);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Order").child(userId).child("OrderItem");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_history);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        getHistoryList();
        return view;

    }

    private void getHistoryList() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Order, HistoryViewHolder>(Order.class, R.layout.history_item,
                HistoryViewHolder.class, dbRef) {
            @Override
            protected void populateViewHolder(HistoryViewHolder viewHolder, Order model, int position) {

               viewHolder.historyDate.setText(model.getDate());
               viewHolder.historyAddress.setText(model.getAddress());
               viewHolder.historyWorth.setText(model.getWorth() + " PLN");
               final Order local = model;

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
