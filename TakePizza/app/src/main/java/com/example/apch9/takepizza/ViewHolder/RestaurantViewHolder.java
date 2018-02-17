package com.example.apch9.takepizza.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apch9.takepizza.Interface.ItemClickListener;
import com.example.apch9.takepizza.R;

/**
 * Created by Marek on 2018-02-15.
 */

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView restaurantName,restaurantCity,restaurantAddress;
    public ImageView restaurantImage;

    private ItemClickListener itemClickListener;


    public RestaurantViewHolder(View itemView) {
        super(itemView);

        restaurantName = (TextView)itemView.findViewById(R.id.restaurant_name);
        restaurantCity = (TextView)itemView.findViewById(R.id.restaurant_city);
        restaurantAddress = (TextView)itemView.findViewById(R.id.restaurant_address);
        //restaurantImage = (ImageView)itemView.findViewById(R.id.restaurant_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }

    public TextView getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(TextView restaurantName) {
        this.restaurantName = restaurantName;
    }

    public ImageView getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(ImageView restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
