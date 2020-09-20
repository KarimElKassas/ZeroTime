package com.zerotime.zerotime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zerotime.zerotime.Pojos.HistoryPojo;
import com.zerotime.zerotime.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<HistoryPojo> historyList = new ArrayList<>();
    Context context;

    public HistoryAdapter(List<HistoryPojo> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryPojo history = historyList.get(position);
        holder.description.setText(history.getDescription());
        holder.date.setText(history.getDate());
        holder.price.setText(history.getPrice());
        holder.size.setText(history.getSize());
        holder.Raddress.setText(history.getRaddress());
        holder.Rname.setText(history.getRname());
        holder.Rphone1.setText(history.getRphone1());
        holder.Rphone2.setText(history.getRphone2());

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void setList(List<HistoryPojo> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView description, date, price, size, Raddress, Rname, Rphone1, Rphone2;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.history_description);
            date = itemView.findViewById(R.id.history_date);
            price = itemView.findViewById(R.id.history_price);
            size = itemView.findViewById(R.id.history_size);
            Raddress = itemView.findViewById(R.id.history_RAddress);
            Rname = itemView.findViewById(R.id.history_RName);
            Rphone1 = itemView.findViewById(R.id.history_RPhone1);
            Rphone2 = itemView.findViewById(R.id.history_RPhone2);
        }
    }
}