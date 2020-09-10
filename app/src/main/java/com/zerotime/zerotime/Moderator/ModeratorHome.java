package com.zerotime.zerotime.Moderator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zerotime.zerotime.databinding.ActivityModeratorHomeBinding;

public class ModeratorHome extends AppCompatActivity {
private ActivityModeratorHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeratorHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toast.makeText(this,"Moderator",Toast.LENGTH_SHORT).show();




        binding.ModeratorHomeAddClerckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ModeratorHome.this, ModeratorAddClerck.class);
                startActivity(i);
            }
        });

 binding.ModeratorHomeViewClercksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ModeratorHome.this, ModeratorViewClerks.class);
                startActivity(i);
            }
        });


    }
}