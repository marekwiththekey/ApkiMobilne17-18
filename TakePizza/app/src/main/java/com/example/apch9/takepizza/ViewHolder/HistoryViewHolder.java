package com.example.apch9.takepizza.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apch9.takepizza.R;

/**
 * Created by Marek on 2018-02-18.
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    public TextView historyDate, historyAddress, historyWorth;

    public HistoryViewHolder(View itemView) {
        super(itemView);

        historyDate = (TextView)itemView.findViewById(R.id.history_date);
        historyAddress = (TextView)itemView.findViewById(R.id.history_address);
        historyWorth = (TextView)itemView.findViewById(R.id.history_price);
    }
}
