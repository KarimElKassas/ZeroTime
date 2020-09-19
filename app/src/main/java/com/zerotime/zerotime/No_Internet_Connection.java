package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.zerotime.zerotime.databinding.ActivityHomeBinding;
import com.zerotime.zerotime.databinding.ActivityNoInternetConnectionBinding;

public class No_Internet_Connection extends AppCompatActivity {
    private ActivityNoInternetConnectionBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoInternetConnectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}