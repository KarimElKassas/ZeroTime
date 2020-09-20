package com.zerotime.zerotime.Moderator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Adapters.HistoryAdapter;
import com.zerotime.zerotime.History;
import com.zerotime.zerotime.Moderator.Adapters.NumberOrdersAdapter;
import com.zerotime.zerotime.Moderator.Pojos.OrdersNumber;
import com.zerotime.zerotime.Pojos.HistoryPojo;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.ActivityMDisplayUserDataBinding;
import com.zerotime.zerotime.databinding.ActivityModeratorNumberOfOrdersBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.ArrayList;
import java.util.Objects;

public class ModeratorNumberOfOrders extends AppCompatActivity {
    DatabaseReference reference;
    SharedPreferences preferences;
    ArrayList<OrdersNumber> ordersNumbers;
    NumberOrdersAdapter adapter;


    private ActivityModeratorNumberOfOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeratorNumberOfOrdersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();
        binding.recyclerOrdersNumber.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerOrdersNumber.setLayoutManager(linearLayoutManager);
        binding.recyclerOrdersNumber.setItemAnimator(new DefaultItemAnimator());

        preferences = getSharedPreferences("UserState", MODE_PRIVATE);
        ordersNumbers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("OrdersCount");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                            String userName = dataSnapshot1.child("UserName").getValue(String.class);
                            long ordersCount = Objects.requireNonNull(dataSnapshot1.child("OrdersCount").getValue(Long.class));

                            OrdersNumber ordersNumber = new OrdersNumber();
                            ordersNumber.setName(userName);
                            ordersNumber.setOrdersNumber(ordersCount);
                            ordersNumber.setPhone(dataSnapshot1.getKey());

                            ordersNumbers.add(ordersNumber);


                        }
                    }
                    adapter = new NumberOrdersAdapter(ordersNumbers, ModeratorNumberOfOrders.this);
                    binding.recyclerOrdersNumber.setAdapter(adapter);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ModeratorNumberOfOrders.this,ModeratorHome.class);
        startActivity(i);
        finish();
    }

    private void checkInternetConnection(){
        myBroadCast broadCast=new myBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast,intentFilter);

    }
}