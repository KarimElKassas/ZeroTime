package com.zerotime.zerotime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerotime.zerotime.databinding.ActivityMDisplayUserDataBinding;

import java.util.Objects;

public class M_DisplayUserData extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ActivityMDisplayUserDataBinding binding;
    String userPrimaryPhone;
    private DatabaseReference usersRef;
    private static final String[] regions = {"القاهرة", "الاسكندرية", "الجيزة"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMDisplayUserDataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userPrimaryPhone = getIntent().getStringExtra("UserPhone");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        //Regions Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,regions);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.moderatorDisplayUserDataUserRegion.setAdapter(adapter);
        binding.moderatorDisplayUserDataUserRegion.setEnabled(false);
        binding.moderatorDisplayUserDataUserAddress.setEnabled(false);
        binding.moderatorDisplayUserDataUserName.setEnabled(false);
        binding.moderatorDisplayUserDataUserPrimaryPhone.setEnabled(false);
        binding.moderatorDisplayUserDataUserSecondaryPhone.setEnabled(false);

        if (userPrimaryPhone != null){
            usersRef.child(userPrimaryPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        if (snapshot.hasChildren()){
                            String userName = snapshot.child("UserName").getValue(String.class);
                            String userSecondaryPhone = snapshot.child("UserSecondaryPhone").getValue(String.class);
                            String userAddress = snapshot.child("UserAddress").getValue(String.class);
                            Integer userRegionIndex = snapshot.child("UserRegionIndex").getValue(Integer.class);

                            binding.moderatorDisplayUserDataUserName.setText(userName);
                            binding.moderatorDisplayUserDataUserPrimaryPhone.setText(userPrimaryPhone);
                            binding.moderatorDisplayUserDataUserSecondaryPhone.setText(userSecondaryPhone);
                            binding.moderatorDisplayUserDataUserAddress.setText(userAddress);
                            binding.moderatorDisplayUserDataUserRegion.setSelection(Objects.requireNonNull(userRegionIndex));

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}