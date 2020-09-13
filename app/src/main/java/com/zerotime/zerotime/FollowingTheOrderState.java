package com.zerotime.zerotime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.databinding.ActivityFollowingTheOrderStateBinding;

import java.util.ArrayList;

public class FollowingTheOrderState extends AppCompatActivity {
    private ActivityFollowingTheOrderStateBinding binding;
    private FollowingOrderAdapter adapter;
    private DatabaseReference OrderStateRef;

    private ArrayList<OrderState> ordersList;

   /* private static final String[] states = {"تم الإستلام", "جارى التوصيل", "تم التوصيل"};

    ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(FollowingTheOrderState.this,
            android.R.layout.simple_spinner_item, states);*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowingTheOrderStateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.OrderStateRecycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.OrderStateRecycler.setLayoutManager(mLayoutManager);

        ordersList = new ArrayList<>();

        OrderStateRef = FirebaseDatabase.getInstance().getReference("PendingOrders");


        OrderStateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}