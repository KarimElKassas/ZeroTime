package com.zerotime.zerotime.Moderator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zerotime.zerotime.Secretary.FollowingTheOrderState;
import com.zerotime.zerotime.databinding.ActivityModeratorHomeBinding;
import com.zerotime.zerotime.myBroadCast;

public class ModeratorHome extends AppCompatActivity {
    private ActivityModeratorHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeratorHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        checkInternetConnection();
        //New Orders Button
        binding.ModeratorHomeOrdersBtn.setOnClickListener(view1 -> {
            Intent i = new Intent(ModeratorHome.this, FollowingTheOrderState.class);
            startActivity(i);
        });
        //Add Clerk Button
        binding.ModeratorHomeAddClerckBtn.setOnClickListener(view1 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorAddClerk.class);
            startActivity(i);
        });
        //View Clerks Button
        binding.ModeratorHomeViewClercksBtn.setOnClickListener(view12 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorViewClerks.class);
            startActivity(i);
        });
        //View Clerks Button
        binding.ModeratorHomeAddOffersBtn.setOnClickListener(view12 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorAddOffer.class);
            startActivity(i);
        });

        //View Clerks Complaints Button
        binding.ModeratorHomeComplaintsBtn.setOnClickListener(view12 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorComplaints.class);
            startActivity(i);
        });

        //View Clerks number of order Button
        binding.ModeratorHomeNumberOFOrdersBtn.setOnClickListener(view12 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorNumberOfOrders.class);
            startActivity(i);
        });

    }
    private void checkInternetConnection(){
        myBroadCast broadCast=new myBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast,intentFilter);

    }
}