package com.example.apch9.takepizza.Fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apch9.takepizza.Interface.ItemClickListener;
import com.example.apch9.takepizza.Model.Product;
import com.example.apch9.takepizza.R;
import com.example.apch9.takepizza.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter;
    ProductDetailsFragment fragment;
    String restaurantId = "";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_product_list, container, false);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Product");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            restaurantId = bundle.getString("restaurantId");
        }

        if(!restaurantId.isEmpty() && restaurantId != null){
            getProductList(restaurantId);
        }
        return view;

    }

    public void onClose(View view) { //finish();
        getFragmentManager().popBackStack();
    }


    private void getProductList(final String resId) {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class, R.layout.product_item,
                ProductViewHolder.class, dbRef.orderByChild("Rest_id").equalTo(resId)) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                Double price = Double.parseDouble(model.getPrice()) * Double.parseDouble(model.getPromotion());
                viewHolder.productName.setText(model.getName());
                viewHolder.productPrice.setText(String.format(price.toString(), 2f) + " PLN");
                Picasso.with(getActivity().getBaseContext()).load(model.getImage()).into(viewHolder.productImage);
                //viewHolder.productDesc.setText(model.getDesc());
                System.out.println("RestaurantID = " + resId);

                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if (getFragmentManager().getBackStackEntryCount() == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putString("productId", firebaseRecyclerAdapter.getRef(position).getKey());
                            fragment = new ProductDetailsFragment();
                            fragment.setArguments(bundle);
                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
                        }
                        //Intent productDetails = new Intent(ProductList.this, ProductDetails.class);
                        //productDetails.putExtra("productId",firebaseRecyclerAdapter.getRef(position).getKey());
                        //startActivity(productDetails);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}
