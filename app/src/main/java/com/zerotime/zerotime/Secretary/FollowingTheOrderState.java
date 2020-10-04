package com.zerotime.zerotime.Secretary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Fragments.ProfileFragment;
import com.zerotime.zerotime.Moderator.ModeratorHome;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.Pojos.OrderState;
import com.zerotime.zerotime.Secretary.Adapters.FollowingOrderAdapter;
import com.zerotime.zerotime.databinding.SecretaryActivityFollowingTheOrderStateBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.ArrayList;
import java.util.Objects;

public class FollowingTheOrderState extends AppCompatActivity {
    private SecretaryActivityFollowingTheOrderStateBinding binding;
    private FollowingOrderAdapter adapter;

    private ArrayList<OrderState> ordersList;
    //-------------------------------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SharedPreferences prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        String userType = prefs.getString("UserType", "");

        if (userType != null) {
            switch (userType) {
                case "Secretary":
                    Intent i = new Intent(FollowingTheOrderState.this, SecretaryHome.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    break;

                case "Moderator":
                    Intent ii = new Intent(FollowingTheOrderState.this, ModeratorHome.class);
                    startActivity(ii);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    break;

                case "User":
                    Intent intent = new Intent(FollowingTheOrderState.this, ProfileFragment.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    break;

            }
        }


    }


    private void layoutAnimation(RecyclerView recyclerView) {

        Context context = recyclerView.getContext();
        LayoutAnimationController animationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_right);
        recyclerView.setLayoutAnimation(animationController);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SecretaryActivityFollowingTheOrderStateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();


        Drawable progressDrawable = binding.secretaryFollowingOrdersProgress.getIndeterminateDrawable().mutate();
        progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        binding.secretaryFollowingOrdersProgress.setProgressDrawable(progressDrawable);

        binding.OrderStateRecycler.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.OrderStateRecycler.setLayoutManager(mLayoutManager);
        binding.OrderStateRecycler.setItemAnimator(new DefaultItemAnimator());
        ordersList = new ArrayList<>();



        DatabaseReference orderStateRef = FirebaseDatabase.getInstance().getReference("PendingOrders");


        orderStateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if(snapshot.hasChildren()){

                        ordersList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            binding.OrderStateRecycler.setVisibility(View.VISIBLE);
                            binding.secretaryFollowingOrdersProgress.setVisibility(View.GONE);

                            String orderDescription = dataSnapshot.child("OrderDescription").getValue(String.class);
                            String orderDate = dataSnapshot.child("OrderDate").getValue(String.class);
                            String orderPrice = dataSnapshot.child("OrderPrice").getValue(String.class);
                            String receiverName = dataSnapshot.child("ReceiverName").getValue(String.class);
                            String OrderState = dataSnapshot.child("OrderState").getValue(String.class);
                            String receiverAddress = dataSnapshot.child("ReceiverAddress").getValue(String.class);
                            String receiverPrimaryPhone = dataSnapshot.child("ReceiverPrimaryPhone").getValue(String.class);
                            String userPrimaryPhone = dataSnapshot.child("UserPrimaryPhone").getValue(String.class);


                            OrderState orderState = new OrderState();

                            orderState.setDescription(orderDescription);
                            orderState.setDate(orderDate);
                            orderState.setPrice(orderPrice);
                            orderState.setName(receiverName);
                            orderState.setAddress(receiverAddress);
                            orderState.setPhone(receiverPrimaryPhone);
                            orderState.setUser_Phone(userPrimaryPhone);

                            assert OrderState != null;
                            if (OrderState.equals("لم يتم الاستلام"))
                                orderState.setCurrentState(0);
                            if (OrderState.equals("تم الاستلام"))
                                orderState.setCurrentState(1);
                            if (OrderState.equals("جارى التوصيل"))
                                orderState.setCurrentState(2);
                            if (OrderState.equals("تم التوصيل"))
                                orderState.setCurrentState(3);


                            ordersList.add(orderState);


                        }
                        adapter = new FollowingOrderAdapter(ordersList, FollowingTheOrderState.this);
                        binding.OrderStateRecycler.setAdapter(adapter);

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkInternetConnection() {
        myBroadCast broadCast = new myBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }

}