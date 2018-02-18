package com.example.apch9.takepizza.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.apch9.takepizza.MainActivity;
import com.example.apch9.takepizza.Model.CartItem;
import com.example.apch9.takepizza.Model.Product;
import com.example.apch9.takepizza.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.HashMap;

public class ProductDetailsFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference dbRef;
    String productId = "";

    public ImageButton close;
    public TextView pName,desc,price;
    public ImageView pImage;
    public ElegantNumberButton amount;
    public Button addToOrder;
    private Integer productAmount = 1;
    private View view;
    private Double constPrice;
    private String promotionString;
    private Integer elementsCount = 0;
    private Double currentWorth = 0.0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_product_details, container, false);

        getElementsCount();
        getCurrentWorth();
        pName = (TextView)view.findViewById(R.id.product_details_name);
        desc = (TextView)view.findViewById(R.id.product_details_desc);
        price = (TextView)view.findViewById(R.id.product_details_price);
        pImage = (ImageView)view.findViewById(R.id.product_details_image);
        amount = (ElegantNumberButton)view.findViewById(R.id.product_amount);
        amount.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double newPrice = constPrice * Double.parseDouble(amount.getNumber());
                String sPrice = newPrice.toString();
                int dotIndex = sPrice.indexOf(".");
                sPrice = sPrice.substring(0, dotIndex+2);
                price.setText(sPrice + " PLN");
            }
        });

        close = (ImageButton) view.findViewById(R.id.close);
        close.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        addToOrder = (Button)view.findViewById(R.id.addToOrder);
        addToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToCart();
                String msg = amount.getNumber() + "x" + pName.getText() + " to Cart";
                Toast.makeText(getContext(), "Added " + msg, Toast.LENGTH_LONG).show();
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

    private void addItemToCart() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String newItem = "";

        elementsCount+=1;
        if(elementsCount < 10) newItem = "0" + elementsCount.toString();
        else newItem = elementsCount.toString();

        CartItem newCartItem = new CartItem(amount.getNumber(), pName.getText().toString(), price.getText().toString(), promotionString );

        dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child("CartItem").child(newItem);
        dbRef.setValue(newCartItem);
        DatabaseReference newdbRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child("Elements");
        newdbRef.setValue(elementsCount.toString());

        String sPrice = newCartItem.getPrice();
        sPrice = sPrice.substring(0, sPrice.length()-4);
        currentWorth += Double.parseDouble(sPrice);

        newdbRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child("Worth");
        newdbRef.setValue(currentWorth.toString());
    }

    private void getElementsCount() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child("Elements");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null ) elementsCount = Integer.parseInt(dataSnapshot.getValue().toString());
                else elementsCount = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getCurrentWorth(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child("Worth");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null ) currentWorth = Double.parseDouble(dataSnapshot.getValue().toString());
                else currentWorth = 0.0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

                constPrice = Double.parseDouble(product.getPrice()) * Double.parseDouble(product.getPromotion());
                promotionString = product.getPromotion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
