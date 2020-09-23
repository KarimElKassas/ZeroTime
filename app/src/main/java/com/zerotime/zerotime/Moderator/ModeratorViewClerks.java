package com.zerotime.zerotime.Moderator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Moderator.Adapters.ClerkAdapter;
import com.zerotime.zerotime.Moderator.Pojos.Clerks;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.ModeratorActivityViewClerksBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.ArrayList;

import java.util.Objects;

public class ModeratorViewClerks extends AppCompatActivity {
    private ModeratorActivityViewClerksBinding binding;
    private DatabaseReference ClerksRef;
    ArrayList<Clerks> clerksList;
    private ClerkAdapter adapter;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ModeratorViewClerks.this,ModeratorHome.class);
        startActivity(i);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModeratorActivityViewClerksBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        binding.recycler.setLayoutManager(mLayoutManager);
        binding.recycler.setItemAnimator(new DefaultItemAnimator());

        ClerksRef = FirebaseDatabase.getInstance().getReference().child("Clerks");
        clerksList = new ArrayList<>();
        ClerksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                            binding.moderatorViewClerkProgress.setVisibility(View.GONE);
                            binding.recycler.setVisibility(View.VISIBLE);

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
                            clerks.setAge(Integer.parseInt(Objects.requireNonNull(age)));

                            clerks.setAge(Integer.parseInt(Objects.requireNonNull(age)));
                            clerks.setHasVehicle(hasVehicle);

                            clerksList.add(clerks);


                        }
                        adapter = new ClerkAdapter(clerksList, ModeratorViewClerks.this);
                        binding.recycler.setAdapter(adapter);

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
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        recyclerView.setLayoutAnimation(animationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();


    }
    private void checkInternetConnection(){
        myBroadCast broadCast=new myBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast,intentFilter);

    }
}