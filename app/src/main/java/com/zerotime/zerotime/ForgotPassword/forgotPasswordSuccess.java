package com.zerotime.zerotime.ForgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zerotime.zerotime.Login;
import com.zerotime.zerotime.databinding.ActivityForgotPasswordSuccessBinding;

import androidx.appcompat.app.AppCompatActivity;

public class forgotPasswordSuccess extends AppCompatActivity {
    private ActivityForgotPasswordSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordSuccessBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }
}