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


import com.zerotime.zerotime.Moderator.ModeratorClerksHistory;
import com.zerotime.zerotime.Moderator.Pojos.Clerks;
import com.zerotime.zerotime.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ClerkAdapter extends RecyclerView.Adapter<ClerkAdapter.ClerkViewHolder> {
    private ArrayList<Clerks> clerkList;
    private Context context;


    public ClerkAdapter(ArrayList<Clerks> clerksList, Context context) {
        this.clerkList = clerksList;
        this.context = context;
    }


    @NonNull
    @Override
    public ClerkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClerkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clerk, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClerkViewHolder holder, int position) {

        final Clerks clerks = clerkList.get(position);
        holder.ClerkName.setText(clerks.getName());
        holder.ClerkAddress.setText(clerks.getAddress());
        holder.ClerkPhone1.setText(clerks.getPhone1());
        holder.ClerkPhone2.setText(clerks.getPhone2());
        holder.ClerkAge.setText(String.valueOf(clerks.getAge()));
        holder.ClerkVehiclel.setText(clerks.getHasVehicle());


        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.expandableView.getVisibility() == View.GONE) {
                    //TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                    holder.expandableView.setVisibility(View.VISIBLE);
                    holder.arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    //TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                    holder.expandableView.setVisibility(View.GONE);
                    holder.arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
            }
        });


        holder.callClerk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + clerks.getPhone1()));
                context.startActivity(intent);
            }
        });

        holder.viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ModeratorClerksHistory.class);
                i.putExtra("ClerkPhone",holder.ClerkPhone1.getText().toString());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return clerkList.size();
    }

    public void removeClerk(int position){
        clerkList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreClerk(Clerks clerk, int position){
        clerkList.add(position,clerk);
        notifyItemInserted(position);
    }

    public static class ClerkViewHolder extends RecyclerView.ViewHolder {
        private TextView ClerkName, ClerkPhone1, ClerkPhone2, ClerkAge, ClerkAddress, ClerkVehiclel;
        private Button viewOrders, arrowBtn;
        private ImageView callClerk;
        public ConstraintLayout view_foreground, view_background,expandableView;
        CardView cardView;

        public ClerkViewHolder(@NonNull View itemView) {
            super(itemView);
            view_foreground = itemView.findViewById(R.id.clerks_view_foreground);
            view_background = itemView.findViewById(R.id.clerks_view_background);
            expandableView = itemView.findViewById(R.id.expandableView);

            cardView = itemView.findViewById(R.id.cardView);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);

            ClerkName = itemView.findViewById(R.id.ViewClerk_clerkName_txt);
            ClerkPhone1 = itemView.findViewById(R.id.ViewClerk_phone1_txt);
            ClerkPhone2 = itemView.findViewById(R.id.ViewClerk_phone2_txt);
            ClerkAge = itemView.findViewById(R.id.ViewClerk_age_txt);
            ClerkAddress = itemView.findViewById(R.id.ViewClerk_address_txt);
            ClerkVehiclel = itemView.findViewById(R.id.ViewClerk_hasVehicle_txt);
            viewOrders = itemView.findViewById(R.id.ViewClerk_ViewOrders_btn);
            callClerk = itemView.findViewById(R.id.ViewClerk_callClerk_img);

        }
    }
}