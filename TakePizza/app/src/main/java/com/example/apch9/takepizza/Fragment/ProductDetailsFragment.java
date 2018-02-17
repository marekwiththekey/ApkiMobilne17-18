package com.example.apch9.takepizza.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.apch9.takepizza.Model.Product;
import com.example.apch9.takepizza.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductDetailsFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference dbRef;
    String productId = "";

    public TextView pName,desc,price;
    public ImageView pImage;
    public ElegantNumberButton amount;
    private Integer productAmount = 1;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_product_details, container, false);

        pName = (TextView)view.findViewById(R.id.product_details_name);
        desc = (TextView)view.findViewById(R.id.product_details_desc);
        price = (TextView)view.findViewById(R.id.product_details_price);
        pImage = (ImageView)view.findViewById(R.id.product_details_image);
        amount = (ElegantNumberButton)view.findViewById(R.id.product_amount);
        amount.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
// zmiana widoku na textview ilosc*cena*promocja
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productId = bundle.getString("productId");
        }

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Product/" + productId);

        if(!productId.isEmpty() && productId != null){
            getProductDetails();
        }
        return view;
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
                Picasso.with(getActivity().getBaseContext()).load(product.getImage()).into(pImage);
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

    public void addToOrder(View view) {
        //Toast.makeText(ProductDetailsFragment.this, "Added selected food to the Order", Toast.LENGTH_SHORT).show();
        //finish();
    }
}
