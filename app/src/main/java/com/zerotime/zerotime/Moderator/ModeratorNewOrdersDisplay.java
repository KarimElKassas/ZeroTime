package com.zerotime.zerotime.Moderator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Moderator.Adapters.ClerckAdapter;
import com.zerotime.zerotime.Moderator.Adapters.NewOrdersAdapter;
import com.zerotime.zerotime.Moderator.Pojos.NewOrders;
import com.zerotime.zerotime.databinding.ActivityModeratorNewOrdersDisplayBinding;
import com.zerotime.zerotime.myBroadCast;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;


public class ModeratorNewOrdersDisplay extends AppCompatActivity {

    private ActivityModeratorNewOrdersDisplayBinding binding;
    private NewOrdersAdapter adapter;
    private DatabaseReference pendingOrderRef;
    ArrayList<NewOrders> ordersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeratorNewOrdersDisplayBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();
        binding.displayOrdersRecycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.displayOrdersRecycler.setLayoutManager(mLayoutManager);

        ordersList = new ArrayList<>();

        pendingOrderRef = FirebaseDatabase.getInstance().getReference("PendingOrders");
        pendingOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String orderDescription = dataSnapshot.child("OrderDescription").getValue(String.class);
                    String orderDate = dataSnapshot.child("OrderDate").getValue(String.class);
                    String orderNotes = dataSnapshot.child("ArrivalNotes").getValue(String.class);
                    String orderPrice = dataSnapshot.child("OrderPrice").getValue(String.class);
                    String orderState = dataSnapshot.child("OrderState").getValue(String.class);
                    String orderSize = dataSnapshot.child("OrderSize").getValue(String.class);
                    String userPhone = dataSnapshot.child("UserPrimaryPhone").getValue(String.class);
                    String receiverName = dataSnapshot.child("ReceiverName").getValue(String.class);
                    String receiverAddress = dataSnapshot.child("ReceiverAddress").getValue(String.class);
                    String receiverPrimaryPhone = dataSnapshot.child("ReceiverPrimaryPhone").getValue(String.class);
                    String receiverSecondaryPhone = dataSnapshot.child("ReceiverSecondaryPhone").getValue(String.class);

                    NewOrders newOrders = new NewOrders();
                    newOrders.setOrderDescription(orderDescription);
                    newOrders.setOrderDate(orderDate);
                    newOrders.setOrderNotes(orderNotes);
                    newOrders.setOrderPrice(orderPrice);
                    newOrders.setOrderState(orderState);
                    newOrders.setOrderSize(orderSize);
                    newOrders.setUserPrimaryPhone(userPhone);
                    newOrders.setReceiverName(receiverName);
                    newOrders.setReceiverAddress(receiverAddress);
                    newOrders.setReceiverPrimaryPhone(receiverPrimaryPhone);
                    newOrders.setReceiverSecondaryPhone(receiverSecondaryPhone);

                    ordersList.add(newOrders);

                }
                adapter = new NewOrdersAdapter(ordersList, ModeratorNewOrdersDisplay.this);
                binding.displayOrdersRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void checkInternetConnection(){
        myBroadCast broadCast=new myBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast,intentFilter);

    }
}