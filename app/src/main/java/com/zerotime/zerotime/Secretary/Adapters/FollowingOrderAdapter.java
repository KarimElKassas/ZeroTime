package com.zerotime.zerotime.Secretary.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.anton46.stepsview.StepsView;
import com.zerotime.zerotime.Secretary.Pojos.OrderState;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.FollowingOrderSettings;
import com.zerotime.zerotime.Secretary.Secretary_UserData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class FollowingOrderAdapter extends RecyclerView.Adapter<FollowingOrderAdapter.FollowingOrderViewHolder> {
    private List<OrderState> ordersList;

    private Context context;
    String[] steps = {"   لم يتم \n الإستلام", "     تم \n الإستلام", "   جاري \n التوصيل", "     تم \n التوصيل"};
    Dialog clerksDialog;

    public FollowingOrderAdapter(List<OrderState> ordersList, Context context) {
        this.ordersList = ordersList;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        clerksDialog = new Dialog(context);
        clerksDialog.setContentView(R.layout.dialog_clercks);

        return new FollowingOrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following_order, parent, false));
    }

    @SuppressLint("PrivateResource")
    @Override
    public void onBindViewHolder(@NonNull FollowingOrderViewHolder holder, int position) {
        SharedPreferences prefs = context.getSharedPreferences("UserState", MODE_PRIVATE);
        final OrderState orderState = ordersList.get(position);
        String userType = prefs.getString("UserType","");
        if (userType != null){
            switch (userType) {
                case "Secretary":
                    holder.settings.setVisibility(View.VISIBLE);
                    holder.userData.setVisibility(View.VISIBLE);
                    break;
                case "Moderator":

                    holder.settings.setVisibility(View.INVISIBLE);
                    holder.userData.setVisibility(View.VISIBLE);
                    break;
                case "User":
                    holder.settings.setVisibility(View.INVISIBLE);
                    holder.userData.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        holder.name.setText("اسم المستلم : "+orderState.getName());
        holder.address.setText("عنوان المستلم : "+orderState.getAddress());
        holder.price.setText("سعر الطلب : "+orderState.getPrice());
        holder.date.setText(orderState.getDate());
        holder.description.setText("وصف الطلب : "+orderState.getDescription());
        holder.phone.setText("رقم هاتف المستلم : "+orderState.getPhone());


        holder.userData.setOnClickListener(view -> {
            Intent intent = new Intent(context, Secretary_UserData.class);
            intent.putExtra("UserPhone", orderState.getUser_Phone());
            context.startActivity(intent);

        });



        holder.settings.setOnClickListener(view -> {

            Intent intent = new Intent(context, FollowingOrderSettings.class);
            intent.putExtra("OrderDate",orderState.getDate());
            context.startActivity(intent);
            ((Activity)context).finish();

        });


        holder.stepsView.setLabels(steps)

                .setBarColorIndicator(context.getResources().getColor(R.color.material_blue_grey_800))
                .setProgressColorIndicator(context.getResources().getColor(R.color.orange))
                .setLabelColorIndicator(context.getResources().getColor(R.color.orange))
                .setCompletedPosition(orderState.getCurrentState())
                .drawView();


    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class FollowingOrderViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, address, description, price, date;
        StepsView stepsView;
        ImageView userData, settings;
        CardView cardView;

        public FollowingOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.followingOrder_name);
            phone = itemView.findViewById(R.id.followingOrder_phoneNumber);
            address = itemView.findViewById(R.id.followingOrder_address);
            description = itemView.findViewById(R.id.followingOrder_orderDescription);
            price = itemView.findViewById(R.id.followingOrder_price);
            date = itemView.findViewById(R.id.followingOrder_date);
            stepsView = itemView.findViewById(R.id.stepsView);
            userData = itemView.findViewById(R.id.FollowingOrder_UserData);
            cardView = itemView.findViewById(R.id.FollowingCard);
            settings = itemView.findViewById(R.id.FollowingOrder_Settings);
        }
    }
}