package com.example.apch9.takepizza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apch9.takepizza.Model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbRef;
    String productId = "";

    public TextView pName,desc,price,amount;
    public Button addButton, subButton;
    private Integer productAmount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        pName = (TextView)findViewById(R.id.product_details_name);
        desc = (TextView)findViewById(R.id.product_details_desc);
        price = (TextView)findViewById(R.id.product_details_price);
        amount = (TextView)findViewById(R.id.product_amount);

        if(getIntent() != null) {
            productId = getIntent().getStringExtra("productId");
        }

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Product/" + productId);

        if(!productId.isEmpty() && productId != null){
            getProductDetails();
        }

    }

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmmunt(Integer productAmount) {
        this.productAmount = productAmount;
    }

    private void getProductDetails() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> value = new HashMap<String, String>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    value.put(child.getKey(), child.getValue(String.class));
                }
                Product product = new Product();
                product.setName(value.get("Name"));
                product.setDesc(value.get("Desc"));
                product.setImage(value.get("Image"));
                product.setPrice(value.get("Price"));
                product.setPromotion(value.get("Promotion"));

                pName.setText(product.getName());
                desc.setText(product.getDesc());
                Double promotion = Double.parseDouble(product.getPromotion());
                Double dprice = Double.parseDouble(product.getPrice()) * promotion;
                price.setText(dprice.toString() + " PLN");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void subAmount(View view) {
        if(productAmount > 0) {
            productAmount -= 1;
            amount.setText(productAmount.toString());
        }
    }

    public void addAmount(View view) {
        productAmount += 1;
        amount.setText(productAmount.toString());
    }

    public void addToOrder(View view) {
        Toast.makeText(ProductDetails.this, "Added selected food to the Order", Toast.LENGTH_SHORT).show();
    }
}
