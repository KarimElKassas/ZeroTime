package com.zerotime.zerotime.Moderator.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.zerotime.zerotime.Moderator.M_DisplayUserData;
import com.zerotime.zerotime.Moderator.Pojos.NewOrders;
import com.zerotime.zerotime.R;

import java.util.ArrayList;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.NewOrderViewHolder> {
    private ArrayList<NewOrders> ordersList;
    private Context context;

    public NewOrdersAdapter(ArrayList<NewOrders> ordersList, Context context) {
        this.ordersList = ordersList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewOrdersAdapter.NewOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewOrdersAdapter.NewOrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull NewOrdersAdapter.NewOrderViewHolder holder, int position) {

        final NewOrders orders = ordersList.get(position);
        holder.orderDescription.setText(orders.getOrderDescription());
        holder.orderNotes.setText(orders.getOrderNotes());
        holder.orderDate.setText(orders.getOrderDate());
        holder.orderPrice.setText(orders.getOrderPrice());
        holder.orderState.setText(orders.getOrderState());
        holder.receiverName.setText(orders.getReceiverName());
        holder.receiverAddress.setText(orders.getReceiverAddress());
        holder.userPrimaryPhone.setText(orders.getUserPrimaryPhone());
        holder.receiverPrimaryPhone.setText(orders.getReceiverPrimaryPhone());
        holder.receiverSecondaryPhone.setText(orders.getReceiverSecondaryPhone());

        //Click On Card Listener
        holder.cardView.setOnClickListener(view -> {
            if (holder.expandableView.getVisibility()==View.GONE){
                //TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                holder.expandableView.setVisibility(View.VISIBLE);
            } else {
                //TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                holder.expandableView.setVisibility(View.GONE);
            }
        });
        //CardView On Long Click
        holder.cardView.setOnLongClickListener(view -> {
            Intent intent = new Intent(context, M_DisplayUserData.class);
            intent.putExtra("UserPhone",orders.getUserPrimaryPhone());
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            //Toast.makeText(context.getApplicationContext(),"On Long Click Listener Clicked",Toast.LENGTH_SHORT).show();
            return true;
        });
        //Order Received Button
        holder.orderReceived.setOnClickListener(view -> {
            Toast.makeText(context.getApplicationContext(),"Order Received Click",Toast.LENGTH_SHORT).show();
        });
        //Order In Progress Button
        holder.orderInProgress.setOnClickListener(view -> {
            Toast.makeText(context.getApplicationContext(),"Order In Progress Click",Toast.LENGTH_SHORT).show();
        });
        //Order Delivered Button
        holder.orderDelivered.setOnClickListener(view -> {
            Toast.makeText(context.getApplicationContext(),"Order Delivered Click",Toast.LENGTH_SHORT).show();
        });


    }
    @Override
    public int getItemCount() {
        return ordersList.size();
    }


    public class NewOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderDescription, orderDate, orderNotes, orderPrice, orderState, receiverName, receiverAddress, userPrimaryPhone, receiverPrimaryPhone, receiverSecondaryPhone;
        private Button orderReceived,orderInProgress,orderDelivered;
        ConstraintLayout expandableView;
        CardView cardView;
        public NewOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            expandableView=itemView.findViewById(R.id.display_orders_expandableView);
            cardView=itemView.findViewById(R.id.display_orders_order_card);
            orderDescription = itemView.findViewById(R.id.display_orders_order_description_value_text_view);
            orderNotes = itemView.findViewById(R.id.display_orders_order_notes_value_text_view);
            orderDate = itemView.findViewById(R.id.display_orders_order_date_value_text_view);
            orderPrice = itemView.findViewById(R.id.display_orders_order_price_value_text_view);
            orderState = itemView.findViewById(R.id.display_orders_order_state_value_text_view);
            receiverName = itemView.findViewById(R.id.display_orders_receiver_name_value_text_view);
            receiverAddress = itemView.findViewById(R.id.display_orders_receiver_address_value_text_view);
            userPrimaryPhone = itemView.findViewById(R.id.display_orders_user_primary_phone_value_text_view);
            receiverPrimaryPhone = itemView.findViewById(R.id.display_orders_receiver_primary_phone_value_text_view);
            receiverSecondaryPhone = itemView.findViewById(R.id.display_orders_receiver_secondary_phone_value_text_view);
            orderReceived = itemView.findViewById(R.id.display_orders_received_btn);
            orderInProgress = itemView.findViewById(R.id.display_orders_in_progress_btn);
            orderDelivered = itemView.findViewById(R.id.display_orders_delivered_btn);
        }
    }
}
