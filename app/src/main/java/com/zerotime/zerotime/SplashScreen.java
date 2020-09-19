package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zerotime.zerotime.databinding.ActivitySplashScreenBinding;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;
    private AlphaAnimation inAnimation = new AlphaAnimation(0f, 2f);
    private AlphaAnimation outAnimation = new AlphaAnimation(2f, 0f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //-------------------------------------------------------------------------------
        //Animation
        animation();
        //-------------------------------------------------------------------------------

        SharedPreferences prefs = getSharedPreferences("UserState", MODE_PRIVATE);

        int splashTimeOut = 3000;
        new Handler().postDelayed(() -> {
            prefs.getString("isLogged", "");
            if (!Objects.requireNonNull(prefs.getString("isLogged", "null")).equals("null")) {
                checkInternetConnection();
                goToHome();
            } else {
                checkInternetConnection();
                goToLogin();
            }

        }, splashTimeOut);
    }

    private void animation() {
        inAnimation.setDuration(200);
        outAnimation.setDuration(200);
        Animation myAnimation = AnimationUtils.loadAnimation(this, R.anim.stb2);
        binding.splashScreenWelcomeText.setAnimation(myAnimation);
    }

    private void goToLogin() {
        Intent i = new Intent(SplashScreen.this, Login.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void goToHome() {
        Intent i = new Intent(SplashScreen.this, Home.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
    private void checkInternetConnection(){
        myBroadCast broadCast=new myBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast,intentFilter);

    }
}