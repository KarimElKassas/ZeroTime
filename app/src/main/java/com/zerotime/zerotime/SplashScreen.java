package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {
    private static int splashTimeOut=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences prefs = getSharedPreferences("UserState", MODE_PRIVATE);

        new Handler().postDelayed(() -> {

            if (prefs.getBoolean("isLogged",false)){
                Intent i=new Intent(SplashScreen.this,Home.class);
                startActivity(i);
                finish();
            }else {
                Intent i=new Intent(SplashScreen.this,Login.class);
                startActivity(i);
                finish();
            }

        },splashTimeOut);
    }
}