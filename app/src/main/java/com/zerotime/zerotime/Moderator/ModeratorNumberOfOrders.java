package com.zerotime.zerotime.Moderator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
        binding.recyclerOrdersNumber.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerOrdersNumber.setLayoutManager(linearLayoutManager);
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
}