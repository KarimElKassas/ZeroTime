package com.zerotime.zerotime.Secretary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zerotime.zerotime.R;
import com.zerotime.zerotime.databinding.ActivitySecretaryHomeBinding;

public class SecretaryHome extends AppCompatActivity {
    private ActivitySecretaryHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecretaryHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.secretaryHomeOrdersBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(SecretaryHome.this,FollowingTheOrderState.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        binding.secretaryHomeChatsBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(SecretaryHome.this, SecretaryDisplayChats.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });



    }
}