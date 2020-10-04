package com.zerotime.zerotime.Moderator;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zerotime.zerotime.Login;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.FollowingTheOrderState;
import com.zerotime.zerotime.databinding.ModeratorActivityHomeBinding;
import com.zerotime.zerotime.myBroadCast;

public class ModeratorHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding view
        ModeratorActivityHomeBinding binding = ModeratorActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Check Internet Connection State
        checkInternetConnection();

        //New Orders Button  ->  go to view pending Orders
        binding.ModeratorHomeOrdersBtn.setOnClickListener(view1 -> {
            Intent i = new Intent(ModeratorHome.this, FollowingTheOrderState.class);
            i.putExtra("from", "M");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        //Add Clerk Button  ->  to add a new Clerk
        binding.ModeratorHomeAddClerkBtn.setOnClickListener(view1 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorAddClerk.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        //View Clerks Button  ->  to view all Clerks in the Company
        binding.ModeratorHomeViewClerksBtn.setOnClickListener(view12 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorViewClerks.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
        //Add Offer Button  ->  to add a new Monthly Offer
        binding.ModeratorHomeAddOffersBtn.setOnClickListener(view12 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorAddOffer.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        // Complaints Button  -> to view users Complaints
        binding.ModeratorHomeComplaintsBtn.setOnClickListener(view12 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorComplaints.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        // number of order Button  ->  to view the order's number of each user
        binding.ModeratorHomeNumberOFOrdersBtn.setOnClickListener(view12 -> {
            Intent i = new Intent(ModeratorHome.this, ModeratorNumberOfOrders.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //go back to login activity
        Intent i = new Intent(ModeratorHome.this, Login.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void checkInternetConnection() {
        myBroadCast broadCast = new myBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }
}