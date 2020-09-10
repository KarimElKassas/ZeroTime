package com.zerotime.zerotime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.zerotime.zerotime.R;

public class ModeratorHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_home);
        Toast.makeText(this,"Moderator",Toast.LENGTH_SHORT).show();
    }
}