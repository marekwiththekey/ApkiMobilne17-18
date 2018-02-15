package com.example.apch9.takepizza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.apch9.takepizza.Interface.ItemClickListener;
import com.example.apch9.takepizza.Model.Product;
import com.example.apch9.takepizza.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter;

    String restaurantId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Product");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent() != null)
            restaurantId = getIntent().getStringExtra("restaurantId");
        if(!restaurantId.isEmpty() && restaurantId != null){
            getProductList(restaurantId);
        }

    }

    private void getProductList(final String resId) {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,R.layout.product_item,
                ProductViewHolder.class, dbRef.orderByChild("Rest_id").equalTo(resId)) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.productName.setText(model.getName());
                viewHolder.productPrice.setText(model.getPrice());
                viewHolder.productDesc.setText(model.getDesc());
                System.out.println("RestaurantID = " + resId);

                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        System.out.println("Restaurant id = " + local.getRest_id());
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
