package com.zerotime.zerotime;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.zerotime.zerotime.Moderator.StartingScreen;
import com.zerotime.zerotime.databinding.ActivitySplashScreenBinding;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
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
        prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        prefs.getString("isLogged", "");


        //-------------------------------------------------------------------------------
        //Animation
        animation();
        //-------------------------------------------------------------------------------


        int splashTimeOut = 3000;
        new Handler().postDelayed(() -> {
            if (!haveNetworkConnection()) {
                Intent i = new Intent(SplashScreen.this, No_Internet_Connection.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("UniqueID","SplashScreen");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            } else {
                checkInternetConnection();

                if (prefs.getBoolean("my_first_time", true)) {

                    prefs.edit().putBoolean("my_first_time", false).apply();

                    //the app is being launched for first time, do something
                    Intent intent = new Intent(SplashScreen.this, StartingScreen.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    // record the fact that the app has been started at least once
                    return;
                }


                prefs.getString("isLogged", "");
                if (!Objects.requireNonNull(prefs.getString("isLogged", "null")).equals("null")) {
                    checkInternetConnection();
                    goToHome();
                } else {


                    goToLogin();

                }
            }


        }, splashTimeOut);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
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
        MyBroadCast broadCast = new MyBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);

    }


}