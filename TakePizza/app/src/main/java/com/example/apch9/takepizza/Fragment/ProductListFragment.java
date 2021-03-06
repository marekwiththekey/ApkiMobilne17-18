package com.example.apch9.takepizza.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

    public android.support.v7.widget.AppCompatButton close;
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

        close = (android.support.v7.widget.AppCompatButton) view.findViewById(R.id.ret);
        close.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            restaurantId = bundle.getString("restaurantId");
        }

        if (!restaurantId.isEmpty() && restaurantId != null) {
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
                viewHolder.productPrice.setText(price.toString() + " PLN");
                Picasso.with(getActivity().getBaseContext()).load(model.getImage()).into(viewHolder.productImage);


                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", firebaseRecyclerAdapter.getRef(position).getKey());
                        fragment = new ProductDetailsFragment();
                        fragment.setArguments(bundle);
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();


                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}
