package com.zerotime.zerotime;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.zerotime.zerotime.Moderator.StartingScreen;
import com.zerotime.zerotime.databinding.ActivitySplashScreenBinding;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;
    private AlphaAnimation inAnimation = new AlphaAnimation(0f, 2f);
    private AlphaAnimation outAnimation = new AlphaAnimation(2f, 0f);
    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        prefs.getString("isLogged", "");


        //-------------------------------------------------------------------------------
        //Animation
        animation();
        //-------------------------------------------------------------------------------


        int splashTimeOut = 3000;
        new Handler().postDelayed(() -> {

            if (prefs.getBoolean("my_first_time", true)) {

                prefs.edit().putBoolean("my_first_time", false).apply();

                //the app is being launched for first time, do something
                Intent intent = new Intent(SplashScreen.this, StartingScreen.class);
                startActivity(intent);
                finish();
                // record the fact that the app has been started at least once
                return;
            }


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
        binding.splashImg.setTranslationY(-100f);
        binding.splashImg.setAlpha(0f);
        binding.splashImg.animate().translationY(100f).alpha(1f).setDuration(600).setStartDelay(500).start();
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

    private void checkInternetConnection() {
        myBroadCast broadCast = new myBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }
}