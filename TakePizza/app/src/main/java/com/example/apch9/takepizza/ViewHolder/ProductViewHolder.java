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

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName,productPrice,productDesc;
    public ImageView productImage;

    private ItemClickListener itemClickListener;

    public ProductViewHolder(View itemView) {
        super(itemView);

        productName = (TextView)itemView.findViewById(R.id.product_name);
        productPrice = (TextView)itemView.findViewById(R.id.product_price);
        productImage = (ImageView)itemView.findViewById(R.id.product_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
