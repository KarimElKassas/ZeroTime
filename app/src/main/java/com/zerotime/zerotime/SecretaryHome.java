package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class SecretaryHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_home);
        Toast.makeText(this,"Secretary",Toast.LENGTH_SHORT).show();
    }
}