package com.zerotime.zerotime.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Secretary.Adapters.FollowingOrderAdapter;
import com.zerotime.zerotime.Secretary.FollowingTheOrderState;
import com.zerotime.zerotime.Secretary.Pojos.OrderState;
import com.zerotime.zerotime.databinding.FragmentFollowMyOrdersBinding;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class FollowMyOrdersFragment extends Fragment {

    private FragmentFollowMyOrdersBinding binding;
    private FollowingOrderAdapter adapter;

    private DatabaseReference ordersRef;
    private ArrayList<OrderState> ordersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFollowMyOrdersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE);
        String userPhone = prefs.getString("isLogged","");

        binding.followingMyOrdersFragmentRecycler.setHasFixedSize(true);
        binding.followingMyOrdersFragmentRecycler.setLayoutManager(new LinearLayoutManager(container.getContext()));

        ordersList = new ArrayList<>();

        ordersRef = FirebaseDatabase.getInstance().getReference("PendingOrders");
        Query query = ordersRef.orderByChild("UserPrimaryPhone").equalTo(userPhone);

        query.addValueEventListener(new ValueEventListener() {
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
                adapter = new FollowingOrderAdapter(ordersList, container.getContext());
                binding.followingMyOrdersFragmentRecycler.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}