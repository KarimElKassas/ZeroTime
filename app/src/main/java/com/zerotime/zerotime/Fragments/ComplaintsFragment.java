package com.zerotime.zerotime.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zerotime.zerotime.databinding.FragmentComplaintsBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class ComplaintsFragment extends Fragment {

    private FragmentComplaintsBinding binding;
    private DatabaseReference complaintsRef;
    private HashMap<String,String> complaintsMap = new HashMap<>();

    SharedPreferences preferences;

    String userPhone;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComplaintsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        preferences = Objects.requireNonNull(getContext()).getSharedPreferences("UserState", MODE_PRIVATE);
        complaintsRef = FirebaseDatabase.getInstance().getReference("Complaints");

        binding.complaintsFragmentSendComplaintBtn.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(binding.complaintsFragmentComplaintEditText.getText())){
                binding.complaintsFragmentComplaintEditText.setError("من فضلك قم بادخال شكوتك");
                binding.complaintsFragmentComplaintEditText.requestFocus();

            }else {
                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                String currentTime = df.format(Calendar.getInstance().getTime());

                userPhone = preferences.getString("isLogged","");
                complaintsMap.put("Complaint", Objects.requireNonNull(binding.complaintsFragmentComplaintEditText.getText()).toString());
                complaintsMap.put("UserPhone",userPhone);

                complaintsRef.child(currentTime)
                        .setValue(complaintsMap)
                        .addOnSuccessListener(aVoid ->{
                            Toast.makeText(getContext(),
                                    "تم ارسال الشكوى بنجاح سوف نرد عليك قريباً",
                                    Toast.LENGTH_SHORT).show();
                            binding.complaintsFragmentComplaintEditText.getText().clear();
                                }).addOnFailureListener(e -> Toast.makeText(getContext(),
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show());

            }
        });

        return view;
    }
}