package com.zerotime.zerotime.Moderator.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zerotime.zerotime.Moderator.Pojos.Clerks;
import com.zerotime.zerotime.Moderator.Pojos.Complaint_Pojo;
import com.zerotime.zerotime.Pojos.HistoryPojo;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Room.Model.Complaint;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ClerkViewHolder> {
    private List<Complaint> complaints=new ArrayList<>();
    private Context context;

/*
    public ComplaintAdapter(ArrayList<Complaint> complaints, Context context) {
        this.complaints = complaints;
        this.context = context;
    }*/


    @NonNull
    @Override
    public ClerkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClerkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClerkViewHolder holder, int position) {

        final Complaint complaint = complaints.get(position);
        holder.name.setText(complaint.getName());
        holder.phone.setText(complaint.getUserPhone());
        holder.complaint.setText(complaint.getUserComplaint());
        holder.date.setText(complaint.getDate());


    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    public void setList(List<Complaint> complaintsList) {
        this.complaints =  complaintsList;
        notifyDataSetChanged();
    }

    public class ClerkViewHolder extends RecyclerView.ViewHolder {
        private TextView name, phone, complaint, date;

        public ClerkViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ComplaintName);
            phone = itemView.findViewById(R.id.ComplaintPhone);
            complaint = itemView.findViewById(R.id.Complaint);
            date = itemView.findViewById(R.id.ComplaintDate);

        }
    }
}