package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    private static int splashTimeOut=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences prefs = getSharedPreferences("UserState", MODE_PRIVATE);

        new Handler().postDelayed(() -> {
                prefs.getString("isLogged","");
            if (!Objects.requireNonNull(prefs.getString("isLogged", "null")).equals("null")){
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