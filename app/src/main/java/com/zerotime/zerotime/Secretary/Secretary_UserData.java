package com.zerotime.zerotime.Secretary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.Moderator.Pojos.Clerks;
import com.zerotime.zerotime.databinding.SecretaryActivityUserDataBinding;
import com.zerotime.zerotime.myBroadCast;

import java.util.ArrayList;

public class Secretary_UserData extends AppCompatActivity {
    private SecretaryActivityUserDataBinding binding;
    String phone;
    private ArrayList<Clerks> UsersList;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SecretaryActivityUserDataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();
        Intent intent = getIntent();
        phone = intent.getStringExtra("UserPhone");

        UserRef = FirebaseDatabase.getInstance().getReference("Users");
        UsersList = new ArrayList<>();

        UserRef.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userAddress = dataSnapshot.child("UserAddress").getValue(String.class);
                String userPrimaryPhone = dataSnapshot.child("UserPrimaryPhone").getValue(String.class);
                String userSecondaryPhone = dataSnapshot.child("UserSecondaryPhone").getValue(String.class);
                String userName = dataSnapshot.child("UserName").getValue(String.class);
                String userRegion = dataSnapshot.child("UserRegion").getValue(String.class);

                binding.UserName.setText(userName);
                binding.UserAddress.setText(userAddress);
                binding.UserPhone1.setText(userPrimaryPhone);
                binding.UserPhone2.setText(userSecondaryPhone);
                binding.UserRegion.setText(userRegion);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Secretary_UserData.this, FollowingTheOrderState.class);
        startActivity(intent);
        finish();
    }

    private void checkInternetConnection(){
        myBroadCast broadCast=new myBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast,intentFilter);

    }

}