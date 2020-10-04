package com.zerotime.zerotime.Moderator;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Moderator.Adapters.ClerkHistoryAdapter;
import com.zerotime.zerotime.Moderator.Pojos.Clerk_History;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.ModeratorActivityClerksHistoryBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.ArrayList;

public class ModeratorClerksHistory extends AppCompatActivity {
    private ModeratorActivityClerksHistoryBinding binding;

    private DatabaseReference ClerksRef;
    ArrayList<Clerk_History> clerksList;
    private ClerkHistoryAdapter adapter;
    String ClerkPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModeratorActivityClerksHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();
        ClerkPhone = getIntent().getStringExtra("ClerkPhone");
        binding.recyclerClerksHistory.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.recyclerClerksHistory.setLayoutManager(mLayoutManager);
        binding.recyclerClerksHistory.setItemAnimator(new DefaultItemAnimator());

        ClerksRef = FirebaseDatabase.getInstance().getReference().child("DeliveredOrders");
        clerksList = new ArrayList<>();

        ClerksRef.orderByChild("ClerkPhone1").equalTo(ClerkPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    binding.recyclerClerksHistory.setVisibility(View.GONE);
                    binding.clerkHistoryNoResult.setVisibility(View.VISIBLE);
                }
                if (snapshot.exists()) {
                    if (!snapshot.hasChildren()) {
                        binding.recyclerClerksHistory.setVisibility(View.GONE);
                        binding.clerkHistoryNoResult.setVisibility(View.VISIBLE);
                    } else {
                        binding.recyclerClerksHistory.setVisibility(View.VISIBLE);
                        binding.clerkHistoryNoResult.setVisibility(View.GONE);

                        clerksList.clear();
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                            String name = (String) dataSnapshot1.child("ClerkName").getValue();
                            String description = (String) dataSnapshot1.child("OrderDescription").getValue();
                            String date = (String) dataSnapshot1.child("OrderDate").getValue();
                            String phone1 = (String) dataSnapshot1.child("ClerkPhone1").getValue();
                            String address = (String) dataSnapshot1.child("ReceiverAddress").getValue();
                            String price = (String) (dataSnapshot1.child("OrderPrice").getValue());
                            String size = (String) (dataSnapshot1.child("OrderSize").getValue());

                            Clerk_History clerks = new Clerk_History();
                            clerks.setClerkName(name);
                            clerks.setDescription(description);
                            clerks.setDate(date);
                            clerks.setReceiverPhone(phone1);
                            clerks.setSize(size);
                            clerks.setPrice(price);
                            clerks.setReceiverAddress(address);


                            clerksList.add(clerks);


                        }
                        adapter = new ClerkHistoryAdapter(clerksList, ModeratorClerksHistory.this);
                        binding.recyclerClerksHistory.setAdapter(adapter);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void layoutAnimation(RecyclerView recyclerView) {

        Context context = recyclerView.getContext();
        LayoutAnimationController animationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_right);
        recyclerView.setLayoutAnimation(animationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();


    }

    private void checkInternetConnection() {
        myBroadCast broadCast = new myBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ModeratorClerksHistory.this, ModeratorHome.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}