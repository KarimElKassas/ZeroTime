package com.zerotime.zerotime.Moderator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zerotime.zerotime.databinding.ActivityModeratorAddClerckBinding;

import java.util.HashMap;
import java.util.Objects;

public class ModeratorAddClerk extends AppCompatActivity {
    private ActivityModeratorAddClerckBinding binding;
    private DatabaseReference clerksRef;
    private HashMap<String, String> clerksMap;
    private String name, phone1, phone2, hasTayara, address;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeratorAddClerckBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        clerksRef = FirebaseDatabase.getInstance().getReference("Clerks");
        clerksMap = new HashMap<>();


        binding.ModeratorAddClerckAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClerk();
            }
        });

    }


    public void addClerk() {

        if(!binding.radioHave.isChecked() && !binding.radioDonthave.isChecked()){
            Toast.makeText(this, "من فضلك اخبرنا إن كنت تمتلك طياره ام لا ", Toast.LENGTH_SHORT).show();
            return;
        }else {
            if(binding.radioHave.isChecked())hasTayara="يمتلك طياره";
            if(binding.radioDonthave.isChecked())hasTayara="لا يمتلك طياره";
        }


        // getting data from user
        name = binding.ModeratorAddClerckNameEdt.getText().toString();
        phone1 = binding.ModeratorAddClerckPhone1Edt.getText().toString();
        phone2 = binding.ModeratorAddClerckPhone2Edt.getText().toString();
        age = Integer.valueOf(binding.ModeratorAddClerckAgeEdt.getText().toString());
        address = binding.ModeratorAddClerckAddressEdt.getText().toString();

// full the map
        clerksMap.put("ClerkName", name);
        clerksMap.put("ClerkPhone1", phone1);
        clerksMap.put("ClerkPhone2", phone2);
        clerksMap.put("ClerkAge", String.valueOf(age));
        clerksMap.put("ClerkAddress", address);
        clerksMap.put("hasVehicle", hasTayara);


        clerksRef.child(phone1)
                .setValue(clerksMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(ModeratorAddClerk.this.getApplicationContext(), "تمت الإضافه بنجاح ", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(ModeratorAddClerk.this.getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}