package com.zerotime.zerotime.Moderator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zerotime.zerotime.Moderator.Pojos.Clerk_History;
import com.zerotime.zerotime.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClerkHistoryAdapter extends RecyclerView.Adapter<ClerkHistoryAdapter.ClerkViewHolder> {
    private List<Clerk_History> clerk_histories=new ArrayList<>();
    private Context context;

    public ClerkHistoryAdapter(List<Clerk_History> clerk_histories, Context context) {
        this.clerk_histories = clerk_histories;
        this.context = context;
    }

    @NonNull
    @Override
    public ClerkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClerkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clerk_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClerkViewHolder holder, int position) {

        final Clerk_History history = clerk_histories.get(position);
        holder.name.setText(history.getClerkName());
        holder.description.setText(history.getDescription());
        holder.price.setText(history.getPrice());
        holder.date.setText(history.getDate());
        holder.size.setText(history.getSize());
        holder.ReceiverPhone.setText(history.getReceiverPhone());
        holder.ReceiverAddress.setText(history.getReceiverAddress());



    }

    @Override
    public int getItemCount() {
        return clerk_histories.size();
    }

    public void setList(List<Clerk_History> histories) {
        this.clerk_histories =  histories;
        notifyDataSetChanged();
    }

    public class ClerkViewHolder extends RecyclerView.ViewHolder {
        private TextView name, description, price,size,ReceiverAddress,ReceiverPhone, date;

        public ClerkViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Clerks_history_name);
            description = itemView.findViewById(R.id.Clerks_history_description);
            date = itemView.findViewById(R.id.Clerks_history_date);
            price = itemView.findViewById(R.id.Clerks_history_price);
            size = itemView.findViewById(R.id.Clerks_history_size);
            ReceiverAddress = itemView.findViewById(R.id.Clerks_history_Receiver_Address);
            ReceiverPhone = itemView.findViewById(R.id.Clerks_history_Receiver_Phone);

        }
    }
}