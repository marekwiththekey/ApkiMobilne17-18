package com.example.apch9.takepizza.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apch9.takepizza.R;

/**
 * Created by Marek on 2018-02-17.
 */

public class CartViewHolder extends RecyclerView.ViewHolder {

    public TextView itemName, itemPrice, totalPrice;

    public CartViewHolder(View itemView) {
        super(itemView);

        itemName = (TextView)itemView.findViewById(R.id.cart_item_name);
        itemPrice = (TextView)itemView.findViewById(R.id.cart_price);
        totalPrice = (TextView)itemView.findViewById(R.id.toPay);
    }
}
