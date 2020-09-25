package com.zerotime.zerotime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Adapters.HistoryAdapter;
import com.zerotime.zerotime.Pojos.HistoryPojo;
import com.zerotime.zerotime.databinding.UserActivityHistoryBinding;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    private UserActivityHistoryBinding binding;
    DatabaseReference reference;
    SharedPreferences preferences;
    ArrayList<HistoryPojo> historyPojos;
    HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.historyRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.historyRecycler.setLayoutManager(linearLayoutManager);

        preferences = getSharedPreferences("UserState", MODE_PRIVATE);
        historyPojos=new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("DeliveredOrders");
        Query query = reference.orderByChild("UserPrimaryPhone").equalTo(preferences.getString("isLogged", "null"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                            String description = dataSnapshot1.child("OrderDescription").getValue(String.class);
                            String date = dataSnapshot1.child("OrderDate").getValue(String.class);
                            String price = dataSnapshot1.child("OrderPrice").getValue(String.class);
                            String size = dataSnapshot1.child("OrderSize").getValue(String.class);
                            String Raddress = dataSnapshot1.child("ReceiverName").getValue(String.class);
                            String Rname = dataSnapshot1.child("ReceiverAddress").getValue(String.class);
                            String Rphone1 = dataSnapshot1.child("ReceiverPrimaryPhone").getValue(String.class);
                            String Rphone2 = dataSnapshot1.child("ReceiverSecondaryPhone").getValue(String.class);

                            HistoryPojo historyPojo = new HistoryPojo();
                            historyPojo.setDescription(description);
                            historyPojo.setDate(date);
                            historyPojo.setSize(size);
                            historyPojo.setPrice(price);
                            historyPojo.setRaddress(Raddress);
                            historyPojo.setRname(Rname);
                            historyPojo.setRphone1(Rphone1);
                            historyPojo.setRphone2(Rphone2);

                            historyPojos.add(historyPojo);


                        }

                    }
                    adapter = new HistoryAdapter(historyPojos, History.this);
                    binding.historyRecycler.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}