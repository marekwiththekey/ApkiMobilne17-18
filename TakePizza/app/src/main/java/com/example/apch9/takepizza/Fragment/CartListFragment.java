package com.example.apch9.takepizza.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apch9.takepizza.Common.PaypalConfig;
import com.example.apch9.takepizza.Interface.ItemClickListener;
import com.example.apch9.takepizza.Model.CartItem;
import com.example.apch9.takepizza.Model.Restaurant;
import com.example.apch9.takepizza.R;
import com.example.apch9.takepizza.ViewHolder.CartViewHolder;
import com.example.apch9.takepizza.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by apch9 on 17.02.2018.
 */

public class CartListFragment extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<CartItem, CartViewHolder> firebaseRecyclerAdapter;
    private View view;
    private String userId, deliveryAddress;
    private Double totalPrice = 0.0;
    private Integer elements = 0;

    public TextView toPay;
    public Button pay, clear;

    static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PaypalConfig.CLIENT_ID);
    private static final int PAYPAL_REQUEST_CODE=9999;
    private static final SimpleDateFormat orderDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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
        getTotalPrice(view);
        getOrderElementsCount();

        pay = (Button)view.findViewById(R.id.proceedToPayment);
        clear = (Button)view.findViewById(R.id.clearCart);

        Intent intent = new Intent(getContext(),PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter delivery address");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                input.setHint("Street name & number, City");
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deliveryAddress = input.getText().toString();
                        newPayment(totalPrice);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCartList(true);
            }
        });

        return view;
    }

    private void newPayment(Double totalPrice) {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(totalPrice)),"PLN","FeedApp Payment",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    private void getCartList() {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child("CartItem");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CartItem, CartViewHolder>(CartItem.class, R.layout.cart_item,
                CartViewHolder.class, dbRef) {
            @Override
            protected void populateViewHolder(CartViewHolder viewHolder, CartItem model, int position) {
                if(model != null) {
                    viewHolder.itemName.setText(model.getName());
                    viewHolder.itemPrice.setText(model.getPrice());
                    clear.setEnabled(true);
                    pay.setEnabled(true);
                    final CartItem local = model;
                }
                else {
                    clear.setEnabled(false);
                    pay.setEnabled(false);
                }


            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void getTotalPrice(View view) {
        toPay = (TextView)view.findViewById(R.id.toPay);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child("Worth");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    String sPrice = dataSnapshot.getValue(String.class);
                    totalPrice = Double.parseDouble(sPrice);
                    toPay.setText("To pay: " + totalPrice.toString() + " PLN");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void clearCartList(boolean withMsg) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId);
        dbRef.setValue(null);
        if(withMsg)Toast.makeText(getContext(), "Item(s) have been removed", Toast.LENGTH_LONG).show();
        getCartList();
        toPay.setText("To pay:  0.0 PLN");
    }

    public void getOrderElementsCount(){
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Order").child(userId).child("Elements");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) elements = 0;
                else elements = Integer.parseInt(dataSnapshot.getValue().toString());
                System.out.println("liczba elementow w zamowieniach: " + elements);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==PAYPAL_REQUEST_CODE)
        {
            if(resultCode== getActivity().RESULT_OK)
            {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null)
                {
                    try
                    {
                        String  paymentDetails =    confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(paymentDetails);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    elements += 1;
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Order").child(userId).child("Elements");
                    database.setValue(elements.toString());

                    String newOrderId;
                    if(elements<10) newOrderId = "0" + elements.toString();
                    else newOrderId = elements.toString();

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String orderDate = orderDateFormat.format(timestamp).toString();
                    database = FirebaseDatabase.getInstance().getReference().child("Order").child(userId).child("OrderItem").child(newOrderId);
                    database.child("Worth").setValue(totalPrice.toString());
                    database.child("Date").setValue(orderDate);
                    database.child("Address").setValue(deliveryAddress);
                    Toast.makeText(getContext(), "Payment have been sucessful", Toast.LENGTH_LONG).show();
                    clearCartList(false);
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED )
            {
                Toast.makeText(getContext(),"Payments Failed",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
                Toast.makeText(getContext(), "Payments Invalid", Toast.LENGTH_SHORT).show();

        }
    }
}
