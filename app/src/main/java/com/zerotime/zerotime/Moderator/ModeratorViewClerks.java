package com.zerotime.zerotime.Moderator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Moderator.Adapters.ClerckAdapter;
import com.zerotime.zerotime.Moderator.Pojos.Clerks;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.ActivityLoginBinding;
import com.zerotime.zerotime.databinding.ActivityModeratorViewClerksBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.ArrayList;

import java.util.Objects;

public class ModeratorViewClerks extends AppCompatActivity {
    private ActivityModeratorViewClerksBinding binding;
    private DatabaseReference ClerksRef;
    ArrayList<Clerks> clerksList;
    private ClerckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeratorViewClerksBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.recycler.setLayoutManager(mLayoutManager);
        ClerksRef = FirebaseDatabase.getInstance().getReference().child("Clerks");
        clerksList = new ArrayList<>();
        ClerksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    String name = (String) dataSnapshot1.child("ClerkName").getValue();
                    String address = (String) dataSnapshot1.child("ClerkAddress").getValue();
                    String hasVehicle = (String) dataSnapshot1.child("hasVehicle").getValue();
                    String phone1 = (String) dataSnapshot1.child("ClerkPhone1").getValue();
                    String phone2 = (String) dataSnapshot1.child("ClerkPhone2").getValue();
                    String age = (String) (dataSnapshot1.child("ClerkAge").getValue());

                    Clerks clerks = new Clerks();
                    clerks.setName(name);
                    clerks.setAddress(address);
                    clerks.setPhone1(phone1);
                    clerks.setPhone2(phone2);
                    clerks.setAge(Integer.valueOf(age));

                    clerks.setAge(Integer.parseInt(Objects.requireNonNull(age)));
                    clerks.setHasVehicle(hasVehicle);

                    clerksList.add(clerks);


                }


                adapter = new ClerckAdapter(clerksList, ModeratorViewClerks.this);
                binding.recycler.setAdapter(adapter);

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