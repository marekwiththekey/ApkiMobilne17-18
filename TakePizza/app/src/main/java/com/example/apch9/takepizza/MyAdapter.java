package com.example.apch9.takepizza;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by apch9 on 13.02.2018.
 */

public class MyAdapter extends RecyclerView.Adapter {
    // źródło danych
    private ArrayList<FoodItem> mArticles = new ArrayList<>();

    // obiekt listy artykułów
    private RecyclerView mRecyclerView;

    // implementacja wzorca ViewHolder
    // każdy obiekt tej klasy przechowuje odniesienie do layoutu elementu listy
    // dzięki temu wywołujemy findViewById() tylko raz dla każdego elementu
    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;

        public MyViewHolder(View pItem) {
            super(pItem);
            mTitle = (TextView) pItem.findViewById(R.id.food_name);
        }
    }

    // konstruktor adaptera
    public MyAdapter(ArrayList<FoodItem> pArticles, RecyclerView pRecyclerView){
        mArticles = pArticles;
        mRecyclerView = pRecyclerView;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        // tworzymy layout artykułu oraz obiekt ViewHoldera
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.food_item, viewGroup, false);

        // dla elementu listy ustawiamy obiekt OnClickListener,
        // który usunie element z listy po kliknięciu na niego
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // odnajdujemy indeks klikniętego elementu
                int positionToDelete = mRecyclerView.getChildAdapterPosition(v);
                // usuwamy element ze źródła danych
                mArticles.remove(positionToDelete);
                // poniższa metoda w animowany sposób usunie element z listy
                notifyItemRemoved(positionToDelete);
            }
        });

        // tworzymy i zwracamy obiekt ViewHolder
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        // uzupełniamy layout artykułu
        FoodItem article = mArticles.get(i);
        ((MyViewHolder) viewHolder).mTitle.setText(article.getTitle());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
